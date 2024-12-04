package com.example.fms_market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowJsonHandler {

    public static void saveShow(Show show) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        List<Show> shows = readShows();

        boolean showExists = false;
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getId() == show.getId()) {
                shows.set(i, show);
                showExists = true;
                break;
            }
        }

        if (!showExists) {
            int newShowId = shows.isEmpty() ? 1 : shows.get(shows.size() - 1).getId() + 1;
            show.setId(newShowId);
            shows.add(show);
        }

        rootNode.set("shows", DataManager.getObjectMapper().valueToTree(shows));
        DataManager.saveData();
    }

    public static void saveShowRating(int userId, int showId, java.util.Date dateOfWatched, int rating) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        User_Watch_record record = new User_Watch_record(userId, showId, dateOfWatched, rating);
        ratingsNode.add(DataManager.getObjectMapper().valueToTree(record));
        DataManager.saveData();
    }

    public static List<Show> readShows() throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        JsonNode showsNode = rootNode.path("shows");

        if (showsNode.isMissingNode() || showsNode.isEmpty()) {
            return new ArrayList<>();
        }

        return DataManager.getObjectMapper().readValue(
                showsNode.toString(),
                DataManager.getObjectMapper().getTypeFactory().constructCollectionType(List.class, Show.class)
        );
    }

    public static List<Movie> readMovies() throws IOException {
        List<Show> shows = readShows();
        List<Movie> movies = new ArrayList<>();
        for (Show show : shows) {
            if (show instanceof Movie) {
                movies.add((Movie) show);
            }
        }
        return movies;
    }

    public static List<Series> readSeries() throws IOException {
        List<Show> shows = readShows();
        List<Series> seriesList = new ArrayList<>();
        for (Show show : shows) {
            if (show instanceof Series) {
                seriesList.add((Series) show);
            }
        }
        return seriesList;
    }
}