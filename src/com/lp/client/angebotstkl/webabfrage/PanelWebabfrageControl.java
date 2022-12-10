package com.lp.client.angebotstkl.webabfrage;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class PanelWebabfrageControl extends JPanel implements IWebabfrageControlListener {

	private static final long serialVersionUID = 8606661469933562208L;
	public static final String ACTION_COMBOBOX_MENGENSTAFFEL = "action_combobox_mengenstaffel_auswahl";
	public static final String ACTION_BUTTON_ABFRAGEN = "action_button_abfrage_starten";
	public static final String ACTION_BUTTON_ABFRAGE_ABBRECHEN = "action_button_abfrage_abbrechen";
	public static final String ACTION_RADIOBUTTON_ALLE = "action_radiobutton_abfrage_alle";
	public static final String ACTION_RADIOBUTTON_SELEKTIERTE = "action_radiobutton_abfrage_selektierte";
	
	private Map<Integer, BigDecimal> mengenstaffel;
	private WrapperComboBox wcbMengenstaffel;
	private WrapperRadioButton wrbAlle;
	private WrapperRadioButton wrbSelektierte;
	private ButtonGroup rbGroup;
	private WrapperButton wbAbfrage;
	private WrapperButton wbAbbrechen;
	private ActionListener actionListener;

	public PanelWebabfrageControl(ActionListener actionListener, Map<Integer, BigDecimal> mengenstaffel) {
		this.actionListener = actionListener;
		this.mengenstaffel = mengenstaffel;
		
		jbInit();
		setDefaults();
	}
	
	private void setDefaults() {
		wbAbfrage.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.abfragestarten"));
	}

	private void jbInit() {
		initComboboxMengenstaffel();
		
		wrbAlle = new WrapperRadioButton(LPMain.getTextRespectUISPr("lp.alle"));
		wrbAlle.addActionListener(actionListener);
		wrbAlle.setActionCommand(ACTION_RADIOBUTTON_ALLE);
		wrbSelektierte = new WrapperRadioButton(LPMain.getTextRespectUISPr("agstkl.webabfrage.selektierte"));
		wrbSelektierte.addActionListener(actionListener);
		wrbSelektierte.setActionCommand(ACTION_RADIOBUTTON_SELEKTIERTE);
		wrbSelektierte.setSelected(true);
		rbGroup = new ButtonGroup();
		rbGroup.add(wrbAlle);
		rbGroup.add(wrbSelektierte);
		
		wbAbfrage = new WrapperButton();
		wbAbfrage.setActionCommand(ACTION_BUTTON_ABFRAGEN);
		wbAbfrage.addActionListener(actionListener);
		wbAbbrechen = new WrapperButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		wbAbbrechen.setActionCommand(ACTION_BUTTON_ABFRAGE_ABBRECHEN);
		wbAbbrechen.addActionListener(actionListener);
		
		setLayout(new MigLayout("", "[15%,fill,grow][15%,fill,grow]50[15px][30px][25%, fill,grow]", "[fill]"));
		add(new WrapperLabel(LPMain.getTextRespectUISPr("agstkl.webabfrage.gewaehltemenge")));
		add(wcbMengenstaffel);
		add(wrbAlle);
		add(wrbSelektierte);
		add(wbAbfrage);
//		add(wbAbbrechen);
	}

	/**
	 * 
	 */
	private void initComboboxMengenstaffel() {
//		List<String> sMengenstaffel = new ArrayList<String>();
		
//		for (BigDecimal menge : mengenstaffel) {
//			sMengenstaffel.add(Helper.formatZahl(menge, WebabfrageBaseModel.getIUINachkommastellenMenge(), LPMain.getInstance().getUISprLocale()));
//		}
		
		wcbMengenstaffel = new WrapperComboBox();
		wcbMengenstaffel.setMandatoryField(true);
		wcbMengenstaffel.setMap(mengenstaffel);
		wcbMengenstaffel.setMandatoryField(false);
//		((JLabel)wcbMengenstaffel.getRenderer()).setHorizontalAlignment(SwingConstants.RIGHT);
//		wcbMengenstaffel.setSelectedIndex(0);
		wcbMengenstaffel.setActionCommand(ACTION_COMBOBOX_MENGENSTAFFEL);
		wcbMengenstaffel.addActionListener(actionListener);
	}

	public Boolean isAlleAbfragenSelected() {
		return wrbAlle.isSelected();
	}
	
	public Boolean isSelektierteAbfragenSelected() {
		return wrbSelektierte.isSelected();
	}

	@Override
	public void updateAbfrageStarted() {
		wbAbfrage.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.abfragelaeuft"));
		validate();
	}

	@Override
	public void updateAbfrageCompleted() {
		wbAbfrage.setText(LPMain.getTextRespectUISPr("agstkl.webabfrage.abfragestarten"));
		validate();
	}

	public Integer getSelectedMengenstaffel() {
		return (Integer) wcbMengenstaffel.getKeyOfSelectedItem();
	}
}
