package com.lp.client.pc.erroraction;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class LosausgabeHttpTopsError implements IErrorAction {

	@Override
	public String getMsg(ExceptionLP exc) {
		String token = getToken(exc);
		
		if (Helper.isOneOf(exc.getICode(), 
				EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_MATERIAL,
				EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_ARTIKEL,
				EJBExceptionLP.FEHLER_TOPS_IO_READING_METADATEN,
				EJBExceptionLP.FEHLER_TOPS_IO_SEARCHING_CADFILE,
				EJBExceptionLP.FEHLER_TOPS_UNBEKANNT)) {
			return LPMain.getMessageTextRespectUISPr(token, exc.getSMsg(), exc.getAlInfoForTheClient());
		}
		return LPMain.getMessageTextRespectUISPr(token, exc.getAlInfoForTheClient().toArray());
	}

	private String getToken(ExceptionLP exc) {
		switch (exc.getICode()) {
		
		case EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEINE_HOEHE:
			return "fert.losausgabe.tops.artikelfehlthoehe";
		case EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEIN_MATERIAL:
			return "fert.losausgabe.tops.artikelfehltmaterial";
		case EJBExceptionLP.FEHLER_TOPS_ARTIKEL_KEINE_LASEROBERFLAECHE:
			return "fert.losausgabe.tops.artikelfehltlaseroberflaeche";
		case EJBExceptionLP.FEHLER_TOPS_LOS_NICHT_GEFUNDEN:
			return "fert.losausgabe.tops.losnichtgefunden";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_KUNDE_ZU_LOS:
			return "fert.losausgabe.tops.keinkunde";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_DEBITORENKONTO:
			return "fert.losausgabe.tops.kundekeindebitor";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_MATERIAL:
			return "fert.losausgabe.tops.keintopsmaterial";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_ARBEITSGANG:
			return "fert.losausgabe.tops.keintopsag";
		case EJBExceptionLP.FEHLER_TOPS_IST_LETZTER_AG:
			return "fert.losausgabe.tops.topsagistletzter";
		case EJBExceptionLP.FEHLER_TOPS_XMLMARSHALLING:
			return exc.getAlInfoForTheClient().size() > 2
					? "fert.losausgabe.tops.xmlmarshalling.material"
					: "fert.losausgabe.tops.xmlmarshalling.artikel";
		case EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_MATERIAL:
			return "fert.losausgabe.tops.io.fileexport.material";
		case EJBExceptionLP.FEHLER_TOPS_IO_FILEEXPORT_ARTIKEL:
			return "fert.losausgabe.tops.io.fileexport.artikel";
		case EJBExceptionLP.FEHLER_TOPS_UPDATE_MATERIAL:
			return "fert.losausgabe.tops.update.material";
		case EJBExceptionLP.FEHLER_TOPS_UPDATE_ARTIKEL:
			return "fert.losausgabe.tops.update.artikel";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_ARTIKEL_PFAD:
			return "fert.losausgabe.tops.keincadfileinartikelpfad";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_STKL_PFAD:
			return "fert.losausgabe.tops.keincadfileinstklpfad";
		case EJBExceptionLP.FEHLER_TOPS_KEIN_CADFILE_IN_HILFSSTKL_PFAD:
			return "fert.losausgabe.tops.keincadfileinhilfsstklpfad";
		case EJBExceptionLP.FEHLER_TOPS_LETZTER_EXPORT_PFAD_UNTERSCHIEDLICH:
			return "fert.losausgabe.tops.letzterexportpfadanders";
		case EJBExceptionLP.FEHLER_TOPS_IO_SEARCHING_CADFILE:
			return "fert.losausgabe.tops.io.cadfilesuche";
		case EJBExceptionLP.FEHLER_TOPS_IO_READING_METADATEN:
			return "fert.losausgabe.tops.io.lesendermetadaten";
		case EJBExceptionLP.FEHLER_TOPS_CADFILE_NICHT_GEFUNDEN:
			return "fert.losausgabe.tops.cadfilenichtgefunden";
		case EJBExceptionLP.FEHLER_TOPS_UNBEKANNT:
			return "fert.losausgabe.tops.unbekannt";
		}
		return null;
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		return false;
	}

}
