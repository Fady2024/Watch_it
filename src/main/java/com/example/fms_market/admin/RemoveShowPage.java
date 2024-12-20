// src/main/java/com/example/fms_market/RemoveShowPage.java

package com.example.fms_market.admin;

import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.*;
import com.example.fms_market.pages.HomePage;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.fms_market.util.ShowCardUtil.SHOW_CARD_WIDTH;


public class RemoveShowPage {

    private final ComboBox<String> showSelector = new ComboBox<>();
    private final TableView<Show> showDetailsTable = new TableView<>();
    private final User user;
    private final Stage stage;

    public RemoveShowPage(User user, Stage stage) throws InterruptedException {
        this.user = user;
        this.stage = stage;
        GridPane showContainer = new GridPane();
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.setAlignment(Pos.TOP_LEFT);
        showContainer.setStyle("-fx-background-color: #1c1c1c;");

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

        // Remove Button
        Button removeButton = new Button("Remove Show");
        removeButton.setOnAction(_ -> {
            String selectedTitle = showSelector.getValue();
            if (selectedTitle == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No show selected.");
                return;
            }

            int showId = getShowIdByTitle(selectedTitle);
            if (showId == -1) {
                showAlert(Alert.AlertType.ERROR, "Error", "Show not found.");
                return;
            }

            new Thread(() -> {
                try {
                    // Remove the show
                    ShowJsonHandler.deleteShow(showId);

                    // Reload data on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        reloadShowData();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Show removed successfully.");
                        // Refresh all pages
                        refreshAllPages();
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to remove the show.");
                        e.printStackTrace();
                    });
                }
            }).start();
        });


        // Listen for show selection changes
        showSelector.setOnAction(_ -> {
            String selectedTitle = showSelector.getValue();
            showDetailsTable.getItems().setAll(
                    loadShowsFromFile().stream()
                            .filter(show -> show.getTitle().equals(selectedTitle))
                            .collect(Collectors.toList())
            );
        });

        // Scene setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(showContainer, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Show entfernen","Remove Show"));
        stage.show();
        List<Movie> finalAllMovies = allMovies;
        List<Series> finalAllSeries = allSeries;
        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(), finalAllMovies, finalAllSeries, user, stage));
        adjustLayout(showContainer, stageWidth, allMovies,allSeries, user, stage);
    }

    /**
     * Configures the TableView for displaying show details.
     */
