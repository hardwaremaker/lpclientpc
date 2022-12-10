package com.lp.client.pc;

import java.awt.BorderLayout;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.common.primitives.Bytes;
import com.lp.client.util.IconFactory;

public class SelfUpdateBundle implements ISelfUpdate {
	private static final Logger log = Logger.getLogger(SelfUpdateBundle.class);
	private static final String appName = "heliumv-client";
	private static final int EXEC_MASK = 0111;

	private JProgressBar progressBar;
	private File receivedBundleFile;
	private File receivedSignatureFile;

	@Override
	public void update() {
		try {
			HttpClient client = new DefaultHttpClient();
			long length = getBundleLength(client);

			File f = getBundle(client, length);
			if (f != null && f.exists() && f.canRead()) {
				boolean consumed = install(f);
				if (!consumed) {
					log.warn("Der Download konnte nicht automatisch verarbeitet werden");
				}
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}

	private boolean install(File f) {
		boolean isMacOs = SystemProperties.isMacOs();
		return isMacOs ? installMacOs(f) : installOther(f);
	}

	private boolean installMacOs(File f) {
		boolean consumed = false;

		try (ZipFile zipFile = new ZipFile(f)) {
			String realAppName = findUniqueAppName();
			int index = realAppName.lastIndexOf("/");
			realAppName = realAppName.substring(index + 1) + "/";
			File destBase = new File(SystemProperties.homeDir(), "Downloads");

			Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();

				String targetName = entry.getName().replace("\\", "/").replace(appName + "/", realAppName);
				if (!entry.isDirectory()) {
					InputStream is = zipFile.getInputStream(entry);
					File newFile = new File(destBase, targetName);
					FileOutputStream os = new FileOutputStream(newFile);
					try {
						IOUtils.copy(is, os);
					} finally {
						os.close();
						is.close();
					}
					newFile.setLastModified(entry.getTime());
					int mode = entry.getUnixMode();
					if ((mode & EXEC_MASK) != 0) {
						if (!newFile.setExecutable(true)) {
						}
					}
				} else {
					File fDir = new File(destBase, targetName);
					fDir.mkdirs();
				}
			}

			ZipFile.closeQuietly(zipFile);
			consumed = true;

			if (java.awt.Desktop.isDesktopSupported()
					&& java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
				try {
					consumed = true;
					if (dialogInstall() == JOptionPane.YES_OPTION) {
						java.awt.Desktop.getDesktop().open(destBase);
						System.exit(1);
					}
				} catch (IOException e) {
					log.warn("install", e);
				}
			}
		} catch (IOException e) {
			log.error("Error in zipfile '" + f.getAbsolutePath() + "'.", e);
		}

		return consumed;
	}

	private boolean installMacOs0(File f) {
		// aus dem /tmp/*.zip ins ~/Download Verzeichnis entpacken
		// damit umgehen koennen, dass es im Download schon eine App gibt.
		boolean consumed = false;

		try {
			String realAppName = findUniqueAppName();
			ZipInputStream zis = new ZipInputStream(new FileInputStream(f));
			ZipEntry zipEntry = zis.getNextEntry();

			int index = realAppName.lastIndexOf("/");
			realAppName = realAppName.substring(index + 1) + "/";
			File destBase = new File(SystemProperties.homeDir(), "Downloads");

			byte[] buffer = new byte[1024 * 1024];
			while (zipEntry != null) {
				String targetName = zipEntry.getName().replace(appName + "/", realAppName);

				if (!zipEntry.isDirectory()) {
					File newFile = new File(destBase, targetName);
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					fos.close();
					newFile.setLastModified(zipEntry.getTime());
				} else {
					File fDir = new File(destBase, targetName);
					fDir.mkdirs();
				}

				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();

			consumed = true;

			if (java.awt.Desktop.isDesktopSupported()
					&& java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
				try {
					consumed = true;
					if (dialogInstall() == JOptionPane.YES_OPTION) {
						java.awt.Desktop.getDesktop().open(destBase);
						System.exit(1);
					}
				} catch (IOException e) {
					log.warn("install", e);
				}
			}

		} catch (FileNotFoundException e) {
			log.error("Couldn't find '" + f.getAbsolutePath() + "'.", e);
		} catch (IOException e) {
			log.error("Error in zipfile '" + f.getAbsolutePath() + "'.", e);
		}
		return consumed;
	}

	private String findUniqueAppName() throws IOException {
		String baseName = appName;

		String home = SystemProperties.homeDir() + "/Downloads";
		int count = -1;
		File f = null;
		do {
			++count;
			f = new File(home, baseName + (count == 0 ? "" : ("-" + count)) + ".app");
		} while (f.exists());

		String s = f.getCanonicalPath();
		return s.substring(s.lastIndexOf('/') + 1);
	}

	private boolean installOther(File f) {
		boolean consumed = false;
		if (java.awt.Desktop.isDesktopSupported()
				&& java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
			try {
				consumed = true;
				if (dialogInstall() == JOptionPane.YES_OPTION) {
					java.awt.Desktop.getDesktop().open(f);
					System.exit(1);
				}
			} catch (IOException e) {
				log.warn("install", e);
			}
		}
		return consumed;
	}

	private int dialogInstall() {
		Object[] options = { LPMain.getTextRespectUISPr("bundle.install.button") };
		return JOptionPane.showOptionDialog(new JFrame(), LPMain.getTextRespectUISPr("bundle.install.message"),
				LPMain.getTextRespectUISPr("bundle.install.title"), JOptionPane.YES_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, // don't use a custom Icon
				options, // the titles of buttons
				options[0]); // default button title
	}

	private long getBundleLength(HttpClient httpClient) throws ClientProtocolException, IOException {
		HttpHead request = new HttpHead(getBundleUrl());
		HttpResponse response = httpClient.execute(request);
		long length = -1l;
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			length = getContentLength(response);
			log.info("Bundle length: " + length + " bytes.");
		} else {
			log.warn("HTTP Status for Bundle length: " + statusCode);
		}

		return length;
	}

	private String getBundleFilenameSuffix() {
		String s = ".deb";
		if (SystemProperties.isWinOs()) {
			s = ".exe";
		} else if (SystemProperties.isMacOs()) {
			s = ".zip";
		}

		return s;
	}

	private String getBundleFilename() {
		return appName + getAdditionalBundleName() + getBundleFilenameSuffix();
	}

	private String getAdditionalBundleName() {
		// Fuer Java 11 client -j11
		String prop = System.getProperty("hv.clientbundle.suffix");
		return prop == null ? "" : prop;
	}

	private File getBundle(HttpClient httpClient, long length) throws ClientProtocolException, IOException {

		final JDialog dialog = new JDialog(new JFrame(), true);
//		dialog.setIconImage(ImageIO.read(
//				getClass().getResource("/com/lp/client/res/heliumv.png")));
		dialog.setIconImage(IconFactory.getHeliumv().getImage());
		prepare(dialog);

		BundleHttpReader worker = new BundleHttpReader(httpClient, length, dialog);
		worker.execute();
		dialog.setVisible(true);
		return receivedBundleFile;
	}

	private void prepare(JDialog dialog) {
		dialog.setTitle(LPMain.getTextRespectUISPr("bundle.download.title"));
		dialog.getContentPane().add(createPanel());

		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);
	}

