package com.lp.client.lieferschein;

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.lieferschein.service.AusliefervorschlagDto;

/**
 * <p>Panel zum Bearbeiten der Auftragszuordnungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>20. 02. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelAusliefervorschlagPosition extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private TabbedPaneAusliefervorschlag tabbedPaneAusliefervorschlag = null;
	private AusliefervorschlagDto ausliefervorschlagDto = null;

	private Border border = null;
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private JPanel jpaWorkingOn = new JPanel();
	private WrapperIdentField wifArtikel = null;

	private WrapperSelectField wsfKunde = new WrapperSelectField(
			WrapperSelectField.KUNDE, getInternalFrame(), false);
	private WrapperSelectField wsfKundeLieferadresse = new WrapperSelectField(
			WrapperSelectField.KUNDE, getInternalFrame(), false);

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperDateField wdfAusliefertermin = new WrapperDateField();
	private WrapperLabel wlaAusliefertermin = new WrapperLabel();

	private WrapperLabel wlaHandeingabe = new WrapperLabel();

	public PanelAusliefervorschlagPosition(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneAusliefervorschlag tabbedPaneAusliefervorschlag)
			throws Throwable {
		super(internalFrame, add2TitleI);
//		this.tabbedPaneAusliefervorschlag = tabbedPaneAusliefervorschlag;
		jbInit();
		initComponents();
	}


	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.getWtfIdent().setMandatoryField(true);

		wlaMenge.setText(LPMain.getTextRespectUISPr("label.menge"));
		
		wlaHandeingabe.setText(LPMain.getTextRespectUISPr("lp.handeingabe"));
		
		
		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfMenge.setMandatoryField(true);
		wlaAusliefertermin.setText(LPMain
				.getTextRespectUISPr("ls.ausliefervorschlag.ausliefertermin"));

		wdfAusliefertermin.setMandatoryField(true);
		wsfKunde.setMandatoryField(true);
		wsfKundeLieferadresse.setMandatoryField(true);
		wsfKundeLieferadresse.setText(LPMain
				.getTextRespectUISPr("ls.ausliefervorschlag.lieferadresse"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		
		jpaWorkingOn.add(wlaHandeingabe, new GridBagConstraints(0,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 200, 0));
		
		iZeile++;

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(2, iZeile, 1, 1, 1,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaAusliefertermin, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		jpaWorkingOn.add(wdfAusliefertermin, new GridBagConstraints(2, iZeile,
				1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wsfKunde.getWrapperGotoButton(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						100, 0));

		jpaWorkingOn.add(wsfKunde.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;

		jpaWorkingOn.add(wsfKundeLieferadresse.getWrapperGotoButton(),
				new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						100, 0));

		jpaWorkingOn.add(wsfKundeLieferadresse.getWrapperTextField(),
				new GridBagConstraints(2, iZeile, 1, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		JPanel jpaOffset = new JPanel();
		jpaOffset.setLayout(new GridBagLayout());

		jpaWorkingOn.add(jpaOffset, new GridBagConstraints(1, iZeile, 3, 1, 1,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_AUSLIEFERVORSCHLAG;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		// locknew: 1 hier muss dafuer gesorgt werden, dass der Framework lockt
		super.eventActionNew(e, true, false);
		clearStatusbar();
		ausliefervorschlagDto = new AusliefervorschlagDto();

		leereAlleFelder(this);

	}

	/**
	 * Loeschen einer Rechnungsposition
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (!isLockedDlg()) {
			DelegateFactory.getInstance().getAusliefervorschlagDelegate()
					.removeAusliefervorschlag(ausliefervorschlagDto);
			this.ausliefervorschlagDto = null;
			// selectafterdelete: key null setzen
			this.setKeyWhenDetailPanel(null);
			this.leereAlleFelder(this);
			super.eventActionDelete(e, false, false);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (ausliefervorschlagDto.getIId() == null) {

				ausliefervorschlagDto.setIId(DelegateFactory.getInstance()
						.getAusliefervorschlagDelegate()
						.createAusliefervorschlag(ausliefervorschlagDto));

				setKeyWhenDetailPanel(ausliefervorschlagDto.getIId());
			} else {
				DelegateFactory.getInstance().getAusliefervorschlagDelegate()
						.updateAusliefervorschlag(ausliefervorschlagDto);

			}

			super.eventActionSave(e, true);

			eventYouAreSelected(false);
		}
	}

	private void components2Dto() throws Throwable {
		ausliefervorschlagDto.setMandantCNr(LPMain.getTheClient().getMandant());
		ausliefervorschlagDto
				.setArtikelIId(wifArtikel.getArtikelDto().getIId());

		ausliefervorschlagDto.setNMenge(wnfMenge.getBigDecimal());
		ausliefervorschlagDto.setKundeIId(wsfKunde.getIKey());
		ausliefervorschlagDto.setKundeIIdLieferadresse(wsfKundeLieferadresse
				.getIKey());

		ausliefervorschlagDto.setTAusliefertermin(wdfAusliefertermin
				.getTimestamp());

	}

	private void dto2Components() throws Throwable {

		ArtikelDto aDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(ausliefervorschlagDto.getArtikelIId());

		wifArtikel.setArtikelDto(aDto);
		if(aDto.getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)){
			wifArtikel.getWbuArtikel().setVisible(false);
			wifArtikel.getWtfIdent().setVisible(false);
			
			wlaHandeingabe.setVisible(true);
			
		} else {
			wifArtikel.getWbuArtikel().setVisible(true);
			wifArtikel.getWtfIdent().setVisible(true);
			wlaHandeingabe.setVisible(false);
		}
		

		wnfMenge.setBigDecimal(ausliefervorschlagDto.getNMenge());
		wsfKunde.setKey(ausliefervorschlagDto.getKundeIId());
		wsfKundeLieferadresse.setKey(ausliefervorschlagDto
				.getKundeIIdLieferadresse());

		wdfAusliefertermin.setTimestamp(ausliefervorschlagDto
				.getTAusliefertermin());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		}
	}

	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {
		PropertyVetoException pve = super.eventActionVetoableChangeLP();
		DelegateFactory.getInstance().getAusliefervorschlagDelegate()
				.removeLockDesAusliefervorschlagesWennIchIhnSperre();
		return pve;
	}
	
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		wlaHandeingabe.setVisible(false);
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.

			leereAlleFelder(this);

			clearStatusbar();
		} else {
			// einen alten Eintrag laden.
			ausliefervorschlagDto = DelegateFactory.getInstance()
					.getAusliefervorschlagDelegate()
					.ausliefervorschlagFindByPrimaryKey((Integer) key);
			dto2Components();

		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wifArtikel.getWbuArtikel();
	}

}
