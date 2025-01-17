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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.EventObject;

import javax.ejb.FinderException;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.jcr.ejb.DokumentgruppierungPK;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;

public class PanelDokumenteGruppe extends PanelBasis {

	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel panelButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
  	private DokumentgruppierungDto dokumentgruppierungDto = null;

  	private WrapperTextField wtfName= new WrapperTextField();
    private WrapperLabel wlaName = new WrapperLabel(LPMain.getTextRespectUISPr("lp.name"));

    public PanelDokumenteGruppe(InternalFrame internalFrameI, String addTitleI)
    throws Throwable {
    	super(internalFrameI, addTitleI);
    	jbInit();
    	initComponents();
    }

    public PanelDokumenteGruppe(InternalFrame internalFrameI, String addTitleI,
    		Object keyWhenDetailPanelI)
    throws Throwable {
    	super(internalFrameI, addTitleI, keyWhenDetailPanelI);
    	jbInit();
    	initComponents();
    }

    public PanelDokumenteGruppe() {
    	super();
    }

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfName;
	}

    private void jbInit()
    throws Throwable {
    	gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);
		panelButtonAction = getToolsPanel();
    	this.setActionMap(null);
    	getInternalFrame().addItemChangedListener(this);
    	this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			, GridBagConstraints.WEST, GridBagConstraints.NONE,
    			new Insets(0, 0, 0, 0), 0, 0));
		String[] aWhichButtonIUse = {  ACTION_SAVE,ACTION_DELETE,ACTION_DISCARD };

		enableToolsPanelButtons(aWhichButtonIUse);

		wlaName.setText(LPMain.getTextRespectUISPr("lp.name"));

		getInternalFrame().addItemChangedListener(this);

		wtfName.setMandatoryField(true);
		wtfName.setActivatable(true);
		wtfName.setColumnsMax(100);

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaName, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfName, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

    }

    public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
    throws Throwable {
    	super.eventYouAreSelected(false);
    	Object key = getKeyWhenDetailPanel();
//    	if (key == null) {
//    		if (dokumentgruppierungDto != null) {
//    			dokumentgruppierungDto.setCNr(null);
//    			dokumentgruppierungDto.setMandantCNr(null);
//    		}
    	if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
    	}
    	else {
//    		FLRDokumentegruppePK flDokumentegruppePK = (FLRDokumentegruppePK) key;
//    		DokumentgruppierungPK pk = new DokumentgruppierungPK(flDokumentegruppePK.getMandant_c_nr(),flDokumentegruppePK.getC_nr());
//    		dokumentgruppierungDto= DelegateFactory.getInstance().getJCRDocDelegate().dokumentgruppierungfindbyPrimaryKey(pk);

    		dokumentgruppierungDto = DelegateFactory.getInstance()
					.getJCRDocDelegate()
					.dokumentgruppierungfindbyPrimaryKey((DokumentgruppierungPK) key);
    	}
    	dto2Components();
    }

	protected void eventItemchanged(EventObject eI)
	throws Throwable {
	}

    protected String getLockMeWer()
    throws Exception {
    	return HelperClient.LOCKME_DOKUMENTGRUPPE;
    }

    protected void dto2Components()
    throws ExceptionLP, RemoteException, FinderException {
    	if(dokumentgruppierungDto!=null){
    		wtfName.setText(dokumentgruppierungDto.getCNr());
    	} else {
    		wtfName.setText("");
    	}
    }

    protected void components2Dto()
    throws Throwable {
    	dokumentgruppierungDto.setCNr(wtfName.getText());
    	dokumentgruppierungDto.setMandantCNr(LPMain.getTheClient().getMandant());
    }


    public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
    throws Throwable {
    	if (allMandatoryFieldsSetDlg()) {
    		components2Dto();
    		DelegateFactory.getInstance().getJCRDocDelegate().createDokumentgruppierung(dokumentgruppierungDto);
			setKeyWhenDetailPanel(dokumentgruppierungDto.getDokumentgruppierungPK());
    	super.eventActionSave(e, bNeedNoSaveI);
    	eventYouAreSelected(false);
    	}
    }

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		dokumentgruppierungDto = new DokumentgruppierungDto();
	}

	protected void eventActionDelete(ActionEvent e, boolean bAdministrateLockKeyI,
			boolean bNeedNoDeleteI)
	throws Throwable {
		DelegateFactory.getInstance().getJCRDocDelegate().removeDokumentgruppierung(dokumentgruppierungDto);
		this.setKeyWhenDetailPanel(null);
//		super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
		super.eventActionDelete(e, false, false);
	}
}
