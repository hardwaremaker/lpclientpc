package com.lp.client.cockpit;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.geodaten.CockpitMapController;
import com.lp.client.geodaten.MapPartner;
import com.lp.client.media.DropPanelSplit;
import com.lp.client.partner.IPartnerDtoService;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.projekt.IProjektDtoService;
import com.lp.client.projekt.PanelCockpit;
import com.lp.client.projekt.PanelProjektHistory;
import com.lp.client.projekt.ProjektFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.AnsprechpartnerDto;
import com.lp.server.partner.service.AnsprechpartnerFac;
import com.lp.server.partner.service.GeodatenDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HvOptional;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.service.google.geocoding.GeocodeException;
import com.lp.service.google.geocoding.GeodatenFinder;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p>Diese Klasse kuemmert sich um den Partner.</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum xx.12.04</p>
 *
 * @author $Author: christian $
 *
 * @version $Revision: 1.27 $ Date $Date: 2013/01/17 10:44:59 $
 */
public class TabbedPaneCockpit extends TabbedPane implements IPartnerDtoService, IProjektDtoService {

	private static final long serialVersionUID = 1L;

	public static int IDX_PANEL_QP = -1;
	public static int IDX_PANEL_3SP_ANSPRECHPARTNER = -1;
	public static int IDX_PANEL_TELEFONZEITEN = -1;
	public static int IDX_PANEL_KONTAKT = -1;
	public static int IDX_PANEL_PROJEKTE = -1;
	public static int IDX_PANEL_PROJEKT_HISTORY = -1;
	public int IDX_PANEL_COCKPIT = -1;
	public static int IDX_PANEL_GEODATEN = -1;

	private WrapperMenuBar wrapperMenuBar = null;

	private PanelQuery panelPartnerQP = null;

	public boolean gotoVonZeiterfassungTelefonzeitOhneAnsprechpartner = false;

	private PartnerDto partnerDto = new PartnerDto();
	private ProjektDto projektDto = new ProjektDto();
	private PrepareGeodaten geodatenCtrl = new PrepareGeodaten();

	public void setServicePartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public PartnerDto getServicePartnerDto() {
		return partnerDto;
	}

	private AnsprechpartnerDto ansprechpartnerDto = new AnsprechpartnerDto();

	public AnsprechpartnerDto getAnsprechpartnerDto() {
		return ansprechpartnerDto;
	}

	public void setAnsprechpartnerDto(AnsprechpartnerDto ansprechpartnerDto) {
		this.ansprechpartnerDto = ansprechpartnerDto;
	}

	private PanelSplit panelSplitAnsprechpartner = null;
	private PanelQuery panelQueryAnsprechpartner = null;

	public PanelQuery getPanelQueryAnsprechpartner() {
		return panelQueryAnsprechpartner;
	}

	private PanelCockpitAnsprechpartner panelDetailAnsprechpartner = null;
	private PanelBasis panelHeadAnsprechpartner = null;

	private PanelQuery panelQueryTelefonzeiten = null;

	public PanelQuery getPanelQueryTelefonzeiten() {
		return panelQueryTelefonzeiten;
	}

	private PanelCockpit projektCockpit = null;

	private PanelCockpitTelefonzeiten panelBottomTelefonzeiten = null;
	private PanelSplit panelSplitTelefonzeiten = null;

	public PanelSplit getPanelSplitTelefonzeiten() {
		return panelSplitTelefonzeiten;
	}

	private PanelHeadCockpitTelefonzeiten panelHeadTelefonzeiten = null;

	private PanelBasis panelSplitKontakt = null;
	public PanelQuery panelQueryKontakt = null;
	private PanelBasis panelDetailKontakt = null;
	private PanelBasis panelHeadKontakt = null;

	private PanelQuery panelQueryProjekte = null;

	private PanelSplit panelSplitHistory = null;
	private PanelProjektHistory panelHistory = null;
	private PanelQuery panelQueryHistory = null;
	private PanelHeadCockpitTelefonzeiten panelHeadProjekthistory = null;
	private CockpitMapController mapController;

	private String rechtModulweit = null;

