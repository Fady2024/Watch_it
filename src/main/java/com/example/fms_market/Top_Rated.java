package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.io.IOException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Top_Rated {
    private static final double MOVIE_CARD_WIDTH = 150;
    private static final double MOVIE_CARD_HEIGHT = 280;

    public Top_Rated(User user, Stage stage) throws IOException {
        Calculate_Rating top = new Calculate_Rating();
        List<Movie> movies = new ArrayList<>();
        List<Series> series = new ArrayList<>();
        List<Show> Top_Movies = new ArrayList<>(top.getTopRatedMovies(movies, 10)); // Assume m = 10


        GridPane showContainer = new GridPane();
        showContainer.setStyle("-fx-background-color: #1c1c1c;");
        showContainer.setHgap(20);
        showContainer.setVgap(20);
        showContainer.setPadding(new Insets(20));

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Top Rated"));
        layout.setCenter(showContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Top Rated");
        stage.show();

        // Adjust layout dynamically
        scene.widthProperty().addListener((observable, oldValue, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), Top_Movies, user));
        adjustLayout(showContainer, stageWidth, Top_Movies, user);
    }

    private void adjustLayout(GridPane showContainer, double width, List<Show> Top_Movies, User user) {
        int columns = (int) (width / (MOVIE_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Show show : Top_Movies) {
            VBox showCard = new VBox(5);
            showCard.setAlignment(Pos.TOP_CENTER);

            // Load the actual show poster image
            Image showPoster = new Image(show.getPoster(), false);
            Rectangle roundedRectangle = new Rectangle(MOVIE_CARD_WIDTH, MOVIE_CARD_HEIGHT);
            roundedRectangle.setArcWidth(20);
            roundedRectangle.setArcHeight(20);
            roundedRectangle.setStyle("-fx-background-color: #1c1c1c;");

            ImageView posterView = new ImageView(showPoster);
            posterView.setFitWidth(MOVIE_CARD_WIDTH);
            posterView.setFitHeight(MOVIE_CARD_HEIGHT);
            posterView.setClip(roundedRectangle);

            Label Rated_Icon = new Label("☆");
            Rated_Icon.setStyle("-fx-font-size: 30px; -fx-text-fill: red; -fx-font-weight: bold;");
            Rated_Icon.setPadding(new Insets(5));
            Rated_Icon.setTranslateY(10);

            Rectangle Star_Background = new Rectangle(30, 30);
            Star_Background.setArcWidth(15);
            Star_Background.setArcHeight(15);
            Star_Background.setFill(Color.color(1, 1, 1, 0.4));

            StackPane StarContainer = new StackPane(Star_Background, Rated_Icon);
            StarContainer.setAlignment(Pos.TOP_LEFT);
            StarContainer.setPadding(new Insets(5));

            StackPane posterContainer = new StackPane(posterView, StarContainer);
            posterContainer.setAlignment(Pos.TOP_LEFT);

            Label title = new Label(show.getTitle());
            title.setFont(Font.font("Arial", 14));
            title.setTextFill(Color.WHITE);
            title.setAlignment(Pos.CENTER);
            title.setMaxWidth(MOVIE_CARD_WIDTH);
            title.setWrapText(true);

            Rated_Icon.setOnMouseClicked(event -> {
                try {
                    UserJsonHandler.removeTopRatedShow(user.getId(), show.getId());
                    Rated_Icon.setText("☆");
                    showContainer.getChildren().remove(showCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Show IMDb rating (common for both Movie and Series)
            Label imdbLabel = new Label("IMDb: " + show.getImdb_score());
            imdbLabel.setFont(Font.font("Arial", 12));
            imdbLabel.setTextFill(Color.LIGHTGRAY);

            if (show instanceof Movie) {
                Movie movie = (Movie) show;
                Label movieLabel = new Label("Movie ID: " + movie.getMovie_id());
                movieLabel.setFont(Font.font("Arial", 12));
                movieLabel.setTextFill(Color.LIGHTGRAY);
                showCard.getChildren().addAll(posterContainer, title, imdbLabel, movieLabel);
            } else if (show instanceof Series) {
                Series series = (Series) show;
                Label seriesLabel = new Label("Episodes: " + series.getSeriesEp());
                seriesLabel.setFont(Font.font("Arial", 12));
                seriesLabel.setTextFill(Color.LIGHTGRAY);
                showCard.getChildren().addAll(posterContainer, title, imdbLabel, seriesLabel);
            }
            // Add showCard to the GridPane
            showContainer.add(showCard, column, row);
            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    public void getTopRatedMovies(List<Movie> movies, int i) {
    }
}
