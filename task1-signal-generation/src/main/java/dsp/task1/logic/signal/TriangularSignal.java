package dsp.task1.logic.signal;

public class TriangularSignal extends PeriodicSignal {

    private final double kw;

    public TriangularSignal(double amplitude, double startTime, double duration, double period, double dutyCycle) {
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

        double riseTime = kw * T;

        if (localTime < riseTime) {
            return (A / riseTime) * localTime;
        } else {
            return A - (A / (T - riseTime)) * (localTime - riseTime);
        }
    }
}
