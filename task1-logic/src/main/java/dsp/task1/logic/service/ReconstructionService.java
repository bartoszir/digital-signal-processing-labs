package dsp.task1.logic.service;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;

import java.util.ArrayList;
import java.util.List;

public class ReconstructionService {

    public SignalData reconstructZOH(SignalData sampled, double targetFs) {
        List<Sample> sampledSamples = sampled.getSamples();
        if (sampledSamples.isEmpty()) {
            throw new IllegalArgumentException("Sygnał nie zawiera próbek.");
        }

        double startTime = sampledSamples.get(0).getTime();
        double endTime = sampledSamples.get(sampledSamples.size() - 1).getTime();
        double dt = 1.0 / targetFs;

        List<Sample> result = new ArrayList<>();
        int sampleIndex = 0;

        for (double t = startTime; t <= endTime + dt / 2; t += dt) {
            while (sampleIndex + 1 < sampledSamples.size()
                    && sampledSamples.get(sampleIndex + 1).getTime() <= t) {
                sampleIndex++;
            }
            result.add(new Sample(t, sampledSamples.get(sampleIndex).getValue()));
        }

        SignalParameters params = new SignalParameters();
        params.setStartTime(startTime);
        params.setSamplingFrequency(targetFs);
        params.setPeriod(sampled.getParameters().getPeriod());
        params.setDuration(sampled.getParameters().getDuration());

        return new SignalData(
            sampled.getName() + "_reconstructed_ZOH",
            SignalType.OPERATION_RESULT,
            params,
            result
        );
    }

    public SignalData reconstructSinc(SignalData sampled, double targetFs,
                                       int samplesLeft, int samplesRight) {
        List<Sample> sampledSamples = sampled.getSamples();
        if (sampledSamples.isEmpty()) {
            throw new IllegalArgumentException("Sygnał nie zawiera próbek.");
        }
        if (samplesLeft < 1 || samplesRight < 1) {
            throw new IllegalArgumentException(
                "Liczba próbek do interpolacji musi być >= 1.");
        }

        double Ts = 1.0 / sampled.getParameters().getSamplingFrequency();
        double startTime = sampledSamples.get(0).getTime();
        double endTime = sampledSamples.get(sampledSamples.size() - 1).getTime();
        double dt = 1.0 / targetFs;

        List<Sample> result = new ArrayList<>();

        for (double t = startTime; t <= endTime + dt / 2; t += dt) {
            int nearestIndex = (int) Math.floor((t - startTime) / Ts);
            nearestIndex = Math.max(0, Math.min(sampledSamples.size() - 1, nearestIndex));

            double value = 0.0;
            int fromIndex = Math.max(0, nearestIndex - samplesLeft + 1);
            int toIndex = Math.min(sampledSamples.size() - 1, nearestIndex + samplesRight);

            for (int i = fromIndex; i <= toIndex; i++) {
                double sampleTime = sampledSamples.get(i).getTime();
                double sincArg = (t - sampleTime) / Ts;
                value += sampledSamples.get(i).getValue() * sinc(sincArg);
            }

            result.add(new Sample(t, value));
        }

        SignalParameters params = new SignalParameters();
        params.setStartTime(startTime);
        params.setSamplingFrequency(targetFs);
        params.setPeriod(sampled.getParameters().getPeriod());
        params.setDuration(sampled.getParameters().getDuration());

        return new SignalData(
            sampled.getName() + "_reconstructed_sinc",
            SignalType.OPERATION_RESULT,
            params,
            result
        );
    }

    private double sinc(double t) {
        if (Math.abs(t) < 1e-10) return 1.0;
        return Math.sin(Math.PI * t) / (Math.PI * t);
    }
}
