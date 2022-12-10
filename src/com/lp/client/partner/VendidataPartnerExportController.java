package com.lp.client.partner;

import java.io.File;

import com.lp.client.artikel.IVendidataExportController;
import com.lp.client.frame.component.InternalFrame;
import com.lp.server.partner.service.VendidataPartnerExportResult;

public abstract class VendidataPartnerExportController implements
		IVendidataExportController<VendidataPartnerExportResult> {
	
	private File exportFile;
	private InternalFrame internalFrame;

	public VendidataPartnerExportController(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	protected File getExportFile() {
		return exportFile;
	}

	protected InternalFrame getInternalFrame() {
		return internalFrame;
	}

	@Override
	public void setFile(File file) {
		exportFile = file;
	}

	@Override
	public void setFile(String path) {
		if (!path.toLowerCase().endsWith(".xml")) {
			path = path + ".xml";
		}
		setFile(new File(path));
	}

	@Override
	public File getFile() {
		return exportFile;
	}

	@Override
	public VendidataPartnerExportResult checkExport() {
		return exportXML(true);
	}


}
