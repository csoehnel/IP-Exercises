package de.soehnel.christoph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Histogram {

	private static final long serialVersionUID = 1L;

	protected BufferedImage histogram_bimage = new BufferedImage(320, 280,
			BufferedImage.TYPE_INT_RGB);

	private int[] histogram;

	private float[] hsi_vector;

	public Histogram() {
		histogram = new int[256];
		hsi_vector = new float[3];
		for (int i = 0; i < 256; i++)
			histogram[i] = 0;
		for (int i = 0; i < 3; i++)
			hsi_vector[i] = 0;
	}

	// Auf log2 skaliertes Histogramm visualisieren
	public BufferedImage getHistogram(BufferedImage bimage, int mode,
			Color scale, boolean intensity, boolean akku) {
		Graphics2D gfx = histogram_bimage.createGraphics();
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, 320, 280);
		gfx.setColor(Color.BLACK);

		if (intensity)
			generateIntensityHistogram(bimage, akku);
		else
			generateHistogram(bimage, mode, akku);

		// Skalieren auf Log2
		double max = 0;
		double scf = 0;
		double logfarb = 0;
		double maxtick = 0;
		double midtick = 0;

		for (int i = 0; i < 256; i++)
			if (histogram[i] > max)
				max = histogram[i];
		max = log2(max);
		scf = max / 250.;

		maxtick = ((int) max) / scf;
		if (maxtick - ((int) maxtick) >= 0.5)
			maxtick = Math.round(maxtick);
		else
			maxtick = (int) maxtick;

		midtick = ((int) (max / 2)) / scf;
		if (midtick - ((int) midtick) >= 0.5)
			midtick = Math.round(midtick);
		else
			midtick = (int) midtick;

		gfx.setColor(Color.BLACK);

		// Skala vertikal
		gfx.drawLine(50, 260, 50, 10);
		gfx.drawLine(46, 260 - (int) maxtick, 54, 260 - (int) maxtick);
		gfx.drawString("2^" + (int) max, 10, 260 - (int) maxtick + 5);
		gfx.drawLine(46, 260 - (int) midtick, 54, 260 - (int) midtick);
		gfx.drawString("2^" + (int) (max / 2), 10, 260 - (int) midtick + 5);
		gfx.drawLine(46, 260, 54, 260);
		gfx.drawString("0", 10, 265);

		gfx.setColor(scale);

		for (int i = 0; i < 256; i++) {
			logfarb = (log2(histogram[i]) / scf);
			if (logfarb < 0)
				logfarb = 0;
			if (logfarb - ((int) logfarb) >= 0.5)
				logfarb = Math.round(logfarb);
			else
				logfarb = (int) logfarb;
			gfx.drawLine(50 + i, 260, 50 + i, (int) (260. - (logfarb)));
		}

		gfx.setColor(Color.BLACK);

		// Skala horizontal
		gfx.drawLine(50, 260, 305, 260);
		gfx.drawLine(50, 256, 50, 264);
		gfx.drawString("0", 47, 276);
		gfx.drawLine(177, 256, 177, 264);
		gfx.drawString("127", 167, 276);
		gfx.drawLine(305, 256, 305, 264);
		gfx.drawString("255", 295, 276);

		return histogram_bimage;
	}

	// Skalierten LUT visualisieren
	public BufferedImage visualizeLUT(int[] lut) {
		BufferedImage lut_bimage = new BufferedImage(320, 280,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D gfx = lut_bimage.createGraphics();
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, 320, 280);

		gfx.setColor(Color.BLACK);

		// Skala vertikal
		gfx.drawLine(50, 260, 50, 5);
		gfx.drawLine(46, 260 - (int) 255, 54, 260 - (int) 255);
		gfx.drawString("255", 10, 260 - (int) 255 + 5);
		gfx.drawLine(46, 260 - (int) 127, 54, 260 - (int) 127);
		gfx.drawString("127", 10, 260 - (int) 127 + 5);
		gfx.drawLine(46, 260, 54, 260);
		gfx.drawString("0", 10, 265);

		gfx.setColor(Color.ORANGE);
		for (int i = 0; i < 256; i++)
			gfx.drawLine(50 + i, 260, 50 + i, (int) (260. - (lut[i])));

		gfx.setColor(Color.BLACK);

		// Skala horizontal
		gfx.drawLine(50, 260, 305, 260);
		gfx.drawLine(50, 256, 50, 264);
		gfx.drawString("0", 47, 276);
		gfx.drawLine(177, 256, 177, 264);
		gfx.drawString("127", 167, 276);
		gfx.drawLine(305, 256, 305, 264);
		gfx.drawString("255", 295, 276);

		return lut_bimage;
	}

	// Logarithmus Duales
	public double log2(double wert) {
		return (Math.log10(wert) / Math.log10(2));
	}

	// (Akkumuliertes) Histogramm berechnen (RGB)
	public void generateHistogram(BufferedImage bimage, int mode, boolean akku) {
		for (int y = 0; y < bimage.getHeight(); y++)
			for (int x = 0; x < bimage.getWidth(); x++) {
				if (mode == 0)
					histogram[(int) (bimage.getRGB(x, y) & 0xff)]++;
				if (mode == 1)
					histogram[(int) ((bimage.getRGB(x, y) >> 16) & 0xff)]++;
				if (mode == 2)
					histogram[(int) ((bimage.getRGB(x, y) >> 8) & 0xff)]++;
			}
		if (akku)
			for (int i = 0; i < 256; i++)
				if (i > 0)
					histogram[i] += histogram[i - 1];
	}

	// (Akkumuliertes) Intensitätshistogramm berechnen (HSI)
	public void generateIntensityHistogram(BufferedImage bimage, boolean akku) {
		int intensity = 0;
		float intens = 0;

		for (int i = 0; i < 256; i++)
			histogram[i] = 0;
		for (int i = 0; i < 3; i++)
			hsi_vector[i] = 0;

		for (int y = 0; y < bimage.getHeight(); y++)
			for (int x = 0; x < bimage.getWidth(); x++) {
				int red = (int) ((bimage.getRGB(x, y) >> 16) & 0xff);
				int green = (int) ((bimage.getRGB(x, y) >> 8) & 0xff);
				int blue = (int) (bimage.getRGB(x, y) & 0xff);
				Color.RGBtoHSB(red, green, blue, hsi_vector);
				intens = hsi_vector[2] * (float) 255;
				intensity = (int) Math.round(intens);
				histogram[intensity]++;
			}
		if (akku)
			for (int i = 0; i < 256; i++)
				if (i > 0)
					histogram[i] += histogram[i - 1];
	}

	// Histogrammvektor zurückgeben
	public int[] getHistogramValues() {
		return histogram;
	}

	// Finden des geringsten Anstiegs
	public Vector<Integer> getSmallestGrowing(int[] farbwerte) {
		Vector<Integer> growing = new Vector<Integer>();
		int anstieg = 9999;
		for (int i = 0; i < 255; i++)
			if (Math.abs(farbwerte[i + 1] - farbwerte[i]) <= anstieg
					&& (farbwerte[i + 1] != 0 || farbwerte[i] != 0))
				anstieg = Math.abs(farbwerte[i + 1] - farbwerte[i]);
		for (int i = 0; i < 255; i++)
			if (Math.abs(farbwerte[i + 1] - farbwerte[i]) == anstieg)
				growing.add(i);
		return growing;
	}
}
