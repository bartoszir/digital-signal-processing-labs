package dsp.task1.logic.service;

import dsp.task1.logic.model.Sample;
import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.model.SignalParameters;
import dsp.task1.logic.signal.SignalType;

import java.util.ArrayList;
import java.util.List;

public class QuantizationService {

    public SignalData quantize(SignalData sampled, int bits) {
        if (bits < 1 || bits > 32) {
            throw new IllegalArgumentException(
                "Liczba bitów musi być z zakresu 1-32.");
        }

        List<Sample> inputSamples = sampled.getSamples();

        double min = inputSamples.stream()
            .mapToDouble(Sample::getValue)
            .min().orElse(-1.0);
        double max = inputSamples.stream()
            .mapToDouble(Sample::getValue)
            .max().orElse(1.0);

        int levels = (int) Math.pow(2, bits);
        double step = (max - min) / levels;

        List<Sample> quantizedSamples = new ArrayList<>();
        for (Sample s : inputSamples) {
            double quantized = Math.round((s.getValue() - min) / step) * step + min;
            quantized = Math.max(min, Math.min(max, quantized));
            quantizedSamples.add(new Sample(s.getTime(), quantized));
        }

        SignalParameters params = new SignalParameters();
        params.setStartTime(sampled.getParameters().getStartTime());
        params.setSamplingFrequency(sampled.getParameters().getSamplingFrequency());
        params.setPeriod(sampled.getParameters().getPeriod());
        params.setDuration(sampled.getParameters().getDuration());

        return new SignalData(
            sampled.getName() + "_quantized",
            SignalType.OPERATION_RESULT,
            params,
            quantizedSamples
        );
    }
}
