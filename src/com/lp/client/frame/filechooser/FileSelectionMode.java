package com.lp.client.frame.filechooser;

import javax.swing.JFileChooser;

public enum FileSelectionMode {
	FILES(JFileChooser.FILES_ONLY),
	DIRECTORIES(JFileChooser.DIRECTORIES_ONLY),
	FILES_DIRECTORIES(JFileChooser.FILES_AND_DIRECTORIES);
	
	private Integer value;
	
	private FileSelectionMode(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public static FileSelectionMode fromInteger(Integer number) {
		if (number != null) {
			for (FileSelectionMode status : FileSelectionMode.values()) {
				if (number.equals(status.value)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + String.valueOf(number) + "'");
	}

}
