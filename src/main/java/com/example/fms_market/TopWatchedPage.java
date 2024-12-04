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

public class  TopWatchedPage{

    private static final double MOVIE_CARD_WIDTH = 200;
    private static final double MOVIE_CARD_HEIGHT =282.94;

    public TopWatchedPage(User user, Stage stage) {
        try {
            Banner.setCurrentUser(user);

            // Fetch Recent Movies
            List<Movie> allMovies = ShowJsonHandler.readMovies();
            List<Movie> TopWatchedMovies = DisplayTopWatched(allMovies);

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
            scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), TopWatchedMovies, user.getId()));
            adjustLayout(showContainer, stageWidth, TopWatchedMovies, user.getId());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustLayout(GridPane showContainer, double width, List<Movie> TopWatchedMovies, int userId) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : TopWatchedMovies) {
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

    public List<Movie> DisplayTopWatched(List<Movie> movies) {
        List<Integer> views = new ArrayList<>();
        List<Movie> MostViews = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            views.add(movies.get(i).getViews());
        }
        Collections.sort(views);
        for (int i = views.size() - 1; i > views.size() - 6; i--) {
            for (int j = 0; j < movies.size(); j++) {
                if (movies.get(j).getViews() == views.get(i))
                    MostViews.add(movies.get(j));
            }
        }
        return MostViews;
    }
}
