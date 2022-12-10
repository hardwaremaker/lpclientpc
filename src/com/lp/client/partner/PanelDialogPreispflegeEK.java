
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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Auswahl des Neu-Datums fuer eine Rechnung</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>09. 09. 2004</I>
 * </p>
 * 
 * @todo Gueltigen Datumsbereich ueberpruefen PJ 4930
 *       <p>
 *       </p>
 * @author Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelDialogPreispflegeEK extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperLabel wlaAendernAb = new WrapperLabel();
	private WrapperDateField wdfAendernAb = new WrapperDateField();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperButton wbuArtikelgruppe = new WrapperButton();
	private WrapperTextField wtfArtikelgruppe = new WrapperTextField();
	private WrapperLabel wlaProzentsatz = new WrapperLabel();
	private WrapperNumberField wnfProzentsatz = new WrapperNumberField();

	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private Integer selectedArtikelgruppeIId = null;

	private static final String ACTION_SPECIAL_FLR_ARTIKELGRUPPE = "ACTION_SPECIAL_FLR_ARTIKELGRUPPE";

	private Integer lieferantIId = null;

	public PanelDialogPreispflegeEK(InternalFrame p0, Integer lieferantIId, String title) throws Throwable {
		super(p0, title);
		this.lieferantIId = lieferantIId;
		jbInit();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);
		wlaAendernAb.setText("Preise \u00E4ndern ab");
		wlaProzentsatz.setText("Preise \u00E4ndern um");
		wbuArtikelgruppe.setText(LPMain.getInstance().getTextRespectUISPr("button.artikelgruppe"));

	
		LieferantDto lfDto = DelegateFactory.getInstance().getLieferantDelegate()
				.lieferantFindByPrimaryKey(lieferantIId);
		wbuArtikelgruppe.setActionCommand(ACTION_SPECIAL_FLR_ARTIKELGRUPPE);
		wbuArtikelgruppe.addActionListener(this);

		wtfArtikelgruppe.setActivatable(false);

		wnfProzentsatz.setMandatoryField(true);

		wdfAendernAb.setMandatoryField(true);

		WrapperLabel wlaProz = new WrapperLabel("%");
		wlaProz.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.setLayout(gridBagLayout1);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(
				new JLabel(LPMain.getInstance().getTextRespectUISPr("lieferant.pflege.preise.fuerlieferant") + " "
						+ lfDto.getPartnerDto().formatFixName1Name2() + ":"),
				new GridBagConstraints(0, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuArtikelgruppe, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfArtikelgruppe, new GridBagConstraints(1, 1, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaAendernAb, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfAendernAb, new GridBagConstraints(1, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaProzentsatz, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wnfProzentsatz, new GridBagConstraints(1, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 10, 0));
		jpaWorkingOn.add(wlaProz, new GridBagConstraints(2, 3, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 20, 0));

		jpaWorkingOn.add(new JLabel(""), new GridBagConstraints(0, 6, 2, 1, 0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Integer iIdArtikelgruppe = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				ArtgruDto artgruDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artgruFindByPrimaryKey(iIdArtikelgruppe);
				selectedArtikelgruppeIId = iIdArtikelgruppe;
				wtfArtikelgruppe.setText(artgruDto.getBezeichnung());
			}
		}
	}

	/**
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 * @todo vom internalframe das dautm abholen PJ 4940
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			if (allMandatoryFieldsSetDlg()) {

				String meldung;
				if (selectedArtikelgruppeIId == null) {
					meldung = LPMain.getInstance().getTextRespectUISPr("lieferant.pflege.preise.frage.alle");
				} else {
					meldung = LPMain.getInstance().getTextRespectUISPr("lieferant.pflege.preise.frage");
				}

				boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(), meldung);

				if (b == true) {
					DelegateFactory.getInstance().getLieferantDelegate().pflegeEKpreise(lieferantIId,
							selectedArtikelgruppeIId, wdfAendernAb.getDate(), wnfProzentsatz.getBigDecimal());
					
				}
			}else {
				return;
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_ARTIKELGRUPPE)) {
			panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
					.createPanelFLRArtikelgruppe(getInternalFrame(), selectedArtikelgruppeIId);
			new DialogQuery(panelQueryFLRArtikelgruppe);

		}
		super.eventActionSpecial(e);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuArtikelgruppe;
	}
}
