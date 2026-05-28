package dsp.task1.logic.operations;

import dsp.common.signal.DiscreteSignal;

public class DiscreteOperationResultSignal extends DiscreteSignal {
    private final DiscreteSignal signal1;
    private final DiscreteSignal signal2;
    private final SignalOperationType operationType;

    public DiscreteOperationResultSignal(
            DiscreteSignal signal1,
            DiscreteSignal signal2,
            SignalOperationType operationType
    ) {
        super(
                0,
                Math.min(signal1.getStartTime(), signal2.getStartTime()),
                Math.max(signal1.getStartTime() + signal1.getDuration(),
                        signal2.getStartTime() + signal2.getDuration())
                        - Math.min(signal1.getStartTime(), signal2.getStartTime())
        );

        this.signal1 = signal1;
        this.signal2 = signal2;
        this.operationType = operationType;
    }

    @Override
    public double getValueAtSample(int n) {
        double v1 = signal1.getValueAtSample(n);
        double v2 = signal2.getValueAtSample(n);

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
