package dsp.task1.logic;

import dsp.task1.logic.io.BinarySampleFileService;
import dsp.task1.logic.io.TextSampleFileService;
import dsp.task1.logic.signal.SinusoidalSignal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SignalGenerator generator = new SignalGenerator();

        List<Sample> samples = generator.generate(new SinusoidalSignal(2.0, 0, 10, 2), 10);

        double mean = SignalStatistics.mean(samples);
        double rms = SignalStatistics.rms(samples);
        double variance = SignalStatistics.variance(samples);

        System.out.println("Średnia: " + mean);
        System.out.println("RMS: " + rms);
        System.out.println("Wariancja: " + variance);
    }
}
