package com.lp.neweditor.ui.editor;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.lp.neweditor.ui.block.BlockView;

/**
 * Klasse, die eine Liste von horizontal angeordneten EditorBlocken verwaltet.
 * Als UI Element dient ein {@link JPanel} mit Horizontalem {@link BoxLayout}
 * 
 * @author Alexander Daum
 *
 */
class EditorLine extends JPanel {
	private static final long serialVersionUID = 1L;
	private List<BlockView> blocks;

	public EditorLine() {
		blocks = new ArrayList<>();
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public List<BlockView> getBlocks() {
		return Collections.unmodifiableList(blocks);
	}

	public void addBlock(BlockView block) {
		blocks.add(block);
		add(block.getUIComponent());
	}

	public BlockView get(int index) {
		return blocks.get(index);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension superSize = super.getPreferredSize();
		Insets ins = getParent().getInsets();
		int width = getParent().getWidth();
		return new Dimension(width - ins.left - ins.right, superSize.height);
	}

}
