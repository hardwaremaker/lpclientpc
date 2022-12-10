package com.lp.client.system.automatik;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.artikel.service.JobDetailsWebabfrageArtikellieferantDto;
import com.lp.server.bestellung.service.JobDetailsWEJournalDto;
import com.lp.server.fertigung.service.JobDetailsArbeitszeitstatusDto;
import com.lp.server.fertigung.service.JobDetailsBedarfsuebernahmeOffeneDto;
import com.lp.server.system.service.AutomatikjobDto;
import com.lp.server.system.service.AutomatikjobtypeDto;
import com.lp.server.system.service.AutomatiktimerFac;
import com.lp.server.system.service.JobDetails4VendingExportDto;
import com.lp.server.system.service.JobDetailsAuslieferlisteDto;
import com.lp.server.system.service.JobDetailsErImportDto;
import com.lp.server.system.service.JobDetailsKpiReportDto;
import com.lp.server.system.service.JobDetailsLumiquoteDto;

public class PanelAutoJobDetailsFactory {

	public PanelAutoJobDetailsFactory() {
	}

	public IPanelAutoJobDetails getPanelAutoJobDetails(AutomatikjobDto jobDto, AutomatikjobtypeDto jobtypeDto) {
		if (AutomatiktimerFac.JOBTYPE_ARBEITSZEITSTATUSPDF_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelArbeitszeitstatus();
		}

		if (AutomatiktimerFac.JOBTYPE_WARENEINGANGSJOURNALPDF_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelWareneingangsjournal();
		}
		
		if (AutomatiktimerFac.JOBTYPE_ER_XLSIMPORT_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelErImport();
		}
		
		if (AutomatiktimerFac.JOBTYPE_AUSLIEFERLISTEPDFDRUCK_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelAuslieferliste();
		}
		
		if (AutomatiktimerFac.JOBTYPE_LUMIQUOTE.equals(jobtypeDto.getCJobtype())) {
			return createPanelLumiquote();
		}
		
		if (AutomatiktimerFac.JOBTYPE_MAHNUNGSVERSAND_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelMahnungsversand();
		}
		if (AutomatiktimerFac.JOBTYPE_MONATSABRECHNUNGVERSAND_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelMonatsabrechnungversand();
		}
		if (AutomatiktimerFac.JOBTYPE_MONATSABRECHNUNGVERSAND_ABTEILUNGEN_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelMonatsabrechnungversandAbteilungen();
		}
		
		if (AutomatiktimerFac.JOBTYPE_BESTELLVORSCHLAGDRUCK_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createBestellvorschlagdruck();
		}
		
		if (AutomatiktimerFac.JOBTYPE_LOSEERLEDIGEN.equals(jobtypeDto.getCJobtype())) {
			return createLoseerledigen();
		}

		if (AutomatiktimerFac.JOBTYPE_FEHLMENGENDRUCK_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createFehlmengendruck();
		}

		if (AutomatiktimerFac.JOBTYPE_MAHNEN_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createMahnen();
		}

		if (AutomatiktimerFac.JOBTYPE_RAHMENDETAILBEDARFDRUCK_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createRahmendetailbedarfdruck();
		}
		
		if (AutomatiktimerFac.JOBTYPE_4VENDINGXMLEXPORT_TYPE.equals(jobtypeDto.getCJobtype())) {
			return create4VendingXmlExport();
		}
		
		if (AutomatiktimerFac.JOBTYPE_ARTIKELLIEFERANT_WEBABFRAGE_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createArtikellieferantWebabfrage();
		}
	 
		if (AutomatiktimerFac.JOBTYPE_KPIREPORTPDFDRUCK_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelKpiReport();
		}

		if (AutomatiktimerFac.JOBTYPE_BEDARFSUEBERNAHMEOFFENJOURNAL_TYPE.equals(jobtypeDto.getCJobtype())) {
			return createPanelBedarfsuebernahmeOffen();
		}
		
		// throw new IllegalArgumentException("AutoJobType '" + autojobType + "' unbekannt");
		return new PanelAutoJobNoDetails();
	}

