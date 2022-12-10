package com.lp.client.rechtschreibung;

/**
 * Ein Span, das als dirty markiert werden kann und den Zeitpunkt dieser
 * markierung speichert. Benutzt um &Auml;nderungen in W&ouml;rtern zu speichern
 *
 */
class MarkedWord extends Span {
	private boolean marked = false;
	private long markedTime = 0;
	final boolean canIgnoreFirst;
	final boolean canIgnoreLast;

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
		markedTime = System.currentTimeMillis();
	}

	/**
	 * Kopiert markierung und markierzeit von anderer Span
	 * 
	 * @param other
	 */
	public void copyMarkingFrom(MarkedWord other) {
		if (other == null)
			return;
		else {
			markedTime = other.markedTime;
			marked = other.marked;
		}
	}

	public long getMarkedTime() {
		return markedTime;
	}

	public MarkedWord(int start, int end, boolean canIgnoreFirst, boolean canIgnoreLast) {
		super(start, end);
		this.canIgnoreFirst = canIgnoreFirst;
		this.canIgnoreLast = canIgnoreLast;
	}

	@Override
	public String toString() {
		if (isMarked()) {
			return "DIRTY" + super.toString();
		} else {
			return super.toString();
		}
	}
}