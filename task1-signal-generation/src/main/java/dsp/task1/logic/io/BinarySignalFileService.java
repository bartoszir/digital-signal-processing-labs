package dsp.task1.logic.io;

import dsp.task1.logic.Sample;
import dsp.task1.logic.SignalData;
import dsp.task1.logic.SignalParameters;
import dsp.task1.logic.signal.SignalType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinarySignalFileService implements SignalFileService {
    private static final String FILE_HEADER = "CPS_SIGNAL_BIN";
    private static final int VALUE_TYPE_REAL = 0;

    @Override
    public void save(String fileName, SignalData signalData) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {

            out.writeUTF(FILE_HEADER);
            out.writeUTF(signalData.getName());
            out.writeUTF(signalData.getSignalType().name());

            SignalParameters params = signalData.getParameters();
            out.writeDouble(params.getStartTime());
            out.writeDouble(params.getDuration());
            out.writeDouble(params.getSamplingFrequency());

            out.writeInt(VALUE_TYPE_REAL);


            List<Sample> samples = signalData.getSamples();
            out.writeInt(samples.size());

            for (Sample sample : samples) {
                out.writeDouble(sample.getValue());
            }
        }
    }

    @Override
    public SignalData load (String fileName) throws IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))) {

            String header = in.readUTF();
            if (!FILE_HEADER.equals(header)) {
                throw new IOException("Niepoprawny format pliku binarnego.");
            }

            String name = in.readUTF();
            SignalType signalType = SignalType.valueOf(in.readUTF());

            double startTime = in.readDouble();
            double duration = in.readDouble();
            if (duration <= 0) {
                throw new IOException("Niepoprawna długość sygnału w pliku.");
            }

            double samplingFrequency = in.readDouble();
            if (samplingFrequency <= 0) {
                throw new IOException("Niepoprawna częstotliwość próbkowania w pliku.");
            }

            int valueType = in.readInt();
            if (valueType != VALUE_TYPE_REAL) {
                throw new IOException("Nieobsługiwany typ wartości w pliku.");
            }

            int sampleCount = in.readInt();
            if (sampleCount < 0) {
                throw new IOException("Niepoprawna liczba próbek w pliku.");
            }


            List<Sample> samples = new ArrayList<>();
            double dt = 1.0 / samplingFrequency;

            for (int i = 0; i < sampleCount; i++) {
                double value = in.readDouble();
                double time = startTime + i * dt;
                samples.add(new Sample(time, value));
            }

            SignalParameters parameters = new SignalParameters();
            parameters.setStartTime(startTime);
            parameters.setDuration(duration);
            parameters.setSamplingFrequency(samplingFrequency);

            return new SignalData(name, signalType, parameters, samples);
        }
    }
}
