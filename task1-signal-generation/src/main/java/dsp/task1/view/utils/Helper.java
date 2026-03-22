package dsp.task1.view.utils;

import javafx.scene.control.Alert;

public class Helper {
    public static double getDoubleFromField(javafx.scene.control.TextField field, String fieldName) {
        try {
            field.setStyle("");
            return Double.parseDouble(field.getText().trim());
        } catch (NumberFormatException e) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Niepoprawna wartość w polu: " + fieldName);
        }

    }

    public static int getIntFromField(javafx.scene.control.TextField field, String fieldName) {
        try {
            field.setStyle("");
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Niepoprawna wartość w polu: " + fieldName);
        }
    }

    public static void validatePositive(double value, String fieldName, javafx.scene.control.TextField field) {
        if (value <= 0) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Pole \"" + fieldName + "\" musi być większe od 0.");
        }
    }

    public static void validateNonNegative(int value, String fieldName, javafx.scene.control.TextField field) {
        if (value < 0) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Pole \"" + fieldName + "\" nie może być ujemne.");
        }
    }

    public static void validateProbability(double value, String fieldName, javafx.scene.control.TextField field) {
        if (value < 0 || value > 1) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Pole \"" + fieldName + "\" musi należeć do przedziału [0, 1].");
        }
    }

    public static void validateKw(double value, String fieldName, javafx.scene.control.TextField field) {
        if (value <= 0 || value >= 1) {
            field.setStyle("-fx-border-color: red;");
            throw new IllegalArgumentException("Pole \"" + fieldName + "\" musi należeć do przedziału (0, 1).");
        }
    }

    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
