package dsp.task1.logic.signal;

import dsp.common.signal.ContinuousSignal;

import java.util.Random;

public class GaussianNoiseSignal extends ContinuousSignal {

    private final Random random;

    public GaussianNoiseSignal(double amplitude, double startTime, double duration) {
        super(amplitude, startTime, duration);
        this.random = new Random();
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        return A * random.nextGaussian();
    }
}
