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
package com.lp.client.finanz;

import javax.swing.event.ChangeEvent;

import com.lp.client.finanz.sepaimportassistent.SepaImportController;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.AssistentView;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.BuchenDelegate;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.FinanzDelegate;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.SepaImportFac;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Diese Klasse kuemmert sich um die Panels der Bankverbindungen in der FiBu</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class TabbedPaneBankverbindung extends TabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelQuery panelQueryBankverbindung = null;
	private PanelFinanzBankverbindungKopfdaten panelDetailBankverbindungKopfdaten = null;
	private PanelSplit panelSplit3 = null;
	private PanelQuery panelQueryBuchungen = null;
	private PanelFinanzBuchungDetails panelDetailBuchung = null;
	private PanelQuery panelQuerySepakontoauszug = null;
	
	private static final String ACTION_SPECIAL_SEPAIMPORT = "action_special_import_sepa";
	private static final String ACTION_SPECIAL_SEPAVERBUCHEN = "action_special_verbuche_sepa";
	private static final String ACTION_SEPAKONTOAUSZUG_DELETE = "action_special_storniere_sepa";
	
	private final String BUTTON_SEPAIMPORT = PanelBasis.LEAVEALONE + ACTION_SPECIAL_SEPAIMPORT;
	private final String BUTTON_SEPAVERBUCHEN = PanelBasis.LEAVEALONE + ACTION_SPECIAL_SEPAVERBUCHEN;
	private final String BUTTON_SEPAKONTOAUSZUG_DELETE = PanelBasis.LEAVEALONE + ACTION_SEPAKONTOAUSZUG_DELETE;

	private int IDX_BANKVERBINDUGEN = -1;
	public int IDX_KOPFDATEN = -1;
	private int IDX_BUCHUNGEN = -1;
	private int IDX_SEPAKONTOAUSZUG = -1;

	private BankverbindungDto bankverbindungDto = null;
	private SepakontoauszugDto sepakontoauszugDto = null;

	private boolean bVollversion = false;
	private Boolean bZusatzfunktionSepa = null;

	public TabbedPaneBankverbindung(InternalFrame internalFrameI)
			throws Throwable {
		super(internalFrameI, LPMain.getInstance().getTextRespectUISPr(
				"finanz.tab.unten.bankverbindungen.title"));
		jbInit();
		initComponents();
	}

	public BankverbindungDto getBankverbindungDto() {
		return bankverbindungDto;
	}

	public void setBankverbindungDto(BankverbindungDto bankverbindungDto) {
		this.bankverbindungDto = bankverbindungDto;
		if (getBankverbindungDto() != null) {
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getBankverbindungDto().getCBez());
		} else {
			getInternalFrame()
					.setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		}
	}

	private void jbInit() throws Throwable {
		// Berechtigungen
		bVollversion = ((InternalFrameFinanz) getInternalFrame())
				.getBVollversion();

		int index = 0;
		// Tab 1: Liste der Bankverbindungen
		IDX_BANKVERBINDUGEN = index;
		insertTab(
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.bankverbindung"),
				null,
				null,
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.bankverbindung"), IDX_BANKVERBINDUGEN);
		// Tab 2: Kopfdaten
		IDX_KOPFDATEN = ++index;
		insertTab(LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				null, null,
				LPMain.getInstance().getTextRespectUISPr("lp.kopfdaten"),
				IDX_KOPFDATEN);

		if (hatZusatzfunktionSepa()) {
			// Tab Sepakontoauszug
			IDX_SEPAKONTOAUSZUG = ++index;
			insertTab(LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug"),
					null, null,
					LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug"),
					IDX_SEPAKONTOAUSZUG);
		}
		// Defaults
		setSelectedComponent(getPanelTop1QueryBankverbindung());
		// refresh
		getPanelTop1QueryBankverbindung().eventYouAreSelected(false);
		// damit gleich eine selektiert ist
		ItemChangedEvent it = new ItemChangedEvent(
				getPanelTop1QueryBankverbindung(),
				ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);
		// Listener
		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			// da will jemand die Buchungen ansehen
			if (e.getSource() == getPanelTop1QueryBankverbindung()) {
				Object key = getPanelTop1QueryBankverbindung().getSelectedId();
				holeBankverbindungDto(key);
				// nur wechseln wenns auch einen gibt
				if (key != null) {

					setSelectedComponent(getPanelDetailBankverbindungKopfdaten());
					getPanelDetailBankverbindungKopfdaten()
							.eventYouAreSelected(false);

				}
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == getPanelTop1QueryBankverbindung()) {
				Object key = getPanelTop1QueryBankverbindung().getSelectedId();
				holeBankverbindungDto(key);
				if (key == null) {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_BANKVERBINDUGEN, false);
				} else {
					getInternalFrame().enableAllOberePanelsExceptMe(this,
							IDX_BANKVERBINDUGEN, true);
					enableTabSepaKontoauszug();
				}
				getPanelTop1QueryBankverbindung().updateButtons();
			} else if (e.getSource() == panelQueryBuchungen) {
				panelDetailBuchung.changed(e);
				panelQueryBuchungen.updateButtons();
			} else if (e.getSource() == panelQuerySepakontoauszug) {
				getPanelQuerySepakontoauszug().updateButtons();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_NEW) {
			if (e.getSource() == getPanelTop1QueryBankverbindung()) {
				if (getPanelTop1QueryBankverbindung().getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				getPanelDetailBankverbindungKopfdaten().eventActionNew(null,
						true, false);
				setSelectedComponent(getPanelDetailBankverbindungKopfdaten());
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_PRINT) {
			if (e.getSource() == panelQueryBuchungen) {
				printKontoblatt();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == getPanelDetailBankverbindungKopfdaten()) {
				setBankverbindungDto(null);
				this.setSelectedComponent(getPanelTop1QueryBankverbindung());
				getPanelTop1QueryBankverbindung().eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == getPanelDetailBankverbindungKopfdaten()) {
				getPanelTop1QueryBankverbindung().clearDirektFilter();
				Object key = getPanelDetailBankverbindungKopfdaten()
						.getKeyWhenDetailPanel();
				getPanelTop1QueryBankverbindung().eventYouAreSelected(false);
				getPanelTop1QueryBankverbindung().setSelectedId(key);
				getPanelTop1QueryBankverbindung().eventYouAreSelected(false);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_SPECIAL_BUTTON) {
			String sAspectInfo = ((ISourceEvent) e.getSource()).getAspect();
			if (sAspectInfo.equals(BUTTON_SEPAIMPORT) && hatZusatzfunktionSepa()) {
				actionImportSepaKontoauszug();
			} else if (sAspectInfo.equals(BUTTON_SEPAVERBUCHEN) && hatZusatzfunktionSepa()) {
				actionVerbucheSepaKontoauszug();
			} else if (sAspectInfo.equals(BUTTON_SEPAKONTOAUSZUG_DELETE) && hatZusatzfunktionSepa()) {
				actionStorniereSepakontoauszug();
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_YOU_ARE_SELECTED) {
			if (e.getSource() instanceof AssistentView) {
				getAktuellesPanel().eventYouAreSelected(false);
			}
		} 
	}

	/**
	 * hole BankverbindugnDto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeBankverbindungDto(Object key) throws Throwable {
		if (key != null) {
			BankverbindungDto kassenbuchDto = getFinanzDelegate()
					.bankverbindungFindByPrimaryKey((Integer) key);
			setBankverbindungDto(kassenbuchDto);
//			getInternalFrame().setKeyWasForLockMe(key.toString());
			getPanelDetailBankverbindungKopfdaten().setKeyWhenDetailPanel(key);
		} else {
			setBankverbindungDto(null);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		if (selectedIndex == IDX_BANKVERBINDUGEN) {
			getPanelTop1QueryBankverbindung().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_KOPFDATEN) {
			getPanelDetailBankverbindungKopfdaten().eventYouAreSelected(false);
		} else if (selectedIndex == IDX_BUCHUNGEN) {
			getPanelSplit3();
			panelQueryBuchungen.setDefaultFilter(buildFiltersBuchungen());
			panelQueryBuchungen.eventYouAreSelected(false);
		} else if (selectedIndex == IDX_SEPAKONTOAUSZUG) {
			getPanelQuerySepakontoauszug().setDefaultFilter(
					FinanzFilterFactory.getInstance().createFKSepakontoauszugBankverbindung(getBankverbindungDto().getIId()));
			getPanelQuerySepakontoauszug().eventYouAreSelected(false);
		}
	}

	private FilterKriterium[] buildFiltersBuchungen() throws Throwable {
		FilterKriterium[] filter = null;
		BankverbindungDto bankverbindungDto = getBankverbindungDto();
		if (bankverbindungDto != null) {
			filter = FinanzFilterFactory.getInstance().createFKBuchungDetail(
					getBankverbindungDto().getKontoIId());
		}
		return filter;
	}

	private void initPanelTop3QueryBuchungen() throws Throwable {
		if (panelQueryBuchungen == null) {
			QueryType[] qtBuchungen = FinanzFilterFactory.getInstance()
					.createQTBuchungDetail();
			String[] aWhichButtonIUseBuchungen = { PanelBasis.ACTION_REFRESH,
					PanelBasis.ACTION_FILTER, PanelBasis.ACTION_PRINT };
			FilterKriterium[] filtersBuchungen = buildFiltersBuchungen();

			panelQueryBuchungen = new PanelQuery(qtBuchungen, filtersBuchungen,
					QueryParameters.UC_ID_BUCHUNGDETAIL,
					aWhichButtonIUseBuchungen, getInternalFrame(), LPMain
							.getInstance().getTextRespectUISPr(
									"finanz.buchungen"), true);
		}
		// Filter updaten
		panelQueryBuchungen.setDefaultFilter(buildFiltersBuchungen());
	}

	/**
	 * Drucken des Kontoblattes
	 * 
	 * @throws Throwable
	 */
	protected void printKontoblatt() throws Throwable {
		if (getBankverbindungDto() != null) {
			Integer kontoIId = getBankverbindungDto().getKontoIId();
			BuchungdetailDto[] buchungen = getBuchenDelegate()
					.buchungdetailFindByKontoIId(kontoIId);
			if (buchungen.length == 0) {
				DialogFactory
						.showModalDialog(
								LPMain.getTextRespectUISPr("lp.error"),
								LPMain.getTextRespectUISPr("finanz.error.keinebuchungenaufdiesemkonto"));
			} else {
				String sTitle = LPMain.getInstance().getTextRespectUISPr(
						"finanz.buchungen");
				getInternalFrame().showReportKriterien(
						new ReportBuchungenAufKonto(getInternalFrame(),
								kontoIId, sTitle));
			}
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr("lp.hint.nopages"));
		}
	}

	private BuchenDelegate getBuchenDelegate() throws Throwable {
		return DelegateFactory.getInstance().getBuchenDelegate();
	}

	private PanelFinanzBankverbindungKopfdaten getPanelDetailBankverbindungKopfdaten()
			throws Throwable {
		if (panelDetailBankverbindungKopfdaten == null) {
			panelDetailBankverbindungKopfdaten = new PanelFinanzBankverbindungKopfdaten(
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.kopfdaten"), null, this);
			this.setComponentAt(IDX_KOPFDATEN,
					panelDetailBankverbindungKopfdaten);
		}
		return panelDetailBankverbindungKopfdaten;
	}

	private PanelSplit getPanelSplit3() throws Throwable {
		if (panelSplit3 == null) {
			panelDetailBuchung = new PanelFinanzBuchungDetails(
					getInternalFrame(), "my title!!!", null);
			initPanelTop3QueryBuchungen();
			panelSplit3 = new PanelSplit(getInternalFrame(),
					panelDetailBuchung, panelQueryBuchungen, 280);
			this.setComponentAt(IDX_BUCHUNGEN, panelSplit3);
		}
		return panelSplit3;
	}

	private FinanzDelegate getFinanzDelegate() throws ExceptionLP {
		return DelegateFactory.getInstance().getFinanzDelegate();
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {
	}

	private PanelQuery getPanelTop1QueryBankverbindung() throws Throwable {
		if (panelQueryBankverbindung == null) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_FILTER };
			panelQueryBankverbindung = new PanelQuery(
					FinanzFilterFactory.getInstance().createQTBankverbindung(),
					SystemFilterFactory.getInstance().createFKMandantCNr(),
					QueryParameters.UC_ID_BANKKONTO,
					aWhichButtonIUse,
					getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.auswahl"),
					true,
					FinanzFilterFactory.getInstance().createFKVBankverbindung(),
					null);
			panelQueryBankverbindung
					.befuellePanelFilterkriterienDirekt(FinanzFilterFactory
							.getInstance().createFKDBankverbindungBank(),
							FinanzFilterFactory.getInstance()
									.createFKDBankverbindungKontonummer());
			setComponentAt(IDX_BANKVERBINDUGEN, panelQueryBankverbindung);
		}
		return panelQueryBankverbindung;
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}

	public Object getDto() {
		return bankverbindungDto;
	}
	
	private boolean hatZusatzfunktionSepa() {
		if (bZusatzfunktionSepa == null) {
			bZusatzfunktionSepa = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_SEPA);
		}
		return bZusatzfunktionSepa;
	}
	
	private PanelQuery getPanelQuerySepakontoauszug() throws Throwable {
		if (panelQuerySepakontoauszug == null) {
			String[] aWhichButtonIUse = new String[] { };
			panelQuerySepakontoauszug = new PanelQuery(
					null, 
					new FilterKriterium[] {}, 
					QueryParameters.UC_ID_SEPAKONTOAUSZUG, 
					aWhichButtonIUse,
					getInternalFrame(), 
					LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug"), 
					true);
			
			panelQuerySepakontoauszug.addDirektFilter(FinanzFilterFactory.getInstance().createFKDAuszugnummer());
			panelQuerySepakontoauszug.befuelleFilterkriteriumSchnellansicht(
					FinanzFilterFactory.getInstance().createFKSchnellansichtSepaKontoauszug());
			
			panelQuerySepakontoauszug.createAndSaveAndShowButton(
					PanelBasis.ICON_PATH_STORNIEREN,
					LPMain.getInstance().getTextRespectUISPr("lp.delete"),
					BUTTON_SEPAKONTOAUSZUG_DELETE, RechteFac.RECHT_FB_FINANZ_CUD);
			panelQuerySepakontoauszug.setHmButtonEnabled(BUTTON_SEPAKONTOAUSZUG_DELETE, true);
			panelQuerySepakontoauszug.createAndSaveAndShowButton(
					"/com/lp/client/res/document_into.png",
					LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug.import.button"),
					BUTTON_SEPAIMPORT, RechteFac.RECHT_FB_FINANZ_CUD);
			panelQuerySepakontoauszug.setHmButtonEnabled(BUTTON_SEPAIMPORT, true);
			panelQuerySepakontoauszug.createAndSaveAndShowButton(
					"/com/lp/client/res/sepa16x16.png",
					LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug.verbuchen.button"),
					BUTTON_SEPAVERBUCHEN, RechteFac.RECHT_FB_FINANZ_CUD);
			panelQuerySepakontoauszug.setHmButtonEnabled(BUTTON_SEPAVERBUCHEN, true);
			
			setComponentAt(IDX_SEPAKONTOAUSZUG, panelQuerySepakontoauszug);
		}
		
		return panelQuerySepakontoauszug;
	}

	private void holeSepakontoauszugDto(Integer iId) throws Throwable {
		if (iId != null) {
			setSepakontoauszugDto(getFinanzDelegate().sepakontoauszugFindByPrimaryKeySmall(iId));
		} else {
			setSepakontoauszugDto(null);
		}
	}
	
	private void setSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}

	private SepakontoauszugDto getSepakontoauszugDto() {
		return sepakontoauszugDto;
	}
	
	private void actionStorniereSepakontoauszug() throws Throwable {
		holeSepakontoauszugDto((Integer) panelQuerySepakontoauszug.getSelectedId());
		
		if (SepaImportFac.SepakontoauszugStatus.STORNIERT.equals(getSepakontoauszugDto().getStatusCNr())) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.hint"), 
					LPMain.getTextRespectUISPr("finanz.sepakontoauszug.bereitsstorniert"));
			return;
		}
		
		boolean doStornieren = DialogFactory.showModalJaNeinDialog(getInternalFrame(), 
				LPMain.getMessageTextRespectUISPr("finanz.sepakontoauszug.stornieren", 
						getSepakontoauszugDto().getIAuszug().toString()),
				LPMain.getTextRespectUISPr("lp.frage"));
		
		if (doStornieren) {
			getFinanzDelegate().storniereSepakontoauszug(getSepakontoauszugDto().getIId());
			refreshAktuellesPanel();
		}
	}

	private void actionImportSepaKontoauszug() throws Throwable {
		if (getBankverbindungDto().getCSepaVerzeichnis() == null) {
			DialogFactory.showModalDialog(
					LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("finanz.sepakontoauszug.import.error.sepaverzeichnis"));
			return;
		}
		
		ISepakontoauszugImportController sepaImportController = new SepakontoauszugImportController(getInternalFrame(), getBankverbindungDto());
		DialogSepakontoauszugImport sepaImportDialog = new DialogSepakontoauszugImport(
				LPMain.getInstance().getDesktop(), 
				LPMain.getTextRespectUISPr("finanz.sepakontoauszug.import.button"), 
				true, sepaImportController);
		sepaImportDialog.setVisible(true);
		getPanelQuerySepakontoauszug().eventYouAreSelected(false);
	}

	private void actionVerbucheSepaKontoauszug() throws Throwable {
		SepakontoauszugDto dto = DelegateFactory.getInstance().getFinanzDelegate()
				.getSepakontoauszugNiedrigsteAuszugsnummer(getBankverbindungDto().getIId());
		
		DelegateFactory.getInstance().getFinanzDelegate().pruefeSepakontoauszugAufVerbuchung(dto.getIId());
		
		AssistentView av = new AssistentView(getInternalFrame(),
				LPMain.getTextRespectUISPr("fb.menu.sepaimport"),
				new SepaImportController(getInternalFrame(), getBankverbindungDto(), dto, bVollversion));
		getInternalFrame().showPanelDialog(av);
		getPanelQuerySepakontoauszug().eventYouAreSelected(false);
		
	}

	protected void enableTabSepaKontoauszug() {
		if(IDX_SEPAKONTOAUSZUG < 0) return;
		
		setEnabledAt(IDX_SEPAKONTOAUSZUG, getBankverbindungDto() != null ? 
				getBankverbindungDto().getCSepaVerzeichnis() != null : false);
	}

}
