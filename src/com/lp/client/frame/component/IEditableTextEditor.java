package com.lp.client.frame.component;

import com.lp.editor.util.TextBlockOverflowException;

public interface IEditableTextEditor {

	void setText(String text);
	
	String getText() throws TextBlockOverflowException;
	
	void setDefaultText(String defaultText);
	
	void setEditable(boolean value);
}
