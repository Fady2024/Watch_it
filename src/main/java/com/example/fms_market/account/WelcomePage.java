package com.example.fms_market.account;

import com.example.fms_market.util.TopPanel;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomePage {

    private static final String WELCOME_MESSAGE = "Welcome to FMS Market!";
    private static final String AUDIO_FILE_PATH = "/FMS.wav";
    private static final int ANIMATION_SPEED = 10;
    private static final int TYPING_SPEED = 70;
    private static final int TIMER_DELAY = 15;

    private final Pane contentPane;
    private final Label welcomeLabel;
    private final Button loginButton;
    private final Button signUpButton;

    private int loginButtonX = -200;
    private int signUpButtonX = 1800;

    public WelcomePage(Stage stage) {
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);


        welcomeLabel = createLabel();
        loginButton = createButton("Login", Color.rgb(0, 123, 255));
        signUpButton = createButton("Sign Up", Color.RED);

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
        button.setStyle("-fx-background-color: " + toRgbString(backgroundColor) + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-effect: dropshadow(gaussian, gray, 10, 0.5, 2, 2);");

        // Add hover effect
        button.setOnMouseEntered(_ -> {
            button.setScaleX(1.1);
            button.setScaleY(1.1);
            button.setStyle(button.getStyle() + "; -fx-cursor: hand;");
        });
        button.setOnMouseExited(_ -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });

        return button;
    }

    private void setupContentPane(TopPanel dayNightSwitch) {
        contentPane.getChildren().addAll(dayNightSwitch.getCanvas(), welcomeLabel, loginButton, signUpButton);

        // Initial layout (before resize)
        updateLayout(contentPane.getWidth(), contentPane.getHeight());

        dayNightSwitch.getCanvas().setLayoutX(10);
        dayNightSwitch.getCanvas().setLayoutY(10);
    }

    private void updateLayout(double width, double height) {
        // Center the elements based on new window size
        double centerX = width / 2.0;
        double centerY = height / 2.0;

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
                System.err.println("Audio file not found: " + AUDIO_FILE_PATH);
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
