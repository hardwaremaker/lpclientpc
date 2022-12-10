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
 *******************************************************************************/
package com.lp.client.projekt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperJTree;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.anfrage.service.AnfrageDto;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.DiaetenDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ReiseDto;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.HistoryDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektVerlaufHelperDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeAgStueckliste;
import com.lp.server.system.jcr.service.docnode.DocNodeAnfrage;
import com.lp.server.system.jcr.service.docnode.DocNodeAngebot;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeBestellung;
import com.lp.server.system.jcr.service.docnode.DocNodeEingangsrechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeGutschrift;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocNodeProformarechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeProjekt;
import com.lp.server.system.jcr.service.docnode.DocNodeProjektHistory;
import com.lp.server.system.jcr.service.docnode.DocNodeRechnung;
import com.lp.server.system.jcr.service.docnode.DocNodeReklamation;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.service.BelegDto;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

public class PanelCockpit extends JPanel implements TreeSelectionListener, TableModelListener, ActionListener {
	private static final long serialVersionUID = 2490947042644964120L;

	private IProjektDtoService iProjektDtoService = null;
	private InternalFrame internalFrame = null;

	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

	private WrapperJTree tree;

	JTable table = null;

	private WrapperMediaControl wmcMedia = null;

	ArrayList<ProjektCockpitMutableTreeNode> alAlleEndpunkteFuerChronologie = new ArrayList<ProjektCockpitMutableTreeNode>();

	private WrapperGotoButton wbuGoto = new WrapperGotoButton(GotoHelper.GOTO_STUECKLISTE_DETAIL);

	boolean bHatStufe0 = false;
	boolean bHatStufe1 = false;
	boolean bHatStufe2 = false;
	boolean bHatStufe3 = false;
	boolean bHatStufe99 = false;

	JScrollPane tableView = null;

	WrapperButton wbuExpand = null;

	WrapperButton wbuCollapse = null;

	private String ACTION_EXPAND = "ACTION_EXPAND";
	private String ACTION_COLLAPSE = "ACTION_COLLAPSE";

	private ProjektDto getProjektDto() {
		return iProjektDtoService.getProjektDto();
	}

