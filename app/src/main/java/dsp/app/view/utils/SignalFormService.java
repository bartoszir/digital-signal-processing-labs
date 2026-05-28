package dsp.app.view.utils;

import dsp.common.model.SignalParameters;
import dsp.common.signal.SignalType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import static dsp.app.view.utils.Helper.*;

public class SignalFormService {

    private final ComboBox<SignalType> signalTypeComboBox;
    private final TextField amplitudeInputField;
    private final TextField startTimeInputField;
    private final TextField durationInputField;
    private final TextField samplingFrequencyInputField;
    private final TextField periodInputField;
    private final TextField periodRectangularInputField;
    private final TextField kwInputField;
    private final TextField tsInputField;
    private final TextField nsInputField;
    private final TextField pInputField;
    private final GridPane periodParamsPane;
    private final GridPane rectangularParamsPane;
    private final GridPane jumpParamsPane;
    private final GridPane unitImpulseParamsPane;
    private final GridPane impulseNoiseParamsPane;

    public SignalFormService(
            ComboBox<SignalType> signalTypeComboBox,
            TextField amplitudeInputField,
            TextField startTimeInputField,
            TextField durationInputField,
            TextField samplingFrequencyInputField,
            TextField periodInputField,
            TextField periodRectangularInputField,
            TextField kwInputField,
            TextField tsInputField,
            TextField nsInputField,
            TextField pInputField,
            GridPane periodParamsPane,
            GridPane rectangularParamsPane,
            GridPane jumpParamsPane,
            GridPane unitImpulseParamsPane,
            GridPane impulseNoiseParamsPane
    ) {
        this.signalTypeComboBox = signalTypeComboBox;
        this.amplitudeInputField = amplitudeInputField;
        this.startTimeInputField = startTimeInputField;
        this.durationInputField = durationInputField;
        this.samplingFrequencyInputField = samplingFrequencyInputField;
        this.periodInputField = periodInputField;
        this.periodRectangularInputField = periodRectangularInputField;
        this.kwInputField = kwInputField;
        this.tsInputField = tsInputField;
        this.nsInputField = nsInputField;
        this.pInputField = pInputField;
        this.periodParamsPane = periodParamsPane;
        this.rectangularParamsPane = rectangularParamsPane;
        this.jumpParamsPane = jumpParamsPane;
        this.unitImpulseParamsPane = unitImpulseParamsPane;
        this.impulseNoiseParamsPane = impulseNoiseParamsPane;
    }

    public void setDefaultValues() {
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

    public void updateSpecificParamsVisibility() {
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

    public SignalParameters buildParameters(SignalType selectedType) {
        SignalParameters params = new SignalParameters();

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

    public void clearFieldStyles() {
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
}
