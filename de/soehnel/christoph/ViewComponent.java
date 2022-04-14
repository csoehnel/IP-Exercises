package de.soehnel.christoph;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ViewComponent extends JComponent {

	private static final long serialVersionUID = 1L;

	protected BufferedImage image;

	public ViewComponent() {
		super();
	}

	protected void paintComponent(Graphics g) {
		if (image != null)
			g.drawImage(image, 0, 0, this);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		if (this.image != null)
			repaint();
	}

	public int getHeight() {
		return image.getHeight(this);
	}

	public int getWidth() {
		return image.getWidth(this);
	}

	public Dimension getPreferredSize() {
		return new Dimension(image.getWidth(), image.getHeight());
	}
}
