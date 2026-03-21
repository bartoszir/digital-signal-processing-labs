package dsp.task1.logic;

import java.util.ArrayList;
import java.util.List;

public class SignalHistogram {
    public static List<HistogramBin> generate(List<Sample> samples, int numberOfBins) {
        List<HistogramBin> bins = new ArrayList<>();

        if (samples == null || samples.isEmpty() || numberOfBins <= 0) {
            return bins;
        }

        double min = samples.stream()
                .mapToDouble(Sample::getValue)
                .min()
                .orElse(0.0);

        double max = samples.stream()
                .mapToDouble(Sample::getValue)
                .max()
                .orElse(0.0);

        if (min == max) {
            bins.add(new HistogramBin(String.format("[%.2f]", min), samples.size()));
            return bins;
        }

        double binWidth = (max - min) / numberOfBins;
        int[] counts = new int[numberOfBins];

        for (Sample sample : samples) {
            double value = sample.getValue();

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
