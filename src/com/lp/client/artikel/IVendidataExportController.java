package com.lp.client.artikel;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.lp.client.frame.component.IFileController;

public interface IVendidataExportController<T> extends IFileController {

	public T checkExport();
	
	public T doExport() throws FileNotFoundException, IOException;
	
	public T exportXML(boolean checkOnly);
	
}
