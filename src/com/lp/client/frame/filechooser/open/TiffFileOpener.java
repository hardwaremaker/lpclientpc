package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.HvTaggedTiffFileFilter;
import com.lp.client.frame.filechooser.filter.TiffAcceptor;

public class TiffFileOpener extends BaseFileOpener<WrapperFile> {

	public TiffFileOpener(Component parent, FileChooserConfigToken token) {
		super(new TiffAcceptor(), parent, token);
	}

	@Override
	protected WrapperFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvTaggedTiffFileFilter()).openSingle();
	}

	@Override
	protected WrapperFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvTaggedTiffFileFilter()).openSingleUnchecked();
	}
}
