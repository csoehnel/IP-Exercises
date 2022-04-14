package de.soehnel.christoph;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ImageRAWDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ButtonGroup bgroup;

	private JRadioButton rb_gray;

	private JRadioButton rb_rgb;

	private JTextField t_imgwidth;

	private JTextField t_imgheight;

	private JLabel l_imgwidth;

	private JLabel l_imgheight;

	private JButton okButton;

	private int width;

	private int height;

	private int mode;

	public ImageRAWDialog(JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		width = 0;
		height = 0;
		mode = 0;
		bgroup = new ButtonGroup();
		t_imgwidth = new JTextField("0", 4);
		t_imgheight = new JTextField("0", 4);
		l_imgwidth = new JLabel("Breite");
		l_imgheight = new JLabel("Höhe");
		rb_gray = new JRadioButton("Graustufen (1-Byte)");
		rb_gray.setActionCommand(rb_gray.getText());
		rb_rgb = new JRadioButton("RGB (3-Byte)");
		rb_rgb.setActionCommand(rb_rgb.getText());
		bgroup.add(rb_gray);
		bgroup.add(rb_rgb);
		rb_gray.setSelected(true);
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2, 10, 5));
		panel.add(l_imgwidth);
		panel.add(l_imgheight);
		panel.add(t_imgwidth);
		panel.add(t_imgheight);
		panel.add(rb_gray);
		panel.add(rb_rgb);
		panel.add(okButton);
		pack();
		getContentPane().add(panel, BorderLayout.CENTER);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getMode() {
		return this.mode;
	}

	public void actionPerformed(ActionEvent event) {
		ButtonModel selected = bgroup.getSelection();
		this.width = Integer.parseInt(t_imgwidth.getText());
		this.height = Integer.parseInt(t_imgheight.getText());
		if (selected.getActionCommand().equals("Graustufen (1-Byte)"))
			this.mode = 1;
		else
			this.mode = 3;
		this.setVisible(false);
	}

	public void setDlgDim(int dim) {
		t_imgwidth.setText(Integer.toString(dim));
		t_imgheight.setText(Integer.toString(dim));
		rb_gray.setSelected(true);
	}
}
