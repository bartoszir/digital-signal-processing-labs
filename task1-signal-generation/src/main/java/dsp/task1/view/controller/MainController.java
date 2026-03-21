package dsp.task1.view.controller;

import dsp.task1.logic.*;
import dsp.task1.logic.signal.SignalType;
import dsp.task1.view.utils.Helper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static dsp.task1.view.utils.Helper.getDoubleFromField;
import static dsp.task1.view.utils.Helper.getIntFromField;

public class MainController implements Initializable{

    /*------------------- Parameter Panes -------------------*/
    // rozne pola parametrow wyswietlane w zaleznosci od wybranego sygnalu
    @FXML private ComboBox<SignalType> signalTypeComboBox;
    @FXML private GridPane periodParamsPane;
    @FXML private GridPane rectangularParamsPane;
    @FXML private GridPane jumpParamsPane;
    @FXML private GridPane unitImpulseParamsPane;
    @FXML private GridPane impulseNoiseParamsPane;

    /*------------------- Parameters Input Fields -------------------*/
    // parametry sygnalu (domyslne wartosci ustawiane automatycznie)
    @FXML private TextField amplitudeInputField;
    @FXML private TextField startTimeInputField;
    @FXML private TextField durationInputField;
    @FXML private TextField samplingFrequencyInputField;
    @FXML private TextField periodInputField;
    @FXML private TextField periodRectangularInputField;
    @FXML private TextField kwInputField;
    @FXML private TextField tsInputField;
    @FXML private TextField nsInputField;
    @FXML private TextField pInputField;

    /*------------------- Parametry Statystyczne Sygnalow -------------------*/
    @FXML private Label meanValueLabel;
    @FXML private Label meanAbsValueLabel;
    @FXML private Label rmsValueLabel;
    @FXML private Label varianceValueLabel;
    @FXML private Label averagePowerValueLabel;

    /*------------------- Histogram Tab -------------------*/
    @FXML private BarChart<String, Number> histogramChart;
    @FXML private CategoryAxis histogramXAxis;
    @FXML private NumberAxis histogramYAxis;
    @FXML private Spinner<Integer> histogramBinsSpinner;

    /*------------------- Signal Chart -------------------*/
    // wykres sygnału
    @FXML private LineChart<Number, Number> lineSignalChart;
    @FXML private NumberAxis lineXAxis;
    @FXML private NumberAxis lineYAxis;

    @FXML private javafx.scene.chart.ScatterChart<Number, Number> scatterSignalChart;
    @FXML private NumberAxis scatterXAxis;
    @FXML private NumberAxis scatterYAxis;

    /*------------------- Others -------------------*/
    private final SignalManager signalManager = new SignalManager();


