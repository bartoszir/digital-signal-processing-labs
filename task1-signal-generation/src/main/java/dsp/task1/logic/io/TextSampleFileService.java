package dsp.task1.logic.io;


import dsp.task1.logic.Sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class TextSampleFileService implements SampleFileService {

    @Override
    public void save(String fileName, List<Sample> samples) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Sample sample : samples) {
                writer.printf(Locale.US, "%.10f %.10f%n", sample.getTime(), sample.getValue());
            }
        }
    }

    @Override
    public List<Sample> load(String fileName) throws IOException {
        List<Sample> samples = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            scanner.useLocale(Locale.US);

            while (scanner.hasNext()) {
                double time = scanner.nextDouble();
                double value = scanner.nextDouble();
                samples.add(new Sample(time, value));
            }
        }

        return samples;
    }
}
