package com.lp.client.fertigung.arbeitsplanvergleich;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lp.client.frame.component.WrapperButton;
import com.lp.client.pc.LPMain;

public class UebernehmenBtn extends WrapperButton {
	private static final long serialVersionUID = 2L;
	private IUebernehmenButtonHandler handler;
	private boolean activated;

	public UebernehmenBtn() {
		setDeactivated();
		addActionListener(new UebernehmenActionHandler());
	}

	public void setDeactivated() {
		activated = false;
		setText(LPMain.getTextRespectUISPr("part.wrapperbuttonzusammenfuehren.label.links"));
	}

	public void setActivated() {
		activated = true;
		setText(LPMain.getTextRespectUISPr("part.wrapperbuttonzusammenfuehren.label.rechts"));
	}
	
	public void setHandler(IUebernehmenButtonHandler handler) {
		this.handler = handler;
	}
	
	private class UebernehmenActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(handler == null)
				return;
			if(!activated) {
				handler.activate(UebernehmenBtn.this);
			}
			else {
				handler.deactivate(UebernehmenBtn.this);
			}
		}
		
	}

}