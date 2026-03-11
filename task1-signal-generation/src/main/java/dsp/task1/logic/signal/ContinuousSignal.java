package dsp.task1.logic.signal;

public abstract class ContinuousSignal extends Signal {
    public ContinuousSignal(double amplitude, double startTime, double duration) {
        super(amplitude, startTime, duration);
    }

    public abstract double getValueAt(double t);
}
