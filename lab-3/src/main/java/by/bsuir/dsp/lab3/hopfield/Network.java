package by.bsuir.dsp.lab3.hopfield;

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
        int[] previous = new int[size];
        System.arraycopy(input, 0, previous, 0, input.length);

        for (int iteration = 0; iteration < 1000; iteration++) {
            Arrays.fill(result, 0);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    result[i] += previous[j] * weights[i][j];
                }
                result[i] = result[i] > 1 ? 1 : -1;
            }

            if (Arrays.equals(previous, result)) {
                //System.out.println("ITERATIONS = " + iteration);
                return arrayToMatrix(toUnipolar(result));
            }

            System.arraycopy(result, 0, previous, 0, result.length);
        }

        System.out.println("ITERATIONS = NOT TERMINATED");
        return arrayToMatrix(toUnipolar(result));
    }

}
