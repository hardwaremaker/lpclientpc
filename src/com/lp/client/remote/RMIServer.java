package com.lp.client.remote;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.service.ClientRemoteFac;

public class RMIServer {

	Integer iPortFound = null;

	public void starteServer() {
		

		for (int i = 7777; i < 7800; i++) {

			System.out.println("--------------Testing port " + i);
			Socket s = null;
			try {
				s = new Socket("localhost", i);

				System.out.println("--------------Port " + i + " in use");
			} catch (IOException e) {
				System.out.println("--------------Port " + i + " is available");
				iPortFound = i;
				break;
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
						throw new RuntimeException(
								"You should handle this error.", e);
					}
				}
			}
			
			
			System.out.println("--------------Port found");
			
		}

		if (iPortFound != null) {
			try {

				DelegateFactory.getInstance().getTheClientDelegate()
						.setRmiPort(iPortFound);

				java.rmi.registry.LocateRegistry.createRegistry(iPortFound);

				RemoteClient objClient = new RMIServiceCallsFromClient();
				RemoteClient stubClient = (RemoteClient) UnicastRemoteObject
						.exportObject(objClient, 0);
				ClientRemoteFac objEjb = new RMIServiceCallsFromEJB();
				ClientRemoteFac stubEjb = (ClientRemoteFac) UnicastRemoteObject
						.exportObject(objEjb, 0);

				// Bind the remote object's stub in the registry
				Registry registry = LocateRegistry.getRegistry(iPortFound);
				registry.bind(RemoteClient.REMOTE_BIND_NAME, stubClient);
				registry.bind(ClientRemoteFac.REMOTE_BIND_NAME, stubEjb);

				System.err.println("Server ready on Port " + iPortFound);
			} catch (Exception e) {
				System.err.println("Server exception: " + e.toString());
				e.printStackTrace();
			}
		} else {
			System.err.println("no Port found");
		}
	}

	
	public void updateRmiPort() throws ExceptionLP {
		DelegateFactory.getInstance().getTheClientDelegate()
		.setRmiPort(iPortFound);
	}
	
	public RMIServer() {
		
	}


}
