package com.lp.client.pc.erroraction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.component.ButtonFactory;
import com.lp.client.frame.component.WrapperGotoArtikelTrumpf;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.IErrorAction;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelTruTopsDto;
import com.lp.server.util.ArtikelId;
import com.lp.server.util.HvOptional;

public class TopsFallbackMehrerePfadeError implements IErrorAction {
	
	private JButton btnBoostPfadSpeichern;
	private ArtikelId artikelId;
	private JDialog errorDialog;
	private JTable tablePfade;
	private BoostPfad boostPfad;

	public TopsFallbackMehrerePfadeError() {
	}

	@Override
	public String getMsg(ExceptionLP exc) {
		return LPMain.getMessageTextRespectUISPr(
				"fert.losausgabe.tops.fallbackmehrerepfade", exc.getAlInfoForTheClient().toArray());
	}

	@Override
	public boolean shouldBeShown(JDialog dialog, JPanel panel, ExceptionLP exception) {
		try {
			errorDialog = dialog;
			errorDialog.setMinimumSize(Defaults.getInstance().bySizeFactor(650,300));
			
			List<?> info = exception.getAlInfoForTheClient();
			if (info.size() < 3)
				return false;
			
			artikelId = new ArtikelId((Integer) info.get(0));
			String msg = (String) info.get(2);
			
			boostPfad = new BoostPfad(Arrays.asList(StringUtils.split(msg, ";")));

			WrapperGotoButton btnGoto = createGotoButtonArtikelTrumpf(artikelId);
			btnGoto.closeDialogOnGoto(dialog);

			tablePfade = createTablePfade();
			JScrollPane scrollPane = new JScrollPane(tablePfade,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			
			if (DialogError.class.isInstance(dialog)) {
				btnBoostPfadSpeichern = createButtonBoostPfadSpeichern();
				((DialogError)dialog).addActionButton(btnBoostPfadSpeichern, 1);
			}

			HvLayout layout = HvLayoutFactory.create(panel, 
					"gap 5", "[center]", "[|]") ;
			layout.add(btnGoto, "split 2, w 50%, h 30:30:400, wrap") ;
			layout.add(scrollPane, "span, w 300:600:600, h 100:130:130, growy, growx");

			dialog.pack();
			
			return true ;				
				
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
		return false;
	}
	
	private JTable createTablePfade() {
		TableModelPfade tableModel = new TableModelPfade();
		JTable table = new JTable(tableModel);
		TableColumn colCheckbox = table.getColumnModel().getColumn(1);
		int width = 80;
		colCheckbox.setMinWidth(width);
		colCheckbox.setMaxWidth(width);
		colCheckbox.setPreferredWidth(width);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				tableModel.fireTableDataChanged();
			}
		});
		return table;
	}
	
	protected void onActionBoostPfadSpeichern() {
		try {
			if (!boostPfad.isSet()) 
				return;
			
			String selectedPfad = boostPfad.getChosenPfad();
			HvOptional<ArtikelTruTopsDto> opt = DelegateFactory.artikel().artikelTruTopsFindByArtikelId(artikelId);
			if (opt.isPresent()) {
				ArtikelTruTopsDto truTopsDto = opt.get();
				truTopsDto.setCPfad(selectedPfad);
				DelegateFactory.artikel().updateArtikelTruTops(truTopsDto);
			} else {
				ArtikelTruTopsDto truTopsDto = createArtikelTruTopsDto(artikelId);
				truTopsDto.setCPfad(selectedPfad);
				DelegateFactory.artikel().createArtikelTruTops(truTopsDto);
			}
			errorDialog.dispose();
		} catch (Throwable t) {
		}
	}
	
	private ArtikelTruTopsDto createArtikelTruTopsDto(ArtikelId artikelId) {
		ArtikelTruTopsDto dto = new ArtikelTruTopsDto();
		dto.setArtikelIId(artikelId.id());
		return dto;
	}

	private WrapperGotoButton createGotoButtonArtikelTrumpf(ArtikelId artikelId) {
		WrapperGotoButton btn = new WrapperGotoArtikelTrumpf(LPMain.getTextRespectUISPr("lp.artikel"));
		btn.setOKey(artikelId.id());
		btn.setEnabled(false);
		return btn;
	}
	
	private JButton createButtonBoostPfadSpeichern() {
		JButton btn = ButtonFactory.createJButton();
		HelperClient.setMinimumAndPreferredSize(btn, HelperClient.getSizeFactoredDimension(220));
		btn.setText(LPMain.getTextRespectUISPr("fert.losausgabe.tops.alsboostpfadspeichern"));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onActionBoostPfadSpeichern();
			}
		});
		btn.setEnabled(false);
		return btn;
	}

	protected void onBoostPfadSelectionChanged() {
		btnBoostPfadSpeichern.setEnabled(boostPfad.isSet());
	}

	private class TableModelPfade extends DefaultTableModel {
		private static final long serialVersionUID = 4239251168328853894L;
		
		public TableModelPfade() {
		}
		
		@Override
		public Class<?> getColumnClass(int column) {
			return column == 0 ? String.class : Boolean.class;
		}
		@Override
		public boolean isCellEditable(int row, int column) {
			return column != 0;
		}
		@Override
		public String getColumnName(int column) {
			return column == 0 
					? LPMain.getTextRespectUISPr("fert.losausgabe.tops.pfad") 
					: LPMain.getTextRespectUISPr("fert.losausgabe.tops.boostpfad");
		}
		@Override
		public Object getValueAt(int row, int column) {
			if (column == 0)
				return boostPfad.get(row);
						
			return boostPfad.isChosenRow(row);
		}
		@Override
		public int getColumnCount() {
			return 2;
		}
		public int getRowCount() {
			return boostPfad.size();
		}
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if (column != 1) return;
			
			Boolean value = (Boolean)aValue;
			if (Boolean.TRUE.equals(value)) {
				boostPfad.set(row);
			} else if (boostPfad.isChosenRow(row)) {
				boostPfad.reset();
			}
			fireTableDataChanged();
			onBoostPfadSelectionChanged();
		}
	}
	
	private class BoostPfad {
		private List<String> pfade = new ArrayList<String>();
		private Integer index;
		
		public BoostPfad(List<String> pfade) {
			this.pfade = pfade;
			reset();
		}
		
		public void set(Integer index) {
			this.index = index;
		}
		
		public boolean isSet() {
			return index >= 0;
		}
		
		public void reset() {
			index = -1;
		}
		
		public boolean isChosenRow(Integer row) {
			return index.equals(row);
		}
		
		public String getChosenPfad() {
			return isSet() ? pfade.get(index) : null;
		}
		
		public String get(Integer index) {
			return pfade.get(index);
		}
		
		public Integer size() {
			return pfade.size();
		}
	}
}
