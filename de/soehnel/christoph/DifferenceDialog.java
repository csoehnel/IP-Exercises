package de.soehnel.christoph;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DifferenceDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton okButton;

	private JLabel l_firstimagetitle;

	private JComboBox cb_firstimagetitle;

	private JLabel l_secondimagetitle;

	private JComboBox cb_secondimagetitle;

	private String firstimagetitle;

	private String secondimagetitle;

	public DifferenceDialog(JFrame parent, String title, boolean modal,
			Vector<String> frametitles) {
		super(parent, title, modal);
		firstimagetitle = null;
		secondimagetitle = null;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2, 10, 5));
		l_firstimagetitle = new JLabel("Bild A: ");
		panel.add(l_firstimagetitle);
		cb_firstimagetitle = new JComboBox(frametitles);
		panel.add(cb_firstimagetitle);
		l_secondimagetitle = new JLabel("Bild B: ");
		panel.add(l_secondimagetitle);
		cb_secondimagetitle = new JComboBox(frametitles);
		panel.add(cb_secondimagetitle);
		pack();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		panel.add(okButton);
		pack();
		getContentPane().add(panel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.firstimagetitle = cb_firstimagetitle.getSelectedItem().toString();
		this.secondimagetitle = cb_secondimagetitle.getSelectedItem()
				.toString();
		this.dispose();
	}

	public String getFirstImageTitle() {
		return this.firstimagetitle;
	}

	public String getSecondImageTitle() {
		return this.secondimagetitle;
	}
}
