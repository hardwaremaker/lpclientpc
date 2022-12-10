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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimestampField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelTruTopsDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.ArtikelTruTopsId;
import com.lp.server.util.HvOptional;

public class PanelArtikelTrumpf extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;

	private WrapperLabel wlaBoostPfad = new WrapperLabel();
	private WrapperTextField wtfBoostPfad = new WrapperTextField();

	private WrapperLabel wlaExportPfad = new WrapperLabel();
	private WrapperTextField wtfExportPfad = new WrapperTextField(1024);

	private WrapperLabel wlaFehlercode = new WrapperLabel();
	private WrapperTextField wtfFehlercode = new WrapperTextField();

	private WrapperLabel wlaFehlertext = new WrapperLabel();
	private WrapperTextArea wtfFehlertext = new WrapperTextArea();

	private WrapperLabel wlaExportBeginn = new WrapperLabel();
	private WrapperTimestampField wdfExportBeginn = new WrapperTimestampField();

	private WrapperLabel wlaExportEnde = new WrapperLabel();
	private WrapperTimestampField wdfExportEnde = new WrapperTimestampField();

	private JScrollPane metadaten = new JScrollPane();
	JList list = null;

	public final static String MY_OWN_NEW_RESET = PanelBasis.ACTION_MY_OWN_NEW + "SPR_RESET";

	TabbedPaneArtikel tpArtikel = null;

	private HvOptional<ArtikelTruTopsDto> artikelTruTopsDto = null;

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	public PanelArtikelTrumpf(InternalFrame internalFrame, TabbedPaneArtikel tpArtikel, String add2TitleI,
			Integer belegartIId) throws Throwable {
		super(internalFrame, add2TitleI, belegartIId);
		this.tpArtikel = tpArtikel;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (MY_OWN_NEW_RESET.equals(e.getActionCommand()) && !artikelTruTopsDto.isEmpty()) {
			DelegateFactory.getInstance().getArtikelDelegate()
					.resetArtikelTruTops(new ArtikelTruTopsId(artikelTruTopsDto.get().getIId()));

			eventYouAreSelected(false);
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		leereAlleFelder(this);

		list.removeAll();
		list.setListData(new Object[0]);

		artikelTruTopsDto = DelegateFactory.getInstance().getArtikelDelegate().artikelTruTopsFindByArtikelId(
				new ArtikelId(tpArtikel.getInternalFrameArtikel().getArtikelDto().getIId()));

		super.eventYouAreSelected(false);
		dto2Components();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaExportPfad.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportpfad"));
		wlaFehlercode.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlercode"));
		wlaFehlertext.setText(LPMain.getTextRespectUISPr("artikel.trumpf.fehlertext"));
		wlaExportBeginn.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportbeginn"));
		wlaExportEnde.setText(LPMain.getTextRespectUISPr("artikel.trumpf.exportende"));

		wlaBoostPfad.setText(LPMain.getTextRespectUISPr("artikel.trumpf.boostpfad"));
		wtfBoostPfad = new WrapperTextField(1024);

		wtfExportPfad.setActivatable(false);
		wtfFehlercode.setActivatable(false);
		wtfFehlertext.setActivatable(false);

		wtfFehlertext.setRows(3);

		
		JScrollPane jspScrollPaneFehlertext = new JScrollPane();
		
		wdfExportBeginn.setActivatable(false);
		wdfExportEnde.setActivatable(false);

		list = new JList();
		Color defaultCellForegroundColor = UIManager.getColor("Table.foreground");
		list.setSelectionForeground(defaultCellForegroundColor);
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		metadaten.getViewport().add(list);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBoostPfad, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(15, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBoostPfad, new GridBagConstraints(1, iZeile, 3, 1, 0.1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(15, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(new JSeparator(JSeparator.HORIZONTAL), new GridBagConstraints(0, iZeile, 4, 1, 0.1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		iZeile++;

		jpaWorkingOn.add(wlaExportPfad, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfExportPfad, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaExportBeginn, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wdfExportBeginn, new GridBagConstraints(1, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		jpaWorkingOn.add(wlaExportEnde, new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wdfExportEnde, new GridBagConstraints(3, iZeile, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 200, 0));

		iZeile++;

		jpaWorkingOn.add(wlaFehlercode, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfFehlercode, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaFehlertext, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(jspScrollPaneFehlertext, new GridBagConstraints(1, iZeile, 3, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jspScrollPaneFehlertext.getViewport().add(wtfFehlertext, null);
		iZeile++;

		jpaWorkingOn.add(metadaten, new GridBagConstraints(0, iZeile, 4, 1, 1, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		
		getToolBar().addButtonRight("/com/lp/client/res/garbage.png",
				LPMain.getTextRespectUISPr("artikel.trumpf.zuruecksetzen"), MY_OWN_NEW_RESET, null, null);
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void eventActionNext(boolean next) throws Throwable {
		super.eventActionNext(next);
	}

	protected void dto2Components() throws Throwable {

		if (artikelTruTopsDto.isPresent()) {
			wtfExportPfad.setText(artikelTruTopsDto.get().getCExportPfad());
			wtfFehlercode.setText(artikelTruTopsDto.get().getCFehlercode());
			wtfFehlertext.setText(artikelTruTopsDto.get().getCFehlertext());

			wdfExportBeginn.setTimestamp(artikelTruTopsDto.get().getTExportBeginn());
			wdfExportEnde.setTimestamp(artikelTruTopsDto.get().getTExportEnde());

			wtfBoostPfad.setText(artikelTruTopsDto.get().getCPfad());

			ArrayList<String> zeilen = DelegateFactory.getInstance().getArtikelDelegate()
					.getTruTopsMetadaten(new ArtikelId(artikelTruTopsDto.get().getArtikelIId()));

			Object[] tempZeilen = new Object[zeilen.size()];

			for (int i = 0; i < zeilen.size(); i++) {
				tempZeilen[i] = zeilen.get(i);
			}

			list.setListData(tempZeilen);

		}
	}

	protected void components2Dto() throws ExceptionLP {
		if (artikelTruTopsDto.isPresent()) {
			artikelTruTopsDto.get().setCPfad(wtfBoostPfad.getText());
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (artikelTruTopsDto.isPresent()) {
				DelegateFactory.getInstance().getArtikelDelegate().updateArtikelTruTops(artikelTruTopsDto.get());
			} else {

				ArtikelTruTopsDto dto = new ArtikelTruTopsDto();
				dto.setCPfad(wtfBoostPfad.getText());
				dto.setArtikelIId(tpArtikel.getInternalFrameArtikel().getArtikelDto().getIId());
				DelegateFactory.getInstance().getArtikelDelegate().createArtikelTruTops(dto);
			}

		}
		super.eventActionSave(e, true);
	}
}
