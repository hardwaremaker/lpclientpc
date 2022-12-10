
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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.mengenstaffel.PanelMengenstaffelArtgru;
import com.lp.client.frame.component.mengenstaffel.PanelMengenstaffelArtikel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.PanelKundesokomengenstaffel;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;

@SuppressWarnings("static-access")
/**
 * <p>
 * In diesem Fenster wird eine Kundesokomengenstaffel erfasst.
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 07.07.2006
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.7 $
 */
public abstract class PanelArtikelSokoBasis extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanelArt = null;

	protected WrapperSelectField wsfKunde = new WrapperSelectField(WrapperSelectField.KUNDE, getInternalFrame(), false);

	private WrapperLabel wlaKundeartikelnummer = null;
	protected WrapperTextField wtfKundeartikelnummer = new WrapperTextField(25);

	protected PanelMengenstaffelArtikel panelArtikel = null;
	protected boolean bMitKundenartikelnummer = false;

	public PanelArtikelSokoBasis(InternalFrame internalFrame, String add2TitleI, Object key,
			boolean bMitKundenartikelnummer) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.bMitKundenartikelnummer = bMitKundenartikelnummer;
		initPanel();
		jbInitLocal();
		initComponents();
	}

	protected InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource().equals(wsfKunde.getPanelQueryFLR())) {

				KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate()
						.kundeFindByPrimaryKey(wsfKunde.getIKey());

				panelArtikel.setWaehrungCNr(kundeDto.getCWaehrung());
				panelArtikel.setLabels(kundeDto.getCWaehrung());

				//aufgrund SP8800 auskommentiert
				/*boolean bEnable = LPMain.getTheClient().getSMandantenwaehrung()
						.equals(kundeDto.getCWaehrung());
				panelArtikel.getWnfRabattsatz().setEditable(bEnable);
				panelArtikel.getWnfRabattsatz().setMandatoryField(bEnable);*/
				
			}
		}
	}

	private void jbInitLocal() throws Throwable {
		wsfKunde.setMandatoryField(true);
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		getInternalFrame().addItemChangedListener(this);

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		createPanelArt();
		iZeile++;
		add(jPanelArt, new GridBagConstraints(0, iZeile++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		panelArtikel = new PanelMengenstaffelArtikel(getInternalFrame(), "Artikel", true);

		add(panelArtikel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
	}

	public void createPanelArt() {
		if (jPanelArt == null) {
			jPanelArt = new JPanel();
			jPanelArt.setLayout(new GridBagLayout());
			jPanelArt.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		}

		wlaKundeartikelnummer = new WrapperLabel(
				LPMain.getInstance().getTextRespectUISPr("kunde.soko.kundeartikelnummer"));

		jPanelArt.add(wsfKunde.getWrapperGotoButton(), new GridBagConstraints(0, 0, 1, 1, .5, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelArt.add(wsfKunde.getWrapperTextField(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelArt.add(wlaKundeartikelnummer, new GridBagConstraints(2, 0, 1, 1, .5, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelArt.add(wtfKundeartikelnummer, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	private void initPanel() throws Throwable {

	}

	protected void setDefaults() throws Throwable {

		leereAlleFelder(this);

		panelArtikel.setDefaults();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

}