    /*========================= METHODS =========================*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signalTypeComboBox.getItems().setAll(SignalType.values());
        signalTypeComboBox.setOnAction(actionEvent -> updateSpecificParamsVisibility());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, 10, 5);
        valueFactory.setValue(10);
        histogramBinsSpinner.setValueFactory(valueFactory);
        histogramChart.setAnimated(false);
        histogramChart.setLegendVisible(false);
        histogramXAxis.setLabel("Przedziały wartości");
        histogramYAxis.setLabel("Liczba próbek");

        setDefaultValues();

        lineSignalChart.setAnimated(false);
        lineSignalChart.setCreateSymbols(false); // czy pokazywac punkty probek
        lineXAxis.setAnimated(false);
        lineYAxis.setAnimated(false);

        scatterSignalChart.setAnimated(false);
        scatterXAxis.setAnimated(false);
        scatterYAxis.setAnimated(false);
    }

    @FXML
    private void onGenerateSignalClicked() {
        try {
            SignalType selectedType = signalTypeComboBox.getValue();
            if (selectedType == null) {
                return;
            }

            SignalParameters parameters = buildParameters();
            List<Sample> samples = signalManager.generateSignalSamples(selectedType, parameters);

            if (isDiscreteSignal(selectedType)) {
                drawScatterSamples(samples, selectedType.getName());
            } else {
                drawLineSamples(samples, selectedType.getName());
            }

            updateStatistics(samples);
            drawHistogram(samples);
        } catch (NumberFormatException e) {
            System.out.println("Niepoprawny format liczby.");
        }

    }

    private void drawLineSamples(List<Sample> samples, String seriesName) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (Sample sample : samples) {
            series.getData().add(new XYChart.Data<>(sample.getTime(), sample.getValue()));
        }

        showLineChart();
        lineSignalChart.getData().clear();
        lineSignalChart.getData().add(series);

        if (series.getNode() != null) {
            series.getNode().setStyle("-fx-stroke-width: 2px;"); // kolor i grubosc serii po jej dodaniu
        }

//        for (XYChart.Data<Number, Number> data : series.getData()) {
//            if (data.getNode() != null) {
//                data.getNode().setStyle(
//                        "-fx-background-color: red, white;" +
//                                "-fx-background-radius: 1px;" +
//                                "-fx-padding: 2px;"
//                );
//            }
//        }
    }

    private void drawScatterSamples(List<Sample> samples, String seriesName) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(seriesName);

        for (Sample sample : samples) {
            series.getData().add(new XYChart.Data<>(sample.getTime(), sample.getValue()));
        }

        showScatterChart();
        scatterSignalChart.getData().clear();
        scatterSignalChart.getData().add(series);

        for (XYChart.Data<Number, Number> data : series.getData()) {
            if (data.getNode() != null) {
                data.getNode().setStyle(
                        "-fx-background-color: red, red;" +
                                "-fx-background-radius: 4px;" +
                                "-fx-padding: 2px;"
                );
            }
        }
    }

    private void updateSpecificParamsVisibility() {
        hideAllSpecificParamBoxes();

        SignalType selectedType = signalTypeComboBox.getValue();
        if (selectedType == null) {
            return;
        }

        switch (selectedType) {
            case SINUSOIDAL_SIGNAL,
                 ONE_HALF_RECTIFIED_SINUSOIDAL_SIGNAL,
                 TWO_HALF_RECTIFIED_SINUSOIDAL_SIGNAL -> showBox(periodParamsPane);

            case RECTANGULAR_SIGNAL,
                 SYMMETRIC_RECTANGULAR_SIGNAL,
                 TRIANGULAR_SIGNAL -> showBox(rectangularParamsPane);

            case UNIT_JUMP_SIGNAL -> showBox(jumpParamsPane);

            case UNIT_IMPULSE_SIGNAL -> showBox(unitImpulseParamsPane);

            case IMPULSE_NOISE_SIGNAL -> showBox(impulseNoiseParamsPane);

            case UNIFORM_NOISE,
                 GAUSSIAN_NOISE -> {
                // brak dodatkowych parametrów
            }
        }
    }

    private void hideAllSpecificParamBoxes() {
        hidePane(periodParamsPane);
        hidePane(rectangularParamsPane);
        hidePane(jumpParamsPane);
        hidePane(unitImpulseParamsPane);
        hidePane(impulseNoiseParamsPane);
    }

    private void showBox(GridPane pane) {
        pane.setVisible(true);
        pane.setManaged(true);
    }

    private void hidePane(GridPane pane) {
        pane.setVisible(false);
        pane.setManaged(false);
    }

    private void setDefaultValues() {
        amplitudeInputField.setText("2.0");
        startTimeInputField.setText("0.0");
        durationInputField.setText("10.0");
        samplingFrequencyInputField.setText("100.0");

        periodInputField.setText("2.0");
        periodRectangularInputField.setText("1.0");
        kwInputField.setText("0.5");
        tsInputField.setText("1.0");
        nsInputField.setText("10");
        pInputField.setText("0.1");

        signalTypeComboBox.setValue(SignalType.SINUSOIDAL_SIGNAL);
        updateSpecificParamsVisibility();
    }

    private SignalParameters buildParameters() {
        SignalParameters params = new SignalParameters();

        params.setAmplitude(getDoubleFromField(amplitudeInputField));
        params.setStartTime(getDoubleFromField(startTimeInputField));
        params.setDuration(getDoubleFromField(durationInputField));
        params.setSamplingFrequency(getDoubleFromField(samplingFrequencyInputField));

        params.setPeriod(getDoubleFromField(periodInputField));
        params.setKw(getDoubleFromField(kwInputField));
        params.setTs(getDoubleFromField(tsInputField));
        params.setNs(getIntFromField(nsInputField));
        params.setP(getDoubleFromField(pInputField));

        return params;
    }

    private void showLineChart() {
        lineSignalChart.setVisible(true);
        lineSignalChart.setManaged(true);

        scatterSignalChart.setVisible(false);
        scatterSignalChart.setManaged(false);
    }

    private void showScatterChart() {
        scatterSignalChart.setVisible(true);
        scatterSignalChart.setManaged(true);

        lineSignalChart.setVisible(false);
        lineSignalChart.setManaged(false);
    }

    private boolean isDiscreteSignal(SignalType type) {
        return switch (type) {
            case UNIT_IMPULSE_SIGNAL, IMPULSE_NOISE_SIGNAL -> true;
            default -> false;
        };
    }

    private void updateStatistics(List<Sample> samples) {
        double mean = SignalStatistics.mean(samples);
        double meanAbs = SignalStatistics.meanAbsoluteValue(samples);
        double rms = SignalStatistics.rms(samples);
        double variance = SignalStatistics.variance(samples);
        double avgPower = SignalStatistics.averagePower(samples);

        meanValueLabel.setText(format(mean));
        meanAbsValueLabel.setText(format(meanAbs));
        rmsValueLabel.setText(format(rms));
        varianceValueLabel.setText(format(variance));
        averagePowerValueLabel.setText(format(avgPower));
    }

    private String format(double value) {
        return String.format("%.4f", value);
    }

    private void drawHistogram(List<Sample> samples) {
        Integer binsCount = histogramBinsSpinner.getValue();
        if (binsCount == null) {
            binsCount = 10;
        }

        List<HistogramBin> bins = SignalHistogram.generate(samples, binsCount);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Histogram");

        for (HistogramBin bin : bins) {
            series.getData().add(new XYChart.Data<>(bin.getLabel(), bin.getCount()));
        }

        histogramChart.getData().clear();
        histogramChart.getData().add(series);
    }
}