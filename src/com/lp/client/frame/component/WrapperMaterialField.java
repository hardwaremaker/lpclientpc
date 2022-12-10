
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
package com.lp.client.frame.component;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.Iterator;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.artikel.service.MaterialFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse bietet Felder zur Eingabe und Anzeige von Identnummern und
 * Bezeichnungen
 * </p>
 * Zum flexiblen GUI-Einbau ist das IdentField keine GUI-Komponente, sondern
 * bietet ihre Bestandteile an. Button, Identnummernfeld und die 3
 * Bezeichnungsfelder koennen damit in jedem Panel beliebig angeordnet werden.
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 10.04.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/01/23 12:41:05 $
 */
public class WrapperMaterialField implements ActionListener, FocusListener, IDirektHilfe {
	protected InternalFrame internalFrame = null;
	private PanelBasis panelBasis = null;
	private PanelQueryFLR panelQueryFLRMaterial = null;
	private MaterialDto materialDto = null;

	// identfield: 0 das identfield verwaltet die gui-komponenten, fuer alle
	// gibt es einen getter
	private WrapperButton wbuMaterial = null;
	private WrapperTextField wtfKennung = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperMaterialField_panelQueryFLRArtikel_eventItemChangedAdapter eventAdapter = null;

	private boolean bMitLeerenButton = false;

	public boolean isBMitLeerenButton() {
		return bMitLeerenButton;
	}

	public void setBMitLeerenButton(boolean bMitLeerenButton) {
		this.bMitLeerenButton = bMitLeerenButton;
	}

	public final static String ACTION_MATERIAL = "WRAPPERMATERIALFIELD_ACTION_MATERIAL";

	private String dhToken = null;

	public WrapperMaterialField() {
		// do nothing, just for testing.
	}

	public WrapperMaterialField(InternalFrame internalFrame, PanelBasis panelBasis) throws Throwable {
		this.internalFrame = internalFrame;
		this.panelBasis = panelBasis;
		jbInit();
		setDefaults();
	}

	private void setDefaults() throws Throwable {
		wtfKennung.setMandatoryField(true);
		wtfKennung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
	}

	private void jbInit() throws Throwable {
		wbuMaterial = new WrapperButton();
		wtfKennung = new WrapperTextField();

		// SP8761
		int iLaengeBezeichnung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();

		wtfBezeichnung = new WrapperTextField(iLaengeBezeichnung);

		wtfBezeichnung.setActivatable(false);
		wbuMaterial.setText(LPMain.getTextRespectUISPr("label.material") + "...");
		wbuMaterial.setToolTipText(LPMain.getTextRespectUISPr("label.material"));
		// ActionListener auf den Artikel
		// identfield: 1 actionlistener auf den artikelbutton
		// es koennen sich auch andere actionlistener anhaengen -> beachte
		// actionCommand
		wbuMaterial.setActionCommand(ACTION_MATERIAL);
		wbuMaterial.addActionListener(this);
		// FocusListener IdentFeld
		wtfKennung.addFocusListener(this);
		// eventhandling
		eventAdapter = new WrapperMaterialField_panelQueryFLRArtikel_eventItemChangedAdapter(this);
		// identfield: 2 allgemeiner ItemChangedListener fuer die Auswahlliste
		// damit kriegen auch die panels die events der liste mit
		internalFrame.addItemChangedListener(eventAdapter);
		// Strg-S aus PanelBasis mitkriegen
		if (panelBasis != null) {
			// Den Save-Button des Panels holen
			LPButtonAction item = (LPButtonAction) panelBasis.getHmOfButtons().get(PanelBasis.ACTION_SAVE);
			item.getButton().addActionListener(this);
		}
	}

	private void dialogQueryMaterial(String sMaterialVorbesetzt) throws Throwable {
		
		
		
		String[] aWhichButtonIUse = null;
		if (bMitLeerenButton) {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER,
					PanelBasis.ACTION_LEEREN };
		} else {
			aWhichButtonIUse = new String[] { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_FILTER };
		}

		panelQueryFLRMaterial = new PanelQueryFLR(ArtikelFilterFactory.getInstance().createQTMaterial(),
				null, QueryParameters.UC_ID_MATERIAL, aWhichButtonIUse, internalFrame,
				LPMain.getTextRespectUISPr("title.materialauswahlliste"));
		

