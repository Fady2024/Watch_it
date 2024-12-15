package com.example.fms_market;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class AccountPage {
    private final User currentUser;
    private final BorderPane layout;
    private final Stage stage;

    private String initialEmail;
    private String initialPhone;
    private String initialAge;

    public AccountPage(User user, Stage stage, Sidebar.SidebarState initialState) {
        this.currentUser = user;
        this.stage = stage;

        Sidebar sidebar = new Sidebar(initialState, stage, currentUser);
        layout = new BorderPane();
        layout.setLeft(sidebar);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("FMS Market with Sidebar");
        stage.show();

        sidebar.setSidebarListener(new Sidebar.SidebarListener() {
            @Override
            public void onUserDetailsSelected() {
                displayUserDetails();
            }

            @Override
            public void onFavouritesSelected() {
                navigateToFavoritesPage();
            }

            @Override
            public void onWatchedSelected() {
                // Placeholder for watched selection action
                System.out.println("Watched menu item selected.");
            }

            @Override
            public void onSubscriptionSelected() {
                // Placeholder for subscription selection action
                System.out.println("Subscription menu item selected.");
            }
            public void onAboutUsSelected(){
                navigateToAboutUs();

            }
        });

        // Display user details directly
        displayUserDetails();
    }

    private void displayUserDetails() {
        VBox mainBox = new VBox(10);
        mainBox.setStyle("-fx-padding: 20; -fx-background-color: #1c1c1c; -fx-alignment: center;");

        Label userDetailsLabel = new Label("User Details");
        userDetailsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-padding-bottom: 30px;");

        VBox userDetailsBox = new VBox(10);
        userDetailsBox.setStyle("-fx-padding: 20; -fx-background-color: #1c1c1c; -fx-alignment: center;");

        Label photoLabel = new Label("User Photo:");
        photoLabel.setStyle("-fx-text-fill: white;");
        ImageView userPhoto = new ImageView(new Image(STR."file:\{currentUser.getUser_photo_path()}"));
        userPhoto.setFitHeight(100);
        userPhoto.setFitWidth(100);
        Circle clip = new Circle(50, 50, 50);
        userPhoto.setClip(clip);

        // Store initial photo path
        AtomicReference<String> initialPhotoPath = new AtomicReference<>(currentUser.getUser_photo_path());
        AtomicReference<File> selectedFileRef = new AtomicReference<>();

        userPhoto.setOnMouseClicked(_ -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                userPhoto.setImage(new Image(STR."file:\{selectedFile.getAbsolutePath()}"));
                selectedFileRef.set(selectedFile);
            }
        });

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");
        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-alignment: center;");

        Label phoneLabel = new Label("Phone:");
        phoneLabel.setStyle("-fx-text-fill: white;");
        TextField phoneField = new TextField(currentUser.getPhone());
        phoneField.setMaxWidth(300);
        phoneField.setStyle("-fx-alignment: center;");

        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-text-fill: white;");
        TextField ageField = new TextField(currentUser.getAge());
        ageField.setMaxWidth(300);
        ageField.setStyle("-fx-alignment: center;");

        // Store initial values
        initialEmail = currentUser.getEmail();
        initialPhone = currentUser.getPhone();
        initialAge = currentUser.getAge();

        Label changePasswordLabel = new Label("Change Password");
        changePasswordLabel.setStyle("-fx-text-fill: #8969ba; -fx-cursor: hand;");
        changePasswordLabel.setOnMouseClicked(_ -> showChangePasswordPopup());

        Button applyButton = new Button("Apply");
        applyButton.setStyle("-fx-background-color: #51209d; -fx-text-fill: white; -fx-background-radius: 10;");
        applyButton.setOnAction(_ -> {
            currentUser.setEmail(emailField.getText().toLowerCase());
            currentUser.setPhone(phoneField.getText());
            currentUser.setAge(ageField.getText());
            try {
                if (selectedFileRef.get() != null) {
                    String newPhotoPath = copyPhotoToResources(selectedFileRef.get());
                    currentUser.setUser_photo_path(newPhotoPath);
                }
                UserJsonHandler.saveUser(currentUser);
                showAlert("Success", "User details updated successfully.");
                // Update initial values
                initialEmail = currentUser.getEmail();
                initialPhone = currentUser.getPhone();
                initialAge = currentUser.getAge();
                initialPhotoPath.set(currentUser.getUser_photo_path());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 10;");
        cancelButton.setOnAction(_ -> {
            // Restore initial values
            emailField.setText(initialEmail);
            phoneField.setText(initialPhone);
            ageField.setText(initialAge);
            userPhoto.setImage(new Image(STR."file:\{initialPhotoPath}"));
            currentUser.setUser_photo_path(initialPhotoPath.get());
            selectedFileRef.set(null);
        });

        HBox buttonBox = new HBox(10, cancelButton, applyButton);
        buttonBox.setStyle("-fx-alignment: center;");
        userDetailsBox.getChildren().addAll(photoLabel, userPhoto, emailLabel, emailField, phoneLabel, phoneField, ageLabel, ageField, changePasswordLabel, buttonBox);

        mainBox.getChildren().addAll(userDetailsLabel, userDetailsBox);
        layout.setCenter(mainBox);
    }

    private String copyPhotoToResources(File selectedFile) throws IOException {
        Path resourcesDir = Path.of("src/main/resources/UserImages");
        if (!Files.exists(resourcesDir)) {
            Files.createDirectories(resourcesDir);
        }

        String fileName = selectedFile.getName();
        Path destinationPath = resourcesDir.resolve(fileName);
        int count = 1;

        while (Files.exists(destinationPath)) {
            String newFileName = fileName.replaceFirst("(\\.[^.]+)$", STR."_\{count}$1");
            destinationPath = resourcesDir.resolve(newFileName);
            count++;
        }

        Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath.toString();
    }

    private void showChangePasswordPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(10);

        Label oldPasswordLabel = new Label("Old Password:");
        oldPasswordLabel.setStyle("-fx-text-fill: white;");
        PasswordField oldPasswordField = new PasswordField();

        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-text-fill: white;");
        PasswordField newPasswordField = new PasswordField();

        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        Button confirmButton = new Button("Confirm");

        cancelButton.setOnAction(_ -> popupStage.close());
        confirmButton.setOnAction(_ -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            if (currentUser.getPassword().equals(oldPassword)) {
                currentUser.setPassword(newPassword);
                try {
                    UserJsonHandler.saveUser(currentUser);
                    showAlert("Success", "Password changed successfully.");
                    popupStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                showAlert("Error", "Old password is incorrect.");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(oldPasswordLabel, oldPasswordField, newPasswordLabel, newPasswordField, buttonBox);
        popupVBox.setStyle("-fx-padding: 20; -fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 300, 200);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Change Password");
        popupStage.show();
    }

    private void navigateToFavoritesPage() {
        try {
            new FavoritesPage(currentUser, stage, Sidebar.SidebarState.FAVOURITES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void navigateToAboutUs() {
        try {
            new Developer_page(currentUser,stage, Sidebar.SidebarState.ABOUT_US);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}