
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
import java.awt.image.BufferedImage;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperBildField;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPdfField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p>Diese Klasse kuemmert sich um die Mandantdaten.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellungsdatum <I>08.03.05</I></p>
 * 
 * @author $Author: christoph $
 * 
 * @version $Revision: 1.5 $
 */
public class PanelMandantAgb extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = null;
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperPdfField wpdPDF = new WrapperPdfField(getInternalFrame(), "", null, true);

	private WrapperLabel wlaAGBs = null;
	private WrapperLabel wlaPDF = null;

	private WrapperLabel wlaBeiBelegen = null;

	private WrapperCheckBox wcbAngebot = new WrapperCheckBox();
	private WrapperCheckBox wcbAnfrage = new WrapperCheckBox();
	private WrapperCheckBox wcbAuftrag = new WrapperCheckBox();
	private WrapperCheckBox wcbBestellung = new WrapperCheckBox();
	private WrapperCheckBox wcbLieferschein = new WrapperCheckBox();
	private WrapperCheckBox wcbRechnung = new WrapperCheckBox();

	private WrapperRadioButton wrbMitdrucken = null;
	private WrapperRadioButton wrbAnhaengen = null;

	private ButtonGroup bg = new ButtonGroup();

	public PanelMandantAgb(InternalFrame internalFrame, String add2TitleI, Object keyI) throws Throwable {
		super(internalFrame, add2TitleI, keyI);
		jbInit();
		initComponents();
	}

	private void initPanel() throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		String[] aButton = { PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aButton);

		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

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

		wlaAGBs = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.mandant.agb"));
		wlaBeiBelegen = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("lp.mandant.agb.beibelegen"));

		wcbAnfrage.setText(LPMain.getInstance().getTextRespectUISPr("anf.anfrage"));
		wcbAngebot.setText(LPMain.getInstance().getTextRespectUISPr("angb.angebot"));
		wcbAuftrag.setText(LPMain.getInstance().getTextRespectUISPr("auft.auftrag"));
		wcbBestellung.setText(LPMain.getInstance().getTextRespectUISPr("best.title.bestellung"));
		wcbLieferschein.setText(LPMain.getInstance().getTextRespectUISPr("label.lieferschein"));
		wcbRechnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.rechnung.modulname"));
		wrbAnhaengen = new WrapperRadioButton(LPMain.getInstance().getTextRespectUISPr("lp.mandant.agb.anhaengen"));
		wrbMitdrucken = new WrapperRadioButton(LPMain.getInstance().getTextRespectUISPr("lp.mandant.agb.mitdrucken"));

		bg.add(wrbAnhaengen);
		bg.add(wrbMitdrucken);

		wrbAnhaengen.setSelected(true);

		wlaPDF = new WrapperLabel("PDF");
		wlaPDF.setHorizontalAlignment(SwingConstants.LEFT);
		// ab hier einhaengen.

		// Zeile.

		iZeile++;
		jpaWorkingOn.add(wlaAGBs, new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 10, 2), 80, 0));

		jpaWorkingOn.add(wrbAnhaengen, new GridBagConstraints(1, iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 10, 2), 0, 0));
		jpaWorkingOn.add(wrbMitdrucken, new GridBagConstraints(2, iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 10, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBeiBelegen, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbAnfrage, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbBestellung, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbAngebot, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbAuftrag, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbLieferschein, new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wcbRechnung, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPDF, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));
		jpaWorkingOn.add(wpdPDF, new GridBagConstraints(1, iZeile, 2, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(1, 2, 1, 2), 0, 0));

	}

	protected void dto2Components() throws Throwable {

		if (Helper.short2boolean(getMandantDto().getBAgbAnhang())) {
			wrbAnhaengen.setSelected(true);
		} else {
			wrbMitdrucken.setSelected(true);
		}

		wcbAnfrage.setShort(getMandantDto().getBAgbAnfrage());
		wcbAngebot.setShort(getMandantDto().getBAgbAngebot());
		wcbAuftrag.setShort(getMandantDto().getBAgbAuftrag());
		wcbBestellung.setShort(getMandantDto().getBAgbBestellung());
		wcbLieferschein.setShort(getMandantDto().getBAgbLieferschein());
		wcbRechnung.setShort(getMandantDto().getBAgbRechnung());

		wpdPDF.setPdf(DelegateFactory.getInstance().getMandantDelegate().getAGBs_PDF(LPMain.getTheClient().getLocUi()));

	}

	protected void components2Dto() throws Throwable {

		getMandantDto().setBAgbAnhang(wrbAnhaengen.getShort());
		getMandantDto().setBAgbAnfrage(wcbAnfrage.getShort());
		getMandantDto().setBAgbAngebot(wcbAngebot.getShort());
		getMandantDto().setBAgbAuftrag(wcbAuftrag.getShort());
		getMandantDto().setBAgbBestellung(wcbBestellung.getShort());
		getMandantDto().setBAgbLieferschein(wcbLieferschein.getShort());
		getMandantDto().setBAgbRechnung(wcbRechnung.getShort());

		// getMandantDto().getPartnerDto().setOBild(wpdPDF.getImageOriginalBytes());

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(true);

		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().getMandant(),
				getMandantDto().getCNr());

		wpdPDF.setPdf(null);

		if (!bNeedNoYouAreSelectedI) {

			String cNr = getMandantDto().getCNr();

			if (cNr == null) {
				throw new Exception("key == null");
			}

			getInternalFrameSystem()
					.setMandantDto(DelegateFactory.getInstance().getMandantDelegate().mandantFindByPrimaryKey(cNr));

			initPanel();

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameSystem().getMandantDto().getCNr());

			setStatusbar();
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getMandantDelegate().updateMandant(getMandantDto());

			DelegateFactory.getInstance().getMandantDelegate().updateAGBs_PDF(wpdPDF.getPdf());

			super.eventActionSave(e, true);

			eventYouAreSelected(false);

			dto2Components();
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_MANDANT;
	}

	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAnlegen(getMandantDto().getIAnlegen());
		setStatusbarPersonalIIdAendern(getMandantDto().getIAendern());
		setStatusbarTAendern(getMandantDto().getTAendern());
		setStatusbarTAnlegen(getMandantDto().getTAnlegen());
	}

	protected MandantDto getMandantDto() {
		return getInternalFrameSystem().getMandantDto();
	}

	private InternalFrameSystem getInternalFrameSystem() {
		return (InternalFrameSystem) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent aE, boolean bNeedNoRefreshI) throws Throwable {

		super.eventActionRefresh(aE, bNeedNoRefreshI);
		this.checkMandantLoggedInEqualsMandantSelected(LPMain.getInstance().getTheClient().getMandant(),
				getMandantDto().getCNr());
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

			item.getButton().setEnabled(false);
			getPanelStatusbar().setLockField(LPMain.getInstance().getTextRespectUISPr("system.nurleserecht"));
		}
	}

}
