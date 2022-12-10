package com.lp.client.stueckliste.importassistent;

import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.CondensedResultList;
import com.lp.server.stueckliste.service.IStklImportResult;
import com.lp.server.system.service.MandantFac;

public class StklImportPage3CtrlVerkauf extends StklImportPage3Ctrl {

	protected boolean bZentralerArtikelstamm;

	public StklImportPage3CtrlVerkauf(StklImportModel model) {
		super(model);
		this.bZentralerArtikelstamm = LPMain.getInstance().getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_ZENTRALER_ARTIKELSTAMM);
	}

	@Override
	public TableModel getResultTableModel() {
		if(model.getResults() != null) {
			List<IStklImportResult> results = zusammengefasst ?
					new CondensedResultList(model.getResults()) : model.getResults();
			return new ResultTableModelVerkauf(
					this, results, model.getSelectedSpezifikation().getColumnTypes());
		}
		return new DefaultTableModel();
	}

	@Override
	public String getMappingUpdateTristateCheckboxText() {
		return LPMain.getTextRespectUISPr(
				"stkl.intelligenterstklimport.sokoupdate.tristatecheckbox");
	}

	public boolean darfArtikelUpdaten() {
		return bZentralerArtikelstamm;
	}

}
