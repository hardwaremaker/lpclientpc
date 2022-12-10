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
package com.lp.client.frame.component.frameposition;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.client.angebot.AGSchnellerfassungPositionData;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.pc.LPMain;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;

/**
 * L&auml;dt die ClientPerspectiveSettings und k&uuml;mmert sich um
 * dessen lazy-loading und File/DB-Anbindung
 * @author robert
 *
 */
public class ClientPerspectiveLoaderAndSaver {

	private static final String DESKTOP = "Desktop";

	private ClientPerspectiveSettings cpSettings;
	private IClientPerspectiveIO clientPerspectiveIO;
	private HashMap<String, String> properties = new HashMap<String, String>();

	protected static final String COLOR_VISION = "COLOR_VISION";
	protected static final String DIREKTHILFE = "DIREKTHILFE";
	protected static final String LOOK_AND_FEEL = "LOOK_AND_FEEL";
	protected static final String LOSIMPORT_XLSPFAD = "LOSIMPORT_XLSPFAD";
	protected static final String HELIUMLOOK = "HELIUMLOOK";
	protected static final String BUCHUNGSJOURNAL_EXPORT = "BUCHUNGSJOURNAL_EXPORT";
	protected static final String FILECHOOSER_CONFIG = "FILECHOOSER_CONFIG";
	protected static final String DIVIDER_LOCATION = "DIVIDER_LOCATION";
	protected static final String REPORT_CLIENT_CONFIG = "REPORT_CLIENT_CONFIG";

	public ClientPerspectiveLoaderAndSaver(IClientPerspectiveIO framePositionIO) {
		this.clientPerspectiveIO = framePositionIO;
		cpSettings = new ClientPerspectiveSettings();
	}

	public boolean doSavedSettingsExist() {
		return clientPerspectiveIO.doSavedSettingsExist();
	}

	public void persistAllSettings() throws Exception {
		clientPerspectiveIO.persistFramePositionMap(cpSettings.getPositionMap());
		clientPerspectiveIO.persistStartupModule(getStartupModule());
	}

	public List<String> getStartupModule() throws Exception {
		if(cpSettings.getStartupModule() == null)
			loadStartupModule();
		return cpSettings.getStartupModule();
	}

	public void setStartupModule(List<String> startupModule) {
		cpSettings.setStartupModule(startupModule);
	}

	public void setDesktopPositionData(FramePositionData fpData) {
		setFramePositionData(DESKTOP, fpData);
	}

	public void setFramePositionData(String key, FramePositionData fpData) {
		cpSettings.getPositionMap().put(key, fpData);
	}

	public void saveFramePosition(String key) throws Exception {
		Map<String, FramePositionData> map = new HashMap<String, FramePositionData>();
		map.put(key, cpSettings.getPositionMap().get(key));
		clientPerspectiveIO.persistFramePositionMap(map);
	}
	public void persistAGSchnellerfassungPositionData(AGSchnellerfassungPositionData positionData) throws Exception {
		clientPerspectiveIO.persistAGSchnellerfassungPositionData(positionData);
	}
	public FramePositionData getDesktopPositionData() throws Exception {
		return getFramePositionData(DESKTOP);
	}

	public void applyFramePosition(InternalFrame iFrame) throws Exception {
		FramePositionData fpData = getFramePositionData(iFrame.getBelegartCNr());
		fpData = fitSizeInto(fpData, LPMain.getInstance().getDesktop().getDesktopPaneSize());
		if(fpData != null) fpData.applyTo(iFrame);
	}

	public void applyFramePosition(PanelQueryFLR panelQuery) throws Exception {
		FramePositionData fpData = getFramePositionData(Integer.toString(panelQuery.getIdUsecase()));
		fpData = fitSizeInto(fpData, Toolkit.getDefaultToolkit().getScreenSize());
		if(fpData != null) fpData.applyTo(panelQuery);
	}

