package com.lp.client.frame;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import com.lp.client.frame.component.FileValidator;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.frame.report.DialogAnhaenge;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

public class AttachmentDialog {

	private List<String> attachmentPaths;
	private final String attachmentSeparator = ";";
	
	public AttachmentDialog() {
	}
	
	public AttachmentDialog(String attachments) {
		addAttachments(attachments);
	}

	public List<String> getAttachmentPaths() {
		if (attachmentPaths == null) {
			attachmentPaths = new ArrayList<String>();
		}
		return attachmentPaths;
	}
	
	private void addAttachments(String attachments) {
		if (Helper.isStringEmpty(attachments)) return;
		
		String[] parts = attachments.split(attachmentSeparator);
		for (String path : parts) {
			getAttachmentPaths().add(path);
		}
	}

	public boolean chooseFiles(JComponent parent) {
		List<WrapperFile> chosenFiles = FileChooserBuilder
				.createOpenDialog(FileChooserConfigToken.EmailAttachment, parent)
				.addAllFileFilter()
				.openMultiple();
		if (chosenFiles.isEmpty()) {
			return false;
		}
		
		List<String> validPaths = new ArrayList<String>();
		FileValidator fileValidator = new FileValidator();
		for (WrapperFile file : chosenFiles) {
			if (!fileValidator.validateFileExistence(file.getFile())) {
				return false;
			}
			validPaths.add(file.getFile().getAbsolutePath());
		}
		
		getAttachmentPaths().addAll(validPaths);
		return true;
	}
	
	public String getAttachmentPathsAsString() {
		StringBuilder builder = new StringBuilder();
		int numberOfPaths = getAttachmentPaths().size();
		for (int idx = 0; idx < numberOfPaths; idx++) {
			builder.append(getAttachmentPaths().get(idx));
			if (idx < numberOfPaths - 1) {
				builder.append(attachmentSeparator);
			}
		}
		return builder.toString();
	}
	
	public boolean remove() throws Throwable {
		if (getAttachmentPaths().isEmpty()) {
			return false;
		}
		
		DialogAnhaenge da = new DialogAnhaenge(getAttachmentPaths().toArray(new String[] {}));
		LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(da);
		da.setVisible(true);
		
		getAttachmentPaths().clear();
		getAttachmentPaths().addAll(da.getAnhaenge());
		
		return true;
	}
}
