package com.example.fms_market.pages;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fms_market.data.CastJsonHandler;
import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.*;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.Calculate_Rating;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MoviePageFX {

    private int stageWidth;
    private int stageHeight;
    private final Stage stage;
    private final User user;
    private final Show show;
    public MoviePageFX(User user, Show show, Stage stage) {
        setScreenDimensions();
        this.stage = stage;
        this.user = user;
        this.show = show;
        // Root Layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #252525;");

        // Add Banner to the top
        HBox banner = Banner.getBanner(stage, LanguageManager.getLanguageBasedString("Filmseite","MoviePage"));
        root.setTop(banner);

        // Create a new VBox for title, description, and details
        VBox titleDescriptionDetails = new VBox(10);
        titleDescriptionDetails.setPadding(new Insets(20));

        Label title = new Label(show.getTitle());
        title.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/Italiana-Regular.ttf")).toString(),60));
        title.setTextFill(Color.WHITE);

        Label description = new Label(show.getDescription());
        description.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        description.setTextFill(Color.rgb(177,177,177));
        description.setWrapText(true);

        // Center Section (Horizontal Layout)
        HBox centerSection = new HBox(20);
        centerSection.setPadding(new Insets(20));

        // Movie Poster
        ImageView poster = new ImageView(new Image(show.getPoster()));
        poster.setTranslateY(10);
        poster.setFitWidth(200);
        poster.setFitHeight(300);

        // Apply rounded corners to the poster
        Rectangle clip = new Rectangle(200, 300);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        poster.setClip(clip);

        VBox details = new VBox(10);
        details.setPadding(new Insets(20));
        details.setAlignment(Pos.TOP_LEFT);

        Rectangle background = new Rectangle(100, 40);
        background.setArcWidth(30);
        background.setArcHeight(30);
        background.setFill(Color.rgb(243,206,19));

        Label imdbRating = new Label(String.format("IMDb: %s", show.getImdb_score()));
        imdbRating.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        imdbRating.setTextFill(Color.BLACK);

        StackPane imdbRatingWithBackground = new StackPane();
        imdbRatingWithBackground.getChildren().addAll(background, imdbRating);
        imdbRatingWithBackground.setAlignment(Pos.CENTER);

        Label duration = new Label(String.format("%d Min", show.getDuration()));
        duration.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        duration.setTextFill(Color.rgb(177,177,177));

        Label date = new Label(new SimpleDateFormat("yyyy-MM-dd").format(show.getDate()));
        date.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        date.setTextFill(Color.rgb(177,177,177));

        Label language = new Label(show.getLanguage() != null ? String.join(", ", show.getLanguage()) : "N/A");
        language.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        language.setTextFill(Color.rgb(177,177,177));

        Label country = new Label(show.getCountry());
        country.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        country.setTextFill(Color.rgb(177,177,177));

        Rectangle separator1 = new Rectangle(1, 30);
        separator1.setFill(Color.rgb(159,159,159));

        Rectangle separator2 = new Rectangle(1, 30);
        separator2.setFill(Color.rgb(159,159,159));

        Rectangle separator3 = new Rectangle(1, 30);
        separator3.setFill(Color.rgb(159,159,159));

        Rectangle separator4 = new Rectangle(1, 30);
        separator4.setFill(Color.rgb(159,159,159));

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();
        Region spacer4 = new Region();

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        HBox.setHgrow(spacer4, Priority.ALWAYS);

        HBox detailsRow = new HBox(10);
        detailsRow.setAlignment(Pos.TOP_LEFT);

        duration.setTranslateY(10);
        date.setTranslateY(10);
        language.setTranslateY(10);
        country.setTranslateY(10);

        detailsRow.getChildren().addAll(
                imdbRatingWithBackground,
                separator1,
                duration,
                separator2,
                date,
                separator3,
                language,
                separator4,
                country
        );

        HBox revenueRow = new HBox(10);
        revenueRow.setPadding(new Insets(10, 0, 0, 0));
        Label revenue = new Label(String.format(LanguageManager.getLanguageBasedString("Einnahmen: %s$","Revenue: %s$"), show.getRevenue()));
        revenue.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        revenue.setTextFill(Color.WHITE);

        revenueRow.getChildren().add(revenue);

        HBox budgetGenresRow = new HBox(30);
        budgetGenresRow.setPadding(new Insets(10, 0, 0, 0));

        Label budget = new Label(String.format("Budget: %s$", show.getBudget()));
        budget.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        budget.setTextFill(Color.WHITE);

        Calculate_Rating calculateRating = new Calculate_Rating();
        double averageRating = calculateRating.calculateAverageRating(show);

        String showType = show.getType();
        String capitalizedShowType = showType.substring(0, 1).toUpperCase() + showType.substring(1);
        Label showTypeLabel = new Label(capitalizedShowType);
        showTypeLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        showTypeLabel.setTextFill(Color.WHITE);

        Label averageRatingTextLabel = new Label(String.format("%.1f", averageRating));
        averageRatingTextLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        averageRatingTextLabel.setTextFill(Color.WHITE);

        HBox genres = new HBox(10);
        for (String genre : show.getGenres()) {
            genres.getChildren().add(createGenreButton(genre));
            genres.setTranslateY(15);
            genres.setTranslateX(20);
        }

        budgetGenresRow.getChildren().addAll(budget, genres);

        HBox buttons = new HBox(10);
        Button playButton = new Button("▶");
        playButton.setPrefSize(50, 30);
        playButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #c9068d, #641271);" +
                        "-fx-text-fill: black;" +
                        "-fx-padding: 0px 5px;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 35px;"
        );
        playButton.setOnAction(_ -> new VideoPlayerFX(show.getVideo(),show.getId(), stage,user));

        Button addButton = new Button(LanguageManager.getLanguageBasedString("Hinzufügen","♥ Add"));
        addButton.setPrefWidth(120);
        addButton.setPrefHeight(50);
        addButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-padding: 10px 20px; -fx-background-radius: 20; -fx-font-size: 18px; -fx-font-weight: bold;");
        addButton.setOnAction(_ -> {
            try {
                if (ShowCardUtil.isShowFavorite(user.getId(), show.getId())) {
                    // Remove favorite
                    UserJsonHandler.removeFavoriteShow(user.getId(), show.getId());
                    addButton.setStyle("-fx-background-color: white; -fx-text-fill: black; " +
                            "-fx-padding: 10px 20px; -fx-background-radius: 20; " +
                            "-fx-font-size: 18px; -fx-font-weight: bold;");
                } else {
                    // Add to favorite
                    UserJsonHandler.addFavoriteShow(user.getId(), show.getId());
                    addButton.setStyle("-fx-background-color: white; -fx-text-fill: red; " +
                            "-fx-padding: 10px 20px; -fx-background-radius: 20; " +
                            "-fx-font-size: 18px; -fx-font-weight: bold;");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addButton.setOnMousePressed(_ -> addButton.setScaleX(0.95));
        addButton.setOnMouseReleased(_ -> addButton.setScaleX(1.0));
        addButton.setTranslateY(2);
        buttons.getChildren().addAll(playButton, addButton);
        buttons.setTranslateY(-45);
        try {
            VBox castTable = createCastTable(show.getTitle());
            VBox directorTable = createDirectorTable(show.getTitle());

            VBox castTableContainer = new VBox(castTable);
            VBox directorTableContainer = new VBox(directorTable);

            HBox tablesHBox = new HBox(10);  // 10 is the spacing between the tables
            tablesHBox.getChildren().addAll(directorTableContainer,castTableContainer);
            details.getChildren().addAll(detailsRow, budgetGenresRow, revenueRow, tablesHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HBox ratingBar = createAnimatedRatingBar();
        HBox averageRatingBar = createRatingBar(averageRating);

        Button saveRatingButton = new Button("✓");
        saveRatingButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 3px; -fx-border-radius: 20; -fx-background-radius: 20; -fx-font-size: 16px;");

        // Pop-up text labels

        Label firstRateLabel = new Label(LanguageManager.getLanguageBasedString("Bewertung hinzugefügt!","Rating added!"));
        firstRateLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        firstRateLabel.setTextFill(Color.GREEN);
        firstRateLabel.setVisible(false);

        Label updateRateLabel = new Label(LanguageManager.getLanguageBasedString("Bewertung aktualisiert!","Rating updated!"));
        updateRateLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        updateRateLabel.setTextFill(Color.ORANGE);
        updateRateLabel.setVisible(false);

        saveRatingButton.setOnAction(_ -> {
            try {
                Date dateOfWatched = new Date();
                if (!ShowJsonHandler.checkIfRatingExists(user.getId(), show.getId())) {
                    ShowJsonHandler.saveShowRating(user.getId(), show.getId(), dateOfWatched, selectedRating[0]);
                    double newAverageRating = calculateRating.calculateAverageRating(show);
                    averageRatingTextLabel.setText(String.format("%.1f", newAverageRating));
                    showPopUpText(firstRateLabel);
                } else {
                    ShowJsonHandler.updateShowRating(user.getId(), show.getId(), dateOfWatched, selectedRating[0]);
                    double newAverageRating = calculateRating.calculateAverageRating(show);
                    averageRatingTextLabel.setText(String.format("%.1f", newAverageRating));
                    showPopUpText(updateRateLabel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox ratingBarWithButton = new HBox(10, ratingBar, saveRatingButton);
        ratingBarWithButton.setAlignment(Pos.CENTER_LEFT);

        HBox typeAndRatingRow = new HBox(100, showTypeLabel, averageRatingTextLabel);
        typeAndRatingRow.setTranslateY(5);
        typeAndRatingRow.setTranslateX(18);
        typeAndRatingRow.setAlignment(Pos.CENTER_LEFT);
        firstRateLabel.setTranslateX(30);
        firstRateLabel.setTranslateY(-15);
        firstRateLabel.setAlignment(Pos.CENTER);
        updateRateLabel.setAlignment(Pos.CENTER);
        updateRateLabel.setTranslateX(25);
        updateRateLabel.setTranslateY(-40);

        VBox posterAndRating = new VBox(10, poster, typeAndRatingRow, averageRatingBar, ratingBarWithButton, firstRateLabel, updateRateLabel, buttons);
        titleDescriptionDetails.getChildren().addAll(title, description, details);
        centerSection.getChildren().addAll(posterAndRating, titleDescriptionDetails);
        centerSection.setPadding(new Insets(50, 30, 0, 50)); // top, right, bottom, left
        root.setCenter(centerSection);
        Scene scene = new Scene(root, stageWidth, stageHeight);


        stage.setTitle(LanguageManager.getLanguageBasedString("Filmseite","Movie Page"));
        stage.setScene(scene);
        stage.show();
    }

    private void setScreenDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stageWidth = (int) screenSize.getWidth();
        stageHeight = (int) (screenSize.getHeight() / 1.1);
    }

    private void showPopUpText(Label label) {
        label.setVisible(true);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(_ -> label.setVisible(false));
        fadeTransition.play();
    }

    private final int[] selectedRating = {0}; // Store the selected rating

    private HBox createAnimatedRatingBar() {
        HBox ratingBar = new HBox(15);
        ratingBar.setAlignment(Pos.CENTER);
        ratingBar.setPadding(new Insets(10));

        for (int i = 1; i <= 5; i++) {
            Polygon star = createStar();
            star.setScaleX(1.4);
            star.setScaleY(1.4);
            int currentRating = i;

            star.setOnMouseEntered(_ -> applyScaleAnimation(star, 1.6));
            star.setOnMouseExited(_ -> applyScaleAnimation(star, 1.4));

            star.setOnMouseClicked(_ -> {
                selectedRating[0] = currentRating;
                updateStarColors(ratingBar, currentRating);
            });

            ratingBar.getChildren().add(star);
        }
        return ratingBar;
    }

    private Polygon createStar() {
        Polygon star = new Polygon();

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i - Math.PI / 2;
            double r = (i % 2 == 0) ? (double) 7 : (double) 7 / 2;
            double x = (double) 7 + Math.cos(angle) * r;
            double y = (double) 7 + Math.sin(angle) * r;
            star.getPoints().addAll(x, y);
        }
        star.setFill(Color.TRANSPARENT);
        star.setStroke(Color.GRAY);
        star.setStrokeWidth(1);
        return star;
    }

    private void applyScaleAnimation(Polygon star, double scale) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), star);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
    }

    private void updateStarColors(HBox ratingBar, int selectedRating) {
        for (int i = 0; i < ratingBar.getChildren().size(); i++) {
            Polygon star = (Polygon) ratingBar.getChildren().get(i);
            if (i < selectedRating) {
                star.setFill(Color.GOLD);
            } else {
                star.setFill(Color.TRANSPARENT);
            }
        }
    }

    private Button createGenreButton(String genre) {
        Button button = new Button(genre);
        button.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        button.setPrefWidth(105);
        button.setPrefHeight(35);
        button.setStyle("-fx-background-color: #ffffff; -fx-text-fill: black; -fx-padding: 5px 10px; -fx-border-radius: 25; -fx-background-radius: 25;");
        return button;
    }

    private HBox createRatingBar(double averageRating) {
        HBox ratingBar = new HBox();
        ratingBar.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < 10; i++) {
            SVGPath segment = new SVGPath();

            if (i == 0) {
                segment.setContent("M20,0 L20,10 Q12,10 12,5 Q12,0 20,0 Z");
            } else if (i == 9) {
                segment.setContent("M0,0 L10,0 Q15,0 15,5 Q15,10 10,10 L0,10 Z");
            } else {
                segment.setContent("M0,0 L20,0 L20,10 L0,10 Z");
            }

            if (i < averageRating) {
                segment.setFill(Color.rgb(243,206,19));
            } else {
                segment.setFill(Color.WHITE);
            }

            ratingBar.getChildren().add(segment);
        }

        return ratingBar;
    }

    private VBox createInfoTable(String titleText, List<String> contentText) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.TOP_CENTER);
        box.setStyle("-fx-background-color: white; -fx-border-radius: 12; -fx-background-radius: 12;");

        Image directorIcon = new Image("Acount/ph_film-slate-fill.png");
        ImageView directorIconView = new ImageView(directorIcon);
        directorIconView.setFitWidth(30);
        directorIconView.setFitHeight(30);

        Image castIcon = new Image("Acount/ic_sharp-people.png");
        ImageView castIconView = new ImageView(castIcon);
        castIconView.setFitWidth(30);
        castIconView.setFitHeight(30);

        HBox titleBox = new HBox(10);
        titleBox.setStyle("-fx-background-color: #5B146F; -fx-padding: 5; -fx-border-radius: 10 10 0 0; -fx-background-radius: 10 10 0 0;");
        Label title = new Label(titleText);
        title.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-background-color: #5B146F; -fx-padding: 5; -fx-border-radius: 10 10 0 0; -fx-background-radius: 10 10 0 0;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setPrefHeight(42);
        title.setAlignment(Pos.CENTER);
        if(titleText.equals("Director"))
        titleBox.getChildren().addAll(directorIconView,title);
        else
            titleBox.getChildren().addAll(castIconView,title);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(5,10,10,10));
        for (int i = 0; i < contentText.size(); i++) {
            Label contentLabel = new Label(contentText.get(i));
            contentLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
            contentLabel.setTextFill(Color.web("Black"));
            contentLabel.setOnMouseClicked(_ -> navigateToDetailsPage(contentLabel.getText(), stage));
            contentBox.getChildren().add(contentLabel);
            if (i < contentText.size() - 1) {
                Separator separator = new Separator();
                separator.setStyle("-fx-background-color: Black;");
                contentBox.getChildren().add(separator);
            }
        }

        box.getChildren().addAll(titleBox, contentBox);
        return box;
    }

    private void navigateToDetailsPage(String name, Stage stage) {
        new DetailsPageFX(user,name, stage,show);
    }

    private VBox createCastTable(String showName) throws IOException {
        List<Cast> castList = CastJsonHandler.readCast();
        List<String> castNames = castList.stream()
                .filter(cast -> cast.getShows().contains(showName))
                .map(cast -> STR."\{cast.getFirst_name()} \{cast.getLast_name()}")
                .collect(Collectors.toList());
        return createInfoTable(LanguageManager.getLanguageBasedString("Gießen","Cast")
                , castNames);
    }

    private VBox createDirectorTable(String showName) throws IOException {
        List<Director> directorList = DirectorJsonHandler.readDirectors();
        List<String> directorNames = directorList.stream()
                .filter(director -> director.getShows().contains(showName))
                .map(director -> STR."\{director.getFirstName()} \{director.getLastName()}")
                .collect(Collectors.toList());

        return createInfoTable(LanguageManager.getLanguageBasedString("Direktorin","Director")
                , directorNames);
    }

}