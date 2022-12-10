package com.lp.client.frame.component;

import java.io.File;
import java.math.BigDecimal;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;

public class FileValidator {

	public FileValidator() {
	}

	public boolean validateFileExistence(File file) {
		return checkAndShowErrorIfFileNotExist(file);
	}
	
	public boolean validateFileExistenceAndExtension(File file, FileNameExtensionFilter filter) {
		return checkAndShowErrorIfFileNotExist(file) 
				&& checkAndShowErrorIfFileFilterNotMatch(file, filter);
	}
	
	public boolean validateFileSize(BigDecimal size) throws Throwable {
		return checkAndShowErrorIfFileSizeTooBig(size);
	}
	
	public boolean checkAndShowErrorIfFileFilterNotMatch(File file,
			FileNameExtensionFilter fileFilter) {
		if (fileFilter == null || file == null) return false;
		
		if (!fileFilter.accept(file)) {
			StringBuilder sb = new StringBuilder();
			for (int idx=0; idx < fileFilter.getExtensions().length; idx++) {
				if (idx > 0) sb.append(", ");
				sb.append(fileFilter.getExtensions()[idx]);
			}
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getMessageTextRespectUISPr("lp.error.dateifalschermimetype", 
							file.getName(), sb.toString()));
			return false;
		}
		
		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean checkAndShowErrorIfFileNotExist(File file) {
		if (file == null) return false;
		
		if (!file.exists()) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.warning"),
					LPMain.getMessageTextRespectUISPr("lp.warning.dateinichtvorhanden", file.getAbsolutePath()));
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param groesseInKB
	 * @throws Throwable
	 */
	public boolean checkAndShowErrorIfFileSizeTooBig(BigDecimal size) throws Throwable {
		Integer maxSize = DelegateFactory.getInstance().getParameterDelegate().getMaximaleDokumentengroesse();
		if (maxSize != null && new BigDecimal(maxSize).compareTo(size) < 0) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.error"),
					LPMain.getTextRespectUISPr(
							"lp.error.dateizugross"));
			return false;
		}
		return true;
	}
}
