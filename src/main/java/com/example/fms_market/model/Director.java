package com.example.fms_market.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Director {
    private String first_name;
    private String last_name;
    private List<String> shows;
    private String nationality;
    private String gender;
    private int age;


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
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;

    }

    public Director(){}

    @JsonProperty("first_name")
    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {this.first_name=first_name;}

    @JsonProperty("last_name")
    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {this.last_name=last_name;}

    public List<String> getShows() {return shows == null ? new ArrayList<>() : shows;}

    public void setShows(List<String> shows) {this.shows=shows;}

    public int getAge() {
        return age;
    }

    public void setAge(int age) {this.age = age;}

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {this.gender = gender;}

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {this.nationality = nationality;}

}
