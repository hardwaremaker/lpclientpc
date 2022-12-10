
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.dialog.DialogQRCodeLesen;
import com.lp.client.lieferschein.InternalFrameLieferschein;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.BelegInfos;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.PanelbeschreibungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.BigDecimal4;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("static-access")
public class DialogSerienChargenauswahlSeriennummern extends JDialog
		implements ActionListener, KeyListener, TableModelListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private String ACTION_NEW_FROM_LAGER = "action_new_from_lager";
	private String ACTION_NEW_FROM_SCANNER = "action_new_from_scanner";
	private String ACTION_DELETE = "action_delete";
	private String ACTION_ADD_FROM_HAND = "ACTION_ADD_FROM_HAND";
	private String ACTION_NEUER_SNR_VORSCHLAG = "ACTION_NEUER_SNR_VORSCHLAG";
	private String ACTION_RUECKGABE = "action_rueckgabe";
	private String ACTION_KOMMA = "action_komma";
	private String ACTION_BIS = "action_bis";

	ArtikelDto artikelDto = null;
	JPanel panelContent = new JPanel(new MigLayout("wrap 1", "[100%,fill]"));
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	JScrollPane jScrollPane = new JScrollPane();

	public List<SeriennrChargennrMitMengeDto> alSeriennummernReturn = null;

	JScrollPane jScrollPane1 = new JScrollPane();
	WrapperTableEditable jTableSnrChnrs = new WrapperTableEditable();
	JLabel wlaVersion = new JLabel(LPMain.getTextRespectUISPr("artikel.lager.version"));
	JButton jButtonUebernehmen = new JButton();
	JLabel jLabelGesamtMenge = new JLabel();
	JLabel jLabelBenoetigt = new JLabel();
	JButton buttonVorschlag = null;
	JLabel labelBeginntMit = null;

	JLabel labelSnrVon = new JLabel(LPMain.getInstance().getTextRespectUISPr("bes.seriennummer_short"));
	JRadioButton rbSnrBis = new JRadioButton(LPMain.getInstance().getTextRespectUISPr("artikel.snrdialog.snrbis"));
	JRadioButton rbAnzahl = new JRadioButton(LPMain.getInstance().getTextRespectUISPr("artikel.snrdialog.anzahl"));
	ButtonGroup bgSnrBis=new ButtonGroup();

	public WrapperTextField wtfSNRBeginntMit = new WrapperTextField();

	public WrapperTextField wtfVersion = new WrapperTextField(40);
	public WrapperSNRField wtfSnrchnr = null;
	public WrapperSNRField wtfSnrchnrBis = null;
	public WrapperNumberField wnfAnzahl = new WrapperNumberField(6);

	public WrapperCheckBox wcbRueckgabe = new WrapperCheckBox();
	public WrapperCheckBox wcbAutomatik = new WrapperCheckBox();
	public WrapperCheckBox wcbKomma = new WrapperCheckBox();

	String[] colNames = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	public List<SeriennrChargennrMitMengeDto> alSeriennummernBereitsSelektiert = null;
	Integer artikelIId = null;
	Integer lagerIId = null;

	LagerDto lagerDto = null;

	InternalFrame internalFrame = null;
	private boolean bVersionMitangeben = false;
	private boolean bFuehrendeNullenWegschneiden = false;
	private boolean bZugang = false;
	boolean selektierteNichtAnzeigen = false;

	int iNachkommastelleMenge = 3;
	private BigDecimal bdBenoetigteMenge = null;

	public boolean bAbbruch = false;

	private boolean bNumerisch = false;

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

	public DialogSerienChargenauswahlSeriennummern(Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennummern, boolean bMultiselection,
			boolean selektierteNichtAnzeigen, InternalFrame internalFrame, WrapperNumberField wnfBeleg, boolean bZugang)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);
		this.selektierteNichtAnzeigen = selektierteNichtAnzeigen;
		this.bZugang = bZugang;

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

		lagerDto = DelegateFactory.getInstance().getLagerDelegate().lagerFindByPrimaryKey(lagerIId);

		this.internalFrame = internalFrame;
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SERIENNUMMERN_FUEHRENDE_NULLEN_ENTFERNEN,
						ParameterFac.KATEGORIE_ARTIKEL, LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null && !parameter.getCWert().equals("0")) {
			bFuehrendeNullenWegschneiden = true;
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_VERSION_BEI_SNR_MITANGEBEN, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getInstance().getTheClient().getMandant());

		bVersionMitangeben = (Boolean) parameter.getCWertAsObject();

		artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

		jbInit();

		this.setSize(1000, 500);
		this.setMinimumSize(new Dimension(800, 500));
		refreshFromArrayList();

		if (wnfBeleg != null && wnfBeleg.getBigDecimal() != null && wnfBeleg.getBigDecimal().doubleValue() < 0) {
			wcbRueckgabe.setSelected(true);

			buttonVorschlag.setVisible(true);
			wtfSNRBeginntMit.setVisible(true);
			labelBeginntMit.setVisible(true);
			if (bVersionMitangeben) {
				wtfVersion.setVisible(true);
				wlaVersion.setVisible(true);
			}

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
		wtfSnrchnr.requestFocusInWindow();

		SwingUtilities.invokeLater(new RequestFocusLater(wtfSnrchnr));
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

		if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
			// Mengen updaten
			if (e.getSource().equals(jTableSnrChnrs.getModel())) {
				for (int i = 0; i < alSeriennummern.size(); i++) {

					if (jTableSnrChnrs.getModel().getValueAt(i, 1) instanceof String) {

						alSeriennummern.get(i)
								.setNMenge(new BigDecimal4((String) jTableSnrChnrs.getModel().getValueAt(i, 1)));

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

			alSeriennummern.add(dto);

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

				wtfSnrchnr.requestFocus();

			} else if (e.getActionCommand().equals(ACTION_ADD_FROM_HAND)) {

				if (wtfSnrchnr.getText() == null) {

					DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
					return;
				}

				String[] snrs = null;

				if (wcbKomma.isSelected()) {
					snrs = wtfSnrchnr.erzeugeSeriennummernArray(null, false);
				} else {

					
					if(rbSnrBis.isSelected() && wtfSnrchnrBis.getText() != null) {
						snrs = Helper.erzeugeSeriennummernArrayBereich(wtfSnrchnr.getText(), wtfSnrchnrBis.getText(), 0,
								false);
					}
					
					else if (rbAnzahl.isSelected() && wnfAnzahl.getInteger() != null) {
						
						
						snrs = Helper.erzeugeSeriennummernAnhandAnzahl(wtfSnrchnr.getText(), wnfAnzahl.getInteger());
					}
					 else {
						snrs = new String[] { wtfSnrchnr.getText() };
					}

				}

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

				wtfSnrchnr.setText(null);
				wtfSnrchnrBis.setText(null);
				wnfAnzahl.setText(null);
				wtfVersion.setText(null);
			} else if (e.getActionCommand().equals(ACTION_NEW_FROM_SCANNER)) {
				DialogQRCodeLesenSeriennummern qr = new DialogQRCodeLesenSeriennummern(this, null, internalFrame);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(qr);

				qr.setVisible(true);

				if (qr.getBarCode() != null && qr.getBarCode().length() > 0) {
					String barcode = qr.getBarCode();
				}

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

				try {
					String snrVorhanden = null;
					if (alSeriennummern.size() > 0) {
						snrVorhanden = alSeriennummern.get(alSeriennummern.size() - 1).getCSeriennrChargennr();
					}

					String naechsteSNR = DelegateFactory.getInstance().getLagerDelegate()
							.getNaechsteSeriennummer(artikelIId, snrVorhanden, wtfSNRBeginntMit.getText());
					wtfSnrchnr.setText(naechsteSNR);

				} catch (ExceptionLP ex) {

					if (ex.getICode() == EJBExceptionLP.FEHLER_SERIENNUMMERNGENERATOR_UNGUELTIGE_ZEICHEN) {
						String sMsg = LPMain.getTextRespectUISPr("fert.seriennummerngenerator.error") + ": "
								+ ex.getAlInfoForTheClient().get(0);
						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), sMsg);
					} else {
						throw ex;
					}

				}

			} else if (e.getActionCommand().equals(ACTION_RUECKGABE)) {

				refreshZeileVersion();

				buttonVorschlag.setVisible(bZugang);
				wtfSNRBeginntMit.setVisible(bZugang);
				labelBeginntMit.setVisible(bZugang);
				
				if (bVersionMitangeben) {
					wtfVersion.setVisible(bZugang);
					wlaVersion.setVisible(bZugang);
				}
				

			} else if (e.getActionCommand().equals(ACTION_KOMMA)) {
				eigenschaftenAufgrundKommaSetzen();

			}else if (e.getActionCommand().equals(ACTION_BIS)) {
				eigenschaftenAufgrundBisSetzen();;

			}


		
		} catch (EJBExceptionLP e1) {
			internalFrame.handleException(new ExceptionLP(e1.getCode(), e1), false);

		} catch (Throwable e1) {
			internalFrame.handleException(e1, false);
		}

	}

	
	private void eigenschaftenAufgrundBisSetzen() throws Throwable {

		if (rbSnrBis.isSelected()) {
			wtfSnrchnrBis.setEditable(true);
			wnfAnzahl.setEditable(false);
			wtfSnrchnrBis.requestFocusInWindow();
		} else {
			
			wtfSnrchnrBis.setEditable(false);
			wnfAnzahl.setEditable(true);
			wnfAnzahl.requestFocusInWindow();
		}
	}
	
	private void eigenschaftenAufgrundKommaSetzen() throws Throwable {

		if (wcbKomma.isSelected()) {
			wtfSnrchnr.setText(null);
			wtfSnrchnrBis.setText(null);

			labelSnrVon = new JLabel(LPMain.getInstance().getTextRespectUISPr("bes.seriennummer_short"));
			wtfSnrchnrBis.setVisible(false);
			rbSnrBis.setVisible(false);
			rbAnzahl.setVisible(false);
			wnfAnzahl.setVisible(false);
			if (bNumerisch) {
				wtfSnrchnr.setMaskNumerisch();
			} else {
				wtfSnrchnr.setMask();
				wtfSnrchnr.setText(null);

			}

		} else {
			wtfSnrchnrBis.setVisible(true);
			rbSnrBis.setVisible(true);
			
			rbAnzahl.setVisible(true);
			wnfAnzahl.setVisible(true);

			
			labelSnrVon = new JLabel(LPMain.getInstance().getTextRespectUISPr("artikel.snrdialog.snrvon"));

			if (bNumerisch) {
				wtfSnrchnr.setMaskNumerisch();
			} else {
				wtfSnrchnr.setMaskAllesErlaubt();
				wtfSnrchnrBis.setMaskAllesErlaubt();
			}

		}
	}

	private BigDecimal refreshFromArrayList() {
		Object[][] data = null;

		setTitle(LPMain.getInstance().getTextRespectUISPr("artikel.titel.snrauswahl") + " "
				+ artikelDto.formatArtikelbezeichnung() + " / "
				+ LPMain.getInstance().getTextRespectUISPr("label.lager") + ": " + lagerDto.getCNr());
		if (bVersionMitangeben == true) {
			colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("artikel.seriennummer"),
					LPMain.getInstance().getTextRespectUISPr("artikel.lager.version") };
		} else {
			colNames = new String[] { LPMain.getInstance().getTextRespectUISPr("artikel.seriennummer") };
		}

		BigDecimal bdGesamt = new BigDecimal(0);

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

		jTableSnrChnrs = new WrapperTableEditable(new MyTableModel1Seriennummern(colNames, data));

		jTableSnrChnrs.getModel().addTableModelListener(this);
		jTableSnrChnrs.addMouseListener(this);

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

	private void refreshZeileVersion() throws Throwable {
		if (bVersionMitangeben == true) {
			if (wcbRueckgabe.isSelected()) {
				bZugang = true;

			} else {
				bZugang = false;
			}

		}

	}

	private void jbInit() throws Throwable {
		add(panelContent);

		wtfSnrchnr = new WrapperSNRField();
		wtfSnrchnr.setMandatoryField(true);
		wtfSnrchnr.addKeyListener(this);
		wtfVersion.addKeyListener(this);

		wtfSnrchnrBis = new WrapperSNRField();
		wtfSnrchnrBis.addKeyListener(this);
		
		
		wnfAnzahl.addKeyListener(this);
		

		JPanel panelSnrChnr = getSeriennummerPanel();

		panelContent.add(getButtonTopPanel());
		panelContent.add(jScrollPane);
		panelContent.add(panelSnrChnr);

	}

	private JPanel getSeriennummerPanel() throws Throwable {

		labelSnrVon.setDisplayedMnemonic('N');

		labelBeginntMit = new JLabel(LPMain.getInstance().getTextRespectUISPr("lp.snrdialog.beginntmit"));
		labelBeginntMit.setDisplayedMnemonic('b');
		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_SERIENNUMMER_NUMERISCH, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		bNumerisch = (Boolean) parameter.getCWertAsObject();

		if (bNumerisch) {
			wtfSnrchnr.setMaskNumerisch();
			wtfSnrchnrBis.setMaskNumerisch();
		}

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_DEFAULT_SNR_DIALOG_KOMMA, ParameterFac.KATEGORIE_ALLGEMEIN,
				LPMain.getTheClient().getMandant());
		boolean bDefaultKomma = (Boolean) parameter.getCWertAsObject();

		parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
				ParameterFac.PARAMETER_SERIENNUMMER_BEGINNT_MIT, ParameterFac.KATEGORIE_ARTIKEL,
				LPMain.getTheClient().getMandant());
		String snrbeginnttmit = (String) parameter.getCWertAsObject();
		if (snrbeginnttmit != null && snrbeginnttmit.length() > 0) {
			wtfSNRBeginntMit.setText(snrbeginnttmit);
		}

		buttonVorschlag = createButton(IconFactory.getDocumentAdd(),
				LPMain.getTextRespectUISPr("artikel.lager.snrvorschlagen"), ACTION_NEUER_SNR_VORSCHLAG, 'O');

		buttonVorschlag.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonVorschlag.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonVorschlag.setMaximumSize(HelperClient.getToolsPanelButtonDimension());

		// SP2605 Wenn Abgang, dann nicth anzeigen
		buttonVorschlag.setVisible(bZugang);
		wtfSNRBeginntMit.setVisible(bZugang);
		labelBeginntMit.setVisible(bZugang);

		wcbRueckgabe.setText(LPMain.getInstance().getTextRespectUISPr("artikel.rueckgabe"));
		wcbRueckgabe.addActionListener(this);
		wcbRueckgabe.setActionCommand(ACTION_RUECKGABE);

		wcbKomma.setText(LPMain.getTextRespectUISPr("artikel.snrdialog.komma"));
		wcbKomma.addActionListener(this);
		wcbKomma.setActionCommand(ACTION_KOMMA);
		wcbKomma.setSelected(bDefaultKomma);

		wcbAutomatik.setText(LPMain.getTextRespectUISPr("artikel.srnchrndialog.automatik"));
		wcbAutomatik.setSelected(true);

		JButton buttonNeuAusString = ButtonFactory.createJButton(IconFactory.getPlus(),
				LPMain.getTextRespectUISPr("lp.new"), ACTION_ADD_FROM_HAND);
		buttonNeuAusString.addActionListener(this);

		buttonNeuAusString.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonNeuAusString.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonNeuAusString.setMaximumSize(HelperClient.getToolsPanelButtonDimension());

		jButtonUebernehmen.setText(LPMain.getInstance().getTextRespectUISPr("lp.uebernehmen"));
		jButtonUebernehmen
				.addActionListener(new DialogSerienChargenauswahlSeriennummern_jButtonUebernehmen_actionAdapter(this));
		jButtonUebernehmen.setMnemonic('B');

		JPanel panelSeriennummer = new JPanel(
				new MigLayout("wrap 8", "[15%,fill][7%,fill][15%,fill][30%,fill][20%,fill][20%,fill][20%,fill][5%,fill]"));

		panelSeriennummer.add(labelBeginntMit, "span 3");
		panelSeriennummer.add(labelSnrVon);
		panelSeriennummer.add(rbSnrBis);
		
		panelSeriennummer.add(rbAnzahl);

		
		
		wnfAnzahl.setFractionDigits(0);
		wnfAnzahl.setMinimumValue(0);
		
		bgSnrBis.add(rbSnrBis);
		bgSnrBis.add(rbAnzahl);
		
		
		rbAnzahl.addActionListener(this);
		rbAnzahl.setActionCommand(ACTION_BIS);
		
		rbSnrBis.addActionListener(this);
		rbSnrBis.setActionCommand(ACTION_BIS);
		rbSnrBis.setSelected(true);
		
		
		panelSeriennummer.add(wlaVersion, "wrap");

		panelSeriennummer.add(wtfSNRBeginntMit);
		panelSeriennummer.add(buttonVorschlag);
		panelSeriennummer.add(wcbKomma);
		panelSeriennummer.add(wtfSnrchnr);
		panelSeriennummer.add(wtfSnrchnrBis);
		panelSeriennummer.add(wnfAnzahl);
		
		
		
		panelSeriennummer.add(wtfVersion);
		panelSeriennummer.add(buttonNeuAusString);

		panelSeriennummer.add(wcbAutomatik, "skip 2");
		panelSeriennummer.add(jButtonUebernehmen);

		if (internalFrame instanceof InternalFrameLieferschein) {
			panelSeriennummer.add(wcbRueckgabe);
		}

		eigenschaftenAufgrundKommaSetzen();

		eigenschaftenAufgrundBisSetzen();
		
		if (!((bVersionMitangeben) && bZugang)) {
			wtfVersion.setVisible(false);
			wlaVersion.setVisible(false);
		}

		return panelSeriennummer;
	}

	private JButton createButton(Icon icon, String text, String actionCommand, char keyStroke) {
		JButton button = ButtonFactory.createJButton(icon, text, actionCommand);
		button.addActionListener(this);
		button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(keyStroke, java.awt.event.InputEvent.CTRL_MASK), actionCommand);
		button.getActionMap().put(actionCommand, new ButtonAbstractActionSeriennummern(this, actionCommand));
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

		JButton buttonScanner = createButton(
				new ImageIcon(IconFactory.class.getResource("/com/lp/client/res/scanner16x16.png")),
				LPMain.getTextRespectUISPr("artikel.snr.neuauslager"), ACTION_NEW_FROM_SCANNER, 'N');

		JPanel buttonTopPanel = new JPanel();
		HvLayout layoutButtonTopPanel = HvLayoutFactory.create(buttonTopPanel, "ins 0",
				"[2%,fill][fill]20[fill,grow]20[fill]", "");
		layoutButtonTopPanel.add(buttonNeuAusLager, "w 20, hmin 20, al left")
				.add(buttonScanner, "w 0, hmin 25, al left").add(mengePanel)
				.add(buttonEntfernen, "w 40, hmin 30, al right");
		return buttonTopPanel;
	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == wtfSnrchnr || e.getSource() == wtfSnrchnrBis || e.getSource() == wnfAnzahl || e.getSource() == wtfVersion) {

				actionPerformed(new ActionEvent(wtfSnrchnr, 0, ACTION_ADD_FROM_HAND));

				wtfSnrchnr.requestFocus();
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
		if (wtfSnrchnr.getText() != null) {
			actionPerformed(new ActionEvent(this, 0, ACTION_ADD_FROM_HAND));
		}

		alSeriennummernReturn = alSeriennummern;
		setVisible(false);
	}

	public class ButtonAbstractActionSeriennummern extends AbstractAction {
		private static final long serialVersionUID = -8681526632257782909L;
		private DialogSerienChargenauswahlSeriennummern adaptee;
		private String sActionCommand = null;

		public ButtonAbstractActionSeriennummern(DialogSerienChargenauswahlSeriennummern adaptee,
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

class DialogSerienChargenauswahlSeriennummern_jButtonUebernehmen_actionAdapter implements ActionListener {
	private DialogSerienChargenauswahlSeriennummern adaptee;

	DialogSerienChargenauswahlSeriennummern_jButtonUebernehmen_actionAdapter(
			DialogSerienChargenauswahlSeriennummern adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jButtonUebernehmen_actionPerformed(e);
	}
}

class MyTableModel1Seriennummern extends AbstractTableModel {
	private static final long serialVersionUID = -6311652299531724223L;
	private String[] columnNames = null;
	private Object[][] data = null;
	private boolean bGebinde;

	public MyTableModel1Seriennummern(String[] columnNames, Object[][] data) {

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
		if (col == 1 && bGebinde == false) {
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
