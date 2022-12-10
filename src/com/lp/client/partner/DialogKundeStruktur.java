package com.lp.client.partner;

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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.partner.KundeTreeNode;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundenInfoFuerStrukturAnsicht;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.util.GotoHelper;

@SuppressWarnings("static-access")
public class DialogKundeStruktur extends JDialog
		implements ActionListener, TreeExpansionListener, TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BorderLayout borderLayout = new BorderLayout();
	private JTree tree = null;
	PanelQuery panelQuery = null;
	private boolean bMitVersteckten;

	JButton wbuSelect = new JButton("SELECT");

	Integer selectedKundeIId = null;

	public DialogKundeStruktur(PanelQuery panelQuery, boolean bMitVersteckten) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance().getTextRespectUISPr("part.kunde.struktur"),
				false);
		this.bMitVersteckten = bMitVersteckten;
		this.panelQuery = panelQuery;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(800, 500);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuSelect)) {
			try {
				panelQuery.setSelectedId(selectedKundeIId);

			} catch (Throwable e2) {
				panelQuery.getInternalFrame().handleException(e2, true);
			}
			panelQuery.getInternalFrame().fireItemChanged(panelQuery, ItemChangedEvent.ITEM_CHANGED);
			panelQuery.getInternalFrame().fireItemChanged(panelQuery, ItemChangedEvent.GOTO_DETAIL_PANEL);
			this.setVisible(false);
		}
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		this.getContentPane().setLayout(borderLayout);

		KundeTreeNode root = new KundeTreeNode("ROOT");

		ArrayList<KundeDto> al = DelegateFactory.getInstance().getKundeDelegate()
				.holeAlleWurzelkundenFuerStruktursicht(bMitVersteckten);

		Iterator it = al.iterator();
		while (it.hasNext()) {

			KundeDto kundeDto = (KundeDto) it.next();

			KundeTreeNode stkl = new KundeTreeNode(kundeDto.getPartnerDto().formatFixName1Name2(), kundeDto.getIId());
			stkl.setUserObject(kundeDto.getPartnerDto().formatFixName1Name2());
			KundeTreeNode next = new KundeTreeNode("POS");
			stkl.add(next);

			root.add(stkl);
		}

		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeExpansionListener(this);

		tree.addTreeSelectionListener(this);

		this.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

		// Goto Stkl ->

		JPanel p = new JPanel(new GridBagLayout());

		wbuSelect.setPreferredSize(new Dimension(100, 21));
		wbuSelect.setMinimumSize(new Dimension(100, 21));
		wbuSelect.addActionListener(this);

		p.add(wbuSelect, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 10, 0));

		WrapperCheckBox mitVersteckten = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.versteckte"));
		mitVersteckten.setSelected(bMitVersteckten);
		mitVersteckten.setEnabled(false);
		p.add(mitVersteckten, new GridBagConstraints(4, 1, 1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 100, 0));

		this.getContentPane().add(p, BorderLayout.NORTH);

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {
		// TODO Auto-generated method stub
		KundeTreeNode node = (KundeTreeNode) arg0.getPath().getLastPathComponent();
		node.removeAllChildren();

		KundeTreeNode next = new KundeTreeNode("POS");
		node.add(next);

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		model.nodeChanged(node);
		int z = 0;
	}

	@Override
	public void treeExpanded(TreeExpansionEvent arg0) {
		// TODO Auto-generated method stub

		KundeTreeNode node = (KundeTreeNode) arg0.getPath().getLastPathComponent();

		node.removeAllChildren();

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		try {
			Integer kundeIId = node.getKundeIId();

			ArrayList<KundenInfoFuerStrukturAnsicht> alKunden = DelegateFactory.getInstance().getKundeDelegate()
					.holeNaechsteEbene(kundeIId, bMitVersteckten);

			Iterator it = alKunden.iterator();

			while (it.hasNext()) {
				KundenInfoFuerStrukturAnsicht kundenInfoFuerStrukturAnsicht = (KundenInfoFuerStrukturAnsicht) it.next();

				KundeTreeNode next = new KundeTreeNode(
						kundenInfoFuerStrukturAnsicht.getKundeDto().getPartnerDto().formatFixName1Name2(),
						kundenInfoFuerStrukturAnsicht.getKundeDto().getIId());

				boolean hatLieferadressen = kundenInfoFuerStrukturAnsicht.getBHasLieferadresse();
				if (hatLieferadressen == true) {
					next.add(new KundeTreeNode("POS", kundenInfoFuerStrukturAnsicht.getKundeDto().getIId()));
				}

				model.insertNodeInto(next, node, node.getChildCount());

			}

		} catch (Throwable e) {
			panelQuery.getInternalFrame().handleException(e, true);
		}
		model.nodeChanged(node);
		model.reload(node);
		// Naechste Ebene Holen

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

		KundeTreeNode node = (KundeTreeNode) e.getPath().getLastPathComponent();

		selectedKundeIId = node.getKundeIId();

	}
}
