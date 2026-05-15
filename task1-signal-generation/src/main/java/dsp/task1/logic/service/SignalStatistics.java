package dsp.task1.logic.service;

import dsp.task1.logic.model.Sample;
import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.model.SignalParameters;

import java.util.List;

public class SignalStatistics {

    private static final double EPS = 1e-9;

    private static void validate(List<Sample> samples) {
        if (samples == null || samples.isEmpty()) {
            throw new IllegalArgumentException("Lista próbek nie może być pusta.");
        }
    }

    private static int getValidSampleCount(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        validate(samples);

        if (!signalData.getSignalType().isPeriodic()) {
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