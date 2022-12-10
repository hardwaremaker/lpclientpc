package com.lp.client.forecast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.lp.client.frame.component.IFileController;
import com.lp.client.remote.IPayloadPublisher;

public interface IImportController<T, E> extends IFileController {

	public static final String ARCHIVE_DIRECTORY = "old";

	public T checkImport() throws FileNotFoundException, IOException;
	
	public T checkImport(IPayloadPublisher publisher) throws FileNotFoundException, IOException;
	
	public T doImport() throws FileNotFoundException, IOException;
	
	public T doImport(IPayloadPublisher publisher) throws FileNotFoundException, IOException;
	
	public T importContent(E content, boolean checkOnly);
	
	public T importFile(File file) throws FileNotFoundException, IOException;
	
	public T importFile(String filename) throws FileNotFoundException, IOException;
}
