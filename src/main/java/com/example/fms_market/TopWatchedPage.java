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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.example.fms_market.ShowCardUtil.SHOW_CARD_WIDTH;

public class TopWatchedPage {


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
            layout.setTop(Banner.getBanner(stage, "Top Watched")); // Use the Banner class here and pass the current page
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
            scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), TopWatchedMovies, user));
            adjustLayout(showContainer, stageWidth, TopWatchedMovies, user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustLayout(GridPane showContainer, double width, List<Movie> TopWatchedMovies, User user) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Movie movie : TopWatchedMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, (Stage) showContainer.getScene().getWindow(),() -> {});
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    public List<Movie> DisplayTopWatched(List<Movie> movies) {
        List<Integer> views = new ArrayList<>();
        List<Movie> MostViews = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            views.add(movies.get(i).getViews());
        }
        Collections.sort(views);
        for (int i = views.size() - 1; i > views.size() - 2; i--) {
            for (int j = 0; j < movies.size(); j++) {
                if (movies.get(j).getViews() == views.get(i))
                    MostViews.add(movies.get(j));
            }
        }
        return MostViews;
    }
}