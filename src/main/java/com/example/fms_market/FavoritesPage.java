package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritesPage {

    private static final double MOVIE_CARD_WIDTH = 150;
    private static final double MOVIE_CARD_HEIGHT = 280;

    public FavoritesPage(User user, Stage stage) throws IOException {
        List<Integer> favoriteShowIds = UserJsonHandler.getFavoriteShows(user.getId());
        List<Show> allShows = ShowJsonHandler.readShows();
        List<Show> favoriteShows = allShows.stream()
                .filter(show -> favoriteShowIds.contains(show.getId()))
                .collect(Collectors.toList());

        GridPane showContainer = new GridPane();
        showContainer.setStyle("-fx-background-color: #2B2B2B;");
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(20);
        showContainer.setVgap(20);

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Favorites"));
        layout.setCenter(showContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Favorites");
        stage.show();

        // Adjust layout dynamically
        scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), favoriteShows, user));
        adjustLayout(showContainer, stageWidth, favoriteShows, user);
    }

    private void adjustLayout(GridPane showContainer, double width, List<Show> favoriteShows, User user) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Show show : favoriteShows) {
            VBox showCard = new VBox(5);
            showCard.setAlignment(Pos.TOP_CENTER);

            Image showPoster = new Image(show.getPoster(), false);

            Rectangle roundedRectangle = new Rectangle(MOVIE_CARD_WIDTH, MOVIE_CARD_HEIGHT);
            roundedRectangle.setArcWidth(20);
            roundedRectangle.setArcHeight(20);
            roundedRectangle.setFill(Color.GRAY);

            ImageView posterView = new ImageView(showPoster);
            posterView.setFitWidth(MOVIE_CARD_WIDTH);
            posterView.setFitHeight(MOVIE_CARD_HEIGHT);
            posterView.setClip(roundedRectangle);

            Label favoriteIcon = new Label("♥");
            favoriteIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: red; -fx-font-weight: bold;");
            favoriteIcon.setPadding(new Insets(5));
            favoriteIcon.setTranslateY(-10);

            Rectangle heartBackground = new Rectangle(30, 30);
            heartBackground.setArcWidth(15);
            heartBackground.setArcHeight(15);
            heartBackground.setFill(Color.color(1, 1, 1, 0.4));

            StackPane heartContainer = new StackPane(heartBackground, favoriteIcon);
            heartContainer.setAlignment(Pos.TOP_LEFT);
            heartContainer.setPadding(new Insets(5));

            StackPane posterContainer = new StackPane(posterView, heartContainer);
            posterContainer.setAlignment(Pos.TOP_LEFT);

            Label title = new Label(show.getTitle());
            title.setFont(Font.font("Arial", 14));
            title.setTextFill(Color.WHITE);
            title.setAlignment(Pos.CENTER);
            title.setMaxWidth(MOVIE_CARD_WIDTH);
            title.setWrapText(true);

            favoriteIcon.setOnMouseClicked(event -> {
                try {
                    UserJsonHandler.removeFavoriteShow(user.getId(), show.getId());
                    showContainer.getChildren().remove(showCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            showCard.getChildren().addAll(posterContainer, title);
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }
}
