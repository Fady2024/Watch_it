// src/main/java/com/example/fms_market/ShowCardUtil.java
package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ShowCardUtil {

    public static final double SHOW_CARD_WIDTH = 200;
    private static final double SHOW_CARD_HEIGHT = 282.94;

    public static VBox createShowCard(Show show, User user, Stage stage, Runnable refreshCallback) {
        VBox showCard = new VBox(5);
        showCard.setAlignment(Pos.TOP_CENTER);

        ImageView posterView = createPosterView(show.getPoster());
        Label title = new Label(show.getTitle());
        title.setFont(Font.loadFont(Objects.requireNonNull(ShowCardUtil.class.getResource("/LexendDecaRegular.ttf")).toString(),14));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);
        title.setWrapText(true);
        title.setMaxWidth(SHOW_CARD_WIDTH);

        Button watchButton = new Button("Watch It");
        watchButton.setOnAction(event -> {
            try {
                new ShowPage(user, show, stage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        StackPane posterContainer = new StackPane(posterView, createFavoriteIcon(show, user.getId(), refreshCallback));
        posterContainer.setAlignment(Pos.TOP_LEFT);
        showCard.getChildren().addAll(posterContainer, title, watchButton);

        return showCard;
    }

    private static ImageView createPosterView(String posterPath) {
        Image moviePoster = new Image(posterPath, false);
        ImageView posterView = new ImageView(moviePoster);
        posterView.setFitWidth(SHOW_CARD_WIDTH);
        posterView.setFitHeight(SHOW_CARD_HEIGHT);

        Rectangle roundedRectangle = new Rectangle(SHOW_CARD_WIDTH, SHOW_CARD_HEIGHT);
        roundedRectangle.setArcWidth(20);
        roundedRectangle.setArcHeight(20);
        posterView.setClip(roundedRectangle);

        return posterView;
    }

    public static StackPane createFavoriteIcon(Show show, int userId, Runnable refreshCallback) {
        Label favoriteIcon = new Label("♥");
        boolean isFavorite = isShowFavorite(userId, show.getId());
        favoriteIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: " + (isFavorite ? "red" : "white") + "; -fx-font-weight: bold;");
        favoriteIcon.setPadding(new Insets(5));
        favoriteIcon.setTranslateY(-10);
        favoriteIcon.setOnMouseClicked(event -> toggleFavorite(userId, show.getId(), favoriteIcon, refreshCallback));

        Rectangle heartBackground = new Rectangle(30, 30);
        heartBackground.setArcWidth(15);
        heartBackground.setArcHeight(15);
        heartBackground.setFill(Color.color(1, 1, 1, 0.4));

        StackPane heartContainer = new StackPane(heartBackground, favoriteIcon);
        heartContainer.setAlignment(Pos.TOP_LEFT);
        heartContainer.setPadding(new Insets(5));

        return heartContainer;
    }

    private static boolean isShowFavorite(int userId, int showId) {
        try {
            List<Integer> favoriteShows = UserJsonHandler.getFavoriteShows(userId);
            return favoriteShows.contains(showId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void toggleFavorite(int userId, int showId, Label favoriteIcon, Runnable refreshCallback) {
        try {
            if (isShowFavorite(userId, showId)) {
                UserJsonHandler.removeFavoriteShow(userId, showId);
            } else {
                UserJsonHandler.addFavoriteShow(userId, showId);
            }
            boolean isFavorite = isShowFavorite(userId, showId);
            favoriteIcon.setStyle("-fx-font-size: 30px;-fx-text-fill: " + (isFavorite ? "red" : "white") + ";");
            refreshCallback.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}