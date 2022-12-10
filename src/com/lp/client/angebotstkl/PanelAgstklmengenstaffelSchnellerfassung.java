
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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelSchnellerfassungDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>
 * Panel zum Bearbeiten der Klassen eines Loses
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>24. 10. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelAgstklmengenstaffelSchnellerfassung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneAngebotstkl tabbedPaneAgstkl = null;

	private AgstklmengenstaffelSchnellerfassungDto agstklmengenstaffelDto = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaAufschlagAZ = new WrapperLabel();
	private WrapperNumberField wnfAufschlagAZ = new WrapperNumberField();

	private WrapperLabel wlaAufschlagMaterial = new WrapperLabel();
	private WrapperNumberField wnfAufschlagMaterial = new WrapperNumberField();

	private WrapperLabel wlaWertMaterial = new WrapperLabel();
	private WrapperNumberField wnfWertMaterial = new WrapperNumberField();

	private WrapperLabel wlaWertAZ = new WrapperLabel();
	private WrapperNumberField wnfWertAZ = new WrapperNumberField();

	private WrapperLabel wlaPreisEinheit = new WrapperLabel();
	private WrapperNumberField wnfPreisEinheit = new WrapperNumberField();

	public PanelAgstklmengenstaffelSchnellerfassung(InternalFrame internalFrame, String add2TitleI, Object key,
			TabbedPaneAngebotstkl tabbedPaneAgstkl) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneAgstkl = tabbedPaneAgstkl;
		jbInit();
		initComponents();
	}

	private TabbedPaneAngebotstkl getTabbedPaneAgstkl() {
		return tabbedPaneAgstkl;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout3);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		// controls
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));

		wlaAufschlagAZ.setText(
				LPMain.getInstance().getTextRespectUISPr("agstkl.mengenstaffel.schnellerfassung.aufschlag.az"));
		wlaAufschlagMaterial.setText(
				LPMain.getInstance().getTextRespectUISPr("agstkl.mengenstaffel.schnellerfassung.aufschlag.material"));

		wlaWertAZ.setText(LPMain.getInstance().getTextRespectUISPr("agstkl.mengenstaffel.schnellerfassung.wert.az"));
		wlaWertMaterial.setText(
				LPMain.getInstance().getTextRespectUISPr("agstkl.mengenstaffel.schnellerfassung.wert.material"));
		wlaPreisEinheit.setText(
				LPMain.getInstance().getTextRespectUISPr("agstkl.mengenstaffel.schnellerfassung.preiseinheit"));

		wnfMenge.setMandatoryField(true);
		wnfAufschlagAZ.setMandatoryField(true);
		wnfAufschlagMaterial.setMandatoryField(true);

		wnfWertAZ.setMandatoryField(true);
		wnfWertMaterial.setMandatoryField(true);
		wnfPreisEinheit.setMandatoryField(true);

		
		wnfWertAZ.setActivatable(false);
		wnfWertMaterial.setActivatable(false);
		wnfPreisEinheit.setActivatable(false);
		
		// Nachkommastellen
		wnfMenge.setFractionDigits(Defaults.getInstance().getIUINachkommastellenMenge());

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		createHvLayout();
//		createGridBagLayout(wlaProzent1, wlaProzent2);

	}

	private void createHvLayout() {
		WrapperLabel wlaProzent1 = new WrapperLabel("%");
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		WrapperLabel wlaProzent2 = new WrapperLabel("%");
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);
		HvLayout layout = HvLayoutFactory.create(jpaWorkingOn, "wrap 6", "[20%|20%|30px|25%|20%|30px]", "");

		layout.add(wlaMenge, "growx").add(wnfMenge, "growx, wrap");

		layout.add(wlaWertMaterial, "growx").add(wnfWertMaterial, "growx, wrap");

		layout.add(wlaAufschlagMaterial, "growx").add(wnfAufschlagMaterial, "growx, wrap");
		layout.add(wlaWertAZ, "growx").add(wnfWertAZ, "growx, wrap");

		layout.add(wlaAufschlagAZ, "growx").add(wnfAufschlagAZ, "growx, wrap");

		layout.add(wlaPreisEinheit, "growx").add(wnfPreisEinheit, "growx, wrap");

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklmengenstaffelDto != null) {
				if (agstklmengenstaffelDto.getIId() == null) {
					agstklmengenstaffelDto.setIId(DelegateFactory.getInstance().getAngebotstklDelegate()
							.createAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelDto));
				} else {
					DelegateFactory.getInstance().getAngebotstklDelegate()
							.updateAgstklmengenstaffelSchnellerfassung(agstklmengenstaffelDto);
				}

				this.agstklmengenstaffelDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.agstklmengenstaffelSchnellerfassungFindByPrimaryKey(agstklmengenstaffelDto.getIId());
				setKeyWhenDetailPanel(agstklmengenstaffelDto.getIId());
				super.eventActionSave(e, true);
				// jetz den anzeigen
				eventYouAreSelected(false);
			}
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		agstklmengenstaffelDto
				.setAgstklIId(getTabbedPaneAgstkl().getInternalFrameAngebotstkl().getAgstklDto().getIId());
		agstklmengenstaffelDto.setNMenge(wnfMenge.getBigDecimal());
		agstklmengenstaffelDto.setNAufschlagAz(wnfAufschlagAZ.getBigDecimal());
		agstklmengenstaffelDto.setNAufschlagMaterial(wnfAufschlagMaterial.getBigDecimal());

		agstklmengenstaffelDto.setNWertAz(wnfWertAZ.getBigDecimal());
		agstklmengenstaffelDto.setNWertMaterial(wnfWertMaterial.getBigDecimal());
		agstklmengenstaffelDto.setNPreisEinheit(wnfPreisEinheit.getBigDecimal());

	}

	private void dto2Components() throws Throwable {
		wnfMenge.setBigDecimal(agstklmengenstaffelDto.getNMenge());
		wnfAufschlagAZ.setBigDecimal(agstklmengenstaffelDto.getNAufschlagAz());
		wnfAufschlagMaterial.setBigDecimal(agstklmengenstaffelDto.getNAufschlagMaterial());

		wnfWertAZ.setBigDecimal(agstklmengenstaffelDto.getNWertAz());
		wnfWertMaterial.setBigDecimal(agstklmengenstaffelDto.getNWertMaterial());
		wnfPreisEinheit.setBigDecimal(agstklmengenstaffelDto.getNPreisEinheit());

	}

	public void eventActionNew(EventObject eventObject, boolean bChangeKeyLockMeI, boolean bNeedNoNewI)
			throws Throwable {
		super.eventActionNew(eventObject, bChangeKeyLockMeI, false);
		agstklmengenstaffelDto = new AgstklmengenstaffelSchnellerfassungDto();

		this.leereAlleFelder(this);
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e                     ActionEvent
	 * @param bAdministrateLockKeyI boolean
	 * @param bNeedNoDeleteI        boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.agstklmengenstaffelDto != null) {
			if (agstklmengenstaffelDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getAngebotstklDelegate()
							.removeAgstklmengenstaffel(agstklmengenstaffelDto.getIId());
					this.agstklmengenstaffelDto = null;
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_AGSTKL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

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

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {
		super.eventYouAreSelected(false);

		
		
		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();

				if(Helper.short2boolean(getTabbedPaneAgstkl().getInternalFrameAngebotstkl().getAgstklDto().getBVorlage())) {
					
					wnfWertAZ.setBigDecimal(BigDecimal.ZERO);
					wnfWertMaterial.setBigDecimal(BigDecimal.ZERO);
					wnfPreisEinheit.setBigDecimal(BigDecimal.ZERO);
				}
				
				
			} else {
				// einen alten Eintrag laden.
				agstklmengenstaffelDto = DelegateFactory.getInstance().getAngebotstklDelegate()
						.agstklmengenstaffelSchnellerfassungFindByPrimaryKey((Integer) key);
				dto2Components();
			}
		}

		tabbedPaneAgstkl.refreshTitle();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

}