	public PanelCockpit(InternalFrame internalFrame, IProjektDtoService iProjektDtoService) throws Throwable {
		this.iProjektDtoService = iProjektDtoService;
		this.internalFrame = internalFrame;
		jbInit();

		StringBuffer projektTitel = new StringBuffer("");
		if (getProjektDto() == null || getProjektDto().getIId() == null) {
			projektTitel.append(LPMain.getTextRespectUISPr("proj.projekt.neu"));
		} else {
			PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(getProjektDto().getPartnerIId());
			projektTitel
					.append(DelegateFactory.getInstance().getProjektServiceDelegate()
							.bereichFindByPrimaryKey(getProjektDto().getBereichIId()).getCBez())
					.append(" ").append(getProjektDto().getCNr() + "|" + partnerDto.getCName1nachnamefirmazeile1() + "|"
							+ getProjektDto().getCTitel());
		}

		// dem Panel die Titel setzen
		internalFrame.setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, LPMain.getTextRespectUISPr("proj.cockpit"));
		internalFrame.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, projektTitel.toString());

	}

	public void valueChanged(TreeSelectionEvent arg0) {

		wbuGoto.setVisible(false);

		if (tree.getLastSelectedPathComponent() instanceof ProjektCockpitMutableTreeNode) {

			ProjektCockpitMutableTreeNode node = (ProjektCockpitMutableTreeNode) tree.getLastSelectedPathComponent();

			befuelleMediacontrol(node);

		}

	}

	public void befuelleMediacontrol(ProjektCockpitMutableTreeNode node) {
		try {
			wmcMedia.setOMedia(null);
			wmcMedia.setMimeType(null);

			if (node.getBelegIId() != null && node.getOptionGotoButton() != -1) {

				wbuGoto.setWhereToGo(node.getOptionGotoButton());
				wbuGoto.setOKey(node.getBelegIId());
				wbuGoto.setText(node.getBelegart());
				wbuGoto.getWrapperButton().setEnabled(false);
				wbuGoto.setVisible(true);

			}

			if (node.getxText() != null) {
				wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
				wmcMedia.setOMediaText(node.getxText());

			} else {
				if (node.getJcrDocDto() != null) {
					if (node.getJcrDocDto().getbData() == null) {
						JCRDocDto dataJCR = DelegateFactory.getInstance().getJCRDocDelegate()
								.getData(node.getJcrDocDto());
						node.getJcrDocDto().setbData(dataJCR.getbData());
					}

					if (node.getJcrDocDto().getbData() != null) {

						if (".JPG".equals(node.getJcrDocDto().getsMIME().toUpperCase())
								|| ".JPEG".equals(node.getJcrDocDto().getsMIME().toUpperCase())) {
							wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
							wmcMedia.setOMedia(node.getJcrDocDto().getbData());
						} else if (".PNG".equals(node.getJcrDocDto().getsMIME().toUpperCase())) {
							wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
							wmcMedia.setOMedia(node.getJcrDocDto().getbData());							
						} else if (".JRPRINT".equals(node.getJcrDocDto().getsMIME().toUpperCase())) {
							wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER);
							wmcMedia.setOMedia(node.getJcrDocDto().getbData());
						} else if (".PDF".equals(node.getJcrDocDto().getsMIME().toUpperCase())) {
							wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
							wmcMedia.setOMediaImage(node.getJcrDocDto().getbData());
							wmcMedia.setDateiname(node.getJcrDocDto().getsFilename());
						} else {
							wmcMedia.setMimeType(null);
						}
					}
				}
			}

		} catch (Throwable e) {
			internalFrame.handleException(e, true);
		}
	}

	private DefaultTreeModel baumZusammenbauen() throws Throwable {

		ProjektCockpitMutableTreeNode root = new ProjektCockpitMutableTreeNode("Projekt");

		// PJ21374 Dokumente des Kopfes hinzufuegen
		BereichDto bDto = DelegateFactory.getInstance().getProjektServiceDelegate()
				.bereichFindByPrimaryKey(getProjektDto().getBereichIId());
		DocPath docpathKopf = new DocPath(new DocNodeProjekt(getProjektDto(), bDto));
		List<DocNodeBase> docsKopf = DelegateFactory.getInstance().getJCRDocDelegate()
				.getDocNodeChildrenFromNode(docpathKopf);
		for (int u = 0; u < docsKopf.size(); u++) {

			DocNodeBase base = docsKopf.get(u);

			if (base.getNodeType() == DocNodeBase.FILE) {

				JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();

				if (darfDocSehen(jcrDocDto)) {

					ArrayList<DocNodeVersion> versions = DelegateFactory.getInstance().getJCRDocDelegate()
							.getAllDocumentVersions(jcrDocDto);

					for (int j = 0; j < versions.size(); j++) {

						JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();

						java.sql.Timestamp tZeitpunkt = new java.sql.Timestamp(jcrDocDtoVersion.getlZeitpunkt());

						ProjektCockpitMutableTreeNode treeNodeVersion = new ProjektCockpitMutableTreeNode(
								Helper.formatTimestamp(tZeitpunkt, LPMain.getTheClient().getLocUi()));
						treeNodeVersion.setJcrDocDto(jcrDocDtoVersion);
						treeNodeVersion.setAenderungsdatum(tZeitpunkt);
						treeNodeVersion.setBelegdatum(tZeitpunkt);
						treeNodeVersion.setJcrdatum(tZeitpunkt);

						root.add(treeNodeVersion);

					}
				}
			}
		}

		ProjektCockpitMutableTreeNode details = new ProjektCockpitMutableTreeNode("Details");

		// History holen
		HistoryDto[] historyDtos = DelegateFactory.getInstance().getProjektDelegate()
				.historyFindByProjektIid(getProjektDto().getIId());
		for (int i = 0; i < historyDtos.length; i++) {

			String titel = "";
			if (historyDtos[i].getCTitel() != null) {
				titel = " " + historyDtos[i].getCTitel();
			}

			ProjektCockpitMutableTreeNode history = new ProjektCockpitMutableTreeNode(
					Helper.formatTimestamp(historyDtos[i].getTBelegDatum(), LPMain.getTheClient().getLocUi()) + titel);
			if (Helper.short2boolean(historyDtos[i].getBHtml()) == false) {
				history.setxText(historyDtos[i].getXText());
			}

			history.setBelegart("Projekt-Detail" + titel);
			history.setJcrdatum(historyDtos[i].getTBelegDatum());
			history.setBelegdatum(historyDtos[i].getTBelegDatum());
			history.setAenderungsdatum(historyDtos[i].getTBelegDatum());
			history.setBelegnummer(history.getBelegart());

			details.add(history);

			BereichDto bereichDto = DelegateFactory.getInstance().getProjektServiceDelegate()
					.bereichFindByPrimaryKey(getProjektDto().getBereichIId());

			DocPath docpath = new DocPath(new DocNodeProjektHistory(historyDtos[i], getProjektDto(), bereichDto));

			List<DocNodeBase> docs = DelegateFactory.getInstance().getJCRDocDelegate()
					.getDocNodeChildrenFromNode(docpath);

			for (int u = 0; u < docs.size(); u++) {

				DocNodeBase base = docs.get(u);

				if (base.getNodeType() == DocNodeBase.FILE) {

					JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();

					if (darfDocSehen(jcrDocDto)) {

						ArrayList<DocNodeVersion> versions = DelegateFactory.getInstance().getJCRDocDelegate()
								.getAllDocumentVersions(jcrDocDto);

						for (int j = 0; j < versions.size(); j++) {

							JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();

							java.sql.Timestamp tZeitpunkt = new java.sql.Timestamp(jcrDocDtoVersion.getlZeitpunkt());

							ProjektCockpitMutableTreeNode treeNodeVersion = new ProjektCockpitMutableTreeNode(
									Helper.formatTimestamp(tZeitpunkt, LPMain.getTheClient().getLocUi()));
							treeNodeVersion.setJcrDocDto(jcrDocDtoVersion);
							treeNodeVersion.setAenderungsdatum(tZeitpunkt);
							treeNodeVersion.setBelegdatum(tZeitpunkt);
							treeNodeVersion.setJcrdatum(tZeitpunkt);

							history.add(treeNodeVersion);

						}
					}

				}

			}

		}

		root.add(details);

		ProjektCockpitMutableTreeNode einkauf = new ProjektCockpitMutableTreeNode("Einkauf");

		ProjektCockpitMutableTreeNode verkauf = new ProjektCockpitMutableTreeNode("Verkauf");
		ProjektCockpitMutableTreeNode fertigung = new ProjektCockpitMutableTreeNode("Fertigung");

		LinkedHashMap<String, ProjektVerlaufHelperDto> hm = DelegateFactory.getInstance().getProjektDelegate()
				.getProjektVerlauf(getProjektDto().getIId());

		Iterator it = hm.keySet().iterator();

		int iTypEinkauf = 0;
		int iTypVerkauf = 1;
		int iTypFertigung = 2;

		TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>> tmEinkauf = new TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>>();
		TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>> tmVerkauf = new TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>>();
		TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>> tmFertigung = new TreeMap<String, ArrayList<ProjektCockpitMutableTreeNode>>();

		while (it.hasNext()) {
			ProjektVerlaufHelperDto belegDto = hm.get(it.next());

			int iTyp = iTypEinkauf;

			String belegart = "Unbekannt";
			String belegnummer = "Unbekannt";
			Integer belegIId = null;
			int optionGotoButton = -1;
			String xText = null;

			DocPath docpath = null;

			java.sql.Timestamp belegDatum = new java.sql.Timestamp(System.currentTimeMillis());
			java.sql.Timestamp aenderungsdatumDatum = null;

			String projekt_bestellnummer = "";

			if (belegDto.getBelegDto() instanceof AngebotDto) {
				AngebotDto dto = (AngebotDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANGEBOT;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				optionGotoButton = GotoHelper.GOTO_ANGEBOT_AUSWAHL;
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				docpath = new DocPath(new DocNodeAngebot(dto));
				iTyp = iTypVerkauf;

			} else if (belegDto.getBelegDto() instanceof AuftragDto) {
				AuftragDto dto = (AuftragDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_AUFTRAG;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				optionGotoButton = GotoHelper.GOTO_AUFTRAG_AUSWAHL;
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				docpath = new DocPath(new DocNodeAuftrag(dto));
				iTyp = iTypVerkauf;

			} else if (belegDto.getBelegDto() instanceof LieferscheinDto) {
				LieferscheinDto dto = (LieferscheinDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LIEFERSCHEIN;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				optionGotoButton = GotoHelper.GOTO_LIEFERSCHEIN_AUSWAHL;
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				docpath = new DocPath(new DocNodeLieferschein(dto));
				iTyp = iTypVerkauf;

			} else if (belegDto.getBelegDto() instanceof AgstklDto) {
				AgstklDto dto = (AgstklDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_AGSTUECKLISTE;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				optionGotoButton = GotoHelper.GOTO_ANGEBOTSTKL_AUSWAHL;
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				docpath = new DocPath(new DocNodeAgStueckliste(dto));
				iTyp = iTypVerkauf;

			} else if (belegDto.getBelegDto() instanceof RechnungDto) {
				RechnungDto dto = (RechnungDto) belegDto.getBelegDto();
				belegart = dto.getRechnungartCNr();
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();

				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				if (belegart.equals(RechnungFac.RECHNUNGART_GUTSCHRIFT)) {
					docpath = new DocPath(new DocNodeGutschrift(dto));
					optionGotoButton = GotoHelper.GOTO_GUTSCHRIFT_AUSWAHL;
				} else if (belegart.equals(RechnungFac.RECHNUNGART_PROFORMARECHNUNG)) {
					docpath = new DocPath(new DocNodeProformarechnung(dto));
					optionGotoButton = GotoHelper.GOTO_PROFORMARECHNUNG_AUSWAHL;
				}else {
					docpath = new DocPath(new DocNodeRechnung(dto));
					optionGotoButton = GotoHelper.GOTO_RECHNUNG_AUSWAHL;
				}
				iTyp = iTypVerkauf;

			} else if (belegDto.getBelegDto() instanceof BestellungDto) {
				BestellungDto dto = (BestellungDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_BESTELLUNG;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				belegDatum = new java.sql.Timestamp(dto.getDBelegdatum().getTime());
				aenderungsdatumDatum = dto.getTAendern();

				optionGotoButton = GotoHelper.GOTO_BESTELLUNG_AUSWAHL;

				docpath = new DocPath(new DocNodeBestellung(dto));
				iTyp = iTypEinkauf;
			} else if (belegDto.getBelegDto() instanceof ReklamationDto) {
				ReklamationDto dto = (ReklamationDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_REKLAMATION;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				optionGotoButton = GotoHelper.GOTO_REKLAMATION_AUSWAHL;

				docpath = new DocPath(new DocNodeReklamation(dto));

				if (dto.getReklamationartCNr().equals(ReklamationFac.REKLAMATIONART_LIEFERANT)) {
					iTyp = iTypEinkauf;
				} else {
					iTyp = iTypVerkauf;
				}

			} else if (belegDto.getBelegDto() instanceof AnfrageDto) {
				AnfrageDto dto = (AnfrageDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_ANFRAGE;
				belegIId = dto.getIId();
				belegnummer = dto.getCNr();
				belegDatum = dto.getTBelegdatum();
				aenderungsdatumDatum = dto.getTAendern();

				optionGotoButton = GotoHelper.GOTO_ANFRAGE_AUSWAHL;
				docpath = new DocPath(new DocNodeAnfrage(dto));
				iTyp = iTypEinkauf;
			} else if (belegDto.getBelegDto() instanceof LosDto) {
				LosDto dto = (LosDto) belegDto.getBelegDto();
				belegart = LocaleFac.BELEGART_LOS;
				belegnummer = dto.getCNr();
				belegIId = dto.getIId();
				belegDatum = new java.sql.Timestamp(dto.getTProduktionsbeginn().getTime());
				aenderungsdatumDatum = dto.getTAendern();

				optionGotoButton = GotoHelper.GOTO_FERTIGUNG_AUSWAHL;
				iTyp = iTypFertigung;
			} else if (belegDto.getBelegDto() instanceof EingangsrechnungAuftragszuordnungDto) {
				EingangsrechnungAuftragszuordnungDto dto = (EingangsrechnungAuftragszuordnungDto) belegDto
						.getBelegDto();
				belegart = LocaleFac.BELEGART_EINGANGSRECHNUNG;

				EingangsrechnungDto erDto = DelegateFactory.getInstance().getEingangsrechnungDelegate()
						.eingangsrechnungFindByPrimaryKey(dto.getEingangsrechnungIId());

				belegnummer = erDto.getCNr();
				belegIId = erDto.getIId();
				belegDatum = new java.sql.Timestamp(erDto.getDBelegdatum().getTime());
				aenderungsdatumDatum = erDto.getTAendern();
				optionGotoButton = GotoHelper.GOTO_EINGANGSRECHNUNG_AUSWAHL;
				docpath = new DocPath(new DocNodeEingangsrechnung(erDto));
				iTyp = iTypEinkauf;
			} else if (belegDto.getBelegDto() instanceof ReiseDto) {
				ReiseDto reiseDto = (ReiseDto) belegDto.getBelegDto();
				belegart = "Reise";
				belegnummer = reiseDto.getTZeit() + "";

				belegDatum = reiseDto.getTZeit();
				aenderungsdatumDatum = reiseDto.getTAendern();

				xText = "";

				if (reiseDto.getCKommentar() != null) {
					xText += "Kommentar: " + reiseDto.getCKommentar() + "\r\n";
				}
				if (reiseDto.getDiaetenIId() != null) {
					DiaetenDto diaetenDto = DelegateFactory.getInstance().getZeiterfassungDelegate()
							.diaetenFindByPrimaryKey(reiseDto.getDiaetenIId());

					xText += "Reiseland: " + diaetenDto.getCBez() + "\r\n";
				}

				if (reiseDto.getTZeit() != null) {
					xText += "Von: " + Helper.formatTimestamp(reiseDto.getTZeit(), LPMain.getTheClient().getLocUi())
							+ "\r\n";
				}
				iTyp = iTypVerkauf;
			} else if (belegDto.getBelegDto() instanceof TelefonzeitenDto) {
				TelefonzeitenDto telefonzeitenDto = (TelefonzeitenDto) belegDto.getBelegDto();
				belegart = "Telefon";
//				belegnummer = Helper.formatTimestamp(telefonzeitenDto.getTVon(), LPMain.getTheClient().getLocUi()) + "";
				belegDatum = telefonzeitenDto.getTVon();
				aenderungsdatumDatum = telefonzeitenDto.getTVon();

				belegIId = telefonzeitenDto.getIId();

				optionGotoButton = GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN;

				BelegText bt = buildTextFrom(telefonzeitenDto);
				xText = bt.getText();
				belegnummer = bt.getBelegnummer();

				iTyp = iTypVerkauf;
			}

			if (belegDto.getBelegDto() instanceof BelegDto) {
				BelegDto bTemp = (BelegDto) belegDto.getBelegDto();
				if (bTemp.getCBez() != null) {
					belegnummer += " " + bTemp.getCBez();
				}

				if (bTemp instanceof AuftragDto) {
					AuftragDto bAB = (AuftragDto) bTemp;

					if (bAB.getCBezProjektbezeichnung() != null) {
						belegnummer += " " + bAB.getCBezProjektbezeichnung();
					}

					if (bAB.getCBestellnummer() != null) {
						belegnummer += " " + bAB.getCBestellnummer();
					}
				}

			}

			ArrayList<ProjektCockpitMutableTreeNode> al = null;

			if (iTyp == iTypEinkauf) {
				if (tmEinkauf.containsKey(belegart)) {
					al = tmEinkauf.get(belegart);
				} else {
					al = new ArrayList<ProjektCockpitMutableTreeNode>();
				}
			} else if (iTyp == iTypVerkauf) {
				if (tmVerkauf.containsKey(belegart)) {
					al = tmVerkauf.get(belegart);
				} else {
					al = new ArrayList<ProjektCockpitMutableTreeNode>();
				}
			} else if (iTyp == iTypFertigung) {
				if (tmFertigung.containsKey(belegart)) {
					al = tmFertigung.get(belegart);
				} else {
					al = new ArrayList<ProjektCockpitMutableTreeNode>();
				}
			}

			ProjektCockpitMutableTreeNode treeNodeBeleg = new ProjektCockpitMutableTreeNode(belegnummer);
			treeNodeBeleg.setBelegIId(belegIId);
			treeNodeBeleg.setOptionGotoButton(optionGotoButton);
			treeNodeBeleg.setBelegart(belegart);
			treeNodeBeleg.setxText(xText);
			treeNodeBeleg.setProjektVerlaufHelperDto(belegDto);
			treeNodeBeleg.setJcrdatum(aenderungsdatumDatum);
			treeNodeBeleg.setAenderungsdatum(aenderungsdatumDatum);
			treeNodeBeleg.setBelegdatum(belegDatum);
			treeNodeBeleg.setBelegnummer(belegnummer);

			if (docpath != null) {

				List<DocNodeBase> docs = DelegateFactory.getInstance().getJCRDocDelegate()
						.getDocNodeChildrenFromNode(docpath);

				for (int i = 0; i < docs.size(); i++) {

					DocNodeBase base = docs.get(i);

					if (base.getNodeType() == DocNodeBase.FILE) {

						JCRDocDto jcrDocDto = ((DocNodeFile) base).getJcrDocDto();

						if (darfDocSehen(jcrDocDto)) {

							ArrayList<DocNodeVersion> versions = DelegateFactory.getInstance().getJCRDocDelegate()
									.getAllDocumentVersions(jcrDocDto);

							for (int j = 0; j < versions.size(); j++) {

								JCRDocDto jcrDocDtoVersion = versions.get(j).getJCRDocDto();

								java.sql.Timestamp tZeitpunkt = new java.sql.Timestamp(
										jcrDocDtoVersion.getlZeitpunkt());

								ProjektCockpitMutableTreeNode treeNodeVersion = new ProjektCockpitMutableTreeNode(
										Helper.formatTimestamp(tZeitpunkt, LPMain.getTheClient().getLocUi()));
								treeNodeVersion.setJcrDocDto(jcrDocDtoVersion);
								treeNodeVersion.setBelegIId(null);
								treeNodeVersion.setOptionGotoButton(optionGotoButton);
								treeNodeVersion.setBelegart(belegart);
								treeNodeVersion.setProjektVerlaufHelperDto(belegDto);
								treeNodeVersion.setAenderungsdatum(aenderungsdatumDatum);
								treeNodeVersion.setBelegdatum(belegDatum);
								treeNodeVersion.setBelegnummer(belegnummer);
								treeNodeVersion.setJcrdatum(tZeitpunkt);

								treeNodeBeleg.add(treeNodeVersion);

							}
						}

					}

				}

			}

			// Telefonzeiten

			ProjektCockpitMutableTreeNode tzZuBeleg = new ProjektCockpitMutableTreeNode("Telefon");

			if (belegDto.getTelefonzeitenZuBeleg() != null && belegDto.getTelefonzeitenZuBeleg().size() > 0) {
				for (int i = 0; i < belegDto.getTelefonzeitenZuBeleg().size(); i++) {

					TelefonzeitenDto telefonzeitenDto = belegDto.getTelefonzeitenZuBeleg().get(i);

					String titel = "";
					if (telefonzeitenDto.getCTitel() != null) {
						titel = " " + telefonzeitenDto.getCTitel();
					}

					String belegnummerTZ = telefonzeitenDto.getTVon() + " " + titel;
					String belegartTZ = "Telefon";

					int optionGotoButtonTZ = GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN;

					belegDatum = telefonzeitenDto.getTVon();
					aenderungsdatumDatum = telefonzeitenDto.getTVon();
					xText = "";

					if (telefonzeitenDto.getXKommentarext() != null) {
						xText += "Kommentar extern: " + telefonzeitenDto.getXKommentarext() + "\r\n";
					}

					if (telefonzeitenDto.getXKommentarint() != null) {
						xText += "Kommentar intern: " + telefonzeitenDto.getXKommentarint() + "\r\n";
					}

					if (telefonzeitenDto.getPartnerIId() != null) {
						PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
								.partnerFindByPrimaryKey(telefonzeitenDto.getPartnerIId());
						xText += "Partner: " + partnerDto.formatFixName1Name2() + "\r\n";
					}

					if (telefonzeitenDto.getAnsprechpartnerIId() != null) {

						AnsprechpartnerDto ansprechpartnerDto = DelegateFactory.getInstance()
								.getAnsprechpartnerDelegate()
								.ansprechpartnerFindByPrimaryKey(telefonzeitenDto.getAnsprechpartnerIId());

						xText += "Ansprechpartner: " + ansprechpartnerDto.getPartnerDto().formatFixTitelName1Name2()
								+ "\r\n";
					}

					if (telefonzeitenDto.getTVon() != null) {
						xText += "Von: "
								+ Helper.formatTimestamp(telefonzeitenDto.getTVon(), LPMain.getTheClient().getLocUi())
								+ "\r\n";
					}

					if (telefonzeitenDto.getTBis() != null) {
						xText += "Bis: "
								+ Helper.formatTimestamp(telefonzeitenDto.getTBis(), LPMain.getTheClient().getLocUi())
								+ "\r\n";
					}

					ProjektCockpitMutableTreeNode treeNodeTelefon = new ProjektCockpitMutableTreeNode(belegnummerTZ);
					treeNodeTelefon.setBelegIId(telefonzeitenDto.getIId());
					treeNodeTelefon.setOptionGotoButton(optionGotoButtonTZ);
					treeNodeTelefon.setBelegart(belegartTZ);
					treeNodeTelefon.setxText(xText);
					treeNodeTelefon.setProjektVerlaufHelperDto(belegDto);
					treeNodeTelefon.setJcrdatum(aenderungsdatumDatum);
					treeNodeTelefon.setAenderungsdatum(aenderungsdatumDatum);
					treeNodeTelefon.setBelegdatum(belegDatum);
					treeNodeTelefon.setBelegnummer(belegnummerTZ);

					tzZuBeleg.add(treeNodeTelefon);
				}

				treeNodeBeleg.add(tzZuBeleg);
			}

			al.add(treeNodeBeleg);

			if (iTyp == iTypEinkauf) {
				tmEinkauf.put(belegart, al);
			} else if (iTyp == iTypVerkauf) {
				tmVerkauf.put(belegart, al);
			} else if (iTyp == iTypFertigung) {
				tmFertigung.put(belegart, al);
			}

		}

		Iterator<String> itKnoten = tmEinkauf.keySet().iterator();
		while (itKnoten.hasNext()) {
			String key = itKnoten.next();

			DefaultMutableTreeNode knoten = new DefaultMutableTreeNode(key);

			ArrayList<ProjektCockpitMutableTreeNode> al = tmEinkauf.get(key);

			al = sortArrayListAnhandBelegnummer(al);

			for (int i = 0; i < al.size(); i++) {
				knoten.add(al.get(i));
			}

			einkauf.add(knoten);

		}

		itKnoten = tmVerkauf.keySet().iterator();
		while (itKnoten.hasNext()) {
			String key = itKnoten.next();

			DefaultMutableTreeNode knoten = new DefaultMutableTreeNode(key);

			ArrayList<ProjektCockpitMutableTreeNode> al = tmVerkauf.get(key);

			al = sortArrayListAnhandBelegnummer(al);

			for (int i = 0; i < al.size(); i++) {
				knoten.add(al.get(i));
			}

			verkauf.add(knoten);

		}

		itKnoten = tmFertigung.keySet().iterator();
		while (itKnoten.hasNext()) {
			String key = itKnoten.next();

			DefaultMutableTreeNode knoten = new DefaultMutableTreeNode(key);

			ArrayList<ProjektCockpitMutableTreeNode> al = tmFertigung.get(key);
			al = sortArrayListAnhandBelegnummer(al);
			for (int i = 0; i < al.size(); i++) {
				knoten.add(al.get(i));
			}

			fertigung.add(knoten);

		}
		if (einkauf.children().hasMoreElements()) {
			root.add(einkauf);
		}
		if (verkauf.children().hasMoreElements()) {
			root.add(verkauf);
		}
		if (fertigung.children().hasMoreElements()) {
			root.add(fertigung);
		}

		// Nun alle Endpunkte rausholen und nach datum sortieren

		alAlleEndpunkteFuerChronologie = alleEndpunkteHolen(root);

		int x = 0;

		// Chronologie sortieren

		for (int i = alAlleEndpunkteFuerChronologie.size() - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				ProjektCockpitMutableTreeNode o = alAlleEndpunkteFuerChronologie.get(j);
				ProjektCockpitMutableTreeNode o1 = alAlleEndpunkteFuerChronologie.get(j + 1);

				if (o.aenderungsdatum == null) {
					o.aenderungsdatum = o.belegdatum;
				}

				if (o1.aenderungsdatum == null) {
					o1.aenderungsdatum = o1.belegdatum;
				}

				if (o.jcrdatum == null) {
					o.jcrdatum = o.aenderungsdatum;
				}
				if (o1.jcrdatum == null) {
					o1.jcrdatum = o1.aenderungsdatum;
				}

				if (o.jcrdatum.before(o1.jcrdatum)) {
					alAlleEndpunkteFuerChronologie.set(j, o1);
					alAlleEndpunkteFuerChronologie.set(j + 1, o);
				}

			}
		}

		// Table befuellen

		//

		Object[][] daten = new Object[alAlleEndpunkteFuerChronologie.size()][4];

		int iZeile = 0;
		for (int i = 0; i < alAlleEndpunkteFuerChronologie.size(); i++) {

			ProjektCockpitMutableTreeNode cZeile = alAlleEndpunkteFuerChronologie.get(i);

			String belegart = cZeile.getBelegart().trim();

			if (cZeile.getProjektVerlaufHelperDto() != null
					&& cZeile.getProjektVerlaufHelperDto().getBelegDto() != null) {
				Object o = cZeile.getProjektVerlaufHelperDto().getBelegDto();

				if (o instanceof BelegDto) {
					BelegDto belegDto = (BelegDto) o;
					belegart += " " + belegDto.getCNr();

					if (belegDto.getCBez() != null) {
						belegart += " " + belegDto.getCBez();
					}

					if (belegDto instanceof AuftragDto) {
						AuftragDto bAB = (AuftragDto) belegDto;

						if (bAB.getCBezProjektbezeichnung() != null) {
							belegart += " " + bAB.getCBezProjektbezeichnung();
						}

						if (bAB.getCBestellnummer() != null) {
							belegart += " " + bAB.getCBestellnummer();
						}
					}

				} else if (cZeile.getBelegnummer() != null) {
					belegart += " " + cZeile.getBelegnummer();
				}

			}

			daten[iZeile][0] = cZeile.jcrdatum;
			daten[iZeile][1] = cZeile.aenderungsdatum;
			daten[iZeile][2] = cZeile.belegdatum;
			daten[iZeile][3] = belegart;

			iZeile++;
		}

		String[] cols = new String[] { LPMain.getTextRespectUISPr("projekt.chronologie.dokdatum"),
				LPMain.getTextRespectUISPr("projekt.chronologie.aenderungsdatum"),
				LPMain.getTextRespectUISPr("projekt.chronologie.belegdatum"),
				LPMain.getTextRespectUISPr("projekt.chronologie.beleg") };


		table = new JTable(new MyTableModel1(cols, daten));
		
	
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(60);
		table.getColumnModel().getColumn(2).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setPreferredWidth(250);
		
		table.getModel().addTableModelListener(this);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			public void valueChanged(ListSelectionEvent lse) {
				if (!lse.getValueIsAdjusting()) {
					System.out.println("Selection Changed");
					befuelleMediacontrol(alAlleEndpunkteFuerChronologie.get(table.getSelectedRow()));
				}
			}
		});

		if (tableView != null) {
			tableView.setViewportView(table);
		}

		return new DefaultTreeModel(root);

	}

	private BelegText buildTextFrom(TelefonzeitenDto telefonzeitenDto) throws Throwable {
		String belegnummer = Helper.formatTimestamp(
				telefonzeitenDto.getTVon(), LPMain.getTheClient().getLocUi());
//		belegDatum = telefonzeitenDto.getTVon();
//		aenderungsdatumDatum = telefonzeitenDto.getTVon();

//		belegIId = telefonzeitenDto.getIId();

//		optionGotoButton = GotoHelper.GOTO_ZEITERFASSUNG_TELEFONZEITEN;

		String xText = "";

		if (telefonzeitenDto.getXKommentarext() != null) {
			xText += LPMain.getTextRespectUISPr("projekt.cockpit.beleg.externerkommentar") + "\r\n";
			xText += telefonzeitenDto.getXKommentarext() + "\r\n";
//			xText += "<style isBold=\"true\">Kommentar extern:</style>\r\n";

//			xText += "Kommentar extern: " + telefonzeitenDto.getXKommentarext() + "\r\n";
		}
		
		if (telefonzeitenDto.getXKommentarint() != null) {
			xText += LPMain.getTextRespectUISPr("projekt.cockpit.beleg.internerkommentar") + "\r\n";
			xText += telefonzeitenDto.getXKommentarint() + "\r\n";
//			xText += "Kommentar intern: " + telefonzeitenDto.getXKommentarint() + "\r\n";
		}

		if (telefonzeitenDto.getPartnerIId() != null) {
			PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(telefonzeitenDto.getPartnerIId());
			xText += LPMain.getMessageTextRespectUISPr(
					"projekt.cockpit.beleg.partner", partnerDto.formatFixName1Name2()) + "\r\n";
//			xText += "Partner: " + partnerDto.formatFixName1Name2() + "\r\n";

			if (partnerDto.getCKbez() != null) {
				belegnummer += " " + partnerDto.getCKbez();
			}

			if (telefonzeitenDto.getAnsprechpartnerIId() != null) {
				AnsprechpartnerDto anspDto = DelegateFactory.getInstance().getAnsprechpartnerDelegate()
						.ansprechpartnerFindByPrimaryKey(telefonzeitenDto.getAnsprechpartnerIId());

				belegnummer += "| " + anspDto.getPartnerDto().formatFixTitelName1Name2();

				xText += LPMain.getMessageTextRespectUISPr(
						"projekt.cockpit.beleg.ansprechpartner",
						anspDto.getPartnerDto().formatFixName1Name2()) + "\r\n";
//				xText += "Ansprechpartner: " + anspDto.getPartnerDto().formatFixTitelName1Name2()
//						+ "\r\n";
			}
			
			if (telefonzeitenDto.getCTitel() != null) {
				belegnummer += "| " + telefonzeitenDto.getCTitel();
			}
		}

		if (telefonzeitenDto.getTVon() != null) {
			xText += LPMain.getMessageTextRespectUISPr(
					"projekt.cockpit.beleg.von", 
					Helper.formatTimestamp(telefonzeitenDto.getTVon(), LPMain.getTheClient().getLocUi())) + "\r\n";
//			xText += "Von: "
//					+ Helper.formatTimestamp(telefonzeitenDto.getTVon(), LPMain.getTheClient().getLocUi())
//					+ "\r\n";
		}

		if (telefonzeitenDto.getTBis() != null) {
			xText += LPMain.getMessageTextRespectUISPr(
					"projekt.cockpit.beleg.bis", 
					Helper.formatTimestamp(telefonzeitenDto.getTBis(), LPMain.getTheClient().getLocUi())) + "\r\n";
//			xText += "Bis: "
//					+ Helper.formatTimestamp(telefonzeitenDto.getTBis(), LPMain.getTheClient().getLocUi())
//					+ "\r\n";
		}

		return new BelegText(belegnummer, xText);
	}
	
	private boolean darfDocSehen(JCRDocDto jcrDocDto) {
		boolean bDarfDocSehen = true;

		if (jcrDocDto != null) {
			int iSicherheitsstufe = (int) jcrDocDto.getlSicherheitsstufe();
			bDarfDocSehen = false;
			switch (iSicherheitsstufe) {
			case (int) JCRDocFac.SECURITY_NONE:
				if (bHatStufe0) {
					bDarfDocSehen = true;
				}
				break;
			case (int) JCRDocFac.SECURITY_LOW:
				if (bHatStufe1) {
					bDarfDocSehen = true;
				}
				break;
			case (int) JCRDocFac.SECURITY_MEDIUM:
				if (bHatStufe2) {
					bDarfDocSehen = true;
				}
				break;
			case (int) JCRDocFac.SECURITY_HIGH:
				if (bHatStufe3) {
					bDarfDocSehen = true;
				}
				break;
			case (int) JCRDocFac.SECURITY_ARCHIV:
				if (bHatStufe99) {
					bDarfDocSehen = true;
				}
				break;
			}
		}
		return bDarfDocSehen;
	}

	private ArrayList<ProjektCockpitMutableTreeNode> alleEndpunkteHolen(DefaultMutableTreeNode knoten) {

		ArrayList<ProjektCockpitMutableTreeNode> al = new ArrayList<ProjektCockpitMutableTreeNode>();

		if (knoten.getChildCount() > 0) {
			Enumeration children = knoten.children();
			while (children.hasMoreElements()) {
				Object o = children.nextElement();

				if (o instanceof DefaultMutableTreeNode) {
					al.addAll(alleEndpunkteHolen((DefaultMutableTreeNode) o));
				}

			}

		} else {
			if (knoten instanceof ProjektCockpitMutableTreeNode) {

				if (((ProjektCockpitMutableTreeNode) knoten).belegart != null) {
					al.add((ProjektCockpitMutableTreeNode) knoten);
				}

			}
		}

		return al;

	}

	private ArrayList<ProjektCockpitMutableTreeNode> sortArrayListAnhandBelegnummer(
			ArrayList<ProjektCockpitMutableTreeNode> al) {
		for (int k = al.size() - 1; k > 0; --k) {
			for (int j = 0; j < k; ++j) {
				ProjektCockpitMutableTreeNode p1 = al.get(j);
				ProjektCockpitMutableTreeNode p2 = al.get(j + 1);
				String belegnummer1 = (String) p1.getUserObject();
				String belegnummer2 = (String) p2.getUserObject();
				if (belegnummer1.compareTo(belegnummer2) < 0) {
					al.set(j, p2);
					al.set(j + 1, p1);
				}
			}
		}

		return al;
	}

	private void jbInit() throws Throwable {

		wbuExpand = new WrapperButton();
		wbuExpand.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/plus_sign.png")));
		wbuExpand.setActionCommand(ACTION_EXPAND);
		wbuExpand.addActionListener(this);
		wbuExpand.setToolTipText(LPMain.getTextRespectUISPr("proj.cockpit.alles.ausklappen"));

		wbuCollapse = new WrapperButton();
		wbuCollapse.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/minus_sign.png")));
		wbuCollapse.setActionCommand(ACTION_COLLAPSE);
		wbuCollapse.addActionListener(this);
		wbuCollapse.setToolTipText(LPMain.getTextRespectUISPr("proj.cockpit.alles.einklappen"));

		bHatStufe0 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU);
		bHatStufe1 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU);
		bHatStufe2 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU);
		bHatStufe3 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU);
		bHatStufe99 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_99_CU);

		setLayout(new GridBagLayout());
		JPanel projektDaten = new JPanel();

		Border border = projektDaten.getBorder();
		Border margin = new LineBorder(Color.gray, 2);
		projektDaten.setBorder(new CompoundBorder(border, margin));
		projektDaten.setLayout(new GridBagLayout());

		projektDaten.add(new JLabel("Projekt Nr.:"), new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 50, 0));

		projektDaten.add(new JLabel(getProjektDto().getCNr() + ", " + getProjektDto().getCTitel()),
				new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0), 0, 0));

		projektDaten.add(new JLabel("Erzeuger:"), new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		PersonalDto personalDto = DelegateFactory.getInstance().getPersonalDelegate()
				.personalFindByPrimaryKey(getProjektDto().getPersonalIIdErzeuger());

		projektDaten.add(new JLabel(personalDto.formatFixName1Name2()), new GridBagConstraints(2, 1, 1, 1, 1, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		wmcMedia = new WrapperMediaControl(internalFrame, "", false, true);

		tree = new WrapperJTree(baumZusammenbauen());
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);
		tree.addTreeSelectionListener(this);

		JScrollPane treeView = new JScrollPane(tree);
		treeView.setMinimumSize(new Dimension(0, 10));
		treeView.setPreferredSize(new Dimension(250, 10));

		ArbeitsplatzparameterDto parameter = DelegateFactory.getInstance().getParameterDelegate()
				.holeArbeitsplatzparameter(ParameterFac.ARBEITSPLATZPARAMETER_PROJEKT_COCKPIT_DEFAULT_EINGEKLAPPT);

		tree.expandAll(tree.getPathForRow(0), true);

		if (parameter == null
				|| (parameter != null && parameter.getCWert() != null && parameter.getCWert().equals("1"))) {
			tree.expandAll(tree.getPathForRow(0), false);
		}

		add(projektDaten, new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 10));

		add(wbuExpand, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 20, 0));

		add(wbuCollapse, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 30, 0, 0), 20, 0));

		JTabbedPane tabbedPaneRoot = new JTabbedPane();

		tabbedPaneRoot.addTab("Themen", treeView);

		tableView = new JScrollPane(table);
		tableView.setMinimumSize(new Dimension(00, 10));
		tableView.setPreferredSize(new Dimension(400, 10));
		// tableView.setMaximumSize(new Dimension(800, 10));

		tabbedPaneRoot.addTab("Chronologie", tableView);

		add(splitPane, new GridBagConstraints(0, 21, 1, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		JPanel right = new JPanel(new GridBagLayout());

		splitPane.setTopComponent(tabbedPaneRoot);
		splitPane.setBottomComponent(right);

		right.add(wbuGoto, new GridBagConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 100, 0));

		right.add(wmcMedia, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		wbuGoto.setVisible(false);

	}

	class ProjektCockpitMutableTreeNode extends DefaultMutableTreeNode {

		public ProjektCockpitMutableTreeNode(Object o) {
			super(o);
		}

		private String xText = null;

		private java.sql.Timestamp jcrdatum = null;
		private java.sql.Timestamp belegdatum = null;

		public java.sql.Timestamp getJcrdatum() {
			return jcrdatum;
		}

		public void setJcrdatum(java.sql.Timestamp jcrdatum) {
			this.jcrdatum = jcrdatum;
		}

		public java.sql.Timestamp getBelegdatum() {
			return belegdatum;
		}

		public void setBelegdatum(java.sql.Timestamp belegdatum) {
			this.belegdatum = belegdatum;
		}

		public java.sql.Timestamp getAenderungsdatum() {
			return aenderungsdatum;
		}

		public void setAenderungsdatum(java.sql.Timestamp aenderungsdatum) {
			this.aenderungsdatum = aenderungsdatum;
		}

		public String getBelegnummer() {
			return belegnummer;
		}

		public void setBelegnummer(String belegnummer) {
			this.belegnummer = belegnummer;
		}

		private java.sql.Timestamp aenderungsdatum = null;
		private String belegnummer = null;

		private ProjektVerlaufHelperDto projektVerlaufHelperDto = null;

		public ProjektVerlaufHelperDto getProjektVerlaufHelperDto() {
			return projektVerlaufHelperDto;
		}

		public void setProjektVerlaufHelperDto(ProjektVerlaufHelperDto projektVerlaufHelperDto) {
			this.projektVerlaufHelperDto = projektVerlaufHelperDto;
		}

		public String getxText() {
			return xText;
		}

		public void setxText(String xText) {
			this.xText = xText;
		}

		private String belegart = null;

		public String getBelegart() {
			return belegart;
		}

		public void setBelegart(String belegart) {
			this.belegart = belegart;
		}

		private Integer belegIId = null;

		public Integer getBelegIId() {
			return belegIId;
		}

		public void setBelegIId(Integer belegIId) {
			this.belegIId = belegIId;
		}

		public int getOptionGotoButton() {
			return optionGotoButton;
		}

		public void setOptionGotoButton(int optionGotoButton) {
			this.optionGotoButton = optionGotoButton;
		}

		private int optionGotoButton = -1;

		private JCRDocDto jcrDocDto = null;

		public JCRDocDto getJcrDocDto() {
			return jcrDocDto;
		}

		public void setJcrDocDto(JCRDocDto jcrDocDto) {
			this.jcrDocDto = jcrDocDto;
		}

	}

	class MyTableModel1 extends AbstractTableModel {
		private static final long serialVersionUID = -6311652299531724223L;
		private String[] columnNames = null;
		private Object[][] data = null;

		public MyTableModel1(String[] columnNames, Object[][] data) {

			this.columnNames = columnNames;
			this.data = data;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			/*
			 * wbuGoto.setVisible(false); if (table.getSelectedRow() >= 0) {
			 * befuelleMediacontrol(alAlleEndpunkteFuerChronologie.get(table.getSelectedRow(
			 * ))); }
			 */

			return data[row][col];
		}

		public Class<?> getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {

			return false;

		}

	}

	private class BelegText {
		private String text;
		private String belegnummer;
		
		public BelegText() {			
		}

		public BelegText(String belegNummer, String text) {
			this.belegnummer = belegNummer;
			this.text = text;
		}
		
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getBelegnummer() {
			return belegnummer;
		}

		public void setBelegnummer(String belegnummer) {
			this.belegnummer = belegnummer;
		}
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		wbuGoto.setVisible(false);

		befuelleMediacontrol(alAlleEndpunkteFuerChronologie.get(table.getSelectedRow()));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(wbuExpand)) {
			tree.expandAll(tree.getPathForRow(0), true);
		} else if (e.getSource().equals(wbuCollapse)) {
			tree.expandAll(tree.getPathForRow(0), false);
		}

	}

}
