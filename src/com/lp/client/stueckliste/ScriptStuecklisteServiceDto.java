package com.lp.client.stueckliste;

import java.io.Serializable;
import java.util.List;

import com.lp.server.stueckliste.service.StuecklistearbeitsplanDto;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class ScriptStuecklisteServiceDto implements Serializable {
	private static final long serialVersionUID = -2470682965423273152L;

	private InternalFrameStueckliste internalFrame ;
	private Integer stuecklisteId ;
	private PanelStuecklistepositionenScript panelBasisScript ;
	private StuecklistepositionDto[]  stuecklistepositionenIn ;
	private List<StuecklistepositionDto> stuecklistepositionenOut ;
	private StuecklistearbeitsplanDto[] stuecklistearbeitsplanIn ;
	private List<StuecklistearbeitsplanDto> stuecklistearbeitsplanOut ;
	private boolean store ;
	
	public InternalFrameStueckliste getInternalFrame() {
		return internalFrame;
	}

	public void setInternalFrame(InternalFrameStueckliste internalFrame) {
		this.internalFrame = internalFrame;
	}

	public PanelStuecklistepositionenScript getPanelBasisScript() {
		return panelBasisScript;
	}

	public void setPanelBasisScript(PanelStuecklistepositionenScript panelBasisScript) {
		this.panelBasisScript = panelBasisScript;
	}

	public StuecklistepositionDto[] getStuecklistepositionenIn() {
		return stuecklistepositionenIn;
	}

	public void setStuecklistepositionenIn(StuecklistepositionDto[] stuecklistepositionenIn) {
		this.stuecklistepositionenIn = stuecklistepositionenIn;
	}

	public List<StuecklistepositionDto> getStuecklistepositionenOut() {
		return stuecklistepositionenOut;
	}

	public void setStuecklistepositionenOut(List<StuecklistepositionDto> stuecklistepositionenOut) {
		this.stuecklistepositionenOut = stuecklistepositionenOut;
	}

	public StuecklistearbeitsplanDto[] getStuecklistearbeitsplanIn() {
		return stuecklistearbeitsplanIn;
	}

	public void setStuecklistearbeitsplanIn(StuecklistearbeitsplanDto[] stuecklistearbeitsplanIn) {
		this.stuecklistearbeitsplanIn = stuecklistearbeitsplanIn;
	}

	public List<StuecklistearbeitsplanDto> getStuecklistearbeitsplanOut() {
		return stuecklistearbeitsplanOut;
	}

	public void setStuecklistearbeitsplanOut(
			List<StuecklistearbeitsplanDto> stuecklistearbeitsplanOut) {
		this.stuecklistearbeitsplanOut = stuecklistearbeitsplanOut;
	}

	public boolean isStore() {
		return store;
	}

	public void setStore(boolean store) {
		this.store = store;
	}

	public Integer getStuecklisteId() {
		return stuecklisteId;
	}

	public void setStuecklisteId(Integer stuecklisteId) {
		this.stuecklisteId = stuecklisteId;
	}
}
