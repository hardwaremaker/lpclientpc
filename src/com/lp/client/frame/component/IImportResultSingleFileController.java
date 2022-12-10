package com.lp.client.frame.component;

public interface IImportResultSingleFileController<T> {

	T doImport();
	
	T checkImport();

	String getFilename();
}
