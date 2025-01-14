package com.example.fms_market.pages;

import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.util.Calculate_Rating;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
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

import static com.example.fms_market.util.ShowCardUtil.createShowCard;

public class Top_Rated {

    public Top_Rated(User user, Stage stage) throws IOException {
        VBox mainContainer = new VBox();
        mainContainer.setSpacing(20);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: #1c1c1c;");

        Calculate_Rating ratingCalculator = new Calculate_Rating();
        List<Show> movies = new ArrayList<>(ShowJsonHandler.readMovies());
        List<Show> series = new ArrayList<>(ShowJsonHandler.readSeries());

        List<Show> topMovies = ratingCalculator.getTopRatedShows(movies, movies.size());
        List<Show> topSeries = ratingCalculator.getTopRatedShows(series, series.size());
        mainContainer.getChildren().add(Categories(LanguageManager.getLanguageBasedString("Filme","Movies")
                , topMovies, user, stage));
        mainContainer.getChildren().add(Categories(        LanguageManager.getLanguageBasedString("Serie","Series")
                , topSeries, user, stage));
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1c1c1c; -fx-background-color: #1c1c1c; -fx-border-color: transparent;");

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Top Rated", "TopRatedPage"));
        layout.setCenter(scrollPane);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Top-Bewertungen","Top Rated"));
        stage.show();
    }

    private VBox Categories(String title, List<Show> shows, User user, Stage stage) {
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

    private void populateShowGrid(GridPane gridPane, List<Show> shows, User user, Stage stage) {
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
}
