package com.lp.neweditor.ui.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.lp.client.frame.HelperClient;
import com.lp.neweditor.common.EditorBackgroundScheduler;
import com.lp.neweditor.common.ImageScaling;
import com.lp.server.util.logger.LpLogger;

public class ScalingImageUI extends JLabel {
	private static final long serialVersionUID = 1L;
	private final Dimension emptySize = new Dimension(64, 64);
	private final BufferedImage emptyImage;

	private Buffer buffer;
	private BufferedImage img = null;

	private Logger logger = LpLogger.getLogger(getClass().getName());

	public ScalingImageUI(BufferedImage img, ImageScaling scaling) {
		buffer = new Buffer();
		emptyImage = createEmptyImage();
		setImage(img, scaling);
	}

	public ScalingImageUI() {
		buffer = new Buffer();
		emptyImage = createEmptyImage();
		setIcon(new ImageIcon(emptyImage));
	}

	protected BufferedImage createEmptyImage() {
		BufferedImage emptyImage = new BufferedImage(getEmptySize().width, getEmptySize().height,
				BufferedImage.TYPE_INT_ARGB);
		Graphics scratchGraphics = emptyImage.createGraphics();
		scratchGraphics.setColor(Color.black);
		scratchGraphics.drawRect(0, 0, getEmptySize().width - 1, getEmptySize().height - 1);
		scratchGraphics.setColor(Color.white);
		scratchGraphics.fillRect(1, 1, getEmptySize().width - 3, getEmptySize().width - 3);
		scratchGraphics.setColor(Color.red);
		drawX(scratchGraphics);

		scratchGraphics.dispose();
		return emptyImage;
	}

	protected Dimension getEmptySize() {
		return emptySize;
	}

	private void drawX(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		final int border = 5;
		final int lineWid = 10;
		Rectangle2D rectHor = new Rectangle2D.Float(border, (getEmptySize().height - lineWid) / 2,
				getEmptySize().width - 2 * border, lineWid);
		Rectangle2D rectVer = new Rectangle2D.Float((getEmptySize().width - lineWid) / 2, border, lineWid,
				getEmptySize().width - 2 * border);

		g2d.rotate(Math.PI / 4, getEmptySize().getWidth() / 2, getEmptySize().getHeight() / 2);
		g2d.fill(rectVer);
		g2d.fill(rectHor);
	}

	@Override
	public Dimension getPreferredSize() {
		if (img != null)
			return new Dimension(img.getWidth(), img.getHeight());
		else
			return new Dimension(getEmptySize());
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public void setImage(BufferedImage img, ImageScaling scaling) {
		buffer.setData(img, scaling, this::refresh);
	}

	private void refresh(BufferedImage img) {
		SwingUtilities.invokeLater(() -> {
			this.img = img;
			setIcon(new ImageIcon(this.img == null ? emptyImage : this.img));
			revalidate();
			repaint();
		});
	}

	/**
	 * Asynchrone Bildskalierung. Um ein Bild zu skalieren, wird setData()
	 * aufgerufen, dieser Methode wird ein Consumer gegeben, dessen apply Methode
	 * mit dem skalierten Bild aufgerufen wird. <br>
	 * Wenn bereits eine vorherige skalierung l&auml;uft, wird keine neue gestartet,
	 * sondern es werden die Daten der alten ge&auml;ndert. In diesem Fall wird der
	 * alte Consumer mit den neuen Daten aufgerufen, der neue Consumer wird nie
	 * aufgerufen
	 */
	private class Buffer {
		// 1 Entry queue -> wird nur aus synchronized Methoden zugegriffen
		private boolean queuePresent = false;
		private BufferedImage queuedImg;
		private ImageScaling queuedScaling;
		private Consumer<BufferedImage> queueTarget;

		private boolean running = false;

		/**
		 * 
		 * @param image
		 * @param scaler
		 * @param target
		 * @param startImmediate
		 */
		public synchronized void setData(BufferedImage image, ImageScaling scaler, Consumer<BufferedImage> target) {
			// Wenn nicht lauft, starte neuen Task. running bleibt true, bis der Task fertig
			// ist und kein Queue Element vorhanden war (Setzen passiert in synchronized
			// bloecken)
			if (!running) {
				running = true;
				CompletableFuture<BufferedImage> future = createFuture(image, scaler);
				future.thenAccept(target);
			} else {
				queuedImg = image;
				queuedScaling = scaler;
				queueTarget = target;
				queuePresent = true;
			}
		}

		private synchronized void startQueued() {
			if (queuePresent) {
				queuePresent = false;
				CompletableFuture<BufferedImage> future = createFuture(queuedImg, queuedScaling);
				future.thenAccept(queueTarget);
				queuedImg = null;
				queuedScaling = null;
				queueTarget = null;
			} else {
				running = false;
			}
		}

		private CompletableFuture<BufferedImage> createFuture(BufferedImage img, ImageScaling scale) {
			CompletableFuture<BufferedImage> future;
			future = CompletableFuture.supplyAsync(() -> scaleImage(img, scale), EditorBackgroundScheduler.getExecutor());
			future.thenRun(this::startQueued);
			return future;
		}

		private BufferedImage scaleImage(BufferedImage img, ImageScaling scale) {
			BufferedImage localBuffer = null;
			if (img != null && scale != null) {
				Dimension sizeOrig = new Dimension(img.getWidth(), img.getHeight());
				Dimension sizeTarget = scale.scaleSize(sizeOrig);
				localBuffer = HelperClient.scaleImage(img, sizeTarget.width, sizeTarget.height,
						RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			}
			return localBuffer;
		}

	}

}