package com.example.fms_market.admin;

import com.example.fms_market.data.CastJsonHandler;
import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.pages.HomePage;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import java.sql.Date;
import java.util.List;

public class AddShow {

    private final User currentUser;
    private final Stage stage;

    public AddShow(User currentUser, Stage stage) throws IOException {
        this.currentUser = currentUser;
        this.stage = stage;
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

        //boolean to check for empty text fields
        boolean checkEmptyFields = false;

        //Add new Show Label
        Text addLabel = new Text("Add New Show");
        addLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),44));
        addLabel.setStyle("-fx-fill: white;");
        GridPane grid = new GridPane();
        grid.setHgap(45);
        grid.setVgap(10);

        //Add Title Field
        Text addTitleLabel = CreateLabel("Title");
        TextField addTitleField = CreateField("Title");
        grid.add(addTitleLabel,1,1);
        grid.add(addTitleField,1,2);

        //Add Release Date Field
        Text addDateLabel = CreateLabel("Release Date");
        TextField addDateField = CreateField("YYYY-MM-DD");
        grid.add(addDateLabel,2,1);
        grid.add(addDateField,2,2);

        //Add Duration Field
        Text addDurationLabel = CreateLabel("Duration");
        TextField addDurationField = CreateField("Duration");
        grid.add(addDurationLabel,1,3);
        grid.add(addDurationField,1,4);

        //Add Genres Field
        Text addGenresLabel = CreateLabel("Genres");
        TextField addGenresField = CreateField("genre 1,genre 2");
        grid.add(addGenresLabel,2,3);
        grid.add(addGenresField,2,4);

        //Add Director Field
        Text addDirectorLabel = CreateLabel("Director");
        TextField addDirectorField = CreateField("Director");
        grid.add(addDirectorLabel,1,5);
        grid.add(addDirectorField,1,6);

        //Add Language Field
        Text addLanguageLabel = CreateLabel("Language");
        TextField addLanguageField = CreateField("Language");
        grid.add(addLanguageLabel,2,5);
        grid.add(addLanguageField,2,6);

        //Add IMDb_Score Field
        Text addIMDbLabel = CreateLabel("IMDb Score(0-10)");
        addIMDbLabel.setStyle("-fx-fill: white");
        TextField addIMDbField = CreateField("IMDb Score");
        grid.add(addIMDbLabel,1,7);
        grid.add(addIMDbField,1,8);

        //Add Country Field
        Text addCountryLabel = CreateLabel("Country");
        TextField addCountryField = CreateField("Country");
        grid.add(addCountryLabel,2,7);
        grid.add(addCountryField,2,8);

        //Add Budget Field
        Text addBudgetLabel = CreateLabel("Budget");
        TextField addBudgetField = CreateField("Budget");
        grid.add(addBudgetLabel,1,9);
        grid.add(addBudgetField,1,10);

        //Add Revenue Field
        Text addRevenueLabel = CreateLabel("Revenue");
        TextField addRevenueField = CreateField("Revenue");
        grid.add(addRevenueLabel,2,9);
        grid.add(addRevenueField,2,10);

        //Add Poster Field
        Text addPosterLabel = CreateLabel("Poster (URL)");
        TextField addPosterField = CreateField("URL");
        grid.add(addPosterLabel,1,11);
        grid.add(addPosterField,1,12);

        //Add Video Field
        Text addVideoLabel = CreateLabel("Video(URL): ");
        TextField addVideoField = CreateField("Video");
        grid.add(addVideoLabel,2,11);
        grid.add(addVideoField,2,12);

        //Select Type
        Text selectTypeLabel = CreateLabel("Type");
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
        TextArea addDescField = new TextArea();
        addDescField.setPrefWidth(300);
        addDescField.setPrefHeight(158);
        addDescField.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;" +
                " -fx-border-width: 1;-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
        addDescField.setPromptText("Description");
        grid.add(addDescLabel,2,13);
        grid.add(addDescField,2,14);
        grid.add(radio,1,13);

        GridPane castGrid = new GridPane();
        castGrid.setHgap(20);
        castGrid.setVgap(10);

        //Cast Label
        Text castLabel = new Text("Cast");
        castLabel.setFont(Font.font("Thoma", 28));
        castLabel.setStyle("-fx-fill: white;");
        castGrid.add(castLabel,1,1);
        Text maxLabel = addActorLabel("Maximum 10 Actors(Full name)");
        castGrid.add(maxLabel,1,2);
        List<String> Cast = new ArrayList<>();

        //Actor 1
        TextField actor1Field = addActorField("Actor 1");
        Button actor1Button = addActorButton();
        actor1Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor1Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor1Field.getText()))
                            {
                                Cast.add(actor1Field.getText());
                                actor1Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor1Field,1,3);
        castGrid.add(actor1Button,2,3);
        //Actor 2
        TextField actor2Field = addActorField("Actor 2");
        Button actor2Button = addActorButton();
        actor2Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor2Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor2Field.getText()))
                            {
                                Cast.add(actor2Field.getText());
                                actor2Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor2Field,3,3);
        castGrid.add(actor2Button,4,3);

        //Actor 3
        TextField actor3Field = addActorField("Actor 3");
        Button actor3Button = addActorButton();
        actor3Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor3Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor3Field.getText()))
                            {
                                Cast.add(actor3Field.getText());
                                actor3Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor3Field,1,4);
        castGrid.add(actor3Button,2,4);

        //Actor 4
        TextField actor4Field = addActorField("Actor 4");
        Button actor4Button = addActorButton();
        actor4Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor4Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor4Field.getText()))
                            {
                                Cast.add(actor4Field.getText());
                                actor4Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor4Field,3,4);
        castGrid.add(actor4Button,4,4);

        //Actor 5
        TextField actor5Field = addActorField("Actor 5");
        Button actor5Button = addActorButton();
        actor5Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor5Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor5Field.getText()))
                            {
                                Cast.add(actor5Field.getText());
                                actor5Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor5Field,1,5);
        castGrid.add(actor5Button,2,5);

        //Actor 6
        TextField actor6Field = addActorField("Actor 6");
        Button actor6Button = addActorButton();
        actor6Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor6Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor6Field.getText()))
                            {
                                Cast.add(actor6Field.getText());
                                actor6Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor6Field,3,5);
        castGrid.add(actor6Button,4,5);

        //Actor 7
        TextField actor7Field = addActorField("Actor 7");
        Button actor7Button = addActorButton();
        actor7Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor7Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor7Field.getText()))
                            {
                                Cast.add(actor7Field.getText());
                                actor7Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor7Field,1,6);
        castGrid.add(actor7Button,2,6);

        //Actor 8
        TextField actor8Field = addActorField("Actor 8");
        Button actor8Button = addActorButton();
        actor8Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor8Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor8Field.getText()))
                            {
                                Cast.add(actor8Field.getText());
                                actor8Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor8Field,3,6);
        castGrid.add(actor8Button,4,6);

        //Actor 9
        TextField actor9Field = addActorField("Actor 9");
        Button actor9Button = addActorButton();
        actor9Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor9Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor9Field.getText()))
                            {
                                Cast.add(actor9Field.getText());
                                actor9Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor9Field,1,7);
        castGrid.add(actor9Button,2,7);

        //Actor 10
        TextField actor10Field = addActorField("Actor 10");
        Button actor10Button = addActorButton();
        actor10Button.setOnMouseClicked(e->
                {
                    if (hasSpace(actor10Field.getText()) == -1) {
                        showAlert("Wrong name", "Please enter full name");
                    } else {
                        try {
                            if(castExists(actor10Field.getText()))
                            {
                                Cast.add(actor10Field.getText());
                                actor10Field.setDisable(true);
                            }
                            else
                                addActorPopup();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
        );
        castGrid.add(actor10Field,3,7);
        castGrid.add(actor10Button,4,7);

        //Create List of actors to save actors
        List<String> actors= new ArrayList<>();

        //Create Buttons Grid
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(20);
        buttonsGrid.setVgap(20);

        //Add Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        cancelButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5px; -fx-background-color: #ffffff;");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(59);
        cancelButton.setOnMouseClicked(e -> new HomePage(currentUser,stage));
        buttonsGrid.add(cancelButton,1,0);

        //Add Save Button
        Button saveButton = new Button("Save");
        saveButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        saveButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5px; -fx-background-color: #8D5BDC;");
        saveButton.setPrefWidth(150);
        saveButton.setPrefHeight(59);
        buttonsGrid.add(saveButton,2,0);
        saveButton.setOnMouseClicked(e -> {
            if(addTitleField.getText().isEmpty()||addDateField.getText().isEmpty()||addDurationField.getText().isEmpty()
                    ||addGenresField.getText().isEmpty()||addDirectorField.getText().isEmpty()||addIMDbField.getText().isEmpty()
                    ||addLanguageField.getText().isEmpty()||addCountryField.getText().isEmpty()||addBudgetField.getText().isEmpty()
                    ||addRevenueField.getText().isEmpty()||addPosterField.getText().isEmpty()||addVideoField.getText().isEmpty()
                    ||addDescField.getText().isEmpty())
            {
                showAlert("Some Fields are Empty", "Please Fill All the Fields and Try Again");
            }
            else if(Integer.parseInt(addIMDbField.getText())>10||Integer.parseInt(addIMDbField.getText())<0)
                showAlert("Wrong Input","IMDb Score Must be From 0-10");
            else if(Cast.isEmpty())
            {
                showAlert("Cast Fields are Empty", "Please Add Cast and Try Again");
            }
            else if(movieButton.isSelected())
            {
                Movie movie = new Movie();
                movie.setTitle(addTitleField.getText());
                try {
                    movie.setDate(Date.valueOf(addDateField.getText()));
                }
                catch (IllegalArgumentException exp)
                {
                    showAlert("Wrong Input","Please Enter Date With the Right Format");
                }
                try {
                    movie.setDuration(Integer.parseInt(addDurationField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In Duration Field");
                }
                movie.setGenres(Arrays.asList(addGenresField.getText().split("\\s*,\\s*")));
                try {
                    if(hasSpace(addDirectorField.getText())==-1)
                    {
                    showAlert("Wrong Director Name","Please Enter Fullname");
                    }
                    else {
                        checkDirector(addDirectorField.getText(), movie);
                        addShowToDirector(addDirectorField.getText(),addTitleField.getText());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                movie.setLanguage(Arrays.asList(addLanguageField.getText().split("\\s*,\\s*")));
                try {
                    movie.setImdb_score(Double.parseDouble(addIMDbField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In IMDb Field");
                }
                movie.setCountry(addCountryField.getText());
                try {
                    movie.setBudget(Integer.parseInt(addBudgetField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In Budget Field");
                }
                try {
                    movie.setBudget(Integer.parseInt(addRevenueField.getText()));
                }
                catch (NumberFormatException ex) {
                    showAlert("Wrong Input", "Please Enter Only Integers In Budget Field");
                }
                movie.setPoster(addPosterField.getText());
                movie.setVideo(addVideoField.getText());
                movie.setType("movie");
                movie.setDescription(addDescLabel.getText());
                try {
                    if(directorExists(addDirectorField.getText())) {
                        try {
                            ShowJsonHandler.saveShow(movie);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                //add show to cast
                try {
                    if(!(hasSpace(actor1Field.getText())==-1)&&!(actor1Field.getText()).isEmpty()) {
                        if (castExists(actor1Field.getText()))
                            addShowToCast(actor1Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor2Field.getText())==-1)&&!(actor2Field.getText()).isEmpty()) {
                        if (castExists(actor2Field.getText())) {
                            addShowToCast(actor2Field.getText(), addTitleField.getText());
                        }
                    }
                    if(!(hasSpace(actor3Field.getText())==-1)&&!(actor3Field.getText()).isEmpty()) {
                        if (castExists(actor3Field.getText()))
                            addShowToCast(actor3Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor4Field.getText())==-1)&&!(actor4Field.getText()).isEmpty()) {
                        if (castExists(actor4Field.getText()))
                            addShowToCast(actor4Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor5Field.getText())==-1)&&!(actor5Field.getText()).isEmpty()) {
                        if (castExists(actor5Field.getText()))
                            addShowToCast(actor5Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor6Field.getText())==-1)&&!(actor6Field.getText()).isEmpty()) {
                        if (castExists(actor6Field.getText()))
                            addShowToCast(actor6Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor7Field.getText())==-1)&&!(actor7Field.getText()).isEmpty()) {
                        if (castExists(actor7Field.getText()))
                            addShowToCast(actor7Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor8Field.getText())==-1)&&!(actor8Field.getText()).isEmpty()) {
                        if (castExists(actor8Field.getText()))
                            addShowToCast(actor8Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor9Field.getText())==-1)&&!(actor9Field.getText()).isEmpty()) {
                        if (castExists(actor9Field.getText()))
                            addShowToCast(actor9Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor10Field.getText())==-1)&&!(actor10Field.getText()).isEmpty()) {
                        if (castExists(actor10Field.getText()))
                            addShowToCast(actor10Field.getText(), addTitleField.getText());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                movie.setCast(Cast);
            }
            else if(seriesButton.isSelected())
            {
                Series series = new Series();
                series.setTitle(addTitleField.getText());
                try {
                    series.setDate(Date.valueOf(addDateField.getText()));
                }
                catch (IllegalArgumentException exp)
                {
                    showAlert("Wrong Input","Please Enter Date With the Right Format");
                }
                try {
                    series.setDuration(Integer.parseInt(addDurationField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In Duration Field");
                }
                series.setGenres(Arrays.asList(addGenresField.getText().split("\\s*,\\s*")));
                try {
                    if(!(hasSpace(addDirectorField.getText())==-1))
                        checkDirector(addDirectorField.getText(),series);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                series.setLanguage(Arrays.asList(addLanguageField.getText().split("\\s*,\\s*")));
                try {
                    series.setImdb_score(Double.parseDouble(addIMDbField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In IMDb Field");
                }
                series.setCountry(addCountryField.getText());
                try {
                    series.setBudget(Integer.parseInt(addBudgetField.getText()));
                }
                catch (NumberFormatException ex)
                {
                    showAlert("Wrong Input","Please Enter Only Integers In Budget Field");
                }
                try {
                    series.setBudget(Integer.parseInt(addRevenueField.getText()));
                }
                catch (NumberFormatException ex) {
                    showAlert("Wrong Input", "Please Enter Only Integers In Budget Field");
                }
                series.setPoster(addPosterField.getText());
                series.setVideo(addVideoField.getText());
                series.setType("movie");
                series.setDescription(addDescLabel.getText());
                try {
                    if(directorExists(addDirectorField.getText())) {
                        try {
                            ShowJsonHandler.saveShow(series);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                //add show to cast
                try {
                    if(!(hasSpace(actor1Field.getText())==-1)&&!(actor1Field.getText()).isEmpty()) {
                        if (castExists(actor1Field.getText()))
                            addShowToCast(actor1Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor2Field.getText())==-1)&&!(actor2Field.getText()).isEmpty()) {
                        if (castExists(actor2Field.getText())) {
                            addShowToCast(actor2Field.getText(), addTitleField.getText());
                        }
                    }
                    if(!(hasSpace(actor3Field.getText())==-1)&&!(actor3Field.getText()).isEmpty()) {
                        if (castExists(actor3Field.getText()))
                            addShowToCast(actor3Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor4Field.getText())==-1)&&!(actor4Field.getText()).isEmpty()) {
                        if (castExists(actor4Field.getText()))
                            addShowToCast(actor4Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor5Field.getText())==-1)&&!(actor5Field.getText()).isEmpty()) {
                        if (castExists(actor5Field.getText()))
                            addShowToCast(actor5Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor6Field.getText())==-1)&&!(actor6Field.getText()).isEmpty()) {
                        if (castExists(actor6Field.getText()))
                            addShowToCast(actor6Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor7Field.getText())==-1)&&!(actor7Field.getText()).isEmpty()) {
                        if (castExists(actor7Field.getText()))
                            addShowToCast(actor7Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor8Field.getText())==-1)&&!(actor8Field.getText()).isEmpty()) {
                        if (castExists(actor8Field.getText()))
                            addShowToCast(actor8Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor9Field.getText())==-1)&&!(actor9Field.getText()).isEmpty()) {
                        if (castExists(actor9Field.getText()))
                            addShowToCast(actor9Field.getText(), addTitleField.getText());
                    }
                    if(!(hasSpace(actor10Field.getText())==-1)&&!(actor10Field.getText()).isEmpty()) {
                        if (castExists(actor10Field.getText()))
                            addShowToCast(actor10Field.getText(), addTitleField.getText());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                series.setCast(Cast);
            }
        });

        int column=0,row=0;
        showContainer.add(backLabel,column+3,row);
        showContainer.add(addLabel,column+3,row+2);
        showContainer.add(grid,column+3,row+4);
        showContainer.add(castGrid,column+3,row+5);
        showContainer.add(buttonsGrid,column+3,row+6);
    }

    private Text CreateLabel(String text)
    {
        Text label = new Text(text);
        label.setStyle("-fx-fill: white");
        label.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        return label;
    }

    private TextField CreateField(String text)
    {
        TextField field = new TextField();
        field.setPromptText(text);
        field.setPrefWidth(300);
        field.setPrefHeight(64);
        field.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        return field;
    }

    private void addActorPopup() {
        Cast cast = new Cast();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(10);
        VBox fnBox = new VBox(10);
        VBox lnBox = new VBox(10);
        VBox texts = new VBox(10);

        Text existText = new Text("This actor doesn't exist");
        existText.setFill(Paint.valueOf("#B5B5B5"));
        existText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),14));
        Text addNewText = new Text("Add new Actor");
        addNewText.setFill(Paint.valueOf("white"));
        addNewText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),20));
        texts.getChildren().addAll(existText,addNewText);

        Text firstnameLabel = CreateLabel("Firstname");
        TextField firstnameField = CreateField("Firstname");
        firstnameField.setPrefSize(180,60);
        fnBox.getChildren().addAll(firstnameLabel,firstnameField);

        Text lastnameLabel = CreateLabel("Lastname");
        TextField lastnameField = CreateField("Lastname");
        lastnameField.setPrefSize(180,60);
        lnBox.getChildren().addAll(lastnameLabel,lastnameField);

        VBox ageBox = new VBox(10);
        Text ageLabel = CreateLabel("Age");
        TextField ageField = CreateField("Age");
        ageField.setPrefSize(380,60);
        ageBox.getChildren().addAll(ageLabel,ageField);

        VBox genderBox = new VBox(10);
        Text genderLabel = CreateLabel("Gender");
        TextField genderField = CreateField("Gender");
        genderField.setPrefSize(380,60);
        genderBox.getChildren().addAll(genderLabel,genderField);

        VBox nationalityBox = new VBox(10);
        Text nationalityLabel = CreateLabel("Nationality");
        TextField nationalityField = CreateField("Nationality");
        nationalityField.setPrefSize(380,60);
        nationalityBox.getChildren().addAll(nationalityLabel,nationalityField);

        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
        "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: white;");
        cancelButton.setPrefSize(180,50);
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: #8D5BDC;");
        confirmButton.setPrefSize(180,50);


        cancelButton.setOnAction(e -> popupStage.close());
        confirmButton.setOnAction(e -> {
            cast.setFirst_name(firstnameField.getText());
            cast.setLast_name(lastnameField.getText());
            cast.setAge(Integer.parseInt(ageField.getText()));
            cast.setGender(genderField.getText());
            cast.setNationality(nationalityField.getText());
            try {
                CastJsonHandler.saveCast(cast);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            showAlert("Success","Actor Added Succesfully");
            popupStage.close();
        });
        HBox nameBox = new HBox(20);
        nameBox.setAlignment(Pos.CENTER);
        nameBox.getChildren().addAll(fnBox,lnBox);
        popupVBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(texts,nameBox,ageBox,genderBox,nationalityBox, buttonBox);
        popupVBox.setPadding(new Insets(30,60,30,60));
        popupVBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 500, 600);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Add New Actor");
        popupStage.show();
    }

    private void addDirectorPopup() {
        Director director = new Director();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(10);
        VBox fnBox = new VBox(10);
        VBox lnBox = new VBox(10);
        VBox texts = new VBox(10);

        Text existText = new Text("This director doesn't exist");
        existText.setFill(Paint.valueOf("#B5B5B5"));
        existText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),14));
        Text addNewText = new Text("Add new Director");
        addNewText.setFill(Paint.valueOf("white"));
        addNewText.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),20));
        texts.getChildren().addAll(existText,addNewText);

        Text firstnameLabel = CreateLabel("Firstname");
        TextField firstnameField = CreateField("Firstname");
        firstnameField.setPrefSize(180,60);
        fnBox.getChildren().addAll(firstnameLabel,firstnameField);

        Text lastnameLabel = CreateLabel("Lastname");
        TextField lastnameField = CreateField("Lastname");
        lastnameField.setPrefSize(180,60);
        lnBox.getChildren().addAll(lastnameLabel,lastnameField);

        VBox ageBox = new VBox(10);
        Text ageLabel = CreateLabel("Age");
        TextField ageField = CreateField("Age");
        ageField.setPrefSize(380,60);
        ageBox.getChildren().addAll(ageLabel,ageField);

        VBox genderBox = new VBox(10);
        Text genderLabel = CreateLabel("Gender");
        TextField genderField = CreateField("Gender");
        genderField.setPrefSize(380,60);
        genderBox.getChildren().addAll(genderLabel,genderField);

        VBox nationalityBox = new VBox(10);
        Text nationalityLabel = CreateLabel("Nationality");
        TextField nationalityField = CreateField("Nationality");
        nationalityField.setPrefSize(380,60);
        nationalityBox.getChildren().addAll(nationalityLabel,nationalityField);

        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: white;");
        cancelButton.setPrefSize(180,50);
        Button confirmButton = new Button("Confirm");
        confirmButton.setStyle("-fx-background-radius: 15; -fx-border-radius: 15;-fx-border-width: 1;" +
                "-fx-padding: 5px;-fx-font-size: 18px;-fx-background-color: #8D5BDC;");
        confirmButton.setPrefSize(180,50);



        cancelButton.setOnAction(e -> popupStage.close());
        confirmButton.setOnAction(e -> {
            director.setFirstName(firstnameField.getText());
            director.setLastName(lastnameField.getText());
            director.setAge(Integer.parseInt(ageField.getText()));
            director.setGender(genderField.getText());
            director.setNationality(nationalityField.getText());
            try {
                DirectorJsonHandler.saveDirector(director);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            showAlert("Success","Director Added Succesfully");
            popupStage.close();
        });
        HBox nameBox = new HBox(20);
        nameBox.setAlignment(Pos.CENTER);
        nameBox.getChildren().addAll(fnBox,lnBox);
        popupVBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(texts,nameBox,ageBox,genderBox,nationalityBox, buttonBox);
        popupVBox.setPadding(new Insets(30,60,30,60));
        popupVBox.setStyle("-fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 500, 600);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Add New Director");
        popupStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Text addActorLabel(String text)
    {
        Text label = new Text(text);
        label.setStyle("-fx-fill: white");
        label.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        return label;
    }

    private TextField addActorField(String text)
    {
        TextField field = new TextField();
        field.setPromptText(text);
        field.setPrefWidth(300);
        field.setPrefHeight(64);
        field.setStyle("-fx-background-radius: 20; -fx-border-radius: 20;-fx-border-width: 1;" +
                "-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        return field;
    }

    private Button addActorButton()
    {
        Button button = new Button("Add");
        button.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),14));
        button.setPrefHeight(50);
        button.setPrefWidth(80);
        button.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5px; -fx-background-color: white;");
        return button;
    }

    private void checkDirector(String fullname, Show show) throws IOException {
        String[] name = fullname.split(" ");
        String firstname = name[0];
        String lastname = name[1];
        List<Director> allDirectors = DirectorJsonHandler.readDirectors();
        boolean directorExists = false;
        int index = 0;
        for (int i = 0; i < allDirectors.size(); i++) {
            if ((allDirectors.get(i).getFirstName().toLowerCase() + allDirectors.get(i).getLastName().toLowerCase()).equals(firstname.toLowerCase() + lastname.toLowerCase())) {
                 directorExists = true;
                 index = i;
                break;
            }
        }
        if(directorExists)
        {
            show.setDirector(allDirectors.get(index));
        }
        else
            addDirectorPopup();
    }

    public boolean directorExists(String fullname) throws IOException {
        if(hasSpace(fullname)==-1)
        {
            showAlert("Wrong Director Name","Please Enter Fullname");
        }
        else {
            fullname = fullname.toLowerCase();
            String[] name = fullname.split(" ");
            String firstname = name[0];
            String lastname = name[1];
            List<Director> allDirectors = DirectorJsonHandler.readDirectors();
            for (int i = 0; i < allDirectors.size(); i++) {
                if ((allDirectors.get(i).getFirstName().toLowerCase() + allDirectors.get(i).getLastName().toLowerCase()).equals(firstname + lastname)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean castExists(String fullname) throws IOException {
        if(hasSpace(fullname)==-1)
        {
            showAlert("Wrong Cast Name","Please Enter Fullname");
        }
        else {
            fullname = fullname.toLowerCase();
            String[] name = fullname.split(" ");
            String firstname = name[0];
            String lastname = name[1];
            List<Cast> allCast = CastJsonHandler.readCast();
            for (int i = 0; i < allCast.size(); i++) {
                if ((allCast.get(i).getFirst_name().toLowerCase() + allCast.get(i).getLast_name().toLowerCase()).equals(firstname + lastname)) {
                    return true;
                }
            }
        }
        return false;
    }
    private int hasSpace(String name)
    {
        return name.indexOf(" ");
    }
    private void addShowToDirector(String fullname,String title) throws IOException {
        String[] name = fullname.split(" ");
        String firstname = name[0];
        String lastname = name[1];
        List<Director> allDirectors = DirectorJsonHandler.readDirectors();
        boolean directorExists = false;
        int index = 0;
        for (int i = 0; i < allDirectors.size(); i++) {
            if ((allDirectors.get(i).getFirstName().toLowerCase() + allDirectors.get(i).getLastName().toLowerCase()).equals(firstname.toLowerCase() + lastname.toLowerCase())) {
                directorExists = true;
                index = i;
                break;
            }
        }
        if(directorExists)
        {
            List <String> shows = new ArrayList<>();
            shows = allDirectors.get(index).getShows();
            shows.add(title);
            allDirectors.get(index).setShows(shows);
            DirectorJsonHandler.saveDirector(allDirectors.get(index));
        }
    }
    private void addShowToCast(String fullname,String title) throws IOException {
        String[] name = fullname.split(" ");
        String firstname = name[0];
        String lastname = name[1];
        List<Cast> allCast = CastJsonHandler.readCast();
        boolean directorExists = false;
        int index = 0;
        for (int i = 0; i < allCast.size(); i++) {
            if ((allCast.get(i).getFirst_name().toLowerCase() + allCast.get(i).getLast_name().toLowerCase()).equals(firstname.toLowerCase() + lastname.toLowerCase())) {
                directorExists = true;
                index = i;
                break;
            }
        }
        if(directorExists)
        {
            List <String> shows = new ArrayList<>();
            shows = allCast.get(index).getShows();
            shows.add(title);
            allCast.get(index).setShows(shows);
            CastJsonHandler.saveCast(allCast.get(index));
        }
    }
}