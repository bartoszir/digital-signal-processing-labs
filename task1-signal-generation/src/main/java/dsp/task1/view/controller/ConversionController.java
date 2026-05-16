package dsp.task1.view.controller;

import dsp.task1.logic.model.ConversionSession;
import dsp.task1.logic.service.SignalManager;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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

    private final XYChart.Series<Number, Number> originalSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> sampledSeries = new XYChart.Series<>();
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
        reconstructedSeries.setName("Zrekonstruowany");
        conversionChart.getData().addAll(originalSeries, sampledSeries, reconstructedSeries);
        conversionChart.setAnimated(false);
        conversionChart.setCreateSymbols(false);

        inputSignalComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasInput = newVal != null;
            sampleButton.setDisable(!hasInput);
            if (hasInput) {
                conversionSession.setOriginalSignal(signalManager.getLoadedSignal(newVal));
            }
        });

        reconstructionMethodComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isSinc = "sinc".equals(newVal);
            sincParamsBox.setVisible(isSinc);
            sincParamsBox.setManaged(isSinc);
        });

        showOriginalCheckBox.selectedProperty().addListener((obs, old, selected) ->
                originalSeries.getNode().setVisible(selected));
        showSampledCheckBox.selectedProperty().addListener((obs, old, selected) ->
                sampledSeries.getNode().setVisible(selected));
        showReconstructedCheckBox.selectedProperty().addListener((obs, old, selected) ->
                reconstructedSeries.getNode().setVisible(selected));
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
