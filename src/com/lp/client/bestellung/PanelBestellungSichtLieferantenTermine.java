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
package com.lp.client.bestellung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.bestellung.service.BestellungFac;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich ...</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>18.02.05</I>
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author josef erlinger
 * @version $Revision: 1.19 $
 */
public class PanelBestellungSichtLieferantenTermine extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// wird benoetigt in der eventActionSave initialisiert wird es im
	// Konstruktor
	private final TabbedPaneBestellung tpBestellung;
	private BestellpositionDto bestellpositionDto = null;

	// von hier ...
	private JPanel jpaWorkingOn = null;
	private JPanel panelButtonAction = null;

	private WrapperLabel wlaKommentar = null;
	private WrapperEditorField lpeKommentar = null;

	private WrapperLabel wlaABTermin = null;
	private WrapperDateField wdfABTermin = null;
	
	private WrapperLabel wlaABBelegdatum = null;
	private WrapperDateField wdfABBelegdatum = null;

	private WrapperLabel wlaABUrsprungtermin = null;
	private WrapperDateField wdfABUrsprungtermin = null;

	private WrapperCheckBox wcbUrsprungterminMitaendern = null;

	private WrapperLabel wlaABNummer = null;
	private WrapperTextField wtfABNummer = null;

	private WrapperGotoButton wbuArtikelGoto = null;
	private WrapperTextField wtfIdent = null;

	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;

	private WrapperLabel wlaZusatzBezeichnung = null;
	private WrapperTextField wtfZusatzBezeichnung = null;

	private WrapperLabel wlaLieferterminbestaetigt = new WrapperLabel();
	private WrapperLabel wlaLieferterminausBestellposition = new WrapperLabel();

	private static final String ACTION_SPECIAL_ABTERMIN_AENDERN = "action_special_abtermin_aendern";
	private static final String ACTION_SPECIAL_LIEFERTERMIN_BESTAETIGT = "action_special_liefertermin_bestaetigt";

	private boolean bMitUrsung = false;

	public PanelBestellungSichtLieferantenTermine(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		// initialisieren von TabbedPaneBestellung
		tpBestellung = getInternalFrameBestellung().getTabbedPaneBestellung();
		jbInit();
		initComponents();
	}

	private BestellpositionDto getBestellpositionDto() {
		return bestellpositionDto;
	}

	private void setBestellpositionDto(BestellpositionDto bestellpositionDto) {
		this.bestellpositionDto = bestellpositionDto;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {

		// PJ19485
		// Wenn es in einem anderen anderen Mandanten ein Auftrag dazu gibt,
		// dann kann die Bestellung nicht mehr geaendert werden
		AuftragDto aDto = DelegateFactory
				.getInstance()
				.getAuftragDelegate()
				.istAuftragBeiAnderemMandantenHinterlegt(
						getBestellpositionDto().getBestellungIId());

		if (aDto != null) {

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("bes.error.auftraginanderemmandanten.hinterlegt"));
			Object[] pattern = new Object[] { aDto.getCNr(),
					aDto.getMandantCNr() };

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					mf.format(pattern));
			return;

		}

		getInternalFrameBestellung().getTabbedPaneBestellung().setTitle();
		WareneingangspositionDto wepDtos[] = DelegateFactory
				.getInstance()
				.getWareneingangDelegate()
				.wareneingangspositionFindByBestellpositionIId(
						getBestellpositionDto().getIId());
		if (getBestellpositionDto().getBestellpositionstatusCNr().equals(
				BestellungFac.BESTELLSTATUS_ERLEDIGT)) {
			this.panelOnOrOff();
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.sichtlieferantenterminebereitsgeliefert"));
			return;
		} else if (wepDtos.length > 0
				&& DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.berechneOffeneMengePosition(
								getBestellpositionDto().getIId())
						.compareTo(new BigDecimal(0)) == 0) {

			this.panelOnOrOff();
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.sichtlieferantenterminepositionnichtveraenderbar"));
			return;
		}

		else {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
			wdfABUrsprungtermin.setEnabled(false);
			// PJ 14881
			// this.setDefaults();
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_ABTERMIN_AENDERN)) {
			
			// SP6209
			// Wenn es in einem anderen anderen Mandanten ein Auftrag dazu gibt,
			// dann kann die Bestellung nicht mehr geaendert werden
			AuftragDto aDto = DelegateFactory
					.getInstance()
					.getAuftragDelegate()
					.istAuftragBeiAnderemMandantenHinterlegt(
							getBestellpositionDto().getBestellungIId());

			if (aDto != null) {

				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("bes.error.auftraginanderemmandanten.hinterlegt"));
				Object[] pattern = new Object[] { aDto.getCNr(),
						aDto.getMandantCNr() };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));
				return;

			}
			
			if (wtfABNummer.getText() != null && wdfABTermin.getDate() != null) {
				java.sql.Date datumNeu = DialogFactory.showDatumseingabe(LPMain
						.getTextRespectUISPr("best.abtermin.neueneingeben"));
				if (datumNeu != null) {

					DelegateFactory
							.getInstance()
							.getBestellungDelegate()
							.updateBestellpositionNurABTermin(
									getBestellpositionDto().getIId(), datumNeu);
				}
				eventYouAreSelected(false);
			} else {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("best.abtermin.neueneingeben.fehler"));

			}
			getInternalFrameBestellung().getTabbedPaneBestellung()
					.getPanelLieferrantentermine().eventYouAreSelected(false);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_LIEFERTERMIN_BESTAETIGT)) {

			DelegateFactory
					.getInstance()
					.getBestellungDelegate()
					.updateBestellpositionNurLieferterminBestaetigt(
							getBestellpositionDto().getIId());
			getInternalFrameBestellung().getTabbedPaneBestellung()
					.getPanelLieferrantentermine().eventYouAreSelected(false);
		}

	}

	private InternalFrameBestellung getInternalFrameBestellung() {
		return (InternalFrameBestellung) getInternalFrame();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getInternalFrameBestellung().getTabbedPaneBestellung().setTitle();
		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		// PJ19485
		// Wenn es in einem anderen anderen Mandanten ein Auftrag dazu gibt,
		// dann kann die Bestellung nicht mehr geaendert werden
		AuftragDto aDto = DelegateFactory
				.getInstance()
				.getAuftragDelegate()
				.istAuftragBeiAnderemMandantenHinterlegt(
						getBestellpositionDto().getBestellungIId());

		if (aDto != null) {

			MessageFormat mf = new MessageFormat(
					LPMain.getTextRespectUISPr("bes.error.auftraginanderemmandanten.hinterlegt"));
			Object[] pattern = new Object[] { aDto.getCNr(),
					aDto.getMandantCNr() };

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					mf.format(pattern));
			return;

		}

		WareneingangspositionDto wepDtos[] = DelegateFactory
				.getInstance()
				.getWareneingangDelegate()
				.wareneingangspositionFindByBestellpositionIId(
						getBestellpositionDto().getIId());
		if (tpBestellung.getBesDto().getStatusCNr()
				.equals(BestellungFac.BESTELLSTATUS_GELIEFERT)) {
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.sichtlieferantenterminebereitsgeliefert"));
			return;
		} else if (wepDtos.length > 0
				&& DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.berechneOffeneMengePosition(
								getBestellpositionDto().getIId())
						.compareTo(new BigDecimal(0)) == 0) {
			this.panelOnOrOff();
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.sichtlieferantenterminepositionnichtveraenderbar"));
			return;
		} else if (getBestellpositionDto().getTAuftragsbestaetigungstermin() == null) {
			this.panelOnOrOff();
			DialogFactory
					.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("bes.sichtlieferantenterminbereitsgeloescht"));
			return;
		} else {
			DelegateFactory.getInstance().getBestellungDelegate()
					.removeABTerminVonBestellposition(getBestellpositionDto());
		}

		super.eventActionDelete(e, true, false);
		this.panelOnOrOff();
	}

	/**
	 * schaltet die panel wep und we richtig aufruf in eventActionDelete und
	 * eventActionUpdate
	 * 
	 * @throws Throwable
	 */
	private void panelOnOrOff() throws Throwable {
		tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
	}

	protected void components2Dto() throws Throwable {
		getBestellpositionDto().setCABKommentar(this.lpeKommentar.getText());
		getBestellpositionDto().setCABNummer(this.wtfABNummer.getText());
		getBestellpositionDto().setTAbBelegdatum(this.wdfABBelegdatum.getDate());

		if (getBestellpositionDto().getTAbursprungstermin() == null
				&& getBestellpositionDto().getTAuftragsbestaetigungstermin() != null
				&& bMitUrsung == true) {
			getBestellpositionDto().setTAbursprungstermin(
					new Timestamp(getBestellpositionDto()
							.getTAuftragsbestaetigungstermin().getTime()));
		}

		getBestellpositionDto().setTAuftragsbestaetigungstermin(
				this.wdfABTermin.getDate());
		if (this.wdfABTermin.getDate() != null) {
			if (getBestellpositionDto().getTAbursprungstermin() != null) {
				if (wcbUrsprungterminMitaendern.isSelected()) {
					getBestellpositionDto().setTAbursprungstermin(
							this.wdfABTermin.getTimestamp());
				}
			} else {
				getBestellpositionDto().setTAbursprungstermin(
						this.wdfABTermin.getTimestamp());
			}
		}
	}

	protected void dto2Components() throws Throwable {
		lpeKommentar.setText(getBestellpositionDto().getCABKommentar());
		wtfABNummer.setText(getBestellpositionDto().getCABNummer());
		wdfABTermin.setDate(getBestellpositionDto()
				.getTAuftragsbestaetigungstermin());
		wdfABBelegdatum.setDate(getBestellpositionDto()
				.getTAbBelegdatum());
		wdfABUrsprungtermin.setDate(getBestellpositionDto()
				.getTAbursprungstermin());

		if (getBestellpositionDto().getTLieferterminbestaetigt() != null) {
			String s = LPMain
					.getTextRespectUISPr("bes.liefertermin.bestaetigtam")
					+ " "
					+ Helper.formatDatumZeit(getBestellpositionDto()
							.getTLieferterminbestaetigt(), LPMain
							.getTheClient().getLocUi());
			if (getBestellpositionDto().getPersonalIIdLieferterminbestaetigt() != null) {

				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								getBestellpositionDto()
										.getPersonalIIdLieferterminbestaetigt());
				s += " (" + personalDto.getCKurzzeichen() + ")";
			}
			wlaLieferterminbestaetigt.setText(s);

		}
		if (getBestellpositionDto().getTUebersteuerterLiefertermin() != null) {
			String s = LPMain.getTextRespectUISPr("bes.wunschliefertermin")
					+ " "
					+ Helper.formatDatum(getBestellpositionDto()
							.getTUebersteuerterLiefertermin(), LPMain
							.getTheClient().getLocUi());

			wlaLieferterminausBestellposition.setText(s);

		}

		String sPositionsart = getBestellpositionDto().getPositionsartCNr();
		if (sPositionsart.equals(BestellpositionFac.BESTELLPOSITIONART_IDENT)
				|| sPositionsart
						.equals(BestellpositionFac.BESTELLPOSITIONART_HANDEINGABE)) {
			ArtikelDto artikelDto = DelegateFactory
					.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(
							getBestellpositionDto().getArtikelIId());
			wbuArtikelGoto.setOKey(artikelDto.getIId());
			wtfIdent.setText(artikelDto.getCNr());
			wtfBezeichnung.setText(artikelDto.getArtikelsprDto().getCBez());
			wtfZusatzBezeichnung.setText(artikelDto.getArtikelsprDto()
					.getCZbez());
		} else {
			wbuArtikelGoto.setOKey(null);
			wtfIdent.setText(null);
			wtfBezeichnung.setText(null);
			wtfZusatzBezeichnung.setText(null);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();

			if (getBestellpositionDto().getTAuftragsbestaetigungstermin() != null) {
				if (Helper.cutDate(
						getBestellpositionDto()
								.getTAuftragsbestaetigungstermin()).before(
						Helper.cutDate(tpBestellung.getBesDto()
								.getDBelegdatum()))) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("best.hint.abterminvorbelegtermin"));
				}
			}

			wcbUrsprungterminMitaendern.setSelected(false);
			if (getBestellpositionDto().getCABKommentar() != null
					&& getBestellpositionDto().getCABKommentar().length() > SystemFac.MAX_LAENGE_EDITORTEXT) {
				tpBestellung.showStatusMessage("lp.hint",
						"lp.error.editor.laengeueberschritten");
			} else {

				DelegateFactory
						.getInstance()
						.getBestellungDelegate()
						.updateBestellpositionMitABTermin(
								getBestellpositionDto(), "");
				super.eventActionSave(e, true);
				eventYouAreSelected(false);
				tpBestellung.enablePanels(tpBestellung.getBesDto(), true);
			}
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		// nothing here
	}

	private void jbInit() throws Throwable {

		try {
			bMitUrsung = (Boolean) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(
							LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_BESTELLUNG,
							ParameterFac.PARAMETER_URSPRUNGSTERMIN_FUER_LIEFERANTENBEURTEILUNG)
					.getCWertAsObject();
		} catch (Throwable e) {
		}
		wlaKommentar = new WrapperLabel();
		lpeKommentar = new WrapperEditorFieldKommentar(getInternalFrame(), "");

		wlaABTermin = new WrapperLabel();
		wdfABTermin = new WrapperDateField();
		
		wlaABBelegdatum = new WrapperLabel();
		wdfABBelegdatum = new WrapperDateField();

		wlaABUrsprungtermin = new WrapperLabel();
		wdfABUrsprungtermin = new WrapperDateField();

		wcbUrsprungterminMitaendern = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("bes.usprungterminmitaendern"));

		wlaABNummer = new WrapperLabel();
		wtfABNummer = new WrapperTextField();

		wbuArtikelGoto = new WrapperGotoButton(
				com.lp.util.GotoHelper.GOTO_ARTIKEL_AUSWAHL);
		wbuArtikelGoto.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer"));
		wbuArtikelGoto.setActivatable(false);
		wtfIdent = new WrapperTextField();

		
		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();
		
		wlaBezeichnung = new WrapperLabel();
		wtfBezeichnung = new WrapperTextField(iLaengenBezeichung);

		wlaZusatzBezeichnung = new WrapperLabel();
		wtfZusatzBezeichnung = new WrapperTextField(iLaengenBezeichung);

		getInternalFrame().addItemChangedListener(this);

		String[] aWhichButtonIUse = null;
		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_REKLA_QUALITAETSSICHERUNG_CUD)) {
			createAndSaveAndShowButton("/com/lp/client/res/date-time.png",
					LPMain.getTextRespectUISPr("best.abtermin.qsaendern"),
					ACTION_SPECIAL_ABTERMIN_AENDERN,
					RechteFac.RECHT_REKLA_QUALITAETSSICHERUNG_CUD);

			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD,
					ACTION_SPECIAL_ABTERMIN_AENDERN,
					ACTION_SPECIAL_LIEFERTERMIN_BESTAETIGT };
		} else {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DELETE, ACTION_DISCARD };
		}

		createAndSaveAndShowButton("/com/lp/client/res/data_ok.png",
				LPMain.getTextRespectUISPr("bes.liefertermin.bestaetigen"),
				ACTION_SPECIAL_LIEFERTERMIN_BESTAETIGT, null);

		enableToolsPanelButtons(aWhichButtonIUse);

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// das Aussenpanel hat immer das Gridbaglayout.
		setLayout(new GridBagLayout());

		// Actionpanel von Oberklasse holen und anhaengen.
		panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		wbuArtikelGoto.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wbuArtikelGoto.setPreferredSize(new Dimension(120, Defaults
				.getInstance().getControlHeight()));
		wbuArtikelGoto.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wtfIdent.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wtfIdent.setActivatable(false);
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setActivatable(false);
		wlaZusatzBezeichnung.setText(LPMain
				.getTextRespectUISPr("lp.zusatzbezeichnung"));
		wtfZusatzBezeichnung.setActivatable(false);
		wlaABNummer.setText(LPMain.getTextRespectUISPr("bes.abnummer"));
		wtfABNummer.setColumnsMax(20);
		wtfABNummer.setMandatoryField(true);

		wlaABTermin.setText(LPMain.getTextRespectUISPr("bes.abtermin"));
		wdfABTermin.setMandatoryField(true);
		
		wlaABBelegdatum.setText(LPMain.getTextRespectUISPr("bes.abbelegdatum"));
		

		wdfABUrsprungtermin.setEnabled(false);
		wdfABUrsprungtermin.getCalendarButton().setVisible(false);
		wdfABUrsprungtermin.setBwithRubber(false);

		wlaABUrsprungtermin.setText(LPMain
				.getTextRespectUISPr("bes.ursprungstermin"));

		wlaKommentar.setText(LPMain.getTextRespectUISPr("bes.abkommentar"));
		// wtaKommentar.setRows(4);
		// wtaKommentar.setColumns(19);
		// lpeKommentar.getTextBlockAttributes(-1).capacity =
		// SystemFac.MAX_LAENGE_EDITORTEXT;

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		jpaWorkingOn.setLayout(new GridBagLayout());
		add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// ab hier einhaengen.
		// Zeile.
		jpaWorkingOn.add(wbuArtikelGoto, new GridBagConstraints(0, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(this.wtfIdent, new GridBagConstraints(1, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(this.wlaBezeichnung, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(this.wtfBezeichnung, new GridBagConstraints(1, iZeile,
				3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLieferterminausBestellposition,
				new GridBagConstraints(4, iZeile, 2, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						250, 0));

		iZeile++;
		jpaWorkingOn.add(this.wlaZusatzBezeichnung, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(this.wtfZusatzBezeichnung, new GridBagConstraints(1,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaLieferterminbestaetigt, new GridBagConstraints(4,
				iZeile, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(this.wlaABTermin, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(this.wdfABTermin, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (bMitUrsung) {
			jpaWorkingOn.add(this.wlaABUrsprungtermin,
					new GridBagConstraints(2, iZeile, 1, 1, 0.2, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 50, 0));
			jpaWorkingOn.add(this.wdfABUrsprungtermin,
					new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(this.wcbUrsprungterminMitaendern,
					new GridBagConstraints(4, iZeile, 1, 1, 0.5, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		iZeile++;
		
		jpaWorkingOn.add(this.wlaABBelegdatum, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(this.wdfABBelegdatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(this.wlaABNummer, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		jpaWorkingOn.add(this.wtfABNummer, new GridBagConstraints(3, iZeile, 5,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(this.wlaKommentar, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(this.lpeKommentar, new GridBagConstraints(1, iZeile,
				5, 10, 0.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BESTELLUNG;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		getInternalFrameBestellung().getTabbedPaneBestellung().setTitle();
		Object key = getKeyWhenDetailPanel();
		wlaLieferterminbestaetigt.setText(null);
		wlaLieferterminausBestellposition.setText(null);
		if (key != null && !key.equals(LPMain.getLockMeForNew())) {
			setBestellpositionDto(DelegateFactory.getInstance()
					.getBestellungDelegate()
					.bestellpositionFindByPrimaryKey((Integer) key));
			// Update
			dto2Components();
			setStatusbar();
		}
		tpBestellung.enablePanels(tpBestellung.getBesDto(), false);
	}

	// hier erfolgt setting der statusbar (lt. felder in der db)
	protected void setStatusbar() throws Throwable {
		setStatusbarPersonalIIdAendern(bestellpositionDto
				.getPersonalIIdAbterminAendern());
		setStatusbarTAendern(bestellpositionDto.getTAbterminAendern());
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfABNummer;
	}
}
