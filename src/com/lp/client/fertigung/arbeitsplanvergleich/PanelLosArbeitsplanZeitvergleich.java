package com.lp.client.fertigung.arbeitsplanvergleich;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosArbeitsplanZeitVergleichDto;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public class PanelLosArbeitsplanZeitvergleich extends PanelLosArbeitsplanVergleich {

	private static final long serialVersionUID = 1L;

	private Set<Integer> verarbeiteteAGs = new HashSet<Integer>();

	private Map<LosArbeitsplanZeitVergleichDto, BigDecimal> uebernommeneDtos = new HashMap<>();

	@Override
	protected void afterSetData() {
		super.afterSetData();
		verarbeiteteAGs.clear();
	}

	@Override
	protected void createVergleichZeile(LosArbeitsplanZeitVergleichDto vergleich, JPanel eintragPanel) {
		verarbeiteteAGs.add(vergleich.getiArbeitsgang());
		BigDecimal hauptZeit;
		BigDecimal zusatzZeit = BigDecimal.ZERO;
		boolean maschineDiffPerson = false;
		if (vergleich.hatMaschine()) {
			hauptZeit = vergleich.getIstZeitMaschine();
			zusatzZeit = vergleich.getIstZeitPerson();
			if (hauptZeit.compareTo(zusatzZeit) != 0 && zusatzZeit.compareTo(BigDecimal.ZERO) != 0) {
				maschineDiffPerson = true;
			}
		} else {
			hauptZeit = vergleich.getIstZeitPerson();
		}

		int spanY = maschineDiffPerson ? 2 : 1;

		WrapperTextField wtfAG = createArbeitsgangTextField(vergleich);
		eintragPanel.add(wtfAG, new CC().growX().spanY(spanY));

		WrapperTextField wtfSoll = new WrapperTextField();
		BigDecimal gesamtZeit = vergleich.getSollGesamtArbeitszeit();
		wtfSoll.setText(String.valueOf(gesamtZeit));
		wtfSoll.setEditable(false);
		wtfSoll.setActivatable(false);
		eintragPanel.add(wtfSoll, new CC().growX().spanY(spanY));

		UebernehmenBtnZeitHandler btnHandler = new UebernehmenBtnZeitHandler(wtfSoll, vergleich);
		if (maschineDiffPerson) {
			addUebernehmneBtnAndIstField(eintragPanel, btnHandler, zusatzZeit, Optional.of(ZeitArt.PERSON));
			addUebernehmneBtnAndIstField(eintragPanel, btnHandler, hauptZeit, Optional.of(ZeitArt.MASCHINE));
		} else {
			addUebernehmneBtnAndIstField(eintragPanel, btnHandler, hauptZeit, Optional.empty());
		}
	}

	private void addUebernehmneBtnAndIstField(JPanel eintragPanel, UebernehmenBtnZeitHandler btnHandler,
			BigDecimal zeit, Optional<ZeitArt> label) {
		UebernehmenButtonData<BigDecimal> btnUebernehmen = new UebernehmenButtonData<BigDecimal>(zeit);
		eintragPanel.add(btnUebernehmen, new CC().growX());
		btnHandler.add(btnUebernehmen);

		int istFieldSpan = label.isPresent() ? 1 : 2;

		WrapperTextField wtfIst = new WrapperTextField();
		wtfIst.setText(rundeZeit(zeit));
		wtfIst.setEditable(false);
		wtfIst.setActivatable(false);
		CC wtfIstCC = new CC().growX().spanX(istFieldSpan);
		wtfIstCC.setWrap(!label.isPresent());
		if (!label.isPresent()) {
			wtfIstCC.gapTop("5");
		}
		eintragPanel.add(wtfIst, wtfIstCC);

		if (label.isPresent()) {
			WrapperLabel labelWhat = new WrapperLabel(label.get().getLabel());
			labelWhat.setHorizontalAlignment(SwingConstants.LEFT);
			eintragPanel.add(labelWhat, new CC().growX().wrap().gapTop(label.get().getGapTop()));
		}
	}

	private String rundeZeit(BigDecimal zeit) {
		return String.valueOf(zeit.setScale(4, RoundingMode.HALF_UP));
	}

	@Override
	protected MigLayout createLayout() {
		return new MigLayout("", super.getDefaultLayoutConstraint() + "[1cm]");
	}

	@Override
	protected boolean acceptVergleichdaten(LosArbeitsplanZeitVergleichDto vergleich) {
		if (verarbeiteteAGs.contains(vergleich.getiArbeitsgang())) {
			return false;
		}
		if (vergleich.getSollGesamtArbeitszeit().compareTo(BigDecimal.ZERO) == 0
				&& vergleich.getIstZeitPerson().compareTo(BigDecimal.ZERO) == 0
				&& vergleich.getIstZeitMaschine().compareTo(BigDecimal.ZERO) == 0) {
			return false;
		}
		return true;
	}

	@Override
	protected String getIstLabelText() {
		return LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.istzeit");
	}

	@Override
	protected String getSollLabelText() {
		return LPMain.getTextRespectUISPr("fert.dialog.istzeitvergleich.sollzeit");
	}

	public Map<LosArbeitsplanZeitVergleichDto, BigDecimal> getUebernommeneDtos() {
		return Collections.unmodifiableMap(uebernommeneDtos);
	}

	private class UebernehmenBtnZeitHandler extends MultiUebernehmenButtonHandler<BigDecimal> {
		private final LosArbeitsplanZeitVergleichDto dto;

		public UebernehmenBtnZeitHandler(WrapperTextField target, LosArbeitsplanZeitVergleichDto dto) {
			super(target);
			this.dto = dto;
		}

		@Override
		public void add(UebernehmenButtonData<BigDecimal> btn) {
			super.add(btn);
			btn.setEnabled(dto.getSollGesamtArbeitszeit().compareTo(btn.getData()) != 0);
		}

		@Override
		protected void onActivate(UebernehmenButtonData<BigDecimal> activatedButton) {
			super.onActivate(activatedButton);
			uebernommeneDtos.put(dto, activatedButton.getData());
			txtFieldTarget.setText(rundeZeit(activatedButton.getData()));
		}

		@Override
		protected void onDeactivate(UebernehmenButtonData<BigDecimal> deactivatedButton) {
			super.onDeactivate(deactivatedButton);
			uebernommeneDtos.remove(dto);
			txtFieldTarget.setText(dto.getSollGesamtArbeitszeit().toString());
		}

	}

	private static enum ZeitArt {
		PERSON("fert.dialog.istzeitvergleich.kurzperson", "5"),
		MASCHINE("fert.dialog.istzeitvergleich.kurzmaschine", "0");

		private final String resKey;
		private final String gapTop;

		private ZeitArt(String resKey, String gapTop) {
			this.resKey = resKey;
			this.gapTop = gapTop;
		}

		public String getLabel() {
			return LPMain.getTextRespectUISPr(resKey);
		}

		public String getGapTop() {
			return gapTop;
		}
	}
	
	@Override
	protected void clearOldData() {
		super.clearOldData();
		verarbeiteteAGs.clear();
		uebernommeneDtos.clear();
	}

}
