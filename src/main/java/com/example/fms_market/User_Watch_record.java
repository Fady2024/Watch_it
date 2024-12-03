package com.example.fms_market;

import java.util.Date;

public class User_Watch_record {
    private int user_id;
    private int movie_id;
    private Date date_of_watched;
    private Integer rating;

    public User_Watch_record(int user_id, int movie_id, Date date_of_watched) {
        this.user_id = user_id;
        this.movie_id = movie_id;
        this.date_of_watched = date_of_watched;
        this.rating=null;
    }

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public void setMovie(int movie_id) {this.movie_id = movie_id;}

    public Date getDate_of_watched() {return date_of_watched;}
    public void setDate_of_watched(Date date_of_watched) {this.date_of_watched = date_of_watched;}

    public Integer getRating() {return rating;}
    public void setRating(Integer rating) {
        if(rating<1||rating>5){
            throw new IllegalArgumentException("The rate must be in the range 1 to 5.");
        }
        this.rating=rating;
    }
}
