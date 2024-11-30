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

public class HomePage {

    private static final double MOVIE_CARD_WIDTH = 150;
    private static final double MOVIE_CARD_HEIGHT = 280;

    public HomePage(User user, Stage stage) {
        try {
            Banner.setCurrentUser(user);

            // Fetch all movies
            List<Movie> allMovies = ShowJsonHandler.readMovies();

            // GridPane to display movies
            GridPane movieContainer = new GridPane();
            movieContainer.setPadding(new Insets(20));
            movieContainer.setHgap(20);
            movieContainer.setVgap(20);
            movieContainer.setAlignment(Pos.TOP_CENTER);
            movieContainer.setStyle("-fx-background-color: #2B2B2B;");

            BorderPane layout = new BorderPane();
            layout.setTop(Banner.getBanner(stage, "Home")); // Use the Banner class here and pass the current page
            layout.setCenter(movieContainer);

            // Adjust stage dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int stageWidth = (int) screenSize.getWidth();
            int stageHeight = (int) (screenSize.getHeight() / 1.1);

            Scene scene = new Scene(layout, stageWidth, stageHeight);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();

            // Dynamically adjust layout
            scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(movieContainer, newValue.doubleValue(), allMovies, user.getId()));
            adjustLayout(movieContainer, stageWidth, allMovies, user.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustLayout(GridPane movieContainer, double width, List<Movie> allMovies, int userId) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        movieContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : allMovies) {
            VBox movieCard = createMovieCard(movie, userId);
            movieContainer.add(movieCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createMovieCard(Movie movie, int userId) {
        VBox movieCard = new VBox(5);
        movieCard.setAlignment(Pos.TOP_CENTER);

        ImageView posterView = createPosterView(movie.getPoster());
        Label title = new Label(movie.getTitle());
        title.setFont(Font.font("Arial", 14));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);
        title.setWrapText(true);
        title.setMaxWidth(MOVIE_CARD_WIDTH);

        StackPane posterContainer = new StackPane(posterView, createFavoriteIcon(movie, userId));
        movieCard.getChildren().addAll(posterContainer, title);

        return movieCard;
    }

    private ImageView createPosterView(String posterPath) {
        Image moviePoster = new Image(posterPath, false);
        ImageView posterView = new ImageView(moviePoster);
        posterView.setFitWidth(MOVIE_CARD_WIDTH);
        posterView.setFitHeight(MOVIE_CARD_HEIGHT);

        Rectangle roundedRectangle = new Rectangle(MOVIE_CARD_WIDTH, MOVIE_CARD_HEIGHT);
        roundedRectangle.setArcWidth(20);
        roundedRectangle.setArcHeight(20);
        posterView.setClip(roundedRectangle);

        return posterView;
    }

    private StackPane createFavoriteIcon(Movie movie, int userId) {
        Label favoriteIcon = new Label("♥");
        boolean isFavorite = isMovieFavorite(userId, movie.getId());
        favoriteIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: " + (isFavorite ? "red" : "gray") + ";");
        favoriteIcon.setOnMouseClicked(event -> toggleFavorite(userId, movie.getId(), favoriteIcon));

        Rectangle background = new Rectangle(30, 30);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setFill(Color.color(0, 0, 0, 0.6));

        return new StackPane(background, favoriteIcon);
    }

    private boolean isMovieFavorite(int userId, int movieId) {
        try {
            List<Integer> favoriteMovies = UserJsonHandler.getFavoriteShows(userId);
            return favoriteMovies.contains(movieId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void toggleFavorite(int userId, int movieId, Label favoriteIcon) {
        try {
            // Toggle favorite status
            UserJsonHandler.addFavoriteShow(userId, movieId);

            // Update the favorite icon color
            boolean isFavorite = isMovieFavorite(userId, movieId);
            favoriteIcon.setStyle("-fx-text-fill: " + (isFavorite ? "red" : "gray") + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
