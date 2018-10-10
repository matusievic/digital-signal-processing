package by.bsuir.dsp.lab3.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageService {
    public static int[][] imageToMap(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int white = Color.WHITE.getRGB();
        int[][] result = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = image.getRGB(i, j);
                result[i][j] = color == white ? 1 : 0;
            }
        }

        return result;
    }

    public static BufferedImage mapToImage(int[][] map) {
        int width = map[0].length;
        int height = map.length;

        int white = Color.WHITE.getRGB();
        int black = Color.BLACK.getRGB();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = map[i][j] == 1 ? white : black;
                result.setRGB(i, j, color);
            }
        }

        return result;
    }

    public static BufferedImage maximize(BufferedImage image, int k) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage result = new BufferedImage(width * k, height * k, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                for (int i0 = 0; i0 < k; i0++) {
                    for (int j0 = 0; j0 < k; j0++) {
                        result.setRGB(i * k + i0, j * k + j0, rgb);
                    }
                }
            }
        }

        return result;
    }
}
