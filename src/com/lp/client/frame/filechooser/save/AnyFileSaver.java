package com.lp.client.frame.filechooser.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class AnyFileSaver implements IDataSaver<File> {

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
	}
}
