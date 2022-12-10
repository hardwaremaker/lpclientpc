package com.lp.client.fertigung.arbeitsplanvergleich;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosArbeitsplanZeitVergleichDto;

public class PanelDialogLosArbeitsplanVergleich extends PanelDialog {
	private static final long serialVersionUID = 1L;

	public static final String ACTION_SPECIAL_OK = "action_" + ALWAYSENABLED + "ok";
	private static final String iconFuerOKButton = "/com/lp/client/res/check2.png";

	private PanelLosArbeitsplanMaschinenVergleich maschinenVergleich;
	private PanelLosArbeitsplanZeitvergleich zeitvergleich;

	private JTabbedPane tabPane;

	public PanelDialogLosArbeitsplanVergleich(InternalFrame internalFrame, String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		createAndSaveAndShowButton(iconFuerOKButton, LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.ok"),
				ACTION_SPECIAL_OK, KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK), null);
		maschinenVergleich = new PanelLosArbeitsplanMaschinenVergleich();
		zeitvergleich = new PanelLosArbeitsplanZeitvergleich();
		tabPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabPane.addTab(LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.tabmaschine"), maschinenVergleich);
		tabPane.addTab(LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.tabzeit"), zeitvergleich);
		jpaWorkingOn.add(tabPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}

	/**
	 * Setze Daten des Vergleichs.
	 * @param vergleichDaten
	 * @return true wenn Daten verarbeitet wurden, false wenn keine passenden Daten vorhanden sind
	 * @throws Throwable
	 */
	public boolean setData(List<LosArbeitsplanZeitVergleichDto> vergleichDaten) throws Throwable {
		LockStateValue lockStateValue = getLockedstateDetailMainKey();
		lockStateValue.setIState(LOCK_NO_LOCKING);
		updateButtons(lockStateValue);
		maschinenVergleich.setData(vergleichDaten);
		zeitvergleich.setData(vergleichDaten);
		setTabPaneDefaults();
		if(!maschinenVergleich.hasData()) {
			tabPane.setSelectedComponent(zeitvergleich);
			tabPane.setEnabledAt(0, false);
		}
		if(!zeitvergleich.hasData()) {
			tabPane.setSelectedComponent(maschinenVergleich);
			tabPane.setEnabledAt(1, false);
		}
		tabPane.revalidate();
		return maschinenVergleich.hasData() || zeitvergleich.hasData();
	}

	private void setTabPaneDefaults() {
		tabPane.setSelectedComponent(maschinenVergleich);
		tabPane.setEnabledAt(0, true);
		tabPane.setEnabledAt(1, true);
	}

	private void doSave() throws Throwable {
		DelegateFactory.getInstance().getFertigungDelegate()
				.uebernehmeNeueArbeitsgangMaschinen(maschinenVergleich.getUebernommeneDtos());
		DelegateFactory.getInstance().getFertigungDelegate()
				.uebernehmeNeueSollzeiten(zeitvergleich.getUebernommeneDtos());
	}

	@Override
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			doSave();
			getInternalFrame().fireItemChanged(new ItemChangedEvent(this, ItemChangedEvent.ITEM_CHANGED));
			getInternalFrame().closePanelDialog();
		}
	}

	@Override
	protected void onEscOrClosePanelDialog() throws Throwable {
		boolean changes = !maschinenVergleich.getUebernommeneDtos().isEmpty();
		boolean reallyClose = !changes;
		if (changes) {
			reallyClose = DialogFactory.showModalJaNeinDialog(getInternalFrame(),
					LPMain.getTextRespectUISPr("lp.verwerfen.text"), LPMain.getTextRespectUISPr("lp.warning"));
		}
		if (reallyClose) {
			getInternalFrame().closePanelDialog();
		}
	}

}