	private FramePositionData fitSizeInto(FramePositionData fpData, Dimension maxSize) throws Exception {

		if(fpData==null) return null;

		Point loc = fpData.getLocation();
		Dimension size = fpData.getSize();
		Dimension defaultSize = HelperClient.getInternalFrameSize();

		if(defaultSize.width > maxSize.width)
			defaultSize.width = maxSize.width;
		if(defaultSize.height > maxSize.height)
			defaultSize.height = maxSize.height;

		if(loc.x > maxSize.width)
			loc.x = maxSize.width-size.width;
		if(loc.y > maxSize.height)
			loc.y = maxSize.height-size.height;

		if(loc.x < 0) {
			size.width = size.width+loc.x;
			loc.x = 0;
		}
		if(loc.y < 0) {
			size.height = size.height+loc.y;
			loc.y = 0;
		}

		if(loc.x + size.width > maxSize.width)
			size.width = maxSize.width - loc.x;
		if(loc.y + size.height> maxSize.height)
			size.height = maxSize.height - loc.y;

//		if(size.width < defaultSize.width) {
//			size.width = defaultSize.width;
//			loc.x = maxSize.width - size.width;
//		}
//		if(size.height < defaultSize.height) {
//			size.height = defaultSize.height;
//			loc.y = maxSize.height - size.height;
//		}
		fpData.setLocation(loc);
		fpData.setSize(size);
		return fpData;
	}

	public void updateFramePosition(InternalFrame iFrame) {
		setFramePositionData(iFrame.getBelegartCNr(),
				new FramePositionData(iFrame));
	}

	public void updateFramePosition(PanelQueryFLR panel) {
		setFramePositionData(Integer.toString(panel.getIdUsecase()).toString(),
				new FramePositionData(panel.getDialog()));
	}

	public FramePositionData getFramePositionData(String key) throws Exception {
		FramePositionData fpData = cpSettings.getPositionMap().get(key);
		if (fpData == null)
			fpData = clientPerspectiveIO.readFramePosition(key);
		cpSettings.getPositionMap().put(key, fpData);
		return fpData;
	}
	public AGSchnellerfassungPositionData readAGSchnellerfassungPositionData() throws Exception {
		return clientPerspectiveIO.readAGSchnellerfassungPositionData();
	}
	public void removeQueryColumnPositions(int usecaseId) throws Exception{
		clientPerspectiveIO.removeQueryColumnPositions(usecaseId);
	}
	
	public void removeQueryColumnWidth(int usecaseId) throws Exception{
		clientPerspectiveIO.removeQueryColumnWidth(usecaseId);
	}
	public void removeQueryDetailHeight(int usecaseId) throws Exception{
		clientPerspectiveIO.removeQueryDetailHeight(usecaseId);
	}
	
	
	public void removeQueryColumnSorting(int usecaseId) throws Exception{
		clientPerspectiveIO.removeQueryColumnSorting(usecaseId);
	}
	
	public List<Integer> loadQueryColumnWidths(int usecaseId) throws Exception{
		return clientPerspectiveIO.readQueryColumnWidth(usecaseId);
	}
	
	public List<Integer> loadQueryColumnPositions(int usecaseId) throws Exception{
		return clientPerspectiveIO.readQueryColumnPositions(usecaseId);
	}

	public void saveQueryColumnWidths(int usecaseId, List<Integer> widths) throws Exception{
		clientPerspectiveIO.persistQueryColumnWidth(usecaseId, widths);
	}
	
	public void saveQueryColumnPosition(int usecaseId, List<Integer> position) throws Exception{
		clientPerspectiveIO.persistQueryColumnPosition(usecaseId, position);
	}

	public List<SortierKriterium> loadQueryColumnSorting(int usecaseId) throws Exception{
		return clientPerspectiveIO.readQueryColumnSorting(usecaseId);
	}

