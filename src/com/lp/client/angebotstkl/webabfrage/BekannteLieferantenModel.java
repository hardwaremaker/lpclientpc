package com.lp.client.angebotstkl.webabfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.lp.client.frame.assistent.view.IDataUpdateListener;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.EkweblieferantDto;
import com.lp.server.angebotstkl.service.IWebpartnerDto;
import com.lp.server.angebotstkl.service.WeblieferantDto;

public class BekannteLieferantenModel extends AbstractTableModel implements IDataUpdateListener {

	private static final long serialVersionUID = 1853836149178398623L;
	public static final int IDX_HELIUMNAME = 0;
	public static final int IDX_FAVORIT_EINKAUFSANGEBOT = 1;
	
	private String [] columnNames = new String[] {
			LPMain.getTextRespectUISPr("lp.lieferant"), LPMain.getTextRespectUISPr("agstkl.weblieferant")};
	private Class[] columnClasses = new Class[] {String.class, Boolean.class};
	
	private IBekannteLieferantenModelController controller;
	private WebpartnerManager manager;
	
	public BekannteLieferantenModel(IBekannteLieferantenModelController controller) {
		this.controller = controller;
		manager = new WebpartnerManager();
		
		this.controller.registerBekannteLieferantenDataUpdateListener(this);
	}
	
	public void initModel(List<IWebpartnerDto> webpartner, List<EkweblieferantDto> ekweblieferanten) {
		manager.setEkweblieferanten(ekweblieferanten);
		manager.setWebpartner(webpartner);
	}
	
	public Boolean isWebpartnerInModel(IWebpartnerDto webpartner) {
		if (webpartner == null || webpartner.getId() == null) return false;
		
		return manager.hasWebpartner(webpartner.getId());
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return manager.getWebpartner().size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= manager.getWebpartner().size()) return null;
		
		IWebpartnerDto dto = manager.getWebpartner().get(rowIndex);
		
		if (IDX_HELIUMNAME == columnIndex) {
			return dto.getLieferantDto().getPartnerDto().getCName1nachnamefirmazeile1();
		} else if (IDX_FAVORIT_EINKAUFSANGEBOT == columnIndex) {
			return manager.isWebpartnerEkweblieferant(dto.getIId());
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (IDX_FAVORIT_EINKAUFSANGEBOT == columnIndex) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (IDX_FAVORIT_EINKAUFSANGEBOT == columnIndex && aValue instanceof Boolean) {
			IWebpartnerDto webpartnerDto = manager.getWebpartner().get(rowIndex);
			if ((Boolean)aValue) {
				controller.actionAddEkWeblieferant(webpartnerDto.getIId());
			} else {
				WeblieferantDto weblieferantDto = manager.getEkweblieferantDto(webpartnerDto.getIId());
				controller.actionRemoveEkweblieferant(weblieferantDto.getIId());
			}
		}
	}

	@Override
	public void dataUpdated() {
		fireTableDataChanged();
	}
	
	public List<EkweblieferantDto> getEkweblieferanten() {
		return manager.getEkweblieferanten();
	}

	public Integer getWebpartnerIId(IWebpartnerDto webpartnerDto) {
		return manager.getWebpartnerIId(webpartnerDto.getId());
	}

	protected class WebpartnerManager {
		
		private List<IWebpartnerDto> webpartner;
		private Map<Integer, EkweblieferantDto> ekweblieferantenMap;
		
		public WebpartnerManager() {
			webpartner = Collections.synchronizedList(new ArrayList<IWebpartnerDto>());
			ekweblieferantenMap = Collections.synchronizedMap(new HashMap<Integer, EkweblieferantDto>());
		}

		public List<EkweblieferantDto> getEkweblieferanten() {
			return new ArrayList<EkweblieferantDto>(ekweblieferantenMap.values());
		}

		public void setWebpartner(List<IWebpartnerDto> webpartner) {
			this.webpartner.clear();
			for (IWebpartnerDto dto : webpartner) {
				if (dto.getLieferantIId() != null) {
					this.webpartner.add(dto);
				}
			}
			
			Collections.sort(this.webpartner, new Comparator<IWebpartnerDto>() {

				@Override
				public int compare(IWebpartnerDto webpartner1, IWebpartnerDto webpartner2) {
					Boolean isWebpartner1Ek = isWebpartnerEkweblieferant(webpartner1.getIId());
					Boolean isWebpartner2Ek = isWebpartnerEkweblieferant(webpartner2.getIId());
					
					if (isWebpartner1Ek && isWebpartner2Ek) {
						return getEkweblieferantDto(webpartner1.getIId()).getISort().compareTo(
							getEkweblieferantDto(webpartner2.getIId()).getISort());
					}
					
					return isWebpartner2Ek.compareTo(isWebpartner1Ek);
				}
			});
		}
		
		public List<IWebpartnerDto> getWebpartner() {
			return webpartner;
		}
	
		public void setEkweblieferanten(List<EkweblieferantDto> ekweblieferanten) {
			synchronized(ekweblieferantenMap) {
				ekweblieferantenMap.clear();
				for (EkweblieferantDto dto : ekweblieferanten) {
					ekweblieferantenMap.put(dto.getWebpartnerIId(), dto);
				}
			}
		}
		
		public Boolean isWebpartnerEkweblieferant(Integer webpartnerIId) {
			return ekweblieferantenMap.containsKey(webpartnerIId);
		}
		
		public Boolean hasWebpartner(String webpartnerId) {
			return getWebpartnerIId(webpartnerId) == null ? false : true;
		}
		
		public Integer getWebpartnerIId(String webpartnerId) {
			if (webpartnerId == null) return null;

			synchronized(webpartner) {
				for (IWebpartnerDto wp : webpartner) {
					if (webpartnerId.equals(wp.getId())) {
						return wp.getIId();
					}
				}
			}
			return null;
		}

		public EkweblieferantDto getEkweblieferantDto(Integer webpartnerIId) {
			return ekweblieferantenMap.get(webpartnerIId);
		}
		
	}

}