	static final public String GOTO_PARTNER = PanelBasis.LEAVEALONE + "GOTO_PARTNER";
	static final public String GOTO_PROJEKT = PanelBasis.ACTION_MY_OWN_NEW + "GOTO_PROJEKT";
	WrapperGotoButton wbuGotoProjekt = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_PROJEKT_AUSWAHL);

	WrapperGotoButton wbuGotoPartner = new WrapperGotoButton(com.lp.util.GotoHelper.GOTO_PARTNER_AUSWAHL);

	public TabbedPaneCockpit(InternalFrame internalFrameI) throws Throwable {

		super(internalFrameI, LPMain.getTextRespectUISPr("cp.cockpit"));
		rechtModulweit = getInternalFrame().getRechtModulweit();
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {

		IDX_PANEL_QP = reiterHinzufuegen(LPMain.getTextRespectUISPr("lp.auswahl"), null);
		IDX_PANEL_3SP_ANSPRECHPARTNER = reiterHinzufuegen(LPMain.getTextRespectUISPr("label.ansprechpartner"), null);
		IDX_PANEL_TELEFONZEITEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("pers.telefonzeiten"), null);

		/*
		 * if (LPMain.getInstance().getDesktop()
		 * .darfAnwenderAufZusatzfunktionZugreifen(MandantFac.
		 * ZUSATZFUNKTION_KONTAKTMANAGMENT)) { IDX_PANEL_KONTAKT =
		 * reiterHinzufuegen(LPMain.getTextRespectUISPr("cp.cockpit.aufgaben"), null); }
		 */

		IDX_PANEL_PROJEKTE = reiterHinzufuegen(LPMain.getTextRespectUISPr("cp.cockpit.projekte"), null);

		IDX_PANEL_PROJEKT_HISTORY = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.projekt.details"), null, null,
				LPMain.getTextRespectUISPr("proj.projekt.details"));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_PROJEKTKLAMMER)) {

			IDX_PANEL_COCKPIT = reiterHinzufuegen(LPMain.getTextRespectUISPr("proj.cockpit"), null, null,
					LPMain.getTextRespectUISPr("proj.cockpit"));
		}

		if (geodatenCtrl.validApiKey()) {
			IDX_PANEL_GEODATEN = reiterHinzufuegen(LPMain.getTextRespectUISPr("geodaten.tab.map"), null);
		}

		// defaults
		// QP1 ist default.
		setSelectedComponent(panelPartnerQP);
		refreshPartnerQP1();

		// wenn es fuer das tabbed pane noch keinen eintrag gibt, die
		// restlichen panel deaktivieren
		if (panelPartnerQP.getSelectedId() == null) {
			getInternalFrame().enableAllPanelsExcept(false);
		}

		// damit D2 einen aktuellen hat.
		ItemChangedEvent it = new ItemChangedEvent(panelPartnerQP, ItemChangedEvent.ITEM_CHANGED);
		lPEventItemChanged(it);

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);

		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				LPMain.getTextRespectUISPr("cp.cockpit") + "|" + getServicePartnerDto().formatFixTitelName1Name2());

	}

	private PanelSplit refreshProjektHistory(Integer projektIId) throws Throwable {

		FilterKriterium[] filters = ProjektFilterFactory.getInstance().createFKProjekt(projektIId);

		if (panelSplitHistory == null) {
			boolean hasEmailFeature = LPMain.getInstance().getDesktop()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_EMAIL_CLIENT);

			panelHeadProjekthistory = new PanelHeadCockpitTelefonzeiten((InternalFrameCockpit) getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), null, this);

			panelHistory = new PanelProjektHistory(getInternalFrame(), this,
					LPMain.getTextRespectUISPr("proj.projekt.history"), null); // eventuell
																				// gibt
																				// es
																				// noch
																				// keine
																				// position

			String[] aWhichButtonIUseHistory = { PanelBasis.ACTION_NEW };
			if (hasEmailFeature) {
				aWhichButtonIUseHistory = new String[] { PanelBasis.ACTION_NEW };
			}

			panelQueryHistory = new PanelQuery(null, filters, QueryParameters.UC_ID_HISTORY, aWhichButtonIUseHistory,
					getInternalFrame(), "", true);

			panelSplitHistory = new DropPanelSplit(getInternalFrame(), panelHeadProjekthistory, 50, panelHistory,
					panelQueryHistory, 200);
			panelSplitHistory.beOneTouchExpandable();
			setComponentAt(IDX_PANEL_PROJEKT_HISTORY, panelSplitHistory);
		}
		panelQueryHistory.setDefaultFilter(filters);
		panelQueryHistory.setMultipleRowSelectionEnabled(true);

		return panelSplitHistory;
	}

	private void refreshPartnerQP1() throws Throwable {
		if (panelPartnerQP == null) {
			String[] aWhichButtonIUse = {};

			FilterKriterium[] kfs = new FilterKriterium[] { new FilterKriterium("i_id", true,
					"(SELECT distinct ansp.partner_i_id_ansprechpartner FROM FLRAnsprechpartner ansp)",
					FilterKriterium.OPERATOR_NOT + " " + FilterKriterium.OPERATOR_IN, false) };

			panelPartnerQP = new PanelQuery(null, kfs, QueryParameters.UC_ID_COCKPIT, aWhichButtonIUse,
					getInternalFrame(), LPMain.getTextRespectUISPr("lp.auswahl"), true,
					PartnerFilterFactory.getInstance().createFKVPartner(), null);

			panelPartnerQP.befuellePanelFilterkriterienDirekt(PartnerFilterFactory.getInstance().createFKDPartnerName(),
					PartnerFilterFactory.getInstance()
							// .createFKDPartnerLandPLZOrt());
							.createFKDLandPLZOrt());
			panelPartnerQP.createAndSaveAndShowButton("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("cp.cockpit.gehezupartner"), GOTO_PARTNER,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), RechteFac.RECHT_PART_PARTNER_R);

			panelPartnerQP.enableToolsPanelButtons(true, GOTO_PARTNER);

			setComponentAt(IDX_PANEL_QP, panelPartnerQP);
		}
	}

	private void refreshAnsprechpartner(Integer iIdPartnerI) throws Throwable {

		if (panelSplitAnsprechpartner == null) {

			String[] aWhichButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKAnsprechpartner(iIdPartnerI);
			panelQueryAnsprechpartner = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_ANSPRECHPARTNER,
					aWhichButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("label.ansprechpartner"), true,
					PartnerFilterFactory.getInstance().createFKVAnsprechpartner(), null);

			panelDetailAnsprechpartner = new PanelCockpitAnsprechpartner(getInternalFrame(),
					LPMain.getTextRespectUISPr("label.ansprechpartner"), null);

			panelHeadAnsprechpartner = new PanelHeadCockpitAnsprechpartner(getInternalFrame(),
					LPMain.getTextRespectUISPr("label.ansprechpartner"), null, this);

			panelSplitAnsprechpartner = new PanelSplit(getInternalFrame(), panelHeadAnsprechpartner, 180,
					panelDetailAnsprechpartner, panelQueryAnsprechpartner, 100);
			panelQueryAnsprechpartner.befuellePanelFilterkriterienDirekt(
					PartnerFilterFactory.getInstance().createFKDAnsprechpartnerPartnerName(), null);

			/*
			 * panelSplitAnsprechpartner.getPanelSplit().addPropertyChangeListener(
			 * JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
			 * 
			 * @Override public void propertyChange(PropertyChangeEvent pce) {
			 * 
			 * panelDetailAnsprechpartner.fitBildToComponent();
			 * 
			 * } });
			 */

			setComponentAt(IDX_PANEL_3SP_ANSPRECHPARTNER, panelSplitAnsprechpartner);
		} else {
			// filter refreshen.
			panelQueryAnsprechpartner
					.setDefaultFilter(PartnerFilterFactory.getInstance().createFKAnsprechpartner(iIdPartnerI));
		}
	}

	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		if (eI.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (eI.getSource() == panelPartnerQP) {

				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				// Partner lesen
				if (key != null) {

					setServicePartnerDto(
							DelegateFactory.getInstance().getPartnerDelegate().partnerFindByPrimaryKey((Integer) key));

					getInternalFrame().setKeyWasForLockMe(key + "");

					getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
							getServicePartnerDto().formatFixTitelName1Name2());

				}

			} else if (eI.getSource() == panelQueryTelefonzeiten) {
				Integer iId = (Integer) panelQueryTelefonzeiten.getSelectedId();
				panelBottomTelefonzeiten.setKeyWhenDetailPanel(iId);
				panelBottomTelefonzeiten.eventYouAreSelected(false);
				panelQueryTelefonzeiten.updateButtons();
			} else if (eI.getSource() == panelQueryAnsprechpartner) {

				Integer iIdAnsprechpartner = (Integer) panelQueryAnsprechpartner.getSelectedId();
				Integer iIdPartner = getServicePartnerDto().getIId();
				refreshAnsprechpartner(iIdPartner);
				panelDetailAnsprechpartner.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelDetailAnsprechpartner.eventYouAreSelected(false);
				panelQueryAnsprechpartner.updateButtons(panelDetailAnsprechpartner.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryKontakt) {

				Integer iIdAnsprechpartner = (Integer) panelQueryKontakt.getSelectedId();
				Integer iIdPartner = getServicePartnerDto().getIId();
				refreshAnsprechpartner(iIdPartner);
				panelDetailKontakt.setKeyWhenDetailPanel(iIdAnsprechpartner);
				panelDetailKontakt.eventYouAreSelected(false);
				panelQueryKontakt.updateButtons(panelDetailKontakt.getLockedstateDetailMainKey());
			} else if (eI.getSource() == panelQueryProjekte) {

				Integer projektIId = (Integer) panelQueryProjekte.getSelectedId();

				if (projektIId != null) {

					setProjektDto(
							DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId));
				} else {
					setProjektDto(null);
				}
			} else if (eI.getSource() == panelQueryHistory) {
				Object key = ((ISourceEvent) eI.getSource()).getIdSelected();
				// key 1; IF
				getInternalFrame().setKeyWasForLockMe(key + "");
				panelHistory.setKeyWhenDetailPanel(key);
				panelHistory.eventYouAreSelected(false);
				panelQueryHistory.updateButtons();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			if (eI.getSource() == panelBottomTelefonzeiten) {
				panelQueryTelefonzeiten.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == panelDetailAnsprechpartner) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryAnsprechpartner.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			} else if (eI.getSource() == panelDetailKontakt) {
				// im QP die Buttons in den Zustand neu setzen.
				panelQueryKontakt.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
				;
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryTelefonzeiten) {
				panelBottomTelefonzeiten.eventActionNew(eI, true, false);
				panelBottomTelefonzeiten.eventYouAreSelected(false);
				this.setSelectedComponent(panelSplitTelefonzeiten);
			} else if (eI.getSource() == panelQueryAnsprechpartner) {
				panelDetailAnsprechpartner.eventActionNew(eI, true, false);
				panelDetailAnsprechpartner.eventYouAreSelected(false);
				setSelectedComponent(panelSplitAnsprechpartner);
			} else if (eI.getSource() == panelQueryKontakt) {
				panelDetailKontakt.eventActionNew(eI, true, false);
				panelDetailKontakt.eventYouAreSelected(false);
				setSelectedComponent(panelSplitKontakt);
			} else if (eI.getSource() == panelQueryHistory) {
				panelHistory.bePlain();
				panelHistory.eventActionNew(eI, true, false);
				panelHistory.eventYouAreSelected(false);
				setSelectedComponent(panelSplitHistory);
			}
		}

		else if (eI.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (eI.getSource() == panelPartnerQP) {
				// jetzt ab zu D2.
				setSelectedComponent(panelSplitAnsprechpartner);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (eI.getSource() == panelBottomTelefonzeiten) {
				setKeyWasForLockMe();
				if (panelBottomTelefonzeiten.getKeyWhenDetailPanel() == null) {
					Object oNaechster = panelQueryTelefonzeiten.getId2SelectAfterDelete();
					panelQueryTelefonzeiten.setSelectedId(oNaechster);
				}
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailAnsprechpartner) {
				Integer iIdPartner = getServicePartnerDto().getIId();

				refreshAnsprechpartner(iIdPartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelSplitAnsprechpartner.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				Integer iIdPartner = getServicePartnerDto().getIId();

				refreshAnsprechpartner(iIdPartner);
				panelSplitAnsprechpartner.eventYouAreSelected(false);
				panelQueryAnsprechpartner.updateButtons();

				Integer iIdansprechpartner = (Integer) panelQueryAnsprechpartner.getSelectedId();

				refreshKontakt(iIdPartner, iIdansprechpartner);
				getInternalFrame().setKeyWasForLockMe(iIdPartner + "");
				panelSplitKontakt.eventYouAreSelected(false);
			} else if (eI.getSource() == panelHistory) {
				panelQueryHistory.updateButtons();
				panelHistory.eventYouAreSelected(false);
				panelQueryHistory.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_DISCARD) {
			if (eI.getSource() == panelBottomTelefonzeiten) {
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailAnsprechpartner) {
				panelSplitAnsprechpartner.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailKontakt) {
				panelSplitKontakt.eventYouAreSelected(false);
			} else if (eI.getSource() == panelHistory) {
				panelSplitHistory.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (eI.getSource() == panelBottomTelefonzeiten) {
				Object oKey = panelBottomTelefonzeiten.getKeyWhenDetailPanel();
				panelQueryTelefonzeiten.eventYouAreSelected(false);
				panelQueryTelefonzeiten.setSelectedId(oKey);
				panelSplitTelefonzeiten.eventYouAreSelected(false);
			} else if (eI.getSource() == panelDetailAnsprechpartner) {

				Object oKey = panelDetailAnsprechpartner.getKeyWhenDetailPanel();
				panelQueryAnsprechpartner.eventYouAreSelected(false);
				panelQueryAnsprechpartner.setSelectedId(oKey);
				panelSplitAnsprechpartner.eventYouAreSelected(false);

			} else if (eI.getSource() == panelDetailKontakt) {
				Object oKey = panelDetailKontakt.getKeyWhenDetailPanel();
				panelQueryKontakt.eventYouAreSelected(false);
				panelQueryKontakt.setSelectedId(oKey);
				panelSplitKontakt.eventYouAreSelected(false);
			} else if (eI.getSource() == panelHistory) {
				Object oKey = panelHistory.getKeyWhenDetailPanel();
				panelQueryHistory.eventYouAreSelected(false);
				panelQueryHistory.setSelectedId(oKey);
				panelSplitHistory.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNMINUS1) {

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VONNNACHNPLUS1) {

		} else if (eI.getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {

		} else if (eI.getID() == ItemChangedEvent.ACTION_MY_OWN_NEW) {

			String sAspectInfo = ((ISourceEvent) eI.getSource()).getAspect();
			if (sAspectInfo.equals(GOTO_PROJEKT)) {
				if (eI.getSource() == panelQueryProjekte) {

					Object key = panelQueryProjekte.getSelectedId();

					if (key != null) {
						wbuGotoProjekt.setOKey((Integer) key);
						wbuGotoProjekt
								.actionPerformed(new ActionEvent(wbuGotoProjekt, 0, WrapperGotoButton.ACTION_GOTO));
					}

				}
			}

		} else if (eI.getID() == ItemChangedEvent.ACTION_LEAVE_ME_ALONE_BUTTON) {
			if (eI.getSource().equals(panelPartnerQP)) {

				Integer partnerIId = (Integer) panelPartnerQP.getSelectedId();

				wbuGotoPartner.setOKey(partnerIId);

				wbuGotoPartner.actionPerformed(new ActionEvent(wbuGotoPartner, 0, WrapperGotoButton.ACTION_GOTO));

			}

		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();
		getInternalFrame().setRechtModulweit(rechtModulweit);

		if (selectedIndex == IDX_PANEL_QP) {
			refreshPartnerQP1();
			panelPartnerQP.eventYouAreSelected(false);

			// btnstate: 3 QP alleine; im QP die Buttons setzen.
			panelPartnerQP.updateButtons();
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN, "");
		} else if (selectedIndex == IDX_PANEL_3SP_ANSPRECHPARTNER) {
			// gehe zu SP5
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("label.ansprechpartner"));
			Integer iIdPartner = (Integer) panelPartnerQP.getSelectedId();

			if (iIdPartner == null) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("part.kundensicht.error.keinpartner"));
				setSelectedComponent(panelPartnerQP);
				return;
			}

			refreshAnsprechpartner(iIdPartner);
			panelSplitAnsprechpartner.eventYouAreSelected(false);
			panelQueryAnsprechpartner.updateButtons(panelDetailAnsprechpartner.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_TELEFONZEITEN) {
			Integer iIdPartner = (Integer) panelPartnerQP.getSelectedId();

			
			if (iIdPartner == null) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("part.kundensicht.error.keinpartner"));
				setSelectedComponent(panelPartnerQP);
				return;
			}
			
			if (gotoVonZeiterfassungTelefonzeitOhneAnsprechpartner) {
				refreshAnsprechpartner(-1);
				gotoVonZeiterfassungTelefonzeitOhneAnsprechpartner = false;
			} else {
				refreshAnsprechpartner(iIdPartner);
			}

			panelSplitAnsprechpartner.eventYouAreSelected(false);
			panelQueryAnsprechpartner.updateButtons();

			Integer iIdansprechpartner = (Integer) panelQueryAnsprechpartner.getSelectedId();

			refreshTelefonzeiten(iIdPartner, iIdansprechpartner);
			panelSplitTelefonzeiten.eventYouAreSelected(false);
			panelQueryTelefonzeiten.updateButtons();
		} else if (selectedIndex == IDX_PANEL_KONTAKT) {
			// gehe zu SP5
			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
					LPMain.getTextRespectUISPr("lp.kontakt"));
			Integer iIdPartner = getServicePartnerDto().getIId();

			if (iIdPartner == null) {

				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
						LPMain.getTextRespectUISPr("part.kundensicht.error.keinpartner"));
				setSelectedComponent(panelPartnerQP);
				return;
			}
			
			refreshAnsprechpartner(iIdPartner);
			panelSplitAnsprechpartner.eventYouAreSelected(false);
			panelQueryAnsprechpartner.updateButtons();

			Integer iIdansprechpartner = (Integer) panelQueryAnsprechpartner.getSelectedId();

			refreshKontakt(iIdPartner, iIdansprechpartner);
			panelSplitKontakt.eventYouAreSelected(false);
			panelQueryKontakt.updateButtons(panelDetailKontakt.getLockedstateDetailMainKey());
		} else if (selectedIndex == IDX_PANEL_PROJEKTE) {
			refreshPanelQueryProjekte(getServicePartnerDto().getIId()).eventYouAreSelected(false);
			panelQueryProjekte.updateButtons();
		} else if (selectedIndex == IDX_PANEL_PROJEKT_HISTORY) {
			Integer iIdPartner = getServicePartnerDto().getIId();
			refreshAnsprechpartner(iIdPartner);
			panelSplitAnsprechpartner.eventYouAreSelected(false);
			panelQueryAnsprechpartner.updateButtons();

			Integer iIdansprechpartner = (Integer) panelQueryAnsprechpartner.getSelectedId();

			refreshPanelQueryProjekte(getServicePartnerDto().getIId());

			Integer projektIId = (Integer) panelQueryProjekte.getSelectedId();

			if (projektIId != null) {

				setProjektDto(DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId));
				refreshProjektHistory(projektIId);
				panelSplitHistory.eventYouAreSelected(false);
				panelQueryHistory.updateButtons();
				panelHistory.eventYouAreSelected(false);
			} else {
				setProjektDto(null);
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("cp.cockpit.projekte.history.keinprojekt"));
				setSelectedComponent(panelQueryProjekte);

			}

		} else if (selectedIndex == IDX_PANEL_COCKPIT) {

			refreshPanelQueryProjekte(getServicePartnerDto().getIId());

			Integer projektIId = (Integer) panelQueryProjekte.getSelectedId();

			if (projektIId != null) {

				setProjektDto(DelegateFactory.getInstance().getProjektDelegate().projektFindByPrimaryKey(projektIId));

				projektCockpit = new PanelCockpit(getInternalFrame(), this);
				setComponentAt(IDX_PANEL_COCKPIT, projektCockpit);

			} else {
				setProjektDto(null);
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
						LPMain.getTextRespectUISPr("cp.cockpit.projekte.history.keinprojekt"));
				setSelectedComponent(panelQueryProjekte);

			}

		} else if (selectedIndex == IDX_PANEL_GEODATEN) {
			geodatenTabSelected();
		}

	}

	private void geodatenTabSelected() throws Throwable {
		geodatenCtrl.prepare(getServicePartnerDto());
		refreshPanelGeodaten(getServicePartnerDto());
	}

	public CockpitMapController getMapController() throws Throwable {
		if (mapController == null) {
			mapController = new CockpitMapController(getInternalFrame());
		}
		return mapController;
	}

	private void refreshPanelGeodaten(PartnerDto servicePartnerDto) throws Throwable {
		if (mapController == null) {
			setComponentAt(IDX_PANEL_GEODATEN, getMapController().getPanelMaps());
		}
		getMapController().loadMap(new MapPartner(servicePartnerDto));
	}

	private PanelQuery refreshPanelQueryProjekte(Integer partnerIId) throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("projekt.flrpartner.i_id", true, partnerIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;

		FilterKriterium[] fkSchnellansicht = new FilterKriterium[] {
				new FilterKriterium("projekt." + ProjektFac.FLR_PROJEKT_FLRPROJEKTSTATUS + ".b_erledigt", true, "0",
						FilterKriterium.OPERATOR_EQUAL, false) };
		if (panelQueryProjekte == null) {
			String[] aWhichButtonIUseAuftrag = {};
			panelQueryProjekte = new PanelQuery(null, kriterien, QueryParameters.UC_ID_PROJEKT, aWhichButtonIUseAuftrag,
					getInternalFrame(), LPMain.getTextRespectUISPr("cp.cockpit.projekte"), true);

			panelQueryProjekte.getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
					LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_PROJEKT,
					KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);
			panelQueryProjekte.enableToolsPanelButtons(true, GOTO_PROJEKT);

			Map m = DelegateFactory.getInstance().getProjektServiceDelegate().getAllBereich();

			panelQueryProjekte
					.setFilterComboBox(m,
							new FilterKriterium("projekt." + ProjektFac.FLR_PROJEKT_BEREICH_I_ID, true, "" + "",
									FilterKriterium.OPERATOR_EQUAL, false),
							false, null, false, m.keySet().iterator().next());

			FilterKriteriumDirekt fkDirekt2 = ProjektFilterFactory.getInstance().createFKDTitelVolltextsuche();

			panelQueryProjekte.befuellePanelFilterkriterienDirekt(
					ProjektFilterFactory.getInstance().createFKDProjektnummer(), fkDirekt2);
			panelQueryProjekte.addDirektFilter(ProjektFilterFactory.getInstance().createFKDTextSuchen());

			panelQueryProjekte.befuelleFilterkriteriumSchnellansicht(fkSchnellansicht);

			setComponentAt(IDX_PANEL_PROJEKTE, panelQueryProjekte);

		} else {
			panelQueryProjekte.setDefaultFilter(kriterien);

		}
		return panelQueryProjekte;
	}

	private void refreshTelefonzeiten(Integer partnerIId, Integer ansprechpartnerIId) throws Throwable {

		FilterKriterium fkVersteckt = new FilterKriterium("flransprechpartner.i_id", true, ansprechpartnerIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (panelQueryTelefonzeiten == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };
			panelQueryTelefonzeiten = new PanelQuery(null, createFKTelefonzeiten(partnerIId),
					QueryParameters.UC_ID_COCKPITTELEFONZEITEN, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), true, fkVersteckt,
					LPMain.getTextRespectUISPr("cp.cockpit.alleansprechpartner"));

			FilterKriteriumDirekt fkDirekt1 = new FilterKriteriumDirekt(
					ZeiterfassungFac.FLR_TELEFONZEITEN_FLRANSPRECHPARTNER + "."
							+ AnsprechpartnerFac.FLR_ANSPRECHPARTNER_PARTNERANSPRECHPARTNER + "."
							+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1,
					"", FilterKriterium.OPERATOR_LIKE, LPMain.getTextRespectUISPr("label.ansprechpartner"),
					FilterKriteriumDirekt.PROZENT_TRAILING, true, // wrapWithSingleQuotes
					true, Facade.MAX_UNBESCHRAENKT);

			panelQueryTelefonzeiten.befuellePanelFilterkriterienDirekt(fkDirekt1,
					ZeiterfassungFilterFactory.getInstance().createFKDTelefonzeitenKommentar());

			panelBottomTelefonzeiten = new PanelCockpitTelefonzeiten(getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), null);

			panelHeadTelefonzeiten = new PanelHeadCockpitTelefonzeiten((InternalFrameCockpit) getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), null, this);

			panelSplitTelefonzeiten = new PanelSplit(getInternalFrame(), panelHeadTelefonzeiten, 50,
					panelBottomTelefonzeiten, panelQueryTelefonzeiten, 180);

			setComponentAt(IDX_PANEL_TELEFONZEITEN, panelSplitTelefonzeiten);
		} else {
			// filter refreshen.
			panelQueryTelefonzeiten.setDefaultFilter(createFKTelefonzeiten(partnerIId));
			panelQueryTelefonzeiten.setFkVersteckteFelderNichtAnzeigen(fkVersteckt);
		}
	}

	private void refreshKontakt(Integer iIdPartnerI, Integer ansprechpartnerIId) throws Throwable {

		FilterKriterium fkVersteckt = new FilterKriterium("flransprechpartner.i_id", true, ansprechpartnerIId + "",
				FilterKriterium.OPERATOR_EQUAL, false);

		if (panelSplitKontakt == null) {
			String[] aWhichStandardButtonIUse = { PanelBasis.ACTION_NEW };

			QueryType[] querytypes = null;
			FilterKriterium[] filters = PartnerFilterFactory.getInstance().createFKKontakt(iIdPartnerI);
			;

			panelQueryKontakt = new PanelQuery(querytypes, filters, QueryParameters.UC_ID_KONTAKT,
					aWhichStandardButtonIUse, getInternalFrame(), LPMain.getTextRespectUISPr("cp.cockpit.aufgaben"),
					true, fkVersteckt, LPMain.getTextRespectUISPr("cp.cockpit.alleansprechpartner"));

			panelDetailKontakt = new PanelCockpitKontakt(getInternalFrame(),
					LPMain.getTextRespectUISPr("cp.cockpit.aufgaben"), null);

			panelQueryKontakt.befuellePanelFilterkriterienDirekt(
					new FilterKriteriumDirekt("c_titel", "", FilterKriterium.OPERATOR_LIKE,
							LPMain.getTextRespectUISPr("lp.titel"), FilterKriteriumDirekt.PROZENT_BOTH, // Auswertung
																										// als '%XX'
							true, // wrapWithSingleQuotes
							true, Facade.MAX_UNBESCHRAENKT),
					null);

			panelHeadKontakt = new PanelHeadCockpitTelefonzeiten((InternalFrameCockpit) getInternalFrame(),
					LPMain.getTextRespectUISPr("pers.telefonzeiten"), null, this);

			panelSplitKontakt = new PanelSplit(getInternalFrame(), panelHeadKontakt, 50, panelDetailKontakt,
					panelQueryKontakt, 150);

			setComponentAt(IDX_PANEL_KONTAKT, panelSplitKontakt);
		} else {
			// filter refreshen.
			panelQueryKontakt.setDefaultFilter(PartnerFilterFactory.getInstance().createFKKontakt(iIdPartnerI));
			panelQueryKontakt.setFkVersteckteFelderNichtAnzeigen(fkVersteckt);
		}
	}

	public FilterKriterium[] createFKTelefonzeiten(Integer partnerIId) {
		FilterKriterium[] kriterien = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium(ZeiterfassungFac.FLR_TELEFONZEITEN_PARTNER_I_ID, true,
				partnerIId + "", FilterKriterium.OPERATOR_EQUAL, false);
		kriterien[0] = krit1;
		return kriterien;
	}

	protected void lPActionEvent(java.awt.event.ActionEvent e) throws Throwable {

	}

	/**
	 * Diese Methode setzt des aktuellen Partner aus der Auswahlliste als den zu
	 * lockenden Partner.
	 */
	public void setKeyWasForLockMe() {
		Object oKey = panelPartnerQP.getSelectedId();

		if (oKey != null) {
			getInternalFrame().setKeyWasForLockMe(oKey.toString());
		} else {
			getInternalFrame().setKeyWasForLockMe(null);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		if (wrapperMenuBar == null) {
			wrapperMenuBar = new WrapperMenuBar(this);
			JMenu menuDatei = (JMenu) wrapperMenuBar.getComponent(WrapperMenuBar.MENU_MODUL);
			menuDatei.add(new JSeparator(), 0);

		}
		return wrapperMenuBar;
	}

	public PanelQuery getPanelPartnerQP1() {
		this.setSelectedIndex(IDX_PANEL_QP);
		this.getInternalFrame().enableAllPanelsExcept(false);
		return this.panelPartnerQP;
	}

	@Override
	public void setProjektDto(ProjektDto projektDto) {
		this.projektDto = projektDto;

	}

	@Override
	public ProjektDto getProjektDto() {

		return projektDto;
	}

	public class PrepareGeodaten {

		public GeodatenDto prepare(PartnerDto partnerDto) throws Throwable {
			if (!validApiKey()) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER_GOOGLE_APIKEY_NICHT_DEFINIERT,
						new Exception("Mandantenparameter GOOGLE_API_KEY ist nicht definiert."));
			}

			if (HvOptional.ofNullable(partnerDto.getGeodatenDto()).isPresent()) {
				return partnerDto.getGeodatenDto();
			}

			GeodatenFinder finder = new GeodatenFinder();
			try {
				GeodatenDto geodatenDto = finder.findExc(partnerDto.formatAdresse());
				if (geodatenDto == null)
					return null;

				geodatenDto.setPartnerIId(partnerDto.getIId());
				partnerDto.setGeodatenDto(geodatenDto);
				updateGeodaten(geodatenDto);

				return geodatenDto;
			} catch (GeocodeException exc) {
				throw new ExceptionLP(EJBExceptionLP.FEHLER_GOOGLE_GEODATEN_ABFRAGE_NICHT_ERFOLGREICH, exc.getMessage(),
						exc, exc.getAddress());
			}
		}

		public boolean validApiKey() throws Throwable {
			String apikey = DelegateFactory.getInstance().getParameterDelegate().getGoogleApiKey();
			return !Helper.isStringEmpty(apikey);
		}

		protected void updateGeodaten(GeodatenDto geodatenDto) {
			try {
				DelegateFactory.getInstance().getPartnerDelegate().createGeodaten(geodatenDto);
			} catch (ExceptionLP e) {
				myLogger.error("Geodaten konnten nicht upgedated werden", e);
			}
		}
	}

}
