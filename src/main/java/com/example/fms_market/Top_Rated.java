package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.fms_market.ShowCardUtil.createShowCard;
import static com.example.fms_market.ShowCardUtil.SHOW_CARD_WIDTH;

public class Top_Rated {

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
        layout.setTop(Banner.getBanner(stage, "Top Rated"));
        layout.setCenter(showContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Top Rated");
        stage.show();

        scene.widthProperty().addListener((_, _, newValue) ->
                adjustLayout(showContainer, newValue.doubleValue(), topShows, user, stage)
        );
        adjustLayout(showContainer, stageWidth, topShows, user, stage);
    }

    private void adjustLayout(GridPane showContainer, double width, List<Show> topShows, User user, Stage stage) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        showContainer.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Show show : topShows) {
            VBox showCard = createShowCard(show, user, stage, () -> {});
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }
}