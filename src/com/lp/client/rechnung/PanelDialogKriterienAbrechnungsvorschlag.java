package com.lp.client.rechnung;

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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.lieferschein.LieferscheinFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

@SuppressWarnings("static-access")
/**
 * <p>
 * <I>Dialog zur Eingabe der Kriterien fuer den Bestellvorschlag.</I>
 * </p>
 * <p>
 * Dieser Dialog wird aus den folgenden Modulen aufgerufen:
 * </p>
 * <ul>
 * <li>Bestellung/Bestellvorschlag
 * <li>Anfrage/Anfragevorschlag
 * </ul>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>22.11.05</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.9 $
 */
public class PanelDialogKriterienAbrechnungsvorschlag extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isMitZeidaten() {
		return wcbMitZeidaten.isSelected();
	}

	public boolean isMitReisezeiten() {
		return wcbMitReisezeiten.isSelected();
	}

	public boolean isMitEingangsrechnungen() {
		return wcbMitEingangsrechnungen.isSelected();
	}

	public boolean isMitTelefonzeiten() {
		return wcbMitTelefonzeiten.isSelected();
	}
	public boolean isMitMaschinenzeitdaten() {
		return wcbMitMaschinenzeitdaten.isSelected();
	}
	public boolean isAltenLoeschen() {
		return wcbAltenAbrechnungsvorschlagLoeschen.isSelected();
	}
	private WrapperLabel wlaBisZumStichtagReservierung = null;
	private WrapperDateField wdfBisZumStichtag = null;

	private WrapperCheckBox wcbMitZeidaten = null;
	private WrapperCheckBox wcbMitTelefonzeiten = null;
	private WrapperCheckBox wcbMitReisezeiten = null;
	private WrapperCheckBox wcbMitEingangsrechnungen = null;
	private WrapperCheckBox wcbAltenAbrechnungsvorschlagLoeschen = null;
	
	private WrapperCheckBox wcbMitMaschinenzeitdaten = null;

	public PanelDialogKriterienAbrechnungsvorschlag(InternalFrame oInternalFrameI, String title, java.util.Date dBisStichtag)
			throws HeadlessException, Throwable {
		super(oInternalFrameI, title);

		try {
			jbInit();
			initComponents();
			
			wdfBisZumStichtag.setDate(dBisStichtag);
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame());
		}
	}

	private void jbInit() throws Throwable {

		wlaBisZumStichtagReservierung = new WrapperLabel();
		wlaBisZumStichtagReservierung
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.stichtag"));
		wdfBisZumStichtag = new WrapperDateField();

		wcbMitZeidaten = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.mitzeitdaten"));

		wcbMitReisezeiten = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.mitreise"));
		wcbMitEingangsrechnungen = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.miter"));

		wcbMitTelefonzeiten = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.mittelefon"));
		
		wcbMitMaschinenzeitdaten = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.mitmaschinen"));
		wcbAltenAbrechnungsvorschlagLoeschen = new WrapperCheckBox(
				LPMain.getInstance().getTextRespectUISPr("rech.abrechnungsvorschlag.erstellen.altenloeschen"));

		wcbMitEingangsrechnungen.setSelected(true);
		wcbMitMaschinenzeitdaten.setSelected(true);
		wcbMitReisezeiten.setSelected(true);
		wcbMitZeidaten.setSelected(true);
		wcbMitTelefonzeiten.setSelected(true);
		
		wcbAltenAbrechnungsvorschlagLoeschen.setSelected(true);
		
		

		iZeile++;
		jpaWorkingOn.add(wcbAltenAbrechnungsvorschlagLoeschen, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBisZumStichtagReservierung, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wdfBisZumStichtag, new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		
		iZeile++;
		jpaWorkingOn.add(wcbMitZeidaten, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitMaschinenzeitdaten, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitTelefonzeiten, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbMitReisezeiten, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbMitEingangsrechnungen, new GridBagConstraints(1, iZeile, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	

	public Date getStichtag() {
		return wdfBisZumStichtag.getDate();
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

	}

	public void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}
	}

}
