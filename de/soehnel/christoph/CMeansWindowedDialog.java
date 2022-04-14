package de.soehnel.christoph;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

public class CMeansWindowedDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton okButton;

	private JLabel l_num_cluster;

	private JFormattedTextField t_num_cluster;

	private JLabel l_error_thresh;

	private JFormattedTextField t_error_thresh;

	private JLabel l_m;

	private JFormattedTextField t_m;

	private JLabel l_n;

	private JFormattedTextField t_n;

	private int num_cluster;

	private int error_thresh;

	private int m;

	private int n;

	public CMeansWindowedDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2, 10, 5));
		l_num_cluster = new JLabel("Anzahl Cluster: ");
		panel.add(l_num_cluster);
		try {
			MaskFormatter mf = new MaskFormatter("##");
			mf.setPlaceholderCharacter('0');
			t_num_cluster = new JFormattedTextField(mf);
			t_error_thresh = new JFormattedTextField(mf);
			t_m = new JFormattedTextField(mf);
			t_n = new JFormattedTextField(mf);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t_num_cluster.setText("02");
		t_error_thresh.setText("00");
		t_m.setText("03");
		t_n.setText("03");
		panel.add(t_num_cluster);
		l_error_thresh = new JLabel("Fehlerschwelle: ");
		panel.add(l_error_thresh);
		panel.add(t_error_thresh);
		l_m = new JLabel("m: ");
		panel.add(l_m);
		panel.add(t_m);
		l_n = new JLabel("n: ");
		panel.add(l_n);
		panel.add(t_n);
		pack();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		panel.add(okButton);
		pack();
		getContentPane().add(panel, BorderLayout.SOUTH);
		num_cluster = 0;
		error_thresh = 0;
		m = 0;
		n = 0;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		num_cluster = Integer.valueOf(t_num_cluster.getText());
		error_thresh = Integer.valueOf(t_error_thresh.getText());
		m = Integer.valueOf(t_m.getText());
		n = Integer.valueOf(t_n.getText());
		this.dispose();
	}

	public int getErrorThresh() {
		return this.error_thresh;
	}

	public int getNumCluster() {
		return this.num_cluster;
	}

	public int getM() {
		return this.m;
	}

	public int getN() {
		return this.n;
	}

	public void setNumClusterChangeDisabled(boolean state) {
		if (state)
			this.t_num_cluster.setEnabled(!state);
	}

	public void setMChangeDisabled(boolean state) {
		if (state)
			this.t_m.setEnabled(!state);
	}

	public void setNChangeDisabled(boolean state) {
		if (state)
			this.t_n.setEnabled(!state);
	}
}
