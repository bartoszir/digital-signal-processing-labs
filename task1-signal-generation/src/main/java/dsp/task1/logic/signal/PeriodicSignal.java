package dsp.task1.logic.signal;

public abstract class PeriodicSignal extends ContinuousSignal {
    protected final double T;

    public PeriodicSignal(double amplitude, double startTime, double duration, double period) {
        super(amplitude, startTime, duration);
        this.T = period;
    }

    public double getPeriod() {
        return T;
    }
}
