package dsp.task1.logic.io;


import dsp.task1.logic.Sample;
import dsp.task1.logic.SignalData;
import dsp.task1.logic.SignalParameters;
import dsp.task1.logic.signal.SignalType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TextSignalFileService implements SignalFileService {
    private static final String FILE_HEADER = "CPS_SIGNAL_TEXT";
    private static final String VALUE_TYPE_REAL = "REAL";

    @Override
    public void save(String fileName, SignalData signalData) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(FILE_HEADER);
            writer.println("name=" + signalData.getName());
            writer.println("type=" + signalData.getSignalType().name());
            writer.println("startTime=" + signalData.getParameters().getStartTime());
            writer.println("samplingFrequency=" + signalData.getParameters().getSamplingFrequency());
            writer.println("valueType=" + VALUE_TYPE_REAL);
            writer.println("sampleCount=" + signalData.getSamples().size());
            writer.println("samples:");

            for (Sample sample : signalData.getSamples()) {
                writer.printf(Locale.US, "%.10f%n", sample.getValue());
            }
        }
    }

    @Override
    public SignalData load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String header = reader.readLine();
            if (!FILE_HEADER.equals(header)) {
                throw new IOException("Niepoprawny format pliku tekstowego.");
            }

            String nameLine = reader.readLine();
            String typeLine = reader.readLine();
            String startTimeLine = reader.readLine();
            String samplingFrequencyLine = reader.readLine();
            String valueTypeLine = reader.readLine();
            String sampleCountLine = reader.readLine();
            String samplesHeader = reader.readLine();

            if (nameLine == null || typeLine == null || startTimeLine == null ||
                    samplingFrequencyLine == null || valueTypeLine == null ||
                    sampleCountLine == null || samplesHeader == null) {
                throw new IOException("Plik tekstowy jest niekompletny.");
            }

            String name = nameLine.split("=", 2)[1];
            SignalType signalType = SignalType.valueOf(typeLine.split("=", 2)[1]);
            double startTime = Double.parseDouble(startTimeLine.split("=", 2)[1]);
            double samplingFrequency = Double.parseDouble(samplingFrequencyLine.split("=", 2)[1]);
            String valueType = valueTypeLine.split("=", 2)[1];
            int sampleCount = Integer.parseInt(sampleCountLine.split("=", 2)[1]);

            if (!VALUE_TYPE_REAL.equals(valueType)) {
                throw new IOException("Nieobsługiwany typ wartości w pliku tekstowym.");
            }

            if (!"samples:".equals(samplesHeader)) {
                throw new IOException("Brak sekcji próbek w pliku tekstowym.");
            }

            if (samplingFrequency <= 0) {
                throw new IOException("Niepoprawna częstotliwość próbkowania w pliku tekstowym.");
            }

            if (sampleCount < 0) {
                throw new IOException("Niepoprawna liczba próbek w pliku tekstowym.");
            }

            List<Sample> samples = new ArrayList<>();
            double dt = 1.0 / samplingFrequency;

            for (int i = 0; i < sampleCount; i++) {
                String valueLine = reader.readLine();
                if (valueLine == null) {
                    throw new IOException("Brakuje wartości próbek w pliku tekstowym.");
                }

                double value = Double.parseDouble(valueLine);
                double time = startTime + i * dt;
                samples.add(new Sample(time, value));
            }

            SignalParameters parameters = new SignalParameters();
            parameters.setStartTime(startTime);
            parameters.setSamplingFrequency(samplingFrequency);

            return new SignalData(name, signalType, parameters, samples);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IOException("Niepoprawny format danych w pliku tekstowym.", e);
        }
    }
}
