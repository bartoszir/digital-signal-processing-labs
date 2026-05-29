package dsp.task3.logic.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * Klasa narzędziowa do projektowania filtrów SOI (o skończonej odpowiedzi impulsowej, ang. FIR - Finite Impulse
 * Response) metodą okna.
 *
 * Parametry filtru:
 *   M — liczba współczynników (rząd filtru), musi być nieparzyste
 *   K — parametr częstotliwości odcięcia: fo = fp/K
 *
 *   Posiada metody:
 *   designLowPass(M, K)      — współczynniki filtru dolnoprzepustowego (okno prostokątne)
 *   applyHammingWindow(h)    — zastosowanie okna Hamminga na współczynnikach filtru
 *   convertToHighPass(h)     — przekształcenie filtru LP na górnoprzepustowy
 */
public final class FirFilterDesigner {

    private FirFilterDesigner() {
        // utility class
    }

    /**
     * Metoda tworząca filtr dolnoprzepustowy. Filtr będzie przepuszczał wartości od 0 od f0.
     * @param M liczba współczynników filtru (rząd filtru). Musi być nieparzyste.
     * @param K parametr częstotliwości odcięcia. Częstotliwość odcięcia wynosi f0=fp/K.
     * @return lista współczynników h(n)
     */
    public static List<Double> designLowPass(int M, int K) {
        if (M % 2 == 0) {
            throw new IllegalArgumentException("M musi być nieparzyste.");
        }

        List<Double> hValues = new ArrayList<>();
        double h;

        for (int n = 0; n < M; n++) {
            if (n == (M - 1) / 2) {
                hValues.add(2.0 / K);
                continue;
            }
            h = Math.sin(2 * Math.PI * (n - (M - 1) / 2) / K) / (Math.PI * (n - (M - 1) / 2));
            hValues.add(h);
        }

        return hValues;
    }

    /**
     * Funkcja nakładająca okno Hamminga na współczynniki filtru.
     * (*) We wzorze w instrukcji podany jest mianownik jako M, aczkolwiek wtedy wyniki są niesymetryczne,
     * dlatego mianownik w tej implementacji jest liczony jako M-1
     * w(n) = 0.53836 - 0.46164 * cos(2𝛑n / (M-1))
     */
    public static List<Double> applyHammingWindow(List<Double> h) {
        int M = h.size();

        // we wzorze zmienione dzielenie przez M na dzielenie przez M-1, bo inaczej wartosci byly niesymetryczne
        return IntStream.range(0, M)
                .mapToObj(n -> h.get(n) * (0.53836 - 0.46164 * Math.cos(2 * Math.PI * n / (M - 1))))
                .toList();
    }

    /**
     * Przekształca współczynniki filtru dolnoprzepustowego na górnoprzepustowy
     * poprzez przemnożenie h(n) przez (-1)^n.
     * @param h współczynniki filtru LP (po zastosowaniu okna)
     * @return współczynniki filtru HP
     */
    public static List<Double> convertToHighPass(List<Double> h) {
        return IntStream.range(0, h.size())
                .mapToObj(n -> h.get(n) * (n % 2 == 0 ? 1.0 : -1.0))
                .toList();
    }
}
