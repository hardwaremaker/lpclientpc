package com.lp.client.frame.filechooser.open;

import java.awt.Component;

import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.filter.CsvAcceptor;
import com.lp.client.frame.filechooser.filter.FileExtension;
import com.lp.client.frame.filechooser.filter.HvCsvFileFilter;
import com.lp.client.frame.filechooser.filter.HvXlsFileFilter;
import com.lp.client.frame.filechooser.filter.XlsAcceptor;

public class CsvXlsFileOpenerNew extends BaseFileOpener<CsvXlsFile> {
	public CsvXlsFileOpenerNew(Component parent, FileChooserConfigToken token) {
		super(new CsvAcceptor(new XlsAcceptor()), parent, token);
	}

	@Override
	protected CsvXlsFile openSingle(Component parent, FileChooserConfigToken token) {
		HvCsvFileFilter csvFilter = new HvCsvFileFilter();
		HvXlsFileFilter xlsFilter = new HvXlsFileFilter();
		WrapperFile wf = FileChooserBuilder.createOpenDialog(token, parent)
				.addFilters(csvFilter, xlsFilter)
				.openSingle();
		if(wf.hasFile()) {
			if(csvFilter.accept(wf.getFile()) ) {
				return new CsvXlsFile(wf.getFile(), FileExtension.CSV);
			}
			
			if(xlsFilter.accept(wf.getFile())) {
				return new CsvXlsFile(wf.getFile(), FileExtension.XLS);
			}	
		} 

		return new CsvXlsFile(null);
	}
	
	@Override
	protected CsvXlsFile openSingleUnchecked(Component parent, FileChooserConfigToken token) {
		return openSingle(parent, token);
	}
}
