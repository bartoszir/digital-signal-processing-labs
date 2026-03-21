package dsp.task1.logic.signal;

public class OneHalfRectifiedSinusoidalSignal extends PeriodicSignal {

    public OneHalfRectifiedSinusoidalSignal(double amplitude, double startTime, double duration, double period) {
        super(amplitude, startTime, duration, period);
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        double sineValue = Math.sin(2 * Math.PI / T * (t - t1));
        return 0.5 * A * (sineValue + Math.abs(sineValue));
    }
}
