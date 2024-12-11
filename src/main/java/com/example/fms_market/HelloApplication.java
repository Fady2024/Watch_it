package com.example.fms_market;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
     DataManager.loadData();
       new LoginPageFX(stage);
        DataManager.saveData();

    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        SubscriptionManager.readSubscriptions();
    }

    @Override
    public void stop() {
        SubscriptionManager.writeSubscriptions();
    }
}
