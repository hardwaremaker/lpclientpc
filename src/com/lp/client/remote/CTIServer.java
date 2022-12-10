package com.lp.client.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.lp.client.cockpit.InternalFrameCockpit;
import com.lp.client.cockpit.TabbedPaneCockpit;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.partner.service.HelperFuerCockpit;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

public class CTIServer {
	private final LpLogger log = (LpLogger) LpLogger.getInstance(CTIServer.class);
	private boolean shouldShutdown = false;
	private CTIAcceptThread acceptThread;

	public void start(TheClientDto theClientDto) throws ExceptionLP {
		Integer rmiPort = DelegateFactory.getInstance().getTheClientDelegate().getRmiPort();

		startServer(takePort(rmiPort));
	}

	public void stop(TheClientDto theClientDto) {
		this.shouldShutdown = true;
	}

	protected Integer takePort(Integer rmiPort) {
		return rmiPort + 3000;
	}

	private boolean shouldShutdown() {
		return this.shouldShutdown;
	}

	private void startServer(Integer port) {
		this.shouldShutdown = false;
		acceptThread = new CTIAcceptThread(port);
		acceptThread.start();
	}

	private enum CmdError {
		Ok(0, ""), ErrorException(10, "Unexpected exception"), ErrorThrowable(11, "Unexpected throwable"),
		ErrorProgram(12, "Programming error"),

		InvalidLeadIn(100, "Starting { expected"), InvalidLeadOut(101, "Closing } expected"),
		InvalidSerialnrLength(102, "Invalid serialnr length (1 <= length < 9)"),
		InvalidSerialnrFormat(103, "Invalid serialnr format (digits 0..9 allowed)"),
		MissingSeperator(104, "Closing ; expected"), InvalidOpcode(105, "Invalid/unknown opcode"),
		ModuleCockpitRequired(200, "Module Cockpit required"),
		GotoMissingFields(1000, "Goto requires 4 fields partnerId;ansprechpartnerId;kundeId;lieferantId"),
		GotoPartnerIdRequired(1001, "Goto partnerId is mandatory");

		private CmdError(int value) {
			this.value = value;
		}

		private CmdError(int value, String text) {
			this.value = value;
			this.text = text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		private int value;
		private String text;
	}

	private class CmdResponse {
		private Integer serialnr;
		private final CmdError error;

		public CmdResponse() {
			error = CmdError.Ok;
		}

		public CmdResponse(CmdError error) {
			this.error = error;
		}

		public CmdResponse(Integer serialnr, CmdError error) {
			this.serialnr = serialnr;
			this.error = error;
		}

		public CmdResponse(Integer serialnr, CmdError error, String errorText) {
			this.serialnr = serialnr;
			this.error = error;
			this.error.setText(errorText);
		}

		public boolean hasSerialnr() {
			return serialnr != null;
		}

		public Integer getSerialnr() {
			return serialnr;
		}

		public CmdError getError() {
			return error;
		}

		public String getErrorText() {
			return error.getText();
		}
	}

	private class CTIAcceptThread extends Thread {
		private Integer port;

		public CTIAcceptThread(Integer port) {
			this.port = port;
		}

		public void run() {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				while (!shouldShutdown()) {
					log.info("Accepting connection on port " + port + " ...");
					Socket socket = serverSocket.accept();
					new CTIServerThread(socket).start();
				}
			} catch (IOException e) {
				log.error("IOException AcceptThread", e);
			}
		}
	}

	private class CTIServerThread extends Thread {
		private Socket socket;

