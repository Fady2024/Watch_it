package com.example.fms_market;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import javafx.scene.control.ComboBox;

public class LoginPageFX {
    private final Pane contentPane;
    private final Label titleLabel;
    private final TextField emailField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Button backButton;
    private final Text emailErrorText;
    private final Text passwordErrorText;
    private final Text signUpText;
    private final TopPanel dayNightSwitch;

    public LoginPageFX(Stage stage) {
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "German");
        languageComboBox.setValue(LanguageManager.getLanguageBasedString("German","English"));
        languageComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LanguageManager.getInstance().toggleLanguage();
            }
        });
        languageComboBox.setTranslateY(20);
        languageComboBox.setTranslateX(200);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        // Create theme switch panel
         dayNightSwitch = new TopPanel();

        backButton = createBackButton(stage);
        signUpText = createSignUpText(stage);

        // Create UI elements
        titleLabel = createLabel();
        emailField = createTextField();
        passwordField = createPasswordField();
        loginButton = createButton(Color.rgb(0, 123, 255));

        // Create error messages
        emailErrorText = createErrorText();
        passwordErrorText = createErrorText();

        // Login action
        loginButton.setOnAction(_ -> {
            String email = emailField.getText().trim().toLowerCase();
            String password = passwordField.getText().trim();

            boolean isValid = true;

            // Clear previous error messages
            emailErrorText.setText("");
            passwordErrorText.setText("");

            if (email.isEmpty()) {
                emailErrorText.setText("Email cannot be empty.");
                isValid = false;
            }

            if (password.isEmpty()) {
                passwordErrorText.setText("Password cannot be empty.");
                isValid = false;
            }

            if (isValid) {
                try {
                    User user = UserJsonHandler.getUserByEmailAndPassword(email, password);

                    if (user != null) {
                        new HomePage(user, stage);
                    } else {
                        showMessage("Invalid credentials!");
                    }
                } catch (IOException ex) {
                    showMessage("Error reading user data: " + ex.getMessage());
                }
            }
        });


        // Content pane
        contentPane = new Pane(languageComboBox);
        setupContentPane(dayNightSwitch);

        // Initial layout (before resize)
        updateLayout(stageWidth, stageHeight);
        Scene scene = new Scene(contentPane, stageWidth, stageHeight);
        scene.widthProperty().addListener((_, _, newWidth) -> updateLayout(newWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newHeight) -> updateLayout(scene.getWidth(), newHeight.doubleValue()));

        stage.setScene(scene);
        stage.show();

        // Theme update
        dayNightSwitch.addActionListener(this::updateColors);
        updateColors();
    }

    private Button createBackButton(Stage stage) {
        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-font-size: 16px; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-color: #888888; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-padding: 10px 20px;"
        );

        backButton.setOnMouseEntered(_ -> backButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-font-size: 16px; " +
                        "-fx-text-fill: #0066cc; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-color: #0066cc; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-padding: 10px 20px;"
        ));

        // Updated setOnMouseExited handler
        backButton.setOnMouseExited(_ -> {
            if (TopPanel.isDayValue()) {
                // Day theme styles
                backButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-font-size: 16px; " +
                                "-fx-text-fill: #333333; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-color: #888888; " +
                                "-fx-border-radius: 20px; " +
                                "-fx-padding: 10px 20px;"
                );
            } else {
                // Night theme styles
                backButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-font-size: 16px; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-color: white; " +
                                "-fx-border-radius: 20px; " +
                                "-fx-padding: 10px 20px;"
                );
            }
        });

        backButton.setOnAction(_ -> new WelcomePage(stage));
        return backButton;
    }


    private Text createSignUpText(Stage stage) {
        Text signUpText = new Text("If you don't have an account, Sign Up.");
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 165, 0)),
                new Stop(0.5, Color.rgb(34, 193, 195)),
                new Stop(1, Color.rgb(253, 29, 29)));

        signUpText.setFill(gradient);
        signUpText.setStyle("-fx-font-size: 14px; -fx-cursor: hand;");
        signUpText.setOnMouseClicked((MouseEvent _) -> new SignUpPage(stage));
        return signUpText;
    }

    private void showMessage(String message) {
        // Use JavaFX Alert for a quick fix
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Label createLabel() {
        Label label = new Label("Login to FMS Market");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, gray, 5, 0.5, 2, 2);");
        label.setTextFill(Color.BLACK);
        return label;
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setPromptText("Email");
        textField.setStyle("-fx-font-size: 16px; -fx-effect: dropshadow(gaussian, gray, 5, 0.5, 2, 2);");
        return textField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-effect: dropshadow(gaussian, gray, 5, 0.5, 2, 2);");
        return passwordField;
    }

    private Button createButton(Color backgroundColor) {
        Button button = new Button("Login");
        button.setStyle("-fx-background-color: " + toRgbString(backgroundColor) + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-effect: dropshadow(gaussian, gray, 10, 0.5, 2, 2);");
        return button;
    }

    private Text createErrorText() {
        Text errorText = new Text();
        errorText.setStyle("-fx-font-size: 14px; -fx-fill: red;");
        return errorText;
    }

    private void setupContentPane(TopPanel dayNightSwitch) {
        contentPane.getChildren().addAll(dayNightSwitch.getCanvas(), titleLabel, emailField, passwordField, loginButton, emailErrorText, passwordErrorText, signUpText,backButton);

        // Initial layout (before resize)
        updateLayout(contentPane.getWidth(), contentPane.getHeight());

    }

    private void updateLayout(double width, double height) {
        // Center the elements based on new window size
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        titleLabel.setLayoutX(centerX - 150);
        titleLabel.setLayoutY(centerY - 200);

        emailField.setLayoutX(centerX - 100);
        emailField.setLayoutY(centerY - 100);

        emailErrorText.setLayoutX(centerX - 100);
        emailErrorText.setLayoutY(centerY - 45);

        passwordField.setLayoutX(centerX - 100);
        passwordField.setLayoutY(centerY - 35);

        passwordErrorText.setLayoutX(centerX - 100);
        passwordErrorText.setLayoutY(centerY+20);

        loginButton.setLayoutX(centerX - 40);
        loginButton.setLayoutY(centerY + 50);

        signUpText.setLayoutX(centerX - 130);
        signUpText.setLayoutY(centerY + 150);

        backButton.setLayoutX(10);
        backButton.setLayoutY(10);

        // Position the dayNightSwitch on the right
        double dayNightSwitchWidth = 50; // Example width of the switch canvas
        double margin = 110; // Margin from the right edge
        dayNightSwitch.getCanvas().setLayoutX(width - dayNightSwitchWidth - margin);
        dayNightSwitch.getCanvas().setLayoutY(10); // Top right corner
    }

    private void updateColors() {
        if (TopPanel.isDayValue()) {
            // Day theme
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #c3cfe2);");
            titleLabel.setTextFill(Color.BLACK);
            backButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-font-size: 16px; " +
                            "-fx-text-fill: #333333; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-color: #888888; " +
                            "-fx-border-radius: 20px; " +
                            "-fx-padding: 10px 20px;"
            );
        } else {
            // Night theme
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #232526, #414345);");
            titleLabel.setTextFill(Color.WHITE);
            backButton.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-font-size: 16px; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-weight: bold; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-color: white; " +
                            "-fx-border-radius: 20px; " +
                            "-fx-padding: 10px 20px;"
            );
        }
    }


    private String toRgbString(Color color) {
        return String.format("rgb(%d, %d, %d)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
