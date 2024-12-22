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

    private final Button languageButton;

    public WelcomePage(Stage stage) {

        languageButton = new Button("🌐"); // Use the class member
        languageButton.setStyle("-fx-font-size: 25px; -fx-background-color: transparent; -fx-text-fill: black;");
        Popup popup = new Popup();
        VBox menuBox = new VBox(5);
        menuBox.setStyle("-fx-background-color: #333333; -fx-padding: 10px; -fx-background-radius: 10;");

        Label englishLabel = new Label("English");
        englishLabel.setStyle("-fx-background-color: #51209d; -fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-padding: 5px 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0.5, 0, 1);");

        Label germanLabel = new Label("German");
        germanLabel.setStyle("-fx-background-color: #51209d; -fx-text-fill: white; -fx-background-radius: 10; " +
                "-fx-padding: 5px 10px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 5, 0.5, 0, 1);");

        menuBox.getChildren().addAll(englishLabel, germanLabel);
        popup.getContent().add(menuBox);

        // Create the PauseTransition
        PauseTransition hideTransition = new PauseTransition(Duration.seconds(2));
        hideTransition.setOnFinished(_ -> popup.hide());

        // Add mouse event handlers to reset the transition if the mouse hovers over the popup
        menuBox.setOnMouseEntered(_ -> hideTransition.stop());
        menuBox.setOnMouseExited(_ -> hideTransition.play());
        if (popup.isShowing()) {
            ScaleTransition hideScaleTransition = new ScaleTransition(Duration.millis(300), menuBox);
            hideScaleTransition.setFromX(1.0);
            hideScaleTransition.setFromY(1.0);
            hideScaleTransition.setToX(0.1);
            hideScaleTransition.setToY(0.1);
            hideScaleTransition.setOnFinished(_ -> popup.hide());
            hideScaleTransition.play();
        }

        languageButton.setOnAction(_ -> Platform.runLater(() -> {
            double popupX = languageButton.localToScreen(languageButton.getBoundsInLocal()).getMaxX() - 70;
            double popupY = languageButton.localToScreen(languageButton.getBoundsInLocal()).getMaxY() - 10;
            if (popup.isShowing()) {
                ScaleTransition hideScaleTransition = new ScaleTransition(Duration.millis(300), menuBox);
                hideScaleTransition.setFromX(1.0);
                hideScaleTransition.setFromY(1.0);
                hideScaleTransition.setToX(0.1);
                hideScaleTransition.setToY(0.1);
                hideScaleTransition.setOnFinished(_ -> popup.hide());
                hideScaleTransition.play();
            } else {
                menuBox.setScaleX(0.1);
                menuBox.setScaleY(0.1);

                ScaleTransition showTransition = new ScaleTransition(Duration.millis(300), menuBox);
                showTransition.setFromX(0.1);
                showTransition.setFromY(0.1);
                showTransition.setToX(1.0);
                showTransition.setToY(1.0);

                popup.show(languageButton, popupX, popupY);
                showTransition.play();
                hideTransition.playFromStart();
            }
        }));

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

signInButton.setTranslateY(185);
loginButton.setTranslateY(185);
signInButton.setTranslateX(250);
loginButton.setTranslateX(-300);
        HBox hbox = new HBox(20, signInButton, loginButton);

        hbox.setStyle("-fx-alignment: center; -fx-padding: 20px;");
languageButton.setTranslateX(500);
languageButton.setTranslateY(-350);
        StackPane root = new StackPane(imageView,hbox,languageButton);
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
