package dsp.task1.logic;

import dsp.task1.logic.signal.SignalType;

import java.util.List;

public class SignalStatistics {

    private static final double EPS = 1e-9;

    private static void validate(List<Sample> samples) {
        if (samples == null || samples.isEmpty()) {
            throw new IllegalArgumentException("Lista próbek nie może być pusta.");
        }
    }

    private static boolean isPeriodic(SignalType type) {
        return switch (type) {
            case SINUSOIDAL_SIGNAL,
                 ONE_HALF_RECTIFIED_SINUSOIDAL_SIGNAL,
                 TWO_HALF_RECTIFIED_SINUSOIDAL_SIGNAL,
                 RECTANGULAR_SIGNAL,
                 SYMMETRIC_RECTANGULAR_SIGNAL,
                 TRIANGULAR_SIGNAL -> true;
            default -> false;
        };
    }

    private static int getValidSampleCount(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        validate(samples);

        if (!isPeriodic(signalData.getSignalType())) {
            return samples.size();
        }

        SignalParameters params = signalData.getParameters();
        double period = params.getPeriod();
        double samplingFrequency = params.getSamplingFrequency();

        if (period <= EPS || samplingFrequency <= EPS) {
            return samples.size();
        }

        int samplesPerPeriod = (int) Math.round(period * samplingFrequency);
        if (samplesPerPeriod <= 0) {
            return samples.size();
        }

        int fullPeriods = samples.size() / samplesPerPeriod;
        int validCount = fullPeriods * samplesPerPeriod;

        if (validCount == 0) {
            throw new IllegalArgumentException("Za mało próbek do wyznaczenia pełnego okresu sygnału.");
        }

        return validCount;
    }

    public static double mean(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        int count = getValidSampleCount(signalData);

        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum += samples.get(i).getValue();
        }
        return sum / count;
    }

    public static double meanAbsoluteValue(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        int count = getValidSampleCount(signalData);

        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            sum += Math.abs(samples.get(i).getValue());
        }
        return sum / count;
    }

    public static double averagePower(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        int count = getValidSampleCount(signalData);

        double sum = 0.0;
        for (int i = 0; i < count; i++) {
            double value = samples.get(i).getValue();
            sum += value * value;
        }
        return sum / count;
    }

    public static double variance(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        int count = getValidSampleCount(signalData);

        double mean = mean(signalData);
        double sum = 0.0;

        for (int i = 0; i < count; i++) {
            double diff = samples.get(i).getValue() - mean;
            sum += diff * diff;
        }
        return sum / count;
    }

    public static double rms(SignalData signalData) {
        return Math.sqrt(averagePower(signalData));
    }
}
