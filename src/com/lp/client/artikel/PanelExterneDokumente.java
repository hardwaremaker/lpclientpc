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
package com.lp.client.artikel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.filechooser.ChooserOpenDialog;
import com.lp.client.frame.filechooser.FileChooserBuilder;
import com.lp.client.frame.filechooser.open.DirectoryFile;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.DokumentenlinkbelegDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.util.Helper;

public class PanelExterneDokumente extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;

	private WrapperButton[] wbuFileDialog = null;
	private WrapperTextField[] wtfPfad = null;
	private JButton[] wbuGoto = null;
	private Integer belegartIId = null;

	private DokumentenlinkbelegDto[] dokumentenlinkbelegDtos = null;

	private GridBagLayout gridBagLayoutWorkingPanel = null;

	public PanelExterneDokumente(InternalFrame internalFrame,
			String add2TitleI, Integer belegartIId) throws Throwable {
		super(internalFrame, add2TitleI, belegartIId);
		this.belegartIId = belegartIId;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	public void setBelegartIId(Integer belegartIId) {
		this.belegartIId = belegartIId;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		for (int i = 0; i < wbuFileDialog.length; i++) {
			if (e.getSource().equals(wbuFileDialog[i])) {
				onActionOpenFileDialog(i);
			} else if (e.getSource().equals(wbuGoto[i])) {
				if (wtfPfad[i].getText() != null) {

					if (Helper.short2boolean(dokumentenlinkbelegDtos[i]
							.getDokumentenlinkDto().getBUrl())) {
						try {
							int f = wtfPfad[i].getText().indexOf("://");
							URI uri = new URI((f < 0 ? "http://" : "")
									+ wtfPfad[i].getText().trim());
							java.awt.Desktop.getDesktop().browse(uri);
						} catch (URISyntaxException ex1) {
							DialogFactory.showModalDialog(LPMain
									.getTextRespectUISPr("lp.error"), LPMain
									.getTextRespectUISPr("lp.fehlerhafteurl"));
						} catch (IOException ex1) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									ex1.getMessage());
						}
					} else {
						try {

							if (Helper.short2boolean(dokumentenlinkbelegDtos[i]
									.getDokumentenlinkDto()
									.getBPfadAusArbeitsplatzparameter())) {

								ArbeitsplatzparameterDto pfadDto = DelegateFactory
										.getInstance()
										.getParameterDelegate()
										.holeArbeitsplatzparameter(
												ParameterFac.ARBEITSPLATZPARAMETER_PROGRAMMPFAD_FUER_DOKUMENTENLINK);

								if (pfadDto == null || pfadDto.getCWert() != null
										&& pfadDto.getCWert().trim().length() == 0) {
									DialogFactory
											.showModalDialog(
													LPMain.getTextRespectUISPr("lp.error"),
													LPMain.getTextRespectUISPr("lp.dokumentenlink.pfadausparameter.nichtdefiniert"));
								} else {
									Runtime runtime = Runtime.getRuntime();
									runtime.exec(pfadDto.getCWert() + " "
											+ wtfPfad[i].getText());
								}

							} else {
								java.io.File f = new File(wtfPfad[i].getText());
								java.awt.Desktop.getDesktop().open(f);
							}

						} catch (java.lang.IllegalArgumentException e1) {
							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									e1.getMessage());

						}
					}

				}

			}
		}

	}

	private void onActionOpenFileDialog(int row) {
		ChooserOpenDialog openDialog = FileChooserBuilder.createOpenDialog(this);
		setChooserDirectory(openDialog, row);
		DirectoryFile chosenDirectory = openDialog.addDirectoryFilter().openSingle();
		if (chosenDirectory.hasDirectory()) {
			wtfPfad[row].setText(chosenDirectory.getDirectory().getAbsolutePath());
		}
	}

	private void setChooserDirectory(ChooserOpenDialog openDialog, int row) {
		String basePath = wtfPfad[row].getText() != null 
				? wtfPfad[row].getText() 
				: (dokumentenlinkbelegDtos != null && dokumentenlinkbelegDtos.length > row
						? dokumentenlinkbelegDtos[row].getDokumentenlinkDto().getCBasispfad()
						: null);
		if (basePath == null) {
			return;
		}
		openDialog.directory(new File(basePath));
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		leereAlleFelder(this);
		
		dokumentenlinkbelegDtos = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getDokumentenlinkbelegs(getInternalFrame().getBelegartCNr(),
						belegartIId);
		
		super.eventYouAreSelected(false);
		dto2Components();

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(-9, 0, 9, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		dokumentenlinkbelegDtos = DelegateFactory
				.getInstance()
				.getMandantDelegate()
				.getDokumentenlinkbelegs(getInternalFrame().getBelegartCNr(),
						belegartIId);

		wbuFileDialog = new WrapperButton[dokumentenlinkbelegDtos.length];
		wtfPfad = new WrapperTextField[dokumentenlinkbelegDtos.length];
		wbuGoto = new JButton[dokumentenlinkbelegDtos.length];

		for (int i = 0; i < dokumentenlinkbelegDtos.length; i++) {
			iZeile++;

			wbuFileDialog[i] = new WrapperButton(dokumentenlinkbelegDtos[i]
					.getDokumentenlinkDto().getCMenuetext());

			wbuFileDialog[i].addActionListener(this);

			wtfPfad[i] = new WrapperTextField();
			wtfPfad[i].setColumnsMax(300);
			wtfPfad[i].setText(dokumentenlinkbelegDtos[i].getCPfad());

			wbuGoto[i] = new JButton("->");
			wbuGoto[i].addActionListener(this);

			if (Helper.short2boolean(dokumentenlinkbelegDtos[i]
					.getDokumentenlinkDto().getBUrl())) {
				jpaWorkingOn.add(new WrapperLabel(dokumentenlinkbelegDtos[i]
						.getDokumentenlinkDto().getCMenuetext()),
						new GridBagConstraints(0, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(0, 0,
										0, 0), 0, 0));
			} else {
				jpaWorkingOn.add(wbuFileDialog[i], new GridBagConstraints(0,
						iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 0));
			}

			jpaWorkingOn.add(wtfPfad[i],
					new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wbuGoto[i], new GridBagConstraints(2, iZeile, 1,
					1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 30,
					0));

		}

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD, ACTION_PREVIOUS, ACTION_NEXT, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected void setDefaults() {
	}

	protected void eventActionNext(boolean next) throws Throwable{
		super.eventActionNext(next);
	}
	
	protected void dto2Components() throws Throwable {
		for (int i = 0; i < dokumentenlinkbelegDtos.length; i++) {
			wtfPfad[i].setText(dokumentenlinkbelegDtos[i].getCPfad());
		}
	}

	protected void components2Dto() throws ExceptionLP {

		for (int i = 0; i < dokumentenlinkbelegDtos.length; i++) {
			dokumentenlinkbelegDtos[i].setCPfad(wtfPfad[i].getText());
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (super.allMandatoryFieldsSetDlg()) {
			components2Dto();

			DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.updateDokumentenlinkbeleg(
							getInternalFrame().getBelegartCNr(), belegartIId,
							dokumentenlinkbelegDtos);
		}
		super.eventActionSave(e, true);
	}
}
