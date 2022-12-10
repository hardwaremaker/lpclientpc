package com.lp.client.angebotstkl.webabfrage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EkweblieferantDto;

public class FoundPartsController implements IFoundPartsViewController, IFoundPartsTableModelController {
	
	private WebabfrageBaseModel baseModel;
	private IFoundPartsTableModelListener foundPartsTablelistener;
	private FoundPartsTableModel foundPartsTableModel;
	private NormalizedWebabfragePart selectedPart;
	private Boolean isFilterNurWeblieferantenEnabled = false;
	private String filterBezeichnung = null;

	public FoundPartsController(WebabfrageBaseModel baseModel) {
		this.baseModel = baseModel;
		this.foundPartsTableModel = new FoundPartsTableModel(this);
	}

	@Override
	public void registerDataUpdateListener(IFoundPartsTableModelListener listener) {
		foundPartsTablelistener = listener;
	}

	@Override
	public List<NormalizedWebabfragePart> getParts() {
		List<NormalizedWebabfragePart> parts = baseModel.getSelectedResult() != null ? 
				baseModel.getSelectedResult().getFoundItems() : 
					new ArrayList<NormalizedWebabfragePart>();
		if (isFilterNurWeblieferantenEnabled) {
			parts = filterWeblieferanten(parts);
		}
		if (filterBezeichnung != null) {
			parts = filterBezeichnung(parts);
		}
		Collections.sort(parts, new Comparator<NormalizedWebabfragePart>() {

			@Override
			public int compare(NormalizedWebabfragePart part1,	NormalizedWebabfragePart part2) {
				return new Float(part1.getRate()).compareTo(new Float(part2.getRate())) * -1;
			}
		});
		return parts;
	}
	
	private List<NormalizedWebabfragePart> filterWeblieferanten(List<NormalizedWebabfragePart> parts) {
		List<NormalizedWebabfragePart> weblieferantenParts = new ArrayList<NormalizedWebabfragePart>();
		Map<Integer,EkweblieferantDto> ekweblieferanten = baseModel.getEkweblieferanten();
		
		for (NormalizedWebabfragePart part : parts) {
			if (part.getDistributor().getIId() != null 
					&& ekweblieferanten.containsKey(part.getDistributor().getIId())) {
				weblieferantenParts.add(part);
			}
		}
		
		return weblieferantenParts;
	}
	
	private List<NormalizedWebabfragePart> filterBezeichnung(List<NormalizedWebabfragePart> parts) {
		List<NormalizedWebabfragePart> bezeichnungParts = new ArrayList<NormalizedWebabfragePart>();
		String[] bezeichnungen = filterBezeichnung.split(" ");
		List<String> includedBez = new ArrayList<String>();
		List<String> excludedBez = new ArrayList<String>();
		for (String bez : bezeichnungen) {
			if (bez == null || bez.isEmpty()) continue;
			if (bez.startsWith("-")) {
				excludedBez.add(bez.substring(1));
			} else {
				includedBez.add(bez);
			}
		}
		
		for (NormalizedWebabfragePart part : parts) {
			boolean isMatch = true;
			String desc = part.getDescription();
			if (desc == null) continue;
			
			for (String bez : includedBez) {
				if (!desc.matches("(?i:.*" + bez + ".*)")) {
					isMatch = false;
					break;
				}
			}
			
			for (String bez : excludedBez) {
				if (desc.matches("(?i:.*" + bez + ".*)")) {
					isMatch = false;
					break;
				}
			}
			if (isMatch) bezeichnungParts.add(part);
		}
		
		return bezeichnungParts;
	}

	public void notifyTableUpdateListener() {
		if (foundPartsTablelistener != null) foundPartsTablelistener.dataUpdated();
	}

	@Override
	public IWebabfrageResult getSelectedPosition() {
		return baseModel.getSelectedResult();
	}

	@Override
	public FoundPartsTableModel getFoundPartsTableModel() {
		return foundPartsTableModel;
	}

	@Override
	public void actionResultSelected(IWebabfrageResult result) {
		baseModel.setSelectedResult(result);
		notifyTableUpdateListener();
	}

	@Override
	public void actionFoundPartSelected(NormalizedWebabfragePart part) {
		selectedPart = part;
	}

	@Override
	public void actionSetFoundPartToResult() {
		baseModel.getSelectedResult().setSelectedPart(selectedPart);
	}

	@Override
	public void actionFilterNurWeblieferanten() {
		isFilterNurWeblieferantenEnabled = true;
		notifyTableUpdateListener();
	}

	@Override
	public void actionFilterAlleLieferanten() {
		isFilterNurWeblieferantenEnabled = false;
		notifyTableUpdateListener();
	}

	@Override
	public void actionUrlButtonPressed(String url) {
		try {
			if (url == null) return;
			
	    	int i = url.indexOf("://");
	    	URI uri = new URI( (i < 0 ? "http://" : "") + url.trim()) ;
			java.awt.Desktop.getDesktop().browse(uri);
		} catch (URISyntaxException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("lp.fehlerhafteurl"));
		} catch (IOException ex1) {
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), ex1.getMessage());
		}
	}

	@Override
	public void actionFilterBezeichnung(String text) {
		if (text != null && text.trim().isEmpty()) text = null;
		
		filterBezeichnung = text;
		notifyTableUpdateListener();
	}

	@Override
	public Integer getSelectedResultRow() {
		return baseModel.getSelectedResult() == null ? null : baseModel.getResults().indexOf(baseModel.getSelectedResult());
	}

	@Override
	public void actionMengenstaffelSelected() {
		notifyTableUpdateListener();
	}

	@Override
	public BigDecimal getSelectedMenge() {
		return baseModel.getSelectedMenge();
	}

}
