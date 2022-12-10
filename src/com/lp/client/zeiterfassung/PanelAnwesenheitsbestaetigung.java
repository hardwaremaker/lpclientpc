
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
package com.lp.client.zeiterfassung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperPdfField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.IconFactory;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.personal.service.AnwesenheitsbestaetigungDto;
import com.lp.server.system.service.BelegartDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class PanelAnwesenheitsbestaetigung extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	WrapperSelectField wsfPerson = new WrapperSelectField(WrapperSelectField.PERSONAL, getInternalFrame(), false);
	private WrapperSelectField wsfProjekt = new WrapperSelectField(WrapperSelectField.PROJEKT, getInternalFrame(),
			true);
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperTextField wtfAuftrag = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();

	private WrapperLabel wlaUnterschrift = new WrapperLabel();
	private WrapperTimestampField wtfUnterschrift = new WrapperTimestampField();

	private WrapperLabel wlaVersandt = new WrapperLabel();
	private WrapperTimestampField wtfVersandt = new WrapperTimestampField();

	private JLabel wlaUnterschriftBild = new JLabel();
	private WrapperBildField wmcUnterschrift = new WrapperBildField(getInternalFrame(), "");

	private JLabel wlaPdf = new JLabel();
	private WrapperPdfField wpdPdf = new WrapperPdfField(getInternalFrame(), "");

	private InternalFrame internalFrame = null;

	private WrapperLabel wlaName = new WrapperLabel();
	private WrapperTextField wtfName = new WrapperTextField(80);

	private WrapperLabel wlaLfdNr = new WrapperLabel();
	private WrapperNumberField wnfLfdNr = new WrapperNumberField();

	static final public String ACTION_SPECIAL_AUFTRAG_FROM_LISTE = "action_auftrag_from_liste";
	static final public String ACTION_SPECIAL_PDF_LEEREN = "action_pdf_leeren";

	private WrapperButton jbuPDFSetNull = null;

	private AnwesenheitsbestaetigungDto anwesenheitsbestaetigungDto = null;

	public AnwesenheitsbestaetigungDto getAnwesenheitsbestaetigungDto() {
		return anwesenheitsbestaetigungDto;
	}

	public void setAnwesenheitsbestaetigungDto(AnwesenheitsbestaetigungDto anwesenheitsbestaetigungDto) {
		this.anwesenheitsbestaetigungDto = anwesenheitsbestaetigungDto;
	}
	
	public PanelAnwesenheitsbestaetigung(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.internalFrame =  internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBemerkung;
	}

	protected void setDefaults() throws Throwable {

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		setAnwesenheitsbestaetigungDto(new AnwesenheitsbestaetigungDto());
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG_FROM_LISTE)) {
			dialogQueryAuftragFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_PDF_LEEREN)) {
			wpdPdf.setPdf(null);
		}

	}


	void dialogQueryAuftragFromListe(ActionEvent e) throws Throwable {
		FilterKriterium[] fk = SystemFilterFactory.getInstance().createFKMandantCNr();
		Integer auftragIId = null;
		if (getAnwesenheitsbestaetigungDto() != null) {
			auftragIId = getAnwesenheitsbestaetigungDto().getAuftragIId();
		}
		panelQueryFLRAuftrag = AuftragFilterFactory.getInstance().createPanelFLRAuftrag(getInternalFrame(), true, true,
				fk, auftragIId);
		new DialogQuery(panelQueryFLRAuftrag);

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getPersonalDelegate()
				.removeAnwesenheitsbestaetigung(getAnwesenheitsbestaetigungDto());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		getAnwesenheitsbestaetigungDto().setCBemerkung(wtfBemerkung.getText());
		getAnwesenheitsbestaetigungDto().setPersonalIId(wsfPerson.getIKey());
		getAnwesenheitsbestaetigungDto().setProjektIId(wsfProjekt.getIKey());

		getAnwesenheitsbestaetigungDto().setTUnterschrift(wtfUnterschrift.getTimestamp());
		getAnwesenheitsbestaetigungDto().setTVersandt(wtfVersandt.getTimestamp());

		getAnwesenheitsbestaetigungDto().setOPdf(wpdPdf.getPdf());
		getAnwesenheitsbestaetigungDto().setOUnterschrift(wmcUnterschrift.getImageOriginalBytes());

		getAnwesenheitsbestaetigungDto().setCName(wtfName.getText());
		getAnwesenheitsbestaetigungDto().setILfdnr(wnfLfdNr.getInteger());

	}

	protected void dto2Components() throws Throwable {
		wtfBemerkung.setText(getAnwesenheitsbestaetigungDto().getCBemerkung());

		String belegCnr = null;
		if (getAnwesenheitsbestaetigungDto().getAuftragIId() != null) {
			AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey(getAnwesenheitsbestaetigungDto().getAuftragIId());

			wtfAuftrag.setText(auftragDto.getCNr());

			BelegartDto belegartDto = DelegateFactory.getInstance().getLocaleDelegate()
					.belegartFindByPrimaryKey(auftragDto.getBelegartCNr());
			belegCnr = belegartDto.getCKurzbezeichnung() + "_" + auftragDto.getCNr();
		} else {
			wtfAuftrag.setText(null);
		}

		wsfProjekt.setKey(getAnwesenheitsbestaetigungDto().getProjektIId());
		wsfPerson.setKey(getAnwesenheitsbestaetigungDto().getPersonalIId());

		wtfUnterschrift.setTimestamp(getAnwesenheitsbestaetigungDto().getTUnterschrift());
		wtfVersandt.setTimestamp(getAnwesenheitsbestaetigungDto().getTVersandt());

		wpdPdf.setPdf(getAnwesenheitsbestaetigungDto().getOPdf());
		wmcUnterschrift.setImage(getAnwesenheitsbestaetigungDto().getOUnterschrift());

		if (belegCnr != null) {
			wmcUnterschrift.setDateiname(belegCnr + "_unterschrift"
					+ getExtensionFromDatenformat(getAnwesenheitsbestaetigungDto().getDatenformatCNr()));
			wpdPdf.setDateiname(belegCnr + ".pdf");
		}

		wnfLfdNr.setInteger(getAnwesenheitsbestaetigungDto().getILfdnr());
		wtfName.setText(getAnwesenheitsbestaetigungDto().getCName());
	}

	private String getExtensionFromDatenformat(String datenformatCnr) {
		if (datenformatCnr != null && "image/png".equals(datenformatCnr.trim()))
			return ".png";
		return "";
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (getAnwesenheitsbestaetigungDto().getIId() == null) {
				getAnwesenheitsbestaetigungDto().setIId(DelegateFactory.getInstance().getPersonalDelegate()
						.createAnwesenheitsbestaetigung(getAnwesenheitsbestaetigungDto()));
				setKeyWhenDetailPanel(getAnwesenheitsbestaetigungDto().getIId());
			} else {
				DelegateFactory.getInstance().getPersonalDelegate()
						.updateAnwesenheitsbestaetigung(getAnwesenheitsbestaetigungDto());
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(getAnwesenheitsbestaetigungDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey((Integer) key);
				wtfAuftrag.setText(auftragDto.getCNr());
				getAnwesenheitsbestaetigungDto().setAuftragIId(auftragDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				wtfAuftrag.setText(null);
				getAnwesenheitsbestaetigungDto().setAuftragIId(null);
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

		wsfPerson.setMandatoryField(true);

		wlaUnterschrift.setText(LPMain.getTextRespectUISPr("pers.anwesenheitsbestaetigung.unterschrieben"));

		wlaUnterschriftBild.setText(LPMain.getTextRespectUISPr("pers.anwesenheitsbestaetigung.unterschrift") + ":");

		jbuPDFSetNull = new WrapperButton(IconFactory.getClear());

		jbuPDFSetNull.setActionCommand(ACTION_SPECIAL_PDF_LEEREN);
		jbuPDFSetNull.addActionListener(this);

		jbuPDFSetNull.setMinimumSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));
		jbuPDFSetNull.setPreferredSize(
				new Dimension(Defaults.getInstance().getControlHeight(), Defaults.getInstance().getControlHeight()));

		wlaPdf.setText("PDF:");
		wlaVersandt.setText(LPMain.getTextRespectUISPr("pers.anwesenheitsbestaetigung.versandt"));

		wlaName.setText(LPMain.getTextRespectUISPr("pers.anwesenheitsbestaetigung.name"));

		wlaLfdNr.setText(LPMain.getTextRespectUISPr("pers.anwesenheitsbestaetigung.lfdnr"));
		wnfLfdNr.setMandatoryField(true);
		wnfLfdNr.setFractionDigits(0);

		wtfAuftrag.setActivatable(false);
		wtfUnterschrift.setMandatoryField(true);

		wbuAuftrag.setText(LPMain.getTextRespectUISPr("button.auftrag"));
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG_FROM_LISTE);
		wbuAuftrag.addActionListener(this);

		wlaBemerkung.setText(LPMain.getTextRespectUISPr("lp.bemerkung"));

		
		
		
		wtfBemerkung.setColumns(80);
		
		
		if(getInternalFrame() instanceof InternalFrameAuftrag) {
			wbuAuftrag.setActivatable(false);
			wtfAuftrag.setMandatoryField(true);
			wtfAuftrag.setActivatable(false);
		}
		
		getInternalFrame().addItemChangedListener(this);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wsfPerson.getWrapperButton(), new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 130, 0));
		jpaWorkingOn.add(wsfPerson.getWrapperTextField(), new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wsfProjekt.getWrapperGotoButton(), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfProjekt.getWrapperTextField(), new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaName, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfName, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLfdNr, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLfdNr, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaUnterschrift, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfUnterschrift, new GridBagConstraints(1, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 150, 0));

		jpaWorkingOn.add(wlaVersandt, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(wtfVersandt, new GridBagConstraints(3, iZeile, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaUnterschriftBild, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPdf, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(jbuPDFSetNull, new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 30, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wmcUnterschrift, new GridBagConstraints(0, iZeile, 3, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wpdPdf, new GridBagConstraints(3, iZeile, 2, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBemerkung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBemerkung, new GridBagConstraints(1, iZeile, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ANWESENHEITSBESTAETIGUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			setAnwesenheitsbestaetigungDto(DelegateFactory.getInstance()
					.getPersonalDelegate().anwesenheitsbestaetigungFindByPrimaryKey((Integer) key));
			dto2Components();
		}
		
		if (key != null && key.equals(LPMain.getLockMeForNew())) {
			if(getInternalFrame() instanceof InternalFrameAuftrag) {
				
				Integer auftragIId=((InternalFrameAuftrag)getInternalFrame()).getTabbedPaneAuftrag().getAuftragDto().getIId(); 
				
				AuftragDto auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey(auftragIId);
				wtfAuftrag.setText(auftragDto.getCNr());
				getAnwesenheitsbestaetigungDto().setAuftragIId(auftragDto.getIId());
			}
		}
		
		
		
	}
}
