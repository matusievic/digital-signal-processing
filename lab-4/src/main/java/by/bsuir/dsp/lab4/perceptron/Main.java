package by.bsuir.dsp.lab4.perceptron;

public class Main {
/*    public static void main(String[] args) {
        // Создание перцептрона
        Perceptron perceptron = new Perceptron();
    }
}

class Perceptron {
    double[] enters;
    double[] hidden;
    double outer;
    double[][] wEH;
    double[] wHO;
    double[][] patterns = {{0, 0}, {1, 0}, {0, 1}, {1, 1}};
    double[] answers = {0, 1, 1, 0};

    Perceptron() {
        enters = new double[patterns[0].length];
        hidden = new double[2];
        wEH = new double[enters.length][hidden.length];
        wHO = new double[hidden.length];
        initWeights();
        study();
        for (int p = 0; p < patterns.length; p++) {
            for (int i = 0; i < enters.length; i++) enters[i] = patterns[p][i];
            countOuter();
            System.out.println(outer);
        }
    }

    public void initWeights() {
        for (int i = 0; i < enters.length; i++) {
            for (int j = 0; j < wEH.length; j++) {
                wEH[i][j] = Math.random() * 0.2 + 0.1;
            }
        }
        for (int i = 0; i < wHO.length; i++) wHO[i] = Math.random() * 0.2 + 0.1;
    }

    public void countOuter() {
        for (int i = 0; i < hidden.length; i++) {
            hidden[i] = 0;
            for (int j = 0; j < enters.length; j++) {
                hidden[i] += enters[j] * wEH[j][i];
            }
            if (hidden[i] > 0.5) hidden[i] = 1;
            else hidden[i] = 0;
        }
        outer = 0;
        for (int i = 0; i < hidden.length; i++) {
            outer += hidden[i] * wHO[i];
        }
        if (outer > 0.5) outer = 1;
        else outer = 0;
    }

    public void study() {
        double[] err = new double[hidden.length];
        double gError = 0;
        do {
            gError = 0;
            for (int p = 0; p < patterns.length; p++) {
                for (int i = 0; i < enters.length; i++) enters[i] = patterns[p][i];
                countOuter();
                double lErr = answers[p] - outer;
                gError += Math.abs(lErr);
                for (int i = 0; i < hidden.length; i++) err[i] = lErr * wHO[i];
                for (int i = 0; i < enters.length; i++) {
                    for (int j = 0; j < hidden.length; j++) {
                        wEH[i][j] += 0.1 * err[j] * enters[i];
                    }
                }
                for (int i = 0; i < hidden.length; i++) wHO[i] += 0.1 * lErr * hidden[i];
            }
        } while (gError != 0);
    }*/
}