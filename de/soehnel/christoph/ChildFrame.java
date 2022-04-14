package de.soehnel.christoph;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

public class ChildFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;

	private JScrollPane scrollpane;

	protected ViewComponent viewComponent;

	protected String title;

	public ChildFrame(String title) {
		super(title, true, true);
		this.title = title;
		viewComponent = new ViewComponent();
		scrollpane = new JScrollPane(viewComponent,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollpane, BorderLayout.CENTER);
		setIconifiable(true);
		setMaximizable(true);
	}
}
