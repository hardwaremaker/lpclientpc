package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;

public class AnyFileOpener extends BaseFileOpener<WrapperFile> {
	
	public AnyFileOpener(Component parent, FileChooserConfigToken token) {
		super(null, parent, token);
	}

	@Override
	protected WrapperFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addAllFileFilter().openSingle();
	}

	@Override
	protected WrapperFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addAllFileFilter().openSingleUnchecked();
	}
}
