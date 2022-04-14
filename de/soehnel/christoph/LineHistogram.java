package de.soehnel.christoph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

public class LineHistogram {
	protected BufferedImage lhistogram = null;

	public Vector<Integer> pixel = null;

	public LineHistogram() {
		pixel = new Vector<Integer>();
	}

	public BufferedImage getLineHistogram(BufferedImage bimage, int x1, int y1,
			int x2, int y2, int mode, Color gcol) {
		int temp = 0;

		boolean switchedxyaxis = false;

		// Tauschen von x1/x2 und y1/y2
		if ((x1 >= x2/* && y1 > y2)|| (x1 > x2 && y2 > y1 */)) {
			temp = x1;
			x1 = x2;
			x2 = temp;

			temp = y1;
			y1 = y2;
			y2 = temp;
		}

		// Tauschen der Achsen
		if (Math.abs((y1 - y2)) > (x2 - x1)) {
			temp = x1;
			x1 = y1;
			y1 = temp;

			temp = x2;
			x2 = y2;
			y2 = temp;

			if ((x1 >= x2/* && y1 > y2) || (x1 > x2 && y2 > y1 */)) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			switchedxyaxis = true;
		}

		int loc_width = Math.abs(x2 - x1) + 64;
		int loc_height = 256 + 30;
		lhistogram = new BufferedImage(loc_width, loc_height,
				BufferedImage.TYPE_INT_RGB);

		Graphics2D gfx = lhistogram.createGraphics();
		gfx.setColor(Color.WHITE);
		gfx.fillRect(0, 0, loc_width, loc_height);
		gfx.setColor(gcol);

		// Geradengleichung Anfang
		/*
		 * double m = (y2 - y1) / (x2 - x1); double n = y1 - (m * x1); int k =
		 * 0; for (int i = x1; i <= x2; i++) { int j = (int) m * i + (int) n;
		 * //if((j - (int) j) >= 0.5) //j++; pixel.insertElementAt((int)
		 * (bimage.getRGB(i, j) & 0xff), k++); }
		 */
		// Geradengleichung Ende
		
		// Bresenham Anfang
		double s = Math.abs((double) (y2 - y1) / (double) (x2 - x1));
		double error = 0.0;
		int x = x1;
		int y = y1;

		int k = 0;

		int read_x = 0;
		int read_y = 0;

		while (x <= x2) {
			if (switchedxyaxis) {
				read_x = y;
				read_y = x;
			} else {
				read_x = x;
				read_y = y;
			}

			// bimage.setRGB(read_x, read_y, new Color(255, 0, 0).getRGB());
			if (mode == 0) {
				pixel.insertElementAt(
						(int) (bimage.getRGB(read_x, read_y) & 0xff), k++);
			}
			if (mode == 1)
				pixel.insertElementAt(
						(int) ((bimage.getRGB(read_x, read_y) >> 16) & 0xff),
						k++);
			if (mode == 2)
				pixel.insertElementAt(
						(int) ((bimage.getRGB(read_x, read_y) >> 8) & 0xff),
						k++);
			x++;
			error = error + s;
			if (error > 0.5) {
				if (y2 < y1)
					y--;
				else
					y++;
				error = error - 1.0;
			}
		}
		// Bresenham Ende

		int i = 0;
		int last_x = 0;
		int last_y = 0;

		boolean first = true;

		for (Enumeration el = pixel.elements(); el.hasMoreElements();) {
			int wert = (Integer) el.nextElement();
			if (first) {
				gfx.drawLine(40 + i, loc_height - 20 - wert, 40 + i, loc_height
						- 20 - wert);
				first = false;
			} else
				gfx.drawLine(last_x, last_y, 40 + i, loc_height - 20 - wert);
			last_x = 40 + i;
			last_y = loc_height - 20 - wert;
			i++;
		}

		gfx.setColor(Color.BLACK);
		// Skala horizontal
		gfx.drawLine(40, loc_height - 20, last_x, loc_height - 20);
		gfx.drawLine(40, loc_height - 20, 40, loc_height - 16);
		gfx.drawString(Integer.toString(x1), 32, loc_height - 5);
		gfx.drawLine(last_x, loc_height - 20, last_x, loc_height - 16);
		gfx.drawString(Integer.toString(x2), last_x - 8, loc_height - 5);

		// Skala vertikal
		gfx.drawLine(40, loc_height - 20, 40, loc_height - 20 - 256);
		gfx.drawLine(36, loc_height - 20, 44, loc_height - 20);
		gfx.drawString("0", 15, loc_height - 16);
		gfx.drawLine(36, loc_height - 20 - 256, 44, loc_height - 20 - 256);
		gfx.drawString("255", 15, loc_height - 16 - 256);

		return lhistogram;
	}
}
