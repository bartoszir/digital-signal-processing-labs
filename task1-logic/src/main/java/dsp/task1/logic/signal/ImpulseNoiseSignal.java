package dsp.task1.logic.signal;

import dsp.common.signal.DiscreteSignal;

import java.util.Random;

public class ImpulseNoiseSignal extends DiscreteSignal {

    private final double p;
    private final Random random;

    public ImpulseNoiseSignal(double amplitude, double startTime, double duration, double probability) {
        super(amplitude, startTime, duration);
        this.p = probability;
        this.random = new Random();
    }

    public double getProbability() {
        return p;
    }

    @Override
    public double getValueAtSample(int n) {
        return random.nextDouble() < p ? A : 0.0;
    }
}
