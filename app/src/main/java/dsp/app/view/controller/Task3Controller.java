package dsp.app.view.controller;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;
import dsp.task1.logic.service.SignalManager;
import dsp.task3.logic.convolution.Convolution;
import dsp.task3.logic.correlation.Correlation;
import dsp.task3.logic.correlation.DistanceSensorSimulator;
import dsp.task3.logic.correlation.SimulationResult;
import dsp.task3.logic.filter.FilterType;
import dsp.task3.logic.filter.FirFilterService;
import dsp.task3.logic.filter.WindowType;
import dsp.app.view.utils.Helper;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.List;

public class Task3Controller {

    private static final String COLOR_BLUE   = "rgba(100, 149, 237, 0.8)";
    private static final String COLOR_RED    = "rgba(232, 69, 60, 0.8)";
    private static final String COLOR_GREEN  = "rgba(80, 200, 120, 0.8)";
    private static final String COLOR_ORANGE = "rgba(255, 165, 0, 0.8)";

    /*------------------- Splot — Left Panel -------------------*/
    private final ComboBox<String> splotHComboBox;
    private final ComboBox<String> splotXComboBox;
    private final Button splotCalculateButton;

    /*------------------- Splot — Right Panel -------------------*/
    private final LineChart<Number, Number> splotChart;

    /*------------------- Filtracja FIR — Left Panel -------------------*/
    private final TextField firMField;
    private final TextField firKField;
    private final ComboBox<WindowType> firWindowComboBox;
    private final ComboBox<FilterType> firFilterTypeComboBox;
    private final ComboBox<String> firSignalComboBox;
    private final Button firApplyButton;

    /*------------------- Filtracja FIR — Right Panel -------------------*/
    private final LineChart<Number, Number> firImpulseChart;
    private final LineChart<Number, Number> firFilteredChart;

    /*------------------- Korelacja — Left Panel -------------------*/
    private final ComboBox<String> corrHComboBox;
    private final ComboBox<String> corrXComboBox;
    private final ComboBox<String> corrMethodComboBox;
    private final Button corrCalculateButton;

    /*------------------- Symulacja — Left Panel -------------------*/
    private final TextField simSignalSpeedField;
    private final TextField simObjectSpeedField;
    private final TextField simInitialDistanceField;
    private final TextField simSamplingFreqField;
    private final TextField simBufferSizeField;
    private final TextField simReportingPeriodField;
    private final TextField simF1Field;
    private final TextField simF2Field;
    private final TextField simStepsField;
    private final Button simulateButton;

    /*------------------- Korelacja/Symulacja — Right Panel -------------------*/
    private final LineChart<Number, Number> corrResultChart;
    private final LineChart<Number, Number> corrSignalsChart;
    private final LineChart<Number, Number> simResultChart;

    /*------------------- Sub-tab panes -------------------*/
    private final TabPane leftSubTabPane;
    private final TabPane rightSubTabPane;

    private final SignalManager signalManager;

