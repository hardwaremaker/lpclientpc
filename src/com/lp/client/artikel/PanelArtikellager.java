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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.lp.client.angebotstkl.PanelEkaglieferantOptimieren;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDtoFuerOptimieren;
import com.lp.server.angebotstkl.service.EkagLieferantoptimierenDto;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellagerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungartDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelArtikellager extends PanelBasis {

	/**
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaLager = new WrapperLabel();
	private WrapperTextField wtfLager = new WrapperTextField();
	private ArtikellagerDto artikellagerDto = null;
	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperNumberField wnfPreis = new WrapperNumberField();
	private WrapperButton wbuLager = new WrapperButton();
	JTable jTableSnrChnrs = new JTable();
	JScrollPane scrollPane = new JScrollPane();

	private WrapperLabel wlaLagermindest = new WrapperLabel();
	private WrapperNumberField wnfLagermindest = new WrapperNumberField();
	private WrapperLabel wlaLagersoll = new WrapperLabel();
	private WrapperNumberField wnfLagersoll = new WrapperNumberField();

	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";

	public final static String MY_OWN_NEW_CHARGE_WEGWERFEN = PanelBasis.ACTION_MY_OWN_NEW + "CHARGE_WEGWERFEN";

	private WrapperGotoButton wbuAuftrag = new WrapperGotoButton(
			LPMain.getTextRespectUISPr("artikel.lager.gotoauftrag"), com.lp.util.GotoHelper.GOTO_AUFTRAG_AUSWAHL);

	static final public String GOTO_AUFTRAG = LEAVEALONE + "GOTO_AUFTRAG";

	private PanelQueryFLR panelQueryFLRLager = null;
	private String[] colNames = new String[] {
			LPMain.getInstance().getTextRespectUISPr("artikel.handlagerbewegung.seriennrchargennr"),
			LPMain.getInstance().getTextRespectUISPr("lp.lagerstand"),
			LPMain.getInstance().getTextRespectUISPr("artikel.lager.version"),
			LPMain.getInstance().getTextRespectUISPr("artikel.gebinde"),
			LPMain.getInstance().getTextRespectUISPr("artikel.gebindemenge"),
			LPMain.getInstance().getTextRespectUISPr("artikel.lager.auftragsnr") };

	public PanelArtikellager(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaPreis.setVisible(false);
			wnfPreis.setVisible(false);
		}
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {

		ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate()
				.artikelFindByPrimaryKey(internalFrameArtikel.getArtikelDto().getIId());
		if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
			panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
					artikellagerDto.getLagerIId(), false, false);
		} else {
			panelQueryFLRLager = ArtikelFilterFactory.getInstance().createPanelFLRLager(getInternalFrame(),
					artikellagerDto.getLagerIId(), false, true);

		}
		new DialogQuery(panelQueryFLRLager);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfPreis;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		artikellagerDto = new ArtikellagerDto();
	}

	protected void dto2Components() throws Throwable {
		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(artikellagerDto.getLagerIId());
		wtfLager.setText(lagerDto.getCNr());
		wnfPreis.setBigDecimal(artikellagerDto.getNGestehungspreis());

		wnfLagermindest.setDouble(artikellagerDto.getFLagermindest());
		wnfLagersoll.setDouble(artikellagerDto.getFLagersoll());

		if (Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBSeriennrtragend())
				|| Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBChargennrtragend())) {
			SeriennrChargennrAufLagerDto[] s = DelegateFactory.getInstance().getLagerDelegate()
					.getAllSerienChargennrAufLagerInfoDtos(artikellagerDto.getArtikelIId(),
							artikellagerDto.getLagerIId());

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
					.getParametermandant(ParameterFac.PARAMETER_AUFTRAG_SERIENNUMMERN,
							ParameterFac.KATEGORIE_AUFTRAG, LPMain.getTheClient().getMandant());
			boolean bAuftragSnr =(Boolean) parameter.getCWertAsObject();

			Object[][] data = new Object[s.length][6];
			for (int i = 0; i < s.length; i++) {
				if (s[i] != null) {
					data[i][0] = s[i].getCSeriennrChargennr();
					data[i][1] = s[i].getNMenge();
					data[i][2] = s[i].getCVersion();
					data[i][3] = s[i].getSGebinde();
					data[i][4] = s[i].getBdGebindemenge();

					if (bAuftragSnr) {

						data[i][5] = DelegateFactory.getInstance().getAuftragpositionDelegate()
								.getAuftragZuAuftragseriennummer(artikellagerDto.getArtikelIId(),
										s[i].getCSeriennrChargennr());
					}

				}
			}
			jTableSnrChnrs = new JTable(data, colNames);

			jTableSnrChnrs.setDefaultRenderer(Object.class, new TableCellRenderer() {
				private DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {

					if (value instanceof BigDecimal) {
						BigDecimal d = (BigDecimal) value;
						try {
							if (d.signum() != 0) {
								value = Helper.formatZahl(d, Defaults.getInstance().getIUINachkommastellenMenge(),
										Defaults.getInstance().getLocUI());
							}
						} catch (Throwable e) {
							handleException(e, true);
						}

						DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.RIGHT);
					} else {
						DEFAULT_RENDERER.setHorizontalAlignment(SwingConstants.LEFT);
					}

					Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus,
							row, column);

					return c;
				}

			});

			scrollPane.getViewport().add(jTableSnrChnrs);

		} else {
			Object[][] data = new Object[0][2];
			jTableSnrChnrs = new JTable(data, colNames);
		}
		scrollPane.getViewport().add(jTableSnrChnrs);
	}

	protected void components2Dto() throws Throwable {
		artikellagerDto.setNGestehungspreis(wnfPreis.getBigDecimal());
		artikellagerDto.setArtikelIId(internalFrameArtikel.getArtikelDto().getIId());
		artikellagerDto.setMandantCNr(LPMain.getInstance().getTheClient().getMandant());
		artikellagerDto.setFLagermindest(wnfLagermindest.getDouble());
		artikellagerDto.setFLagersoll(wnfLagersoll.getDouble());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);

				try {
					DelegateFactory.getInstance().getLagerDelegate().artikellagerFindByPrimaryKey(
							internalFrameArtikel.getArtikelDto().getIId(), lagerDto.getIId());
					DialogFactory.showModalDialog("Fehler", "Es gibt bereits einen Eintrag zu diesem Lager.");
					return;
				} catch (ExceptionLP ex) {
					// Wenn nicht, normal weiter

				}

				wtfLager.setText(lagerDto.getCNr());
				artikellagerDto.setLagerIId(lagerDto.getIId());
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

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate()
				.getParametermandant(ParameterFac.PARAMETER_LAGERMIN_JE_LAGER, ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getTheClient().getMandant());
		boolean bLagerminJeLager = (Boolean) parameter.getCWertAsObject();

		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr("button.lager"));
		wbuLager.setActionCommand(PanelHandlagerbewegung.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wlaLager.setText(LPMain.getInstance().getTextRespectUISPr("label.lager"));
		wtfLager.setColumnsMax(ArtikelFac.MAX_KATALOG_KATALOG);
		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr("lp.gestehungspreis"));

		wlaLagermindest.setText(LPMain.getInstance().getTextRespectUISPr("artikel.lagermindeststand"));
		wlaLagersoll.setText(LPMain.getInstance().getTextRespectUISPr("artikel.lagersollstand"));

		int iNachkommastellen = Defaults.getInstance().getIUINachkommastellenPreiseEK();
		wnfPreis.setFractionDigits(iNachkommastellen);
		wnfPreis.setMandatoryField(true);

		wnfLagermindest.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
		wnfLagersoll.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());
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
		jpaWorkingOn.add(wlaLager, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPreis, new GridBagConstraints(2, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPreis, new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (bLagerminJeLager) {
			jpaWorkingOn.add(wlaLagermindest, new GridBagConstraints(0, 1, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfLagermindest, new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaLagersoll, new GridBagConstraints(2, 1, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfLagersoll, new GridBagConstraints(3, 1, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

		scrollPane.getViewport().add(jTableSnrChnrs);
		jpaWorkingOn.add(scrollPane, new GridBagConstraints(0, 2, 4, 1, 0, 1.0, GridBagConstraints.NORTH,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_CHARGENNUMMERN)) {

			getToolBar().addButtonRight("/com/lp/client/res/garbage.png",
					LPMain.getTextRespectUISPr("artikel.chargennummer.wegwerfen.button"), MY_OWN_NEW_CHARGE_WEGWERFEN,
					null, RechteFac.RECHT_WW_HANDLAGERBEWEGUNG_CUD);

		}

		boolean bDarfGestpreiseaendern = false;
		try {
			bDarfGestpreiseaendern = DelegateFactory.getInstance().getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_ARTIKEL_GESTPREISE_CU);
		} catch (Throwable ex) {
			handleException(ex, true);
		}

		String[] aWhichButtonIUse = null;
		if (bDarfGestpreiseaendern == true) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE, ACTION_DISCARD, };
		} else {
			aWhichButtonIUse = new String[] {};
		}

		getToolBar().addButtonLeft("/com/lp/client/res/data_into.png",
				LPMain.getTextRespectUISPr("proj.projektverlauf.gehezubeleg"), GOTO_AUFTRAG,
				KeyStroke.getKeyStroke('G', java.awt.event.InputEvent.CTRL_MASK), null);

		enableToolsPanelButtons(true, GOTO_AUFTRAG);
		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			Object[][] data = new Object[0][3];
			jTableSnrChnrs = new JTable(data, colNames);
			jTableSnrChnrs.setDefaultRenderer(BigDecimal.class, HelperClient.getBigDecimalRenderer());

			scrollPane.getViewport().add(jTableSnrChnrs);

			leereAlleFelder(this);
			clearStatusbar();

			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				jpaWorkingOn.remove(wlaLager);
				jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				wbuLager.setEnabled(true);

				if (Helper.short2boolean(internalFrameArtikel.getArtikelDto().getBLagerbewirtschaftet())) {
					// CK Projekt 8688
					LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

					try {
						DelegateFactory.getInstance().getLagerDelegate().artikellagerFindByPrimaryKey(
								internalFrameArtikel.getArtikelDto().getIId(), lagerDto.getIId());
					} catch (ExceptionLP ex) {
						wtfLager.setText(lagerDto.getCNr());
						artikellagerDto.setLagerIId(lagerDto.getIId());
					}
				} else {
					LagerDto tempLager = DelegateFactory.getInstance().getLagerDelegate()
							.lagerFindByCNrByMandantCNr(LagerFac.LAGER_KEINLAGER);
					wtfLager.setText(tempLager.getCNr());
					artikellagerDto.setLagerIId(tempLager.getIId());
				}

			}

		} else {
			jpaWorkingOn.remove(wbuLager);
			jpaWorkingOn.add(wlaLager, new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

			WwArtikellagerPK pk = (WwArtikellagerPK) key;

			artikellagerDto = DelegateFactory.getInstance().getLagerDelegate()
					.artikellagerFindByPrimaryKey(pk.getArtikel_i_id(), pk.getLager_i_id());
			dto2Components();
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		} else if (e.getActionCommand().equals(MY_OWN_NEW_CHARGE_WEGWERFEN)) {
			actionChargeWegwerfen();
			eventYouAreSelected(false);
//			if (jTableSnrChnrs.getRowCount() > 0
//					&& jTableSnrChnrs.getSelectedRow() >= 0) {
//				String chnr = (String) jTableSnrChnrs.getValueAt(
//						jTableSnrChnrs.getSelectedRow(), 0);
//
//				ArrayList<LosistmaterialDto> losistmaterialDtos = DelegateFactory
//						.getInstance()
//						.getLagerDelegate()
//						.getAlleBetroffenenLosistmaterialIIdsEinerArtikelIIdUndCharge(
//								artikellagerDto.getArtikelIId(), chnr, true);
//
//				if (losistmaterialDtos == null
//						|| losistmaterialDtos.size() == 0) {
//					// Hinweis
//
//					DialogFactory
//							.showModalDialog(
//									LPMain.getTextRespectUISPr("lp.info"),
//									LPMain.getTextRespectUISPr("artikel.chargennummer.wegwerfen.hinweis.handlagerbewegung"));
//				}
//
//				DelegateFactory
//						.getInstance()
//						.getLagerDelegate()
//						.chargennummerWegwerfen(
//								artikellagerDto.getArtikelIId(), chnr,
//								losistmaterialDtos);
//
//				if (losistmaterialDtos != null && losistmaterialDtos.size() > 0) {
//
//					String lose = "";
//
//					for (int i = 0; i < losistmaterialDtos.size(); i++) {
//
//						LossollmaterialDto lossollmaterialDto = DelegateFactory
//								.getInstance()
//								.getFertigungDelegate()
//								.lossollmaterialFindByPrimaryKey(
//										losistmaterialDtos.get(i)
//												.getLossollmaterialIId());
//
//						LosDto lDto = DelegateFactory
//								.getInstance()
//								.getFertigungDelegate()
//								.losFindByPrimaryKey(
//										lossollmaterialDto.getLosIId());
//
//						lose += lDto.getCNr();
//
//						if (i != losistmaterialDtos.size() - 1) {
//							lose += ",";
//						}
//
//					}
//					DialogFactory
//							.showModalDialog(
//									LPMain.getTextRespectUISPr("lp.info"),
//									LPMain.getTextRespectUISPr("artikel.chargennummer.wegwerfen.hinweis.verteilt")
//											+ lose);
//				}
//
//				eventYouAreSelected(false);
//			}

		} else if (e.getActionCommand().equals(GOTO_AUFTRAG)) {

			if (jTableSnrChnrs.getColumnCount() == 6) {

				int iZeileCursor = jTableSnrChnrs.getSelectionModel().getAnchorSelectionIndex();
				if (iZeileCursor >= 0) {
					Object key = jTableSnrChnrs.getValueAt(iZeileCursor, 5);

					if (key != null && key instanceof String) {

						wbuAuftrag.setOKey(key);
						wbuAuftrag.actionPerformed(new ActionEvent(wbuAuftrag, 0, WrapperGotoButton.ACTION_GOTO));
					}
				}
			}
		}
	}

	private void actionChargeWegwerfen() throws ExceptionLP, Throwable {
		if (!(jTableSnrChnrs.getRowCount() > 0 && jTableSnrChnrs.getSelectedRow() >= 0))
			return;

		String chnr = (String) jTableSnrChnrs.getValueAt(jTableSnrChnrs.getSelectedRow(), 0);

		List<LosDto> betroffeneLose = DelegateFactory.getInstance().getLagerDelegate()
				.getAlleBetroffenenLoseEinerArtikelIIdUndCharge(artikellagerDto.getArtikelIId(), chnr);

		if (betroffeneLose == null || betroffeneLose.isEmpty()) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("artikel.chargennummer.wegwerfen.hinweis.handlagerbewegung"));
		}

		betroffeneLose = DelegateFactory.getInstance().getLagerDelegate()
				.chargennummerWegwerfen(artikellagerDto.getArtikelIId(), chnr, betroffeneLose);

		if (betroffeneLose != null && !betroffeneLose.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < betroffeneLose.size(); i++) {
				builder.append(betroffeneLose.get(i).getCNr());
				if (i < betroffeneLose.size() - 1)
					builder.append(", ");
			}
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.info"),
					LPMain.getTextRespectUISPr("artikel.chargennummer.wegwerfen.hinweis.verteilt")
							+ builder.toString());
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getLagerDelegate().updateGestpreisArtikellager(artikellagerDto);

			setKeyWhenDetailPanel(new WwArtikellagerPK(artikellagerDto.getArtikelIId(), artikellagerDto.getLagerIId()));

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(internalFrameArtikel.getArtikelDto().getIId().toString());
			}
			eventYouAreSelected(false);
		}
	}

	class MyBigDecimalCellRenderer extends DefaultTableCellRenderer {

		EkagLieferantoptimierenDto optDto = null;

		public MyBigDecimalCellRenderer(EkagLieferantoptimierenDto optDto) {
			this.optDto = optDto;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			EinkaufsangebotpositionDtoFuerOptimieren ekagPosDto = optDto.getPos().get(row);

			if (isSelected) {
				c.setBackground(new Color(187, 240, 236));
			}

			return c;

		}

		@Override
		public int getHorizontalAlignment() {
			return SwingConstants.RIGHT;
		}

	}

}
