package com.lp.client.frame.filechooser.filter;

import com.lp.client.pc.LPMain;

public class HvTaggedPdfFileFilter extends HvTaggedFileFilter {

	public HvTaggedPdfFileFilter() {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.PDF));
	}
	
	@Override
	public String getTag() {
		return LPMain.getTextRespectUISPr("lp.datei.filter.tag.pdf");
	}
}
