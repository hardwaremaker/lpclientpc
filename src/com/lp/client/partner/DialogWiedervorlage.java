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
package com.lp.client.partner;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.ComboBoxPopupMenuListener;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQuery;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.IconFactory;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class DialogWiedervorlage extends JFrame implements ActionListener,ItemChangedListener{

	private static final long serialVersionUID = 1L;

	private PanelQuery panelQueryWiedervorlage = null;

	private GridBagLayout gridBagLayout = new GridBagLayout();

	private InternalFrame internalFrame = null;

	private WrapperComboBox wcbPerson = new WrapperComboBox();

	public DialogWiedervorlage(InternalFrame internalFrame) throws Throwable {
		setTitle(LPMain.getTextRespectUISPr("lp.wiedervorlage"));

		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(IconFactory.getHeliumv().getImage());
		
//		try {
//			if(getClass().getResource("/com/lp/client/res/heliumv.png") != null)
//				this.setIconImage(ImageIO.read(getClass().getResource("/com/lp/client/res/heliumv.png")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		jbInit();
		pack();

		panelQueryWiedervorlage.updateButtons(new LockStateValue(
				PanelBasis.LOCK_IS_NOT_LOCKED));

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		FilterKriterium[] kriterien = new FilterKriterium[2];

		kriterien[0] = new FilterKriterium("t_erledigt", true, "",
				FilterKriterium.OPERATOR_IS + " "
						+ FilterKriterium.OPERATOR_NULL, false);

		kriterien[1] = new FilterKriterium("flrpersonal.i_id", true,
				LPMain.getTheClient().getIDPersonal() + "",
				FilterKriterium.OPERATOR_EQUAL, false);


		panelQueryWiedervorlage = new PanelQuery(null, kriterien,
				QueryParameters.UC_ID_WIEDERVORLAGE, null, internalFrame,
				LPMain.getTextRespectUISPr("lp.wiedervorlage"),
				true);

		wcbPerson.setMandatoryField(true);

		Map m = DelegateFactory.getInstance().getPartnerDelegate()
				.getAllPersonenWiedervorlage();

		wcbPerson.setMap(m);

		wcbPerson.setKeyOfSelectedItem(LPMain.getTheClient().getIDPersonal());

		wcbPerson.addActionListener(this);

		wcbPerson.setMinimumSize(new Dimension(150, -1));
		wcbPerson.setPreferredSize(new Dimension(150, -1));

		ComboBoxPopupMenuListener listener = new ComboBoxPopupMenuListener();
		wcbPerson.addPopupMenuListener(listener);

		panelQueryWiedervorlage.eventYouAreSelected(false);

		panelQueryWiedervorlage.getToolBar().getToolsPanelCenter().add(wcbPerson);

		this.getContentPane().setLayout(gridBagLayout);

		this.getContentPane().add(
				panelQueryWiedervorlage,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));

	}
	@Override
	final public void changed(EventObject e) {
		int u = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		FilterKriterium[] kriterien = null;

		if(((Integer)wcbPerson.getKeyOfSelectedItem())<0){
			kriterien = new FilterKriterium[1];

			kriterien[0] = new FilterKriterium("t_erledigt", true, "",
					FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NULL, false);

		} else {
			kriterien = new FilterKriterium[2];
			kriterien[0] = new FilterKriterium("t_erledigt", true, "",
					FilterKriterium.OPERATOR_IS + " "
							+ FilterKriterium.OPERATOR_NULL, false);

			kriterien[1] = new FilterKriterium("flrpersonal.i_id", true,
					wcbPerson.getKeyOfSelectedItem() + "",
					FilterKriterium.OPERATOR_EQUAL, false);
		}

		panelQueryWiedervorlage.setDefaultFilter(kriterien);

		try {
			panelQueryWiedervorlage.eventYouAreSelected(false);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
	}
}
