package com.lp.client.remote;

import java.rmi.RemoteException;

import com.lp.client.pc.LPMain;
import com.lp.client.pc.PanelDesktopStatusbar;
import com.lp.server.system.service.ClientRemoteFac;
import com.lp.server.system.service.PayloadDto;
import com.lp.server.system.service.TheClientDto;

/**
 * RemoteClient fuer Calls vom EJB
 * 
 * @author andi
 *
 */
public class RMIServiceCallsFromEJB implements ClientRemoteFac {

	public void publish(PayloadDto payloadDto) throws RemoteException {
		Router.getInstance().delegate(payloadDto);
	}
	public void neueNachrichtenVerfuegbar() throws RemoteException{
		LPMain.getInstance().getDesktop().getDesktopStatusBar().showPopupWennNeueNachrichtenVerfuegbar(false);
	}
}
