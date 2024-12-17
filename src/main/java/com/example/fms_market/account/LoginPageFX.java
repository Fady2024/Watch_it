package com.example.fms_market.account;

import com.example.fms_market.pages.HomePage;
import com.example.fms_market.util.TopPanel;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginPageFX {
    private final Pane contentPane;
    private final Text messageText = createErrorText();
    private final Label login;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Text emailErrorText;
    private final Text passwordErrorText;
    private final HBox signUpText;
    private final VBox loginForm;
    private final VBox usernameBox;
    private StackPane passwordStack;
    private final VBox passwordBox;

    public LoginPageFX(Stage stage) {
        // Initialize passwordStack
        passwordStack = new StackPane();

        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "German");
        languageComboBox.setValue(LanguageManager.getLanguageBasedString("German", "English"));
        languageComboBox.setTranslateY(20);
        languageComboBox.setTranslateX(200);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double stageWidth = screenSize.getWidth();
        double stageHeight = screenSize.getHeight() / 1.1;

        TopPanel dayNightSwitch = new TopPanel();

        signUpText = createSignUpText(stage);

        login = labelLogin();
        usernameField = createTextField();
        passwordField = createPasswordField();
        loginButton = createButton();
        emailErrorText = createErrorText();
        passwordErrorText = createErrorText();

        loginButton.setOnAction(_ -> handleLogin(stage));

        contentPane = new Pane(languageComboBox);

        Pane leftPane = new Pane();
        leftPane.setStyle("-fx-background-color: black;");
        leftPane.setPrefWidth(stageWidth / 2);
        leftPane.setPrefHeight(stageHeight);

        Pane insidePane = new Pane();
        insidePane.setStyle("-fx-background-color: #636363;" +
                "-fx-background-radius: 20px;");
        double insidePaneWidth = stageWidth / 4;
        double insidePaneHeight = stageHeight / 1.5;

        insidePane.setPrefWidth(insidePaneWidth);
        insidePane.setPrefHeight(insidePaneHeight);
        insidePane.setLayoutX((leftPane.getPrefWidth() - insidePane.getPrefWidth()) / 2);
        insidePane.setLayoutY((leftPane.getPrefHeight() - insidePane.getPrefHeight()) / 2);

        leftPane.getChildren().add(insidePane);

        Pane rightPane = new Pane();
        rightPane.setStyle("-fx-background-color: #3e1a47;");
        rightPane.setPrefWidth(stageWidth / 2);
        rightPane.setPrefHeight(stageHeight);

        Pane rightInsidePane = new Pane();
        rightInsidePane.setStyle("-fx-background-color: #a67c9d;" +
                "-fx-background-radius: 20px;");

        double rightInsidePaneWidth = stageWidth / 4;
        double rightInsidePaneHeight = stageHeight / 1.5;

        rightInsidePane.setPrefWidth(rightInsidePaneWidth);
        rightInsidePane.setPrefHeight(rightInsidePaneHeight);

        rightInsidePane.setLayoutX((rightPane.getPrefWidth() - rightInsidePane.getPrefWidth()) / 2);
        rightInsidePane.setLayoutY((rightPane.getPrefHeight() - rightInsidePane.getPrefHeight()) / 2);

        Label titleLabel = new Label("WATCH IT");
        titleLabel.setFont(new Font(36));
        titleLabel.setPadding(new Insets(0, 0, 35, 0));
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        Image cameraIcon = new Image("Acount/camera.png");
        ImageView cameraIconView = new ImageView(cameraIcon);
        cameraIconView.setFitWidth(30);
        cameraIconView.setFitHeight(30);
        Text watchText = new Text("Watch Movies anytime");
        watchText.setFont(new Font(20));
        watchText.setStyle("-fx-fill: white;");
        HBox movie = new HBox(10);
        movie.getChildren().addAll(cameraIconView, watchText);

        Image searchIcon = new Image("Acount/search.png");
        ImageView searchIconView = new ImageView(searchIcon);
        searchIconView.setFitWidth(30);
        searchIconView.setFitHeight(30);
        Text searchText = new Text("Search for any movie you want");
        searchText.setFont(new Font(20));
        searchText.setStyle("-fx-fill: white;");
        HBox search = new HBox(10);
        search.getChildren().addAll(searchIconView, searchText);

        Image listIconImage = new Image("Acount/list.png");
        ImageView listIconView = new ImageView(listIconImage);
        listIconView.setFitWidth(28);
        listIconView.setFitHeight(33);
        Text watchedText = new Text("List your watched Movies");
        watchedText.setFont(new Font(20));
        watchedText.setStyle("-fx-fill: white;");
        HBox list = new HBox(10);
        list.getChildren().addAll(listIconView, watchedText);

        Image ratedIconImage = new Image("Acount/star.png");
        ImageView ratedIconView = new ImageView(ratedIconImage);
        ratedIconView.setFitWidth(30);
        ratedIconView.setFitHeight(30);
        Text ratedText = new Text("Show the most rated movies");
        ratedText.setFont(new Font(20));
        ratedText.setStyle("-fx-fill: white;");
        HBox rated = new HBox(10);
        rated.getChildren().addAll(ratedIconView, ratedText);

        Image favoriteIconImage = new Image("Acount/heart.png");
        ImageView favoriteIconView = new ImageView(favoriteIconImage);
        favoriteIconView.setFitWidth(30);
        favoriteIconView.setFitHeight(30);
        Text favoriteText = new Text("Add Shows to your Favourite List");
        favoriteText.setFont(new Font(20));
        favoriteText.setStyle("-fx-fill: white;");
        HBox favourite = new HBox(10);
        favourite.getChildren().addAll(favoriteIconView, favoriteText);

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));

        VBox titleBox = new VBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);

        VBox itemsBox = new VBox(15, movie, search, list, rated, favourite);
        itemsBox.setAlignment(Pos.TOP_LEFT);

        layout.getChildren().addAll(titleBox, itemsBox);
        layout.setPrefWidth(stageWidth * 0.22);
        layout.setPrefHeight(stageHeight);

        layout.setLayoutX((rightInsidePane.getPrefWidth() - layout.getPrefWidth()) / 2);
        layout.setLayoutY((rightInsidePane.getPrefHeight() - layout.getPrefHeight()) / 2);

        rightInsidePane.getChildren().add(layout);
        rightPane.getChildren().add(rightInsidePane);

        rightPane.setLayoutX(leftPane.getPrefWidth());

        contentPane.getChildren().addAll(leftPane, rightPane);

        loginForm = new VBox(10);
        loginForm.setAlignment(Pos.TOP_CENTER);
        loginForm.setLayoutX(260);
        loginForm.setLayoutY(200);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        usernameBox = new VBox(0, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        passwordBox = new VBox();
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
        ImageView eyeIcon = createEyeIcon();

        passwordStack = new StackPane();
        passwordStack.getChildren().addAll(passwordField, eyeIcon);
        StackPane.setAlignment(eyeIcon, Pos.CENTER_RIGHT);
        StackPane.setMargin(eyeIcon, new Insets(0, 10, 0, 0));

        passwordBox.getChildren().clear();
        passwordBox.getChildren().addAll(passwordLabel, passwordStack);
        loginForm.getChildren().addAll(login, usernameBox, emailErrorText, passwordBox, passwordErrorText, messageText, loginButton, signUpText);
        contentPane.getChildren().add(loginForm);

        updateLayout(stageWidth, stageHeight, true,true);
        Scene scene = new Scene(contentPane, stageWidth, stageHeight);

        scene.widthProperty().addListener((_, _, newWidth) -> updateLayout(newWidth.doubleValue(), scene.getHeight(), true,true));
        scene.heightProperty().addListener((_, _, newHeight) -> updateLayout(scene.getWidth(), newHeight.doubleValue(), true,true));
        stage.setScene(scene);
        stage.show();

        dayNightSwitch.addActionListener(this::updateColors);
        updateColors();
    }


    private void updateLayout(double width, double height, boolean EmailIsValid, boolean PasswordIsValid) {
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        login.setLayoutX(centerX - 60);
        login.setLayoutY(centerY - 200);

        usernameBox.setLayoutX(centerX - 100);
        usernameBox.setLayoutY(centerY - 100);
        emailErrorText.setLayoutX(centerX - 100);
        emailErrorText.setLayoutY(centerY - 45);
        passwordErrorText.setLayoutX(centerX - 100);
        passwordErrorText.setLayoutY(centerY - 20);
        messageText.setLayoutX(centerX - 100);
        messageText.setLayoutY(centerY + 80);

        if (!EmailIsValid) {
            emailErrorText.setVisible(true);
            messageText.setVisible(false);
            passwordBox.setTranslateY(-8);
        } else {
            emailErrorText.setVisible(false);
            passwordBox.setTranslateY(-25);
        }

        if (!PasswordIsValid) {
            passwordErrorText.setVisible(true);
            messageText.setVisible(false);
            passwordErrorText.setTranslateY(EmailIsValid?-24:-12);
            loginButton.setTranslateY(EmailIsValid?-52:-42);
            signUpText.setTranslateY(EmailIsValid?-52:-42);
            messageText.setTranslateY(EmailIsValid?-58:-48);
        } else {
            passwordErrorText.setVisible(false);
            loginButton.setTranslateY(-50);
            signUpText.setTranslateY(-50);
            messageText.setTranslateY(-54);
        }

        loginButton.setLayoutX(centerX - 40);
        loginButton.setLayoutY(centerY + 50);

        signUpText.setLayoutX(centerX - 130);
        signUpText.setLayoutY(centerY + 70);

        contentPane.requestLayout();
    }

    private void handleLogin(Stage stage) {
        String email = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        boolean EmailIsValid = true;
        boolean PasswordIsValid = true;

        emailErrorText.setText("");
        passwordErrorText.setText("");
        messageText.setText("");

        if (email.isEmpty()) {
            emailErrorText.setText("User Name cannot be empty.");
            EmailIsValid = false;
        }

        if (password.isEmpty()) {
            passwordErrorText.setText("Password cannot be empty.");
            PasswordIsValid = false;
        }

        if (EmailIsValid && PasswordIsValid) {
            emailErrorText.setVisible(false);
            passwordErrorText.setVisible(false);

            try {
                User user = UserJsonHandler.getUserByEmailAndPassword(email, password);
                if (user != null) {
                    new HomePage(user, stage);
                } else {
                    messageText.setText("Invalid credentials!");
                    messageText.setVisible(true);
                    playShakeTransition(messageText);
                }
            } catch (IOException ex) {
                messageText.setText(STR."Error reading user data: \{ex.getMessage()}");
                playShakeTransition(loginForm);
            }
        } else {
            emailErrorText.setVisible(true);
            passwordErrorText.setVisible(true);
            playShakeTransition(loginForm);
        }

        updateLayout(stage.getWidth(), stage.getHeight(), EmailIsValid, PasswordIsValid);
        contentPane.requestLayout();
    }

    private void playShakeTransition(Node node) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(100), node);
        transition.setFromX(0);
        transition.setByX(10);
        transition.setCycleCount(6);
        transition.setAutoReverse(true);
        transition.play();
    }


    private HBox createSignUpText(Stage stage) {
        Label noAccountLabel = new Label("You Don't Have an Account?");
        noAccountLabel.setTextFill(Color.WHITE);

        Hyperlink signUpLink = new Hyperlink("Sign up");
        signUpLink.setTextFill(Color.DEEPSKYBLUE);
        signUpLink.setStyle("-fx-font-weight: bold;");
        signUpLink.setOnMouseClicked(_ -> new SignUpPage(stage));

        HBox signUpBox = new HBox(5);
        signUpBox.getChildren().addAll(noAccountLabel, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);

        return signUpBox;
    }

    private Label labelLogin() {
        Label loginTitle = new Label("Login");
        loginTitle.setStyle("-fx-font-size: 35px; -fx-font-weight: bold;");
        loginTitle.setTextFill(Color.WHITE);
        return loginTitle;
    }

    private TextField createTextField() {
        TextField field = new TextField();
        field.setPromptText("Username");
        field.setPrefWidth(250);
        field.setPrefHeight(40);
        field.setStyle("-fx-font-size: 16px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 5px;" +
                "-fx-background-color: #FFFFFF;");
        return field;
    }

    private PasswordField createPasswordField() {
        PasswordField field = new PasswordField();
        field.setPromptText("Password");
        field.setPrefWidth(250);
        field.setPrefHeight(40);
        field.setStyle("-fx-font-size: 16px;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 5px;" +
                "-fx-background-color: #FFFFFF;");
        return field;
    }

    private ImageView createEyeIcon() {
        Image eyeOpenImage = new Image("file:src/main/resources/Acount/visibility.png");
        Image eyeClosedImage = new Image("file:src/main/resources/Acount/close-eye.png");
        ImageView eyeIcon = new ImageView(eyeClosedImage);
        eyeIcon.setFitWidth(30);
        eyeIcon.setFitHeight(30);
        eyeIcon.setPickOnBounds(true);
        eyeIcon.setCursor(Cursor.HAND);

        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(2);
        shadow.setRadius(8);
        shadow.setColor(Color.rgb(0, 0, 0, 0.8));
        eyeIcon.setEffect(shadow);

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPrefWidth(passwordField.getPrefWidth());
        visiblePasswordField.setPrefHeight(passwordField.getPrefHeight());
        visiblePasswordField.setStyle(passwordField.getStyle());
        visiblePasswordField.setPromptText(passwordField.getPromptText());

        eyeIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, _ -> {
            if (passwordField.isVisible()) {
                visiblePasswordField.setText(passwordField.getText());
                passwordStack.getChildren().set(0, visiblePasswordField);
                passwordField.setVisible(false);
                eyeIcon.setImage(eyeOpenImage);
            } else {
                passwordField.setText(visiblePasswordField.getText());
                passwordStack.getChildren().set(0, passwordField);
                passwordField.setVisible(true);
                eyeIcon.setImage(eyeClosedImage);
            }
        });

        return eyeIcon;
    }


    private Button createButton() {
        Button button = new Button("Login");
        String colorHex = colorToHex();

        button.setPrefWidth(170);
        button.setPrefHeight(45);
        button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", colorHex));

        button.setOnMouseEntered(_ ->
                button.setStyle(String.format(
                        "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", darkenColor())));

        button.setOnMouseExited(_ ->
                button.setStyle(String.format(
                        "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; " +
                                "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", colorHex)));

        return button;
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



    private Text createErrorText() {
        Text errorText = new Text();
        errorText.setStyle("-fx-font-size: 14px; -fx-fill: red; -fx-effect: dropshadow(gaussian, rgba(128,128,128,1), 4, 0.5, 0, 0);");
        return errorText;
    }

    private void updateColors() {
        if (TopPanel.isDayValue()) {
            contentPane.setStyle("-fx-background-color: white;");
            login.setTextFill(Color.BLACK);
        } else {
            contentPane.setStyle("-fx-background-color: #333;");
            login.setTextFill(Color.WHITE);
        }
    }
}

