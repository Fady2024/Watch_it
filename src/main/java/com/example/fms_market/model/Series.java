package com.example.fms_market.model;

import java.util.ArrayList;
import java.util.List;
public class Series extends Show {
    private int series_id;
    private int series_episodes;
    public void setSeries_id(int series_id) {
        this.series_id = series_id;
    }

    public int getSeries_id() {
        return series_id;
    }

    public void setSeriesEp(int series_episodes) {
        this.series_episodes = series_episodes;
    }

    public int getSeriesEp() {
        return series_episodes;
    }



    // Search for seris
    public static List<Series> searchSeries(List<Show> shows, String keyword)  {
        List<Series> searchedResults = new ArrayList<>();

        // Validate input
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty.");
        }

        for (Show show : shows) {
            if (show instanceof Series series) {
                if (series.getTitle().toLowerCase().startsWith(keyword.toLowerCase())) {
                    searchedResults.add(series);
                }
            }
        }



        return searchedResults;
    }
}

