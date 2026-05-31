package dsp.task3.logic.correlation;

import java.util.List;


/**
 * Wynik pojedynczego kroku symulacji czujnika odległości.
 * Zawiera rzeczywistą i zmierzoną odległość oraz sygnały do wizualizacji.
 *
 * Pola:
 *   realDistance — rzeczywista odległość obiektu [m]
 *   measuredDistance — odległość wyznaczona przez czujnik [m]
 *   probingSignal — sygnał sondujący (bufor N próbek)
 *   returnSignal — sygnał zwrotny (opóźniony sygnał sondujący)
 *   correlationValues — wartości korelacji wzajemnej (rozmiar 2N-1)
 */
public class SimulationResult {
    final double realDistance;
    final double measuredDistance;
    final List<Double> probingSignal;
    final List<Double> returnSignal;
    final List<Double> correlationValues;

    public SimulationResult(double realDistance, double measuredDistance, List<Double> probingSignal,
                            List<Double> returnSignal, List<Double> correlationValues) {
        this.realDistance = realDistance;
        this.measuredDistance = measuredDistance;
        this.probingSignal = probingSignal;
        this.returnSignal = returnSignal;
        this.correlationValues = correlationValues;
    }

    public double getRealDistance() { return realDistance; }

    public double getMeasuredDistance() { return measuredDistance; }

    public List<Double> getProbingSignal() { return probingSignal; }

    public List<Double> getReturnSignal() { return returnSignal; }

    public List<Double> getCorrelationValues() { return correlationValues; }
}