    public Task3Controller(
            ComboBox<String> splotHComboBox,
            ComboBox<String> splotXComboBox,
            Button splotCalculateButton,
            LineChart<Number, Number> splotChart,
            TextField firMField,
            TextField firKField,
            ComboBox<WindowType> firWindowComboBox,
            ComboBox<FilterType> firFilterTypeComboBox,
            ComboBox<String> firSignalComboBox,
            Button firApplyButton,
            LineChart<Number, Number> firImpulseChart,
            LineChart<Number, Number> firFilteredChart,
            ComboBox<String> corrHComboBox,
            ComboBox<String> corrXComboBox,
            ComboBox<String> corrMethodComboBox,
            Button corrCalculateButton,
            TextField simSignalSpeedField,
            TextField simObjectSpeedField,
            TextField simInitialDistanceField,
            TextField simSamplingFreqField,
            TextField simBufferSizeField,
            TextField simReportingPeriodField,
            TextField simF1Field,
            TextField simF2Field,
            TextField simStepsField,
            Button simulateButton,
            LineChart<Number, Number> corrResultChart,
            LineChart<Number, Number> corrSignalsChart,
            LineChart<Number, Number> simResultChart,
            TabPane leftSubTabPane,
            TabPane rightSubTabPane,
            SignalManager signalManager) {
        this.splotHComboBox = splotHComboBox;
        this.splotXComboBox = splotXComboBox;
        this.splotCalculateButton = splotCalculateButton;
        this.splotChart = splotChart;
        this.firMField = firMField;
        this.firKField = firKField;
        this.firWindowComboBox = firWindowComboBox;
        this.firFilterTypeComboBox = firFilterTypeComboBox;
        this.firSignalComboBox = firSignalComboBox;
        this.firApplyButton = firApplyButton;
        this.firImpulseChart = firImpulseChart;
        this.firFilteredChart = firFilteredChart;
        this.corrHComboBox = corrHComboBox;
        this.corrXComboBox = corrXComboBox;
        this.corrMethodComboBox = corrMethodComboBox;
        this.corrCalculateButton = corrCalculateButton;
        this.simSignalSpeedField = simSignalSpeedField;
        this.simObjectSpeedField = simObjectSpeedField;
        this.simInitialDistanceField = simInitialDistanceField;
        this.simSamplingFreqField = simSamplingFreqField;
        this.simBufferSizeField = simBufferSizeField;
        this.simReportingPeriodField = simReportingPeriodField;
        this.simF1Field = simF1Field;
        this.simF2Field = simF2Field;
        this.simStepsField = simStepsField;
        this.simulateButton = simulateButton;
        this.corrResultChart = corrResultChart;
        this.corrSignalsChart = corrSignalsChart;
        this.simResultChart = simResultChart;
        this.leftSubTabPane = leftSubTabPane;
        this.rightSubTabPane = rightSubTabPane;
        this.signalManager = signalManager;
    }

