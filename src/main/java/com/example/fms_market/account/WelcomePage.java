package com.example.fms_market.account;

import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.TopPanel;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.text.Font;

public class WelcomePage {

    private final Button languageButton;
    private final Button signInButton;
    private final Button loginButton;
    private final ImageView imageView;

    public WelcomePage(Stage stage) {
        languageButton = new Button("🌐");
        languageButton.setStyle("-fx-font-size: 25px; -fx-background-color: transparent; -fx-text-fill: white;");
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

        PauseTransition hideTransition = new PauseTransition(Duration.seconds(2));
        hideTransition.setOnFinished(_ -> popup.hide());

        menuBox.setOnMouseEntered(_ -> hideTransition.stop());
        menuBox.setOnMouseExited(_ -> hideTransition.play());

        languageButton.setOnMouseClicked(event -> {
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
        });

        englishLabel.setOnMouseClicked(_ -> {
            if (!"English".equals(LanguageManager.getInstance().getLanguage())) {
                LanguageManager.getInstance().toggleLanguage();
                updateTexts();
            }
            popup.hide();
        });

        germanLabel.setOnMouseClicked(_ -> {
            if (!"German".equals(LanguageManager.getInstance().getLanguage())) {
                LanguageManager.getInstance().toggleLanguage();
                updateTexts();
            }
            popup.hide();
        });

        Image gifImage = new Image(LanguageManager.getLanguageBasedString("file:src/main/resources/image/ff.gif", "file:src/main/resources/image/final.gif"));
        imageView = new ImageView(gifImage);
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        signInButton = new Button(LanguageManager.getLanguageBasedString("anmelden", "Sign in"));
        loginButton = new Button(LanguageManager.getLanguageBasedString("einloggen", "Login"));

        TopPanel topPanel = new TopPanel();
        topPanel.addActionListener(() -> {
            boolean isDay = TopPanel.isDayValue();
            applyTheme(isDay);
            styleButton(signInButton, isDay);
            styleButton(loginButton, isDay);
        });

        boolean isDay = TopPanel.isDayValue();
        applyTheme(isDay);
        styleButton(signInButton, isDay);
        styleButton(loginButton, isDay);

        signInButton.setOnMouseEntered(e -> signInButton.setScaleX(1.1));
        signInButton.setOnMouseExited(e -> signInButton.setScaleX(1));
        signInButton.setOnAction(e -> new SignUpPage(stage));
        loginButton.setOnMouseEntered(e -> loginButton.setScaleX(1.1));
        loginButton.setOnMouseExited(e -> loginButton.setScaleX(1));
        loginButton.setOnAction(e -> new LoginPageFX(stage));

        signInButton.setTranslateY(180);
        loginButton.setTranslateY(180);
        signInButton.setTranslateX(210);
        loginButton.setTranslateX(-350);
        HBox hbox = new HBox(20, signInButton, loginButton);

        hbox.setStyle("-fx-alignment: center; -fx-padding: 20px;");
        StackPane.setAlignment(languageButton, Pos.TOP_RIGHT);
        languageButton.setTranslateX(-18);
        languageButton.setTranslateY(20);

        StackPane.setAlignment(topPanel.getCanvas(), Pos.TOP_RIGHT);
        topPanel.getCanvas().setTranslateX(-35);
        topPanel.getCanvas().setTranslateY(-3);

        StackPane root = new StackPane(imageView, hbox, languageButton, topPanel.getCanvas());
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void updateTexts() {
        signInButton.setText(LanguageManager.getLanguageBasedString("anmelden", "Sign in"));
        loginButton.setText(LanguageManager.getLanguageBasedString("einloggen", "Login"));
        Image gifImage = new Image(LanguageManager.getLanguageBasedString("file:src/main/resources/image/ff.gif", "file:src/main/resources/image/final.gif"));
        imageView.setImage(gifImage);
    }

    private void styleButton(Button button, boolean isDay) {
        String dayStyle = "-fx-background-color: linear-gradient(#ff5400, #be1d00); " +
                "-fx-background-radius: 30; " +
                "-fx-background-insets: 0; " +
                "-fx-text-fill: black; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0.5, 0, 0);";
        String nightStyle = "-fx-background-color: linear-gradient(#ff5400, #be1d00); " +
                "-fx-background-radius: 30; " +
                "-fx-background-insets: 0; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16px; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0.5, 0, 0);";

        button.setStyle(!isDay ? dayStyle : nightStyle);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setStyle(STR."-fx-background-color: linear-gradient(#ff7f50, #ff4500); -fx-background-radius: 30; -fx-background-insets: 0; -fx-text-fill: \{!isDay ? "black" : "white"}; -fx-font-size: 16px; -fx-padding: 10 20 10 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0.5, 0, 0);");
        });
        button.setOnMouseExited(e -> {
            button.setScaleX(1);
            button.setScaleY(1);
            button.setStyle(!isDay ? dayStyle : nightStyle);
        });
    }

    private void applyTheme(boolean isDay) {
        if (!isDay) {
            languageButton.setStyle("-fx-font-size: 25px; -fx-background-color: transparent; -fx-text-fill: gray;");
        } else {
            languageButton.setStyle("-fx-font-size: 25px; -fx-background-color: transparent; -fx-text-fill: white;");
        }
    }
}