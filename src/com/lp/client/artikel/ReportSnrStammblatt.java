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
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperSNRField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelReportFac;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
public class ReportSnrStammblatt extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperSNRField wtfSeriennr = new WrapperSNRField();
	private Integer lagerIId = null;
	private Integer artikelIId = null;

	private WrapperLabel wlaSeriennr = new WrapperLabel();
	private WrapperCheckBox wcbSnrWildcard = new WrapperCheckBox();

	private WrapperCheckBox wcbNurSeriennummern = new WrapperCheckBox();
	private WrapperCheckBox wcbNurObersteEbene = new WrapperCheckBox();

	private WrapperButton wbuArtikel = new WrapperButton();
	private WrapperTextField wtfArtikel = new WrapperTextField();

	private WrapperSNRField wtfSnrWildcard = new WrapperSNRField();

	private WrapperLabel wlaVersionWildcard = new WrapperLabel();
	private WrapperTextField wtfVersionWildcard = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRLager = null;
	public static final String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	public static final String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	private PanelQueryFLR panelQueryFLRArtikel = null;

	private WrapperRadioButton wrbSortIdent = new WrapperRadioButton();
	private WrapperRadioButton wrbSortSnr = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperLabel wlaSortierung = new WrapperLabel();

	public ReportSnrStammblatt(InternalFrameArtikel internalFrame,
			String add2Title, Integer artikelIId) throws Throwable {
		super(internalFrame, add2Title);
		// this.artikelIId = artikelIId;
		LPMain.getInstance()
				.getTextRespectUISPr("artikel.report.snrstammblatt");
		jbInit();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuLager;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));

		wlaSeriennr.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.seriennrchargennr"));
		wcbSnrWildcard.setText("oder Serien/Chargennr enth\u00E4lt");
		wcbSnrWildcard
				.addActionListener(new ReportSnrStammblatt_wcbSnrWildcard_actionAdapter(
						this));
		wcbSnrWildcard.setHorizontalAlignment(SwingConstants.RIGHT);
		wtfLager.setEditable(false);
		wtfSnrWildcard.setEditable(false);
		wtfVersionWildcard.setEditable(false);

		wbuLager.setActionCommand(this.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wtfSeriennr.setMandatoryField(true);

		wtfArtikel.setEditable(false);
		wtfArtikel.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		/*
		 * if (artikelIId != null) { ArtikelDto artikelDto =
		 * DelegateFactory.getInstance()
		 * .getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);
		 * 
		 * wtfArtikel.setText(artikelDto.formatArtikelbezeichnung()); }
		 */
		wbuArtikel.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.artikel"));
		wbuArtikel.setActionCommand(this.ACTION_SPECIAL_ARTIKEL_FROM_LISTE);
		wbuArtikel.addActionListener(this);

		wlaSortierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.sortierung"));

		wlaVersionWildcard.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.seriennummern.version.filter"));

		wcbNurObersteEbene.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.seriennummernstammblatt.nurobersteebene"));
		wcbNurSeriennummern.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.seriennummernstammblatt.nurseriennummern"));

		wrbSortIdent.setSelected(true);
		wrbSortIdent.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artikelnummer"));
		wrbSortSnr.setText(LPMain.getInstance().getTextRespectUISPr(
				"bes.seriennummer_short"));
		buttonGroupSortierung.add(wrbSortIdent);
		buttonGroupSortierung.add(wrbSortSnr);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 0, 3, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 0, 1, 1, 0.8, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfArtikel, new GridBagConstraints(1, 1, 3, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuArtikel, new GridBagConstraints(0, 1, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfSeriennr, new GridBagConstraints(1, 2, 2, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSeriennr, new GridBagConstraints(0, 2, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSnrWildcard, new GridBagConstraints(1, 3, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbSnrWildcard, new GridBagConstraints(0, 3, 1, 1,
				0.1, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVersionWildcard, new GridBagConstraints(2, 3, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersionWildcard, new GridBagConstraints(3, 3, 1, 1,
				0.6, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, 4, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbSortIdent, new GridBagConstraints(1, 4, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 200, 0));
		
		jpaWorkingOn.add(wcbNurSeriennummern, new GridBagConstraints(3, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		

		jpaWorkingOn.add(wrbSortSnr, new GridBagConstraints(1, 5, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbNurObersteEbene, new GridBagConstraints(3, 5, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ArtikelFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ArtikelReportFac.REPORT_SNRSTAMMBLATT;
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance()
				.createPanelFLRLager(getInternalFrame(), lagerIId, true, false);

		new DialogQuery(panelQueryFLRLager);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);
				wtfLager.setText(lagerDto.getCNr());
				lagerIId = lagerDto.getIId();
				wtfLager.setText(lagerDto.getCNr());
			} else if (e.getSource() == panelQueryFLRArtikel) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtikelDto artikelDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artikelFindByPrimaryKey((Integer) key);
				artikelIId = artikelDto.getIId();
				wtfArtikel.setText(artikelDto.formatArtikelbezeichnung());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLager) {
				wtfLager.setText(null);
				lagerIId = null;
			}
			if (e.getSource() == panelQueryFLRArtikel) {
				wtfArtikel.setText(null);
				artikelIId = null;
			}

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ARTIKEL_FROM_LISTE)) {
			dialogQueryArtikelFromListe(e);
		}
	}

	void dialogQueryArtikelFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(getInternalFrame(), true);

		new DialogQuery(panelQueryFLRArtikel);
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		return DelegateFactory
				.getInstance()
				.getArtikelReportDelegate()
				.printSeriennummernStammblatt(
						lagerIId,
						artikelIId,
						wtfSeriennr.erzeugeSeriennummernArray(
								new BigDecimal(0), false),
						wrbSortIdent.isSelected(),

						wtfSnrWildcard.getText(), wtfVersionWildcard.getText(),
						wcbNurSeriennummern.isSelected(),
						wcbNurObersteEbene.isSelected());

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	public void wcbSnrWildcard_actionPerformed(ActionEvent e) {
		if (wcbSnrWildcard.isSelected()) {
			wtfSnrWildcard.setEditable(true);
			wtfSnrWildcard.setText("");

			wtfSeriennr.setEditable(false);
			wtfSeriennr.setMandatoryField(false);
			wtfSeriennr.setText("");

			wtfVersionWildcard.setText("");
			wtfVersionWildcard.setEditable(true);
		} else {
			wtfSnrWildcard.setEditable(false);
			wtfSnrWildcard.setText("");

			wtfSeriennr.setEditable(true);
			wtfSeriennr.setMandatoryField(true);
			wtfSeriennr.setText("");

			wtfVersionWildcard.setText("");
			wtfVersionWildcard.setEditable(false);

		}
	}
}

class ReportSnrStammblatt_wcbSnrWildcard_actionAdapter implements
		ActionListener {
	private ReportSnrStammblatt adaptee;

	ReportSnrStammblatt_wcbSnrWildcard_actionAdapter(ReportSnrStammblatt adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbSnrWildcard_actionPerformed(e);
	}
}
