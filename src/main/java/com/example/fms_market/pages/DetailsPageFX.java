package com.example.fms_market.pages;

import com.example.fms_market.data.CastJsonHandler;
import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.Cast;
import com.example.fms_market.model.Director;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DetailsPageFX {
    private final User user;
    private final Show show;
    private int stageWidth;
    private int stageHeight;

    public DetailsPageFX(User user, String name, Stage stage, Show show) {
        this.user = user;
        this.show = show;
        setScreenDimensions();

        VBox root = new VBox();
        root.setStyle("-fx-background-color: #252525;");

        HBox banner = Banner.getBanner(stage, "DetailsPage", "DetailsPage");
        root.getChildren().add(banner);

        // Details section
        FlowPane detailsBox = new FlowPane();
        detailsBox.setPadding(new Insets(20));
        detailsBox.setHgap(10);
        detailsBox.setVgap(10);
        detailsBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Content area
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #252525;");
        contentBox.setAlignment(javafx.geometry.Pos.CENTER);

        try {
            if (isDirector(name)) {
                Director director = getDirectorDetails(name);
                displayDirectorDetails(contentBox, director, stage);
            } else {
                Cast cast = getCastDetails(name);
                displayCastDetails(contentBox, cast, stage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1c1c1c; -fx-background-color: #1c1c1c; -fx-border-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, stageWidth, stageHeight);
        stage.setTitle("Details Page");
        stage.setScene(scene);
        stage.show();
    }

    private void setScreenDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stageWidth = (int) screenSize.getWidth();
        stageHeight = (int) (screenSize.getHeight() / 1.1);
    }

    private boolean isDirector(String name) throws IOException {
        List<Director> directors = DirectorJsonHandler.readDirectors();
        return directors.stream().anyMatch(director -> (STR."\{director.getFirstName()} \{director.getLastName()}").equals(name));
    }

    private Director getDirectorDetails(String name) throws IOException {
        List<Director> directors = DirectorJsonHandler.readDirectors();
        return directors.stream().filter(director -> (STR."\{director.getFirstName()} \{director.getLastName()}").equals(name)).findFirst().orElse(null);
    }

    private Cast getCastDetails(String name) throws IOException {
        List<Cast> castList = CastJsonHandler.readCast();
        return castList.stream().filter(cast -> (STR."\{cast.getFirst_name()} \{cast.getLast_name()}").equals(name)).findFirst().orElse(null);
    }

    private void displayDirectorDetails(VBox contentBox, Director director, Stage stage) {
        if (director != null) {
            Button backButton = new Button(LanguageManager.getLanguageBasedString("Zurück","Back"));
            backButton.setOnAction(_ -> new MoviePageFX(user, show, stage));
            HBox backButtonBox = new HBox(backButton);
            backButtonBox.setPadding(new Insets(10));
            backButtonBox.setAlignment(Pos.TOP_LEFT);
            contentBox.getChildren().add(backButtonBox);
            Label userIcon = new Label("👤");
            userIcon.setStyle(
                            "-fx-font-size: 50px; " +
                            "-fx-background-color: white; " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 100%; " +
                            "-fx-padding: 10px 20px 10px 20px; " +
                            "-fx-alignment: center;"
            );
            contentBox.getChildren().add(userIcon);

            Label nameLabel = new Label(STR."\{director.getFirstName()} \{director.getLastName()}");
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            nameLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            contentBox.getChildren().add(nameLabel);

            HBox detailsBox = new HBox(10);
            detailsBox.setAlignment(Pos.CENTER); // Center the content horizontally
            detailsBox.setPadding(new Insets(20, 0, 20, 0)); // Add space above and below the box (top: 20, right: 0, bottom: 20, left: 0)

            detailsBox.getChildren().add(createDetailLabel(STR."Nationality: \{director.getNationality()}"));
            detailsBox.getChildren().add(createDetailLabel(STR."Age: \{director.getAge()}"));
            detailsBox.getChildren().add(createDetailLabel(STR."Gender: \{director.getGender()}"));

            contentBox.getChildren().add(detailsBox);

            TilePane showsPane = createShowsPane(director.getShows(), stage);
            contentBox.getChildren().add(showsPane);
        }
    }

    private void displayCastDetails(VBox contentBox, Cast cast, Stage stage) {
        if (cast != null) {
            Button backButton = new Button(LanguageManager.getLanguageBasedString("Zurück","Back"));
            backButton.setOnAction(_ -> new MoviePageFX(user, show, stage));
            HBox backButtonBox = new HBox(backButton);
            backButtonBox.setPadding(new Insets(10));
            backButtonBox.setAlignment(Pos.TOP_LEFT);
            contentBox.getChildren().add(backButtonBox);

            Label userIcon = new Label("👤");
            userIcon.setStyle(
                            "-fx-font-size: 50px; " +
                            "-fx-background-color: white; " +
                            "-fx-text-fill: black; " +
                            "-fx-background-radius: 100%; " +
                            "-fx-padding: 10px 20px 10px 20px; " +
                            "-fx-alignment: center;"
            );
            contentBox.getChildren().add(userIcon);

            Label nameLabel = new Label(STR."\{cast.getFirst_name()} \{cast.getLast_name()}");
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            nameLabel.setTextFill(javafx.scene.paint.Color.WHITE);
            contentBox.getChildren().add(nameLabel);

            HBox detailsBox = new HBox(10);
            detailsBox.setAlignment(Pos.CENTER); // Center the content horizontally
            detailsBox.setPadding(new Insets(20, 0, 20, 0)); // Add space above and below the box (top: 20, right: 0, bottom: 20, left: 0)

            detailsBox.getChildren().add(createDetailLabel(STR."Nationality: \{cast.getNationality()}"));
            detailsBox.getChildren().add(createDetailLabel(STR."Age: \{cast.getAge()}"));
            detailsBox.getChildren().add(createDetailLabel(STR."Gender: \{cast.getGender()}"));

            contentBox.getChildren().add(detailsBox);

            TilePane showsPane = createShowsPane(cast.getShows(), stage);
            contentBox.getChildren().add(showsPane);
        }
    }

    private TilePane createShowsPane(List<String> showTitles, Stage stage) {
        TilePane showsPane = new TilePane();
        showsPane.setHgap(10);
        showsPane.setVgap(10);
        showsPane.setPrefColumns(3);

        showTitles.forEach(showTitle -> {
            try {
                Show show = ShowJsonHandler.getShowByTitle(showTitle);
                if (show != null) {
                    showsPane.getChildren().add(ShowCardUtil.createShowCard(show, user, stage, () -> {}));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return showsPane;
    }

    private Label createDetailLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        return label;
    }
}