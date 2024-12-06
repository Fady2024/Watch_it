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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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

        Text mostWatchedShow = new Text("Most Watched Show");
        mostWatchedShow.setFont(Font.font("Tahoma", 20));
        mostWatchedShow.setStyle("-fx-fill: white;");

        VBox recentMoviesBox = new VBox(10);
        recentMoviesBox.setPadding(new Insets(10));
        recentMoviesBox.setStyle("-fx-background-color:  #1c1c1c;");
        recentMoviesBox.setPrefWidth(width - 60);
        recentMoviesBox.setPrefHeight(200);

        Text mostWatchedShowTitle = new Text();
        mostWatchedShowTitle.setFont(Font.font("Georgia", 60));
        mostWatchedShowTitle.setStyle("-fx-fill: white;");

        Text mostWatchedShowDesc = new Text();
        mostWatchedShowDesc.setFont(Font.font("Tahoma", 18));
        mostWatchedShowDesc.setStyle("-fx-fill: #b1b1b1;");
        mostWatchedShowDesc.setWrappingWidth(((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth())-600); // Adjust width as needed
        Button Watch = new Button("WATCH");
        Watch.setStyle("-fx-background-color: #8E0D7D; -fx-text-fill: black; -fx-font-size: 18px;");
        Watch.setPrefSize(110, 50); // Set button size

        Rectangle roundedRectangle = new Rectangle(110,40);
        roundedRectangle.setArcWidth(40);
        roundedRectangle.setArcHeight(40);
        Watch.setClip(roundedRectangle);

        Button Info = new Button("Info");
        Info.setStyle("-fx-background-color: White; -fx-text-fill: black; -fx-font-size: 18px; -fx-border-radius: 80px;");
        Info.setPrefSize(110, 50); // Set button size

        Rectangle roundedRectangle1 = new Rectangle(110,40);
        roundedRectangle1.setArcWidth(40);
        roundedRectangle1.setArcHeight(40);
        Info.setClip(roundedRectangle1);

        HBox buttonContainer = new HBox(10); // Spacing of 10 pixels between the buttons
        buttonContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
        buttonContainer.getChildren().addAll(Watch, Info); // Add buttons to the HBox

        // Add the HBox to the GridPane in the same column
        Text RecentMovies = new Text("Recent Movies");
        RecentMovies.setFont(Font.font("Tahoma", 20));
        RecentMovies.setStyle("-fx-fill: white;");

        // Add the HBox to the GridPane in the same column
        Text RecentSeries = new Text("Recent Series");
        RecentSeries.setFont(Font.font("Tahoma", 20));
        RecentSeries.setStyle("-fx-fill: white;");
        int column = 0;
        int row = 0;

        recentMoviesBox.getChildren().addAll(mostWatchedShow, mostWatchedShowTitle, mostWatchedShowDesc, buttonContainer);
        showContainer.add(recentMoviesBox, column, row);

        showContainer.setHgap(27); // Horizontal gap between cells
        showContainer.setVgap(20); // Vertical gap between cells
        showContainer.add(RecentMovies, column, row + 3);
        showContainer.add(RecentSeries, column, row + 6);

        HBox showCardContainer = new HBox(30); // Spacing of 10 pixels between the buttons
        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, stage, () -> {});
            showCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27); // Horizontal gap between cells
            showContainer.setVgap(20); // Vertical gap between cells
            //column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
        showContainer.add(showCardContainer, column, row + 5);
        final int[] currentIndex = {0};
        updateMovieInfo(mostWatchedShowTitle, mostWatchedShowDesc, recentMovies, currentIndex[0]); // Initialize with the first movie

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            updateMovieInfo(mostWatchedShowTitle, mostWatchedShowDesc, recentMovies, currentIndex[0]);
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

            updateMovieInfo(mostWatchedShowTitle, mostWatchedShowDesc, recentMovies, currentIndex[0]);
            timeline.play();
        });

        showContainer.setOnMouseReleased(event -> {
            timeline.play();
        });
    }

    private void updateMovieInfo(Text titleLabel, Text descriptionLabel, List<Movie> recentMovies, int currentIndex) {
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