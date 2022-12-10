package com.lp.client.remote;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.VerfuegbareHostsDto;

public class RMIClient {

	private static RMIClient rmiClient = null;

	HashMap hmVerbundeneClients = new HashMap();

	static public RMIClient getInstance() {
		if (rmiClient == null) {
			rmiClient = new RMIClient();
		}

		return rmiClient;
	}

	private RemoteClient sucheRemoteclient(String mandantCNr) throws Throwable {

		List<VerfuegbareHostsDto> hosts = DelegateFactory.getInstance().getTheClientDelegate()
				.getVerfuegbareHosts(LPMain.getTheClient().getBenutzername(), mandantCNr);

		if (hosts != null && hosts.size() > 0) {
			boolean bClientGefunden = false;

			Iterator it = hosts.iterator();
			while (it.hasNext() && bClientGefunden == false) {
				try {

					VerfuegbareHostsDto host = (VerfuegbareHostsDto) it.next();

					Registry registry = LocateRegistry.getRegistry(host.getHostname(), host.getPort());
					RemoteClient stub = (RemoteClient) registry.lookup(RemoteClient.REMOTE_BIND_NAME);
					bClientGefunden = true;
					return stub;
				} catch (Exception e) {
					System.err.println("Client exception: " + e.toString());
					e.printStackTrace();
				}
			}

			if (bClientGefunden == false) {
				DialogFactory.showModalDialog("Info",
						"Keinen offenen Client des Mandanten " + mandantCNr + " gefunden");

			}

		} else {
			DialogFactory.showModalDialog("Info", "Keinen offenen Client des Mandanten " + mandantCNr + " gefunden");
		}
		return null;
	}

	public void gotoStuecklisteAndererMandant(Integer stuecklisteIId, String mandantCNr) throws Throwable {

		RemoteClient rc = sucheRemoteclient(mandantCNr);
		if (rc != null) {
			rc.gotoStueckliste(stuecklisteIId);
		}

	}

	public void gotoArtikellieferantAndererMandant(Integer artikelIId, Integer artikellieferantIId, String mandantCNr)
			throws Throwable {

		RemoteClient rc = sucheRemoteclient(mandantCNr);
		if (rc != null) {
			rc.gotoArtikelieferant(artikelIId, artikellieferantIId);
		}

	}
}
