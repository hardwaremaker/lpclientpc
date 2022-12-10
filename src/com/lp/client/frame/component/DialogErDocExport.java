package com.lp.client.frame.component;

import java.awt.Frame;
import java.util.List;

import com.lp.client.finanz.TabbedPaneExport;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.ErDocExportProperties;

public class DialogErDocExport extends DialogBelegDocExport {
	private static final long serialVersionUID = -7503559164319018786L;

	private ErDocExportProperties properties;
	
	public DialogErDocExport(Frame owner, InternalFrame internalFrame) {
		super(owner, internalFrame, LPMain.getTextRespectUISPr("er.export.dokumente.dialogtitel"));
	}

	public ErDocExportProperties getProperties() {
		if (properties == null) {
			properties = new ErDocExportProperties();
		}
		return properties;
	}
	
	@Override
	protected PanelQueryFLR createPartnerPanelFLR() throws Throwable {
		if (getProperties().getSupplierId().isPresent())
			return PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
					getProperties().getSupplierId().get().id(), true, false);

		return PartnerFilterFactory.getInstance().createPanelFLRLieferant(getInternalFrame(),
				null, true, false);
	}

	@Override
	protected void onExportButton() {
		try {
			TabbedPaneExport paneExport = new TabbedPaneExport(getInternalFrame());
			List<EingangsrechnungDto> eingangsrechnungen = DelegateFactory.getInstance()
					.getEingangsrechnungDelegate().eingangsrechnungFindByBelegdatumVonBis(getStartdate(), getEnddate());
			paneExport.saveEingangsrechnungBelege(eingangsrechnungen, getDirectory());
			showDialogExportSucceeded();
		} catch (Throwable e) {
			showDialogError(e);
		}		
	}
	
	@Override
	protected FileChooserConfigToken getFileChooserConfigToken() {
		return FileChooserConfigToken.ExportDocEingangsrechnung;
	}
}
