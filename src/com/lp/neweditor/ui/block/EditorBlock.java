package com.lp.neweditor.ui.block;

import com.lp.neweditor.data.IModelChangedProvider;
import com.lp.neweditor.data.block.BaseBlockModel;
import com.lp.neweditor.event.BlockSelectedEvent;
import com.lp.neweditor.event.BlockSelectedListener;
import com.lp.neweditor.ui.editor.HvBlockEditor;

/**
 * Interface f&uuml;r alle Editorbl&ouml;cke. <br>
 * Der ModelType wird &uuml;ber einen Generic-Typ bestimmt, damit wenn bekannt
 * ist, was f&uuml;r ein Block vorhanden ist, direkt ein spezifischeres Model
 * erhalten und auch gesetzt werden kann. <br>
 * 
 * Wenn ModelType ein Capture Generic mit extends ist (enth&auml;lt ? oder ?
 * extends), kann die setModel Methode nicht aufgerufen werden. F&uuml;r
 * Information dazu siehe {@link IModelChangedProvider#setModel(Object)}
 * 
 * @author Alexander Daum
 *
 * @param <ModelType> Typ des Models. Muss {@link BlockModel} implementieren
 */
public interface EditorBlock<ModelType extends BaseBlockModel> extends IModelChangedProvider<ModelType> {
	/**
	 * Gibt zurück, ob dieser EditorComponent gezogen werden kann. Dadurch erhält
	 * der EditorComponent allerdings keine MouseDragged Events mehr. Wenn ein Drag
	 * abgeschlossen wurde, wird ein DragEvent gefeuert.
	 * 
	 * @return
	 */
	boolean canBeDragged();

	/**
	 * Gibt das View des Block Zur&uuml;ck
	 * 
	 * @return
	 */
	BlockView getView();

	/**
	 * Erzeuge ein neues Modell nach default Einstellungen
	 * 
	 * @return
	 */
	ModelType createDefaultModel();

	/**
	 * F&uuml;gt einen Listener auf {@link BlockSelectedEvent} hinzu
	 * 
	 * @param listener
	 */
	void addSelectedListener(BlockSelectedListener listener);

	/**
	 * Entfernt einen Listener auf {@link BlockSelectedEvent}. Wenn der Listener
	 * nicht zu diesem Block hinzugef&uuml;gt wurde, hat diese Funktion keine
	 * Auswirkung
	 * 
	 * @param listener
	 */
	void removeSelectedListener(BlockSelectedListener listener);

	/**
	 * Gibt den Editor, dem dieser Block zugeordnet ist, zur&uuml;ck
	 * 
	 * @return
	 */
	HvBlockEditor getEditor();
	
	void setEditable(boolean enabled);
	
	boolean isEditable();
}
