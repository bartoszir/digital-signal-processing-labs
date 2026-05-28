package dsp.common.model;

public class SignalParameters {

    private double amplitude = 2.0;
    private double startTime = 0.0;
    private double duration = 10.0;
    private double samplingFrequency = 100.0;

    private double period = 4.0;
    private double kw;

    private double ts;

    private int ns;

    private double p;

    public double getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getSamplingFrequency() {
        return samplingFrequency;
    }

    public void setSamplingFrequency(double samplingFrequency) {
        this.samplingFrequency = samplingFrequency;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getKw() {
        return kw;
    }

    public void setKw(double kw) {
        this.kw = kw;
    }

    public double getTs() {
        return ts;
    }

    public void setTs(double ts) {
        this.ts = ts;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }
}
