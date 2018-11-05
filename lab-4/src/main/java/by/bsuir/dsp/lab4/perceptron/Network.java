package by.bsuir.dsp.lab4.perceptron;

import by.bsuir.dsp.lab4.array.ArrayService;

import java.util.Random;

import static by.bsuir.dsp.lab4.array.ArrayService.toBipolar;
import static java.lang.Math.exp;

public class Network {
    private int n;
    private int h;
    private int m;

    private double[] entrance;
    private double[] hidden;
    private double[] output;

    private double[][] eh;
    private double[][] ho;

    private double[] q;
    private double[] t;

    public Network(int n, int h, int m) {
        this.n = n;
        this.h = h;
        this.m = m;

        this.entrance = new double[n];
        this.hidden = new double[h];
        this.output = new double[m];

        this.eh = new double[n][h];
        this.ho = new double[h][m];

        this.q = new double[h];
        this.t = new double[m];

        this.init();
    }

    private void init() {
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < h; j++) {
                eh[i][j] = r.nextDouble() * (r.nextBoolean() ? 1.0 : -1.0);
            }
        }
        for (int j = 0; j < h; j++) {
            for (int k = 0; k < m; k++) {
                ho[j][k] = r.nextDouble() * (r.nextBoolean() ? 1.0 : -1.0);
            }
        }

        for (int j = 0; j < h; j++) {
            q[j] = r.nextDouble() * (r.nextBoolean() ? 1.0 : -1.0);
        }
        for (int k = 0; k < m; k++) {
            t[k] = r.nextDouble() * (r.nextBoolean() ? 1.0 : -1.0);
        }
    }

    public long learn(int[][] bitmap, double[] result, double a, double b, double maxError) {
        int[] input = toBipolar(bitmap);
        double error;

        // back propogate
        long iterCount = 0;
        do {
            error = 0;
            supply(input);

            double[] diff = ArrayService.sub(result, output);

            for (int j = 0; j < h; j++) {
                for (int k = 0; k < m; k++) {
                    if (Math.abs(diff[k]) > error) {
                        error = Math.abs(diff[k]);
                    }
                    double temp = b * output[k] * (1.0 - output[k]) * diff[k];
                    ho[j][k] += temp * hidden[j];
                    if (j == 0) {
                        t[k] += temp;
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < h; j++) {
                    double ej = 0;
                    for (int k = 0; k < m; k++) {
                        if (Math.abs(diff[k]) > error) {
                            error = Math.abs(diff[k]);
                        }
                        ej += diff[k] * output[k] * (1.0 - output[k]) * ho[j][k];
                    }
                    double temp = a * hidden[j] * (1.0 - hidden[j]) * ej;
                    eh[i][j] += temp * entrance[i];
                    if (i == 0) {
                        q[j] += temp;
                    }
                }
            }
            iterCount++;
        } while (error > maxError);

        return iterCount;
    }

    private void supply(int[] input) {
        for (int i = 0; i < n; i++) {
            entrance[i] = input[i];
        }
        calculate();
    }

    private void calculate() {
        for (int j = 0; j < h; j++) {
            hidden[j] = 0;
            for (int i = 0; i < n; i++) {
                hidden[j] += entrance[i] * eh[i][j];
            }
            hidden[j] = activation(hidden[j] + q[j]);
        }

        for (int k = 0; k < m; k++) {
            output[k] = 0;
            for (int j = 0; j < h; j++) {
                output[k] += hidden[j] * ho[j][k];
            }
            output[k] = activation(output[k] + t[k]);
        }
    }

    private double activation(double value) {
        return (1.0 / (1.0 + exp(-value)));
    }

    public int recognize(int[][] bitmap) {
        int[] input = toBipolar(bitmap);
        supply(input);
        int max = 0;
        for (int i = 1; i < m; i++) {
            if (output[i] > output[max]) {
                max = i;
            }
        }
        return max;
    }

    public double[] getOutput() {
        return output;
    }
}
