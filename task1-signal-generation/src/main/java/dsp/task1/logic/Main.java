package dsp.task1.logic;

import dsp.task1.logic.operations.SignalOperations;
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

//        Signal signal = new HalfWaveRectifiedSinusoidalSignal(A, t1, d, T);
//        SignalGenerator generator = new SignalGenerator();
//        List<Sample> samples = generator.generate(signal, samplingFrequency);
//
//        SignalPlotter.plot(signal, samples);

        ContinuousSignal s1 = new SinusoidalSignal(10.0, 0.0, 10.0, 2.0);
        ContinuousSignal s2 = new SymmetricRectangularSignal(5.0, 0.0, 10.0, 0.5, 0.5);
        ContinuousSignal test_signal = new Test();

        ContinuousSignal sum = SignalOperations.add(s1, s2);
        ContinuousSignal diff = SignalOperations.subtract(s1, s2);
        ContinuousSignal product = SignalOperations.multiply(s1, s2);
        ContinuousSignal quotient = SignalOperations.divide(s1, s2);

        SignalGenerator generator = new SignalGenerator();

        // testowy
//        List<Sample> test_samples = generator.generate(test_signal, 100);
//        SignalPlotter.plot(test_signal, test_samples);
//
        List<Sample> samples1 = generator.generate(s1, 100);
        SignalPlotter.plot(s1, samples1);
//
//
        List<Sample> samples2 = generator.generate(s2, 100);
        SignalPlotter.plot(s2, samples2);

        List<Sample> sample12 = generator.generate(product, 100);
        SignalPlotter.plot(product, sample12);
//
    }
}
