package com.lp.client.frame.filechooser.save;

import java.io.File;

public interface IDataSaver<T> {

	void save(T data, File file) throws Exception;
}
