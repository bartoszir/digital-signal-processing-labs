package dsp.task1.logic.operations;

import dsp.task1.logic.signal.ContinuousSignal;

public class ContinuousOperationResultSignal extends ContinuousSignal {

    private final ContinuousSignal signal1;
    private final ContinuousSignal signal2;
    private final SignalOperationType operationType;

    public ContinuousOperationResultSignal(
            ContinuousSignal signal1,
            ContinuousSignal signal2,
            SignalOperationType operationType
    ) {
        super(
                0,
                Math.min(signal1.getStartTime(), signal2.getStartTime()),
                Math.max(signal1.getStartTime() + signal1.getDuration(), signal2.getStartTime() + signal2.getDuration()) - Math.min(signal1.getStartTime(), signal2.getStartTime())
        );

        this.signal1 = signal1;
        this.signal2 = signal2;
        this.operationType = operationType;
    }

    @Override
    public double getValueAt(double t) {
        double v1 = signal1.getValueAt(t);
        double v2 = signal2.getValueAt(t);

        return switch (operationType) {
            case ADD -> v1 + v2;
            case SUBTRACT -> v1 - v2;
            case MULTIPLY -> v1 * v2;
            case DIVIDE -> {
                if (Math.abs(v2) < 1e-12) {
                    yield Double.NaN;
                }
                yield v1 / v2;
            }
        };
    }
}