//    private void setupTable() {
//        TableColumn<Show, Integer> idColumn = new TableColumn<>("ID");
//        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
//        idColumn.setPrefWidth(50);
//
//        TableColumn<Show, String> titleColumn = new TableColumn<>("Title");
//        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
//        titleColumn.setPrefWidth(200);
//
//        TableColumn<Show, String> descriptionColumn = new TableColumn<>("Description");
//        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
//        descriptionColumn.setPrefWidth(200);
//
//        showDetailsTable.getColumns().addAll(idColumn, titleColumn, descriptionColumn);
//        showDetailsTable.setPrefHeight(200);
//    }

    /**
     * Reloads the shows from the JSON file into the UI.
     */
    private void reloadShowData() {
        List<Show> shows = loadShowsFromFile();
        showSelector.getItems().setAll(shows.stream()
                .map(Show::getTitle)
                .collect(Collectors.toList()));
        showDetailsTable.getItems().clear();
    }

    /**
     * Loads the shows from the JSON file.
     */
    private List<Show> loadShowsFromFile() {
        try {
            return ShowJsonHandler.readShows();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * Retrieves the ID of a show by its title.
     */

    private void adjustLayout(GridPane showContainer, double width, List<Movie> recentMovies,List<Series> recentSeries, User user, Stage stage) {
        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        HBox adminBox = new HBox(10);

        //Back Icon
        Image backIconImage = new Image("Acount/stash_arrow-left-solid.png");
        ImageView backIconView = new ImageView(backIconImage);
        backIconView.setFitWidth(40);
        backIconView.setFitHeight(40);

        //Back button
        Text backLabel = new Text("Back");
        backLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),32));
        backLabel.setStyle("-fx-fill: white;" );

        HBox backBox = new HBox(10);
        backBox.getChildren().addAll(backIconView,backLabel);
        backBox.setStyle("-fx-cursor: hand;");
        backBox.setOnMouseClicked(e -> new HomePage(user,stage));

        //Add new Show Label
        Text deleteLabel = new Text("Select a Show to Delete:");
        deleteLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),34));
        deleteLabel.setStyle("-fx-fill: white;");
        GridPane grid = new GridPane();
        grid.setHgap(45);
        grid.setVgap(10);
        VBox title = new VBox(10);
        title.getChildren().addAll(backBox,deleteLabel);

        TextField searchField = new TextField();
        searchField.setPrefWidth(700);
        searchField.setPrefHeight(60);
        searchField.setStyle("-fx-background-radius: 50; -fx-border-radius: 50;-fx-border-width: 1;" +
                "-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        searchField.setPromptText(LanguageManager.getLanguageBasedString("\uD83D\uDD0D Suche nach Stichwort","\uD83D\uDD0D Search by keyword"));
        searchField.setFont(Font.font("Arial", 15));

        Text mostWatchedShow = LanguageManager.createLanguageText("Meistgesehene Filme","Most Watched Movies","20","white");

        mostWatchedShow.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));

        VBox titleBox = new VBox(30);
        titleBox.setPadding(new Insets(10));
        titleBox.setStyle("-fx-background-color:  #1c1c1c;");
        titleBox.setPrefWidth(width - 60);
        titleBox.setPrefHeight(200);

        Text RecentMovies = LanguageManager.createLanguageText("Filme","Movies","20","white");
        RecentMovies.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));

        Text RecentSeries = LanguageManager.createLanguageText("Serien","Series","20","white");
        RecentSeries.setFont(Font.loadFont(Objects.requireNonNull(HomePage.class.getResource("/LexendDecaRegular.ttf")).toString(),20));
        int column = 0;
        int row = 0;
            titleBox.getChildren().addAll(backBox,deleteLabel);
        showContainer.add(titleBox, column, row);
        showContainer.add(searchField,column+1,row);
        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.add(RecentMovies, column, row + 3);
        showContainer.add(RecentSeries, column, row + 6);

        HBox showMoviesCardContainer = new HBox(30);
        for (Movie movie : recentMovies) {
            VBox showCard = ShowCardUtil.createDeleteShowCard(movie, user, stage);
            showMoviesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showMoviesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
            showCard.setOnMouseReleased(e->confirmDeletePopup(movie.getId()));
        }
        showContainer.add(showMoviesCardContainer, column, row + 5);

        HBox showSeriesCardContainer = new HBox(30);
        for (Series series : recentSeries) {
            VBox showCard = ShowCardUtil.createDeleteShowCard(series, user, stage);
            showSeriesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showSeriesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
            showCard.setOnMouseReleased(e->confirmDeletePopup(series.getId()));
        }
        showContainer.add(showSeriesCardContainer, column, row + 8);
        //showSeriesCardContainer.setOnMouseClicked(e-> confirmDeletePopup());
    }

    private int getShowIdByTitle(String title) {
        return loadShowsFromFile().stream()
                .filter(show -> show.getTitle().equals(title))
                .findFirst()
                .map(Show::getId)
                .orElse(-1);
    }

    /**
     * Displays an alert to the user.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Refreshes all pages.
     */
    private void refreshAllPages() {
        // Reload the show data
        reloadShowData();

        // Refresh the show details table
        showDetailsTable.getItems().setAll(loadShowsFromFile());

        // Optionally, refresh other UI components if needed
    }
    private void confirmDeletePopup(int id) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(50);
        VBox texts = new VBox(10);

        Text addNewText = new Text("Are You Sure You Want to Delete This Show From WATCH IT?");
        addNewText.setFill(Paint.valueOf("white"));
        addNewText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),20));
        texts.getChildren().addAll(addNewText);
        addNewText.setWrappingWidth(380);
        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: white;-fx-text-fill: black;");
        cancelButton.setPrefSize(180,50);
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: #8D5BDC;-fx-text-fill: black;");
        confirmButton.setPrefSize(180,50);
        cancelButton.setOnAction(e -> popupStage.close());
        confirmButton.setOnAction(e-> {
            showAlert("Success", "Show Deleted Succesfully");
            popupStage.close();
            new HomePage(user,stage);
            try {
                ShowJsonHandler.deleteShow(id);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        popupVBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(texts,buttonBox);
        popupVBox.setPadding(new Insets(30,60,30,60));
        popupVBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 500, 250);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Confirm Delete");
        popupStage.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}