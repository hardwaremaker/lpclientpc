package com.lp.client.fertigung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.ScrollableJPanel;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperNumberFieldPropertyAdapter;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.client.util.GridBagConstraintBuilder;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.server.fertigung.service.RestmengeUndChargennummerDto;

public class DialogRestmengenChargennummernAuswahl extends JDialog {
	private static final int PREF_WIDTH = Defaults.sizeFactor(160);

	private static final int MIN_WIDTH = Defaults.sizeFactor(100);

	private static final int INSETS = 2;

	private static final long serialVersionUID = 1L;

	private boolean save = false;
	private final RestmengeUndChargennummerDto chnrDto;
	private List<ChnrMengeListener> mengeListeners;

	private int frameMinSize = 0;

	private WnfSummeListener summeListener;

	public DialogRestmengenChargennummernAuswahl(RestmengeUndChargennummerDto chnrDto) throws ExceptionLP {
		super(LPMain.getInstance().getDesktop());
		this.chnrDto = chnrDto;
		mengeListeners = new ArrayList<>();
		setTitle(LPMain.getTextRespectUISPr("fert.restmenge.chnr.dialogtitel"));
		jbInit();
		pack();

		setMinimumSize(new Dimension(frameMinSize, Defaults.sizeFactor(160)));

	}

	private void jbInit() throws ExceptionLP {
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraintBuilder constraintBuilder = new GridBagConstraintBuilder();
		constraintBuilder.insets(INSETS);

		JButton btnAbbrechen = new JButton(LPMain.getTextRespectUISPr("lp.abbrechen"));
		JButton btnSpeichern = new JButton(LPMain.getTextRespectUISPr("button.ok"));

		btnAbbrechen.addActionListener(e -> close());
		btnSpeichern.addActionListener(e -> {
			save = true;
			close();
		});

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new GridBagLayout());

		btnPanel.add(btnAbbrechen, constraintBuilder.build());
		btnPanel.add(btnSpeichern, constraintBuilder.x(1).build());

		JLabel labelSollMenge = new JLabel(LPMain.getTextRespectUISPr("fert.restmenge.gesamt"));
		labelSollMenge.setHorizontalAlignment(SwingConstants.RIGHT);
		WrapperNumberField wnfSollMenge = new WrapperNumberField();
		wnfSollMenge.setEditable(false);
		wnfSollMenge.setFocusable(false);
		wnfSollMenge.setBigDecimal(chnrDto.getGesMenge());
		wnfSollMenge.setHorizontalAlignment(SwingConstants.RIGHT);

