package com.example.fms_market.main;

import com.example.fms_market.account.WelcomePage;
import com.example.fms_market.admin.Revenue_page;
import com.example.fms_market.data.DataManager;
import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.payment_page;
import com.example.fms_market.pages.subscription_page;
import com.example.fms_market.util.LanguageManager;
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
            //new LoginPageFX(stage);
        //   new HomePage(new User("ouio","feew","fwef","fwefw","fwef","Wefwe","fwefw"),stage);

     //      new payment_page(stage,"file:src/main/resources/image/premium plan.png","basic");
            new WelcomePage(stage);
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