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
        // Get the favorite shows from the user's data
        List<Integer> favoriteShowIds = UserJsonHandler.getFavoriteShows(user.getId());
        List<Show> allShows = ShowJsonHandler.readShows();
        List<Movie> favoriteMovies = allShows.stream()
                .filter(show -> favoriteShowIds.contains(show.getId()) && show instanceof Movie)
                .map(show -> (Movie) show)
                .collect(Collectors.toList());

        GridPane movieContainer = new GridPane();
        movieContainer.setStyle("-fx-background-color: #2B2B2B;");
        movieContainer.setPadding(new Insets(20));
        movieContainer.setHgap(20);
        movieContainer.setVgap(20);

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Favorites"));
        layout.setCenter(movieContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Favorites");
        stage.show();

        // Adjust layout dynamically
        scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(movieContainer, newValue.doubleValue(), favoriteMovies,user));
        adjustLayout(movieContainer, stageWidth, favoriteMovies,user);
    }

    private void adjustLayout(GridPane movieContainer, double width, List<Movie> favoriteMovies, User user) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        movieContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : favoriteMovies) {
            VBox movieCard = new VBox(5);
            movieCard.setAlignment(Pos.TOP_CENTER);

            Image moviePoster = new Image(movie.getPoster(), false);

            Rectangle roundedRectangle = new Rectangle(MOVIE_CARD_WIDTH, MOVIE_CARD_HEIGHT);
            roundedRectangle.setArcWidth(20);
            roundedRectangle.setArcHeight(20);
            roundedRectangle.setFill(Color.GRAY);

            ImageView posterView = new ImageView(moviePoster);
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

            Label title = new Label(movie.getTitle());
            title.setFont(Font.font("Arial", 14));
            title.setTextFill(Color.WHITE);
            title.setAlignment(Pos.CENTER);
            title.setMaxWidth(MOVIE_CARD_WIDTH);
            title.setWrapText(true);

            favoriteIcon.setOnMouseClicked(event -> {
                try {
                    UserJsonHandler.removeFavoriteShow(user.getId(), movie.getId());
                    movieContainer.getChildren().remove(movieCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            movieCard.getChildren().addAll(posterContainer, title);
            movieContainer.add(movieCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }
}
