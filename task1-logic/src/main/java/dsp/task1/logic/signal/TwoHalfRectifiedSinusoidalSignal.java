package dsp.task1.logic.signal;

import dsp.common.signal.PeriodicSignal;

public class TwoHalfRectifiedSinusoidalSignal extends PeriodicSignal {

    public TwoHalfRectifiedSinusoidalSignal(double amplitude, double startTime, double duration, double period) {
        super(amplitude, startTime, duration, period);
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        return A * Math.abs(Math.sin(2 * Math.PI / T * (t - t1)));
    }
}
