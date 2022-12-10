package com.lp.neweditor.action;

import javax.swing.Action;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.neweditor.ui.menu.BlockEditorData;

/**
 * Klasse, die alle Actions des {@link HvBlockEditor}s verwaltet
 * 
 * @author Alexander Daum
 * 
 *
 */
public class BlockEditorActions {
	private final Action actionCopy;
	private final Action actionCut;
	private final Action actionPaste;

	private final Action actionFontSelector;

	private final Action actionBold;
	private final Action actionItalic;
	private final Action actionStrikethrough;
	private final Action actionUnderline;

	private final Action actionInsertDateTimeUser;
	private final Action actionInsertSignatur;
	private final Action actionInsertTextbaustein;
	private final Action actionInsertImage;
	private final Action actionInsertTextblock;
	
	private final Action actionImageProperties;

	private final Action actionDeleteBlock;

	private final Action actionColorForeground;
	private final Action actionColorBackground;

	private final UndoRedoAction actionUndoRedo;

	/**
	 * Erzeugt eine neue Instanz. <br>
	 * Achtung: greift auf {@link HvBlockEditor#getUndoManager()} und
	 * {@link HvBlockEditor#addUndoableEditListener(javax.swing.event.UndoableEditListener)}
	 * zu, sicherstellen, dass diese Methode im {@link HvBlockEditor} Konstruktor
	 * nicht vor erzeugen von {@link UndoManager} und {@link UndoableEditSupport}
	 * aufgerufen wird
	 * 
	 * @param editor
	 */
	public BlockEditorActions(HvBlockEditor editor) {
		actionCopy = new ActionEditCopy();
		actionCut = new ActionEditCut();
		actionPaste = new ActionEditPaste();

		actionFontSelector = new ActionFormatFont(BlockEditorData.getInstance());

		actionBold = new ActionFormatStyleBold();
		actionItalic = new ActionFormatStyleItalic();
		actionStrikethrough = new ActionFormatStyleStrikethrough();
		actionUnderline = new ActionFormatStyleUnderline();

		actionInsertDateTimeUser = new ActionInsertDateTimeUserShortcut();
		actionInsertSignatur = new ActionInsertSignatur(editor);
		actionInsertTextbaustein = new ActionInsertTextbaustein(editor);
		actionInsertImage = new ActionInsertImageBlock(editor);
		actionInsertTextblock = new ActionInsertTextblock(editor);
		
		actionImageProperties = new ActionImageProperties(editor);

		actionDeleteBlock = new ActionDeleteBlock(editor);

		actionColorForeground = new ActionFormatColorForeground();
		actionColorBackground = new ActionFormatColorBackground();

		actionUndoRedo = new UndoRedoAction(editor.getUndoManager());
		editor.addUndoableEditListener(actionUndoRedo);
	}

	public Action getActionCopy() {
		return actionCopy;
	}

	public Action getActionCut() {
		return actionCut;
	}

	public Action getActionPaste() {
		return actionPaste;
	}

	public Action getActionFontSelector() {
		return actionFontSelector;
	}

	public Action getActionBold() {
		return actionBold;
	}

	public Action getActionItalic() {
		return actionItalic;
	}

	public Action getActionStrikethrough() {
		return actionStrikethrough;
	}

	public Action getActionUnderline() {
		return actionUnderline;
	}

	public Action getActionInsertDateTimeUser() {
		return actionInsertDateTimeUser;
	}

	public Action getActionInsertSignatur() {
		return actionInsertSignatur;
	}

	public Action getActionInsertTextbaustein() {
		return actionInsertTextbaustein;
	}

	public Action getActionInsertImage() {
		return actionInsertImage;
	}

	public Action getActionColorForeground() {
		return actionColorForeground;
	}

	public Action getActionColorBackground() {
		return actionColorBackground;
	}

	public Action getActionInsertTextblock() {
		return actionInsertTextblock;
	}

	public Action getActionDeleteBlock() {
		return actionDeleteBlock;
	}

	public Action getActionImageProperties() {
		return actionImageProperties;
	}

	public UndoRedoAction getActionUndoRedo() {
		return actionUndoRedo;
	}

	public Action getUndoAction() {
		return actionUndoRedo.getUndoAction();
	}

	public Action getRedoAction() {
		return actionUndoRedo.getRedoAction();
	}
}
