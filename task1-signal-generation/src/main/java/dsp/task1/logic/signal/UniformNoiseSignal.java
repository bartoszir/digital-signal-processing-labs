package dsp.task1.logic.signal;

import java.util.Random;

public class UniformNoiseSignal extends ContinuousSignal {

    private final Random random;

    public UniformNoiseSignal(double amplitude, double startTime, double duration) {
        super(amplitude, startTime, duration);
        this.random = new Random();
    }

    // random.nextDouble() zwraca losowa liczbe z zakresu [0,1). wiec najpierw mnozymy zakres *2A, czyli mamy
    // [0, 1) -> [0, 2A) i przesuwamy w lewo o A czyli -A i mamy [0, 2A) -> [-A, A)
    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }
        return -A + 2 * A * random.nextDouble();
    }
}
