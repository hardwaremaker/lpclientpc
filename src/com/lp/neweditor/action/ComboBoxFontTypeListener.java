package com.lp.neweditor.action;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.lp.neweditor.common.NewEditorHelper;

/**
 * ItemListener zum Umstellen des Fonts eines JTextPanes. <br>
 * Nimmt an, dass ItemEvents mit Objekten des Typs {@link String} erhalten
 * werden. Jedes Item muss auch ein valider Font Name sein
 * 
 * @author Alexander Daum
 *
 */
public class ComboBoxFontTypeListener implements ItemListener {
	private final JTextPane editor;

	public ComboBoxFontTypeListener(JTextPane editor) {
		this.editor = editor;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (editor.getSelectionStart() != editor.getSelectionEnd()) {
				String font = e.getItem().toString();
				MutableAttributeSet attrSet = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attrSet, font);
				editor.setCharacterAttributes(attrSet, false);
				NewEditorHelper.updateUIAndCaret(editor, false);
			}
		}
	}

}
