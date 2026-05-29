package dsp.task3.logic.filter;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirFilterServiceTest {
    SignalData xSignal;

    @BeforeEach
    void setup() {
        List<Sample> xSamples = List.of(new Sample(0.0, 1.0), new Sample(0.01, 2.0), new Sample(0.02, 3.0), new Sample(0.03, 4.0));
        SignalParameters xParameters = new SignalParameters();
        xParameters.setSamplingFrequency(100);
        xSignal = new SignalData("x", SignalType.SINUSOIDAL_SIGNAL, xParameters, xSamples);
    }

    @Test
    void filterTest() {
        int M = 7, K = 8;
        int N = xSignal.getSamples().size();
        FirFilterService filterService = new FirFilterService(M, K, WindowType.RECTANGULAR, FilterType.LOW_PASS);
        assertEquals(M + N - 1, filterService.filter(xSignal).size());
    }
}
