package dsp.task1.logic.operations;

import dsp.task1.logic.signal.ContinuousSignal;
import dsp.task1.logic.signal.DiscreteSignal;

public class SignalOperations {
    public static ContinuousSignal add(ContinuousSignal s1, ContinuousSignal s2) {
        return new ContinuousOperationResultSignal(s1, s2, SignalOperationType.ADD);
    }

    public static ContinuousSignal subtract(ContinuousSignal s1, ContinuousSignal s2) {
        return new ContinuousOperationResultSignal(s1, s2, SignalOperationType.SUBTRACT);
    }

    public static ContinuousSignal multiply(ContinuousSignal s1, ContinuousSignal s2) {
        return new ContinuousOperationResultSignal(s1, s2, SignalOperationType.MULTIPLY);
    }

    public static ContinuousSignal divide(ContinuousSignal s1, ContinuousSignal s2) {
        return new ContinuousOperationResultSignal(s1, s2, SignalOperationType.DIVIDE);
    }

    public static DiscreteSignal add(DiscreteSignal s1, DiscreteSignal s2) {
        return new DiscreteOperationResultSignal(s1, s2, SignalOperationType.ADD);
    }

    public static DiscreteSignal subtract(DiscreteSignal s1, DiscreteSignal s2) {
        return new DiscreteOperationResultSignal(s1, s2, SignalOperationType.SUBTRACT);
    }

    public static DiscreteSignal multiply(DiscreteSignal s1, DiscreteSignal s2) {
        return new DiscreteOperationResultSignal(s1, s2, SignalOperationType.MULTIPLY);
    }

    public static DiscreteSignal divide(DiscreteSignal s1, DiscreteSignal s2) {
        return new DiscreteOperationResultSignal(s1, s2, SignalOperationType.DIVIDE);
    }
}
