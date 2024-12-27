package com.example.fms_market.pages;
import com.example.fms_market.data.CastJsonHandler;
import com.example.fms_market.data.DirectorJsonHandler;
import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.model.*;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.ShowCardUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
        SearchForShow.mainSection = new VBox();
        MovieSearch(stage, user);
    }

    public SearchForShow(User_Filter userFilter, Stage stage) throws Exception {
        this.user_filter = userFilter;
        this.keyword = null; // No keyword in this constructor(for searching using keyword)
        SearchForShow.mainSection = new VBox();
        MovieSearch(stage, Banner.currentUser);
    }

    public SearchForShow(User user, Stage stage) throws Exception {
        this.keyword = null;
        this.user_filter = null;
        SearchForShow.mainSection = new VBox();
        MovieSearch(stage,user);
    }

    public void MovieSearch(Stage stage, User user) throws Exception {
        List<Show> allShows = ShowJsonHandler.readShows();
        List<Movie> Movie_results = ShowJsonHandler.readMovies();
        List<Series> seriesResults = ShowJsonHandler.readSeries();

        try {
            if (keyword != null) {
                Movie_results = searchMoviesByKeyword(Movie_results, keyword.toLowerCase(), user, stage);
                seriesResults = searchSeriesByKeyword(seriesResults, keyword.toLowerCase(), user, stage);
            }
            if (user_filter != null) {
                Movie_results = user_filter.filterMovies(allShows);
                seriesResults = user_filter.filterSeries(allShows);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Movie_results = List.of();
            seriesResults = List.of();
        }

        FlowPane movieFlowPane = new FlowPane();
        movieFlowPane.setPadding(new Insets(20));
        movieFlowPane.setHgap(20);
        movieFlowPane.setVgap(20);
        movieFlowPane.setAlignment(Pos.TOP_CENTER);
        movieFlowPane.setStyle("-fx-background-color: #1c1c1c;");

        FlowPane seriesFlowPane = new FlowPane();
        seriesFlowPane.setPadding(new Insets(20));
        seriesFlowPane.setHgap(20);
        seriesFlowPane.setVgap(20);
        seriesFlowPane.setAlignment(Pos.TOP_CENTER);
        seriesFlowPane.setStyle("-fx-background-color: #1c1c1c;");

        Button filterButton = new Button();
        filterButton.setStyle("-fx-background-color: #451952; -fx-text-fill: white; -fx-background-radius: 20px; -fx-cursor: hand;");
        filterButton.setVisible(true);
        try {
            String imagePath = Objects.requireNonNull(SearchForShow.class.getResource("/filter.png")).toExternalForm();
            Image filterImage = new Image(imagePath);
            ImageView filterIcon = new ImageView(filterImage);
            filterIcon.setFitWidth(20);
            filterIcon.setFitHeight(20);
            filterButton.setGraphic(filterIcon);
        } catch (RuntimeException e) {
            System.out.println(STR."Error loading image: \{e.getMessage()}");
            e.printStackTrace();
        }

        Label searchedForLabel = new Label(STR."You searched for: \"\{keyword}\"");
        searchedForLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        searchedForLabel.setVisible(false);

        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10, 20, 10, 20));
        filterBox.setStyle("-fx-background-color: #1c1c1c;");
        filterBox.getChildren().addAll(filterButton, searchedForLabel);
        filterButton.setVisible(true);
        if (keyword != null && !keyword.isEmpty()) {
            searchedForLabel.setVisible(true);
        }
        filterButton.setOnAction(event -> {
            if (isFilterWindowVisible) {
                SlideOutUp slideOutUp = new SlideOutUp(filterPane);
                slideOutUp.setOnFinished(e -> {
                    mainSection.getChildren().remove(filterPane);
                    filterPane.setVisible(false);
                    mainSection.requestLayout();
                    movieFlowPane.setTranslateY(0);
                    seriesFlowPane.setTranslateY(0);
                });
                slideOutUp.play();
                isFilterWindowVisible = false;
            } else {
                if (filterPane == null) {
                    filterPane = new Filter_Window(new User_Filter()).getFilterVBox();
                }
                mainSection.getChildren().add(1, filterPane);
                filterPane.setVisible(true);

                SlideInDown slideInDownFilterPane = new SlideInDown(filterPane);
                slideInDownFilterPane.play();

                movieFlowPane.setTranslateY(filterPane.getHeight());
                seriesFlowPane.setTranslateY(filterPane.getHeight());
                SlideInDown slideInDownMovieFlowPane = new SlideInDown(movieFlowPane);
                slideInDownMovieFlowPane.play();

                SlideInDown slideInDownSeriesFlowPane = new SlideInDown(seriesFlowPane);
                slideInDownSeriesFlowPane.play();

                isFilterWindowVisible = true;
            }
        });

        VBox contentBox = new VBox();
        contentBox.getChildren().addAll(movieFlowPane, seriesFlowPane);
        contentBox.setSpacing(10);

        mainSection.getChildren().addAll(
                Banner.getBanner(stage, keyword != null ? STR."Search Results for: \{keyword}" : "SearchForShow", "SearchForShow"),
                filterBox,
                contentBox
        );

        if (Movie_results.isEmpty() && seriesResults.isEmpty()) {
            movieFlowPane.getChildren().clear();
            seriesFlowPane.getChildren().clear();

            List<Cast> castResults = CastJsonHandler.readCast().stream()
                    .filter(cast -> keyword != null && (cast.getFirst_name().toLowerCase().contains(keyword.toLowerCase())|| cast.getLast_name().toLowerCase().contains(keyword.toLowerCase())))
                    .collect(Collectors.toList());


            List<Director> directorResults = DirectorJsonHandler.readDirectors().stream()
                    .filter(director -> keyword != null && (director.getFirstName().toLowerCase().contains(keyword.toLowerCase()) ||
                            director.getLastName().toLowerCase().contains(keyword.toLowerCase())))
                    .collect(Collectors.toList());


            if (castResults.isEmpty() && directorResults.isEmpty()) {
                Label notFoundLabel = new Label("No movies, series, cast, or directors found");
                notFoundLabel.setTextFill(Color.WHITE);
                notFoundLabel.setFont(Font.font("Arial", 20));
                movieFlowPane.getChildren().add(notFoundLabel);
            } else {
                FlowPane castFlowPane = new FlowPane(20, 20);
                castFlowPane.setAlignment(Pos.TOP_LEFT);
                castFlowPane.setStyle("-fx-background-color: #1c1c1c;");

                FlowPane directorFlowPane = new FlowPane(20, 20);
                directorFlowPane.setAlignment(Pos.TOP_LEFT);
                directorFlowPane.setStyle("-fx-background-color: #1c1c1c;");
                if (!castResults.isEmpty()) {
                    filterButton.setVisible(false);
                    Label castLabel = new Label("Cast");
                    castLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");
                    castLabel.setTranslateX(20);
                    displayCast(castResults, castFlowPane, user, stage);
                    mainSection.getChildren().add(castLabel);
                    mainSection.getChildren().add(castFlowPane);
                }

                if (!directorResults.isEmpty()) {
                    filterButton.setVisible(false);
                    Label directorLabel = new Label("Directors");
                    directorLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");
                    directorLabel.setTranslateX(20);
                    displayDirectors(directorResults, directorFlowPane, user, stage);
                    mainSection.getChildren().add(directorLabel);
                    mainSection.getChildren().add(directorFlowPane);
                }
            }
        } else if (seriesResults.isEmpty()) {
            Label movieLabel = new Label("Movies");
            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(movieLabel, movieFlowPane);
            displayMovies(Movie_results, movieFlowPane, user, stage);
        } else if (Movie_results.isEmpty()) {
            Label seriesLabel = new Label("Series");
            seriesLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(seriesLabel, seriesFlowPane);
            displaySeries(seriesResults, seriesFlowPane, user, stage);
        } else {
            Label movieLabel = new Label("Movies");
            movieLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");
            Label seriesLabel = new Label("Series");
            seriesLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #FAF8F5; -fx-font-weight: bold;");

            mainSection.getChildren().addAll(movieLabel, movieFlowPane, seriesLabel, seriesFlowPane);
            displayMovies(Movie_results, movieFlowPane, user, stage);
            displaySeries(seriesResults, seriesFlowPane, user, stage);
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
        stage.setTitle(keyword != null ? STR."Search Results for: \{keyword}" : "Filtered Results");
        stage.show();
    }

    private void displayMovies(List<Movie> movies, FlowPane movieFlowPane, User user, Stage stage) {
        for (Movie movie : movies) {
            VBox movieCard = ShowCardUtil.createShowCard(movie, user, stage, () -> {
            });
            movieFlowPane.getChildren().add(movieCard);
        }
    }

    private void displaySeries(List<Series> seriesList, FlowPane seriesFlowPane, User user, Stage stage) {
        for (Series series : seriesList) {
            VBox seriesCard = ShowCardUtil.createShowCard(series, user, stage, () -> {
            });
            seriesFlowPane.getChildren().add(seriesCard);
        }
    }
    private static VBox createPersonCard(String firstName, String lastName, String gender, User user, Stage stage, Runnable onClick) {
        VBox personCard = new VBox(5);
        personCard.setAlignment(Pos.TOP_CENTER);
        Label name = new Label(STR."\{firstName} \{lastName}");
        name.setFont(Font.loadFont(Objects.requireNonNull(ShowCardUtil.class.getResource("/LexendDecaRegular.ttf")).toString(), 14));
        name.setTextFill(Color.WHITE);
        name.setAlignment(Pos.CENTER);
        name.setWrapText(true);
        name.setMaxWidth(SHOW_CARD_WIDTH);

        Rectangle rectangle = new Rectangle(SHOW_CARD_WIDTH, SHOW_CARD_HEIGHT - 30);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setArcWidth(20);
        rectangle.setArcHeight(20);

        String imagePath = gender.equalsIgnoreCase("male") ? "file:src/main/java/com/example/fms_market/man.png" : "file:src/main/java/com/example/fms_market/single-person.png";
        Image userImage = new Image(imagePath);
        ImageView profileView = new ImageView(userImage);
        profileView.setFitWidth(SHOW_CARD_WIDTH);
        profileView.setFitHeight(SHOW_CARD_HEIGHT - 30);

        StackPane profileContainer = new StackPane(profileView, rectangle);
        profileContainer.setAlignment(Pos.TOP_LEFT);
        StackPane.setAlignment(rectangle, Pos.BOTTOM_CENTER);

        personCard.getChildren().addAll(profileContainer, name);

        profileContainer.setOnMouseEntered(_ -> profileContainer.setStyle(
                "-fx-background-color: #333333; -fx-border-color: #6A1B9A; " +
                        "-fx-border-width: 3; -fx-border-radius: 15; -fx-background-radius: 15;"));

        profileContainer.setOnMouseExited(_ -> profileContainer.setStyle(""));

        rectangle.setOnMouseReleased(_ -> {
            personCard.setStyle("");
            onClick.run();
        });

        return personCard;
    }

    public static VBox createDirectorCard(Director director, User user, Stage stage) {
        return createPersonCard(director.getFirstName(), director.getLastName(), director.getGender(), user, stage, () -> new DetailsPageFX(user, STR."\{director.getFirstName()} \{director.getLastName()}", stage, null));
    }

    public static VBox createCastCard(Cast cast, User user, Stage stage) {
        return createPersonCard(cast.getFirst_name(), cast.getLast_name(), cast.getGender(), user, stage, () -> new DetailsPageFX(user, STR."\{cast.getFirst_name()} \{cast.getLast_name()}", stage, null));
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
        }
        return results;
    }

    private List<Series> searchSeriesByKeyword(List<Series> seriesList, String keyword, User user, Stage stage) {
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
        }
        return results;
    }

    private void displayCast(List<Cast> castList, FlowPane castFlowPane, User user, Stage stage) {
        for (Cast cast : castList) {
            VBox castCard = createCastCard(cast, user, stage);
            castFlowPane.getChildren().add(castCard);
        }
    }

    private void displayDirectors(List<Director> directorList, FlowPane directorFlowPane, User user, Stage stage) {
        for (Director director : directorList) {
            VBox directorCard = createDirectorCard(director, user, stage);
            directorFlowPane.getChildren().add(directorCard);
        }
    }
}