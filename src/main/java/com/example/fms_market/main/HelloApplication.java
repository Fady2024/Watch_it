package com.example.fms_market.main;

import com.example.fms_market.account.WelcomePage;
import com.example.fms_market.data.DataManager;
import com.example.fms_market.pages.Start;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Start.app();
            Image icon = new Image("file:src/main/java/com/example/fms_market/watch-movie.png");
            stage.getIcons().add(icon);
            stage.setTitle("watch it");
            DataManager.loadData();
            new WelcomePage(stage);
            DataManager.saveData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
