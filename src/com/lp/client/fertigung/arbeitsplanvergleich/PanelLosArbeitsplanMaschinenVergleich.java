package com.lp.client.fertigung.arbeitsplanvergleich;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;

import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosArbeitsplanZeitVergleichDto;

import net.miginfocom.layout.CC;

public class PanelLosArbeitsplanMaschinenVergleich extends PanelLosArbeitsplanVergleich {
	private static final long serialVersionUID = 1L;

	private Map<LosArbeitsplanZeitVergleichDto, Integer> uebernommeneDtos = new HashMap<>();

	@Override
	protected void createVergleichZeile(LosArbeitsplanZeitVergleichDto vergleich, JPanel eintragPanel) {
		int numMaschinen = vergleich.getIstmaschinenBez().size();

		WrapperTextField wtfAG = createArbeitsgangTextField(vergleich);
		eintragPanel.add(wtfAG, new CC().growX().spanY(numMaschinen).alignY("center"));

		WrapperTextField wtfSoll = new WrapperTextField();
		wtfSoll.setText(vergleich.getcSollmaschine());
		wtfSoll.setEditable(false);
		wtfSoll.setActivatable(false);
		eintragPanel.add(wtfSoll, new CC().growX().spanY(numMaschinen).alignY("center"));

		UebernehmenBtnMaschineHandler actionHandler = new UebernehmenBtnMaschineHandler(wtfSoll, vergleich);

		int count = 0;
		for (Entry<Integer, String> istMaschine : vergleich.getIstmaschinenMap().entrySet()) {
			UebernehmenButtonData<Integer> btnUebernehmen = new UebernehmenButtonData<>(istMaschine.getKey());
			eintragPanel.add(btnUebernehmen, new CC().growX());
			actionHandler.add(btnUebernehmen);
			WrapperTextField wtfIst = new WrapperTextField();
			wtfIst.setText(istMaschine.getValue());
			wtfIst.setEditable(false);
			wtfIst.setActivatable(false);
			String gapTop = (count == 0 && numMaschinen > 1) ? "5":"0";
			String gapBot = (count == numMaschinen - 1 && numMaschinen > 1) ? "5":"0"; 
			eintragPanel.add(wtfIst, new CC().growX().wrap().gapY(gapTop, gapBot));
			count++;
		}
	}

	@Override
	protected void afterSetData() {
		super.afterSetData();
		uebernommeneDtos.clear();
	}

	public Map<LosArbeitsplanZeitVergleichDto, Integer> getUebernommeneDtos() {
		return Collections.unmodifiableMap(uebernommeneDtos);
	}

	private class UebernehmenBtnMaschineHandler extends MultiUebernehmenButtonHandler<Integer> {
		private final LosArbeitsplanZeitVergleichDto dto;

		public UebernehmenBtnMaschineHandler(WrapperTextField target, LosArbeitsplanZeitVergleichDto dto) {
			super(target);
			this.dto = dto;
		}

		@Override
		public void add(UebernehmenButtonData<Integer> btn) {
			super.add(btn);
			btn.setEnabled(dto.getiSollmaschineIId() != btn.getData());
		}

		@Override
		public void onActivate(UebernehmenButtonData<Integer> clickedBtn) {
			super.onActivate(clickedBtn);
			uebernommeneDtos.put(dto, clickedBtn.getData());
			txtFieldTarget.setText(dto.getIstmaschinenMap().get(clickedBtn.getData()));
		}

		@Override
		public void onDeactivate(UebernehmenButtonData<Integer> clickedBtn) {
			super.onDeactivate(clickedBtn);
			uebernommeneDtos.remove(dto);
			txtFieldTarget.setText(dto.getcSollmaschine());
		}

	}

	@Override
	protected boolean acceptVergleichdaten(LosArbeitsplanZeitVergleichDto vergleich) {
		return vergleich.hatMaschine() && !vergleich.getIstmaschinenBez().isEmpty();
	}

	@Override
	protected String getIstLabelText() {
		return LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.istmaschine");
	}

	@Override
	protected String getSollLabelText() {
		return LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.sollmaschine");
	}
	
	@Override
	protected void clearOldData() {
		super.clearOldData();
		uebernommeneDtos.clear();
	}
}
