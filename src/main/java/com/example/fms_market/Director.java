package com.example.fms_market;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Director {
    private final String first_name;
    private final String last_name;
    private final List<String> movies;
    private final String nationality;
    private final String gender;
    private final int age;


    @JsonCreator
            public Director(
                    @JsonProperty("first_name") String first_name,
            @JsonProperty("last_name") String last_name,
            @JsonProperty("movies") List<String> movies,
            @JsonProperty("age") int age,
            @JsonProperty("gender") String gender,
            @JsonProperty("nationality") String nationality)
             {
        this.first_name = first_name;
        this.last_name = last_name;
        this.movies = movies;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;

    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return first_name;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return last_name;
    }

    public List<String> getMovies() {
        return movies;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getNationality() {
        return nationality;
    }

    public static List<String> searchDirectorByName(String keyword, List<Director> directors) throws Exception {
        List<String> results = new ArrayList<>();

        // Validate input
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty.");
        }

        // Search through directors
        for (Director director : directors) {
            if (director.getFirstName().toLowerCase().contains(keyword.toLowerCase())|| director.getLastName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add("Director: " + director.getFirstName() +" "+ director.getLastName() + " | Age:" +director.getAge() + " | List of movies:"+director.getMovies());
            }

        }

        // Handle no matches
        if (results.isEmpty()) {
            throw new Exception("No director found with the name: " + keyword);
        }

        return results;
    }

}
