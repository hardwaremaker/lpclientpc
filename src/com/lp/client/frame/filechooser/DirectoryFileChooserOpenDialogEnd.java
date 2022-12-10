package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.filechooser.open.DirectoryFile;

public class DirectoryFileChooserOpenDialogEnd extends FileChooserOpenDialogEnd<DirectoryFile> {
	
	public DirectoryFileChooserOpenDialogEnd(WrapperFileChooser fileChooser) {
		super(fileChooser);
	}

	@Override
	public DirectoryFile openSingle() {
		return createDirectory(chooseFile());
	}
	
	@Override
	public List<DirectoryFile> openMultiple() {
		List<DirectoryFile> wrapperFiles = new ArrayList<DirectoryFile>();
		List<File> files = chooseFiles();
		if (files == null) return wrapperFiles;
		
		for (File file : files) {
			wrapperFiles.add(createDirectory(file));
		}
		return wrapperFiles;
	}
	
	private DirectoryFile createDirectory(File directory) {
		return new DirectoryFile(directory);
	}

	@Override
	public DirectoryFile openSingleUnchecked() {
		// TODO Auto-generated method stub
		return null;
	}
}
