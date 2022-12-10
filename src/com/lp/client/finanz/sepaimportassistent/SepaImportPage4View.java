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
package com.lp.client.finanz.sepaimportassistent;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.lp.client.finanz.InternalFrameFinanz;
import com.lp.client.finanz.sepaimportassistent.SepaImportModel.SearchResultTableModel;
import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.ISepaImportResult;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

public class SepaImportPage4View extends AssistentPageView implements IManuelleAuswahlListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String TABLEPANEL = "TABLEPANEL";
	private static final String MANUELLEAUSWAHLPANEL = "MANUELLEAUSWAHLPANEL";
	private static final String ACTION_REFRESH_SALDEN = "action_refresh_salden";

	private SepaImportPage4Ctrl controller;
	
	private JPanel panelTable;
	private JTable tableResults;
	private CardLayout cardLayout;
	private TabbedPaneBelegZahlung tabbedPaneManuelleAuswahl;
	private JPanel panelSalden;
	private WrapperLabel wlKtoauszugAnfangsSaldo;
	private WrapperTextField wtKtoauszugAnfangsSaldo;
	private WrapperLabel wlKtoauszugEndSaldo;
	private WrapperTextField wtKtoauszugEndSaldo;
	private WrapperLabel wlBankkontoSaldo;
	private WrapperTextField wtBankkontoSaldo;
	private WrapperLabel wlErwarteteterEndSaldo;
	private WrapperTextField wtErwarteteterEndSaldo;
	private WrapperLabel wlWaehrung1;
	private WrapperLabel wlWaehrung2;
	private WrapperLabel wlWaehrung3;
	private WrapperLabel wlWaehrung4;
	private WrapperButton wbuRefresh;
	private TableModel tableModel;
	
	public SepaImportPage4View(SepaImportPage4Ctrl controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
		controller.setManuelleAuswahlListener(this);
		cardLayout = new CardLayout();
	}

	@Override
	public void dataUpdated() {
		initTableModel();
		if (getController().getImportException() != null) {
			getInternalFrame().handleException(getController().getImportException(), true);
		}
//		tableResults.setModel(getController().getSearchResultsTableModel());
		fitColumnsToContentWidth();
		tableResults.setVisible(!getController().isBusyImporting());
		wtBankkontoSaldo.setText(Helper.formatZahl(getController().getBankkontoSaldo(), FinanzFac.NACHKOMMASTELLEN,  
				LPMain.getInstance().getUISprLocale()));
		wtErwarteteterEndSaldo.setText(Helper.formatZahl(getController().getErwarteterEndSaldo(), FinanzFac.NACHKOMMASTELLEN,  
				LPMain.getInstance().getUISprLocale()));
		if (getController().getErwarteterEndSaldo().compareTo(getController().getEndSaldoBankKtoauszug()) != 0) {
			wtErwarteteterEndSaldo.setBackground(new Color(255,64,64));
		} else {
			wtErwarteteterEndSaldo.setBackground(new Color(150, 255, 150));
		}
	}

	private void initTableModel() {
		if (tableModel == null) {
			tableModel = getController().getSearchResultsTableModel();
			tableResults.setModel(tableModel);
		}
	}
	@Override
	public SepaImportPage4Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("fb.sepa.import.sepakontoauszug") + " - " 
				+ LPMain.getTextRespectUISPr("fb.sepa.import.suchergebnis");
	}

	@Override
	protected void initViewImpl() {
		panelTable = new JPanel();

		tableResults = new JTable() {
			private static final long serialVersionUID = 8962370711608570656L;

			@Override
			public String getToolTipText(MouseEvent e) {
				int columnIndex = columnAtPoint(e.getPoint());
				if (SepaImportModel.SearchResultTableModel.IDX_TREFFER == columnIndex) {
					return getController().getToolTipText(rowAtPoint(e.getPoint()));
				}
				return null;
			}

			@Override
			public boolean getScrollableTracksViewportWidth() {
				return getPreferredSize().width < getParent().getWidth();
			}
			
			@Override
			protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
					private static final long serialVersionUID = -2974983629707778142L;

					public String getToolTipText(MouseEvent e) {
                        Point point = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(point.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        return ((SearchResultTableModel)tableModel).getColumnNameToolTip(realIndex);
                    }
                };
			}
		};
		tableResults.setDefaultRenderer(Object.class, new SearchResultTableCellRenderer(controller));
		tableResults.setDefaultRenderer(Boolean.class, new SearchResultTableCellRendererCheckbox());
		tableResults.setDefaultEditor(ISepaImportResult.class, new SearchResultTableCellEditorChooser(controller));
		tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableResults.setPreferredScrollableViewportSize(Toolkit.getDefaultToolkit().getScreenSize());
		
		wlBankkontoSaldo = new WrapperLabel();
		wlBankkontoSaldo.setText(getController().getSepakonto().getTextSuchergebnis());
		wtBankkontoSaldo = new WrapperTextField();
		wtBankkontoSaldo.setEditable(false);
		wtBankkontoSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		wtBankkontoSaldo.setVisible(getController().hasFibu());
		
		wlErwarteteterEndSaldo = new WrapperLabel();
		wlErwarteteterEndSaldo.setText(LPMain.getMessageTextRespectUISPr(
				"fb.sepa.import.erwarteterendsaldo", new Object[]{getController().getKontonummerBank()}) 
				+ " " + getController().getAuszugsnummer());
		wtErwarteteterEndSaldo = new WrapperTextField();
		wtErwarteteterEndSaldo.setEditable(false);
		wtErwarteteterEndSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		wlWaehrung1 = new WrapperLabel(getController().getWaehrung());
		wlWaehrung2 = new WrapperLabel(getController().getWaehrung());
		wlWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlWaehrung1.setVisible(getController().hasFibu());
		wlWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wbuRefresh = new WrapperButton(IconFactory.getRefresh());
		wbuRefresh.setActionCommand(ACTION_REFRESH_SALDEN);
		wbuRefresh.addActionListener(this);
		wbuRefresh.setVisible(getController().hasFibu());
		
		if (controller.hasSalden()) {
			initPanelSalden();
		} else {
			initPanelSaldenWithoutKontoauszug();
		}

		panelTable.setLayout(new MigLayout("wrap 1, hidemode 2, fill", "[fill, grow]", "[fill|fill, grow|10%,fill]"));
		panelTable.add(new JScrollPane(tableResults,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), "newline");
		panelTable.add(panelSalden);
		
		try {
			tabbedPaneManuelleAuswahl = 
					new TabbedPaneBelegZahlung((InternalFrameFinanz) getInternalFrame(), this, getController());
		} catch (Throwable e) {
			getInternalFrame().handleException(e, true);
			cancelAssistent();
		}
		setLayout(cardLayout);
		add(panelTable, TABLEPANEL);
		add(tabbedPaneManuelleAuswahl, MANUELLEAUSWAHLPANEL);
		
		cardLayout.show(this, TABLEPANEL);
	}
	
	private void cancelAssistent() {
		try {
			controller.cancelAssistent();
		} catch (Throwable e) {
			getInternalFrame().handleException(e, true);
		}
		
	}

	private void initPanelSaldenWithoutKontoauszug() {
		panelSalden = new JPanel();
		panelSalden.setLayout(new MigLayout("wrap 8, hidemode 2", "[25%,fill|10%,fill|45px,fill|30%,fill|25%,fill|10%,fill|45px,fill|40px,fill"));
		panelSalden.add(wlBankkontoSaldo, "skip 4");
		panelSalden.add(wtBankkontoSaldo);
		panelSalden.add(wlWaehrung1, "align left");
		panelSalden.add(wlErwarteteterEndSaldo, "skip 4");
		panelSalden.add(wtErwarteteterEndSaldo);
		panelSalden.add(wlWaehrung2, "align left");
		panelSalden.add(wbuRefresh, "span 1 2");
		
	}

	/**
	 * 
	 */
	public void initPanelSalden() {
		wlKtoauszugAnfangsSaldo = new WrapperLabel();
		wlKtoauszugAnfangsSaldo.setText(LPMain.getMessageTextRespectUISPr(
				"fb.sepa.import.anfangssaldokontoauszug") + " " + getController().getAuszugsnummer());
		wtKtoauszugAnfangsSaldo = new WrapperTextField();
		wtKtoauszugAnfangsSaldo.setEditable(false);
		wtKtoauszugAnfangsSaldo.setText(Helper.formatZahl(getController().getAnfangsSaldoBankKtoauszug(), 
				FinanzFac.NACHKOMMASTELLEN,  
				LPMain.getInstance().getUISprLocale()));
		wtKtoauszugAnfangsSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wlKtoauszugEndSaldo = new WrapperLabel();
		wlKtoauszugEndSaldo.setText(LPMain.getMessageTextRespectUISPr(
				"fb.sepa.import.endsaldokontoauszug") 
				+ " " + getController().getAuszugsnummer());
		wlBankkontoSaldo.setVisible(getController().hasFibu());
		wtKtoauszugEndSaldo = new WrapperTextField();
		wtKtoauszugEndSaldo.setEditable(false);
		wtKtoauszugEndSaldo.setText(Helper.formatZahl(getController().getEndSaldoBankKtoauszug(), 
				FinanzFac.NACHKOMMASTELLEN,  
				LPMain.getInstance().getUISprLocale()));
		wtKtoauszugEndSaldo.setHorizontalAlignment(SwingConstants.RIGHT);

		wlWaehrung3 = new WrapperLabel(getController().getWaehrung());
		wlWaehrung4 = new WrapperLabel(getController().getWaehrung());
		wlWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlWaehrung4.setHorizontalAlignment(SwingConstants.LEFT);
		
		panelSalden = new JPanel();
		panelSalden.setLayout(new MigLayout("wrap 8, hidemode 2", "[25%,fill|10%,fill|45px,fill|30%,fill|25%,fill|10%,fill|45px,fill|40px,fill]"));
		panelSalden.add(wlKtoauszugAnfangsSaldo);
		panelSalden.add(wtKtoauszugAnfangsSaldo);
		panelSalden.add(wlWaehrung3, "align left");
		panelSalden.add(wlBankkontoSaldo, "span 2, align right");
		panelSalden.add(wtBankkontoSaldo);
		panelSalden.add(wlWaehrung1, "align left");
		panelSalden.add(wbuRefresh, "span 1 2");
		
		panelSalden.add(wlKtoauszugEndSaldo, "newline");
		panelSalden.add(wtKtoauszugEndSaldo);
		panelSalden.add(wlWaehrung4, "align left");
		panelSalden.add(wlErwarteteterEndSaldo, "span 2, align right");
		panelSalden.add(wtErwarteteterEndSaldo);
		panelSalden.add(wlWaehrung2, "align left");
	}

	private void fitColumnsToContentWidth() {
		TableColumn tableColumn = tableResults.getColumn(tableResults.getColumnName(SearchResultTableModel.IDX_BUCHUNGSDATUM));
		tableColumn.setPreferredWidth(100);
		tableColumn.setMaxWidth(100);
		tableColumn.setMinWidth(80);
		tableColumn = tableResults.getColumn(tableResults.getColumnName(SearchResultTableModel.IDX_ERLEDIGT));
		tableColumn.setPreferredWidth(20);
		tableColumn.setMaxWidth(20);
		tableColumn.setMinWidth(20);
		tableColumn = tableResults.getColumn(tableResults.getColumnName(SearchResultTableModel.IDX_UEBERNAHME));
		tableColumn.setPreferredWidth(20);
		tableColumn.setMaxWidth(20);
		tableColumn.setMinWidth(20);
		if (!getController().hasFibu()) {
			tableColumn = tableResults.getColumn(tableResults.getColumnName(SearchResultTableModel.IDX_IGNORIEREN));
			tableColumn.setPreferredWidth(20);
			tableColumn.setMaxWidth(20);
			tableColumn.setMinWidth(20);
		}
		SepaImportPage3View.fitToContentWidth(tableResults, SearchResultTableModel.IDX_ZAHLUNG);
//		fitToContentWidth(tableResults, SearchResultTableModel.IDX_BUCHUNGSDATUM);
		SepaImportPage3View.fitToContentWidth(tableResults, SearchResultTableModel.IDX_BETRAG);
		SepaImportPage3View.fitToContentWidth(tableResults, SearchResultTableModel.IDX_TREFFER);
		SepaImportPage3View.fitToContentWidth(tableResults, SearchResultTableModel.IDX_RESTBETRAG);
//		fitToContentWidth(tableResults, SearchResultTableModel.IDX_ERLEDIGT);
//		fitToContentWidth(tableResults, SearchResultTableModel.IDX_UEBERNAHME);
	}

	@Override
	public void waehleBelegzahlungManuell() {
		getController().setPageNavigationAllowed(false);
		tabbedPaneManuelleAuswahl.manuelleAuswahlSelected();
		
		if (getController().getResultWaitingForManuelleAuswahl().getPayment().getBetrag().isHaben()) {
			tabbedPaneManuelleAuswahl.setSelectedIndex(TabbedPaneBelegZahlung.IDX_EINGANGSRECHNUNG);
		} else {
			tabbedPaneManuelleAuswahl.setSelectedIndex(TabbedPaneBelegZahlung.IDX_AUSGANGSRECHNUNG);
		}

		cardLayout.show(this, MANUELLEAUSWAHLPANEL);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == null) return;
		
		if (ACTION_REFRESH_SALDEN.equals(e.getActionCommand())) {
			getController().berechneSaldoBankkontoHV();
			dataUpdated();
			
		} else if (e.getActionCommand().startsWith(TabbedPaneBelegZahlung.BUTTON_ACTION_ZAHLUNG_UEBERNEHMEN)) {
			getController().actionManuelleAuswahlUebernehmen();
			cardLayout.show(this, TABLEPANEL);
			getController().setPageNavigationAllowed(true);
			
		} else if (e.getActionCommand().startsWith(TabbedPaneBelegZahlung.BUTTON_ACTION_ZURUECK_AUS_MANUELLER_AUSWAHL)) {
			if(!getController().hasResultWaitingForManuelleAuswahlChanges() || 
					DialogFactory.showModalJaNeinDialog(getInternalFrame(), 
							LPMain.getTextRespectUISPr("fb.sepa.warnung.aenderungenverwerfen"))) {
				getController().actionManuelleAuswahlZurueck();
				cardLayout.show(this, TABLEPANEL);
				getController().setPageNavigationAllowed(true);
			}
		}
		
	}

}
