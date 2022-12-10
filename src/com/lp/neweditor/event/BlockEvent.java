package com.lp.neweditor.event;

import java.util.EventObject;

import com.lp.neweditor.common.Direction;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.EditorBlock;

/**
 * Klasse, die Events von ver&auml;nderungen der Bl&ouml;cke in einem Editor
 * darstellt. <br>
 * Diese Klasse behandelt 2 verschiedene Operationen, die anhand von
 * {@link BlockEvent#getType()} unterschieden werden k&ouml;nnen: <br>
 * <ul>
 * <li><b>Insert:</b> Ein neuer Block wurde eingef&uuml;gt.
 * {@link BlockEvent#getDir()} ist VERTICAL, wenn eine neue Zeile angelegt wurde
 * (dann wurden auch alle Zeilen darunter verschoben) und HORIZONTAL, wenn der
 * Block einer existierenden Zeile hinzugef&uuml;gt wurde (dann wurden auch alle
 * Bl&ouml;cke rechts dieses Blocks verschoben). <br>
 * <li><b>Remove:</b> Ein Block wurde gel&ouml;scht. {@link BlockEvent#getDir()}
 * ist VERTICAL, wenn das der letzte Block einer Zeile war und die Zeile
 * entfernt wurde, HORIZONTAL, wenn noch weitere Bl&ouml;cke in dieser Zeile
 * sind <br>
 * </ul>
 * <br>
 * Wird ein Block verschoben, werden separate Remove und Insert events erzeugt
 * (Remove garantiert zuerst)
 * 
 * @author Alexander Daum
 *
 */
public class BlockEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final EditorBlock<?> block;
	private final BlockPosition position;
	private final Type type;
	private final Direction dir;

	public BlockEvent(EditorBlock<?> block, BlockPosition position, Type type, Direction dir, Object source) {
		super(source);
		this.block = block;
		this.position = position;
		this.type = type;
		this.dir = dir;
	}

	public EditorBlock<?> getBlock() {
		return block;
	}

	public Type getType() {
		return type;
	}

	public Direction getDir() {
		return dir;
	}

	public BlockPosition getPosition() {
		return position;
	}

	public static enum Type {
		INSERT, REMOVE
	}

	@Override
	public String toString() {
		switch (type) {
		case INSERT:
			return String.format("INSERT: to %s", position.toString());
		case REMOVE:
			return String.format("REMOVE: %d", position.toString());
		}

		return "Unknown Type";
	}
}
