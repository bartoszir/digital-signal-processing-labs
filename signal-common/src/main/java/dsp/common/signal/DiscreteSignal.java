package dsp.common.signal;

public abstract class DiscreteSignal extends Signal {
    public DiscreteSignal(double amplitude, double startTime, double duration) {
        super(amplitude, startTime, duration);
    }

    public abstract double getValueAtSample(int n);
}
