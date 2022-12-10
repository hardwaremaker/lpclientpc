package com.lp.client.frame.filechooser;

import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.filechooser.save.IDataSaver;

public interface ChooserSaveDialogFilter<T> extends ChooserDialogFilterSelection<ChooserSaveDialogFilter<T>>, ChooserSaveDialog<T>, ChooserDialogEnd {

	<E extends FileFilter & IDataSaver<T>> ChooserSaveDialogFilter<T> addFileFilterSaver(E filter);

	WrapperSaveFileChooser<T> build();
}
