package com.lp.client.frame.filechooser.filter;

import com.lp.client.pc.LPMain;

public class HvTaggedImageFilter extends HvTaggedFileFilter {

	public HvTaggedImageFilter() {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.JPG)
				.add(FileExtension.JPEG)
				.add(FileExtension.PNG)
				.add(FileExtension.TIF)
				.add(FileExtension.TIFF)
				.add(FileExtension.GIF));
	}
	
	@Override
	public String getTag() {
		return LPMain.getTextRespectUISPr("lp.datei.filter.tag.bilder");
	}
}
