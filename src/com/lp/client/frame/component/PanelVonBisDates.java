package com.lp.client.frame.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Date;

import javax.swing.JPanel;

import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.pc.LPMain;

public class PanelVonBisDates extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 7698738810878344346L;
	
	private WrapperDateField wdfVon;
	private WrapperDateField wdfBis;
	private WrapperLabel wlaBis;
	
	private boolean hideBisIfVonIsNull = false;
	
	public PanelVonBisDates() {
		jbInit();
	}
	
	private void jbInit() {
		wdfVon = new WrapperDateField();
		wdfVon.addPropertyChangeListener(this);
		wdfBis = new WrapperDateField();
		wdfBis.addPropertyChangeListener(this);
		
		wlaBis = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bis.lowercase"));
		
		HvLayout hvLayout = HvLayoutFactory.createWithoutInset(this);
		hvLayout.add(wdfVon, 120)
			.add(wlaBis, 20)
			.add(wdfBis, 120);
	}
	
	/**
	 * Bewirkt, dass wenn das Von-Datum auf <code>null</code> gesetzt wird, das
	 * Bis-Datum ebenfalls auf <code>null</code> gesetzt wird und die Komponenten
	 * dazu verborgen werden.
	 */
	public void hideBisIfVonIsNull() {
		hideBisIfVonIsNull = true;
	}
	
	private void setVisability() {
		if (!hideBisIfVonIsNull) return;
		
		boolean isBisVisible = getDateVon() != null;
		wlaBis.setVisible(isBisVisible);
		wdfBis.setVisible(isBisVisible);
	}
	
	public Date getDateVon() {
		return wdfVon.getDate();
	}

	public void setDateVon(Date von) {
		wdfVon.setDate(von);
	}
	
	public Date getDateBis() {
		return wdfBis.getDate();
	}
	
	public void setDateBis(Date bis) {
		wdfBis.setDate(bis);
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wdfVon.setEnabled(enabled);
		wdfBis.setEnabled(enabled);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (hasDateChanged(evt, wdfVon)) {
			processVonChanged();
		
		} else if (hasDateChanged(evt, wdfBis)) {
			processBisChanged();
		}
	}

	private void processVonChanged() {
		Date von = wdfVon.getDate();
		if (hideBisIfVonIsNull && von == null) {
			wdfBis.setDate(null);
		} else {
			wdfBis.setMinimumValue(von);
		}
		setVisability();
	}
	
	private void processBisChanged() {
		wdfVon.setMaximumValue(wdfBis.getDate());
	}

	private boolean hasDateChanged(PropertyChangeEvent evt, WrapperDateField dateField) {
		return evt.getSource() == dateField
				&& "date".equals(evt.getPropertyName());
	}
}
