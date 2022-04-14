package de.soehnel.christoph;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Histogram2EnhancedImage {

	private int[] histogram;

	private int[] lut;

	private int max_histo_value;

	public Histogram2EnhancedImage(int[] histogram) {
		this.histogram = histogram;
		this.lut = new int[256];
		for (int i = 0; i <= 255; i++)
			lut[i] = 0;
		max_histo_value = 0;
		searchMaxValue();
	}

	// Graubild ausgleichen
	public BufferedImage enhanceImage(BufferedImage bimage) {
		BufferedImage enhancedImage = new BufferedImage(bimage.getWidth(),
				bimage.getHeight(), BufferedImage.TYPE_INT_RGB);
		generateLUT(histogram, lut, max_histo_value);
		for (int j = 0; j < bimage.getHeight(); j++)
			for (int i = 0; i < bimage.getWidth(); i++) {
				int gray = lut[(int) ((bimage.getRGB(i, j) >> 16) & 0xff)];
				enhancedImage
						.setRGB(i, j, new Color(gray, gray, gray).getRGB());
			}
		return enhancedImage;
	}

	// RGB-Bild ausgleichen (use HSI)
	public BufferedImage enhanceRGBImage(BufferedImage bimage) {
		float[] hsi_vector = new float[3];
		float intens = 0f;
		int intensity = 0;
		BufferedImage enhancedImage = new BufferedImage(bimage.getWidth(),
				bimage.getHeight(), BufferedImage.TYPE_INT_RGB);
		generateLUT(histogram, lut, max_histo_value);
		for (int j = 0; j < bimage.getHeight(); j++)
			for (int i = 0; i < bimage.getWidth(); i++) {
				for (int p = 0; p < 3; p++)
					hsi_vector[p] = 0;
				intensity = 0;
				intens = 0f;
				Color.RGBtoHSB((int) ((bimage.getRGB(i, j) >> 16) & 0xff),
						(int) ((bimage.getRGB(i, j) >> 8) & 0xff),
						(int) (bimage.getRGB(i, j) & 0xff), hsi_vector);
				intens = hsi_vector[2] * 255f;
				intensity = (int) Math.round(intens);
				intensity = lut[intensity];
				intens = ((float) intensity) / 255f;
				enhancedImage.setRGB(i, j, Color.HSBtoRGB(hsi_vector[0],
						hsi_vector[1], intens));
			}
		return enhancedImage;
	}

	// Maximalen Histogrammwert suchen
	public void searchMaxValue() {
		max_histo_value = 0;
		for (int i = 0; i <= 255; i++)
			if (histogram[i] > max_histo_value)
				max_histo_value = histogram[i];
	}

	// Look-Up-Table erstellen
	public void generateLUT(int[] farbwerte, int[] lut, int max) {
		double temp = 0.;
		double scf = ((double) max) / ((double) 255);
		for (int i = 0; i <= 255; i++) {
			temp = ((double) farbwerte[i]) / scf;
			lut[i] = (int) Math.round(temp);
		}
	}

	public int[] getLUT() {
		return lut;
	}

}
