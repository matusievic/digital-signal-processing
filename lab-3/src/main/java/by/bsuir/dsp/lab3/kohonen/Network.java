package by.bsuir.dsp.lab3.kohonen;

import java.util.Arrays;

public class Network {
    private Neuron[] neurons;
    private int[][] links;
    private int size;
    private int[] lastY;

    public Network(int size) {
        this.neurons = new Neuron[size];
        for (int i = 0; i < size; i++) {
            this.neurons[i] = Neuron.builder().index(i).build();
        }

        this.links = new int[size][size];
        this.size = size;
        this.lastY = new int[size];
    }

    public Network learn(int[][] bitmap) {
        int[] input = Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray();
        for (int i = 0; i < size; i++) {
            this.neurons[i].setX(input[i]);
        }

        this.recalculateLinks();
        return this;
    }

    private void recalculateLinks() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    links[i][j] += neurons[i].getX() * neurons[j].getX();
                }
            }
        }
    }

    public int recognize(int[][] bitmap, int iteration) {
        int[] input = Arrays.stream(bitmap).flatMapToInt(Arrays::stream).toArray();
        for (int i = 0; i < size; i++) {
            this.neurons[i].setX(input[i]);
            this.lastY[i] = neurons[i].getY();
        }

        for (int i = 0; i < size; i++) {
            neurons[i].setState(0);
            for (int j = 0; j < size; j++) {
                int state = neurons[i].getState() + links[i][j] * neurons[j].getX();
                neurons[i].setState(state);
            }
            neurons[i].changeState();
        }

        for (int i = 0; i < size; i++) {
            if (lastY[i] != neurons[i].getY()) {
                int subSize = (int) Math.sqrt(size);
                int[][] current = new int[subSize][subSize];

                for (int i0 = 0; i0 < subSize; i0++) {
                    for (int j0 = 0; j0 < subSize; j0++) {
                        current[i0][j0] = neurons[i0 * subSize + j0].getY();
                    }
                }

                return this.recognize(current, iteration + 1);
            }
        }

        return iteration;
    }
}