		wnfSollMenge.setMinimumSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));
		wnfSollMenge.setPreferredSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));

		contentPane.add(labelSollMenge, constraintBuilder.x(0).weightX(1.0).anchor(GridBagConstraints.WEST).build());
		contentPane.add(wnfSollMenge, constraintBuilder.x(1).anchor(GridBagConstraints.CENTER).build());

		Component alleChnrMenge = initPanelAlleChnrMenge();

		GridBagConstraints scrollPaneConstraint = constraintBuilder.incY().x(0).width(2).clone()
				.width(GridBagConstraints.REMAINDER).weight(1.0, 1.0).fill(GridBagConstraints.BOTH).build();
		contentPane.add(alleChnrMenge, scrollPaneConstraint);
		contentPane.add(btnPanel, constraintBuilder.incY().anchor(GridBagConstraints.SOUTHWEST).build());

		setContentPane(contentPane);
	}

	public boolean sollSpeichern() {
		return save;
	}

	/**
	 * Gibt neue {@link RestmengeUndChargennummerDto} zurueck. Wenn abbrechen
	 * gewaehlt wurde, dann ist das ein leeres Optional
	 * 
	 * @return
	 */
	public Optional<RestmengeUndChargennummerDto> getRuckgabeMenge() {
		if (!save)
			return Optional.empty();
		List<SeriennrChargennrMitMengeDto> neueMengen = mengeListeners.stream()
				.map(lis -> new SeriennrChargennrMitMengeDto(lis.getChnr(), lis.getNMenge()))
				.collect(Collectors.toCollection(ArrayList::new));
		return Optional.of(new RestmengeUndChargennummerDto(chnrDto.getGesMenge(), neueMengen));
	}

	private Component initPanelAlleChnrMenge() throws ExceptionLP {
		JPanel panelAlleChnrMenge = new ScrollableJPanel();
		panelAlleChnrMenge.setLayout(new GridBagLayout());
		GridBagConstraintBuilder cb = new GridBagConstraintBuilder().insets(INSETS);

		WrapperLabel labelChnr = new WrapperLabel(LPMain.getTextRespectUISPr("lp.chargennummer_lang"));
		WrapperLabel labelMenge = new WrapperLabel(LPMain.getTextRespectUISPr("lp.menge"));
		labelChnr.setHorizontalAlignment(SwingConstants.CENTER);
		labelMenge.setHorizontalAlignment(SwingConstants.CENTER);

		labelChnr.setMinimumSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));
		labelMenge.setMinimumSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));

		cb.anchor(GridBagConstraints.CENTER);
		panelAlleChnrMenge.add(labelChnr, cb.x(0).build());
		panelAlleChnrMenge.add(labelMenge, cb.x(1).build());

		BigDecimal gesAusgMenge = chnrDto.getChargenNummernMitMenge().stream().map(chMitMenge -> chMitMenge.getNMenge())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		cb.fill(GridBagConstraints.HORIZONTAL).weightX(1.0);

		for (SeriennrChargennrMitMengeDto dto : chnrDto.getChargenNummernMitMenge()) {
			cb.incY();
			WrapperTextField wtfChnr = new WrapperTextField();
			wtfChnr.setText(dto.getCSeriennrChargennr());

			WrapperNumberField wnfMenge = new WrapperNumberField();
			wnfMenge.setFractionDigits(3);
			wnfMenge.setMinimumValue(BigDecimal.ZERO);
			wnfMenge.setMaximumValue(dto.getNMenge());
			wtfChnr.setEditable(false);
			wtfChnr.setActivatable(false);
			wtfChnr.setFocusable(false);

			wnfMenge.setMinimumSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));
			wtfChnr.setMinimumSize(new Dimension(MIN_WIDTH, Defaults.getInstance().getControlHeight()));
			wnfMenge.setPreferredSize(new Dimension(PREF_WIDTH, Defaults.getInstance().getControlHeight()));
			wtfChnr.setPreferredSize(new Dimension(PREF_WIDTH, Defaults.getInstance().getControlHeight()));

			BigDecimal defMenge = dto.getNMenge();
			defMenge = chnrDto.getGesMenge().multiply(defMenge).setScale(3, RoundingMode.HALF_UP).divide(gesAusgMenge,
					RoundingMode.HALF_UP);
			mengeListeners.add(new ChnrMengeListener(wnfMenge, defMenge, dto.getCSeriennrChargennr()));

			panelAlleChnrMenge.add(wtfChnr, cb.x(0).build());
			panelAlleChnrMenge.add(wnfMenge, cb.x(1).build());
		}

		cb.insets(2 * INSETS, INSETS, INSETS, INSETS);

		WrapperLabel labelSumme = new WrapperLabel(LPMain.getTextRespectUISPr("lp.summe"));
		labelSumme.setHorizontalAlignment(SwingConstants.RIGHT);

		WrapperNumberField wnfSumme = new WrapperNumberField();
		wnfSumme.setFractionDigits(3);
		wnfSumme.setEditable(false);
		wnfSumme.setFocusable(false);

		summeListener = new WnfSummeListener(wnfSumme, chnrDto.getGesMenge());

		for (ChnrMengeListener lis : mengeListeners) {
			summeListener.addMengeListener(lis);
		}

		cb.incY();
		panelAlleChnrMenge.add(labelSumme, cb.x(0).build());
		panelAlleChnrMenge.add(wnfSumme, cb.x(1).build());

		frameMinSize = panelAlleChnrMenge.getMinimumSize().width;
		return new JScrollPane(panelAlleChnrMenge, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}

	/**
	 * Verstecken und close event schicken beim schliessen, dadurch kann ein
	 * potentieller Window Listener noch benachrichtigt werden
	 */
	private void close() {
		setVisible(false);
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private static class ChnrMengeListener extends WrapperNumberFieldPropertyAdapter {
		private final String chnr;

		public ChnrMengeListener(WrapperNumberField wnfToMonitor, BigDecimal defaultVal, String chnr) {
			super(wnfToMonitor, defaultVal);
			this.chnr = chnr;
		}

		public String getChnr() {
			return chnr;
		}
	}

	private static class WnfSummeListener {
		private BigDecimal summeCache = BigDecimal.ZERO;
		private final BigDecimal bdSoll;
		private final WrapperNumberField wnfSumme;

		private PropertyChangeListener changeListener;

		public WnfSummeListener(WrapperNumberField wnfSumme, BigDecimal bdSoll) {
			this.wnfSumme = wnfSumme;
			this.bdSoll = bdSoll == null ? BigDecimal.ZERO : bdSoll;
			changeListener = event -> updateSumme((BigDecimal) event.getOldValue(), (BigDecimal) event.getNewValue());
		}

		public void addMengeListener(ChnrMengeListener listener) {
			listener.addPropertyChangeListener("menge", changeListener);
			updateSumme(BigDecimal.ZERO, listener.getNMenge());
		}

		private void updateSumme(BigDecimal oldVal, BigDecimal newVal) {
			synchronized (wnfSumme) {
				if (oldVal == null)
					oldVal = BigDecimal.ZERO;
				if (newVal == null)
					newVal = BigDecimal.ZERO;
				summeCache = summeCache.subtract(oldVal).add(newVal);
				try {
					wnfSumme.setBigDecimal(summeCache);
					boolean istSollmenge = summeCache.compareTo(bdSoll) == 0;
					wnfSumme.setForeground(istSollmenge ? Color.black : Color.red);
				} catch (ExceptionLP e) {
				}
			}
		}

	}

}
