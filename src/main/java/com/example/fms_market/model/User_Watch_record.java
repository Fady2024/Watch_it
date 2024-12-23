package com.example.fms_market.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User_Watch_record {
    private int user_id;
    private int show_id;
    private List<Integer> show_ids;
    private Date date_of_watched;
    private Integer rating;

    @JsonCreator
    public User_Watch_record(
            @JsonProperty("user_id") int user_id,
            @JsonProperty("movie_id") int show_id,
            @JsonProperty("date_of_watched") Date date_of_watched,
            @JsonProperty("rating") Integer rating) {
        this.user_id = user_id;
        this.show_id = show_id;
        this.date_of_watched = date_of_watched;
        this.rating = rating;
    }

    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }

    public int getShow_id() { return show_id; }
    public void setShow_id(int show_id) { this.show_id = show_id; }

    public Date getDate_of_watched() { return date_of_watched; }
    public void setDate_of_watched(Date date_of_watched) { this.date_of_watched = date_of_watched; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}