package dsp.task1.logic;

import dsp.task1.logic.io.BinarySignalFileService;
import dsp.task1.logic.io.TextSignalFileService;
import dsp.task1.logic.signal.SignalType;
import dsp.task1.logic.signal.SinusoidalSignal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        SignalParameters params = new SignalParameters();
        params.setStartTime(0.0);
        params.setSamplingFrequency(100.0);

        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(0.0, 1.0));
        samples.add(new Sample(0.01, 2.0));
        samples.add(new Sample(0.02, 3.0));

        SignalData data = new SignalData("testSignal", SignalType.SINUSOIDAL_SIGNAL, params, samples);

        TextSignalFileService service = new TextSignalFileService();

        service.save("test.txt", data);

        SignalData loaded = service.load("test.txt");

        System.out.println(loaded.getName());
        System.out.println(loaded.getSignalType());
        for (Sample s : loaded.getSamples()) {
            System.out.println(s.getTime() + " " + s.getValue());
        }
    }
}
