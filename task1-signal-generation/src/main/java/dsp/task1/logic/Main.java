package dsp.task1.logic;

import dsp.task1.logic.io.BinarySampleFileService;
import dsp.task1.logic.io.TextSampleFileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // test IO
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(0.0, 1.0));
        samples.add(new Sample(0.1, 1.5));
        samples.add(new Sample(0.2, 2.0));

        TextSampleFileService textService = new TextSampleFileService();
        BinarySampleFileService binaryService = new BinarySampleFileService();

        try {
            textService.save("signal.txt", samples);
            binaryService.save("signal.bin", samples);

            List<Sample> loadedTextSamples = textService.load("signal.txt");
            List<Sample> loadedBinarySamples = binaryService.load("signal.bin");

            System.out.println("Odczyt z pliku tekstowego:");
            for (Sample sample : loadedTextSamples) {
                System.out.println(sample);
            }

            System.out.println("\nOdczyt z pliku binarnego:");
            for (Sample sample : loadedBinarySamples) {
                System.out.println(sample);
            }

        } catch (IOException e) {
            System.out.println("Błąd podczas operacji na pliku: " + e.getMessage());
        }
    }
}
