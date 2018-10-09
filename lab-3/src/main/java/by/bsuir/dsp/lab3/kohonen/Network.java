package by.bsuir.dsp.lab3.kohonen;

import java.util.Arrays;

public class Network {
    private Neuron[] neurons;
    private int[][] links;
    private int size;
    private int[] previousY;

    public Network(int size) {
        this.neurons = new Neuron[size];
        for (int i = 0; i < size; i++) {
            this.neurons[i] = Neuron.builder().index(i).build();
        }

        this.links = new int[size][size];
        this.size = size;
        this.previousY = new int[size];
    }

    public Network learn(int[][] bitmap) {
        int[] input = toBipolar(Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    links[i][j] += input[i] * input[j];
                }
            }
        }
        return this;
    }

    public int[][] recognize(int[][] bitmap) {
        int[] input = toBipolar(Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray());

//        /*for (int iteration = 0; iteration < 100; iteration++) {
//            // supply an input image to neurons
//            for (int i = 0; i < size; i++) {
//                this.neurons[i].setX(input[i]);
//                this.previousY[i] = neurons[i].getY();
//            }
//
//            for (int i = 0; i < size; i++) {
//                this.neurons[i].setState(0);
//                for (int j = 0; j < size; j++) {
//                    int state = this.neurons[i].getState() + links[i][j] * neurons[j].getX();
//                    this.neurons[i].setState(state);
//                }
//                this.neurons[i].changeState();
//            }
//
//            if (Arrays.equals(this.previousY, input)) {
//                return iteration;
//            }
//
//            input = Arrays.stream(neurons).mapToInt(Neuron::getY).toArray();
//        }
//
//
//
//        for (int i = 0; i < size; i++) {
//            if (previousY[i] != neurons[i].getY()) {
//                int subSize = (int) Math.sqrt(size);
//                int[][] current = new int[subSize][subSize];
//
//                for (int i0 = 0; i0 < subSize; i0++) {
//                    for (int j0 = 0; j0 < subSize; j0++) {
//                        current[i0][j0] = neurons[i0 * subSize + j0].getY();
//                    }
//                }
//
//                return this.recognize(current, iteration + 1);
//            }
//        }*/

        int[] result = Arrays.copyOf(input, size);
        for (int iteration = 0; iteration < 100000; iteration++) {
            this.previousY = Arrays.copyOf(result, size);
            for (int i = 0; i < size; i++) {
                result[i] = 0;
                for (int j = 0; j < size; j++) {
                    result[i] += previousY[j] * links[i][j];
                }
                result[i] = result[i] > 1 ? 1 : -1;
            }

            if (Arrays.equals(result, this.previousY)) {
                System.out.println("ITER = " + iteration);
                return arrayToMatrix(toUnipolar(result));
            }
        }
        return null;
    }

    private int[] toBipolar(int[] input) {
        return Arrays.stream(input).map(i -> i == 0 ? -1 : i).toArray();
    }

    private int[] toUnipolar(int[] input) {
        return Arrays.stream(input).map(i -> i < 0 ? 0 : i).toArray();
    }

    private int[][] arrayToMatrix(int[] input) {
        int resultSize = (int) Math.sqrt(25D);
        int[][] result = new int[resultSize][resultSize];
        for (int i = 0; i < resultSize; i++) {
            for (int j = 0; j < resultSize; j++) {
                result[i][j] = input[i * resultSize + j];
            }
        }
        return result;
    }
}
