package com.example.fms_market;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
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

        // Director Information
        Label directorFirstNameLabel = new Label("Director First Name:");
        TextField directorFirstNameField = new TextField();
        grid.add(directorFirstNameLabel, 0, 12);
        grid.add(directorFirstNameField, 1, 12);

        Label directorLastNameLabel = new Label("Director Last Name:");
        TextField directorLastNameField = new TextField();
        grid.add(directorLastNameLabel, 0, 13);
        grid.add(directorLastNameField, 1, 13);

        Label directorAgeLabel = new Label("Director Age:");
        TextField directorAgeField = new TextField();
        grid.add(directorAgeLabel, 0, 14);
        grid.add(directorAgeField, 1, 14);

        Label directorGenderLabel = new Label("Director Gender:");
        TextField directorGenderField = new TextField();
        grid.add(directorGenderLabel, 0, 15);
        grid.add(directorGenderField, 1, 15);

        Label directorNationalityLabel = new Label("Director Nationality:");
        TextField directorNationalityField = new TextField();
        grid.add(directorNationalityLabel, 0, 16);
        grid.add(directorNationalityField, 1, 16);

        // Toggle for Movie or Series
        ToggleGroup group = new ToggleGroup();
        RadioButton movieButton = new RadioButton("Movie");
        movieButton.setToggleGroup(group);
        movieButton.setSelected(true);
        RadioButton seriesButton = new RadioButton("Series");
        seriesButton.setToggleGroup(group);
        grid.add(movieButton, 0, 17);
        grid.add(seriesButton, 1, 17);

        // Series-specific fields
        Label episodeCountLabel = new Label("Number of Episodes:");
        TextField episodeCountField = new TextField();
        grid.add(episodeCountLabel, 0, 18);
        grid.add(episodeCountField, 1, 18);

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
                Show show;
                Director director = new Director(
                        directorFirstNameField.getText(),
                        directorLastNameField.getText(),
                        new ArrayList<>(), // Assuming movies list is empty for now
                        Integer.parseInt(directorAgeField.getText()),
                        directorGenderField.getText(),
                        directorNationalityField.getText(),
                        directorFirstNameField.getText() + " " + directorLastNameField.getText() // Fullname
                );

                if (movieButton.isSelected()) {
                    Movie movie = new Movie();
                    movie.setTitle(titleField.getText());
                    movie.setDate(releaseDateField.getText());
                    movie.setDuration(Integer.parseInt(durationField.getText())); // Parse the duration
                    movie.setGenres(genresField.getText().split(","));
                    movie.setLanguage(languageField.getText().split(","));
                    movie.setImdb_score(new BigDecimal(imdbScoreField.getText())
                            .setScale(1, RoundingMode.HALF_UP)
                            .floatValue());
                    movie.setCountry(countryField.getText());
                    movie.setBudget(Long.parseLong(budgetField.getText()));
                    movie.setRevenue(Long.parseLong(revenueField.getText()));
                    movie.setDescription(descriptionField.getText());
                    movie.setPoster(posterField.getText()); // Set poster
                    movie.setVideo(videoField.getText()); // Set video
                    movie.setType("movie"); // Set type
                    movie.setDirector(director); // Set director
                    show = movie;
                } else {
                    Series series = new Series();
                    series.setTitle(titleField.getText());
                    series.setDate(releaseDateField.getText());
                    series.setDuration(Integer.parseInt(durationField.getText())); // Parse the duration
                    series.setGenres(genresField.getText().split(","));
                    series.setLanguage(languageField.getText().split(","));
                    series.setImdb_score(new BigDecimal(imdbScoreField.getText())
                            .setScale(1, RoundingMode.HALF_UP)
                            .floatValue());
                    series.setCountry(countryField.getText());
                    series.setBudget(Long.parseLong(budgetField.getText()));
                    series.setRevenue(Long.parseLong(revenueField.getText()));
                    series.setPoster(posterField.getText()); // Set poster
                    series.setVideo(videoField.getText()); // Set video
                    series.setDescription(descriptionField.getText());
                    series.setSeriesEp(Integer.parseInt(episodeCountField.getText()));
                    series.setType("series"); // Set type
                    series.setDirector(director); // Set director
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

        grid.add(saveButton, 1, 19);

        // Set scene and show stage
        Scene scene = new Scene(grid, 500, 700);
        stage.setTitle("Add/Edit Show");
        stage.setScene(scene);
        stage.show();
    }
}