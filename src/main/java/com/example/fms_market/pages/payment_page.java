package com.example.fms_market.pages;

import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Label;


import java.awt.*;
import java.util.function.UnaryOperator;

public class payment_page {
    boolean valid=true;

    public payment_page(Stage stage, String finalImagePath,String plane_name) {

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

        Text title = LanguageManager.createLanguageText("Letzter Schritt","Last Step", "30px", "white");
        Text text = LanguageManager.createLanguageText("Sie sind beim letzten Schritt angekommen. Geben Sie Ihre \nKarteninformationen ein und bezahlen Sie Ihr Abonnement.","You arrived at the last step.Enter your card \ninformation and pay for your subscription plan.", "16px", "white");
        Text cardnumber = LanguageManager.createLanguageText("Kartennummer\n"+ "\n"+"Geben Sie die 16-stellige Nummer auf der Karte ein","Card Number\n" +
                "\n" +
                "Enter the 16-digit number on the card", "16px", "white");

        Text Expiredate = LanguageManager.createLanguageText("Verfallsdatum\n"+"\n"+"Geben Sie das Ablaufdatum Ihrer Karte ein","Expiry Date\n" +
                "\n" +
                "Enter the Expiration date of your card", "16px", "white");
        Text cvc = LanguageManager.createLanguageText("Kartenprüfziffer\n"+"\n"+"Geben Sie die 3- bis 4-stellige Nummer auf der Karte ein","CVC\n" +
                "\n" +
                "Enter the 3 a 4 digit number on the card", "16px", "white");


        TextField numberField_card = LanguageManager.languageTextField("Kartennummer","Card Number","14","black");
        numberField_card.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");
        TextField numberField_date = LanguageManager.languageTextField("MM/JJ","MM/YY","14","black");
        numberField_date.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");

        TextField numberField_cvc = LanguageManager.languageTextField("Kartenprüfziffer","cvc","14","black");
        numberField_cvc.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-prompt-text-fill: #2B2D30;-fx-background-radius: 25;");

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
            VBox.setMargin(Expiredate, new Insets(-stageHeight*0.2, stageWidth*0.15, stageHeight*0.09, -stageWidth*0.01));
//            VBox.setMargin(numberField_date, new Insets(-stageHeight*0.095, stageWidth*0.13, stageHeight*0.09, 0));
            VBox.setMargin(cardnumber, new Insets(stageHeight*0.02, stageWidth*0.11, stageHeight*0.02, 0));
//            VBox.setMargin(numberField_card, new Insets(-stageHeight*0.025, stageWidth*0.13, stageHeight*0.2, 0));
            VBox.setMargin(numberField_card, new Insets(-stageHeight*0.025, stageWidth*0.1, stageHeight*0.2, 0));
            VBox.setMargin(numberField_date, new Insets(-stageHeight*0.095, stageWidth*0.1, stageHeight*0.09, 0));


        } else {
            VBox.setMargin(Expiredate, new Insets(-stageHeight*0.2, stageWidth*0.1, stageHeight*0.09, -stageWidth*0.01));
            VBox.setMargin(numberField_date, new Insets(-stageHeight*0.095, stageWidth*0.1, stageHeight*0.09, 0));
            VBox.setMargin(cardnumber, new Insets(stageHeight*0.02, stageWidth*0.1, stageHeight*0.02, 0));
            VBox.setMargin(numberField_card, new Insets(-stageHeight*0.025, stageWidth*0.1, stageHeight*0.2, 0));
        }
//100=0.1
        //20=0.02
        //25=0.025
        VBox.setMargin(title, new Insets(stageHeight*0.1, 0, 0, 0));
        VBox.setMargin(text, new Insets(stageHeight*0.02, 0, 0, stageWidth*0.02));


        VBox.setMargin(cvc, new Insets(-stageHeight*0.09, stageWidth*0.1, stageHeight*0.09, stageWidth*0.01));
        VBox.setMargin(numberField_cvc, new Insets(-stageHeight*0.1, stageWidth*0.1, stageHeight*0.09, 0));
        Button backButton = createStyledButton(LanguageManager.getLanguageBasedString("Zurück","Back"));
        Button proceedButton = createStyledButton(LanguageManager.getLanguageBasedString("Fortfahren","Proceed"));
        proceedButton.setStyle("-fx-font-size: 12px; -fx-background-color: #8E5BDC; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-background-radius: 30px; -fx-border-color: transparent;");
        if(numberField_cvc.getText().length()<3||numberField_card.getText().length()<16){valid=false;}
        backButton.setOnAction(e -> {
            new subscription_page(stage);
        });

        proceedButton.setOnAction(e -> {
Subscription add_new =new Subscription("userid",plane_name);

SubscriptionManager.addSubscriptions(add_new);



        });


        HBox buttonBox = new HBox(20, backButton, proceedButton);
        backButton.setPrefHeight(10);
        proceedButton.setMinHeight(10);

        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(-50, 150, 20, 0));
        backButton.setAlignment(Pos.CENTER);
        proceedButton.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();


        vbox.getChildren().addAll(title, text, cardnumber, numberField_card, Expiredate, numberField_date, cvc, numberField_cvc,buttonBox);
        vbox.setTranslateX(-300);
       numberField_card.setPrefWidth(400);
       numberField_date.setPrefWidth(400);
       numberField_cvc.setPrefWidth(400);
        vbox.setFillWidth(false);

        stackPane.getChildren().add(vbox);

        StackPane stackPane2 = new StackPane();
        innerRectangle.setTranslateX(stageWidth*0.2);
imageView.setTranslateX(stageWidth*0.2);
        stackPane.getChildren().addAll(innerRectangle,imageView);


        StackPane.setAlignment(vbox, Pos.CENTER);
        StackPane.setMargin(vbox, new Insets(0, 100, 0, 0));


        hbox.getChildren().addAll(stackPane,stackPane2);

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
        text.setStyle("-fx-font-size: " + fontSize + "; -fx-fill: " + color + ";");
        return text;
    }


}
