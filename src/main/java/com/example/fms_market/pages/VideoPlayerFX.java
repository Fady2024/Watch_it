package com.example.fms_market.pages;

import com.example.fms_market.util.Banner;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;

public class VideoPlayerFX {
    private final String videoUrl;
    private final Stage stage;
    private int stageWidth;
    private int stageHeight;

    public VideoPlayerFX(String videoUrl, Stage stage) {
        this.videoUrl = videoUrl;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.setJavaScriptEnabled(true);
        webEngine.setCreatePopupHandler(_ -> null);  // Disable pop-ups

        String adBlockScript =
                "document.querySelectorAll('iframe, img, .ads, .ad, .banner, .popup').forEach(function(el) { el.style.display = 'none'; });";

        webEngine.getLoadWorker().stateProperty().addListener((_, _, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                webEngine.executeScript(adBlockScript);
            }
        });

        webEngine.load(videoUrl);

        setScreenDimensions();

        StackPane videoContainer = new StackPane();
        videoContainer.getChildren().add(webView);

        double scaleFactor = 0.6;
        videoContainer.setScaleX(scaleFactor);
        videoContainer.setScaleY(scaleFactor);

        BorderPane root = new BorderPane();
        root.setTop(Banner.getBanner(stage, "Video Player"));
        root.setCenter(videoContainer);

        // Set background color to #252525
        root.setStyle("-fx-background-color: #252525;");

        Scene scene = new Scene(root, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Video Player");
        stage.show();
    }

    private void setScreenDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stageWidth = (int) screenSize.getWidth();
        stageHeight = (int) (screenSize.getHeight() / 1.1);
    }
}