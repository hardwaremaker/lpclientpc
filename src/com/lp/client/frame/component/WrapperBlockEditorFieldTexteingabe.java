package com.lp.client.frame.component;

import com.lp.server.system.service.ParameterFac;

public class WrapperBlockEditorFieldTexteingabe extends WrapperBlockEditorField {
	private static final long serialVersionUID = 1L;

	public WrapperBlockEditorFieldTexteingabe(InternalFrame internalFrameI, String addTitleI,
			Object keyWhenDetailPanelI) throws Throwable {
		super(internalFrameI, addTitleI, keyWhenDetailPanelI);
		setDocumentWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_TEXTEINGABE));
	}

	public WrapperBlockEditorFieldTexteingabe(InternalFrame internalFrameI, String addTitleI) throws Throwable {
		super(internalFrameI, addTitleI);
		setDocumentWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_TEXTEINGABE));
	}

}
