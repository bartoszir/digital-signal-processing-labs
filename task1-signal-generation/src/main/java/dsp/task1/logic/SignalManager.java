package dsp.task1.logic;

import dsp.task1.logic.io.BinarySignalFileService;
import dsp.task1.logic.io.TextSignalFileService;
import dsp.task1.logic.signal.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SignalManager {
    private final Map<String, SignalData> loadedSignals = new LinkedHashMap<>();
    private final BinarySignalFileService binaryFileService = new BinarySignalFileService();
    private final TextSignalFileService textFileService = new TextSignalFileService();

    public void addLoadedSignal(SignalData signalData) {
        String uniqueName = getUniqueSignalName(signalData.getName());
        signalData.setName(uniqueName);
        loadedSignals.put(uniqueName, signalData);
    }

    public Map<String, SignalData> getLoadedSignals() {
        return loadedSignals;
    }

    public SignalData getLoadedSignal(String name) {
        return loadedSignals.get(name);
    }

    public void saveSignalBinary(String fileName, SignalData signalData) throws IOException {
        binaryFileService.save(fileName, signalData);
    }

    public SignalData loadSignalBinary(String fileName) throws IOException {
        SignalData signalData = binaryFileService.load(fileName);
        addLoadedSignal(signalData);
        return signalData;
    }

    public void saveSignalText(String fileName, SignalData signalData) throws IOException {
        textFileService.save(fileName, signalData);
    }

    public SignalData loadSignalText(String fileName) throws IOException {
        SignalData signalData = textFileService.load(fileName);
        addLoadedSignal(signalData);
        return signalData;
    }

    private String getUniqueSignalName(String baseName) {
        if (!loadedSignals.containsKey(baseName)) {
            return baseName;
        }

        int counter = 1;
        String newName;

        do {
            newName = baseName + "(" + counter + ")";
            counter++;
        } while (loadedSignals.containsKey(newName));

        return newName;
    }

    public List<Sample> generateSignalSamples(SignalType type, SignalParameters p) {

        Signal signal = switch (type) {
            case UNIFORM_NOISE ->
                    new UniformNoiseSignal(p.getAmplitude(), p.getStartTime(), p.getDuration());

            case GAUSSIAN_NOISE ->
                    new GaussianNoiseSignal(p.getAmplitude(), p.getStartTime(), p.getDuration());

            case SINUSOIDAL_SIGNAL ->
                    new SinusoidalSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getPeriod());

            case ONE_HALF_RECTIFIED_SINUSOIDAL_SIGNAL ->
                    new OneHalfRectifiedSinusoidalSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getPeriod());

            case TWO_HALF_RECTIFIED_SINUSOIDAL_SIGNAL ->
                    new TwoHalfRectifiedSinusoidalSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getPeriod());

            case RECTANGULAR_SIGNAL ->
                    new RectangularSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(),
                            p.getPeriod(), p.getKw());

            case SYMMETRIC_RECTANGULAR_SIGNAL ->
                    new SymmetricRectangularSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(),
                            p.getPeriod(), p.getKw());

            case TRIANGULAR_SIGNAL ->
                    new TriangularSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(),
                            p.getPeriod(), p.getKw());

            case UNIT_JUMP_SIGNAL ->
                    new UnitJumpSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getTs());

            case UNIT_IMPULSE_SIGNAL ->
                    new UnitImpulseSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getNs());

            case IMPULSE_NOISE_SIGNAL ->
                    new ImpulseNoiseSignal(p.getAmplitude(), p.getStartTime(), p.getDuration(), p.getP());

            default -> throw new IllegalArgumentException("Unsupported signal type");
        };

        return SignalGenerator.generate(signal, p.getSamplingFrequency());
    }
}
