package com.example.fms_market.account;

import com.example.fms_market.util.LanguageManager;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.util.Duration;

import javafx.scene.text.Font;

import javafx.scene.effect.DropShadow;

import java.awt.*;
public class WelcomePage {


    public WelcomePage(Stage stage) {

        Popup popup = new Popup();

        Label englishLabel = new Label("English");
        englishLabel.setStyle("-fx-background-color: #51209d; -fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-padding: 5px 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0.5, 0, 1);");

        Label germanLabel = new Label("German");
        germanLabel.setStyle("-fx-background-color: #51209d; -fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-padding: 5px 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0.5, 0, 1);");



        // Create the PauseTransition
        PauseTransition hideTransition = new PauseTransition(Duration.seconds(2));
        hideTransition.setOnFinished(_ -> popup.hide());

        // Add mouse event handlers to reset the transition if the mouse hovers over the popup
        if (popup.isShowing()) {
            ScaleTransition hideScaleTransition = new ScaleTransition(Duration.millis(300));
            hideScaleTransition.setFromX(1.0);
            hideScaleTransition.setFromY(1.0);
            hideScaleTransition.setToX(0.1);
            hideScaleTransition.setToY(0.1);
            hideScaleTransition.setOnFinished(_ -> popup.hide());
            hideScaleTransition.play();
        };

        englishLabel.setOnMouseClicked(_ -> {
            if(!"English".equals(LanguageManager.getInstance().getLanguage())){
                LanguageManager.getInstance().toggleLanguage();}
            popup.hide();
        });

        germanLabel.setOnMouseClicked(_ -> {
            if(!"German".equals(LanguageManager.getInstance().getLanguage())){
                LanguageManager.getInstance().toggleLanguage();}

            popup.hide();
        });


        Image gifImage = new Image(LanguageManager.getLanguageBasedString("file:src/main/resources/image/ff.gif","file:src/main/resources/image/final.gif"));
        ImageView imageView = new ImageView(gifImage);
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        Button signInButton = new Button(LanguageManager.getLanguageBasedString("anmelden","Sign in"));
        Button loginButton = new Button(LanguageManager.getLanguageBasedString("einloggen","Login"));

        styleButton(signInButton);
        styleButton(loginButton);

        signInButton.setOnMouseEntered(e -> signInButton.setScaleX(1.1));
        signInButton.setOnMouseExited(e -> signInButton.setScaleX(1));
        signInButton.setOnAction(e -> new SignUpPage(stage));
        loginButton.setOnMouseEntered(e -> loginButton.setScaleX(1.1));
        loginButton.setOnMouseExited(e -> loginButton.setScaleX(1));
        loginButton.setOnAction(e -> new LoginPageFX(stage));

        signInButton.setTranslateY(175);
        loginButton.setTranslateY(175);

        signInButton.setTranslateX(150);
        loginButton.setTranslateX(-300);

        StackPane root = new StackPane(imageView,signInButton,loginButton);
        Scene scene = new Scene(root);

        stage.setTitle("GIF");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

    }
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #3c3737; -fx-text-fill: white;");
        button.setFont(Font.font(16));

        button.setEffect(new DropShadow());
    }
}
