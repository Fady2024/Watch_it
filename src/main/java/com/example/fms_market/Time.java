package com.example.fms_market;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Time {
    private int hours;
    private int minutes;

    @JsonCreator
    public Time(@JsonProperty("hours") int hours, @JsonProperty("minutes") int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    // Getters and setters

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hours, minutes);
    }
}