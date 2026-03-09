package dsp.task1.logic;


/**
 * Klasa reprezentująca sygnał sinusoidalny.
 */
public class SinusoidalSignal implements Signal {
    private final double A; // Amplituda
    private final double T; // Okres
    private final double t1; // Okres poczatkowy

    public SinusoidalSignal(double A, double T, double t1) {
        this.A = A;
        this.T = T;
        this.t1 = t1;
    }

    @Override
    public double valueAt(double t) {
        return A * Math.sin((2 * Math.PI / T) * (t - t1));
    }
}
