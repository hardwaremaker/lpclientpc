package com.lp.client.frame.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.lp.client.frame.filechooser.WrapperFileChooser.SelectResult;

public abstract class FileChooserOpenDialogEnd<T> implements ChooserOpenDialogEnd<T> {
	private WrapperFileChooser fileChooser;
	private SingleFileChooser singleFileChooser;
	private MultipleFileChooser multipleFileChooser;

	public FileChooserOpenDialogEnd(WrapperFileChooser fileChooser) {
		this.fileChooser = fileChooser;
		singleFileChooser = new SingleFileChooser();
		multipleFileChooser = new MultipleFileChooser();
	}

	protected File chooseFile() {
		return singleFileChooser.choose();
	}

	protected List<File> chooseFiles() {
		return multipleFileChooser.choose();
	}

	@Override
	public WrapperFileChooser build() {
		return fileChooser;
	}

	private class SingleFileChooser implements Function<File, SelectResult>, ICanceled {
		private File file;

		@Override
		public void canceled() {
			file = null;
		}

		public File choose() {
			fileChooser.choose(fileChooser.createSingleFileHandlerExisiting(this), this);
			return file;
		}

		@Override
		public SelectResult apply(File t) {
			file = t;
			return SelectResult.ACCEPT;
		}
	}

	private class MultipleFileChooser implements Function<List<File>, SelectResult>, ICanceled {
		private List<File> files = null;

		@Override
		public void canceled() {
			files = new ArrayList<File>();
		}

		public List<File> choose() {
			fileChooser.chooseMultiple(fileChooser.createMultiFileHandlerExisting(this), this);
			return files;
		}

		@Override
		public SelectResult apply(List<File> t) {
			files = t;
			return SelectResult.ACCEPT;
		}
	}
}
