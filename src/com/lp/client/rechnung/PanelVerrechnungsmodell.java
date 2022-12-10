package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.rechnung.service.VerrechnungsmodellDto;

@SuppressWarnings("static-access")
public class PanelVerrechnungsmodell extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerrechnungsmodellDto verrechnungsmodellDto = null;
	private InternalFrameRechnung internalFrameRechnung = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = new JPanel();
	private GridBagLayout gridBagLayoutWorkingPanel = new GridBagLayout();
	private GridBagLayout gridBagLayoutAll = new GridBagLayout();

	private WrapperLabel wlaBezeichnung = new WrapperLabel();

	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();

	private WrapperCheckBox wcbPreiseAusAuftrag = new WrapperCheckBox();

	private WrapperCheckBox wcbNachArtikelVerdichten = new WrapperCheckBox();

	
	private WrapperIdentField wifReisespesen = new WrapperIdentField(getInternalFrame(), this);
	private WrapperIdentField wifReisekilometer = new WrapperIdentField(getInternalFrame(), this);
	private WrapperIdentField wifTelefon = new WrapperIdentField(getInternalFrame(), this);

	private WrapperIdentField wifER = new WrapperIdentField(getInternalFrame(), this);

	private WrapperNumberField wnfAufschlag = new WrapperNumberField();

	public InternalFrameRechnung getInternalFrameRechnung() {
		return internalFrameRechnung;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	public PanelVerrechnungsmodell(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameRechnung = (InternalFrameRechnung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI) throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);
		// leereAlleFelder(this);
		if (!getKeyWhenDetailPanel().equals(LPMain.getLockMeForNew())) {

			verrechnungsmodellDto = DelegateFactory.getInstance().getRechnungDelegate()
					.verrechnungsmodellFindByPrimaryKey(getInternalFrameRechnung().getVerrechnungsmodellDto().getIId());

			dto2Components();

			getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE,
					getInternalFrameRechnung().getVerrechnungsmodellDto().getCBez());
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

	}

	protected void dto2Components() throws Throwable {

		wtfBezeichnung.setText(verrechnungsmodellDto.getCBez());

		wcbVersteckt.setShort(verrechnungsmodellDto.getBVersteckt());

		wnfAufschlag.setDouble(verrechnungsmodellDto.getFAufschlagEr());
		wifER.setArtikelIId(verrechnungsmodellDto.getArtikelIIdEr());
		wifReisekilometer.setArtikelIId(verrechnungsmodellDto.getArtikelIIdReisekilometer());
		wifReisespesen.setArtikelIId(verrechnungsmodellDto.getArtikelIIdReisespesen());
		wifTelefon.setArtikelIId(verrechnungsmodellDto.getArtikelIIdTelefon());
		wcbPreiseAusAuftrag.setShort(verrechnungsmodellDto.getBPreiseAusAuftrag());
		wcbNachArtikelVerdichten.setShort(verrechnungsmodellDto.getBNachArtikelVerdichten());

	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		getInternalFrame().addItemChangedListener(this);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));

		wtfBezeichnung.setText("");

		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr("lp.versteckt"));

		wcbPreiseAusAuftrag
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.preiseausauftrag"));
		
		wcbNachArtikelVerdichten
		.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.nachartikelverdichten"));

		wifER.getWbuArtikel().setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.er"));
		wifReisekilometer.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.reisekilometer"));
		wifReisespesen.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.reisespesen"));
		wifTelefon.getWbuArtikel()
				.setText(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.telefonzeiten"));
		wnfAufschlag.setMandatoryField(true);
		wtfBezeichnung.setMandatoryField(true);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		iZeile = 0;

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 7, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(
				new JLabel(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.verrechnungsartikel")),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(20, 2, 5, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wifER.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifER.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifER.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.6, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifER.getWtfZusatzBezeichnung(), new GridBagConstraints(3, iZeile, 1, 1, 0.4, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getInstance().getTextRespectUISPr("rech.verrechnungsmodell.aufschlag.er")),
				new GridBagConstraints(4, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAufschlag, new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), -100, 0));
		iZeile++;

		jpaWorkingOn.add(wifTelefon.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifTelefon.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.3, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifTelefon.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifTelefon.getWtfZusatzBezeichnung(), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wifReisespesen.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifReisespesen.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifReisespesen.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifReisespesen.getWtfZusatzBezeichnung(), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wifReisekilometer.getWbuArtikel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifReisekilometer.getWtfIdent(), new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifReisekilometer.getWtfBezeichnung(), new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wifReisekilometer.getWtfZusatzBezeichnung(), new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbPreiseAusAuftrag, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbNachArtikelVerdichten, new GridBagConstraints(1, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;

		if (DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {

			jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

		iZeile++;

		this.add(getPanelStatusbar(), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		verrechnungsmodellDto = new VerrechnungsmodellDto();

		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VERRECHNUNGSMODELL;
	}

	protected void setDefaults() throws Throwable {

	}

	protected void components2Dto() throws Throwable {
		verrechnungsmodellDto.setCBez(wtfBezeichnung.getText());
		verrechnungsmodellDto.setMandantCNr(LPMain.getTheClient().getMandant());

		verrechnungsmodellDto.setBVersteckt(wcbVersteckt.getShort());
		verrechnungsmodellDto.setFAufschlagEr(wnfAufschlag.getDouble());

		verrechnungsmodellDto.setArtikelIIdEr(wifER.getArtikelIId());
		verrechnungsmodellDto.setArtikelIIdReisekilometer(wifReisekilometer.getArtikelIId());
		verrechnungsmodellDto.setArtikelIIdReisespesen(wifReisespesen.getArtikelIId());
		verrechnungsmodellDto.setArtikelIIdTelefon(wifTelefon.getArtikelIId());
		verrechnungsmodellDto.setBPreiseAusAuftrag(wcbPreiseAusAuftrag.getShort());
		verrechnungsmodellDto.setBNachArtikelVerdichten(wcbNachArtikelVerdichten.getShort());

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getRechnungDelegate().removeVerrechnungsmodell(verrechnungsmodellDto);
		super.eventActionDelete(e, true, true);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (verrechnungsmodellDto.getIId() == null) {
				verrechnungsmodellDto.setIId(DelegateFactory.getInstance().getRechnungDelegate()
						.createVerrechnungsmodell(verrechnungsmodellDto));
				setKeyWhenDetailPanel(verrechnungsmodellDto.getIId());
				internalFrameRechnung.setVerrechnungsmodellDto(verrechnungsmodellDto);
			} else {
				DelegateFactory.getInstance().getRechnungDelegate().updateVerrechnungsmodell(verrechnungsmodellDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(verrechnungsmodellDto.getIId().toString());
			}
			eventYouAreSelected(false);
			verrechnungsmodellDto = DelegateFactory.getInstance().getRechnungDelegate()
					.verrechnungsmodellFindByPrimaryKey(verrechnungsmodellDto.getIId());
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		}

	}
}
