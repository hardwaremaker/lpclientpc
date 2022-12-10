package com.lp.client.frame.filechooser.filter;

import java.io.File;

public abstract class FileAcceptor implements IAccept<File> {

	private IAccept<File> acceptorDecorator;
	
	public FileAcceptor(IAccept<File> acceptorDecorator) {
		this.acceptorDecorator = acceptorDecorator;
	}
	
	public FileAcceptor() {
	}
	
	protected IAccept<File> getAcceptorDecorator() {
		if (acceptorDecorator == null) {
			acceptorDecorator = new IAccept<File>() {
				public boolean accept(File file) {
					return false;
				}
			};
		}
		return acceptorDecorator;
	}
	
	protected void setAcceptorDecorator(IAccept<File> acceptorDecorator) {
		this.acceptorDecorator = acceptorDecorator;
	}
	
	@Override
	public boolean accept(File file) {
		return acceptImpl(file) || getAcceptorDecorator().accept(file);
	}

	protected abstract boolean acceptImpl(File file);
}
