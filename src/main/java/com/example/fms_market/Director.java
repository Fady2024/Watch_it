package com.example.fms_market;

import java.util.ArrayList;
import java.util.List;

public class Director {
    private String first_name ;
    private String last_name ;
    private List<String> movies;
    private String nationality;
    private String gender;
    private int age;

    public Director(String first_name,String last_name,List<String>movies, int age, String gender, String nationality){
        this.first_name=first_name;
        this.last_name=last_name;
        this.movies=movies;
    }
    public String getFullName(){
        return first_name+" "+last_name;
    }

    public List<String> getMovies() {
        return movies;
    }

    public int getAge()
    {
        return age ;
    }
    public String getGender()
    {
        return gender;
    }
    public String getNationality()
    {
        return nationality ;
    }

    public static List<String> searchDirectorByName(String keyword, List<Director> directors) throws Exception {
        List<String> results = new ArrayList<>();

        // Validate input
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty.");
        }

        // Search through directors
        for (Director director : directors) {
            if (director.getFullName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add("Director: " + director.getFullName() + " | Age of Director: " +director.getAge() + " | List of movies:"+director.getMovies());
            }

        }

        // Handle no matches
        if (results.isEmpty()) {
            throw new Exception("No director found with the name: " + keyword);
        }

        return results;
    }

}