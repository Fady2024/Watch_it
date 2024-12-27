package com.example.fms_market.admin;

import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.HomePage;
import com.example.fms_market.model.*;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.util.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import static com.example.fms_market.util.ShowCardUtil.SHOW_CARD_WIDTH;

public class UpdateShowPage {

    private final ComboBox<String> showSelector = new ComboBox<>();
    private final User user;
    private final Stage stage;
   // private  final Show selectedShow;
    private final TableView<Show> showDetailsTable = new TableView<>();

    public UpdateShowPage(User user, Stage stage) throws InterruptedException {
        this.user = user;
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

        // Add movies and series to the scene
        //addShowsToContainer(showContainer, allMovies, allSeries);


// Load all shows into the showSelector
        showSelector.getItems().setAll(
                loadShowsFromFile().stream()
                        .map(Show::getTitle)
                        .collect(Collectors.toList())
        );


        // Listen for show selection changes
        showSelector.setOnAction(_ -> {
            String selectedTitle = showSelector.getValue();
            showDetailsTable.getItems().setAll(
                    loadShowsFromFile().stream()
                            .filter(show -> show.getTitle().equals(selectedTitle))
                            .collect(Collectors.toList())
            );
        });


        // Update Button
        Button updateButton = new Button(LanguageManager.getLanguageBasedString("Show aktualisieren","Update Show"));
        updateButton.setOnAction(_ -> {
            String selectedTitle = showSelector.getValue();
            if (selectedTitle == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "No show selected.");
                return;
            }


            Show selectedShow = loadShowsFromFile().stream()
                    .filter(show -> show.getTitle().equals(selectedTitle))
                    .findFirst()
                    .orElse(null);

            if (selectedShow == null) {
                showAlert(Alert.AlertType.ERROR, LanguageManager.getLanguageBasedString("Fehler", "Error"), LanguageManager.getLanguageBasedString("Anzeige nicht gefunden.", "Show not found."));
                return;
            }
            new Thread(() -> {
                try {
                    // update the show
                    ShowJsonHandler.updateShow(selectedShow);

                    // Reload data on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        reloadShowData();
                        showAlert(Alert.AlertType.INFORMATION, LanguageManager.getLanguageBasedString("Erfolg","Success"), LanguageManager.getLanguageBasedString("Show erfolgreich entfernt.","Show removed successfully."));
                        // Refresh all pages
                        refreshAllPages();
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        showAlert(Alert.AlertType.ERROR, LanguageManager.getLanguageBasedString("Fehler","Error"), LanguageManager.getLanguageBasedString("Die Sendung konnte nicht entfernt werden.","Failed to remove the show."));
                        e.printStackTrace();
                    });
                }
            }).start();
        });


        // Scene setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(showContainer, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Show aktualisieren", "Update Show"));
        stage.show();
        List<Movie> finalAllMovies = allMovies;
        List<Series> finalAllSeries = allSeries;
        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(showContainer, newValue.doubleValue(),finalAllMovies,finalAllSeries , user, stage));
        adjustLayout(showContainer, stageWidth, allMovies,allSeries, user, stage);
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


    private void adjustLayout(GridPane showContainer, double width, List<Movie>allMovies,List<Series>allSeries, User user, Stage stage) {



        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        HBox adminBox = new HBox(10);

        // Back Icon
        Image backIconImage = new Image("Acount/stash_arrow-left-solid.png");
        ImageView backIconView = new ImageView(backIconImage);
        backIconView.setFitWidth(40);
        backIconView.setFitHeight(40);

        // Back button
        Text backLabel = new Text("Back");
        backLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 32));
        backLabel.setStyle("-fx-fill: white;");

        HBox backBox = new HBox(10);
        backBox.getChildren().addAll(backIconView, backLabel);
        backBox.setStyle("-fx-cursor: hand;");
        backBox.setOnMouseClicked(e -> new HomePage(user, stage));
