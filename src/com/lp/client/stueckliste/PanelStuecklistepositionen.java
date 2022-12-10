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
package com.lp.client.stueckliste;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.DialogArtikelkopieren;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.editor.PanelEditorPlainText;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.inserat.service.InseraterDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklisteScriptartDto;
import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.EinheitDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelStuecklistepositionen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameStueckliste internalFrameStueckliste = null;
	private WrapperTextField wtfHandartikel = new WrapperTextField();
	private WrapperButton wbuMontageart = new WrapperButton();
	private WrapperTextField wtfMontageart = new WrapperTextField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaPosition = new WrapperLabel();
	private WrapperTextField wtfPosition = new WrapperTextField();
	private WrapperButton wbuPositionEdit = new WrapperButton();

	private WrapperLabel wlaFormel = new WrapperLabel();
	private WrapperTextField wtfFormel = new WrapperTextField();
	private WrapperButton wbuFormelEdit = new WrapperButton();

	private WrapperButton wbuErsatztypen = new WrapperButton();

	private WrapperLabel wlaLfdNummer = new WrapperLabel();
	private WrapperNumberField wnfLfdNummer = new WrapperNumberField();
	private WrapperLabel wlaZielmengeEinheit = new WrapperLabel();
	private WrapperLabel wlaPositionsart = new WrapperLabel();
	private WrapperComboBox wcbEinheit = new WrapperComboBox();

	private PanelEditorPlainText panelEditorPlainTextFormel = null;

	public WrapperComboBox getWcbEinheit() {
		return wcbEinheit;
	}

	private WrapperComboBox wcbPositionsart = new WrapperComboBox();
	private WrapperLabel wlaDimension1 = new WrapperLabel();
	private WrapperNumberField wnfDimension1 = new WrapperNumberField();
	private WrapperLabel wlaDimension2 = new WrapperLabel();
	private WrapperNumberField wnfDimension2 = new WrapperNumberField();
	private WrapperLabel wlaDimension3 = new WrapperLabel();
	private WrapperNumberField wnfDimension3 = new WrapperNumberField();
	private WrapperLabel wlaKalkpreis = new WrapperLabel();
	private WrapperNumberField wnfKalkpreis = new WrapperNumberField();
	private WrapperGotoButton wbuUnterstueckliste = null;
	private WrapperGotoButton wbuUnterstuecklisteAndererMandant = null;

	private WrapperLabel wlaBeginnterminOffset = new WrapperLabel();
	private WrapperNumberField wnfBeginnterminOffset = new WrapperNumberField();

	private WrapperCheckBox wcbSofortigeBestellung = new WrapperCheckBox();
	private WrapperCheckBox wcbRuestmenge = new WrapperCheckBox();
	private WrapperCheckBox wcbInitial = new WrapperCheckBox();

	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;
	private boolean bFruehzeitigeBeschaffung = false;

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaArtikeleinheit = new WrapperLabel();

	private WrapperLabel wlaBezeichnung = new WrapperLabel();

	private StuecklistepositionDto stuecklistepositionDto = null;

	private WrapperLabel wlaEinheitStk = new WrapperLabel();

	private WrapperLabel wlaEinheitKalkpreis = new WrapperLabel();

	private WrapperCheckBox wcbMitdrucken = new WrapperCheckBox();

	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";
	static final public String ACTION_SPECIAL_MONTAGEART_FROM_LISTE = "action_montageart_from_liste";

	private static final String ACTION_SPECIAL_ERSATZTYPEN = "action_special_ersatztypen";

	private final String ACTION_SPECIAL_NEUE_VERSION = PanelBasis.ACTION_MY_OWN_NEW + "NEUE_VERSION";

	static final public String ACTION_TEXT_FORMEL = "action_text_formel";

	private PanelQueryFLR panelQueryFLRMontageart = null;
	private PanelQueryFLR panelQueryFLRArtikelMitErsatztypen = null;

	private ArtikelDto letzterArtikel = null;

	public MontageartDto defaultMontageartDto = null;

	static final public String ACTION_SPECIAL_CALL_SCRIPT = "action_call_script";
	private WrapperButton wbuScript = new WrapperButton();

	private StuecklisteCreateScript scriptEngine;

	public PanelStuecklistepositionen(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameStueckliste) internalFrame;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_FRUEHZEITIGE_BESCHAFFUNG,
						ParameterFac.KATEGORIE_STUECKLISTE, LPMain.getTheClient().getMandant());
		bFruehzeitigeBeschaffung = (Boolean) parameter.getCWertAsObject();

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcbPositionsart;
	}

	private void setDefaults() throws Throwable {
		wcbEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllEinheiten());

		Map<String, String> m = new LinkedHashMap<String, String>();
		m.put(LocaleFac.POSITIONSART_IDENT, LocaleFac.POSITIONSART_IDENT);
		m.put(LocaleFac.POSITIONSART_HANDEINGABE, LocaleFac.POSITIONSART_HANDEINGABE);
		wcbPositionsart.setMap(m);

		MontageartDto[] dtos = DelegateFactory.getInstance().getStuecklisteDelegate().montageartFindByMandantCNr();

		if (dtos != null && dtos.length > 0) {
			defaultMontageartDto = dtos[0];
		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		letzterArtikel = wifArtikel.getArtikelDto();
		super.eventActionNew(eventObject, true, false);
		stuecklistepositionDto = new StuecklistepositionDto();
		panelEditorPlainTextFormel = null;

		leereAlleFelder(this);
		wnfMenge.setBigDecimal(new java.math.BigDecimal(0));
		wifArtikel.setArtikelDto(null);

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MONTAGEART_FROM_LISTE)) {
			dialogQueryMontageartFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CALL_SCRIPT)) {
			actionScript();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_NEUE_VERSION)) {
			// Neu aus Artikel.
			if (stuecklistepositionDto != null && stuecklistepositionDto.getArtikelIId() != null) {
				Integer artikelIId = stuecklistepositionDto.getArtikelIId();

				StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(artikelIId);

				Integer stuecklisteIId = null;

				if (stklDto != null) {
					stuecklisteIId = stklDto.getIId();
				}

				DialogArtikelkopieren d = new DialogArtikelkopieren(
						DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(artikelIId),
						getInternalFrame());
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
				if (d.getArtikelnummerNeu() != null) {
					Object[] o = null;
					if (stuecklisteIId != null) {
						o = DelegateFactory.getInstance().getStuecklisteDelegate().kopiereStueckliste(stuecklisteIId,
								d.getArtikelnummerNeu(), d.getZuKopierendeFelder(), d.getHerstellerIIdNeu(),
								stuecklistepositionDto.getIId());
					} else {
						o = DelegateFactory.getInstance().getArtikelDelegate().kopiereArtikel(artikelIId,
								d.getArtikelnummerNeu(), d.getZuKopierendeFelder(), d.getHerstellerIIdNeu(),
								stuecklistepositionDto.getIId());
					}

					internalFrameStueckliste.getTabbedPaneStueckliste().getPanelQueryPositionen()
							.eventYouAreSelected(false);

					eventYouAreSelected(false);

					boolean bAndereSprachenKopiert = (Boolean) o[1];

					if (bAndereSprachenKopiert == true) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("artikel.info.mehreresprachenkopiert"));
					}

					boolean bEsSindZukuenftigePreisevorhanden = (Boolean) o[2];
					if (bEsSindZukuenftigePreisevorhanden == true) {

						DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
								LPMain.getTextRespectUISPr("artikel.info.zukuenftigepreisekopiert"));
					}
				}

			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ERSATZTYPEN)) {

			if (stuecklistepositionDto.getArtikelIId() == null) {

				panelQueryFLRArtikelMitErsatztypen = ArtikelFilterFactory.getInstance()
						.createPanelFLRArtikel(getInternalFrame(), stuecklistepositionDto.getArtikelIId(), false, true);
				new DialogQuery(panelQueryFLRArtikelMitErsatztypen);
			} else {
				DialogErsatztypen d = new DialogErsatztypen(this, stuecklistepositionDto, false);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

			}

		}
	}

	protected void actionScriptPosition() throws ExceptionLP {
		StuecklisteScript script = new StuecklisteScript(internalFrameStueckliste.getStuecklisteDto(),
				stuecklistepositionDto);
		BigDecimal v = script.getValue();
		System.out.println("Value = " + v + "<");
		if (v != null) {
			wnfMenge.setBigDecimal(v);
		}
	}

	private ScriptStuecklisteServiceDto createServiceDto() throws Throwable {
		ScriptStuecklisteServiceDto dto = new ScriptStuecklisteServiceDto();
		dto.setInternalFrame(internalFrameStueckliste);
		dto.setStuecklisteId(internalFrameStueckliste.getStuecklisteDto().getIId());
		dto.setPanelBasisScript(new PanelStuecklistepositionenScript(getInternalFrame(), "panelbasis", null));

		StuecklistepositionDto[] positionDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklistepositionFindByStuecklisteIIdAllData(dto.getStuecklisteId());
		dto.setStuecklistepositionenIn(positionDtos);

		StuecklistearbeitsplanDto[] arbeitDtos = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklistearbeitsplanFindByStuecklisteIId(dto.getStuecklisteId());
		dto.setStuecklistearbeitsplanIn(arbeitDtos);
		return dto;
	}

	protected void actionScriptPositionen() throws ExceptionLP {
		try {
			StuecklisteScriptartDto scriptartDto = internalFrameStueckliste.getStuecklisteDto()
					.getStuecklisteScriptartDto();
			if (scriptartDto == null)
				return;

			ScriptStuecklisteServiceDto serviceDto = createServiceDto();
			scriptEngine = new StuecklisteCreateScript(serviceDto, scriptartDto.getCScript());
			scriptEngine.getValue();
			scriptEngine = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	protected void actionScript() throws ExceptionLP {
		if (internalFrameStueckliste.getStuecklisteDto().getStuecklisteScriptartIId() == null) {
			actionScriptPosition();
		} else {
			actionScriptPositionen();
		}
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		Object[] o = internalFrameStueckliste.getTabbedPaneStueckliste().getPanelQueryPositionen().getSelectedIds();
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				StuecklistepositionDto toRemove = new StuecklistepositionDto();
				toRemove.setIId((Integer) o[i]);
				toRemove.setStuecklisteIId(stuecklistepositionDto.getStuecklisteIId());
				DelegateFactory.getInstance().getStuecklisteDelegate().removeStuecklisteposition(toRemove);
			}
		}

		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {

		stuecklistepositionDto.setPositionsartCNr((String) wcbPositionsart.getKeyOfSelectedItem());
		stuecklistepositionDto.setStuecklisteIId(internalFrameStueckliste.getStuecklisteDto().getIId());
		stuecklistepositionDto.setCKommentar(wtfKommentar.getText());
		stuecklistepositionDto.setCPosition(wtfPosition.getText());

		if (panelEditorPlainTextFormel != null) {
			stuecklistepositionDto.setXFormel(panelEditorPlainTextFormel.getPlainText());
		}

		stuecklistepositionDto.setBMitdrucken(wcbMitdrucken.getShort());
		stuecklistepositionDto.setBRuestmenge(wcbRuestmenge.getShort());
		stuecklistepositionDto.setBInitial(wcbInitial.getShort());
		if (wnfDimension1.getBigDecimal() != null) {
			stuecklistepositionDto.setFDimension1(wnfDimension1.getBigDecimal().floatValue());
		} else {
			stuecklistepositionDto.setFDimension1(null);
		}

		if (wnfDimension2.getBigDecimal() != null) {
			stuecklistepositionDto.setFDimension2(wnfDimension2.getBigDecimal().floatValue());
		} else {
			stuecklistepositionDto.setFDimension2(null);
		}

		if (wnfDimension3.getBigDecimal() != null) {
			stuecklistepositionDto.setFDimension3(wnfDimension3.getBigDecimal().floatValue());
		} else {
			stuecklistepositionDto.setFDimension3(null);
		}

		if (bFruehzeitigeBeschaffung == true) {
			if (wcbSofortigeBestellung.isSelected()) {
				stuecklistepositionDto.setIBeginnterminoffset(-999);
			} else {
				stuecklistepositionDto.setIBeginnterminoffset(0);
			}
		} else {
			stuecklistepositionDto.setIBeginnterminoffset(wnfBeginnterminOffset.getInteger());
		}

		stuecklistepositionDto.setNMenge(wnfMenge.getBigDecimal());
		stuecklistepositionDto.setNKalkpreis(wnfKalkpreis.getBigDecimal());
		stuecklistepositionDto.setILfdnummer(wnfLfdNummer.getInteger());
		stuecklistepositionDto.setEinheitCNr((String) wcbEinheit.getKeyOfSelectedItem());

		if (((String) wcbPositionsart.getKeyOfSelectedItem()).equals(LocaleFac.POSITIONSART_IDENT)) {
			stuecklistepositionDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		} else {
			stuecklistepositionDto.setArtikelIId(null);
			stuecklistepositionDto.setArtikelDto(null);
			stuecklistepositionDto.setSHandeingabe(wtfHandartikel.getText());

		}

	}

	protected void dto2Components() throws Throwable {
		wlaZielmengeEinheit.setForeground(Color.BLACK);
		wlaZielmengeEinheit.setToolTipText("");

		wbuUnterstuecklisteAndererMandant.setOKey(null);
		wbuUnterstuecklisteAndererMandant.setEnabled(false);
		try {
			wcbEinheit.setKeyOfSelectedItem(stuecklistepositionDto.getEinheitCNr());
		} catch (Throwable ex) {
			handleException(ex, false);
		}

		if (bFruehzeitigeBeschaffung == true) {

			if (stuecklistepositionDto.getIBeginnterminoffset() == -999) {
				wcbSofortigeBestellung.setSelected(true);
			} else {
				wcbSofortigeBestellung.setSelected(false);
			}
		} else {
			wnfBeginnterminOffset.setInteger(stuecklistepositionDto.getIBeginnterminoffset());
		}

		// wtfArtikel.setText(stuecklistepositionDto.getArtikelDto().formatArtikelbezeichnung());
		wtfMontageart.setText(stuecklistepositionDto.getMontageartDto().getCBez());
		wtfKommentar.setText(stuecklistepositionDto.getCKommentar());
		wtfPosition.setText(stuecklistepositionDto.getCPosition());
		wtfFormel.setText(stuecklistepositionDto.getXFormel());
		if (stuecklistepositionDto.getFDimension1() != null) {
			wnfDimension1.setBigDecimal(new BigDecimal(stuecklistepositionDto.getFDimension1()));
		} else {
			wnfDimension1.setBigDecimal(null);
		}
		if (stuecklistepositionDto.getFDimension2() != null) {
			wnfDimension2.setBigDecimal(new BigDecimal(stuecklistepositionDto.getFDimension2()));
		} else {
			wnfDimension2.setBigDecimal(null);
		}
		if (stuecklistepositionDto.getFDimension3() != null) {
			wnfDimension3.setBigDecimal(new BigDecimal(stuecklistepositionDto.getFDimension3()));
		} else {
			wnfDimension3.setBigDecimal(null);
		}
		wnfMenge.setBigDecimal(stuecklistepositionDto.getNMenge());
		wnfKalkpreis.setBigDecimal(stuecklistepositionDto.getNKalkpreis());
		wnfLfdNummer.setInteger(stuecklistepositionDto.getILfdnummer());

		wcbMitdrucken.setShort(stuecklistepositionDto.getBMitdrucken());

		wcbRuestmenge.setShort(stuecklistepositionDto.getBRuestmenge());
		wcbInitial.setShort(stuecklistepositionDto.getBInitial());

		BigDecimal losgroesse = internalFrameStueckliste.getStuecklisteDto().getNLosgroesse();

		if (losgroesse == null) {
			losgroesse = BigDecimal.ONE;
		}
		BigDecimal bdZielmenge = null;
		try {
			if (wcbRuestmenge.isSelected()) {
				losgroesse = BigDecimal.ONE;
			}

			bdZielmenge = DelegateFactory.getInstance().getStuecklisteDelegate()
					.berechneZielmenge(stuecklistepositionDto.getIId(), losgroesse);

			// SP7024
			if (bdZielmenge.doubleValue() < 0) {
				if (internalFrameStueckliste.isbNegativeZielmengenWarnungAnzeigen()) {
					StuecklisteDto stklDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stuecklistepositionDto.getArtikelIId());
					if (stklDto != null && Helper.short2boolean(stklDto.getBFremdfertigung()) == false) {

						ArtikelDto aDtoStkl = DelegateFactory.getInstance().getArtikelDelegate()
								.artikelFindByPrimaryKey(stklDto.getArtikelIId());
						String s = LPMain.getMessageTextRespectUISPr(
								"stkl.error.zielmengekleinernull.beieigengefertigterstueckliste",
								stuecklistepositionDto.getArtikelDto().getCNr(), aDtoStkl.getCNr());

						JCheckBox checkbox = new JCheckBox(LPMain.getMessageTextRespectUISPr(
								"stkl.error.zielmengekleinernull.beieigengefertigterstueckliste.nichtmehranzeigen"));
						Object[] params = { s, checkbox };
						JOptionPane.showMessageDialog(getInternalFrame(), params,
								LPMain.getTextRespectUISPr("lp.warning"), JOptionPane.WARNING_MESSAGE);
						if (checkbox.isSelected()) {
							internalFrameStueckliste.setBNegativeZielmengenWarnungAnzeigen(false);
						}

					}

				}
			}

		}

		catch (ExceptionLP ex1) {

			if (ex1.getICode() == EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT) {
				wlaZielmengeEinheit.setForeground(Color.RED);
				wlaZielmengeEinheit.setToolTipText("Fehler in der Einheitenkonvertierung");
			} else {
				handleException(ex1, false);
			}

		} catch (Throwable ex1) {
			handleException(ex1, false);
		}

		if (bdZielmenge == null) {
			bdZielmenge = new BigDecimal(0);
		}

		if (stuecklistepositionDto.getArtikelDto().getNVerschnittmenge() != null) {

			bdZielmenge = bdZielmenge.add(stuecklistepositionDto.getArtikelDto().getNVerschnittmenge());

		}

		bdZielmenge = Helper.rundeKaufmaennisch(bdZielmenge, 4);
		losgroesse = Helper.rundeKaufmaennisch(losgroesse, 2);

		EinheitDto dto = DelegateFactory.getInstance().getSystemDelegate()
				.einheitFindByPrimaryKey(stuecklistepositionDto.getArtikelDto().getEinheitCNr());

		wlaArtikeleinheit.setText(Helper.formatZahl(bdZielmenge, 3, LPMain.getInstance().getUISprLocale()) + " "
				+ dto.formatBez() + ", " + LPMain.getTextRespectUISPr("stkl.beilosgroesse") + " "
				+ Helper.formatZahl(losgroesse, 2, LPMain.getInstance().getUISprLocale()));

		if (stuecklistepositionDto.getArtikelDto().getArtikelartCNr().equals(ArtikelFac.ARTIKELART_HANDARTIKEL)) {
			wcbPositionsart.setKeyOfSelectedItem(LocaleFac.POSITIONSART_HANDEINGABE);
			wtfHandartikel.setText(stuecklistepositionDto.getArtikelDto().getCBezAusSpr());
			wifArtikel.setArtikelDto(null);
		} else {
			dto2ComponentsArtikelDto();

		}

		panelEditorPlainTextFormel = null;

		this.setStatusbarPersonalIIdAendern(stuecklistepositionDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(stuecklistepositionDto.getTAendern());
		this.setStatusbarPersonalIIdAnlegen(stuecklistepositionDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(stuecklistepositionDto.getTAnlegen());
	}

	protected void dto2ComponentsArtikelDto() throws Throwable, ExceptionLP {
		wcbPositionsart.setKeyOfSelectedItem(LocaleFac.POSITIONSART_IDENT);
		wifArtikel.setArtikelDto(stuecklistepositionDto.getArtikelDto());

		wtfHandartikel.setText(null);
		// Wenn Stueckliste, dann Goto Unterstueckliste Key setzen
		StuecklisteDto stuecklisteDto = DelegateFactory.getInstance().getStuecklisteDelegate()
				.stuecklisteFindByMandantCNrArtikelIIdOhneExc(stuecklistepositionDto.getArtikelDto().getIId());
		if (stuecklisteDto != null) {
			wbuUnterstueckliste.setEnabled(false);
			wbuUnterstueckliste.setOKey(stuecklisteDto.getIId());
		} else {
			wbuUnterstueckliste.setOKey(null);
			wbuUnterstueckliste.setEnabled(false);
		}

		wbuUnterstuecklisteAndererMandant.setEnabled(false);
		wbuUnterstuecklisteAndererMandant.setOKey(stuecklistepositionDto.getArtikelDto().getIId());
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		panelEditorPlainTextFormel = null;
	}

	protected void eventActionText(ActionEvent e) throws Throwable {
		if (e.getSource().equals(wbuFormelEdit)) {

			String plaintText = null;
			if (panelEditorPlainTextFormel != null) {
				plaintText = panelEditorPlainTextFormel.getPlainText();
			} else {
				plaintText = stuecklistepositionDto.getXFormel();
			}

			panelEditorPlainTextFormel = new PanelEditorPlainText(getInternalFrame(), wtfFormel, getAdd2Title(),
					plaintText, getLockedstateDetailMainKey().getIState());
			panelEditorPlainTextFormel.setColumnsMax(StuecklisteFac.MAX_FORMEL);

			getInternalFrame().showPanelDialog(panelEditorPlainTextFormel);
			getInternalFrame().getContentPane().validate();

		} else {
			getInternalFrame().showPanelEditorPlainText(wtfPosition, this.getAdd2Title(), wtfPosition.getText(),
					getLockedstateDetailMainKey().getIState());
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		try {
			wifArtikel.refresh();
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (stuecklistepositionDto.getIId() == null) {

					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdAktuellePosition = (Integer) internalFrameStueckliste.getTabbedPaneStueckliste()
								.getPanelQueryPositionen().getSelectedId();

						// erstepos: 0 die erste Position steht an der Stelle 1
						Integer iSortAktuellePosition = new Integer(1);

						// erstepos: 1 die erste Position steht an der Stelle 1
						if (iIdAktuellePosition != null) {
							iSortAktuellePosition = DelegateFactory.getInstance().getStuecklisteDelegate()
									.stuecklistepositionFindByPrimaryKey(iIdAktuellePosition).getISort();

							// Die bestehenden Positionen muessen Platz fuer die
							// neue schaffen
							DelegateFactory.getInstance().getStuecklisteDelegate()
									.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
											internalFrameStueckliste.getStuecklisteDto().getIId(),
											iSortAktuellePosition.intValue());
						}
						// Die neue Position wird an frei gemachte Position
						// gesetzt
						stuecklistepositionDto.setISort(iSortAktuellePosition);
					}

					stuecklistepositionDto.setIId(DelegateFactory.getInstance().getStuecklisteDelegate()
							.createStuecklisteposition(stuecklistepositionDto));
					setKeyWhenDetailPanel(stuecklistepositionDto.getIId());
					stuecklistepositionDto = DelegateFactory.getInstance().getStuecklisteDelegate()
							.stuecklistepositionFindByPrimaryKey(stuecklistepositionDto.getIId());

					BigDecimal bdZielmenge = null;
					try {
						bdZielmenge = DelegateFactory.getInstance().getStuecklisteDelegate()
								.berechneZielmenge(stuecklistepositionDto.getIId());
					}

					catch (ExceptionLP ex1) {
						handleException(ex1, false);
					}

				} else {
					DelegateFactory.getInstance().getStuecklisteDelegate()
							.updateStuecklisteposition(stuecklistepositionDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(internalFrameStueckliste.getStuecklisteDto().getIId() + "");
				}
				eventYouAreSelected(false);
			}

		} finally {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == panelQueryFLRMontageart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				MontageartDto montagaeartTempDto = DelegateFactory.getInstance().getStuecklisteDelegate()
						.montageartFindByPrimaryKey((Integer) key);

				wtfMontageart.setText(montagaeartTempDto.getCBez());
				stuecklistepositionDto.setMontageartIId(montagaeartTempDto.getIId());
			} else if (e.getSource() == panelQueryFLRArtikelMitErsatztypen) {
				Integer artikelIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				stuecklistepositionDto.setArtikelIId(artikelIId);

				DialogErsatztypen d = new DialogErsatztypen(this, stuecklistepositionDto, true);
				LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);
			} else if (e.getSource() == wifArtikel) {

				if (stuecklistepositionDto.getArtikelDto() != null) {
					String einheitVorher = stuecklistepositionDto.getArtikelDto().getEinheitCNr();

					// Wenn die Artikeleinheit des Vorgaengerartikel gleich war,
					// dann die Zielmengeneinheit unveraendert lassen

					if (einheitVorher.equals(wifArtikel.getArtikelDto().getEinheitCNr())) {
						// NIX
					} else {
						wcbEinheit.setKeyOfSelectedItem(wifArtikel.getArtikelDto().getEinheitCNr());
					}

				} else {
					wcbEinheit.setKeyOfSelectedItem(wifArtikel.getArtikelDto().getEinheitCNr());
				}
				stuecklistepositionDto.setArtikelDto(wifArtikel.getArtikelDto());
			}

			if (scriptEngine != null) {
				scriptEngine.eventItemchanged(e);
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wlaArtikeleinheit.setHorizontalAlignment(SwingConstants.LEFT);
		wtfHandartikel.setMandatoryField(true);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wbuMontageart.setText(LPMain.getTextRespectUISPr("stkl.montageart") + "...");

		wcbRuestmenge.setText(LPMain.getTextRespectUISPr("stkl.ruestmenge"));

		wtfMontageart.setMandatoryField(true);
		wtfMontageart.setActivatable(false);
		wlaDimension1.setText(LPMain.getTextRespectUISPr("stkl.breite"));
		wlaDimension2.setText(LPMain.getTextRespectUISPr("stkl.hoehe"));
		wlaDimension3.setText(LPMain.getTextRespectUISPr("stkl.tiefe"));
		wlaZielmengeEinheit.setText(LPMain.getTextRespectUISPr("stkl.zielmenge") + ":");

		wnfDimension1.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());
		wnfDimension2.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());
		wnfDimension3.setFractionDigits(Defaults.getInstance().getIUINachkommastellenDimensionen());

		wlaKalkpreis.setText(LPMain.getTextRespectUISPr("stkl.kalkpreis"));

		// Mandantenwaehrung
		String whg = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(LPMain.getTheClient().getMandant()).getWaehrungCNr();

		wlaEinheitKalkpreis.setText(whg.trim());

		wlaEinheitStk.setText(SystemFac.EINHEIT_STUECK.trim());
		wlaEinheitStk.setHorizontalAlignment(SwingConstants.LEFT);

		wcbEinheit.setMandatoryField(true);
		wcbEinheit.addActionListener(new PanelStuecklistepositionen_wcbEinheit_actionAdapter(this));

		wbuPositionEdit.setMinimumSize(new Dimension(23, 23));
		wbuPositionEdit.setPreferredSize(new Dimension(23, 23));
		wbuPositionEdit.setActionCommand(ACTION_TEXT);
		wbuPositionEdit.setToolTipText(LPMain.getTextRespectUISPr("text.bearbeiten"));
		wbuPositionEdit.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png")));
		wbuPositionEdit.addActionListener(this);
		wbuPositionEdit.getActionMap().put(ACTION_TEXT, new ButtonAbstractAction(this, ACTION_TEXT));

		wlaFormel.setText(LPMain.getTextRespectUISPr("stl.positionen.formel"));

		wbuFormelEdit.setMinimumSize(new Dimension(23, 23));
		wbuFormelEdit.setPreferredSize(new Dimension(23, 23));
		wbuFormelEdit.setActionCommand(ACTION_TEXT);
		wbuFormelEdit.setToolTipText(LPMain.getTextRespectUISPr("text.bearbeiten"));
		wbuFormelEdit.setIcon(new ImageIcon(getClass().getResource("/com/lp/client/res/notebook.png")));
		wbuFormelEdit.addActionListener(this);
		wbuFormelEdit.getActionMap().put(ACTION_TEXT, new ButtonAbstractAction(this, ACTION_TEXT));

		wtfFormel.setColumnsMax(3000);
		wtfFormel.setActivatable(false);

		wbuErsatztypen.setText(LPMain.getTextRespectUISPr("stkl.artikelersatz"));
		wbuErsatztypen.addActionListener(this);
		wbuErsatztypen.setActionCommand(ACTION_SPECIAL_ERSATZTYPEN);

		wcbMitdrucken.setText(LPMain.getTextRespectUISPr("stkl.positionen.mitdrucken"));
		wcbMitdrucken.setMnemonic('A');
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaKommentar.setText(LPMain.getTextRespectUISPr("lp.kommentar"));
		wlaLfdNummer.setText(LPMain.getTextRespectUISPr("stkl.positionen.lfdnummer"));
		wnfLfdNummer.setFractionDigits(0);
		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaPositionsart.setText(LPMain.getTextRespectUISPr("lp.positionsart"));

		wtfKommentar.setColumnsMax(StuecklisteFac.FieldLength.STUECKLISTEPOSITION_KOMMENTAR);

		wnfMenge.setMandatoryField(true);

		wnfKalkpreis.setFractionDigits(Defaults.getInstance().getIUINachkommastellenPreiseAllgemein());

		int iNachkommastellenMenge = Defaults.getInstance().getIUINachkommastellenMenge();

		wnfMenge.setFractionDigits(iNachkommastellenMenge);

		wlaPosition.setText(LPMain.getTextRespectUISPr("lp.position"));

		int iLaengenBezeichung = DelegateFactory.getInstance().getArtikelDelegate().getLaengeArtikelBezeichnungen();

		wtfHandartikel.setColumnsMax(iLaengenBezeichung);
		wtfPosition.setColumnsMax(StuecklisteFac.MAX_POSITION);

		getInternalFrame().addItemChangedListener(this);
		wcbPositionsart.addActionListener(new PanelStuecklistepositionen_wcbPositionsart_actionAdapter(this));
		wcbPositionsart.setMandatoryField(true);

		wifArtikel.getWtfIdent().addFocusListener(new PanelStuecklistepositionen_wtfIdent_focusAdapter(this));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		wbuMontageart.setActionCommand(ACTION_SPECIAL_MONTAGEART_FROM_LISTE);
		wbuMontageart.addActionListener(this);

		wbuScript.setText(LPMain.getTextRespectUISPr("lp.script.stk_positionen_01"));
		wbuScript.setActionCommand(ACTION_SPECIAL_CALL_SCRIPT);
		wbuScript.addActionListener(this);

		HelperClient.setMinimumAndPreferredSize(wcbRuestmenge, HelperClient.getSizeFactoredDimension(100));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wlaPositionsart, new GridBagConstraints(0, 0, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ERSATZTYPENVERWALTUNG)) {

			jpaWorkingOn.add(wbuErsatztypen, new GridBagConstraints(0, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, Defaults.sizeFactor(80)), -150, 0));
		}

		jpaWorkingOn.add(wcbPositionsart, new GridBagConstraints(1, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));
		
		
		wcbInitial.setText(LPMain.getTextRespectUISPr("stkl.positionen.initialkosten"));
		wcbInitial.setToolTipText(LPMain.getTextRespectUISPr("stkl.positionen.initialkosten.tooltip"));
		
		jpaWorkingOn.add(wcbInitial, new GridBagConstraints(4, 0, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wlaLfdNummer, new GridBagConstraints(5, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLfdNummer, new GridBagConstraints(6, 0, 1, 1, 0.01, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wifArtikel.getWtfIdent().setUppercaseField(true);
		jpaWorkingOn.add(wifArtikel.getKundenidentnummerButton(), new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(2, 1, 5, 1, 0.01, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));

		jpaWorkingOn.add(wlaEinheitStk, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40, 0));

		jpaWorkingOn.add(wlaPosition, new GridBagConstraints(5, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPosition, new GridBagConstraints(6, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 26), 0, 0));

		jpaWorkingOn.add(wbuPositionEdit, new GridBagConstraints(6, 2, 1, 1, 0, 0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaArtikeleinheit, new GridBagConstraints(1, 2, 4, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 15, 0));

		jpaWorkingOn.add(wlaZielmengeEinheit, new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wcbEinheit, new GridBagConstraints(3, 3, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbRuestmenge, new GridBagConstraints(4, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKalkpreis, new GridBagConstraints(5, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKalkpreis, new GridBagConstraints(6, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));
		wlaEinheitKalkpreis.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaEinheitKalkpreis, new GridBagConstraints(6, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 15, 0));

		jpaWorkingOn.add(wlaDimension1, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));
		jpaWorkingOn.add(wnfDimension1, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));
		jpaWorkingOn.add(wlaDimension2, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfDimension2, new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));
		jpaWorkingOn.add(wlaDimension3, new GridBagConstraints(4, 5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfDimension3, new GridBagConstraints(5, 5, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -45, 0));

		jpaWorkingOn.add(wbuMontageart, new GridBagConstraints(0, 6, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMontageart, new GridBagConstraints(1, 6, 6, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, 10, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, 10, 6, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		// Formel
		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_STUECKLISTE_MIT_FORMELN)) {
			jpaWorkingOn.add(wlaFormel, new GridBagConstraints(0, 11, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfFormel, new GridBagConstraints(1, 11, 6, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 26), 0, 0));
			jpaWorkingOn.add(wbuFormelEdit, new GridBagConstraints(6, 11, 1, 1, 0, 0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		}
		// Formel

		jpaWorkingOn.add(wcbMitdrucken, new GridBagConstraints(1, 12, 3, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (bFruehzeitigeBeschaffung == true) {
			wcbSofortigeBestellung.setText(LPMain.getTextRespectUISPr("stk.positionen.sofortigebestellung"));
			jpaWorkingOn.add(wcbSofortigeBestellung, new GridBagConstraints(6, 12, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		} else {
			wlaBeginnterminOffset.setText(LPMain.getTextRespectUISPr("stk.positionen.beginnterminoffset"));
			jpaWorkingOn.add(wlaBeginnterminOffset, new GridBagConstraints(5, 12, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			wnfBeginnterminOffset.setMandatoryField(true);
			wnfBeginnterminOffset.setFractionDigits(0);
			jpaWorkingOn.add(wnfBeginnterminOffset, new GridBagConstraints(6, 12, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

		jpaWorkingOn.add(wbuScript, new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

		// Goto Unterstueckliste
		wbuUnterstuecklisteAndererMandant = new WrapperGotoButton(LPMain.getTextRespectUISPr("button.stueckliste"),
				com.lp.util.GotoHelper.GOTO_STUECKLISTE_ANDERER_MANDANT_AUSWAHL);
		wbuUnterstuecklisteAndererMandant.setEnabled(false);
		wbuUnterstuecklisteAndererMandant.setActivatable(false);
		wbuUnterstuecklisteAndererMandant.getWrapperButtonGoTo().setToolTipText("GOTO_ANDERER_MANDANT");
		wbuUnterstuecklisteAndererMandant
				.setMinimumSize(new Dimension(110, HelperClient.getToolsPanelButtonDimension().height));
		wbuUnterstuecklisteAndererMandant
				.setPreferredSize(new Dimension(110, HelperClient.getToolsPanelButtonDimension().height));

		// Goto Unterstueckliste
		wbuUnterstueckliste = new WrapperGotoButton(LPMain.getTextRespectUISPr("button.stueckliste"),
				com.lp.util.GotoHelper.GOTO_STUECKLISTE_AUSWAHL);
		wbuUnterstueckliste.setEnabled(false);
		wbuUnterstueckliste.setActivatable(false);
		wbuUnterstueckliste.getWrapperButtonGoTo().setToolTipText(LPMain.getTextRespectUISPr("lp.goto"));

		HelperClient.setMinimumAndPreferredSize(wbuUnterstueckliste,
				new Dimension(Defaults.sizeFactor(100), HelperClient.getToolsPanelButtonDimension().height));

		if (LPMain.getInstance().getDesktopController()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM)) {

			jpaButtonAction.add(wbuUnterstuecklisteAndererMandant);

		} else {
			jpaButtonAction.add(wbuUnterstueckliste);
		}

		boolean bBenutzerIstInMandantensprechAngemeldet = false;
		if (LPMain.getTheClient().getLocMandantAsString().equals(LPMain.getTheClient().getLocUiAsString())) {
			bBenutzerIstInMandantensprechAngemeldet = true;
		}
		if (bBenutzerIstInMandantensprechAngemeldet) {
			// Hier den zusaetzlichen Button aufs Panel bringen
			createAndSaveAndShowButton("/com/lp/client/res/up_plus.png", LPMain.getTextRespectUISPr("stkl.neueversion"),
					ACTION_SPECIAL_NEUE_VERSION, null);
		}

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	void dialogQueryMontageartFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRMontageart = StuecklisteFilterFactory.getInstance().createPanelFLRMontageart(getInternalFrame(),
				stuecklistepositionDto.getMontageartIId());
		new DialogQuery(panelQueryFLRMontageart);
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		// PJ18550
		if (internalFrameStueckliste.getTabbedPaneStueckliste().bStuecklistenfreigabe == true) {
			// Wenn Fregegeben, dann nicht mehr aenderbar
			if (internalFrameStueckliste.getStuecklisteDto() != null
					&& internalFrameStueckliste.getStuecklisteDto().getTFreigabe() != null) {
				lockStateValue = new LockStateValue(PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
			}
		}

		return lockStateValue;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			wlaArtikeleinheit.setText("");
			clearStatusbar();

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT, ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());
			wcbEinheit.setKeyOfSelectedItem(Helper.fitString2Length(parameter.getCWert(), 15, ' '));

			if (key != null && key.equals(LPMain.getLockMeForNew())) {

				wnfMenge.setBigDecimal(new java.math.BigDecimal(1));

				if (stuecklistepositionDto != null && defaultMontageartDto != null) {
					stuecklistepositionDto.setMontageartIId(defaultMontageartDto.getIId());
					wtfMontageart.setText(defaultMontageartDto.getCBez());
				}

				if (letzterArtikel != null) {
					wifArtikel.getWtfIdent().setText(letzterArtikel.getCNr());
					wcbEinheit.setKeyOfSelectedItem(letzterArtikel.getEinheitCNr());
				}

				if (bFruehzeitigeBeschaffung == false) {
					wnfBeginnterminOffset.setInteger(0);
				}

			}
		} else {
			stuecklistepositionDto = DelegateFactory.getInstance().getStuecklisteDelegate()
					.stuecklistepositionFindByPrimaryKey((Integer) key);

			dto2Components();
		}

		if (internalFrameStueckliste.getStuecklisteDto().getPartnerIId() != null) {
			KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByiIdPartnercNrMandantOhneExc(
					internalFrameStueckliste.getStuecklisteDto().getPartnerIId(), LPMain.getTheClient().getMandant());
			if (kundeDto != null) {
				wifArtikel.setKundeIId(kundeDto.getIId());
			}
		} else {
			wifArtikel.setKundeIId(null);
		}

		wbuPositionEdit.setEnabled(true);
		wbuFormelEdit.setEnabled(true);

		if (Helper.short2boolean(internalFrameStueckliste.getStuecklisteDto().getBMitFormeln()) == true) {
			wlaFormel.setVisible(true);
			wtfFormel.setVisible(true);
			wbuFormelEdit.setVisible(true);
		} else {
			wlaFormel.setVisible(false);
			wtfFormel.setVisible(false);
			wbuFormelEdit.setVisible(false);
		}

	}

	public void wcbPositionsart_actionPerformed(ActionEvent e) {
		String key = (String) wcbPositionsart.getKeyOfSelectedItem();
		if (key.equals(LocaleFac.POSITIONSART_IDENT)) {
			wtfHandartikel.setText(null);
			jpaWorkingOn.remove(wlaBezeichnung);
			jpaWorkingOn.remove(wtfHandartikel);
			wtfHandartikel.setEditable(true);
			wbuErsatztypen.setVisible(true);

			if (wbuMontageart.isEnabled()) {
				wifArtikel.getWbuArtikel().setEnabled(true);
				// SP5022
				if (wifArtikel.getKundenidentnummerButton() != null) {
					wifArtikel.getKundenidentnummerButton().setEnabled(true);
				}
			}
			jpaWorkingOn.add(wifArtikel.getKundenidentnummerButton(), new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));
			jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 22, 2, 2), 0, 0));
			jpaWorkingOn.repaint();
			jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, 1, 1, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(2, 1, 4, 1, 0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		} else if (key.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			wifArtikel.getWtfIdent().setText(null);
			wtfHandartikel.setActivatable(true);
			wbuErsatztypen.setVisible(false);
			if (stuecklistepositionDto != null) {
				stuecklistepositionDto.setArtikelIId(null);
			}
			jpaWorkingOn.remove(wifArtikel.getKundenidentnummerButton());
			jpaWorkingOn.remove(wifArtikel.getWbuArtikel());
			jpaWorkingOn.remove(wifArtikel.getWtfIdent());
			jpaWorkingOn.remove(wifArtikel.getWtfBezeichnung());
			jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wtfHandartikel, new GridBagConstraints(1, 1, 5, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.repaint();
			jpaWorkingOn.revalidate();
		}

	}

	void wtfIdent_focusLost(FocusEvent e) {
		if (wifArtikel.getArtikelDto() != null && stuecklistepositionDto.getIId() == null) {
			wcbEinheit.setKeyOfSelectedItem(wifArtikel.getArtikelDto().getEinheitCNr());
		}
	}

	public void wcbEinheit_actionPerformed(ActionEvent e) {

		wlaZielmengeEinheit.setForeground(Color.BLACK);
		wlaZielmengeEinheit.setToolTipText("");

		wnfDimension1.setVisible(false);
		wnfDimension2.setVisible(false);
		wnfDimension3.setVisible(false);

		wlaDimension1.setVisible(false);
		wlaDimension2.setVisible(false);
		wlaDimension3.setVisible(false);

		wlaEinheitStk.setVisible(true);

		try {
			EinheitDto einheitDto = DelegateFactory.getInstance().getSystemDelegate()
					.einheitFindByPrimaryKey((String) wcbEinheit.getKeyOfSelectedItem());

			if (einheitDto.getIDimension().intValue() == 0) {

				wnfDimension1.setInteger(null);
				wnfDimension2.setInteger(null);
				wnfDimension3.setInteger(null);

				wnfDimension1.setMandatoryField(false);
				wnfDimension2.setMandatoryField(false);
				wnfDimension3.setMandatoryField(false);

				wnfDimension1.setVisible(false);
				wnfDimension2.setVisible(false);
				wnfDimension3.setVisible(false);

				wlaDimension1.setVisible(false);
				wlaDimension2.setVisible(false);
				wlaDimension3.setVisible(false);

				wlaEinheitStk.setVisible(false);

			} else if (einheitDto.getIDimension().intValue() == 1) {
				wnfDimension2.setInteger(null);
				wnfDimension3.setInteger(null);

				wnfDimension1.setMandatoryField(true);
				wnfDimension2.setMandatoryField(false);
				wnfDimension3.setMandatoryField(false);

				wnfDimension1.setVisible(true);
				wnfDimension2.setVisible(false);
				wnfDimension3.setVisible(false);

				wlaDimension1.setVisible(true);
				wlaDimension2.setVisible(false);
				wlaDimension3.setVisible(false);

			} else if (einheitDto.getIDimension().intValue() == 2) {
				wnfDimension3.setInteger(null);

				wnfDimension1.setMandatoryField(true);
				wnfDimension2.setMandatoryField(true);
				wnfDimension3.setMandatoryField(false);

				wnfDimension1.setVisible(true);
				wnfDimension2.setVisible(true);
				wnfDimension3.setVisible(false);

				wlaDimension1.setVisible(true);
				wlaDimension2.setVisible(true);
				wlaDimension3.setVisible(false);

			} else if (einheitDto.getIDimension().intValue() == 3) {
				wnfDimension1.setMandatoryField(true);
				wnfDimension2.setMandatoryField(true);
				wnfDimension3.setMandatoryField(true);

				wnfDimension1.setVisible(true);
				wnfDimension2.setVisible(true);
				wnfDimension3.setVisible(true);

				wlaDimension1.setVisible(true);
				wlaDimension2.setVisible(true);
				wlaDimension3.setVisible(true);

			}
		} catch (Throwable ex) {
			handleException(ex, false);
		}

		// Positionseinheit nach Zieleinheit umrechnen
		ArtikelDto artikelTempDto = null;
		try {
			if (stuecklistepositionDto != null && stuecklistepositionDto.getArtikelIId() != null) {
				artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
						.artikelFindByPrimaryKey(stuecklistepositionDto.getArtikelIId());
				// MB->CK ab hier geaendert
				BigDecimal bdMengeUmgerechnet = DelegateFactory.getInstance().getSystemDelegate()
						.rechneUmInAndereEinheit(wnfMenge.getBigDecimal(), artikelTempDto.getEinheitCNr(),
								(String) wcbEinheit.getKeyOfSelectedItem(), stuecklistepositionDto.getIId());
				System.out.println("Menge umgerechnet: " + bdMengeUmgerechnet);

			}
		} catch (ExceptionLP ex1) {

			if (ex1.getICode() == EJBExceptionLP.FEHLER_EINHEITKONVERTIERUNG_KEIN_DIVISOR_DEFINIERT) {
				wlaZielmengeEinheit.setForeground(Color.RED);
				wlaZielmengeEinheit.setToolTipText("Fehler in der Einheitenkonvertierung");
			} else {
				handleException(ex1, false);
			}

		} catch (Throwable ex1) {
			handleException(ex1, false);
		}
	}
}

class PanelStuecklistepositionen_wcbEinheit_actionAdapter implements ActionListener {
	private PanelStuecklistepositionen adaptee;

	PanelStuecklistepositionen_wcbEinheit_actionAdapter(PanelStuecklistepositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbEinheit_actionPerformed(e);
	}
}

class PanelStuecklistepositionen_wcbPositionsart_actionAdapter implements ActionListener {
	private PanelStuecklistepositionen adaptee;

	PanelStuecklistepositionen_wcbPositionsart_actionAdapter(PanelStuecklistepositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbPositionsart_actionPerformed(e);
	}
}

class PanelStuecklistepositionen_wtfIdent_focusAdapter extends java.awt.event.FocusAdapter {
	PanelStuecklistepositionen adaptee;

	PanelStuecklistepositionen_wtfIdent_focusAdapter(PanelStuecklistepositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfIdent_focusLost(e);
	}
}
