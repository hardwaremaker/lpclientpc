/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.auftrag;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelKonditionen;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragbegruendungDto;
import com.lp.server.system.service.MandantFac;

/**
 * <p>
 * In diesem Fenster werden Konditionen zum Auftrag erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 22. 04. 2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */
public class PanelAuftragKonditionen2 extends PanelKonditionen {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Cache for convenience. */
	private InternalFrameAuftrag intFrame = null;
	/** Cache for convenience. */
	private TabbedPaneAuftrag tpAuftrag = null;

	private WrapperLabel wlaGarantie = null;
	private WrapperNumberField wnfGarantie = null;

	private WrapperLabel wlaVersteckterAufschlag = null;
	private WrapperNumberField wnfVersteckterAufschlag = null;
	private WrapperLabel wlaProzent3 = null;

	private WrapperLabel wlaProjektierungsrabatt = null;
	private WrapperNumberField wnfProjektierungsrabatt = null;
	private WrapperLabel wlaProzent4 = null;

	private WrapperLabel wlaAuftragwertInMandantenwaehrung = null;
	private WrapperLabel wlaMandantenwaehrung0 = null;
	private WrapperLabel wlaMaterialwertInMandantenwaehrung = null;
	private WrapperLabel wlaMandantenwaehrung1 = null;
	private WrapperLabel wlaRohdeckungInMandantenwaehrung = null;
	private WrapperLabel wlaMandantenwaehrung2 = null;

	private WrapperCheckBox wcbMitZusammenfassung = new WrapperCheckBox();

	private WrapperButton wbuBegruendung = new WrapperButton();
	private WrapperTextField wtfBegruendung = new WrapperTextField();

	private WrapperLabel wlaIndexanpassung = null;
	private WrapperNumberField wnfIndexanpassung = null;
	private WrapperLabel wlaIndexanpassungProzent = null;

	private PanelQueryFLR panelQueryFLRBegruendung = null;
	public final static String ACTION_SPECIAL_BEGRUENDUNG = "action_special_begruendung";

	private WrapperNumberField wnfAuftragwertInMandantenwaehrung = null;
	private WrapperNumberField wnfMaterialwertInMandantenwaehrung = null;
	private WrapperNumberField wnfRohdeckungInMandantenwaehrung = null;
	private WrapperNumberField wnfRohdeckungErrechnetInMandantenwaehrung = null;

	private WrapperLabel wlaAuftragwertInAuftragwaehrung = null;
	private WrapperLabel wlaAuftragwaehrung = null;
	private WrapperLabel wlaAuftragwaehrung2 = null;
	private WrapperLabel wlaRohdeckungAltInMandantenwaehrung = null;
	private WrapperLabel wlaRohdeckungManuellInMandantenwaehrung = null;

	private WrapperLabel wlaRohdeckungErrechnetInMandantenwaehrung = null;

	private WrapperLabel wlaMandantenwaehrung3 = null;

	private WrapperNumberField wnfAuftragwertInAuftragwaehrung = null;
	private WrapperNumberField wnfRohdeckungAltInMandantenwaehrung = null;

	private WrapperLabel wlaPraemie = null;
	private WrapperNumberField wnfPraemieInMandantenwaehrung = null;
	private WrapperLabel wlaMandantenwaehrungPraemie = null;

	private WrapperLabel wlaKorrekturbetrag = null;
	private WrapperNumberField wnfKorrekturbetrag = null;

	protected WrapperCheckBox wcbMindermengenzuschlag = new WrapperCheckBox();
	
