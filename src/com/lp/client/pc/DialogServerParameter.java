package com.lp.client.pc;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.Delegate;
import com.lp.client.util.IconFactory;
import com.lp.server.system.service.PingPacket;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.HvOptional;

import net.miginfocom.swing.MigLayout;

public class DialogServerParameter extends JDialog {
	private static final long serialVersionUID = 1239925559308658826L;

	// Absichtlich kein WrapperTextfield, weil dieses einen Arbeitsplatzparameter
	// holt. Und genau das wollen wir hier nicht, wir muessen ja davon ausgehen,
	// das wir keinen/falschen Server haben. Wir wollen keine Blockierung/Timeout
	private final JTextField wtfServerName = new JTextField();
	private final WrapperCheckBox wcbAdvanced = new WrapperCheckBox(LPMain.getTextRespectUISPr("dialog.serverparam.advanced"));
	private final WrapperLabel wlaType = new WrapperLabel(LPMain.getTextRespectUISPr("dialog.serverparam.type"));
	private final WrapperRadioButton wrbWildfly = new WrapperRadioButton(LPMain.getTextRespectUISPr("dialog.serverparam.type.wildfly"));
	private final WrapperRadioButton wrbJBoss = new WrapperRadioButton(LPMain.getTextRespectUISPr("dialog.serverparam.type.jboss"));
	private final WrapperButton wbuCheck = new WrapperButton(LPMain.getTextRespectUISPr("dialog.serverparam.check"));
	private final WrapperButton wbuStore = new WrapperButton(LPMain.getTextRespectUISPr("dialog.serverparam.store"));
	private final WrapperButton wbuCancel = new WrapperButton(LPMain.getTextRespectUISPr("dialog.serverparam.cancel"));
	
	private final IconPanel okIcon = new IconPanel(IconFactory.getDocumentOk());
	private final IconPanel stopIcon = new IconPanel(IconFactory.getStop());
//	private final IconPanel desktopIcon = new IconPanel(IconFactory.getHeliumvDesktop());
	
	private final Controller controller = new Controller();
	
