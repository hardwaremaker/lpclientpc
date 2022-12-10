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
package com.lp.client.rechnung;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.frame.report.PanelDateRange;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer den Rechnung WA Export.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>15. 07. 2005</I>
 * </p>
 * <p>
 * </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelDialogWAExport extends PanelDialogKriterien {
	private static final long serialVersionUID = 4416049571940461298L;

	private final static String ACTION_SPECIAL_KUNDE_EINER = "action_special_kunde_einer";

	private PanelQueryFLR panelQueryFLRKunde = null;
	private KundeDto kundeDto = null;
	protected WrapperButton wbuKunde = new WrapperButton();
	protected WrapperTextField wtfKunde = new WrapperTextField();
	private PanelDateRange panelDateRange = null;
	private JPanel panel = null;
	private HvLayout hvLayout = null;

	public PanelDialogWAExport(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		wbuKunde.setText(LPMain.getTextRespectUISPr("button.kunde"));
		wbuKunde.setToolTipText(LPMain.getTextRespectUISPr("button.kunde.tooltip"));
		wbuKunde.setActionCommand(ACTION_SPECIAL_KUNDE_EINER);
		wbuKunde.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);

		panelDateRange = new PanelDateRange();
		panel = new JPanel();
		hvLayout = HvLayoutFactory.create(panel);

		hvLayout
			.add(panelDateRange).wrap()
			.add(wbuKunde, 90).split(2)
			.add(wtfKunde, 160);

		jpaWorkingOn.add(panel);

	}

	private void setDefaults() throws Throwable {
		wtfKunde.setActivatable(false);
		wtfKunde.setEditable(false);
		panelDateRange.getWdrc().doClickDown();
		panelDateRange.getWdrc().doClickUp();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KUNDE_EINER)) {
			dialogQueryKunde();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			Integer kundeIId = null;
			if (kundeDto != null) {
				kundeIId = kundeDto.getIId();
			}
			byte[] data = DelegateFactory.getInstance().getRechnungDelegate().exportWAJournal(kundeIId,
					panelDateRange.getWdfVon().getDate(), panelDateRange.getWdfBis().getDate(), new Integer(Helper.SORTIERUNG_NACH_IDENT)).getBytes();
			HvOptional<WrapperFile> wf = HelperClient.showSaveDialog(
					this, FileChooserConfigToken.ExportLastDirectory, null);
			if (wf.isPresent()) {
				File file = wf.get().getFile();
				LPMain.getInstance().saveFile(getInternalFrame(), file.getAbsolutePath(), data, false);				
			}
		
			getInternalFrame().closePanelDialog();
		}
		super.eventActionSpecial(e);
	}

	private void dialogQueryKunde() throws Throwable {
		panelQueryFLRKunde = PartnerFilterFactory.getInstance().createPanelFLRKunde(getInternalFrame(), true, true);
		new DialogQuery(panelQueryFLRKunde);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKunde) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey((Integer) key);
					dto2ComponentsKunde();
				}
			}
		}
	}

	/**
	 * Traegt die Daten fuer die Kostenstelle ein.
	 */
	private void dto2ComponentsKunde() {
		if (kundeDto != null) {
			wtfKunde.setText(kundeDto.getPartnerDto().formatFixTitelName1Name2());
		} else {
			wtfKunde.setText(null);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuKunde;
	}

}
