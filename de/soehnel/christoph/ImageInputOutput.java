package de.soehnel.christoph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;

public class ImageInputOutput {

	ImageRAWDialog imgrdlg = null;

	public ImageInputOutput(JFrame parent) {
		imgrdlg = new ImageRAWDialog(parent, "RAW-Bild: Einstellungen", true);
		imgrdlg.setSize(300, 150);
		imgrdlg.setLocation(200, 200);
	}

	public BufferedImage loadImage(File imgfile) {
		BufferedImage image = null;
		if (imgfile != null) {
			if (imgfile.getName().endsWith(".raw"))
				image = loadRAW(imgfile.toString());
			else
				image = toBufferedImage(Jimi.getImage(imgfile.toString()));
		}
		return image;
	}

	public void saveImage(File imgfile, BufferedImage image) {
		if (imgfile != null) {
			if (imgfile.getName().endsWith(".raw")) {
				saveRAW(image, imgfile.toString());
			} else {
				try {
					Jimi.putImage(image, imgfile.toString());
				} catch (JimiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public BufferedImage loadRAW(String filename) {
		BufferedImage bimage = null;
		RandomAccessFile readfile;
		int gray, r, g, b;
		try {
			readfile = new RandomAccessFile(filename, "r");
			imgrdlg.setDlgDim((int) Math.sqrt(readfile.length()));
			imgrdlg.setVisible(true);
			bimage = new BufferedImage(imgrdlg.getWidth(), imgrdlg.getHeight(),
					BufferedImage.TYPE_INT_RGB);

			for (int j = 0; j < imgrdlg.getHeight(); j++)
				for (int i = 0; i < imgrdlg.getWidth(); i++)
					if (imgrdlg.getMode() == 1) {
						gray = readfile.readByte();
						if (gray < 0)
							gray += Math.pow(2, 8);
						bimage.setRGB(i, j, new Color(gray, gray, gray)
								.getRGB());
					} else {
						r = readfile.readByte();
						g = readfile.readByte();
						b = readfile.readByte();
						if (r < 0)
							r += Math.pow(2, 8);
						if (g < 0)
							g += Math.pow(2, 8);
						if (b < 0)
							b += Math.pow(2, 8);
						bimage.setRGB(i, j, new Color(r, g, b).getRGB());
					}
		} catch (IOException e) {
		}
		return bimage;
	}

	public void saveRAW(BufferedImage bimage, String filename) {
		RandomAccessFile writefile = null;
		boolean isgray = is1ByteGray(bimage);
		try {
			writefile = new RandomAccessFile(filename, "rw");
			for (int j = 0; j < bimage.getHeight(); j++) {
				for (int i = 0; i < bimage.getWidth(); i++) {
					if (isgray)
						writefile.writeByte(bimage.getRGB(i, j));
					else {
						writefile.writeByte((bimage.getRGB(i, j) >> 16) & 0xff);
						writefile.writeByte((bimage.getRGB(i, j) >> 8) & 0xff);
						writefile.writeByte((bimage.getRGB(i, j)) & 0xff);
					}
				}
			}
			writefile.close();
		} catch (IOException e) {
		}
	}

	public static boolean is1ByteGray(BufferedImage bimage) {
		boolean isgray = true;
		int rgb;
		for (int j = 0; j < bimage.getHeight(); j++) {
			for (int i = 0; i < bimage.getWidth() && isgray; i++) {
				rgb = bimage.getRGB(i, j);
				if (((rgb >> 8) & 0xff) != (rgb & 0xff))
					isgray = false;
			}
		}
		return isgray;
	}

	public BufferedImage toBufferedImage(Image image) {
		// This code ensures that all the pixels in
		// the image are loaded.
		image = new ImageIcon(image).getImage();

		// Create the buffered image.
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics();

		// Clear background and paint the image.
		g.setColor(Color.white);
		g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bufferedImage;
	}

	public BufferedImage to1ByteGray(BufferedImage bimage) {
		BufferedImage grayImage = new BufferedImage(bimage.getWidth(), bimage
				.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < bimage.getHeight(); j++)
			for (int i = 0; i < bimage.getWidth(); i++) {
				double gray = (0.299 * ((bimage.getRGB(i, j) >> 16) & 0xff))
						+ (0.587 * ((bimage.getRGB(i, j) >> 8) & 0xff))
						+ (0.114 * (bimage.getRGB(i, j) & 0xff));
				if (gray - (int) gray >= 0.5)
					gray = Math.round(gray);
				grayImage.setRGB(i, j, new Color((int) gray, (int) gray,
						(int) gray).getRGB());
			}
		return grayImage;
	}
}
