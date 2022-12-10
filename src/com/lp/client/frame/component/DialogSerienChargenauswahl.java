
package com.lp.client.frame.component;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.JLabel;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogSerienChargenauswahl {

	boolean bSeriennummer = false;

	private DialogSerienChargenauswahlSeriennummern dialogSnr = null;

	private DialogSerienChargenauswahlChargennummern dialogChnr = null;

	public List<SeriennrChargennrMitMengeDto> alSeriennummern = null;
	public boolean bAbbruch = false;

	public DialogSerienChargenauswahl(Integer artikelIId, Integer lagerIId,
			List<SeriennrChargennrMitMengeDto> alSeriennummern, boolean bMultiselection,
			boolean selektierteNichtAnzeigen, InternalFrame internalFrame, WrapperNumberField wnfBeleg, boolean bZugang,
			Integer gebindeIId, BigDecimal bdGebindemenge) throws Throwable {

		ArtikelDto artikelDto = DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

		if (artikelDto.isSeriennrtragend()) {
			bSeriennummer = true;
			dialogSnr = new DialogSerienChargenauswahlSeriennummern(artikelIId, lagerIId, alSeriennummern,
					bMultiselection, selektierteNichtAnzeigen, internalFrame, wnfBeleg, bZugang);
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialogSnr);

		} else {
			bSeriennummer = false;
			dialogChnr = new DialogSerienChargenauswahlChargennummern(artikelIId, lagerIId, alSeriennummern,
					bMultiselection, selektierteNichtAnzeigen, internalFrame, wnfBeleg, bZugang, gebindeIId,
					bdGebindemenge);
			LPMain.getInstance().getDesktop().platziereDialogInDerMitteDesFensters(dialogChnr);

		}

	}

	public void setBdBenoetigteMenge(BigDecimal bdBenoetigteMenge) throws Throwable {

		if (bSeriennummer) {
			dialogSnr.setBdBenoetigteMenge(bdBenoetigteMenge);
		} else {
			dialogChnr.setBdBenoetigteMenge(bdBenoetigteMenge);
		}

	}

	public void setBdOffeneMenge(BigDecimal bdOffen) throws Throwable {

		if (bSeriennummer) {
			dialogSnr.setBdOffeneMenge(bdOffen);
		} else {
			dialogChnr.setBdOffeneMenge(bdOffen);
		}

	}

	public void setVisible(boolean b) throws Throwable {

		if (bSeriennummer) {
			dialogSnr.setVisible(b);

			alSeriennummern = dialogSnr.alSeriennummern;
			bAbbruch = dialogSnr.bAbbruch;

		} else {
			dialogChnr.setVisible(b);
			alSeriennummern = dialogChnr.alSeriennummern;
			bAbbruch = dialogChnr.bAbbruch;
		}

	}

	public boolean isRueckgabe() {
		if (bSeriennummer) {
			return dialogSnr.wcbRueckgabe.isSelected();
		} else {
			return dialogChnr.wcbRueckgabe.isSelected();
		}
	}

}
