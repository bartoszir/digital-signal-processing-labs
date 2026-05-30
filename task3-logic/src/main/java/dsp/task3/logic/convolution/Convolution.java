package dsp.task3.logic.convolution;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Klasa narzędziowa wykonująca splot sydkretny dwóch sygnałów.
 * Implementuje wzór (2): (h*x)(n) = suma k=0..M-1 z h(k)*x(n-k)
 * Warunek: oba sygnały muszą mieć tę samą częstotliwość próbkowania (fp).
 * Sygnał wynikowy ma M+N-1 próbek i oś czasu zaczynającą się od t=0.
 */
public final class Convolution {

    private Convolution() {
        // klasa utility, nie mozna utworzyc instancji
    }

    private static List<Double> convolveValues(List<Double> h, List<Double> x) {
        int M = h.size();
        int N = x.size();
        int resultSize = M + N - 1;

        List<Double> resultList = new ArrayList<>(resultSize);
        double sum = 0;

        for (int n = 0; n < resultSize; n++) {
            for (int k = 0; k <= n; k++) {
                if ((n - k) < N && k < M) {
                    sum += h.get(k) * x.get(n - k);
                }
            }
            resultList.add(n, sum);
            sum = 0;
        }

        return resultList;
    }


    /**
     * Oblicza splot dwóch sygnałów dyskretnych.
     * @param h pierwszy sygnał wejściowy
     * @param x drugi sygnał wejściowy
     * @return splot h*x jako lista próbek o rozmiarze M+N-1
     * @throws IllegalArgumentException gdy sygnały są puste lub mają różne fp
     */
    public static List<Sample> convolve(SignalData h, SignalData x) {
        if (h.getSamples().isEmpty() || x.getSamples().isEmpty()) {
            throw new IllegalArgumentException("Sygnały nie mogą być puste.");
        }
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


    /**
     * Oblicza splot współczynników filtru z sygnałem wejściowym.
     * Używane w filtracji FIR oraz korelacji przez splot.
     * @param hCoefficients współczynniki filtru h(n)
     * @param x sygnał wejściowy — fp pobierane z tego sygnału
     * @return splot jako lista próbek o rozmiarze M+N-1
     * @throws IllegalArgumentException gdy lista lub sygnał są puste
     */
    public static List<Sample> convolve(List<Double> hCoefficients, SignalData x) {
        if (hCoefficients.isEmpty() || x.getSamples().isEmpty()) {
            throw new IllegalArgumentException("Sygnały nie mogą być puste.");
        }
        double fp = x.getParameters().getSamplingFrequency();
        double dt = 1.0 / fp;

        List<Double> xValues = x.getSamples().stream()
                .map(Sample::getValue)
                .toList();

        List<Double> convolvedValues = convolveValues(hCoefficients, xValues);

        return IntStream.range(0, convolvedValues.size())
                .mapToObj(n -> new Sample(n * dt, convolvedValues.get(n)))
                .toList();
    }
}