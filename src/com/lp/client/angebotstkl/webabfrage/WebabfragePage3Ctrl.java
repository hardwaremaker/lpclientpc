package com.lp.client.angebotstkl.webabfrage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.SwingWorker;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.AssistentPageController;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;

public class WebabfragePage3Ctrl extends AssistentPageController implements PropertyChangeListener {

	private WebabfrageBaseModel model;
	private Boolean isImporting = true;
	private Throwable importThrowable = null;
	private BoundedRangeModel progressModel;
	private Integer anzahlImportierterPositionen;
	private Integer progress = 0;
	
	public WebabfragePage3Ctrl(WebabfrageBaseModel model) {
		this.model = model;
	}

	public Boolean isImporting() {
		return isImporting;
	}

	public void setIsImporting(Boolean isImporting) {
		this.isImporting = isImporting;
	}

	public Throwable getImportThrowable() {
		return importThrowable;
	}

	public void setImportThrowable(Throwable importThrowable) {
		this.importThrowable = importThrowable;
	}

	public BoundedRangeModel getProgressModel() {
		if (progressModel == null) {
			progressModel = new DefaultBoundedRangeModel(progress, 1, 0, 100);
		}
		return progressModel;
	}

	public void setProgressModel(BoundedRangeModel progressModel) {
		this.progressModel = progressModel;
	}

	public void setAnzahlImportierterPositionen(Integer anzahlImportierterPositionen) {
		this.anzahlImportierterPositionen = anzahlImportierterPositionen;
	}

	public Integer getAnzahlImportierterPositionen() {
		return anzahlImportierterPositionen;
	}

	@Override
	public boolean isNextAllowed() {
		if (isImporting()) return false;
		
		return true;
	}

	@Override
	public boolean isPrevAllowed() {
		return false;
	}

	@Override
	public boolean isCancelAllowed() {
		return false;
	}

	@Override
	public void activateByNext() throws Throwable {
		setIsImporting(true);
		ImportWorker importWorker = new ImportWorker();
		importWorker.addPropertyChangeListener(this);
		
		importWorker.execute();
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
		return true;
	}

	@Override
	public boolean cancelIfPossible() throws ExceptionLP, Throwable {
		return false;
	}
	
	protected class ImportWorker extends SwingWorker<Integer, Void> {

		WebabfrageResultTransformer transformer;
		
		public ImportWorker() {
			transformer = new WebabfrageResultTransformer(model);
		}

		@Override
		protected Integer doInBackground() throws Exception {
			Integer positionen = 0;
			List<IWebabfrageResult> results = model.getResults();
			for (int resIdx = 0; resIdx < results.size(); resIdx++) {
				IWebabfrageResult result = results.get(resIdx);
				try {
					List<EinkaufsangebotpositionDto> positionDtos = transformer.transformWebabfrageResult(model.getEinkaufsangebotDto(), result);
					for (EinkaufsangebotpositionDto dto : positionDtos) {
						DelegateFactory.getInstance().getAngebotstklDelegate().updateEinkaufsangebotposition(dto);
						positionen++;
					}
				} catch (Throwable e) {
					setImportThrowable(e);
					return positionen;
				}
				setProgress((resIdx+1) * 100 / results.size());
			}
			
			return positionen;
		}

		@Override
		protected void done() {
			setIsImporting(false);
			try {
				setAnzahlImportierterPositionen(get());
			} catch (InterruptedException e) {
				setAnzahlImportierterPositionen(null);
				setImportThrowable(e);
			} catch (ExecutionException e) {
				setAnzahlImportierterPositionen(null);
				setImportThrowable(e);
			}
			
			fireDataUpdateEvent();
			fireNavigationUpdateEvent();
		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			progress = (Integer) evt.getNewValue();
			getProgressModel().setValue(progress);
			fireDataUpdateEvent();
		}
	}

}
