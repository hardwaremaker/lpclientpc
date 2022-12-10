package com.lp.client.frame.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.lp.client.artikel.IWebabfrageArtikellieferantCtrl;
import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.LieferantId;

public class WrapperWebBauteilAbfrageButton extends WrapperButton implements ActionListener {
	private static final long serialVersionUID = 2074900370541763900L;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this.getClass());
	
	public static final String ActionCommand = PanelBasis.ACTION_MY_OWN_NEW + "_WEBABFRAGE";
	private ArtikellieferantDto artikellieferantDto;
	private IWebabfrageArtikellieferantCtrl requestCtrl;
	
	public WrapperWebBauteilAbfrageButton(ArtikellieferantDto artikellieferantDto) {
		super(IconFactory.getWebRequest());
		setArtikellieferantDto(artikellieferantDto);
		addActionListener(this);
		setActivatable(true);
		setActionCommand(ActionCommand);
		setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		setToolTipText(LPMain.getTextRespectUISPr("artikel.artikellieferant.button.webabfrage"));
	}

	public WrapperWebBauteilAbfrageButton(ArtikellieferantDto artikellieferantDto, IWebabfrageArtikellieferantCtrl requestCtrl) {
		this(artikellieferantDto);
		setRequestCtrl(requestCtrl);
	}

	public void setRequestCtrl(IWebabfrageArtikellieferantCtrl requestCtrl) {
		this.requestCtrl = requestCtrl;
	}
	
	public IWebabfrageArtikellieferantCtrl getRequestCtrl() {
		return requestCtrl;
	}
	
	public void setArtikellieferantDto(ArtikellieferantDto artikellieferantDto) {
		this.artikellieferantDto = artikellieferantDto;
	}
	
	public ArtikellieferantDto getArtikellieferantDto() {
		return artikellieferantDto;
	}
	
	public void actionPerformed(ActionEvent event) {
		if (getRequestCtrl() == null) return;
		
		try {
			getRequestCtrl().request(new ArtikelId(artikellieferantDto.getArtikelIId()), new LieferantId(artikellieferantDto.getLieferantIId()));
		} catch (Throwable e) {
			myLogger.error("Error while requesting web part [Artikellieferant: " + artikellieferantDto.toString() + "]", e);
		}
	}

	@Override
	public void setEnabled(boolean bEnabled) {
		super.setEnabled(bEnabled);
	}
}
