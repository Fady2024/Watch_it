package com.example.fms_market.account;
import com.example.fms_market.pages.HomePage;
import com.example.fms_market.util.TopPanel;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
    private final Label login;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Text emailErrorText;
    private final Text passwordErrorText;
    private final HBox signUpText;
    private final TopPanel dayNightSwitch;
    private double stageWidth;
    private double stageHeight;

    public LoginPageFX(Stage stage) {
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "German");
        languageComboBox.setValue(LanguageManager.getLanguageBasedString("German", "English"));
        languageComboBox.setTranslateY(20);
        languageComboBox.setTranslateX(200);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stageWidth = screenSize.getWidth();
        stageHeight = screenSize.getHeight() / 1.1;

        dayNightSwitch = new TopPanel();

        signUpText = createSignUpText(stage);

        login = labelLogin();
        usernameField = createTextField();
        passwordField = createPasswordField();
        loginButton = createButton(Color.rgb(0, 123, 255));

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
        movie.setAlignment(Pos.CENTER);
        movie.getChildren().addAll(cameraIconView, watchText);

        Image searchIcon = new Image("Acount/search.png");
        ImageView searchIconView = new ImageView(searchIcon);
        searchIconView.setFitWidth(30);
        searchIconView.setFitHeight(30);
        Text searchText = new Text("Search for any movie you want");
        searchText.setFont(new Font(20));
        searchText.setStyle("-fx-fill: white;");
        HBox search = new HBox(10);
        search.setAlignment(Pos.CENTER);
        search.getChildren().addAll(searchIconView, searchText);

        Image listIconImage = new Image("Acount/list.png");
        ImageView listIconView = new ImageView(listIconImage);
        listIconView.setFitWidth(50);
        listIconView.setFitHeight(50);
        Text watchedText = new Text("List your watched Movies");
        watchedText.setFont(new Font(20));
        watchedText.setStyle("-fx-fill: white;");
        HBox list = new HBox(10);
        list.setAlignment(Pos.CENTER);
        list.getChildren().addAll(listIconView, watchedText);

        Image ratedIconImage = new Image("Acount/star.png");
        ImageView ratedIconView = new ImageView(ratedIconImage);
        ratedIconView.setFitWidth(30);
        ratedIconView.setFitHeight(30);
        Text ratedText = new Text("Show the most rated movies");
        ratedText.setFont(new Font(20));
        ratedText.setStyle("-fx-fill: white;");
        HBox rated = new HBox(10);
        rated.setAlignment(Pos.CENTER);
        rated.getChildren().addAll(ratedIconView, ratedText);

        Image favoriteIconImage = new Image("Acount/heart.png");
        ImageView favoriteIconView = new ImageView(favoriteIconImage);
        favoriteIconView.setFitWidth(30);
        favoriteIconView.setFitHeight(30);
        Text favoriteText = new Text("Add Shows to your Favourite List");
        favoriteText.setFont(new Font(20));
        favoriteText.setStyle("-fx-fill: white;");
        HBox favourite = new HBox(10);
        favourite.setAlignment(Pos.CENTER);
        favourite.getChildren().addAll(favoriteIconView, favoriteText);


        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(5, 5, 5, 5));
        layout.getChildren().addAll(titleLabel, movie, search, list, rated, favourite);

        layout.setPrefWidth(stageWidth * 0.5);
        layout.setPrefHeight(stageHeight);

        layout.setLayoutX((rightInsidePane.getPrefWidth() - layout.getPrefWidth()) / 2);
        layout.setLayoutY((rightInsidePane.getPrefHeight() - layout.getPrefHeight()) / 2);

        rightInsidePane.getChildren().add(layout);
        rightPane.getChildren().add(rightInsidePane);

        rightPane.setLayoutX(leftPane.getPrefWidth());

        contentPane.getChildren().addAll(leftPane, rightPane);

        VBox loginForm = new VBox(10);
        loginForm.setAlignment(Pos.TOP_CENTER);
        loginForm.setLayoutX(260);
        loginForm.setLayoutY(200);

        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        VBox usernameBox = new VBox(0, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.TOP_LEFT);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        VBox passwordBox = new VBox(0, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.TOP_LEFT);

        loginForm.getChildren().addAll(
                login,
                usernameBox,
                emailErrorText,
                passwordBox,
                passwordErrorText,
                loginButton,
                signUpText
        );

        contentPane.getChildren().add(loginForm);

        updateLayout(stageWidth, stageHeight);
        Scene scene = new Scene(contentPane, stageWidth, stageHeight);

        scene.widthProperty().addListener((_, _, newWidth) -> updateLayout(newWidth.doubleValue(), scene.getHeight()));
        scene.heightProperty().addListener((_, _, newHeight) -> updateLayout(scene.getWidth(), newHeight.doubleValue()));

        stage.setScene(scene);
        stage.show();

        dayNightSwitch.addActionListener(this::updateColors);
        updateColors();
    }


    private void handleLogin(Stage stage) {
        String email = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        boolean isValid = true;

        emailErrorText.setText("");
        passwordErrorText.setText("");

        if (email.isEmpty()) {
            emailErrorText.setText("User Name cannot be empty.");
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
    }

    private HBox createSignUpText(Stage stage) {
        Label noAccountLabel = new Label("You Don't Have an Account?");
        noAccountLabel.setTextFill(Color.WHITE);

        Hyperlink signUpLink = new Hyperlink("Sign up");
        signUpLink.setTextFill(Color.DEEPSKYBLUE);
        signUpLink.setStyle("-fx-font-weight: bold;");
        signUpLink.setOnMouseClicked(event -> new SignUpPage(stage));

        HBox signUpBox = new HBox(5);
        signUpBox.getChildren().addAll(noAccountLabel, signUpLink);
        signUpBox.setAlignment(Pos.CENTER);

        return signUpBox;
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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


    private Button createButton(Color backgroundColor) {
        Button button = new Button("Login");
        String colorHex = String.format("#800080",
                (int) (backgroundColor.getRed() * 255),
                (int) (backgroundColor.getGreen() * 255),
                (int) (backgroundColor.getBlue() * 255));

        button.setPrefWidth(170);
        button.setPrefHeight(45);
        button.setStyle("-fx-background-color: " + colorHex + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 15px;" +
                "-fx-border-radius: 15px;" +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: " + darkenColor("#800080") + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-radius: 15px;" +
                        "-fx-cursor: hand;"));

        button.setOnMouseExited(e ->
                button.setStyle("-fx-background-color: " + colorHex + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 15px;" +
                        "-fx-border-radius: 15px;" +
                        "-fx-cursor: hand;"));

        return button;
    }

    private String darkenColor(String hexColor) {
        double factor = 0.8;

        int red = Integer.valueOf(hexColor.substring(1, 3), 16);
        int green = Integer.valueOf(hexColor.substring(3, 5), 16);
        int blue = Integer.valueOf(hexColor.substring(5, 7), 16);

        red = (int) (red * factor);
        green = (int) (green * factor);
        blue = (int) (blue * factor);

        return String.format("#%02X%02X%02X", red, green, blue);
    }


    private Text createErrorText() {
        Text errorText = new Text();
        errorText.setStyle("-fx-font-size: 14px; -fx-fill: red;");
        return errorText;
    }

    private ImageView createImageView(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(stageWidth * 0.5);
        imageView.setFitHeight(stageHeight);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void updateLayout(double width, double height) {
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        login.setLayoutX(centerX - 60);
        login.setLayoutY(centerY - 200);

        usernameField.setLayoutX(centerX - 100);
        usernameField.setLayoutY(centerY - 100);

        emailErrorText.setLayoutX(centerX - 100);
        emailErrorText.setLayoutY(centerY - 45);

        passwordField.setLayoutX(centerX - 100);
        passwordField.setLayoutY(centerY - 35);

        loginButton.setLayoutX(centerX - 40);
        loginButton.setLayoutY(centerY + 50);

        signUpText.setLayoutX(centerX - 130);
        signUpText.setLayoutY(centerY + 150);
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