	public PanelAuftragKonditionen2(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);
		setVisibleVerrechenbar(true);
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ABRECHNUNGSVORSCHLAG)) {
			setVisibleVerrechnungsmodell(true);
		}
		intFrame = (InternalFrameAuftrag) internalFrame;
		tpAuftrag = intFrame.getTabbedPaneAuftrag();

		jbInit();
		initComponents();
		setDarfPreiseSehenSichtbarkeit();
	}

	private void jbInit() throws Throwable {

		wtfLieferart.setMandatoryField(true);
		wtfZahlungsziel.setMandatoryField(true);
		wtfSpedition.setMandatoryField(true);

		remove(wlaAllgemeinerRabatt);
		remove(wnfAllgemeinerRabatt);
		remove(wlaProzent2);

		wlaGarantie = new WrapperLabel(LPMain.getTextRespectUISPr("kond.label.garantie"));
		wnfGarantie = new WrapperNumberField();

		wlaIndexanpassung = new WrapperLabel(LPMain.getTextRespectUISPr("kond.label.indexanpassung"));
		wnfIndexanpassung = new WrapperNumberField();

		wbuBegruendung.setText(LPMain.getTextRespectUISPr("ls.begruendung") + "...");
		wbuBegruendung.setActionCommand(ACTION_SPECIAL_BEGRUENDUNG);
		wbuBegruendung.addActionListener(this);
		wtfBegruendung.setColumnsMax(80);
		wtfBegruendung.setActivatable(false);

		wcbMindermengenzuschlag.setText(LPMain.getTextRespectUISPr("label.mindermengenzuschlag"));
		
		wcbMitZusammenfassung.setText(LPMain.getTextRespectUISPr("angebot.mitzusammmenfassung"));

		wlaVersteckterAufschlag = new WrapperLabel(
				LPMain.getTextRespectUISPr("label.versteckteraufschlag"));
		wnfVersteckterAufschlag = new WrapperNumberField();
		wnfVersteckterAufschlag.setDependenceField(true);
		wlaProzent3 = new WrapperLabel(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent3.setHorizontalAlignment(SwingConstants.LEFT);

		wlaIndexanpassungProzent = new WrapperLabel(LPMain.getTextRespectUISPr("label.prozent"));
		wlaIndexanpassungProzent.setHorizontalAlignment(SwingConstants.LEFT);

		wlaProjektierungsrabatt = new WrapperLabel(
				LPMain.getTextRespectUISPr("kond.label.projektRabatt"));
		wnfProjektierungsrabatt = new WrapperNumberField();
		wnfProjektierungsrabatt.setDependenceField(true);
		wlaProzent4 = new WrapperLabel(LPMain.getTextRespectUISPr("label.prozent"));
		wlaProzent4.setHorizontalAlignment(SwingConstants.LEFT);
		wnfAllgemeinerRabatt.setDependenceField(true);

		wlaMandantenwaehrung0 = new WrapperLabel();
		wlaMandantenwaehrung0.setHorizontalAlignment(SwingConstants.LEFT);

		wlaMaterialwertInMandantenwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.materialwert"));

		wlaMandantenwaehrung1 = new WrapperLabel();
		wlaMandantenwaehrung1.setHorizontalAlignment(SwingConstants.LEFT);

		wlaRohdeckungInMandantenwaehrung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.rohdeckung"));
		wlaRohdeckungInMandantenwaehrung.setMaximumSize(new Dimension(90, Defaults.getInstance().getControlHeight()));
		wlaRohdeckungInMandantenwaehrung.setMinimumSize(new Dimension(90, Defaults.getInstance().getControlHeight()));
		wlaRohdeckungInMandantenwaehrung.setPreferredSize(new Dimension(90, Defaults.getInstance().getControlHeight()));

		wlaMandantenwaehrung2 = new WrapperLabel();
		wlaMandantenwaehrung2.setMaximumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung2.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung2.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung2.setHorizontalAlignment(SwingConstants.LEFT);

		wnfAuftragwertInAuftragwaehrung = new WrapperNumberField();

		wnfAuftragwertInAuftragwaehrung.setDependenceField(true);
		wnfAuftragwertInAuftragwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfAuftragwertInAuftragwaehrung.addFocusListener(this);

		wnfMaterialwertInMandantenwaehrung = new WrapperNumberField();
		wnfMaterialwertInMandantenwaehrung.setActivatable(false);
		wnfMaterialwertInMandantenwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfRohdeckungInMandantenwaehrung = new WrapperNumberField();
		wnfRohdeckungInMandantenwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());

		wnfRohdeckungErrechnetInMandantenwaehrung = new WrapperNumberField();
		wnfRohdeckungErrechnetInMandantenwaehrung
				.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfRohdeckungErrechnetInMandantenwaehrung.setActivatable(false);
		wlaAuftragwertInAuftragwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("kond.label.manueller.auftragswert"));
		wlaAuftragwaehrung = new WrapperLabel(tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		wlaAuftragwaehrung2 = new WrapperLabel(tpAuftrag.getAuftragDto().getCAuftragswaehrung());

		wlaAuftragwaehrung.setMaximumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung.setHorizontalAlignment(SwingConstants.LEFT);

		wlaAuftragwaehrung2.setMaximumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung2.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung2.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaAuftragwaehrung2.setHorizontalAlignment(SwingConstants.LEFT);

		wlaAuftragwertInMandantenwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("kond.label.auftragswert"));
		wnfAuftragwertInMandantenwaehrung = new WrapperNumberField();
		wnfAuftragwertInMandantenwaehrung.setActivatable(false);
		wnfAuftragwertInMandantenwaehrung.setDependenceField(true);
		wnfAuftragwertInMandantenwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wlaRohdeckungAltInMandantenwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.rohdeckungalt"));
		wlaRohdeckungManuellInMandantenwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.rohdeckungmanuell"));

		wlaRohdeckungErrechnetInMandantenwaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("auft.rohdeckungerrechnet"));

		wlaMandantenwaehrung3 = new WrapperLabel();
		wlaMandantenwaehrung3.setMaximumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung3.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung3.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrung3.setHorizontalAlignment(SwingConstants.LEFT);

		wlaPraemie = new WrapperLabel(LPMain.getTextRespectUISPr("auft.konditionen.praemie"));
		wnfPraemieInMandantenwaehrung = new WrapperNumberField();
		wnfPraemieInMandantenwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wlaMandantenwaehrungPraemie = new WrapperLabel(LPMain.getTheClient().getSMandantenwaehrung());
		wlaMandantenwaehrungPraemie.setMaximumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrungPraemie.setMinimumSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrungPraemie.setPreferredSize(new Dimension(25, Defaults.getInstance().getControlHeight()));
		wlaMandantenwaehrungPraemie.setHorizontalAlignment(SwingConstants.LEFT);

		wnfRohdeckungAltInMandantenwaehrung = new WrapperNumberField();
		wnfRohdeckungAltInMandantenwaehrung.setActivatable(false);
		wnfRohdeckungAltInMandantenwaehrung.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());

		wnfKorrekturbetrag = new WrapperNumberField();

		wnfKorrekturbetrag.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseVK());
		wnfKorrekturbetrag.setActivatable(false);

		wlaKorrekturbetrag = new WrapperLabel(LPMain.getTextRespectUISPr("lp.korrekturbetrag"));

		iZeile = 3; // mein eigener Teil beginnt nach den ersten drei Zeilen des
					// BasisPanel
		/*
		 * jPanelWorkingOn.add(wbuBegruendung, new GridBagConstraints(0, iZeile, 1, 1,
		 * 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,
		 * 2, 2, 2), 0, 0));
		 */// nicht notwendig, da ueber Menue erreichbar
		jPanelWorkingOn.add(wtfBegruendung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(5, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wcbMitZusammenfassung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wcbMindermengenzuschlag, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		// Zeile
		jPanelWorkingOn.add(wlaVersteckterAufschlag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfVersteckterAufschlag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent3, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaGarantie, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		jPanelWorkingOn.add(wnfGarantie, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaAllgemeinerRabatt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAllgemeinerRabatt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent2, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaIndexanpassung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfIndexanpassung, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wlaIndexanpassungProzent, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaProjektierungsrabatt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfProjektierungsrabatt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaProzent4, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaPraemie, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfPraemieInMandantenwaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrungPraemie, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaAuftragwertInMandantenwaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAuftragwertInMandantenwaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung0, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaAuftragwertInAuftragwaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfAuftragwertInAuftragwaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wlaAuftragwaehrung, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaMaterialwertInMandantenwaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfMaterialwertInMandantenwaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung1, new GridBagConstraints(2, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wlaKorrekturbetrag, new GridBagConstraints(3, iZeile, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfKorrekturbetrag, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jPanelWorkingOn.add(wlaAuftragwaehrung2, new GridBagConstraints(5, iZeile, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaRohdeckungErrechnetInMandantenwaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 2, 0, 2), 0, 0));

		jPanelWorkingOn.add(wlaRohdeckungManuellInMandantenwaehrung, new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 2, 0, 2), 0, 0));

		jPanelWorkingOn.add(wlaRohdeckungAltInMandantenwaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(8, 2, 0, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaRohdeckungInMandantenwaehrung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfRohdeckungErrechnetInMandantenwaehrung, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfRohdeckungInMandantenwaehrung, new GridBagConstraints(2, iZeile, 2, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfRohdeckungAltInMandantenwaehrung, new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wlaMandantenwaehrung3, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 2, 2, 2), 0, 0));
	}

	/**
	 * Default Werte im Panel setzen.
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		leereAlleFelder(this);

		if (tpAuftrag.getKundeAuftragDto() != null) {
			if (tpAuftrag.getKundeAuftragDto().getFRabattsatz() != null) {
				wnfAllgemeinerRabatt
						.setDouble(new Double(tpAuftrag.getKundeAuftragDto().getFRabattsatz().doubleValue()));
			}
			if (tpAuftrag.getKundeAuftragDto().getIGarantieinmonaten() != null) {
				wnfGarantie.setInteger(tpAuftrag.getKundeAuftragDto().getIGarantieinmonaten());
			}
		}

		String cNrMandantenwaehrung = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();

		wlaMandantenwaehrung0.setText(cNrMandantenwaehrung);
		wlaMandantenwaehrung1.setText(cNrMandantenwaehrung);
		wlaMandantenwaehrung2.setText(cNrMandantenwaehrung);
		wlaMandantenwaehrung3.setText(cNrMandantenwaehrung);

		wlaAuftragwaehrung.setText(tpAuftrag.getAuftragDto().getCAuftragswaehrung());
		wlaAuftragwaehrung2.setText(tpAuftrag.getAuftragDto().getCAuftragswaehrung());

		// belegartkonditionen: 8 die Default Texte zum Ruecksetzen hinterlegen
		wefKopftext.setDefaultText(tpAuftrag.getKopftextDto().getCTextinhalt());
		wefFusstext.setDefaultText(tpAuftrag.getFusstextDto().getCTextinhalt());
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_AUFTRAG;
	}

	public void focusGained(FocusEvent e) {

	}

	public void focusLost(FocusEvent e) {

		try {
			BigDecimal bdAuftragswertinanfragewaehrungVorhanden = DelegateFactory.getInstance().getAuftragDelegate()
					.berechneGesamtwertAuftrag(tpAuftrag.getAuftragDto().getIId());

			if (wnfAuftragwertInAuftragwaehrung.getBigDecimal() != null
					&& bdAuftragswertinanfragewaehrungVorhanden != null && !bdAuftragswertinanfragewaehrungVorhanden
							.equals(wnfAuftragwertInAuftragwaehrung.getBigDecimal())) {
				// Versteckten Aufschlag neu berechnen, wenn im editieren-Modus

				BigDecimal bdFaktor = new BigDecimal(1 + (tpAuftrag.getAuftragDto().getFVersteckterAufschlag() / 100));
				if (bdFaktor.doubleValue() == 0) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.berechnung.versteckter.aufschlag"));
				} else {

					BigDecimal bdWertOhneVerstAufschlag = bdAuftragswertinanfragewaehrungVorhanden.divide(bdFaktor,
							BigDecimal.ROUND_HALF_EVEN, Defaults.getInstance().getIUINachkommastellenPreiseVK());
					if (bdWertOhneVerstAufschlag.doubleValue() != 0) {
						BigDecimal bd = bdWertOhneVerstAufschlag
								.subtract(wnfAuftragwertInAuftragwaehrung.getBigDecimal());

						bd = bd.divide(bdWertOhneVerstAufschlag, BigDecimal.ROUND_HALF_EVEN,
								Defaults.getInstance().getIUINachkommastellenPreiseVK());
						bd = bd.multiply(new BigDecimal(100));

						wnfVersteckterAufschlag.setBigDecimal(bd.negate());

						// Eigentlich muss nun das Angebot gespeichert werden ->
						tpAuftrag.getAuftragDto().setNKorrekturbetrag(null);
						tpAuftrag.getAuftragDto().setFVersteckterAufschlag(wnfVersteckterAufschlag.getDouble());
						DelegateFactory.getInstance().getAuftragDelegate().updateAuftrag(tpAuftrag.getAuftragDto(),
								null);
						DelegateFactory.getInstance().getAuftragDelegate()
								.updateAuftragKonditionen(tpAuftrag.getAuftragDto().getIId());

						BigDecimal bdWertBerechnetNeu = DelegateFactory.getInstance().getAuftragDelegate()
								.berechneGesamtwertAuftrag(tpAuftrag.getAuftragDto().getIId());

						// Korrekturbetrag
						BigDecimal bdKorrektur = wnfAuftragwertInAuftragwaehrung.getBigDecimal()
								.subtract(bdWertBerechnetNeu);
						wnfKorrekturbetrag.setBigDecimal(bdKorrektur);

						tpAuftrag.getAuftragDto().setNKorrekturbetrag(bdKorrektur);
						DelegateFactory.getInstance().getAuftragDelegate().updateAuftrag(tpAuftrag.getAuftragDto(),
								null);

					}
				}

			}
		} catch (Throwable e1) {
			getInternalFrame().handleException(e1, true);
		}

	}

	/**
	 * Einen existierenden Lieferschein zur Anzeige bringen.
	 * 
	 * @throws Throwable
	 */
	public void dto2Components() throws Throwable {
		wnfPraemieInMandantenwaehrung.setBigDecimal(tpAuftrag.getAuftragDto().getNPraemie());
		wnfVersteckterAufschlag.setDouble(tpAuftrag.getAuftragDto().getFVersteckterAufschlag());
		wnfAllgemeinerRabatt.setDouble(tpAuftrag.getAuftragDto().getFAllgemeinerRabattsatz());
		wnfProjektierungsrabatt.setDouble(tpAuftrag.getAuftragDto().getFProjektierungsrabattsatz());
		wtfLieferartort.setText(tpAuftrag.getAuftragDto().getCLieferartort());
		wcbMitZusammenfassung.setShort(tpAuftrag.getAuftragDto().getBMitzusammenfassung());
		wcbMindermengenzuschlag.setShort(tpAuftrag.getAuftragDto().getBMindermengenzuschlag());
		wcoVerrechenbar.setKeyOfSelectedItem(tpAuftrag.getAuftragDto().getVerrechenbarIId());

		wnfIndexanpassung.setBigDecimal(tpAuftrag.getAuftragDto().getNIndexanpassung());

		holeLieferart(tpAuftrag.getAuftragDto().getLieferartIId());
		holeZahlungsziel(tpAuftrag.getAuftragDto().getZahlungszielIId());
		holeSpediteur(tpAuftrag.getAuftragDto().getSpediteurIId());

		setKundeIId_FuerKundepediteur(tpAuftrag.getAuftragDto().getKundeIIdLieferadresse());
		
		wnfGarantie.setInteger(tpAuftrag.getAuftragDto().getIGarantie());

		// die Werte des Auftrags anzeigen
		BigDecimal bdAuftragwertInAuftragwaehrung = tpAuftrag.getAuftragDto()
				.getNGesamtauftragswertInAuftragswaehrung();
		BigDecimal bdAuftragwertInMandantenwaehrung = null;
		BigDecimal bdMaterialwertInMandantenwaehrung = tpAuftrag.getAuftragDto().getNMaterialwertInMandantenwaehrung();
		BigDecimal bdRohdeckungInMandantenwaehrung = tpAuftrag.getAuftragDto().getNRohdeckungInMandantenwaehrung();
		BigDecimal bdRohdeckungaltInMandantenwaehrung = tpAuftrag.getAuftragDto()
				.getNRohdeckungaltInMandantenwaehrung();

		if (bdAuftragwertInAuftragwaehrung == null
				|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)) {
			bdAuftragwertInAuftragwaehrung = DelegateFactory.getInstance().getAuftragDelegate()
					.berechneGesamtwertAuftrag(tpAuftrag.getAuftragDto().getIId());

			if (tpAuftrag.getAuftragDto().getNKorrekturbetrag() != null) {
				bdAuftragwertInAuftragwaehrung = bdAuftragwertInAuftragwaehrung
						.add(tpAuftrag.getAuftragDto().getNKorrekturbetrag());
			}

		}
		BigDecimal bdWechselkurs = new BigDecimal(
				tpAuftrag.getAuftragDto().getFWechselkursmandantwaehrungzubelegwaehrung().doubleValue());

		wnfKorrekturbetrag.setBigDecimal(tpAuftrag.getAuftragDto().getNKorrekturbetrag());

		bdAuftragwertInMandantenwaehrung = DelegateFactory.getInstance().getLocaleDelegate()
				.rechneUmInMandantenWaehrung(bdAuftragwertInAuftragwaehrung, bdWechselkurs);

		if (bdMaterialwertInMandantenwaehrung == null) {
			bdMaterialwertInMandantenwaehrung = DelegateFactory.getInstance().getAuftragDelegate()
					.berechneMaterialwertAuftrag(tpAuftrag.getAuftragDto().getIId());
		}

		if (bdAuftragwertInMandantenwaehrung != null && bdMaterialwertInMandantenwaehrung != null) {
			// vorschlagswert, wenn rohdeckung null
			if (bdRohdeckungInMandantenwaehrung == null) {
				bdRohdeckungInMandantenwaehrung = bdAuftragwertInMandantenwaehrung
						.subtract(bdMaterialwertInMandantenwaehrung);
			}

			wnfRohdeckungErrechnetInMandantenwaehrung
					.setBigDecimal(bdAuftragwertInMandantenwaehrung.subtract(bdMaterialwertInMandantenwaehrung));

		}

		wsfVerrechnungsmodell.setKey(tpAuftrag.getAuftragDto().getVerrechnungsmodellIId());

		wnfAuftragwertInMandantenwaehrung.setBigDecimal(bdAuftragwertInMandantenwaehrung);
		wnfMaterialwertInMandantenwaehrung.setBigDecimal(bdMaterialwertInMandantenwaehrung);
		wnfRohdeckungInMandantenwaehrung.setBigDecimal(bdRohdeckungInMandantenwaehrung);
		wnfAuftragwertInAuftragwaehrung.setBigDecimal(bdAuftragwertInAuftragwaehrung);
		wnfRohdeckungAltInMandantenwaehrung.setBigDecimal(bdRohdeckungaltInMandantenwaehrung);

		// Kopftext fuer diesen Auftrag in der Sprache des Kunden anzeigen
		if (tpAuftrag.getAuftragDto().getCKopftextUebersteuert() != null) {
			wefKopftext.setText(tpAuftrag.getAuftragDto().getCKopftextUebersteuert());
		} else {
			wefKopftext.setText(DelegateFactory.getInstance().getAuftragServiceDelegate()
					.getAufragkopfDefault(tpAuftrag.getKundeAuftragDto().getPartnerDto().getLocaleCNrKommunikation())
					.getCTextinhalt());
		}

		// Fusstext fuer diesen Auftrag in der Sprache des Kunden anzeigen
		if (tpAuftrag.getAuftragDto().getCFusstextUebersteuert() != null) {
			wefFusstext.setText(tpAuftrag.getAuftragDto().getCFusstextUebersteuert());
		} else {
			wefFusstext.setText(DelegateFactory.getInstance().getAuftragServiceDelegate()
					.getAufragfussDefault(tpAuftrag.getKundeAuftragDto().getPartnerDto().getLocaleCNrKommunikation())
					.getCTextinhalt());
		}
		if (tpAuftrag.getAuftragDto().getAuftragbegruendungIId() != null) {
			AuftragbegruendungDto begruendungDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
					.auftragbegruendungFindByPrimaryKey(tpAuftrag.getAuftragDto().getAuftragbegruendungIId());
			wtfBegruendung.setText(begruendungDto.getBezeichnung());
		} else {
			wtfBegruendung.setText(null);
		}
		aktualisiereStatusbar();
	}

	private void components2Dto() throws Throwable {
		tpAuftrag.getAuftragDto().setNPraemie(wnfPraemieInMandantenwaehrung.getBigDecimal());
		tpAuftrag.getAuftragDto().setFVersteckterAufschlag(wnfVersteckterAufschlag.getDouble());
		tpAuftrag.getAuftragDto().setFAllgemeinerRabattsatz(wnfAllgemeinerRabatt.getDouble());
		tpAuftrag.getAuftragDto().setFProjektierungsrabattsatz(wnfProjektierungsrabatt.getDouble());
		tpAuftrag.getAuftragDto().setLieferartIId(lieferartDto.getIId());
		tpAuftrag.getAuftragDto().setZahlungszielIId(zahlungszielDto.getIId());
		tpAuftrag.getAuftragDto().setSpediteurIId(spediteurDto.getIId());
		tpAuftrag.getAuftragDto().setIGarantie(wnfGarantie.getInteger());
		// belegartkonditionen: 9 Kopf- und Fusstext mit den Konditionen
		// abspeichern
		tpAuftrag.getAuftragDto().setAuftragtextIIdKopftext(tpAuftrag.getKopftextDto().getIId());
		tpAuftrag.getAuftragDto().setNRohdeckungInMandantenwaehrung(wnfRohdeckungInMandantenwaehrung.getBigDecimal());

		tpAuftrag.getAuftragDto().setNIndexanpassung(wnfIndexanpassung.getBigDecimal());

		tpAuftrag.getAuftragDto().setCLieferartort(wtfLieferartort.getText());

		tpAuftrag.getAuftragDto().setBMitzusammenfassung(wcbMitZusammenfassung.getShort());
		
		tpAuftrag.getAuftragDto().setBMindermengenzuschlag(wcbMindermengenzuschlag.getShort());

		// wenn der Kopftext nicht ueberschrieben wurde -> null setzen
		if (wefKopftext.getText() != null
				&& wefKopftext.getText().equals(tpAuftrag.getKopftextDto().getCTextinhalt())) {
			tpAuftrag.getAuftragDto().setCKopftextUebersteuert(null);
		} else {
			tpAuftrag.getAuftragDto().setCKopftextUebersteuert(wefKopftext.getText());
		}

		tpAuftrag.getAuftragDto().setAuftragtextIIdFusstext(tpAuftrag.getFusstextDto().getIId());

		// wenn der Fusstext nicht ueberschrieben wurde -> null setzen
		if (wefFusstext.getText() != null
				&& wefFusstext.getText().equals(tpAuftrag.getFusstextDto().getCTextinhalt())) {
			tpAuftrag.getAuftragDto().setCFusstextUebersteuert(null);
		} else {
			tpAuftrag.getAuftragDto().setCFusstextUebersteuert(wefFusstext.getText());
		}

		tpAuftrag.getAuftragDto().setVerrechenbarIId((Integer) wcoVerrechenbar.getKeyOfSelectedItem());

		tpAuftrag.getAuftragDto().setVerrechnungsmodellIId(wsfVerrechnungsmodell.getIKey());

	}

	public void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_BEGRUENDUNG)) {
			panelQueryFLRBegruendung = AuftragFilterFactory.getInstance().createPanelFLRBegruendung(getInternalFrame(),
					tpAuftrag.getAuftragDto().getAuftragbegruendungIId(), true);
			new DialogQuery(panelQueryFLRBegruendung);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			components2Dto();

			DelegateFactory.getInstance().getAuftragDelegate().updateAuftrag(tpAuftrag.getAuftragDto(), null);

			DelegateFactory.getInstance().getAuftragDelegate()
					.updateAuftragKonditionen(tpAuftrag.getAuftragDto().getIId());

			super.eventActionSave(e, false); // buttons schalten

			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false); // Stati aller Components
											// aktualisieren

		// neu einlesen, ausloeser war ev. ein refresh oder discard
		Object oKey = tpAuftrag.getAuftragDto().getIId();

		// zuerst alles zuruecksetzen, ausloeser war ev. ein discard
		setDefaults();

		tpAuftrag.setAuftragDto(
				DelegateFactory.getInstance().getAuftragDelegate().auftragFindByPrimaryKey((Integer) oKey));

		wnfIndexanpassung.setBigDecimal(null);
		if (tpAuftrag.getAuftragDto().getAuftragartCNr().equals(AuftragServiceFac.AUFTRAGART_WIEDERHOLEND)) {
			wlaIndexanpassung.setVisible(true);
			wnfIndexanpassung.setVisible(true);
			wlaIndexanpassungProzent.setVisible(true);
		} else {
			wlaIndexanpassung.setVisible(false);
			wnfIndexanpassung.setVisible(false);
			wlaIndexanpassungProzent.setVisible(false);
		}

		dto2Components();

		tpAuftrag.getAuftragKonditionen().updateButtons(getLockedstateDetailMainKey());
		tpAuftrag.setTitleAuftrag(LPMain.getTextRespectUISPr("auft.title.panel.konditionen"));
	}

	private void setDarfPreiseSehenSichtbarkeit() {

		// Darf Preise Sehen Recht
		if (!getInternalFrame().bRechtDarfPreiseSehenVerkauf) {
			this.setVisibleAllgemeinerRabatt(false);
			// this.setVisibleZahlungsziel(false);

			wcoMehrwertsteuer.setVisible(false);
			wlaMehrwertsteuer.setVisible(false);
			wlaVersteckterAufschlag.setVisible(false);
			wnfVersteckterAufschlag.setVisible(false);
			wlaProzent3.setVisible(false);

			wlaAuftragwertInMandantenwaehrung.setVisible(false);
			wlaMandantenwaehrung0.setVisible(false);
			wlaMandantenwaehrung1.setVisible(false);
			wlaMandantenwaehrung2.setVisible(false);
			wlaMandantenwaehrung3.setVisible(false);
			wlaMandantenwaehrungPraemie.setVisible(false);

			wlaAuftragwertInAuftragwaehrung.setVisible(false);
			wlaMandantenwaehrung1.setVisible(false);

			wnfRohdeckungAltInMandantenwaehrung.setVisible(false);
			wnfRohdeckungInMandantenwaehrung.setVisible(false);
			wnfRohdeckungErrechnetInMandantenwaehrung.setVisible(false);
			wnfKorrekturbetrag.setVisible(false);
			wnfMaterialwertInMandantenwaehrung.setVisible(false);

			wlaPraemie.setVisible(false);
			wnfPraemieInMandantenwaehrung.setVisible(false);

			wlaProjektierungsrabatt.setVisible(false);
			wnfProjektierungsrabatt.setVisible(false);

			wlaProzent2.setVisible(false);
			wlaProzent4.setVisible(false);

			wlaMaterialwertInMandantenwaehrung.setVisible(false);

			wlaRohdeckungManuellInMandantenwaehrung.setVisible(false);
			wlaRohdeckungErrechnetInMandantenwaehrung.setVisible(false);
			wlaRohdeckungAltInMandantenwaehrung.setVisible(false);
			wlaRohdeckungInMandantenwaehrung.setVisible(false);
			wlaKorrekturbetrag.setVisible(false);

			wlaAuftragwaehrung.setVisible(false);
			wlaAuftragwaehrung2.setVisible(false);

			wnfAuftragwertInMandantenwaehrung.setVisible(false);

			wnfAuftragwertInAuftragwaehrung.setVisible(false);

		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (tpAuftrag.istAktualisierenAuftragErlaubt()) {

			super.eventActionUpdate(aE, false); // Buttons schalten
		}
	}

	private void aktualisiereStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(tpAuftrag.getAuftragDto().getPersonalIIdAnlegen());
		setStatusbarTAnlegen(tpAuftrag.getAuftragDto().getTAnlegen());
		setStatusbarPersonalIIdAendern(tpAuftrag.getAuftragDto().getPersonalIIdAendern());
		setStatusbarTAendern(tpAuftrag.getAuftragDto().getTAendern());
		setStatusbarStatusCNr(tpAuftrag.getAuftragStatus());
	}

	public boolean handleOwnException(ExceptionLP exfc) {
		boolean bErrorErkannt = true;
		int code = exfc.getICode();

		switch (code) {
		default:
			bErrorErkannt = false;
			break;
		}

		return bErrorErkannt;
	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBegruendung) {
				Integer key = (Integer) panelQueryFLRBegruendung.getSelectedId();
				tpAuftrag.getAuftragDto().setAuftragbegruendungIId(key);
				AuftragbegruendungDto auftragbegruendungDto = DelegateFactory.getInstance().getAuftragServiceDelegate()
						.auftragbegruendungFindByPrimaryKey(key);
				wtfBegruendung.setText(auftragbegruendungDto.getBezeichnung());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRBegruendung) {
				tpAuftrag.getAuftragDto().setAuftragbegruendungIId(null);
				wtfBegruendung.setText(null);
			}
		}
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (tpAuftrag.getAuftragDto().getIId() != null) {
			if (tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_STORNIERT)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_TEILERLEDIGT)
					|| tpAuftrag.getAuftragDto().getStatusCNr().equals(AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
				lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLieferart;
	}
}
