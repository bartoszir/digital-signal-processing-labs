package dsp.task3.logic.correlation;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CorrelationTest {
    static SignalData hSignal, xSignal;

    @BeforeEach
    void setup() {
        List<Sample> hSamples = List.of(new Sample(0.0, 1.0), new Sample(0.01, 2.0), new Sample(0.02, 3.0), new Sample(0.03, 4.0));
        SignalParameters hParameters = new SignalParameters();
        hParameters.setSamplingFrequency(100);
        hSignal = new SignalData("h", SignalType.SINUSOIDAL_SIGNAL, hParameters, hSamples);

        List<Sample> xSamples = List.of(new Sample(0.0, 5.0), new Sample(0.01, 6.0), new Sample(0.02, 7.0));
        SignalParameters xParameters = new SignalParameters();
        xParameters.setSamplingFrequency(100);
        xSignal = new SignalData("x", SignalType.SINUSOIDAL_SIGNAL, xParameters, xSamples);
    }

    @Test
    void bothMethodsShouldReturnSameValue() {
        List<Sample> correlated = Correlation.correlate(hSignal, xSignal);
        List<Sample> correlatedViaConvolution = Correlation.correlateViaConvolution(hSignal, xSignal);

        System.out.println("correlate: " + correlated.stream().map(Sample::getValue).toList());
        System.out.println("viaConv: " + correlatedViaConvolution.stream().map(Sample::getValue).toList());

        for (int i = 0; i < correlated.size(); i++) {
            assertEquals(correlated.get(i).getValue(),
                    correlatedViaConvolution.get(i).getValue(), 1e-9);
        }
    }
}
