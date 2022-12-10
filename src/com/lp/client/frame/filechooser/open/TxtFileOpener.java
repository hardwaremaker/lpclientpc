package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.TxtAcceptor;

public class TxtFileOpener extends BaseFileOpener<TxtFile> {

	public TxtFileOpener(Component parent, FileChooserConfigToken token) {
		super(new TxtAcceptor(), parent, token);
	}
	
	@Override
	protected TxtFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addTxtFilter().openSingle();
	}
	
	@Override
	protected TxtFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addTxtFilter().openSingleUnchecked();
	}
}
