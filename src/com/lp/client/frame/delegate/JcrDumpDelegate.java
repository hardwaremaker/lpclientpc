package com.lp.client.frame.delegate;

import javax.naming.Context;
import javax.naming.InitialContext;

import com.lp.client.frame.ExceptionLP;
import com.lp.server.instandhaltung.service.InstandhaltungReportFac;
import com.lp.server.system.jcr.service.JcrDmsConfig;
import com.lp.server.system.jcr.service.JcrDumpFac;
import com.lp.server.system.jcr.service.JcrDumpResult;
import com.lp.server.system.jcr.service.JcrScanResult;
import com.lp.util.EJBExceptionLP;

public class JcrDumpDelegate extends Delegate {
	private Context context;
	private JcrDumpFac fac;

	public JcrDumpDelegate() throws ExceptionLP {
		try {
			context = new InitialContext();
			fac = lookupFac(context, JcrDumpFac.class);
		} catch (Throwable t) {
			throw new ExceptionLP(EJBExceptionLP.FEHLER, t);
		}
	}
	
	public JcrScanResult scan() throws ExceptionLP {
		try {
			return fac.scan() ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}		
	}
	
	/**
	 * Die Quell-Dokumentenablage auf die Ziel-Dokumentenablage dumpen
	 * @return die Anzahl der verarbeiteten Knoten
	 * @throws ExceptionLP
	 */
	public JcrDumpResult dumpPath(String rootPath) throws ExceptionLP {
		try {
			return fac.dumpPath(rootPath) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}

	/**
	 * Die Quell-Dokumentenablage auf die Ziel-Dokumentenablage dumpen
	 * @return die Anzahl der verarbeiteten Knoten
	 * @throws ExceptionLP
	 */
	public JcrDumpResult dump() throws ExceptionLP {
		try {
			return fac.dump() ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}

	public JcrDumpResult dump(JcrDmsConfig sourceConfig, JcrDmsConfig targetConfig) throws ExceptionLP {
		try {
			return fac.dump(sourceConfig, targetConfig) ;
		} catch(Throwable t) {
			handleThrowable(t) ;
			return null ;
		}
	}
}
