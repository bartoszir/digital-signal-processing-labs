package dsp.task3.logic.correlation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


/**
 * Symulator korelacyjnego czujnika odległości.
 * Symuluje działanie radaru który wysyła sygnał sondujący, odbiera echo
 * i wyznacza odległość od obiektu na podstawie analizy korelacyjnej.
 *
 * Sygnał sondujący jest sumą dwóch sinusów o częstotliwościach f1 i f2.
 * Sygnał zwrotny to sygnał sondujący opóźniony o czas 2*d/V (droga tam i z powrotem).
 * Odległość wyznaczana jest z położenia maksimum korelacji wzajemnej.
 *
 * Parametry:
 *   signalSpeed — prędkość rozchodzenia się sygnału V [m/s]
 *   objectSpeed — prędkość poruszającego się obiektu [m/s]
 *   initialDistance — początkowa odległość obiektu d0 [m]
 *   samplingFreq — częstotliwość próbkowania fp [Hz]
 *   bufferSize — liczba próbek w buforze N
 *   reportingPeriod — okres raportowania między krokami symulacji [s]
 *   f1, f2 — częstotliwości składowych sygnału sondującego [Hz]
 */
public class DistanceSensorSimulator {
    private final double signalSpeed; // prędkość sygnału V
    private final double objectSpeed; // prędkość obiektu
    private final double initialDistance; // początkowa odległość d0
    private final double samplingFreq; // częstotliwość próbkowania fp
    private final int bufferSize; // długość bufora N
    private final double reportingPeriod; // okres raportowania
    private final double f1, f2;

    public DistanceSensorSimulator(double signalSpeed, double objectSpeed, double initialDistance, double samplingFreq,
                                   int bufferSize, double reportingPeriod, double f1, double f2) {
        this.signalSpeed = signalSpeed;
        this.objectSpeed = objectSpeed;
        this.initialDistance = initialDistance;
        this.samplingFreq = samplingFreq;
        this.bufferSize = bufferSize;
        this.reportingPeriod = reportingPeriod;
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getSignalSpeed() { return signalSpeed; }

    public double getObjectSpeed() { return objectSpeed; }

    public double getInitialDistance() { return initialDistance; }

    public double getSamplingFreq() { return samplingFreq; }

    public int getBufferSize() { return bufferSize; }

    public double getReportingPeriod() { return reportingPeriod; }


    // generuje sygnal sondujacy (suma 2+ sinusow)
    private List<Double> generateProbingSignal() {
        return IntStream.range(0, bufferSize)
                .mapToObj(n -> Math.sin(2 * Math.PI * f1 * n / samplingFreq) +
                        Math.sin(2 * Math.PI * f2 * n / samplingFreq))
                .toList();
    }

    // generuje sygnal zwrotny (przesuniety o delay probek)
    private List<Double> generateReturnSignal(List<Double> probing, int delayInSamples) {
        return IntStream.range(0, bufferSize)
                .mapToObj(n -> n < delayInSamples ? 0.0 : probing.get(n - delayInSamples))
                .toList();
    }

    /**
     * Oblicza rzeczywistą odległość obiektu w chwili t.
     * d(t) = initialDistance + objectSpeed * t
     */
    public double getRealDistance(double t) {
        return initialDistance + objectSpeed * t;
    }


    /**
     * Wykonuje jeden krok symulacji dla chwili t.
     * Generuje sygnały, oblicza korelację i wyznacza zmierzoną odległość.
     * @param t chwila czasowa symulacji [s]
     * @return wynik symulacji zawierający rzeczywistą i zmierzoną odległość oraz sygnały
     */
    public SimulationResult simulate(double t) {

        // 1. Oblicza rzeczywista odleglosc
        double realDistance = getRealDistance(t);

        // 2. oblicza opóźnienie w próbkach
        double delay = 2 * realDistance / signalSpeed;
        int delayInSamples = (int) Math.round(delay * samplingFreq);

        // 3. generuje sygnał sondujący
        List<Double> probingSignal = generateProbingSignal();

        // 4. generuje sygnał zwrotny
        List<Double> returnSignal = generateReturnSignal(probingSignal, delayInSamples);

        // 5. oblicza korelacje
        List<Double> correlationResult = Correlation.correlate(probingSignal, returnSignal);
//            List<Double> correlationResult = Correlation.correlate(returnSignal, probingSignal);

        // 6. znajduje maksimum w prawej połowie korelacji
        int center = bufferSize - 1; // potrzebne zeby szukac minimum tylko w prawej polowie korelacji
        int maxIndex = IntStream.range(0, center + 1)
                .reduce((a, b) -> correlationResult.get(a) > correlationResult.get(b) ? a : b)
                .getAsInt();

        // 7. oblicza zmierzona odleglosc
        int delayFound = center - maxIndex;
        double measuredDelay = delayFound / samplingFreq;
        double measuredDistance = measuredDelay * signalSpeed / 2;

        return new SimulationResult(realDistance, measuredDistance, probingSignal, returnSignal, correlationResult);
    }


    /**
     * Wykonuje zadaną liczbę kroków symulacji co reportingPeriod sekund.
     * @param steps liczba kroków symulacji
     * @return lista wyników dla kolejnych chwil: 0, reportingPeriod, 2*reportingPeriod, ...
     */
    public List<SimulationResult> simulateSteps(int steps) {
        return IntStream.range(0, steps)
                .mapToObj(i -> simulate(i * reportingPeriod))
                .toList();
    }
}
