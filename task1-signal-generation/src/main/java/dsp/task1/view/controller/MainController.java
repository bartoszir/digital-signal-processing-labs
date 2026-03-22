package dsp.task1.view.controller;

import dsp.task1.logic.*;
import dsp.task1.logic.operations.SampleOperations;
import dsp.task1.logic.operations.SignalOperationType;
import dsp.task1.logic.signal.SignalType;

import dsp.task1.view.utils.Helper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    /*------------------- Zapis/Odczyt -------------------*/
    @FXML private TextField signalNameInputField;
    @FXML private javafx.scene.control.ListView<String> loadedSignalsListView;
    @FXML private Button showLoadedSignalButton;
    @FXML private TextArea fileContentTextArea;

    /*------------------- Operacje na sygnałach -------------------*/
    @FXML private ComboBox<String> operationSignal1ComboBox;
    @FXML private ComboBox<String> operationSignal2ComboBox;
    @FXML private ComboBox<SignalOperationType> signalOperationTypeComboBox;
    @FXML private TextField operationResultNameField;
    @FXML private Button performOperationButton;

    /*------------------- Others -------------------*/
    @FXML private Button generateButton;
    private final SignalManager signalManager = new SignalManager();
    private SignalData currentSignalData; // ostatnio wygenerowany SignalData



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
        refreshLoadedSignalsList();

        signalOperationTypeComboBox.getItems().setAll(SignalOperationType.values());

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
            clearFieldStyles();
            SignalType selectedType = signalTypeComboBox.getValue();
            if (selectedType == null) {
                return;
            }

            SignalParameters parameters = buildParameters(selectedType);
            List<Sample> samples = signalManager.generateSignalSamples(selectedType, parameters);

            currentSignalData = new SignalData(
                    "currentSignal",
                    selectedType,
                    parameters,
                    samples
            );

            signalNameInputField.setText(selectedType.name().toLowerCase());

            if (isDiscreteSignal(selectedType)) {
                drawScatterSamples(samples, selectedType.getName());
            } else {
                drawLineSamples(samples, selectedType.getName());
            }

            updateStatistics(samples);
            drawHistogram(samples);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        }

    }

    @FXML
    private void onSaveBinaryClicked() {
        if (currentSignalData == null) {
            Helper.showError("Błąd", "Brak sygnału do zapisania.");
            return;
        }

        try {
            String signalName = Helper.getStringFromField(signalNameInputField, "Nazwa sygnału");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz sygnał binarnie");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Pliki binarne", "*.bin")
            );
            fileChooser.setInitialFileName(signalName + ".bin");

            File file = fileChooser.showSaveDialog(generateButton.getScene().getWindow());
            if (file == null) {
                return;
            }

            currentSignalData.setName(signalName);
            signalManager.saveSignalBinary(file.getAbsolutePath(), currentSignalData);
            Helper.showInfo("Sukces", "Sygnał zapisano do pliku binarnego.");

        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        } catch (IOException e) {
            Helper.showError("Błąd zapisu", e.getMessage());
        }
    }

    @FXML
    private void onLoadBinaryClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj sygnał binarny");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Pliki binarne", "*.bin")
        );

        File file = fileChooser.showOpenDialog(generateButton.getScene().getWindow());
        if (file == null) {
            return;
        }

        try {
            SignalData loaded = signalManager.loadSignalBinary(file.getAbsolutePath());
            currentSignalData = loaded;
            signalNameInputField.setText(loaded.getName());
            displayLoadedSignal(loaded);
            refreshLoadedSignalsList();
            Helper.showInfo("Sukces", "Sygnał wczytano z pliku binarnego.");
        } catch (IOException e) {
            Helper.showError("Błąd odczytu", e.getMessage());
        }
    }

    @FXML
    private void onShowLoadedSignalClicked() {
        String selectedName = loadedSignalsListView.getSelectionModel().getSelectedItem();

        if (selectedName == null) {
            Helper.showError("Błąd", "Wybierz sygnał z listy.");
            return;
        }

        SignalData signalData = signalManager.getLoadedSignal(selectedName);
        if (signalData == null) {
            Helper.showError("Błąd", "Nie znaleziono sygnału w pamięci programu.");
            return;
        }

        currentSignalData = signalData;
        signalNameInputField.setText(signalData.getName());
        displayLoadedSignal(signalData);
    }

    @FXML
    private void onDeleteLoadedSignalClicked() {
        String selectedName = loadedSignalsListView.getSelectionModel().getSelectedItem();
        SignalType deletedSignalType = currentSignalData.getSignalType();

        if (selectedName == null) {
            Helper.showError("Błąd", "Wybierz sygnał z listy.");
            return;
        }

        signalManager.removeLoadedSignal(selectedName);

        // jeśli usunięty był aktualnie wyświetlany sygnał
        if (currentSignalData != null && selectedName.equals(currentSignalData.getName())) {
            currentSignalData = null;

            if (isDiscreteSignal(deletedSignalType)) {
                scatterSignalChart.getData().clear();
            } else {
                lineSignalChart.getData().clear();
            }
            histogramChart.getData().clear();

        }

        refreshLoadedSignalsList();
        meanValueLabel.setText("-");
        meanAbsValueLabel.setText("-");
        rmsValueLabel.setText("-");
        varianceValueLabel.setText("-");
        averagePowerValueLabel.setText("-");

        Helper.showInfo("Sukces", "Sygnał został usunięty.");
    }

    @FXML
    private void onAddToLoadedSignalsClicked() {
        if (currentSignalData == null) {
            Helper.showError("Błąd", "Brak wygenerowanego sygnału do dodania.");
            return;
        }

        try {
            String signalName = Helper.getStringFromField(signalNameInputField, "Nazwa sygnału");

            if (signalManager.getLoadedSignal(signalName) != null) {
                Helper.showError("Błąd", "Sygnał o tej nazwie już istnieje na liście.");
                return;
            }

            currentSignalData.setName(signalName);
            signalManager.addLoadedSignal(currentSignalData);
            refreshLoadedSignalsList();
            loadedSignalsListView.getSelectionModel().select(signalName);

            Helper.showInfo("Sukces", "Sygnał dodano do listy.");

        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        }
    }

    @FXML
    private void onPerformSignalOperationClicked() {
        try {
            String signal1Name = operationSignal1ComboBox.getValue();
            String signal2Name = operationSignal2ComboBox.getValue();
            SignalOperationType operationType = signalOperationTypeComboBox.getValue();
            String resultName = Helper.getStringFromField(operationResultNameField, "Nazwa wyniku");

            if (signal1Name == null || signal2Name == null) {
                Helper.showError("Błąd", "Wybierz oba sygnały.");
                return;
            }

            if (operationType == null) {
                Helper.showError("Błąd", "Wybierz operację.");
                return;
            }

            if (signalManager.getLoadedSignal(resultName) != null) {
                Helper.showError("Błąd", "Sygnał o takiej nazwie już istnieje.");
                return;
            }

            SignalData signal1 = signalManager.getLoadedSignal(signal1Name);
            SignalData signal2 = signalManager.getLoadedSignal(signal2Name);

            validateSignalsCompatibility(signal1, signal2);

            List<Sample> resultSamples = SampleOperations.execute(
                    signal1.getSamples(),
                    signal2.getSamples(),
                    operationType
            );

            SignalParameters resultParams = new SignalParameters();
            resultParams.setStartTime(signal1.getParameters().getStartTime());
            resultParams.setSamplingFrequency(signal1.getParameters().getSamplingFrequency());

            SignalData resultSignal = new SignalData(
                    resultName,
                    signal1.getSignalType(),
                    resultParams,
                    resultSamples
            );

            signalManager.addLoadedSignal(resultSignal);
            currentSignalData = resultSignal;

            refreshLoadedSignalsList();
            loadedSignalsListView.getSelectionModel().select(resultName);
            displayLoadedSignal(resultSignal);

            signalNameInputField.setText(resultName);

            Helper.showInfo("Sukces", "Operację wykonano poprawnie.");

        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
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

    private SignalParameters buildParameters(SignalType selectedType) {
        SignalParameters params = new SignalParameters();

        // podstawowe parametry
        double amplitude = getDoubleFromField(amplitudeInputField, "Amplituda (A)");
        double startTime = getDoubleFromField(startTimeInputField, "Czas początkowy (t1)");
        double duration = getDoubleFromField(durationInputField, "Czas trwania (d)");
        double samplingFrequency = getDoubleFromField(samplingFrequencyInputField, "Częstotliwość próbkowania (f)");

        Helper.validatePositive(duration, "Czas trwania sygnału (d)", durationInputField);
        Helper.validatePositive(samplingFrequency, "Częstotliwość próbkowania (f)", samplingFrequencyInputField);

        params.setAmplitude(amplitude);
        params.setStartTime(startTime);
        params.setDuration(duration);
        params.setSamplingFrequency(samplingFrequency);

        switch (selectedType) {
            case SINUSOIDAL_SIGNAL,
                 ONE_HALF_RECTIFIED_SINUSOIDAL_SIGNAL,
                 TWO_HALF_RECTIFIED_SINUSOIDAL_SIGNAL -> {
                double period = getDoubleFromField(periodInputField, "Okres podstawowy (T)");
                Helper.validatePositive(period, "Okres podstawowy (T)", periodInputField);
                params.setPeriod(period);
            }

            case RECTANGULAR_SIGNAL,
                 SYMMETRIC_RECTANGULAR_SIGNAL,
                 TRIANGULAR_SIGNAL -> {
                double period = getDoubleFromField(periodRectangularInputField, "Okres podstawowy (T)");
                double kw = getDoubleFromField(kwInputField, "Współczynnik wypełnienia (kw)");

                Helper.validatePositive(period, "Okres podstawowy (T)", periodRectangularInputField);
                Helper.validateKw(kw, "Współczynnik wypełnienia (kw)", kwInputField);

                params.setPeriod(period);
                params.setKw(kw);
            }

            case UNIT_JUMP_SIGNAL -> {
                double ts = getDoubleFromField(tsInputField, "Czas skoku (ts)");
                params.setTs(ts);
            }

            case UNIT_IMPULSE_SIGNAL -> {
                int ns = getIntFromField(nsInputField, "Numer próbki skoku (ns)");
                Helper.validateNonNegative(ns, "Numer próbki skoku (ns)", nsInputField);
                params.setNs(ns);
            }

            case IMPULSE_NOISE_SIGNAL -> {
                double p = getDoubleFromField(pInputField, "Prawdopodobieństwo (p)");
                Helper.validateProbability(p, "Prawdopodobieństwo (p)", pInputField);
                params.setP(p);
            }

            case UNIFORM_NOISE, GAUSSIAN_NOISE -> {
            }
        }

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

    private void clearFieldStyles() {
        amplitudeInputField.setStyle("");
        startTimeInputField.setStyle("");
        durationInputField.setStyle("");
        samplingFrequencyInputField.setStyle("");
        periodInputField.setStyle("");
        periodRectangularInputField.setStyle("");
        kwInputField.setStyle("");
        tsInputField.setStyle("");
        nsInputField.setStyle("");
        pInputField.setStyle("");
    }

    private void displayLoadedSignal(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        SignalType type = signalData.getSignalType();

        if (isDiscreteSignal(type)) {
            drawScatterSamples(samples, signalData.getName());
        } else {
            drawLineSamples(samples, signalData.getName());
        }

        updateStatistics(samples);
        drawHistogram(samples);
        showSignalDataAsText(signalData);
    }

    private void refreshLoadedSignalsList() {
//        loadedSignalsListView.getItems().setAll(signalManager.getLoadedSignals().keySet());
        List<String> names = new ArrayList<>(signalManager.getLoadedSignals().keySet());

        // ListView
        loadedSignalsListView.getItems().setAll(names);

        // ComboBoxy do operacji na sygnalach
        refreshOperationSignalSelectors(names);
    }

    private void refreshOperationSignalSelectors(List<String> names) {
        String selected1 = operationSignal1ComboBox.getValue();
        String selected2 = operationSignal2ComboBox.getValue();

        operationSignal1ComboBox.getItems().setAll(names);
        operationSignal2ComboBox.getItems().setAll(names);

        if (names.contains(selected1)) {
            operationSignal1ComboBox.setValue(selected1);
        }

        if (names.contains(selected2)) {
            operationSignal2ComboBox.setValue(selected2);
        }
    }

    private void validateSignalsCompatibility(SignalData signal1, SignalData signal2) {
        if (signal1.getSamples().size() != signal2.getSamples().size()) {
            throw new IllegalArgumentException("Sygnały muszą mieć taką samą liczbę próbek.");
        }

        if (Math.abs(signal1.getParameters().getSamplingFrequency() - signal2.getParameters().getSamplingFrequency()) > 1e-9) {
            throw new IllegalArgumentException("Sygnały muszą mieć taką samą częstotliwość próbkowania.");
        }

        if (Math.abs(signal1.getParameters().getStartTime() - signal2.getParameters().getStartTime()) > 1e-9) {
            throw new IllegalArgumentException("Sygnały muszą mieć taki sam czas początkowy.");
        }
    }

    private void showSignalDataAsText(SignalData signalData) {
        StringBuilder sb = new StringBuilder();

        sb.append("Nazwa: ").append(signalData.getName()).append("\n");
        sb.append("Typ: ").append(signalData.getSignalType()).append("\n");

        SignalParameters params = signalData.getParameters();
        sb.append("Czas początkowy: ").append(params.getStartTime()).append("\n");
        sb.append("Częstotliwość próbkowania: ").append(params.getSamplingFrequency()).append("\n");
        sb.append("Czas trwania: ").append(params.getDuration()).append("\n");
        sb.append("\n");

        sb.append("Próbki:\n");
        sb.append("time\tvalue\n");

        for (Sample sample : signalData.getSamples()) {
            sb.append(sample.getTime())
                    .append("\t")
                    .append(sample.getValue())
                    .append("\n");
        }

        fileContentTextArea.setText(sb.toString());
    }
}