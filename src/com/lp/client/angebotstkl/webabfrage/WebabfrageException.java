package com.lp.client.angebotstkl.webabfrage;

public class WebabfrageException extends WebabfrageError {
	
	private Exception ex;

	public WebabfrageException(Integer id, String search, Integer abfrageType, Exception ex) {
		super(id, search, abfrageType);
		this.ex = ex;
	}

	public Exception getException() {
		return ex;
	}

	public void setException(Exception ex) {
		this.ex = ex;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WebabfrageException: ");
		builder.append(errorToString());
		
		if (ex != null) {
			builder.append(", exception: message=");
			builder.append(ex.getMessage());
			builder.append(", cause=");
			builder.append(ex.getCause());
		}
		
		return builder.toString();
	}


}
