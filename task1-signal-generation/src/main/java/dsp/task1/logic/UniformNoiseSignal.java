package dsp.task1.logic;


import java.util.Random;

/**
 * Klasa reprezentująca szum jednostajny.
 * Wartości losowane są z przedziału <-A, A>.
 */
public class UniformNoiseSignal implements Signal {

    private final double A;
    private final Random random;

    public UniformNoiseSignal(double A) {
        this.A = A;
        this.random = new Random();
    }

    @Override
    public double valueAt(double t) {
        return -A + 2 * A * random.nextDouble();
    }
}
