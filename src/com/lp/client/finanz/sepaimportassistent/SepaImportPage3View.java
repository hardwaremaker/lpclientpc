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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.assistent.view.AssistentPageView;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.util.Helper;

public class SepaImportPage3View extends AssistentPageView {
	
	private static final long serialVersionUID = 1L;
	
	private SepaImportPage3Ctrl controller;
	
	private WrapperLabel wlStatementNumber;
	private WrapperTextField wtfStatementNumber;
	private WrapperLabel wlBank;
	private WrapperTextField wtfBank;
	private WrapperLabel wlCreationDate;
	private WrapperTextField wtfCreationDate;
	private WrapperLabel wlOpeningSaldo;
	private WrapperTextField wtfOpeningSaldo;
	private WrapperLabel wlClosingSaldo;
	private WrapperTextField wtfClosingSaldo;
	private WrapperLabel wlWaehrung1;
	private WrapperLabel wlWaehrung2;
	private WrapperLabel wlErrorOpeningSaldo;
	
	private JTable jTableOverview;
	private JScrollPane jPanelTable;
	
	private DefaultTableCellRenderer rightRenderer;

	public SepaImportPage3View(SepaImportPage3Ctrl controller,
			InternalFrame iFrame) {
		super(controller, iFrame);
		this.controller = controller;
	}

	@Override
	public void dataUpdated() {
//		String sStmtNumber = wtfStatementNumber.getText();
//		if (sStmtNumber == null || sStmtNumber.isEmpty() || !getController().isValueAsStatementNumberValid(sStmtNumber)) {
//			wtfStatementNumber.setBackground(new Color(255, 64, 64));
//			getController().setStatementNumber(null);
//		} else {
//			wtfStatementNumber.setBackground(Color.WHITE);
//			getController().setStatementNumber(sStmtNumber);
//		}
		if (getController().getThrowable() != null) {
			getInternalFrame().handleException(getController().getThrowable(), true);
			return;
		}
		
		if (!getController().isAnfangssaldoOk()) {
			wtfOpeningSaldo.setBackground(new Color(255,64,64));
			wlErrorOpeningSaldo.setText(getController().getSepakonto().getTextUebersicht());
		}
	}

	@Override
	public SepaImportPage3Ctrl getController() {
		return controller;
	}

	@Override
	public String getTitle() {
		return LPMain.getTextRespectUISPr("fb.sepa.import.sepakontoauszug");
	}

