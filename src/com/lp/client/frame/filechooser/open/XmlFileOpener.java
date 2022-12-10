package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.XmlAcceptor;

public class XmlFileOpener extends BaseFileOpener<XmlFile> {

	public XmlFileOpener(Component parent, FileChooserConfigToken token) {
		super(new XmlAcceptor(), parent, token);
	}

	@Override
	protected XmlFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXmlFilter().openSingle();
	}

	@Override
	protected XmlFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addXmlFilter().openSingleUnchecked();
	}
}
