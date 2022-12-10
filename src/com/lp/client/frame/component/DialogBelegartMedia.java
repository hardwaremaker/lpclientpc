package com.lp.client.frame.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import org.apache.tika.mime.MediaType;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.BelegartmediaDto;
import com.lp.server.system.service.BelegartmediaFac;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

public class DialogBelegartMedia extends JDialog
		implements ActionListener, ItemChangedListener, FocusListener, PropertyChangeListener, ItemListener {

	private JPanel panel = new JPanel(new GridBagLayout());
	private InternalFrame intFrame = null;
	private PanelBasis panelBasis = null;

	private DefaultComboBoxModel model = null;

	private WrapperMediaControl wmc = null;

	private WrapperComboBox comboBox = null;
	private WrapperComboBox comboBoxAusrichtung = null;

	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperButton wbuAbbrechen = new WrapperButton();

	public boolean bEsSindVochUngespeicherteDatenVorhanden = true;

	private ArrayList<BelegartmediaDto> belegartmediaDtos = new ArrayList<BelegartmediaDto>();

	private int iUseCase = -1;
	private Integer iKey = null;

	private boolean bTausch = false;

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

	public Integer getIKeyFromPanelQuery() {
		if (panelBasis.getParent() instanceof JSplitPane) {
			JSplitPane splitPane = (JSplitPane) panelBasis.getParent();
			if (splitPane.getTopComponent() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) splitPane.getTopComponent();
				return (Integer) pq.getSelectedId();
			}
		}
		return null;
	}

	public DialogBelegartMedia(InternalFrame intFrame, PanelBasis panelBasis) throws Throwable {
		super(LPMain.getInstance().getDesktop(), "", true);

		this.intFrame = intFrame;
		this.panelBasis = panelBasis;
		this.setSize(1050, 400);

		setTitle("MEDIEN");

		// Vorhandene suchen

		if (panelBasis.getParent() instanceof JSplitPane) {
			JSplitPane splitPane = (JSplitPane) panelBasis.getParent();
			if (splitPane.getTopComponent() instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) splitPane.getTopComponent();
				iUseCase = pq.getIdUsecase();
				iKey = (Integer) pq.getSelectedId();
			}
		}

		model = new DefaultComboBoxModel(new String[] { "1" });

		comboBox = new WrapperComboBox(model);
		comboBox.setEditable(true);

		comboBox.addActionListener(new ActionListener() {
			private int selectedIndex = -1;

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = comboBox.getSelectedIndex();
				if (index >= 0) {
					selectedIndex = index;
				} else if ("comboBoxEdited".equals(e.getActionCommand())) {
					Object newValue = model.getSelectedItem();
					model.removeElementAt(selectedIndex);
					model.insertElementAt(newValue, selectedIndex);
				}
			}
		});
		comboBox.setSelectedIndex(0);

		comboBox.addActionListener(this);

		comboBox.addPropertyChangeListener(this);
		comboBox.addItemListener(this);

		// Toolbar

		JPanel panelToolbar = new JPanel(new GridBagLayout());

		// Button vorher

		JButton buttonPrevious = new JButton(
				new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_left.png")));
		buttonPrevious.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonPrevious.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonPrevious.setActionCommand(PanelBasis.ACTION_PREVIOUS);
		buttonPrevious.addActionListener(this);

		panelToolbar.add(buttonPrevious, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		panelToolbar.add(comboBox, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 200, 0));

		JButton buttonNext = new JButton(
				new ImageIcon(getClass().getResource("/com/lp/client/res/navigate_right.png")));
		buttonNext.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonNext.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonNext.setActionCommand(PanelBasis.ACTION_NEXT);
		buttonNext.addActionListener(this);

		panelToolbar.add(buttonNext, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		JButton buttonNew = new JButton(new ImageIcon(getClass().getResource("/com/lp/client/res/document.png")));
		buttonNew.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonNew.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonNew.setActionCommand(PanelBasis.ACTION_NEW);
		buttonNew.addActionListener(this);

		panelToolbar.add(buttonNew, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

		HvDropTarget dt = new HvDropTarget(buttonNew);
		dt.addDropListener(new FileImportDropHandler(this));

		JButton buttonDelete = new JButton(new ImageIcon(getClass().getResource("/com/lp/client/res/delete2.png")));
		buttonDelete.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonDelete.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonDelete.setActionCommand(PanelBasis.ACTION_DELETE);
		buttonDelete.addActionListener(this);

		panelToolbar.add(buttonDelete, new GridBagConstraints(4, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

		JButton buttonPLUS = new JButton(
				new ImageIcon(getClass().getResource("/com/lp/client/res/arrow_down_blue.png")));
		buttonPLUS.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonPLUS.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonPLUS.setActionCommand(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1);
		buttonPLUS.addActionListener(this);

		panelToolbar.add(buttonPLUS, new GridBagConstraints(5, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

		JButton buttonUP = new JButton(new ImageIcon(getClass().getResource("/com/lp/client/res/arrow_up_blue.png")));
		buttonUP.setMinimumSize(HelperClient.getToolsPanelButtonDimension());
		buttonUP.setPreferredSize(HelperClient.getToolsPanelButtonDimension());
		buttonUP.setActionCommand(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1);
		buttonUP.addActionListener(this);

		panelToolbar.add(buttonUP, new GridBagConstraints(6, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

		LinkedHashMap<Integer, String> lhmAusrichtung = new LinkedHashMap<Integer, String>();
		lhmAusrichtung.put(BelegartmediaFac.AUSRICHTUNG_LINKSBUENDIG,
				LPMain.getTextRespectUISPr("lp.ausrichtung.linksbuendig"));
		lhmAusrichtung.put(BelegartmediaFac.AUSRICHTUNG_RECHTSBUENDIG,
				LPMain.getTextRespectUISPr("lp.ausrichtung.rechtsbuendig"));
		lhmAusrichtung.put(BelegartmediaFac.AUSRICHTUNG_ZENTRIERT,
				LPMain.getTextRespectUISPr("lp.ausrichtung.zentriert"));

		comboBoxAusrichtung = new WrapperComboBox();
		comboBoxAusrichtung.setMandatoryField(true);

		comboBoxAusrichtung.setMap(lhmAusrichtung);

		panelToolbar.add(comboBoxAusrichtung, new GridBagConstraints(7, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 100, 0));

		// Und MediaControl

		wmc = new WrapperMediaControl(intFrame, "");
		wmc.getWbuBildEntfernen().setVisible(false);

		Map<String, String> tmArten = new TreeMap<String, String>();
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF, MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);

		wmc.getWcoArt().setMap(tmArten, false);

		panel.add(panelToolbar, new GridBagConstraints(0, 0, 1, 1, 1.0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(3, 0, 0, 0), 0, 0));

		panel.add(wmc, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		wbuSpeichern.addActionListener(this);
		wbuAbbrechen.addActionListener(this);
		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.report.save"));
		wbuAbbrechen.setText(LPMain.getTextRespectUISPr("Cancel"));

		JPanel panelSave = new JPanel(new GridBagLayout());

		panelSave.add(wbuSpeichern, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 200, 0));
		panelSave.add(wbuAbbrechen, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 200, 0));

		panel.add(panelSave, new GridBagConstraints(0, 2, 1, 1, 1.0, 0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 3, 0), 0, 0));

		getContentPane().add(panel);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		this.addEscapeListener(this);

		aktualisiereInhalt();

	}

	private void selektierteZeileAnzeigen() throws Throwable {

		int index = comboBox.getSelectedIndex();
		if (index >= 0) {
			BelegartmediaDto bmDto = belegartmediaDtos.get(index);
			if (bmDto.getDatenformatCNr() != null) {
				wmc.setMimeType(bmDto.getDatenformatCNr().trim());
			}

			wmc.setOMediaImage(bmDto.getOMedia());
			wmc.setOMediaText(bmDto.getXText());
			wmc.setDateiname(bmDto.getCDateiname());
		}

	}

	public void addZeile(String mimeType, String filename, byte[] oFile) throws Throwable {

		int i = model.getSize();

		if (comboBox.getSelectedIndex() >= 0 && wmc.getOMediaImage() == null && wmc.getOMediaText() == null) {
			BelegartmediaDto bmDto = belegartmediaDtos.get(comboBox.getSelectedIndex());

			bmDto.setDatenformatCNr(mimeType);
			bmDto.setOMedia(oFile);
			bmDto.setCDateiname(filename);
			bmDto.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());
			belegartmediaDtos.set(comboBox.getSelectedIndex(), bmDto);
			wmc.setOMediaImage(oFile);

		} else {
			comboBox.addItem((i + 1) + "");

			BelegartmediaDto bmDto = new BelegartmediaDto();

			bmDto.setDatenformatCNr(mimeType);
			bmDto.setOMedia(oFile);
			bmDto.setCDateiname(filename);
			bmDto.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());

			belegartmediaDtos.add(bmDto);
		}

	}

	public void aktualisiereInhalt() throws Throwable {

		Integer iKey = getIKeyFromPanelQuery();

		boolean bNeu = false;
		if (panelBasis.getKeyWhenDetailPanel() != null
				&& panelBasis.getKeyWhenDetailPanel().equals(LPMain.getLockMeForNew())) {
			bNeu = true;
			bEsSindVochUngespeicherteDatenVorhanden = true;
			iKey = null;
		}

		if (bNeu == false && !iKey.equals(this.iKey)) {

			belegartmediaDtos = DelegateFactory.getInstance().getBelegartmediaDelegate().getBelegartMediaDtos(iUseCase,
					iKey);
		}

		this.iKey = iKey;

		comboBox.removeAllItems();

		if (belegartmediaDtos.size() > 0) {

			for (int i = 0; i < belegartmediaDtos.size(); i++) {

				BelegartmediaDto belegartmediaDto = belegartmediaDtos.get(i);

				if (belegartmediaDto.getCBez() != null) {
					comboBox.addItem(belegartmediaDto.getCBez());
				} else {
					comboBox.addItem((i + 1) + "");
				}

			}

			comboBox.setSelectedIndex(0);
			if (belegartmediaDtos.get(0).getDatenformatCNr() != null) {
				wmc.setMimeType(belegartmediaDtos.get(0).getDatenformatCNr().trim());
			}
			wmc.setOMedia(belegartmediaDtos.get(0).getOMedia());
			wmc.setOMediaText(belegartmediaDtos.get(0).getXText());

			wmc.setDateiname(belegartmediaDtos.get(0).getCDateiname());

			comboBoxAusrichtung.setKeyOfSelectedItem(belegartmediaDtos.get(0).getIAusrichtung());

		} else {

			inhaltLeeren();
		}

	}

	public void inhaltLeeren() throws Throwable {
		belegartmediaDtos = new ArrayList<BelegartmediaDto>();
		belegartmediaDtos.add(new BelegartmediaDto());
		belegartmediaDtos.get(0).setDatenformatCNr(null);
		comboBox.addItem("1");
		wmc.setOMedia(null);
		wmc.setOMediaText(null);
		wmc.setDateiname(null);
	}

	private void jbInit() throws Throwable {

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changed(EventObject e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {

			if (e.getSource().equals(comboBox)) {

				selektierteZeileAnzeigen();

			}

			if (e.getSource().equals(wbuAbbrechen)) {
				setVisible(false);
			}

			if (e.getSource().equals(wbuSpeichern)) {

				if (iKey != null) {

					speichern(iKey);

				}

				setVisible(false);
			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_NEW)) {

				int iSel = comboBox.getSelectedIndex();
				BelegartmediaDto vorherDto = belegartmediaDtos.get(iSel);

				byte[] oMedia = wmc.getOMediaImage();
				vorherDto.setOMedia(oMedia);
				String mimeType = wmc.getMimeType();
				vorherDto.setDatenformatCNr(mimeType);

				Integer iAusrichtung = (Integer) comboBoxAusrichtung.getKeyOfSelectedItem();
				vorherDto.setIAusrichtung(iAusrichtung);

				String dateiname = wmc.getDateiname();
				vorherDto.setCDateiname(dateiname);

				int i = model.getSize();

				comboBox.addItem((i + 1) + "");

				BelegartmediaDto bmDto = new BelegartmediaDto();
				bmDto.setDatenformatCNr(null);
				wmc.setMimeType(null);

				belegartmediaDtos.add(bmDto);

				comboBox.setSelectedIndex(i);

				vorherDto = belegartmediaDtos.get(iSel);

				vorherDto.setOMedia(oMedia);
				vorherDto.setDatenformatCNr(mimeType);
				vorherDto.setIAusrichtung(iAusrichtung);
				vorherDto.setCDateiname(dateiname);
				belegartmediaDtos.set(iSel, vorherDto);

			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_DELETE)) {
				if (comboBox.getSelectedIndex() > 0) {

					int index = comboBox.getSelectedIndex();
					comboBox.removeItemAt(index);

					belegartmediaDtos.remove(index);

				} else if (comboBox.getSelectedIndex() == 0 && belegartmediaDtos.size() > 1) {
					int index = comboBox.getSelectedIndex();
					comboBox.removeItemAt(index);

					belegartmediaDtos.remove(index);

				} else if (comboBox.getSelectedIndex() == 0) {
					wmc.setOMedia(null);
					wmc.setOMediaText(null);
					wmc.setDateiname(null);
					wmc.getWcoArt().setKeyOfSelectedItem(null);

					belegartmediaDtos.get(0).setOMedia(null);
					belegartmediaDtos.get(0).setCDateiname(null);

				}

			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_POSITION_VONNNACHNPLUS1)) {
				if (model.getSize() > 1 && comboBox.getSelectedIndex() < model.getSize() - 1) {

					int selectedIndex = comboBox.getSelectedIndex();

					Object o = comboBox.getItemAt(comboBox.getSelectedIndex());

					Object o1 = comboBox.getItemAt(comboBox.getSelectedIndex() + 1);

					BelegartmediaDto bmDto = belegartmediaDtos.get(comboBox.getSelectedIndex());

					bmDto.setOMedia(wmc.getOMediaImage());
					bmDto.setDatenformatCNr(wmc.getMimeType());
					bmDto.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());
					bmDto.setCDateiname(wmc.getDateiname());

					BelegartmediaDto bmDto1 = belegartmediaDtos.get(comboBox.getSelectedIndex() + 1);

					belegartmediaDtos.set(comboBox.getSelectedIndex(), bmDto1);
					belegartmediaDtos.set(comboBox.getSelectedIndex() + 1, bmDto);

					bTausch = true;

					model.removeElementAt(comboBox.getSelectedIndex());
					model.insertElementAt(o1, comboBox.getSelectedIndex());
					model.removeElementAt(comboBox.getSelectedIndex() + 1);
					model.insertElementAt(o, comboBox.getSelectedIndex() + 1);

					comboBox.setSelectedIndex(comboBox.getSelectedIndex() + 1);
					bTausch = false;

				}
			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_POSITION_VONNNACHNMINUS1)) {
				if (model.getSize() > 1 && comboBox.getSelectedIndex() > 0) {

					int selectedIndex = comboBox.getSelectedIndex();

					Object o = comboBox.getItemAt(selectedIndex);

					Object o1 = comboBox.getItemAt(selectedIndex - 1);

					BelegartmediaDto bmDto = belegartmediaDtos.get(selectedIndex);

					bmDto.setOMedia(wmc.getOMediaImage());
					bmDto.setDatenformatCNr(wmc.getMimeType());
					bmDto.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());
					bmDto.setCDateiname(wmc.getDateiname());

					BelegartmediaDto bmDto1 = belegartmediaDtos.get(selectedIndex - 1);

					belegartmediaDtos.set(selectedIndex, bmDto1);
					belegartmediaDtos.set(selectedIndex - 1, bmDto);

					bTausch = true;

					model.removeElementAt(selectedIndex - 1);
					model.insertElementAt(o, selectedIndex - 1);

					model.removeElementAt(selectedIndex);
					model.insertElementAt(o1, selectedIndex);

					comboBox.setSelectedIndex(selectedIndex - 1);
					bTausch = false;

				}
			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_PREVIOUS)) {
				int index = comboBox.getSelectedIndex();
				if (index > 0) {
					index--;
					comboBox.setSelectedIndex(index);
				}
			}

			if (e.getActionCommand().equals(PanelBasis.ACTION_NEXT)) {
				int index = comboBox.getSelectedIndex();
				if (index < model.getSize() - 1) {
					index++;
					comboBox.setSelectedIndex(index);
				}
			}

		} catch (Throwable e1) {
			intFrame.handleException(e1, true);
		}

	}

	public void speichern(Integer iKey) throws Throwable, ExceptionLP {
		this.iKey = iKey;
		int iSel = comboBox.getSelectedIndex();
		if (iSel >= 0) {
			BelegartmediaDto bmDto = belegartmediaDtos.get(iSel);

			bmDto.setCBez(comboBox.getSelectedItem() + "");
			bmDto.setDatenformatCNr(wmc.getMimeType());

			bmDto.setOMedia(wmc.getOMediaImage());
			bmDto.setXText(wmc.getOMediaText());
			bmDto.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());
			bmDto.setCDateiname(wmc.getDateiname());

		}

		Iterator it = belegartmediaDtos.iterator();

		try {
			while (it.hasNext()) {
				BelegartmediaDto bmDto = (BelegartmediaDto) it.next();
				if (bmDto.getDatenformatCNr() == null || bmDto.getOMedia() == null) {
					belegartmediaDtos.remove(bmDto);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		DelegateFactory.getInstance().getBelegartmediaDelegate().syncBelegartmediaDtos(iUseCase, iKey,
				belegartmediaDtos);

		bEsSindVochUngespeicherteDatenVorhanden = false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		System.out.println("Property-Name" + evt.getPropertyName());

		System.out.println("OLD-Value" + evt.getOldValue());

	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		try {
			if (bTausch == false) {

				if (ie.getStateChange() == ItemEvent.DESELECTED) // edit: bracket was missing
				{
					int selectedIndexBefore = model.getIndexOf(ie.getItem());

					if (selectedIndexBefore > -1) {

						BelegartmediaDto bm = belegartmediaDtos.get(selectedIndexBefore);
						bm.setCBez(ie.getItem() + "");
						bm.setDatenformatCNr(wmc.getMimeType());

						bm.setOMedia(wmc.getOMediaImage());
						bm.setXText(wmc.getOMediaText());
						bm.setCDateiname(wmc.getDateiname());
						bm.setIAusrichtung((Integer) comboBoxAusrichtung.getKeyOfSelectedItem());

					}

				} else if (ie.getStateChange() == ItemEvent.SELECTED) {
					int selectedIndexActual = model.getIndexOf(ie.getItem());

					if (selectedIndexActual > -1) {

						BelegartmediaDto bmDto = belegartmediaDtos.get(selectedIndexActual);

						if (bmDto.getDatenformatCNr() != null) {
							wmc.setMimeType(bmDto.getDatenformatCNr().trim());
						}

						wmc.setOMediaImage(bmDto.getOMedia());
						wmc.setOMediaText(bmDto.getXText());
						wmc.setDateiname(bmDto.getCDateiname());

						comboBoxAusrichtung.setKeyOfSelectedItem(bmDto.getIAusrichtung());
					}

				}
			}
		} catch (Throwable e) {
			intFrame.handleException(e, true);
		}
	}

	private class FileImportDropHandler implements DropListener {

		private DialogBelegartMedia dialog = null;

		public FileImportDropHandler(DialogBelegartMedia dialog) {
			this.dialog = dialog;
		}

		@Override
		public void filesDropped(List<File> files) {

			try {

				String allFilesAusgelassen = "";

				for (File f : files) {
					MediaType mimeType = HelperClient.getMimeTypeOfFile(f);
					String filename = f.getName();

					String extension = filename.substring(filename.lastIndexOf(".") + 1);

					String sMimeType = null;
					if (extension.toUpperCase().equals("GIF")) {
						sMimeType = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF;
					} else if (extension.toUpperCase().equals("PNG")) {
						sMimeType = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG;
					} else if (extension.toUpperCase().equals("JPG") || extension.toUpperCase().equals("JPEG")) {
						sMimeType = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG;
					} else if (extension.toUpperCase().equals("TIFF")) {
						sMimeType = MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF;
					}

					if (sMimeType != null) {

						addZeile(sMimeType, filename, Helper.getBytesFromFile(f));

						comboBox.setSelectedIndex(comboBox.getModel().getSize() - 1);
					}

					if (sMimeType == null) {

						allFilesAusgelassen += filename + "\n";
					}
					
					
					if (allFilesAusgelassen.length() > 0) {
						JOptionPane pane = new JOptionPane(allFilesAusgelassen);
						final JDialog dialog = pane
								.createDialog(LPMain.getTextRespectUISPr("lp.bilder.nicht.hinzugefuegt"));
						dialog.setModal(true);
						dialog.setSize(400, dialog.getHeight());
						
						dialog.setLocationRelativeTo(panelBasis);

						dialog.setVisible(true);
					}
					

				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
