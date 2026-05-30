package dsp.task3.logic.filter;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.task3.logic.convolution.Convolution;

import static dsp.task3.logic.filter.FirFilterDesigner.*;
import static dsp.task3.logic.filter.WindowType.*;
import static dsp.task3.logic.filter.FilterType.*;

import java.util.List;


/**
 * Serwis do filtracji sygnałów dyskretnych metodą okna przy użyciu filtrów FIR.
 * Projektuje filtr na podstawie podanych parametrów i umożliwia filtrację sygnałów.
 *
 * Obsługiwane typy filtrów: dolnoprzepustowy (LOW_PASS), górnoprzepustowy (HIGH_PASS)
 * Obsługiwane okna: prostokątne (RECTANGULAR), Hamminga (HAMMING)
 *
 * Pipeline projektowania filtru:
 *   1. Obliczenie współczynników LP według wzoru (4) z instrukcji
 *   2. Opcjonalne nałożenie okna Hamminga
 *   3. Opcjonalna konwersja do filtru górnoprzepustowego
 */
public class FirFilterService {
    private final int M;
    private final int K;
    private final WindowType windowType;
    private final FilterType filterType;
    private final List<Double> coefficients;

    /**
     * Tworzy serwis filtracji i projektuje filtr na podstawie podanych parametrów.
     * Współczynniki filtru są obliczane raz przy tworzeniu obiektu.
     *
     * @param M liczba współczynników filtru, musi być nieparzysta i większa od 0
     * @param K parametr częstotliwości odcięcia, fo = fp/K, musi być większy od 0
     * @param windowType typ okna: RECTANGULAR lub HAMMING
     * @param filterType typ filtru: LOW_PASS lub HIGH_PASS
     * @throws IllegalArgumentException gdy parametry są nieprawidłowe
     */
    public FirFilterService(int M, int K, WindowType windowType, FilterType filterType) {
        if (M <= 0 || K <= 0) {
            throw new IllegalArgumentException("Parametry M i K muszą być większe od 0.");
        }
        if (M % 2 == 0) {
            throw new IllegalArgumentException("Parametr M musi być nieparzysty.");
        }
        if (windowType == null || filterType == null) {
            throw new IllegalArgumentException("Typ okna i typ filtru nie mogą być null.");
        }

        this.M = M;
        this.K = K;
        this.windowType = windowType;
        this.filterType = filterType;

        List<Double> h = designLowPass(M, K);
        if (windowType == HAMMING) {
            h = applyHammingWindow(h);
        }
        if (filterType == HIGH_PASS) {
            h = convertToHighPass(h);
        }
        this.coefficients = h;
    }

    /**
     * Zwraca zaprojektowane współczynniki odpowiedzi impulsowej filtru h(n).
     * Może być użyte do wizualizacji charakterystyki filtru.
     */
    public List<Double> getFilterCoefficients() {
        return coefficients;
    }

    /**
     * Filtruje sygnał wejściowy przy użyciu zaprojektowanego filtru.
     * Filtracja realizowana jest poprzez splot sygnału z odpowiedzią impulsową filtru.
     *
     * @param x sygnał wejściowy do filtracji
     * @return przefiltrowany sygnał jako lista próbek o rozmiarze M + N - 1
     */
    public List<Sample> filter(SignalData x) {
        if (x.getSamples().isEmpty()) {
            throw new IllegalArgumentException("Sygnały nie mogą być puste.");
        }
        return Convolution.convolve(coefficients, x);
    }
}
