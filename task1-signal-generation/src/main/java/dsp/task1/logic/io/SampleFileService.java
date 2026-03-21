package dsp.task1.logic.io;

import dsp.task1.logic.Sample;

import java.io.IOException;
import java.util.List;

public interface SampleFileService {

    void save(String fileName, List<Sample> samples) throws IOException;

    List<Sample> load(String fileName) throws IOException;
}
