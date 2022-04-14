package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class GrayWedge {

	protected BufferedImage image;

	protected int x;

	protected int y;

	protected int steps;

	public GrayWedge(int steps) {
		this.steps = steps;
		this.x = 256 * steps;
		this.y = 128 * steps;
		this.image = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		generateGrayWedge();
	}

	public void generateGrayWedge() {
		int gray = 0;
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++)
				image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
			if (i > 0 && i % steps == 0)
				gray++;
		}
	}
}
