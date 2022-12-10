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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.PanelAdditiveVerpackungsmengen;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.InventurFac;
import com.lp.server.artikel.service.InventurlisteDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelInventurliste extends PanelBasis {

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
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperIdentField wifArtikel = null;
	private InventurlisteDto inventurlisteDto = null;
	private WrapperButton wbuLager = new WrapperButton();
	private WrapperTextField wtfLager = new WrapperTextField();
	private WrapperSnrChnrField wtfSeriennummer = new WrapperSnrChnrField(getInternalFrame(), true);

	private WrapperLabel wlaInventurmenge = new WrapperLabel();
	private WrapperLabel wlaSeriennummer = new WrapperLabel();

	private WrapperLabel wlaVoraussichtlicherInventurstand = new WrapperLabel();

	private WrapperLabel wlaEinheitMenge = new WrapperLabel();

	private WrapperNumberField wnfInventurmenge = new WrapperNumberField();

	PanelAdditiveVerpackungsmengen pa = null;

	Integer lastLager = null;

	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";

	private PanelQueryFLR panelQueryFLRLager = null;

	public PanelInventurliste(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	protected void setDefaults() {

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {

		if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().getBInventurdurchgefuehrt())) {

			ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artikelFindByPrimaryKey(inventurlisteDto.getArtikelIId());
			if (artikelTempDto.istArtikelSnrOderchargentragend()) {
				DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
						LPMain.getInstance().getTextRespectUISPr("artikel.invenur.snrchnrupdatenichtmoeglich"));
				return;
			}

			super.eventActionUpdate(aE, bNeedNoUpdateI);

			if (inventurlisteDto.getArtikelDto() != null
					&& inventurlisteDto.getArtikelDto().istArtikelSnrOderchargentragend() == false) {
				wtfSeriennummer.getButtonSnrAuswahl().setEnabled(false);
			}

		} else {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("artikel.error.inventur.bereitsdurchgefuehrt"));
			return;
		}

	}

	private void setLagerMitLagerplaetzen(Integer lagerIId, Integer artikelIId) throws Throwable {

		if (lagerIId != null) {
			LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().lagerFindByPrimaryKey(lagerIId);
			String lager = lagerDto.getCNr();

			if (artikelIId != null) {
				String s = DelegateFactory.getInstance().getLagerDelegate().getLagerplaezteEinesArtikels(artikelIId,
						lagerIId);
				if (s != null && s.length() > 0) {
					lager += "  (" + LPMain.getInstance().getTextRespectUISPr("artikel.inventurliste.lagerplaetze")
							+ ":" + s + ")";
				}
			}

			wtfLager.setText(lager);
		} else {
			wtfLager.setText(null);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		if (Helper.short2boolean(internalFrameArtikel.getInventurDto().getBInventurdurchgefuehrt())) {
			wnfInventurmenge.setActivatable(true);
		} else {
			wnfInventurmenge.setActivatable(true);
		}

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();
			wlaVoraussichtlicherInventurstand.setText(null);
			if (lastLager != null) {
				inventurlisteDto.setLagerIId(lastLager);

				setLagerMitLagerplaetzen(lastLager, null);

			} else {

				if (internalFrameArtikel.getInventurDto().getLagerIId() != null) {

					if (key != null) {

						setLagerMitLagerplaetzen(internalFrameArtikel.getInventurDto().getLagerIId(), null);

						inventurlisteDto.setLagerIId(internalFrameArtikel.getInventurDto().getLagerIId());
					}
				} else {

					ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate()
							.getMandantparameter(LPMain.getInstance().getTheClient().getMandant(),
									ParameterFac.KATEGORIE_ARTIKEL, ParameterFac.PARAMETER_DEFAULT_LAGER);
					LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
							.lagerFindByPrimaryKeyOhneExc(new Integer(parameter.getCWert()));

					if (lagerDto == null) {
						// SP740
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"), LPMain
								.getInstance().getTextRespectUISPr("artikel.inventur.parameter.defaultlager.falsch"));
					}

					if (key != null && lagerDto != null
							&& lagerDto.getMandantCNr().equals(LPMain.getInstance().getTheClient().getMandant())) {

						setLagerMitLagerplaetzen(lagerDto.getIId(), null);

						inventurlisteDto.setLagerIId(lagerDto.getIId());
					}
				}

			}

			if (key != null) {

				wbuLager.setEnabled(true);
				wifArtikel.getWbuArtikel().setEnabled(true);
				wifArtikel.getWtfIdent().setEditable(true);
				wifArtikel.getWtfIdent().requestFocus();
			}

			wtfSeriennummer.getButtonSnrAuswahl().setEnabled(false);
		} else {
			inventurlisteDto = DelegateFactory.getInstance().getInventurDelegate()
					.inventurlisteFindByPrimaryKey((Integer) key);

			if (Helper.short2boolean(inventurlisteDto.getArtikelDto().getBSeriennrtragend())
					|| Helper.short2boolean(inventurlisteDto.getArtikelDto().getBChargennrtragend())) {
				wtfSeriennummer.setMandatoryField(true);

			}

			else {
				wtfSeriennummer.setMandatoryField(false);
				wtfSeriennummer.setText(null);
			}
			dto2Components();

			BigDecimal inventurstand = DelegateFactory.getInstance().getInventurDelegate().getInventurstand(
					inventurlisteDto.getArtikelIId(), inventurlisteDto.getLagerIId(),
					internalFrameArtikel.getInventurDto().getIId(),
					internalFrameArtikel.getInventurDto().getTInventurdatum());

			wlaVoraussichtlicherInventurstand.setText(
					LPMain.getInstance().getTextRespectUISPr("artikel.inventur.voraussichtlicherinventurstand") + " "
							+ Helper.formatZahl(inventurstand, Defaults.getInstance().getIUINachkommastellenMenge(),
									LPMain.getInstance().getTheClient().getLocUi()));

		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().getBInventurdurchgefuehrt())) {
			wnfInventurmenge.setActivatable(true);

			wifArtikel.getWbuArtikel().setActivatable(true);
			wbuLager.setActivatable(true);
			// wtfSeriennummer.setActivatable(true);

			super.eventActionNew(eventObject, true, false);
			leereAlleFelder(this);
			inventurlisteDto = new InventurlisteDto();

		} else {
			DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
					LPMain.getInstance().getTextRespectUISPr("artikel.error.inventur.bereitsdurchgefuehrt"));
		}
	}

	protected void dto2Components() throws Throwable {

		setLagerMitLagerplaetzen(inventurlisteDto.getLagerDto().getIId(), inventurlisteDto.getArtikelIId());

		wnfInventurmenge.setBigDecimal(inventurlisteDto.getNInventurmenge());
		wtfSeriennummer.setText(inventurlisteDto.getCSeriennrchargennr());

		ArtikelDto artikelTempDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(inventurlisteDto.getArtikelIId());

		pa.setVerpackungsmenge(artikelTempDto.getFVerpackungsmenge());

		wtfSeriennummer.setArtikelIdLagerId(artikelTempDto, null);

		wtfSeriennummer
				.setSeriennummern(
						SeriennrChargennrMitMengeDto.erstelleDtoAusEinerChargennummer(
								inventurlisteDto.getCSeriennrchargennr(), inventurlisteDto.getNInventurmenge()),
						artikelTempDto.getIId(), null);

		wifArtikel.setArtikelDto(artikelTempDto);
		wlaEinheitMenge.setText(artikelTempDto.getEinheitCNr().trim());
		inventurlisteDto.setArtikelIId(artikelTempDto.getIId());

		this.setStatusbarPersonalIIdAnlegen(inventurlisteDto.getPersonalIIdAendern());
		this.setStatusbarTAnlegen(inventurlisteDto.getTAendern());

	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {
		panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
				inventurlisteDto.getLagerIId());

		new DialogQuery(panelQueryFLRLager);
	}

	protected void components2Dto() throws ExceptionLP {
		inventurlisteDto.setNInventurmenge(wnfInventurmenge.getBigDecimal());

		inventurlisteDto.setInventurIId(internalFrameArtikel.getInventurDto().getIId());

		if (wtfSeriennummer.getSeriennummern() != null && wtfSeriennummer.getSeriennummern().size() > 0) {
			inventurlisteDto.setCSeriennrchargennr(wtfSeriennummer.getSeriennummern().get(0).getCSeriennrChargennr());
			inventurlisteDto.setNInventurmenge(wtfSeriennummer.getSeriennummern().get(0).getNMenge());

		} else {
			inventurlisteDto.setCSeriennrchargennr(null);
		}

		inventurlisteDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

			if (e.getSource() == wifArtikel) {

				ArtikelDto artikelTempDto = wifArtikel.getArtikelDto();
				wifArtikel.setArtikelDto(artikelTempDto);
				wtfSeriennummer.setArtikelIdLagerId(artikelTempDto, inventurlisteDto.getLagerIId());
				wlaEinheitMenge.setText(artikelTempDto.getEinheitCNr().trim());
				inventurlisteDto.setArtikelIId(artikelTempDto.getIId());

				setLagerMitLagerplaetzen(inventurlisteDto.getLagerIId(), inventurlisteDto.getArtikelIId());

				pa.setVerpackungsmenge(artikelTempDto.getFVerpackungsmenge());

				if (Helper.short2boolean(artikelTempDto.getBSeriennrtragend())) {
					wtfSeriennummer.setMandatoryField(true);
					wtfSeriennummer.getButtonSnrAuswahl().setEnabled(true);

				} else if (Helper.short2boolean(artikelTempDto.getBChargennrtragend())) {
					wtfSeriennummer.setMandatoryField(true);
					wtfSeriennummer.getButtonSnrAuswahl().setEnabled(true);

				} else {
					wtfSeriennummer.setMandatoryField(false);
					wtfSeriennummer.getButtonSnrAuswahl().setEnabled(false);

				}
			} else if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);

				setLagerMitLagerplaetzen(lagerDto.getIId(), inventurlisteDto.getArtikelIId());
				inventurlisteDto.setLagerIId(lagerDto.getIId());
			}
		} else if (e.getID() == ItemChangedEvent.ITEM_CHANGED) {
			if (inventurlisteDto != null && inventurlisteDto.getArtikelDto() != null) {

				if (Helper.short2boolean(inventurlisteDto.getArtikelDto().getBSeriennrtragend())
						|| Helper.short2boolean(inventurlisteDto.getArtikelDto().getBChargennrtragend())) {
					wtfSeriennummer.setMandatoryField(true);
					wtfSeriennummer.setEditable(false);
				} else {
					wtfSeriennummer.setMandatoryField(false);
					wtfSeriennummer.setEditable(false);
					wtfSeriennummer.setText(null);
				}
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

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wtfSeriennummer.setWnfBelegMenge(wnfInventurmenge);

		getInternalFrame().addItemChangedListener(this);

		wnfInventurmenge.setMandatoryField(true);

		wnfInventurmenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		wlaInventurmenge.setText(LPMain.getInstance().getTextRespectUISPr("artikel.inventurmenge"));
		wlaSeriennummer.setText(
				LPMain.getInstance().getTextRespectUISPr("artikel.handlagerbewegung.seriennrchargennreinzeln"));

		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr("button.lager"));
		wbuLager.setActionCommand(PanelInventurliste.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wbuLager.setActivatable(false);
		wifArtikel.getWbuArtikel().setActivatable(false);
		wifArtikel.getWtfIdent().setActivatable(false);

		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);
		wlaEinheitMenge.setHorizontalAlignment(SwingConstants.LEFT);

		pa = new PanelAdditiveVerpackungsmengen(getInternalFrame(), wnfInventurmenge);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0, 0, 1, 1, 1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wifArtikel.getWtfIdent().setUppercaseField(true);
		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1, 0, 1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), new GridBagConstraints(2, 0, 1, 1, 2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfSeriennummer.getButtonSnrAuswahl(), new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSeriennummer, new GridBagConstraints(1, 1, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 2, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 2, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaInventurmenge, new GridBagConstraints(0, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfInventurmenge, new GridBagConstraints(1, 3, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaEinheitMenge, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 20, 0));

		ParametermandantDto parameter = DelegateFactory.getInstance().getParameterDelegate().getMandantparameter(
				LPMain.getInstance().getTheClient().getMandant(), ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.PARAMETER_VERPACKUNGSMENGEN_EINGABE);
		int bVerpackungsmengeneingabe = (Integer) parameter.getCWertAsObject();

		if (bVerpackungsmengeneingabe > 0) {

			jpaWorkingOn.add(pa, new GridBagConstraints(0, 4, 2, 1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}

		jpaWorkingOn.add(wlaVoraussichtlicherInventurstand, new GridBagConstraints(0, 5, 2, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_INVENTUR;
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable, Throwable {
		if (!Helper.short2boolean(internalFrameArtikel.getInventurDto().getBInventurdurchgefuehrt())) {

			if (DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getInstance().getTextRespectUISPr("lp.eintrag.loeschen"))) {

				DelegateFactory.getInstance().getInventurDelegate()
						.removeInventurlisteUndNimmProtokolleintraegeZurueckInventurliste(inventurlisteDto);
				this.setKeyWhenDetailPanel(null);
				super.eventActionDelete(e, false, false);
			}
		} else {
			DialogFactory.showModalDialog("Fehler", "Inventur wurde bereits durchgef\u00FChrt");
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {

			if (wifArtikel.getArtikelDto().istArtikelSnrOderchargentragend()) {
				List<SeriennrChargennrMitMengeDto> snrs = wtfSeriennummer.getSeriennummern();
				String sZeilenAusgelassen = "";

				for (int i = 0; i < snrs.size(); i++) {
					inventurlisteDto.setIId(null);

					if (wifArtikel.getArtikelDto().isSeriennrtragend() && wnfInventurmenge.getDouble() == 0) {
						inventurlisteDto.setNInventurmenge(BigDecimal.ZERO);
					} else {
						inventurlisteDto.setNInventurmenge(snrs.get(i).getNMenge());
					}

					inventurlisteDto.setCSeriennrchargennr(snrs.get(i).getCSeriennrChargennr());
					inventurlisteDto.setInventurIId(internalFrameArtikel.getInventurDto().getIId());
					java.math.BigDecimal mengeAusInventurliste = inventurlisteDto.getNInventurmenge();
					java.math.BigDecimal lagerstandVeraenderung = DelegateFactory.getInstance().getLagerDelegate()
							.getLagerstandsVeraenderungOhneInventurbuchungen(inventurlisteDto.getArtikelIId(),
									inventurlisteDto.getLagerIId(),
									internalFrameArtikel.getInventurDto().getTInventurdatum(),
									new java.sql.Timestamp(System.currentTimeMillis()),
									inventurlisteDto.getCSeriennrchargennr());

					if (mengeAusInventurliste.subtract(lagerstandVeraenderung).doubleValue() < 0) {
						DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
								"Aufgrund der Artikelbewegungen zwischen Inventurdatum ("
										+ Helper.formatDatum(internalFrameArtikel.getInventurDto().getTInventurdatum(),
												LPMain.getInstance().getTheClient().getLocUi())
										+ ") und jetzt, w\u00FCrde sich ein Inventurstand von "
										+ mengeAusInventurliste.subtract(lagerstandVeraenderung).doubleValue()
										+ " ergeben. Bitte korrigieren! Buchung wurde nicht durchgef\u00FChrt.");
					} else {

						// Nachsehen, ob es schon einen Eintrag gibt, wenn ja,
						// dann auslassen und nachher Meldung anzeigen

						InventurlisteDto[] vorhandeneDtos = DelegateFactory

								.getInstance().getInventurDelegate()
								.inventurlisteFindByInventurIIdLagerIIdArtikelIIdCSeriennrchargennrOhneExc(
										inventurlisteDto.getInventurIId(), inventurlisteDto.getArtikelIId(),
										inventurlisteDto.getLagerIId(), inventurlisteDto.getCSeriennrchargennr());

						if (vorhandeneDtos == null || vorhandeneDtos.length == 0) {

							inventurlisteDto.setIId(DelegateFactory.getInstance().getInventurDelegate()
									.createInventurliste(inventurlisteDto));

						} else {
							sZeilenAusgelassen += inventurlisteDto.getCSeriennrchargennr() + "\r\n";
						}

					}
				}
				setKeyWhenDetailPanel(inventurlisteDto.getIId());

				lastLager = inventurlisteDto.getLagerIId();
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArtikelDto().getIId().toString());
				}
				eventYouAreSelected(false);
				if (sZeilenAusgelassen.length() > 0) {

					DialogFactory.showModalDialog(LPMain.getInstance().getTextRespectUISPr("lp.error"),
							LPMain.getInstance().getTextRespectUISPr("artikel.invenur.snrchnrausgelassen")
									+ sZeilenAusgelassen);
				}

			} else {

				components2Dto();

				inventurlisteDto.setCSeriennrchargennr(null);
				java.math.BigDecimal mengeAusInventurliste = inventurlisteDto.getNInventurmenge();
				java.math.BigDecimal lagerstandVeraenderung = DelegateFactory.getInstance().getLagerDelegate()
						.getLagerstandsVeraenderungOhneInventurbuchungen(inventurlisteDto.getArtikelIId(),
								inventurlisteDto.getLagerIId(),
								internalFrameArtikel.getInventurDto().getTInventurdatum(),
								new java.sql.Timestamp(System.currentTimeMillis()),
								inventurlisteDto.getCSeriennrchargennr());

				if (mengeAusInventurliste.subtract(lagerstandVeraenderung).doubleValue() < 0) {
					boolean b = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
							
							"Aufgrund der Artikelbewegungen zwischen Inventurdatum ("
									+ Helper.formatDatum(internalFrameArtikel.getInventurDto().getTInventurdatum(),
											LPMain.getInstance().getTheClient().getLocUi())
									+ ") und jetzt, w\u00FCrde sich ein Inventurstand von "
									+ mengeAusInventurliste.subtract(lagerstandVeraenderung).doubleValue()
									+ " ergeben. Wollen Sie die Buchung trotzdem druchf\u00FChren?",LPMain.getInstance().getTextRespectUISPr("lp.error"));
					
					if(b==false) {
						return;
					}
				}

				if (inventurlisteDto.getIId() == null) {

					inventurlisteDto.setInventurIId(internalFrameArtikel.getInventurDto().getIId());
					inventurlisteDto.setIId(
							DelegateFactory.getInstance().getInventurDelegate().createInventurliste(inventurlisteDto));

					setKeyWhenDetailPanel(inventurlisteDto.getIId());
				} else {
					Integer key_neu = DelegateFactory.getInstance().getInventurDelegate()
							.updateInventurliste(inventurlisteDto);
					inventurlisteDto.setIId(key_neu);
					setKeyWhenDetailPanel(inventurlisteDto.getIId());
				}
				lastLager = inventurlisteDto.getLagerIId();
				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArtikelDto().getIId().toString());
				}
				eventYouAreSelected(false);

			}
		}
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);

	}

}
