package de.soehnel.christoph;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

public class BinaryImage {

	private static final long serialVersionUID = 1L;

	private BufferedImage binaryImage;

	private GlobalDynamicThreshold gdt;

	private CMeans cm;

	public BinaryImage() {
	}

	// Binarisierung
	public BufferedImage binarizeImage(BufferedImage bimage, int threshold) {
		binaryImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < bimage.getHeight(); j++)
			for (int i = 0; i < bimage.getWidth(); i++) {
				if ((bimage.getRGB(i, j) & 0xff) >= threshold)
					binaryImage.setRGB(i, j, new Color(255, 255, 255).getRGB());
				else
					binaryImage.setRGB(i, j, new Color(0, 0, 0).getRGB());
			}
		return binaryImage;
	}

	// Binarisierung mittels Globalem Dynamischen Schwellwert
	public BufferedImage binarizeImageGDT(BufferedImage bimage, int r,
			int neighbours, int overlap) {
		binaryImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		int gray = 0;
		double threshold = 0;
		Vector<Integer> pixelstream = new Vector<Integer>();
		gdt = new GlobalDynamicThreshold(r);
		for (int j = 0; j < bimage.getHeight(); j++) {
			for (int i = 0; i < bimage.getWidth(); i += 1 + overlap) {
				// Zero-Padding
				if (i - neighbours >= 0 && i + neighbours < bimage.getWidth()) {
					for (int p = i - neighbours; p <= i + neighbours; p++) {
						if (p != i) {
							gray = bimage.getRGB(p, j) & 0xff;
							pixelstream.addElement(bimage.getRGB(p, j) & 0xff);
						}
					}
					threshold = gdt.getGDThreshold(pixelstream);
					for (int p = i - neighbours; p <= i + neighbours; p++) {
						gray = bimage.getRGB(p, j) & 0xff;
						if (gray >= threshold)
							gray = 255;
						else
							gray = 0;
						binaryImage.setRGB(p, j, new Color(gray, gray, gray)
								.getRGB());
					}
					pixelstream.clear();
				}
			}
		}
		return binaryImage;
	}

	// Binarisierung mittels C-Means-Algorithmus
	public BufferedImage binarizeImageCMeans(BufferedImage bimage,
			int num_clusters, int error_thresh) {
		int class0gray, class1gray;
		binaryImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		cm = new CMeans(bimage, 2, 0);
		HashMap<Point, Integer> hm = cm.getCluster();
		HashMap<Integer, Integer> centers = cm.getCenters();
		if (centers.get(0) > centers.get(1)) {
			class0gray = 255;
			class1gray = 0;
		} else {
			class0gray = 0;
			class1gray = 255;
		}
		for (int k = 0; k < 2; k++) {
			for (int j = 0; j < bimage.getHeight(); j++) {
				for (int i = 0; i < bimage.getWidth(); i++) {
					if ((Integer) hm.get(new Point(i, j)) == 0)
						binaryImage.setRGB(i, j, new Color(class0gray,
								class0gray, class0gray).getRGB());
					else
						binaryImage.setRGB(i, j, new Color(class1gray,
								class1gray, class1gray).getRGB());
				}
			}
		}
		return binaryImage;
	}

	// Gefensterte Binarisierung mittels C-Means-Algorithmus
	public BufferedImage binarizeImageWindowed(BufferedImage bimage, int m,
			int n, int num_clusters, int error_thresh) {
		int class0gray, class1gray;
		BufferedImage partition = new BufferedImage(m, n,
				BufferedImage.TYPE_INT_RGB);
		binaryImage = new BufferedImage(bimage.getWidth(), bimage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		HashMap<Point, Integer> cluster = null;
		HashMap<Integer, Integer> centers = null;
		for (int l = 0; l < bimage.getHeight() - n; l += n) {
			for (int k = 0; k < bimage.getWidth() - m; k += m) {
				for (int j = l % n; j < n; j++) {
					for (int i = k % m; i < m; i++) {
						partition.setRGB(i, j, new Color(bimage.getRGB(k + i, l
								+ j)).getRGB());
					}
				}
				cm = new CMeans(partition, num_clusters, error_thresh);
				cluster = cm.getCluster();
				centers = cm.getCenters();
				if (centers.get(0) > centers.get(1)) {
					class0gray = 255;
					class1gray = 0;
				} else {
					class0gray = 0;
					class1gray = 255;
				}
				for (int j = l % n; j < n; j++) { // richtige koordinaten
					for (int i = k % m; i < m; i++) {
						if ((Integer) cluster.get(new Point(i, j)) == 0)
							binaryImage.setRGB(k + i, l + j, new Color(
									class0gray, class0gray, class0gray)
									.getRGB());
						else
							binaryImage.setRGB(k + i, l + j, new Color(
									class1gray, class1gray, class1gray)
									.getRGB());
					}
				}
			}
		}
		return binaryImage;
	}
}
