package com.example.fms_market;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShowCardUtil {

    public static final double SHOW_CARD_WIDTH = 200;
    private static final double SHOW_CARD_HEIGHT = 282.94;
    private static final double STAR_SIZE = 100;  // Increased size for the circle
    private static final double RADIUS = 40;

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

        StackPane posterContainer = new StackPane(posterView, createRatedIcon(show), createFavoriteIcon(show, user.getId(), refreshCallback));
        posterContainer.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(createRatedIcon(show), Pos.TOP_RIGHT);
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
    public static StackPane createRatedIcon(Show show) {
        Calculate_Rating ratingCalculator = new Calculate_Rating();
        double averageRating = ratingCalculator.calculateAverageRating(show);

        String formattedRating = String.format(Locale.ENGLISH, "%.1f", averageRating);
        if (formattedRating.endsWith(".0")) {
            formattedRating = formattedRating.substring(0, formattedRating.length() - 2);
        }

        int current = (int) Math.round((averageRating / 10) * 20);
        StackPane starCanvas = createStarCanvas(averageRating, current, STAR_SIZE, STAR_SIZE, RADIUS);
        starCanvas.setAlignment(Pos.TOP_RIGHT);
        starCanvas.setTranslateX(30);
        Label ratingLabel = new Label(formattedRating);
        ratingLabel.setFont(Font.font("Arial", 14));
        ratingLabel.setTextFill(Color.BLACK);
        ratingLabel.setAlignment(Pos.TOP_RIGHT);
        ratingLabel.setTranslateY(-30);
        ratingLabel.setTranslateX(formattedRating.matches("\\d+") ? -15 : -10);

        VBox allrate = new VBox(0);
        allrate.setTranslateY(-30);
        allrate.setAlignment(Pos.TOP_RIGHT);
        allrate.setPadding(new Insets(0, 0, 0, 0));
        allrate.setSpacing(0);
        allrate.getChildren().addAll(starCanvas, ratingLabel);
        Rectangle background = new Rectangle(45, 60);
        background.setArcWidth(10);
        background.setArcHeight(10);
        background.setFill(Color.color(1, 1, 1, 0.4));

        StackPane finalrate = new StackPane(background, allrate);
        finalrate.setPadding(new Insets(0));
        finalrate.setAlignment(Pos.TOP_RIGHT);

        return finalrate;
    }

    private static StackPane createStarCanvas(double averageRating, int currentValue, double centerX, double centerY, double radius) {
        double canvasSize = STAR_SIZE;
        double scale = 0.4;

        StackPane ratedIcon = new StackPane();
        Canvas canvas = new Canvas(canvasSize, canvasSize);
        ratedIcon.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        double anglePerSection = 360.0 / 20;

        gc.clearRect(0, 0, canvasSize, canvasSize);
        for (int i = 0; i < 20; i++) {
            double startAngle = i * anglePerSection;
            gc.setFill(i < currentValue ? Color.web("#DAA520") : Color.web("494848"));
            gc.fillArc(
                    canvasSize / 2 - radius, canvasSize / 2 - radius,
                    radius * 2, radius * 2,
                    startAngle, anglePerSection, ArcType.ROUND
            );
        }

        //inner circle
        double innerRadius = radius * 0.9;
        gc.setFill(Color.web("#D4D4D4"));
        gc.fillOval(
                canvasSize / 2 - innerRadius, canvasSize / 2 - innerRadius,
                innerRadius * 2, innerRadius * 2
        );

        //star at center
        double starOuterRadius = innerRadius * 0.6;
        double starInnerRadius = starOuterRadius * 0.5;
        star(gc, canvasSize / 2, canvasSize / 2, starOuterRadius, starInnerRadius);

        canvas.setScaleX(scale);
        canvas.setScaleY(scale);

        return ratedIcon;
    }
    private static void star(GraphicsContext gc, double centerX, double centerY, double outerRadius, double innerRadius) {
        gc.setFill(Color.web("#DAA520"));
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(i * 36); // 360 / 10
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;

            xPoints[i] = centerX + radius * Math.cos(angle);
            yPoints[i] = centerY - radius * Math.sin(angle);
        }
        gc.fillPolygon(xPoints, yPoints, 10);
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
            favoriteIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: " + (isFavorite ? "red" : "white") + ";");
            System.out.println("Favorite status updated: " + isFavorite);
            refreshCallback.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}