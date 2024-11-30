package com.example.fms_market;

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
}
