package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

public class MedianFilter {

	private int x;

	private int y;

	private BufferedImage img_med;
	
	private Vector<Integer> pixel_r;

	private Vector<Integer> pixel_g;

	private Vector<Integer> pixel_b;

	public MedianFilter(int x, int y) {
		this.x = x;
		this.y = y;
		pixel_r = new Vector<Integer>();
		pixel_g = new Vector<Integer>();
		pixel_b = new Vector<Integer>();
	}

	// Filter anwenden
	public BufferedImage applicateFilter(BufferedImage bimage, int winwidth) {
		img_med = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		double temp_r = 0.;
		double temp_g = 0.;
		double temp_b = 0.;
		int ffc = (int) Math.floor((double) winwidth / 2.);
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				if (j - ffc >= 0 && j + ffc < y && i - ffc >= 0 && i + ffc < x) {
					for (int h = j - ffc; h <= j + ffc; h++) {
						for (int g = i - ffc; g <= i + ffc; g++) {
							pixel_r.add((bimage.getRGB(g, h) >> 16) & 0xff);
							pixel_g.add((bimage.getRGB(g, h) >> 8) & 0xff);
							pixel_b.add(bimage.getRGB(g, h) & 0xff);
						}
					}
					Collections.sort(pixel_r);
					Collections.sort(pixel_g);
					Collections.sort(pixel_b);
					if (pixel_r.size() % 2 > 0) {
						temp_r = pixel_r.get(pixel_r.lastIndexOf(pixel_r
								.lastElement()) / 2);
						temp_g = pixel_g.get(pixel_g.lastIndexOf(pixel_g
								.lastElement()) / 2);
						temp_b = pixel_b.get(pixel_b.lastIndexOf(pixel_b
								.lastElement()) / 2);
					} else {
						temp_r = Math
								.round((pixel_r.get((int) Math
										.floor((double) pixel_r
												.lastIndexOf(pixel_r
														.lastElement()) / 2.)) + pixel_r
										.get((int) Math.ceil((double) pixel_r
												.lastIndexOf(pixel_r
														.lastElement()) / 2.))) / 2.);
						temp_g = Math
								.round((pixel_g.get((int) Math
										.floor((double) pixel_g
												.lastIndexOf(pixel_g
														.lastElement()) / 2.)) + pixel_g
										.get((int) Math.ceil((double) pixel_g
												.lastIndexOf(pixel_g
														.lastElement()) / 2.))) / 2.);
						temp_b = Math
								.round((pixel_b.get((int) Math
										.floor((double) pixel_b
												.lastIndexOf(pixel_b
														.lastElement()) / 2.)) + pixel_b
										.get((int) Math.ceil((double) pixel_b
												.lastIndexOf(pixel_b
														.lastElement()) / 2.))) / 2.);
					}
					img_med.setRGB(i, j, new Color((int) temp_r, (int) temp_g,
							(int) temp_b).getRGB());
					pixel_r.clear();
					pixel_g.clear();
					pixel_b.clear();
				}
			}
		}
		return img_med;
	}
}