	public DialogServerParameter(Frame owner) {
		super(owner, "Angabe des Servers", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
	}
	
	public HvOptional<String> serverUrl() {
		return controller.getServerName();
	}
	
	public HvOptional<Boolean> serverType() {
		return controller.isWildfly();
	}
	
	private void jbInit() {
		setIconImage(IconFactory.getHeliumv().getImage());
//		try {
//			setIconImage(ImageIO.read(getClass()
//					.getResource("/com/lp/client/res/heliumv.png")));
//		} catch (IOException e) {
//		}
		
		JPanel myPanel = new JPanel(new MigLayout(
				"width 480::1024, height 30:160:400, gap 10 10 10 10", 
				"[90!][190!][200!]",
				"[][][]20"));
		
		createServerNamePanel(myPanel);
		createServerTypePanel(myPanel);
		createConnectionCheckPanel(myPanel);
		createStorePanel(myPanel);
		
		controller.hideAdvanced();
		controller.bootstrap();
		
		add(myPanel);
		wtfServerName.requestFocusInWindow();
	}
	
	private void createServerNamePanel(JPanel onPanel) {
		WrapperLabel wlaName = new WrapperLabel(LPMain.getTextRespectUISPr("dialog.serverparam.name"));
		wtfServerName.setText("");
		wtfServerName.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
				controller.onServerNameChanged(wtfServerName.getText());
			}
		});

		wcbAdvanced.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.onAdvancedChanged(wcbAdvanced.isSelected());
			}
		});;
		
		onPanel.add(wlaName, "growx");	
		onPanel.add(wtfServerName, "growx");
		onPanel.add(wcbAdvanced, "growx, wrap");
	}
	
	private void createServerTypePanel(JPanel onPanel) {
		wrbWildfly.setSelected(true);
		
		ButtonGroup bgrp = new ButtonGroup();
		bgrp.add(wrbWildfly);
		bgrp.add(wrbJBoss);

		ActionListener l = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(wrbWildfly.equals(e.getSource())) {
					controller.onTypeChangedWildfly();
				} else if (wrbJBoss.equals(e.getSource())) {
					controller.onTypeChangedJBoss();
				}
			}
		};
		wrbWildfly.addActionListener(l);
		wrbJBoss.addActionListener(l);
		
		onPanel.add(wlaType, "growx, hidemode 3");
		onPanel.add(wrbWildfly, "split 2, hidemode 3");
		onPanel.add(wrbJBoss, "wrap, hidemode 3");
	}
	
	private void createConnectionCheckPanel(JPanel onPanel) {
		wbuCheck.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.checkConnection();
			}
		});
		
		onPanel.add(wbuCheck, "skip, growx");		
		onPanel.add(okIcon, "hidemode 3");
		onPanel.add(stopIcon, "hidemode 3");
		onPanel.add(new JPanel(), "wrap");
	}
	
	
	private void createStorePanel(JPanel onPanel) {
		wbuStore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.storeParams();
				setVisible(false);
			};
		});
		
		wbuCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);				
			}
		});
		
		onPanel.add(wbuStore, "skip, growx, split 2");
		onPanel.add(wbuCancel, "growx");
	}

	
	private class IconPanel extends JPanel {
		private static final long serialVersionUID = 3604660943241362307L;
		private final ImageIcon icon;
		
		public IconPanel(ImageIcon icon) {
			this.icon = icon;
			initPanel();
		}
		
		private void initPanel() {
			int w = icon.getIconWidth();
			int h = icon.getIconHeight();
			setPreferredSize(new Dimension(w, h));
			setMaximumSize(new Dimension(w, h));
		}
		
		@Override
		public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        icon.paintIcon(this, g, 0, 0);
	    }
	}
	
	private class Controller {
		private HvOptional<String> serverName = HvOptional.empty();
		private HvOptional<Boolean> wildflySelected = HvOptional.empty();
		
		public Controller() {
		}
		
		public HvOptional<String> getServerName() {
			return serverName;
		}
		
		public HvOptional<Boolean> isWildfly() {
			return wildflySelected;
		}
		
		public void onServerNameChanged(String newName) {
			updateCheckButton();
		}
		
		public void onAdvancedChanged(boolean selected) {
			showAdvanced(selected);
		}
		
		public void onTypeChangedWildfly() {
			hideStatusIcon();
		}
		
		public void onTypeChangedJBoss() {
			hideStatusIcon();
		}
		
		public void storeParams() {
			if(wbuCheck.isEnabled()) {
				serverName = HvOptional.of(buildServerName(
						wtfServerName.getText(), wrbWildfly.isSelected()));				
				wildflySelected = HvOptional.of(wrbWildfly.isSelected());
			}
		}
		
		public void showAdvanced(boolean visible) {
			wlaType.setVisible(visible);
			wrbJBoss.setVisible(visible);
			wrbWildfly.setVisible(visible);
		}
		
		public void hideAdvanced() {
			showAdvanced(false);
		}
		
		public void hideOkIcon() {
			okIcon.setVisible(false);
		}
		
		public void showOkIcon() {
			okIcon.setVisible(true);
		}
		
		public void hideStopIcon() {
			stopIcon.setVisible(false);
		}

		public void showStopIcon() {
			stopIcon.setVisible(true);
		}
		
		public void hideStatusIcon() {
			hideStopIcon();
			hideOkIcon();			
		}
		
		public void showStatusIcon(boolean ok) {			
			if(ok) {
				hideStopIcon();
				showOkIcon();
			} else {
				hideOkIcon();
				showStopIcon();
			}
		}
		
		public void bootstrap() {
			updateCheckButton();
			hideStatusIcon();
		}
		
		private void updateCheckButton() {
			String server = wtfServerName.getText();
			boolean enabled = server != null && server.trim().length() > 0;
			wbuCheck.setEnabled(enabled);
			wbuStore.setEnabled(enabled);
			
			hideStatusIcon();
		}
		
		public void checkConnection() {
			if(!wbuCheck.isEnabled()) return;
			
			wbuCheck.setEnabled(false);
			hideStatusIcon();
			showStatusIcon(checkConnectionImpl());
			wbuCheck.setEnabled(true);
			wtfServerName.requestFocusInWindow();
		}
		
		private boolean checkConnectionImpl() {
			try {
				String naming = wrbWildfly.isSelected() 
						? "org.wildfly.naming.client.WildFlyInitialContextFactory" 
						: "org.jnp.interfaces.NamingContextFactory";
				String server = buildServerName(wtfServerName.getText(), wrbWildfly.isSelected());
				
				PingDelegate pinger = new PingDelegate(naming, server);
				PingPacket pingPacket = pinger.ping(1);
				return true;
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
			return false;
		}
		
		private String buildServerName(String rawServer, boolean isWildfly) {
			String server = isWildfly ? buildWildflyServer(rawServer) : buildJBossServer(rawServer);
			return isWildfly ? "remote+" + server : server;
		}
		
		private String buildWildflyServer(String rawServer) {
			String server = rawServer.toLowerCase();
			boolean httpsServer = false;
			boolean httpServer = server.indexOf("http://") > -1;
			if(!httpServer) {
				httpsServer = server.indexOf("https://") > -1;
			}
			if(!httpServer && !httpsServer) {
				server = "http://" + rawServer;
				httpServer = true;
			}
			
			int portIndex = server.lastIndexOf(':');
			if(portIndex <= 6) { // http://
				if(httpServer) { // httpsServer keine Standardportangabe
					server = server + ":8080";
				}
			}
			
			return server;
		}
		
		private String buildJBossServer(String rawServer) {
			String server = rawServer.toLowerCase();
			boolean jnpServer = server.indexOf("jnp://") > -1;
			if(!jnpServer) {
				server = "jnp://" + rawServer;
				jnpServer = true;
			}
			
			int portIndex = server.lastIndexOf(':');
			if(portIndex <= 5) { // jnp://
				server = server + ":2099";
			}
			
			return server;			
		}
	}
	
	private class PingDelegate extends Delegate {
		private final SystemFac systemFac;
		private final Context context;
		
		public PingDelegate(String 	namingFactory, String providerUrl) throws NamingException {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(SystemProperties.factoryClass, namingFactory);
			env.put(SystemProperties.providerUrl, providerUrl);
			context = new InitialContext(env);
			
			systemFac = lookupFac(context, SystemFac.class);
		}
		
		public PingPacket ping(int sequenceNumber) throws ExceptionLP {
			PingPacket packet = new PingPacket();
			packet.setRequestNumber(sequenceNumber);
			packet.setPingTimeSender(System.currentTimeMillis());
			systemFac.ping(packet);
			packet.setPingTimeSenderStop(System.currentTimeMillis());
			return packet;
		}	
	}
}
