package dsp.task1.logic.signal;


public class SinusoidalSignal extends PeriodicSignal {
    public SinusoidalSignal(double amplitude, double startTime, double duration, double period) {
        super(amplitude, startTime, duration, period);
    }

    @Override
    public double getValueAt(double t) {
        return A * Math.sin((2 * Math.PI / T) * (t - t1));
    }
}
