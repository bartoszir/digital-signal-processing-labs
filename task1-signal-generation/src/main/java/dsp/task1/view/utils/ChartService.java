package dsp.task1.view.utils;

import dsp.task1.logic.model.HistogramBin;
import dsp.task1.logic.model.Sample;
import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.service.SignalHistogram;
import javafx.scene.chart.*;
import javafx.scene.control.Spinner;

import java.util.List;

public class ChartService {

    private final LineChart<Number, Number> lineSignalChart;
    private final NumberAxis lineXAxis;
    private final NumberAxis lineYAxis;
    private final ScatterChart<Number, Number> scatterSignalChart;
    private final NumberAxis scatterXAxis;
    private final NumberAxis scatterYAxis;
    private final BarChart<String, Number> histogramChart;
    private final Spinner<Integer> histogramBinsSpinner;

    public ChartService(
            LineChart<Number, Number> lineSignalChart,
            NumberAxis lineXAxis,
            NumberAxis lineYAxis,
            ScatterChart<Number, Number> scatterSignalChart,
            NumberAxis scatterXAxis,
            NumberAxis scatterYAxis,
            BarChart<String, Number> histogramChart,
            Spinner<Integer> histogramBinsSpinner
    ) {
        this.lineSignalChart = lineSignalChart;
        this.lineXAxis = lineXAxis;
        this.lineYAxis = lineYAxis;
        this.scatterSignalChart = scatterSignalChart;
        this.scatterXAxis = scatterXAxis;
        this.scatterYAxis = scatterYAxis;
        this.histogramChart = histogramChart;
        this.histogramBinsSpinner = histogramBinsSpinner;
    }

    public void drawLineSamples(List<Sample> samples, String seriesName) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (Sample sample : samples) {
            series.getData().add(new XYChart.Data<>(sample.getTime(), sample.getValue()));
        }

        showLineChart();
        lineSignalChart.getData().clear();
        lineSignalChart.getData().add(series);

        if (!samples.isEmpty()) {
            double startTime = samples.get(0).getTime();
            double endTime = samples.get(samples.size() - 1).getTime();
            double range = endTime - startTime;
            lineXAxis.setAutoRanging(false);
            lineXAxis.setLowerBound(startTime - 0.5);
            lineXAxis.setUpperBound(endTime + 0.5);
            lineXAxis.setTickUnit(Math.max(1.0, Math.ceil(range / 10.0)));
        }

        if (series.getNode() != null) {
            series.getNode().setStyle("-fx-stroke: rgba(232, 69, 60, 0.8); -fx-stroke-width: 1px;");
        }
    }

    public void drawScatterSamples(List<Sample> samples, String seriesName) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (Sample sample : samples) {
            series.getData().add(new XYChart.Data<>(sample.getTime(), sample.getValue()));
        }

        showScatterChart();
        scatterSignalChart.getData().clear();
        scatterSignalChart.getData().add(series);

        if (!samples.isEmpty()) {
            double startTime = samples.get(0).getTime();
            double endTime = samples.get(samples.size() - 1).getTime();
            double range = endTime - startTime;
            scatterXAxis.setAutoRanging(false);
            scatterXAxis.setLowerBound(startTime - 0.5);
            scatterXAxis.setUpperBound(endTime + 0.5);
            scatterXAxis.setTickUnit(Math.max(1.0, Math.ceil(range / 10.0)));
        }

        for (XYChart.Data<Number, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle(
                        "-fx-stroke: rgba(232, 69, 60, 0.8); -fx-stroke-width: 1px;" + 
                        "-fx-background-color: rgba(232, 69, 60, 0.8), rgba(232, 69, 60, 0.8);" +
                        "-fx-background-radius: 2px;" +
                        "-fx-padding: 2px;"
                );
            }
        }
    }

    public void drawHistogram(SignalData signalData) {
        Integer binsCount = histogramBinsSpinner.getValue();
        if (binsCount == null) {
            binsCount = 10;
        }

        List<HistogramBin> bins = SignalHistogram.generate(signalData, binsCount);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Histogram");

        for (HistogramBin bin : bins) {
            series.getData().add(new XYChart.Data<>(bin.getLabel(), bin.getCount()));
        }

        histogramChart.getData().clear();
        histogramChart.getData().add(series);

        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-bar-fill: rgba(232, 69, 60, 0.8);");
            }
        }
    }

    public void showLineChart() {
        lineSignalChart.setVisible(true);
        lineSignalChart.setManaged(true);
        scatterSignalChart.setVisible(false);
        scatterSignalChart.setManaged(false);
    }

    public void showScatterChart() {
        scatterSignalChart.setVisible(true);
        scatterSignalChart.setManaged(true);
        lineSignalChart.setVisible(false);
        lineSignalChart.setManaged(false);
    }
}