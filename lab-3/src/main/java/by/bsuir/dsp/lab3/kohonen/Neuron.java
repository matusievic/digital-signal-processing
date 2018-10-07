package by.bsuir.dsp.lab3.kohonen;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Neuron {
    private int x;
    private int y;
    private int state;
    private int index;

    public void changeState() {
        y = Integer.compare(state, 0);
    }
}
