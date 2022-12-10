package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.XlsAcceptor;

public class XlsFileOpenerNew extends BaseFileOpener<XlsFile> {
	
	public XlsFileOpenerNew(Component parent, FileChooserConfigToken token) {
		super(new XlsAcceptor(), parent, token);
	}
	
	@Override
	protected XlsFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXlsFilter().openSingle();
	}
	
	@Override
	protected XlsFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXlsFilter().openSingleUnchecked();
	}
}