	private IPanelAutoJobDetails createArtikellieferantWebabfrage() {
		IAutoJobDetailsPanelCtrl<JobDetailsWebabfrageArtikellieferantDto> panelCtrl = 
				new AutoJobPanelCtrl<>(
						DelegateFactory.getInstance().getAutoJobWebabfrageArtikellieferantDelegate(),
						JobDetailsWebabfrageArtikellieferantDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobWebabfrageArtikellieferant(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails create4VendingXmlExport() {
		IAutoJobDetailsPanelCtrl<JobDetails4VendingExportDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJob4VendingExportDelegate(),
				JobDetails4VendingExportDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJob4VendingXmlExport(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createRahmendetailbedarfdruck() {
		AutoJobRahmendetailbedarfCtrl panelCtrl = new AutoJobRahmendetailbedarfCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobRahmendetailbedarf(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createMahnen() {
		AutoJobMahnenCtrl panelCtrl = new AutoJobMahnenCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobMahnen(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createFehlmengendruck() {
		AutoJobFehlmengendruckCtrl panelCtrl = new AutoJobFehlmengendruckCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobFehlmengendruck(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createBestellvorschlagdruck() {
		AutoJobBestellvorschlagCtrl panelCtrl = new AutoJobBestellvorschlagCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobBestellvorschlag(panelCtrl);
		return panel;
	}
	
	private IPanelAutoJobDetails createLoseerledigen() {
		AutoJobLoseerledigenCtrl panelCtrl = new AutoJobLoseerledigenCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobLoseerledigen(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelMahnungsversand() {
		AutoJobMahnungsversandCtrl panelCtrl = new AutoJobMahnungsversandCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobMahnungsversand(panelCtrl);
		return panel;
	}
	
	private IPanelAutoJobDetails createPanelMonatsabrechnungversand() {
		AutoJobMonatsabrechnungversandCtrl panelCtrl = new AutoJobMonatsabrechnungversandCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobMonatsabrechnungversand(panelCtrl);
		return panel;
	}
	
	private IPanelAutoJobDetails createPanelLumiquote() {
		
		IAutoJobDetailsPanelCtrl<JobDetailsLumiquoteDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobLumiquoteDelegate(), 
				JobDetailsLumiquoteDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobLumiquote(panelCtrl);
		return panel;
		
	}
	
	private IPanelAutoJobDetails createPanelMonatsabrechnungversandAbteilungen() {
		AutoJobMonatsabrechnungversandAbteilungenCtrl panelCtrl = new AutoJobMonatsabrechnungversandAbteilungenCtrl();
		IPanelAutoJobDetails panel = new PanelAutoJobMonatsabrechnungversandAbteilungen(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelAuslieferliste() {
		IAutoJobDetailsPanelCtrl<JobDetailsAuslieferlisteDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobAuslieferlisteDelegate(), 
				JobDetailsAuslieferlisteDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobAuslieferliste(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelErImport() {
		IAutoJobDetailsPanelCtrl<JobDetailsErImportDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobErImportXlsImportDelegate(), 
				JobDetailsErImportDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobErXlsImport(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelWareneingangsjournal() {
		IAutoJobDetailsPanelCtrl<JobDetailsWEJournalDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobWEJournalDelegate(), 
				JobDetailsWEJournalDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobWEJournal(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelArbeitszeitstatus() {
		IAutoJobDetailsPanelCtrl<JobDetailsArbeitszeitstatusDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobArbeitszeitstatusDelegate(), 
				JobDetailsArbeitszeitstatusDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobArbeitszeitstatus(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelKpiReport() {
		IAutoJobDetailsPanelCtrl<JobDetailsKpiReportDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobKpiReportDelegate(), 
				JobDetailsKpiReportDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobKpiReport(panelCtrl);
		return panel;
	}

	public IPanelAutoJobDetails getPanelAutoJobHead(AutomatikjobDto jobDto, AutomatikjobtypeDto jobTypeDto) {
		IAutoJobHeadPanelCtrl panelCtrl = new AutoJobHeadCtrl(jobDto, jobTypeDto);
		IPanelAutoJobDetails panel = new PanelAutoJobHead(panelCtrl);
		return panel;
	}

	private IPanelAutoJobDetails createPanelBedarfsuebernahmeOffen() {
		IAutoJobDetailsPanelCtrl<JobDetailsBedarfsuebernahmeOffeneDto> panelCtrl = new AutoJobPanelCtrl<>(
				DelegateFactory.getInstance().getAutoJobBedarfsuebernahmeOffenDelegate(), JobDetailsBedarfsuebernahmeOffeneDto.class);
		IPanelAutoJobDetails panel = new PanelAutoJobBedarfsuebernahmeOffene(panelCtrl);
		return panel;
	}
}
