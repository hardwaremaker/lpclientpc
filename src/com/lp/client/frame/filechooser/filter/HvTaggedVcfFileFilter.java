package com.lp.client.frame.filechooser.filter;

import com.lp.client.pc.LPMain;

public class HvTaggedVcfFileFilter extends HvTaggedFileFilter {

	public HvTaggedVcfFileFilter() {
		super(new HvTaggedFileExtensionAcceptor(FileExtension.VCF));		
	}
	
	@Override
	public String getTag() {
		return LPMain.getTextRespectUISPr("lp.datei.filter.tag.vcf");
	}
}
