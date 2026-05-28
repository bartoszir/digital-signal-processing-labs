package dsp.common.model;

public class ConversionSession {

    private SignalData originalSignal;
    private SignalData sampledSignal;
    private SignalData quantizedSignal;
    private SignalData reconstructedSignal;

    public SignalData getOriginalSignal() { return originalSignal; }
    public void setOriginalSignal(SignalData originalSignal) { this.originalSignal = originalSignal; }

    public SignalData getSampledSignal() { return sampledSignal; }
    public void setSampledSignal(SignalData sampledSignal) { this.sampledSignal = sampledSignal; }

    public SignalData getQuantizedSignal() { return quantizedSignal; }
    public void setQuantizedSignal(SignalData quantizedSignal) { this.quantizedSignal = quantizedSignal; }

    public SignalData getReconstructedSignal() { return reconstructedSignal; }
    public void setReconstructedSignal(SignalData reconstructedSignal) { this.reconstructedSignal = reconstructedSignal; }

    public void reset() {
        sampledSignal = null;
        quantizedSignal = null;
        reconstructedSignal = null;
    }
}
