package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import javax.swing.table.TableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.WebFindChipsDto;

public class WeblieferantenController implements INeueLieferantenModelController, INeueLieferantenViewController, 
		IBekannteLieferantenModelController, IBekannteLieferantenViewController, ItemChangedListener, IWebpartnerUpdateController {
//	private final static LpLogger myLogger = (LpLogger) LpLogger.getInstance(WeblieferantenController.class);
	
	private WebabfrageBaseModel baseModel;
	private BekannteLieferantenModel bekannteLieferantenModel;
	private NeueLieferantenModel neueLieferantenModel;
	
	private List<IDataUpdateListener> bekannteLieferantenUpdateListener;
	private List<IDataUpdateListener> neueLieferantenUpdateListener;
	private InternalFrame internalFrame;
	private PanelQueryFLR panelQueryFLRLieferant;
	private IWebpartnerDto webpartnerDto;

	public WeblieferantenController(InternalFrame internalFrame, WebabfrageBaseModel baseModel) throws ExceptionLP, Throwable {
		bekannteLieferantenUpdateListener = new ArrayList<IDataUpdateListener>();
		neueLieferantenUpdateListener = new ArrayList<IDataUpdateListener>();
		this.internalFrame = internalFrame;
		this.internalFrame.addItemChangedListener(this);
		this.baseModel = baseModel;
		neueLieferantenModel = new NeueLieferantenModel(this);
		bekannteLieferantenModel = new BekannteLieferantenModel(this);
		
		refreshLieferantenModels();
	}
	
	private void refreshNeueLieferantenModel() throws ExceptionLP, Throwable {
		List<IWebpartnerDto> list = getWebpartnerDtoListAll(false);
		List<IWebpartnerDto> findChipsList = new ArrayList<IWebpartnerDto>();

		for (IWebpartnerDto dto : list) {
			if (dto.getLieferantIId() == null) {
				findChipsList.add(dto);
			}
		}
		neueLieferantenModel.setWebpartner(findChipsList);
		notifyNeueLieferantenDataUpdateListener();
	}
	
	private List<IWebpartnerDto> getWebpartnerDtoList(Boolean loadWithDtos) throws ExceptionLP, Throwable {
		List<IWebpartnerDto> webpartnerDtoList = new ArrayList<IWebpartnerDto>();

		if (AngebotstklFac.WebabfrageTyp.FINDCHIPS == baseModel.getSelectedWebabfrageTyp()) {
			List<WebFindChipsDto> list = DelegateFactory.getInstance().getAngebotstklDelegate().webfindchipsFindByMandantCNr(loadWithDtos);
			webpartnerDtoList.addAll(list);
		}
		
		return webpartnerDtoList;
	}

	private List<IWebpartnerDto> getWebpartnerDtoListAll(Boolean loadWithDtos) throws ExceptionLP, Throwable {
		List<IWebpartnerDto> webpartnerDtoList = new ArrayList<IWebpartnerDto>();

		if (AngebotstklFac.WebabfrageTyp.FINDCHIPS == baseModel.getSelectedWebabfrageTyp()) {
			List<WebFindChipsDto> list = DelegateFactory.getInstance().getAngebotstklDelegate()
					.webfindchipsFindByMandantCNrWithNullLieferanten(loadWithDtos);
			webpartnerDtoList.addAll(list);
		}
		
		return webpartnerDtoList;
	}

	private void refreshBekannteLieferantenModel() throws ExceptionLP, Throwable {
		List<IWebpartnerDto> webpartner = getWebpartnerDtoList(true);
		List<EkweblieferantDto> ekweblieferanten = DelegateFactory.getInstance()
				.getAngebotstklDelegate().ekweblieferantFindByEinkaufsangebotIIdWebabfrageTyp(
						baseModel.getEinkaufsangebotIId(), baseModel.getSelectedWebabfrageTyp(), true);
		
		ekweblieferanten = Collections.synchronizedList(ekweblieferanten);
		baseModel.setEkweblieferanten(ekweblieferanten);
		bekannteLieferantenModel.initModel(webpartner, ekweblieferanten);
		notifyBekannteLieferantenDataUpdateListener();
	}
	
	private void refreshLieferantenModels() throws ExceptionLP, Throwable {
		refreshNeueLieferantenModel();
		refreshBekannteLieferantenModel();
	}

	@Override
	public void actionZuordnen(Integer webpartnerIId) {
		try {
			webpartnerDto = DelegateFactory.getInstance().getAngebotstklDelegate().webpartnerFindByPrimaryKey(webpartnerIId);
			panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
					.createPanelFLRLieferant(internalFrame,	null, false, false);
			new DialogQuery(panelQueryFLRLieferant);
		} catch (Throwable e) {
			handleThrowable(e);
		}
	}

	@Override
	public void registerNeueLieferantenDataUpdateListener(IDataUpdateListener listener) {
		neueLieferantenUpdateListener.add(listener);
	}

	public void notifyNeueLieferantenDataUpdateListener() {
		for (IDataUpdateListener listener : neueLieferantenUpdateListener) {
			listener.dataUpdated();
		}
	}

	@Override
	public TableModel getNeueLieferantenTableModel() {
		return neueLieferantenModel;
	}

	@Override
	public BekannteLieferantenModel getBekannteLieferantenTableModel() {
		return bekannteLieferantenModel;
	}

	@Override
	public void actionAddEkWeblieferant(Integer webpartnerIId) {
		try {
			EkweblieferantDto ekweblieferantDto = new EkweblieferantDto();
			ekweblieferantDto.setWebpartnerIId(webpartnerIId);
			ekweblieferantDto.setEinkaufsangebotIId(baseModel.getEinkaufsangebotIId());
			DelegateFactory.getInstance().getAngebotstklDelegate().createEkweblieferant(ekweblieferantDto);
			refreshBekannteLieferantenModel();
		} catch (Throwable t) {
			handleThrowable(t);
		}
	}

	@Override
	public void actionRemoveEkweblieferant(Integer weblieferantIId) {
		try {
			DelegateFactory.getInstance().getAngebotstklDelegate().removeEkweblieferant(weblieferantIId);
			refreshBekannteLieferantenModel();
		} catch (Throwable t) {
			handleThrowable(t);	
		}
	}

	@Override
	public void registerBekannteLieferantenDataUpdateListener(IDataUpdateListener listener) {
		bekannteLieferantenUpdateListener.add(listener);
	}

	public void notifyBekannteLieferantenDataUpdateListener() {
		for (IDataUpdateListener listener : bekannteLieferantenUpdateListener) {
			listener.dataUpdated();
		}
	}

	@Override
	public void changed(EventObject e) {
		if (e == null) return;
		
		if (e.getSource() == panelQueryFLRLieferant && panelQueryFLRLieferant.getSelectedId() != null) {
			Integer selectedId = (Integer) panelQueryFLRLieferant.getSelectedId();
			webpartnerDto.setLieferantIId(selectedId);
			try {
				DelegateFactory.getInstance().getAngebotstklDelegate().updateWebpartner(webpartnerDto);
				refreshLieferantenModels();
			} catch (Throwable t) {
				handleThrowable(t);
			}
		}
		
	}
	
	private void handleThrowable(Throwable t) {
		// TODO!!!!
		LpLogger.getInstance(this.getClass()).error(t);
	}

	/* 
	 * IWeblieferantenUpdateController Methoden
	*/

	@Override
	public Integer actionUpdateWebpartner(IWebpartnerDto webpartnerDto) {
		Integer iId = null;
		if (!neueLieferantenModel.isWebpartnerInModel(webpartnerDto) 
				&& !bekannteLieferantenModel.isWebpartnerInModel(webpartnerDto)) {
			try {
				iId = DelegateFactory.getInstance().getAngebotstklDelegate().createWebpartner(webpartnerDto);
				refreshNeueLieferantenModel();
			} catch (Throwable e) {
				handleThrowable(e);
			}
		} else {
			iId = bekannteLieferantenModel.getWebpartnerIId(webpartnerDto);
		}
		
		return iId;
	}

	@Override
	public List<EkweblieferantDto> getEkweblieferanten() {
		return bekannteLieferantenModel.getEkweblieferanten();
	}

}