	@Override
	protected void initViewImpl() {
		wlBank = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.bank"));
		wtfBank = new WrapperTextField();
		wtfBank.setText(getController().getSelectedBankverbindungName());
		wtfBank.setEditable(false);
		wtfBank.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wlStatementNumber = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.auszugsnummer"));
		wtfStatementNumber = new WrapperTextField();
		wtfStatementNumber.setEditable(false);
		wtfStatementNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		wtfStatementNumber.setText(getController().getStatementNumber());
//		wtfStatementNumber = new JTextField();
//		wtfStatementNumber.setText(getController().getStatementNumber());
//		wtfStatementNumber.setEditable(true);
//		wtfStatementNumber.setHorizontalAlignment(SwingConstants.RIGHT);
//		wtfStatementNumber.setBorder(Defaults.getInstance().getMandatoryFieldBorder());
//		wtfStatementNumber.setPreferredSize(wtfBank.getPreferredSize());
//		wtfStatementNumber.setMinimumSize(wtfBank.getMinimumSize());
//		wtfStatementNumber.setMaximumSize(wtfBank.getMaximumSize());
//		wtfStatementNumber.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//					getController().viewDataChanged();
//			}
//		});;
		
		wlCreationDate = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.erstellungsdatum"));
		wtfCreationDate = new WrapperTextField();
		wtfCreationDate.setText(getController().getCreationDate());
		wtfCreationDate.setEditable(false);
		wtfCreationDate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		wlOpeningSaldo = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.anfangssaldo"));
		wtfOpeningSaldo = new WrapperTextField();
		wlClosingSaldo = new WrapperLabel(LPMain.getTextRespectUISPr("fb.sepa.import.endsaldo"));
		wtfClosingSaldo = new WrapperTextField();

		if (controller.hasSalden()) {
			wtfOpeningSaldo.setText(Helper.formatZahl(getController().getAnfangsSaldo(), 
					FinanzFac.NACHKOMMASTELLEN,  
					LPMain.getInstance().getUISprLocale()));
			wtfOpeningSaldo.setEditable(false);
			wtfOpeningSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
			wtfClosingSaldo.setText(Helper.formatZahl(getController().getEndSaldo(), 
					FinanzFac.NACHKOMMASTELLEN,  
					LPMain.getInstance().getUISprLocale()));
			wtfClosingSaldo.setEditable(false);
			wtfClosingSaldo.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		
		wlWaehrung1 = new WrapperLabel(getController().getWaehrung());
		wlWaehrung2 = new WrapperLabel(getController().getWaehrung());
		wlErrorOpeningSaldo = new WrapperLabel();
		wlErrorOpeningSaldo.setHorizontalAlignment(SwingConstants.LEFT);
		wlErrorOpeningSaldo.setForeground(new Color(255,64,64));
		
		rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		
		jTableOverview = new JTable() {
			private static final long serialVersionUID = 1L;

			// fuer horizontale Scrollbar falls von der Breite her notwendig
			@Override
			public boolean getScrollableTracksViewportWidth() {
			   return getRowCount() == 0 ? super.getScrollableTracksViewportWidth() 
			           : getPreferredSize().width < getParent().getWidth();
			}
		};
		jTableOverview.setModel(getController().getOverviewTableModel());
		jTableOverview.getColumnModel().getColumn(StatementOverviewTableModel.Columns.BETRAG)
			.setCellRenderer(rightRenderer);
		jTableOverview.setDefaultRenderer(MultilineText.class, new MultilineTextTableCellRenderer(160));
		
		fitToContentWidth(jTableOverview,StatementOverviewTableModel.Columns.NAME);
		fitToContentWidth(jTableOverview,StatementOverviewTableModel.Columns.BETRAG);
		fitToContentWidth(jTableOverview,StatementOverviewTableModel.Columns.IBAN);
		fitToContentWidth(jTableOverview,StatementOverviewTableModel.Columns.ZAHLDATUM);
		fitToContentWidth(jTableOverview,StatementOverviewTableModel.Columns.REFERENZTEXT);
		
		jPanelTable = new JScrollPane(jTableOverview);
		
		setLayout(new MigLayout("wrap 8, hidemode 3", 
				"[10%,fill|20%,fill|20%,fill|15%,fill|37px,fill|20%,fill|15%,fill|37px,fill]", 
				"[5%,fill|5%,fill|5%,fill|90%,fill]"));
		add(wlBank);
		add(wtfBank);
		add(wlStatementNumber);
		add(wtfStatementNumber);
		add(wlCreationDate, "skip 1");
		add(wtfCreationDate);
		
		if (controller.hasSalden()) {
			add(wlOpeningSaldo, "newline, skip 2");
			add(wtfOpeningSaldo);
			add(wlWaehrung1, "align left");
			add(wlClosingSaldo);
			add(wtfClosingSaldo);
			add(wlWaehrung2, "align left");
			add(wlErrorOpeningSaldo, "skip 3, span 3, align left");
			add(jPanelTable, "newline, span, gaptop 10px");
		} else {
			add(jPanelTable, "cell 0 2, newline, span, gaptop 5%");
		}

	}
	
	public static void fitToContentWidth(JTable table, int column) {
		 int width = 0;
		 for (int row = 0; row < table.getRowCount(); ++row) {
			  Object cellValue = table.getValueAt(row, column);
			  TableCellRenderer renderer = table.getCellRenderer(row, column);
			  Component comp = renderer.getTableCellRendererComponent(table, cellValue, false, false, row, column);
			  width = Math.max(width, comp.getPreferredSize().width);
		 }
		 TableColumn tableColumn = table.getColumn(table.getColumnName(column));
		 width += table.getIntercellSpacing().width * 2;
		 tableColumn.setPreferredWidth(width);
		 tableColumn.setMinWidth(width);
	}
}
