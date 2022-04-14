package de.soehnel.christoph;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class HoughLines {

	private BufferedImage imgAccumulator;

	public HoughLines() {
		imgAccumulator = null;
	}

	public BufferedImage transform(BufferedImage imgInput) {
		int inputImgWidth = imgInput.getWidth();
		int inputImgHeight = imgInput.getHeight();
		int dimROffset = (int) Math.round(Math.sqrt(inputImgWidth
				* inputImgWidth + inputImgHeight * inputImgHeight));
		int accuWidth = 2 * dimROffset;
		int accuHeight = 181; // 0 to 180¡ (PI)
		int accumulator[] = new int[accuWidth * accuHeight];

		// init accumulator
		for (int j = 0; j < accuHeight; ++j) {
			for (int i = 0; i < accuWidth; ++i) {
				accumulator[j * accuWidth + i] = 0;
			}
		}

		// process input image, fill voting array
		int maxAccuValForNormalization = 0;
		for (int j = 0; j < inputImgHeight; ++j) {
			for (int i = 0; i < inputImgWidth; ++i) {
				int inputRGB_R = (imgInput.getRGB(i, j) >> 16) & 0xff;
				if (0 != inputRGB_R) {
					for (int angleDeg = 0; angleDeg < accuHeight; ++angleDeg) {
						double angleRad = Math.toRadians(angleDeg);
						int r = (int) Math.round(i * Math.cos(angleRad) + j
								* Math.sin(angleRad));
						accumulator[angleDeg * accuWidth + r + dimROffset]++;
						// For normalization at visualization only
						maxAccuValForNormalization = Math.max(
								maxAccuValForNormalization,
								accumulator[angleDeg * accuWidth + r
										+ dimROffset]);
					}
				}
			}
		}

		// prepare image for visualization
		double normalizationScale = 255.0 / maxAccuValForNormalization;
		imgAccumulator = new BufferedImage(accuWidth, accuHeight,
				BufferedImage.TYPE_INT_RGB);
		for (int j = 0; j < accuHeight; ++j) {
			for (int i = 0; i < accuWidth; ++i) {
				int visValue = (int) Math.round(accumulator[j * accuWidth + i]
						* normalizationScale);
				// int visValue = Math.min(accumulator[j * accuWidth + i], 255);
				imgAccumulator.setRGB(i, j, new Color(visValue, visValue,
						visValue).getRGB());
			}
		}

		// draw lines into source image, transform hnf into general line equation
		for (int i = 0; i < inputImgWidth; ++i) {
			for (int angleDeg = 0; angleDeg < accuHeight; ++angleDeg) {
				for (int r = 0; r < (2 * dimROffset); ++r) {
					if (accumulator[angleDeg * accuWidth + r] >= 600 /* TEST */) {
						double currentR = r - dimROffset;
						double angleRad = Math.toRadians(angleDeg);
						int j = (int) Math.round(-(Math.cos(angleRad) / Math
								.sin(angleRad))
								* i
								+ (currentR / Math.sin(angleRad)));
						if (j >= 0 && j < inputImgHeight) {
							imgInput.setRGB(i, j, new Color(255, 0, 0).getRGB());
						}
					}
				}
			}
		}

		return imgAccumulator;
	}

}
