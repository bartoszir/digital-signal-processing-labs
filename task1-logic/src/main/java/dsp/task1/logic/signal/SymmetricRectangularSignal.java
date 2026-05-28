package dsp.task1.logic.signal;

import dsp.common.signal.PeriodicSignal;

public class SymmetricRectangularSignal extends PeriodicSignal {

    private final double kw;

    public SymmetricRectangularSignal(double amplitude, double startTime, double duration, double period, double dutyCycle) {
        super(amplitude, startTime, duration, period);
        this.kw = dutyCycle;
    }

    public double getDutyCycle() {
        return kw;
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        double localTime = (t - t1) % T;
        if (localTime < 0) {
            localTime += T;
        }

        return localTime < kw * T ? A : -A;
    }
}
