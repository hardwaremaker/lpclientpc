
package com.lp.client.system.automatik;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;

public class PanelAutoJobMonatsabrechnungversandAbteilungen implements IPanelAutoJobDetails {

	private AutoJobMonatsabrechnungversandAbteilungenCtrl panelCtrl;

	private ButtonGroup bg = new ButtonGroup();

	private WrapperRadioButton wrbWoechentlich = null;
	private WrapperComboBox wcbWochentag = null;

	private WrapperRadioButton wrbMonatlich = null;

	public PanelAutoJobMonatsabrechnungversandAbteilungen(AutoJobMonatsabrechnungversandAbteilungenCtrl panelCtrl) {
		setPanelCtrl(panelCtrl);
	}

	public void setPanelCtrl(AutoJobMonatsabrechnungversandAbteilungenCtrl panelCtrl) {
		this.panelCtrl = panelCtrl;
	}

	public AutoJobMonatsabrechnungversandAbteilungenCtrl getPanelCtrl() {
		return panelCtrl;
	}

	private void initComponents() throws Throwable {

		wrbWoechentlich = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("lp.system.automatik.monatsabrechnungversand.woechentlich"));

		wrbMonatlich = new WrapperRadioButton(
				LPMain.getTextRespectUISPr("lp.system.automatik.monatsabrechnungversand.monatlich"));

		bg.add(wrbWoechentlich);
		bg.add(wrbMonatlich);

		wcbWochentag = new WrapperComboBox();
		wcbWochentag.setMandatoryField(true);
		DateFormatSymbols symbols = new DateFormatSymbols(LPMain.getInstance().getUISprLocale());
		String[] defaultMonths = symbols.getWeekdays();
		Map<Integer, String> m = new TreeMap<Integer, String>();
		for (int i = 1; i < defaultMonths.length; i++) {
			m.put(new Integer(i), defaultMonths[i]);

		}

		wcbWochentag.setMap(m, false);
		
		
		wrbWoechentlich.setSelected(true);
		wcbWochentag.setKeyOfSelectedItem(Calendar.SATURDAY);
		
	}

	@Override
	public Integer installComponents(JPanel jPanelWorkingOn, Integer iZeile) throws Throwable {
		initComponents();

		jPanelWorkingOn.add(wrbWoechentlich, new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcbWochentag, new GridBagConstraints(3, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jPanelWorkingOn.add(wrbMonatlich, new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		return ++iZeile;
	}

	@Override
	public void loadDetails() throws Throwable {

		Integer iMonatlich = getPanelCtrl().getJobDetailsDto().getBMonatlich();
		Integer iWochentag = getPanelCtrl().getJobDetailsDto().getIWochentag();
		wcbWochentag.setKeyOfSelectedItem(iWochentag);

		if (iMonatlich!=null && iMonatlich == 1) {
			wrbMonatlich.setSelected(true);
		} else if (iMonatlich!=null && iMonatlich == 0) {
			wrbWoechentlich.setSelected(true);
		}

	}

	@Override
	public void saveDetails() throws Throwable {

		if (wrbMonatlich.isSelected()) {
			getPanelCtrl().getJobDetailsDto().setBMonatlich(1);
			getPanelCtrl().getJobDetailsDto().setIWochentag(null);
		} else {
			getPanelCtrl().getJobDetailsDto().setBMonatlich(0);
			getPanelCtrl().getJobDetailsDto().setIWochentag((Integer) wcbWochentag.getKeyOfSelectedItem());
		}

		getPanelCtrl().save();
	}

}
