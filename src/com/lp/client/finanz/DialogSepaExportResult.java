package com.lp.client.finanz;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.pc.LPMain;

public abstract class DialogSepaExportResult extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTable tableMessages;
	private JScrollPane panelTable;
	private WrapperButton wbOk;
	
	public DialogSepaExportResult(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		pack();
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(this);
	}

	private void jbInit() {
		tableMessages = new JTable();
		tableMessages.setModel(getTableModel());
		setColumnWidths(tableMessages);
		panelTable = new JScrollPane(tableMessages);
		
		wbOk = new WrapperButton();
		wbOk.setText(LPMain.getTextRespectUISPr("button.ok"));
		wbOk.addActionListener(this);
		
		setLayout(new MigLayout("wrap 3, hidemode 2", "[30%, fill|40%, fill|30%, fill]", "[fill,grow|fill]"));
		add(panelTable, "span 3");
		add(wbOk, "skip 1");
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (wbOk == event.getSource()) {
			setVisible(false);
		}
	}
	
	protected JTable getTable() {
		return tableMessages;
	}

	protected abstract TableModel getTableModel();

	protected abstract void setColumnWidths(JTable table);
}
