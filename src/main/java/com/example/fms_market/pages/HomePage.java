package com.example.fms_market.pages;

import com.example.fms_market.admin.AddShow;
import com.example.fms_market.admin.RemoveShowPage;
import com.example.fms_market.admin.UpdateShowPage;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.data.SubscriptionManager;
import com.example.fms_market.model.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.StageStyle;
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
        layout.setTop(Banner.getBanner(stage, LanguageManager.getLanguageBasedString("Heim", "Home"), "HomePage"));        layout.setCenter(scrollPane);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Heim","Home"));
        stage.show();

        Show show = recentMovies.isEmpty() ? null : recentMovies.get(0);

        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), recentMovies, recentSeries, user, stage, show));
        adjustLayout(showContainer, stageWidth, recentMovies, recentSeries, user, stage, show);
    }
    private void adjustLayout(GridPane showContainer, double width, List<Movie> recentMovies, List<Series> recentSeries, User user, Stage stage, Show show) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        HBox adminBox = new HBox(10);

        // Edit Show Button
        Button editButton = LanguageManager.createLanguageButton("Show bearbeiten", "Edit Show", "18", "black");
        editButton.setPadding(new Insets(5, 10, 10, 10));
        editButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(), 18));
        editButton.setStyle("-fx-background-color: #BBAAD2; -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;-fx-padding: 0px 5px;");
        Rectangle roundedRectangle2 = new Rectangle(150, 50);
        roundedRectangle2.setArcWidth(40);
        roundedRectangle2.setArcHeight(40);
        editButton.setClip(roundedRectangle2);
        editButton.setOnAction(e -> {
            try {
                new UpdateShowPage(user, stage);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Add Show Button
        Button addButton = LanguageManager.createLanguageButton("Show hinzufügen", "Add Show", "18", "black");
        addButton.setPadding(new Insets(5, 10, 10, 10));
        addButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(), 18));
        addButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #BBAAD2, #522193); -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;");
        addButton.setOnMouseClicked(e -> {
            try {
                new AddShow(user, stage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        Rectangle roundedRectangle3 = new Rectangle(150, 50);
        roundedRectangle3.setArcWidth(40);
        roundedRectangle3.setArcHeight(40);
        addButton.setClip(roundedRectangle3);

        // Delete Show Button
        Button deleteButton = LanguageManager.createLanguageButton("Show löschen", "Delete Show", "18", "black");
        deleteButton.setPadding(new Insets(5, 10, 10, 10));
        deleteButton.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Kufam-VariableFont_wght.ttf")).toString(), 18));
        deleteButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #522193, #9F1F93); -fx-text-fill: black; -fx-border-radius: 20px;-fx-background-radius:20px;-fx-padding: 0px 5px;");
        deleteButton.setPrefSize(150, 50);
        editButton.setPrefSize(150, 50);
        addButton.setPrefSize(150, 50);
        Rectangle roundedRectangle4 = new Rectangle(150, 50);
        roundedRectangle4.setArcWidth(40);
        roundedRectangle4.setArcHeight(40);
        deleteButton.setClip(roundedRectangle4);
        deleteButton.setOnMouseClicked(e -> {
            try {
                new RemoveShowPage(user, stage);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        adminBox.setAlignment(Pos.TOP_RIGHT);
        adminBox.getChildren().addAll(editButton, addButton, deleteButton);

        Text mostWatchedShow = LanguageManager.createLanguageText("Meistgesehene Filme", "Most Watched Movies", "20", "white");
        mostWatchedShow.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(), 20));

        StackPane recentMoviesBox = new StackPane();
        recentMoviesBox.setPadding(new Insets(10));
        recentMoviesBox.setStyle("-fx-background-color:  #1c1c1c;");
        recentMoviesBox.setPrefWidth(width - 60);
        recentMoviesBox.setPrefHeight(200);

        VBox contentBox = new VBox(30);
        contentBox.setPadding(new Insets(10));
        contentBox.setStyle("-fx-background-color: transparent;");

        Text mostWatchedShowTitle = new Text();
        mostWatchedShowTitle.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/Italiana-Regular.ttf")).toString(), 60));
        mostWatchedShowTitle.setStyle("-fx-fill: white;");

        Text mostWatchedShowDesc = new Text();
        mostWatchedShowDesc.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(), 18));
        mostWatchedShowDesc.setStyle("-fx-fill: #b1b1b1;");
        mostWatchedShowDesc.setWrappingWidth(((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()) - 600);

        final int[] currentIndex = {0};

        Button watchButton = LanguageManager.createLanguageButton("▶ Betrachten", "▶ Watch", "20", "black");
        watchButton.setOnAction(_ -> {
            SubscriptionManager subscriptionManager = new SubscriptionManager();
            Subscription subscription = SubscriptionManager.getSubscriptionByUserId(user.getId());
            if (subscription != null && subscription.canWatchMovie()) {
                subscription.watchMovie(recentMovies.get(currentIndex[0]).getTitle());
                new VideoPlayerFX(recentMovies.get(currentIndex[0]).getVideo(), recentMovies.get(currentIndex[0]).getId(), stage, user);
            } else {
                showSubscriptionExpiredPopup(stage, user);
            }
        });
        watchButton.setPrefSize(110, 50);
        watchButton.setPadding(new Insets(5, 10, 10, 10));
        watchButton.setStyle("-fx-background-color: linear-gradient(to bottom, #c9068d, #641271); -fx-text-fill: black; -fx-font-size: 18px;");
        Rectangle roundedRectangle = new Rectangle(110, 40);
        roundedRectangle.setArcWidth(40);
        roundedRectangle.setArcHeight(40);
        watchButton.setClip(roundedRectangle);

        Button infoButton = new Button("\uD83D\uDEC8 Info");
        infoButton.setOnAction(e -> new MoviePageFX(user, recentMovies.get(currentIndex[0]), stage));
        infoButton.setStyle("-fx-background-color: White; -fx-text-fill: black;-fx-border-radius: 80px; -fx-font-size: 20px;");
        infoButton.setPrefSize(110, 50);
        infoButton.setPadding(new Insets(5, 10, 10, 10));
        Rectangle roundedRectangle1 = new Rectangle(110, 40);
        roundedRectangle1.setArcWidth(40);
        roundedRectangle1.setArcHeight(40);
        infoButton.setClip(roundedRectangle1);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.getChildren().addAll(watchButton, infoButton);



        String imagePath = "/Posters/wallpaperflare.com_wallpaper (1).png";
        ImageView posterImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
        posterImageView.setPreserveRatio(true);
        posterImageView.setSmooth(true); // Enable smooth scaling for better visual quality

        // Bind the fitWidth of the ImageView to the width of the container
        posterImageView.fitWidthProperty().bind(recentMoviesBox.widthProperty());
        posterImageView.setFitHeight(330); // Optional: Set a desired height to maintain proportions

        // Create a Rectangle with rounded corners for clipping
        Rectangle clip = new Rectangle();
        clip.setArcWidth(30); // Set the arc width for rounded corners
        clip.setArcHeight(30); // Set the arc height for rounded corners

        // Bind the clip dimensions to the ImageView dimensions
        clip.widthProperty().bind(posterImageView.fitWidthProperty());
        clip.heightProperty().bind(posterImageView.fitHeightProperty());

        // Set the clip to the ImageView
        posterImageView.setClip(clip);
        if (user.getRole().equals("Admin") || user.getRole().equals("admin")) {
            contentBox.getChildren().addAll(adminBox, mostWatchedShow, mostWatchedShowTitle, mostWatchedShowDesc, buttonContainer);
            posterImageView.setTranslateY(65);
        } else {
            contentBox.getChildren().addAll(mostWatchedShow, mostWatchedShowTitle, mostWatchedShowDesc, buttonContainer);
            posterImageView.setTranslateY(-20);
        }
        posterImageView.setTranslateX(-10);
        recentMoviesBox.getChildren().clear(); // Clear any existing children
        recentMoviesBox.getChildren().addAll(posterImageView, contentBox);
        // Set alignment for the posterImageView to the left
        StackPane.setAlignment(posterImageView, Pos.TOP_LEFT);
        StackPane.setAlignment(contentBox, Pos.BOTTOM_CENTER);

        showContainer.getChildren().clear(); // Clear previous children to avoid duplicates
        showContainer.add(recentMoviesBox, 0, 0);

        Text recentMoviesText = LanguageManager.createLanguageText("Aktuelle Filme", "Recent Movies", "20", "white");
        recentMoviesText.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(), 20));

        Text recentSeriesText = LanguageManager.createLanguageText("Aktuelle Serien", "Recent Series", "20", "white");
        recentSeriesText.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(), 20));

        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.add(recentMoviesText, 0, 3);
        showContainer.add(recentSeriesText, 0, 6);

        HBox showMoviesCardContainer = new HBox(30);
        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createShowCard(movie, user, stage, () -> {});
            showMoviesCardContainer.getChildren().add(showCard);
            showMoviesCardContainer.setAlignment(Pos.CENTER_LEFT);
            showContainer.setHgap(27);
            showContainer.setVgap(20);
        }
        showContainer.add(showMoviesCardContainer, 0, 5);

        HBox showSeriesCardContainer = new HBox(30);
        for (Series series : recentSeries) {
            VBox showCard = ShowCardUtil.createShowCard(series, user, stage, () -> {});
            showSeriesCardContainer.getChildren().add(showCard);
            showSeriesCardContainer.setAlignment(Pos.CENTER_LEFT);
            showContainer.setHgap(27);
            showContainer.setVgap(20);
        }
        showContainer.add(showSeriesCardContainer, 0, 8);

        updateMovieInfo(mostWatchedShowTitle, mostWatchedShowDesc, recentMovies, currentIndex[0]);

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

    private void showSubscriptionExpiredPopup(Stage stage, User user) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.TRANSPARENT);
        popupStage.initOwner(stage);

        VBox popupContent = new VBox(20);
        popupContent.setPadding(new Insets(20));
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setStyle("-fx-background-color: rgba(51, 51, 51, 0.8); -fx-background-radius: 20;"); // Rounded corners

        // Centered Label with all text visible
        Label messageLabel = new Label("Your subscription has expired. Please renew your subscription to continue watching movies.");
        messageLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);
        messageLabel.setAlignment(Pos.CENTER);

        VBox messageContainer = new VBox(messageLabel);
        messageContainer.setAlignment(Pos.CENTER);

        Button renewButton = new Button("Renew Subscription");
        renewButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        renewButton.setStyle("-fx-background-color: #c9068d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;");
        renewButton.setOnAction(_ -> {
            new subscription_page(stage, user);
            popupStage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        cancelButton.setStyle("-fx-background-color: #c9068d; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 15px; -fx-border-radius: 15px; -fx-cursor: hand;");
        cancelButton.setOnAction(_ -> popupStage.close());

        popupContent.getChildren().addAll(messageContainer, renewButton, cancelButton);
        Scene popupScene = new Scene(popupContent, 400, 250);
        popupScene.setFill(Color.TRANSPARENT);
        popupStage.setScene(popupScene);
        popupStage.show();
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
