package by.bsuir.dsp.lab2.service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
        int allIntensitySum = 0;
        int allPixelCount = 0;

        for (int t = 0; t < 256; t++) {
            allIntensitySum += t * histogram[t];
            allPixelCount += histogram[t];
        }

        double maxSigma = -1;
        int threshold = 0;

        int firstClassIntensitySum = 0;
        int firstClassPixelCount = 0;

        for (int t = 0; t < 256; t++) {
            firstClassIntensitySum += t * histogram[t];
            firstClassPixelCount += histogram[t];

            double firstClassProb = (double) firstClassPixelCount / allPixelCount;
            double firstClassMean = (double) firstClassIntensitySum / firstClassPixelCount;
            double deltaMean = firstClassMean - (double) (allIntensitySum - firstClassIntensitySum) / (allPixelCount - firstClassPixelCount);

            double sigma = firstClassProb * (1 - firstClassProb) * deltaMean * deltaMean;

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

    public static BufferedImage erosion(BufferedImage image) {
        return morph(image, true);
    }

    public static BufferedImage dilatation(BufferedImage image) {
        return morph(image, false);
    }

    private static BufferedImage morph(BufferedImage image, boolean erosionFlag) {
        int width = image.getWidth();
        int height = image.getHeight();

        int targetColor = erosionFlag ? Color.BLACK.getRGB() : Color.WHITE.getRGB();
        int reverseColor = erosionFlag ? Color.WHITE.getRGB() : Color.BLACK.getRGB();

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image.getRGB(i, j) == reverseColor) {
                    boolean isReversePixelFound = false;
                    for (int i0 = i - 3; i0 <= i + 3 && !isReversePixelFound; i0++) {
                        for (int j0 = j - 3; j0 <= j + 3 && !isReversePixelFound; j0++) {
                            if (i0 >= 0 && i0 < width && j0 >= 0 && j0 < height && image.getRGB(i0, j0) != reverseColor) {
                                isReversePixelFound = true;
                                result.setRGB(i, j, targetColor);
                            }
                        }
                    }
                    if (!isReversePixelFound) {
                        result.setRGB(i, j, reverseColor);
                    }
                } else {
                    result.setRGB(i, j, targetColor);
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
            Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
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
                if ((p.x > 1 && map[p.x - 1][p.y] == 0) || (p.x < width - 1 && map[p.x + 1][p.y] == 0) || (p.y > 0 && map[p.x][p.y - 1] == 0) || (p.y < height - 1 && map[p.x][p.y + 1] == 0)) {
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

    public static List<List<Map.Entry<Integer, Property>>> kMeans(Map<Integer, Property> objects, int k) {
        List<List<Map.Entry<Integer, Property>>> clusters = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            clusters.add(new ArrayList<>());
        }

        List<Property> centers = new ArrayList<>(k);

        Random r = new Random();
        for (int i = 0; i < k; i++) {
            centers.add(Property.builder()
                                .square(r.nextInt(10000))
                                .perimeter(r.nextInt(1000))
                                .density(r.nextInt(100))
                                .elongation(r.nextInt(200))
                                .orientation(r.nextInt(200))
                                .build());
        }


        boolean isChanged;

        do {
            isChanged = false;

            for (Map.Entry<Integer, Property> o : objects.entrySet()) {
                Property cluster = nearest(o.getValue(), centers);
                int ind = centers.indexOf(cluster);

                if (!clusters.get(ind).contains(o)) {
                    clusters.forEach(ls -> ls.remove(o));
                    clusters.get(ind).add(o);

                    updateCenter(cluster, clusters.get(ind));

                    isChanged = true;
                }
            }

        } while (isChanged);

        return clusters;
    }

    private static double distance(Property p1, Property p2) {
        double a = 0, b = 0, c = 0, d = 0, e = 0;

        a = Math.pow(p1.getSquare() - p2.getSquare(), 2);
        b = Math.pow(p1.getPerimeter() - p2.getPerimeter(), 2);

        if (!Double.isNaN(p1.getDensity()) && !Double.isNaN(p2.getDensity())) {
            c = Math.pow(p1.getDensity() - p2.getDensity(), 2);
        }

        if (!Double.isNaN(p1.getElongation()) && !Double.isNaN(p2.getElongation())) {
            d = Math.pow(p1.getElongation() - p2.getElongation(), 2);
        }

        if (!Double.isNaN(p1.getOrientation()) && !Double.isNaN(p2.getOrientation())) {
            e = Math.pow(p1.getOrientation() - p2.getOrientation(), 2);
        }

        return Math.sqrt(a + b + c + d + e);
    }

    private static Property nearest(Property p, List<Property> centers) {
        Property cluster = null;
        double minDist = Double.MAX_VALUE;

        for (Property c : centers) {
            double dist = distance(c, p);
            if (dist < minDist) {
                minDist = dist;
                cluster = c;
            }
        }

        return cluster;
    }

    private static void updateCenter(Property c, List<Map.Entry<Integer, Property>> objects) {
        int count = objects.size();
        c.setSquare(objects.stream().mapToInt(e -> e.getValue().getSquare()).sum() / count);
        c.setPerimeter(objects.stream().mapToInt(e -> e.getValue().getPerimeter()).sum() / count);
        c.setDensity(objects.stream().mapToDouble(e -> e.getValue().getDensity()).sum() / count);
        c.setElongation(objects.stream().mapToDouble(e -> e.getValue().getElongation()).sum() / count);
        c.setOrientation(objects.stream().mapToDouble(e -> e.getValue().getOrientation()).sum() / count);
    }

    public static BufferedImage mapToClusteredImage(int[][] map, List<List<Map.Entry<Integer, Property>>>
            clusters) {
        int count = clusters.size();
        int width = map.length;
        int height = map[0].length;

        List<List<Integer>> labels = clusters.stream()
                                             .map(ls -> ls.stream()
                                                          .map(Map.Entry::getKey)
                                                          .collect(Collectors.toList()))
                                             .collect(Collectors.toList());

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        List<Color> colors = new ArrayList<>(count);
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            colors.add(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
        }

        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height - 1; j++) {
                int cur = map[i][j];
                Color color = null;

                if (cur == 0) {
                    color = Color.BLACK;
                } else {
                    int ind = 0;
                    for (int i0 = 0; i0 < labels.size(); i0++) {
                        if (labels.get(i0).contains(cur)) {
                            ind = i0;
                            break;
                        }
                    }
                    color = colors.get(ind);
                }
                result.setRGB(i, j, color.getRGB());
            }
        }

        return result;
    }
}