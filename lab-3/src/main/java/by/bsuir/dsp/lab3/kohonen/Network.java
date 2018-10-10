package by.bsuir.dsp.lab3.kohonen;

import java.util.Arrays;

import static by.bsuir.dsp.lab3.array.ArrayService.arrayToMatrix;
import static by.bsuir.dsp.lab3.array.ArrayService.toBipolar;
import static by.bsuir.dsp.lab3.array.ArrayService.toUnipolar;

public class Network {
    private int[][] weights;
    private int size;

    public Network(int size) {
        this.weights = new int[size][size];
        this.size = size;
    }

    public Network learn(int[][] bitmap) {
        int[] input = toBipolar(Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    weights[i][j] += input[i] * input[j];
                }
            }
        }
        return this;
    }

    public int[][] recognize(int[][] bitmap) {
        int[] input = toBipolar(Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray());
        int[] result = new int[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i] += input[j] * weights[i][j];
            }
            result[i] = result[i] > 1 ? 1 : -1;
        }

        return arrayToMatrix(toUnipolar(result));
    }

}
