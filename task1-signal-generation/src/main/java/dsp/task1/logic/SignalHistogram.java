package dsp.task1.logic;

import dsp.task1.logic.signal.SignalType;

import java.util.ArrayList;
import java.util.List;

public class SignalHistogram {
    private static final double EPS = 1e-9;

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

        if (samples == null || samples.isEmpty()) {
            return 0;
        }

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

    public static List<HistogramBin> generate(SignalData signalData, int numberOfBins) {
        List<HistogramBin> bins = new ArrayList<>();

        List<Sample> samples = signalData.getSamples();
        if (samples == null || samples.isEmpty() || numberOfBins <= 0) {
            return bins;
        }

        int validCount = getValidSampleCount(signalData);

        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < validCount; i++) {
            double value = samples.get(i).getValue();
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        if (Math.abs(min - max) < EPS) {
            bins.add(new HistogramBin(String.format("[%.2f]", min), validCount));
            return bins;
        }

        double binWidth = (max - min) / numberOfBins;
        int[] counts = new int[numberOfBins];

        for (int i = 0; i < validCount; i++) {
            double value = samples.get(i).getValue();

            int index = (int) ((value - min) / binWidth);

            if (index == numberOfBins) {
                index = numberOfBins - 1;
            }

            counts[index]++;
        }

        for (int i = 0; i < numberOfBins; i++) {
            double start = min + i * binWidth;
            double end = start + binWidth;

            String label;
            if (i == numberOfBins - 1) {
                label = String.format("[%.2f, %.2f]", start, end);
            } else {
                label = String.format("[%.2f, %.2f)", start, end);
            }

            bins.add(new HistogramBin(label, counts[i]));
        }

        return bins;
    }
}
