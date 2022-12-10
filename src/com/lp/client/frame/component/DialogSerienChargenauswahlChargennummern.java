
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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.NumberColumnFormat;
import com.lp.client.frame.PanelAdditiveVerpackungsmengen;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.bestellung.service.BestellpositionDto;
import com.lp.server.bestellung.service.WareneingangDto;
import com.lp.server.bestellung.service.WareneingangspositionDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.PaneldatenDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.BigDecimal3;
import com.lp.util.BigDecimal4;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogSerienChargenauswahlChargennummern extends JDialog
		implements ActionListener, KeyListener, TableModelListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private String ACTION_NEW_FROM_LAGER = "action_new_from_lager";
	private String ACTION_DELETE = "action_delete";
	private String ACTION_ADD_FROM_HAND = "ACTION_ADD_FROM_HAND";
	private String ACTION_NEUER_SNR_VORSCHLAG = "ACTION_NEUER_SNR_VORSCHLAG";
	private String ACTION_RUECKGABE = "action_rueckgabe";

	ArtikelDto artikelDto = null;
	JPanel panel1 = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public List<SeriennrChargennrMitMengeDto> alSeriennummernReturn = null;

	JScrollPane jScrollPane1 = new JScrollPane();
	WrapperTableEditable jTableSnrChnrs = new WrapperTableEditable();
	JLabel wlaVersion = new JLabel(LPMain.getTextRespectUISPr("artikel.lager.version"));
	JButton jButtonUebernehmen = new JButton();
	JLabel jLabelGesamtMenge = new JLabel();
	JLabel jLabelBenoetigt = new JLabel();
	WrapperNumberField wnfSnrchnr = null;

	public WrapperTextField wtfVersion = new WrapperTextField(40);
	public WrapperSNRField tfSnrchnr = null;
	public WrapperCheckBox wcbRueckgabe = new WrapperCheckBox();
	public WrapperCheckBox wcbAutomatik = new WrapperCheckBox();
	private WrapperDateField wdfMHD = new WrapperDateField();

	String[] colNames = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummernBereitsSelektiert = null;
	Integer artikelIId = null;
	Integer lagerIId = null;
	public Integer gebindeIId = null;
	private BigDecimal nGebindemenge = null;
	InternalFrame internalFrame = null;
	private boolean bMindesthaltbarkeitsdatum = false;
	private boolean bVersionMitangeben = false;
	private boolean bFuehrendeNullenWegschneiden = false;
	private boolean bZugang = false;
	boolean selektierteNichtAnzeigen = false;
	private int automatischeChargennummerBeiWEPOS = 0;

	private int automatischeChargennummerBeiLosablieferung = 0;

	private BigDecimal bdVerpackungsmenge = null;

	int iNachkommastelleMenge = 3;
	private BigDecimal bdBenoetigteMenge = null;

	public boolean bAbbruch = false;

	private PaneldatenDto[] letzte_panelDatenDtos = null;

	public void setBdBenoetigteMenge(BigDecimal bdBenoetigteMenge) throws Throwable {
		this.bdBenoetigteMenge = bdBenoetigteMenge;
		getLabelBenoetigt()
				.setText("Ben\u00F6tigt: " + Helper.formatZahl(bdBenoetigteMenge, 4, LPMain.getTheClient().getLocUi()));

	}

	public void setBdOffeneMenge(BigDecimal bdOffen) throws Throwable {

		getLabelBenoetigt().setText("offene Menge: " + Helper.formatZahl(bdOffen, 4, LPMain.getTheClient().getLocUi()));

	}

	public JLabel getLabelBenoetigt() {
		return jLabelBenoetigt;
	}

	public DialogSerienChargenauswahlChargennummern(Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennummern, boolean bMultiselection,
			boolean selektierteNichtAnzeigen, InternalFrame internalFrame, WrapperNumberField wnfBeleg, boolean bZugang,
			Integer gebindeIId, BigDecimal bdGebindemenge) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.selektierteNichtAnzeigen = selektierteNichtAnzeigen;
		this.bZugang = bZugang;
		this.gebindeIId = gebindeIId;
		this.nGebindemenge = bdGebindemenge;

		iNachkommastelleMenge = Defaults.getInstance().getIUINachkommastellenMenge();

		if (alSeriennummern != null) {

			if (selektierteNichtAnzeigen == true) {
				alSeriennummernBereitsSelektiert = new ArrayList<SeriennrChargennrMitMengeDto>(alSeriennummern);
				this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
			} else {
				this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>(alSeriennummern);
			}

		} else {
			this.alSeriennummern = new ArrayList<SeriennrChargennrMitMengeDto>();
			alSeriennummernBereitsSelektiert = new ArrayList<SeriennrChargennrMitMengeDto>();
		}

		this.artikelIId = artikelIId;
		this.lagerIId = lagerIId;
		this.internalFrame = internalFrame;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_CHARGENNUMMER_BEINHALTET_MINDESTHALTBARKEITSDATUM,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bMindesthaltbarkeitsdatum = true;
		}
		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bFuehrendeNullenWegschneiden = true;
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_VERSION_BEI_CHNR_MITANGEBEN, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getInstance().getTheClient().getMandant());

		bVersionMitangeben = (Boolean) parameter.getCWertAsObject();

		artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_WEP, ParameterFac.KATEGORIE_BESTELLUNG,
				LPMain.getInstance().getTheClient().getMandant());

		automatischeChargennummerBeiWEPOS = (Integer) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_AUTOMATISCHE_CHARGENNUMMER_BEI_LOSABLIEFERUNG, ParameterFac.KATEGORIE_FERTIGUNG,
				LPMain.getInstance().getTheClient().getMandant());

		automatischeChargennummerBeiLosablieferung = (Integer) parameter.getCWertAsObject();

		// Wenn Verpackungsmengen hinterlegt sind, dann auch die Umrechnung dazu
		// anzeigen
		if (internalFrame instanceof InternalFrameBestellung) {

			ArtikellieferantDto artliefDto = DelegateFactory.getInstance().getArtikelDelegate().getArtikelEinkaufspreis(
					artikelIId,
					((InternalFrameBestellung) internalFrame).getTabbedPaneBestellung().getBesDto()
							.getLieferantIIdBestelladresse(),
					new BigDecimal(1), LPMain.getTheClient().getSMandantenwaehrung(),
					new java.sql.Date(((InternalFrameBestellung) internalFrame).getTabbedPaneBestellung().getBesDto()
							.getDBelegdatum().getTime()));

			if (artliefDto != null) {

				if (artliefDto.getNVerpackungseinheit() != null
						&& artliefDto.getNVerpackungseinheit().doubleValue() > 0) {

					bdVerpackungsmenge = artliefDto.getNVerpackungseinheit();
				}

			}

			// lt. SP4786: Wenn keine Verpackungsmenge in Lief1, dann default
			// aus Artikel
			if (bdVerpackungsmenge == null && artikelDto.getFVerpackungsmenge() != null
					&& artikelDto.getFVerpackungsmenge() > 0) {
				bdVerpackungsmenge = new BigDecimal(artikelDto.getFVerpackungsmenge());
			}

		} else {
			if (artikelDto.getFVerpackungsmenge() != null && artikelDto.getFVerpackungsmenge() > 0) {
				bdVerpackungsmenge = new BigDecimal(artikelDto.getFVerpackungsmenge());
			}
		}

		jbInit();

		this.setSize(1000, 500);
		this.setMinimumSize(new Dimension(800, 500));
		refreshFromArrayList();

		if (wnfBeleg != null && wnfBeleg.getBigDecimal() != null && wnfBeleg.getBigDecimal().doubleValue() < 0) {
			wcbRueckgabe.setSelected(true);
		}
		jTableSnrChnrs.setRowSelectionAllowed(true);

		if (bMultiselection) {
			jTableSnrChnrs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			jTableSnrChnrs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}

		jScrollPane.getViewport().add(jTableSnrChnrs);

		if (jTableSnrChnrs.getModel().getRowCount() > 0) {
			jTableSnrChnrs.changeSelection(0, 0, false, false);
		}
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				bAbbruch = true;
				setVisible(false);
				dispose();
			}
		});
		tfSnrchnr.requestFocusInWindow();

		SwingUtilities.invokeLater(new RequestFocusLater(tfSnrchnr));
		jButtonUebernehmen.grabFocus();
	}

	private static class RequestFocusLater implements Runnable {
		private Component comp;

		RequestFocusLater(Component src) {
			comp = src;
		}

		public void run() {
			comp.requestFocusInWindow();
		}
	}

	public void tableChanged(TableModelEvent e) {

		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1 || e.getColumn() == 2) {
			// Mengen updaten
			if (e.getSource().equals(jTableSnrChnrs.getModel())) {
				for (int i = 0; i < alSeriennummern.size(); i++) {

					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof String) {

						if (bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) {
							alSeriennummern.get(i).setCVersion((String) jTableSnrChnrs.getModel().getValueAt(i, 1));

							if (jTableSnrChnrs.getModel().getValueAt(i, 2) instanceof Long) {
								alSeriennummern.get(i)
										.setNMenge(Helper.rundeKaufmaennisch(
												new BigDecimal4((Long) jTableSnrChnrs.getModel().getValueAt(i, 2)),
												iNachkommastelleMenge));
							}
							
							if (jTableSnrChnrs.getModel().getValueAt(i, 2) instanceof Double) {
								alSeriennummern.get(i)
										.setNMenge(Helper.rundeKaufmaennisch(
												new BigDecimal4((Double) jTableSnrChnrs.getModel().getValueAt(i, 2)),
												iNachkommastelleMenge));
							}
						} else {
							alSeriennummern.get(i)
									.setNMenge(new BigDecimal4((String) jTableSnrChnrs.getModel().getValueAt(i, 1)));
						}

					}
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof BigDecimal) {
						alSeriennummern.get(i).setNMenge((BigDecimal) jTableSnrChnrs.getModel().getValueAt(i, 1));
					}
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof Double) {

						alSeriennummern.get(i)
								.setNMenge(Helper.rundeKaufmaennisch(
										new BigDecimal4((Double) jTableSnrChnrs.getModel().getValueAt(i, 1)),
										iNachkommastelleMenge));

					}
					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof Long) {
						alSeriennummern.get(i)
								.setNMenge(Helper.rundeKaufmaennisch(
										new BigDecimal4((Long) jTableSnrChnrs.getModel().getValueAt(i, 1)),
										iNachkommastelleMenge));
					}
				}
			}

			refreshFromArrayList();

			jTableSnrChnrs.changeSelection(e.getFirstRow(), e.getColumn(), false, false);
			requestFocusInWindow();
		}

	}

	public boolean add2List(SeriennrChargennrMitMengeDto dto) throws Throwable {
		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_ARTIKEL_MINDESTLAENGE_SERIENNUMMER);

			Integer ichnrlaenge = (Integer) parameter.getCWertAsObject();
			boolean bAusArtikelSonstiges = false;
			// PJ20380
			if (artikelDto.getILaengeminSnrchnr() != null) {
				ichnrlaenge = artikelDto.getILaengeminSnrchnr();
				bAusArtikelSonstiges = true;
			}

			if (dto.getCSeriennrChargennr().length() < ichnrlaenge) {

				if (bAusArtikelSonstiges) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.error.snrzukurz.artikelsonstiges") + " " + ichnrlaenge);
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.error.snrzukurz") + " " + ichnrlaenge);
				}

				return false;

			}

			parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
					ParameterFac.PARAMETER_ARTIKEL_MAXIMALELAENGE_SERIENNUMMER, ParameterFac.KATEGORIE_ARTIKEL,
					LPMain.getInstance().getTheClient().getMandant());

			Integer iLaengeSnr = (Integer) parameter.getCWertAsObject();

			// PJ20380

			if (artikelDto.getILaengemaxSnrchnr() != null) {
				iLaengeSnr = artikelDto.getILaengemaxSnrchnr();
				bAusArtikelSonstiges = true;
			}

			if (dto.getCSeriennrChargennr().length() > iLaengeSnr) {
				if (bAusArtikelSonstiges) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.error.snrzulang.artikelsonstiges") + " " + iLaengeSnr
									+ "  (" + dto.getCSeriennrChargennr() + ")");
				} else {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("artikel.error.snrzulang") + " " + iLaengeSnr + "  ("
									+ dto.getCSeriennrChargennr() + ")");
				}
				return false;

			}

			// Pruefen, ob bereits vorhanden
			for (int i = 0; i < alSeriennummern.size(); i++) {

				if (alSeriennummern.get(i).getCSeriennrChargennr().equals(dto.getCSeriennrChargennr())) {
					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("artikel.snr.doppelt") + " "
									+ alSeriennummern.get(i).getCSeriennrChargennr());
					return false;
				}

			}

			// SP4006
			// Wenn Zugang, dann pruefen, ob SNR schon einmal verwendet wurde
			if (bZugang) {

				BelegInfos bi = DelegateFactory.getInstance().getLagerDelegate()
						.wurdeSeriennummerSchonEinmalVerwendet(artikelDto.getIId(), dto.getCSeriennrChargennr());

				if (bi != null) {
					boolean b = DialogFactory.showModalJaNeinDialog(internalFrame,

							LPMain.getTextRespectUISPr("artikel.snr.bereitsverwendet") + (bi.getBelegart() + "").trim()
									+ " " + bi.getBelegnummer() + " " + bi.getBelegbezeichnung() + " "
									+ Helper.formatDatum(bi.getBelegdatum(), LPMain.getTheClient().getLocUi()),
							LPMain.getTextRespectUISPr("lp.hint"));

					if (b == false) {
						return false;
					}

				}

			}

			alSeriennummern.add(dto);

			BigDecimal bdAusgewaehlt = refreshFromArrayList();

			// Focus in Zelle setzen
			jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel().getRowCount() - 1, 0, false, false);

			if (jTableSnrChnrs.getModel().getRowCount() > 0) {

				jTableSnrChnrs.requestFocus();
				DefaultCellEditor ed = (DefaultCellEditor) jTableSnrChnrs
						.getCellEditor(jTableSnrChnrs.getModel().getRowCount() - 1, 0);

				ed.shouldSelectCell(new ListSelectionEvent(this, jTableSnrChnrs.getModel().getRowCount() - 1,
						jTableSnrChnrs.getModel().getRowCount() - 1, true));

			}

			// PJ18588
			if (wcbAutomatik.isSelected() && bdBenoetigteMenge != null && bdAusgewaehlt != null
					&& bdAusgewaehlt.doubleValue() >= bdBenoetigteMenge.doubleValue()) {
				alSeriennummernReturn = alSeriennummern;
				setVisible(false);
			}

		} else {
			ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
					LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ARTIKEL,
					ParameterFac.PARAMETER_CHARGENNUMMER_MINDESTLAENGE);

			Integer ichnrlaenge = (Integer) parameter.getCWertAsObject();
			if (dto.getCSeriennrChargennr().length() < ichnrlaenge) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("artikel.error.chnrzukurz") + " " + ichnrlaenge);
				return false;

			}

			// PJ18452 Nur bei Chargen
			ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(artikelIId);

			if (bZugang) {
				if (artikelDto.isChargennrtragend()) {
					PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
							.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
									artikelDto.getArtgruIId());
					if (dtos.length > 0) {

						// SP4129 Wenn es f�r die Chargennummer bereits
						// Eigenschaften gibt, dann die vorhandenen verwenden
						// und gar nicht mehr danach fragen

						PaneldatenDto[] panelDatenvorhanden = DelegateFactory.getInstance().getLagerDelegate()
								.getLetzteChargeninfosEinesArtikels(artikelIId, null, null, null,
										dto.getCSeriennrChargennr());

						if (panelDatenvorhanden != null && panelDatenvorhanden.length > 0) {
							dto.setPaneldatenDtos(panelDatenvorhanden);
						} else {
							DialogDynamischChargeneigenschaften d = new DialogDynamischChargeneigenschaften(artikelDto,
									internalFrame, dto);
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

							d.setVisible(true);

							if (d.bAbbruch == true) {
								return false;
							}
							// PJ19189
							if (dto != null) {
								letzte_panelDatenDtos = dto.getPaneldatenDtos();
							}

						}

					}

					// PJ19367
					// Wenn Zugang, dann pruefen, ob CHNR schon einmal verwendet
					// wurde
					if (bZugang) {

						BelegInfos bi = DelegateFactory.getInstance().getLagerDelegate()
								.wurdeChargennummerSchonEinmalVerwendet(artikelDto.getIId(),
										dto.getCSeriennrChargennr());

						if (bi != null) {
							boolean b = DialogFactory.showModalJaNeinDialog(internalFrame,

									LPMain.getTextRespectUISPr("artikel.chnr.bereitsverwendet")
											+ (bi.getBelegart() + "").trim() + " " + bi.getBelegnummer() + " "
											+ bi.getBelegbezeichnung() + " "
											+ Helper.formatDatum(bi.getBelegdatum(), LPMain.getTheClient().getLocUi()),
									LPMain.getTextRespectUISPr("lp.hint"));

							if (b == false) {
								return false;
							}

						}

					}

				}
			}

			if (bZugang && hatGebindehinterlegt() && Helper.short2Boolean(artikelDto.getBChargennrtragend())) {

				for (int i = 0; i < wnfSnrchnr.getInteger(); i++) {

					SeriennrChargennrMitMengeDto dtoJeGebinde = new SeriennrChargennrMitMengeDto(
							dto.getCSeriennrChargennr() + "-" + (i + 1), nGebindemenge, gebindeIId, nGebindemenge);

					alSeriennummern.add(dtoJeGebinde);

				}

			} else {

				if (Helper.short2Boolean(artikelDto.getBChargennrtragend()) && automatischeChargennummerBeiWEPOS == 1
						&& internalFrame instanceof InternalFrameBestellung) {
					// PJ21681

					InternalFrameBestellung intBest = (InternalFrameBestellung) internalFrame;

					ArtikellieferantDto alDto = DelegateFactory.getInstance().getArtikelDelegate()
							.getArtikelEinkaufspreis(artikelDto.getIId(),
									intBest.getTabbedPaneBestellung().getBesDto().getLieferantIIdBestelladresse(),
									BigDecimal.ONE, intBest.getTabbedPaneBestellung().getBesDto().getWaehrungCNr(),
									intBest.getTabbedPaneBestellung().getBesDto().getDBelegdatum());
					if (alDto != null && alDto.getNVerpackungseinheit() != null
							&& alDto.getNVerpackungseinheit().doubleValue() > 0) {

						BigDecimal bdMenge = dto.getNMenge();

						Integer iZaehler = 0;

						if (alSeriennummern != null && alSeriennummern.size() > 0) {
							String letzteChhnr = alSeriennummern.get(alSeriennummern.size() - 1)
									.getCSeriennrChargennr();

							String sZaehlerVorhanden = letzteChhnr.substring(letzteChhnr.length() - 2);

							try {
								iZaehler = new Integer(sZaehlerVorhanden);
								iZaehler++;

							} catch (NumberFormatException e) {
								// Dann wars keine zahl
							}

						}

						while (bdMenge.doubleValue() > 0) {

							SeriennrChargennrMitMengeDto dtoNew = new SeriennrChargennrMitMengeDto();

							dtoNew.setCSeriennrChargennr(tfSnrchnr.getText() + "-"
									+ Helper.fitString2LengthAlignRight(iZaehler + "", 2, '0'));
							if (bdMenge.subtract(alDto.getNVerpackungseinheit()).doubleValue() > 0) {

								dtoNew.setNMenge(alDto.getNVerpackungseinheit());
								alSeriennummern.add(dtoNew);
							} else {
								dtoNew.setNMenge(bdMenge);
								alSeriennummern.add(dtoNew);
							}
							iZaehler++;
							bdMenge = bdMenge.subtract(alDto.getNVerpackungseinheit());

						}

					} else {
						alSeriennummern.add(dto);
					}

				} else {
					alSeriennummern.add(dto);
				}

			}

			refreshFromArrayList();
			// Focus in Zelle setzen
			jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel().getRowCount() - 1, 1, false, false);

			if (jTableSnrChnrs.getModel().getRowCount() > 0) {
				jTableSnrChnrs.editCellAt(jTableSnrChnrs.getModel().getRowCount() - 1, 1);

				jTableSnrChnrs.requestFocus();
				DefaultCellEditor ed = (DefaultCellEditor) jTableSnrChnrs
						.getCellEditor(jTableSnrChnrs.getModel().getRowCount() - 1, 1);

				ed.shouldSelectCell(new ListSelectionEvent(this, jTableSnrChnrs.getModel().getRowCount() - 1,
						jTableSnrChnrs.getModel().getRowCount() - 1, true));

			}

		}

		return true;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_DELETE)) {

				int[] zeilen = jTableSnrChnrs.getSelectedRows();
				for (int i = zeilen.length; i > 0; i--) {
					alSeriennummern.remove(zeilen[i - 1]);

				}
				refreshFromArrayList();

				jTableSnrChnrs.changeSelection(jTableSnrChnrs.getModel().getRowCount() - 1,
						jTableSnrChnrs.getModel().getColumnCount(), false, false);

				
				tfSnrchnr.requestFocus();
				
			} else if (e.getActionCommand().equals(ACTION_ADD_FROM_HAND)) {

				if (bMindesthaltbarkeitsdatum == true && Helper.short2Boolean(artikelDto.getBChargennrtragend())) {
					if (tfSnrchnr.getText() == null || wnfSnrchnr.getBigDecimal() == null
							|| wdfMHD.getTimestamp() == null) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}
				} else {
					if (tfSnrchnr.getText() == null || wnfSnrchnr.getBigDecimal() == null) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
						return;
					}

					if (automatischeChargennummerBeiWEPOS == 2) {

						if (bZugang == true) {
							if (tfSnrchnr.getText() == null || wnfSnrchnr.getBigDecimal() == null
									|| wtfVersion.getText() == null) {

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
								return;
							}
						} else {
							if (tfSnrchnr.getText() == null || wnfSnrchnr.getBigDecimal() == null) {

								DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
								return;
							}
						}

					}

				}

				if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

					String[] snrs = tfSnrchnr.erzeugeSeriennummernArray(null, false);

					for (int i = 0; i < snrs.length; i++) {

						if (bFuehrendeNullenWegschneiden) {
							snrs[i] = snrs[i].replaceFirst("0*", "");
						}

						boolean bHatFunktioniert = add2List(
								new SeriennrChargennrMitMengeDto(snrs[i], wtfVersion.getText(), new BigDecimal(1)));
						if (bHatFunktioniert == false) {
							break;
						}
					}
				} else {

					if (bMindesthaltbarkeitsdatum == true) {

						StringBuffer s = new StringBuffer();

						Calendar c = Calendar.getInstance();
						c.setTimeInMillis(wdfMHD.getTimestamp().getTime());

						int iTag = c.get(Calendar.DAY_OF_MONTH);
						int iMonat = c.get(Calendar.MONTH) + 1;
						int iJahr = c.get(Calendar.YEAR);

						s.append(iJahr);

						if (iMonat < 10) {
							s.append("0").append(iMonat);
						} else {
							s.append(iMonat);
						}
						if (iTag < 10) {
							s.append("0").append(iTag);
						} else {
							s.append(iTag);
						}

						if (tfSnrchnr.getText() != null) {
							s.append(tfSnrchnr.getText());
						}
						add2List(new SeriennrChargennrMitMengeDto(new String(s), wnfSnrchnr.getBigDecimal()));
					} else {
						// PJ19071 Chargeneigenschaften aus letzter Bewegung

						SeriennrChargennrMitMengeDto snrDto = new SeriennrChargennrMitMengeDto(tfSnrchnr.getText(),
								wnfSnrchnr.getBigDecimal(), gebindeIId, nGebindemenge);

						// PJ19613
						if (bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) {
							snrDto.setCVersion(wtfVersion.getText());
						}

						if (letzte_panelDatenDtos == null) {

							if (internalFrame instanceof InternalFrameBestellung) {

								WareneingangDto weDto = ((InternalFrameBestellung) internalFrame)
										.getTabbedPaneBestellung().getWareneingangDto();

								if (weDto != null) {

									WareneingangspositionDto[] weposDtos = DelegateFactory.getInstance()
											.getWareneingangDelegate()
											.wareneingangspositionFindByWareneingangIId(weDto.getIId());

									for (int i = weposDtos.length - 1; i >= 0; i--) {

										BestellpositionDto bsPos = DelegateFactory.getInstance().getBestellungDelegate()
												.bestellpositionFindByPrimaryKey(weposDtos[i].getBestellpositionIId());

										if (bsPos.getArtikelIId() != null && bsPos.getArtikelIId().equals(artikelIId)) {

											PaneldatenDto[] panelDatenVorschlag = DelegateFactory.getInstance()
													.getLagerDelegate().getLetzteChargeninfosEinesArtikels(artikelIId,
															LocaleFac.BELEGART_BESTELLUNG, weDto.getBestellungIId(),
															weposDtos[i].getIId(), null);

											if (panelDatenVorschlag != null && panelDatenVorschlag.length > 0) {
												snrDto.setPaneldatenDtos(panelDatenVorschlag);
											}

											break;
										}

									}

								}

							} else if (internalFrame instanceof InternalFrameArtikel) {

								PaneldatenDto[] panelDatenVorschlag = DelegateFactory.getInstance().getLagerDelegate()
										.getLetzteChargeninfosEinesArtikels(artikelIId, LocaleFac.BELEGART_HAND, null,
												null, null);

								if (panelDatenVorschlag != null && panelDatenVorschlag.length > 0) {
									snrDto.setPaneldatenDtos(panelDatenVorschlag);
								}

							} else if (internalFrame instanceof InternalFrameFertigung) {

								LosDto losDto = ((InternalFrameFertigung) internalFrame).getTabbedPaneLos().getLosDto();

								if (losDto != null) {

									PaneldatenDto[] panelDatenVorschlag = DelegateFactory.getInstance()
											.getLagerDelegate().getLetzteChargeninfosEinesArtikels(artikelIId,
													LocaleFac.BELEGART_LOSABLIEFERUNG, losDto.getIId(), null, null);

									if (panelDatenVorschlag != null && panelDatenVorschlag.length > 0) {
										snrDto.setPaneldatenDtos(panelDatenVorschlag);
									}

								}

							}

						} else {
							snrDto.setPaneldatenDtos(letzte_panelDatenDtos);
						}

						add2List(snrDto);
					}

				}

				String letzteChnr = tfSnrchnr.getText();

				tfSnrchnr.setText(null);

				// PJ19189
				if (bZugang && letzteChnr != null) {
					if (artikelDto.isChargennrtragend()) {
						PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
								.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
										artikelDto.getArtgruIId());
						if (dtos.length > 0) {

							tfSnrchnr.setText(DelegateFactory.getInstance().getLagerDelegate()
									.getNaechsteChargennummer(artikelDto.getIId(), letzteChnr));
						}
					}
				}

				wtfVersion.setText(null);

			} else if (e.getActionCommand().equals(ACTION_NEW_FROM_LAGER)) {

				DialogSnrChnrauswahl d = null;
				if (selektierteNichtAnzeigen == true) {
					d = new DialogSnrChnrauswahl(artikelIId, lagerIId, true, alSeriennummernBereitsSelektiert);

				} else {
					d = new DialogSnrChnrauswahl(artikelIId, lagerIId, true, alSeriennummern);
				}

				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);

				d.setVisible(true);
				List<SeriennrChargennrMitMengeDto> s = d.alSeriennummern;

				for (int i = 0; i < s.size(); i++) {
					add2List(s.get(i));
				}

			} else if (e.getActionCommand().equals(ACTION_NEUER_SNR_VORSCHLAG)) {

				if (automatischeChargennummerBeiWEPOS == 1 && internalFrame instanceof InternalFrameBestellung) {

					InternalFrameBestellung iternalFrameBestellung = (InternalFrameBestellung) internalFrame;

					String naechsteCHNR = DelegateFactory.getInstance().getWareneingangDelegate()
							.generiereAutomatischeChargennummerAnhandBestellnummerWEPNrPosnr(
									(Integer) iternalFrameBestellung.getTabbedPaneBestellung()
											.getPanelBestellungWepQP5().getSelectedId(),
									iternalFrameBestellung.getTabbedPaneBestellung().getWareneingangDto().getIId());

					tfSnrchnr.setText(naechsteCHNR);
					wnfSnrchnr.requestFocus();

				} else if (automatischeChargennummerBeiLosablieferung > 0
						&& internalFrame instanceof InternalFrameFertigung) {

					InternalFrameFertigung iternalFrameLos = (InternalFrameFertigung) internalFrame;

					String s = null;

					boolean bHierarchischeChargennummer = false;

					if (automatischeChargennummerBeiLosablieferung == 2
							&& iternalFrameLos.getTabbedPaneRoot().getSelectedComponent() != null
							&& iternalFrameLos.getTabbedPaneRoot().getSelectedComponent()
									.equals(iternalFrameLos.getTabbedPaneLos())
							&& iternalFrameLos.getTabbedPaneLos()
									.getSelectedIndex() == iternalFrameLos.getTabbedPaneLos().IDX_ABLIEFERUNG) {

						if (iternalFrameLos.getTabbedPaneLos().getLosDto().getStuecklisteIId() != null) {
							StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklisteFindByPrimaryKey(
											iternalFrameLos.getTabbedPaneLos().getLosDto().getStuecklisteIId());
							if (Helper.short2boolean(stklDto.getBHierarchischeChargennummern())) {
								bHierarchischeChargennummer = true;
							}
						}

					}

					if (bHierarchischeChargennummer) {
						s = DelegateFactory.getInstance().getFertigungDelegate()
								.getHierarchischeChargennummer(iternalFrameLos.getTabbedPaneLos().getLosDto().getIId());
					} else {
						s = DelegateFactory.getInstance().getFertigungDelegate()
								.generiereChargennummer(iternalFrameLos.getTabbedPaneLos().getLosDto().getIId());
					}

					tfSnrchnr.setText(s);
					wnfSnrchnr.requestFocus();

				} else if (automatischeChargennummerBeiWEPOS == 2) {

					String versionVorhanden = null;
					if (alSeriennummern.size() > 0) {
						versionVorhanden = alSeriennummern.get(alSeriennummern.size() - 1).getCVersion();
					}

					String s = DelegateFactory.getInstance().getLagerDelegate().getNaechsteTafelnummer(artikelIId,
							versionVorhanden);

					tfSnrchnr.setText("TAFEL_" + s);
					wtfVersion.setText(s);
					wnfSnrchnr.requestFocus();

				}
			} else if (e.getActionCommand().equals(ACTION_RUECKGABE)) {
				refreshZeileVersion();

			}
		} catch (EJBExceptionLP e1) {
			internalFrame.handleException(new ExceptionLP(e1.getCode(), e1), false);

		} catch (Throwable e1) {
			internalFrame.handleException(e1, false);
		}

	}

	private boolean hatGebindehinterlegt() {
		if (gebindeIId != null && nGebindemenge != null) {
			return true;
		} else {
			return false;
		}

	}

	private BigDecimal refreshFromArrayList() {
		Object[][] data = null;

		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			setTitle(LPMain.getInstance().getTextRespectUISPr("artikel.titel.snrauswahl") + " "
					+ artikelDto.formatArtikelbezeichnung());
			if (bVersionMitangeben == true) {
				colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("artikel.seriennummer"),
						LPMain.getInstance().getTextRespectUISPr("artikel.lager.version") };
			} else {
				colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("artikel.seriennummer") };
			}

		} else {
			setTitle(LPMain.getInstance().getTextRespectUISPr("artikel.titel.chnrauswahl") + " "
					+ artikelDto.formatArtikelbezeichnung());

			// PJ19613
			if (bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) {
				colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("lp.chargennummer_lang"),
						LPMain.getInstance().getTextRespectUISPr("artikel.lager.version"),
						LPMain.getInstance().getTextRespectUISPr("lp.menge") };
			} else {
				colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("lp.chargennummer_lang"),
						LPMain.getInstance().getTextRespectUISPr("lp.menge") };
			}

		}

		BigDecimal bdGesamt = new BigDecimal(0);

		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			if (alSeriennummern != null) {

				if (bVersionMitangeben) {
					data = new Object[alSeriennummern.size()][2];
					for (int i = 0; i < alSeriennummern.size(); i++) {
						if (alSeriennummern.get(i) != null) {
							data[i][0] = alSeriennummern.get(i).getCSeriennrChargennr();

							if (alSeriennummern.get(i).getCVersion() != null) {
								data[i][1] = alSeriennummern.get(i).getCVersion();

							} else {
								data[i][1] = "";
							}

							bdGesamt = bdGesamt.add(alSeriennummern.get(i).getNMenge());
						}
					}

				} else {
					data = new Object[alSeriennummern.size()][1];
					for (int i = 0; i < alSeriennummern.size(); i++) {
						if (alSeriennummern.get(i) != null) {
							data[i][0] = alSeriennummern.get(i).getCSeriennrChargennr();

							bdGesamt = bdGesamt.add(alSeriennummern.get(i).getNMenge());
						}
					}
				}
			} else {
				data = new Object[0][2];
			}

		} else {
			if (alSeriennummern != null) {

				if (bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) {
					data = new Object[alSeriennummern.size()][3];
					for (int i = 0; i < alSeriennummern.size(); i++) {
						if (alSeriennummern.get(i) != null) {

							data[i][0] = alSeriennummern.get(i).getCSeriennrChargennr();

							if (alSeriennummern.get(i).getCVersion() != null) {
								data[i][1] = alSeriennummern.get(i).getCVersion();

							} else {
								data[i][1] = "";
							}

							if (iNachkommastelleMenge == 3) {
								data[i][2] = new BigDecimal3(alSeriennummern.get(i).getNMenge());
							} else {
								data[i][2] = new BigDecimal4(alSeriennummern.get(i).getNMenge());
							}

							bdGesamt = bdGesamt.add(alSeriennummern.get(i).getNMenge());
						}
					}
				} else {
					data = new Object[alSeriennummern.size()][2];
					for (int i = 0; i < alSeriennummern.size(); i++) {
						if (alSeriennummern.get(i) != null) {

							data[i][0] = alSeriennummern.get(i).getCSeriennrChargennr();

							if (iNachkommastelleMenge == 3) {
								data[i][1] = new BigDecimal3(alSeriennummern.get(i).getNMenge());
							} else {
								data[i][1] = new BigDecimal4(alSeriennummern.get(i).getNMenge());
							}

							bdGesamt = bdGesamt.add(alSeriennummern.get(i).getNMenge());
						}
					}
				}

			} else {
				data = new Object[0][2];
			}

		}

		// Wenn Verpackungsmenge, dann 2 Spalten hinzufuegen
		if (bdVerpackungsmenge != null) {

			try {
				colNames = addElement(colNames, LPMain.getTextRespectUISPr("artikel.verpackungsmenge.karton") + " "
						+ Helper.formatZahl(bdVerpackungsmenge, 2, LPMain.getTheClient().getLocUi()));
				colNames = addElement(colNames, LPMain.getTextRespectUISPr("artikel.einzelmenge"));

				for (int i = 0; i < data.length; i++) {

					Object[] zeile = data[i];

					BigDecimal bdGesamtmenge = alSeriennummern.get(i).getNMenge();

					BigDecimal einzelmenge = new BigDecimal(
							bdGesamtmenge.doubleValue() % bdVerpackungsmenge.doubleValue());
					Integer kartons = new BigDecimal(
							alSeriennummern.get(i).getNMenge().doubleValue() / bdVerpackungsmenge.doubleValue())
									.intValue();

					zeile = addElement(zeile, kartons);
					zeile = addElement(zeile, einzelmenge);

					data[i] = zeile;

				}

			} catch (Throwable e1) {
				internalFrame.handleException(e1, true);
			}

		}

		jTableSnrChnrs = new WrapperTableEditable(
				new MyTableModel1Chargennummern(colNames, data, hatGebindehinterlegt(), bVersionMitangeben));

		jTableSnrChnrs.getModel().addTableModelListener(this);
		jTableSnrChnrs.addMouseListener(this);

		if (!Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {

			try {
				if (bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) {
					NumberColumnFormat numberCF2 = new NumberColumnFormat("###,###,###.0000");
					TableColumn tc = jTableSnrChnrs.getColumnModel().getColumn(2);
					tc.setCellRenderer(numberCF2.getRenderer());
					tc.setCellEditor(numberCF2.getEditor());
				} else {
					NumberColumnFormat numberCF2 = new NumberColumnFormat("###,###,###.0000");
					TableColumn tc = jTableSnrChnrs.getColumnModel().getColumn(1);
					tc.setCellRenderer(numberCF2.getRenderer());
					tc.setCellEditor(numberCF2.getEditor());
				}

			} catch (Throwable e1) {
				internalFrame.handleException(e1, true);
			}
			jTableSnrChnrs.setSurrendersFocusOnKeystroke(true);
		}
		jTableSnrChnrs.setRowSelectionAllowed(true);
		jTableSnrChnrs.repaint();

		jScrollPane.getViewport().add(jTableSnrChnrs);
		SwingUtilities.updateComponentTreeUI(jScrollPane);

		try {
			jLabelGesamtMenge.setText("Ausgew\u00E4hlt: "
					+ Helper.formatZahl(bdGesamt, Defaults.getInstance().getIUINachkommastellenMenge(),
							LPMain.getInstance().getTheClient().getLocUi()));
		} catch (Throwable e) {
			internalFrame.handleException(e, true);
		}
		return bdGesamt;

	}

	private String[] addElement(String[] a, String e) {
		a = Arrays.copyOf(a, a.length + 1);
		a[a.length - 1] = e;
		return a;
	}

	private Object[] addElement(Object[] a, Object e) {
		a = Arrays.copyOf(a, a.length + 1);
		a[a.length - 1] = e;
		return a;
	}

	private void refreshZeileVersion() throws Throwable {
		if (bVersionMitangeben == true) {
			// panel1.removeAll();
			// remove(panel1);
			if (wcbRueckgabe.isSelected()) {
				bZugang = true;

			} else {
				bZugang = false;
			}

			// jbInit();
			// repaint();
			// LPMain.getInstance().getDesktop().repaint();
		}

	}

	private void jbInit() throws Throwable {
		jbInitMig();
	}

	// private void jbInitOld() throws Throwable {
	// panel1.setLayout(gridBagLayout1);
	// jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr(
	// "lp.uebernehmen"));
	// jButtonUebernehmen
	// .addActionListener(new
	// DialogSerienChargenauswahl_jButtonUebernehmen_actionAdapter(
	// this));
	// jButtonUebernehmen.setMnemonic('B');
	//
	// add(panel1);
	//
	// // Loeschen
	// JButton buttonEntfernen = ButtonFactory.createJButton(
	// IconFactory.getDelete(),
	// LPMain.getTextRespectUISPr("artikel.snr.entfernen"),
	// ACTION_DELETE);
	// buttonEntfernen.addActionListener(this);
	//
	// buttonEntfernen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	// .put(KeyStroke.getKeyStroke('D',
	// java.awt.event.InputEvent.CTRL_MASK), ACTION_DELETE);
	// buttonEntfernen.getActionMap().put(ACTION_DELETE,
	// new ButtonAbstractAction(this, ACTION_DELETE));
	//
	// panel1.add(buttonEntfernen, new GridBagConstraints(0, 0, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
	// 0, 0, 0, 0), 0, 0));
	//
	// JButton buttonNeuAusLager = ButtonFactory.createJButton(
	// IconFactory.getNew(),
	// LPMain.getTextRespectUISPr("artikel.snr.neuauslager"),
	// ACTION_NEW_FROM_LAGER);
	// buttonNeuAusLager.addActionListener(this);
	// buttonNeuAusLager.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	// .put(KeyStroke.getKeyStroke('N',
	// java.awt.event.InputEvent.CTRL_MASK),
	// ACTION_NEW_FROM_LAGER);
	// buttonNeuAusLager.getActionMap().put(ACTION_NEW_FROM_LAGER,
	// new ButtonAbstractAction(this, ACTION_NEW_FROM_LAGER));
	//
	// panel1.add(buttonEntfernen, new GridBagConstraints(4, 0, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
	// 0, 0, 0, 0), 0, 0));
	// panel1.add(buttonNeuAusLager, new GridBagConstraints(0, 0, 1, 1, 1.0,
	// 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 0, 0, 0), 0, 0));
	// panel1.add(jLabelBenoetigt, new GridBagConstraints(1, 0, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
	// 0, 0, 0, 0), 0, 0));
	// panel1.add(jLabelGesamtMenge, new GridBagConstraints(2, 0, 2, 1, 1.0,
	// 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 0, 0, 0), 0, 0));
	// panel1.add(jScrollPane, new GridBagConstraints(0, 1, 5, 1, 1.0, 1.0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
	// 0, 0, 0, 0), 0, 0));
	//
	// tfSnrchnr = new WrapperSNRField();
	// tfSnrchnr.setMandatoryField(true);
	// tfSnrchnr.addKeyListener(this);
	// wtfVersion.addKeyListener(this);
	//
	// JButton buttonNeuAusString = ButtonFactory.createJButton(
	// IconFactory.getPlus(), LPMain.getTextRespectUISPr("lp.new"),
	// ACTION_ADD_FROM_HAND);
	// buttonNeuAusString.addActionListener(this);
	//
	// String textFuerMenge = LPMain.getTextRespectUISPr("label.menge");
	// if (hatGebindehinterlegt()) {
	//
	// String gebinde = DelegateFactory.getInstance().getArtikelDelegate()
	// .gebindeFindByPrimaryKey(gebindeIId).getCBez();
	//
	// textFuerMenge = LPMain
	// .getTextRespectUISPr("artikel.gebindezubuchen.anzahlgebinde")
	// + " ("
	// + gebinde
	// + " \u00E0 "
	// + Helper.formatZahl(nGebindemenge, Defaults.getInstance()
	// .getIUINachkommastellenMenge(), LPMain
	// .getTheClient().getLocUi())
	// + " "
	// + artikelDto.getEinheitCNr().trim() + ")";
	// }
	//
	// panel1.add(new JLabel(textFuerMenge), new GridBagConstraints(3, 2, 1,
	// 1, 1.0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
	// new Insets(2, 0, 2, 0), 0, 0));
	//
	// panel1.add(tfSnrchnr, new GridBagConstraints(1, 3, 1, 1, 1.0, 0,
	// GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
	// 0, 0, 0), 150, 0));
	//
	// wdfMHD.setMandatoryField(true);
	//
	// wnfSnrchnr = new WrapperNumberField();
	// if (hatGebindehinterlegt()) {
	// wnfSnrchnr.setFractionDigits(0);
	// } else {
	// wnfSnrchnr.setFractionDigits(Defaults.getInstance()
	// .getIUINachkommastellenMenge());
	// }
	// wnfSnrchnr.setMinimumValue(0);
	// wnfSnrchnr.setMandatoryField(true);
	// wnfSnrchnr.addKeyListener(this);
	//
	// JLabel labnelsnrChnr = null;
	//
	// if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
	//
	// labnelsnrChnr = new JLabel(LPMain.getInstance()
	// .getTextRespectUISPr("bes.seriennummer_short"));
	//
	// panel1.add(labnelsnrChnr, new GridBagConstraints(1, 2, 1, 1, 1.0,
	// 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
	// new Insets(2, 0, 2, 0), 0, 0));
	//
	// wnfSnrchnr.setBigDecimal(new BigDecimal(1));
	// labnelsnrChnr.setLabelFor(tfSnrchnr);
	//
	// ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
	// .getInstance()
	// .getParameterDelegate()
	// .getParametermandant(
	// ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH,
	// ParameterFac.KATEGORIE_ARTIKEL,
	// LPMain.getTheClient().getMandant());
	// if ((Boolean) parameter.getCWertAsObject()) {
	// tfSnrchnr.setMaskNumerisch();
	// }
	//
	// } else {
	//
	// labnelsnrChnr = new JLabel(LPMain.getInstance()
	// .getTextRespectUISPr("lp.chargennummer_lang"));
	// panel1.add(labnelsnrChnr, new GridBagConstraints(1, 2, 1, 1, 1.0,
	// 0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
	// new Insets(2, 0, 2, 0), 0, 0));
	// labnelsnrChnr.setLabelFor(tfSnrchnr);
	//
	// if (bMindesthaltbarkeitsdatum == true) {
	// panel1.add(
	// new JLabel(LPMain.getInstance().getTextRespectUISPr(
	// "artikel.journal.mindesthaltbarkeit")),
	// new GridBagConstraints(0, 2, 1, 1, 1.0, 0,
	// GridBagConstraints.WEST,
	// GridBagConstraints.BOTH,
	// new Insets(2, 0, 2, 0), 0, 0));
	// panel1.add(wdfMHD, new GridBagConstraints(0, 3, 1, 1, 0, 0,
	// GridBagConstraints.WEST, GridBagConstraints.NONE,
	// new Insets(0, 0, 0, 0), -10, 0));
	//
	// }
	//
	// if (alSeriennummern == null || alSeriennummern.size() == 0) {
	//
	// PanelbeschreibungDto[] dtos = DelegateFactory
	// .getInstance()
	// .getPanelDelegate()
	// .panelbeschreibungFindByPanelCNrMandantCNr(
	// PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
	// artikelDto.getArtgruIId());
	// if (dtos.length > 0) {
	//
	// tfSnrchnr
	// .setText(DelegateFactory
	// .getInstance()
	// .getLagerDelegate()
	// .getNaechsteChargennummer(
	// artikelDto.getIId(), null));
	// }
	// }
	//
	// }
	//
	// labnelsnrChnr.setDisplayedMnemonic('N');
	//
	// if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())
	// || (Helper.short2Boolean(artikelDto.getBChargennrtragend()) &&
	// automatischeChargennummerBeiWEPOS > 0)) {
	// JButton buttonSnrVorschlagen = ButtonFactory.createJButton(
	// IconFactory.getDocumentAdd(),
	// LPMain.getTextRespectUISPr("artikel.lager.snrvorschlagen"),
	// ACTION_NEUER_SNR_VORSCHLAG);
	//
	// if (Helper.short2Boolean(artikelDto.getBChargennrtragend())
	// && automatischeChargennummerBeiWEPOS == 1
	// && internalFrame instanceof InternalFrameBestellung) {
	//
	// buttonSnrVorschlagen.setToolTipText(LPMain
	// .getTextRespectUISPr("artikel.lager.chnrvorschlagen"));
	// }
	//
	// buttonSnrVorschlagen.addActionListener(this);
	// buttonSnrVorschlagen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
	// .put(KeyStroke.getKeyStroke('O',
	// java.awt.event.InputEvent.CTRL_MASK),
	// ACTION_NEUER_SNR_VORSCHLAG);
	// buttonSnrVorschlagen.getActionMap().put(ACTION_NEUER_SNR_VORSCHLAG,
	// new ButtonAbstractAction(this, ACTION_NEUER_SNR_VORSCHLAG));
	// panel1.add(buttonSnrVorschlagen, new GridBagConstraints(0, 3, 1, 1,
	// 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 0, 0, 0), 0, 0));
	//
	// // SP2605 Wenn Abgang, dann nicth anzeigen
	// if (bZugang == false) {
	// buttonSnrVorschlagen.setVisible(false);
	// }
	//
	// if ((bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2)
	// && bZugang == true) {
	// panel1.add(wlaVersion, new GridBagConstraints(2, 2, 1, 1, 0, 0,
	// GridBagConstraints.WEST, GridBagConstraints.BOTH,
	// new Insets(2, 0, 2, 0), 0, 0));
	//
	// if (automatischeChargennummerBeiWEPOS == 2) {
	// wtfVersion.setMandatoryField(true);
	// }
	//
	// panel1.add(wtfVersion, new GridBagConstraints(2, 3, 1, 1, 1.0,
	// 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 2, 0, 0), 50, 0));
	//
	// }
	//
	// }
	//
	// panel1.add(wnfSnrchnr, new GridBagConstraints(3, 3, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
	// 0, 2, 0, 3), 70, 0));
	// // ------
	//
	// if (bdVerpackungsmenge != null) {
	// PanelAdditiveVerpackungsmengen pa = new PanelAdditiveVerpackungsmengen(
	// internalFrame, wnfSnrchnr);
	//
	// panel1.add(pa, new GridBagConstraints(3, 4, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 2, 0, 0), 150, 0));
	//
	// pa.setVerpackungsmenge(bdVerpackungsmenge.doubleValue());
	// }
	//
	// // -------
	//
	// wcbRueckgabe.setText(LPMain.getInstance().getTextRespectUISPr(
	// "artikel.rueckgabe"));
	// wcbRueckgabe.addActionListener(this);
	// wcbRueckgabe.setActionCommand(ACTION_RUECKGABE);
	//
	// if (internalFrame instanceof InternalFrameLieferschein) {
	// panel1.add(wcbRueckgabe, new GridBagConstraints(4, 2, 1, 1, 1.0, 0,
	// GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 2, 0, 0), 50, 0));
	// }
	//
	// panel1.add(buttonNeuAusString, new GridBagConstraints(4, 3, 1, 1, 1.0,
	// 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	// new Insets(0, 0, 0, 0), 0, 0));
	//
	// if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
	// panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 2, 1,
	// 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
	// new Insets(0, 0, 0, 4), 0, 0));
	//
	// wcbAutomatik.setText(LPMain
	// .getTextRespectUISPr("artikel.srnchrndialog.automatik"));
	// wcbAutomatik.setSelected(true);
	// if (bdVerpackungsmenge != null) {
	// panel1.add(wcbAutomatik, new GridBagConstraints(2, 4, 1, 1,
	// 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
	// 70, 0));
	// } else {
	// panel1.add(wcbAutomatik, new GridBagConstraints(2, 4, 2, 1,
	// 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
	// 0, 0));
	// }
	//
	// } else {
	// panel1.add(jButtonUebernehmen, new GridBagConstraints(0, 4, 4, 1,
	// 0.0, 0.0, GridBagConstraints.CENTER,
	// GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
	// }
	//
	// }

	private void jbInitMig() throws Throwable {
		add(panel1);

		tfSnrchnr = new WrapperSNRField();
		tfSnrchnr.setMandatoryField(true);
		tfSnrchnr.addKeyListener(this);
		wtfVersion.addKeyListener(this);

		wdfMHD.setMandatoryField(true);

		wnfSnrchnr = new WrapperNumberField();
		if (hatGebindehinterlegt()) {
			wnfSnrchnr.setFractionDigits(0);
		} else {
			wnfSnrchnr.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		}
		wnfSnrchnr.setMinimumValue(0);
		wnfSnrchnr.setMandatoryField(true);
		wnfSnrchnr.addKeyListener(this);

		JPanel panelSnrChnr = getChargennummerPanel();

		JPanel panelDetails = new JPanel();
		HvLayout layoutDetails = HvLayoutFactory.create(panelDetails, "ins 0, hidemode 3", "[fill,40%|fill|fill,grow]",
				"[fill,grow][fill]");
		layoutDetails.add(panelSnrChnr).add(getPanelVersion()).add(getPanelMengen(), "spany 2, wrap");

		HvLayout layoutDialog = HvLayoutFactory.create(panel1, "", "[fill,grow]", "[fill|fill,grow]");
		layoutDialog.add(getButtonTopPanel()).wrap().add(jScrollPane).wrap().add(panelDetails);
	}

	private JComponent getPanelUebernehmen() {
		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr("lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogSerienChargenauswahlChargennummern_jButtonUebernehmen_actionAdapter(this));
		jButtonUebernehmen.setMnemonic('B');

		JPanel panelUebernehmen = new JPanel();
		HvLayout layoutUebernehmen = HvLayoutFactory.create(panelUebernehmen, "ins 0, hidemode 3", "[fill][fill,grow]",
				"[fill]");
		layoutUebernehmen.add(jButtonUebernehmen, "skip 1, wmin 150, wmax 250");
		if (Helper.short2Boolean(artikelDto.getBSeriennrtragend())) {
			wcbAutomatik.setText(LPMain.getTextRespectUISPr("artikel.srnchrndialog.automatik"));
			wcbAutomatik.setSelected(true);
			layoutUebernehmen.add(wcbAutomatik, "wmin 100");
		}

		return panelUebernehmen;
	}

	private JPanel getPanelVersion() {
		JPanel panelVersion = new JPanel();
		if (!((bVersionMitangeben || automatischeChargennummerBeiWEPOS == 2) && bZugang))
			return panelVersion;

		if (automatischeChargennummerBeiWEPOS == 2) {
			wtfVersion.setMandatoryField(true);
		}

		HvLayout layoutVersion = HvLayoutFactory.create(panelVersion, "ins 0, wrap 1", "[120:150:150,fill]",
				"[fill,20:20|fill]");
		layoutVersion.add(wlaVersion).add(wtfVersion);

		return panelVersion;
	}

	private JPanel getPanelMengen() throws Throwable {
		JPanel panelMengen = new JPanel();
		HvLayout layoutMengen = HvLayoutFactory.create(panelMengen, "ins 0, wrap 4", "[fill|fill,grow|fill|fill,grow]",
				"[fill,20:20][fill]5[fill]");
		layoutMengen.add(new JLabel(getTextFuerMenge())).wrap().add(wnfSnrchnr).spanAndWrap(2);

		if (bdVerpackungsmenge != null) {
			PanelAdditiveVerpackungsmengen pa = new PanelAdditiveVerpackungsmengen(internalFrame, wnfSnrchnr);
			layoutMengen.add(pa, "span 3, wmin 350");
			pa.setVerpackungsmenge(bdVerpackungsmenge.doubleValue());
		}

		wcbRueckgabe.setText(LPMain.getInstance().getTextRespectUISPr("artikel.rueckgabe"));
		wcbRueckgabe.addActionListener(this);
		wcbRueckgabe.setActionCommand(ACTION_RUECKGABE);

		if (internalFrame instanceof InternalFrameLieferschein) {
			layoutMengen.add(wcbRueckgabe, "cell 2 0 3 0, wmin 100, gapbottom -5");
		}

		JButton buttonNeuAusString = ButtonFactory.createJButton(IconFactory.getPlus(),
				LPMain.getTextRespectUISPr("lp.new"), ACTION_ADD_FROM_HAND);
		buttonNeuAusString.addActionListener(this);
		layoutMengen.add(buttonNeuAusString, "cell 2 1, w 40");

		return panelMengen;
	}

	private JPanel getChargennummerPanel() throws Throwable {
		JPanel panelChargennummer = new JPanel();
		HvLayout layoutChnr = HvLayoutFactory.create(panelChargennummer, "ins 0", "[fill|fill|fill,grow]",
				"[fill,20:20|fill]");
		if (bMindesthaltbarkeitsdatum) {
			layoutChnr.add(new JLabel(LPMain.getInstance().getTextRespectUISPr("artikel.journal.mindesthaltbarkeit")),
					"cell 0 0").add(wdfMHD, "cell 0 1");
		}
		if (alSeriennummern == null || alSeriennummern.size() == 0) {
			PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
					.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
							artikelDto.getArtgruIId());
			if (dtos.length > 0) {
				tfSnrchnr.setText(DelegateFactory.getInstance().getLagerDelegate()
						.getNaechsteChargennummer(artikelDto.getIId(), null));
			}
		}

		JLabel labelChnr = new JLabel(LPMain.getInstance().getTextRespectUISPr("lp.chargennummer_lang"));
		labelChnr.setDisplayedMnemonic('N');
		labelChnr.setLabelFor(tfSnrchnr);

		layoutChnr.add(labelChnr, "cell 2 0").add(tfSnrchnr, "cell 2 1").add(getPanelUebernehmen(),
				"cell 1 2 2 2, gaptop 20");

		if (automatischeChargennummerBeiWEPOS > 0 || automatischeChargennummerBeiLosablieferung > 0) {
			JButton buttonVorschlag = createButton(IconFactory.getDocumentAdd(),
					LPMain.getTextRespectUISPr("artikel.lager.chnrvorschlagen"), ACTION_NEUER_SNR_VORSCHLAG, 'O');

			buttonVorschlag.setVisible(false);

			// SP2605 Wenn Abgang, dann nicth anzeigen
			if (automatischeChargennummerBeiWEPOS > 0) {
				buttonVorschlag.setVisible(bZugang);
			}
			if (automatischeChargennummerBeiLosablieferung > 0 && internalFrame instanceof InternalFrameFertigung) {
				buttonVorschlag.setVisible(bZugang);
			}

			if (automatischeChargennummerBeiWEPOS == 1 && internalFrame instanceof InternalFrameBestellung) {
				buttonVorschlag.setToolTipText(LPMain.getTextRespectUISPr("artikel.lager.chnrvorschlagen"));
			}
			layoutChnr.add(buttonVorschlag, "cell 1 1, wmin 40");
		}
		return panelChargennummer;
	}

	private String getTextFuerMenge() throws Throwable {
		String textFuerMenge = LPMain.getTextRespectUISPr("label.menge");
		if (hatGebindehinterlegt()) {

			String gebinde = DelegateFactory.getInstance().getArtikelDelegate().gebindeFindByPrimaryKey(gebindeIId)
					.getCBez();

			textFuerMenge = LPMain.getTextRespectUISPr("artikel.gebindezubuchen.anzahlgebinde") + " (" + gebinde
					+ " \u00E0 " + Helper.formatZahl(nGebindemenge,
							Defaults.getInstance().getIUINachkommastellenMenge(), LPMain.getTheClient().getLocUi())
					+ " " + artikelDto.getEinheitCNr().trim() + ")";
		}

		return textFuerMenge;
	}

	private JButton createButton(Icon icon, String text, String actionCommand, char keyStroke) {
		JButton button = ButtonFactory.createJButton(icon, text, actionCommand);
		button.addActionListener(this);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(keyStroke, java.awt.event.InputEvent.CTRL_MASK), actionCommand);
		button.getActionMap().put(actionCommand, new ButtonAbstractActionChargennummern(this, actionCommand));
		return button;
	}

	private JPanel getButtonTopPanel() {
		JPanel mengePanel = new JPanel();
		HvLayout layoutMengePanel = HvLayoutFactory.create(mengePanel, "ins 0", "[50%,fill]5[50%,fill]", "");
		layoutMengePanel.add(jLabelBenoetigt, "al right").add(jLabelGesamtMenge, "al left");
		// Loeschen
		JButton buttonEntfernen = createButton(IconFactory.getDelete(),
				LPMain.getTextRespectUISPr("artikel.snr.entfernen"), ACTION_DELETE, 'D');
		JButton buttonNeuAusLager = createButton(IconFactory.getNew(),
				LPMain.getTextRespectUISPr("artikel.snr.neuauslager"), ACTION_NEW_FROM_LAGER, 'N');

		JPanel buttonTopPanel = new JPanel();
		HvLayout layoutButtonTopPanel = HvLayoutFactory.create(buttonTopPanel, "ins 0", "[fill]20[fill,grow]20[fill]",
				"");
		layoutButtonTopPanel.add(buttonNeuAusLager, "w 40, hmin 30, al left").add(mengePanel).add(buttonEntfernen,
				"w 40, hmin 30, al right");
		return buttonTopPanel;
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == tfSnrchnr || e.getSource() == wtfVersion || e.getSource() == wnfSnrchnr) {

				actionPerformed(new ActionEvent(tfSnrchnr, 0, ACTION_ADD_FROM_HAND));

				tfSnrchnr.requestFocus();
			}

		}

		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
			int iRowCount = jTableSnrChnrs.getRowCount();
			int iSelectedRow = jTableSnrChnrs.getSelectedRow();
			if (e.getKeyCode() == KeyEvent.VK_UP && iSelectedRow > 0) {
				jTableSnrChnrs.setRowSelectionInterval(iSelectedRow - 1, iSelectedRow - 1);

			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && iSelectedRow < (iRowCount - 1)) {
				jTableSnrChnrs.setRowSelectionInterval(iSelectedRow + 1, iSelectedRow + 1);

			}
			int indexOfSelectedRow = this.jTableSnrChnrs.getSelectedRow();
			jTableSnrChnrs.scrollRectToVisible(jTableSnrChnrs.getCellRect(indexOfSelectedRow, 0, true));
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void jButtonUebernehmen_actionPerformed(ActionEvent e) {
		if (tfSnrchnr.getText() != null && wnfSnrchnr.getText() != null) {
			actionPerformed(new ActionEvent(this, 0, ACTION_ADD_FROM_HAND));
		}

		alSeriennummernReturn = alSeriennummern;
		setVisible(false);
	}

	public class ButtonAbstractActionChargennummern extends AbstractAction {
		private static final long serialVersionUID = -8681526632257782909L;
		private DialogSerienChargenauswahlChargennummern adaptee;
		private String sActionCommand = null;

		public ButtonAbstractActionChargennummern(DialogSerienChargenauswahlChargennummern adaptee,
				String sActionCommandI) {
			this.adaptee = adaptee;
			sActionCommand = sActionCommandI;
		}

		public void actionPerformed(java.awt.event.ActionEvent e) {
			ActionEvent ae = new ActionEvent(this, 1, sActionCommand);
			adaptee.actionPerformed(ae);
		}

		public void setSActionCommand(String sActionCommand) {

			this.sActionCommand = sActionCommand;
		}

		public String getSActionCommand() {
			return sActionCommand;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(jTableSnrChnrs) && e.getClickCount() == 2) {

			try {
				// PJ18452 Nur bei Chargen
				ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(artikelIId);

				if (bZugang) {
					if (artikelDto.isChargennrtragend()) {
						PanelbeschreibungDto[] dtos = DelegateFactory.getInstance().getPanelDelegate()
								.panelbeschreibungFindByPanelCNrMandantCNr(PanelFac.PANEL_CHARGENEIGENSCHAFTEN,
										artikelDto.getArtgruIId());
						if (dtos.length > 0) {

							SeriennrChargennrMitMengeDto dto = alSeriennummern.get(jTableSnrChnrs.getSelectedRow());

							DialogDynamischChargeneigenschaften d = new DialogDynamischChargeneigenschaften(artikelDto,
									internalFrame, dto);
							LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
							d.setVisible(true);

						}
					}
				}
			} catch (Throwable e1) {
				internalFrame.handleException(e1, true);
			}

		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

class DialogSerienChargenauswahlChargennummern_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogSerienChargenauswahlChargennummern adaptee;

	DialogSerienChargenauswahlChargennummern_jButtonUebernehmen_actionAdapter(
			DialogSerienChargenauswahlChargennummern adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel1Chargennummern extends AbstractTableModel {
	private static final long serialVersionUID = -6311652299531724223L;
	private String[] columnNames = null;
	private Object[][] data = null;
	private boolean bGebinde;
	private boolean bVersion;

	public MyTableModel1Chargennummern(String[] columnNames, Object[][] data, boolean bGebinde, boolean bVersion) {

		this.columnNames = columnNames;
		this.data = data;
		this.bGebinde = bGebinde;
		this.bVersion = bVersion;
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
		return data[row][col];
	}

	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.

		if (bVersion && col == 2) {
			return true;
		}

		if (col == 1 && bGebinde == false && bVersion == false) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
