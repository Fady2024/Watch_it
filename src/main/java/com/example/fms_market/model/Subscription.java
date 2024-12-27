package com.example.fms_market.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.example.fms_market.data.SubscriptionManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscription {

    private int user_id;
    private String plans;
    private int price = 0;
    private int watched_movies_number = 0;
    private String start_date;
    private String endDate;
    private List<String> cardDetails = new ArrayList<>();
    private int available_movies;
    private int moviesWatched;
    private int movieLimit;
    public static final int price_basic = 3;
    public static final int price_standard = 15;
    public static final int price_permium = 30;
    public static final int maxMovies_basic = 5;
    public static final int maxMovies_standard = 10;
    public static final int maxMovies_premium = 30;

    private static int[][] freq_month = new int[12][3];
    private static final int[] arr_revenue = new int[12];
    private static String max_month;

    private static int current_year = 2024;
    private static final int current_month = 12;

    @JsonCreator
    public Subscription(@JsonProperty("user_id") int user_id, @JsonProperty("plans") String plans) {
        this.user_id = user_id;
        this.plans = plans;
        this.watched_movies_number = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.start_date = LocalDate.now().format(formatter);
        LocalDate parsedStartDate = LocalDate.parse(start_date, formatter);
        LocalDate parsedEndDate = parsedStartDate.plusMonths(1);
        this.endDate = parsedEndDate.format(formatter);

        if (current_year != parsedStartDate.getYear()) {
            current_year = parsedStartDate.getYear();
            for (int[] month : freq_month) {
                Arrays.fill(month, 0);
            }
        }

        int index_plan;
        if (plans.equals("Basic")) {
            index_plan = 0;
            this.price = price_basic;
            this.available_movies = maxMovies_basic;
        } else if (plans.equals("Standard")) {
            index_plan = 1;
            this.price = price_standard;
            this.available_movies = maxMovies_standard;
        } else {
            index_plan = 2;
            this.price = price_permium;
            this.available_movies = maxMovies_premium;
        }

        freq_month[parsedStartDate.getMonthValue() - 1][index_plan] += 1;
    }

    public Subscription(int movieLimit) {
        this.movieLimit = movieLimit;
        this.moviesWatched = 0;
    }

    public void watchMovie(String movieTitle) {
        if (movieTitle == null || movieTitle.isEmpty()) {
            System.out.println("Invalid movie title.");
            return;
        }
        if (canWatchMovie()) {
            watched_movies_number++;
            moviesWatched = watched_movies_number;
            System.out.println("One movie will be decremented from your plan.");
            showPopUpMessage();
            SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
        } else {
            System.out.println("You have reached the limit of movies for your subscription plan or your subscription has expired.");
        }
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public boolean canWatchMovie() {
        if (isSubscriptionExpired()) {
            return false;
        }
        int maxMovies = getMaxMovies();
        return watched_movies_number < maxMovies;
    }

    private boolean isSubscriptionExpired() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(start_date, formatter);
        return LocalDate.now().isAfter(parsedDate.plusDays(30));
    }

    private int getMaxMovies() {
        if (plans.equals("Basic")) {
            return maxMovies_basic;
        } else if (plans.equals("Standard")) {
            return maxMovies_standard;
        } else {
            return maxMovies_premium;
        }
    }

    private void showPopUpMessage() {
        System.out.println("One movie will be decremented from your plan.");
    }

    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public String getPlans() {
        return plans;
    }

    public void setPlans(String plans) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(start_date, formatter);
        this.plans = plans;
        if (plans.equals("Basic")) {
            this.price = price_basic;
            freq_month[parsedDate.getMonthValue() - 1][0] += 1;
        } else if (plans.equals("Standard")) {
            this.price = price_standard;
            freq_month[parsedDate.getMonthValue() - 1][1] += 1;
        } else {
            this.price = price_permium;
            freq_month[parsedDate.getMonthValue() - 1][2] += 1;
        }
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public List<String> getCardDetails() {
        return cardDetails;
    }

    public boolean isExpired() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endDate = LocalDate.parse(this.endDate, formatter);
        return LocalDate.now().isAfter(endDate);
    }

    public void setCardDetails(List<String> cardDetails) {
        this.cardDetails = Objects.requireNonNullElseGet(cardDetails, ArrayList::new);
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public int getMoviesWatched() {
        return watched_movies_number;
    }

    public void setMoviesWatched(int moviesWatched) {
        this.watched_movies_number = moviesWatched;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public static int[][] getFreq_month() {
        return freq_month;
    }

    public static void setFreq_month(int[][] freq_month) {
        Subscription.freq_month = freq_month;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    public static int getCurrent_year() {
        return current_year;
    }

    public void setCurrent_year(int current_year) {
        Subscription.current_year = current_year;
        SubscriptionManager.writeSubscriptions(); // Save changes to JSON file
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = String.format(String.valueOf(formatter));

        return STR."Subscription{user_id='\{user_id}', plans='\{plans}', price=\{price}, start_date=\{formattedDate}, freq_monthe=\{Arrays.toString(freq_month)}, current_year=\{current_year}}";
    }

    public static int[] getArr_revenue() {
        for (int i = 0; i < 12; i++) {
            arr_revenue[i] = freq_month[i][0] * Subscription.price_basic +
                    freq_month[i][1] * Subscription.price_standard +
                    freq_month[i][2] * Subscription.price_permium;
        }
        return arr_revenue;
    }

    public static int getMax_revenue() {
        int max_revenue = -1;
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        int INDEX = 0;
        for (int i = 0; i < 12; i++) {
            arr_revenue[i] = freq_month[i][0] * Subscription.price_basic +
                    freq_month[i][1] * Subscription.price_standard +
                    freq_month[i][2] * Subscription.price_permium;
            if (arr_revenue[i] > max_revenue) {
                max_revenue = arr_revenue[i];
                max_month = months[i];
                INDEX = i;
            }
        }
        return INDEX;
    }
}