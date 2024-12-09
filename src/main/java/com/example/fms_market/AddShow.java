package com.example.fms_market;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.*;
import javafx.scene.control.TextArea;
import java.util.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

class AddShow {

    public AddShow(User currentUser, Stage stage) {
        GridPane showContainer = new GridPane();
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.setAlignment(Pos.TOP_LEFT);
        showContainer.setStyle("-fx-background-color: #1c1c1c;");

        ScrollPane scrollPane = new ScrollPane(showContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #1c1c1c;");

        BorderPane layout = new BorderPane();
        layout.setCenter(scrollPane);

        // Adjust stage dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("AddShow");
        stage.show();


        //Back button
        Text backLabel = new Text("Back");
        backLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),32));
        backLabel.setStyle("-fx-fill: white; -fx-cursor: hand;");
        backLabel.setOnMouseClicked(e -> new HomePage(currentUser,stage));

        //Add new Show Label
        Text addLabel = new Text("Add New Show");
        addLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),44));
        addLabel.setStyle("-fx-fill: white;");

        GridPane grid = new GridPane();
        grid.setHgap(45);
        grid.setVgap(10);

        //Add Title Field
        Text addTitleLabel = CreateLabel("Title");
        addTitleLabel.setStyle("-fx-fill: white;");
        TextField addTitleField = new TextField();
        addTitleField.setPromptText("Title");
        addTitleField.setPrefWidth(300);
        addTitleField.setPrefHeight(64);
        addTitleField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        grid.add(addTitleLabel,1,1);
        grid.add(addTitleField,1,2);

        //Add Release Date Field
        Text addDateLabel = CreateLabel("Release Date");
        addDateLabel.setStyle("-fx-fill: white;");
        TextField addDateField = new TextField();
        addDateField.setPrefWidth(300);
        addDateField.setPrefHeight(64);
        addDateField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        addDateField.setPromptText("YYYY-MM-DD");
        grid.add(addDateLabel,2,1);
        grid.add(addDateField,2,2);

        //Add Duration Field
        Text addDurationLabel = CreateLabel("Duration");
        addDurationLabel.setStyle("-fx-fill: white;");
        TextField addDurationField = new TextField();
        addDurationField.setPrefWidth(300);
        addDurationField.setPrefHeight(64);
        addDurationField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        addDurationField.setPromptText("Duration");
        grid.add(addDurationLabel,1,3);
        grid.add(addDurationField,1,4);

        //Add Genres Field
        Text addGenresLabel = CreateLabel("Genres");
        addGenresLabel.setStyle("-fx-fill: white");
        TextField addGenresField = new TextField();
        addGenresField.setPrefWidth(300);
        addGenresField.setPrefHeight(64);
        addGenresField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        addGenresField.setPromptText("genre 1,genre 2");
        grid.add(addGenresLabel,2,3);
        grid.add(addGenresField,2,4);

        //Add Director Field
        Text addDirectorLabel = CreateLabel("Director");
        addDirectorLabel.setStyle("-fx-fill: white");
        TextField addDirectorField = new TextField();
        addDirectorField.setPrefWidth(300);
        addDirectorField.setPrefHeight(64);
        addDirectorField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        addDirectorField.setPromptText("Director");
        grid.add(addDirectorLabel,1,5);
        grid.add(addDirectorField,1,6);

        //Add Language Field
        Text addLanguageLabel = CreateLabel("Language");
        addLanguageLabel.setStyle("-fx-fill: white");
        TextField addLanguageField = new TextField();
        addLanguageField.setPrefWidth(300);
        addLanguageField.setPrefHeight(64);
        addLanguageField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addLanguageField.setPromptText("Language");
        grid.add(addLanguageLabel,2,5);
        grid.add(addLanguageField,2,6);

        //Add IMDb_Score Field
        Text addIMDbLabel = CreateLabel("IMDb Score");
        addIMDbLabel.setStyle("-fx-fill: white");
        TextField addIMDbField = new TextField();
        addIMDbField.setPrefWidth(300);
        addIMDbField.setPrefHeight(64);
        addIMDbField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addIMDbField.setPromptText("IMDb Score");
        grid.add(addIMDbLabel,1,7);
        grid.add(addIMDbField,1,8);

        //Add Country Field
        Text addCountryLabel = CreateLabel("Country");
        addCountryLabel.setStyle("-fx-fill: white");
        TextField addCountryField = new TextField();
        addCountryField.setPrefWidth(300);
        addCountryField.setPrefHeight(64);
        addCountryField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addCountryField.setPromptText("Country");
        grid.add(addCountryLabel,2,7);
        grid.add(addCountryField,2,8);

        //Add Budget Field
        Text addBudgetLabel = CreateLabel("Budget");
        addBudgetLabel.setStyle("-fx-fill: white");
        TextField addBudgetField = new TextField();
        addBudgetField.setPrefWidth(300);
        addBudgetField.setPrefHeight(64);
        addBudgetField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addBudgetField.setPromptText("Budget");
        grid.add(addBudgetLabel,1,9);
        grid.add(addBudgetField,1,10);

        //Add Revenue Field
        Text addRevenueLabel = CreateLabel("Revenue");
        addRevenueLabel.setStyle("-fx-fill: white");
        TextField addRevenueField = new TextField();
        addRevenueField.setPrefWidth(300);
        addRevenueField.setPrefHeight(64);
        addRevenueField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addRevenueField.setPromptText("Revenue");
        grid.add(addRevenueLabel,2,9);
        grid.add(addRevenueField,2,10);

        //Add Poster Field
        Text addPosterLabel = CreateLabel("Poster (URL)");
        addPosterLabel.setStyle("-fx-fill: white");
        TextField addPosterField = new TextField();
        addPosterField.setPrefWidth(300);
        addPosterField.setPrefHeight(64);
        addPosterField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addPosterField.setPromptText("URL");
        grid.add(addPosterLabel,1,11);
        grid.add(addPosterField,1,12);

        //Add Video Field
        Text addVideoLabel = CreateLabel("Add Video(URL): ");
        addVideoLabel.setStyle("-fx-fill: white");
        TextField addVideoField = new TextField();
        addVideoField.setPromptText("Video");
        addVideoField.setPrefWidth(300);
        addVideoField.setPrefHeight(64);
        addVideoField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        grid.add(addVideoLabel,2,11);
        grid.add(addVideoField,2,12);

        //Select Type
        Text selectTypeLabel = CreateLabel("Type");
        selectTypeLabel.setStyle("-fx-fill: white");
        ToggleGroup type = new ToggleGroup();
        RadioButton movieButton = new RadioButton("Movie");
        movieButton.setToggleGroup(type);
        movieButton.setStyle("-fx-text-fill: white");
        movieButton.setFont(new Font("Thoma",14));
        movieButton.setSelected(true);
        RadioButton seriesButton = new RadioButton("Series");
        seriesButton.setStyle("-fx-text-fill: white");
        seriesButton.setFont(new Font("Thoma",14));
        seriesButton.setToggleGroup(type);
        GridPane radio = new GridPane();
        radio.setHgap(40);
        radio.setVgap(20);
        radio.add(selectTypeLabel,0,0);
        radio.add(movieButton,0,1);
        radio.add(seriesButton,1,1);

        //Add Description Field
        Text addDescLabel = CreateLabel("Description");
        addDescLabel.setStyle("-fx-fill: white");
        TextArea addDescField = new TextArea();
        addDescField.setPrefWidth(300);
        addDescField.setPrefHeight(158);
        addDescField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addDescField.setPromptText("Description");
        grid.add(addDescLabel,2,13);
        grid.add(addDescField,2,14);
        grid.add(radio,1,13);

        GridPane castGrid = new GridPane();
        castGrid.setHgap(45);
        castGrid.setVgap(2);

        //Cast Label
        Text castLabel = new Text("Cast");
        castLabel.setFont(Font.font("Thoma", 28));
        castLabel.setStyle("-fx-fill: white;");
        castGrid.add(castLabel,1,1);

        //Add Actor Button
        Button addActorButton = new Button("+ Add Actor");
        addActorButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        addActorButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5;");
        addActorButton.setPrefWidth(150);
        addActorButton.setPrefHeight(64);
        castGrid.add(addActorButton,1,15);

        addActorScene(addActorButton,stage);

        int column=0,row=0;
        showContainer.add(backLabel,column+3,row);
        showContainer.add(addLabel,column+3,row+2);
        showContainer.add(grid,column+3,row+4);
        showContainer.add(castGrid,column+3,row+5);
    }
    public Text CreateLabel(String text)
    {
        //Font lexendDeca = Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16);
        Text label = new Text(text);
        label.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        return label;
    }
    public void addActorScene(Button addActorButton,Stage stage)
    {

    }
}