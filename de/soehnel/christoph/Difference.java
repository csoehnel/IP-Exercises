package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Difference {

	private BufferedImage image_a;

	private BufferedImage image_b;

	private BufferedImage image_diff;

	public Difference(BufferedImage first, BufferedImage second) {
		image_a = first;
		image_b = second;
		image_diff = new BufferedImage(image_a.getWidth(), image_a.getHeight(),
				BufferedImage.TYPE_INT_RGB);
	}

	public BufferedImage getDifferenceImage() {
		int temp_r = 0;
		int temp_g = 0;
		int temp_b = 0;
		for (int j = 0; j < image_a.getHeight(); j++) {
			for (int i = 0; i < image_a.getWidth(); i++) {
				if (j < image_b.getHeight() && i < image_b.getWidth()) {
					temp_r = ((image_a.getRGB(i, j) >> 16) & 0xff)
							- ((image_b.getRGB(i, j) >> 16) & 0xff);
					temp_g = ((image_a.getRGB(i, j) >> 8) & 0xff)
							- ((image_b.getRGB(i, j) >> 8) & 0xff);
					temp_b = (image_a.getRGB(i, j) & 0xff)
							- (image_b.getRGB(i, j) & 0xff);
					if (temp_r < 0)
						temp_r = 0;
					if (temp_g < 0)
						temp_g = 0;
					if (temp_b < 0)
						temp_b = 0;
				} else {
					temp_r = ((image_a.getRGB(i, j) >> 16) & 0xff);
					temp_g = ((image_a.getRGB(i, j) >> 8) & 0xff);
					temp_b = (image_a.getRGB(i, j) & 0xff);
				}
				image_diff.setRGB(i, j, new Color(temp_r, temp_g, temp_b)
						.getRGB());

			}
		}
		return image_diff;
	}

}
