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
package com.lp.client.lieferschein;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportIfJRDSHasNonPersistentFields;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.lieferschein.service.PaketVersandAntwortDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferschein;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.miginfocom.swing.MigLayout;


public class ReportPostVersandetiketten extends ReportEtikett implements
	PanelReportIfJRDSHasNonPersistentFields {
	private static final long serialVersionUID = 1L;
	private LieferscheinDto lieferscheinDto = null;

	private WrapperLabel wlaPakete = null;
	private WrapperNumberField wnfPakete = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperLabel wlaVersandnummer = null;
	private WrapperTextField wtfVersandnummer = null;

	private WrapperLabel wlaVersandnummer2 = null;
	private WrapperTextField wtfVersandnummer2 = null;

	private WrapperCheckBox wcbVersandnummernUeberschreiben = null;
	private WrapperLabel wlaVersandnummernUeberschreiben = null;
	private PanelAuslieferdatum panelAuslieferdatum = null ;
	

	public ReportPostVersandetiketten(InternalFrame internalFrame,
			LieferscheinDto lieferscheinDto, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		this.lieferscheinDto = lieferscheinDto;
		jbInit();
//		initializeVersandnummernUeberschreiben();
		setDefaults();
	}

	private void setDefaults() {
		if(Helper.isStringEmpty(lieferscheinDto.getCVersandnummer())) {
			wcbVersandnummernUeberschreiben.setVisible(false);
			wlaVersandnummernUeberschreiben.setVisible(false);
			wlaVersandnummernUeberschreiben.setText("");
			wlaVersandnummernUeberschreiben.setForeground(
					Defaults.getInstance().getDefaultTextColor());
		} else {
			wcbVersandnummernUeberschreiben.setVisible(false);
			wlaVersandnummernUeberschreiben.setText(lieferscheinDto.getCVersandnummer());
			wlaVersandnummernUeberschreiben.setForeground(
					Defaults.getInstance().getInvalidTextColor());
			wcbVersandnummernUeberschreiben.setVisible(true);
			wlaVersandnummernUeberschreiben.setVisible(true);
		}
	}

	private void initializeVersandnummernUeberschreiben() {
		if(Helper.isStringEmpty(lieferscheinDto.getCVersandnummer())) {
			wcbVersandnummernUeberschreiben.setSelected(true);
		} else {
			wcbVersandnummernUeberschreiben.setSelected(false);
		}		
	}
	
	public String getModul() {
		return LieferscheinReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_PLC_VERSANDETIKETTEN;

	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		if(!Helper.isStringEmpty(lieferscheinDto.getCVersandnummer()) && 
				!wcbVersandnummernUeberschreiben.isSelected()) {
			DocPath dp = new DocPath(new DocNodeLieferschein(lieferscheinDto))
					.add(new DocNodeFile("Paketetiketten.pdf"));
			JCRDocDto jcrDto = DelegateFactory.getInstance()
					.getJCRDocDelegate().getJCRDocDtoFromNode(dp);
			jcrDto = DelegateFactory.getInstance().getJCRDocDelegate().getData(jcrDto);
			byte[] pdf = jcrDto.getbData();
			return DelegateFactory.getInstance().getLieferscheinReportDelegate()
					.printPostVersandetikett(lieferscheinDto.getIId(), pdf);			
	
/*			
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_ES_EXISTIERT_BEREITS_EINE_VERSANDNUMMER,
					textFromToken("ls.versandnummer.existiert"), null,
					lieferscheinDto.getCVersandnummer(), lieferscheinDto.getCVersandnummer2());
*/								
		}

//		PaketVersandAntwortDto antwortDto = dialogPlcApiCall();
//		if(workerAntwortDto != null) {
//			antwortDto = workerAntwortDto;
//		}
		
		PaketVersandAntwortDto antwortDto = DelegateFactory.getInstance()
				.getLsDelegate().erzeugePaketInfo(lieferscheinDto.getIId());
		if(!antwortDto.isOk()) {
			int errcode = EJBExceptionLP.FEHLER_VERSANDNUMMER_NICHT_ERZEUGBAR;
			if(antwortDto.getErrorCode().contains("RecipientAddress-AddressLine1")) {
				errcode = EJBExceptionLP.FEHLER_PLC_PFLICHTFELD_STRASSE_FEHLT;
			}
			
			throw new ExceptionLP(errcode,
					textFromToken("ls.versandnummer.apifehler"), antwortDto.getException(),
					antwortDto.getErrorCode(), antwortDto.getErrorMessage());
		}

		wlaVersandnummernUeberschreiben.setText(
				StringUtils.join(antwortDto.getTrackingNumbers().iterator(), ","));
		wlaVersandnummernUeberschreiben.setForeground(
				Defaults.getInstance().getDefaultTextColor());
		
		byte[] pdf = Base64.decodeBase64(antwortDto.getPdfContent().getBytes());
		return DelegateFactory.getInstance().getLieferscheinReportDelegate()
				.printPostVersandetikett(lieferscheinDto.getIId(), pdf);			
	}

	public boolean getBErstelleReportSofort() {
		return true;
		
//		return Helper.isStringEmpty(lieferscheinDto.getCVersandnummer()) && 
//				lieferscheinDto.getIAnzahlPakete() > 0;
	}

	public void reportKonfGeladen() {
		wcbVersandnummernUeberschreiben.setSelected(false);
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	private void jbInit() throws Throwable {
		iZeile++;

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };

		wlaPakete = new WrapperLabel(textFromToken("ls.pakete"));
		wnfPakete = new WrapperNumberField();
		wnfPakete.setFractionDigits(0);
		wnfPakete.setMinimumValue(0);

		wnfPakete.setInteger(lieferscheinDto.getIAnzahlPakete());
		
		wlaGewicht = new WrapperLabel(textFromToken("lp.gewicht"));
		wnfGewicht = new WrapperNumberField();
		wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
		wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setDefaultsToComponent(wlaEinheit, 25);

		wlaVersandnummer = new WrapperLabel(textFromToken("ls.versandnummer"));
		wtfVersandnummer = new WrapperTextField();

		wlaVersandnummer2 = new WrapperLabel(textFromToken("ls.versandnummer2"));
		wtfVersandnummer2 = new WrapperTextField();
		
		HelperClient.setMinimumAndPreferredSize(wlaVersandnummer, HelperClient.getSizeFactoredDimension(100));

		wcbVersandnummernUeberschreiben = new WrapperCheckBox(
				textFromToken("ls.versandnummer.ueberschreiben"));
		wcbVersandnummernUeberschreiben.setSelected(false);
		HelperClient.setMinimumAndPreferredSize(
				wcbVersandnummernUeberschreiben, 
				HelperClient.getSizeFactoredDimension(250));
		wlaVersandnummernUeberschreiben = new WrapperLabel("");
		wlaVersandnummernUeberschreiben.setHorizontalAlignment(SwingConstants.LEFT);
		HelperClient.setMinimumAndPreferredSize(
				wlaVersandnummernUeberschreiben, 
				HelperClient.getSizeFactoredDimension(250));
		
		panelAuslieferdatum = new PanelAuslieferdatum(getInternalFrame()) ;
		
//		iZeile++;
//		jpaWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile, 1,
//				1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		iZeile++;
//		jpaWorkingOn.add(wlaVersandnummer2, new GridBagConstraints(0, iZeile,
//				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		jpaWorkingOn.add(wtfVersandnummer2, new GridBagConstraints(1, iZeile,
//				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(panelAuslieferdatum.getWlaAuslieferdatum(), new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		JPanel panelDatum = new JPanel(new MigLayout("insets 0", "[left,fill,90%][right,fill]")) ;
		panelDatum.add(panelAuslieferdatum.getWdfAuslieferdatum()) ;
		panelDatum.add(panelAuslieferdatum.getBtnAuslieferdatumJetzt()) ;

		jpaWorkingOn.add(panelDatum, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcbVersandnummernUeberschreiben, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaVersandnummernUeberschreiben, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	
		iZeile++;

		enableToolsPanelButtons(aWhichButtonIUse);
		getInternalFrame().addItemChangedListener(this);

		this.add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		super.eventActionSave(e, true);
		components2Dto();
		if (lieferscheinDto != null)
			DelegateFactory.getInstance().getLsDelegate()
					.updateLieferscheinOhneWeitereAktion(lieferscheinDto);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (lieferscheinDto.getStatusCNr().equals(
				LieferscheinFac.LSSTATUS_ANGELEGT)
				|| lieferscheinDto.getStatusCNr().equals(
						LieferscheinFac.LSSTATUS_OFFEN)
				|| lieferscheinDto.getStatusCNr().equals(
						LieferscheinFac.LSSTATUS_GELIEFERT)) {
			super.eventActionUpdate(aE, false); // Buttons schalten
			panelAuslieferdatum.enableForcedEingabe();
		} else {
			MessageFormat mf = new MessageFormat(LPMain.getTextRespectUISPr(
							"ls.warning.lskannnichtgeaendertwerden"));
			mf.setLocale(LPMain.getTheClient().getLocUi());
			Object pattern[] = { lieferscheinDto.getStatusCNr() };
			DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.warning"), mf.format(pattern));
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		setDefaults();
		dto2Components();
	}

	public void updateButtons() throws Throwable {
		super.updateButtons();
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
//		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	protected void dto2Components() throws Throwable {
		wnfGewicht.setDouble(lieferscheinDto.getFGewichtLieferung());
		panelAuslieferdatum.dto2Components();
		Integer pakete = lieferscheinDto.getIAnzahlPakete();
		if(pakete < 1) {
			pakete = 1;
		}
		wnfPakete.setInteger(pakete);
		
//		wtfVersandnummer.setText(lieferscheinDto.getCVersandnummer());
//		wtfVersandnummer2.setText(lieferscheinDto.getCVersandnummer2());
	}

	protected void components2Dto() throws Throwable {
		if (wnfGewicht.getDouble() != null)
			lieferscheinDto.setFGewichtLieferung(wnfGewicht.getDouble());
		lieferscheinDto.setIAnzahlPakete(wnfPakete.getInteger());
		panelAuslieferdatum.components2Dto();

//		lieferscheinDto.setCVersandnummer(wtfVersandnummer.getText());
//		lieferscheinDto.setCVersandnummer2(wtfVersandnummer2.getText());
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}
	
	
	private Throwable resultException = null;
	private PaketVersandAntwortDto workerAntwortDto = null;
	
	private PaketVersandAntwortDto dialogPlcApiCall() throws ExecutionException, InterruptedException {
		JTextArea msgLabel;
		JProgressBar progressBar;
		final int MAXIMUM = 100;
		JPanel panel;

		progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		msgLabel = new JTextArea(textFromToken("ls.versandnummer.abrufen"));
		msgLabel.setEditable(false);

		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

		final JDialog dialog = new JDialog();
		dialog.getContentPane().add(panel);
		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
		msgLabel.setBackground(panel.getBackground());

		SwingWorker<PaketVersandAntwortDto, Object> worker = new SwingWorker<PaketVersandAntwortDto, Object>() {
//			@Override
//			protected void done() {
//				dialog.dispose();
//
//				if (resultException != null) {
//					handleException(resultException, false);
//				}
//			}

			@Override
			protected PaketVersandAntwortDto doInBackground() throws Exception {
				PaketVersandAntwortDto result = null;
				resultException = null;
				try {
					PaketVersandAntwortDto antwortDto = DelegateFactory.getInstance()
							.getLsDelegate().erzeugePaketInfo(lieferscheinDto.getIId());
					workerAntwortDto = antwortDto;
					publish(antwortDto);
					setProgress(100);
				} catch (Throwable t) {
					resultException = t;
				}

				return result;
			}

			@Override
			protected void process(List<Object> chunks) {
				for (Object result : chunks) {
					if (result instanceof PaketVersandAntwortDto) {
						workerAntwortDto = (PaketVersandAntwortDto) result;
					}
				}
			}
		};
		
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if("state".equals(evt.getPropertyName()) 
						&& SwingWorker.StateValue.DONE == evt.getNewValue()) {
					dialog.setVisible(false);
					dialog.dispose();				
				}
//				if ("progress".equals(evt.getPropertyName())) {
//					setProgressValue((Integer) evt.getNewValue());
//					fireProgressEvent(null);
//				}
			}
		});

		worker.execute();
		worker.get();
		dialog.setVisible(true);
		return workerAntwortDto;
	}
}