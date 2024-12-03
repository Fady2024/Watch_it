package com.example.fms_market;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Calculate_Rating {
    private List<String> nameofshows;
    private List<List<Integer>> ratings;

    //constructor
    public Calculate_Rating() {
        nameofshows = new ArrayList<>();
        ratings = new ArrayList<>();
    }

    public void addRating(String show, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("The rating must be in the range 1 to 5.");
        }

        int index = nameofshows.indexOf(show);
        if (index == -1) { //لو الفيلم موجودًا في الليست، اضف التقييم الجديد للتقييمات الخاصة به، ولو الفيلم غير موجود ضيفه للقائمة مع التقييم الأول
            // If the show is not in the list, add it
            nameofshows.add(show);
            List<Integer> newRatings = new ArrayList<>(); //لحصول على التقييمات للفيلم
            newRatings.add(rating);
            ratings.add(newRatings);}
        else {
            // Add the rating to the existing list
            ratings.get(index).add(rating);}}


    public double getAverageRating(String show) {
        int index = nameofshows.indexOf(show);
        if (index == -1) {return 0.0;}
        List<Integer> movieRatings = ratings.get(index);
        return movieRatings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    private double getAverageRatingAcrossAll() {
        int totalSum = 0;
        int totalCount = 0;
        for (List<Integer> ratingList : ratings) {
            for (Integer rating : ratingList) {
                totalSum += rating;
                totalCount++;
            }
        }
        return totalCount == 0 ? 0.0 : (double) totalSum / totalCount;
    }

    public  <T extends Show> List<T> getTopRatedShows(
            List<T> shows, double m, double globalAverageRating){
        shows.forEach(show -> {
            // v = num of ratings for the movie
            double v = getNumberOfRatings(show);
            double weightedRating = calculateWeightedRating(v, show.getImdb_score(), m, globalAverageRating);
            show.setWeightedRating(weightedRating);
        });

        shows.sort(Comparator.comparingDouble(Show::getWeightedRating).reversed());
        return shows;
    }
    // c -> globalAverage
    public List<Movie> getTopRatedMovies(List<Movie> movies, double m) {
        double globalAverage = getAverageRatingAcrossAll();
        return getTopRatedShows(movies, m, globalAverage);
    }

    public List<Series> getTopRatedSeries(List<Series> series, double m) {
        double globalAverage = getAverageRatingAcrossAll();
        return getTopRatedShows(series, m, globalAverage);
    }

    private double calculateWeightedRating(double v, double r, double m, double c) {
        return ((v * r) + (m * c)) / (v + m);
    }

    private int getNumberOfRatings(Show show) {
        int index = nameofshows.indexOf(show.getTitle());
        return index != -1 ? ratings.get(index).size() : 0;
    }
}