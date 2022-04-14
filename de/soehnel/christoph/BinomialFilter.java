package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class BinomialFilter {
	private int x;

	private int y;

	private double binfil[];

	private BufferedImage img_smt;

	public BinomialFilter(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double lFCalc(int value) {
		double fac = 0;
		for (int i = value; i > 0; i--)
			fac += Math.log10((double) i);
		return fac;
	}

	// Gaußfilter initialisieren
	public void bfInit(int winwidth) {
		int i_bin;
		double temp;
		binfil = new double[winwidth * winwidth];
		if (winwidth >= 3 && winwidth % 2 == 1) {
			int ffc = winwidth / 2;
			int n = --winwidth;
			for (int l = -ffc; l <= ffc; l++)
				for (int k = -ffc; k <= ffc; k++) {
					i_bin = (winwidth + 1) * (l + ffc) + k + ffc;
					temp = (lFCalc(n) - (lFCalc(n / 2 + k) + lFCalc(n / 2 - k)))
							+ (lFCalc(n) - (lFCalc(n / 2 + l) + lFCalc(n / 2
									- l)));
					temp = Math.pow((double) 10, temp);
					if (temp - Math.floor(temp) >= 0.5)
						temp++;
					binfil[i_bin] = (double) temp;
				}
		}
	}

	// Anwenden
	public BufferedImage bfApp(BufferedImage img_raw, int winwidth) {
		img_smt = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
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
							bfcnt += binfil[i_bin];
					}
				}
				bfcnt = 1 / bfcnt;
				temp_r = temp_g = temp_b = 0;
				for (int h = -ffc; h <= ffc; h++) {
					for (int g = -ffc; g <= ffc; g++) {
						i_bin = winwidth * (h + (Math.abs(ffc))) + g
								+ Math.abs(ffc);
						if (j + h >= 0 && j + h < y && i + g >= 0 && i + g < x) {
							temp_r += ((img_raw.getRGB(i + g, j + h) >> 16) & 0xff)
									* binfil[i_bin];
							temp_g += ((img_raw.getRGB(i + g, j + h) >> 8) & 0xff)
									* binfil[i_bin];
							temp_b += (img_raw.getRGB(i + g, j + h) & 0xff)
									* binfil[i_bin];
						}
					}
				}
				temp_r *= bfcnt;
				temp_g *= bfcnt;
				temp_b *= bfcnt;
				if (temp_r - Math.floor(temp_r) >= 0.5)
					temp_r++;
				if (temp_g - Math.floor(temp_g) >= 0.5)
					temp_g++;
				if (temp_b - Math.floor(temp_b) >= 0.5)
					temp_b++;
				img_smt.setRGB(i, j, new Color((int) temp_r, (int) temp_g,
						(int) temp_b).getRGB());
			}
		}
		return img_smt;
	}
}