//boolean to check for empty text fields
        boolean checkEmptyFields = false;

        // update  Show Label
        Text updateLabel = new Text("Select a Show to Update:");
        updateLabel.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 34));
        updateLabel.setStyle("-fx-fill: white;");
        GridPane grid = new GridPane();
        grid.setHgap(45);
        grid.setVgap(10);
        VBox title = new VBox(10);
        title.getChildren().addAll(backBox, updateLabel);

        TextField searchField = new TextField();
        searchField.setPrefWidth(700);
        searchField.setPrefHeight(60);
        searchField.setStyle("-fx-background-radius: 50; -fx-border-radius: 50;-fx-border-width: 1;" +
                "-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        searchField.setPromptText(LanguageManager.getLanguageBasedString("\uD83D\uDD0D Suche nach Stichwort", "\uD83D\uDD0D Search by keyword"));
        searchField.setFont(Font.font("Arial", 15));

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
        titleBox.getChildren().addAll(backBox, updateLabel);
        showContainer.add(titleBox, column, row);
        showContainer.add(searchField, column + 1, row);
        showContainer.setHgap(27);
        showContainer.setVgap(20);
        showContainer.add(RecentMovies, column, row + 3);
        showContainer.add(RecentSeries, column, row + 6);

        HBox showMoviesCardContainer = new HBox(30);

        for (Movie movie : allMovies) {
            VBox showCard = ShowCardUtil.createDeleteShowCard(movie, user, stage);
            showMoviesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showMoviesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
            showCard.setOnMouseReleased(e->displayUpdateForm(movie));
        }
        showContainer.add(showMoviesCardContainer, column, row + 5);

        HBox showSeriesCardContainer = new HBox(30);
        for (Series series : allSeries) {
            VBox showCard = ShowCardUtil.createDeleteShowCard(series, user, stage);
            showSeriesCardContainer.getChildren().add(showCard); // Add buttons to the HBox
            showSeriesCardContainer.setAlignment(Pos.CENTER_LEFT); // Align buttons to the left
            showContainer.setHgap(27);
            showContainer.setVgap(20);
            //column++;
            if (column == columns) {
                row++;
            }
            showCard.setOnMouseReleased(e->displayUpdateForm(series));
        }
        showContainer.add(showSeriesCardContainer, column, row + 8);
        //showSeriesCardContainer.setOnMouseClicked(e-> confirmDeletePopup());
    }

    private void displayUpdateForm(Show show) {
        // Create a new stage for the update form
//        Stage updateStage = new Stage();
//        updateStage.initModality(Modality.APPLICATION_MODAL);
//        updateStage.initOwner(stage);

        // Create a grid pane for the form
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #1c1c1c;");
        // Add fields and labels to the grid


        TextField titleField = new TextField(show.getTitle());
        TextField dateField = new TextField(show.getDate().toString());
        TextField durationField = new TextField(String.valueOf(show.getDuration()));
        TextField genresField = new TextField(String.join(", ", show.getGenres()));
        TextField directorField = new TextField(STR."\{show.getDirector().getFirstName()} \{show.getDirector().getLastName()}");
        TextField languageField = new TextField(String.join(", ", show.getLanguage()));
        TextField imdbField = new TextField(String.valueOf(show.getImdb_score()));
        TextField countryField = new TextField(show.getCountry());
        TextField budgetField = new TextField(String.valueOf(show.getBudget()));
        TextField revenueField = new TextField(String.valueOf(show.getRevenue()));
        TextField posterField = new TextField(show.getPoster());
        TextField videoField = new TextField(show.getVideo());
        TextArea descField = new TextArea(show.getDescription());



        grid.add(createLabel(LanguageManager.getLanguageBasedString("Titel", "Title")), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Veröffentlichungsdatum", "Release Date")), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Dauer", "Duration")), 0, 2);
        grid.add(durationField, 1, 2);
        grid.add(createLabel("Genres"), 0, 3);
        grid.add(genresField, 1, 3);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Direktorin", "Director")), 0, 4);
        grid.add(directorField, 1, 4);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Sprache", "Language")), 0, 5);
        grid.add(languageField, 1, 5);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("IMDb Punktzahl(0-10)", "IMDb Score(0-10)")), 0, 6);
        grid.add(imdbField, 1, 6);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Land", "Country")), 0, 7);
        grid.add(countryField, 1, 7);
        grid.add(createLabel("Budget"), 0, 8);
        grid.add(budgetField, 1, 8);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Einnahmen", "Revenue")), 0, 9);
        grid.add(revenueField, 1, 9);
        grid.add(createLabel("Poster (URL)"), 0, 10);
        grid.add(posterField, 1, 10);
        grid.add(createLabel("Video (URL)"), 0, 11);
        grid.add(videoField, 1, 11);
        grid.add(createLabel(LanguageManager.getLanguageBasedString("Beschreibung", "Description")), 0, 12);
        grid.add(descField, 1, 12);




// Add text fields for each cast member
        List<String> cast = show.getCast();
        for (int i = 0; i < cast.size(); i++) {
            TextField castField = new TextField(cast.get(i));
          //  grid.add(createLabel("Cast Member " + (i + 1) + ":"), 0, 13 + i);
            grid.add(createLabel(LanguageManager.getLanguageBasedString(STR."Cast Member \{i + 1}:", STR."Cast Member \{i + 1}:")), 0, 13 + i);
            grid.add(castField, 1, 13 + i);
        }
        //Select Type
        Label selectTypeLabel = new Label("Type");
        ToggleGroup type = new ToggleGroup();
        RadioButton movieButton = new RadioButton("Movie");
        movieButton.setToggleGroup(type);
        movieButton.setStyle("-fx-text-fill: white");
        movieButton.setFont(new Font("Thoma", 14));
        movieButton.setSelected(true);
        RadioButton seriesButton = new RadioButton("Series");
        seriesButton.setStyle("-fx-text-fill: white");
        seriesButton.setFont(new Font("Thoma", 14));
        seriesButton.setToggleGroup(type);
        GridPane radio = new GridPane();
        radio.setHgap(40);
        radio.setVgap(20);
        radio.add(selectTypeLabel, 0, 0);
        radio.add(movieButton, 0, 1);
        radio.add(seriesButton, 1, 1);


        // Create Buttons Grid
        HBox buttonsBox = new HBox(20);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));
        buttonsBox.setAlignment(Pos.CENTER);


