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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomePage {

    private static final double MOVIE_CARD_WIDTH = 200;
    private static final double MOVIE_CARD_HEIGHT =282.94;

    public HomePage(User user, Stage stage) {
        try {
            Banner.setCurrentUser(user);

            // Fetch Recent Movies
            List<Movie> allMovies = ShowJsonHandler.readMovies();
            List<Movie> recentMovies = DisplayRecentMovies(allMovies);

            // GridPane to display shows
            GridPane showContainer = new GridPane();
            showContainer.setPadding(new Insets(94));
            showContainer.setHgap(27);
            showContainer.setVgap(27);
            showContainer.setAlignment(Pos.TOP_LEFT);
            showContainer.setStyle("-fx-background-color: #1c1c1c;");

            BorderPane layout = new BorderPane();
            layout.setTop(Banner.getBanner(stage, "Home")); // Use the Banner class here and pass the current page
            layout.setCenter(showContainer);

            // Adjust stage dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int stageWidth = (int) screenSize.getWidth();
            int stageHeight = (int) (screenSize.getHeight() / 1.1);
            Scene scene = new Scene(layout, stageWidth, stageHeight);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.show();

            // Dynamically adjust layout
            scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), recentMovies, user.getId()));
            adjustLayout(showContainer, stageWidth, recentMovies, user.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustLayout(GridPane showContainer, double width, List<Movie> recentMovies, int userId) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : recentMovies) {
            VBox showCard = createShowCard(movie, userId);
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createShowCard(Show show, int userId) {
        VBox showCard = new VBox(5);
        showCard.setAlignment(Pos.TOP_CENTER);

        ImageView posterView = createPosterView(show.getPoster());
        Label title = new Label(show.getTitle());
        title.setFont(Font.font("Tahoma", 14));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER_LEFT);
        title.setWrapText(true);
        title.setMaxWidth(MOVIE_CARD_WIDTH);

        StackPane posterContainer = new StackPane(posterView, createFavoriteIcon(show, userId));
        showCard.getChildren().addAll(posterContainer, title);

        return showCard;
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

    private StackPane createFavoriteIcon(Show show, int userId) {
        Label favoriteIcon = new Label("♥");
        boolean isFavorite = isShowFavorite(userId, show.getId());
        favoriteIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: " + (isFavorite ? "red" : "gray") + ";");
        favoriteIcon.setOnMouseClicked(event -> toggleFavorite(userId, show.getId(), favoriteIcon));

        Rectangle background = new Rectangle(30, 30);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setFill(Color.color(0, 0, 0, 0.6));

        return new StackPane(background, favoriteIcon);
    }

    private boolean isShowFavorite(int userId, int showId) {
        try {
            List<Integer> favoriteShows = UserJsonHandler.getFavoriteShows(userId);
            return favoriteShows.contains(showId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void toggleFavorite(int userId, int showId, Label favoriteIcon) {
        try {
            // Toggle favorite status
            UserJsonHandler.addFavoriteShow(userId, showId);

            // Update the favorite icon color
            boolean isFavorite = isShowFavorite(userId, showId);
            favoriteIcon.setStyle("-fx-text-fill: " + (isFavorite ? "red" : "gray") + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public List<Movie> DisplayRecentMovies(List<Movie> movies) {
        List<Date> dates = new ArrayList<>();
        List<Movie> recentMovies = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            dates.add(movies.get(i).getDate());
        }
        Collections.sort(dates);
        for (int i = dates.size() - 1; i > dates.size() - 6; i--) {
            for (int j = 0; j < movies.size(); j++) {
                if (movies.get(j).getDate() == dates.get(i))
                    recentMovies.add(movies.get(j));

            }
        }
    return recentMovies;
    }
    public List<Series> DisplayRecentSeries(List<Series> series) {
        List<Date> dates = new ArrayList<>();
        List<Series> recentSeries = new ArrayList<>();
        for (int i = 0; i < series.size(); i++) {
            dates.add(series.get(i).getDate());
        }
        Collections.sort(dates);
        for (int i = dates.size() - 1; i > dates.size() - 6; i--) {
            for (int j = 0; j < series.size(); j++) {
                if (series.get(j).getDate() == dates.get(i))
                    recentSeries.add(series.get(j));

            }
        }
        return recentSeries;
    }

    private StackPane createRatingIcon(Show show,int userId){
        Label Top_Rated = new Label("☆");
        boolean isTopRated = isShowTopRated(userId, show.getId());
        Top_Rated.setOnMouseClicked(event -> toggleTopRated(userId, show.getId(), Top_Rated));

        Top_Rated.setStyle("-fx-font-size: 24px; -fx-text-fill: " + (isTopRated ? "gold" : "gray") + ";");

        // Add the click event to toggle top-rated status
        Top_Rated.setOnMouseClicked(event -> toggleTopRated(userId, show.getId(), Top_Rated));

        Rectangle background = new Rectangle(30, 30);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setFill(Color.color(0, 0, 0, 0.6));

        return new StackPane(background, Top_Rated);
    }
    private boolean isShowTopRated(int userId, int showId) {
        try {
            List<Integer> topRatedShows = UserJsonHandler.getTopRatedShows(userId);
            return topRatedShows.contains(showId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void toggleTopRated(int userId, int showId, Label topRatedIcon) {
        try {
            if (isShowTopRated(userId, showId)) {
                UserJsonHandler.removeTopRatedShow(userId, showId);}
            else {UserJsonHandler.addTopRatedShow(userId, showId);}

            boolean isTopRated = isShowTopRated(userId, showId);
            topRatedIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: " + (isTopRated ? "gold" : "gray") + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
