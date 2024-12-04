package com.example.fms_market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowJsonHandler {

    private static final String SHOWS_FILE_PATH = "data.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static void saveShowRating(int userId, int showId, Date dateOfWatched, int rating) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        User_Watch_record record = new User_Watch_record(userId, showId, dateOfWatched, Integer.valueOf(rating));
        ratingsNode.add(DataManager.getObjectMapper().valueToTree(record));
        DataManager.saveData();
    }

    public static List<User_Watch_record> getRatings() throws IOException {
        File file = new File(SHOWS_FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        ObjectNode rootNode = (ObjectNode) objectMapper.readTree(file);
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        List<User_Watch_record> ratings = new ArrayList<>();
        ratingsNode.forEach(node -> {
            try {
                User_Watch_record record = objectMapper.treeToValue(node, User_Watch_record.class);
                ratings.add(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ratings;
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