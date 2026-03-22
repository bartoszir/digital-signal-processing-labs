package dsp.task1.logic;

import dsp.task1.logic.signal.SignalType;

import java.util.List;

public class SignalData {

    private String name;
    private SignalType signalType;
    private SignalParameters parameters;
    private List<Sample> samples;

    public SignalData(String name, SignalType signalType, SignalParameters parameters, List<Sample> samples) {
        this.name = name;
        this.signalType = signalType;
        this.parameters = parameters;
        this.samples = samples;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public SignalParameters getParameters() {
        return parameters;
    }

    public List<Sample> getSamples() {
        return samples;
    }
}
