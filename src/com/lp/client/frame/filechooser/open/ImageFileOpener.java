package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.GifAcceptor;
import com.lp.client.frame.filechooser.filter.HvTaggedImageFilter;
import com.lp.client.frame.filechooser.filter.JpgAcceptor;
import com.lp.client.frame.filechooser.filter.PngAcceptor;
import com.lp.client.frame.filechooser.filter.TiffAcceptor;

public class ImageFileOpener extends BaseFileOpener<WrapperFile> {

	public ImageFileOpener(Component parent, FileChooserConfigToken token) {
		super(new JpgAcceptor(new PngAcceptor(new TiffAcceptor(new GifAcceptor()))), parent, token);
	}
	

	@Override
	protected WrapperFile openSingle(Component parent, FileChooserConfigToken token) {
/*		
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvImageFileFilter()).openSingle();
*/
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addFilter(new HvTaggedImageFilter()).openSingle();
	}
	
	@Override
	protected WrapperFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return openSingle(parent, token);
	}
}