//Add Cancel Button
        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        cancelButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5px; -fx-background-color: #ffffff;");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(59);
        cancelButton.setOnMouseClicked(e -> {
            try {
                new UpdateShowPage(user, stage);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        buttonsBox.getChildren().add(cancelButton);

        //Add Save Button
        Button saveButton = new Button("Save");
        saveButton.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        saveButton.setStyle("-fx-text-fill: black; -fx-background-radius: 20; -fx-border-radius: 20;" +
                "-fx-border-width: 1;-fx-padding: 5px; -fx-background-color: #8D5BDC;");
        saveButton.setPrefWidth(150);
        saveButton.setPrefHeight(59);
        buttonsBox.getChildren().add(saveButton);
        // Add save button

        saveButton.setOnAction(e -> {
            if (titleField.getText().isEmpty() || dateField.getText().isEmpty() || durationField.getText().isEmpty()
                    || genresField.getText().isEmpty() || directorField.getText().isEmpty() || imdbField.getText().isEmpty()
                    || languageField.getText().isEmpty() || countryField.getText().isEmpty() || budgetField.getText().isEmpty()
                    || revenueField.getText().isEmpty() || posterField.getText().isEmpty() || videoField.getText().isEmpty()
                    || descField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Some Fields are Empty", "Please Fill All the Fields and Try Again");
            } else if (Double.parseDouble(imdbField.getText()) > 10 || Double.parseDouble(imdbField.getText()) < 0) {
                showAlert(Alert.AlertType.ERROR, "Wrong Input", "IMDb Score Must be From 0-10");
            } else if (cast.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Cast Fields are Empty", "Please Add Cast and Try Again");
            } else {
                show.setTitle(titleField.getText());
                try {
                    show.setDate(Date.valueOf(dateField.getText()));
                } catch (IllegalArgumentException exp) {
                    showAlert(Alert.AlertType.ERROR, "Wrong Input", "Please Enter Date With the Right Format");
                    return;
                }
                try {
                    show.setDuration(Integer.parseInt(durationField.getText()));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Wrong Input", "Please Enter Only Integers In Duration Field");
                    return;
                }
                show.setGenres(Arrays.asList(genresField.getText().split("\\s*,\\s*")));
                try {
                    if (hasSpace(directorField.getText()) == -1) {
                        showAlert(Alert.AlertType.ERROR, "Wrong Director Name", "Please Enter Fullname");
                        return;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                show.setLanguage(Arrays.asList(languageField.getText().split("\\s*,\\s*")));
                try {
                    show.setImdb_score(Double.parseDouble(imdbField.getText()));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Wrong Input", "Please Enter Only Integers In IMDb Field");
                    return;
                }
                show.setCountry(countryField.getText());
                try {
                    show.setBudget(Integer.parseInt(budgetField.getText()));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Wrong Input", "Please Enter Only Integers In Budget Field");
                    return;
                }
                try {
                    show.setRevenue(Integer.parseInt(revenueField.getText()));
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Wrong Input", "Please Enter Only Integers In Revenue Field");
                    return;
                }
                show.setPoster(posterField.getText());
                show.setVideo(videoField.getText());
                show.setDescription(descField.getText());

                // Save the updated show
                try {
                    ShowJsonHandler.saveShow(show);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Show updated successfully.");
                    new UpdateShowPage(user, stage);
                } catch (IOException ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the show.");
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(grid, buttonsBox);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("HinzufügenAnzeigen","Update Show"));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Text createLabel(String text) {
        Text label = new Text(text);
        label.setStyle("-fx-fill: white");
        label.setFont(Font.loadFont(Objects.requireNonNull(getClass().getResource("/LexendDecaRegular.ttf")).toString(), 16));
        return label;
    }
    private int hasSpace(String name) throws IOException {
        int space = name.indexOf(" ");
        if (space == -1) {
            showAlert(Alert.AlertType.ERROR, "Wrong Director Name", "Please Enter Fullname");
            return -1;
        }
        return space;
    }
    private int getShowIdByTitle(String title) {
        return loadShowsFromFile().stream()
                .filter(show -> show.getTitle().equals(title))
                .findFirst()
                .map(Show::getId)
                .orElse(-1);
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
