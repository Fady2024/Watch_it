package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.fms_market.ShowCardUtil.createShowCard;

public class TopWatchedPage {
    private final VBox mainContainer;

    public TopWatchedPage(User user, Stage stage) {
        this.mainContainer = new VBox();
        this.mainContainer.setSpacing(20);
        this.mainContainer.setPadding(new Insets(20));
        this.mainContainer.setStyle("-fx-background-color: #1c1c1c;");

        try {
            List<Movie> allMovies = ShowJsonHandler.readMovies();
            List<Series> allSeries = ShowJsonHandler.readSeries();

            List<Movie> topWatchedMovies = displayTopWatchedMovies(allMovies);
            List<Show> topWatchedSeries = displayTopWatchedSeries(allSeries);

            mainContainer.getChildren().add(Categories("Movies", topWatchedMovies, user, stage));
            mainContainer.getChildren().add(Categories("Series", topWatchedSeries, user, stage));

            ScrollPane scrollPane = new ScrollPane(mainContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: #1c1c1c; -fx-border-color: transparent;");

            BorderPane layout = new BorderPane();
            layout.setTop(Banner.getBanner(stage, "Top Watched"));
            layout.setCenter(scrollPane);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int stageWidth = (int) screenSize.getWidth();
            int stageHeight = (int) (screenSize.getHeight() / 1.1);
            Scene scene = new Scene(layout, stageWidth, stageHeight);
            stage.setScene(scene);
            stage.setTitle("Top Watched");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox Categories(String title, List<? extends Show> shows, User user, Stage stage) {
        VBox categoryContainer = new VBox();
        categoryContainer.setSpacing(10);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

        GridPane showGrid = createGridPane();
        populateShowGrid(showGrid, shows, user, stage);

        categoryContainer.getChildren().addAll(titleLabel, showGrid);
        return categoryContainer;
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(27);
        gridPane.setVgap(27);
        gridPane.setAlignment(Pos.TOP_LEFT);
        return gridPane;
    }

    private void populateShowGrid(GridPane gridPane, List<? extends Show> shows, User user, Stage stage) {
        int columns = 4;
        int column = 0;
        int row = 0;

        for (Show show : shows) {
            VBox showCard = createShowCard(show, user, stage, () -> {});
            gridPane.add(showCard, column, row);
            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    public List<Movie> displayTopWatchedMovies(List<Movie> movies) {
        movies.sort((m1, m2) -> Integer.compare(m2.getViews(), m1.getViews()));
        return movies.subList(0, Math.min(10, movies.size()));
    }

    public List<Show> displayTopWatchedSeries(List<Series> series) {
        series.sort((s1, s2) -> Integer.compare(s2.getViews(), s1.getViews()));
        return new ArrayList<>(series.subList(0, Math.min(10, series.size())));
    }
}
