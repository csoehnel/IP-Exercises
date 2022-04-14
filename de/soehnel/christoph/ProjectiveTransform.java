package de.soehnel.christoph;

import java.awt.Point;
import java.awt.image.BufferedImage;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class ProjectiveTransform {

	private int x;

	private int y;

	private BufferedImage imgTrf;

	public ProjectiveTransform(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Todo: Checken, ob isotrope Normierung und invertierte proj.
	// Transformation wirklich korrekt sind. Evtl. klaeren, warum SVD(A) nicht
	// immer
	// V mit Nullraum von A liefert.
	public BufferedImage applicateProjTrafo(BufferedImage img_in, Point p1,
			Point p2, Point p3, Point p4) {
		imgTrf = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
		double x1 = 0;
		double y1 = 0;
		double x2 = x - 1;
		double y2 = 0;
		double x3 = 0;
		double y3 = y - 1;
		double x4 = x - 1;
		double y4 = y - 1;
		double x1s = p1.getX();
		double y1s = p1.getY();
		double x2s = p2.getX();
		double y2s = p2.getY();
		double x3s = p3.getX();
		double y3s = p3.getY();
		double x4s = p4.getX();
		double y4s = p4.getY();
		double avgX_In = (x1s + x2s + x3s + x4s) / 4.0;
		double avgY_In = (y1s + y2s + y3s + y4s) / 4.0;
		double avgX_Tf = (x1 + x2 + x3 + x4) / 4.0;
		double avgY_Tf = (y1 + y2 + y3 + y4) / 4.0;
		// Mittelwertbefreiung
		x1 = x1 - avgX_Tf;
		y1 = y1 - avgY_Tf;
		x2 = x2 - avgX_Tf;
		y2 = y2 - avgY_Tf;
		x3 = x3 - avgX_Tf;
		y3 = y3 - avgY_Tf;
		x4 = x4 - avgX_Tf;
		y4 = y4 - avgY_Tf;
		x1s = x1s - avgX_In;
		y1s = y1s - avgY_In;
		x2s = x2s - avgX_In;
		y2s = y2s - avgY_In;
		x3s = x3s - avgX_In;
		y3s = y3s - avgY_In;
		x4s = x4s - avgX_In;
		y4s = y4s - avgY_In;
		double isoScaleX_In = ((Math.abs(x1s) + Math.abs(x2s) + Math.abs(x3s) + Math
				.abs(x4s)) / 4.0) / Math.sqrt(2.0);
		double isoScaleY_In = ((Math.abs(y1s) + Math.abs(y2s) + Math.abs(y3s) + Math
				.abs(y4s)) / 4.0) / Math.sqrt(2.0);
		double isoScaleX_Tf = ((Math.abs(x1) + Math.abs(x2) + Math.abs(x3) + Math
				.abs(x4)) / 4.0) / Math.sqrt(2.0);
		double isoScaleY_Tf = ((Math.abs(y1) + Math.abs(y2) + Math.abs(y3) + Math
				.abs(y4)) / 4.0) / Math.sqrt(2.0);
		// Isotrope Normierung
		x1 = x1 / isoScaleX_Tf;
		y1 = y1 / isoScaleY_Tf;
		x2 = x2 / isoScaleX_Tf;
		y2 = y2 / isoScaleY_Tf;
		x3 = x3 / isoScaleX_Tf;
		y3 = y3 / isoScaleY_Tf;
		x4 = x4 / isoScaleX_Tf;
		y4 = y4 / isoScaleY_Tf;
		x1s = x1s / isoScaleX_In;
		y1s = y1s / isoScaleY_In;
		x2s = x2s / isoScaleX_In;
		y2s = y2s / isoScaleY_In;
		x3s = x3s / isoScaleX_In;
		y3s = y3s / isoScaleY_In;
		x4s = x4s / isoScaleX_In;
		y4s = y4s / isoScaleY_In;
		// SVD vorbereiten
		Matrix A = new Matrix(8, 9);
		A.set(0, 0, x1);
		A.set(0, 1, y1);
		A.set(0, 2, 1.0);
		A.set(0, 6, -x1s * x1);
		A.set(0, 7, -x1s * y1);
		A.set(0, 8, -x1s);
		A.set(1, 3, x1);
		A.set(1, 4, y1);
		A.set(1, 5, 1.0);
		A.set(1, 6, -y1s * x1);
		A.set(1, 7, -y1s * y1);
		A.set(1, 8, -y1s);
		A.set(2, 0, x2);
		A.set(2, 1, y2);
		A.set(2, 2, 1.0);
		A.set(2, 6, -x2s * x2);
		A.set(2, 7, -x2s * y2);
		A.set(2, 8, -x2s);
		A.set(3, 3, x2);
		A.set(3, 4, y2);
		A.set(3, 5, 1.0);
		A.set(3, 6, -y2s * x2);
		A.set(3, 7, -y2s * y2);
		A.set(3, 8, -y2s);
		A.set(4, 0, x3);
		A.set(4, 1, y3);
		A.set(4, 2, 1.0);
		A.set(4, 6, -x3s * x3);
		A.set(4, 7, -x3s * y3);
		A.set(4, 8, -x3s);
		A.set(5, 3, x3);
		A.set(5, 4, y3);
		A.set(5, 5, 1.0);
		A.set(5, 6, -y3s * x3);
		A.set(5, 7, -y3s * y3);
		A.set(5, 8, -y3s);
		A.set(6, 0, x4);
		A.set(6, 1, y4);
		A.set(6, 2, 1.0);
		A.set(6, 6, -x4s * x4);
		A.set(6, 7, -x4s * y4);
		A.set(6, 8, -x4s);
		A.set(7, 3, x4);
		A.set(7, 4, y4);
		A.set(7, 5, 1.0);
		A.set(7, 6, -y4s * x4);
		A.set(7, 7, -y4s * y4);
		A.set(7, 8, -y4s);
		// SVD durchfuehren
		SingularValueDecomposition svd = A.svd();
		Matrix V = svd.getV();
		Matrix h = V.getMatrix(0, 8, 8, 8);
		// Zielbild zeichnen
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				// x-Koordinate im Ursprungsbild passend zur x-Koordinate im
				// Zielbild berechnen
				int iOrig = (int) Math.round((avgX_In + (isoScaleX_In * ((h
						.get(0, 0)
						* ((i - avgX_Tf) / isoScaleX_Tf)
						+ h.get(1, 0) * ((j - avgY_Tf) / isoScaleY_Tf) + h.get(
						2, 0)) / (h.get(6, 0) * ((i - avgX_Tf) / isoScaleX_Tf)
						+ h.get(7, 0) * ((j - avgY_Tf) / isoScaleY_Tf) + h.get(
						8, 0))))));
				// y-Koordinate im Ursprungsbild passend zur y-Koordinate im
				// Zielbild berechnen
				int jOrig = (int) Math.round((avgY_In + (isoScaleY_In * ((h
						.get(3, 0)
						* ((i - avgX_Tf) / isoScaleX_Tf)
						+ h.get(4, 0) * ((j - avgY_Tf) / isoScaleY_Tf) + h.get(
						5, 0)) / (h.get(6, 0) * ((i - avgX_Tf) / isoScaleX_Tf)
						+ h.get(7, 0) * ((j - avgY_Tf) / isoScaleY_Tf) + h.get(
						8, 0))))));
				int rgbOrig = 0;
				if (iOrig >= 0 && iOrig < x && jOrig >= 0 && jOrig < y) {
					rgbOrig = img_in.getRGB(iOrig, jOrig);
				}
				imgTrf.setRGB(i, j, rgbOrig);
			}
		}

		return imgTrf;
	}
}