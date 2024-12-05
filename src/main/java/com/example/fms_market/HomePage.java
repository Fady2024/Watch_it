package com.example.fms_market;

import javafx.scene.control.ContentDisplay;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static com.example.fms_market.ShowCardUtil.SHOW_CARD_WIDTH;

public class HomePage {

    private double startX;

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
        showContainer.setPadding(new Insets(20));
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

        Label mostPopularLabel = new Label("Most Popular Show");
        mostPopularLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
        showContainer.add(mostPopularLabel, 0, 0, columns, 1);

        VBox recentMoviesBox = new VBox(10);
        recentMoviesBox.setPadding(new Insets(10));
        recentMoviesBox.setStyle("-fx-background-color: #444444; -fx-border-radius: 20; -fx-background-radius: 20; -fx-border-color: white; -fx-border-width: 2px;");
        recentMoviesBox.setPrefWidth(width - 60);
        recentMoviesBox.setPrefHeight(200);

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");
        Label descriptionLabel = new Label();
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Button watchButton = new Button("WATCH");
        watchButton.setStyle("-fx-background-color: linear-gradient(to right, #4b0082, #8a2be2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-border-radius: 20;");
        watchButton.setGraphic(new Label("▶"));
        watchButton.setContentDisplay(ContentDisplay.LEFT);

        Button infoButton = new Button("Info");
        infoButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 1px; -fx-background-radius: 20; -fx-border-radius: 20;");
        infoButton.setGraphic(new Label("ℹ"));
        infoButton.setContentDisplay(ContentDisplay.LEFT);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(watchButton, infoButton);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);

        VBox.setMargin(buttonBox, new Insets(40, 0, 0, 0));

        recentMoviesBox.getChildren().addAll(titleLabel, descriptionLabel, buttonBox);
        showContainer.add(recentMoviesBox, 0, 1, columns, 1);

        int column = 0;
        int row = 2;

        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, stage, () -> {});
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }

        final int[] currentIndex = {0};
        updateMovieInfo(titleLabel, descriptionLabel, recentMovies, currentIndex[0]); // Initialize with the first movie

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            updateMovieInfo(titleLabel, descriptionLabel, recentMovies, currentIndex[0]);
            currentIndex[0] = (currentIndex[0] + 1) % recentMovies.size();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        recentMoviesBox.setOnMousePressed(event -> {
            timeline.pause();
            startX = event.getSceneX();
        });

        recentMoviesBox.setOnMouseReleased(event -> {
            double deltaX = event.getSceneX() - startX;

            if (deltaX > 0) {
                currentIndex[0] = (currentIndex[0] - 1 + recentMovies.size()) % recentMovies.size();
            } else if (deltaX < 0) {
                currentIndex[0] = (currentIndex[0] + 1) % recentMovies.size();
            }

            updateMovieInfo(titleLabel, descriptionLabel, recentMovies, currentIndex[0]);
            timeline.play();
        });

        showContainer.setOnMouseReleased(event -> {
            timeline.play();
        });
    }

    private void updateMovieInfo(Label titleLabel, Label descriptionLabel, List<Movie> recentMovies, int currentIndex) {
        Movie currentMovie = recentMovies.get(currentIndex);
        titleLabel.setText(currentMovie.getTitle());
        descriptionLabel.setText(currentMovie.getDescription());
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
}