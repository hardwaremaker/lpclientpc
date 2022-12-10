package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.XlsxAcceptor;

public class XlsxFileOpener extends BaseFileOpener<XlsxFile> {
	
	public XlsxFileOpener(Component parent, FileChooserConfigToken token) {
		super(new XlsxAcceptor(), parent, token);
	}
	
	@Override
	protected XlsxFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXlsxFilter().openSingle();
	}
	
	@Override
	protected XlsxFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXlsxFilter().openSingleUnchecked();
	}
}
