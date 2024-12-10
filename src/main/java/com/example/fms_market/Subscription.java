package com.example.fms_market;
import java.time.format.DateTimeFormatter;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Subscription {

    private String user_id;


    private String plans;


    private int price=0;

    static final int price_basic=3;
    static final int price_standard=15;

    static final int price_permium=30;

    private String start_date;


private static int freq_month[][]=new int[12][3];



    private static int current_year=2024;
    private static int current_month=12;
    @JsonCreator
    public Subscription( @JsonProperty("id") String user_id,  @JsonProperty("plans") String plans) {
        this.user_id = user_id;
        this.plans = plans;

        this.start_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(start_date, formatter);

        if (current_year != parsedDate.getYear()) {
            current_year = parsedDate.getYear();
            Arrays.fill(freq_month, 0);
        }
int index_plan;
        if (plans.equals("Basic")) {
            index_plan=0;

            this.price = price_basic;
        } else if (plans.equals("Standard")) {
            index_plan=1;

            this.price = price_standard;
        } else {
            index_plan=2;
            this.price = price_permium;
        }

        freq_month[parsedDate.getMonthValue() - 1][index_plan]+=1;
    }



    // Getters and Setters
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPlans() {
        return plans;
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












}
