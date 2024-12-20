package com.example.fms_market.pages;

import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.*;
import javafx.util.Duration;
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
            public void onAboutUsSelected(){navigateToAboutUs();}
        });

        // Display user details directly
        displayUserDetails();
    }

    private void displayUserDetails() {
        VBox mainBox = new VBox(10);
        mainBox.setStyle("-fx-padding: 20px; -fx-background-color: #1c1c1c; -fx-alignment: center;");

        Label userDetailsLabel = LanguageManager.TcreateLanguageitle("Benutzerdetails", "User Details", "24", "white");
        userDetailsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-padding-bottom: 30px;");

        VBox userDetailsBox = new VBox(10);
        userDetailsBox.setStyle("-fx-padding: 20px; -fx-background-color: #1c1c1c; -fx-alignment: center;");

        Label photoLabel = LanguageManager.TcreateLanguageitle("Benutzerfoto:", "User Photo:", "16", "white");
        ImageView userPhoto = new ImageView(new Image(STR."file:\{currentUser.getUser_photo_path()}"));
        userPhoto.setFitHeight(100);
        userPhoto.setFitWidth(100);
        Circle clip = new Circle(50, 50, 50);
        userPhoto.setClip(clip);

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

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        TextField usernameField = new TextField(currentUser.getUsername());
        usernameField.setMaxWidth(300);
        usernameField.setStyle("-fx-alignment: center;");

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");
        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setMaxWidth(300);
        emailField.setStyle("-fx-alignment: center;");

        Label phoneLabel = LanguageManager.TcreateLanguageitle("Telefon:", "Phone:", "16", "white");
        TextField phoneField = new TextField(currentUser.getPhone());
        phoneField.setMaxWidth(300);
        phoneField.setStyle("-fx-alignment: center;");

        Label ageLabel = LanguageManager.TcreateLanguageitle("Alter:", "Age:", "16", "white");
        TextField ageField = new TextField(currentUser.getAge());
        ageField.setMaxWidth(300);
        ageField.setStyle("-fx-alignment: center;");

        AtomicReference<String> initialEmail = new AtomicReference<>(currentUser.getEmail());
        AtomicReference<String> initialPhone = new AtomicReference<>(currentUser.getPhone());
        AtomicReference<String> initialAge = new AtomicReference<>(currentUser.getAge());

        Label changePasswordLabel = LanguageManager.TcreateLanguageitle("Kennwort ändern", "Change Password", "16", "#8969ba");
        changePasswordLabel.setStyle("-fx-text-fill: #8969ba; -fx-cursor: hand;");
        changePasswordLabel.setOnMouseClicked(_ -> showChangePasswordPopup());

        Button applyButton = LanguageManager.createLanguageButton("Anwenden", "Apply", "16", "white");
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
                initialEmail.set(currentUser.getEmail());
                initialPhone.set(currentUser.getPhone());
                initialAge.set(currentUser.getAge());
                initialPhotoPath.set(currentUser.getUser_photo_path());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = LanguageManager.createLanguageButton("Stornieren", "Cancel", "16", "black");
        cancelButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 10;");
        cancelButton.setOnAction(_ -> {
            emailField.setText(initialEmail.get());
            phoneField.setText(initialPhone.get());
            ageField.setText(initialAge.get());
            userPhoto.setImage(new Image(STR."file:\{initialPhotoPath.get()}"));
            currentUser.setUser_photo_path(initialPhotoPath.get());
            selectedFileRef.set(null);
        });

        Button languageButton = new Button("🌐");
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

        // Create the PauseTransition
        PauseTransition hideTransition = new PauseTransition(Duration.seconds(2));
        hideTransition.setOnFinished(_ -> popup.hide());

        // Add mouse event handlers to reset the transition if the mouse hovers over the popup
        menuBox.setOnMouseEntered(_ -> hideTransition.stop());
        menuBox.setOnMouseExited(_ -> hideTransition.play());

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
                hideTransition.playFromStart(); // Start the hide transition
            }
        }));

        englishLabel.setOnMouseClicked(_ -> {
            if (!"English".equals(LanguageManager.getInstance().getLanguage())) {
                LanguageManager.getInstance().toggleLanguage();
            }
            popup.hide();
        });

        germanLabel.setOnMouseClicked(_ -> {
            if (!"German".equals(LanguageManager.getInstance().getLanguage())) {
                LanguageManager.getInstance().toggleLanguage();
            }
            popup.hide();
        });

        languageButton.setTranslateY(-620);
        languageButton.setTranslateX(640);

        HBox buttonBox = new HBox(10, cancelButton, applyButton);
        buttonBox.setStyle("-fx-alignment: center;");
        userDetailsBox.getChildren().addAll(photoLabel, userPhoto,usernameLabel, usernameField, emailLabel, emailField, phoneLabel, phoneField, ageLabel, ageField, changePasswordLabel, buttonBox);

        mainBox.getChildren().addAll(userDetailsLabel, userDetailsBox, languageButton);
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
        popupVBox.setStyle("-fx-padding: 20px; -fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

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