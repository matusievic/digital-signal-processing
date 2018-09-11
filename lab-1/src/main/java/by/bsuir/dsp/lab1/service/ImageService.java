package by.bsuir.dsp.lab1.service;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageService {
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

    public static int[] getHistogram(BufferedImage image) {
        return getHistogram(image, 'r');
    }

    public static int[] getHistogram(BufferedImage image, char key) {
        int[] result = new int[255];

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

    public static BufferedImage dissect(BufferedImage image, int f, int g, int mode) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color oldColor = new Color(image.getRGB(i, j));
                int newR = getDissected(oldColor.getRed(), f, g, mode);
                int newG = getDissected(oldColor.getGreen(), f, g, mode);
                int newB = getDissected(oldColor.getBlue(), f, g, mode);
                Color newColor = new Color(newR, newG, newB);
                result.setRGB(i, j, newColor.getRGB());
            }
        }

        return result;
    }


    private static int getDissected(int intensity, int f, int g, int mode) {
        switch (mode) {
            case 0:
                if (intensity < f) {
                    return 0;
                } else {
                    return (g * (intensity - f)) / (255 - f);
                }
            case 1:
                if (intensity > f) {
                    return 255;
                } else {
                    return (intensity * (255 - g)) / f + g;
                }
            default:
                throw new IllegalArgumentException("Please provide a valid mode");
        }
    }
}
