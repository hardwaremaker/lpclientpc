package com.lp.client.util;

import java.awt.Color;
import java.util.ResourceBundle;

public class ClientConfiguration {
	private static ResourceBundle cfgBundle = null;
	private static final String CONFIG_BUNDLE_LP = "com.lp.client.res.lpconfig";

	private static ResourceBundle versionBundle = null ;
	private static final String VERSION_BUNDLE_LP = "com.lp.client.res.lp";

	private static ResourceBundle config() {
		if (cfgBundle == null) {
			cfgBundle = ResourceBundle.getBundle(CONFIG_BUNDLE_LP);
		}
		return cfgBundle ;
	}

	private static ResourceBundle version() {
		if (versionBundle == null) {
			versionBundle = ResourceBundle.getBundle(VERSION_BUNDLE_LP);
		}
		return versionBundle ;
	}

	private static Color toColor(String s) {
		int iColor = Integer.parseInt(s, 16);
		return new Color(iColor);
	}

	public static Color getHeliumLookNotEditableColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.noteditable." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	/**
	 * Hintergrundfarbe nicht editierbarer Felder
	 * @return die Hintergrundfarbe von nichteditierbaren Feldern
	 */
	public static Color getNotEditableColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.noteditable." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookEditableColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.editable." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	/**
	 * Hintergrundfarbe editierbarer Felder
	 *
	 * @return die konfigurierte Farbe falls auswertbar, ansonsten weiss
	 */
	public static Color getEditableColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.editable." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookColorOnFocus(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.onfocus." + colorVision));
		} catch (Throwable t) {
			return null;
		}
	}

	public static Color getColorOnFocus(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.onfocus." + colorVision));
		} catch (Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookInactiveForegroundColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.inactiveforeground." + colorVision));
		} catch (Throwable t) {
			return null;
		}
	}

	public static Color getInactiveForegroundColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.inactiveforeground." + colorVision));
		} catch (Throwable t) {
			return null;
		}
	}

	/**
	 * Die Breite des InternalFrame
	 * @return die Breite des InternalFrame bzw. Default wenn nicht vorhanden
	 */
	public static Integer getInternalFrameSizeWidth() {
		try {
			return Integer.parseInt(config().getString("internalframe.size.x")) ;
		} catch(Throwable t) {
			return 700 ;
		}
	}

	/**
	 * H&ouml;he des InternalFrame
	 *
	 * @return die H&ouml;he des InternalFrame bzw. Default wenn nicht gesetzt
	 */
	public static Integer getInternalFrameSizeHeight() {
		try {
			return Integer.parseInt(config().getString("internalframe.size.y")) ;
		} catch(Throwable t) {
			return 600 ;
		}
	}

	public static String getUiFontName() {
		return config().getString("ui.font.name") ;
	}

	public static String getUiFontStyle() {
		return config().getString("ui.font.style") ;
	}

	public static String getUiFontSize() {
		return config().getString("ui.font.size") ;
	}

	public static Integer getUiControlHeight() {
		return Integer.parseInt(config().getString("ui.controls.height")) ;
	}

	/**
	 * Report Zoom-Faktor
	 * @throws NullPointerException wenn der Wert nicht konfiguriert ist
	 * @throws NumberFormatException wenn der konfigurierte Wert nicht parseable ist
	 * @return den konfigurierten ZoomFaktor.
	 */
	public static int getReportZoom() {
		return Integer.parseInt(config().getString("report.zoom")) ;
	}

	/**
	 * Die Rahmenfarbe eines Mandatory Fields
	 *
	 * @param colorVision
	 * @return die entsprechende Rahmenfarbe sofern konfiguriert, ansonsten rot
	 */
	public static Color getMandatoryFieldBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.mandatoryfield.border." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(207, 0, 104) ;
		}
	}

	public static Color getHeliumLookMandatoryFieldBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.mandatoryfield.border." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(207, 0, 104) ;
		}
	}

	/**
	 * Die Rahmenfarbe eines "Druckeinstellungen gespeichert" Button-Borders
	 *
	 * @param colorVision
	 * @return die entsprechende Rahmenfarbe sofern konfiguriert, ansonsten gruen
	 */
	public static Color getPrinterPropertiesSavedBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.printerpropertiessaved.border." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(0, 200, 0) ;
		}
	}

	public static Color getHeliumLookPrinterPropertiesSavedBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.printerpropertiessaved.border." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(0, 200, 0);
		}
	}
	
	/**
	 * Die Textfarbe eines Default Fields
	 *
	 * @return Color
	 */
	public static Color getDefaultTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.text." + colorVision));
		} catch (Throwable t) {
			return new Color(0, 0, 0);
		}
	}

	public static Color getHeliumLookDefaultTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.text." + colorVision));
		} catch (Throwable t) {
			return new Color(0, 0, 0);
		}
	}

	/**
	 * Die Textfarbe eines Valid Fields
	 *
	 * @param colorVision
	 * @return Color
	 */
	public static Color getValidTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.text.valid." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookValidTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.text.valid." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	/**
	 * Die Textfarbe eines Invalid Fields
	 *
	 * @param colorVision
	 * @return Color
	 */
	public static Color getInvalidTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.text.invalid." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(255, 0, 0) ;
		}
	}

	public static Color getHeliumLookInvalidTextColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.text.invalid." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(255, 0, 0) ;
		}
	}

	public static String getLogpScan() {
		return config().getString("doc.logpscantool") ;
	}

	public static String getQuickLoginUserName() {
		return config().getString("benutzer") ;
	}

	public static String getQuickLoginPassword() {
		return config().getString("kennwort") ;
	}


	public static String getUiSprLocale() {
		return config().getString("locale.uisprache") ;
	}

	/**
	 * Der Benutzername des Admin-Benutzers
	 * @return der Name (Account-Name) des Admin-Users
	 */
	public static String getAdminUsername() {
		return config().getString("user.admin") ;
	}

	/**
	 * Die Client Buildnummer
	 * @return die Buildnummer
	 */
	public static Integer getBuildNumber() {
		return Integer.parseInt(version().getString("lp.version.client.build")) ;
	}

	/**
	 * Die Client Versionsnummer
	 * @return die Versionsnummer
	 */
	public static String getVersion() {
		return version().getString("lp.version.client") ;
	}

	/**
	 * Die Client Modul Nummer, also 11, oder 12
	 * @return die Client Modulnummer
	 */
	public static String getModul() {
		return version().getString("lp.version.client.modul") ;
	}

	public static String getBugfixNr() {
		return version().getString("lp.version.client.bugfix") ;
	}
	
	public static Color getHeliumLookPanelBackgroundColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.panel.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getPanelBackgroundColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.panel.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getHeliumLookTextfieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getTextfieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
