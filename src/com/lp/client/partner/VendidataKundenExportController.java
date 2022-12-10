package com.lp.client.partner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.partner.service.VendidataPartnerExportResult;
import com.lp.util.EJBExceptionLP;

public class VendidataKundenExportController extends
		VendidataPartnerExportController {

	public VendidataKundenExportController(InternalFrame internalFrame) {
		super(internalFrame);
	}

	@Override
	public VendidataPartnerExportResult doExport() throws FileNotFoundException, IOException {
		VendidataPartnerExportResult result = exportXML(false);
		if (getExportFile() == null) return result;
		
		LPMain.getInstance().saveFile(getInternalFrame(), 
				getExportFile().getAbsolutePath(), result.getXmlContent().getBytes("UTF-8"), false);
		
		return result;
	}

	@Override
	public VendidataPartnerExportResult exportXML(boolean checkOnly) {
		try {
			VendidataPartnerExportResult result = DelegateFactory.getInstance().getKundeDelegate()
					.exportiere4VendingKunden(checkOnly);
			return result;
		} catch (Throwable e) {
			return new VendidataPartnerExportResult(new ArrayList<EJBExceptionLP>(), new VendidataExportStats());
		}
	}
}
