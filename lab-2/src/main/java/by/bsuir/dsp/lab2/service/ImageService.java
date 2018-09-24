package by.bsuir.dsp.lab2.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        int width = image.getWidth();
        int height = image.getHeight();
        int n = width * height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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

    public static int[][] mark(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int black = Color.BLACK.getRGB();

        int label = 0;

        int[][] map = new int[width][height];

        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                boolean x1 = map[i - 1][j] != 0 && map[i][j - 1] == 0;
                boolean x2 = map[i - 1][j] == 0 && map[i][j - 1] != 0;

                if (image.getRGB(i, j) == black) {
                    continue;
                } else if (map[i - 1][j] == 0 && map[i][j - 1] == 0) {
                    label++;
                    map[i][j] = label;
                } else if (x1 || x2) {
                    if (x1) {
                        map[i][j] = map[i - 1][j];
                    } else {
                        map[i][j] = map[i][j - 1];
                    }
                } else if (map[i - 1][j] != 0 && map[i][j - 1] != 0) {
                    if (map[i - 1][j] == map[i][j - 1]) {
                        map[i][j] = map[i - 1][j];
                    } else {
                        int cMark = map[i][j - 1];
                        int bMark = map[i - 1][j];

                        for (int i0 = 1; i0 < width; i0++) {
                            for (int j0 = 1; j0 < height; j0++) {
                                if (map[i0][j0] == cMark) {
                                    map[i0][j0] = bMark;
                                }
                            }
                        }

                        map[i][j] = bMark;
                    }
                }
            }
        }

        return map;
    }

    public static BufferedImage mapToImage(int[][] map) {
        Color[] colors = {Color.BLUE, Color.CYAN, Color.YELLOW, Color.RED, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.LIGHT_GRAY, Color.GRAY};

        int width = map.length;
        int height = map[0].length;

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        List<Integer> labels = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int cur = map[i][j];
                if (cur != 0 && !labels.contains(cur)) {
                    labels.add(cur);
                }
            }
        }

        Map<Integer, Color> colorMap = new HashMap<>(labels.size());
        Random r = new Random();
        for (int i = 0; i < labels.size(); i++) {
            Color color = null;
            if (i < colors.length) {
                color = colors[i];
            } else {
                color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            }
            colorMap.put(labels.get(i), color);
        }

        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                Color color = colorMap.getOrDefault(map[i][j], Color.BLACK);
                result.setRGB(i, j, color.getRGB());
            }
        }

        return result;
    }

    public static Map<Integer, Property> getProperties(int[][] map) {
        int width = map.length;
        int height = map[0].length;

        Map<Integer, List<Point>> points = new HashMap<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int cur = map[i][j];
                if (cur != 0) {
                    points.putIfAbsent(cur, new ArrayList<>());
                    points.get(cur).add(new Point(i, j));
                }
            }
        }

        int count = points.size();
        Map<Integer, Integer> square = new HashMap<>(count);
        Map<Integer, Integer> perimeter = new HashMap<>(count);
        Map<Integer, Double> centerX = new HashMap<>(count);
        Map<Integer, Double> centerY = new HashMap<>(count);
        Map<Integer, Double> density = new HashMap<>(count);
        Map<Integer, Double> elongation = new HashMap<>(count);
        Map<Integer, Double> orientation = new HashMap<>(count);

        points.forEach((k, v) -> {
            int size = v.size();

            // square
            square.put(k, size);

            // perimeter
            v.forEach(p -> {
                if (map[p.x - 1][p.y] == 0 || map[p.x + 1][p.y] == 0 || map[p.x][p.y - 1] == 0 || map[p.x][p.y + 1] == 0) {
                    perimeter.merge(k, 1, (cur, i) -> cur + i);
                }
            });

            // center
            centerX.put(k, v.stream().mapToDouble(p -> p.x).sum() / size);
            centerY.put(k, v.stream().mapToDouble(p -> p.y).sum() / size);

            // density
            density.put(k, (Math.pow(perimeter.get(k), 2) / square.get(k)));

            // auxiliary params
            double xCenter = centerX.get(k);
            double m20 = v.stream().mapToDouble(p -> p.x - xCenter).sum();

            double yCenter = centerY.get(k);
            double m02 = v.stream().mapToDouble(p -> p.y - yCenter).sum();

            double m11 = v.stream().mapToDouble(p -> (p.x - xCenter) * (p.y - yCenter)).sum();

            // elongation
            double a = m20 + m02;
            double b = Math.sqrt(Math.pow(m20 - m02, 2) + 4 * Math.pow(m11, 2));
            elongation.put(k, (a + b) / (a - b));

            // orientation
            orientation.put(k, (1 / 2) * Math.atan((2 * m11) / (m20 - m02)));
        });

        Map<Integer, Property> result = new HashMap<>(count);
        for (int label : points.keySet()) {
            result.put(label, Property.builder()
                                      .square(square.get(label))
                                      .perimeter(perimeter.get(label))
                                      .centerX(centerX.get(label))
                                      .centerY(centerY.get(label))
                                      .density(density.get(label))
                                      .elongation(elongation.get(label))
                                      .orientation(orientation.get(label))
                                      .build());
        }

        return result;
    }
}