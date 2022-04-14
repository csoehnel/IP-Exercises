package de.soehnel.christoph;

import java.util.Enumeration;
import java.util.Vector;

public class GlobalDynamicThreshold {

	private int r;

	public GlobalDynamicThreshold(int r) {
		this.r = r;
	}

	// Globalen dynamischen Schwellwert berechnen
	public double getGDThreshold(Vector<Integer> pixelstream) {
		return (double) r
				+ ((1. - ((double) r / 128.)) * getMittlererGrauwert(pixelstream));
	}

	// Mittleren Grauwert ermitteln
	private double getMittlererGrauwert(Vector<Integer> pixelstream) {
		Enumeration el = pixelstream.elements();
		int count = pixelstream.size();
		double c = 0;
		while (el.hasMoreElements()) {
			c += (Integer) el.nextElement();
		}
		c *= ((double) 1 / (double) count);
		return c;
	}
}
