package dsp.task1.logic;

import java.util.List;

public class SignalStatistics {

    private static void validate(List<Sample> samples) {
        if (samples == null || samples.isEmpty()) {
            throw new IllegalArgumentException("Lista próbek nie może być pusta.");
        }
    }

    public static double mean(List<Sample> samples) {
        validate(samples);

        double sum = 0.0;
        for (Sample sample : samples) {
            sum += sample.getValue();
        }
        return sum / samples.size();
    }

    public static double meanAbsoluteValue(List<Sample> samples) {
        validate(samples);

        double sum = 0.0;
        for (Sample sample : samples) {
            sum += Math.abs(sample.getValue());
        }
        return sum / samples.size();
    }

    public static double averagePower(List<Sample> samples) {
        validate(samples);

        double sum = 0.0;
        for (Sample sample : samples) {
            double y = sample.getValue();
            sum += Math.pow(sample.getValue(), 2.0);
        }
        return sum / samples.size();
    }

    public static double variance(List<Sample> samples) {
        validate(samples);

        double mean = mean(samples);
        double sum = 0.0;

        for (Sample sample : samples) {
            double diff = sample.getValue() - mean;
            sum += diff * diff;
        }
        return sum / samples.size();
    }

    public static double rms(List<Sample> samples) {
        return Math.sqrt(averagePower(samples));
    }
}
