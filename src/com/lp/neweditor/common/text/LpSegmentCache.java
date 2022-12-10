package com.lp.neweditor.common.text;

import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.GlyphView;
import javax.swing.text.Segment;

import com.lp.neweditor.excep.EditorError;

/**
 * Klasse, die javax.swing.text.SegmentCache f&uuml;r den neuen Editor ersetzt,
 * da SegmentCache nur package visible ist. Bietet auch noch Methoden zum holen
 * eines Segments von einem view, da die getText Methode nicht verwendet werden
 * kann, weil diese den nicht Sichtbaren SegmentCache verwendet und das Segment
 * wieder frei gegeben werden muss... <br>
 * 
 * 
 * @apiNote Immer wenn Segments ben&ouml;tigt werden, sollte diese Klasse
 *          verwendet werden, statt neue Segments zu erzeugen um Garbage Objekte zu
 *          reduzieren.
 * 
 * @author Alexander Daum
 *
 */
public class LpSegmentCache {

	private static final int CACHE_SIZE = 16;

	/**
	 * A global cache.
	 */
	private static LpSegmentCache sharedCache = new LpSegmentCache();

	/**
	 * A list of the currently unused Segments.
	 */
	private ArrayBlockingQueue<Segment> segments;

	/**
	 * Returns the shared SegmentCache.
	 */
	public static LpSegmentCache getSharedInstance() {
		return sharedCache;
	}

	/**
	 * A convenience method to get a Segment from the shared
	 * <code>SegmentCache</code>.
	 */
	public static Segment getSharedSegment() {
		return getSharedInstance().getSegment();
	}

	/**
	 * A convenience method to release a Segment to the shared
	 * <code>SegmentCache</code>.
	 */
	public static void releaseSharedSegment(Segment segment) {
		getSharedInstance().releaseSegment(segment);
	}

	/**
	 * Creates and returns a SegmentCache.
	 */
	public LpSegmentCache() {
		segments = new ArrayBlockingQueue<>(CACHE_SIZE);
	}

	/**
	 * Returns a <code>Segment</code>. When done, the <code>Segment</code> should be
	 * recycled by invoking <code>releaseSegment</code>.
	 */
	public Segment getSegment() {
		Segment queed = segments.poll();
		return queed == null ? new LpCachedSegment() : queed;
	}

	/**
	 * Releases a Segment. You should not use a Segment after you release it, and
	 * you should NEVER release the same Segment more than once, eg:
	 * 
	 * <pre>
	 * segmentCache.releaseSegment(segment);
	 * segmentCache.releaseSegment(segment);
	 * </pre>
	 * 
	 * Will likely result in very bad things happening!
	 */
	public void releaseSegment(Segment segment) {
		if (segment instanceof LpCachedSegment) {
			segment.array = null;
			segment.count = 0;
			segments.offer(segment);
		}
	}

	/**
	 * CachedSegment is used as a tagging interface to determine if a Segment can
	 * successfully be shared.
	 */
	private static class LpCachedSegment extends Segment {
	}

	/**
	 * Holt ein Text Segment aus einem GlyphView und speichert es in einem Segment,
	 * das gecached ist. <br>
	 * Ersetzt {@link GlyphView#getText(int, int)}, weil dort der nicht public
	 * SegmentCache verwendet wird. <br>
	 * Nach der Benutzung muss das Segment mit
	 * {@link LpSegmentCache#releaseSharedSegment(Segment)} wieder frei gegeben
	 * werden
	 * 
	 * @param v
	 * @param p0
	 * @param p1
	 * @return
	 */
	public static Segment getText(GlyphView v, int p0, int p1) {
		Segment text = getSharedSegment();
		try {
			Document doc = v.getDocument();
			doc.getText(p0, p1 - p0, text);
		} catch (BadLocationException e) {
			throw new EditorError("Segment ist nicht in Document von GlyphView enthalten!", e);
		}
		return text;
	}

}
