package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.HvTaggedPdfFileFilter;

public class PdfFileOpener extends BaseFileOpener<PdfFile> {

	public PdfFileOpener(Component parent, FileChooserConfigToken token) {
		super(null, parent, token);
	}
	
	@Override
	protected PdfFile openSingle(Component parent, FileChooserConfigToken token) {
		WrapperFile wf = FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvTaggedPdfFileFilter()).openSingle();
		return new PdfFile(wf != null ? wf.getFile() : null);
	}

	@Override
	protected PdfFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		WrapperFile wf = FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvTaggedPdfFileFilter()).openSingleUnchecked();
		return new PdfFile(wf != null ? wf.getFile() : null);
	}
}
