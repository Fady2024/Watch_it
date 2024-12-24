
package com.example.fms_market.model;
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
        private List<String> shows;
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
            this.shows = shows;

        }
        public Cast(){};

        public String getFirst_name() {
            return first_name ;
        }

        public void setFirst_name(String first_name) {this.first_name = first_name;}

        public String getLast_name() {
            return last_name ;
        }

        public void setLast_name(String last_name) {this.last_name = last_name;}

        public int getAge() {return age;}

        public void setAge(int age) {this.age = age;}

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {this.gender = gender;}

        public List<String> getShows() {
            return shows == null ? new ArrayList<>() : shows;
        }

        public void setShows(List<String> shows) {this.shows = shows;}

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {this.nationality = nationality;}

}