	private JPanel createPanel() {
		progressBar = new JProgressBar(0, 100);
		progressBar.setIndeterminate(true);
		JTextArea msgLabel = new JTextArea(LPMain.getTextRespectUISPr("bundle.download.lable"));
		msgLabel.setEditable(false);

		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));
		msgLabel.setBackground(panel.getBackground());
		return panel;
	}

	private long getContentLength(HttpResponse response) {
		Header header = response.getFirstHeader("Content-Length");
		if (header != null) {
			try {
				return Long.parseLong(header.getValue());
			} catch (NumberFormatException e) {

			}
		}

		return -1l;
	}

	private String getBundleUrl() {
		String s = SystemProperties.providerUrl().get().replace("remote+", "");
		s = s + "/helium-install/resources/" + getBundleFilename();

		return s;
	}

	private String getBundleSignatureUrl() {
		String s = SystemProperties.providerUrl().get().replace("remote+", "");
		s = s + "/helium-install/resources/" + getBundleFilename() + ".MD5";

		return s;
	}

	private class BundleHttpReader extends SwingWorker<String, Integer> {

		private final HttpClient httpClient;
		private final long expectedLength;
		private long receivedLength = 0l;

		private final JDialog theirDialog;

		public BundleHttpReader(HttpClient httpClient, long expectedLength, JDialog dialog) {
			this.httpClient = httpClient;
			this.expectedLength = expectedLength;
			this.theirDialog = dialog;
		}

		@Override
		protected void done() {
			log.info("Bundle-Transfer done");
			theirDialog.dispose();
		}

		@Override
		protected String doInBackground() throws Exception {
			receivedSignatureFile = backgroundFileRequest(getBundleSignatureUrl(), ".md5");

			receivedBundleFile = backgroundFileRequest(getBundleUrl(), getBundleFilenameSuffix());

			if (receivedBundleFile != null && receivedSignatureFile != null) {
				try (FileInputStream fis = new FileInputStream(receivedBundleFile)) {
					byte[] sigMD5Sum = readMd5(receivedSignatureFile);
					if (!Arrays.equals(sigMD5Sum, DigestUtils.md5(fis))) {
						log.info("Different checksum in downloaded file");
					}

					receivedSignatureFile.delete();
				}
			} else {

			}

			publish(-1);
			setProgress(100);

			return null;
		}

		private byte[] readMd5(File f) throws FileNotFoundException, IOException {
			List<Byte> result = new ArrayList<Byte>();
			byte[] hex = new byte[2];
			int len = 0;
			try (FileInputStream fis = new FileInputStream(f)) {
				int n;
				while (len < 16 && (n = fis.read(hex, 0, 2)) == 2) {
					if (hex[0] < '0')
						break;

					String s = new String(hex);
					try {
						result.add((byte) Integer.parseInt(s, 16));
					} catch (NumberFormatException e) {
						log.error("Numberformaterror in '" + s + "' (pos " + (len * 2) + ").", e);
					}
				}
			}

			return Bytes.toArray(result);
		}

		private File backgroundFileRequest(String requestUrl, String suffix) throws Exception {
			log.info("requesting '" + requestUrl + "' and suffix " + suffix + ".");

			receivedLength = 0;

			HttpGet request = new HttpGet(requestUrl);
			HttpResponse response = httpClient.execute(request);

			HttpEntity outerEntity = response.getEntity();
			if (outerEntity != null) {
				Header h = outerEntity.getContentType();
				InputStream is = outerEntity.getContent();

				File f = File.createTempFile("hvb", suffix);

				FileOutputStream os = new FileOutputStream(f);
				byte[] buffer = new byte[1024 * 1024];
				BufferedOutputStream bos = new BufferedOutputStream(os, 2 * buffer.length);

				int x = 0;
				while ((x = is.read(buffer, 0, buffer.length)) >= 0) {
					bos.write(buffer, 0, x);

					publish(x);
				}

				bos.close();
				is.close();

				return f;
			}

			return null;
		}

		@Override
		protected void process(List<Integer> chunks) {
			for (Integer chunk : chunks) {
				if (chunk >= 0) {
					receivedLength += chunk;
				}
			}

			log.info("received total: " + receivedLength);

			if (expectedLength >= 0l) {
				int progress = (int) (100l * receivedLength / expectedLength);
				progressBar.setIndeterminate(false);
				progressBar.setValue(progress);
			}
		}
	}
}
