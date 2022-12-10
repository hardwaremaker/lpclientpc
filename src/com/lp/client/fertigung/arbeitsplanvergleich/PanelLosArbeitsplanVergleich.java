package com.lp.client.fertigung.arbeitsplanvergleich;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosArbeitsplanZeitVergleichDto;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

public abstract class PanelLosArbeitsplanVergleich extends PanelBasis {
	private static final long serialVersionUID = 1L;

	private JLabel wlaArbeitsgang;
	private WrapperLabel wlaSollmaschine;
	private WrapperLabel wlaIstmaschine;

	private JScrollPane scrollPaneContent;

	private boolean hasData = false;

	public final void setData(List<LosArbeitsplanZeitVergleichDto> vergleichDaten) {
		clearOldData();
		hasData = false;
		this.setLayout(new GridBagLayout());
		if (scrollPaneContent != null) {
			this.remove(scrollPaneContent);
		}
		scrollPaneContent = null;

		JPanel panelVergleich = new JPanel();

		panelVergleich.setLayout(createLayout());

		wlaArbeitsgang = new JLabel(LPMain.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"));
		wlaSollmaschine = new WrapperLabel(getSollLabelText());
		wlaIstmaschine = new WrapperLabel(getIstLabelText());

		wlaArbeitsgang.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSollmaschine.setHorizontalAlignment(SwingConstants.CENTER);
		wlaIstmaschine.setHorizontalAlignment(SwingConstants.CENTER);

		panelVergleich.add(wlaArbeitsgang, new CC().growX());
		panelVergleich.add(wlaSollmaschine, new CC().growX());
		panelVergleich.add(wlaIstmaschine, new CC().growX().skip().wrap());
		for (LosArbeitsplanZeitVergleichDto vergleich : vergleichDaten) {
			if (acceptVergleichdaten(vergleich)) {
				hasData = true;
				createVergleichZeile(vergleich, panelVergleich);
			}
		}
		scrollPaneContent = new JScrollPane(panelVergleich);
		this.add(scrollPaneContent, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		afterSetData();
	}

	protected MigLayout createLayout() {
		return new MigLayout("", getDefaultLayoutConstraint());
	}

	/**
	 * Wird am ende von setData aufgerufen, hier kann erbende Klasse bereinigungen
	 * etc durchführen
	 */
	protected void afterSetData() {

	}

	/**
	 * Wird am Anfang von setData aufgerufen. Hier sollten alte Daten gel&ouml;scht
	 * werden
	 */
	protected void clearOldData() {

	}

	protected abstract void createVergleichZeile(LosArbeitsplanZeitVergleichDto vergleich, JPanel panelContent);

	protected abstract boolean acceptVergleichdaten(LosArbeitsplanZeitVergleichDto vergleich);

	/**
	 * Text f&uuml;r das Label &uuml;ber der Istzeit bzw. Istmaschine Spalte
	 * 
	 * @return
	 */
	protected abstract String getIstLabelText();

	/**
	 * Text f&uuml;r das Label &uuml;ber der Sollzeit bzw. Sollmaschine Spalte
	 * 
	 * @return
	 */
	protected abstract String getSollLabelText();

	protected WrapperTextField createArbeitsgangTextField(LosArbeitsplanZeitVergleichDto vergleich) {
		WrapperTextField wtfAG = new WrapperTextField();
		wtfAG.setEditable(false);
		wtfAG.setActivatable(false);
		wtfAG.setText(String.valueOf(vergleich.getiArbeitsgang()));
		return wtfAG;
	}

	public boolean hasData() {
		return hasData;
	}

	protected String getDefaultLayoutConstraint() {
		JLabel testLabel = new JLabel();
		Font font = testLabel.getFont();
		FontMetrics fmetric = testLabel.getFontMetrics(font);
		int widPx = fmetric.stringWidth(LPMain.getTextRespectUISPr("stkl.arbeitsplan.arbeitsgang"));
		return "[" + widPx + "px][grow][1cm][grow]";
	}

}
