package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class ShowPage {
    public ShowPage(User user, Show show, Stage stage) throws IOException {
        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Show Details"));

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #2c2c2c;"); // Set background color

        // Show poster
        ImageView posterView = new ImageView(new Image(show.getPoster()));
        posterView.setFitWidth(200);
        posterView.setFitHeight(300);

        // Show title
        Label title = new Label(show.getTitle());
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.WHITE);

        // Show description
        Label description = new Label(show.getDescription());
        description.setFont(Font.font("Arial", 16));
        description.setTextFill(Color.WHITE);
        description.setWrapText(true);

        // Rating input
        Label ratingLabel = new Label("Rate this show (1-5):");
        ratingLabel.setFont(Font.font("Arial", 16));
        ratingLabel.setTextFill(Color.WHITE);

        TextField ratingInput = new TextField();
        ratingInput.setMaxWidth(50);

        Button saveButton = new Button("Save Rating");
        saveButton.setOnAction(event -> {
            try {
                int rating = Integer.parseInt(ratingInput.getText());
                if (rating < 1 || rating > 5) {
                    throw new IllegalArgumentException("Rating must be between 1 and 5.");
                }
                Date dateOfWatched = new Date();
                ShowJsonHandler.saveShowRating(user.getId(), show.getId(), dateOfWatched, rating);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        content.getChildren().addAll(posterView, title, description, ratingLabel, ratingInput, saveButton);
        layout.setCenter(content);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Show Details");
        stage.show();
    }
}