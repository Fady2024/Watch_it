package com.example.fms_market.account;

import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.model.Subscription;
import com.example.fms_market.pages.HomePage;
import com.example.fms_market.pages.subscription_page;
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
import javafx.stage.Modality;
import javafx.stage.StageStyle;
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
import java.util.Objects;

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


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double stageWidth = screenSize.getWidth();
        double stageHeight = screenSize.getHeight() / 1.1;


        signUpText = createSignUpText(stage);

        login = labelLogin();
        usernameField = createTextField();
        passwordField = createPasswordField();
        loginButton = createButton();
        emailErrorText = createErrorText();
        passwordErrorText = createErrorText();

        loginButton.setOnAction(_ -> handleLogin(stage));

        contentPane = new Pane();

        Pane leftPane = new Pane();
        leftPane.setStyle("-fx-background-color: #1c1c1c;");
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
        titleLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),36));
        titleLabel.setPadding(new Insets(0, 0, 35, 0));
        titleLabel.setStyle("-fx-text-fill: white;");

        Image cameraIcon = new Image("Acount/bxs_camera-movie.png");
        ImageView cameraIconView = new ImageView(cameraIcon);
        cameraIconView.setFitWidth(30);
        cameraIconView.setFitHeight(30);
        Text watchText = new Text(LanguageManager.getLanguageBasedString("Schauen Sie sich jederzeit Filme an","Watch Movies anytime"));
        watchText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),16));
        watchText.setStyle("-fx-fill: white;");
        HBox movie = new HBox(10);
        movie.getChildren().addAll(cameraIconView, watchText);

        Image searchIcon = new Image("Acount/majesticons_search-line.png");
        ImageView searchIconView = new ImageView(searchIcon);
        searchIconView.setFitWidth(30);
        searchIconView.setFitHeight(30);
        Text searchText = new Text(LanguageManager.getLanguageBasedString("Suchen Sie nach jedem gewünschten \nFilm","Search for any movie you want"));
        searchText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),16));
        searchText.setStyle("-fx-fill: white;");
        HBox search = new HBox(10);
        search.getChildren().addAll(searchIconView, searchText);

        Image listIconImage = new Image("Acount/jam_task-list.png");
        ImageView listIconView = new ImageView(listIconImage);
        listIconView.setFitWidth(30);
        listIconView.setFitHeight(30);
        Text watchedText = new Text(LanguageManager.getLanguageBasedString("Liste deine gesehenen Filme auf","List your watched Movies"));
        watchedText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),16));
        watchedText.setStyle("-fx-fill: white;");
        HBox list = new HBox(10);
        list.getChildren().addAll(listIconView, watchedText);

        Image ratedIconImage = new Image("Acount/tabler_star-filled.png");
        ImageView ratedIconView = new ImageView(ratedIconImage);
        ratedIconView.setFitWidth(30);
        ratedIconView.setFitHeight(30);
        Text ratedText = new Text(LanguageManager.getLanguageBasedString("Die am besten bewerteten Filme \nanzeigen","Show the most rated movies"));
        ratedText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),16));
        ratedText.setStyle("-fx-fill: white;");
        HBox rated = new HBox(10);
        rated.getChildren().addAll(ratedIconView, ratedText);

        Image favoriteIconImage = new Image("Acount/solar_heart-bold.png");
        ImageView favoriteIconView = new ImageView(favoriteIconImage);
        favoriteIconView.setFitWidth(30);
        favoriteIconView.setFitHeight(30);
        Text favoriteText = new Text(LanguageManager.getLanguageBasedString("Sendungen zu Ihrer Favoritenliste \nhinzufügen","Add Shows to your Favourite List"));
        favoriteText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Kufam-VariableFont_wght.ttf")).toString(),16));
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

        layout.setLayoutX((rightInsidePane.getPrefWidth() - layout.getPrefWidth()) / 2);
        layout.setLayoutY((rightInsidePane.getPrefHeight() - layout.getPrefHeight()) / 2);

        rightInsidePane.getChildren().add(layout);
        rightPane.getChildren().add(rightInsidePane);

        rightPane.setLayoutX(leftPane.getPrefWidth());

        contentPane.getChildren().addAll(leftPane, rightPane);

        loginForm = new VBox(15);
        loginForm.setAlignment(Pos.TOP_CENTER);
        loginForm.setLayoutX(260);
        loginForm.setLayoutY(185);

        Label usernameLabel = new Label(LanguageManager.getLanguageBasedString("Benutzername:","Username:"));
        usernameLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        usernameLabel.setStyle("-fx-text-fill: white;");

        usernameBox = new VBox(0, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        passwordBox = new VBox();
        Label passwordLabel = new Label(LanguageManager.getLanguageBasedString("Passwort:","Password:"));
        passwordLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        passwordLabel.setStyle("-fx-text-fill: white;");
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
        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        boolean UsernameIsValid = true;
        boolean PasswordIsValid = true;

        emailErrorText.setText("");
        passwordErrorText.setText("");
        messageText.setText("");

        if (username.isEmpty()) {
            emailErrorText.setText(LanguageManager.getLanguageBasedString("Der Benutzername darf nicht leer sein.","User Name cannot be empty."));
            UsernameIsValid = false;
        }

        if (password.isEmpty()) {
            passwordErrorText.setText(LanguageManager.getLanguageBasedString("Das Passwort darf nicht leer sein.","Password cannot be empty."));
            PasswordIsValid = false;
        }

        if (UsernameIsValid && PasswordIsValid) {
            emailErrorText.setVisible(false);
            passwordErrorText.setVisible(false);

            try {
                User user = UserJsonHandler.getUserByEmailAndPassword(username, password);
                if (user != null) {
                    Subscription subscription = SubscriptionManager.getSubscriptionByUserId(user.getId());
                    if (subscription != null && subscription.isExpired()) {
                        showSubscriptionExpiredPopup(stage, user);
                    } else {
                        new HomePage(user, stage);
                    }
                } else {
                    messageText.setText(LanguageManager.getLanguageBasedString("Ungültige Anmeldeinformationen!","Invalid credentials!"));
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

        updateLayout(stage.getWidth(), stage.getHeight(), UsernameIsValid, PasswordIsValid);
        contentPane.requestLayout();
    }

    private void showSubscriptionExpiredPopup(Stage stage, User user) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.initOwner(stage);

        VBox popupContent = new VBox(20);
        popupContent.setPadding(new Insets(20));
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setStyle("-fx-background-color: rgba(51, 51, 51, 0.8); -fx-background-radius: 20;"); // Rounded corners

        // Centered Label with all text visible
        Label messageLabel = new Label("Your subscription has expired. Please renew your subscription to continue watching movies.");
        messageLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        messageLabel.setAlignment(Pos.CENTER);

        VBox messageContainer = new VBox(messageLabel);
        messageContainer.setAlignment(Pos.CENTER);

        Button renewButton = new Button("Renew Subscription");
        renewButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        renewButton.setStyle("-fx-background-color: #c9068d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;");
        renewButton.setOnAction(_ -> {
            new subscription_page(stage, user);
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        cancelButton.setStyle("-fx-background-color: #c9068d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;");
        cancelButton.setOnAction(_ -> popupStage.close());

        popupContent.getChildren().addAll(messageContainer, renewButton, cancelButton);
        Scene popupScene = new Scene(popupContent, 400, 250);
        popupScene.setFill(Color.TRANSPARENT);
        popupStage.setScene(popupScene);
        popupStage.show();
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
        Label noAccountLabel = new Label(LanguageManager.getLanguageBasedString("Sie haben noch kein Konto?","You Don't Have an Account?"));
        noAccountLabel.setTextFill(Color.WHITE);

        Hyperlink signUpLink = new Hyperlink(LanguageManager.getLanguageBasedString("Melden Sie sich an","Sign up"));
        //signUpLink.setTextFill(Color.DEEPSKYBLUE);
        signUpLink.setStyle("-fx-font-weight: bold; -fx-text-fill: #1425BB;");
        signUpLink.setOnMouseClicked(_ -> new SignUpPage(stage));

        HBox signUpBox = new HBox(5);
        signUpBox.getChildren().addAll(noAccountLabel, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);

        return signUpBox;
    }

    private Label labelLogin() {
        Label loginTitle = new Label("Login");
        loginTitle.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Lato-Bold.ttf")).toString(),40));
        loginTitle.setStyle("-fx-text-fill: white;");
        return loginTitle;
    }

    private TextField createTextField() {
        TextField field = new TextField();
        field.setPromptText(LanguageManager.getLanguageBasedString("Benutzername","Username"));
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setRadius(8);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.25));
        field.setEffect(shadow);
        field.setStyle("-fx-font-size: 16px;" +
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 5px;" +
                "-fx-background-color: #FFFFFF;");
        return field;
    }

    private PasswordField createPasswordField() {
        PasswordField field = new PasswordField();
        field.setPromptText(LanguageManager.getLanguageBasedString("Passwort","Password"));
        field.setPrefWidth(270);
        field.setPrefHeight(55);
        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setRadius(8);
        shadow.setSpread(0.1);
        shadow.setColor(Color.rgb(0, 0, 0, 0.25));
        field.setEffect(shadow);
        field.setStyle("-fx-font-size: 16px;" +
                "-fx-background-radius: 20px;" +
                "-fx-border-radius: 20px;" +
                "-fx-border-color: #CCCCCC;" +
                "-fx-border-width: 1px;" +
                "-fx-padding: 5px;" +
                "-fx-background-color: #FFFFFF;");
        return field;
    }

    private ImageView createEyeIcon() {
        Image eyeOpenImage = new Image("file:src/main/resources/Acount/mdi_eye.png");
        Image eyeClosedImage = new Image("file:src/main/resources/Acount/ph_eye-closed-bold.png");
        ImageView eyeIcon = new ImageView(eyeClosedImage);
        eyeIcon.setFitWidth(25);
        eyeIcon.setFitHeight(25);
        eyeIcon.setPickOnBounds(true);
        eyeIcon.setCursor(Cursor.HAND);

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setPrefWidth(passwordField.getPrefWidth());
        visiblePasswordField.setPrefHeight(passwordField.getPrefHeight());
        visiblePasswordField.setFont(passwordField.getFont());
        visiblePasswordField.setEffect(passwordField.getEffect());
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
        String darkencolor=darkenColor();

        button.setPrefWidth(170);
        button.setPrefHeight(45);
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
                                "-fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;", darkencolor)));

        button.setOnMouseExited(_ ->
                button.setStyle(String.format(
                        "-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold; " +
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
        double factor = 2;
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

}