package com.example.fms_market.main;

import com.example.fms_market.account.LoginPageFX;
import com.example.fms_market.data.DataManager;
import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.HomePage;
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

            new HomePage(new User("feew","fwef","fwefw","fwef","Wefwe","fwefw","hdh"),stage);

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