package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.List;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.assistent.IGotoPages;
import com.lp.server.angebotstkl.service.WebabfragepositionDto;

public class WebabfragePage1Ctrl extends AssistentPageController {

	private WebabfrageBaseModel model;
	private IGotoPages pageNavigator;
	
	public WebabfragePage1Ctrl(WebabfrageBaseModel model, IGotoPages pageNavigator) {
		this.model = model;
		this.pageNavigator = pageNavigator;
	}

	@Override
	public boolean isNextAllowed() {
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}

	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() throws Throwable {
		convertEinkaufsangebotpositionen();
	}

	@Override
	public void activateByPrev() throws Throwable {
	}

	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() throws ExceptionLP, Throwable {
		return false;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return false;
	}

	protected WebabfrageBaseModel getModel() {
		return model;
	}
	
	protected void gotoNextPage() throws Throwable {
		pageNavigator.gotoNextPage();
	}

	private void convertEinkaufsangebotpositionen() throws ExceptionLP, Throwable {
		List<WebabfragepositionDto> positionen = model.getPositionen();
		List<IWebabfrageResult> results = new ArrayList<IWebabfrageResult>();

		for (WebabfragepositionDto dto : positionen) {
			IWebabfrageResult result = new WebabfrageResult();
			result.setEinkaufsangebotpositionDto(dto.getEinkaufsangebotpositionDto());
			result.setArtikelDto(dto.getArtikelDto());
			result.setArtikellieferantDtos(dto.getArtikellieferantDtos());
			results.add(result);
		}
		
		model.setResults(new CondensedWebabfrageResultList(results));
	}

}
