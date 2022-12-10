package com.lp.client.frame.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.HvLayout;
import com.lp.client.frame.HvLayoutFactory;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.ReversechargeartDto;



public class DialogReversechargeGeaendertER extends JDialog {
	private static final long serialVersionUID = -1035708115525024971L;
	
	private Integer otherReversechargeId ;
	private Integer myReversechargeId ;
	
	private boolean modified ;
	private JButton btnBeleg ;
	private JButton btnLieferant ;
	
	private Map<Integer, ReversechargeartDto> rcartDtos ;
	
	public DialogReversechargeGeaendertER(Integer erReversechargeId, Integer otherReversechargeId) throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getTextRespectUISPr("er.dialog.beleglieferantunterschiedlich.titel"), true) ;
		this.myReversechargeId = erReversechargeId ;
		this.otherReversechargeId = otherReversechargeId ;

		rcartDtos = DelegateFactory.getInstance().getFinanzServiceDelegate()
				.reversechargeartFindByPrimaryKeys(myReversechargeId, otherReversechargeId) ;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(Defaults.getInstance().bySizeFactor(600, 150));
	}

	public boolean isModified() {
		return modified ;
	}
	
	private void jbInit() {
		
		JPanel panel = new JPanel() ;

		HvLayout layout = HvLayoutFactory.create(panel, "wrap 1") ;
		layout
			.add(new JLabel(LPMain.getMessageTextRespectUISPr(
					"er.dialog.beleglieferantunterschiedlich.info",
					rcartDtos.get(myReversechargeId).getSprDto().getcBez(),
					rcartDtos.get(otherReversechargeId).getSprDto().getcBez())));
		layout
			.add(new JLabel(LPMain.getTextRespectUISPr(
				"er.dialog.beleglieferantunterschiedlich.hinweis"))) ;

		WrapperGotoButton btnFinanzamt = new WrapperGotoFinanzamt("Finanzamt") ;
		btnFinanzamt.getWrapperButtonGoTo().addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogReversechargeGeaendertER.this.setVisible(false) ;				
			}
		});
		layout
			.add(btnFinanzamt, "growx") ;
		btnFinanzamt.setOKey(new Integer(1286));
		
		btnBeleg = new JButton(LPMain.getTextRespectUISPr("er.eingangsrechnung")) ;
		btnBeleg.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogReversechargeGeaendertER.this.setVisible(false) ;				
			}
		});
		
		btnLieferant = new JButton(LPMain.getTextRespectUISPr("lp.lieferant")) ;
		btnLieferant.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				modified = true ;
				DialogReversechargeGeaendertER.this.setVisible(false) ;
			}
		});
		
		layout
			.add(btnBeleg, "split 2, skip")
			.add(btnLieferant) ;

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER) ;
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			dispose();
		}
	}	
}
