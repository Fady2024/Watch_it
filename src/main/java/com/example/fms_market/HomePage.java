package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import static com.example.fms_market.ShowCardUtil.SHOW_CARD_WIDTH;

public class HomePage {

    public HomePage(User user, Stage stage) {
        Banner.setCurrentUser(user);

        // Fetch Recent Movies
        List<Movie> allMovies;
        try {
            allMovies = ShowJsonHandler.readMovies();
        } catch (IOException e) {
            e.printStackTrace();
            allMovies = new ArrayList<>();
        }
        List<Movie> recentMovies = DisplayRecentMovies(allMovies);

        // GridPane to display shows
        GridPane showContainer = new GridPane();
        showContainer.setPadding(new Insets(94));
        showContainer.setHgap(27);
        showContainer.setVgap(27);
        showContainer.setAlignment(Pos.TOP_LEFT);
        showContainer.setStyle("-fx-background-color: #1c1c1c;");

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Home"));
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
        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), recentMovies, user, stage));
        adjustLayout(showContainer, stageWidth, recentMovies, user, stage);

    }

    private void adjustLayout(GridPane showContainer, double width, List<Movie> recentMovies, User user, Stage stage) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, stage,() -> {});
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    public List<Movie> DisplayRecentMovies(List<Movie> movies) {
        List<Date> dates = new ArrayList<>();
        Set<Movie> recentMovies = new HashSet<>();
        for (Movie movie : movies) {
            dates.add(movie.getDate());
        }
        Collections.sort(dates);
        for (int i = dates.size() - 1; i >= Math.max(0, dates.size() - 2); i--) {
            for (Movie movie : movies) {
                if (movie.getDate().equals(dates.get(i))) {
                    recentMovies.add(movie);
                }
            }
        }
        return new ArrayList<>(recentMovies);
    }

    public List<Series> DisplayRecentSeries(List<Series> series) {
        List<Date> dates = new ArrayList<>();
        List<Series> recentSeries = new ArrayList<>();
        for (Series s : series) {
            dates.add(s.getDate());
        }
        Collections.sort(dates);
        for (int i = dates.size() - 1; i >= Math.max(0, dates.size() - 6); i--) {
            for (Series s : series) {
                if (s.getDate().equals(dates.get(i))) {
                    recentSeries.add(s);
                }
            }
        }
        return recentSeries;
    }

    /*
    private StackPane createRatingIcon(Show show, int userId) {
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
                UserJsonHandler.removeTopRatedShow(userId, showId);
            } else {
                UserJsonHandler.addTopRatedShow(userId, showId);
            }

            boolean isTopRated = isShowTopRated(userId, showId);
            topRatedIcon.setStyle("-fx-font-size: 24px; -fx-text-fill: " + (isTopRated ? "gold" : "gray") + ";");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}