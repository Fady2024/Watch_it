package com.example.fms_market.pages;
import java.time.format.DateTimeFormatter;


import java.time.LocalDate;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscription {

    private int user_id;
    private String plans;
    private int price = 0;
    private int watched_movies_number = 0;
    //private MovieViewingHistory viewingHistory;
    static final int price_basic=3;
    static final int price_standard=15;
    static final int price_permium=30;

    private String start_date;
    private String endDate;
    static final int maxMovies_basic = 5;
    static final int maxMovies_standard = 10;
    static final int maxMovies_premium = 30;

    int available_movies;
    private static int freq_month[][]=new int[12][3];
    private static int arr_revenue[]= new int[12];
    private static String max_month;
    private static int current_year=2024;
    private static int current_month=12;

    @JsonCreator
    public Subscription( @JsonProperty("id") int user_id,  @JsonProperty("plans") String plans) {
        this.user_id = user_id;
        this.plans = plans; //kda 3rft el plan

        //date of subscription starts from this day when the user starts his subscription
        // this.start_date = LocalDate.now();
        this.watched_movies_number = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.start_date = LocalDate.now().format(formatter);

        LocalDate parsedStartDate = LocalDate.parse(start_date, formatter);
        LocalDate parsedEndDate = parsedStartDate.plusMonths(1);
        this.endDate = parsedEndDate.format(formatter);

        if (current_year != parsedStartDate.getYear()) {
            current_year = parsedStartDate.getYear();
            Arrays.fill(freq_month, 0);
        }
        int index_plan;
        if (plans.equals("Basic")) {
            index_plan=0;
            this.price = price_basic;
            this.available_movies=maxMovies_basic;
        }
        else if (plans.equals("Standard")) {
            index_plan=1;
            this.price = price_standard;
            this.available_movies=maxMovies_standard;

        }
        else {
            index_plan=2;
            this.price = price_permium;
            this.available_movies=maxMovies_premium;
        }

        //  freq_month[this.start_date.getMonthValue() - 1][index_plan]+=1;
    }

    public void watchMovie(String movieTitle) {
        if (canWatchMovie()) {
            watched_movies_number++;
          //  viewingHistory.addWatchedMovie(movieTitle);
            showPopUpMessage();
        } else {
            System.out.println("You have reached the limit of movies for your subscription plan or your subscription has expired.");
        }
    }



    public boolean canWatchMovie() {
        if (isSubscriptionExpired()) {
            return false;
        }

        int maxMovies = getMaxMovies();
        return watched_movies_number < maxMovies;
    }
    public void watchMovie() {
        if (canWatchMovie()) {
            watched_movies_number++;
            showPopUpMessage();
        } else {
            System.out.println("You have reached the limit of movies for your subscription plan or your subscription has expired.");
        }
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
        // Implement the pop-up message logic here
        System.out.println("One movie will be decremented from your plan.");
    }



    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPlans() {
        return plans;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPlans(String plans) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(start_date, formatter);
        this.plans = plans;
        if(plans.equals("Basic") ){
            this.price=3;
            freq_month[parsedDate.getMonthValue() - 1][0]+=1;
        }
        else if (plans.equals("Standard")){
            this.price=15;
            freq_month[parsedDate.getMonthValue() - 1][1]+=1;
        }
        else{
            this.price=20;
            freq_month[parsedDate.getMonthValue() - 1][2]+=1;
        }

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }


    public static int[][] getFreq_month() {
        return freq_month;
    }

    public static void setFreq_month(int[][] freq_month) {
        Subscription.freq_month = freq_month;
    }


    public int getMoviesWatched() {
        return watched_movies_number;
    }


    public static int getCurrent_year() {
        return current_year;
    }

    public void setCurrent_year(int current_year) {
        this.current_year = current_year;
    }



    @Override

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = start_date.format(String.valueOf(formatter));

        return "Subscription{" +
                "user_id='" + user_id + '\'' +
                ", plans='" + plans + '\'' +
                ", price=" + price +
                ", start_date=" + formattedDate +
                ", freq_monthe=" + Arrays.toString(freq_month) +
                ", current_year=" + current_year +
                '}';
    }

    public static int[] getArr_revenue() {
        for (int i=0;i<12;i++){

            arr_revenue[i]=freq_month[i][0]*Subscription.price_basic+
                    freq_month[i][1]*Subscription.price_standard+
                    freq_month[i][2]*Subscription.price_permium;


        }
        return arr_revenue;
    }
/////////////////////////////////////////////////////////////////////////////////////
    public static String getMax_revenue() {
        int max_revenue=-1;
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        int INDEX=0;
        for (int i=0;i<12;i++){

            arr_revenue[i]=freq_month[i][0]*Subscription.price_basic+
                    freq_month[i][1]*Subscription.price_standard+
                    freq_month[i][2]*Subscription.price_permium;
            if( arr_revenue[i]>max_revenue){max_revenue=arr_revenue[i];
                max_month=months[i];
                INDEX=i;
            }

        }
        return max_month;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////

}
