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

public class FilterSizeDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JButton okButton;

	private JLabel l_dimension;

	private JFormattedTextField t_dimension;

	private int dimension;

	public FilterSizeDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		dimension = 0;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 10, 5));
		l_dimension = new JLabel("Filterkernelgröße: ");
		panel.add(l_dimension);
		try {
			MaskFormatter mf = new MaskFormatter("##");
			mf.setPlaceholderCharacter('0');
			t_dimension = new JFormattedTextField(mf);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t_dimension.setText("03");
		panel.add(t_dimension);
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
		this.dimension = Integer.valueOf(t_dimension.getText());
		this.dispose();
	}

	public int getDimension() {
		return this.dimension;
	}
}
