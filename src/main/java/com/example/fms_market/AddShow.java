package com.example.fms_market;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import java.sql.Date;
import java.util.List;
import javax.swing.text.Position;

class AddShow {

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
        Text addIMDbLabel = CreateLabel("IMDb Score");
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
                " -fx-border-width: 1;-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;" );
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

        //Actor 1
        Text actorLabel = addActorLabel("Actor 1");
        TextField actorField = addActorField("Actor 1");
        Button actorButton = addActorButton();
        int castRow = 2;
        castGrid.add(actorLabel,1,castRow);
        castGrid.add(actorField,1,castRow+1);
        castGrid.add(actorButton,2,castRow+1);

        //Create List of actors to save actors
        List<String> actors= new ArrayList<>();

        /*if button clicked check in cast.json file
        if found save in list if not add new*/
        actorButton.setOnMouseClicked( e-> {
            try {
                if(castExists(actorField.getText()))
                {
                    actors.add(actorField.getText());
                }
                else
                    addActorPopup();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

    });

        //Create Buttons Grid
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(20);
        buttonsGrid.setVgap(20);

        //Add Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        cancelButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5; -fx-background-color: #ffffff;");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(59);
        buttonsGrid.add(cancelButton,1,0);

        //Add Save Button
        Button saveButton = new Button("Save");
        saveButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        saveButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5; -fx-background-color: #8D5BDC;");
        saveButton.setPrefWidth(150);
        saveButton.setPrefHeight(59);
        buttonsGrid.add(saveButton,2,0);
        saveButton.setOnMouseClicked(e -> {
            if(movieButton.isSelected())
            {
                Movie movie = new Movie();
                movie.setTitle(addTitleField.getText());
                movie.setDate(Date.valueOf(addDateField.getText()));
                movie.setDuration(Integer.parseInt(addDurationField.getText()));
                movie.setGenres(Arrays.asList(addGenresField.getText().split("\\s*,\\s*")));
                try {
                    checkDirector(addDirectorField.getText(),movie);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                movie.setLanguage(Arrays.asList(addLanguageField.getText().split("\\s*,\\s*")));
                movie.setImdb_score(Double.parseDouble(addIMDbField.getText()));
                movie.setCountry(addCountryField.getText());
                movie.setBudget(Integer.parseInt(addBudgetField.getText()));
                movie.setRevenue(Integer.parseInt(addRevenueField.getText()));
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
            }
            if(seriesButton.isSelected())
            {
                Series series = new Series();
                series.setTitle(addTitleField.getText());
                series.setDate(Date.valueOf(addDateField.getText()));
                series.setDuration(Integer.parseInt(addDurationField.getText()));
                series.setGenres(Arrays.asList(addGenresField.getText().split("\\s*,\\s*")));
                try {
                    checkDirector(addDirectorField.getText(),series);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                series.setLanguage(Arrays.asList(addLanguageField.getText().split("\\s*,\\s*")));
                series.setImdb_score(Double.parseDouble(addIMDbField.getText()));
                series.setCountry(addCountryField.getText());
                series.setBudget(Integer.parseInt(addBudgetField.getText()));
                series.setRevenue(Integer.parseInt(addRevenueField.getText()));
                series.setPoster(addPosterField.getText());
                series.setVideo(addVideoField.getText());
                series.setType("series");
                series.setDescription(addDescLabel.getText());
                try {
                    ShowJsonHandler.saveShow(series);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        return field;
    }

    private void addActorPopup() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(10);

        Label oldPasswordLabel = new Label("Old Password:");
        oldPasswordLabel.setStyle("-fx-text-fill: white;");
        PasswordField oldPasswordField = new PasswordField();

        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-text-fill: white;");
        PasswordField newPasswordField = new PasswordField();

        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        Button confirmButton = new Button("Confirm");

        cancelButton.setOnAction(e -> popupStage.close());
        confirmButton.setOnAction(e -> {
            String oldPassword = oldPasswordField.getText();
            String newPassword = newPasswordField.getText();
            if (currentUser.getPassword().equals(oldPassword)) {
                currentUser.setPassword(newPassword);
                try {
                    UserJsonHandler.saveUser(currentUser);
                    showAlert("Success", "Password changed successfully.");
                    popupStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                showAlert("Error", "Old password is incorrect.");
            }
        });

        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(oldPasswordLabel, oldPasswordField, newPasswordLabel, newPasswordField, buttonBox);
        popupVBox.setStyle("-fx-padding: 20; -fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 600, 400);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Change Password");
        popupStage.show();
    }

    private void addDirectorPopup() {
        Director director = new Director();
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(stage);
        popupStage.initStyle(StageStyle.UNDECORATED);

        VBox popupVBox = new VBox(1);

        Text firstnameLabel = CreateLabel("Firstname");
        TextField firstnameField = CreateField("Firstname");

        Text lastnameLabel = CreateLabel("Lastname");
        TextField lastnameField = CreateField("Lastname");

        Text showsLabel = CreateLabel("Shows");
        TextField showsField = CreateField("show 1,show 2");

        Text ageLabel = CreateLabel("Age");
        TextField ageField = CreateField("Age");

        Text genderLabel = CreateLabel("Gender");
        TextField genderField = CreateField("Gender");

        Text nationalityLabel = CreateLabel("Nationality");
        TextField nationalityField = CreateField("Nationality");

        HBox buttonBox = new HBox(10);
        Button cancelButton = new Button("Cancel");
        Button confirmButton = new Button("Confirm");

        cancelButton.setOnAction(e -> popupStage.close());
        confirmButton.setOnAction(e -> {
            director.setFirstName(firstnameField.getText());
            director.setLastName(lastnameField.getText());
            director.setShows(Arrays.asList(showsField.getText().split("\\s*,\\s*")));
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

        buttonBox.getChildren().addAll(cancelButton, confirmButton);
        popupVBox.getChildren().addAll(firstnameLabel, firstnameField, lastnameLabel,lastnameField, showsLabel,
                showsField,ageLabel,ageField,genderLabel,genderField,nationalityLabel, buttonBox);
        popupVBox.setStyle("-fx-padding: 20; -fx-background-color: #2b2b2b; -fx-border-radius: 10; -fx-background-radius: 10;");

        Scene popupScene = new Scene(popupVBox, 600, 400);
        popupScene.setFill(javafx.scene.paint.Color.web("#1c1c1c"));
        popupStage.setScene(popupScene);
        popupStage.setTitle("Change Password");
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
                "-fx-padding: 5; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        return field;
    }

    private Button addActorButton()
    {
        Button button = new Button("Add");
        button.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(),16));
        button.setStyle("-fx-text-fill: white; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5; -fx-background-color: #3FC635;");
        button.setPrefWidth(64);
        button.setPrefHeight(64);
        return button;
    }

    private void checkDirector(String fullname,Show show) throws IOException {
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
        return false;
    }

    public boolean castExists(String fullname) throws IOException {
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
        return false;
    }
}