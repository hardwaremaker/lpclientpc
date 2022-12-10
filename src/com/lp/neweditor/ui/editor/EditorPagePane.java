package com.lp.neweditor.ui.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.swing.JPanel;

import com.lp.neweditor.common.EditorZoomFaktor;
import com.lp.neweditor.common.ScaledUnitDimension;
import com.lp.neweditor.common.SizeUnit;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.BlockView;

public class EditorPagePane extends JPanel implements Iterable<BlockView> {
	private static final long serialVersionUID = 1L;
	public static final Dimension FORMAT_A4 = new Dimension(210, 297);
	public static final Dimension FORMAT_A5 = new Dimension(148, 210);

	private ScaledUnitDimension dimension;
	private List<EditorLine> lines = new ArrayList<>();
	private int modCount = 0;

	/**
	 * Wenn true, dann kann die Seite unendlich lang werden, wenn false wird auch
	 * vertikal die Groesse beschraenkt.
	 */
	private boolean canGrowInfinitely = true;

	public EditorPagePane() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		addMouseListener(new MouseEvents());
		dimension = new ScaledUnitDimension(HvBlockEditor.DISPLAY_DPI);
		dimension.setDimension(FORMAT_A4, SizeUnit.MM);
		setBackground(Color.white);
		setBorder(new EditorPageBorder());
	}

	public EditorZoomFaktor getZoomFaktor() {
		return dimension.getZoomFaktor();
	}

	public void setZoomFaktor(EditorZoomFaktor zoomFactor) {
		dimension.setZoomFaktor(zoomFactor);
		revalidate();
	}

	@Override
	public Dimension getPreferredSize() {
		if (canGrowInfinitely) {
			Dimension sizeFromLayout = super.getPreferredSize();
			sizeFromLayout.width = dimension.getWidth();
			sizeFromLayout.height = Math.max(sizeFromLayout.height, dimension.getHeight());
			return sizeFromLayout;
		} else {
			return dimension.getAsPixels();
		}
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public BlockView getBlock(BlockPosition pos) {
		return lines.get(pos.y).get(pos.x);
	}

	public void removeAllBlocks() {
		lines.clear();
		this.removeAll();
	}

	protected void addBlock(BlockView block) {
		modCount++;
		synchronized (getTreeLock()) {
			if (lines.isEmpty()) {
				lines.add(new EditorLine());
				add(lines.get(0));
			}
			EditorLine lastLine = lines.get(lines.size() - 1);
			lastLine.addBlock(block);
		}
	}

	protected void addLine() {
		modCount++;
		EditorLine newLine = new EditorLine();
		lines.add(newLine);
		add(newLine);
	}

	public Iterator<BlockView> iterator() {
		return new Itr();
	}

	@Override
	public Color getBackground() {
		return Color.white;
	}

	public void setSize(Dimension dim, SizeUnit unit) {
		dimension.setDimension(dim, unit);
	}

	public void setWidth(double width, SizeUnit unit) {
		dimension.setWidth(width, unit);
	}

	public void setHeight(double height, SizeUnit unit) {
		dimension.setHeight(height, unit);
	}

	private class MouseEvents extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (getComponentAt(e.getPoint()) == EditorPagePane.this) {
				System.out.println("Klick ausserhalb von TextPane");
				List<BlockView> blocks = lines.get(lines.size() - 1).getBlocks();
				BlockView lastBlock = blocks.get(blocks.size() - 1);
				lastBlock.getFocusClickBehind();
			}
		}
	}

	private class Itr implements Iterator<BlockView> {

		private int x, y;
		private int expectedModCount = modCount;

		public Itr() {
			x = 0;
			y = 0;
		}

		private boolean isBlockValid() {
			return lines.get(y).getBlocks().size() > x;
		}

		private boolean hasMoreRows() {
			return lines.size() > (y + 1);
		}

		@Override
		public boolean hasNext() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			return hasMoreRows() || isBlockValid();
		}

		@Override
		public BlockView next() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (!isBlockValid()) {
				if (hasMoreRows()) {
					x = 0;
					y++;
				} else {
					throw new NoSuchElementException(String.format("No Element at (%d, %d)", x, y));
				}
			}
			List<BlockView> line = lines.get(y).getBlocks();
			BlockView obj = line.get(x);
			x++;
			return obj;
		}
	}

}
