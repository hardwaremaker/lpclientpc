package com.lp.client.rechnung;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.rechnung.service.MmzDto;
import com.lp.server.rechnung.service.VorkassepositionDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * Diese Klasse kuemmert sich um die Wechselkurse
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Martin Bluehweis; 22.06.05
 * </p>
 *
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 *
 * @version not attributable Date $Date: 2012/08/06 07:42:38 $
 */
public class PanelAnzahlungspositionen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();

	private WrapperLabel wlaZeilensumme = new WrapperLabel();
	private WrapperNumberField wnfZeilensumme = new WrapperNumberField();

	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperNumberField wnfProzent = new WrapperNumberField();

	private final TabbedPaneRechnung tpRechnung;

	private AuftragpositionDto auftragpositionDto = null;

	private VorkassepositionDto anzahlungspositionDto = null;

	private WrapperIdentField wifArtikelauswahl = null;

	public PanelAnzahlungspositionen(InternalFrame internalFrame, String add2TitleI, Object pk,
			TabbedPaneRechnung tpRechnung) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tpRechnung = tpRechnung;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		anzahlungspositionDto = new VorkassepositionDto();
		leereAlleFelder(this);
		setDefaults();
		clearStatusbar();

	}

	private void berechneProzent() throws Throwable {

		if (wnfBetrag.getBigDecimal() != null && auftragpositionDto != null) {
			wnfProzent.setBigDecimal(Helper.getProzentsatzBD(
					auftragpositionDto.getNNettoeinzelpreis().multiply(auftragpositionDto.getNMenge()),
					wnfBetrag.getBigDecimal(), 2));
		}

	}

	protected void dto2Components() throws Throwable {
		if (anzahlungspositionDto != null && auftragpositionDto != null) {
			wnfBetrag.setBigDecimal(anzahlungspositionDto.getNBetrag());

			berechneProzent();

		} else {
			wnfBetrag.setBigDecimal(null);
			wnfProzent.setBigDecimal(null);
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		if (((InternalFrameRechnung) tpRechnung.getInternalFrame())
				.isUpdateAllowedForRechnungDto(tpRechnung.getRechnungDto())) {
			super.eventActionUpdate(aE, false);
			if (anzahlungspositionDto == null || anzahlungspositionDto.getIId() == null) {
				//SP7956
				BigDecimal zeilensummme = auftragpositionDto.getNNettoeinzelpreis()
						.multiply(auftragpositionDto.getNMenge());

				BigDecimal bdBereitsAngezahlt = DelegateFactory.getInstance().getRechnungDelegate()
						.getSummeVorkasseposition(tpRechnung.getRechnungDto().getIId(), auftragpositionDto.getIId());

				BigDecimal bdOffen = zeilensummme.subtract(bdBereitsAngezahlt);

				wnfBetrag.setBigDecimal(bdOffen);

				wnfProzent.setBigDecimal(
						Helper.getProzentsatzBD(zeilensummme, zeilensummme.subtract(bdBereitsAngezahlt), 2));

			}
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr("rech.anzahlung.positionen.betrag") + " "
				+ tpRechnung.getRechnungDto().getWaehrungCNr().trim());
		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();

		} else {

			Integer auftragpositionIId = (Integer) key;

			auftragpositionDto = DelegateFactory.getInstance().getAuftragpositionDelegate()
					.auftragpositionFindByPrimaryKey(auftragpositionIId);

			BigDecimal zeilensumme = auftragpositionDto.getNNettoeinzelpreis().multiply(auftragpositionDto.getNMenge());

			wnfZeilensumme.setBigDecimal(zeilensumme);

			wifArtikelauswahl.setArtikelIId(auftragpositionDto.getArtikelIId());

			anzahlungspositionDto = DelegateFactory.getInstance().getRechnungDelegate()
					.vorkassepositionFindByRechnungIIdAuftragspositionIId(tpRechnung.getRechnungDto().getIId(),
							auftragpositionIId);
			dto2Components();
		}

	}

	protected void components2Dto() throws Throwable {

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if(wnfProzent.hasFocus()) {
				
				FocusEvent fe=new FocusEvent(wnfProzent,0);
				
				focusLost(fe);
			}
			
			if (auftragpositionDto != null) {
				anzahlungspositionDto = new VorkassepositionDto();
				anzahlungspositionDto.setRechnungIId(tpRechnung.getRechnungDto().getIId());
				anzahlungspositionDto.setAuftragspositionIId(auftragpositionDto.getIId());
				anzahlungspositionDto.setNBetrag(wnfBetrag.getBigDecimal());

				anzahlungspositionDto.setIId(DelegateFactory.getInstance().getRechnungDelegate()
						.updateVorkasseposition(anzahlungspositionDto));
				// setKeyWhenDetailPanel(anzahlungspositionDto.getIId());

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(anzahlungspositionDto.getIId() + "");
				}
			}

			eventYouAreSelected(false);

		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (((InternalFrameRechnung) tpRechnung.getInternalFrame())
				.isUpdateAllowedForRechnungDto(tpRechnung.getRechnungDto())) {

			if (anzahlungspositionDto != null) {
				DelegateFactory.getInstance().getRechnungDelegate().removeVorkassepositionDto(anzahlungspositionDto);
				anzahlungspositionDto = null;
			}

			super.eventActionDelete(e, false, false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
	}

	public void focusLost(FocusEvent e) {
		try {
			if (e.getSource().equals(wnfBetrag)) {

				berechneProzent();

			} else if (e.getSource().equals(wnfProzent)) {

				if (wnfProzent.getBigDecimal() != null && auftragpositionDto != null) {
					BigDecimal zeilensumme = auftragpositionDto.getNNettoeinzelpreis()
							.multiply(auftragpositionDto.getNMenge());

					wnfBetrag.setBigDecimal(Helper.getProzentWert(zeilensumme, wnfProzent.getBigDecimal(),
							Defaults.getInstance().getIUINachkommastellenPreiseVK()));
				}

			}
		} catch (Throwable e1) {
			handleException(e1, true);
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		wnfBetrag.setMandatoryField(true);

		wifArtikelauswahl = new WrapperIdentField(getInternalFrame(), this);
		wifArtikelauswahl.getWbuArtikel().setActivatable(false);

		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr("rech.anzahlung.positionen.betrag"));
		wlaProzent.setText(LPMain.getInstance().getTextRespectUISPr("rech.anzahlung.positionen.prozentsatz"));
		wlaZeilensumme.setText(LPMain.getInstance().getTextRespectUISPr("rech.anzahlung.positionen.zeilensumme"));

		int iPreiseUINachkommastellenVK = Defaults.getInstance().getIUINachkommastellenPreiseVK();

		wnfBetrag.setFractionDigits(iPreiseUINachkommastellenVK);
		wnfZeilensumme.setActivatable(false);

		wnfBetrag.addFocusListener(this);
		wnfProzent.addFocusListener(this);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wifArtikelauswahl.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wifArtikelauswahl.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wifArtikelauswahl.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.8, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaZeilensumme, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfZeilensumme, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wlaProzent, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wnfProzent, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_WAEHRUNG;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
