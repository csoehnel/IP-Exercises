package de.soehnel.christoph;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class CMeans {

	private int num_clusters;

	private BufferedImage bimage;

	private HashMap<Integer, Integer> centers;

	private HashMap<Point, Integer> center_of_pixel;

	private int ck_iter_changed;

	private int error_thresh;

	private int iterations;

	public CMeans(BufferedImage bimage, int num_clusters, int error_thresh) {
		iterations = 0;
		this.bimage = bimage;
		this.num_clusters = num_clusters;
		this.error_thresh = error_thresh;
		this.centers = new HashMap<Integer, Integer>();
		this.center_of_pixel = new HashMap<Point, Integer>();
		this.ck_iter_changed = 0;
		setRandomCenters();
		do {
			iterations++;
			buildClusters();
			setCenters();
		} while (ck_iter_changed > this.error_thresh);
	}

	public void setRandomCenters() {
		int c = 0;
		for (int i = 0; i < num_clusters; i++) {
			c = (int) (Math.random() * (double) 255);
			centers.put(i, c);
		}
	}

	public void buildClusters() {
		int min_ck = 0;
		int min_k = 0;
		int temp = 0;
		for (int j = 0; j < bimage.getHeight(); j++) {
			for (int i = 0; i < bimage.getWidth(); i++) {
				for (int k = 0; k < num_clusters; k++) {
					temp = (int) Math.pow((bimage.getRGB(i, j) & 0xff)
							- centers.get(k), 2);
					if (k == 0 || temp < min_ck) {
						min_ck = temp;
						min_k = k;
					}
				}
				center_of_pixel.put(new Point(i, j), min_k);
			}
		}
	}

	public void setCenters() {
		int ck;
		int count;
		ck_iter_changed = 0;
		for (int k = 0; k < num_clusters; k++) {
			ck = 0;
			count = 0;
			for (int j = 0; j < bimage.getHeight(); j++) {
				for (int i = 0; i < bimage.getWidth(); i++) {
					if (center_of_pixel.get(new Point(i, j)) == k) {
						ck += bimage.getRGB(i, j) & 0xff;
						++count;
					}
				}
			}
			if (count > 0)
				ck /= count;
			if (Math.abs(centers.get(k) - ck) > ck_iter_changed)
				ck_iter_changed = Math.abs(centers.get(k) - ck);
			centers.put(k, ck);
		}
	}

	public HashMap<Point, Integer> getCluster() {
		return center_of_pixel;
	}

	public HashMap<Integer, Integer> getCenters() {
		return centers;
	}

	public int getNumIterations() {
		return iterations;
	}
}