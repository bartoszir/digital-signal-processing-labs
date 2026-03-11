package dsp.task1.logic.signal;

public class UnitStepSignal extends ContinuousSignal {

    private final double ts;

    public UnitStepSignal(double amplitude, double startTime, double duration, double stepTime) {
        super(amplitude, startTime, duration);
        this.ts = stepTime;
    }

    public double getStepTime() {
        return ts;
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        if (t > ts) {
            return A;
        } else if (t == ts) {
            return A / 2.0;
        } else {
            return 0.0;
        }
    }
}
