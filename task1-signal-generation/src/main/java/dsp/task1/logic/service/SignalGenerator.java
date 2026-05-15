package dsp.task1.logic.service;

import dsp.task1.logic.model.Sample;
import dsp.task1.logic.signal.Signal;
import dsp.task1.logic.signal.ContinuousSignal;
import dsp.task1.logic.signal.DiscreteSignal;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa generująca próbki sygnału na podstawie częstotliwości próbkowania
 * i przedziału czasu.
 */
public class SignalGenerator {

    public static List<Sample> generate(Signal signal, double samplingFrequency) {
        List<Sample> samples = new ArrayList<>();

        double startTime = signal.getStartTime();
        double duration = signal.getDuration();
        double dt = 1.0 / samplingFrequency;

        int sampleCount = (int) Math.floor(duration * samplingFrequency);

        if (signal instanceof ContinuousSignal continuousSignal) {
            for (int n = 0; n <= sampleCount; n++) {
                double t = startTime + n * dt;
                double value = continuousSignal.getValueAt(t);
                samples.add(new Sample(t, value));
            }
        } else if (signal instanceof DiscreteSignal discreteSignal) {
            for (int n = 0; n <= sampleCount; n++) {
                double t = startTime + n * dt;
                double value = discreteSignal.getValueAtSample(n);
                samples.add(new Sample(t, value));
            }
        }

        return samples;
    }
}