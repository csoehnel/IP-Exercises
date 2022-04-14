package de.soehnel.christoph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.text.MaskFormatter;

public class CustomFilterDialog extends JDialog implements ActionListener,
		MouseListener {

	private static final long serialVersionUID = 1L;

	private JButton okButton;

	private JTable tbl_filterkernel;

	private JScrollPane sp_filterkernel;

	private Vector<Integer> filterkernel;

	private JLabel l_weight;

	private JFormattedTextField t_weight;

	private int x_dim;

	private int y_dim;

	private int weight;

	public CustomFilterDialog(JFrame parent, String title, boolean modal,
			int x_dim, int y_dim) {
		super(parent, title, modal);
		try {
			MaskFormatter mf = new MaskFormatter("##");
			mf.setPlaceholderCharacter('0');
			t_weight = new JFormattedTextField(mf);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.x_dim = x_dim;
		this.y_dim = y_dim;
		this.weight = 0;
		filterkernel = new Vector<Integer>();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel();
		tbl_filterkernel = new JTable(x_dim, y_dim);
		tbl_filterkernel.setTableHeader(null);
		tbl_filterkernel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbl_filterkernel.setCellSelectionEnabled(true);
		tbl_filterkernel.addMouseListener(this);
		for (int j = 0; j < y_dim; j++) {
			for (int i = 0; i < x_dim; i++) {
				tbl_filterkernel.setValueAt(1, i, j);
			}
		}
		for (int i = 0; i < x_dim; i++)
			tbl_filterkernel.getColumnModel().getColumn(i).setPreferredWidth(3);
		sp_filterkernel = new JScrollPane(tbl_filterkernel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp_filterkernel.setPreferredSize(new Dimension(138, 147));
		panel.add(sp_filterkernel);
		pack();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 10, 5));
		l_weight = new JLabel("Norm. (1/?) :");
		panel.add(l_weight);
		t_weight.setText("01");
		panel.add(t_weight);
		pack();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel = new JPanel();
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		panel.add(okButton);
		pack();
		getContentPane().add(panel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		for (int j = 0; j < y_dim; j++) {
			for (int i = 0; i < x_dim; i++) {
				filterkernel.add(Integer.parseInt(tbl_filterkernel.getValueAt(
						i, j).toString()));
			}
		}
		weight = Integer.parseInt(t_weight.getText());
		this.dispose();
	}

	public int getWeight() {
		return weight;
	}

	public Vector<Integer> getFilterKernel() {
		return filterkernel;
	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.setTitle("Filterkernel ("
				+ tbl_filterkernel.getSelectionModel()
						.getAnchorSelectionIndex()
				+ ", "
				+ tbl_filterkernel.getColumnModel().getSelectionModel()
						.getAnchorSelectionIndex() + ")");
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
