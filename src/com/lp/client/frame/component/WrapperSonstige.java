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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.filechooser.FileChooserConfigToken;
import com.lp.client.frame.filechooser.open.AnyFileOpener;
import com.lp.client.frame.filechooser.open.WrapperFile;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;

/**
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 *
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 *
 * <p>
 * Erstellung: Vorname Nachname; dd.mm.06
 * </p>
 *
 * <p>
 * @author $Author: christoph $
 * </p>
 *
 * @version not attributable Date $Date: 2009/08/19 08:11:21 $
 *
 * @todo scrollpane auf bild PJ 3416
 * @todo texte uebersetzen PJ 3416
 */
public class WrapperSonstige extends PanelBasis implements IControl {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final static String ACTION_SPECIAL_DATEI_OFFEN = "action_special_datei_offen";
	private final static String ACTION_SPECIAL_DATEI_SPEICHERN = "action_special_datei_speichern";

	protected WrapperNumberField wnfGroesse = new WrapperNumberField();
	private WrapperTextField wtfDatei = new WrapperTextField();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDatei = new WrapperButton();
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperLabel wlaKb = new WrapperLabel();
	private WrapperTextField fieldToDisplayFileName = null;
	private byte[] datei = null;
	private boolean isActivatable = true;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	public WrapperSonstige(InternalFrame internalFrame, String addTitel) throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperButton getDateiButton() {
		return wbuDatei;
	}

	public WrapperSonstige(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName)
			throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wbuDatei.setText(LPMain.getTextRespectUISPr("lp.datei"));

		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.speichernunter"));

		wlaKb.setText("kB");
		jpaWorkingOn.setLayout(gridBagLayout2);
		wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));

		wbuSpeichern.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wbuSpeichern.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));

		wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
		wnfGroesse.setActivatable(false);

		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI_OFFEN);
		wbuDatei.addActionListener(this);

		wbuSpeichern.setActionCommand(ACTION_SPECIAL_DATEI_SPEICHERN);
		wbuSpeichern.addActionListener(this);

		wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
		wtfDatei.setActivatable(false);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jpaWorkingOn.add(wbuDatei, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuSpeichern, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfDatei, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfGroesse, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKb, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (ACTION_SPECIAL_DATEI_OFFEN.equalsIgnoreCase(e.getActionCommand())) {
			actionSelectFile();
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI_SPEICHERN)) {
			boolean saveFile = true;
			if (getDatei() == null) {
				saveFile = JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, 
						LPMain.getMessageTextRespectUISPr(
								"lp.datei.dialog.keininhalt", getDateiname()),
						LPMain.getTextRespectUISPr("lp.hinweis"),
						JOptionPane.OK_CANCEL_OPTION);;
			}
			
			if (saveFile) {
				HvOptional<WrapperFile> wf = HelperClient.showSaveDialog(
						this, FileChooserConfigToken.ExportLastDirectory,
						new File(getDateiname()));
				if (wf.isPresent()) {
					try (FileOutputStream out = new FileOutputStream(wf.get().getFile())) {
						if (getDatei() != null) {
							out.write(getDatei());
						}
					}
				}
			}			
		}
	}

	/**
	 * @throws ExceptionLP
	 * @throws Throwable
	 * @throws IOException
	 */
	private void actionSelectFile() throws ExceptionLP, Throwable, IOException {
		HvOptional<WrapperFile> wf = new AnyFileOpener(
				getInternalFrame(), FileChooserConfigToken.ImportLast)
				.selectSingleUnchecked();
		if (!wf.isPresent()) return ;
		
		FileValidator validator = new FileValidator();
		double size = wf.get().getLengthInKB();
		if (!validator.validateFileSize(new BigDecimal(size))) {
			setDatei((byte[]) null);
			return;
		}
		
		File file = wf.get().getFile();
		datei = wf.get().getBytes();
		if (fieldToDisplayFileName != null) {
			fieldToDisplayFileName.setText(file.getName());

		}
		
		wtfDatei.setText(file.getName());
		wbuDatei.setToolTipText(file.getAbsolutePath());
		wnfGroesse.setDouble(size);		
	}

	public String getDateiname() {
		return wtfDatei.getText();
	}

	public byte[] getDatei() {
		return datei;
	}

	public void setDatei(byte[] datei) throws Throwable {
		this.datei = datei;
		if (datei != null) {
			double groesseInKB = ((double) datei.length) / ((double) 1024);
			wnfGroesse.setDouble(new Double(groesseInKB));
			wbuSpeichern.setEnabled(true);
		} else {
			wnfGroesse.setDouble(0D);
			wbuSpeichern.setEnabled(false);
		}
	}

	public void setDateiname(String dateiname) {
		wtfDatei.setText(dateiname);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

	@Override
	public void removeContent() throws Throwable {
		setDatei(null);
		setDateiname(null);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return wtfDatei.hasContent();
	}

	@Override
	public boolean isMandatoryField() {
		return wtfDatei.isMandatoryField();
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		wtfDatei.setMandatoryField(isMandatoryField);
	}

	@Override
	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if (!isActivatable) {
			setEnabled(false);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		wbuDatei.setEnabled(enabled);
		wbuSpeichern.setEnabled(!enabled && getDatei() != null);
	}
}