    public void initialize() {
        firWindowComboBox.getItems().setAll(WindowType.values());
        firWindowComboBox.getSelectionModel().selectFirst();
        firFilterTypeComboBox.getItems().setAll(FilterType.values());
        firFilterTypeComboBox.getSelectionModel().selectFirst();
        corrMethodComboBox.getItems().addAll("Bezpośrednia", "Przez splot");
        corrMethodComboBox.getSelectionModel().selectFirst();

        configureChart(splotChart);
        configureChart(firImpulseChart);
        configureChart(firFilteredChart);
        configureChart(corrResultChart);
        configureChart(corrSignalsChart);
        configureChart(simResultChart);

        // CSS classes drive per-series colors (line + symbol + legend symbol).
        // Charts with a single series inherit the default red from style.css.
        splotChart.getStyleClass().add("task3-chart");
        firFilteredChart.getStyleClass().add("task3-chart");
        corrSignalsChart.getStyleClass().add("task3-chart");
        simResultChart.getStyleClass().add("task3-sim-chart");

        splotCalculateButton.setOnAction(e -> onSplotCalculate());
        firApplyButton.setOnAction(e -> onFirApply());
        corrCalculateButton.setOnAction(e -> onCorrCalculate());
        simulateButton.setOnAction(e -> onSimulate());

        leftSubTabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            if (newIdx != null) {
                rightSubTabPane.getSelectionModel().select(newIdx.intValue());
            }
        });
    }

    public void updateSignalComboBoxes(List<String> names) {
        preserveAndUpdate(splotHComboBox, names);
        preserveAndUpdate(splotXComboBox, names);
        preserveAndUpdate(firSignalComboBox, names);
        preserveAndUpdate(corrHComboBox, names);
        preserveAndUpdate(corrXComboBox, names);
    }

    private void preserveAndUpdate(ComboBox<String> cb, List<String> names) {
        String current = cb.getValue();
        cb.getItems().setAll(names);
        if (names.contains(current)) {
            cb.setValue(current);
        }
    }

    private void configureChart(LineChart<Number, Number> chart) {
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(true);
        chart.setHorizontalZeroLineVisible(true);
        chart.setVerticalZeroLineVisible(true);
        ((NumberAxis) chart.getXAxis()).setAnimated(false);
        ((NumberAxis) chart.getYAxis()).setAnimated(false);
    }

    // Sets the line color on the series node explicitly (null-checked, same pattern as ChartService).
    // CSS class rules handle the legend symbol and data-point symbol colors.
    private void applySeriesStyle(XYChart.Series<Number, Number> series, String color) {
        if (series.getNode() != null) {
            series.getNode().setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 2px;");
        }
    }

    private void onSplotCalculate() {
        try {
            String hName = splotHComboBox.getValue();
            String xName = splotXComboBox.getValue();
            if (hName == null || xName == null) {
                Helper.showError("Błąd", "Wybierz oba sygnały.");
                return;
            }
            SignalData h = signalManager.getLoadedSignal(hName);
            SignalData x = signalManager.getLoadedSignal(xName);
            if (h == null || x == null) {
                Helper.showError("Błąd", "Nie można znaleźć wybranych sygnałów.");
                return;
            }

            List<Sample> convResult = Convolution.convolve(h, x);

            XYChart.Series<Number, Number> hSeries    = samplesToSeries("h",   h.getSamples());
            XYChart.Series<Number, Number> xSeries    = samplesToSeries("x",   x.getSamples());
            XYChart.Series<Number, Number> convSeries = samplesToSeries("h*x", convResult);

            splotChart.getData().clear();
            splotChart.getData().addAll(hSeries, xSeries, convSeries);
            applySeriesStyle(hSeries,    COLOR_BLUE);
            applySeriesStyle(xSeries,    COLOR_RED);
            applySeriesStyle(convSeries, COLOR_GREEN);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", e.getMessage());
        }
    }

    private void onFirApply() {
        try {
            int M = Helper.getIntFromField(firMField, "M");
            int K = Helper.getIntFromField(firKField, "K");
            if (M % 2 == 0) {
                firMField.setStyle("-fx-border-color: red;");
                Helper.showError("Błąd", "Rząd filtru M musi być nieparzysty.");
                return;
            }
            WindowType windowType = firWindowComboBox.getValue();
            FilterType filterType = firFilterTypeComboBox.getValue();
            String signalName = firSignalComboBox.getValue();
            if (signalName == null) {
                Helper.showError("Błąd", "Wybierz sygnał wejściowy.");
                return;
            }
            SignalData signalData = signalManager.getLoadedSignal(signalName);
            if (signalData == null) {
                Helper.showError("Błąd", "Nie można znaleźć sygnału.");
                return;
            }

            FirFilterService filterService = new FirFilterService(M, K, windowType, filterType);
            List<Double> coefficients = filterService.getFilterCoefficients();
            List<Sample> filteredSamples = filterService.filter(signalData);

            XYChart.Series<Number, Number> impulseSeries = new XYChart.Series<>();
            impulseSeries.setName("h(n)");
            for (int i = 0; i < coefficients.size(); i++) {
                impulseSeries.getData().add(new XYChart.Data<>(i, coefficients.get(i)));
            }
            firImpulseChart.getData().clear();
            firImpulseChart.getData().add(impulseSeries);
            applySeriesStyle(impulseSeries, COLOR_RED);

            XYChart.Series<Number, Number> inputSeries    = samplesToSeries("Wejście",       signalData.getSamples());
            XYChart.Series<Number, Number> filteredSeries = samplesToSeries("Przefiltrowany", filteredSamples);
            firFilteredChart.getData().clear();
            firFilteredChart.getData().addAll(inputSeries, filteredSeries);
            applySeriesStyle(inputSeries,    COLOR_BLUE);
            applySeriesStyle(filteredSeries, COLOR_RED);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", e.getMessage());
        }
    }

    private void onCorrCalculate() {
        try {
            String hName = corrHComboBox.getValue();
            String xName = corrXComboBox.getValue();
            if (hName == null || xName == null) {
                Helper.showError("Błąd", "Wybierz oba sygnały.");
                return;
            }
            SignalData h = signalManager.getLoadedSignal(hName);
            SignalData x = signalManager.getLoadedSignal(xName);
            if (h == null || x == null) {
                Helper.showError("Błąd", "Nie można znaleźć wybranych sygnałów.");
                return;
            }

            String method = corrMethodComboBox.getValue();
            List<Sample> corrResult = "Przez splot".equals(method)
                    ? Correlation.correlateViaConvolution(h, x)
                    : Correlation.correlate(h, x);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("Rhx");
            for (Sample s : corrResult) {
                series.getData().add(new XYChart.Data<>(s.getTime(), s.getValue()));
            }
            corrResultChart.getData().clear();
            corrResultChart.getData().add(series);
            applySeriesStyle(series, COLOR_RED);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", e.getMessage());
        }
    }

    private void onSimulate() {
        try {
            double signalSpeed      = Helper.getDoubleFromField(simSignalSpeedField,      "Prędkość sygnału");
            double objectSpeed      = Helper.getDoubleFromField(simObjectSpeedField,      "Prędkość obiektu");
            double initialDistance  = Helper.getDoubleFromField(simInitialDistanceField,  "Odległość początkowa");
            double samplingFreq     = Helper.getDoubleFromField(simSamplingFreqField,     "Częstotliwość próbkowania");
            int    bufferSize       = Helper.getIntFromField(simBufferSizeField,          "Rozmiar bufora");
            double reportingPeriod  = Helper.getDoubleFromField(simReportingPeriodField,  "Okres raportowania");
            double f1               = Helper.getDoubleFromField(simF1Field,               "f1");
            double f2               = Helper.getDoubleFromField(simF2Field,               "f2");
            int    steps            = Helper.getIntFromField(simStepsField,               "Liczba kroków");

            DistanceSensorSimulator simulator = new DistanceSensorSimulator(
                    signalSpeed, objectSpeed, initialDistance, samplingFreq,
                    bufferSize, reportingPeriod, f1, f2
            );

            List<SimulationResult> results = simulator.simulateSteps(steps);

            if (!results.isEmpty()) {
                SimulationResult last = results.get(results.size() - 1);
                double dt = 1.0 / samplingFreq;

                XYChart.Series<Number, Number> probingSeries = new XYChart.Series<>();
                probingSeries.setName("Sygnał sondujący");
                XYChart.Series<Number, Number> returnSeries = new XYChart.Series<>();
                returnSeries.setName("Sygnał zwrotny");
                for (int i = 0; i < last.getProbingSignal().size(); i++) {
                    double t = i * dt;
                    probingSeries.getData().add(new XYChart.Data<>(t, last.getProbingSignal().get(i)));
                    returnSeries.getData().add(new XYChart.Data<>(t, last.getReturnSignal().get(i)));
                }
                corrSignalsChart.getData().clear();
                corrSignalsChart.getData().addAll(probingSeries, returnSeries);
                applySeriesStyle(probingSeries, COLOR_BLUE);
                applySeriesStyle(returnSeries,  COLOR_RED);
            }

            XYChart.Series<Number, Number> realSeries     = new XYChart.Series<>();
            realSeries.setName("Rzeczywista");
            XYChart.Series<Number, Number> measuredSeries = new XYChart.Series<>();
            measuredSeries.setName("Zmierzona");
            for (int i = 0; i < results.size(); i++) {
                realSeries.getData().add(new XYChart.Data<>(i, results.get(i).getRealDistance()));
                measuredSeries.getData().add(new XYChart.Data<>(i, results.get(i).getMeasuredDistance()));
            }
            simResultChart.getData().clear();
            simResultChart.getData().addAll(realSeries, measuredSeries);
            applySeriesStyle(realSeries,     COLOR_GREEN);
            applySeriesStyle(measuredSeries, COLOR_ORANGE);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", e.getMessage());
        }
    }

    private XYChart.Series<Number, Number> samplesToSeries(String name, List<Sample> samples) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (Sample s : samples) {
            series.getData().add(new XYChart.Data<>(s.getTime(), s.getValue()));
        }
        return series;
    }
}
