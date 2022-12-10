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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
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
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTimeField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.SonderzeitenDto;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelSonderzeiten extends PanelBasis {

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
	private InternalFrameZeiterfassung internalFrameZeiterfassung = null;
	private WrapperLabel wlaVon = new WrapperLabel();
	private WrapperDateField wdfVon = new WrapperDateField();
	private SonderzeitenDto sonderzeitenDto = null;
	private WrapperLabel wlaBis = new WrapperLabel();
	private WrapperDateField wdfBis = new WrapperDateField();
	private WrapperButton wbuTaetigkeit = new WrapperButton();
	private WrapperTextField wtfTaetigkeit = new WrapperTextField();
	private WrapperLabel wlaDauer = new WrapperLabel();
	private WrapperTimeField wtfStunden = new WrapperTimeField();
	private PanelQueryFLR panelQueryFLRTaetigkeit = null;
	private WrapperCheckBox wcbAlle = new WrapperCheckBox();
	private Integer zuletzgewaehlteSondertaetigkeit = null;
	private Timestamp zuletztgewaehltesDatum = null;

	private ButtonGroup buttonGroupArt = new ButtonGroup();
	private WrapperRadioButton wrbTageweise = new WrapperRadioButton();
	private WrapperRadioButton wrbHalbtageweise = new WrapperRadioButton();
	private WrapperRadioButton wrbStundenweise = new WrapperRadioButton();

	private final String ACTION_SPECIAL_TAETIGKEITART = "ACTION_SPECIAL_TAETIGKEITART";

	static final public String ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE = "action_taetigkeit_from_liste";

	private ZeiterfassungPruefer zeiterfassungPruefer = null;

	public PanelSonderzeiten(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameZeiterfassung = (InternalFrameZeiterfassung) internalFrame;
		zeiterfassungPruefer = new ZeiterfassungPruefer(getInternalFrame());
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfVon;
	}

	protected void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		sonderzeitenDto = new SonderzeitenDto();

		wcbAlle.setVisible(true);

		leereAlleFelder(this);
		wdfBis.setActivatable(true);

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (zeiterfassungPruefer.pruefeObBuchungMoeglich(sonderzeitenDto
				.getTDatum(), internalFrameZeiterfassung.getPersonalDto()
				.getIId())) {
			super.eventActionUpdate(aE, bNeedNoUpdateI);
		} else {
			return;
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE)) {
			dialogQueryTaetigkeitFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_TAETIGKEITART)) {
			if (e.getSource().equals(wrbTageweise)) {
				wtfStunden.setMandatoryField(false);
				wtfStunden.setEnabled(false);
				wtfStunden.setActivatable(false);

			} else if (e.getSource().equals(wrbHalbtageweise)) {
				wtfStunden.setMandatoryField(false);
				wtfStunden.setEnabled(false);
				wtfStunden.setActivatable(false);

			} else if (e.getSource().equals(wrbStundenweise)) {
				wtfStunden.setMandatoryField(true);
				wtfStunden.setEnabled(true);
				wtfStunden.setActivatable(true);

			}

		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		
		Object[] o = internalFrameZeiterfassung.getTabbedPaneZeiterfassung().getPanelQuerySonderzeiten()
				.getSelectedIds();
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				
				SonderzeitenDto sonderzeitenDto =DelegateFactory.getInstance().getZeiterfassungDelegate().sonderzeitenFindByPrimaryKey((Integer)o[i]);
				
				if (zeiterfassungPruefer.pruefeObBuchungMoeglich(sonderzeitenDto
						.getTDatum(), internalFrameZeiterfassung.getPersonalDto()
						.getIId())) {
					DelegateFactory.getInstance().getZeiterfassungDelegate()
							.removeSonderzeiten(sonderzeitenDto);
					this.setKeyWhenDetailPanel(null);
					super.eventActionDelete(e, false, false);
				} 
			}
		}
		
		
		
	}

	protected void components2Dto() throws ExceptionLP {
		sonderzeitenDto.setTDatum(wdfVon.getTimestamp());

		zuletztgewaehltesDatum = wdfVon.getTimestamp();

		sonderzeitenDto.setBTag(wrbTageweise.getShort());

		sonderzeitenDto.setBHalbtag(wrbHalbtageweise.getShort());
		
		
		sonderzeitenDto.setBAutomatik(Helper.boolean2Short(false));

		sonderzeitenDto.setPersonalIId(internalFrameZeiterfassung
				.getPersonalDto().getIId());

		if (wrbTageweise.isSelected() || wrbHalbtageweise.isSelected()) {

		} else {

			sonderzeitenDto.setUStunden(wtfStunden.getTime());
		}
	}

	protected void dto2Components() throws ExceptionLP, Throwable {
		wdfVon.setTimestamp(sonderzeitenDto.getTDatum());
		wtfStunden.setTime(sonderzeitenDto.getUStunden());
		this.setStatusbarPersonalIIdAendern(sonderzeitenDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(sonderzeitenDto.getTAendern());
		if (sonderzeitenDto.getTaetigkeitDto() != null) {
			wtfTaetigkeit.setText(sonderzeitenDto.getTaetigkeitDto().getCNr());
		}
		wrbTageweise
				.setSelected(Helper.short2Boolean(sonderzeitenDto.getBTag()));
		wrbHalbtageweise.setSelected(Helper.short2Boolean(sonderzeitenDto
				.getBHalbtag()));

		if (Helper.short2Boolean(sonderzeitenDto.getBTag()) == false
				&& Helper.short2Boolean(sonderzeitenDto.getBHalbtag()) == false) {
			wrbStundenweise.setSelected(true);
		}

		if (Helper.short2Boolean(sonderzeitenDto.getBTag()) == true) {
			wtfStunden.setMandatoryField(false);
			wtfStunden.setActivatable(false);

		} else if (Helper.short2Boolean(sonderzeitenDto.getBHalbtag()) == true) {
			wtfStunden.setMandatoryField(false);
			wtfStunden.setActivatable(false);

		} else {
			wtfStunden.setMandatoryField(true);
			wtfStunden.setActivatable(true);

		}

		wdfBis.setActivatable(false);
		wdfBis.setEnabled(false);
		wdfBis.setTimestamp(null);

	}

	void dialogQueryTaetigkeitFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_FILTER };

		panelQueryFLRTaetigkeit = new PanelQueryFLR(ZeiterfassungFilterFactory
				.getInstance().createQTTaetigkeit(), ZeiterfassungFilterFactory
				.getInstance().createFKAlleTagbuchbarentaetigkeiten(),
				QueryParameters.UC_ID_SONDERTAETIGKEIT, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("title.taetigkeitauswahlliste"));
		panelQueryFLRTaetigkeit.setSelectedId(sonderzeitenDto
				.getTaetigkeitIId());
		new DialogQuery(panelQueryFLRTaetigkeit);

	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {
		LockStateValue lockStateValue = super.getLockedstateDetailMainKey();

		if (getKeyWhenDetailPanel() instanceof Integer) {

			Integer key = (Integer) getKeyWhenDetailPanel();
			sonderzeitenDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.sonderzeitenFindByPrimaryKey(key);

			ParametermandantDto parameter = DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getMandantparameter(LPMain.getTheClient().getMandant(),
							ParameterFac.KATEGORIE_PERSONAL,
							ParameterFac.PARAMETER_URLAUBSANTRAG);
			boolean bUrlaubsantrag = (java.lang.Boolean) parameter
					.getCWertAsObject();

			boolean bSonderzeitenCUD = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_PERS_SONDERZEITEN_CUD);

			if (bSonderzeitenCUD == false) {

				if (bUrlaubsantrag == true) {
					if (sonderzeitenDto != null
							&& sonderzeitenDto.getTaetigkeitIId() != null
							&& (sonderzeitenDto
									.getTaetigkeitIId()
									.equals(DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.taetigkeitFindByCNr(
													ZeiterfassungFac.TAETIGKEIT_URLAUB)
											.getIId()) || !Helper
									.short2boolean(DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.taetigkeitFindByPrimaryKey(
													sonderzeitenDto
															.getTaetigkeitIId())
											.getBDarfSelberBuchen()))) {

						if (sonderzeitenDto
								.getTaetigkeitIId()
								.equals(DelegateFactory
										.getInstance()
										.getZeiterfassungDelegate()
										.taetigkeitFindByCNr(
												ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG).getIId())
								|| sonderzeitenDto
										.getTaetigkeitIId()
										.equals(DelegateFactory
												.getInstance()
												.getZeiterfassungDelegate()
												.taetigkeitFindByCNr(
														ZeiterfassungFac.TAETIGKEIT_ZAANTRAG).getIId())	|| sonderzeitenDto
														.getTaetigkeitIId()
														.equals(DelegateFactory
																.getInstance()
																.getZeiterfassungDelegate()
																.taetigkeitFindByCNr(
																		ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG).getIId())) {

						} else {
							lockStateValue = new LockStateValue(
									PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
						}

					}
				} else {

					boolean urlaubsOderZAAntrag = false;

					if (sonderzeitenDto
							.getTaetigkeitIId()
							.equals(DelegateFactory
									.getInstance()
									.getZeiterfassungDelegate()
									.taetigkeitFindByCNr(
											ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG)
									.getIId())
							|| sonderzeitenDto
									.getTaetigkeitIId()
									.equals(DelegateFactory
											.getInstance()
											.getZeiterfassungDelegate()
											.taetigkeitFindByCNr(
													ZeiterfassungFac.TAETIGKEIT_ZAANTRAG)
											.getIId())|| sonderzeitenDto
											.getTaetigkeitIId()
											.equals(DelegateFactory
													.getInstance()
													.getZeiterfassungDelegate()
													.taetigkeitFindByCNr(
															ZeiterfassungFac.TAETIGKEIT_KRANKANTRAG)
													.getIId())) {
						urlaubsOderZAAntrag = true;
					}

					if (sonderzeitenDto != null
							&& sonderzeitenDto.getTaetigkeitIId() != null
							&& !urlaubsOderZAAntrag) {

						lockStateValue = new LockStateValue(
								PanelBasis.LOCK_ENABLE_REFRESHANDPRINT_ONLY);
					}
				}

			}
		}
		return lockStateValue;
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		wcbAlle.setVisible(false);
		wcbAlle.setSelected(false);
		super.eventActionUnlock(e);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if ((!wrbTageweise.isSelected() && !wrbHalbtageweise.isSelected())
					&& wtfStunden.getTime().getTime() == -3600000) {

				DialogFactory.showModalDialog("Achtung",
						"Stunden m\u00FCssen gr\u00F6sser als 00:00 sein.");

			} else {
				TaetigkeitDto taetigkeitDto = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByPrimaryKey(
								sonderzeitenDto.getTaetigkeitIId());

				boolean bUrlaubOderUrlaubsantrag = false;
				if (taetigkeitDto.getCNr().equals(
						ZeiterfassungFac.TAETIGKEIT_URLAUB)
						|| taetigkeitDto.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_URLAUBSANTRAG)
						|| taetigkeitDto.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_ZEITAUSGLEICH)
						|| taetigkeitDto.getCNr().equals(
								ZeiterfassungFac.TAETIGKEIT_ZAANTRAG)) {
					bUrlaubOderUrlaubsantrag = true;
				}

				if (bUrlaubOderUrlaubsantrag == false
						&& com.lp.util.Helper.short2boolean(wrbHalbtageweise
								.getShort())) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("zeiterfassung.error.nururlaubisthalbtaegigerlaubt"));
				} else {

					components2Dto();

					if (zeiterfassungPruefer.pruefeObBuchungMoeglich(
							sonderzeitenDto.getTDatum(),
							internalFrameZeiterfassung.getPersonalDto()
									.getIId())) {

						if (sonderzeitenDto.getIId() == null) {
							components2Dto();

							PersonalDto[] personalDtos = null;

							if (wcbAlle.isSelected()) {
								personalDtos = DelegateFactory.getInstance()
										.getPersonalDelegate()
										.personalFindByMandantCNr();
							} else {
								personalDtos = new PersonalDto[1];
								personalDtos[0] = DelegateFactory
										.getInstance()
										.getPersonalDelegate()
										.personalFindByPrimaryKey(
												sonderzeitenDto
														.getPersonalIId());
							}

							for (int k = 0; k < personalDtos.length; k++) {

								PartnerDto partnerDto = DelegateFactory
										.getInstance()
										.getPartnerDelegate()
										.partnerFindByPrimaryKey(
												personalDtos[k].getPartnerIId());
								personalDtos[k].setPartnerDto(partnerDto);
								String sPerson = " ("
										+ personalDtos[k].formatAnrede() + ")";
								sonderzeitenDto.setPersonalIId(personalDtos[k]
										.getIId());

								try {
									if (wdfBis.getTimestamp() == null) {

										boolean bNoetig = DelegateFactory
												.getInstance()
												.getZeiterfassungDelegate()
												.istUrlaubstagZuDatumNoetig(
														sonderzeitenDto
																.getPersonalIId(),
														sonderzeitenDto
																.getTDatum());

										if (bNoetig == true) {
											sonderzeitenDto
													.setIId(DelegateFactory
															.getInstance()
															.getZeiterfassungDelegate()
															.createSonderzeiten(
																	sonderzeitenDto));
										} else {
											boolean ja = DialogFactory
													.showModalJaNeinDialog(
															getInternalFrame(),
															"Zu diesem Datum wird aufgrund des Betriebskalenders/ Zeitmodells keine Sonderzeit ben\u00F6tigt. Wollen Sie dennoch eine Sonderzeit eintragen?"
																	+ sPerson,
															"Warnung");

											if (ja == true) {
												sonderzeitenDto
														.setIId(DelegateFactory
																.getInstance()
																.getZeiterfassungDelegate()
																.createSonderzeiten(
																		sonderzeitenDto));
											}
										}
									} else {

										long lDiesesMonat = wdfVon
												.getTimestamp().getTime();
										long lNaechstesMonat = wdfBis
												.getTimestamp().getTime();
										long lDifferenz = lNaechstesMonat
												- lDiesesMonat;

										long lTage = lDifferenz / 3600000 / 24;

										boolean bAbbuchen = true;
										if (lTage > 31) {
											bAbbuchen = (DialogFactory
													.showMeldung(
															LPMain.getTextRespectUISPr("zeiterfassung.warning.zeitraumuebersteigt31tage"),
															LPMain.getTextRespectUISPr("lp.warning"),
															javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION);
										}
										if (lTage < 0) {
											DialogFactory
													.showModalDialog(

															LPMain.getTextRespectUISPr("lp.warning"),
															LPMain.getTextRespectUISPr("zeiterfassung.error.beginnmussvorendeliegen"));
											bAbbuchen = false;
										}
										if (bAbbuchen) {

											java.sql.Timestamp[] buchungenVorhanden = DelegateFactory
													.getInstance()
													.getZeiterfassungDelegate()
													.sindIstZeitenVorhandenWennUrlaubGebuchtWird(
															sonderzeitenDto,
															wdfVon.getTimestamp(),
															wdfBis.getTimestamp());

											if (buchungenVorhanden != null
													&& buchungenVorhanden.length > 0) {
												Object[] objs = new Object[2];
												objs[0] = LPMain
														.getTextRespectUISPr("zeiterfassung.error.sonderzeitenistbuchungenvorhanden.trotzdem");
												objs[1] = LPMain
														.getTextRespectUISPr("zeiterfassung.error.sonderzeitenistbuchungenvorhanden.auslassen");

												String meldung = LPMain
														.getTextRespectUISPr("zeiterfassung.error.sonderzeitenistbuchungenvorhanden")
														+ sPerson;

												meldung += " (";

												for (int i = 0; i < buchungenVorhanden.length; i++) {
													meldung += Helper
															.formatDatum(
																	buchungenVorhanden[i],
																	LPMain.getTheClient()
																			.getLocUi());
													if (i != buchungenVorhanden.length - 1) {
														meldung += ",";
													}

												}
												meldung += ")";

												int i = DialogFactory
														.showModalDialog(
																getInternalFrame(),
																meldung,
																LPMain.getTextRespectUISPr("lp.warning"),
																objs, objs[1]);
												if (i == 0) {
													sonderzeitenDto
															.setIId(DelegateFactory
																	.getInstance()
																	.getZeiterfassungDelegate()
																	.createSonderzeitenVonBis(
																			sonderzeitenDto,
																			wdfVon.getTimestamp(),
																			wdfBis.getTimestamp(),
																			null));
												} else if (i == 1) {
													sonderzeitenDto
															.setIId(DelegateFactory
																	.getInstance()
																	.getZeiterfassungDelegate()
																	.createSonderzeitenVonBis(
																			sonderzeitenDto,
																			wdfVon.getTimestamp(),
																			wdfBis.getTimestamp(),
																			buchungenVorhanden));
												} else if (i == -1) {
													return;
												}
											} else {
												sonderzeitenDto
														.setIId(DelegateFactory
																.getInstance()
																.getZeiterfassungDelegate()
																.createSonderzeitenVonBis(
																		sonderzeitenDto,
																		wdfVon.getTimestamp(),
																		wdfBis.getTimestamp(),
																		null));
											}

											if (sonderzeitenDto.getIId() == null) {
												DialogFactory
														.showModalDialog(
																"Achtung",
																"Im angegebenen Zeitraum wurde keine Sollzeit gefunden. Daher wurde kein Zeiteintrag erstellt."
																		+ sPerson);
											}
										}
									}
								} catch (ExceptionLP e1) {
									if (e1.getICode() == EJBExceptionLP.FEHLER_DUPLICATE_UNIQUE) {
										String s = "(FDU) Es gibt bereits einen Eintrag in der Datenbank"
												+ sPerson
												+ " Die Person wird \u00FCbersprungen";
										DialogFactory
												.showModalDialog(
														LPMain.getTextRespectUISPr("lp.warning"),
														s);
									} else {
										handleException(e1, false);
									}

									if (k != personalDtos.length - 1) {
										continue;
									}

								}
							}
							wcbAlle.setVisible(false);
							wcbAlle.setSelected(false);
						} else {
							DelegateFactory.getInstance()
									.getZeiterfassungDelegate()
									.updateSonderzeiten(sonderzeitenDto);
						}

					}

					super.eventActionSave(e, true);
					if (getInternalFrame().getKeyWasForLockMe() == null) {
						getInternalFrame().setKeyWasForLockMe(
								internalFrameZeiterfassung.getPersonalDto()
										.getIId() + "");
					}
					eventYouAreSelected(false);
				}
			}

		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRTaetigkeit) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				sonderzeitenDto.setTaetigkeitIId(key);

				TaetigkeitDto taetigkeitDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByPrimaryKey(key);
				wtfTaetigkeit.setText(taetigkeitDto.getCNr());

				zuletzgewaehlteSondertaetigkeit = taetigkeitDto.getIId();
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

		wlaVon.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.sonderzeiten.tagvon"));
		wdfVon.setMandatoryField(true);

		wlaBis.setText(LPMain.getTextRespectUISPr("lp.bis"));
		wdfBis.setToolTipText("");
		// wdfBis.setActivatable(false);
		getInternalFrame().addItemChangedListener(this);
		wbuTaetigkeit
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.zeitdaten.sondertaetigkeit")
						+ "...");
		wbuTaetigkeit
				.setActionCommand(PanelSonderzeiten.ACTION_SPECIAL_TAETIGKEIT_FROM_LISTE);
		wbuTaetigkeit.addActionListener(this);

		wrbTageweise.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.sonderzeiten.ganzertag"));
		wrbHalbtageweise.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.sonderzeiten.halbertag"));
		wrbStundenweise.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.sonderzeiten.oderstunden"));

		wrbTageweise.addActionListener(this);
		wrbHalbtageweise.addActionListener(this);
		wrbStundenweise.addActionListener(this);

		wrbTageweise.setActionCommand(ACTION_SPECIAL_TAETIGKEITART);
		wrbHalbtageweise.setActionCommand(ACTION_SPECIAL_TAETIGKEITART);
		wrbStundenweise.setActionCommand(ACTION_SPECIAL_TAETIGKEITART);

		buttonGroupArt.add(wrbTageweise);
		buttonGroupArt.add(wrbHalbtageweise);
		buttonGroupArt.add(wrbStundenweise);

		wtfTaetigkeit.setText("");
		wtfTaetigkeit.setMandatoryField(true);
		wtfTaetigkeit.setActivatable(false);

		wcbAlle.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.report.allepersonen"));
		wcbAlle.setVisible(false);

		wlaDauer.setText(LPMain.getTextRespectUISPr("lp.dauer") + ":");
		wtfStunden.setMandatoryField(true);

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
		jpaWorkingOn.add(wlaVon, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVon, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBis, new GridBagConstraints(2, 0, 1, 1, 0.05, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfBis, new GridBagConstraints(3, 0, 1, 1, 0.15, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuTaetigkeit, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfTaetigkeit, new GridBagConstraints(1, 1, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbAlle, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaDauer, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbTageweise, new GridBagConstraints(1, 2, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbStundenweise, new GridBagConstraints(1, 4, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfStunden, new GridBagConstraints(2, 4, 1, 2, 0.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 60, 0));

		jpaWorkingOn.add(wrbHalbtageweise, new GridBagConstraints(1, 3, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_SONDERZEITEN;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
			wrbTageweise.setSelected(true);
			wtfStunden.setEnabled(false);
			wdfVon.setTimestamp(new java.sql.Timestamp(System
					.currentTimeMillis()));
			// Zuletzt gewaehlte Sondertaetigkeit anzeigen;
			if (zuletzgewaehlteSondertaetigkeit != null) {
				TaetigkeitDto taetigkeitDto = DelegateFactory
						.getInstance()
						.getZeiterfassungDelegate()
						.taetigkeitFindByPrimaryKey(
								zuletzgewaehlteSondertaetigkeit);
				sonderzeitenDto
						.setTaetigkeitIId(zuletzgewaehlteSondertaetigkeit);
				wtfTaetigkeit.setText(taetigkeitDto.getCNr());
			}
			if (zuletztgewaehltesDatum != null) {
				wdfVon.setTimestamp(zuletztgewaehltesDatum);
			}

		} else {
			sonderzeitenDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.sonderzeitenFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

}
