package dsp.task1.logic.operations;

import dsp.task1.logic.model.Sample;

import java.util.ArrayList;
import java.util.List;

public class SampleOperations {
    public static List<Sample> execute(List<Sample> samples1,
                                       List<Sample> samples2,
                                       SignalOperationType operationType) {

        if (samples1.size() != samples2.size()) {
            throw new IllegalArgumentException("Sygnały muszą mieć taką samą liczbę próbek.");
        }

        List<Sample> result = new ArrayList<>();

        for (int i = 0; i < samples1.size(); i++) {
            Sample s1 = samples1.get(i);
            Sample s2 = samples2.get(i);

            double value1 = s1.getValue();
            double value2 = s2.getValue();

            double resultValue = switch (operationType) {
                case ADD -> value1 + value2;
                case SUBTRACT -> value1 - value2;
                case MULTIPLY -> value1 * value2;
                case DIVIDE -> {
                    if (Math.abs(value2) < 1e-12) {
                        yield Double.NaN;
                    }
                    yield value1 / value2;
                }
            };

            result.add(new Sample(s1.getTime(), resultValue));
        }

        return result;
    }
}
