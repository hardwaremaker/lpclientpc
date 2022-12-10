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
package com.lp.client.benutzer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.lp.client.artikel.ReportLieferantenpreis;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperPasswordField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelBenutzer extends PanelBasis {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameBenutzer internalFramePersonal = null;
	private WrapperTextField wtfKennung = new WrapperTextField();
	private BenutzerDto benutzerDto = null;
	private WrapperLabel wlaKennwort = new WrapperLabel();
	private WrapperPasswordField wpfKennwort = new WrapperPasswordField();
	private WrapperLabel wlaGueltigbis = new WrapperLabel();
	// private WrapperDateFieldMitJDialog wdfGueltigbis = new
	// WrapperDateFieldMitJDialog();
	private WrapperDateField wdfGueltigbis = new WrapperDateField();
	private WrapperLabel wlaBenutzerkannung = new WrapperLabel();
	private WrapperButton wbuDefMandant = new WrapperButton();
	private WrapperTextField wtfDefaultMandant = new WrapperTextField();
	private String sDefaultMandantCnr = new String();
	private WrapperCheckBox wcbGesperrt = new WrapperCheckBox();
	private WrapperCheckBox wcbAendern = new WrapperCheckBox();

	private PanelQueryFLR panelQueryFLRMandant = null;

	static final public String ACTION_SPECIAL_MANDANT_FROM_LISTE = "action_mandant_from_liste";
	private WrapperLabel wlaKennwortBestaetigen = new WrapperLabel();
	private WrapperPasswordField wpfKennwortBestaetigen = new WrapperPasswordField();

	public PanelBenutzer(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		benutzerDto = new BenutzerDto();
		// getInternalFrame().setKeyWasForLockMe(LPMain.getLockMeForNew());

		leereAlleFelder(this);
	}

	public char[] getPassword(String title) {
		JPanel panel = new JPanel();
		final JPasswordField passwordField = new JPasswordField(20);
		panel.add(new JLabel(LPMain
				.getTextRespectUISPr("pers.benutzer.report.passwort")));
		panel.add(passwordField);
		JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION) {
			@Override
			public void selectInitialValue() {
				passwordField.requestFocusInWindow();
			}
		};
		pane.createDialog(null, title).setVisible(true);
		return passwordField.getPassword();
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {

		char[] password = getPassword(LPMain
				.getTextRespectUISPr("pers.benutzer.report.passwort.title"));
		if (password != null && password.length > 0) // pressing OK button
		{

			String add2Title = LPMain
					.getTextRespectUISPr("pers.benutzer.report.login");
			getInternalFrame().showReportKriterien(
					new ReportLogin(internalFramePersonal,
							wtfKennung.getText(), new String(password),
							add2Title));

		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MANDANT_FROM_LISTE)) {
			dialogQueryMandantFromListe(e);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getBenutzerDelegate()
				.removeBenutzer(benutzerDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		benutzerDto.setBAendern(wcbAendern.getShort());
		benutzerDto.setBGesperrt(wcbGesperrt.getShort());

		if (wtfKennung.getText() != null) {
			wtfKennung.setText(wtfKennung.getText().trim());
		}

		benutzerDto.setCBenutzerkennung(wtfKennung.getText());

		if (!new String(wpfKennwort.getPassword()).equals("**********")) {
			benutzerDto
					.setCKennwort(Helper.getMD5Hash((wtfKennung.getText() + new String(
							wpfKennwort.getPassword())).toCharArray()));
		}

		benutzerDto.setTGueltigbis(wdfGueltigbis.getTimestamp());
		benutzerDto.setMandantCNrDefault(sDefaultMandantCnr);
		// benutzerDto.setMandantCNrDefault(wtfDefaultMandant.getText());
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wcbAendern.setShort(benutzerDto.getBAendern());
		wcbGesperrt.setShort(benutzerDto.getBGesperrt());
		wtfKennung.setText(benutzerDto.getCBenutzerkennung());
		wpfKennwort.setText("**********");
		wpfKennwortBestaetigen.setText("**********");
		sDefaultMandantCnr = benutzerDto.getMandantCNrDefault();
		wtfDefaultMandant.setText(benutzerDto.getMandantCNrDefault()
				+ " - "
				+ DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								benutzerDto.getMandantCNrDefault())
						.getPartnerDto().formatAnrede());
		wdfGueltigbis.setTimestamp(benutzerDto.getTGueltigbis());

		this.setStatusbarPersonalIIdAendern(benutzerDto.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(benutzerDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(benutzerDto.getTAnlegen());
		this.setStatusbarTAendern(benutzerDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			if (!new String(wpfKennwort.getPassword()).equals(new String(
					wpfKennwortBestaetigen.getPassword()))) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"pers.error.kennwoerterstimmennichtueberein"));
			} else {

				if (wtfKennung.getText().length() > 3
						&& wpfKennwort.getPassword().length > 3) {

					components2Dto();

					if (benutzerDto.getIId() == null) {
						benutzerDto.setIId(DelegateFactory.getInstance()
								.getBenutzerDelegate()
								.createBenutzer(benutzerDto));
						setKeyWhenDetailPanel(benutzerDto.getIId());
					} else {
						DelegateFactory.getInstance().getBenutzerDelegate()
								.updateBenutzer(benutzerDto);
					}
					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(
								benutzerDto.getIId() + "");
					}
					eventYouAreSelected(false);
					DialogFactory
							.showModalDialog(
									LPMain.getInstance().getTextRespectUISPr(
											"lp.warning"),
									LPMain.getInstance()
											.getTextRespectUISPr(
													"pers.warning.aenderungenwerdenerstnacheinerneuanmeldungwirksam"));
				} else {
					DialogFactory
							.showModalDialog(LPMain.getInstance()
									.getTextRespectUISPr("lp.warning"),
									"Benutzer bzw. Kennwort muss mindestens 4-stellig sein.");

				}
			}
		}
	}

	void dialogQueryMandantFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRMandant = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.mandantauswahlliste"));

		panelQueryFLRMandant.setSelectedId(benutzerDto.getMandantCNrDefault());

		new DialogQuery(panelQueryFLRMandant);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMandant) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				wtfDefaultMandant.setText(key + "");
				sDefaultMandantCnr = key.toString();
				benutzerDto.setMandantCNrDefault(key + "");
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRMandant) {
				wtfDefaultMandant.setText(null);
				sDefaultMandantCnr = "";
				benutzerDto.setMandantCNrDefault(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wtfKennung.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNUNG);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);

		getInternalFrame().addItemChangedListener(this);

		wlaKennwort.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.kennwort"));
		wpfKennwort.setMandatoryField(true);
		wpfKennwort.setText("");
		wpfKennwort.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNWORT);
		wpfKennwort
				.addFocusListener(new PanelBenutzer_wpfKennwort_focusAdapter(
						this));
		wlaGueltigbis.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gueltigbis"));
		wlaBenutzerkannung.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.benutzerkennung"));
		wbuDefMandant.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.defaultmandant")
				+ "...");
		wbuDefMandant
				.setActionCommand(PanelBenutzer.ACTION_SPECIAL_MANDANT_FROM_LISTE);
		wbuDefMandant.addActionListener(this);

		wtfDefaultMandant.setActivatable(false);
		wtfDefaultMandant.setMandatoryField(true);

		wtfDefaultMandant.setText("");
		wcbGesperrt.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.gesperrt"));
		wcbAendern.setText(LPMain.getInstance().getTextRespectUISPr(
				"pers.benutzer.kannkennwortaendern"));
		wlaKennwortBestaetigen.setText(LPMain.getInstance()
				.getTextRespectUISPr("pers.benutzer.kennwortbestaetigen"));
		wpfKennwortBestaetigen.setText("");
		wpfKennwortBestaetigen.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNWORT);

		wpfKennwortBestaetigen.setMandatoryField(true);
		wpfKennwortBestaetigen
				.addFocusListener(new PanelBenutzer_wpfKennwortBestaetigen_focusAdapter(
						this));
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1, 0.5,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKennwort, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wpfKennwort, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGueltigbis, new GridBagConstraints(0, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfGueltigbis, new GridBagConstraints(1, 5, 1, 1, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wlaBenutzerkannung, new GridBagConstraints(0, 0, 1, 1,
				0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuDefMandant, new GridBagConstraints(0, 3, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfDefaultMandant, new GridBagConstraints(1, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbGesperrt, new GridBagConstraints(1, 6, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wcbAendern, new GridBagConstraints(1, 7, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaKennwortBestaetigen, new GridBagConstraints(0, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wpfKennwortBestaetigen, new GridBagConstraints(1, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_PRINT, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BENUTZER;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			benutzerDto = DelegateFactory.getInstance().getBenutzerDelegate()
					.benutzerFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	// protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
	// throws Throwable {
	// super.eventActionUpdate(aE, bNeedNoUpdateI);
	// wpfKennwort.setText("");
	// wpfKennwortBestaetigen.setText("");
	// }

	void wpfKennwort_focusLost(FocusEvent e) {
		if (wpfKennwort.isEditable()) {
			String sPassword = new String(wpfKennwort.getPassword());
			if (sPassword.length() < 1) {

				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"pers.error.kennwortdarfnichtleersein"));
			} else {
				wpfKennwortBestaetigen.setText("");
				wpfKennwortBestaetigen.requestFocus();
			}
		}
	}

	void wpfKennwortBestaetigen_focusLost(FocusEvent e) {
		if (wpfKennwort.isEditable()) {
			if (!new String(wpfKennwort.getPassword()).equals(new String(
					wpfKennwortBestaetigen.getPassword()))) {
				DialogFactory.showModalDialog(
						LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr(
								"pers.error.kennwoerterstimmennichtueberein"));
				wpfKennwort.setText("");
				wpfKennwortBestaetigen.setText("");
			}
		}
	}
}

class PanelBenutzer_wpfKennwort_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelBenutzer adaptee;

	PanelBenutzer_wpfKennwort_focusAdapter(PanelBenutzer adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wpfKennwort_focusLost(e);
	}
}

class PanelBenutzer_wpfKennwortBestaetigen_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelBenutzer adaptee;

	PanelBenutzer_wpfKennwortBestaetigen_focusAdapter(PanelBenutzer adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wpfKennwortBestaetigen_focusLost(e);
	}
}
