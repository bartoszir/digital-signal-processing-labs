package dsp.task1.logic.io;

import dsp.task1.logic.SignalData;

import java.io.IOException;

/*
======== FORMAT PLIKU ========
CPS_SIGNAL_TEXT
name=testSignal
type=SINUSOIDAL_SIGNAL
startTime=0.0
samplingFrequency=100.0
valueType=REAL
sampleCount=3
samples:
1.0
2.0
3.0
 */

public interface SignalFileService {

    void save(String fileName, SignalData signalData) throws IOException;

    SignalData load(String fileName) throws IOException;
}
