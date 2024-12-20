package com.example.fms_market.account;

import com.example.fms_market.util.TopPanel;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.subscription_page;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class SignUpPage {
    private File profileImage;
    private final VBox profileImagePanel = new VBox();
    private boolean _hasDigits = false;
    private boolean _hasSpecialChar = false;
    private boolean _hasUppercase = false;
    private boolean _hasLowercase = false;
    private String user_photo_path;
    private final Label emailLabel = new Label("Email:");
    private final Label passwordLabel = new Label("Password:");
    private final Label ageLabel = new Label("Age:");
    private final Label phoneLabel = new Label("Phone:");
    private final Text loginLabel;
    private final Button backButton;
    private final Button signUpButton;
    private static boolean isStrengthPanelVisible;

    private final Label digitCheckIcon = new Label("❌");
    private final Label specialCharCheckIcon = new Label("❌");
    private final Label upperCaseCheckIcon = new Label("❌");
    private final Label lowerCaseCheckIcon = new Label("❌");

    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField ageField = new TextField();

    private final VBox strengthPanel = new VBox();
    private final Pane contentPane = new Pane();
    private final TopPanel dayNightSwitch; // Declare signUpText as an instance variable

    public SignUpPage(Stage primaryStage) {
        primaryStage.setTitle("Sign Up Page");

        backButton = createBackButton(primaryStage);

// Profile image panel setup
        profileImagePanel.setPrefSize(110, 110);
        profileImagePanel.setStyle("-fx-background-color: lightgray; -fx-border-color: gray; -fx-border-width: 2;");

// Create a label with a camera emoji
        Label cameraEmoji = new Label("📷");
        cameraEmoji.setStyle("-fx-font-size: 40px; -fx-text-fill: gray;");

// Use StackPane to center the emoji and profile image
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(cameraEmoji);

// Adjust the alignment to move the emoji down
        StackPane.setAlignment(cameraEmoji, Pos.CENTER); // Centered vertically and horizontally

// Alternatively, you can use a manual translation to move it further down
        cameraEmoji.setTranslateY(25); // Adjust this value to control how far down the emoji moves

// Set mouse event to select profile image on click
        profileImagePanel.setOnMouseClicked(_ -> {
            selectProfileImage(primaryStage);
            stackPane.getChildren().clear();  // Remove camera emoji when image is selected
        });

        profileImagePanel.getChildren().add(stackPane); // Add StackPane to the profileImagePanel

        // Strength panel for password
        strengthPanel.setSpacing(5);
        strengthPanel.getChildren().addAll(
                new HBox(10, digitCheckIcon, new Label("Contains digits")),
                new HBox(10, specialCharCheckIcon, new Label("Contains special characters")),
                new HBox(10, upperCaseCheckIcon, new Label("Contains uppercase letter")),
                new HBox(10, lowerCaseCheckIcon, new Label("Contains lowercase letter"))
        );
        strengthPanel.setVisible(false);

        // Password field logic
        passwordField.setOnKeyReleased(_ -> validatePasswordStrength(passwordField.getText()));
        dayNightSwitch = new TopPanel();

        // Sign-up button
         signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        signUpButton.setOnAction(_ -> new subscription_page(primaryStage));

        // Login label
        loginLabel=createLoginText(primaryStage);

        setupContentPane(dayNightSwitch);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        // Set initial positions and sizes
        updateLayout(stageWidth, stageHeight);

        // Create the scene and add resize listener
        Scene scene = new Scene(contentPane, stageWidth, stageHeight);
        scene.widthProperty().addListener((_, _, newVal) -> updateLayout(newVal.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newVal) -> updateLayout(scene.getWidth(), newVal.doubleValue()));

        primaryStage.setScene(scene);
        primaryStage.show();
        dayNightSwitch.addActionListener(this::updateColors);
        updateColors();
    }

    private void setupContentPane(TopPanel dayNightSwitch) {
        contentPane.getChildren().addAll(dayNightSwitch.getCanvas(),
                backButton, profileImagePanel, emailLabel, emailField, phoneLabel, phoneField,
                passwordLabel, passwordField, ageLabel, ageField, strengthPanel, signUpButton, loginLabel
        );
        // Initial layout (before resize)
        updateLayout(contentPane.getWidth(), contentPane.getHeight());

    }
    private void updateLayout(double width, double height) {
        double centerX = width / 2.0;
        double ySpacing = 50;
        double startY = (height / 5)+100;

        profileImagePanel.setLayoutX(centerX - 50);
        profileImagePanel.setLayoutY(startY-120);

        emailLabel.setLayoutX(centerX - 150);
        emailLabel.setLayoutY(startY);

        emailField.setLayoutX(centerX - 75);
        emailField.setLayoutY(startY);

        phoneLabel.setLayoutX(centerX - 150);
        phoneLabel.setLayoutY(startY + ySpacing);

        phoneField.setLayoutX(centerX - 75);
        phoneField.setLayoutY(startY + ySpacing);

        passwordLabel.setLayoutX(centerX - 150);
        passwordLabel.setLayoutY(startY + 2 * ySpacing);

        passwordField.setLayoutX(centerX - 75);
        passwordField.setLayoutY(startY + 2 * ySpacing);

        strengthPanel.setLayoutX(centerX - 75);
        strengthPanel.setLayoutY(startY + 3 * ySpacing);

        ageLabel.setLayoutY(isStrengthPanelVisible?startY + 5 * ySpacing:startY + 3 * ySpacing);
        ageLabel.setLayoutX(centerX - 150);

        ageField.setLayoutX(centerX - 75);
        ageField.setLayoutY(isStrengthPanelVisible?startY + 5 * ySpacing:startY + 3 * ySpacing);



       backButton.setLayoutX(10);
        backButton.setLayoutY(10);

        signUpButton.setLayoutX(centerX - 50);
        signUpButton.setLayoutY(isStrengthPanelVisible?startY + 6 * ySpacing:startY + 4 * ySpacing);

        loginLabel.setLayoutX(centerX - 85);
        loginLabel.setLayoutY(isStrengthPanelVisible?startY + 7 * ySpacing:startY + 5 * ySpacing);

        // Position the dayNightSwitch on the right
        double dayNightSwitchWidth = 50; // Example width of the switch canvas
        double margin = 110; // Margin from the right edge
        dayNightSwitch.getCanvas().setLayoutX(width - dayNightSwitchWidth - margin);
        dayNightSwitch.getCanvas().setLayoutY(10); // Top right corner
        contentPane.requestLayout(); // This triggers a re-layout of the contentPane when anything changes

    }
    private void updateLayoutAndRecalculate() {
        updateLayout(contentPane.getWidth(), contentPane.getHeight());
        contentPane.requestLayout();  // Ensure re-layout is triggered
    }
    private void validatePasswordStrength(String password) {
        _hasDigits = Pattern.compile("[0-9]").matcher(password).find();
        _hasSpecialChar = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find();
        _hasUppercase = Pattern.compile("[A-Z]").matcher(password).find();
        _hasLowercase = Pattern.compile("[a-z]").matcher(password).find();

        // Set the icon and color based on the password validation
        digitCheckIcon.setText(_hasDigits ? "✅" : "❌");
        digitCheckIcon.setTextFill(_hasDigits ? Color.GREEN : Color.RED);

        specialCharCheckIcon.setText(_hasSpecialChar ? "✅" : "❌");
        specialCharCheckIcon.setTextFill(_hasSpecialChar ? Color.GREEN : Color.RED);

        upperCaseCheckIcon.setText(_hasUppercase ? "✅" : "❌");
        upperCaseCheckIcon.setTextFill(_hasUppercase ? Color.GREEN : Color.RED);

        lowerCaseCheckIcon.setText(_hasLowercase ? "✅" : "❌");
        lowerCaseCheckIcon.setTextFill(_hasLowercase ? Color.GREEN : Color.RED);

        // Set visibility of the strength panel based on whether the password is empty or not
        isStrengthPanelVisible = !password.isEmpty();
        strengthPanel.setVisible(isStrengthPanelVisible);

        // Update layout and trigger a re-layout if needed
        updateLayoutAndRecalculate();
    }
    private Text createLoginText(Stage stage) {
        Text loginText = new Text("If you have an account, Login!");
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 165, 0)),
                new Stop(0.5, Color.rgb(34, 193, 195)),
                new Stop(1, Color.rgb(253, 29, 29)));

        loginText.setFill(gradient);
        loginText.setStyle("-fx-font-size: 14px; -fx-cursor: hand;");
        loginText.setOnMouseClicked((MouseEvent _) -> new LoginPageFX(stage));
        return loginText;
    }
    private void handleSignUp(Stage primaryStage) {
        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        String phone = phoneField.getText().trim();
        String age = ageField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || age.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        if (isValidPassword()) {
            try {
                if (UserJsonHandler.emailExists(email)) {
                    showAlert(Alert.AlertType.ERROR, "Email already exists! Please use a different email.");
                    return;
                }

                // Ensure the user_photo_path is set
                if (profileImage != null) {
                    user_photo_path = profileImage.getAbsolutePath();
                }

             //   UserJsonHandler.saveUser(new User(email, password, "customer", phone, age, user_photo_path));
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful!");
                new LoginPageFX(primaryStage);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error saving user: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Password does not meet the criteria!");
        }
    }

    private void selectProfileImage(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        profileImage = fileChooser.showOpenDialog(primaryStage);

        profileImagePanel.getChildren().clear();  // Clear previous content

        if (profileImage != null) {
            // Image selection
            Image img = new Image(profileImage.toURI().toString());
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            // Using StackPane to center the image
            StackPane centeredPane = new StackPane();
            centeredPane.getChildren().add(imageView);
            StackPane.setAlignment(imageView, Pos.CENTER);

            // Add the centered pane to the profileImagePanel
            profileImagePanel.getChildren().add(centeredPane);
        } else {
            // Show camera emoji if no profile image is selected
            Label cameraEmoji = new Label("📷");
            cameraEmoji.setStyle("-fx-font-size: 40px; -fx-text-fill: gray;");

            // Use StackPane to center the emoji
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(cameraEmoji);

            // Adjust the alignment to move the emoji down
            StackPane.setAlignment(cameraEmoji, Pos.CENTER);

            // Optionally, move the emoji further down with translation
            cameraEmoji.setTranslateY(25);  // Adjust this value to control how far down the emoji moves

            // Add the emoji pane to the profileImagePanel
            profileImagePanel.getChildren().add(stackPane);
        }
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

    private boolean isValidPassword() {
        return _hasDigits && _hasSpecialChar && _hasUppercase && _hasLowercase;
    }
    private void updateColors() {
        boolean isDayMode = TopPanel.isDayValue();

        String backgroundColor = isDayMode ? "white" : "#2b2b2b"; // Light or dark background
        String textColor = isDayMode ? "black" : "white";         // Light or dark text
        String buttonColor = isDayMode ? "#007bff" : "#0056b3";   // Light or dark button
        String labelStyle = String.format("-fx-text-fill: %s;", textColor);

        // Update the background of the content pane
        contentPane.setStyle(String.format("-fx-background-color: %s;", backgroundColor));

        // Update label styles
        emailLabel.setStyle(labelStyle);
        passwordLabel.setStyle(labelStyle);
        phoneLabel.setStyle(labelStyle);
        ageLabel.setStyle(labelStyle);
        loginLabel.setStyle(String.format("-fx-text-fill: %s; -fx-underline: true; -fx-cursor: hand;", textColor));


        // Update button styles
        backButton.setStyle(String.format("-fx-font-size: 10px; -fx-font-weight: bold; -fx-background-color: %s; -fx-text-fill: %s;", backgroundColor, textColor));
        signUpButton.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white;", buttonColor));

        // Update strength panel styles
        String checkIconColor = isDayMode ? "green" : "lightgreen";
        String crossIconColor = isDayMode ? "red" : "darkred";
        digitCheckIcon.setStyle(String.format("-fx-text-fill: %s;", _hasDigits ? checkIconColor : crossIconColor));
        specialCharCheckIcon.setStyle(String.format("-fx-text-fill: %s;", _hasSpecialChar ? checkIconColor : crossIconColor));
        upperCaseCheckIcon.setStyle(String.format("-fx-text-fill: %s;", _hasUppercase ? checkIconColor : crossIconColor));
        lowerCaseCheckIcon.setStyle(String.format("-fx-text-fill: %s;", _hasLowercase ? checkIconColor : crossIconColor));

        // Profile image panel border update
        profileImagePanel.setStyle(String.format("-fx-background-color: %s; -fx-border-color: gray; -fx-border-width: 2;", backgroundColor));

    if(isDayMode){
        contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #c3cfe2);");
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
    }else{
        contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #232526, #414345);");
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

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }
}
