package com.example.fms_market;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        new WelcomePage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}