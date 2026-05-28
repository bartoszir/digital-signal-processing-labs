package dsp.common.signal;

public abstract class Signal {
    protected final double A;
    protected final double t1;
    protected final double d;

    public Signal(double amplitude, double startTime, double duration) {
        this.A = amplitude;
        this.t1 = startTime;
        this.d = duration;
    }

    public double getAmplitude() {
        return A;
    }

    public double getStartTime() {
        return t1;
    }

    public double getDuration() {
        return d;
    }

    public boolean isActiveAt(double t) {
        return (t >= t1) && (t <= t1 + d);
    }
}
