package dsp.task1.logic;


/**
 * Wspólny interfejs dla wszystkich sygnałów.
 * Określa metodę zwracającą wartość sygnału w danej chwili czasu.
 */
public interface Signal {
    double valueAt(double t);
}
