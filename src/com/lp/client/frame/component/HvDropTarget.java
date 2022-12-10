package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.SystemProperties;
import com.lp.client.util.HvTempFileCreator;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.Helper;

import io.undertow.server.session.PathParameterSessionConfig;

public class HvDropTarget {
	private DropTarget dt;
	private DropHandler dh;
	List<DropListener> listeners;

	public HvDropTarget(Component comp) {
		dh = new DropHandler();
		dt = new DropTarget(comp, dh);
		dh.installFileNameWFlavorIfWindows(dt);
	}

	public void addDropListener(DropListener listener) {
		dh.addDropListener(listener);
	}

	public void removeDropListener(DropListener listener) {
		dh.removeDropListener(listener);
	}

	private static class DropHandler extends DropTargetAdapter {

		private static final String nativeFileNameW = "FileNameW";
		private static final DataFlavor fileNameWFlavor = new DataFlavor(InputStream.class, nativeFileNameW);
		private List<DropListener> dropListeners = new ArrayList<DropListener>();
		private final Logger logger;

		private final Map<String, String> mimeTypeToSuffix = new HashMap<>();

		public DropHandler() {
			this.logger = Logger.getLogger(getClass());
			initMimeTypeList();
		}

		private void initMimeTypeList() {
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG, ".png");
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG, ".jpg");
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF, ".gif");
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF, ".tiff");

			// Derzeit nur outlook benoetigt...
			// Wenn andere Office Dokumente erkannt werden sollen
			// Muss noch mehr erkannt werden
			mimeTypeToSuffix.put("application/x-tika-msoffice", ".msg");
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_MESSAGE_RFC822, ".eml");

			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF, ".pdf");

			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML, ".html");
			mimeTypeToSuffix.put(MediaFac.DATENFORMAT_MIMETYPE_TEXT_PLAIN, ".txt");
		}

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			Transferable transfer = dtde.getTransferable();
			int dropAction = dtde.getSourceActions();
			HvOptional<DataFlavor> dataFlavor = getDataFlavorForImport(transfer, dropAction);
			if (!dataFlavor.isPresent()) {
				dtde.rejectDrag();
				return;
			}
			dtde.acceptDrag(DnDConstants.ACTION_COPY);
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			Transferable transfer = dtde.getTransferable();
			int dropAction = dtde.getSourceActions();
			HvOptional<DataFlavor> dataFlavor = getDataFlavorForImport(transfer, dropAction);
			if (!dataFlavor.isPresent()) {
				dtde.rejectDrop();
				return;
			}

			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			boolean accepted = handleData(transfer, dataFlavor);
			dtde.dropComplete(accepted);
		}

		@SuppressWarnings("unchecked")
		private boolean handleData(Transferable transfer, HvOptional<DataFlavor> dataFlavor) {
			try {
				Object data = transfer.getTransferData(dataFlavor.get());
				if (dataFlavor.get().equals(DataFlavor.javaFileListFlavor)) {
					handleDropFileList((List<File>) data);
					return true;
				} else if (dataFlavor.get().equals(fileNameWFlavor)) {
					handleDropInputStreamFileNameW((InputStream) data);
					return true;
				}
			} catch (IOException e) {
				if (dataFlavor.get().equals(DataFlavor.javaFileListFlavor)) {
					// try with other flavor if supported
					if (transfer.isDataFlavorSupported(fileNameWFlavor) && SystemProperties.isWinOs()) {
						return handleData(transfer, HvOptional.of(fileNameWFlavor));
					}
				}
				logger.error("IOException bei DropEvent, DataFlavor=" + dataFlavor.get(), e);
			} catch (UnsupportedFlavorException e) {
				// Kann nicht auftreten, isDataFlavorSupported wird vorher aufgerufen
				e.printStackTrace();
			}
			return false;
		}

		private String getFileSuffixForMime(MediaType type) {
			return mimeTypeToSuffix.get(type.toString());
		}

		MediaType type;

		private void handleDropInputStreamFileNameW(InputStream data) {
			try {
				/*
				 * WICHTIG: Wegen eines Bugs zwischen der Windows API, Java Native Code fuer
				 * Drag&Drop und Outlook ist der Pointer pUnkForRelease im native STGMEDIUM
				 * nicht richtig initialisiert. Dadurch kann der Stream, der dieser Funktion
				 * uebergeben wird nicht geschlossen werden, wenn der Drop von Outlook kommt,
				 * sonst stuerzt das Programm wegen Speicherzugriffsfehler ab, der Fehler kann
				 * in Java nicht abgefangen werden. Siehe Details PJ21857
				 */
				Path firstTempFile = Files.createTempFile(null, null);
				Files.copy(data, firstTempFile, StandardCopyOption.REPLACE_EXISTING);
				try (InputStream is = new BufferedInputStream(Files.newInputStream(firstTempFile))) {
					type = MimeTypes.getDefaultMimeTypes().detect(is, new org.apache.tika.metadata.Metadata());
				}
				String suffix = getFileSuffixForMime(type);
				if (suffix == null) {
					// Unbekannter MimeType
					logger.fatal("Unbekannter MIME-Type: " + type.toString());
					return;
				}
				Path tempFile = Files.createTempFile("hv_drag_drop", suffix);
//				Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
				Files.move(firstTempFile, tempFile, StandardCopyOption.REPLACE_EXISTING);
				HvTempFileCreator.addFileToDelete(tempFile);
				fireFileDropEvent(Collections.singletonList(tempFile.toFile()));
			} catch (IOException e) {
				logger.error("Konnte temp Datei nicht erstellen", e);
			}
		}

		private void handleDropFileList(List<File> data) {
			// Dateien in Temp Ordner zuerst kopieren, werden eventuell automatisch nach
			// transfer geloescht. z.B. Thunderbird macht das so.
			String tmpdir = System.getProperty("java.io.tmpdir");
			Path tmppath = Paths.get(tmpdir);
			for(int i = 0; i < data.size(); i++) {
				File f = data.get(i);
				if(!f.exists())
					continue;
				if(f.toPath().startsWith(tmppath)) {
					try {
						Path temp = HvTempFileCreator.createTempFile(Helper.getName(f.getName()), Helper.getMime(f.getName()));
						Files.copy(f.toPath(), temp, StandardCopyOption.REPLACE_EXISTING);
						data.set(i, temp.toFile());
						f.delete();
					} catch (IOException e) {
					}
				}
			}

			fireFileDropEvent(data);
		}

		private void fireFileDropEvent(List<File> files) {
			// Kein handler darf Liste modifizieren, sonst passt es fuer die handler nachher
			// nicht mehr
			files = Collections.unmodifiableList(files);
			for (DropListener l : dropListeners) {
				l.filesDropped(files);
			}
		}

		private HvOptional<DataFlavor> getDataFlavorForImport(Transferable transfer, int dropAction) {
			boolean copySupported = (DnDConstants.ACTION_COPY & dropAction) == DnDConstants.ACTION_COPY;
			if (!copySupported) {
				return HvOptional.empty();
			}

			if (transfer.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				return HvOptional.of(DataFlavor.javaFileListFlavor);
			} else if (transfer.isDataFlavorSupported(fileNameWFlavor) && SystemProperties.isWinOs()) {
				return HvOptional.of(fileNameWFlavor);
			}
			return HvOptional.empty();
		}

		public void addDropListener(DropListener listener) {
			dropListeners.add(listener);
		}

		public void removeDropListener(DropListener listener) {
			dropListeners.remove(listener);
		}

		/**
		 * Installiert den DataFlavor fuer FileNameW in der FlavorMap des dropTargets,
		 * falls das Betriebssystem Windows ist. Falls die FlavorMap des DropTargets
		 * keine SystemFlavorMap ist wird die
		 * {@link SystemFlavorMap#getDefaultFlavorMap()} verwendet.
		 * 
		 * @param dt
		 */
		public void installFileNameWFlavorIfWindows(DropTarget dt) {
			if (SystemProperties.isWinOs()) {
				FlavorMap fm = dt.getFlavorMap();
				// Wenn keine systemFlavorMap, dann neu erzeugen
				if (!(fm instanceof SystemFlavorMap)) {
					fm = SystemFlavorMap.getDefaultFlavorMap();
				}
				// getDefaultFlavorMap muss nicht zwingend eine SystemFlavorMap sein
				if (fm instanceof SystemFlavorMap) {
					SystemFlavorMap sysFM = (SystemFlavorMap) fm;
					sysFM.addFlavorForUnencodedNative(nativeFileNameW, fileNameWFlavor);
					sysFM.addUnencodedNativeForFlavor(fileNameWFlavor, nativeFileNameW);
				}
			}
		}
	}
}
