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
package com.lp.client.system;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.event.ChangeEvent;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.PanelSplit;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.component.WrapperMenuBar;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.system.mail.DialogTestMailConfiguration;
import com.lp.client.system.mail.MailPropertiesController;
import com.lp.client.system.mail.MailPropertiesViewController;
import com.lp.client.system.mail.PanelMailPropertiesAdmin;
import com.lp.client.system.mail.PanelMailPropertiesUser;
import com.lp.client.system.mail.TestMailConfigCtrl;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.fastlanereader.generated.service.FLRParameteranwenderPK;
import com.lp.server.system.fastlanereader.generated.service.FLRParametermandantPK;
import com.lp.server.system.fastlanereader.generated.service.FLRParametermandantgueltigabPK;
import com.lp.server.system.service.MailServiceParameterSource;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

/**
 * <p>
 * Diese Klasse kuemmert sich um Parameter.
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellungsdatum <I>01. 06. 05</I>
 * </p>
 *
 * @author Uli Walch
 * @version $Revision: 1.3 $
 */
public class TabbedPaneParameter extends TabbedPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelSplit panelSplitParametermandant = null;
	private PanelQuery panelQueryParametermandant = null;

	public PanelQuery getPanelQueryParametermandant() {
		return panelQueryParametermandant;
	}

	private PanelParameterMandant panelBottomParametermandant = null;

	private PanelSplit panelSplitParametermandantgueltigab = null;
	private PanelQuery panelQueryParametermandantgueltigab = null;
	private PanelParametermandantGueltigab panelBottomParametermandantgueltigab = null;

	private PanelSplit panelSplitParameteranwender = null;
	private PanelQuery panelQueryParameteranwender = null;
	private PanelParameterAnwender panelBottomParameteranwender = null;
	
	private PanelSplit panelSplitMailPropertiesAdmin = null;
	private PanelQuery panelQueryMailPropertiesAdmin = null;
	private PanelMailPropertiesAdmin panelMailPropertiesAdmin = null;
	private PanelMailPropertiesUser panelMailProperties = null;
	private MailPropertiesController mailPropertiesCtrl;
	private MailPropertiesViewController mailPropertiesViewCtrl;

	// Reihenfolge der Panels
	public static final int IDX_PANEL_PARAMETERMANDANT = 0;
	public static final int IDX_PANEL_PARAMETERMANDANTGUELTIGAB = 1;
	public static final int IDX_PANEL_PARAMETERANWENDER = 2;
	public static final int IDX_PANEL_MAILPROPERTY = 3;
	public static final int IDX_PANEL_MAILPROPERTY_ADMIN = 4;

	public static final String ACTION_SPECIAL_TESTMAIL_SENDEN = PanelBasis.LEAVEALONE + "ACTION_SPECIAL_TESTMAIL_SENDEN";
	private TestMailConfigCtrl testMailConfigCtrl = new TestMailConfigCtrl();
	
	private Set<String> setMandantparameterZeitabhaenigCNr;
	
	public TabbedPaneParameter(InternalFrame internalFrameI) throws Throwable {
		super(internalFrameI, LPMain.getTextRespectUISPr("lp.parameter"));
		jbInit();
		initComponents();
	}

	public void disableParametermandantgueltigab() {

		setEnabledAt(IDX_PANEL_PARAMETERMANDANTGUELTIGAB, false);

		FLRParametermandantPK pk = (FLRParametermandantPK) panelQueryParametermandant.getSelectedId();
		if (pk != null) {
			try {
				if(setMandantparameterZeitabhaenigCNr.contains(pk.getC_nr())) {
					setEnabledAt(IDX_PANEL_PARAMETERMANDANTGUELTIGAB, true);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

	}

	private void jbInit() throws Throwable {
		
		setMandantparameterZeitabhaenigCNr = DelegateFactory.getInstance().getBenutzerServicesDelegate().getMandantparameterZeitabhaenigCNr();
		
		insertTab(LPMain.getTextRespectUISPr("lp.parametermandant"), null, null,
				LPMain.getTextRespectUISPr("lp.parametermandant"), IDX_PANEL_PARAMETERMANDANT);
		insertTab(LPMain.getTextRespectUISPr("lp.parametermandant.zumzeitpunkt"), null, null,
				LPMain.getTextRespectUISPr("lp.parametermandant.zumzeitpunkt"),
				IDX_PANEL_PARAMETERMANDANTGUELTIGAB);

		insertTab(LPMain.getTextRespectUISPr("lp.parameteranwender"), null, null,
				LPMain.getTextRespectUISPr("lp.parameteranwender"), IDX_PANEL_PARAMETERANWENDER);

		if (showMailParameter()) {
			insertTab(LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailproperties"),
					null, null, 
					LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailproperties"),
					IDX_PANEL_MAILPROPERTY);
			insertTab(LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailpropertiesadmin"),
					null, null, 
					LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailpropertiesadmin"),
					IDX_PANEL_MAILPROPERTY_ADMIN);
		}
		
		refreshPanelParametermandant();
		setSelectedComponent(panelSplitParametermandant);
		panelQueryParametermandant.eventYouAreSelected(false);

		// wenn es keine Parametermandant gibt, die fix verdrahteten Werte fuer
		// den aktuellen Mandanten anlegen
		if (panelQueryParametermandant.getSelectedId() == null) {
			DelegateFactory.getInstance().getParameterDelegate().createFixverdrahteteParametermandant();

			panelQueryParametermandant.eventYouAreSelected(false);
		}

		addChangeListener(this);
		getInternalFrame().addItemChangedListener(this);
	}

	protected void lPActionEvent(ActionEvent e) {
		// nothing here.
	}

	/**
	 * changed: hier wird alles durchlaufen und abgefragt zb. save event, discard
	 * event, wenn ein panel refresht werden soll...
	 * 
	 * @param eI ItemChangedEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent eI) throws Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (e.getSource() == panelQueryParametermandant) {
				FLRParametermandantPK pk = (FLRParametermandantPK) panelQueryParametermandant.getSelectedId();

				if (pk != null) {
					getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
				}

				panelBottomParametermandant.setKeyWhenDetailPanel(pk);
				panelBottomParametermandant.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				this.panelQueryParametermandant
						.updateButtons(panelBottomParametermandant.getLockedstateDetailMainKey());

			} else if (e.getSource() == panelQueryParametermandantgueltigab) {
				FLRParametermandantgueltigabPK pk = (FLRParametermandantgueltigabPK) panelQueryParametermandantgueltigab
						.getSelectedId();

				if (pk != null) {
					getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
				}

				panelBottomParametermandantgueltigab.setKeyWhenDetailPanel(pk);
				panelBottomParametermandantgueltigab.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				this.panelQueryParametermandantgueltigab
						.updateButtons(panelBottomParametermandantgueltigab.getLockedstateDetailMainKey());

			} else if (e.getSource() == panelQueryParameteranwender) {
				FLRParameteranwenderPK pk = (FLRParameteranwenderPK) panelQueryParameteranwender.getSelectedId();

				if (pk != null) {
					getInternalFrame().setKeyWasForLockMe(pk.getC_nr() + pk.getC_kategorie());
				}

				panelBottomParameteranwender.setKeyWhenDetailPanel(pk);
				panelBottomParameteranwender.eventYouAreSelected(false);

				// im QP die Buttons in den Zustand nolocking/save setzen.
				this.panelQueryParameteranwender
						.updateButtons(panelBottomParameteranwender.getLockedstateDetailMainKey());

			} else if (e.getSource() == panelQueryMailPropertiesAdmin) {
				String cnr = (String) panelQueryMailPropertiesAdmin.getSelectedId();
//				if (cnr != null) {
//					getInternalFrame().setKeyWasForLockMe(cnr);
//				}
				//ana: damit Panel fuer User und Admin gemeinsam gesperrt werden, wenn eines bearbeitet wird
				getInternalFrame().setKeyWasForLockMe(HelperClient.LOCKME_MAILPROPERTIES);
				panelMailPropertiesAdmin.setKeyWhenDetailPanel(cnr);
				panelMailPropertiesAdmin.eventYouAreSelected(false);
				
				panelQueryMailPropertiesAdmin.updateButtons();
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_UPDATE) {
			// hier kommt man nach upd im D bei einem 1:n hin.
			if (eI.getSource() == this.panelBottomParametermandant) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryParametermandant.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelBottomParametermandantgueltigab) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryParametermandantgueltigab.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
			// hier kommt man nach upd im D bei einem 1:n hin.
			else if (eI.getSource() == this.panelBottomParameteranwender) {
				// im QP die Buttons in den Zustand neu setzen.
				this.panelQueryParameteranwender.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			} else if (eI.getSource() == this.panelMailPropertiesAdmin) {
				panelQueryMailPropertiesAdmin.updateButtons(new LockStateValue(PanelBasis.LOCK_FOR_NEW));
			}
		}

		else if (e.getID() == ItemChangedEvent.ACTION_DISCARD) {
			// wir landen hier bei der Abfolge Button Aendern -> xx -> Button
			// Discard
			if (e.getSource() == panelBottomParametermandant) {
				panelSplitParametermandant.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParameteranwender) {
				panelSplitParameteranwender.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParametermandantgueltigab) {
				panelSplitParametermandantgueltigab.eventYouAreSelected(false);
			} else if (eI.getSource() == this.panelMailPropertiesAdmin) {
				panelSplitMailPropertiesAdmin.eventYouAreSelected(false);
			}
		}

		// selektiert nach save: 0 Wir landen hier nach Button Save
		else if (e.getID() == ItemChangedEvent.ACTION_SAVE) {
			if (e.getSource() == panelBottomParametermandant) {
				Object oKey = panelBottomParametermandant.getKeyWhenDetailPanel();
				panelQueryParametermandant.eventYouAreSelected(false);
				panelQueryParametermandant.setSelectedId(oKey);
				panelSplitParametermandant.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParametermandantgueltigab) {
				Object oKey = panelBottomParametermandantgueltigab.getKeyWhenDetailPanel();
				panelQueryParametermandantgueltigab.eventYouAreSelected(false);
				panelQueryParametermandantgueltigab.setSelectedId(oKey);
				panelSplitParametermandantgueltigab.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParameteranwender) {
				Object oKey = panelBottomParameteranwender.getKeyWhenDetailPanel();
				panelQueryParameteranwender.eventYouAreSelected(false);
				panelQueryParameteranwender.setSelectedId(oKey);
				panelSplitParameteranwender.eventYouAreSelected(false);
			} else if (e.getSource() == panelMailPropertiesAdmin) {
				Object oKey = panelMailPropertiesAdmin.getKeyWhenDetailPanel();
				panelQueryMailPropertiesAdmin.eventYouAreSelected(false);
				panelQueryMailPropertiesAdmin.setSelectedId(oKey);
				panelSplitMailPropertiesAdmin.eventYouAreSelected(false);
			}
		}

		// wir landen hier nach der Abfolge Button Neu -> xx -> Button Discard
		else if (e.getID() == ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP) {
			if (e.getSource() == panelBottomParametermandant) {
				panelSplitParametermandant.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParametermandantgueltigab) {
				panelSplitParametermandantgueltigab.eventYouAreSelected(false);
			} else if (e.getSource() == panelBottomParameteranwender) {
				panelSplitParameteranwender.eventYouAreSelected(false);
			} else if (e.getSource() == panelMailPropertiesAdmin) {
				panelSplitMailPropertiesAdmin.eventYouAreSelected(false);
			}
		}

		else if (eI.getID() == ItemChangedEvent.ACTION_NEW) {
			if (eI.getSource() == panelQueryParametermandant) {
				if (panelQueryParametermandant.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomParametermandant.eventActionNew(eI, true, false);
				panelBottomParametermandant.eventYouAreSelected(false);
				setSelectedComponent(panelSplitParametermandant);
			} else if (eI.getSource() == panelQueryParametermandantgueltigab) {

				panelBottomParametermandantgueltigab.eventActionNew(eI, true, false);
				panelBottomParametermandantgueltigab.eventYouAreSelected(false);
				setSelectedComponent(panelSplitParametermandantgueltigab);
			} else if (eI.getSource() == panelQueryParameteranwender) {
				if (panelQueryParameteranwender.getSelectedId() == null) {
					getInternalFrame().enableAllPanelsExcept(true);
				}
				panelBottomParameteranwender.eventActionNew(eI, true, false);
				panelBottomParameteranwender.eventYouAreSelected(false);
				setSelectedComponent(panelSplitParameteranwender);
			} else if (eI.getSource() == panelQueryMailPropertiesAdmin) {
				panelMailPropertiesAdmin.eventActionNew(eI, true, false);
				panelMailPropertiesAdmin.eventYouAreSelected(false);
			}
		} else if (eI.getID() == ItemChangedEvent.ACTION_LEAVE_ME_ALONE_BUTTON) {
			DialogTestMailConfiguration dialogTest = new DialogTestMailConfiguration(
					LPMain.getInstance().getDesktop(), getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.system.parameter.mail.testmailsenden.dialog"), 
					false, testMailConfigCtrl);
			dialogTest.setVisible(true);
		}
	}

	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		super.lPEventObjectChanged(e);
		int selectedIndex = this.getSelectedIndex();

		switch (selectedIndex) {
		case IDX_PANEL_PARAMETERMANDANT:
			refreshPanelParametermandant();
			panelQueryParametermandant.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryParametermandant.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			if (panelQueryParametermandant.getSelectedId() == null) {
				getInternalFrame().enableAllOberePanelsExceptMe(this, IDX_PANEL_PARAMETERMANDANT, false);
			}
			break;

		case IDX_PANEL_PARAMETERMANDANTGUELTIGAB:
			refreshPanelParametermandantgueltigab();
			panelQueryParametermandantgueltigab.eventYouAreSelected(false);
			panelQueryParametermandantgueltigab.updateButtons();
			break;

		case IDX_PANEL_PARAMETERANWENDER:
			refreshPanelParameteranwender();
			panelQueryParameteranwender.eventYouAreSelected(false);

			// im QP die Buttons setzen.
			panelQueryParameteranwender.updateButtons();

			// wenn es fuer das tabbed pane noch keinen Eintrag gibt,
			// die restlichen oberen Laschen deaktivieren, ausser ...
			panelBottomParameteranwender.validate();
			panelBottomParameteranwender.repaint();
			break;
			
		case IDX_PANEL_MAILPROPERTY:
			refreshPanelMailproperties();
			getInternalFrame().setKeyWasForLockMe(HelperClient.LOCKME_MAILPROPERTIES);
			panelMailProperties.eventYouAreSelected(false);
			panelMailProperties.updateButtons();
			
			break;
			
		case IDX_PANEL_MAILPROPERTY_ADMIN:
			refreshPanelMailpropertiesAdmin();
			panelSplitMailPropertiesAdmin.eventYouAreSelected(false);
			panelQueryMailPropertiesAdmin.updateButtons();
			LPButtonAction item4 = (LPButtonAction) panelQueryMailPropertiesAdmin
					.getHmOfButtons().get(ACTION_SPECIAL_TESTMAIL_SENDEN);
			item4.getButton().setEnabled(true);
			
			break;
		}
	}

	private void refreshPanelMailpropertiesAdmin() throws Throwable {
		if (panelSplitMailPropertiesAdmin == null) {
			String[] whichButtonIUse = { PanelBasis.ACTION_NEW };
			
			panelQueryMailPropertiesAdmin = new PanelQuery(null, 
					SystemFilterFactory.getInstance().createFKMandantCNr(), 
					QueryParameters.UC_ID_MAILPROPERTY, whichButtonIUse, getInternalFrame(), 
					LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailpropertiesadmin"), true);
			panelQueryMailPropertiesAdmin.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDMailProperty(), null);
			panelQueryMailPropertiesAdmin.befuelleFilterkriteriumSchnellansicht(
					new FilterKriterium[] { SystemFilterFactory.getInstance().createFKMailPropertyHasWert() });
			panelQueryMailPropertiesAdmin.getCbSchnellansicht().setText("nur mit Wert");
			panelQueryMailPropertiesAdmin.getCbSchnellansicht().setSelected(false);
			
			panelQueryMailPropertiesAdmin.createAndSaveAndShowButton(
					"/com/lp/client/res/mail.png",
					LPMain.getTextRespectUISPr("lp.system.parameter.mail.testmailsenden"),
					ACTION_SPECIAL_TESTMAIL_SENDEN,
					RechteFac.RECHT_LP_DARF_EMAIL_SENDEN);
			
			panelMailPropertiesAdmin = new PanelMailPropertiesAdmin((InternalFrameSystem) getInternalFrame(), 
					LPMain.getTextRespectUISPr("lp.system.parameter.tab.mailpropertiesadmin"));
			panelSplitMailPropertiesAdmin = new PanelSplit(getInternalFrame(), 
					panelMailPropertiesAdmin, panelQueryMailPropertiesAdmin, 400);
			
			setComponentAt(IDX_PANEL_MAILPROPERTY_ADMIN, panelSplitMailPropertiesAdmin);
		}
	}

	private void refreshPanelMailproperties() throws Throwable {
		if (panelMailProperties == null) {
			panelMailProperties = new PanelMailPropertiesUser((InternalFrameSystem)getInternalFrame(), getViewController(), getController());
			setComponentAt(IDX_PANEL_MAILPROPERTY, panelMailProperties);
		}
	}

	private MailPropertiesController getController() {
		if (mailPropertiesCtrl == null) {
			mailPropertiesCtrl = new MailPropertiesController();
		}
		return mailPropertiesCtrl;
	}

	private MailPropertiesViewController getViewController() {
		if (mailPropertiesViewCtrl == null) {
			mailPropertiesViewCtrl = new MailPropertiesViewController(getController());
		}
		return mailPropertiesViewCtrl;
	}

	private void refreshPanelParametermandant() throws Throwable {

		if (panelSplitParametermandant == null) {
			String[] aWhichStandardButtonIUse = null;

			panelQueryParametermandant = new PanelQuery(null,
					SystemFilterFactory.getInstance().createFKIdCompMandantcnr(),
					QueryParameters.UC_ID_PARAMETERMANDANT, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parametermandant"), true);

			panelQueryParametermandant.befuellePanelFilterkriterienDirekt(
					SystemFilterFactory.getInstance().createFKDIdCompCNr(),
					SystemFilterFactory.getInstance().createFKDIdCompCKategorie());

			panelBottomParametermandant = new PanelParameterMandant(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parametermandant"), null);

			panelSplitParametermandant = new PanelSplit(getInternalFrame(), panelBottomParametermandant,
					panelQueryParametermandant, 260);

			setComponentAt(IDX_PANEL_PARAMETERMANDANT, panelSplitParametermandant);
		}
	}

	private void refreshPanelParametermandantgueltigab() throws Throwable {

		FLRParametermandantPK pk = (FLRParametermandantPK) panelQueryParametermandant.getSelectedId();
		FilterKriterium[] defaultFilter = SystemFilterFactory.getInstance()
				.createFKParametermandantgueltigab(pk.getC_nr());

		if (panelSplitParametermandantgueltigab == null) {
			String[] aWhichStandardButtonIUse = new String[] { PanelBasis.ACTION_NEW };

			panelQueryParametermandantgueltigab = new PanelQuery(null, defaultFilter,
					QueryParameters.UC_ID_PARAMETERMANDANTGUELTIGAB, aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parametermandant.zumzeitpunkt"), true);

			panelBottomParametermandantgueltigab = new PanelParametermandantGueltigab(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parametermandant.zumzeitpunkt"), null);

			panelSplitParametermandantgueltigab = new PanelSplit(getInternalFrame(),
					panelBottomParametermandantgueltigab, panelQueryParametermandantgueltigab, 260);

			setComponentAt(IDX_PANEL_PARAMETERMANDANTGUELTIGAB, panelSplitParametermandantgueltigab);
		}

		panelQueryParametermandantgueltigab.setDefaultFilter(defaultFilter);

	}

	private void refreshPanelParameteranwender() throws Throwable {

		if (panelSplitParameteranwender == null) {
			String[] aWhichStandardButtonIUse = null;

			panelQueryParameteranwender = new PanelQuery(null, null, QueryParameters.UC_ID_PARAMETERANWENDER,
					aWhichStandardButtonIUse, getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parameteranwender"), true);

			panelBottomParameteranwender = new PanelParameterAnwender(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.parameteranwender"), null);

			panelSplitParameteranwender = new PanelSplit(getInternalFrame(), panelBottomParameteranwender,
					panelQueryParameteranwender, 260);

			setComponentAt(IDX_PANEL_PARAMETERANWENDER, panelSplitParameteranwender);
		}
	}

	protected javax.swing.JMenuBar getJMenuBar() throws Throwable {
		return new WrapperMenuBar(this);
	}
	
	private boolean showMailParameter() throws Throwable {
		return MailServiceParameterSource.DB.equals(
				DelegateFactory.getInstance().getParameterDelegate().getMailServiceParameter());
	}
}