		public CTIServerThread(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));

				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);

				String cmd;
				while ((cmd = reader.readLine()) != null) {
					log.info("received (" + socket.getLocalPort() + "): " + cmd);

					CmdResponse response = parseCommand(cmd);
					writer.println("{" + (response.hasSerialnr() ? response.getSerialnr() : "") + ";"
							+ (response.getError().name()) + ";" + "\"" + (response.getErrorText()) + "\"" + ";}");
				}

				this.socket.close();
			} catch (IOException e) {
				log.error("IOException Thread, stopping", e);
			}
		}

		private CmdResponse parseCommand(String cmd) {
			if (!cmd.startsWith("{")) {
				log.error("command ignored, wrong leadin >" + cmd + "<");
				return new CmdResponse(CmdError.InvalidLeadIn);
			}
			if (!cmd.endsWith("}")) {
				log.error("command ignored, wrong leadout >" + cmd + "<");
				return new CmdResponse(CmdError.InvalidLeadOut);
			}

			int seperator = cmd.indexOf(';', 1);
			if (seperator < 2 || seperator > 10) {
				log.error("command ignored, no valid serialnr >" + cmd + "<");
				return new CmdResponse(CmdError.InvalidSerialnrLength);
			}

			int serialnr = 0;
			try {
				serialnr = Integer.parseInt(cmd.substring(1, seperator), 10);
			} catch (NumberFormatException e) {
				log.error("command ignored, invalid serialnr >" + cmd + "<");
				return new CmdResponse(CmdError.InvalidSerialnrFormat);
			}

			int lastSeperator = seperator + 1;
			seperator = cmd.indexOf(';', lastSeperator);
			if (seperator < lastSeperator) {
				log.error("command ignored, invalid operator >" + cmd + "<");
				return new CmdResponse(serialnr, CmdError.MissingSeperator);
			}

			String op = cmd.substring(lastSeperator, seperator);
			if (op.length() > 0) {
				return parseOp(serialnr, op, cmd.substring(seperator + 1));
			}

			return new CmdResponse(serialnr, CmdError.InvalidOpcode, "No Opcode given");
		}

		private CmdResponse parseOp(Integer serialnr, String op, String value) {
			log.info("received op " + op + " with <" + value + ">");
			if ("GOTO".equals(op)) {
				return parseGoto(serialnr, value);
			}

			return new CmdResponse(serialnr, CmdError.InvalidOpcode, "Unknown opcode '" + op + "'");
		}

		private CmdResponse parseGoto(Integer serialnr, String value) {
			if (!LPMain.getInstance().getDesktop().darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_COCKPIT)) {
				return new CmdResponse(serialnr, CmdError.ModuleCockpitRequired);
			}

			String[] parts = value.split(";");
			if (parts.length != 5) {
				log.error("command GOTO ignored, invalid length " + parts.length + " of fields for >" + value + "<");
				return new CmdResponse(serialnr, CmdError.GotoMissingFields);
			}

			int partnerId = parseInt(parts[0]);
			int ansprechpartnerId = parseInt(parts[1]);
			int kundeId = parseInt(parts[2]);
			int lieferantId = parseInt(parts[3]);
			if (partnerId < 0) {
				log.error("command GOTO ignored, no valid partnerId");
				return new CmdResponse(serialnr, CmdError.GotoPartnerIdRequired);
			}

			if (!"}".equals(parts[4])) {
				log.error("command GOTO ignored, to much fields");
				return new CmdResponse(serialnr, CmdError.GotoMissingFields);
			}

			try {
				PartnerDto partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
						.partnerFindByPrimaryKey(partnerId);
				if (kundeId > 0) {
					KundeDto kundeDto = DelegateFactory.getInstance().getKundeDelegate().kundeFindByPrimaryKey(kundeId);
					if (!kundeDto.getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
						kundeId = -1;
					}
				}
				if (lieferantId > 0) {
					LieferantDto lieferantDto = DelegateFactory.getInstance().getLieferantDelegate()
							.lieferantFindByPrimaryKey(lieferantId);
					if (!lieferantDto.getMandantCNr().equals(LPMain.getTheClient().getMandant())) {
						lieferantId = -1;
					}
				}
				log.info("could find partnerId, ...");

				excecuteGoto(new Integer(partnerId), ansprechpartnerId < 0 ? null : new Integer(ansprechpartnerId));
				return new CmdResponse(serialnr, CmdError.Ok);
			} catch (ExceptionLP e) {
				log.error("ExceptionLP", e);
				return new CmdResponse(serialnr, CmdError.ErrorException, e.getMessage());
			} catch (Throwable t) {
				log.error("Throwable", t);
				return new CmdResponse(serialnr, CmdError.ErrorThrowable, t.getMessage());
			}
		}

		private void excecuteGoto(Integer partnerId, Integer ansprechpartnerId)
				throws Throwable {
			final HelperFuerCockpit hc = new HelperFuerCockpit(partnerId, ansprechpartnerId);
			final FilterKriterium[] kriterien = new FilterKriterium[1];
			kriterien[0] = new FilterKriterium("partner_i_id", true, hc.getPartnerIId() + "",
					FilterKriterium.OPERATOR_EQUAL, false);

			final InternalFrameCockpit cockpit = (InternalFrameCockpit) LPMain.getInstance().getDesktop()
					.holeModul(LocaleFac.BELEGART_COCKPIT);
			runGoto(TabbedPaneCockpit.IDX_PANEL_QP, cockpit, hc, kriterien);
		}

		private void runGoto(final int idxPanel, final InternalFrameCockpit cockpit, final HelperFuerCockpit hc,
				final FilterKriterium[] kriterien) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						HelperFuerCockpit helper = hc;
						cockpit.geheZu(InternalFrameCockpit.IDX_PANE_COCKPIT,
								TabbedPaneCockpit.IDX_PANEL_3SP_ANSPRECHPARTNER, helper.getPartnerIId(),
								helper.getAnsprechpartnerIId(),
								PartnerFilterFactory.getInstance().createFKPartnerKey(helper.getPartnerIId()));
						
						
					} catch (Throwable t) {
						log.error("trowable bei gehezu:", t);
					}
				}
			});
		}

		private int parseInt(String value) {
			try {
				if (value.length() > 0) {
					return Integer.parseInt(value, 10);
				}
			} catch (NumberFormatException e) {
				log.error("Can't parse '" + value + "' as number.");
			}

			return -1;
		}
	}
}
