package dsp.common.model;

public class Sample {
    private final double time;
    private final double value;

    public Sample(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("t=%.4f, y=%.4f", time, value);
    }
}
