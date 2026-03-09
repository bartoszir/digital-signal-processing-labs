package dsp.task1.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainPanel {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