		FilterKriteriumDirekt fkd = SystemFilterFactory.getInstance().createFKDKennung();

		fkd.value = sMaterialVorbesetzt;

		panelQueryFLRMaterial.befuellePanelFilterkriterienDirekt(fkd, SystemFilterFactory.getInstance()
				.createFKDSprTabelleBezeichnung(MaterialFac.FLR_MATERIAL_MATERIALSPRSET));

		if (fkd.value != null && !fkd.value.trim().equals("")) {
			panelQueryFLRMaterial.eventActionRefresh(null, false);
		}

		if (materialDto != null) {
			panelQueryFLRMaterial.setSelectedId(materialDto.getIId());
		}

		new DialogQuery(panelQueryFLRMaterial);

	}

	public WrapperButton getWbuMaterial() {
		return wbuMaterial;
	}

	public WrapperTextField getWtfBezeichnung() {
		return wtfBezeichnung;
	}

	public WrapperTextField getWtfKennung() {
		return wtfKennung;
	}

	@Override
	public void setToken(String token) {
		dhToken = token;
		wbuMaterial.setToken("wbuMaterial." + token);
		wtfKennung.setToken("ident." + token);
		wtfBezeichnung.setToken("bez." + token);

	}

	@Override
	public void removeCib() {
		wbuMaterial.removeCib();
	}

	@Override
	public String getToken() {
		return dhToken;
	}

	@Override
	public Point getLocationOffset() {
		return InfoButtonRelocator.getInstance().getRelocation(wtfKennung);
	}

	/**
	 * identfield: 7 artikel reinsetzen
	 * 
	 * @param artikelDto ArtikelDto
	 * @throws Throwable
	 */
	public void setMaterialDto(MaterialDto materialDto) throws Throwable {
		this.materialDto = materialDto;
		dto2Components();
	}

	public void setMaterialIId(Integer materialIId) throws Throwable {

		if (materialIId != null) {
			this.materialDto = DelegateFactory.getInstance().getMaterialDelegate()
					.materialFindByPrimaryKey(materialIId);

		} else {
			this.materialDto = null;
		}
		dto2Components();

	}

	private void dto2Components() throws Throwable {
		if (materialDto != null) {
			wtfKennung.setText(materialDto.getCNr());
			// Goto Button Ziel setzen

			if (materialDto.getMaterialsprDto() != null) {
				wtfBezeichnung.setText(materialDto.getMaterialsprDto().getCBez());
			} else {
				wtfBezeichnung.setText(null);
			}

		} else {
			wtfKennung.setText(null);
			wtfBezeichnung.setText(null);

		}
	}

	/**
	 * identfield: 6 artikel auslesen
	 * 
	 * @return ArtikelDto
	 */
	public MaterialDto getMaterialDto() {
		return materialDto;
	}

	public Integer getMaterialIId() {
		if (materialDto != null) {
			return materialDto.getIId();
		} else {
			return null;
		}
	}

	/**
	 * Das aktuelle QueryPanel. Zum behandeln von Events. // identfield: 3 die
	 * source aller events der artikelliste (zum mithoeren von aussen)
	 * 
	 * @return PanelQueryFLR
	 */
	public PanelQueryFLR getPanelQueryFLRArtikel() {
		return panelQueryFLRMaterial;
	}

	public void actionPerformed(ActionEvent e) {
		try {

			if (e.getActionCommand().equals(ACTION_MATERIAL)) {
				dialogQueryMaterial(wtfKennung.getText());
			}

		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame, t);
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		try {
			// focusLost am Ident-Feld, aber ich bleibe in HeliumV
			if (e.getSource() == wtfKennung) {
				if (wtfKennung.getText() == null) {
					// keine ident eingegeben
				} else {

					// Wenn auf den Button geklicht wurde, dann macht die
					// Auswahl die Liste
					if (e.getOppositeComponent() != null && e.getOppositeComponent() == wbuMaterial) {
						return;
					}

					try {

						Integer materialIId = DelegateFactory.getInstance().getMaterialDelegate()
								.materialFindByCNrOhneExc(wtfKennung.getText());

						if (materialIId != null) {
							MaterialDto aDto = DelegateFactory.getInstance().getMaterialDelegate()
									.materialFindByPrimaryKey(materialIId);
							this.setMaterialDto(aDto);
							// Benutzer informieren
							if (wtfKennung.isEditable() == true) {
								fireItemChangedEvent_GOTO_DETAIL_PANEL();
							}
						} else {
							dialogQueryMaterial(wtfKennung.getText());
						}

					} catch (ExceptionLP ex) {
						switch (ex.getICode()) {
						case EJBExceptionLP.FEHLER_BEI_FIND: {

						}
							break;
						default: {
							throw ex;
						}
						}
					}
				}
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(internalFrame);
		}
	}

	public void setComponentNames(String sFieldName) {
		wbuMaterial.setName(sFieldName + "_wbuArtikel");
		wtfBezeichnung.setName(sFieldName + "_wtfBezeichnung");
		wtfKennung.setName(sFieldName + "_wtfIdent");
	}

	public void refresh() throws Throwable {
		if (wtfKennung.getText() != null) {
			MaterialDto aDto = null;
			try {

				Integer matIId = DelegateFactory.getInstance().getMaterialDelegate()
						.materialFindByCNrOhneExc(wtfKennung.getText());
				if (matIId != null) {
					aDto = DelegateFactory.getInstance().getMaterialDelegate().materialFindByPrimaryKey(matIId);
				}

			} catch (ExceptionLP ex) {
				switch (ex.getICode()) {
				case EJBExceptionLP.FEHLER_BEI_FIND: {
					// nothing here
				}
					break;
				default: {
					throw ex;
				}
				}
			}
			if (aDto != null) {
				setMaterialDto(aDto);
			} else {
				wtfKennung.setText(null);

			}
		}
	}

	/**
	 * Die Eingabe pruefen. Falls im Textfeld Ident eine ungueltige Artikelnummer
	 * steht, wird das Feld geleert. <br>
	 * Falls bereits ein g&uuml;ltiger Artikel ausgew&auml;hlt ist, wird dieser nur
	 * bei einer g&uuml;ltigen Artikelnummer ersetzt, ansonsten belassen
	 * 
	 * @throws Throwable
	 */
	public void validate() throws Throwable {
		// Bevor der Der Benutzer speichern kann muss noch die eingegebene Ident
		// geprueft werden
		if (wtfKennung.getText() == null) {
			// keine ident eingegeben -> passt
		} else {
			MaterialDto aDto = null;
			try {

				Integer matIId = DelegateFactory.getInstance().getMaterialDelegate()
						.materialFindByCNrOhneExc(wtfKennung.getText());
				if (matIId != null) {
					aDto = DelegateFactory.getInstance().getMaterialDelegate().materialFindByPrimaryKey(matIId);
				}

			} catch (ExceptionLP ex) {
				switch (ex.getICode()) {
				case EJBExceptionLP.FEHLER_BEI_FIND: {
					// nothing here
				}
					break;
				default: {
					throw ex;
				}
				}
			}
			if (aDto != null) {
				setMaterialDto(aDto);
			} else {
				if (materialDto == null) {
					wtfKennung.setText(null);

				} else {
					wtfKennung.setText(materialDto.getCNr());
				}
			}
		}
	}

	protected void fireItemChangedEvent_GOTO_DETAIL_PANEL() {
		internalFrame.fireItemChanged(this, ItemChangedEvent.GOTO_DETAIL_PANEL);
	}
}

class WrapperMaterialField_panelQueryFLRArtikel_eventItemChangedAdapter implements ItemChangedListener {
	private WrapperMaterialField adaptee;

	WrapperMaterialField_panelQueryFLRArtikel_eventItemChangedAdapter(WrapperMaterialField adaptee) {
		this.adaptee = adaptee;
	}

	public void changed(EventObject eI) {
		try {
			ItemChangedEvent e = (ItemChangedEvent) eI;
			if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
				if (e.getSource() == adaptee.getPanelQueryFLRArtikel()) {
					Integer key = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();
					MaterialDto artikelDto = DelegateFactory.getInstance().getMaterialDelegate()
							.materialFindByPrimaryKey(key);

					adaptee.setMaterialDto(artikelDto);
					// Benutzer informieren
					adaptee.fireItemChangedEvent_GOTO_DETAIL_PANEL();

				}

			} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
				if (e.getSource() == adaptee.getPanelQueryFLRArtikel()) {
					adaptee.setMaterialDto(null);
				}
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(adaptee.internalFrame, t);
		}
	}
}
