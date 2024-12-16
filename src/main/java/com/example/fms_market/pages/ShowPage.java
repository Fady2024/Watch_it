package com.example.fms_market.pages;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.util.Calculate_Rating;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.*;
import java.io.IOException;
import java.util.Date;

public class ShowPage {
    public ShowPage(User user, Show show, Stage stage) throws IOException {
        // Screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        // Layout setup
        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Show Details"));

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #2c2c2c;");

        // Show title
        Label title = new Label(show.getTitle());
        title.setFont(Font.font("Arial", 24));
        title.setTextFill(Color.WHITE);

        // Show description
        Label description = new Label(show.getDescription());
        description.setFont(Font.font("Arial", 16));
        description.setTextFill(Color.WHITE);
        description.setWrapText(true);

        // Average rating label
        Label averageRatingLabel = new Label("Calculating rating...");
        averageRatingLabel.setFont(Font.font("Arial", 16));
        averageRatingLabel.setTextFill(Color.YELLOW);

        // Calculate and display the average rating
        Calculate_Rating calculateRating = new Calculate_Rating();
        double averageRating = calculateRating.calculateAverageRating(show);
        averageRatingLabel.setText(String.format("Average Rating: %.2f", averageRating));

        // Animated rating bar
        Label ratingLabel = new Label("Rate this show:");
        ratingLabel.setFont(Font.font("Arial", 16));
        ratingLabel.setTextFill(Color.WHITE);

        HBox ratingBar = createAnimatedRatingBar(user, show, averageRatingLabel);

        // Add a submit button to the ShowPage class
        Button submitButton = new Button("Submit Rating");
        submitButton.setFont(Font.font("Arial", 16));
        submitButton.setStyle("-fx-background-color: #451952; -fx-text-fill: white;");
        submitButton.setOnAction(event -> {
            try {
                Date dateOfWatched = new Date();
                ShowJsonHandler.saveShowRating(user.getId(), show.getId(), dateOfWatched, selectedRating[0]);
                double newAverageRating = calculateRating.calculateAverageRating(show);
                averageRatingLabel.setText(String.format("Average Rating: %.2f", newAverageRating));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Add elements to the content layout
        content.getChildren().addAll(title, description, averageRatingLabel, ratingLabel, ratingBar, submitButton);
        layout.setCenter(content);

        // Scene setup
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Show Details");
        stage.show();
    }

    private int[] selectedRating = {0}; // Store the selected rating

    private HBox createAnimatedRatingBar(User user, Show show, Label averageRatingLabel) {
        HBox ratingBar = new HBox(10);
        ratingBar.setAlignment(Pos.CENTER);
        ratingBar.setPadding(new Insets(10));

        Calculate_Rating calculateRating = new Calculate_Rating();

        // Create 5 stars
        for (int i = 1; i <= 5; i++) {
            Polygon star = createStar(20, Color.GRAY); // Default unselected star
            int currentRating = i;

            // Hover animation
            star.setOnMouseEntered(event -> applyScaleAnimation(star, 1.2));
            star.setOnMouseExited(event -> applyScaleAnimation(star, 1.0));

            // Click event
            star.setOnMouseClicked(event -> {
                selectedRating[0] = currentRating; // Update the selected rating
                updateStarColors(ratingBar, currentRating); // Update stars to reflect the selected rating
            });

            ratingBar.getChildren().add(star);
        }
        return ratingBar;
    }

    private Polygon createStar(double size, Color color) {
        Polygon star = new Polygon();
        double centerX = size, centerY = size;
        double radius = size;

        // Points for a 5-pointed star
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2; // Adjust rotation
            double r = (i % 2 == 0) ? radius : radius / 2; // Alternate between outer and inner radius
            double x = centerX + Math.cos(angle) * r;
            double y = centerY + Math.sin(angle) * r;
            star.getPoints().addAll(x, y);
        }
        star.setFill(color);
        return star;
    }

    private void applyScaleAnimation(Polygon star, double scale) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), star);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
    }

    private void updateStarColors(HBox ratingBar, int selectedRating) {
        for (int i = 0; i < ratingBar.getChildren().size(); i++) {
            Polygon star = (Polygon) ratingBar.getChildren().get(i);
            if (i < selectedRating) {
                star.setFill(Color.GOLD); // Selected stars
            } else {
                star.setFill(Color.GRAY); // Unselected stars
            }
        }
    }
}