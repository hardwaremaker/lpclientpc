package com.lp.client.util;

import java.util.List;

import com.lp.client.anfrage.InternalFrameAnfrage;
import com.lp.client.angebot.InternalFrameAngebot;
import com.lp.client.angebotstkl.InternalFrameAngebotstkl;
import com.lp.client.angebotstkl.TabbedPaneEinkaufsangebot;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.auftrag.InternalFrameAuftrag;
import com.lp.client.bestellung.InternalFrameBestellung;
import com.lp.client.eingangsrechnung.InternalFrameEingangsrechnung;
import com.lp.client.eingangsrechnung.TabbedPaneEingangsrechnung;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.TabbedPane;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.MandantDelegate;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.InternalFrameLieferant;
import com.lp.client.partner.InternalFramePartner;
import com.lp.client.projekt.InternalFrameProjekt;
import com.lp.client.reklamation.InternalFrameReklamation;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.DokumentenlinkDto;

public class DokumentenlinkController {
	protected final LpLogger myLogger = (LpLogger) com.lp.client.util.logger.LpLogger
			.getInstance(this.getClass());

	private String belegartCnr;
	private TabbedPane tabbedPane;
	private List<DokumentenlinkDto> doklinkDtos;
	
	public DokumentenlinkController(TabbedPane tabbedPane, String belegartCnr) {
		this.tabbedPane = tabbedPane;
		this.belegartCnr = belegartCnr;
	}

	public List<DokumentenlinkDto> getDokumentenlinkDtos() throws ExceptionLP, Throwable {
		if (doklinkDtos == null) {
			doklinkDtos = loadDokumentenlinkDtos();
		}
		return doklinkDtos;
	}
	
	private MandantDelegate mandantDelegate() throws Throwable {
		return DelegateFactory.getInstance().getMandantDelegate();
	}
	
	private List<DokumentenlinkDto> loadDokumentenlinkDtos() throws ExceptionLP, Throwable {
		List<DokumentenlinkDto> dokumentenlinkDtos = mandantDelegate()
				.getSichtbareDokumentenlinks(belegartCnr, false);
		return dokumentenlinkDtos;
	}

	public boolean hasDokumentenlinks() throws ExceptionLP, Throwable {
		return !getDokumentenlinkDtos().isEmpty();
	}

	public void actionDokumentenlink(Integer doklinkId) throws Throwable {
		DokumentenlinkDto doklinkDto = mandantDelegate().dokumentenlinkFindByPrimaryKey(doklinkId);
		DokumentenlinkAction doklinkAction = getDokumentenlinkActionFuerModul(doklinkDto);
		if (doklinkAction != null) {
			doklinkAction.open();
		}
	}

	private DokumentenlinkAction getDokumentenlinkActionFuerModul(DokumentenlinkDto doklinkDto) throws Throwable {
		if (tabbedPane.getInternalFrame() instanceof InternalFrameProjekt
					&& ((InternalFrameProjekt) tabbedPane.getInternalFrame()).getTabbedPaneProjekt() != null) {
				return new DokumentenlinkProjektAction(doklinkDto, 
						((InternalFrameProjekt) tabbedPane.getInternalFrame()).getTabbedPaneProjekt().getProjektDto());
		}
		
		String modulPlatzhalter = null;
		
		if (tabbedPane.getInternalFrame() instanceof InternalFrameArtikel) {
			modulPlatzhalter = ((InternalFrameArtikel) tabbedPane
					.getInternalFrame()).getArtikelDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameStueckliste) {
			modulPlatzhalter = ((InternalFrameStueckliste) tabbedPane
					.getInternalFrame()).getStuecklisteDto()
					.getArtikelDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameAuftrag) {
			modulPlatzhalter = ((InternalFrameAuftrag) tabbedPane
					.getInternalFrame()).getTabbedPaneAuftrag()
					.getAuftragDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameEingangsrechnung) {
			if (tabbedPane instanceof TabbedPaneEingangsrechnung) {
				modulPlatzhalter = ((TabbedPaneEingangsrechnung) tabbedPane)
						.getEingangsrechnungDto().getCNr();
			}
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameAngebotstkl) {
			if (tabbedPane instanceof TabbedPaneEinkaufsangebot) {
				modulPlatzhalter = ((TabbedPaneEinkaufsangebot) tabbedPane)
						.getEinkaufsangebotDto().getCNr();
			}
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameAngebot) {
			modulPlatzhalter = ((InternalFrameAngebot) tabbedPane
					.getInternalFrame()).getTabbedPaneAngebot()
					.getAngebotDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameAnfrage) {
			modulPlatzhalter = ((InternalFrameAnfrage) tabbedPane
					.getInternalFrame()).getTabbedPaneAnfrage()
					.getAnfrageDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameFertigung) {
			modulPlatzhalter = ((InternalFrameFertigung) tabbedPane
					.getInternalFrame()).getTabbedPaneLos().getLosDto()
					.getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameReklamation) {
			modulPlatzhalter = ((InternalFrameReklamation) tabbedPane
					.getInternalFrame()).getReklamationDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameKunde) {
			modulPlatzhalter = ((InternalFrameKunde) tabbedPane
					.getInternalFrame()).getKundeDto().getPartnerDto()
					.getCKbez();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFramePartner) {
			modulPlatzhalter = ((InternalFramePartner) tabbedPane
					.getInternalFrame()).getTpPartner()
					.getServicePartnerDto().getCKbez();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameLieferant) {
			modulPlatzhalter = ((InternalFrameLieferant) tabbedPane
					.getInternalFrame()).getLieferantDto()
					.getPartnerDto().getCKbez();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameReklamation) {
			modulPlatzhalter = ((InternalFrameReklamation) tabbedPane
					.getInternalFrame()).getReklamationDto().getCNr();
		} else if (tabbedPane.getInternalFrame() instanceof InternalFrameBestellung) {
			modulPlatzhalter = ((InternalFrameBestellung) tabbedPane
					.getInternalFrame()).getTabbedPaneBestellung()
					.getBesDto().getCNr();
		}
		
		return modulPlatzhalter != null 
				? new DokumentenlinkAction(doklinkDto, modulPlatzhalter) 
				: null;
	}
}
