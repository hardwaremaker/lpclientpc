package com.lp.client.pc;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.server.system.service.InstallerDto;

public class SelfUpdateInstaller implements ISelfUpdate {
	private final JProgressBar updateBar = new JProgressBar();

	@Override
	public void update() {
		// Wenn ja, Version pruefen:
		// Runable Thread fuer
		// Update
		Runnable updateRun = new Runnable() {
			public void run() {
				File f = null;
				try {
					f = File.createTempFile("hvi", ".jar");

					java.io.FileOutputStream out = new java.io.FileOutputStream(
							f);
					InstallerDto installerDto = null;

					updateBar.setVisible(true);
					updateBar.setValue(0);
					updateBar.setStringPainted(true);
					for (int i = 0; i < 100; i++) {
						try {
							installerDto = DelegateFactory.getInstance()
									.getTheClientDelegate()
									.getInstallerPart(i);
						} catch (ExceptionLP e) {
						}
						out.write(installerDto.getOClientpc());
						updateBar.setValue(i);
					}
					out.close();

					boolean bSchreibrechteInWindows = false;
					if (SystemProperties.isWinOs()) {
						try {
							File fBet = new File(
									System.getProperty("user.dir")
											+ "\\lpclientpc.bat");
							bSchreibrechteInWindows = fBet.exists()
									&& fBet.canWrite();
						} catch (Exception e) {
							e.printStackTrace();
							bSchreibrechteInWindows = false;
						}

					}

					if (SystemProperties.isWinOs() && !bSchreibrechteInWindows) {

						URL url = getClass().getResource(
								"/com/lp/client/res/installer_template.txt");

						BufferedReader in = new BufferedReader(
								new InputStreamReader(url.openStream()));

						StringBuffer sb = new StringBuffer();

						byte[] CRLFAscii = { 13, 10 };

						String inputLine;
						while ((inputLine = in.readLine()) != null)
							sb.append(inputLine + new String(CRLFAscii));

						in.close();

						sb.append(
								"start javaw -jar \"" + f.getAbsolutePath()
										+ "\" -default.install.path=\""
										+ System.getProperty("user.dir")
										+ "\"" + new String(CRLFAscii));

						File fBat = File.createTempFile("start_installer",
								".bat");
						java.io.FileOutputStream outBat = new java.io.FileOutputStream(
								fBat);

						outBat.write(sb.toString().getBytes());
						outBat.close();

						ProcessBuilder pb = new ProcessBuilder("cmd.exe",
								"/C", fBat.getAbsolutePath());

						pb.start();
					} else {
						ProcessBuilder pb = new ProcessBuilder("java",
								"-jar", f.getAbsolutePath(),
								"-default.install.path="
										+ System.getProperty("user.dir"));
						String javahome = System.getProperty("java.home");

						pb.directory(new File(javahome + "/bin"));
						pb.start();
					}

					DelegateFactory.getInstance().getLogonDelegate()
							.logout(LPMain.getTheClient());
					System.exit(0);
				} catch (Throwable ex1) {
					ex1.printStackTrace();
				}
			}
		};

		Thread updatethread = new Thread(updateRun);
		updatethread.start();
		// Anzeigen des
		// Fortschrittbalkens
		updateBar.setValue(0);

		JOptionPane barPane = new JOptionPane();
		barPane.setMessageType(JOptionPane.PLAIN_MESSAGE);
		barPane.setOptions(new Object[0]);
		barPane.setMessage("Updating...");
		barPane.setIcon(null);
		barPane.add(updateBar);
		JDialog barDialog = barPane.createDialog(barPane, "Update");
		barDialog.setVisible(true);
	}
}
