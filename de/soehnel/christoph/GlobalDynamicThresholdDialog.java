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

public class GlobalDynamicThresholdDialog extends JDialog implements
		ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton okButton;

	private JLabel l_neighbours;

	private JFormattedTextField t_neighbours;

	private JLabel l_overlap;

	private JFormattedTextField t_overlap;

	private JLabel l_parameter_r;

	private JFormattedTextField t_parameter_r;

	private int neighbours;

	private int overlap;

	private int parameter_r;

	public GlobalDynamicThresholdDialog(JFrame parent, String title,
			boolean modal) {
		super(parent, title, modal);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 10, 5));
		try {
			MaskFormatter mf = new MaskFormatter("###");
			mf.setPlaceholderCharacter('0');
			t_neighbours = new JFormattedTextField(mf);
			t_overlap = new JFormattedTextField(mf);
			t_parameter_r = new JFormattedTextField(mf);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t_neighbours.setText("003");
		t_overlap.setText("000");
		t_parameter_r.setText("040");
		l_neighbours = new JLabel("Nachbarn je Seite: ");
		panel.add(l_neighbours);
		panel.add(t_neighbours);
		l_overlap = new JLabel("Streamüberlappung: ");
		panel.add(l_overlap);
		panel.add(t_overlap);
		l_parameter_r = new JLabel("Wert für r: ");
		panel.add(l_parameter_r);
		panel.add(t_parameter_r);
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
		this.neighbours = Integer.valueOf(t_neighbours.getText());
		this.overlap = Integer.valueOf(t_overlap.getText());
		this.parameter_r = Integer.valueOf(t_parameter_r.getText());
		this.dispose();
	}

	public int getNeighbours() {
		return this.neighbours;
	}

	public int getOverlap() {
		return this.overlap;
	}

	public int getParameterR() {
		return this.parameter_r;
	}
}
