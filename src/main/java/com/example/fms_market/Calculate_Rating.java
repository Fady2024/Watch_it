package com.example.fms_market;

import java.io.IOException;
import java.util.List;

public class Calculate_Rating {

    public double calculateAverageRating(Show show) {
        List<User_Watch_record> allRatings;
        try {
            allRatings = ShowJsonHandler.getRatings();
        } catch (IOException e) {
            e.printStackTrace();
            return show.getImdb_score();
        }

        List<User_Watch_record> showRatings = allRatings.stream()
                .filter(rating -> rating.getShow_id() == show.getId())
                .toList();

        double totalRating = show.getImdb_score();
        int count = 1;

        for (User_Watch_record rating : showRatings) {
            totalRating += rating.getRating() * 2;
            count++;
        }

        return totalRating / count;
    }

    public List<Show> getTopRatedShows(List<Show> shows, int topN) {
        shows.sort((show1, show2) -> Double.compare(calculateAverageRating(show2), calculateAverageRating(show1)));
        return shows.subList(0, Math.min(topN, shows.size()));
    }
}