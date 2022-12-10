
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.WaffenausfuehrungDto;
import com.lp.server.artikel.service.WaffenkaliberDto;
import com.lp.server.artikel.service.WaffenkategorieDto;
import com.lp.server.artikel.service.WaffentypDto;
import com.lp.server.artikel.service.WaffentypFeinDto;
import com.lp.server.artikel.service.WaffenzusatzDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("static-access")
public class PanelArtikelWaffenregister extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArtikelDto artikelDto = null;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaButtonAction = null;

	private WrapperLabel wlaArtikelBez = new WrapperLabel();
	private WrapperTextField wtfArtikelBez = new WrapperTextField();
	private WrapperLabel wlaArtikelZBez = new WrapperLabel();
	private WrapperLabel wlaArtikelnummer = new WrapperLabel();
	private WrapperTextField wtfArtikelnummer = new WrapperTextField();
	private WrapperTextField wtfArtikelZBez = new WrapperTextField();
	private WrapperLabel wlaZBez2 = new WrapperLabel();
	private WrapperTextField wtfArtikelZBez2 = new WrapperTextField();

	private WrapperButton wbuKaliber = new WrapperButton();
	private WrapperTextField wtfKaliber = new WrapperTextField();

	private WrapperButton wbuTyp = new WrapperButton();
	private WrapperTextField wtfTyp = new WrapperTextField();
	private WrapperButton wbuTypFein = new WrapperButton();
	private WrapperTextField wtfTypFein = new WrapperTextField();
	private WrapperButton wbuAusfuehrung = new WrapperButton();
	private WrapperTextField wtfAusfuehrung = new WrapperTextField();
	private WrapperButton wbuKategorie = new WrapperButton();
	private WrapperTextField wtfKategorie = new WrapperTextField();
	private WrapperButton wbuZusatz = new WrapperButton();
	private WrapperTextField wtfZusatz = new WrapperTextField();

	static final public String ACTION_SPECIAL_KALIBER_FROM_LISTE = "ACTION_SPECIAL_KALIBER_FROM_LISTE";
	static final public String ACTION_SPECIAL_TYP_FROM_LISTE = "ACTION_SPECIAL_TYP_FROM_LISTE";
	static final public String ACTION_SPECIAL_TYPFEIN_FROM_LISTE = "ACTION_SPECIAL_TYPFEIN_FROM_LISTE";
	static final public String ACTION_SPECIAL_AUSFUEHRUNG_FROM_LISTE = "ACTION_SPECIAL_AUSFUEHRUNG_FROM_LISTE";
	static final public String ACTION_SPECIAL_KATEGORIE_FROM_LISTE = "ACTION_SPECIAL_KATEGORIE_FROM_LISTE";
	static final public String ACTION_SPECIAL_ZUSATZ_FROM_LISTE = "ACTION_SPECIAL_ZUSATZ_FROM_LISTE";

	private PanelQueryFLR panelQueryFLRKaliber = null;
	private PanelQueryFLR panelQueryFLRTyp = null;
	private PanelQueryFLR panelQueryFLRTypFein = null;
	private PanelQueryFLR panelQueryFLRAusfuehrung = null;
	private PanelQueryFLR panelQueryFLRKategorie = null;
	private PanelQueryFLR panelQueryFLRZusatz = null;

	public PanelArtikelWaffenregister(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		leereAlleFelder(this);
		super.eventYouAreSelected(false);
		artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(((InternalFrameArtikel) getInternalFrame()).getArtikelDto().getIId());
		dto2Components();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		getInternalFrame().addItemChangedListener(this);

		wbuKaliber.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffenkaliber") + "...");
		wbuKaliber.addActionListener(this);
		wbuKaliber.setActionCommand(ACTION_SPECIAL_KALIBER_FROM_LISTE);
		wtfKaliber.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKaliber.setActivatable(false);

		wbuTyp.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffentyp") + "...");
		wbuTyp.addActionListener(this);
		wbuTyp.setActionCommand(ACTION_SPECIAL_TYP_FROM_LISTE);
		wtfTyp.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfTyp.setActivatable(false);

		wbuTypFein.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffentypfein") + "...");
		wbuTypFein.addActionListener(this);
		wbuTypFein.setActionCommand(ACTION_SPECIAL_TYPFEIN_FROM_LISTE);
		wtfTypFein.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfTypFein.setActivatable(false);

		wbuAusfuehrung.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffenausfuehrung") + "...");
		wbuAusfuehrung.addActionListener(this);
		wbuAusfuehrung.setActionCommand(ACTION_SPECIAL_AUSFUEHRUNG_FROM_LISTE);
		wtfAusfuehrung.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfAusfuehrung.setActivatable(false);

		wbuZusatz.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffenzusatz") + "...");
		wbuZusatz.addActionListener(this);
		wbuZusatz.setActionCommand(ACTION_SPECIAL_ZUSATZ_FROM_LISTE);
		wtfZusatz.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfZusatz.setActivatable(false);

		wbuKategorie.setText(LPMain.getInstance().getTextRespectUISPr("artikel.waffenkategorie") + "...");
		wbuKategorie.addActionListener(this);
		wbuKategorie.setActionCommand(ACTION_SPECIAL_KATEGORIE_FROM_LISTE);
		wtfKategorie.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfKategorie.setActivatable(false);

		wlaArtikelBez.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wtfArtikelBez.setText("");
		wtfArtikelBez.setActivatable(false);
		wlaArtikelZBez.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbezeichnung"));
		wlaArtikelnummer.setText(LPMain.getInstance().getTextRespectUISPr("artikel.artikelnummer"));

		wtfArtikelnummer.setText("");
		wtfArtikelnummer.setActivatable(false);
		wtfArtikelZBez.setText("");
		wtfArtikelZBez.setActivatable(false);
		wlaZBez2.setRequestFocusEnabled(true);
		wlaZBez2.setText(LPMain.getInstance().getTextRespectUISPr("lp.zusatzbez2"));
		wtfArtikelZBez2.setText("");
		wtfArtikelZBez2.setActivatable(false);

		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();
		wtfArtikelBez.setColumnsMax(iLaengenBezeichung);
		wtfArtikelZBez.setColumnsMax(iLaengenBezeichung);
		wtfArtikelZBez2.setColumnsMax(iLaengenBezeichung);

		JPanel jpaWorkingOn = new JPanel(new MigLayout("wrap 6", "[20%][15%][5%][30%][15%][5%]"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaArtikelnummer, "growx");
		jpaWorkingOn.add(wtfArtikelnummer, "growx, span 3, wrap");

		jpaWorkingOn.add(wlaArtikelBez, "growx");
		jpaWorkingOn.add(wtfArtikelBez, "growx, span");

		jpaWorkingOn.add(wlaArtikelZBez, "growx");
		jpaWorkingOn.add(wtfArtikelZBez, "growx, span");

		jpaWorkingOn.add(wlaZBez2, "growx");
		jpaWorkingOn.add(wtfArtikelZBez2, "growx, span, wrap 20");

		jpaWorkingOn.add(wbuKaliber, "growx");
		jpaWorkingOn.add(wtfKaliber, "growx,span 5,wrap");

		jpaWorkingOn.add(wbuAusfuehrung, "growx");
		jpaWorkingOn.add(wtfAusfuehrung, "growx,span 5,wrap");

		jpaWorkingOn.add(wbuTyp, "growx");
		jpaWorkingOn.add(wtfTyp, "growx,span 5,wrap");

		jpaWorkingOn.add(wbuTypFein, "growx");
		jpaWorkingOn.add(wtfTypFein, "growx,span 5,wrap");

		jpaWorkingOn.add(wbuKategorie, "growx");
		jpaWorkingOn.add(wtfKategorie, "growx,span 5,wrap");

		jpaWorkingOn.add(wbuZusatz, "growx");
		jpaWorkingOn.add(wtfZusatz, "growx,span 5,wrap");

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void dto2Components() throws Throwable {

		if (artikelDto.getWaffenkaliberIId() != null) {
			WaffenkaliberDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffenkaliberFindByPrimaryKey(artikelDto.getWaffenkaliberIId());
			wtfKaliber.setText(dto.formatKennungBezeichnung());
		}

		if (artikelDto.getWaffentypIId() != null) {
			WaffentypDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffentypFindByPrimaryKey(artikelDto.getWaffentypIId());
			wtfTyp.setText(dto.formatKennungBezeichnung());
		}

		if (artikelDto.getWaffentypFeinIId() != null) {
			WaffentypFeinDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffentypFeinFindByPrimaryKey(artikelDto.getWaffentypFeinIId());
			wtfTypFein.setText(dto.formatKennungBezeichnung());
		}

		if (artikelDto.getWaffenkategorieIId() != null) {
			WaffenkategorieDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffenkategorieFindByPrimaryKey(artikelDto.getWaffenkategorieIId());
			wtfKategorie.setText(dto.formatKennungBezeichnung());
		}

		if (artikelDto.getWaffenzusatzIId() != null) {
			WaffenzusatzDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffenzusatzFindByPrimaryKey(artikelDto.getWaffenzusatzIId());
			wtfZusatz.setText(dto.formatKennungBezeichnung());
		}

		if (artikelDto.getWaffenausfuehrungIId() != null) {
			WaffenausfuehrungDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
					.waffenausfuehrungFindByPrimaryKey(artikelDto.getWaffenausfuehrungIId());
			wtfAusfuehrung.setText(dto.formatKennungBezeichnung());
		}

		wtfArtikelnummer.setText(artikelDto.getCNr());
		if (artikelDto.getArtikelsprDto() != null) {
			wtfArtikelBez.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfArtikelZBez.setText(artikelDto.getArtikelsprDto().getCZbez());
			wtfArtikelZBez2.setText(artikelDto.getArtikelsprDto().getCZbez2());
		}

		this.setStatusbarPersonalIIdAendern(artikelDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(artikelDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artikelDto.getTAnlegen());
		this.setStatusbarTAendern(artikelDto.getTAendern());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };

		if (e.getActionCommand().equals(ACTION_SPECIAL_KALIBER_FROM_LISTE)) {
			panelQueryFLRKaliber = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENKALIBER, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffenkaliber"));
			panelQueryFLRKaliber.setSelectedId(artikelDto.getWaffenkaliberIId());
			panelQueryFLRKaliber.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRKaliber.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRKaliber);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_AUSFUEHRUNG_FROM_LISTE)) {
			panelQueryFLRAusfuehrung = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENAUSFUEHRUNG, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffenausfuehrung"));
			panelQueryFLRAusfuehrung.setSelectedId(artikelDto.getWaffenausfuehrungIId());
			panelQueryFLRAusfuehrung.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRAusfuehrung.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRAusfuehrung);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_KATEGORIE_FROM_LISTE)) {
			panelQueryFLRKategorie = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENKATEGORIE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffenkategorie"));
			panelQueryFLRKategorie.setSelectedId(artikelDto.getWaffenkategorieIId());
			panelQueryFLRKategorie.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRKategorie.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRKategorie);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_TYP_FROM_LISTE)) {
			panelQueryFLRTyp = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENTYP, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffentyp"));
			panelQueryFLRTyp.setSelectedId(artikelDto.getWaffentypIId());
			panelQueryFLRTyp.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRTyp.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRTyp);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_TYPFEIN_FROM_LISTE)) {
			panelQueryFLRTypFein = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENTYPFEIN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffentypfein"));
			panelQueryFLRTypFein.setSelectedId(artikelDto.getWaffentypFeinIId());
			panelQueryFLRTypFein.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRTypFein.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRTypFein);
		}else if (e.getActionCommand().equals(ACTION_SPECIAL_ZUSATZ_FROM_LISTE)) {
			panelQueryFLRZusatz = new PanelQueryFLR(null, null, QueryParameters.UC_ID_WAFFENZUSATZ, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr("artikel.waffenzusatz"));
			panelQueryFLRZusatz.setSelectedId(artikelDto.getWaffentypFeinIId());
			panelQueryFLRZusatz.addDirektFilter(SystemFilterFactory.getInstance().createFKDKennung());
			panelQueryFLRZusatz.addDirektFilter(SystemFilterFactory.getInstance().createFKDBezeichnung());
			new DialogQuery(panelQueryFLRZusatz);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRKaliber) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffenkaliberDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffenkaliberFindByPrimaryKey((Integer) key);
				wtfKaliber.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffenkaliberIId(dto.getIId());
			}else if (e.getSource() == panelQueryFLRAusfuehrung) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffenausfuehrungDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffenausfuehrungFindByPrimaryKey((Integer) key);
				wtfAusfuehrung.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffenausfuehrungIId(dto.getIId());
			}else if (e.getSource() == panelQueryFLRKategorie) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffenkategorieDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffenkategorieFindByPrimaryKey((Integer) key);
				wtfKategorie.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffenkategorieIId(dto.getIId());
			}else if (e.getSource() == panelQueryFLRTyp) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffentypDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffentypFindByPrimaryKey((Integer) key);
				wtfTyp.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffentypIId(dto.getIId());
			}else if (e.getSource() == panelQueryFLRTypFein) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffentypFeinDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffentypFeinFindByPrimaryKey((Integer) key);
				wtfTypFein.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffentypFeinIId(dto.getIId());
			}else if (e.getSource() == panelQueryFLRZusatz) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				WaffenzusatzDto dto = DelegateFactory.getInstance().getArtikelServiceDelegate()
						.waffenzusatzFindByPrimaryKey((Integer) key);
				wtfZusatz.setText(dto.formatKennungBezeichnung());
				artikelDto.setWaffenzusatzIId(dto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRKaliber) {
				wtfKaliber.setText(null);
				artikelDto.setWaffenkaliberIId(null);
			}else if (e.getSource() == panelQueryFLRAusfuehrung) {
				wtfAusfuehrung.setText(null);
				artikelDto.setWaffenausfuehrungIId(null);
			}else if (e.getSource() == panelQueryFLRKategorie) {
				wtfKategorie.setText(null);
				artikelDto.setWaffenkategorieIId(null);
			}else if (e.getSource() == panelQueryFLRTyp) {
				wtfTyp.setText(null);
				artikelDto.setWaffentypIId(null);
			}else if (e.getSource() == panelQueryFLRTypFein) {
				wtfTypFein.setText(null);
				artikelDto.setWaffentypFeinIId(null);
			}else if (e.getSource() == panelQueryFLRZusatz) {
				wtfZusatz.setText(null);
				artikelDto.setWaffenzusatzIId(null);
			}
			
		}
	}

	protected void components2Dto() throws Throwable {

	

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory.getInstance().getArtikelDelegate().updateArtikel(artikelDto);

		}
		super.eventActionSave(e, true);
	}
}
