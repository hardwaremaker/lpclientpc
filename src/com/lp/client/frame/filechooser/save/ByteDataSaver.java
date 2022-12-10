package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileOutputStream;

public class ByteDataSaver implements IDataSaver<byte[]> {

	@Override
	public void save(byte[] data, File file) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
	}

}
