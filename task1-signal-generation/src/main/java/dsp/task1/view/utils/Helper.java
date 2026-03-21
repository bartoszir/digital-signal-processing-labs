package dsp.task1.view.utils;

public class Helper {
    public static double getDoubleFromField(javafx.scene.control.TextField field) {
        return Double.parseDouble(field.getText().trim());
    }

    public static int getIntFromField(javafx.scene.control.TextField field) {
        return Integer.parseInt(field.getText().trim());
    }
}
