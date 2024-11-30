package com.example.fms_market;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Show Add/Edit page for Movie or Series
        new AddEditShowPage(stage); // Add Movie or Series
    }

    public static void main(String[] args) {
        launch();
    }
}
