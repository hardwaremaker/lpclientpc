package com.lp.client.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {

	public static String REMOTE_BIND_NAME = "CALLS_FROM_CLIENT";

	public void zeigeMeldung(String message) throws RemoteException;
	
	public void gotoStueckliste(Integer stuecklisteIId) throws Throwable;

	public void gotoArtikelieferant(Integer artikelIId, Integer artikellieferantIId) throws Throwable;
}
