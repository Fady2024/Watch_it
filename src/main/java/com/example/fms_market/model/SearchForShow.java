package com.example.fms_market.model;

import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.*;
import com.example.fms_market.pages.Sidebar;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import animatefx.animation.SlideInDown;
import animatefx.animation.SlideOutUp;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchForShow {

    private final User_Filter user_filter; //to call functions in user_filter
   // private final User user;

    private final String keyword;
    private final BorderPane layout = new BorderPane(); // to organize UI components
    private static boolean isFilterWindowVisible = false;
    private static VBox filterPane;
    private static VBox mainSection;

    public SearchForShow(User user, Stage stage, String keyword) throws Exception {
        this.user_filter = null; // No user filter in this constructor
        this.keyword = keyword;
        this.mainSection = new VBox();
        MovieSearch( stage,user);
    }

    // Constructor for invoking from user_filter
    public SearchForShow(User_Filter userFilter, Stage stage) throws Exception {
        this.user_filter = userFilter;
        this.keyword = null; // No keyword in this constructor
        this.mainSection = new VBox();
        MovieSearch(stage, Banner.currentUser);
    }

    public void MovieSearch(Stage stage,User user) throws Exception {
       // Sidebar sidebar = new Sidebar(Sidebar.SidebarState.HOME, stage, null); // Create an instance of Sidebar
      // Call the non-static method on the instance


        List<Show> allShows = ShowJsonHandler.readShows(); 
        List<Movie> Movie_results=ShowJsonHandler.readMovies();
        List<Series> seriesResults=ShowJsonHandler.readSeries();

     try {
            if (keyword != null) {
                Movie_results = searchMoviesByKeyword(Movie_results, keyword.toLowerCase(),user,stage);
                seriesResults = searchSeriesByKeyword(seriesResults, keyword.toLowerCase(),user,stage);
            } if(user_filter!=null){
                Movie_results = user_filter.filterMovies(allShows);
                seriesResults = user_filter.filterSeries(allShows);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Movie_results = List.of();
            // Set to empty list if no movies found
            seriesResults = List.of(); // Set to empty list if no series found
        }

        GridPane movieGrid = new GridPane();
        movieGrid.setPadding(new Insets(20));
        movieGrid.setHgap(20);
        movieGrid.setVgap(20);
        movieGrid.setAlignment(Pos.TOP_CENTER);
        movieGrid.setStyle("-fx-background-color: #1c1c1c;");

        GridPane seriesGrid = new GridPane();
        seriesGrid.setPadding(new Insets(20));
        seriesGrid.setHgap(20);
        seriesGrid.setVgap(20);
        seriesGrid.setAlignment(Pos.TOP_CENTER);
        seriesGrid.setStyle("-fx-background-color: #1c1c1c;");

        // Filter button
        Button filterButton = new Button();
        filterButton.setStyle("-fx-background-color: #451952; -fx-text-fill: white; -fx-background-radius: 20px; -fx-cursor: hand;");
        filterButton.setVisible(false); // Initially hidden
        try {
            String imagePath = Objects.requireNonNull(SearchForShow.class.getResource("/filter.png")).toExternalForm();
            javafx.scene.image.Image filterImage = new Image(imagePath);
            ImageView filterIcon = new ImageView(filterImage);
            filterIcon.setFitWidth(20);
            filterIcon.setFitHeight(20);
            filterButton.setGraphic(filterIcon);
        } catch (RuntimeException e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        // HBox banner = Banner.getBanner(stage, keyword!=null ? "Search Results for: " + keyword : "Filtered Results");
        Label searchedForLabel = new Label(LanguageManager.getLanguageBasedString("Sie haben nach: \"" + keyword + "\" gesucht", "You searched for: \"" + keyword + "\""));
        searchedForLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        searchedForLabel.setVisible(false); // Initially hidden

// New HBox for the filter button and label
        HBox filterBox = new HBox(10); // Spacing between elements
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10, 20, 10, 20)); // Padding around the HBox
        filterBox.setStyle("-fx-background-color: #1c1c1c;"); // Match the background color
        filterBox.getChildren().addAll(filterButton, searchedForLabel);
// Set visibility based on the keyword
        if (keyword != null && !keyword.isEmpty()) {
            filterButton.setVisible(true);
            searchedForLabel.setVisible(true);
        }
        filterButton.setOnAction(event -> {
            if (isFilterWindowVisible) {
                SlideOutUp slideOutUp = new SlideOutUp(filterPane);
                slideOutUp.setOnFinished(e -> {
                    mainSection.getChildren().remove(filterPane);
                    filterPane.setVisible(false);
                    mainSection.requestLayout();
                    movieGrid.setTranslateY(0);
                    seriesGrid.setTranslateY(0);
                });
                slideOutUp.play();
                isFilterWindowVisible = false;
            } else {
                if (filterPane == null) {
                    filterPane = new Filter_Window(new User_Filter()).getFilterVBox();
                }
                mainSection.getChildren().add(1, filterPane); // Add below the banner (index 1)
                filterPane.setVisible(true);

                SlideInDown slideInDownFilterPane = new SlideInDown(filterPane);
                slideInDownFilterPane.play();

                movieGrid.setTranslateY(filterPane.getHeight());
                seriesGrid.setTranslateY(filterPane.getHeight());
                SlideInDown slideInDownMovieGrid = new SlideInDown(movieGrid);
                slideInDownMovieGrid.play();

                SlideInDown slideInDownSeriesGrid = new SlideInDown(seriesGrid);
                slideInDownSeriesGrid.play();

                isFilterWindowVisible = true;
            }
        });

// Add banner, filterBox, and movie/series grids to a new VBox
        VBox contentBox = new VBox();
        contentBox.getChildren().addAll(movieGrid, seriesGrid);
        contentBox.setSpacing(10);

// Add all components to the mainSection
        mainSection.getChildren().addAll(
                Banner.getBanner(stage, LanguageManager.getLanguageBasedString(
                        "Suchergebnisse für: " + (keyword != null ? keyword : ""),
                        "Search Results for: " + (keyword != null ? keyword : "")
                ))
, // Banner
                filterBox, // Filter section
                contentBox // Movie/Series content
        );

        if (Movie_results.isEmpty() && seriesResults.isEmpty()) {
            Label notFoundLabel = new Label(LanguageManager.getLanguageBasedString(
                    "Keine Filme oder Serien gefunden",
                    "No movies or series found"
            ));
            notFoundLabel.setTextFill(Color.WHITE);
            notFoundLabel.setFont(Font.font("Arial", 20));
            movieGrid.add(notFoundLabel, 0, 0);
        } else if (seriesResults.isEmpty()) {

            Label movieLabel = new Label(LanguageManager.getLanguageBasedString(
                    "Filme",
                    "Movies"
            ));
            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(movieLabel, movieGrid);
            displayMovies(Movie_results, movieGrid, user, stage);

        }
        else if(Movie_results.isEmpty()) {
            Label seriesLabel = new Label(LanguageManager.getLanguageBasedString(
                    "Serien",
                    "Series"
            ));
            seriesLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(seriesLabel, seriesGrid);
            displaySeries(seriesResults, seriesGrid, user, stage);
        }
        else
        {
            Label movieLabel = new Label(LanguageManager.getLanguageBasedString(
                    "Filme",
                    "Movies"
            ));

            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");
            Label seriesLabel = new Label(LanguageManager.getLanguageBasedString(
                    "Serien",
                    "Series"
            ));
            seriesLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(movieLabel, movieGrid, seriesLabel, seriesGrid);
            displayMovies(Movie_results, movieGrid, user, stage);
            displaySeries(seriesResults, seriesGrid, user, stage);

        }

        ScrollPane scrollPane = new ScrollPane(mainSection);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: #1c1c1c;");
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1c1c1c; -fx-background-color: #1c1c1c; -fx-border-color:transparent;");

        BorderPane layout = new BorderPane();
        layout.setCenter(scrollPane);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString(
                "Suchergebnisse für: " + (keyword != null ? keyword : "Gefilterte Ergebnisse"),
                "Search Results for: " + (keyword != null ? keyword : "Filtered Results")
        ));
        stage.show();
    }

    private List<Movie> searchMoviesByKeyword(List<Movie> movies, String keyword) throws Exception {
        List<Movie> results = movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().startsWith(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            results = movies.stream()
                    .filter(movie -> {
                        try {
                            return movie.getDirector().getFirstName().toLowerCase().startsWith(keyword) ||
                                    movie.getDirector().getLastName().toLowerCase().startsWith(keyword) ||
                                    movie.getCast().stream().anyMatch(cast -> cast.toLowerCase().startsWith(keyword));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return results;
    }

    private List<Series> searchSeriesByKeyword(List<Series> seriesList, String keyword) throws Exception {
        List<Series> results = seriesList.stream()
                .filter(series -> series.getTitle().toLowerCase().startsWith(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            results = seriesList.stream()
                    .filter(series -> {
                        try {
                            return series.getDirector().getFirstName().toLowerCase().startsWith(keyword) ||
                                    series.getDirector().getLastName().toLowerCase().startsWith(keyword) ||
                                    series.getCast().stream().anyMatch(cast -> cast.toLowerCase().startsWith(keyword));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }
        return results;
    }

    private void displayMovies(List<Movie> movies, GridPane movieGrid, User user , Stage stage) {
        int column = 0, row = 0;


        for (Movie movie : movies) {
            VBox movieCard = ShowCardUtil.createShowCard(movie,user, stage, () -> {
            });
            movieGrid.add(movieCard, column++, row);

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private void displaySeries(List<Series> seriesList, GridPane seriesGrid, User user , Stage stage) {
        int column = 0, row = 0;
        for (Series series : seriesList) {
            VBox seriesCard = ShowCardUtil.createShowCard(series, user, stage, () -> {
            });
            seriesGrid.add(seriesCard, column++, row);

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}
