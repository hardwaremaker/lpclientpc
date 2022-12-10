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
 *******************************************************************************/
package com.lp.client.util;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class IconFactory {

	private static Map<String, ImageIcon> cachedIcons = new HashMap<String, ImageIcon>() ;

	public static void clear() {
		cachedIcons.clear();
	}

	public static ImageIcon getInternalNamedIcon(String resourcename) {
		ImageIcon icon = cachedIcons.get(resourcename) ;
		if(icon == null) {
			icon = new ImageIcon(IconFactory.class.getResource("/com/lp/client/res/" + resourcename));
			cachedIcons.put(resourcename, icon) ;
		}

		return icon ;
	}

	public static ImageIcon getMailForward() {
		return getInternalNamedIcon("mail_forward.png");
	}

	public static ImageIcon getEditorEdit() {
		return getInternalNamedIcon("notebook.png") ;
	}

	public static ImageIcon getCommentExist() {
		return getInternalNamedIcon("notebook_comment_exist_16x16.png");
	}

	public static ImageIcon getReset() {
		return getInternalNamedIcon("undo.png") ;
	}

	public static ImageIcon getDateChooser() {
		return getInternalNamedIcon("JDateChooserIcon.png") ;
	}

	/**
	 * Pfeil nach oben im Kalender (vorige Woche)
	 * @return Image f&uuml;r Pfeil nach oben
	 */
	public static ImageIcon getUpArrow() {
		return getInternalNamedIcon("navigate_close.png") ;
	}

	/**
	 * Pfeil nach unten im Kalender (n&auml;chste Woche)
	 * @return Pfeil nach unten im Kalender (n&auml;chste Woche)
	 */
	public static ImageIcon getDownArrow() {
		return getInternalNamedIcon("navigate_open.png") ;
	}

	public static ImageIcon getEraserClearFormat() {
		return getInternalNamedIcon("clear_format_16x16.png") ;
	}

	public static ImageIcon getHeavyOperation() {
		return getInternalNamedIcon("heavy_operation.png");
	}
	
	public static ImageIcon getNew() {
		return getInternalNamedIcon("document.png");
	}
	
	public static ImageIcon getMobilephone() {
		return getInternalNamedIcon("mobilephone2.png");
	}

	public static ImageIcon getMail() {
		return getInternalNamedIcon("mail.png");
	}

	public static ImageIcon getURL() {
		return getInternalNamedIcon("earth_view.png");
	}

	public static ImageIcon getRefresh() {
		return getInternalNamedIcon("refresh.png");
	}
	
	public static ImageIcon getExit() {
		return getInternalNamedIcon("exit.png");
	}
	
	public static ImageIcon getServerOk() {
		return getInternalNamedIcon("server_ok.png");
	}
	
	public static ImageIcon getEdit() {
		return getInternalNamedIcon("edit.png");
	}
	
	public static ImageIcon getDelete() {
		return getInternalNamedIcon("delete2.png");
	}
	
	public static ImageIcon getPlus() {
		return getInternalNamedIcon("plus_sign.png");
	}

	public static ImageIcon getDocumentAdd() {
		return getInternalNamedIcon("document_add.png");
	}
	
	public static ImageIcon getClear() {
		return getInternalNamedIcon("leeren.png");
	}
	public static ImageIcon getLock() {
		return getInternalNamedIcon("lock_closed.png");
	}
	public static ImageIcon getLockOpen() {
		return getInternalNamedIcon("lock_open.png");
	}
	
	public static ImageIcon getCopy() {
		return getInternalNamedIcon("copy.png");
	}
	public static ImageIcon getPaste() {
		return getInternalNamedIcon("paste.png");
	}
	
	public static ImageIcon getImport() {
		return getInternalNamedIcon("import1.png");
	}
	
	public static ImageIcon getLocation(){
		return getInternalNamedIcon("earth_location.png");
	}
	
	public static ImageIcon getGeocoordinates() {
		return getInternalNamedIcon("earth_view.png");
	}

	public static ImageIcon getModuleMap() {
		return getInternalNamedIcon("dot-chart16x16.png");
	}
	
	public static ImageIcon getWebRequest() {
		return getInternalNamedIcon("earth_find.png");
	}
	
	public static ImageIcon getDocumentOk() {
		return getInternalNamedIcon("document_ok.png");
	}
	
	public static ImageIcon getStop() {
		return getInternalNamedIcon("status_gestoppt.png");
	}
	
	/**
	 * Das HELIUM V Icon
	 * 
	 * @return
	 */
	public static ImageIcon getHeliumv() {
//		return getInternalNamedIcon("heliumv.png");
		return getInternalNamedIcon("Helium5_Desktop_Icon_128x128.png");
	}
	
	public static ImageIcon getHeliumvDesktop() {
//		return getInternalNamedIcon("heliumvdesktop.png");
		return getInternalNamedIcon("helium5desktop.png");
	}
	
	public static ImageIcon getHeliumvAbout() {
		return getInternalNamedIcon("H5_Logo_240x120.png");
	}
}
