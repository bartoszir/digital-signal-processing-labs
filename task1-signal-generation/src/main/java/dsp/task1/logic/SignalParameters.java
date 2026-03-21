package dsp.task1.logic;

public class SignalParameters {

    // wspólne
    private double amplitude = 2.0;
    private double startTime = 0.0;
    private double duration = 10.0;
    private double samplingFrequency = 100.0;

    // dla sinusów / prostokątnych / trójkątnych
    private double period = 4.0;
    private double kw;

    // dla skoku
    private double ts;

    // dla impulsu jednostkowego
    private int ns;

    // dla szumu impulsowego
    private double p;

    // --- gettery i settery ---


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
