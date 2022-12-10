package com.lp.client.frame.filechooser.open;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.filechooser.filter.FileAcceptor;

public abstract class BaseDirectoryFile<T extends IWrapperFile> {

	private File directory;
	private FileAcceptor fileAcceptor;
	
	public BaseDirectoryFile(File directory) {
		this.directory = directory;
	}
	
	public File getDirectory() {
		return directory;
	}
	
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	
	public void setFileAcceptor(FileAcceptor fileAcceptor) {
		this.fileAcceptor = fileAcceptor;
	}
	
	public FileAcceptor getFileAcceptor() {
		if (fileAcceptor == null) {
			fileAcceptor = new FileAcceptor() {
				protected boolean acceptImpl(File file) {
					return true;
				}
			};
		}
		return fileAcceptor;
	}
	
	public List<T> getFiles() {
		List<T> files = new ArrayList<T>();
		if (!hasDirectory()) {
			return files;
		}
		
		File[] filesInDirectory = getDirectory().listFiles();
		if (filesInDirectory == null) return files;
		
		for (File file : filesInDirectory) {
			if (getFileAcceptor().accept(file)) {
				files.add(createWrapperFile(file));
			}
		}
		return files;
	}

	protected abstract T createWrapperFile(File file);
	
	public boolean hasDirectory() {
		return getDirectory() != null;
	}
}
