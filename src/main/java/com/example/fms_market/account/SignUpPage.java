package com.example.fms_market.account;

import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.subscription_page;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import org.apache.poi.ss.formula.functions.T;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SignUpPage {
    private final Pane contentPane;
    private File profileImage;
    private final VBox profileImagePanel = new VBox();
    private String user_photo_path;
    private String username;
    private final Label signup;
    private final HBox loginLabel;
    private final Button signUpButton;


    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int stageWidth = (int) screenSize.getWidth();
    int stageHeight = (int) (screenSize.getHeight() / 1.1);

    private final Label passwordStrengthMessage = new Label();

    private final VBox strengthPanel = new VBox();
    private final ProgressBar strengthBar = new ProgressBar(0);

    private TextField first_name = new TextField("First Name");
    private TextField last_name = new TextField("Last Name");
    Text signup_label;
    private TextField usernsme = new TextField("User Name");
    private TextField emailField = new TextField("Email");
    private TextField phoneField = new TextField("Phone");
    private PasswordField passwordField = new PasswordField();
    private TextField ageField = new TextField("Age");

    public SignUpPage(Stage primaryStage) {
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "German");
        languageComboBox.setValue("English");
        languageComboBox.setTranslateY(20);
        languageComboBox.setTranslateX(200);
        signup = labelSignup();
        signUpButton = createButton();
        signUpButton.setOnAction(event -> handleSignUp(primaryStage));
        contentPane = new Pane(languageComboBox);
        maincontentpane();

        setupProfileImagePanel(primaryStage);
        setupStrengthPanel();
        first_name = createFirstNameField();
        last_name = createLastNameField();
        usernsme = createUserNameField();
        emailField = createEmailField();
        phoneField = createPhoneField();
        ageField = createAgeField();
        passwordField = createPasswordField();
        signup_label = new Text("Sign Up");
        signup_label.setFill(Color.WHITE);
        signup_label.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 44));
        strengthBar.setPrefWidth(200);
        strengthBar.setPrefHeight(10);

        passwordField.setOnKeyReleased(_ -> validatePasswordStrength(passwordField.getText()));
        contentPane.getChildren().addAll(
                signup_label,
                first_name,
                last_name,
                usernsme,
                profileImagePanel,
                emailField,
                phoneField,
                passwordField,
                ageField,
                strengthPanel,
                signUpButton,
                loginLabel = createLoginText(primaryStage)
        );
        passwordField.setOnKeyReleased(event -> {
            validatePasswordStrength(passwordField.getText());
            signUpButton.setDisable(calculatePasswordStrength(passwordField.getText()) < 75);
        });


        updateLayout(stageWidth, stageHeight);

        Scene scene = new Scene(contentPane, stageWidth, stageHeight);
        scene.widthProperty().addListener((observable, oldVal, newVal) -> updateLayout(newVal.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((observable, oldVal, newVal) -> updateLayout(scene.getWidth(), newVal.doubleValue()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void maincontentpane() {
        Pane rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: #1c1c1c;");
        rightPane.setPrefWidth((double) stageWidth / 2);
        rightPane.setPrefHeight(stageHeight);

        Pane insidePane = new Pane();
        insidePane.setStyle("-fx-background-color: #636363; -fx-background-radius: 20px;");
        double insidePaneWidth = (double) stageWidth / 4;
        double insidePaneHeight = stageHeight / 1.5;
        insidePane.setPrefWidth(insidePaneWidth);
        insidePane.setPrefHeight(insidePaneHeight);
        insidePane.setLayoutX((rightPane.getPrefWidth() - insidePane.getPrefWidth()) / 2);
        insidePane.setLayoutY((rightPane.getPrefHeight() - insidePane.getPrefHeight()) / 2);
        rightPane.getChildren().add(insidePane);

        Pane leftPane = new Pane();
        leftPane.setStyle("-fx-background-color: #3e1a47;");
        leftPane.setPrefWidth((double) stageWidth / 2);
        leftPane.setPrefHeight(stageHeight);
        Pane leftInsidePane = new Pane();
        leftInsidePane.setStyle("-fx-background-color: #a67c9d;" +
                "-fx-background-radius: 20px;");

        double leftInsidePaneWidth = (double) stageWidth / 4;
        double leftInsidePaneHeight = stageHeight / 1.5;
        leftInsidePane.setPrefWidth(leftInsidePaneWidth);
        leftInsidePane.setPrefHeight(leftInsidePaneHeight);

        leftInsidePane.setLayoutX((rightPane.getPrefWidth() - leftInsidePane.getPrefWidth()) / 2);
        leftInsidePane.setLayoutY((rightPane.getPrefHeight() - leftInsidePane.getPrefHeight()) / 2);

        Label titleLabel = new Label("WATCH IT");
        titleLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 36));
        titleLabel.setPadding(new Insets(0, 0, 35, 0));
        titleLabel.setStyle("-fx-text-fill: white;");

        Image cameraIcon = new Image("Acount/bxs_camera-movie.png");
        ImageView cameraIconView = new ImageView(cameraIcon);
        cameraIconView.setFitWidth(30);
        cameraIconView.setFitHeight(30);
        Text watchText = new Text("Watch Movies anytime");
        watchText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 16));
        watchText.setStyle("-fx-fill: white;");
        HBox movie = new HBox(10);
        movie.getChildren().addAll(cameraIconView, watchText);

        Image searchIcon = new Image("Acount/majesticons_search-line.png");
        ImageView searchIconView = new ImageView(searchIcon);
        searchIconView.setFitWidth(30);
        searchIconView.setFitHeight(30);
        Text searchText = new Text("Search for any movie you want");
        searchText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 16));
        searchText.setStyle("-fx-fill: white;");
        HBox search = new HBox(10);
        search.getChildren().addAll(searchIconView, searchText);

        Image listIconImage = new Image("Acount/jam_task-list.png");
        ImageView listIconView = new ImageView(listIconImage);
        listIconView.setFitWidth(30);
        listIconView.setFitHeight(30);
        Text watchedText = new Text("List your watched Movies");
        watchedText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 16));
        watchedText.setStyle("-fx-fill: white;");
        HBox list = new HBox(10);
        list.getChildren().addAll(listIconView, watchedText);

        Image ratedIconImage = new Image("Acount/tabler_star-filled.png");
        ImageView ratedIconView = new ImageView(ratedIconImage);
        ratedIconView.setFitWidth(30);
        ratedIconView.setFitHeight(30);
        Text ratedText = new Text("Show the most rated movies");
        ratedText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 16));
        ratedText.setStyle("-fx-fill: white;");
        HBox rated = new HBox(10);
        rated.getChildren().addAll(ratedIconView, ratedText);

        Image favoriteIconImage = new Image("Acount/solar_heart-bold.png");
        ImageView favoriteIconView = new ImageView(favoriteIconImage);
        favoriteIconView.setFitWidth(30);
        favoriteIconView.setFitHeight(30);
        Text favoriteText = new Text("Add Shows to your Favourite List");
        favoriteText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(), 16));
        favoriteText.setStyle("-fx-fill: white;");
        HBox favourite = new HBox(10);
        favourite.getChildren().addAll(favoriteIconView, favoriteText);

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        VBox itemsBox = new VBox(40, movie, search, list, rated, favourite);
        itemsBox.setAlignment(Pos.TOP_LEFT);

        layout.getChildren().addAll(titleBox, itemsBox);
        layout.setPrefWidth(stageWidth * 0.22);
        layout.setPrefHeight(stageHeight);

        layout.setLayoutX((leftInsidePane.getPrefWidth() - layout.getPrefWidth()) / 2);
        layout.setLayoutY((leftInsidePane.getPrefHeight() - layout.getPrefHeight()) / 2);

        leftInsidePane.getChildren().add(layout);
        leftPane.getChildren().add(leftInsidePane);

        rightPane.setLayoutX(leftPane.getPrefWidth());

        contentPane.getChildren().addAll(leftPane, rightPane);
    }

    private void updateLayout(double width, double height) {
        double centerX = width / 2.0;
        double ySpacing = 43;
        double startY = (height / 5) + 100;
        signup_label.setLayoutX(centerX + 300);
        signup_label.setLayoutY(startY - 80);

        profileImagePanel.setLayoutX(centerX + 363);
        profileImagePanel.setLayoutY(startY - 67);

        first_name.setLayoutX(centerX + 295);
        first_name.setLayoutY(startY);

        last_name.setLayoutX(centerX + 397);
        last_name.setLayoutY(startY);
        usernsme.setLayoutX(centerX + 295);
        usernsme.setLayoutY(startY + 2 * ySpacing);

        ageField.setLayoutX(centerX + 295);
        ageField.setLayoutY(startY + 3 * ySpacing);
        phoneField.setLayoutX(centerX + 295);
        phoneField.setLayoutY(startY + ySpacing);
        emailField.setLayoutX(centerX + 295);
        emailField.setLayoutY(startY + 4 * ySpacing);
        passwordField.setLayoutX(centerX + 295);
        passwordField.setLayoutY(startY + 5 * ySpacing);
        strengthPanel.setLayoutX(centerX+295);
        strengthPanel.setLayoutY(startY + 6 * ySpacing);
        signUpButton.setLayoutX(centerX + 310);
        signUpButton.setLayoutY(startY + 7 * ySpacing);
        loginLabel.setLayoutX(centerX + 295);
        loginLabel.setLayoutY(startY + 8 * ySpacing);
    }

    private void updateLayoutAndRecalculate() {
        updateLayout(contentPane.getWidth(), contentPane.getHeight());
        contentPane.requestLayout();
    }

    private HBox createLoginText(Stage stage) {
        Text loginText = new Text("Alraedy have an account?");
        loginText.setFill(Color.WHITE);
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setStyle("-fx-font-weight: bold; -fx-text-fill: #1425BB;");
        loginLink.setOnMouseClicked(_ -> new LoginPageFX(stage));
        HBox loginBox = new HBox(5);
        loginBox.getChildren().addAll(loginText, loginLink);
        loginBox.setAlignment(Pos.CENTER);

        return loginBox;
    }

    private Label labelSignup() {
        Label signupTitle = new Label("Sign Up");
        signupTitle.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Lato-Bold.ttf")).toString(), 40));
        signupTitle.setStyle("-fx-text-fill: white;");
        return signupTitle;
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\+?\\d{1,3}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private boolean isValidAge(String ageStr) {
        try {
            int age = Integer.parseInt(ageStr);
            return age >= 18 && age <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void handleSignUp(Stage primaryStage) {
        String email = emailField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        String phone = phoneField.getText().trim();
        String age = ageField.getText().trim();
        String firstName = first_name.getText().trim();
        String lastName = last_name.getText().trim();
        String userName = usernsme.getText().trim();

        if (email.isEmpty() || password.isEmpty() || phone.isEmpty() || age.isEmpty()|| firstName.isEmpty() || lastName.isEmpty() || userName.isEmpty()) {
            if (email.isEmpty()) {
                playShakeTransition(emailField);
            }
            if (password.isEmpty()) {
                playShakeTransition(passwordField);
            }
            if (phone.isEmpty()) {
                playShakeTransition(phoneField);
            }
            if (age.isEmpty()) {
                playShakeTransition(ageField);
            }
            if (firstName.isEmpty()) {
                playShakeTransition(first_name);
            }
            if (lastName.isEmpty()) {
                playShakeTransition(last_name);
            }
            if (userName.isEmpty()) {
                playShakeTransition(usernsme);
            }
            showAlert(Alert.AlertType.ERROR, "Error! All fields are required!");
            return;
        }
        if (!isValidEmail(email)) {
            playShakeTransition(emailField);
            showAlert(Alert.AlertType.ERROR, "Invalid email ! Please enter a valid email address.");
            return;
        }
        if (!isValidPhoneNumber(phone)) {
            playShakeTransition(phoneField);
            showAlert(Alert.AlertType.ERROR, "Invalid phone ! Please enter a valid phone.");
            return;
        }
        if (!isValidAge(age)) {
            playShakeTransition(ageField);
            showAlert(Alert.AlertType.ERROR, "Invalid age ! Please enter a valid age.");
            return;
        }

        if (isValidPassword()) {
            try {
                if (UserJsonHandler.emailExists(email)) {
                    playShakeTransition(emailField);
                    showAlert(Alert.AlertType.ERROR, "Email already exists! Please use a different email.");
                    return;
                }

                if (profileImage != null) {
                    user_photo_path = profileImage.getAbsolutePath();
                }

                UserJsonHandler.saveUser(new User( username,email, password, "customer",phone, age, user_photo_path));
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful!");
                new subscription_page(primaryStage);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error saving user: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Password does not meet the criteria!");
            playShakeTransition(passwordField);
        }
    }

    private TextField createFirstNameField() {
        TextField field = new TextField();
        field.setPromptText("First Name");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyNameStyle(field);
        return field;
    }

    private TextField createLastNameField() {
        TextField field = new TextField();
        field.setPromptText("Last Name");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyNameStyle(field);
        return field;
    }

    private void applyNameStyle(TextField field) {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setRadius(8);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.25));
        field.setEffect(shadow);
        field.setStyle("-fx-font-size: 12px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 3px;" +
                "-fx-background-color: #FFFFFF;");
        field.setPrefWidth(95);
        field.setPrefHeight(35);

    }

    private TextField createUserNameField() {
        TextField field = new TextField();
        field.setPromptText("User Name");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyTextFieldStyle(field);
        return field;
    }

    private PasswordField createPasswordField() {
        PasswordField field = new PasswordField();
        field.setPromptText("Password");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyTextFieldStyle(field);
        return field;
    }

    private TextField createEmailField() {
        TextField field = new TextField();
        field.setPromptText("Email");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyTextFieldStyle(field);
        return field;
    }

    private TextField createAgeField() {
        TextField field = new TextField();
        field.setPromptText("Age");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyTextFieldStyle(field);
        return field;
    }

    private TextField createPhoneField() {
        TextField field = new TextField();
        field.setPromptText("Phone");
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        applyTextFieldStyle(field);
        return field;
    }
    private void applyTextFieldStyle(TextField field) {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setRadius(8);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.25));
        field.setEffect(shadow);
        field.setStyle("-fx-font-size: 12px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 3px;" +
                "-fx-background-color: #FFFFFF;");
        field.setPrefWidth(200);
        field.setPrefHeight(35);
    }

    private Button createButton() {
        Button button = new Button("Sign Up");
        String colorHex = colorToHex();
        button.setPrefWidth(170);
        button.setPrefHeight(45);
        applyButtonStyle(button, colorHex);
        return button;
    }

    private void applyButtonStyle(Button button, String colorHex) {
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setRadius(8);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.25));
        button.setEffect(shadow);
        button.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", colorHex));

        button.setOnMouseEntered(_ ->
                button.setStyle(String.format(
                        "-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", darkenColor())));

        button.setOnMouseExited(_ ->
                button.setStyle(String.format(
                        "-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", colorHex)));
    }

    private String colorToHex() {
        return String.format("#%02X%02X%02X",
                (int) (Color.PURPLE.getRed() * 255),
                (int) (Color.PURPLE.getGreen() * 255),
                (int) (Color.PURPLE.getBlue() * 255));
    }

    private String darkenColor() {
        double factor = 0.8;
        return String.format("#%02X%02X%02X",
                (int) (Color.PURPLE.getRed() * 255 * factor),
                (int) (Color.PURPLE.getGreen() * 255 * factor),
                (int) (Color.PURPLE.getBlue() * 255 * factor));
    }

    private void setupProfileImagePanel(Stage primaryStage) {
        double size = 60;

        profileImagePanel.setPrefSize(size, size);
        profileImagePanel.setStyle(
                "-fx-background-color: #636363;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-radius: 30;"
        );

        StackPane stackPane = new StackPane();
        Label cameraEmoji = new Label("📷");
        cameraEmoji.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        stackPane.getChildren().add(cameraEmoji);
        StackPane.setAlignment(cameraEmoji, Pos.CENTER);
        cameraEmoji.setTranslateY(15);

        profileImagePanel.setOnMouseClicked(_ -> selectProfileImage(primaryStage));
        profileImagePanel.getChildren().add(stackPane);
    }
    private void selectProfileImage(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File profileImage = fileChooser.showOpenDialog(primaryStage);

        profileImagePanel.getChildren().clear();

        if (profileImage != null) {
            Image img = new Image(profileImage.toURI().toString());
            ImageView imageView = new ImageView(img);

            double frameWidth = profileImagePanel.getWidth();
            double frameHeight = profileImagePanel.getHeight();
            double imgWidth = img.getWidth();
            double imgHeight = img.getHeight();
            double scaleFactor = Math.max(frameWidth / imgWidth, frameHeight / imgHeight);

            imageView.setFitWidth(imgWidth * scaleFactor);
            imageView.setFitHeight(imgHeight * scaleFactor);

            imageView.setPreserveRatio(true);

            Circle clip = new Circle(frameWidth / 2);
            clip.setCenterX(frameWidth / 2);
            clip.setCenterY(frameHeight / 2);
            imageView.setClip(clip);

            StackPane centeredPane = new StackPane(imageView);
            profileImagePanel.getChildren().add(centeredPane);

            profileImagePanel.setStyle("-fx-background-color: transparent;");
        } else {
            profileImagePanel.setStyle("-fx-background-color: #636363;" +
                    "-fx-border-color: white;" +
                    "-fx-border-width: 2;" +
                    "-fx-background-radius: 30;" +
                    "-fx-border-radius: 30;" );

            Label cameraEmoji = new Label("📷");
            cameraEmoji.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(cameraEmoji);
            stackPane.setPrefSize(profileImagePanel.getWidth(), profileImagePanel.getHeight());

            Circle clip = new Circle(profileImagePanel.getWidth() / 2);
            clip.setCenterX(profileImagePanel.getWidth() / 2);
            clip.setCenterY(profileImagePanel.getHeight() / 2);
            stackPane.setClip(clip);

            profileImagePanel.getChildren().add(stackPane);
        }
    }


    private void validatePasswordStrength(String password) {
        int score = calculatePasswordStrength(password);
        updateStrengthBar(score);
        if (!password.isEmpty()) {
            updatePasswordStrengthMessage(score);
        } else {
            passwordStrengthMessage.setText("");
        }
        updateLayoutAndRecalculate();
    }

    private void setupStrengthPanel() {
        strengthPanel.setSpacing(5);
        strengthPanel.setStyle("-fx-background-color: #636363;");
        strengthBar.setPrefWidth(150);
        strengthBar.setPrefHeight(8);
        strengthPanel.getChildren().add(strengthBar);
        passwordStrengthMessage.setWrapText(true);
        strengthPanel.getChildren().add(passwordStrengthMessage);
        passwordField.setOnKeyReleased(event -> validatePasswordStrength(passwordField.getText()));
    }

    private int calculatePasswordStrength(String password) {
        return (Pattern.compile("[0-9]").matcher(password).find() ? 25 : 0) +
                (Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find() ? 25 : 0) +
                (Pattern.compile("[A-Z]").matcher(password).find() ? 25 : 0) +
                (Pattern.compile("[a-z]").matcher(password).find() ? 25 : 0);
    }

    private void updateStrengthBar(int score) {
        strengthBar.setProgress(score / 100.0);
    }
    private void updatePasswordStrengthMessage(int score) {
        if (score > 0 && score <= 30) {
            setPasswordStrengthMessage("Too weak! Add digits, special chars, upper/lowercase.", "#9C0000");
        } else if (score > 30 && score < 70) {
            setPasswordStrengthMessage("Weak. Add variety for strength.", "#FF7F00");
        } else if (score >= 70 && score < 80) {
            setPasswordStrengthMessage("Okay. Add more for a strong password.", "#FFEB3B");
        } else if (score >= 80) {
            setPasswordStrengthMessage("Strong password.", "#4CAF50");
        }
    }

    private void setPasswordStrengthMessage(String message, String color) {
        passwordStrengthMessage.setText(message);
        passwordStrengthMessage.setTextFill(Color.web(color));
        passwordStrengthMessage.setStyle("-fx-font-size: 11px;");
    }

    private boolean isValidPassword() {
        return calculatePasswordStrength(passwordField.getText()) >= 70;
    }
    private void playShakeTransition(Node node) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), node);
        transition.setFromX(0);
        transition.setByX(10);
        transition.setCycleCount(6);
        transition.setAutoReverse(true);
        transition.play();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.getDialogPane().getStyleClass().add("alert");
        alert.getDialogPane().setStyle("-fx-background-color: #1c1c1c;");
        alert.getDialogPane().setHeaderText(null);
        alert.getDialogPane().setStyle("-fx-background-color: #fee2fe; -fx-text-fill: white;");
        alert.getDialogPane().setContent(new VBox(10, new Text(message)));
        Button alertButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        alertButton.setStyle("-fx-background-color: #6a0dad; -fx-text-fill: white; -fx-font-weight: bold; -fx-alignment: center;");
        alertButton.setOnMouseEntered(event -> alertButton.setStyle("-fx-background-color: #6a006a; -fx-text-fill: white;"));
        alertButton.setOnMouseExited(event -> alertButton.setStyle("-fx-background-color: #b300b3; -fx-text-fill: white;"));
        StackPane.setAlignment(alertButton, Pos.CENTER);
        alert.showAndWait();
    }
}