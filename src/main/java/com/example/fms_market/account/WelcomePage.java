package com.example.fms_market.account;

import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.TopPanel;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomePage {

    private static final String WELCOME_MESSAGE = "Welcome to WATCH IT!";
    private static final String AUDIO_FILE_PATH = "/FMS.wav";
    private static final int ANIMATION_SPEED = 10;
    private static final int TYPING_SPEED = 70;
    private static final int TIMER_DELAY = 15;

    private final Pane contentPane;
    private final Label welcomeLabel;
    private final Button loginButton;
    private final Button signUpButton;
    private final Button languageButton;

    private int loginButtonX = -200;
    private int signUpButtonX = 1800;

    public WelcomePage(Stage stage) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

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

        welcomeLabel = createLabel();
        loginButton = createButton("Login", Color.rgb(0, 123, 255));
        signUpButton = createButton("Sign Up", Color.RED);
        Runnable onHover = () -> {
            loginButton.setStyle("-fx-background-color: linear-gradient(to top left, #5C0C5A, #9C0479); -fx-text-fill: white; -fx-background-radius: 80;");
            signUpButton.setStyle("-fx-background-color: linear-gradient(to top left, #5C0C5A, #9C0479); -fx-text-fill: white; -fx-background-radius: 80;");

        };


        Runnable onExit = () -> {
            loginButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 80;");
            signUpButton.setStyle("-fx-background-color: linear-gradient(to top left, #5C0C5A, #9C0479); -fx-text-fill: white; -fx-background-radius: 80;");

        };

        loginButton.setOnAction(_ -> new LoginPageFX(stage));
        signUpButton.setOnAction(_ -> new SignUpPage(stage));
        TopPanel dayNightSwitch = new TopPanel();

        dayNightSwitch.addActionListener(this::updateColors);

        contentPane = new Pane();
        setupContentPane(dayNightSwitch);

        Scene scene = new Scene(contentPane, stageWidth, stageHeight);
        scene.widthProperty().addListener((_, _, newWidth) -> updateLayout(newWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newHeight) -> updateLayout(scene.getWidth(), newHeight.doubleValue()));

        stage.setScene(scene);
        stage.show();

        startAnimation();
        updateColors();
    }

    private Label createLabel() {
        Label label = new Label("");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, gray, 5, 0.5, 2, 2);");
        label.setTextFill(Color.BLACK);
        return label;
    }

    private Button createButton(String text, Color backgroundColor) {
        Button button = new Button(text);
        button.setStyle(STR."-fx-background-color: \{toRgbString(backgroundColor)}; -fx-text-fill: white; -fx-font-size: 16px; -fx-effect: dropshadow(gaussian, gray, 10, 0.5, 2, 2);");

        // Add hover effect
        button.setOnMouseEntered(_ -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setStyle(STR."\{button.getStyle()}; -fx-cursor: hand;");
        });
        button.setOnMouseExited(_ -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        return button;
    }

    private void setupContentPane(TopPanel dayNightSwitch) {
        contentPane.getChildren().addAll(dayNightSwitch.getCanvas(), languageButton, welcomeLabel, loginButton, signUpButton);

        // Initial layout (before resize)
        updateLayout(contentPane.getWidth(), contentPane.getHeight());

        dayNightSwitch.getCanvas().setLayoutX(10);
        dayNightSwitch.getCanvas().setLayoutY(10);
    }

    private void updateLayout(double width, double height) {
        // Center the elements based on new window size
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        languageButton.setLayoutX(width-100);
        languageButton.setLayoutY(15);
        welcomeLabel.setLayoutX(centerX - 150);
        welcomeLabel.setLayoutY(centerY - 150);

        loginButton.setLayoutX(centerX - 75);
        loginButton.setLayoutY(centerY - 50);

        signUpButton.setLayoutX(centerX - 75);
        signUpButton.setLayoutY(centerY + 50);
    }

    private void startTypingAnimation() {
        StringBuilder currentText = new StringBuilder();
        AnimationTimer typingTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= TYPING_SPEED * 1_000_000) {
                    if (currentText.length() < WELCOME_MESSAGE.length()) {
                        currentText.append(WELCOME_MESSAGE.charAt(currentText.length()));
                        welcomeLabel.setText(currentText.toString());
                    } else {
                        stop();
                    }
                    lastUpdate = now;
                }
            }
        };
        typingTimer.start();
    }

    private void playAudio() {
        try {
            URL audioFileURL = getClass().getResource(AUDIO_FILE_PATH);
            if (audioFileURL == null) {
                System.err.println(STR."Audio file not found: \{AUDIO_FILE_PATH}");
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFileURL);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Logger.getLogger(WelcomePage.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void startAnimation() {
        playAudio();
        startTypingAnimation();
        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= TIMER_DELAY * 1_000_000) {
                    int centerX = (int) (contentPane.getWidth() / 2);

                    if (loginButtonX < centerX - 5) {
                        loginButtonX += ANIMATION_SPEED;
                        loginButton.setLayoutX(loginButtonX);
                    }

                    if (signUpButtonX > centerX) {
                        signUpButtonX -= ANIMATION_SPEED;
                        signUpButton.setLayoutX(signUpButtonX);
                    }

                    lastUpdate = now;
                }
            }
        };
        animationTimer.start();
    }

    private void updateColors() {
        if (TopPanel.isDayValue()) {
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #c3cfe2);");
            welcomeLabel.setTextFill(Color.BLACK);
        } else {
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #232526, #414345);");
            welcomeLabel.setTextFill(Color.WHITE);
        }
    }

    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}
