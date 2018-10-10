package by.bsuir.dsp.lab3.array;

import java.util.Arrays;

public class ArrayService {
    public static int[] toBipolar(int[] input) {
        return Arrays.stream(input).map(i -> i == 0 ? -1 : i).toArray();
    }

    public static int[] toUnipolar(int[] input) {
        return Arrays.stream(input).map(i -> i < 0 ? 0 : i).toArray();
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
}