	public void saveQueryColumnSorting(int usecaseId, List<SortierKriterium> widths) throws Exception{
		clientPerspectiveIO.persistQueryColumnSorting(usecaseId, widths);
	}

	private void loadStartupModule() throws Exception {
		List<String> startupModule = clientPerspectiveIO.readStartupModule();
		if(startupModule == null)
			startupModule = new ArrayList<String>();
		cpSettings.setStartupModule(startupModule);
	}

	public void deleteSettings() throws Exception {
		clientPerspectiveIO.resetAllLayout();
	}

	public void saveFont(Font font) throws Exception {
		clientPerspectiveIO.persistClientFont(font);
	}

	public Font readFont() throws Exception {
		return clientPerspectiveIO.readClientFont();
	}

	
	public void resetFont() throws Exception {
		clientPerspectiveIO.resetClientFont();
	}

	protected void savePropertiesMap() throws Exception {
		clientPerspectiveIO.persistPropertyMap(properties);
	}

	protected void readPropertiesMap() throws Exception {
		try {
			properties = clientPerspectiveIO.readPropertyMap();
		} catch(Exception e) {}
		if(properties == null) properties = new HashMap<String, String>();
	}

	protected void setProperty(String key, String value) throws Exception {
		readPropertiesMap();
		properties.put(key, value);
		savePropertiesMap();
	}

	protected String getProperty(String key) throws Exception {
		readPropertiesMap();
		return properties.get(key);
	}

	public void saveColorVision(String colorVision) throws Exception {
		setProperty(COLOR_VISION, colorVision);
	}

	public String readColorVision() throws Exception{
		return getProperty(COLOR_VISION);
	}
	public void saveLosXLSPath(String path) throws Exception {
		setProperty(LOSIMPORT_XLSPFAD, path);
	}

	public String readLosXLSPath() throws Exception{
		return getProperty(LOSIMPORT_XLSPFAD);
	}

	public void saveDirekthilfeVisible(boolean b) throws Exception {
		setProperty(DIREKTHILFE, Boolean.toString(b));
	}

	public boolean readDirekthilfeVisible() throws Exception {
		String b = getProperty(DIREKTHILFE);
		return b == null ? true : new Boolean(b);
	}

	public void saveLookAndFeel(String lookAndFeel) throws Exception {
		setProperty(LOOK_AND_FEEL, lookAndFeel);
	}

	public String readLookAndFeel() throws Exception{
		return getProperty(LOOK_AND_FEEL);
	}

	public void saveHeliumLookEnabled(boolean b) throws Exception {
		setProperty(HELIUMLOOK, Boolean.toString(b));
	}

	public boolean readHeliumLookEnabled() throws Exception {
		String b = getProperty(HELIUMLOOK);
		return b == null ? false : new Boolean(b);
	}

	public void saveBuchungsjournalExportProperties(String properties) throws Exception {
		setProperty(BUCHUNGSJOURNAL_EXPORT, properties);
	}
	
	public String readBuchungsjournalExportProperties() throws Exception {
		return getProperty(BUCHUNGSJOURNAL_EXPORT);
	}
	
	public void saveFileChooserConfig(String config) throws Exception {
		setProperty(FILECHOOSER_CONFIG, config);
	}
	
	public String readFileChooserConfig() throws Exception {
		return getProperty(FILECHOOSER_CONFIG);
	}
	
	public void saveDetailPanelHeight(int usecaseId, int detailHeight) throws Exception {
		clientPerspectiveIO.persistDetailHeight(usecaseId, detailHeight);
	}
	
	public Integer readDetailPanelHeight(int usecaseId) throws Exception {
		return clientPerspectiveIO.readDetailHeight(usecaseId);
	}
	
	public void saveReportClientConfig(String config) throws Exception {
		setProperty(REPORT_CLIENT_CONFIG, config);
	}
	
	public String readReportClientConfig() throws Exception {
		return getProperty(REPORT_CLIENT_CONFIG);
	}
}
