package dsp.task1.logic;

import dsp.task1.logic.signal.*;

import java.util.ArrayList;
import java.util.List;

public class SignalManager {

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
