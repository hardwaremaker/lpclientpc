package com.lp.client.remote;

import java.awt.Frame;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.artikel.InternalFrameArtikel;
import com.lp.client.artikel.TabbedPaneArtikel;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.stueckliste.InternalFrameStueckliste;
import com.lp.client.stueckliste.StuecklisteFilterFactory;
import com.lp.client.stueckliste.TabbedPaneStueckliste;
import com.lp.server.system.service.LocaleFac;

/**
 * RemoteClient fuer Calls zwischen Clients
 *
 */
public class RMIServiceCallsFromClient implements RemoteClient {

	public void zeigeMeldung(String message) {
		DialogFactory.showModalDialog("INFO", message);
	}

	public void gotoStueckliste(Integer stuecklisteIId) throws Throwable {
		
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_STUECKLISTE)) {
			InternalFrameStueckliste ifStueckliste = (InternalFrameStueckliste) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_STUECKLISTE);
			ifStueckliste
					.geheZu(InternalFrameStueckliste.IDX_TABBED_PANE_STUECKLISTE,
							TabbedPaneStueckliste.IDX_PANEL_AUSWAHL,
							stuecklisteIId,
							null,
							StuecklisteFilterFactory
									.getInstance()
									.createFKStuecklisteKey(
											(Integer) stuecklisteIId));
			
			
              java.awt.EventQueue.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                    	 LPMain.getInstance().getDesktop().setState(Frame.NORMAL);
                    	 LPMain.getInstance().getDesktop().toFront();
                    	 LPMain.getInstance().getDesktop().setAlwaysOnTop(true);
                    	 LPMain.getInstance().getDesktop().setVisible(true);
                    	 LPMain.getInstance().getDesktop().setAlwaysOnTop(false);
                     }
                 }); 
             
			
		}
	}
	
	
public void gotoArtikelieferant(Integer artikelIId, Integer artikellieferantIId) throws Throwable {
		
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufModulZugreifen(
						LocaleFac.BELEGART_ARTIKEL)) {
			InternalFrameArtikel ifArtikel= (InternalFrameArtikel) LPMain
					.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_ARTIKEL);
			ifArtikel
					.geheZu(InternalFrameArtikel.IDX_TABBED_PANE_ARTIKEL,
							TabbedPaneArtikel.IDX_PANEL_LIEFERANT,
							artikelIId,
							artikellieferantIId,
							ArtikelFilterFactory
									.getInstance()
									.createFKArtikellisteKey(
											(Integer) artikelIId));
			
			
              java.awt.EventQueue.invokeLater(new Runnable() {
                     @Override
                     public void run() {
                    	 LPMain.getInstance().getDesktop().setState(Frame.NORMAL);
                    	 LPMain.getInstance().getDesktop().toFront();
                    	 LPMain.getInstance().getDesktop().setAlwaysOnTop(true);
                    	 LPMain.getInstance().getDesktop().setVisible(true);
                    	 LPMain.getInstance().getDesktop().setAlwaysOnTop(false);
                     }
                 }); 
             
			
		}
	}
	

}
