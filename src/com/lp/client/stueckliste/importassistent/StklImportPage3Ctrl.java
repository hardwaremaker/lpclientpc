/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
 package com.lp.client.stueckliste.importassistent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.component.WrapperTristateCheckbox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.StuecklisteDelegate;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikelsprDto;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.service.MandantDto;
import com.lp.util.Helper;

public abstract class StklImportPage3Ctrl extends AssistentPageController {
	
	protected static final int LINES_PER_SERVER_CALL = 5;
	
	public static final ArtikelDto HANDARTIKEL = getHandartikel();
	public static final ArtikelDto FLR_LISTE = getFLRAuswahl();
	public static final ArtikelDto ZUVIELE_ARTIKEL_FLR_LISTE = getZuvieleArtikel();
	public static final ArtikelDto ARTIKEL_ERZEUGEN = getArtikelErzeugen();
	
	protected StklImportModel model;
	protected int progress = 0;
	protected boolean isBusyImporting = true;
	protected BoundedRangeModel boundedRangeModel;
	protected boolean zusammengefasst = true;
	protected int cbMappingUpdateState = WrapperTristateCheckbox.SELECTED;
	protected SwingWorker<List<IStklImportResult>, Void> worker;
	protected IStklImportResult resultWaitingForArtikelIId;
	protected INeedArtikelAuswahlListener artikelAuswahlListener;
	private Boolean demo = null;
	
	public StklImportPage3Ctrl(StklImportModel model) {
		this.model = model;
	}

	private static ArtikelDto getHandartikel() {
		ArtikelDto artikel = new ArtikelDto();
		artikel.setCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
		artikel.setArtikelartCNr(ArtikelFac.ARTIKELART_HANDARTIKEL);
		return artikel;
	}
	
	private static ArtikelDto getFLRAuswahl() {
		ArtikelDto artikel = new ArtikelDto();
		artikel.setCNr(LPMain.getTextRespectUISPr("title.artikelauswahlliste"));
		return artikel;
	}
	
