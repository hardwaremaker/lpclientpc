package com.lp.neweditor.excep;

/**
 * Exception, die vom neuen Editor geworfen wird, wenn interne Fehler auftreten,
 * die nicht sinnvoll behandelt werden k&ouml;nnen.
 * 
 * @author Alexander Daum
 *
 */
public class EditorError extends Error {
	private static final long serialVersionUID = -7390442554408656142L;

	public EditorError() {
		super();
	}

	public EditorError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public EditorError(String message, Throwable cause) {
		super(message, cause);
	}

	public EditorError(String message) {
		super(message);
	}

	public EditorError(Throwable cause) {
		super(cause);
	}

}
