// src/main/java/com/example/fms_market/AddEditShowPage.java
package com.example.fms_market;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;

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
        Label durationLabel = new Label("Duration:");
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

        // Revenue
        Label revenueLabel = new Label("Revenue:");
        TextField revenueField = new TextField();
        grid.add(revenueLabel, 0, 8);
        grid.add(revenueField, 1, 8);

        // Poster (file path or URL)
        Label posterLabel = new Label("Poster (URL):");
        TextField posterField = new TextField();
        grid.add(posterLabel, 0, 9);
        grid.add(posterField, 1, 9);

        // Video (file path or URL)
        Label videoLabel = new Label("Video (URL):");
        TextField videoField = new TextField();
        grid.add(videoLabel, 0, 10);
        grid.add(videoField, 1, 10);

        // Description
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionField = new TextArea();
        descriptionField.setPrefRowCount(3);
        grid.add(descriptionLabel, 0, 11);
        grid.add(descriptionField, 1, 11);

        Label viewsLabel = new Label("Views:");
        TextField viewsField = new TextField();
        grid.add(viewsLabel, 0, 12);
        grid.add(viewsField, 1, 12);

        // Director Details
        Label directorFirstNameLabel = new Label("Director First Name:");
        TextField directorFirstNameField = new TextField();
        grid.add(directorFirstNameLabel, 0, 13);
        grid.add(directorFirstNameField, 1, 13);

        Label directorLastNameLabel = new Label("Director Last Name:");
        TextField directorLastNameField = new TextField();
        grid.add(directorLastNameLabel, 0, 14);
        grid.add(directorLastNameField, 1, 14);

        Label directorMoviesLabel = new Label("Director Movies (comma separated):");
        TextField directorMoviesField = new TextField();
        grid.add(directorMoviesLabel, 0, 15);
        grid.add(directorMoviesField, 1, 15);

        Label directorAgeLabel = new Label("Director Age:");
        TextField directorAgeField = new TextField();
        grid.add(directorAgeLabel, 0, 16);
        grid.add(directorAgeField, 1, 16);

        Label directorGenderLabel = new Label("Director Gender:");
        TextField directorGenderField = new TextField();
        grid.add(directorGenderLabel, 0, 17);
        grid.add(directorGenderField, 1, 17);

        Label directorNationalityLabel = new Label("Director Nationality:");
        TextField directorNationalityField = new TextField();
        grid.add(directorNationalityLabel, 0, 18);
        grid.add(directorNationalityField, 1, 18);

        // Toggle for Movie or Series
        ToggleGroup group = new ToggleGroup();
        RadioButton movieButton = new RadioButton("Movie");
        movieButton.setToggleGroup(group);
        movieButton.setSelected(true);
        RadioButton seriesButton = new RadioButton("Series");
        seriesButton.setToggleGroup(group);
        grid.add(movieButton, 0, 19);
        grid.add(seriesButton, 1, 19);

        // Series-specific fields
        Label episodeCountLabel = new Label("Number of Episodes:");
        TextField episodeCountField = new TextField();
        grid.add(episodeCountLabel, 0, 20);
        grid.add(episodeCountField, 1, 20);

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
                    movie.setDuration(Integer.parseInt(durationField.getText())); // Parse the duration
                    movie.setGenres(Arrays.asList(genresField.getText().split(",")));
                    movie.setLanguage(Arrays.asList(languageField.getText().split(",")));
                    movie.setImdb_score(Float.parseFloat(imdbScoreField.getText()));
                    movie.setCountry(countryField.getText());
                    movie.setBudget(Long.parseLong(budgetField.getText()));
                    movie.setRevenue(Long.parseLong(revenueField.getText()));
                    movie.setDescription(descriptionField.getText());
                    movie.setViews(Integer.parseInt(viewsField.getText()));
                    movie.setPoster(posterField.getText()); // Set poster
                    movie.setVideo(videoField.getText()); // Set video
                    movie.setType("movie"); // Set type
                    show = movie;
                } else {
                    Series series = new Series();
                    series.setTitle(titleField.getText());
                    series.setDate(releaseDate);
                    series.setDuration(Integer.parseInt(durationField.getText())); // Parse the duration
                    series.setGenres(Arrays.asList(genresField.getText().split(",")));
                    series.setLanguage(Arrays.asList(languageField.getText().split(",")));
                    series.setImdb_score(Float.parseFloat(imdbScoreField.getText()));
                    series.setCountry(countryField.getText());
                    series.setBudget(Long.parseLong(budgetField.getText()));
                    series.setRevenue(Long.parseLong(revenueField.getText()));
                    series.setPoster(posterField.getText()); // Set poster
                    series.setVideo(videoField.getText()); // Set video
                    series.setDescription(descriptionField.getText());
                    series.setViews(Integer.parseInt(viewsField.getText()));
                    series.setSeriesEp(Integer.parseInt(episodeCountField.getText()));
                    series.setType("series"); // Set type
                    show = series;
                }

                Director director = new Director(
                        directorFirstNameField.getText(),
                        directorLastNameField.getText(),
                        Arrays.asList(directorMoviesField.getText().split(",")),
                        Integer.parseInt(directorAgeField.getText()),
                        directorGenderField.getText(),
                        directorNationalityField.getText()
                );

                show.setDirector(director);
                ShowJsonHandler.saveShow(show);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Show and Director saved successfully!", ButtonType.OK);
                alert.show();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        grid.add(saveButton, 1, 21);

        // Set scene and show stage
        Scene scene = new Scene(grid, 500, 800);
        stage.setTitle("Add/Edit Show");
        stage.setScene(scene);
        stage.show();
    }
}