package com.lp.client.frame.component;

import java.io.File;

public interface IFileController {

	void setFile(File file);
	
	void setFile(String path);
	
	File getFile();
}
