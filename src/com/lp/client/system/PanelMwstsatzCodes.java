package com.lp.client.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MwstsatzCodeDto;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;

public class PanelMwstsatzCodes extends PanelBasis {
	private static final long serialVersionUID = -4120512618215708936L;

	private Map<Integer, String> rcArten = new HashMap<Integer, String>();
	private Map<ReversechargeartId, SteuercodeRow> rcRows = new HashMap<ReversechargeartId, SteuercodeRow>();

	public PanelMwstsatzCodes(InternalFrame internalFrameI, String addTitleI) throws Throwable {
		super(internalFrameI, addTitleI);
		prepareReversechargearten();
		jbInit();
		initComponents();
	}

	private void jbInit() {
		HvLayout hvLayout = HvLayoutFactory.create(this, "ins 0", "6[fill][fill]15[fill]push", "5[][]");
		hvLayout
			.add(new JLabel(LPMain.getTextRespectUISPr("lp.reversecharge")), "gap bottom 10")
			.add(new JLabel(LPMain.getTextRespectUISPr("system.mwstsatz.steuercodear")))
			.add(new JLabel(LPMain.getTextRespectUISPr("system.mwstsatz.steuercodeer"))).wrap();
		
		for (Entry<Integer, String> rc : rcArten.entrySet()) {
			ReversechargeartId rcId = new ReversechargeartId(rc.getKey());
			SteuercodeRow row = new SteuercodeRow(rcId);
			row.setReversechargeBez(rc.getValue());
			rcRows.put(rcId, row);
			
			hvLayout.add(row.getComponentRcArt(), "width 200:300, growx")
				.add(row.getComponentCodeAr(), "wmin 120, wmax 150")
				.add(row.getComponentCodeEr(), "wmin 120, wmax 150, wrap");
		}
	}

	private void prepareReversechargearten() throws ExceptionLP, Throwable {
		rcArten = DelegateFactory.finanzservice().getAllReversechargeartAllowedMitVersteckten();
	}
	
	public void loadData(MwstsatzId mwstsatzId) throws ExceptionLP, Throwable {
		List<MwstsatzCodeDto> rcMwstsatzCodes = DelegateFactory.mandant().getAllReversechargeartMwstsatzCodeByMwstsatzId(mwstsatzId);
		for (MwstsatzCodeDto dto : rcMwstsatzCodes) {
			SteuercodeRow rowPanel = rcRows.get(dto.getReversechargeartId());
			if (rowPanel != null) {
				rowPanel.setMwstsatzCodeDto(dto);
			}
		}
	}
	
	public void saveData(MwstsatzId mwstsatzId) throws ExceptionLP, Throwable {
		List<MwstsatzCodeDto> updateList = new ArrayList<MwstsatzCodeDto>();
		rcRows.values().forEach(row -> {
			row.components2Dto(mwstsatzId);
			updateList.add(row.getMwstsatzCodeDto());
		});
		DelegateFactory.mandant().updateOrCreateMwstsatzCodes(updateList);
	}
	
	public void resetData() {
		rcRows.values().forEach(row -> row.resetData());
	}

	class SteuercodeRow extends JPanel {
		private static final long serialVersionUID = 8740189134886100367L;

		private ReversechargeartId rcId;
		private MwstsatzCodeDto codeDto;
		private WrapperLabel wlaRcArt;
		private JTextField wtfCodeAr;
		private WrapperTextField wtfCodeEr;
		
		public SteuercodeRow(ReversechargeartId rcId) {
			super();
			this.rcId = rcId;
			initComponents();
		}
		
		public void initComponents() {
			wtfCodeAr = new WrapperTextField(MandantFac.MAX_MWSTSATZCODE_STEUERCODE);
			wtfCodeEr = new WrapperTextField(MandantFac.MAX_MWSTSATZCODE_STEUERCODE);
			wlaRcArt = new WrapperLabel();
			wlaRcArt.setHorizontalAlignment(SwingConstants.LEFT);
		}
		
		public void setReversechargeBez(String rcBez) {
			wlaRcArt.setText(rcBez);
		}
		
		public void setMwstsatzCodeDto(MwstsatzCodeDto codeDto) {
			this.codeDto = codeDto;
			setCodeComponents();
		}
		public MwstsatzCodeDto getMwstsatzCodeDto() {
			return codeDto;
		}
		
		public void resetData() {
			resetCodeComponents();
			resetCodeDto();
		}
		
		private void resetCodeDto() {
			codeDto = new MwstsatzCodeDto(); 
			codeDto.setReversechargeartId(rcId);
		}

		private void setCodeComponents() {
			wtfCodeAr.setText(codeDto.getCSteuercodeAr());
			wtfCodeEr.setText(codeDto.getCSteuercodeEr());
		}
		
		private void resetCodeComponents() {
			wtfCodeAr.setText(null);
			wtfCodeEr.setText(null);
		}
		
		public void components2Dto(MwstsatzId mwstsatzId) {
			codeDto.setMwstsatzId(mwstsatzId);
			codeDto.setCSteuercodeAr(StringUtils.isNotEmpty(wtfCodeAr.getText()) ? wtfCodeAr.getText() : null);
			codeDto.setCSteuercodeEr(StringUtils.isNotEmpty(wtfCodeEr.getText()) ? wtfCodeEr.getText() : null);
		}
		
		public JComponent getComponentRcArt() {
			return wlaRcArt;
		}
		public JComponent getComponentCodeAr() {
			return wtfCodeAr;
		}
		public JComponent getComponentCodeEr() {
			return wtfCodeEr;
		}
	}
}
