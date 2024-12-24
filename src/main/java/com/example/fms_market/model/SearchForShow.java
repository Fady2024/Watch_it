package com.example.fms_market.model;

import com.example.fms_market.data.CastJsonHandler;
import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.*;
import com.example.fms_market.pages.DetailsPageFX;
import com.example.fms_market.pages.Sidebar;
import com.example.fms_market.util.Banner;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
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

    public static final double SHOW_CARD_WIDTH = 200;
    private static final double SHOW_CARD_HEIGHT = 282.94;
    private static final double STAR_SIZE = 100;  // Increased size for the circle
    private static final double RADIUS = 40;



    public SearchForShow(User user, Stage stage, String keyword) throws Exception {
        this.user_filter = null; // No user filter in this constructor(for filtering show )
        this.keyword = keyword;
        this.mainSection = new VBox();
        MovieSearch(stage,user);
    }

    // Constructor for invoking from user_filter
    public SearchForShow(User_Filter userFilter, Stage stage) throws Exception {
        this.user_filter = userFilter;
        this.keyword = null; // No keyword in this constructor(for searching using keyword)
        this.mainSection = new VBox();
        MovieSearch(stage, Banner.currentUser);
    }

    public void MovieSearch(Stage stage,User user) throws Exception {

        List<Show> allShows = ShowJsonHandler.readShows(); //3shan fel filter
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
            Image filterImage = new Image(imagePath);
            ImageView filterIcon = new ImageView(filterImage);
            filterIcon.setFitWidth(20);
            filterIcon.setFitHeight(20);
            filterButton.setGraphic(filterIcon);
        } catch (RuntimeException e) {
            System.out.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
        // HBox banner = Banner.getBanner(stage, keyword!=null ? "Search Results for: " + keyword : "Filtered Results");
        Label searchedForLabel = new Label("You searched for: \"" + keyword + "\"");
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
                Banner.getBanner(stage, keyword != null ? "Search Results for: " + keyword : "SearchForShow", "SearchForShow"), // Banner
                filterBox, // Filter section
                contentBox // Movie/Series content
        );

        if (Movie_results.isEmpty() && seriesResults.isEmpty()) {
            Label notFoundLabel = new Label("No movies or series found");
            notFoundLabel.setTextFill(Color.WHITE);
            notFoundLabel.setFont(Font.font("Arial", 20));
            movieGrid.add(notFoundLabel, 0, 0);
        } else if (seriesResults.isEmpty()) {

            Label movieLabel = new Label("Movies");
            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(movieLabel, movieGrid);
            displayMovies(Movie_results, movieGrid, user, stage);

        }
        else if(Movie_results.isEmpty()) {
            Label seriesLabel = new Label("Series");
            seriesLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(seriesLabel, seriesGrid);
            displaySeries(seriesResults, seriesGrid, user, stage);
        }
        else
        {
            Label movieLabel = new Label("Movies");
            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");
            Label seriesLabel = new Label("Series");
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
        stage.setTitle(keyword != null ? "Search Results for: " + keyword : "Filtered Results");
        stage.show();
    }

    public static VBox createDirectorCard(Director director, User user, Stage stage) {
        VBox directorCard = new VBox(5);
        directorCard.setAlignment(Pos.TOP_CENTER);
        String imagePath = Objects.requireNonNull(Banner.class.getResource("/Acount/user.png")).toString();
        Image userImage = new Image(imagePath);
        ImageView profileView = new ImageView(userImage);
        Label name = new Label(director.getFirstName() + " " + director.getLastName());
        name.setFont(Font.loadFont(Objects.requireNonNull(ShowCardUtil.class.getResource("/LexendDecaRegular.ttf")).toString(), 14));
        name.setTextFill(Color.WHITE);
        name.setAlignment(Pos.CENTER);
        name.setWrapText(true);
        name.setMaxWidth(SHOW_CARD_WIDTH);

        Rectangle rectangle = new Rectangle(SHOW_CARD_WIDTH, SHOW_CARD_HEIGHT - 30);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);
        StackPane profileContainer = new StackPane(profileView, rectangle);
        profileContainer.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(rectangle, Pos.BOTTOM_CENTER);
        directorCard.getChildren().addAll(profileContainer, name);

        profileContainer.setOnMouseEntered(_ -> profileContainer.setStyle("-fx-background-color: #333333; -fx-border-color: #6A1B9A; -fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;"));

        profileContainer.setOnMouseExited(_ -> profileContainer.setStyle(""));

        rectangle.setOnMouseReleased(_ -> {
            directorCard.setStyle("");
            new DetailsPageFX(user, director.getFirstName() + " " + director.getLastName(), stage, null);
        });

        mainSection.getChildren().add(directorCard); // Add the card to the main section
        mainSection.requestLayout(); // Request layout update

        return directorCard;
    }

    public static VBox createCastCard(Cast cast, User user, Stage stage) {
        // Ensure the cast object and its fields are not null
        if (cast == null) {
            throw new IllegalArgumentException("Cast object cannot be null");
        }
        if (cast.getFirst_name() == null) {
            throw new IllegalArgumentException("Cast first name cannot be null");
        }
        if (cast.getLast_name() == null) {
            throw new IllegalArgumentException("Cast last name cannot be null");
        }

        // Create a vertical box for the cast card with spacing
        VBox castCard = new VBox(5);
        castCard.setAlignment(Pos.TOP_CENTER);

        // Load a default user image for the cast card
        String imagePath = "file:src/main/java/com/example/fms_market/single-person.png";
        Image userImage = new Image(imagePath);
        ImageView profileView = new ImageView(userImage);

        // Create a label for the cast's name and configure its appearance
        Label name = new Label(cast.getFirst_name() + " " + cast.getLast_name());
        name.setFont(Font.loadFont(Objects.requireNonNull(ShowCardUtil.class.getResource("/LexendDecaRegular.ttf")).toString(), 14));
        name.setTextFill(Color.WHITE);
        name.setAlignment(Pos.CENTER);
        name.setWrapText(true);
        name.setMaxWidth(SHOW_CARD_WIDTH);

        // Create a rectangle to add styling and effects to the profile image
        Rectangle rectangle = new Rectangle(SHOW_CARD_WIDTH, SHOW_CARD_HEIGHT - 30);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        // Create a stack pane to contain the profile image and rectangle
        StackPane profileContainer = new StackPane(profileView, rectangle);
        profileContainer.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(rectangle, Pos.BOTTOM_CENTER);

        // Add the profile container and name label to the cast card
        castCard.getChildren().addAll(profileContainer, name);

        // Add hover effects for the profile container
        profileContainer.setOnMouseEntered(_ -> profileContainer.setStyle(
                "-fx-background-color: #333333; -fx-border-color: #6A1B9A; " +
                        "-fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;"));

        profileContainer.setOnMouseExited(_ -> profileContainer.setStyle(""));

        // Add a click listener to the rectangle for showing details
        rectangle.setOnMouseReleased(_ -> {
            castCard.setStyle("");
            new DetailsPageFX(user, cast.getFirst_name() + " " + cast.getLast_name(), stage, null);
        });

        // Ensure the index is within bounds before adding the cast card
        int index = Math.min(5, mainSection.getChildren().size());
        mainSection.getChildren().add(index, castCard); // Add below the banner and filter box
        mainSection.requestLayout(); // Request layout update

        return castCard; // Return the created cast card
    }

    private List<Movie> searchMoviesByKeyword(List<Movie> movies, String keyword, User user, Stage stage) throws Exception {
        List<Director> directors = DirectorJsonHandler.readDirectors();
        List<Cast> castList = CastJsonHandler.readCast();

        List<Movie> results = movies.stream()
                .filter(movie -> movie.getTitle() != null && movie.getTitle().toLowerCase().startsWith(keyword.toLowerCase()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            results = movies.stream()
                    .filter(movie -> {
                        try {
                            return (movie.getDirector() != null && (
                                    movie.getDirector().getFirstName().toLowerCase().startsWith(keyword.toLowerCase()) ||
                                            movie.getDirector().getLastName().toLowerCase().startsWith(keyword.toLowerCase())
                            )) ||
                                    (movie.getCast() != null && movie.getCast().stream().anyMatch(castName ->
                                            castName.toLowerCase().startsWith(keyword.toLowerCase())
                                    ));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                boolean cardAdded = false;
                for (Director director : directors) {
                    if (director.getFirstName().toLowerCase().startsWith(keyword.toLowerCase()) ||
                            director.getLastName().toLowerCase().startsWith(keyword.toLowerCase())) {
                        VBox directorCard = createDirectorCard(director, user, stage);
                        if (!mainSection.getChildren().contains(directorCard)) {
                            mainSection.getChildren().add(directorCard);
                            cardAdded = true;
                        }
                    }
                }

                for (Cast cast : castList) {
                    if (cast.getFirst_name().toLowerCase().startsWith(keyword.toLowerCase()) ||
                            cast.getLast_name().toLowerCase().startsWith(keyword.toLowerCase())) {
                        VBox castCard = createCastCard(cast, user, stage);
                        if (!mainSection.getChildren().contains(castCard)) {
                            mainSection.getChildren().add(castCard);
                            cardAdded = true;
                        }
                    }
                }

                if (cardAdded) {
                    return List.of(); // Stop further processing if any card is added
                }
            }
        }
        return results;
    }

    private List<Series> searchSeriesByKeyword(List<Series> seriesList, String keyword, User user, Stage stage) throws Exception {
        List<Series> results = seriesList.stream()
                .filter(series -> series.getTitle() != null && series.getTitle().toLowerCase().startsWith(keyword.toLowerCase()))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            results = seriesList.stream()
                    .filter(series -> {
                        try {
                            return (series.getDirector() != null && (
                                    series.getDirector().getFirstName().toLowerCase().startsWith(keyword.toLowerCase()) ||
                                            series.getDirector().getLastName().toLowerCase().startsWith(keyword.toLowerCase())
                            )) ||
                                    (series.getCast() != null && series.getCast().stream().anyMatch(castName ->
                                            castName.toLowerCase().startsWith(keyword.toLowerCase())
                                    ));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            if (results.isEmpty()) {
                for (Series series : seriesList) {
                    if (series.getDirector() != null && (
                            series.getDirector().getFirstName().toLowerCase().startsWith(keyword.toLowerCase()) ||
                                    series.getDirector().getLastName().toLowerCase().startsWith(keyword.toLowerCase())
                    )) {
                        VBox directorCard = createDirectorCard(series.getDirector(), user, stage);
                        mainSection.getChildren().add(directorCard);
                        return List.of(); // Stop further processing
                    }

                    if (series.getCast() != null) {
                        for (String castName : series.getCast()) {
                            if (castName.toLowerCase().startsWith(keyword.toLowerCase())) {
                                VBox castCard = createCastCard(new Cast(castName.split(" ")[0], castName.split(" ")[1], null, null, 0, null), user, stage);
                                mainSection.getChildren().add(castCard);
                                return List.of(); // Stop further processing
                            }
                        }
                    }
                }
            }
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