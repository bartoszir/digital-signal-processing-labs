package dsp.common.io;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinarySignalFileService implements SignalFileService {
    private static final String FILE_HEADER = "CPS_SIGNAL_BIN";
    private static final int VALUE_TYPE_REAL = 0;
    private static final double NO_PERIOD = -1.0;

    @Override
    public void save(String fileName, SignalData signalData) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(fileName)))) {

            out.writeUTF(FILE_HEADER);
            out.writeUTF(signalData.getName());
            out.writeUTF(signalData.getSignalType().name());

            SignalParameters params = signalData.getParameters();
            out.writeDouble(params.getStartTime());
            double period = signalData.getSignalType() == SignalType.OPERATION_RESULT
                    ? NO_PERIOD : params.getPeriod();
            out.writeDouble(period);
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
    public SignalData load(String fileName) throws SignalFileException, IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(fileName)))) {

            String header = in.readUTF();
            if (!FILE_HEADER.equals(header)) {
                throw new SignalFileException("Niepoprawny format pliku binarnego.");
            }

            String name = in.readUTF();

            SignalType signalType;
            try {
                signalType = SignalType.valueOf(in.readUTF());
            } catch (IllegalArgumentException e) {
                throw new SignalFileException("Nieznany typ sygnału w pliku.", e);
            }

            double startTime = in.readDouble();
            double period = in.readDouble();

            double samplingFrequency = in.readDouble();
            if (samplingFrequency <= 0) {
                throw new SignalFileException("Niepoprawna częstotliwość próbkowania w pliku.");
            }

            int valueType = in.readInt();
            if (valueType != VALUE_TYPE_REAL) {
                throw new SignalFileException("Nieobsługiwany typ wartości w pliku.");
            }

            int sampleCount = in.readInt();
            if (sampleCount < 0) {
                throw new SignalFileException("Niepoprawna liczba próbek w pliku.");
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
            if (period != NO_PERIOD) {
                parameters.setPeriod(period);
            }
            parameters.setSamplingFrequency(samplingFrequency);

            return new SignalData(name, signalType, parameters, samples);
        }
    }
}
