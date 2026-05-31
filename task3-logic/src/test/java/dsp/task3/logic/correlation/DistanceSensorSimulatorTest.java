package dsp.task3.logic.correlation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceSensorSimulatorTest {
    double signalSpeed, objectSpeed, initialDistance, samplingFreq, reportingPeriod, f1, f2;
    int bufferSize;

    @BeforeEach
    void setup() {
        signalSpeed = 1000.0;
        objectSpeed = 0.0;
        initialDistance = 10.0;
        samplingFreq = 1000.0;
        bufferSize = 256;
        reportingPeriod = 1.0;
        f1 = 50.0;
        f2 = 120.0;
    }

    @Test
    void simulateShouldMeasureCorrectDistance() {
        DistanceSensorSimulator simulator = new DistanceSensorSimulator(
                signalSpeed, objectSpeed, initialDistance, samplingFreq, bufferSize, reportingPeriod, f1, f2
        );

        SimulationResult result = simulator.simulate(0.0);
        assertEquals(10.0, result.getRealDistance(), 1e-9);
        assertEquals(10.0, result.getMeasuredDistance(), 0.5);
    }

    @Test
    void simulateShouldMeasureCorrectDistanceForMovingObject() {
        DistanceSensorSimulator simulator = new DistanceSensorSimulator(
                1000.0, 0.0, 2.0, 1000.0, 512, 1.0, 5.0, 12.0
        );
        SimulationResult result = simulator.simulate(0.0);
        // delayInSamples = round(2*2.0/1000*1000) = 4 próbki
        assertEquals(2.0, result.getRealDistance(), 1e-9);
        assertEquals(2.0, result.getMeasuredDistance(), 0.5);
    }

    @Test
    void simulateStepsShouldReturnCorrectNumberOfResults() {
        DistanceSensorSimulator simulator = new DistanceSensorSimulator(
                signalSpeed, objectSpeed, initialDistance, samplingFreq, bufferSize, reportingPeriod, f1, f2
        );

        int steps = 5;
        var results = simulator.simulateSteps(steps);

        assertEquals(steps, results.size());
        // kazdy krok ma inna rzeczywista odleglosc (obiekt stoi wiec ta sama)
        results.forEach(r -> assertEquals(10.0, r.getRealDistance(), 1e-9));
    }
}
