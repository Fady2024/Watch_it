package com.example.fms_market.model;

import com.example.fms_market.model.Movie;
import com.example.fms_market.model.Series;
import com.example.fms_market.model.Show;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.logging.Logger;
public class User_Filter {

    private List<String> genres = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private double imdbRating;
    private List<String> countries = new ArrayList<>();
    private List<String> types = new ArrayList<>(); // Movie or series
    private List<String> years = new ArrayList<>();  // years of selected movies
    private static final Logger logger = Logger.getLogger(User_Filter.class.getName());

    // Getters and Setters
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public List<String> getGenres() {
        return genres;
    }

    public List<String> getLanguages() {
        return languages;
    }
    public void setLanguage(List<String> languages) {
        this.languages = languages;
    }

    public double getImdbRating() {
        return imdbRating;
    }
    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public List<String> getCountry() {
        return countries;
    }
    public void setCountry(List<String> country) {
        this.countries = countries;
    }

    public List<String> getType() {
        return types;
    }
    public void setType(List<String> type) {
        this.types = type;
    }

    public List<String> getYears() {
        return years;
    }
    public void setYears(List<String> years) {
        this.years = years;
    }
    public List<Movie> filterMovies(List<Show> allShows) {
        System.out.println("Starting filterMovies with " + allShows.size() + " shows");
        System.out.println("Types: " + types);

        List<Movie> movies = allShows.stream()
                .filter(show -> show instanceof Movie)
                .peek(show -> System.out.println("Filtered Movie: " + show.getTitle()))
                .filter(show -> types.isEmpty() || types.contains("Movie"))
                .map(show -> (Movie) show)
                .filter(movie -> genres.isEmpty() || genres.stream().anyMatch(genre -> movie.getGenres().contains(genre)))
                .peek(movie -> System.out.println("Filtered by genre: " + movie.getTitle()))
                .filter(movie -> languages.isEmpty() || languages.stream().anyMatch(language -> movie.getLanguage().contains(language)))
                .peek(movie -> System.out.println("Filtered by language: " + movie.getTitle()))
                .filter(movie -> movie.getImdb_score() >= imdbRating)
                .peek(movie -> System.out.println("Filtered by IMDB rating: " + movie.getTitle()))
                .filter(movie -> countries.isEmpty() || countries.contains(movie.getCountry()))
                .peek(movie -> System.out.println("Filtered by country: " + movie.getTitle()))
                .filter(movie -> years.isEmpty() || years.contains(String.valueOf(movie.getYear())))
                .peek(movie -> System.out.println("Filtered by year: " + movie.getTitle()))
                .collect(Collectors.toList());

        System.out.println("Finished filterMovies with " + movies.size() + " movies");
        return movies;
    }

    public List<Series> filterSeries(List<Show> allShows) {
        System.out.println("Starting filterSeries with " + allShows.size() + " shows");

        List<Series> seriesList = allShows.stream()
                .filter(show -> show instanceof Series)
                .peek(show -> System.out.println("Filtered Series: " + show.getTitle()))
                .filter(show -> types.isEmpty() || types.contains("Series"))
                .map(show -> (Series) show)
                .filter(series -> genres.isEmpty() || genres.stream().anyMatch(genre -> series.getGenres().contains(genre)))
                .peek(series -> System.out.println("Filtered by genre: " + series.getTitle()))
                .filter(series -> languages.isEmpty() || languages.stream().anyMatch(language -> series.getLanguage().contains(language)))
                .peek(series -> System.out.println("Filtered by language: " + series.getTitle()))
                .filter(series -> series.getImdb_score() >= imdbRating)
                .peek(series -> System.out.println("Filtered by IMDB rating: " + series.getTitle()))
                .filter(series -> countries.isEmpty() || countries.contains(series.getCountry()))
                .peek(series -> System.out.println("Filtered by country: " + series.getTitle()))
                .filter(series -> types.isEmpty() || types.contains(series.getType()))
                .peek(series -> System.out.println("Filtered by type: " + series.getTitle()))
                .filter(series -> years.isEmpty() || years.contains(String.valueOf(series.getYear())))
                .peek(series -> System.out.println("Filtered by year: " + series.getTitle()))
                .collect(Collectors.toList());

        System.out.println("Finished filterSeries with " + seriesList.size() + " series");
        return seriesList;
    }
}





