package dsp.task3.logic.correlation;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.task3.logic.convolution.Convolution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * Klasa narzędziowa wykonująca korelację wzajemną dwóch sygnałów dyskretnych.
 * Dostępne są dwie implementacje dające identyczne wyniki:
 *   - bezpośrednia: wzór Rhx(n) = suma h(k)*x(k-n)
 *   - przez splot: korelacja h z x = splot h z odwróconym x
 *
 * Uwaga: kolejność argumentów ma znaczenie.
 * Sygnał wynikowy ma M+N-1 próbek, oś czasu zaczyna się od -(N-1)*dt.
 */
public final class Correlation {
    private Correlation() {
        // utility class
    }

    /**
     * Oblicza korelację wzajemną bezpośrednio ze wzoru.
     * @param h pierwszy sygnał
     * @param x drugi sygnał (przesuwany)
     * @return korelacja Rhx jako lista próbek, oś czasu od -(N-1)*dt
     * @throws IllegalArgumentException gdy sygnały są puste lub mają różne fp
     */
    public static List<Sample> correlate(SignalData h, SignalData x) {
        if (h.getSamples().isEmpty() || x.getSamples().isEmpty()) {
            throw new IllegalArgumentException("Sygnały nie mogą być puste.");
        }
        if (Math.abs(h.getParameters().getSamplingFrequency() - x.getParameters().getSamplingFrequency()) > 1e-9) {
            throw new IllegalArgumentException("Sygnały muszą mieć tę samą częstotliwość próbkowania (fp)");
        }
        double fp = h.getParameters().getSamplingFrequency();
        double dt = 1.0 / fp;
        int N = x.getSamples().size();

        List<Double> hValues = h.getSamples().stream()
                .map(Sample::getValue)
                .toList();
        List<Double> xValues = x.getSamples().stream()
                .map(Sample::getValue)
                .toList();

        List<Double> correlatedValues = correlateValues(hValues, xValues);

        return IntStream.range(0, correlatedValues.size())
                .mapToObj(n -> new Sample((n - (N - 1)) * dt, correlatedValues.get(n)))
                .toList();
    }

    /**
     * Prywatna implementacja korelacji operująca na czystych wartościach.
     * Pętla zewnętrzna: n od -(N-1) do M-1
     * Pętla wewnętrzna: k od 0 do M-1, warunek: 0 <= k-n < N
     */
    private static List<Double> correlateValues(List<Double> h, List<Double> x) {
        int M = h.size();
        int N = x.size();
        int resultSize = M + N - 1;

        List<Double> resultList = new ArrayList<>(resultSize);
        double sum = 0;

        for (int n = -(N - 1); n < M; n++) {
            for (int k = 0; k < M; k++) {
                if ((k - n) >= 0 && (k - n) < N) {
                    sum += h.get(k) * x.get(k - n);
                }
            }
            resultList.add(sum);
            sum = 0;
        }

        return resultList;
    }


    /**
     * Oblicza korelację wzajemną przez splot z odwróconym x.
     * Wynik identyczny jak correlate() — różni się tylko implementacją.
     * @param h pierwszy sygnał
     * @param x drugi sygnał (zostanie odwrócony przed splotem)
     * @return korelacja Rhx jako lista próbek, oś czasu od -(N-1)*dt
     * @throws IllegalArgumentException gdy sygnały są puste lub mają różne fp
     */
    public static List<Sample> correlateViaConvolution(SignalData h, SignalData x) {
        if (h.getSamples().isEmpty() || x.getSamples().isEmpty()) {
            throw new IllegalArgumentException("Sygnały nie mogą być puste.");
        }
        if (Math.abs(h.getParameters().getSamplingFrequency() - x.getParameters().getSamplingFrequency()) > 1e-9) {
            throw new IllegalArgumentException("Sygnały muszą mieć tę samą częstotliwość próbkowania (fp)");
        }
        double fp = h.getParameters().getSamplingFrequency();
        double dt = 1.0 / fp;
        int N = x.getSamples().size();

        // odwrocony sygnal x
        List<Double> xValuesReversed = x.getSamples().stream()
                .map(Sample::getValue)
                .toList().reversed();

        List<Double> convolvedValues = Convolution.convolve(xValuesReversed, h).stream()
                .map(Sample::getValue)
                .toList();

        return IntStream.range(0, convolvedValues.size())
                .mapToObj(n -> new Sample((n - (N - 1)) * dt, convolvedValues.get(n)))
                .toList();
    }
}
