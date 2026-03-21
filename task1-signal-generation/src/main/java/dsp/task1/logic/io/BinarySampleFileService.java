package dsp.task1.logic.io;

import dsp.task1.logic.Sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinarySampleFileService implements SampleFileService {

    @Override
    public void save(String fileName, List<Sample> samples) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {

            out.writeInt(samples.size());

            for (Sample sample : samples) {
                out.writeDouble(sample.getTime());
                out.writeDouble(sample.getValue());
            }
        }
    }

    @Override
    public List<Sample> load (String fileName) throws IOException {
        List<Sample> samples = new ArrayList<>();

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))) {

            int size = in.readInt();

            for (int i = 0; i < size; i++) {
                double time = in.readDouble();
                double value = in.readDouble();
                samples.add(new Sample(time, value));
            }
        }

        return samples;
    }
}
