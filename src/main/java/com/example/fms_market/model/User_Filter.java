package com.example.fms_market.model;

import com.example.fms_market.model.Movie;
import com.example.fms_market.model.Series;
import com.example.fms_market.model.Show;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class User_Filter {

    private List<String> genres = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private double imdbRating;
    private List<String> countries = new ArrayList<>();
    private List<String> types = new ArrayList<>(); // Movie or series
    private List<String> years = new ArrayList<>();  // years of selected movies


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
        return allShows.stream()
                .filter(show -> show instanceof Movie)
                .map(show -> (Movie) show)
                .filter(movie -> genres.isEmpty() || genres.stream().anyMatch(genre -> movie.getGenres().contains(genre)))
                .filter(movie -> languages.isEmpty() || languages.stream().anyMatch(language -> movie.getLanguage().contains(language)))
                .filter(movie -> movie.getImdb_score() >= imdbRating)
                .filter(movie -> countries.isEmpty() || countries.contains(movie.getCountry()))
                .filter(movie -> types.isEmpty() || types.contains(movie.getType()))
                .filter(movie -> years.isEmpty() || years.contains(String.valueOf(movie.getDate().getYear() + 1900)))
                .collect(Collectors.toList());
    }

    public List<Series> filterSeries(List<Show> allShows) {
        return allShows.stream()
                .filter(show -> show instanceof Series)
                .map(show -> (Series) show)
                .filter(series -> genres.isEmpty() || genres.stream().anyMatch(genre -> series.getGenres().contains(genre)))
                .filter(series -> languages.isEmpty() || languages.stream().anyMatch(language -> series.getLanguage().contains(language)))
                .filter(series -> series.getImdb_score() >= imdbRating)
                .filter(series -> countries.isEmpty() || countries.contains(series.getCountry()))
                .filter(series -> types.isEmpty() || types.contains(series.getType()))
                .filter(series -> years.isEmpty() || years.contains(String.valueOf(series.getDate().getYear() + 1900)))
                .collect(Collectors.toList());
    }
}


