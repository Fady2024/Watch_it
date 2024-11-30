package com.example.fms_market;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

public class AccountPage {

    private static final String WELCOME_FONT = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Arial';";
    private static final String NAME_FONT = "-fx-font-size: 20px; -fx-font-family: 'Arial';";
    private static final String BUTTON_FONT = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial';";

    private final Stage stage;
    private final Pane contentPane;
    private final Label welcomeLabel;
    private final Label nameLabel;
    private final Button logOutButton;
    private final ImageView userImageView;

    // Editable fields for user details
    private final TextField emailField;
    private final TextField phoneField;
    private final TextField ageField;
    private final Button saveButton;

    private final User currentUser;

    public AccountPage(User user, Stage primaryStage) throws IOException {
        this.currentUser = user;
        this.stage = primaryStage;
        primaryStage.setTitle("Home Page");

        // Create UI components
        welcomeLabel = createWelcomeLabel(user.getRole());
        nameLabel = createNameLabel(user.getEmail());
        logOutButton = createLogOutButton();
        userImageView = createUserImageView(user.getUser_photo_path());

        // Editable user information fields
        emailField = createTextField(user.getEmail());
        phoneField = createTextField(user.getPhone());
        ageField = createTextField(user.getAge());
        saveButton = createSaveButton();

        // Set up the content pane
        TopPanel dayNightSwitch = new TopPanel();
        dayNightSwitch.addActionListener(this::updateColors);
        contentPane = new Pane();
        setupContentPane(dayNightSwitch);

        // Set the initial window size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(contentPane, stageWidth, stageHeight);

        // Listeners for window resizing
        scene.widthProperty().addListener((_, _, newWidth) -> updateLayout(newWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newHeight) -> updateLayout(scene.getWidth(), newHeight.doubleValue()));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupContentPane(TopPanel dayNightSwitch) {
        contentPane.getChildren().addAll(
                dayNightSwitch.getCanvas(),
                welcomeLabel,
                nameLabel,
                userImageView,
                logOutButton,
                emailField,
                phoneField,
                ageField,
                saveButton
        );

        // Initial layout (before resize)
        updateLayout(contentPane.getWidth(), contentPane.getHeight());

        dayNightSwitch.getCanvas().setLayoutX(10);
        dayNightSwitch.getCanvas().setLayoutY(10);
    }

    private Label createWelcomeLabel(String role) {
        String welcomeText = "Welcome " + (role.equals("admin") ? "Admin" : "Customer");
        Label welcomeLabel = new Label(welcomeText);
        welcomeLabel.setStyle(WELCOME_FONT);
        return welcomeLabel;
    }

    private Label createNameLabel(String username) {
        Label nameLabel = new Label(username);
        nameLabel.setStyle(NAME_FONT);
        return nameLabel;
    }

    private Button createLogOutButton() {
        Button logOutButton = new Button("Log Out");
        logOutButton.setStyle(BUTTON_FONT);
        logOutButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-background-radius: 10px;");
        logOutButton.setOnAction(_ -> new WelcomePage(stage));

        // Hover effects
        logOutButton.setOnMouseEntered(_ -> logOutButton.setStyle("-fx-background-color: #ff4500; -fx-text-fill: white; -fx-background-radius: 10px;"));
        logOutButton.setOnMouseExited(_ -> logOutButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-background-radius: 10px;"));

        return logOutButton;
    }

    private ImageView createUserImageView(String userPhotoPath) {
        File imageFile = new File(userPhotoPath);
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);

        // Set the image view size
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(false); // Allow the image to fill the entire space, possibly stretching

        // Create a circle to clip the image view
        Circle clip = new Circle(75, 75, 75);
        imageView.setClip(clip);

        // Add a border around the image
        imageView.setStyle("-fx-border-color: #ffffff; -fx-border-width: 5; -fx-border-radius: 75px;");

        return imageView;
    }

    private TextField createTextField(String text) {
        TextField textField = new TextField(text);
        textField.setStyle("-fx-font-size: 16px; -fx-font-family: 'Arial'; -fx-background-color: #f0f0f0;");
        return textField;
    }

    private Button createSaveButton() {
        Button saveButton = new Button("Save");
        saveButton.setStyle(BUTTON_FONT);
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10px;");
        saveButton.setOnAction(_ -> saveUserDetails());
        return saveButton;
    }

    private void saveUserDetails() {
        // Update the user object with the new details
        currentUser.setEmail(emailField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setAge(ageField.getText());

        // Save the updated user details to the JSON file
        try {
            UserJsonHandler.saveUser(currentUser);
            System.out.println("Updated user details saved.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save updated user details.");
        }
    }

    private void updateLayout(double width, double height) {
        // Center the elements based on the new window size
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        welcomeLabel.setLayoutX(centerX - 95);
        welcomeLabel.setLayoutY(centerY - 250);

        userImageView.setLayoutX(centerX - 75);  // Adjust to center
        userImageView.setLayoutY(centerY - 180);

        nameLabel.setLayoutX(centerX - 80);  // Adjust to center
        nameLabel.setLayoutY(centerY);

        emailField.setLayoutX(centerX - 100);
        emailField.setLayoutY(centerY + 30);

        phoneField.setLayoutX(centerX - 100);
        phoneField.setLayoutY(centerY + 80);

        ageField.setLayoutX(centerX - 100);
        ageField.setLayoutY(centerY + 130);

        saveButton.setLayoutX(centerX + 120);
        saveButton.setLayoutY(centerY + 180);

        logOutButton.setLayoutX(centerX + 300);
        logOutButton.setLayoutY(centerY + 250);
    }

    private void updateColors() {
        // Check if day or night mode is enabled
        if (TopPanel.isDayValue()) {
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #f5f7fa, #c3cfe2);");
            welcomeLabel.setTextFill(Color.BLACK);
            nameLabel.setTextFill(Color.BLACK);
            emailField.setStyle("-fx-background-color: #f0f0f0;");
            phoneField.setStyle("-fx-background-color: #f0f0f0;");
            ageField.setStyle("-fx-background-color: #f0f0f0;");
            logOutButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-background-radius: 10px;");
        } else {
            contentPane.setStyle("-fx-background-color: linear-gradient(to bottom, #232526, #414345);");
            welcomeLabel.setTextFill(Color.WHITE);
            nameLabel.setTextFill(Color.WHITE);
            emailField.setStyle("-fx-background-color: #333;");
            phoneField.setStyle("-fx-background-color: #333;");
            ageField.setStyle("-fx-background-color: #333;");
            logOutButton.setStyle("-fx-background-color: #ff6347; -fx-text-fill: white; -fx-background-radius: 10px;");
        }
    }
}