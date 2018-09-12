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

    public static BufferedImage prewitt(BufferedImage image) {
        final int[][] pCore = {
                {1, 0, -1},
                {1, 0, -1},
                {1, 0, -1}
        };

        final int[][] qCore = {
                {-1, -1, -1},
                {0, 0, 0},
                {1, 1, 1}
        };

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 1; i < image.getWidth() - 1; i++) {
            for (int j = 1; j < image.getHeight() - 1; j++) {
                int[][] currentRed = {
                        {new Color(image.getRGB(i - 1, j - 1)).getRed(), new Color(image.getRGB(i - 1, j)).getRed(), new Color(image.getRGB(i - 1, j + 1)).getRed()},
                        {new Color(image.getRGB(i, j - 1)).getRed(), new Color(image.getRGB(i, j)).getRed(), new Color(image.getRGB(i, j + 1)).getRed()},
                        {new Color(image.getRGB(i + 1, j - 1)).getRed(), new Color(image.getRGB(i + 1, j)).getRed(), new Color(image.getRGB(i + 1, j + 1)).getRed()}
                };

                int red = Math.max(calculateProperty(pCore, currentRed), calculateProperty(qCore, currentRed));
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }

                int[][] currentGreen = {
                        {new Color(image.getRGB(i - 1, j - 1)).getGreen(), new Color(image.getRGB(i - 1, j)).getGreen(), new Color(image.getRGB(i - 1, j + 1)).getGreen()},
                        {new Color(image.getRGB(i, j - 1)).getGreen(), new Color(image.getRGB(i, j)).getGreen(), new Color(image.getRGB(i, j + 1)).getGreen()},
                        {new Color(image.getRGB(i + 1, j - 1)).getGreen(), new Color(image.getRGB(i + 1, j)).getGreen(), new Color(image.getRGB(i + 1, j + 1)).getGreen()}
                };

                int green = Math.max(calculateProperty(pCore, currentGreen), calculateProperty(qCore, currentGreen));
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }

                int[][] currentBlue = {
                        {new Color(image.getRGB(i - 1, j - 1)).getBlue(), new Color(image.getRGB(i - 1, j)).getBlue(), new Color(image.getRGB(i - 1, j + 1)).getBlue()},
                        {new Color(image.getRGB(i, j - 1)).getBlue(), new Color(image.getRGB(i, j)).getBlue(), new Color(image.getRGB(i, j + 1)).getBlue()},
                        {new Color(image.getRGB(i + 1, j - 1)).getBlue(), new Color(image.getRGB(i + 1, j)).getBlue(), new Color(image.getRGB(i + 1, j + 1)).getBlue()}
                };

                int blue = Math.max(calculateProperty(pCore, currentBlue), calculateProperty(qCore, currentBlue));
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }

                result.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }

        return result;
    }

    private static int calculateProperty(int[][] core, int[][] current) {
        int accumulator = 0;

        for (int i = 0; i < core.length; i++) {
            for (int j = 0; j < core.length; j++) {
                accumulator += core[i][j] * current[i][j];
            }
        }

        return accumulator;
    }


    public static BufferedImage highFrequencyFilter(BufferedImage image) {
        final int[][] core = {
                {-1, -1, -1},
                {-1, 9, -1},
                {-1, -1, -1}
        };

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 1; i < image.getWidth() - 1; i++) {
            for (int j = 1; j < image.getHeight() - 1; j++) {
                int[][] currentRed = {
                        {new Color(image.getRGB(i - 1, j - 1)).getRed(), new Color(image.getRGB(i - 1, j)).getRed(), new Color(image.getRGB(i - 1, j + 1)).getRed()},
                        {new Color(image.getRGB(i, j - 1)).getRed(), new Color(image.getRGB(i, j)).getRed(), new Color(image.getRGB(i, j + 1)).getRed()},
                        {new Color(image.getRGB(i + 1, j - 1)).getRed(), new Color(image.getRGB(i + 1, j)).getRed(), new Color(image.getRGB(i + 1, j + 1)).getRed()}
                };

                int red = calculateProperty(core, currentRed);
                if (red > 255) {
                    red = 255;
                } else if (red < 0) {
                    red = 0;
                }

                int[][] currentGreen = {
                        {new Color(image.getRGB(i - 1, j - 1)).getGreen(), new Color(image.getRGB(i - 1, j)).getGreen(), new Color(image.getRGB(i - 1, j + 1)).getGreen()},
                        {new Color(image.getRGB(i, j - 1)).getGreen(), new Color(image.getRGB(i, j)).getGreen(), new Color(image.getRGB(i, j + 1)).getGreen()},
                        {new Color(image.getRGB(i + 1, j - 1)).getGreen(), new Color(image.getRGB(i + 1, j)).getGreen(), new Color(image.getRGB(i + 1, j + 1)).getGreen()}
                };

                int green = calculateProperty(core, currentGreen);
                if (green > 255) {
                    green = 255;
                } else if (green < 0) {
                    green = 0;
                }

                int[][] currentBlue = {
                        {new Color(image.getRGB(i - 1, j - 1)).getBlue(), new Color(image.getRGB(i - 1, j)).getBlue(), new Color(image.getRGB(i - 1, j + 1)).getBlue()},
                        {new Color(image.getRGB(i, j - 1)).getBlue(), new Color(image.getRGB(i, j)).getBlue(), new Color(image.getRGB(i, j + 1)).getBlue()},
                        {new Color(image.getRGB(i + 1, j - 1)).getBlue(), new Color(image.getRGB(i + 1, j)).getBlue(), new Color(image.getRGB(i + 1, j + 1)).getBlue()}
                };

                int blue = calculateProperty(core, currentBlue);
                if (blue > 255) {
                    blue = 255;
                } else if (blue < 0) {
                    blue = 0;
                }

                result.setRGB(i, j, new Color(red, green, blue).getRGB());
            }
        }

        return result;
    }
}
