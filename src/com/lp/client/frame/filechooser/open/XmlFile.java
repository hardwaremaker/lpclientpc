package com.lp.client.frame.filechooser.open;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class XmlFile extends WrapperFile {

	public XmlFile(File file) {
		super(file);
	}

	public String read(String charsetName) throws IOException {
		BufferedReader reader = null;
		
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(getInputStream(), charsetName);
			reader = new BufferedReader(inputStreamReader);
			StringBuilder builder = new StringBuilder();
			
			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}
			
			return builder.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
