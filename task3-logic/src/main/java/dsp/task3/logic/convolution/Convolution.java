package dsp.task3.logic.convolution;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * klasa wykonująca splot dwóch sygnałów dyskretnych.
 * Warunek: oba sygnały muszą mieć tę samą częstotliwość próbkowania (fp)
 */
public final class Convolution {

    private Convolution() {
        // klasa utility, nie mozna utworzyc instancji
    }

    private static List<Double> convolveValues(List<Double> h, List<Double> x) {
        int M = h.size();
        int N = x.size();
        int resultSize = M + N - 1;

        List<Double> result_list = new ArrayList<>(resultSize);
        double sum = 0;

        for (int n = 0; n < resultSize; n++) {
            for (int k = 0; k <= n; k++) {
                if ((n - k) < N && k < M) {
                    sum += h.get(k) * x.get(n - k);
                }
            }
            result_list.add(n, sum);
            sum = 0;
        }

        return result_list;
    }

    public static List<Sample> convolve(SignalData h, SignalData x) {

        // porównywanie nie poprzez != z uwagi na problemy z precyzją zmiennoprzecinkową w takich porównaniach
        if (Math.abs(h.getParameters().getSamplingFrequency() - x.getParameters().getSamplingFrequency()) > 1e-9) {
            throw new IllegalArgumentException("Sygnały muszą mieć tę samą częstotliwość próbkowania (fp)");
        }
        double fp = h.getParameters().getSamplingFrequency(); // czestotliwosc probkowania
        double dt = 1.0 / fp; // okres czyli czas pojedynczego kroku

        List<Double> hValues = h.getSamples().stream()
                .map(Sample::getValue)
                .toList();

        List<Double> xValues = x.getSamples().stream()
                .map(Sample::getValue)
                .toList();

        List<Double> convolvedValues = convolveValues(hValues, xValues);

        return IntStream.range(0, convolvedValues.size())
                .mapToObj(n -> new Sample(n * dt, convolvedValues.get(n)))
                .toList();
    }
}