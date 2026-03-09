package dsp.task1.logic;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        double A, T, t1, duration, samplingFrequency;
        int signal_type;

        // ----- parametry -----
        A = 1;
        T = 1;
        t1 = 0;
        duration = 2;
        samplingFrequency = 100;
        // typ sygnalu: 1-sinusoida 2-szum jednostajny
        signal_type = 2;
        // --------------------

        Signal signal;

        if (signal_type == 1) {
            signal = new SinusoidalSignal(A, T, t1);
            SignalGenerator generator = new SignalGenerator();
            List<Sample> samples = generator.generate(signal, t1, duration, samplingFrequency);

            SignalPlotter.plot(samples);
        } else if (signal_type == 2) {
            signal = new UniformNoiseSignal(A);
            SignalGenerator generator = new SignalGenerator();
            List<Sample> samples = generator.generate(signal, t1, duration, samplingFrequency);

            SignalPlotter.plot(samples);
        } else {
            System.out.println("bledny wybor");
        }
    }
}
