package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class CustomFilter {

	private int x;

	private int y;

	private int weight;

	private Vector<Integer> kernel;

	private BufferedImage img_flt;

	public CustomFilter(int x, int y, Vector<Integer> kernel, int weight) {
		this.x = x;
		this.y = y;
		this.weight = weight;
		this.kernel = kernel;
	}

	public BufferedImage applicateFilter(BufferedImage img_raw, int winwidth) {
		img_flt = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		double temp_r, temp_g, temp_b;
		int i_bin;
		int ffc = winwidth / 2;
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				temp_r = temp_g = temp_b = 0;
				for (int h = -ffc; h <= ffc; h++) {
					for (int g = -ffc; g <= ffc; g++) {
						i_bin = winwidth * (h + (Math.abs(ffc))) + g
								+ Math.abs(ffc);
						if (j + h >= 0 && j + h < y && i + g >= 0 && i + g < x) {
							temp_r += ((img_raw.getRGB(i + g, j + h) >> 16) & 0xff)
									* kernel.elementAt(i_bin);
							temp_g += ((img_raw.getRGB(i + g, j + h) >> 8) & 0xff)
									* kernel.elementAt(i_bin);
							temp_b += (img_raw.getRGB(i + g, j + h) & 0xff)
									* kernel.elementAt(i_bin);
						}
					}
				}
				temp_r *= (double) 1 / (double) weight;
				temp_g *= (double) 1 / (double) weight;
				temp_b *= (double) 1 / (double) weight;
				if (temp_r < 0)
					temp_r = 0;
				if (temp_r > 255)
					temp_r = 255;
				if (temp_g < 0)
					temp_g = 0;
				if (temp_g > 255)
					temp_g = 255;
				if (temp_b < 0)
					temp_b = 0;
				if (temp_b > 255)
					temp_b = 255;
				if (temp_r - Math.floor(temp_r) >= 0.5)
					temp_r++;
				if (temp_g - Math.floor(temp_g) >= 0.5)
					temp_g++;
				if (temp_b - Math.floor(temp_b) >= 0.5)
					temp_b++;
				img_flt.setRGB(i, j, new Color((int) temp_r, (int) temp_g,
						(int) temp_b).getRGB());
			}
		}
		return img_flt;
	}
}
