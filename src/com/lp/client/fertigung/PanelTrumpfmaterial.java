package com.lp.client.fertigung;

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
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelTruTopsDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LossollmaterialId;
import com.lp.util.GotoHelper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um das Panel zur Erfassung von Preislisten.
 * <br>
 * Preislisten sind mandantenabhaengig. <br>
 * Es koennen maximal 10 Preislisten pro Mandant erfasst werden.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>29. 09. 04</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.8 $ Date $Date: 2012/10/09 08:01:39 $
 */
public class PanelTrumpfmaterial extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InternalFrameFertigung intFrame = null;

	/** Wenn true, dann die neue Position vor der aktuell markierten einfuegen. */
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private LossollmaterialDto lossollmaterialDto = null;

	private WrapperIdentField wifArtikel = new WrapperIdentField(getInternalFrame(), this);

	private WrapperLabel wlaFehlercode = new WrapperLabel();
	private WrapperTextField wtfFehlercode = new WrapperTextField();

	private WrapperLabel wlaExportBeginn = new WrapperLabel();
	private WrapperTimestampField wdfExportBeginn = new WrapperTimestampField();

	private WrapperLabel wlaExportEnde = new WrapperLabel();
	private WrapperTimestampField wdfExportEnde = new WrapperTimestampField();

	private WrapperLabel wlaFehlertext = new WrapperLabel();
	private WrapperTextArea wtfFehlertext = new WrapperTextArea();

	private WrapperLabel wlaFehlercodeArtikel = new WrapperLabel();
	private WrapperTextField wtfFehlercodeArtikel = new WrapperTextField();

	private WrapperLabel wlaExportBeginnArtikel = new WrapperLabel();
	private WrapperTimestampField wdfExportBeginnArtikel = new WrapperTimestampField();

	private WrapperLabel wlaExportEndeArtikel = new WrapperLabel();
	private WrapperTimestampField wdfExportEndeArtikel = new WrapperTimestampField();

	private WrapperLabel wlaFehlertextArtikel = new WrapperLabel();
	private WrapperTextArea wtfFehlertextArtikel = new WrapperTextArea();

	private WrapperGotoButton wbuMaterial = new WrapperGotoButton(GotoHelper.GOTO_LOSSOLLMATERIAL);

	public final static String MY_OWN_NEW_RESET = PanelBasis.ACTION_MY_OWN_NEW + "SPR_RESET";

	public PanelTrumpfmaterial(InternalFrame internalFrame, String add2TitleI, Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameFertigung) internalFrame;

		jbInit();
		initComponents();
		initPanel();
	}

	private void initPanel() throws Throwable {

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);

		getInternalFrame().addItemChangedListener(this);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = {};
		enableToolsPanelButtons(aWhichButtonIUse);

		wtfFehlercode = new WrapperTextField();
		wtfFehlercode.setColumnsMax(15);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		wlaFehlercode.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlercode.los"));
		wlaExportBeginn.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportbeginn"));
		wlaExportEnde.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportende"));
		wlaFehlertext.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlertext"));

		wlaFehlercodeArtikel.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlercode.artikel"));
		wlaExportBeginnArtikel.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportbeginn"));
		wlaExportEndeArtikel.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportende"));
		wlaFehlertextArtikel.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlertext"));

		wtfFehlertext.setRows(3);
		wtfFehlertextArtikel.setRows(3);

		JScrollPane jspScrollPaneFehlertext = new JScrollPane();
		JScrollPane jspScrollPaneFehlertextArtikel = new JScrollPane();

		wbuMaterial.setText(LPMain.getTextRespectUISPr("artikel.trumpf.sollmaterial"));

		jPanelWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 2, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jPanelWorkingOn.add(wbuMaterial, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaExportBeginn, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jPanelWorkingOn.add(wdfExportBeginn, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		jPanelWorkingOn.add(wlaExportEnde, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jPanelWorkingOn.add(wdfExportEnde, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaFehlercode, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfFehlercode, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaFehlertext, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(jspScrollPaneFehlertext, new GridBagConstraints(1, iZeile, 3, 1, 0, 0.1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jspScrollPaneFehlertext.getViewport().add(wtfFehlertext, null);

		iZeile++;
		jPanelWorkingOn.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, iZeile, 4, 1, 0.1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaExportBeginnArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jPanelWorkingOn.add(wdfExportBeginnArtikel, new GridBagConstraints(1, iZeile, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		jPanelWorkingOn.add(wlaExportEndeArtikel, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jPanelWorkingOn.add(wdfExportEndeArtikel, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaFehlercodeArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfFehlercodeArtikel, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jPanelWorkingOn.add(wlaFehlertextArtikel, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(jspScrollPaneFehlertextArtikel, new GridBagConstraints(1, iZeile, 3, 1, 0, 0.1,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jspScrollPaneFehlertextArtikel.getViewport().add(wtfFehlertextArtikel, null);

		getToolBar().addButtonRight("/com/lp/client/res/garbage.png",
				LPMain.getTextRespectUISPr("artikel.trumpf.zuruecksetzen"), MY_OWN_NEW_RESET, null, null);

	}

	private void setDefaults() throws Throwable {

		leereAlleFelder(this);

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		try {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();

				super.eventActionSave(e, true);

				eventYouAreSelected(false);
			}
		} finally {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		super.eventActionDelete(e, false, false);
	}

	private void components2Dto() throws Throwable {

	}

	private void dto2Components() throws Throwable {

		wifArtikel.setArtikelIId(lossollmaterialDto.getArtikelIId());

		wbuMaterial.setOKey(lossollmaterialDto.getIId());

		wdfExportBeginn.setTimestamp(lossollmaterialDto.getTExportBeginn());
		wdfExportEnde.setTimestamp(lossollmaterialDto.getTExportEnde());
		wtfFehlercode.setText(lossollmaterialDto.getCFehlercode());
		wtfFehlertext.setText(lossollmaterialDto.getCFehlertext());

		HvOptional<ArtikelTruTopsDto> artikelTruTopsDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelTruTopsFindByArtikelId(new ArtikelId(lossollmaterialDto.getArtikelIId()));

		if (artikelTruTopsDto.isPresent()) {

			wtfFehlercodeArtikel.setText(artikelTruTopsDto.get().getCFehlercode());
			wtfFehlertextArtikel.setText(artikelTruTopsDto.get().getCFehlertext());

			wdfExportBeginnArtikel.setTimestamp(artikelTruTopsDto.get().getTExportBeginn());
			wdfExportEndeArtikel.setTimestamp(artikelTruTopsDto.get().getTExportEnde());

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (MY_OWN_NEW_RESET.equals(e.getActionCommand()) && lossollmaterialDto != null) {
			DelegateFactory.getInstance().getFertigungDelegate()
					.resetLossollmaterialTruTops(new LossollmaterialId(lossollmaterialDto.getIId()), Boolean.FALSE);
			eventYouAreSelected(false);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		setDefaults();

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		setDefaults();

		// Key neu einlesen, Ausloeser war ev. ein Refresh oder Discard
		Object oKey = getKeyWhenDetailPanel();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {

			lossollmaterialDto = DelegateFactory.getInstance().getFertigungDelegate()
					.lossollmaterialFindByPrimaryKey((Integer) oKey);

			dto2Components();
		}

	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e Ereignis
	 * @throws Throwable Ausnahme
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VKPF_PREISLISTENNAME;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
