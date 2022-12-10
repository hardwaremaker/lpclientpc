package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.function.Function;

import com.lp.client.frame.filechooser.WrapperFileChooser.SelectResult;

public interface ChooserSaveDialogEnd<T> extends ChooserDialogEnd {

	File save(T data) throws Exception;
	
	void choose(Function<File, SelectResult> approved);
	
	void choose(Function<File, SelectResult> approvedCallback, ICanceled canceledCallback);
}
