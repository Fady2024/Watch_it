package com.example.fms_market;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Date;

public class AddEditShowPage {
    private Stage stage;

    public AddEditShowPage(Stage stage) {
        this.stage = stage;
        initializePage();
    }

    private void initializePage() {
        // Create the form elements
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Title
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);

        // Release Date
        Label releaseDateLabel = new Label("Release Date (YYYY-MM-DD):");
        TextField releaseDateField = new TextField();
        grid.add(releaseDateLabel, 0, 1);
        grid.add(releaseDateField, 1, 1);

        // Duration
        Label durationLabel = new Label("Duration (HH:MM):");
        TextField durationField = new TextField();
        grid.add(durationLabel, 0, 2);
        grid.add(durationField, 1, 2);

        // Genres
        Label genresLabel = new Label("Genres (comma separated):");
        TextField genresField = new TextField();
        grid.add(genresLabel, 0, 3);
        grid.add(genresField, 1, 3);

        // Language
        Label languageLabel = new Label("Language (comma separated):");
        TextField languageField = new TextField();
        grid.add(languageLabel, 0, 4);
        grid.add(languageField, 1, 4);

        // IMDb Score
        Label imdbScoreLabel = new Label("IMDb Score:");
        TextField imdbScoreField = new TextField();
        grid.add(imdbScoreLabel, 0, 5);
        grid.add(imdbScoreField, 1, 5);

        // Country
        Label countryLabel = new Label("Country:");
        TextField countryField = new TextField();
        grid.add(countryLabel, 0, 6);
        grid.add(countryField, 1, 6);

        // Budget
        Label budgetLabel = new Label("Budget:");
        TextField budgetField = new TextField();
        grid.add(budgetLabel, 0, 7);
        grid.add(budgetField, 1, 7);

        // Poster (file path or URL)
        Label posterLabel = new Label("Poster (URL):");
        TextField posterField = new TextField();
        grid.add(posterLabel, 0, 8);
        grid.add(posterField, 1, 8);

        // Description
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionField = new TextArea();
        descriptionField.setPrefRowCount(3);
        grid.add(descriptionLabel, 0, 9);
        grid.add(descriptionField, 1, 9);

        // Toggle for Movie or Series
        ToggleGroup group = new ToggleGroup();
        RadioButton movieButton = new RadioButton("Movie");
        movieButton.setToggleGroup(group);
        movieButton.setSelected(true);
        RadioButton seriesButton = new RadioButton("Series");
        seriesButton.setToggleGroup(group);
        grid.add(movieButton, 0, 10);
        grid.add(seriesButton, 1, 10);

        // Series-specific fields
        Label episodeCountLabel = new Label("Number of Episodes:");
        TextField episodeCountField = new TextField();
        grid.add(episodeCountLabel, 0, 11);
        grid.add(episodeCountField, 1, 11);

        episodeCountLabel.setVisible(false);
        episodeCountField.setVisible(false);

        // Toggle visibility of fields
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isSeries = seriesButton.isSelected();
            episodeCountLabel.setVisible(isSeries);
            episodeCountField.setVisible(isSeries);
        });

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            try {
                String releaseDateText = releaseDateField.getText();
                // Validate date format before conversion
                if (!releaseDateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
                }

                Date releaseDate = Date.valueOf(releaseDateText); // Parse the validated date

                Show show;
                if (movieButton.isSelected()) {
                    Movie movie = new Movie();
                    movie.setTitle(titleField.getText());
                    movie.setDate(releaseDate);
                    String[] durationParts = durationField.getText().split(":");
                    int hours = Integer.parseInt(durationParts[0]);
                    int minutes = Integer.parseInt(durationParts[1]);
                    movie.setDuration(new Time(hours, minutes)); // Parse the duration
                    movie.setGenres(genresField.getText().split(","));
                    movie.setLanguage(languageField.getText().split(","));
                    movie.setImdb_score(Float.parseFloat(imdbScoreField.getText()));
                    movie.setCountry(countryField.getText());
                    movie.setBudget(Long.parseLong(budgetField.getText()));
                    movie.setDescription(descriptionField.getText());
                    movie.setPoster(posterField.getText()); // Set poster
                    movie.setType("movie"); // Set type
                    show = movie;
                } else {
                    Series series = new Series();
                    series.setTitle(titleField.getText());
                    series.setDate(releaseDate);
                    String[] durationParts = durationField.getText().split(":");
                    int hours = Integer.parseInt(durationParts[0]);
                    int minutes = Integer.parseInt(durationParts[1]);
                    series.setDuration(new Time(hours, minutes)); // Parse the duration
                    series.setGenres(genresField.getText().split(","));
                    series.setLanguage(languageField.getText().split(","));
                    series.setImdb_score(Float.parseFloat(imdbScoreField.getText()));
                    series.setCountry(countryField.getText());
                    series.setBudget(Long.parseLong(budgetField.getText()));
                    series.setPoster(posterField.getText()); // Set poster
                    series.setDescription(descriptionField.getText());
                    series.setSeriesEp(Integer.parseInt(episodeCountField.getText()));
                    series.setType("series"); // Set type
                    show = series;
                }

                ShowJsonHandler.saveShow(show);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Show saved successfully!", ButtonType.OK);
                alert.show();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        grid.add(saveButton, 1, 12);

        // Set scene and show stage
        Scene scene = new Scene(grid, 500, 600);
        stage.setTitle("Add/Edit Show");
        stage.setScene(scene);
        stage.show();
    }
}