package dsp.common.io;

import dsp.common.model.SignalData;

import java.io.IOException;

public interface SignalFileService {

    void save(String fileName, SignalData signalData) throws IOException;

    SignalData load(String fileName) throws SignalFileException, IOException;
}
