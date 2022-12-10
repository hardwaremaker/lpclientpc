package com.lp.client.frame.component;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class JButtonWithData<T> extends JButton {

	private static final long serialVersionUID = -1242339322733855991L;

	private T data;
	
	public JButtonWithData() {
	}

	public JButtonWithData(Icon icon, T data) {
		super(icon);
		setData(data);
	}

	public JButtonWithData(String name, T data) {
		super(name);
		setData(data);
	}

	public JButtonWithData(Action a, T data) {
		super(a);
		setData(data);
	}

	public JButtonWithData(String text, Icon icon, T data) {
		super(text, icon);
		setData(data);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
