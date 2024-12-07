// src/main/java/com/example/fms_market/RemoveShowPage.java

package com.example.fms_market;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class RemoveShowPage {

    private final ComboBox<String> showSelector = new ComboBox<>();
    private final TableView<Show> showDetailsTable = new TableView<>();

    public RemoveShowPage(Stage stage) throws InterruptedException {
        stage.setTitle("Remove Show");

        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // Title
        Label titleLabel = new Label("Remove Show");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown for show selection
        showSelector.setPromptText("Select a show to remove");
        showSelector.setMinWidth(300);

        // Show details table
        setupTable();

        // Load show data into the dropdown
        reloadShowData();

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

        layout.getChildren().addAll(titleLabel, showSelector, showDetailsTable, removeButton);

        // Scene setup
        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Configures the TableView for displaying show details.
     */
    private void setupTable() {
        TableColumn<Show, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setPrefWidth(50);

        TableColumn<Show, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        titleColumn.setPrefWidth(200);

        TableColumn<Show, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionColumn.setPrefWidth(200);

        showDetailsTable.getColumns().addAll(idColumn, titleColumn, descriptionColumn);
        showDetailsTable.setPrefHeight(200);
    }

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
}