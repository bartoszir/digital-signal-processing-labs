package dsp.task1.logic.signal;

public class Test extends ContinuousSignal {
    public Test() {
        super(1, 0.0, 10.0);
    }

    @Override
    public double getValueAt(double t) {
        if (!isActiveAt(t)) {
            return 0.0;
        }

        return -0.5;
    }
}
