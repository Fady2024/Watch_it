package com.example.fms_market;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void init() {
        SubscriptionManager.readSubscriptions();
    }

    @Override
    public void start(Stage stage) {
        try {
            DataManager.loadData();

            new LoginPageFX(stage);

            DataManager.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        SubscriptionManager.writeSubscriptions();
    }

    public static void main(String[] args) {
        launch();
    }
}