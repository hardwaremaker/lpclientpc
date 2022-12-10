package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import javax.swing.filechooser.FileFilter;

public class FileSaver extends FileFilter implements IDataSaver<File>  {

	private final String extension;
	
	public FileSaver(String extension) {
		this.extension = extension;
	}
	
	@Override
	public void save(File data, File file) throws Exception {
		try(
			FileInputStream src = new FileInputStream(data);
			FileOutputStream dest = new FileOutputStream(file)) {

			FileChannel srcChannel = src.getChannel();
			FileChannel destChannel = dest.getChannel();
			long size = srcChannel.size();
			srcChannel.transferTo(0, size, destChannel);
		}
/*		
		FileInputStream src = null;
		FileOutputStream dest = null;
		try {
			src = new FileInputStream(data);
			dest = new FileOutputStream(file);
			FileChannel srcChannel = src.getChannel();
			FileChannel destChannel = dest.getChannel();
			long size = srcChannel.size();
			srcChannel.transferTo(0, size, destChannel);
		} finally {
			if (src != null) {
				try {
					src.close();
				} catch (IOException e) {
				}
			}
			if (dest != null) {
				try {
					dest.close();
				} catch (IOException e) {
				}
			}
		}	
*/		
	}
	
	@Override
	public boolean accept(File f) {
		return f != null && (f.isDirectory() ||
				f.getName().toLowerCase().endsWith(extension.toLowerCase()));
	}
	
	private String getFileType() {
		String s = extension;
		while(s.startsWith(".")) {
			s = s.substring(1);
		}
		
		return s.toUpperCase();
	}
	
	private String getExtension() {
		return extension.startsWith(".") ? extension : ("." + extension);
	}
	
	@Override
	public String getDescription() {
		return getFileType() + "-Dateien (*" +  getExtension() + ")";
	}
}
