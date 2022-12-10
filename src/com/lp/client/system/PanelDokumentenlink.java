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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.DokumentenlinkDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelDokumentenlink extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameSystem internalFramePersonal = null;
	private DokumentenlinkDto dokumentenlinkDto = null;
	private WrapperLabel wlaBasispfad = new WrapperLabel();
	private WrapperTextField wtfBasispfad = new WrapperTextField();
	private WrapperLabel wlaOrdner = new WrapperLabel();
	private WrapperTextField wtfOrdner = new WrapperTextField();
	private WrapperLabel wlaMenuetext = new WrapperLabel();
	private WrapperTextField wtfMenuetext = new WrapperTextField();
	private WrapperCheckBox wcbPfadabsolut = new WrapperCheckBox();
	private WrapperCheckBox wcbUrl = new WrapperCheckBox();
	private WrapperCheckBox wcbPfadAusArbeitsplatzparameter = new WrapperCheckBox();
	private WrapperCheckBox wcbTitel = new WrapperCheckBox();
	private WrapperLabel wlaRecht = new WrapperLabel();
	private WrapperComboBox wcoRecht = new WrapperComboBox();

	private String ACTION_SPECIAL_FLR_BELEGART = "ACTION_SPECIAL_FLR_BELEGART";

	private PanelQueryFLR panelQueryFLRBelegart = null;

	private WrapperButton wbuBelegart = new WrapperButton();
	private WrapperTextField wtfBelegart = new WrapperTextField();

	public PanelDokumentenlink(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameSystem) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBasispfad;
	}

	protected void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		dokumentenlinkDto = new DokumentenlinkDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
		wcoRecht.setKeyOfSelectedItem(null);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_BELEGART)) {

			FilterKriterium[] kriterien = new FilterKriterium[1];

			FilterKriterium krit1 = new FilterKriterium("c_nr", true, "('"
					+ LocaleFac.BELEGART_ARTIKEL + "','"
					+ LocaleFac.BELEGART_BESTELLUNG + "','"
					+ LocaleFac.BELEGART_STUECKLISTE + "','"
					+ LocaleFac.BELEGART_AUFTRAG + "','"
					+ LocaleFac.BELEGART_ANGEBOT + "','"
					+ LocaleFac.BELEGART_ANFRAGE + "','"
					+ LocaleFac.BELEGART_LOS + "','"
					+ LocaleFac.BELEGART_EINGANGSRECHNUNG + "','"
					+ LocaleFac.BELEGART_ZUSATZKOSTEN + "','"
					+ LocaleFac.BELEGART_EINKAUFSANGEBOT + "','"
					+ LocaleFac.BELEGART_REKLAMATION + "','"
					+ LocaleFac.BELEGART_PARTNER + "','"
					+ LocaleFac.BELEGART_LIEFERANT + "','"
					+ LocaleFac.BELEGART_PROJEKT + "','"
					+ LocaleFac.BELEGART_KUNDE + "','"
					+ LocaleFac.BELEGART_LIEFERANT + "')",
					FilterKriterium.OPERATOR_IN, false);

			kriterien[0] = krit1;

			panelQueryFLRBelegart = new PanelQueryFLR(null, kriterien,
					QueryParameters.UC_ID_BELEGART, null, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.belegart"));
			if (dokumentenlinkDto.getBelegartCNr() != null) {
				panelQueryFLRBelegart.setSelectedId(dokumentenlinkDto
						.getBelegartCNr());
			}

			new DialogQuery(panelQueryFLRBelegart);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getMandantDelegate()
				.removeDokumentenlink(dokumentenlinkDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {

		dokumentenlinkDto.setCBasispfad(wtfBasispfad.getText());
		dokumentenlinkDto.setCMenuetext(wtfMenuetext.getText());
		dokumentenlinkDto.setCOrdner(wtfOrdner.getText());
		dokumentenlinkDto.setBPfadabsolut(wcbPfadabsolut.getShort());
		dokumentenlinkDto.setBUrl(wcbUrl.getShort());
		dokumentenlinkDto
				.setBPfadAusArbeitsplatzparameter(wcbPfadAusArbeitsplatzparameter
						.getShort());
		dokumentenlinkDto.setMandantCNr(internalFramePersonal.getMandantDto()
				.getCNr());
		dokumentenlinkDto.setBelegartCNr(wtfBelegart.getText());
		dokumentenlinkDto.setBTitel(wcbTitel.getShort());
		dokumentenlinkDto.setRechtCNr((String) wcoRecht.getKeyOfSelectedItem());
	}

	protected void dto2Components() throws Throwable {
		wtfBasispfad.setText(dokumentenlinkDto.getCBasispfad());
		wtfMenuetext.setText(dokumentenlinkDto.getCMenuetext());
		wtfOrdner.setText(dokumentenlinkDto.getCOrdner());
		wtfBelegart.setText(dokumentenlinkDto.getBelegartCNr());
		wcbPfadabsolut.setShort(dokumentenlinkDto.getBPfadabsolut());
		wcbUrl.setShort(dokumentenlinkDto.getBUrl());
		wcbPfadAusArbeitsplatzparameter.setShort(dokumentenlinkDto
				.getBPfadAusArbeitsplatzparameter());
		wcbTitel.setShort(dokumentenlinkDto.getBTitel());
		if(dokumentenlinkDto.getRechtCNr()!=null){
			wcoRecht.setKeyOfSelectedItem(dokumentenlinkDto.getRechtCNr().trim());	
		} else{
			wcoRecht.setKeyOfSelectedItem(null);
		}
		

		setDependentComponentsVisibleByBelegart(dokumentenlinkDto
				.getBelegartCNr());
	}

	private void setDependentComponentsVisibleByBelegart(String belegart) {
		wcbPfadabsolut.setVisible(LocaleFac.BELEGART_ARTIKEL.equals(belegart));
		wcbTitel.setVisible(LocaleFac.BELEGART_PROJEKT.equals(belegart)
				&& !wcbUrl.isSelected());
	}

	private void setDependentComponentsInvisible() {
		setDependentComponentsVisibleByBelegart(null);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (dokumentenlinkDto.getIId() == null) {
				dokumentenlinkDto.setIId(DelegateFactory.getInstance()
						.getMandantDelegate()
						.createDokumentenlink(dokumentenlinkDto));
				setKeyWhenDetailPanel(dokumentenlinkDto.getIId());

			} else {
				DelegateFactory.getInstance().getMandantDelegate()
						.updateDokumentenlink(dokumentenlinkDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						dokumentenlinkDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRBelegart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfBelegart.setText((String) key);

				setDependentComponentsVisibleByBelegart(wtfBelegart.getText());
			}
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
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wcbTitel.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dokumentenlink.mittitel"));
		wcbPfadabsolut.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dokumentenlink.pfadabsolut"));

		wcbUrl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dokumentenlink.weblink"));
		wcbUrl.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				setDependentComponentsVisibleByBelegart(wtfBelegart.getText());
			}
		});

		wcbPfadAusArbeitsplatzparameter.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.dokumentenlink.pfadausparameter"));

		wlaRecht.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.dokumentenlink.recht"));

		wlaBasispfad.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.dokumentenlink.basispfad"));

		Map m = new LinkedHashMap();
		m.put(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU,
				RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU);
		m.put(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU,
				RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU);
		m.put(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU,
				RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU);
		m.put(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU,
				RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU);

		wcoRecht.setMap(m);

		wtfBasispfad.setMandatoryField(true);
		wtfBasispfad.setColumnsMax(300);

		wlaMenuetext.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.dokumentenlink.menuetext"));
		wtfMenuetext.setMandatoryField(true);

		wlaOrdner.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.dokumentenlink.ordner"));
		wtfOrdner.setColumnsMax(80);

		wbuBelegart.setText(LPMain.getInstance().getTextRespectUISPr(
				"system.dokumentenlink.belegart"));
		wbuBelegart.setActionCommand(ACTION_SPECIAL_FLR_BELEGART);
		wbuBelegart.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);

		wtfBelegart.setMandatoryField(true);
		wtfBelegart.setActivatable(false);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBasispfad, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBasispfad, new GridBagConstraints(1, iZeile, 2, 1,
				0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaOrdner, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfOrdner, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuBelegart, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBelegart, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenuetext, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMenuetext, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbPfadabsolut, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcbUrl, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbPfadAusArbeitsplatzparameter,
				new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbTitel, new GridBagConstraints(1, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaRecht, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoRecht, new GridBagConstraints(1, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);

		// wcbPfadabsolut.setVisible(false);
		setDependentComponentsInvisible();

		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			dokumentenlinkDto = DelegateFactory.getInstance()
					.getMandantDelegate()
					.dokumentenlinkFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
