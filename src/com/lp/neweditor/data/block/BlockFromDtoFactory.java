package com.lp.neweditor.data.block;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.lp.neweditor.ui.block.EditorBlock;
import com.lp.neweditor.ui.block.ImageBlockController;
import com.lp.neweditor.ui.block.TextBlockController;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.server.system.ejb.EditorTextBlock;
import com.lp.server.system.service.EditorBaseBlockDto;
import com.lp.server.system.service.EditorImageBlockDto;
import com.lp.server.system.service.EditorTextBlockDto;

/**
 * Klasse um aus einem Dto den richtigen {@link EditorBlock} zu erstellen.
 * 
 * @author Alexander Daum
 *
 */
public class BlockFromDtoFactory {
	private Map<Class<? extends EditorBaseBlockDto>, Function<HvBlockEditor, EditorBlock<?>>> typeMap = null;
	private final HvBlockEditor editor;

	/**
	 * Erstelle die TypeMap, die Dto Klassen zu EditorBlock Factories mappt. Eine
	 * Factory sollte den Default-Konstruktor aufrufen, die intialisierung mit Dto
	 * Daten geschieht sp&auml;ter
	 * 
	 * @return
	 * 
	 */
	protected Map<Class<? extends EditorBaseBlockDto>, Function<HvBlockEditor, EditorBlock<?>>> createTypeMap() {
		Map<Class<? extends EditorBaseBlockDto>, Function<HvBlockEditor, EditorBlock<?>>> typeMap = new HashMap<>();
		typeMap.put(EditorTextBlockDto.class, TextBlockController::new);
		typeMap.put(EditorImageBlockDto.class, ImageBlockController::new);
		return typeMap;
	}

	/**
	 * Getter fuer TypeMap.
	 * 
	 * @return
	 */
	protected Map<Class<? extends EditorBaseBlockDto>, Function<HvBlockEditor, EditorBlock<?>>> getTypeMap() {
		if (typeMap == null) {
			typeMap = createTypeMap();
		}
		return typeMap;
	}

	public BlockFromDtoFactory(HvBlockEditor editor) {
		this.editor = editor;
	}

	public EditorBlock<?> createBlockForDto(EditorBaseBlockDto dto) {
		EditorBlock<?> block = getTypeMap().get(dto.getClass()).apply(editor);
		block.getModel().fromDto(dto);
		return block;
	}
}
