package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.CsvAcceptor;

public class CsvFileOpener extends BaseFileOpener<CsvFile> {
	
	public CsvFileOpener(Component parent, FileChooserConfigToken token) {
		super(new CsvAcceptor(), parent, token);
	}
	
	@Override
	protected CsvFile openSingle(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addCsvFilter().openSingle();
	}
	
	@Override
	protected CsvFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return FileChooserBuilder.createOpenDialog(token, parent)
				.addCsvFilter().openSingleUnchecked();
	}
}
