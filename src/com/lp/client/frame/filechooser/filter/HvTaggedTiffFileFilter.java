package com.lp.client.frame.filechooser.filter;

import com.lp.client.pc.LPMain;

public class HvTaggedTiffFileFilter extends HvTaggedFileFilter {

	public HvTaggedTiffFileFilter() {
		super(new HvTaggedFileExtensionAcceptor(
				FileExtension.TIFF).add(FileExtension.TIF));
	}
	
	@Override
	public String getTag() {
		return LPMain.getTextRespectUISPr("lp.datei.filter.tag.tiff");
	}
}
