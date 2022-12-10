package com.lp.client.frame.filechooser;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.lp.server.util.HvOptional;
import com.lp.server.util.Validator;

public class SwingFileChooser implements IFileChooser {
	private final JFileChooser fileChooser;
	
	public SwingFileChooser() {
		this(new JFileChooser());
	}
	
	public SwingFileChooser(JFileChooser chooser) {
		this.fileChooser = chooser;
	}
	
	@Override
	public void locale(Locale locale) {
		Validator.notNull(locale, "locale");
		fileChooser().setLocale(locale);
		fileChooser().updateUI();
	}
	
	@Override
	public void title(String title) {
		Validator.dtoNotNull(title, "title");
		fileChooser().setDialogTitle(title);
	}
	
	@Override
	public void selectionMode(FileSelectionMode fileSelectionMode) {
		fileChooser().setFileSelectionMode(fileSelectionMode.getValue());
	}
	
	@Override
	public FileSelectionMode selectionMode() {
		return FileSelectionMode.fromInteger(
				fileChooser().getFileSelectionMode());
	}
	
	@Override
	public void beMultiSelect() {
		fileChooser().setMultiSelectionEnabled(true);
	}	
	
	@Override
	public void beSingleSelect() {
		fileChooser().setMultiSelectionEnabled(false);
	}
	
	@Override
	public void enableMultiSelect(boolean multiSelect) {
		fileChooser().setMultiSelectionEnabled(multiSelect);
	}

	@Override
	public FileFilter fileFilter() {
		return fileChooser().getFileFilter();
	}
	
	@Override
	public void fileFilter(final FileFilter newFilter) {
		fileChooser().setFileFilter(newFilter);
	}
	
	@Override
	public void enableAllFileFilter() {
		fileChooser().setAcceptAllFileFilterUsed(true);
	}
	
	@Override
	public void disableAllFileFilter() {
		fileChooser().setAcceptAllFileFilterUsed(false);
	}
	
	@Override
	public boolean isAllFileFilterUsed() {
		return fileChooser().isAcceptAllFileFilterUsed();
	}
	
	@Override
	public List<FileFilter> choosableFileFilters() {
		FileFilter[] filters = fileChooser().getChoosableFileFilters();
		return filters == null 
				? new ArrayList<FileFilter>() 
				: Arrays.asList(filters);
	}
	
	@Override
	public void addChoosableFileFilter(FileFilter filter) {
		Validator.notNull(filter, "filter");
		fileChooser().addChoosableFileFilter(filter);
	}
	
	
	@Override
	public File selectedFile() {
		return fileChooser().getSelectedFile();
	}
	
	@Override
	public List<File> selectedFiles() {		
		File[] choosen = fileChooser().getSelectedFiles();
		return choosen == null ? new ArrayList<File>() : Arrays.asList(choosen);
	}
	
	@Override
	public void selectedFile(File file) {
		Validator.notNull(file, "file");
		fileChooser().setSelectedFile(file);
	}
	
	@Override
	public void currentDirectory(File directory) {
		Validator.notNull(directory, "directory");
		fileChooser().setCurrentDirectory(directory);
	}
	
	@Override
	public void removeAllFileFilters() {
		fileChooser().setAcceptAllFileFilterUsed(false);
		FileFilter[] all = fileChooser().getChoosableFileFilters();
		for (FileFilter fileFilter : all) {
			fileChooser().removeChoosableFileFilter(fileFilter);
		}		
	}
	
	@Override
	public int showOpenDialog(HvOptional<Component> parent) {
		return fileChooser().showOpenDialog(parent.orElse(null));
	}
	
	@Override
	public int showSaveDialog(HvOptional<Component> parent) {
		return fileChooser().showSaveDialog(parent.orElse(null));
	}
	
	@Override
	public void prompt(String buttonText) {
		fileChooser.setApproveButtonText(buttonText);
	}
	
	private JFileChooser fileChooser() {
		return fileChooser;
	}
}
