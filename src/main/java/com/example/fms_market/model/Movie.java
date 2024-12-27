package com.example.fms_market.model;

import java.util.ArrayList;
import java.util.List;


public class Movie extends Show {
    private int movie_id;
    private List<Movie> movies;

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public int getMovie_id() {
        return movie_id;
    }


    //display all movies
   public String getMovies(List<Movie>movies)
    {
        return movies.toString();
    }

    // Search function
    //criteria is (name or genre)
    public static List<Movie> searchMovies(List<Show> shows, String keyword, String criteria) throws Exception {
        List<Movie> results = new ArrayList<>();

        // Validate input
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty.");
        }

        for (Show show : shows) {
            if (show instanceof Movie movie) {  //check is the user enters a show that already exists
                if ("name".equalsIgnoreCase(criteria)) // Check if the user searches by name or genre
                {
                    if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                        results.add(movie);
                    }
                } else if ("genre".equalsIgnoreCase(criteria)) {
                    if (movie.getGenres().stream().anyMatch(genre -> genre.toLowerCase().contains(keyword.toLowerCase()))) {
                        results.add(movie);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid search criteria. Use 'name' or 'genre'.");
                }
            }
        }

        if (results.isEmpty()) {
            throw new Exception(STR."No movies found matching the criteria: \{criteria} with keyword: \{keyword}");
        }

        return results;
    }
}