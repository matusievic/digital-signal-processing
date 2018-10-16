package by.bsuir.dsp.lab2.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Property {
    int square;
    int perimeter;
    double centerX;
    double centerY;
    double density;
    double elongation;
    double orientation;
}
