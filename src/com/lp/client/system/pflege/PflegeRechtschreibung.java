package com.lp.client.system.pflege;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxItem;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.rechtschreibung.RechtschreibpruefungCore;
import com.lp.client.util.GridBagConstraintBuilder;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.WoerterbuchEintragDto;
import com.lp.server.util.WoerterbuchEintragIId;

import net.miginfocom.swing.MigLayout;

public class PflegeRechtschreibung implements IPflegefunktion {

	private JPanel panel;

	private JTable table;
	private String selectedLanguage;
	private List<WoerterbuchEintragDto> deleted = new ArrayList<WoerterbuchEintragDto>();
	private Logger myLogger = LpLogger.getLogger(getClass());

	@Override
	public String getKategorie() {
		return KATEGORIE_ALLGEMEIN;
	}

	@Override
	public String getName() {
		return LPMain.getTextRespectUISPr("pflege.rechtschreibung.name");
	}

	@Override
	public String getBeschreibung() {
		return "Mit dieser Funktion k&ouml;nnen neue W&ouml;rter zur HeliumV Rechtschreibpr&uuml;fung hinzugef&uuml;gt"
				+ " werden.<br>"
				+ "Ein Wort kann entweder f&uuml;r nur eine Sprache, oder f&uuml;r alle Sprachen g&uuml;ltig"
				+ " sein, diese Option kann in der Combobox ausgew&auml;hlt werden.<br>"
				+ "Neue W&ouml;rter werden mit dem Button "
				+ LPMain.getTextRespectUISPr("pflege.rechtschreibung.upload") + " auf den Server hochgeladen, mit "
				+ LPMain.getTextRespectUISPr("pflege.rechtschreibung.sync")
				+ " kann die Wortliste vom Server aktualisiert werden. Beide Funktionen aktualisieren auch die in der"
				+ " Rechtschreibpr&uuml;fung verwendeten W&ouml;rter.";
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public boolean supportsProgressBar() {
		return false;
	}

	@Override
	public boolean isStartable() {
		return false;
	}

	@Override
	public void run() {
	}

	@Override
	public void cancel() {
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void addPflegeEventListener(PflegeEventListener listener) {
	}

	@Override
	public void removeAllPflegeEventListeners() {
	}

	@Override
	public void init() {
		panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.add(createTopPanel(), BorderLayout.PAGE_START);
		panel.add(createList(), BorderLayout.CENTER);
		panel.add(createBottomPanel(), BorderLayout.SOUTH);
		syncFromServer();
	}

	@Override
	public void eventYouAreSelected() {
		syncFromServer();
	}

	@Override
	public String toString() {
		return getName();
	}

	@SuppressWarnings("unchecked")
	private Component createTopPanel() {
		JPanel topPanel = new JPanel();
		JButton btnLoeschen = ButtonFactory
				.createJButton(new ImageIcon(getClass().getResource("/com/lp/client/res/delete2.png")));
		btnLoeschen.addActionListener(e -> deleteSelected());
		HelperClient.setMinimumAndPreferredSize(btnLoeschen, HelperClient.getToolsPanelButtonDimension());
		btnLoeschen.addActionListener(e -> deleteSelected());
		WrapperComboBox comboBoxSprache = new WrapperComboBox();
		comboBoxSprache.setMandatoryField(true);
		try {
			Map<?, ?> localeMap = DelegateFactory.getInstance().getLocaleDelegate()
					.getAllLocales(LPMain.getTheClient().getLocUi());
			comboBoxSprache.setMap(localeMap);
		} catch (Throwable e) {
			myLogger.error("Fehler beim Laden der Locales vom Server", e);
		}
		comboBoxSprache.addItem(new WrapperComboBoxItem(LocaleFac.WOERTERBUCH_LOCALE_ALL,
				LPMain.getTextRespectUISPr("pflege.rechtschreibung.alllang")));
		comboBoxSprache.setKeyOfSelectedItem(LocaleFac.WOERTERBUCH_LOCALE_ALL);
		selectedLanguage = LocaleFac.WOERTERBUCH_LOCALE_ALL;
		comboBoxSprache.addItemListener(this::spracheUpdate);
		topPanel.setLayout(new MigLayout("wrap 3, fill", "[2cm][fill,grow][grow,fill,5cm:7cm:10cm]"));
		topPanel.add(btnLoeschen);
		topPanel.add(new JLabel(LPMain.getTextRespectUISPr("lp.locale"), SwingConstants.RIGHT), "growx");
		topPanel.add(comboBoxSprache, "growx");
		return topPanel;
	}

	private Component createList() {
		TableModel model = newTableModel();
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		table.getTableHeader().setUI(null);
		table.setDefaultRenderer(TableObject.class, new RechtschreibungCellRenderer());
		table.setDefaultEditor(TableObject.class, new DefaultCellEditor(new JTextField()));
		return scrollPane;
	}

	private RechtschreibungTableModel newTableModel() {
		return new RechtschreibungTableModel();
	}

	private Component createBottomPanel() {
		JPanel botPanel = new JPanel();
		JLabel labelNewWord = new JLabel(LPMain.getTextRespectUISPr("pflege.rechtschreibung.neuwort"));
		WrapperTextField newWordField = new WrapperTextField();
		newWordField.addKeyListener(new NewWordOnEnterHandler(newWordField));
		JButton btnAdd = ButtonFactory
				.createJButton(new ImageIcon(getClass().getResource("/com/lp/client/res/plus_sign.png")));
		HelperClient.setMinimumAndPreferredSize(btnAdd, HelperClient.getToolsPanelButtonDimension());

		WrapperButton btnSync = new WrapperButton(LPMain.getTextRespectUISPr("pflege.rechtschreibung.sync"));
		WrapperButton btnUpload = new WrapperButton(LPMain.getTextRespectUISPr("pflege.rechtschreibung.upload"));

		btnSync.addActionListener(this::actionSync);
		btnUpload.addActionListener(e -> uploadToServer());

		GridBagLayout layout = new GridBagLayout();
		botPanel.setLayout(layout);
		layout.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0 };
		GridBagConstraintBuilder cb = new GridBagConstraintBuilder();
		cb.anchor(GridBagConstraints.EAST).fill(GridBagConstraints.HORIZONTAL);
		botPanel.add(labelNewWord, cb.build());
		cb.anchor(GridBagConstraints.EAST).x(1).width(2);
		botPanel.add(newWordField, cb.build());
		botPanel.add(btnSync, cb.width(1).incY().build());
		GridBagConstraintBuilder cbAdd = cb.clone().fill(GridBagConstraints.NONE).x(3).y(0);
		botPanel.add(btnAdd, cbAdd.build());
		cb.width(2).x(2);
		botPanel.add(btnUpload, cb.build());

		btnAdd.addActionListener(new NewWordHandler(newWordField));

		return botPanel;
	}

	private void actionSync(ActionEvent e) {
		if (shouldContinueWithModifications()) {
			syncFromServer();
		}
	}

	private void syncFromServer() {
		RechtschreibungTableModel model = newTableModel();
		try {
			List<WoerterbuchEintragDto> woerter = DelegateFactory.getInstance().getLocaleDelegate()
					.getAllWoerterbuchEintragZuSprache(selectedLanguage);
			woerter.forEach(w -> model.addRow(new TableObject(w.getId(), w.getWort())));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		table.setModel(model);
		deleted.clear();
		RechtschreibpruefungCore.getInstance().reloadCustomWords();
	}

	private void uploadToServer() {
		List<TableObject> modifiedTableObjects = getModifiedTableObjects();

		if (modifiedTableObjects.isEmpty() && deleted.isEmpty())
			return;

		List<WoerterbuchEintragDto> toCreate = new ArrayList<WoerterbuchEintragDto>();
		List<WoerterbuchEintragDto> toUpdate = new ArrayList<WoerterbuchEintragDto>();

		for (TableObject mod : modifiedTableObjects) {
			if (!mod.db_id.isValid()) {
				// Insert
				toCreate.add(new WoerterbuchEintragDto(selectedLanguage, mod.word));
			} else {
				// Update
				toUpdate.add(new WoerterbuchEintragDto(mod.db_id, selectedLanguage, mod.word));
			}
		}

		try {
			DelegateFactory.getInstance().getLocaleDelegate().woerterbuchEintragBatchCRUD(toCreate, toUpdate, deleted);
			deleted.clear();
			syncFromServer();
		} catch (ExceptionLP e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void spracheUpdate(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			WrapperComboBoxItem item = (WrapperComboBoxItem) e.getItem();
			String newLang = item.getKey().toString();
			if (newLang.equals(selectedLanguage)) {
				return;
			}
			boolean cont = shouldContinueWithModifications();
			if (cont) {
				selectedLanguage = newLang;
				syncFromServer();
			} else {
				((WrapperComboBox) (e.getSource())).setKeyOfSelectedItem(selectedLanguage);
			}
		}
	}

	private void addNewRow(String newText) {
		((RechtschreibungTableModel) table.getModel()).addRow(TableObject.createNew(newText));
	}

	private class NewWordHandler implements ActionListener {
		private final JTextComponent input;

		public NewWordHandler(JTextComponent input) {
			this.input = input;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String newText = input.getText();
			addNewRow(newText);
			input.setText("");
		}
	}

	private class NewWordOnEnterHandler extends KeyAdapter {
		private final JTextComponent input;

		public NewWordOnEnterHandler(JTextComponent input) {
			this.input = input;
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String newText = input.getText();
				input.setText("");
				addNewRow(newText);
			}
		}
	}

	private List<TableObject> getModifiedTableObjects() {
		RechtschreibungTableModel model = (RechtschreibungTableModel) table.getModel();
		return model.getData().stream().filter(TableObject::isModified).collect(Collectors.toList());
	}

	private boolean isTableModified() {
		return !(deleted.isEmpty() && getModifiedTableObjects().isEmpty());
	}

	private void deleteSelected() {
		int[] selected = table.getSelectedRows();
		RechtschreibungTableModel model = (RechtschreibungTableModel) table.getModel();
		for (int i : selected) {
			TableObject data = model.getData().get(i);
			if (data.db_id.isValid()) {
				deleted.add(new WoerterbuchEintragDto(data.db_id, selectedLanguage, data.word));
			}
		}
		model.deleteRows(selected);
	}

	public static class TableObject {
		private String word;
		public final String originalWord;
		public final WoerterbuchEintragIId db_id;

		public TableObject(WoerterbuchEintragIId id, String origWord) {
			this.originalWord = origWord;
			this.word = origWord;
			db_id = id;
		}

		public static TableObject createNew(String text) {
			TableObject newObj = new TableObject(WoerterbuchEintragIId.emptyKey(), "");
			newObj.setWord(text);
			return newObj;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public boolean isModified() {
			return !(word.equals(originalWord) && db_id.isValid());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((db_id == null) ? 0 : db_id.hashCode());
			result = prime * result + ((originalWord == null) ? 0 : originalWord.hashCode());
			result = prime * result + ((word == null) ? 0 : word.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableObject other = (TableObject) obj;
			if (db_id == null) {
				if (other.db_id != null)
					return false;
			} else if (!db_id.equals(other.db_id))
				return false;
			if (originalWord == null) {
				if (other.originalWord != null)
					return false;
			} else if (!originalWord.equals(other.originalWord))
				return false;
			if (word == null) {
				if (other.word != null)
					return false;
			} else if (!word.equals(other.word))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return word;
		}

	}

	/**
	 * Prueft ob es Aenderungen gibt, wenn ja wird der Benutzer gefragt, ob diese
	 * Verworfen werden sollen. Soll vor jeder Operation aufgerufen werden, die
	 * Daten &uuml;berschreibt, wenn sie durchgef&uuml;hrt wird.
	 * 
	 * @return true falls keine &Auml;nderung vorhanden ist oder der Nutzer die
	 *         &Auml;nderungen verwirft. Sonst false
	 */
	private boolean shouldContinueWithModifications() {
		if (isTableModified()) {
			boolean result = DialogFactory.showModalJaNeinDialog(null,
					LPMain.getTextRespectUISPr("pflege.rechtschreibung.aenderung"),
					LPMain.getTextRespectUISPr("lp.warning"), JOptionPane.WARNING_MESSAGE, JOptionPane.NO_OPTION);
			return result;
		}
		return true;
	}

	/**
	 * Cell Renderer fuer die Liste der W&ouml;rter in der Rechtschreibungspflege.
	 * Neue W&ouml;rter werden gr&uuml;n, ver&auml;nderte W&ouml;rter werden gelb
	 * hinterlegt.
	 */
	private static class RechtschreibungCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		private Color unselectedForeground;
		private Color unselectedBackground;

		/*
		 * Impl-Note Diese Methode muss ganz Ueberschrieben werden, weil (zumindest bis
		 * Java 11) die DefaultTableCellRenderer Klasse in setBackground die Farbe
		 * speichert und weiter verwendet. Wird damit einmal die Farbe gesetzt, bekommt
		 * jede Zelle diese Farbe (was nicht gewollt ist)
		 */
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			if (table == null) {
				return this;
			}

			Color fg = null;
			Color bg = null;

			JTable.DropLocation dropLocation = table.getDropLocation();
			if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
					&& dropLocation.getRow() == row && dropLocation.getColumn() == column) {

				fg = UIManager.getColor("Table.dropCellForeground");
				bg = UIManager.getColor("Table.dropCellBackground");

				isSelected = true;
			}

			if (isSelected) {
				super.setForeground(fg == null ? table.getSelectionForeground() : fg);
				super.setBackground(bg == null ? table.getSelectionBackground() : bg);
			} else {
				Color background = getColorIfModified(value);
				if (background == null) {
					background = unselectedBackground != null ? unselectedBackground : table.getBackground();
				}
				if (background == null || background instanceof javax.swing.plaf.UIResource) {
					Color alternateColor = UIManager.getColor("Table.alternateRowColor");
					if (alternateColor != null && row % 2 != 0) {
						background = alternateColor;
					}
				}
				super.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
				super.setBackground(background);
			}

			setFont(table.getFont());

			if (hasFocus) {
				Border border = null;
				if (isSelected) {
					border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
				}
				if (border == null) {
					border = UIManager.getBorder("Table.focusCellHighlightBorder");
				}
				setBorder(border);
			} else {
				setBorder(noFocusBorder);
			}

			setValue(value);

			return this;
		}

		private Color getColorIfModified(Object value) {
			if (value instanceof TableObject) {
				TableObject to = (TableObject) value;
				if (!to.db_id.isValid()) {
					return Color.green;
				} else if (to.isModified()) {
					return Color.yellow;
				}
			}
			return null;
		}
	}

	private static class RechtschreibungTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private Vector<TableObject> data;

		public RechtschreibungTableModel() {
			data = new Vector<>();
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return TableObject.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data.get(rowIndex);
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (aValue instanceof TableObject) {
				TableObject to = (TableObject) aValue;
				if (validateData(to.getWord(), rowIndex)) {
					data.set(rowIndex, to);
				}
			} else {
				if (validateData(aValue.toString(), rowIndex)) {
					data.get(rowIndex).setWord(aValue.toString());
				}
			}

			fireTableCellUpdated(rowIndex, columnIndex);
		}

		public void addRow(TableObject newRow) {
			if (validateData(newRow.getWord(), -1)) {
				data.add(newRow);
				fireTableRowsInserted(data.size() - 1, data.size() - 1);
			}
		}

		public List<TableObject> getData() {
			return data;
		}

		public void deleteRows(int... rows) {
			// In absteigender Reihenfolge entfernen, sonst kann sich der index aendern weil
			// bei jedem entfernen Elemente mit höherem index verschoben werden
			Arrays.sort(rows, 0, rows.length);
			for (int idx = rows.length - 1; idx >= 0; idx--) {
				data.remove(rows[idx]);
				fireTableRowsDeleted(rows[idx], rows[idx]);
			}
		}

		protected boolean validateData(String newWord, int rowIndex) {
			for (int i = 0; i < data.size(); i++) {
				if (i != rowIndex && data.get(i).word.equals(newWord)) {
					// TODO Dialog
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("pflege.rechtschreibung.wortdoppelt.titel"),
							LPMain.getMessageTextRespectUISPr("pflege.rechtschreibung.wortdoppelt.message", newWord));
					return false;
				}
			}
			return true;
		}

	}

}
