package dsp.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/dsp/app/view/main-view-v2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        scene.getStylesheets().add(MainApp.class.getResource("/dsp/app/view/style.css").toExternalForm());
        stage.setTitle("CPS");
        stage.setScene(scene);
        stage.show();
    }

    public void stop() {
        System.out.println("Application is closing...");
    }

    public static void main(String[] args) {
        launch();
    }
}
