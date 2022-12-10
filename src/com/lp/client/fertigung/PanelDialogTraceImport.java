
/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.fertigung;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonAbstractAction;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelsperrenDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.fertigung.service.BucheSerienChnrAufLosDto;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.fertigung.service.LossollmaterialDto;
import com.lp.server.fertigung.service.TraceImportDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.GotoHelper;
import com.lp.util.Helper;

/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.8 $
 */
public class PanelDialogTraceImport extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<LossollmaterialDto> lossollmaterialDtos = new ArrayList<LossollmaterialDto>();

	private ArrayList<WrapperNumberField> wnfzuBuchen = new ArrayList<WrapperNumberField>();

	private ArrayList<TraceImportDto> alZuBuchen = null;

	private InternalFrameFertigung internalFrameFertigung = null;

	private JScrollPane jspScrollPane = new JScrollPane();
	
	private File file=null;

	public PanelDialogTraceImport(InternalFrameFertigung internalFrameFertigung, ArrayList<TraceImportDto> alZuBuchen,
			String title, File file) throws Throwable {
		super(internalFrameFertigung, title);
		this.internalFrameFertigung = internalFrameFertigung;
		this.alZuBuchen = alZuBuchen;
		this.file=file;

		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		getInternalFrame().addItemChangedListener(this);

		iZeile++;

		WrapperLabel wlaStueckliste = new WrapperLabel(LPMain.getTextRespectUISPr("stkl.stueckliste"));

		wlaStueckliste.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		/*
		 * WrapperLabel wlaBezeichnung = new
		 * WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		 * wlaBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);
		 * jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(1, iZeile, 1, 1, 0.1,
		 * 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2,
		 * 2, 2, 2), 0, 0));
		 */

		jpaWorkingOn.add(new JLabel("Material"), new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));

		jpaWorkingOn.add(new JLabel("Bezeichnung"), new GridBagConstraints(2, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));

		jpaWorkingOn.add(new JLabel("Charge"), new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30, 0));

		jpaWorkingOn.add(new JLabel("PCB"), new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel("Lagerstand"), new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(new WrapperLabel("zu Verbrauchen"), new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 60, 0));

		jpaWorkingOn.add(new JLabel("Originalartikel"), new GridBagConstraints(7, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));
		
		jpaWorkingOn.add(new JLabel("Losnummer"), new GridBagConstraints(8, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(new WrapperLabel("Losgroesse"), new GridBagConstraints(9, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(new WrapperLabel("Offen"), new GridBagConstraints(10, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 50, 0));

		jpaWorkingOn.add(new WrapperLabel("zu Buchen"), new GridBagConstraints(11, iZeile, 1, 1, 0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		for (int i = 0; i < alZuBuchen.size(); i++) {

			TraceImportDto traceImportDto = alZuBuchen.get(i);

			BigDecimal bdVerfuegbareMenge = traceImportDto.getMengeAufHauptlager();

			Iterator itLose= traceImportDto.getSollmaterialoffeneLose().keySet().iterator();
			int j=0;
			while(itLose.hasNext()) {

				String losnummer=(String) itLose.next();
				
				LossollmaterialDto sollMatDto = traceImportDto.getSollmaterialoffeneLose().get(losnummer);
				if (j == 0) {
					jpaWorkingOn.add(new JLabel(traceImportDto.getStuecklisteDto().getArtikelDto().getCNr()),
							new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 2), 0, 0));

					/*
					 * jpaWorkingOn.add(new
					 * JLabel(traceImportDto.getStuecklisteDto().getArtikelDto().formatBezeichnung()
					 * ), new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
					 * GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 2), 0, 0));
					 */

					jpaWorkingOn.add(new JLabel(traceImportDto.getArtikelDtoMaterial().getCNr()),
							new GridBagConstraints(1, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
					
					
					JLabel materialBezeichnung = new JLabel(traceImportDto.getArtikelDtoMaterial().getCBezAusSpr());

					materialBezeichnung.setToolTipText(traceImportDto.getArtikelDtoMaterial().formatArtikelbezeichnung());
					
					Dimension d = new Dimension(150, Defaults.getInstance().getControlHeight());

					materialBezeichnung.setPreferredSize(d);
					materialBezeichnung.setMaximumSize(d);
					
					jpaWorkingOn.add(materialBezeichnung,
							new GridBagConstraints(2, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

					jpaWorkingOn.add(new JLabel(traceImportDto.getChargennummer()),
							new GridBagConstraints(3, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

					jpaWorkingOn.add(new WrapperLabel(Helper.formatZahl(traceImportDto.getAnzahlPcb(), 0,
							LPMain.getTheClient().getLocUi())),
							new GridBagConstraints(4, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40, 0));

					jpaWorkingOn.add(
							new WrapperLabel(Helper.formatZahl(traceImportDto.getMengeAufHauptlager(), 2,
									LPMain.getTheClient().getLocUi())),
							new GridBagConstraints(5, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

					jpaWorkingOn.add(
							new WrapperLabel(Helper.formatZahl(traceImportDto.getZuBuchendeMenge(), 2,
									LPMain.getTheClient().getLocUi())),
							new GridBagConstraints(6, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
									GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				}
				LosDto losDto = sollMatDto.getLosDto_NOT_IN_DB();

				
				
				String originalartikel="";
				if(sollMatDto.getLossollmaterialIIdOriginal()!=null) {
					LossollmaterialDto lossollmaterialDtoOrgi=DelegateFactory.getInstance().getFertigungDelegate().lossollmaterialFindByPrimaryKey(sollMatDto.getLossollmaterialIIdOriginal());
					ArtikelDto aDtoOri=DelegateFactory.getInstance().getArtikelDelegate().artikelFindByPrimaryKeySmallOhneExc(lossollmaterialDtoOrgi.getArtikelIId());
					originalartikel=aDtoOri.getCNr();
				}else {
					originalartikel=traceImportDto.getArtikelDtoMaterial().getCNr();
				}
				
				jpaWorkingOn.add(new JLabel(originalartikel), new GridBagConstraints(7, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				
				jpaWorkingOn.add(new JLabel(losDto.getCNr()), new GridBagConstraints(8, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
				
				

				jpaWorkingOn.add(
						new WrapperLabel(
								Helper.formatZahl(losDto.getNLosgroesse(), 2, LPMain.getTheClient().getLocUi())),
						new GridBagConstraints(9, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				jpaWorkingOn.add(
						new WrapperLabel(Helper.formatZahl(sollMatDto.getNOffeneMenge_NOT_IN_DB(), 2,
								LPMain.getTheClient().getLocUi())),
						new GridBagConstraints(10, iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				WrapperNumberField wnfZuBuchen = new WrapperNumberField(5);

				
				wnfZuBuchen.setBigDecimal(sollMatDto.getNZuVerbrauchendenMenge_NOT_IN_DB());
				

				jpaWorkingOn.add(wnfZuBuchen, new GridBagConstraints(11, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

				iZeile++;

				wnfzuBuchen.add(wnfZuBuchen);

				lossollmaterialDtos.add(sollMatDto);
				
				j++;

			}

		}

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {

			if (allMandatoryFieldsSetDlg()) {

				ArrayList<BucheSerienChnrAufLosDto> zuBuchen = new ArrayList<BucheSerienChnrAufLosDto>();

				LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate().getHauptlagerDesMandanten();

				for (int i = 0; i < wnfzuBuchen.size(); i++) {

					if (wnfzuBuchen.get(i).getBigDecimal() != null
							&& wnfzuBuchen.get(i).getBigDecimal().doubleValue() > 0) {

						BucheSerienChnrAufLosDto charge = new BucheSerienChnrAufLosDto();
						charge.setArtikelIId(lossollmaterialDtos.get(i).getArtikelIId());
						charge.setCSeriennrChargennr(lossollmaterialDtos.get(i).getSnrChnr_NOT_IN_DB());
						charge.setLagerIId(lagerDto.getIId());
						charge.setLossollmaterialIId(lossollmaterialDtos.get(i).getIId());
						charge.setNMenge(wnfzuBuchen.get(i).getBigDecimal());
						zuBuchen.add(charge);

					}

				}

				DelegateFactory.getInstance().getFertigungDelegate().traceImportBuchen(zuBuchen);

				
				String filename=file.getAbsolutePath();
				
				int iPunkt=filename.lastIndexOf('.');
				if(iPunkt>0) {
					String endung=filename.substring(iPunkt);
					String datei=filename.substring(0,iPunkt);
					file.renameTo(new File(datei+"_IMPORTIERT"+endung));
				}
				
				getInternalFrame().closePanelDialog();
			}

		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */

	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
