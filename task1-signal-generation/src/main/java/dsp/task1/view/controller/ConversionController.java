package dsp.task1.view.controller;

import dsp.task1.logic.model.ConversionSession;
import dsp.task1.logic.model.Sample;
import dsp.task1.logic.model.SignalData;
import dsp.task1.logic.service.QuantizationService;
import dsp.task1.logic.service.ReconstructionService;
import dsp.task1.logic.service.SamplingService;
import dsp.task1.logic.service.SignalManager;
import dsp.task1.view.utils.Helper;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.List;

public class ConversionController {

    private final ComboBox<String> inputSignalComboBox;
    private final TextField samplingFrequencyField;
    private final Button sampleButton;
    private final TextField bitsField;
    private final ComboBox<String> quantizationMethodComboBox;
    private final Button quantizeButton;
    private final ComboBox<String> reconstructionMethodComboBox;
    private final javafx.scene.layout.VBox sincParamsBox;
    private final TextField sincLeftSamplesField;
    private final TextField sincRightSamplesField;
    private final Button reconstructButton;

    private final CheckBox showOriginalCheckBox;
    private final CheckBox showSampledCheckBox;
    private final CheckBox showQuantizedCheckBox;
    private final CheckBox showReconstructedCheckBox;
    private final LineChart<Number, Number> conversionChart;

    private final Label mseSamplingLabel;
    private final Label mseQuantizationLabel;
    private final Label snrSamplingLabel;
    private final Label snrQuantizationLabel;
    private final Label psnrSamplingLabel;
    private final Label psnrQuantizationLabel;
    private final Label mdSamplingLabel;
    private final Label mdQuantizationLabel;

    private final SignalManager signalManager;
    private final ConversionSession conversionSession;
    private final SamplingService samplingService = new SamplingService();
    private final QuantizationService quantizationService = new QuantizationService();
    private final ReconstructionService reconstructionService = new ReconstructionService();

    private final XYChart.Series<Number, Number> originalSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> sampledSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> quantizedSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> reconstructedSeries = new XYChart.Series<>();

    public ConversionController(
            ComboBox<String> inputSignalComboBox,
            TextField samplingFrequencyField,
            Button sampleButton,
            TextField bitsField,
            ComboBox<String> quantizationMethodComboBox,
            Button quantizeButton,
            ComboBox<String> reconstructionMethodComboBox,
            javafx.scene.layout.VBox sincParamsBox,
            TextField sincLeftSamplesField,
            TextField sincRightSamplesField,
            Button reconstructButton,
            CheckBox showOriginalCheckBox,
            CheckBox showSampledCheckBox,
            CheckBox showQuantizedCheckBox,
            CheckBox showReconstructedCheckBox,
            LineChart<Number, Number> conversionChart,
            Label mseSamplingLabel,
            Label mseQuantizationLabel,
            Label snrSamplingLabel,
            Label snrQuantizationLabel,
            Label psnrSamplingLabel,
            Label psnrQuantizationLabel,
            Label mdSamplingLabel,
            Label mdQuantizationLabel,
            SignalManager signalManager,
            ConversionSession conversionSession
    ) {
        this.inputSignalComboBox = inputSignalComboBox;
        this.samplingFrequencyField = samplingFrequencyField;
        this.sampleButton = sampleButton;
        this.bitsField = bitsField;
        this.quantizationMethodComboBox = quantizationMethodComboBox;
        this.quantizeButton = quantizeButton;
        this.reconstructionMethodComboBox = reconstructionMethodComboBox;
        this.sincParamsBox = sincParamsBox;
        this.sincLeftSamplesField = sincLeftSamplesField;
        this.sincRightSamplesField = sincRightSamplesField;
        this.reconstructButton = reconstructButton;
        this.showOriginalCheckBox = showOriginalCheckBox;
        this.showSampledCheckBox = showSampledCheckBox;
        this.showQuantizedCheckBox = showQuantizedCheckBox;
        this.showReconstructedCheckBox = showReconstructedCheckBox;
        this.conversionChart = conversionChart;
        this.mseSamplingLabel = mseSamplingLabel;
        this.mseQuantizationLabel = mseQuantizationLabel;
        this.snrSamplingLabel = snrSamplingLabel;
        this.snrQuantizationLabel = snrQuantizationLabel;
        this.psnrSamplingLabel = psnrSamplingLabel;
        this.psnrQuantizationLabel = psnrQuantizationLabel;
        this.mdSamplingLabel = mdSamplingLabel;
        this.mdQuantizationLabel = mdQuantizationLabel;
        this.signalManager = signalManager;
        this.conversionSession = conversionSession;
    }

