package dsp.task1.view.utils;

import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.service.SignalStatistics;
import javafx.scene.control.Label;

public class StatisticsDisplayService {

    private final Label meanValueLabel;
    private final Label meanAbsValueLabel;
    private final Label rmsValueLabel;
    private final Label varianceValueLabel;
    private final Label averagePowerValueLabel;

    public StatisticsDisplayService(
            Label meanValueLabel,
            Label meanAbsValueLabel,
            Label rmsValueLabel,
            Label varianceValueLabel,
            Label averagePowerValueLabel
    ) {
        this.meanValueLabel = meanValueLabel;
        this.meanAbsValueLabel = meanAbsValueLabel;
        this.rmsValueLabel = rmsValueLabel;
        this.varianceValueLabel = varianceValueLabel;
        this.averagePowerValueLabel = averagePowerValueLabel;
    }

    public void updateStatistics(SignalData signalData) {
        double mean = SignalStatistics.mean(signalData);
        double meanAbs = SignalStatistics.meanAbsoluteValue(signalData);
        double rms = SignalStatistics.rms(signalData);
        double variance = SignalStatistics.variance(signalData);
        double avgPower = SignalStatistics.averagePower(signalData);

        meanValueLabel.setText(format(mean));
        meanAbsValueLabel.setText(format(meanAbs));
        rmsValueLabel.setText(format(rms));
        varianceValueLabel.setText(format(variance));
        averagePowerValueLabel.setText(format(avgPower));
    }

    public void clearStatistics() {
        meanValueLabel.setText("-");
        meanAbsValueLabel.setText("-");
        rmsValueLabel.setText("-");
        varianceValueLabel.setText("-");
        averagePowerValueLabel.setText("-");
    }

    private String format(double value) {
        return String.format("%.4f", value);
    }
}