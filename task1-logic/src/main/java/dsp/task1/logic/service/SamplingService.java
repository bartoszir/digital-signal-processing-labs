package dsp.task1.logic.service;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;

import java.util.ArrayList;
import java.util.List;

public class SamplingService {

    public SignalData sample(SignalData original, double targetFs) {
        double originalFs = original.getParameters().getSamplingFrequency();

        if (targetFs <= 0) {
            throw new IllegalArgumentException(
                "Częstotliwość próbkowania musi być większa od 0.");
        }
        if (targetFs > originalFs) {
            throw new IllegalArgumentException(
                "Częstotliwość próbkowania (" + targetFs + " Hz) nie może być większa " +
                "niż częstotliwość oryginalnego sygnału (" + originalFs + " Hz).");
        }

        int step = (int) Math.round(originalFs / targetFs);
        List<Sample> originalSamples = original.getSamples();
        List<Sample> sampledSamples = new ArrayList<>();

        for (int i = 0; i < originalSamples.size(); i += step) {
            sampledSamples.add(originalSamples.get(i));
        }

        SignalParameters params = new SignalParameters();
        params.setStartTime(original.getParameters().getStartTime());
        params.setSamplingFrequency(targetFs);
        params.setPeriod(original.getParameters().getPeriod());
        params.setDuration(original.getParameters().getDuration());

        return new SignalData(
            original.getName() + "_sampled",
            SignalType.OPERATION_RESULT,
            params,
            sampledSamples
        );
    }
}