//	public static Color getTextfieldInactiveBackground(String colorVision) {
//		try {
//			return toColor(config().getString("color.text.invalid." + colorVision)) ;
//		} catch(Throwable t) {
//			return new Color(255, 0, 0) ;
//		}
//	}
	
	public static Color getHeliumLookCheckboxBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.checkbox.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getCheckboxBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.checkbox.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getHeliumLookLabelBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.label.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getLabelBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.label.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
//	public static Color getComboboxBackground(String colorVision) {
//		try {
//			return toColor(config().getString("color.text.invalid." + colorVision)) ;
//		} catch(Throwable t) {
//			return new Color(255, 0, 0) ;
//		}
//	}

	
	public static Color getHeliumLookOptionPaneBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.optionpane.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getOptionPaneBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.optionpane.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getHeliumLookRadioButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.radiobutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getRadioButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.radiobutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getHeliumLookTableHeaderBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.tableheader.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getTableHeaderBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.tableheader.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getHeliumLookViewportBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.viewport.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getViewportBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.viewport.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getDependenceFieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.dependencefield.background." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(220, 220, 255); // ein nicht so helles blau
		}
	}
	
	public static Color getDependenceFieldDisabledBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.dependencefield.background.disabled." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(230, 230, 255); // ein helles blau
		}
	}

	public static Color getSelectedFieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.selectedfield.background." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(180, 195, 255); // helles blau
		}
	}
	
	public static Color getHeliumLookDependenceFieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.dependencefield.background." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(220, 220, 255); // ein nicht so helles blau
		}
	}
	
	public static Color getHeliumLookDependenceFieldDisabledBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.dependencefield.background.disabled." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(230, 230, 255); // ein helles blau
		}
	}

	public static Color getHeliumLookSelectedFieldBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.selectedfield.background." + colorVision)) ;
		} catch(Throwable t) {
			return new Color(180, 195, 255); // helles blau
		}
	}
	
	public static Color getHeliumLookToolBarBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.toolbar.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getToolBarBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.toolbar.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.button.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.button.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.button.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.button.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookStatusbarBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.statusbar.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getStatusbarBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.statusbar.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookDesktopStatusbarBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.desktopstatusbar.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getDesktopStatusbarBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.desktopstatusbar.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookLPModulToolbarButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.lpmodul.toolbarbutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getLPModulToolbarButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.lpmodul.toolbarbutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookLPModulToolbarButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.lpmodul.toolbarbutton.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getLPModulToolbarButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.lpmodul.toolbarbutton.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getPanelToolbarButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.panel.toolbarbutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookPanelToolbarButtonBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.panel.toolbarbutton.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getPanelToolbarButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.panel.toolbarbutton.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookPanelToolbarButtonBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.panel.toolbarbutton.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookScrollPaneBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.scrollpane.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getScrollPaneBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.scrollpane.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static boolean showHeliumLookTextfieldBorder() {
		try {
			return isTrue(config().getString("color.heliumlook.textfield.showborder"));
		} catch (Throwable t) {
			return true;
		}
	}
	
	public static boolean showTextfieldBorder() {
		try {
			return isTrue(config().getString("color.default.textfield.showborder"));
		} catch (Throwable t) {
			return true;
		}
	}
	
	private static boolean isTrue(String value) {
		if (value == null) return false;
		
		return "1".equals(value.trim()) || "true".equals(value.trim()) ? true : false;
	}

	public static Color getHeliumLookTableGridColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.table.gridcolor." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getTableGridColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.table.gridcolor." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookListBackground(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.list.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
	
	public static Color getListBackground(String colorVision) {
		try {
			return toColor(config().getString("color.default.list.background." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getHeliumLookTextfieldBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.heliumlook.textfield.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}

	public static Color getTextfieldBorderColor(String colorVision) {
		try {
			return toColor(config().getString("color.default.textfield.border." + colorVision)) ;
		} catch(Throwable t) {
			return null;
		}
	}
}
