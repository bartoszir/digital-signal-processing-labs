package dsp.task1.logic;

import dsp.task1.logic.signal.*;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        double A, T, t1, d, samplingFrequency;
        int signal_type;

        // ----- parametry -----
        A = 1;
        T = 1;
        t1 = 0;
        d = 2;
        samplingFrequency = 300;
        // typ sygnalu: 1-sinusoida 2-szum jednostajny
        signal_type = 1;
        // --------------------

        Signal signal = new HalfWaveRectifiedSinusoidalSignal(A, t1, d, T);
        SignalGenerator generator = new SignalGenerator();
        List<Sample> samples = generator.generate(signal, samplingFrequency);

        SignalPlotter.plot(signal, samples);
    }
}
