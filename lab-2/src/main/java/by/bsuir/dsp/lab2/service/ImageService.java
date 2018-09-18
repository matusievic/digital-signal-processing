package by.bsuir.dsp.lab2.service;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageService {
    public static BufferedImage toGrayscaled(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color oldColor = new Color(image.getRGB(i, j));
                int g = (int) (0.30 * oldColor.getRed() + 0.59 * oldColor.getGreen() + 0.11 * oldColor.getBlue());
                Color newColor = new Color(g, g, g);
                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }

    public static BufferedImage toBinary(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage gray = toGrayscaled(image);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);


        int white = new Color(255, 255, 255).getRGB();
        int black = new Color(0, 0, 0).getRGB();

        int[] histogram = getHistogram(gray);
        int m = 0;
        int n = 0;

        for (int t = 0; t < 256; t++) {
            m += t * histogram[t];
            n += histogram[t];
        }

        double maxSigma = -1;
        int threshold = 0;

        int alpha1 = 0;
        int beta1 = 0;

        for (int t = 0; t < 256; t++) {
            alpha1 += t * histogram[t];
            beta1 += histogram[t];

            double w1 = (double) beta1 / n;
            double a = (double) alpha1 / beta1 - (double) (m - alpha1) / (n - beta1);

            double sigma = w1 * (1 - w1) * a * a;

            if (sigma > maxSigma) {
                maxSigma = sigma;
                threshold = t;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (new Color(gray.getRGB(i, j)).getRed() > threshold) {
                    result.setRGB(i, j, white);
                } else {
                    result.setRGB(i, j, black);
                }
            }
        }

        return result;
    }

    public static int[] getHistogram(BufferedImage image) {
        return getHistogram(image, 'r');
    }

    public static int[] getHistogram(BufferedImage image, char key) {
        int[] result = new int[256];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int index;
                switch (key) {
                    case 'r':
                        index = new Color(image.getRGB(i, j)).getRed();
                        break;
                    case 'g':
                        index = new Color(image.getRGB(i, j)).getGreen();
                        break;
                    case 'b':
                        index = new Color(image.getRGB(i, j)).getBlue();
                        break;
                    default:
                        throw new IllegalArgumentException("Please provide a valid key");
                }
                result[index]++;
            }
        }

        return result;
    }
}