    public void initialize() {
        quantizationMethodComboBox.getItems().add("Zaokrąglanie");
        quantizationMethodComboBox.getSelectionModel().selectFirst();

        reconstructionMethodComboBox.getItems().addAll("ZOH", "sinc");
        reconstructionMethodComboBox.getSelectionModel().selectFirst();

        originalSeries.setName("Oryginalny");
        sampledSeries.setName("Spróbkowany");
        quantizedSeries.setName("Skwantyzowany");
        reconstructedSeries.setName("Zrekonstruowany");

        conversionChart.getData().addAll(originalSeries, sampledSeries, quantizedSeries, reconstructedSeries);
        conversionChart.setAnimated(false);
        conversionChart.setCreateSymbols(true);

        inputSignalComboBox.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    conversionSession.setOriginalSignal(signalManager.getLoadedSignal(newVal));
                    conversionSession.setSampledSignal(null);
                    conversionSession.setQuantizedSignal(null);
                    conversionSession.setReconstructedSignal(null);
                    updateChart();
                    sampleButton.setDisable(false);
                    quantizeButton.setDisable(true);
                    reconstructButton.setDisable(true);
                }
            });

        reconstructionMethodComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isSinc = "sinc".equals(newVal);
            sincParamsBox.setVisible(isSinc);
            sincParamsBox.setManaged(isSinc);
        });

        sampleButton.setOnAction(e -> onSampleButtonClicked());
        quantizeButton.setOnAction(e -> onQuantizeButtonClicked());
        reconstructButton.setOnAction(e -> onReconstructButtonClicked());

        showOriginalCheckBox.selectedProperty().addListener((obs, old, selected) ->
            setSeriesVisible(originalSeries, conversionSession.getOriginalSignal(), selected));
        showSampledCheckBox.selectedProperty().addListener((obs, old, selected) ->
            setSeriesVisible(sampledSeries, conversionSession.getSampledSignal(), selected));
        showQuantizedCheckBox.selectedProperty().addListener((obs, old, selected) ->
            setSeriesVisible(quantizedSeries, conversionSession.getQuantizedSignal(), selected));
        showReconstructedCheckBox.selectedProperty().addListener((obs, old, selected) ->
            setSeriesVisible(reconstructedSeries, conversionSession.getReconstructedSignal(), selected));
    }

    private void onSampleButtonClicked() {
        try {
            double targetFs = Double.parseDouble(samplingFrequencyField.getText().trim());
            SignalData sampled = samplingService.sample(
                conversionSession.getOriginalSignal(),
                targetFs
            );
            conversionSession.setSampledSignal(sampled);
            conversionSession.setQuantizedSignal(null);
            conversionSession.setReconstructedSignal(null);
            updateChart();
            quantizeButton.setDisable(false);
            reconstructButton.setDisable(false);
        } catch (NumberFormatException e) {
            Helper.showError("Błąd", "Podaj poprawną częstotliwość próbkowania.");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        }
    }

    private void onReconstructButtonClicked() {
        try {
            SignalData source = conversionSession.getQuantizedSignal() != null
                ? conversionSession.getQuantizedSignal()
                : conversionSession.getSampledSignal();

            double targetFs = conversionSession.getOriginalSignal()
                .getParameters().getSamplingFrequency();

            String method = reconstructionMethodComboBox.getValue();
            SignalData reconstructed;

            if ("ZOH".equals(method)) {
                reconstructed = reconstructionService.reconstructZOH(source, targetFs);
            } else {
                int samplesLeft = Integer.parseInt(sincLeftSamplesField.getText().trim());
                int samplesRight = Integer.parseInt(sincRightSamplesField.getText().trim());
                reconstructed = reconstructionService.reconstructSinc(
                    source, targetFs, samplesLeft, samplesRight);
            }

            conversionSession.setReconstructedSignal(reconstructed);
            updateChart();
        } catch (NumberFormatException e) {
            Helper.showError("Błąd", "Podaj poprawne wartości liczby próbek.");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        }
    }

    private void onQuantizeButtonClicked() {
        try {
            int bits = Integer.parseInt(bitsField.getText().trim());
            SignalData quantized = quantizationService.quantize(
                conversionSession.getSampledSignal(),
                bits
            );
            conversionSession.setQuantizedSignal(quantized);
            conversionSession.setReconstructedSignal(null);
            updateChart();
        } catch (NumberFormatException e) {
            Helper.showError("Błąd", "Podaj poprawną liczbę bitów (liczba całkowita).");
        } catch (IllegalArgumentException e) {
            Helper.showError("Błąd", e.getMessage());
        }
    }

    private void updateChart() {
        setSeriesVisible(originalSeries, conversionSession.getOriginalSignal(),
            showOriginalCheckBox.isSelected());
        setSeriesVisible(sampledSeries, conversionSession.getSampledSignal(),
            showSampledCheckBox.isSelected());
        setSeriesVisible(quantizedSeries, conversionSession.getQuantizedSignal(),
            showQuantizedCheckBox.isSelected());
        setSeriesVisible(reconstructedSeries, conversionSession.getReconstructedSignal(),
            showReconstructedCheckBox.isSelected());
    }

    private void setSeriesVisible(XYChart.Series<Number, Number> series,
                                   SignalData data,
                                   boolean visible) {
        series.getData().clear();
        if (visible && data != null) {
            for (Sample s : data.getSamples()) {
                series.getData().add(new XYChart.Data<>(s.getTime(), s.getValue()));
            }
        }
    }

    public void updateInputSignalComboBox(List<String> signalNames) {
        String current = inputSignalComboBox.getValue();
        inputSignalComboBox.getItems().setAll(signalNames);
        if (signalNames.contains(current)) {
            inputSignalComboBox.setValue(current);
        } else if (!signalNames.isEmpty()) {
            inputSignalComboBox.setValue(null);
            conversionSession.setOriginalSignal(null);
            sampleButton.setDisable(true);
            quantizeButton.setDisable(true);
            reconstructButton.setDisable(true);
        }
    }
}
