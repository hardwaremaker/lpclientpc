package com.lp.client.artikel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.VendidataArticleExportResult;

public class VendidataArtikelExportController implements IVendidataExportController<VendidataArticleExportResult> {
	
	private File exportFile;
	private InternalFrame internalFrame;

	public VendidataArtikelExportController(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	@Override
	public void setFile(File fileToExport) {
		exportFile = fileToExport;
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
	public VendidataArticleExportResult checkExport() {
		return exportXML(true);
	}

	@Override
	public VendidataArticleExportResult doExport() throws FileNotFoundException, IOException {
		VendidataArticleExportResult result = exportXML(false);
		LPMain.getInstance().saveFile(internalFrame,
				exportFile.getAbsolutePath(), result.getXmlContent().getBytes("UTF-8"), false);
		
		return result;
	}

	@Override
	public VendidataArticleExportResult exportXML(boolean checkOnly) {
		try {
			VendidataArticleExportResult result = DelegateFactory.getInstance().getArtikelDelegate()
					.exportiere4VendingArtikel(checkOnly);
			return result;
		} catch (ExceptionLP e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
