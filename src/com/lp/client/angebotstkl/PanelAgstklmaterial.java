
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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklmaterialDto;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
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
public class PanelAgstklmaterial extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneAngebotstkl tabbedPaneAgstkl = null;

	private AgstklmaterialDto agstklmaterialDto = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private static final String ACTION_SPECIAL_MATERIAL = "action_special_material";
	private WrapperButton wbuMaterial = null;
	private PanelQueryFLR panelQueryFLRMaterial = null;
	private WrapperTextField wtfMaterial = null;

	protected WrapperNumberField wnfDimension1 = new WrapperNumberField();
	protected WrapperNumberField wnfDimension2 = new WrapperNumberField();
	protected WrapperNumberField wnfDimension3 = new WrapperNumberField();

	protected BigDecimal bdGewichtPreisCache = null;

	private WrapperComboBox wcbMaterialtyp = new WrapperComboBox();

	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;

	private WrapperLabel wlaGewichtPreis = new WrapperLabel(
			"Gewicht [kg] /" + LPMain.getTheClient().getSMandantenwaehrung());
	protected WrapperNumberField wnfGewicht = new WrapperNumberField();
	protected WrapperNumberField wnfGewichtPreis = new WrapperNumberField();

	public PanelAgstklmaterial(InternalFrame internalFrame, String add2TitleI, Object key,
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

		wbuMaterial = new WrapperButton();
		wbuMaterial.setText(LPMain.getTextRespectUISPr("label.material") + "...");
		wbuMaterial.setActionCommand(ACTION_SPECIAL_MATERIAL);
		wbuMaterial.addActionListener(this);

		wtfMaterial = new WrapperTextField();
		wtfMaterial.setMandatoryField(true);
		wtfMaterial.setActivatable(false);
		wtfMaterial.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wlaBezeichnung = new WrapperLabel(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung = new WrapperTextField(80);

		Map m = new LinkedHashMap();
		m.put(AngebotstklFac.MATERIALTYP_QUADER, AngebotstklFac.MATERIALTYP_QUADER.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.quader"));
		m.put(AngebotstklFac.MATERIALTYP_RUND, AngebotstklFac.MATERIALTYP_RUND.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.rund"));
		m.put(AngebotstklFac.MATERIALTYP_ROHR, AngebotstklFac.MATERIALTYP_ROHR.trim() + " "
				+ LPMain.getTextRespectUISPr("angb.schnellerfassung.materialtyp.rohr"));
		wcbMaterialtyp.setMap(m, false);

		wcbMaterialtyp.addActionListener(this);

		FocusAdapterMaterial2 fa = new FocusAdapterMaterial2(this);

		wnfDimension1.addFocusListener(fa);
		wnfDimension3.addFocusListener(fa);
		wnfDimension2.addFocusListener(fa);

		wnfGewicht.addFocusListener(fa);

		wnfGewichtPreis.addFocusListener(fa);

		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout3);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

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
		HvLayout layout = HvLayoutFactory.create(jpaWorkingOn, "wrap 9", "[15%|20%|15%|50px|50px|50px|15%|50px|50px]",
				"");

		layout.add(wbuMaterial, "growx").add(wtfMaterial, "growx").add(wcbMaterialtyp, "growx")
				.add(wnfDimension1, "growx").add(wnfDimension2, "growx").add(wnfDimension3, "growx")
				.add(wlaGewichtPreis, "growx").add(wnfGewicht, "growx").add(wnfGewichtPreis, "growx, wrap");

		layout.add(wlaBezeichnung, "growx").add(wtfBezeichnung, "growx, span");

	}

	private void dialogQueryMaterial(ActionEvent e) throws Throwable {
		panelQueryFLRMaterial = ArtikelFilterFactory.getInstance().createPanelFLRMaterial(getInternalFrame(),
				agstklmaterialDto.getMaterialIId(), true);

		new DialogQuery(panelQueryFLRMaterial);
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklmaterialDto != null) {
				if (agstklmaterialDto.getIId() == null) {
					agstklmaterialDto.setIId(DelegateFactory.getInstance().getAngebotstklpositionDelegate()
							.createAgstklmaterial(agstklmaterialDto));
				} else {
					DelegateFactory.getInstance().getAngebotstklpositionDelegate()
							.updateAgstklmaterial(agstklmaterialDto);
				}

				this.agstklmaterialDto = DelegateFactory.getInstance().getAngebotstklpositionDelegate()
						.agstklmaterialFindByPrimaryKey(agstklmaterialDto.getIId());
				setKeyWhenDetailPanel(agstklmaterialDto.getIId());
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

		agstklmaterialDto.setAgstklIId(getTabbedPaneAgstkl().getInternalFrameAngebotstkl().getAgstklDto().getIId());

		agstklmaterialDto.setNDimension1(wnfDimension1.getBigDecimal());
		agstklmaterialDto.setNDimension2(wnfDimension2.getBigDecimal());
		agstklmaterialDto.setNDimension3(wnfDimension3.getBigDecimal());
		agstklmaterialDto.setNGewichtpreis(wnfGewichtPreis.getBigDecimal());
		agstklmaterialDto.setNGewicht(wnfGewicht.getBigDecimal());
		agstklmaterialDto.setCMaterialtyp((String) wcbMaterialtyp.getKeyOfSelectedItem());

		agstklmaterialDto.setCBez(wtfBezeichnung.getText());

	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */

	private void dto2Components() throws Throwable {

		MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
				.materialFindByPrimaryKey(agstklmaterialDto.getMaterialIId());
		String material = materialDto.getBezeichnung();
		if (materialDto.getNGewichtInKG() != null) {
			material += " (" + Helper.formatZahl(materialDto.getNGewichtInKG(), 2, LPMain.getTheClient().getLocUi())
					+ " kg/dm³)";
		}

		wtfMaterial.setText(material);

		wcbMaterialtyp.setKeyOfSelectedItem(agstklmaterialDto.getCMaterialtyp());

		wnfDimension1.setBigDecimal(agstklmaterialDto.getNDimension1());
		wnfDimension2.setBigDecimal(agstklmaterialDto.getNDimension2());
		wnfDimension3.setBigDecimal(agstklmaterialDto.getNDimension3());

		wnfGewicht.setBigDecimal(agstklmaterialDto.getNGewicht());

		wnfGewichtPreis.setBigDecimal(agstklmaterialDto.getNGewichtpreis());

		wtfBezeichnung.setText(agstklmaterialDto.getCBez());

	}

	public void eventActionNew(EventObject eventObject, boolean bChangeKeyLockMeI, boolean bNeedNoNewI)
			throws Throwable {
		super.eventActionNew(eventObject, bChangeKeyLockMeI, false);
		agstklmaterialDto = new AgstklmaterialDto();

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
		if (this.agstklmaterialDto != null) {
			if (agstklmaterialDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getAngebotstklpositionDelegate()
							.removeAgstklmaterial(agstklmaterialDto);
					this.agstklmaterialDto = null;
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
		if (e.getActionCommand().equals(ACTION_SPECIAL_MATERIAL)) {
			dialogQueryMaterial(e);
		}

		if (e.getSource().equals(wcbMaterialtyp)) {

			wnfDimension1.setEditable(false);
			wnfDimension2.setEditable(false);
			wnfDimension3.setEditable(false);

			if (wcbMaterialtyp.getKeyOfSelectedItem() != null) {
				if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_QUADER)
						|| wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_ROHR)) {
					wnfDimension1.setEditable(true);
					wnfDimension2.setEditable(true);
					wnfDimension3.setEditable(true);
				}
				if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
					wnfDimension1.setEditable(true);
					wnfDimension2.setEditable(true);
					wnfDimension3.setBigDecimal(null);
				}

			}

			berechneGewicht();

		}

	}

	protected void berechneGewichtPreis() throws Throwable {
		if (agstklmaterialDto.getMaterialIId() != null) {

			BigDecimal preisprokg = DelegateFactory.getInstance().getMaterialDelegate().getMaterialpreisInZielwaehrung(
					agstklmaterialDto.getMaterialIId(),
					getTabbedPaneAgstkl().getInternalFrameAngebotstkl().getAgstklDto().getTBelegdatum(),
					LPMain.getTheClient().getSMandantenwaehrung());

			if (preisprokg != null && wnfGewicht.getBigDecimal() != null) {

				BigDecimal preis = wnfGewicht.getBigDecimal().multiply(preisprokg);
				wnfGewichtPreis.setBigDecimal(preis);
				wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());

			}
		}
	}

	protected void berechneGewicht() throws Throwable {

		if (agstklmaterialDto.getMaterialIId() != null && wcbMaterialtyp.getKeyOfSelectedItem() != null) {

			wnfGewicht.setBigDecimal(null);
			wnfGewicht.setBackground(Color.RED);

			wnfGewichtPreis.setBigDecimal(null);
			wnfGewichtPreis.setBackground(Color.RED);

			MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
					.materialFindByPrimaryKey(agstklmaterialDto.getMaterialIId());

			if (materialDto.getNGewichtInKG() != null && wcbMaterialtyp.getKeyOfSelectedItem() != null) {

				BigDecimal gewicht = BigDecimal.ZERO;

				if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_QUADER)) {
					double b = 0;
					double h = 0;
					double t = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						b = wnfDimension1.getBigDecimal().doubleValue() / 100;
					}
					if (wnfDimension3.getBigDecimal() != null) {
						h = wnfDimension3.getBigDecimal().doubleValue() / 100;
					}
					if (wnfDimension2.getBigDecimal() != null) {
						t = wnfDimension2.getBigDecimal().doubleValue() / 100;
					}

					gewicht = new BigDecimal(b * h * t).multiply(materialDto.getNGewichtInKG());
				} else if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
					double radius = 0;
					double laenge = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						radius = wnfDimension1.getBigDecimal().doubleValue() / 2 / 100;
					}
					if (wnfDimension2.getBigDecimal() != null) {
						laenge = wnfDimension2.getBigDecimal().doubleValue() / 100;
					}

					gewicht = new BigDecimal(radius * radius * Math.PI * laenge).multiply(materialDto.getNGewichtInKG());
				} else if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_ROHR)) {
					double radiusAussen = 0;

					double radiusInnen = 0;

					double laenge = 0;
					if (wnfDimension1.getBigDecimal() != null) {
						radiusAussen = wnfDimension1.getBigDecimal().doubleValue() / 2 / 100;
					}

					if (wnfDimension2.getBigDecimal() != null) {
						radiusInnen = wnfDimension2.getBigDecimal().doubleValue() / 2 / 100;
					}
					if (wnfDimension3.getBigDecimal() != null) {
						laenge = wnfDimension3.getBigDecimal().doubleValue() / 100;
					}

					BigDecimal bdZylinderAussen = new BigDecimal(radiusAussen * radiusAussen * Math.PI * laenge);
					BigDecimal bdZylinderInnen = new BigDecimal(radiusInnen * radiusInnen * Math.PI * laenge);

					gewicht = bdZylinderAussen.subtract(bdZylinderInnen).multiply(materialDto.getNGewichtInKG());
				}

				wnfGewicht.setBigDecimal(gewicht);
				wnfGewicht.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			}

			berechneGewichtPreis();

		} else {
			wnfGewicht.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
			wnfGewichtPreis.setBackground(Defaults.getInstance().getTextfieldBackgroundColor());
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
			if (e.getSource() == panelQueryFLRMaterial) {

				Integer materialIId = (Integer) ((ISourceEvent) e.getSource()).getIdSelected();

				MaterialDto materialDto = DelegateFactory.getInstance().getMaterialDelegate()
						.materialFindByPrimaryKey(materialIId);
				String material = materialDto.getBezeichnung();
				if (materialDto.getNGewichtInKG() != null) {
					material += " ("
							+ Helper.formatZahl(materialDto.getNGewichtInKG(), 2, LPMain.getTheClient().getLocUi())
							+ " kg/dm³)";
				}

				wtfMaterial.setText(material);
				agstklmaterialDto.setMaterialIId(materialIId);

			}
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

				if ((key != null && key.equals(LPMain.getLockMeForNew()))) {
					wnfDimension1.setEditable(false);
					wnfDimension2.setEditable(false);
					wnfDimension3.setEditable(false);

					if (wcbMaterialtyp.getKeyOfSelectedItem() != null) {
						if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_QUADER)
								|| wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_ROHR)) {
							wnfDimension1.setEditable(true);
							wnfDimension2.setEditable(true);
							wnfDimension3.setEditable(true);
						}
						if (wcbMaterialtyp.getKeyOfSelectedItem().equals(AngebotstklFac.MATERIALTYP_RUND)) {
							wnfDimension1.setEditable(true);
							wnfDimension2.setEditable(true);
							wnfDimension3.setBigDecimal(null);
						}

					}
				}

			} else {
				// einen alten Eintrag laden.
				agstklmaterialDto = DelegateFactory.getInstance().getAngebotstklpositionDelegate()
						.agstklmaterialFindByPrimaryKey((Integer) key);
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

class FocusAdapterMaterial2 extends java.awt.event.FocusAdapter {
	private PanelAgstklmaterial adaptee;

	FocusAdapterMaterial2(PanelAgstklmaterial adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {

			if (e.getSource().equals(adaptee.wnfDimension1) || e.getSource().equals(adaptee.wnfDimension2)
					|| e.getSource().equals(adaptee.wnfDimension3)) {
				if (adaptee.wnfDimension1.getBigDecimal() != null && adaptee.wnfDimension2.getBigDecimal() != null) {
					adaptee.bdGewichtPreisCache = null;
					adaptee.berechneGewicht();
				}
			}

			if (adaptee.wnfDimension1.getBigDecimal() != null) {
				adaptee.berechneGewicht();
			}
			
			
			if (adaptee.wnfDimension1.getBigDecimal() != null) {
				adaptee.berechneGewicht();
			}
			
			if (e.getSource().equals(adaptee.wnfGewicht)) {
				adaptee.berechneGewichtPreis();
			}
			
		} catch (Throwable tDummy) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
