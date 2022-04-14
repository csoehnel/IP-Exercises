package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelFilter {

	private int x;

	private int y;

	private int winwidth;

	private double sobel_x[];

	private double sobel_y[];

	private BufferedImage img_flt;

	public SobelFilter(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double lFCalc(int value) {
		double fac = 0;
		for (int i = value; i > 0; i--)
			fac += Math.log10((double) i);
		return fac;
	}

	public void initFilter(int winwidth) {
		int i_bin;
		double temp;
		this.winwidth = winwidth;
		sobel_x = new double[winwidth * winwidth];
		sobel_y = new double[winwidth * winwidth];
		if (winwidth >= 3 && winwidth % 2 == 1) {
			int ffc = winwidth / 2;
			int n = winwidth - 1;
			for (int l = -ffc; l <= ffc; l++)
				for (int k = -ffc; k <= ffc; k++) {
					i_bin = winwidth * (l + ffc) + k + ffc;
					temp = (lFCalc(n) - (lFCalc(n / 2 + k) + lFCalc(n / 2 - k)))
							+ (lFCalc(n) - (lFCalc(n / 2 + l) + lFCalc(n / 2
									- l)));
					temp = Math.pow((double) 10, temp);
					if (temp - Math.floor(temp) >= 0.5)
						temp++;
					if (l == 0)
						sobel_x[i_bin] = 0;
					else if (l > 0)
						sobel_x[i_bin] = (double) -temp;
					else
						sobel_x[i_bin] = (double) temp;
					if (k == 0)
						sobel_y[i_bin] = 0;
					else if (k > 0)
						sobel_y[i_bin] = (double) -temp;
					else
						sobel_y[i_bin] = (double) temp;
				}
		}
	}

	public BufferedImage applicateFilter(BufferedImage img_raw) {
		img_flt = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		double bfcnt, temp, temp_r, temp_g, temp_b, temp_r_x, temp_g_x, temp_b_x, temp_r_y, temp_g_y, temp_b_y;
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
				temp = temp_r = temp_g = temp_b = temp_r_x = temp_g_x = temp_b_x = temp_r_y = temp_g_y = temp_b_y = 0;
				for (int h = -ffc; h <= ffc; h++) {
					for (int g = -ffc; g <= ffc; g++) {
						i_bin = winwidth * (h + (Math.abs(ffc))) + g
								+ Math.abs(ffc);
						if (j + h >= 0 && j + h < y && i + g >= 0 && i + g < x) {
							temp = (img_raw.getRGB(i + g, j + h) >> 16) & 0xff;
							temp_r_x += temp * sobel_x[i_bin];
							temp_r_y += temp * sobel_y[i_bin];
							temp = (img_raw.getRGB(i + g, j + h) >> 8) & 0xff;
							temp_g_x += temp * sobel_x[i_bin];
							temp_g_y += temp * sobel_y[i_bin];
							temp = img_raw.getRGB(i + g, j + h) & 0xff;
							temp_b_x += temp * sobel_x[i_bin];
							temp_b_y += temp * sobel_y[i_bin];
						}
					}
				}
				temp_r_x = Math.abs(temp_r_x);
				temp_r_y = Math.abs(temp_r_y);
				temp_g_x = Math.abs(temp_g_x);
				temp_g_y = Math.abs(temp_g_y);
				temp_b_x = Math.abs(temp_b_x);
				temp_b_y = Math.abs(temp_b_y);
				if (temp_r_x > 255)
					temp_r_x = 255;
				if (temp_g_x > 255)
					temp_g_x = 255;
				if (temp_b_x > 255)
					temp_b_x = 255;
				if (temp_r_y > 255)
					temp_r_y = 255;
				if (temp_g_y > 255)
					temp_g_y = 255;
				if (temp_b_y > 255)
					temp_b_y = 255;
				temp_r = Math.max(temp_r_x, temp_r_y);
				temp_g = Math.max(temp_g_x, temp_g_y);
				temp_b = Math.max(temp_b_x, temp_b_y);
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
