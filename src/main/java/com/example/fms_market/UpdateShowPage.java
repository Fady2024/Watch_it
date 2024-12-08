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
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateShowPage {

    private final ComboBox<String> showSelector = new ComboBox<>();
    private final TableView<Show> showDetailsTable = new TableView<>();

    public UpdateShowPage(Stage stage) {
        stage.setTitle("Update Show");

        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // Title
        Label titleLabel = new Label("Update Show");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown for show selection
        showSelector.setPromptText("Select a show to update");
        showSelector.setMinWidth(300);

        // Show details table
        setupTable();

        // Load show data into the dropdown
        reloadShowData();

        // Update Button
        Button updateButton = new Button("Update Show");
        updateButton.setOnAction(_ -> updateShow());

        // Listen for show selection changes
        showSelector.setOnAction(_ -> loadSelectedShowDetails());

        layout.getChildren().addAll(titleLabel, showSelector, showDetailsTable, updateButton);

        // Scene setup
        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

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

    private void reloadShowData() {
        List<Show> shows = loadShowsFromFile();
        showSelector.getItems().setAll(shows.stream()
                .map(Show::getTitle)
                .collect(Collectors.toList()));
        showDetailsTable.getItems().clear();
    }

    private List<Show> loadShowsFromFile() {
        try {
            return ShowJsonHandler.readShows();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void loadSelectedShowDetails() {
        String selectedTitle = showSelector.getValue();
        showDetailsTable.getItems().setAll(
                loadShowsFromFile().stream()
                        .filter(show -> show.getTitle().equals(selectedTitle))
                        .collect(Collectors.toList())
        );
    }

    private void updateShow() {
        try {
            Show selectedShow = showDetailsTable.getSelectionModel().getSelectedItem();
            if (selectedShow == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No show selected.");
                return;
            }

            // Update the show details
            ShowJsonHandler.saveShow(selectedShow);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Show updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the show.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}