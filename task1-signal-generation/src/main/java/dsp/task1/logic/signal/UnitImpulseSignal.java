package dsp.task1.logic.signal;

public class UnitImpulseSignal extends DiscreteSignal {

    private final int ns;

    public UnitImpulseSignal(double amplitude, double startTime, double duration, int impulseSampleNumber) {
        super(amplitude, startTime, duration);
        this.ns = impulseSampleNumber;
    }

    public int getImpulseSampleNumber() {
        return ns;
    }

    @Override
    public double getValueAtSample(int n) {
        return n == ns ? A : 0.0;
    }
}
