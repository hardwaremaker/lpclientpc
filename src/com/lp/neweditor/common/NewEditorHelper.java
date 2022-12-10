package com.lp.neweditor.common;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

public class NewEditorHelper {
	public static void updateUIAndCaret(JTextComponent textComp, boolean requestFocus) {
		SwingUtilities.invokeLater(() -> {
			int selStart = textComp.getSelectionStart();
			int selStop = textComp.getSelectionEnd();
			int caretPos = textComp.getCaretPosition();
			textComp.getCaret().deinstall(textComp);
			textComp.updateUI();
			if (requestFocus) {
				textComp.requestFocusInWindow();
			}
			textComp.setCaretPosition(caretPos);
			textComp.setSelectionEnd(selStop);
			textComp.setSelectionStart(selStart);
			textComp.repaint();
		});
	}

	public static void getFocusKeepSelection(JTextComponent textComp) {
		if(textComp == null)
			return;
		SwingUtilities.invokeLater(() -> {
			int selStart = textComp.getSelectionStart();
			int selStop = textComp.getSelectionEnd();
			textComp.requestFocusInWindow();
			textComp.setSelectionEnd(selStop);
			textComp.setSelectionStart(selStart);
			textComp.repaint();
		});
	}
}
