package com.lp.client.frame.component;

public interface IImportResultController<T> {

	T doImport();
	
	T checkImport();

	boolean hasNext();

	String getCurrentFilename();

	String getNextFilename();

	Integer getPosition();

	Integer getNumberOfFiles();
}
