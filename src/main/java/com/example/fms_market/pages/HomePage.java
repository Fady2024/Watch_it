package com.example.fms_market.pages;

import com.example.fms_market.admin.AddShow;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.Movie;
import com.example.fms_market.model.Series;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static com.example.fms_market.util.ShowCardUtil.SHOW_CARD_WIDTH;

public class HomePage {

    private double startX;

    public HomePage(User user, Stage stage) {

        Banner.setCurrentUser(user);

        List<Movie> allMovies;
        try {
            allMovies = ShowJsonHandler.readMovies();
        } catch (IOException e) {
            e.printStackTrace();
            allMovies = new ArrayList<>();
        }
        List<Series> allSeries;
        try {
            allSeries = ShowJsonHandler.readSeries();
        } catch (IOException e) {
            e.printStackTrace();
            allSeries = new ArrayList<>();
        }

        List<Movie> recentMovies = DisplayRecentMovies(allMovies);
        List<Series> recentSeries = DisplayRecentSeries(allSeries);

        GridPane showContainer = new GridPane();
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(27);
        showContainer.setVgap(27);
        showContainer.setAlignment(Pos.TOP_LEFT);
        showContainer.setStyle("-fx-background-color: #1c1c1c;");

        ScrollPane scrollPane = new ScrollPane(showContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1c1c1c; -fx-background-color: #1c1c1c; -fx-border-color: transparent;");

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, LanguageManager.getLanguageBasedString("Heim","Home")));
        layout.setCenter(scrollPane);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Heim","Home"));
        stage.show();

        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), recentMovies,recentSeries, user, stage));
        adjustLayout(showContainer, stageWidth, recentMovies,recentSeries, user, stage);

    }
    private void adjustLayout(GridPane showContainer, double width, List<Movie> recentMovies,List<Series> recentSeries, User user, Stage stage) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        HBox adminBox = new HBox(10);

        //Edit Show Button
        Button editButton =LanguageManager.createLanguageButton("Show bearbeiten","Edit Show","18","black");
        editButton.setPrefSize(150, 50); // Set button size
        editButton.setPadding(new Insets(5,10,10,10));
        editButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(),18));
        editButton.setStyle("-fx-background-color: #BBAAD2; -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;-fx-padding: 0px 5px;");

        Rectangle roundedRectangle2 = new Rectangle(150,50);
        roundedRectangle2.setArcWidth(40);
        roundedRectangle2.setArcHeight(40);
        editButton.setClip(roundedRectangle2);

        //Add Show Button
        Button addButton =LanguageManager.createLanguageButton("Show hinzufügen","Add Show","18","black");
        addButton.setPrefSize(150, 50); // Set button size
        addButton.setPadding(new Insets(5,10,10,10));
        addButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(),18));
        addButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #BBAAD2, #522193); -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;");

        addButton.setOnMouseClicked( e-> {
            try {
                new AddShow(user,stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Rectangle roundedRectangle3 = new Rectangle(150,50);
        roundedRectangle3.setArcWidth(40);
        roundedRectangle3.setArcHeight(40);
        addButton.setClip(roundedRectangle3);

        //Delete Show Button
        Button deleteButton =LanguageManager.createLanguageButton("Show löschen","Delete Show","18","black");
        deleteButton.setPrefSize(150, 50); // Set button size
        deleteButton.setPadding(new Insets(5,10,10,10));
        deleteButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(),18));
        deleteButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #522193, #9F1F93); -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;-fx-padding: 0px 5px;");

        Rectangle roundedRectangle4 = new Rectangle(150,50);
        roundedRectangle4.setArcWidth(40);
        roundedRectangle4.setArcHeight(40);
        deleteButton.setClip(roundedRectangle4);

        adminBox.setAlignment(Pos.TOP_RIGHT);
        adminBox.getChildren().addAll(editButton,addButton,deleteButton);

        Text mostWatchedShow = LanguageManager.createLanguageText("Meistgesehene Filme","Most Watched Movies","20","white");

        mostWatchedShow.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));

        VBox recentMoviesBox = new VBox(30);
        recentMoviesBox.setPadding(new Insets(10));
        recentMoviesBox.setStyle("-fx-background-color:  #1c1c1c;");
        recentMoviesBox.setPrefWidth(width - 60);
        recentMoviesBox.setPrefHeight(200);

        Text mostWatchedShowTitle = new Text();
        mostWatchedShowTitle.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Italiana-Regular.ttf")).toString(),60));
        mostWatchedShowTitle.setStyle("-fx-fill: white;");

        Text mostWatchedShowDesc = new Text();
        mostWatchedShowDesc.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),18));
        mostWatchedShowDesc.setStyle("-fx-fill: #b1b1b1;");
        mostWatchedShowDesc.setWrappingWidth(((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth())-600); // Adjust width as needed

        Button Watch =LanguageManager.createLanguageButton("▶ Betrachten","▶ Watch","20","black");
        Watch.setPrefSize(110, 50); // Set button size
        Watch.setPadding(new Insets(5,10,10,10));
        Watch.setStyle("-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 18px;");

        Rectangle roundedRectangle = new Rectangle(110,40);
        roundedRectangle.setArcWidth(40);
        roundedRectangle.setArcHeight(40);
        Watch.setClip(roundedRectangle);

        Button Info = new Button("\uD83D\uDEC8 Info");
        Info.setStyle("-fx-background-color: White; -fx-text-fill: black;-fx-border-radius: 80px; -fx-font-size: 20px;");
        Info.setPrefSize(110, 50); // Set button size
        Info.setPadding(new Insets(5,10,10,10));

        Rectangle roundedRectangle1 = new Rectangle(110,40);
        roundedRectangle1.setArcWidth(40);
        roundedRectangle1.setArcHeight(40);
        Info.setClip(roundedRectangle1);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.getChildren().addAll(Watch, Info);

        Text RecentMovies = LanguageManager.createLanguageText("Aktuelle Filme","Recent Movies","20","white");
        RecentMovies.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));

        Text RecentSeries = LanguageManager.createLanguageText("Aktuelle Serien","Recent Series","20","white");
        RecentSeries.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));
        int column = 0;
        int row = 0;
        if(user.getRole().equals("Admin")||user.getRole().equals("admin"))
            recentMoviesBox.getChildren().addAll(adminBox,mostWatchedShow, mostWatchedShowTitle, mostWatchedShowDesc, buttonContainer);
        else
            recentMoviesBox.getChildren().addAll(mostWatchedShow, mostWatchedShowTitle, mostWatchedShowDesc, buttonContainer);
        showContainer.add(recentMoviesBox, column, row);

        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.add(RecentMovies, column, row + 3);
        showContainer.add(RecentSeries, column, row + 6);

        HBox showMoviesCardContainer = new HBox(30);
        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, stage, () -> {});
            showMoviesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showMoviesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
        }
        showContainer.add(showMoviesCardContainer, column, row + 5);

        HBox showSeriesCardContainer = new HBox(30);
        for (Series series : recentSeries) {
            VBox showCard = ShowCardUtil.createShowCard(series, user, stage, () -> {});
            showSeriesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showSeriesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
        }
        showContainer.add(showSeriesCardContainer, column, row + 8);
        final int[] currentIndex = {0};
        updateMovieInfo(mostWatchedShowTitle, mostWatchedShowDesc, recentMovies, currentIndex[0]); // Initialize with the first movie

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), _ -> {
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

        showContainer.setOnMouseReleased(_ -> timeline.play());
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
        for (int i = dates.size() - 1; i >= Math.max(0, dates.size() - 6); i--) {
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