	private static ArtikelDto getZuvieleArtikel() {
		ArtikelDto artikel = new ArtikelDto();
		artikel.setCNr(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.zuvieletreffer"));
		artikel.setArtikelsprDto(new ArtikelsprDto());
		artikel.getArtikelsprDto().setCBez(LPMain.getTextRespectUISPr("title.artikelauswahlliste"));
		return artikel;
	}

	@Override
	public boolean isNextAllowed() {
		if(isBusyImporting) return false;
		if(model == null || model.getResults() == null) return false;
		for(IStklImportResult res : model.getResults()) {
			if(res.getSelectedIndex() == null || res.getSelectedIndex() == -1)
				return false;
		}
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return true;
	}

	@Override
	public boolean isCancelAllowed() {
		return true;
	}

	@Override
	public void activateByNext() {
		isBusyImporting = true;
		setProgressValue(0);
		try {
			if(worker != null)
				worker.cancel(true);
			worker = new SearchWorker(model);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		worker.addPropertyChangeListener(new ProgressListener());
		worker.execute();
	}

	@Override
	public void activateByPrev() {
	}
	
	@Override
	public boolean nextPageIfPossible() throws ExceptionLP, Throwable {
		return true;
	}

	@Override
	public boolean prevPageIfPossible() {
		worker.cancel(true);
		return true;
	}
	
	@Override
	public boolean cancelIfPossible() {
		worker.cancel(true);
		return true;
	}
	
	public BoundedRangeModel getProgressModel() {
		if(boundedRangeModel == null) boundedRangeModel = new DefaultBoundedRangeModel(progress, 1, 0, 100);
		return boundedRangeModel;
	}
	
	public abstract TableModel getResultTableModel();
	
	public void tableModelValueChanged() {
		updateCheckboxStatus();
		fireNavigationUpdateEvent();
		fireDataUpdateEvent();
	}
	
	protected void setBusyImporting(boolean isBusyImporting) {
		this.isBusyImporting = isBusyImporting;
		if(!isBusyImporting) boundedRangeModel = null;
		fireDataUpdateEvent();
	}
	
	protected void setProgressValue(int progress) {
		this.progress = progress;
		boundedRangeModel.setValue(progress);
		fireDataUpdateEvent();
	}
	
	public void setZusammengefasst(boolean b) {
		zusammengefasst = b;
		fireDataUpdateEvent();
	}
	
	public boolean isZusammengefasst() {
		return zusammengefasst;
	}
	
	public void undefinierteMitHandartikelBefuellen() {
		for(IStklImportResult res : model.getResults()) {
			if(res.getSelectedIndex() == null || res.getSelectedIndex() == -1)
				res.setSelectedIndex(res.getFoundItems().indexOf(HANDARTIKEL));
		}
		tableModelValueChanged();
	}
	
	public boolean isBusyImporting() {
		return isBusyImporting;
	}
	
	protected boolean isWaitingForArtikelIId() {
		return getResultWaitingForArtikelIId() != null;
	}
	
	public void setChosenArtikelIId(Integer iid) {
		if(isWaitingForArtikelIId()) {
			if(iid == null) {
				getResultWaitingForArtikelIId().setSelectedIndex(-1);
			} else {
				try {
					ArtikelDto artikel = 
							DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(iid);
					List<ArtikelDto> foundItems = new ArrayList<ArtikelDto>(
							getResultWaitingForArtikelIId().getFoundItems());
					foundItems.add(0, artikel);
					getResultWaitingForArtikelIId().setFoundItems(foundItems);
					getResultWaitingForArtikelIId().setSelectedIndex(0);
					doSomethingWithJustChosenArtikel(iid);
				} catch (Throwable e) {
					getResultWaitingForArtikelIId().setSelectedIndex(null);
				}
			}
		}
	}
	
	/**
	 * Zu &uuml;berschreibende Methode, wenn mit dem gerade aus dem Auswahlfenster
	 * ausgew&auml;hlten Artikel noch etwas getan werden muss.
	 * 
	 * @param iid Id des gerade ausgew&auml;hlten Artikels
	 */
	protected void doSomethingWithJustChosenArtikel(Integer iid) {
		//wenn weitere Schritte mit dem gerade ausgew&auml;hlten Artikel n&ouml;tig sind
		//dann diese Methode &uuml;berschreiben
	}

	public void setResultWaitingForArtikelIId(
			IStklImportResult resultWaitingForArtikelIId) {
		this.resultWaitingForArtikelIId = resultWaitingForArtikelIId;
		if(getArtikelAuswahlListener() != null) {
			getArtikelAuswahlListener().waehleArtikelAus();
		}
	}
	
	public IStklImportResult getResultWaitingForArtikelIId() {
		return resultWaitingForArtikelIId;
	}

	public INeedArtikelAuswahlListener getArtikelAuswahlListener() {
		return artikelAuswahlListener;
	}

	public void setArtikelAuswahlListener(INeedArtikelAuswahlListener artikelAuswahlListener) {
		this.artikelAuswahlListener = artikelAuswahlListener;
	}

	protected class SearchWorker extends SwingWorker<List<IStklImportResult>, Void> {
		
		private StklImportModel model;
		private StuecklisteDelegate delegate;
		
		public SearchWorker(StklImportModel model) throws Throwable {
			this.model = model;
			 delegate = DelegateFactory.getInstance().getStuecklisteDelegate();
		}

		@Override
		protected List<IStklImportResult> doInBackground() throws Exception {
			List<IStklImportResult> results = new ArrayList<IStklImportResult>();
			List<String> importLines = model.getImportLines();
			for(int i = 0; i < importLines.size(); i+= LINES_PER_SERVER_CALL) {
				if(isCancelled()) return new ArrayList<IStklImportResult>();
				int range = i + LINES_PER_SERVER_CALL > importLines.size() ? importLines.size() - i : LINES_PER_SERVER_CALL;
				List<String> lines = new ArrayList<String>(importLines.subList(i, i+range));
				try {
					results.addAll(delegate.searchForImportMatches(model.getSelectedSpezifikation(), lines, i));
				} catch (ExceptionLP e) {
					e.printStackTrace();
				}
				setProgress((int)(i*100f/model.getImportLines().size()));
			}
			
			for(IStklImportResult result : results) {
				if(!result.isTotalMatch()) {
					result.getFoundItems().add(HANDARTIKEL);
					if (isDemo()) {
						result.getFoundItems().add(ARTIKEL_ERZEUGEN);
					}
					
					if(result.foundTooManyArticles()) {
						result.getFoundItems().add(ZUVIELE_ARTIKEL_FLR_LISTE);
					} else {
						result.getFoundItems().add(FLR_LISTE);
					}
				}
			}
			return results;
		}
		
		@Override
		protected void done() {
			try {
				if(!isCancelled())
				model.setResults(get());
				updateCheckboxStatus();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			setBusyImporting(false);
			fireNavigationUpdateEvent();
		}
	}
	
	protected void updateCheckboxStatus() {
		setMappingUpdateStatusAusStklImportResults();
	}
	
	protected class ProgressListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if("progress".equals(e.getPropertyName())) {
				setProgressValue((Integer)e.getNewValue());
			} else if("state".equals(e.getPropertyName())) {
				setBusyImporting(SwingWorker.StateValue.STARTED.equals(e.getNewValue())
						|| SwingWorker.StateValue.PENDING.equals(e.getNewValue()));
			}
		}
	}

	/**
	 * Selektiert die Artikelnummer Mapping Updates aller importierten Stklresults
	 * in der Tabelle. Sind diese unterschiedlich gesetzt (HALFSELECTED) werden alle 
	 * auf true gesetzt.
	 * 
	 * @param status gibt den Status der Tristate Checkbox an, nach dem alle
	 * Mapping Updates gesetzt werden
	 */
	public void selektiereMappingUpdate(int status) {
		for(IStklImportResult res : model.getResults()) {
			if(status == WrapperTristateCheckbox.SELECTED)
				res.setUpdateArtikelnummerMapping(false);
			else
				res.setUpdateArtikelnummerMapping(true);
		}
		setMappingUpdateStatusAusStklImportResults();
		fireDataUpdateEvent();
	}
	
	/**
	 * Berechnet den Status der Tristate Checkbox des MappingUpdate der
	 * Artikelnummern
	 * 
	 * @return Status der Mapping Updates aller Artikel in der Tabelle:
	 * 	SELECTED, wenn alle auf true sind;
	 * 	DESELECTED, wenn alle auf false sind;
	 * 	HALFSELECTED, wenn sich welche unterscheiden;
	 * 	DISABLE, wenn keine Imports geladen oder nur TotalMatches und/oder Handartikel gefunden wurden.
	 */
	public void setMappingUpdateStatusAusStklImportResults() {
		TableModel resultTableModel = getResultTableModel();
		if(resultTableModel instanceof ResultTableModel
				&& ((ResultTableModel) resultTableModel).bezugsobjektArtikelnummerColumnTypeExists()
				&& model.getResults() != null) {
			IStklImportResult lastStklImportResult = null;
			Iterator<IStklImportResult> iter = model.getResults().iterator();

			while(iter.hasNext()) {
				IStklImportResult res = iter.next();
				if(res.getSelectedArtikelDto() != null 
						&& ArtikelFac.ARTIKELART_HANDARTIKEL.equals(res.getSelectedArtikelDto().getArtikelartCNr())) {
					continue;
				}
				if(lastStklImportResult != null) {
					if(res.isUpdateArtikelnummerMapping() != lastStklImportResult.isUpdateArtikelnummerMapping()) {
						cbMappingUpdateState = WrapperTristateCheckbox.HALFSELECTED;
						return;
					}
				}
				lastStklImportResult = res;
			}
			
			if(lastStklImportResult != null) {
				cbMappingUpdateState = lastStklImportResult.isUpdateArtikelnummerMapping() 
						? WrapperTristateCheckbox.SELECTED : WrapperTristateCheckbox.DESELECTED;
				return;
			}
		}
		cbMappingUpdateState = WrapperTristateCheckbox.DISABLE;
	}
	
	public int getMappingUpdateStatus() {
		return cbMappingUpdateState;
	}
	
	public void setUpdateArtikel(boolean selected) {
		model.setUpdateArtikel(selected);		
	}
	
	public abstract String getMappingUpdateTristateCheckboxText();
	
	public Integer getStklIId() {
		return model.getStklIId();
	}

	public void undefinierteAlsArtikelerzeugen() {
		for(IStklImportResult res : model.getResults()) {
			if(res.getSelectedIndex() == null || res.getSelectedIndex() == -1) {
				res.setSelectedIndex(res.getFoundItems().indexOf(ARTIKEL_ERZEUGEN));
			}
		}
		tableModelValueChanged();
	}

	private static ArtikelDto getArtikelErzeugen() {
		ArtikelDto artikel = new ArtikelDto();
		artikel.setCNr(LPMain.getTextRespectUISPr("stkl.intelligenterstklimport.neuerartikel"));
		artikel.setArtikelartCNr(ArtikelFac.ARTIKELART_ARTIKEL);
		return artikel;
	}
	
	public boolean isDemo() {
		if (demo == null) {
			try {
				demo = LPMain.getInstance().getDesktopController().hatTestFeature();
			} catch (Throwable e) {
				demo = Boolean.FALSE;
			}
		}
		return demo;
	}
}
