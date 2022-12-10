package com.lp.client.lieferschein;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;

public abstract class DialogLieferaviso {	
	private final int MAXIMUM = 100;
	private Throwable resultException = null;

	
	public void showModal(Frame owner, final LieferscheinDto lsDto) {
		final JDialog dialog = new JDialog(owner, true);
		showImpl(dialog, lsDto);
	}
	
	public void show(final LieferscheinDto lsDto) {
		final JDialog dialog = new JDialog();
		showImpl(dialog, lsDto);
	}

	private JPanel createPanel() {
		JProgressBar progressBar = new JProgressBar(0, MAXIMUM);
		progressBar.setIndeterminate(true);
		JTextArea msgLabel = new JTextArea(
				LPMain.getTextRespectUISPr("lp.versandweg.durchfuehren"));
		msgLabel.setEditable(false);

		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(msgLabel, BorderLayout.PAGE_START);
		panel.add(progressBar, BorderLayout.CENTER);
		panel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));
		msgLabel.setBackground(panel.getBackground());
		return panel;
	}
	
	private void prepare(JDialog dialog) {
		dialog.setTitle(LPMain.getTextRespectUISPr("ls.lieferaviso"));
		dialog.getContentPane().add(createPanel());

		dialog.setResizable(false);
		dialog.pack();
		dialog.setSize(500, dialog.getHeight());
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setAlwaysOnTop(false);	
	}
	
	private void showImpl(final JDialog dialog, final LieferscheinDto lsDto) {
		prepare(dialog);
		
		SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {
			@Override
			protected void done() {
				dialog.dispose();

				if (resultException != null) {
					processException(resultException);
				}
			}

			@Override
			protected String doInBackground() throws Exception {
				String result = "";
				resultException = null;
				try {
					result = DelegateFactory.getInstance().getLsDelegate()
							.createLieferscheinAvisoPost(lsDto.getIId());
					publish(result);
					setProgress(100);
				} catch (Throwable t) {
					resultException = t;
				}

				return result;
			}

			@Override
			protected void process(List<Object> chunks) {
				for (Object result : chunks) {
					if (result instanceof String) {
						try {
							processed(lsDto);
						} catch (Throwable t) {
							resultException = t;
						}
					}
				}
			}
		};

		worker.execute();
		dialog.setVisible(true);
	}
	
	public Throwable getThrowable() {
		return resultException;
	}
	
	protected abstract void processed(LieferscheinDto lsDto) throws Throwable;
	
	protected abstract void processException(Throwable t);
}
