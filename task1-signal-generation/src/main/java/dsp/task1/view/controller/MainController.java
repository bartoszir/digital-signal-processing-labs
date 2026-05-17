package dsp.task1.view.controller;

import dsp.task1.logic.model.ConversionSession;
import dsp.task1.logic.model.Sample;
import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.model.SignalParameters;
import dsp.task1.logic.operations.SampleOperations;
import dsp.task1.logic.operations.SignalOperationType;
import dsp.task1.logic.io.SignalFileException;
import dsp.task1.logic.service.SignalManager;
import dsp.task1.logic.signal.SignalType;
import dsp.task1.view.utils.ChartService;
import dsp.task1.view.utils.Helper;
import dsp.task1.view.utils.SignalFormService;
import dsp.task1.view.utils.StatisticsDisplayService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    /*------------------- Parameter Panes -------------------*/
    @FXML private ComboBox<SignalType> signalTypeComboBox;
    @FXML private GridPane periodParamsPane;
    @FXML private GridPane rectangularParamsPane;
    @FXML private GridPane jumpParamsPane;
    @FXML private GridPane unitImpulseParamsPane;
    @FXML private GridPane impulseNoiseParamsPane;

    /*------------------- Parameters Input Fields -------------------*/
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
    @FXML private LineChart<Number, Number> lineSignalChart;
    @FXML private NumberAxis lineXAxis;
    @FXML private NumberAxis lineYAxis;
    @FXML private ScatterChart<Number, Number> scatterSignalChart;
    @FXML private NumberAxis scatterXAxis;
    @FXML private NumberAxis scatterYAxis;

    /*------------------- Zapis/Odczyt -------------------*/
    @FXML private ListView<String> loadedSignalsListView;
    @FXML private TextArea fileContentTextArea;

    /*------------------- Operacje na sygnałach -------------------*/
    @FXML private ComboBox<String> operationSignal1ComboBox;
    @FXML private ComboBox<String> operationSignal2ComboBox;
    @FXML private ComboBox<SignalOperationType> signalOperationTypeComboBox;
    @FXML private Button performOperationButton;

    /*------------------- Conversion Tab — Left Panel -------------------*/
    @FXML private ComboBox<String> conversionInputSignalComboBox;
    @FXML private TextField conversionSamplingFrequencyField;
    @FXML private Button sampleButton;
    @FXML private TextField conversionBitsField;
    @FXML private ComboBox<String> quantizationMethodComboBox;
    @FXML private Button quantizeButton;
    @FXML private ComboBox<String> reconstructionMethodComboBox;
    @FXML private VBox sincParamsBox;
    @FXML private TextField sincLeftSamplesField;
    @FXML private TextField sincRightSamplesField;
    @FXML private Button reconstructButton;

    /*------------------- Conversion Tab — Right Panel -------------------*/
    @FXML private CheckBox conversionShowOriginalCheckBox;
    @FXML private CheckBox conversionShowSampledCheckBox;
    @FXML private CheckBox conversionShowQuantizedCheckBox;
    @FXML private CheckBox conversionShowReconstructedCheckBox;
    @FXML private LineChart<Number, Number> conversionLineChart;
    @FXML private NumberAxis conversionXAxis;
    @FXML private NumberAxis conversionYAxis;
    @FXML private Label mseSamplingLabel;
    @FXML private Label mseQuantizationLabel;
    @FXML private Label snrSamplingLabel;
    @FXML private Label snrQuantizationLabel;
    @FXML private Label psnrSamplingLabel;
    @FXML private Label psnrQuantizationLabel;
    @FXML private Label mdSamplingLabel;
    @FXML private Label mdQuantizationLabel;

    /*------------------- Tab Panes -------------------*/
    @FXML private TabPane leftTabPane;
    @FXML private TabPane rightTabPane;

    /*------------------- Others -------------------*/
    @FXML private Button generateButton;
    @FXML private CheckBox showSymbolsCheckBox;

    private final SignalManager signalManager = new SignalManager();
    private SignalData currentSignalData;
    private final ConversionSession conversionSession = new ConversionSession();

    private ChartService chartService;
    private StatisticsDisplayService statisticsDisplayService;
    private SignalFormService signalFormService;
    private ConversionController conversionController;

    /*========================= METHODS =========================*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chartService = new ChartService(
                lineSignalChart, lineXAxis, lineYAxis,
                scatterSignalChart, scatterXAxis, scatterYAxis,
                histogramChart, histogramBinsSpinner
        );

        statisticsDisplayService = new StatisticsDisplayService(
                meanValueLabel, meanAbsValueLabel, rmsValueLabel,
                varianceValueLabel, averagePowerValueLabel
        );

        signalFormService = new SignalFormService(
                signalTypeComboBox,
                amplitudeInputField, startTimeInputField, durationInputField, samplingFrequencyInputField,
                periodInputField, periodRectangularInputField, kwInputField,
                tsInputField, nsInputField, pInputField,
                periodParamsPane, rectangularParamsPane, jumpParamsPane,
                unitImpulseParamsPane, impulseNoiseParamsPane
        );

        conversionController = new ConversionController(
                conversionInputSignalComboBox,
                conversionSamplingFrequencyField, sampleButton,
                conversionBitsField, quantizationMethodComboBox, quantizeButton,
                reconstructionMethodComboBox, sincParamsBox,
                sincLeftSamplesField, sincRightSamplesField, reconstructButton,
                conversionShowOriginalCheckBox, conversionShowSampledCheckBox, conversionShowQuantizedCheckBox, conversionShowReconstructedCheckBox,
                conversionLineChart,
                mseSamplingLabel, mseQuantizationLabel,
                snrSamplingLabel, snrQuantizationLabel,
                psnrSamplingLabel, psnrQuantizationLabel,
                mdSamplingLabel, mdQuantizationLabel,
                signalManager, conversionSession
        );
        conversionController.initialize();

        signalTypeComboBox.getItems().setAll(
            Arrays.stream(SignalType.values())
                .filter(type -> type != SignalType.OPERATION_RESULT)
                .toList()
        );  
        signalTypeComboBox.setOnAction(e -> signalFormService.updateSpecificParamsVisibility());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, 10, 5);
        valueFactory.setValue(10);
        histogramBinsSpinner.setValueFactory(valueFactory);
        histogramBinsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (currentSignalData != null) {
                chartService.drawHistogram(currentSignalData);
            }
        });
        histogramChart.setAnimated(false);
        histogramChart.setLegendVisible(false);
        histogramXAxis.setLabel("Przedziały wartości");
        histogramYAxis.setLabel("Liczba próbek");

        leftTabPane.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldTab, newTab) -> {
                if (newTab != null) {
                    switch (newTab.getText()) {
                        case "Generowanie", "Pliki", "Operacje" ->
                            rightTabPane.getTabs().stream()
                                .filter(tab -> "Wykres".equals(tab.getText()))
                                .findFirst()
                                .ifPresent(tab -> rightTabPane.getSelectionModel().select(tab));
                        case "Konwersja" ->
                            rightTabPane.getTabs().stream()
                                .filter(tab -> "Konwersja".equals(tab.getText()))
                                .findFirst()
                                .ifPresent(tab -> rightTabPane.getSelectionModel().select(tab));
                    }
                }
            });

        signalFormService.setDefaultValues();
        refreshLoadedSignalsList();

        loadedSignalsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal == null) return;
                    SignalData signalData = signalManager.getLoadedSignal(newVal);
                    if (signalData == null) return;
                    currentSignalData = signalData;
                    displayLoadedSignal(signalData);
                }
        );

        showSymbolsCheckBox.selectedProperty().addListener((obs, oldVal, selected) ->
                lineSignalChart.setCreateSymbols(selected));

        signalOperationTypeComboBox.getItems().setAll(SignalOperationType.values());

        lineSignalChart.setAnimated(false);
        lineSignalChart.setCreateSymbols(true);
        lineSignalChart.setHorizontalZeroLineVisible(true);
        lineSignalChart.setVerticalZeroLineVisible(true);
        lineXAxis.setAnimated(false);
        lineYAxis.setAnimated(false);

        scatterSignalChart.setAnimated(false);
        scatterSignalChart.setHorizontalZeroLineVisible(true);
        scatterSignalChart.setVerticalZeroLineVisible(true);
        scatterXAxis.setAnimated(false);
        scatterYAxis.setAnimated(false);
    }

    @FXML
    private void onGenerateSignalClicked() {
        try {
            signalFormService.clearFieldStyles();
            SignalType selectedType = signalTypeComboBox.getValue();
            if (selectedType == null) {
                return;
            }

            SignalParameters parameters = signalFormService.buildParameters(selectedType);
            List<Sample> samples = signalManager.generateSignalSamples(selectedType, parameters);

            String signalName = generateUniqueName(selectedType.name().toLowerCase());
            currentSignalData = new SignalData(signalName, selectedType, parameters, samples);
            signalManager.addLoadedSignal(currentSignalData);

            if (isDiscreteSignal(selectedType)) {
                chartService.drawScatterSamples(samples, selectedType.getName());
            } else {
                chartService.drawLineSamples(samples, selectedType.getName());
            }
            lineSignalChart.setCreateSymbols(showSymbolsCheckBox.isSelected());

            statisticsDisplayService.updateStatistics(currentSignalData);
            chartService.drawHistogram(currentSignalData);

            refreshLoadedSignalsList();
            loadedSignalsListView.getSelectionModel().select(signalName);
            showSignalDataAsText(currentSignalData);
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onSaveBinaryClicked() {
        try {
            if (currentSignalData == null) {
                Helper.showError("Błąd", "Brak sygnału do zapisania.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zapisz sygnał binarnie");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Pliki binarne", "*.bin")
            );
            fileChooser.setInitialFileName(currentSignalData.getName() + ".bin");

            File file = fileChooser.showSaveDialog(generateButton.getScene().getWindow());
            if (file == null) {
                return;
            }

            signalManager.saveSignalBinary(file.getAbsolutePath(), currentSignalData);
            Helper.showInfo("Sukces", "Sygnał zapisano do pliku binarnego.");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
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
            signalManager.addLoadedSignal(loaded);
            refreshLoadedSignalsList();
            loadedSignalsListView.getSelectionModel().select(loaded.getName());
            Helper.showInfo("Sukces", "Sygnał wczytano z pliku binarnego.");
        } catch (SignalFileException e) {
            Helper.showError("Błąd pliku", e.getMessage());
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onDeleteLoadedSignalClicked() {
        try {
            String selectedName = loadedSignalsListView.getSelectionModel().getSelectedItem();
            if (selectedName == null) {
                Helper.showError("Błąd", "Wybierz sygnał z listy.");
                return;
            }

            if (currentSignalData == null) {
                Helper.showError("Błąd", "Brak aktywnego sygnału.");
                return;
            }

            SignalType deletedSignalType = currentSignalData.getSignalType();
            signalManager.removeLoadedSignal(selectedName);

            if (selectedName.equals(currentSignalData.getName())) {
                currentSignalData = null;

                if (isDiscreteSignal(deletedSignalType)) {
                    scatterSignalChart.getData().clear();
                } else {
                    lineSignalChart.getData().clear();
                }
                histogramChart.getData().clear();
            }

            refreshLoadedSignalsList();
            statisticsDisplayService.clearStatistics();
            Helper.showInfo("Sukces", "Sygnał został usunięty.");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    @FXML
    private void onPerformSignalOperationClicked() {
        try {
            String signal1Name = operationSignal1ComboBox.getValue();
            String signal2Name = operationSignal2ComboBox.getValue();
            SignalOperationType operationType = signalOperationTypeComboBox.getValue();

            if (signal1Name == null || signal2Name == null) {
                Helper.showError("Błąd", "Wybierz oba sygnały.");
                return;
            }

            if (operationType == null) {
                Helper.showError("Błąd", "Wybierz operację.");
                return;
            }

            String resultName = generateUniqueName(buildOperationResultName(operationType, signal1Name, signal2Name));

            SignalData signal1 = signalManager.getLoadedSignal(signal1Name);
            SignalData signal2 = signalManager.getLoadedSignal(signal2Name);

            if (signal1 == null || signal2 == null) {
                Helper.showError("Błąd", "Nie można znaleźć wybranych sygnałów.");
                return;
            }

            validateSignalsCompatibility(signal1, signal2);

            List<Sample> resultSamples = SampleOperations.execute(
                    signal1.getSamples(),
                    signal2.getSamples(),
                    operationType
            );

            SignalParameters resultParams = new SignalParameters();
            resultParams.setStartTime(signal1.getParameters().getStartTime());
            resultParams.setSamplingFrequency(signal1.getParameters().getSamplingFrequency());
            resultParams.setPeriod(signal1.getParameters().getPeriod());
            resultParams.setDuration(signal1.getParameters().getDuration());

            SignalData resultSignal = new SignalData(
                    resultName,
                    SignalType.OPERATION_RESULT,
                    resultParams,
                    resultSamples
            );

            signalManager.addLoadedSignal(resultSignal);
            currentSignalData = resultSignal;

            refreshLoadedSignalsList();
            loadedSignalsListView.getSelectionModel().select(resultName);

            Helper.showInfo("Sukces", "Operację wykonano poprawnie.");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd danych", e.getMessage());
        } catch (Exception e) {
            Helper.showError("Nieoczekiwany błąd", "Wystąpił nieoczekiwany błąd: " + e.getMessage());
        }
    }

    private void displayLoadedSignal(SignalData signalData) {
        List<Sample> samples = signalData.getSamples();
        SignalType type = signalData.getSignalType();

        if (isDiscreteSignal(type)) {
            chartService.drawScatterSamples(samples, signalData.getName());
        } else {
            chartService.drawLineSamples(samples, signalData.getName());
        }
        lineSignalChart.setCreateSymbols(showSymbolsCheckBox.isSelected());

        statisticsDisplayService.updateStatistics(currentSignalData);
        chartService.drawHistogram(currentSignalData);
        showSignalDataAsText(signalData);
    }

    private void refreshLoadedSignalsList() {
        List<String> names = new ArrayList<>(signalManager.getLoadedSignals().keySet());
        loadedSignalsListView.getItems().setAll(names);
        refreshOperationSignalSelectors(names);
        updateConversionSignalComboBox(names);
    }

    private void updateConversionSignalComboBox(List<String> names) {
        conversionController.updateInputSignalComboBox(names);
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
        SignalParameters params = signalData.getParameters();

        sb.append(String.format("%-18s%s%n", "Nazwa:", signalData.getName()));
        sb.append(String.format("%-18s%s%n", "Typ:", signalData.getSignalType()));
        sb.append(String.format("%-18s%.4f s%n", "t1:", params.getStartTime()));

        // okres tylko jeśli sygnał jest okresowy (nie OPERATION_RESULT, nie szumy itp.)
        if (signalData.getSignalType().isPeriodic()) {
            sb.append(String.format("%-18s%.4f s%n", "T:", params.getPeriod()));
        }

        sb.append(String.format("%-18s%.2f Hz%n", "fs:", params.getSamplingFrequency()));
        sb.append(String.format("%-18s%s%n", "Rodzaj wartości:", "rzeczywiste"));
        sb.append(String.format("%-18s%d%n", "Liczba próbek:", signalData.getSamples().size()));
        sb.append("\n");

        sb.append(String.format("%-12s%s%n", "Próbki:", ""));
        sb.append(String.format("%10s  %12s%n", "t [s]", "x(t)"));
        sb.append("─".repeat(46)).append("\n");

        for (Sample sample : signalData.getSamples()) {
            sb.append(String.format("%10.4f  %12.6f%n",
                    sample.getTime(),
                    sample.getValue()));
        }

        fileContentTextArea.setText(sb.toString());
    }

    private String buildOperationResultName(SignalOperationType operationType, String name1, String name2) {
        String abbrev1 = name1.length() > 4 ? name1.substring(0, 4) : name1;
        String abbrev2 = name2.length() > 4 ? name2.substring(0, 4) : name2;
        return operationType.getName() + "_" + abbrev1 + "_" + abbrev2;
    }

    private String generateUniqueName(String base) {
        if (signalManager.getLoadedSignal(base) == null) {
            return base;
        }
        int counter = 2;
        while (signalManager.getLoadedSignal(base + "_" + counter) != null) {
            counter++;
        }
        return base + "_" + counter;
    }

    private boolean isDiscreteSignal(SignalType type) {
        return switch (type) {
            case UNIT_IMPULSE_SIGNAL, IMPULSE_NOISE_SIGNAL -> true;
            default -> false;
        };
    }
}