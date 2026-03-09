package dsp.task1.logic;


import java.util.ArrayList;
import java.util.List;

/**
 * Klasa generująca próbki sygnału na podstawie częstotliwości próbkowania
 * i przedziału czasu.
 */
public class SignalGenerator {

    public List<Sample> generate(Signal signal, double startTime, double duration, double samplingFrequency) {
        List<Sample> samples = new ArrayList<>();

        double endTime = startTime + duration;
        double dt = 1.0 / samplingFrequency;

        for (double t = startTime; t <= endTime; t += dt) {
            double value = signal.valueAt(t);
            samples.add(new Sample(t, value));
        }

        return samples;
    }
}
