package dsp.task3.logic.convolution;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConvolutionTest {

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
    void shouldReturnCorrectConvolutionValues() {
        List<Sample> convolvedSamples = Convolution.convolve(hSignal, xSignal);

        List<Double> correctResult = List.of(5.0, 16.0, 34.0, 52.0, 45.0, 28.0);
        for (int i = 0; i < correctResult.size(); i++) {
            assertEquals(correctResult.get(i), convolvedSamples.get(i).getValue());
        }
    }

    @Test
    void shouldThrowExceptionWhenDifferentFpParameters() {
        hSignal.getParameters().setSamplingFrequency(400);
        xSignal.getParameters().setSamplingFrequency(100);

        assertThrows(IllegalArgumentException.class, () -> Convolution.convolve(hSignal, xSignal));
    }

    @Test
    void shouldGiveSameResultWhenInputIsReversed() {
        List<Sample> hx = Convolution.convolve(hSignal, xSignal);

        List<Sample> xh = Convolution.convolve(xSignal, hSignal);

        for (int i = 0; i < hx.size(); i++) {
            assertEquals(hx.get(i).getValue(), xh.get(i).getValue(), 1e-9);
        }
    }
}
