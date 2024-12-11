package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.awt.*;
import java.util.List;

public class subscription_page {
    public subscription_page(Stage stage){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Button backButton = createBackButton(stage);
        backButton.setTranslateX(-stageWidth*0.45);
        backButton.setTranslateY(-stageHeight*0.45);

        HBox hbox = new HBox(30);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefWidth(stageWidth * 0.6);
        hbox.setPrefHeight(stageHeight * 0.6);



        StackPane root = new StackPane();
        root.setPadding(new Insets(10));


        for (int i = 0; i < 3; i++) {
            Rectangle outerRectangle = new Rectangle();
            outerRectangle.setFill(Color.web("#421B50"));
            outerRectangle.setArcWidth(20);
            outerRectangle.setArcHeight(20);
            outerRectangle.setOpacity(0.0);

            Rectangle innerRectangle = new Rectangle();
            innerRectangle.setFill(Color.web("#404040"));
            innerRectangle.setArcWidth(20);
            innerRectangle.setArcHeight(20);

            Button button1 = new Button("Subscribe");


            button1.setPrefWidth(stageWidth * 0.1);
            button1.setPrefHeight(stageHeight * 0.05);





            String image_path;
            ImageView imageView = null;
String plan_name;
            if (i == 0) {
                image_path = "file:src/main/resources/image/Basic plan.png";
                plan_name="Basic";
                Image image1 = new Image(image_path);
                imageView = new ImageView(image1);
                imageView.setFitWidth(400);
                imageView.setFitHeight(400);
            } else if (i == 1) {
                image_path = "file:src/main/resources/image/Standard plan.png";
                plan_name="Standard";
                Image image2 = new Image(image_path);
                imageView = new ImageView(image2);
                imageView.setFitWidth(400);
                imageView.setFitHeight(400);
            } else {
                image_path = "file:src/main/resources/image/premium plan.png";
                plan_name="Premium";
                Image image3 = new Image(image_path);
                imageView = new ImageView(image3);

            }

            String finalImagePath = image_path;
            button1.setOnAction(_ -> {
                new payment_page(stage,finalImagePath,plan_name);
            });

            Runnable onHover = () -> {
                button1.setStyle("-fx-background-color: linear-gradient(to top left, #5C0C5A, #9C0479); -fx-text-fill: white; -fx-background-radius: 80;");
                outerRectangle.setOpacity(1.0);
            };


            Runnable onExit = () -> {
                button1.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 80;");
                outerRectangle.setOpacity(0.0);
            };


            innerRectangle.setOnMouseEntered(e -> onHover.run());
            innerRectangle.setOnMouseExited(e -> onExit.run());



            button1.setOnMouseEntered(e -> onHover.run());
            button1.setOnMouseExited(e -> onExit.run());



            imageView.setOnMouseEntered(e -> onHover.run());
            imageView.setOnMouseExited(e -> onExit.run());

            StackPane stack = new StackPane(outerRectangle, innerRectangle, button1, imageView);

            hbox.getChildren().add(stack);
        }

        hbox.setStyle("-fx-background-color: black; -fx-background-radius: 20;");
        root.getChildren().addAll(hbox,backButton);
        Scene scene = new Scene(root,stageWidth,stageHeight);

        scene.widthProperty().addListener((_, _, newValue) ->
                updateLayout(hbox, newValue.doubleValue(), scene.getHeight())

        );
        scene.heightProperty().addListener((_, _, newValue) ->
                updateLayout(hbox, scene.getWidth(), newValue.doubleValue())
        );

        stage.setScene(scene);
        stage.setTitle("Improved JavaFX App");
        stage.setMaximized(true);
        stage.show();}
    private void updateLayout(HBox hbox, double width, double height) {
        hbox.getChildren().forEach(node -> {
            StackPane stack = (StackPane) node;
            Rectangle outerRect = (Rectangle) stack.getChildren().get(0);
            Rectangle innerRect = (Rectangle) stack.getChildren().get(1);
            Button button = (Button) stack.getChildren().get(2);
            ImageView imageView = (ImageView) stack.getChildren().get(3);

            outerRect.setWidth(width * 0.3);
            outerRect.setHeight(height * 0.8);

            innerRect.setWidth(width * 0.27);
            innerRect.setHeight(height * 0.73);

            double fontSize = Math.min(button.getWidth(), button.getHeight()) / 4;
            button.setStyle("-fx-font-size: " + fontSize + "px;");
            button.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-background-radius: 80;");
            StackPane.setAlignment(button, Pos.BOTTOM_CENTER);
            StackPane.setMargin(button, new Insets(0, 0, height * 0.2, 0));

            imageView.setFitWidth(width * 0.25);
            imageView.setFitHeight(height * 0.4);

            if (imageView.getImage().getUrl().contains("Basic plan.png")) {
                imageView.setFitHeight(height * 0.45);
                StackPane.setMargin(imageView, new Insets(0, 0, height * 0.15, 0));
            } else if (imageView.getImage().getUrl().contains("Standard plan")) {
                imageView.setFitHeight(height * 0.40);
                imageView.setFitWidth(width * 0.25);
                StackPane.setMargin(imageView, new Insets(0, 0, height * 0.2, 0));



            } else {
                imageView.setFitHeight(height * 0.5);

                StackPane.setAlignment(imageView, Pos.BOTTOM_CENTER);

                StackPane.setMargin(imageView, new Insets(0, 0, height * 0.3, 0));

            }

        });
    }

    public static javafx.scene.text.Text createStyledText(String content, String fontSize, String color) {
        javafx.scene.text.Text text = new javafx.scene.text.Text(content);
        text.setStyle("-fx-font-size: " + fontSize + "; -fx-fill: " + color + ";");
        return text;
    }
    private Button createBackButton(Stage stage) {
        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-font-size: 16px; " +
                        "-fx-text-fill:#992193; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-color:#992193; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-padding: 10px 20px;"
        );

        backButton.setOnMouseEntered(_ -> backButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-font-size: 16px; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-color: white; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-padding: 10px 20px;"
        ));

        // Updated setOnMouseExited handler
        backButton.setOnMouseExited(_ -> {


                // Night theme styles
                backButton.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-font-size: 16px; " +
                                "-fx-text-fill: #992193; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-color: #992193; " +
                                "-fx-border-radius: 20px; " +
                                "-fx-padding: 10px 20px;"
                );

        });

        backButton.setOnAction(_ -> new WelcomePage(stage));
        return backButton;
    }
}