package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class LaplaceFilter {

	private int x;

	private int y;

	private int winwidth;

	private Vector<Integer> kernel;

	private BufferedImage img_flt;

	public LaplaceFilter(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void initFilter(int winwidth) {
		this.winwidth = winwidth;
		kernel = new Vector<Integer>();
		for (int i = 0; i < (winwidth * winwidth); i++)
			kernel.add(-1);
		int center = (winwidth * winwidth) - 1;
		kernel.setElementAt(center, (winwidth * ((int) winwidth / 2))
				+ ((int) winwidth / 2));
	}

	public BufferedImage applicateFilter(BufferedImage img_raw) {
		img_flt = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		double bfcnt, temp_r, temp_g, temp_b;
		int i_bin;
		int ffc = winwidth / 2;
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				bfcnt = 0;
				for (int h = -ffc; h <= ffc; h++) {
					for (int g = -ffc; g <= ffc; g++) {
						i_bin = winwidth * (h + (Math.abs(ffc))) + g
								+ Math.abs(ffc);
						if (j + h >= 0 && j + h < y && i + g >= 0 && i + g < x)
							bfcnt++;
					}
				}
				// bfcnt = 1 / bfcnt;
				// int center = (int) bfcnt;
				// kernel.setElementAt(center, (winwidth * ((int) winwidth / 2))
				// + ((int) winwidth / 2));
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
				// wichtung m^2 oder sum of positive values in kernel
				// Abs oder + 127
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
