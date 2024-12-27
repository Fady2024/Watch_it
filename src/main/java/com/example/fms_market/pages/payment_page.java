package com.example.fms_market.pages;

import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.model.Subscription;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class payment_page {
    boolean valid = true;
    boolean valid2 = true;
    boolean valid3 = true;
    private final TextField numberField_card;
    private final TextField numberField_date;
    private final TextField numberField_cvc;

    public payment_page(Stage stage, User user, String finalImagePath, String plane_name) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        HBox hbox = new HBox(0);
        hbox.setStyle("-fx-padding: 20px; -fx-background-color: #1C1C1C;");
        hbox.setAlignment(Pos.CENTER);
        Rectangle innerRectangle = new Rectangle();
        innerRectangle.setFill(Color.web("#404040"));
        innerRectangle.setWidth(stageWidth * 0.27);
        innerRectangle.setHeight(stageHeight * 0.73);
        innerRectangle.setArcWidth(30);
        innerRectangle.setArcHeight(30);

        Image image = new Image(finalImagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(stageHeight * 0.5);
        imageView.setFitWidth(stageWidth * 0.25);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setStyle("-fx-background-color: #1C1C1C;");
        vbox.setPrefWidth(400);
        vbox.setPrefHeight(400);

        Text title = LanguageManager.createLanguageText("Letzter Schritt", "Last Step", "30px", "white");
        Text text = LanguageManager.createLanguageText("Sie sind beim letzten Schritt angekommen. Geben Sie Ihre \nKarteninformationen ein und bezahlen Sie Ihr Abonnement.", "You arrived at the last step.Enter your card \ninformation and pay for your subscription plan.", "16px", "white");
        Text cardnumber = LanguageManager.createLanguageText("Kartennummer\n\nGeben Sie die 16-stellige Nummer auf der Karte ein", "Card Number\n\nEnter the 16-digit number on the card", "16px", "white");
        Text Expiredate = LanguageManager.createLanguageText("Verfallsdatum\n\nGeben Sie das Ablaufdatum Ihrer Karte ein", "Expiry Date\n\nEnter the Expiration date of your card", "16px", "white");
        Text cvc = LanguageManager.createLanguageText("Kartenprüfziffer\n\nGeben Sie die 3- bis 4-stellige Nummer auf der Karte ein", "CVC\n\nEnter the 3 a 4 digit number on the card", "16px", "white");
        cvc.setTranslateY(33);

        numberField_card = LanguageManager.languageTextField("Kartennummer", "Card Number", "14", "black");
        numberField_card.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");
        numberField_date = LanguageManager.languageTextField("MM/JJ", "MM/YY", "14", "black");
        numberField_date.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");
        numberField_cvc = LanguageManager.languageTextField("Kartenprüfziffer", "cvc", "14", "black");
        numberField_cvc.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");
        numberField_cvc.setTranslateY(33);


        Label errorMessageLabel = new Label();
        errorMessageLabel.setStyle("-fx-text-fill: red;");

        UnaryOperator<TextFormatter.Change> cvcFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}")) {
                if (newText.length() < 3 || newText.length() > 4) {
                    return change;
                }
                return change;
            }
            return null;
        };

        TextFormatter<String> cvcFormatter = new TextFormatter<>(cvcFilter);
        numberField_cvc.setTextFormatter(cvcFormatter);

        UnaryOperator<TextFormatter.Change> dateFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}/?\\d{0,2}")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> dateFormatter = new TextFormatter<>(dateFilter);
        numberField_date.setTextFormatter(dateFormatter);

        Label errorLabel_card = new Label();
        errorLabel_card.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
        errorLabel_card.setTranslateY(-165);
        errorLabel_card.setTranslateX(-100);

        Label errorLabel_cvc = new Label();
        errorLabel_cvc.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
        errorLabel_cvc.setTranslateY(-40);
        errorLabel_cvc.setTranslateX(-105);

        Label errorLabel_date = new Label();
        errorLabel_date.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
        errorLabel_date.setTranslateY(-220);
        errorLabel_date.setTranslateX(-120);

        UnaryOperator<TextFormatter.Change> cardFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 16) {
                return change;
            }
            return null;
        };

        TextFormatter<String> cardFormatter = new TextFormatter<>(cardFilter);
        numberField_card.setTextFormatter(cardFormatter);

        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            VBox.setMargin(Expiredate, new Insets(-stageHeight * 0.2, stageWidth * 0.15, stageHeight * 0.09, -stageWidth * 0.01));
            VBox.setMargin(cardnumber, new Insets(stageHeight * 0.02, stageWidth * 0.11, stageHeight * 0.02, 0));
            VBox.setMargin(numberField_card, new Insets(-stageHeight * 0.025, stageWidth * 0.1, stageHeight * 0.2, 0));
            VBox.setMargin(numberField_date, new Insets(-stageHeight * 0.095, stageWidth * 0.1, stageHeight * 0.09, 0));
        } else {
            VBox.setMargin(Expiredate, new Insets(-stageHeight * 0.2, stageWidth * 0.1, stageHeight * 0.09, -stageWidth * 0.01));
            VBox.setMargin(numberField_date, new Insets(-stageHeight * 0.095, stageWidth * 0.1, stageHeight * 0.09, 0));
            VBox.setMargin(cardnumber, new Insets(stageHeight * 0.02, stageWidth * 0.1, stageHeight * 0.02, 0));
            VBox.setMargin(numberField_card, new Insets(-stageHeight * 0.025, stageWidth * 0.1, stageHeight * 0.2, 0));
        }

        VBox.setMargin(title, new Insets(stageHeight * 0.1, 0, 0, 0));
        VBox.setMargin(text, new Insets(stageHeight * 0.02, 0, 0, stageWidth * 0.02));
        VBox.setMargin(cvc, new Insets(-stageHeight * 0.09, stageWidth * 0.1, stageHeight * 0.09, stageWidth * 0.01));
        VBox.setMargin(numberField_cvc, new Insets(-stageHeight * 0.1, stageWidth * 0.1, stageHeight * 0.09, 0));

        Button backButton = createStyledButton(LanguageManager.getLanguageBasedString("Zurück", "Back"));
        Button proceedButton = createStyledButton(LanguageManager.getLanguageBasedString("Fortfahren", "Proceed"));
        proceedButton.setStyle("-fx-font-size: 12px; -fx-background-color: #8E5BDC; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-background-radius: 30px; -fx-border-color: transparent;");

        backButton.setOnAction(e -> new subscription_page(stage, user));

        proceedButton.setOnAction(e -> {
            String expireDate = numberField_date.getText();
            if (!expireDate.matches("\\d{2}/\\d{2}")) {
                errorLabel_date.setText("Date must be in MM/YY format.");
                valid = false;
            } else {
                String[] parts = expireDate.split("/");
                int month = Integer.parseInt(parts[0]);
                int year = Integer.parseInt(parts[1]) + 2000;

                LocalDate today = LocalDate.now();
                YearMonth expire = YearMonth.of(year, month);

                if (month < 1 || month > 12) {
                    errorLabel_date.setText("The month must be between 01 and 12.");
                    valid = false;
                } else if (expire.isBefore(YearMonth.from(today))) {
                    errorLabel_date.setText("Expiration date must be in the future.");
                    valid = false;
                } else {
                    errorLabel_date.setText("");
                    valid = true;
                }
            }

            if (numberField_card.getText().length() < 16 || !numberField_card.getText().matches("\\d+")) {
                errorLabel_card.setText("The card number must contain 16 digits.");
                valid2 = false;
            } else {
                errorLabel_card.setText("");
                valid2 = true;
            }

            if (numberField_cvc.getText().length() < 3 || !numberField_cvc.getText().matches("\\d+")) {
                errorLabel_cvc.setText("The CVC code must contain at least 3 digits.");
                valid3 = false;
            } else {
                errorLabel_cvc.setText("");
                valid3 = true;
            }

            if (valid && valid2 && valid3) {
                List<String> cardDetails = Arrays.asList(
                        numberField_card.getText(),
                        numberField_date.getText(),
                        numberField_cvc.getText()
                );
                confirmPopup(stage, user, plane_name);
            }
        });

        HBox buttonBox = new HBox(20, backButton, proceedButton);
        backButton.setPrefHeight(10);
        proceedButton.setMinHeight(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(-50, 150, 20, 0));
        backButton.setAlignment(Pos.CENTER);
        proceedButton.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        vbox.getChildren().addAll(title, text, cardnumber, numberField_card, errorLabel_card, Expiredate, numberField_date, cvc, numberField_cvc, errorLabel_cvc, errorLabel_date, buttonBox);
        vbox.setTranslateX(-300);
        numberField_card.setPrefWidth(400);
        numberField_date.setPrefWidth(400);
        numberField_cvc.setPrefWidth(400);
        vbox.setFillWidth(false);
        stackPane.getChildren().add(vbox);

        StackPane stackPane2 = new StackPane();
        innerRectangle.setTranslateX(stageWidth * 0.2);
        imageView.setTranslateX(stageWidth * 0.2);
        stackPane.getChildren().addAll(innerRectangle, imageView);
        StackPane.setAlignment(vbox, Pos.CENTER);
        StackPane.setMargin(vbox, new Insets(0, 100, 0, 0));

        hbox.getChildren().addAll(stackPane, stackPane2);
        Scene scene = new Scene(hbox, stageWidth, stageHeight);
        stage.setTitle("Simple JavaFX Test");
        stage.setScene(scene);
        stage.show();
    }

    public static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 12px; -fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-background-radius: 30px; -fx-border-color: transparent;");
        button.setMinWidth(100);
        button.setMinHeight(40);
        return button;
    }

    public static Text createStyledText(String content, String fontSize, String color) {
        Text text = new Text(content);
        text.setStyle(String.format("-fx-font-size: %s; -fx-fill: %s;", fontSize, color));
        return text;
    }

    private void confirmPopup(Stage stage, User user, String plane_name) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(50);
        VBox texts = new VBox(10);
        int price;
        if (Objects.equals(plane_name, "basic")) {
            price = Subscription.price_basic;
        } else if (Objects.equals(plane_name, "standard")) {
            price = Subscription.price_standard;
        } else {
            price = Subscription.price_permium;
        }
        String str = String.valueOf(price);
        Text addNewText = new Text(LanguageManager.getLanguageBasedString(
                STR."Möchten Sie wirklich fortfahren? Hinweis: Wenn Sie fortfahren, zahlen Sie \{str}$",
                STR."Are You Sure You Want to Continue? Note: if you continue you will pay \{str}$"
        ));
        addNewText.setFill(Paint.valueOf("white"));
        addNewText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 20));
        texts.getChildren().addAll(addNewText);
        addNewText.setWrappingWidth(380);
        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button(LanguageManager.getLanguageBasedString("Stornieren", "Cancel"));
        cancelButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: white;-fx-text-fill: black;");
        cancelButton.setPrefSize(180, 50);
        Button confirmButton = new Button(LanguageManager.getLanguageBasedString("Bestätigen", "Confirm"));
        confirmButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: #8D5BDC;-fx-text-fill: black;");
        confirmButton.setPrefSize(180, 50);
        cancelButton.setOnAction(e -> popupStage.close());

        confirmButton.setOnAction(e -> {
            showAlert(LanguageManager.getLanguageBasedString("Erfolg", "Success"), LanguageManager.getLanguageBasedString("Die Zahlung wurde erfolgreich durchgeführt", "Payment has been made successfully"));
            popupStage.close();

            List<String> cardDetails = Arrays.asList(
                    numberField_card.getText(),
                    numberField_date.getText(),
                    numberField_cvc.getText()
            );

            Subscription existingSubscription = SubscriptionManager.getSubscriptionByUserId(user.getId());
            if (existingSubscription != null) {
                existingSubscription.setStart_date(LocalDate.now().toString());
                SubscriptionManager.resetMoviesWatched(user.getId());
            } else {
                Subscription newSubscription = new Subscription(user.getId(), plane_name);
                newSubscription.setCardDetails(cardDetails);
                SubscriptionManager.addSubscriptions(newSubscription);
            }
            new HomePage(user, stage);
        });

        popupVBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(texts, buttonBox);
        popupVBox.setPadding(new Insets(30, 60, 30, 60));
        popupVBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 500, 250);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle(LanguageManager.getLanguageBasedString("Löschen bestätigen", "Confirm Delete"));
        popupStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}