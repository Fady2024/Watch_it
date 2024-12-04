package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Top_Rated {
    private static final double SHOW_CARD_WIDTH = 150;
    private static final double SHOW_CARD_HEIGHT = 280;

    public Top_Rated(User user, Stage stage) throws IOException {
        Calculate_Rating ratingCalculator = new Calculate_Rating();
        List<Show> allShows = new ArrayList<>();
        allShows.addAll(ShowJsonHandler.readMovies());
        allShows.addAll(ShowJsonHandler.readSeries());

        List<Show> topShows = ratingCalculator.getTopRatedShows(allShows, allShows.size());

        GridPane showContainer = new GridPane();
        showContainer.setStyle("-fx-background-color: #1c1c1c;");
        showContainer.setHgap(20);
        showContainer.setVgap(20);
        showContainer.setPadding(new Insets(20));

        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Top Rated Shows"));
        layout.setCenter(showContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Top Rated Shows");
        stage.show();

        scene.widthProperty().addListener((_, _, newValue) ->
                adjustLayout(showContainer, newValue.doubleValue(), topShows, user)
        );
        adjustLayout(showContainer, stageWidth, topShows, user);
    }

    private void adjustLayout(GridPane showContainer, double width, List<Show> topShows, User user) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Show show : topShows) {
            VBox showCard = createShowCard(show);
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createShowCard(Show show) {
        VBox showCard = new VBox(5);
        showCard.setAlignment(Pos.TOP_CENTER);

        Image showPoster = new Image(show.getPoster(), false);
        Rectangle roundedRectangle = new Rectangle(SHOW_CARD_WIDTH, SHOW_CARD_HEIGHT);
        roundedRectangle.setArcWidth(20);
        roundedRectangle.setArcHeight(20);

        ImageView posterView = new ImageView(showPoster);
        posterView.setFitWidth(SHOW_CARD_WIDTH);
        posterView.setFitHeight(SHOW_CARD_HEIGHT);
        posterView.setClip(roundedRectangle);

        Label title = new Label(show.getTitle());
        title.setFont(Font.font("Arial", 14));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(SHOW_CARD_WIDTH);
        title.setWrapText(true);

        Label imdbLabel = new Label("IMDb: " + show.getImdb_score());
        imdbLabel.setFont(Font.font("Arial", 12));
        imdbLabel.setTextFill(Color.LIGHTGRAY);

        Label ratingLabel = new Label("Rating: " + new Calculate_Rating().calculateAverageRating(show));
        ratingLabel.setFont(Font.font("Arial", 12));
        ratingLabel.setTextFill(Color.LIGHTGRAY);

        showCard.getChildren().addAll(posterView, title, imdbLabel, ratingLabel);

        return showCard;
    }
}