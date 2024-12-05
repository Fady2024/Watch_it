// src/main/java/com/example/fms_market/FavoritesPage.java
package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.fms_market.ShowCardUtil.SHOW_CARD_WIDTH;

public class FavoritesPage {

    private GridPane showContainer;
    private BorderPane layout;
    private User user;
    private Stage stage;
    private List<Show> favoriteShows;

    public FavoritesPage(User user, Stage stage) throws IOException {
        this.user = user;
        this.stage = stage;

        showContainer = new GridPane();
        showContainer.setStyle("-fx-background-color: #2B2B2B;");
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(20);
        showContainer.setVgap(20);

        layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "Favorites"));
        layout.setCenter(showContainer);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Favorites");
        stage.show();

        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(newValue.doubleValue()));
        loadFavorites();
    }

    private void loadFavorites() throws IOException {
        List<Integer> favoriteShowIds = UserJsonHandler.getFavoriteShows(user.getId());
        List<Show> allShows = ShowJsonHandler.readShows();
        favoriteShows = allShows.stream()
                .filter(show -> favoriteShowIds.contains(show.getId()))
                .collect(Collectors.toList());

        adjustLayout(stage.getScene().getWidth());

        if (favoriteShows.isEmpty()) {
            showEmptyMessage();
        }
    }

    private void adjustLayout(double width) {
        showContainer.getChildren().clear();

        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        int column = 0;
        int row = 0;

        for (Show show : favoriteShows) {
            VBox showCard = ShowCardUtil.createShowCard(show, user, stage, this::refreshFavorites);
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private void showEmptyMessage() {
        Label emptyLabel = new Label("No favorite shows found.");
        emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-alignment: center;");
        emptyLabel.setPadding(new Insets(20));

        BorderPane emptyLayout = new BorderPane(emptyLabel);
        emptyLayout.setStyle("-fx-background-color: #2B2B2B;");
        layout.setCenter(emptyLayout);
    }

    private void refreshFavorites() {
        try {
            loadFavorites();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}