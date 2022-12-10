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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.LieferartDto;
import com.lp.server.system.service.LieferartsprDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
/**
 * <p>
 * Diese Klasse kuemmert sich um das KostenstellenCRUD.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Josef Ornetsmueller; 13.05.05
 * </p>
 *
 * <p>
 * @author $Author: christian $
 * </p>
 *
 * @version $Revision: 1.4 $ Date $Date: 2009/11/13 10:12:50 $
 */
public class PanelLieferart extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private LieferartDto lieferartDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaVersteckt = new WrapperLabel();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperLabel wlaFrachtkostenAlsErledigtVerbuchen = new WrapperLabel();
	private WrapperCheckBox wcbFrachtkostenAlsErledigtVerbuchen = new WrapperCheckBox();
	private WrapperLabel wlaVersandort = new WrapperLabel();;
	private WrapperTextField wtfVersandort = new WrapperTextField();

	private WrapperLabel wlaLieferort = new WrapperLabel();
	private WrapperComboBox wcoLieferort = new WrapperComboBox();
	private WrapperLabel wlaExtern = new WrapperLabel();
	private WrapperTextField wtfExtern = new WrapperTextField(40);

	private WrapperIdentField wifVersandkosten = new WrapperIdentField(getInternalFrame(), this);

	public PanelLieferart(InternalFrame internalFrame, String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
		if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Exception, Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();

		add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// jetzt meine Felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		getInternalFrame().addItemChangedListener(this);

		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung = new WrapperTextField(SystemFac.MAX_KOSTENSTELLE_BEZEICHNUNG);

		wlaKennung = new WrapperLabel(textFromToken("label.kennung"));
		wtfKennung.setColumnsMax(MandantFac.MAX_LIEFERART_C_NR);
		wtfKennung.setMandatoryField(true);

		wcoLieferort.setMandatoryField(true);

		wlaBezeichnung = new WrapperLabel(textFromToken("label.bezeichnung_lang"));
		wtfBezeichnung.setColumnsMax(120);

		wifVersandkosten.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("lp.lieferart.versandkosten"));
		wifVersandkosten.getWtfIdent().setMandatoryField(false);
		wifVersandkosten.setBMitLeerenButton(true);
		wlaFrachtkostenAlsErledigtVerbuchen = new WrapperLabel(
				textFromToken("lp.frachtenkosten_als_erledigt_verbuchen"));

		wlaVersteckt = new WrapperLabel(textFromToken("lp.versteckt"));

		wlaVersandort = new WrapperLabel(textFromToken("lp.versandort"));
		wlaLieferort = new WrapperLabel(textFromToken("lp.lieferart.lieferort"));

		wcbVersteckt = new WrapperCheckBox();

		wlaExtern = new WrapperLabel(textFromToken("lp.lieferart.extern"));

		Map<Integer, String> m = new LinkedHashMap<Integer, String>();

		m.put(LocaleFac.LIEFERART_LIEFERORT_AUS_KONDITIONEN,
				LPMain.getInstance().getTextRespectUISPr("lp.lieferart.lieferort.auskonditionen"));
		m.put(LocaleFac.LIEFERART_LIEFERORT_AUS_MANDANTENADRESSE,
				LPMain.getInstance().getTextRespectUISPr("lp.lieferart.lieferort.ausmandantenadresse"));
		m.put(LocaleFac.LIEFERART_LIEFERORT_AUS_LIEFERADRESSE,
				LPMain.getInstance().getTextRespectUISPr("lp.lieferart.lieferort.auslieferadresse"));

		wcoLieferort.setMap(m);

		// Zeile.
		iZeile++;
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaFrachtkostenAlsErledigtVerbuchen, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbFrachtkostenAlsErledigtVerbuchen, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersteckt, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersandort, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandort, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLieferort, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoLieferort, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaExtern, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfExtern, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wifVersandkosten.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifVersandkosten.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void dto2Components() throws Throwable {

		wtfKennung.setText(lieferartDto.getCNr());
		if (lieferartDto.getLieferartsprDto() != null) {
			wtfBezeichnung.setText(lieferartDto.getLieferartsprDto().getCBezeichnung());
		} else {
			wtfBezeichnung.setText(null);
		}
		wtfVersandort.setText(lieferartDto.getCVersandort());

		wcbVersteckt.setShort(lieferartDto.getBVersteckt());
		wcbFrachtkostenAlsErledigtVerbuchen.setShort(lieferartDto.getBFrachtkostenalserledigtverbuchen());
		wcoLieferort.setKeyOfSelectedItem(lieferartDto.getILieferort());
		wtfExtern.setText(lieferartDto.getCExtern());

		wifVersandkosten.setArtikelIId(lieferartDto.getArtikelIIdVersand());

	}

	protected void components2Dto() throws Throwable {

		lieferartDto.setCNr(wtfKennung.getText());
		if (lieferartDto.getLieferartsprDto() == null) {
			lieferartDto.setLieferartsprDto(new LieferartsprDto());
		}
		lieferartDto.getLieferartsprDto().setCBezeichnung(wtfBezeichnung.getText());
		lieferartDto.getLieferartsprDto().setLocaleCNr(LPMain.getTheClient().getLocUiAsString());

		lieferartDto.setCVersandort(wtfVersandort.getText());
		lieferartDto.setILieferort((Integer) wcoLieferort.getKeyOfSelectedItem());
		lieferartDto.setBVersteckt(wcbVersteckt.getShort());
		lieferartDto.setBFrachtkostenalserledigtverbuchen(wcbFrachtkostenAlsErledigtVerbuchen.getShort());
		lieferartDto.setCExtern(wtfExtern.getText());

		lieferartDto.setArtikelIIdVersand(wifVersandkosten.getArtikelIId());

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		DelegateFactory.getInstance().getLocaleDelegate().removeLieferart(lieferartDto);

		super.eventActionDelete(e, false, false);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		lieferartDto = new LieferartDto();

		setDefaults();

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);

		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().getMandant(),
				getInternalFrameSystem().getMandantDto().getCNr());

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
				leereAlleFelder(this);
				clearStatusbar();
				setDefaults();
			} else {
				lieferartDto = DelegateFactory.getInstance().getLocaleDelegate()
						.lieferartFindByPrimaryKey((Integer) key);
				getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
						getInternalFrameSystem().getMandantDto().getCNr());

				setStatusbar();
			}
			dto2Components();
			initPanel();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (!bNeedNoSaveI) {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (lieferartDto.getIId() == null) {
					// create
					Integer key = DelegateFactory.getInstance().getLocaleDelegate().createLieferart(lieferartDto);
					setKeyWhenDetailPanel(key);
					lieferartDto.setIId(key);
				} else {
					// update
					DelegateFactory.getInstance().getLocaleDelegate().updateLieferart(lieferartDto);
				}
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
			}
		} else {
			super.eventActionSave(e, true);
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERART;
	}

	protected void setStatusbar() throws Throwable {

	}

	protected void setDefaults() throws Throwable {
		wcoLieferort.setKeyOfSelectedItem(LocaleFac.LIEFERART_LIEFERORT_AUS_KONDITIONEN);
	}

	private void initPanel() {

	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI) throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);
		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().getMandant(),
				getInternalFrameSystem().getMandantDto().getCNr());
	}

	/**
	 * 
	 * @param loggedinMandant String
	 * @param selectedMandant String
	 * @throws Throwable
	 */
	private void checkMandantLoggedInEqualsMandantSelected(String loggedinMandant, String selectedMandant)
			throws Throwable {

		if (!loggedinMandant.equals(selectedMandant)) {

			LPButtonAction item = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_UPDATE);
			LPButtonAction item1 = (LPButtonAction) getHmOfButtons().get(PanelBasis.ACTION_DELETE);

			item.getButton().setEnabled(false);
			item1.getButton().setEnabled(false);

			getPanelStatusbar().setLockField(LPMain.getInstance().getTextRespectUISPr("system.nurleserecht"));
		}
	}

}
