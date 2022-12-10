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
package com.lp.client.projekt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.stueckliste.service.StuecklisteeigenschaftartDto;

@SuppressWarnings("static-access")
public class PanelBereich extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperCheckBox wcbMitBetreiber = new WrapperCheckBox();
	private WrapperCheckBox wcbMitArtikel = new WrapperCheckBox();
	private WrapperCheckBox wcbArtikelEindeutig = new WrapperCheckBox();
	private WrapperCheckBox wcbDurchgefuehrtInOffene = new WrapperCheckBox();
	private WrapperCheckBox wcbDetailtextIstPflichtfeld = new WrapperCheckBox();
	
	private WrapperCheckBox wcbArtikelPflichtfeld = new WrapperCheckBox();
	
	private BereichDto bereichDto = null;

	public PanelBereich(InternalFrame internalFrame, String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	private void setDefaults() {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		bereichDto = new BereichDto();
		leereAlleFelder(this);
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getSource().equals(wcbMitArtikel)) {
			if (wcbMitArtikel.isSelected()) {
				wcbArtikelEindeutig.setEnabled(true);
				wcbArtikelPflichtfeld.setEnabled(true);

			} else {
				wcbArtikelEindeutig.setEnabled(false);
				wcbArtikelPflichtfeld.setEnabled(false);
				wcbArtikelEindeutig.setSelected(false);
				wcbArtikelPflichtfeld.setSelected(false);

			}

		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI) throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		if (wcbMitArtikel.isSelected()) {
			wcbArtikelEindeutig.setEnabled(true);
			wcbArtikelPflichtfeld.setEnabled(true);

		} else {
			wcbArtikelEindeutig.setEnabled(false);
			wcbArtikelPflichtfeld.setEnabled(false);

		}

	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getProjektServiceDelegate().removeBereich(bereichDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		bereichDto.setCBez(wtfBezeichnung.getText());
		bereichDto.setMandantCNr(LPMain.getTheClient().getMandant());
		bereichDto.setBProjektMitBetreiber(wcbMitBetreiber.getShort());

		bereichDto.setBProjektMitArtikel(wcbMitArtikel.getShort());
		bereichDto.setBProjektArtikeleindeutig(wcbArtikelEindeutig.getShort());
		bereichDto.setBProjektArtikelPflichtfeld(wcbArtikelPflichtfeld.getShort());
		
		bereichDto.setBDurchgefuehrtVonInOffene(wcbDurchgefuehrtInOffene.getShort());
		
		bereichDto.setBDetailtextIstPflichtfeld(wcbDetailtextIstPflichtfeld.getShort());

	}

	protected void dto2Components() {
		wtfBezeichnung.setText(bereichDto.getCBez());
		wcbMitBetreiber.setShort(bereichDto.getBProjektMitBetreiber());

		wcbMitArtikel.setShort(bereichDto.getBProjektMitArtikel());
		wcbArtikelEindeutig.setShort(bereichDto.getBProjektArtikeleindeutig());
		wcbArtikelPflichtfeld.setShort(bereichDto.getBProjektArtikelPflichtfeld());
		
		wcbDurchgefuehrtInOffene.setShort(bereichDto.getBDurchgefuehrtVonInOffene());
		wcbDetailtextIstPflichtfeld.setShort(bereichDto.getBDetailtextIstPflichtfeld());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (bereichDto.getIId() == null) {
				bereichDto.setCBez(wtfBezeichnung.getText());
				bereichDto.setIId(DelegateFactory.getInstance().getProjektServiceDelegate().createBereich(bereichDto));
				setKeyWhenDetailPanel(bereichDto.getIId());
			} else {
				DelegateFactory.getInstance().getProjektServiceDelegate().updateBereich(bereichDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(bereichDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setText("");
		wtfBezeichnung.setMandatoryField(true);

		wcbMitBetreiber.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.mitbetreiber"));

		wcbMitArtikel.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.mitartikel"));

		wcbArtikelEindeutig.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.artikeleindeutig"));

		wcbArtikelPflichtfeld.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.artikelpflichtfeld"));
		
		wcbDetailtextIstPflichtfeld.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.detailtextistpflichtfeld"));
		
		wcbMitArtikel.addActionListener(this);
		
		
		wcbDurchgefuehrtInOffene.setText(LPMain.getInstance().getTextRespectUISPr("proj.bereich.durchgefuehrtinoffene"));
		

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, 0, 2, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbMitBetreiber, new GridBagConstraints(1, 1, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbDetailtextIstPflichtfeld, new GridBagConstraints(2, 1, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		jpaWorkingOn.add(wcbMitArtikel, new GridBagConstraints(1, 2, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbArtikelEindeutig, new GridBagConstraints(2, 2, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wcbArtikelPflichtfeld, new GridBagConstraints(2, 3, 1, 1, 0.3, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		
		jpaWorkingOn.add(wcbDurchgefuehrtInOffene, new GridBagConstraints(1, 3, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE, ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BEREICH;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI) throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				if (wcbMitArtikel.isSelected()) {
					wcbArtikelEindeutig.setEnabled(true);
					wcbArtikelPflichtfeld.setEnabled(true);

				} else {
					wcbArtikelEindeutig.setEnabled(false);
					wcbArtikelPflichtfeld.setEnabled(false);

				}
			}

		} else {
			bereichDto = DelegateFactory.getInstance().getProjektServiceDelegate()
					.bereichFindByPrimaryKey((Integer) key);

			dto2Components();

		}
	}

}
