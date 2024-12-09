package com.example.fms_market;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Load data at the beginning
            DataManager.loadData();

            new HomePage(new User("few","feewf","Fwefw","efwfwe","fwefwe","fwefew"),stage);

            // Save data at the end
            DataManager.saveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
