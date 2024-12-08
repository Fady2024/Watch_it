
package com.example.fms_market;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Cast {
        private String first_name;
        private String last_name;
        private String nationality;
        private List<String> movies;
        private String gender;
        private int age;

        @JsonCreator
        public Cast(
                @JsonProperty("first_name") String first_name,
                @JsonProperty("last_name") String last_name,
                @JsonProperty("nationality") String nationality,
                @JsonProperty("movies") List<String> movies,
                @JsonProperty("age") int age,
                @JsonProperty("gender") String gender
        )
        {
            this.first_name = first_name;
            this.last_name = last_name;
            this.nationality = nationality;
            this.age = age;
            this.gender = gender;
            this.movies = movies;

        }

        public String getFirst_name() {
            return first_name ;
        }
        public String getLast_name() {
            return last_name ;
        }
        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public List<String> getMovies() {
            return movies;
        }

        public String getNationality() {
            return nationality;
        }

public static List<Cast>SearchCast(List<Cast>casts,String keyword)
{
    if (keyword == null || keyword.trim().isEmpty()) {
        throw new IllegalArgumentException("Search keyword cannot be null or empty.");
    }
    List<Cast> Matched_casts = new ArrayList<>();
    for (Cast cast : casts) {
        if (cast.getFirst_name().toLowerCase().contains(keyword.toLowerCase())||cast.getLast_name().toLowerCase().contains(keyword.toLowerCase())) {
            Matched_casts.add(cast);
        }
    }
    // Handle no matches
    if (Matched_casts.isEmpty()) {
        throw new IllegalArgumentException("No cast found with the name: " + keyword);
    }

    return Matched_casts;
}



    }


