package com.lp.client.frame.filechooser.filter;

public enum FileExtension {
	PDF("pdf"),
	JPG("jpg"),
	JPEG("jpeg"),
	PNG("png"),
	TIF("tif"),
	TIFF("tiff"),
	CSV("csv"), 
	GIF("gif"), 
	VCF("vcf"),
	XLS("xls"),
	TXT("txt"), 
	XML("xml"),
	XLSX("xlsx"),
	INVALID("-invalid-");
	
	private String value;
	
	private FileExtension(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static FileExtension fromString(String extension) {
		if (extension != null) {
			for (FileExtension status : FileExtension.values()) {
				if (extension.toLowerCase().equals(status.value)) {
					return status;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + extension + "'");
	}
}
