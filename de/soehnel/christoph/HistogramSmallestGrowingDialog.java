package de.soehnel.christoph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HistogramSmallestGrowingDialog extends JDialog implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JComboBox combo_thresholds;

	private JLabel label_thresholds;

	private JButton okButton;

	private int threshold;

	public HistogramSmallestGrowingDialog(JFrame parent, String title,
			boolean modal, Vector<Integer> growing) {
		super(parent, title, modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		label_thresholds = new JLabel("Schwellwert: ");
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
		panel.add(label_thresholds);
		combo_thresholds = new JComboBox(growing);
		combo_thresholds.setEditable(true);
		panel.add(combo_thresholds);
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		panel.add(okButton);
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.threshold = (Integer) combo_thresholds.getSelectedItem();
		this.dispose();
	}

	public int getThreshold() {
		return this.threshold;
	}
}
