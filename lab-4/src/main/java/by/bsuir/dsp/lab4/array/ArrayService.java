package by.bsuir.dsp.lab4.array;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ArrayService {
    public static int[] toBipolar(int[] input) {
        return Arrays.stream(input).map(i -> i == 0 ? -1 : 1).toArray();
    }

    public static int[] toBipolar(int[][] bitmap) {
        int[] input = Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray();
        return Arrays.stream(input).map(i -> i == 0 ? -1 : 1).toArray();
    }

    public static int[] toUnipolar(int[] input) {
        return Arrays.stream(input).map(i -> i < 0 ? 0 : 1).toArray();
    }

    public static int[][] arrayToMatrix(int[] input) {
        int resultSize = (int) Math.sqrt(100D);
        int[][] result = new int[resultSize][resultSize];
        for (int i = 0; i < resultSize; i++) {
            for (int j = 0; j < resultSize; j++) {
                result[i][j] = input[i * resultSize + j];
            }
        }
        return result;
    }

    public static double[] sub(double[] first, double[] second) {
        return IntStream.range(0, first.length).mapToDouble(i -> first[i] - second[i]).toArray();
    }
}
