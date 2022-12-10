package com.lp.neweditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.PropertyParams;
import com.lp.client.pc.SystemProperties;
import com.lp.client.util.feature.FeatureContext;
import com.lp.client.util.feature.FeatureState;
import com.lp.client.util.feature.HvFeatures;
import com.lp.neweditor.common.InsertPosition;
import com.lp.neweditor.data.BlockPosition;
import com.lp.neweditor.ui.block.ImageBlockController;
import com.lp.neweditor.ui.block.TextBlockController;
import com.lp.neweditor.ui.editor.HvBlockEditor;
import com.lp.neweditor.ui.menu.BlockEditorData;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.util.HvOptional;
import com.lp.util.EditorBlockAlignment;
import com.lp.util.Helper;

/**
 * Testklasse fuer neuen Editor
 * 
 * @author Alexander Daum
 *
 */
public class EditorTestMain {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
			HvOptional<String> context = SystemProperties.featureContextParam();
			if(context.isPresent()) { // -Dhv.feature.contextparam=value
				FeatureState fs = new FeatureState(HvFeatures.ContextParam, true);
				FeatureContext.getManager().setFeatureState(fs);

				Defaults.getInstance()
					.setParameterContext(context);
				setupDefaultsFromProperties(context);		
			} else {
				if(HvFeatures.ContextParam.isActive()) { // feature.properties
					context = HvOptional
							.ofNullable(System.getenv("HV_CONTEXT"));
					Defaults.getInstance()
						.setParameterContext(context);
					setupDefaultsFromProperties(context);
				}			
			}

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			//Ignore
		}
		LPMain.getInstance().setUISprLocale(Helper.string2Locale(LocaleFac.LP_LOCALE_DE_AT));
		new EditorTestMain().createFrame();
	}

	private static void setupDefaultsFromProperties(HvOptional<String> context) {
		try {
			PropertyParams params = PropertyParams.load();
			params.apply(context.orElse(PropertyParams.defaultContext));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private JPanel panel;
	private HvBlockEditor editor;
	private boolean menuVisible = true; 
	
	private void createFrame() {
		BlockEditorData.getInstance().initDataFromServer();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		editor = new HvBlockEditor();
		editor.setMenuVisible(menuVisible);
		editor.setRechtschreibpruefungLocale(new Locale("de", "AT"));
		editor.aktiviereRechtschreibpruefung();
		
		TextBlockController block1 = new TextBlockController(editor);
		TextBlockController block2 = new TextBlockController(editor);
		block1.setText("Das ist ein Text");
		block2.setText("Das ist ein anderer Text");

		ImageBlockController image1 = new ImageBlockController(editor);
		try {
			URL res = this.getClass().getResource("/com/lp/client/res/heliumv_info.png");

//			image1.getModel().setImage(ImageIO.read(new File("C:/Users/Alex.HVS/Pictures/Gimp.png")), MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
			image1.getModel().setImage(ImageIO.read(res), MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
//			image1.setSize(new ImageSize(20, 20, SizeUnit.MM));
			image1.getModel().setAlignment(EditorBlockAlignment.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}

		editor.addBlock(block1, new BlockPosition(0, 0), InsertPosition.BEFORE_VERTICAL);
		editor.addBlock(image1, new BlockPosition(0, 1), InsertPosition.BEFORE_VERTICAL);
		editor.addBlock(block2, new BlockPosition(0, 2), InsertPosition.BEFORE_VERTICAL);
		panel.add(editor.getView().getUIComponent(), BorderLayout.CENTER);
		panel.add(createBtnPanel(), BorderLayout.SOUTH);

		frame.add(panel);
		frame.setSize(1280, 720);

		frame.setVisible(true);
	}

	private JPanel createBtnPanel() {
		JButton print = new JButton("Print");
		print.addActionListener(e -> debugPrintComponents(panel));
		JButton relayout = new JButton("Layout");
		relayout.addActionListener(e -> toggleMenu());
		JPanel btnPanel = new JPanel();
		btnPanel.add(print);
		btnPanel.add(relayout);
		return btnPanel;
	}

	private static void debugPrintComponents(Container container) {
		for (Component comp : container.getComponents()) {
			if (comp instanceof Container) {
				debugPrintComponents((Container) comp);
			}
			if (comp instanceof JEditorPane) {
				System.out.println("Found an EditorPane");
				System.out.println(comp.getPreferredSize());
				System.out.println("visible=" + comp.isVisible());
				((JEditorPane) comp).updateUI();
				;
			}
		}
	}
	
	private void toggleMenu() {
		menuVisible = !menuVisible;
		
		editor.setMenuVisible(menuVisible);
	}

}
