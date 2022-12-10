package com.lp.client.rechtschreibung;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import com.lp.client.frame.component.WrapperComboBox;
import com.lp.util.Helper;

/**
 * ActionListener f&uuml;r eine {@link WrapperComboBox}, die das Locale f&uuml;r
 * ein Rechtschreibpr&uuml;fbares Objekt bestimmt.
 * 
 * @author Alexander Daum
 *
 */
public class RSLocaleChangeListener implements ActionListener {
	private final IRechtschreibPruefbar component;

	public RSLocaleChangeListener(IRechtschreibPruefbar component) {
		this.component = component;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof WrapperComboBox) {
			WrapperComboBox box = (WrapperComboBox) e.getSource();
			Object selected = box.getKeyOfSelectedItem();
			if (selected != null) {
				Locale newLocale = Helper.string2Locale(selected.toString());
				component.setRechtschreibpruefungLocale(newLocale);
			}
		}
	}

}
