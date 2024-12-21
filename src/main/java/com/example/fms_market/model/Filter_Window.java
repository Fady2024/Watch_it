package com.example.fms_market.model;

import com.example.fms_market.model.User_Filter;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Filter_Window {
    private final User_Filter user_filter;
    private VBox filterPane;
    private  User user;

    public Filter_Window(User_Filter userFilter) {

        this.user_filter = userFilter;
        //this.filterPane = new VBox(); // Initialize the filterPane
        this.filterPane = new VBox(); // Initialize the filterPane
        this.filterPane.setStyle("-fx-background-color: rgba(64,64,64,255); -fx-background-radius: 20px; -fx-padding: 20px;");
        this.user=user;
    }
    public VBox getFilterVBox() {
        // Genres Section
        Label genresLabel = new Label("Genres:");
        HBox genresBox = new HBox(10);
        genresLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),18));
        genresLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        CheckBox actionCheckBox = new CheckBox("Action");
        CheckBox dramaCheckBox = new CheckBox("Drama");
        CheckBox comedyCheckBox = new CheckBox("Comedy");
        CheckBox horrorCheckBox = new CheckBox("Horror");
        styleCheckBoxes(actionCheckBox, dramaCheckBox, comedyCheckBox, horrorCheckBox);
        genresBox.getChildren().addAll(actionCheckBox, dramaCheckBox, comedyCheckBox, horrorCheckBox);

        // Language Section
        Label languageLabel = new Label("Language:");
        languageLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),18));
        languageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        HBox languageBox = new HBox(10);
        CheckBox englishCheckBox = new CheckBox("English");
        CheckBox arabicCheckBox = new CheckBox("Arabic");
        CheckBox italianCheckBox = new CheckBox("Italian");
        styleCheckBoxes(englishCheckBox, arabicCheckBox, italianCheckBox);
        languageBox.getChildren().addAll(englishCheckBox, arabicCheckBox, italianCheckBox);

        // IMDB Slider Section
        Label imdbLabel = new Label("IMDB:");
        imdbLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),18));
        imdbLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
        Slider imdbSlider = new Slider(0, 10, 5);
        imdbSlider.setShowTickLabels(true);
        imdbSlider.setShowTickMarks(true);
        imdbSlider.setMajorTickUnit(1);
        Label imdbValue = new Label("5.0");
        imdbValue.setStyle("-fx-font-family: 'Roboto', sans-serif; -fx-font-size: 16px; -fx-text-fill: white;");
        imdbSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            imdbValue.setText(String.format("%.1f", newValue));
            user_filter.setImdbRating(newValue.doubleValue());
        });
        HBox imdbBox = new HBox(10, imdbLabel, imdbSlider, imdbValue);

        // Country Section
        Label countryLabel = new Label("Country:");
        countryLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),18));
        countryLabel.setStyle(" -fx-font-weight: bold; -fx-text-fill: white;");
        HBox countryBox = new HBox(10);
        CheckBox usaCheckBox = new CheckBox("USA");
        CheckBox englandCheckBox = new CheckBox("England");
        CheckBox egyptCheckBox = new CheckBox("Egypt");
        styleCheckBoxes(usaCheckBox, englandCheckBox, egyptCheckBox);
        countryBox.getChildren().addAll(usaCheckBox, englandCheckBox, egyptCheckBox);

        // Type Section
        Label typeLabel = new Label("Type:");
        typeLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),18));
        typeLabel.setStyle(" -fx-font-weight: bold; -fx-text-fill: white;");
        HBox typeBox = new HBox(10);
        CheckBox movieCheckBox = new CheckBox("Movie");
        CheckBox seriesCheckBox = new CheckBox("Series");
        styleCheckBoxes(movieCheckBox, seriesCheckBox);
        typeBox.getChildren().addAll(movieCheckBox, seriesCheckBox);

        // Year Section
        Label yearLabel = new Label("Year:");
        yearLabel.setStyle("-fx-font-family: 'Roboto', sans-serif; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox yearBox = new HBox(10);
        CheckBox year2024CheckBox = new CheckBox("2024");
        CheckBox year2023CheckBox = new CheckBox("2023");
        CheckBox year2022CheckBox = new CheckBox("2022");
        CheckBox olderCheckBox = new CheckBox("older");
        styleCheckBoxes(year2024CheckBox, year2023CheckBox, year2022CheckBox, olderCheckBox);
        yearBox.getChildren().addAll(year2024CheckBox, year2023CheckBox, year2022CheckBox, olderCheckBox);

        // Apply Button
        Button applyButton = new Button("Apply");
        applyButton.setStyle("-fx-background-color: #40046a; -fx-text-fill: #FFFFFF; -fx-font-family: 'Roboto', sans-serif; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 8px; -fx-cursor: hand;");
        applyButton.setOnAction(event -> {
            user_filter.setGenres(getSelectedValues(actionCheckBox, dramaCheckBox, comedyCheckBox, horrorCheckBox));
            user_filter.setLanguage(getSelectedValues(englishCheckBox, arabicCheckBox, italianCheckBox));
            user_filter.setCountry(getSelectedValues(usaCheckBox, englandCheckBox, egyptCheckBox));
            user_filter.setType(getSelectedValues(movieCheckBox, seriesCheckBox));
            user_filter.setYears(getSelectedValues(year2024CheckBox, year2023CheckBox, year2022CheckBox, olderCheckBox));
            user_filter.setImdbRating(imdbSlider.getValue());


            try {
                new SearchForShow(user_filter,(Stage) applyButton.getScene().getWindow());
                closeFilterWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Layout
        VBox filterOptions = new VBox(15);
        // filterOptions.setPadding(new Insets(20));
        filterOptions.getChildren().addAll(
                genresLabel, genresBox,
                languageLabel, languageBox,
                imdbBox,
                countryLabel, countryBox,
                typeLabel, typeBox,
                yearLabel, yearBox, applyButton
        );
        filterOptions.setStyle("-fx-background-color: rgba(64,64,64,255);  -fx-background-radius:20px; -fx-padding: 20px;");
        return filterOptions;
    }

    // Add a method to close the filter window
    public void closeFilterWindow() {
        if(filterPane.getScene()!=null && filterPane.getScene().getWindow() != null) {
            Stage stage = (Stage) filterPane.getScene().getWindow();
            stage.close();
        }
    }
    private List<String> getSelectedValues(CheckBox... checkBoxes) {
        return Arrays.stream(checkBoxes)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toList());
    }
    private void styleCheckBoxes(CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setStyle("-fx-font-family: 'Roboto', sans-serif; -fx-font-size: 16px; -fx-text-fill: white;");
        }
    }
}