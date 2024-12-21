package com.example.fms_market.data;

import com.example.fms_market.model.Movie;
import com.example.fms_market.model.Series;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User_Watch_record;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowJsonHandler {

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
            int newShowId = shows.isEmpty() ? 1 : shows.getLast().getId() + 1;
            show.setId(newShowId);
            shows.add(show);
        }

        rootNode.set("shows", objectMapper.valueToTree(shows));
        DataManager.saveData();
    }

    public static void saveShowRating(int userId, int showId, Date dateOfWatched, int rating) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        for (JsonNode node : ratingsNode) {
            User_Watch_record record = objectMapper.treeToValue(node, User_Watch_record.class);
            if (record.getUser_id() == userId && record.getShow_id() == showId) {
                ((ObjectNode) node).put("rating", rating);
                ((ObjectNode) node).put("dateOfWatched", dateOfWatched.getTime());
                DataManager.saveData();
                return;
            }
        }

        // If no existing rating is found, add a new one
        User_Watch_record newRecord = new User_Watch_record(userId, showId, dateOfWatched, rating);
        ratingsNode.add(objectMapper.valueToTree(newRecord));
        DataManager.saveData();
    }

    public static boolean checkIfRatingExists(int userId, int showId) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        for (JsonNode node : ratingsNode) {
            int nodeUserId = node.get("user_id").asInt();
            int nodeShowId = node.get("show_id").asInt();
            if (nodeUserId == userId && nodeShowId == showId) {
                return true;
            }
        }
        return false;
    }
    public static void updateShowRating(int userId, int showId, Date dateOfWatched, int rating) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        for (JsonNode node : ratingsNode) {
            User_Watch_record record = objectMapper.treeToValue(node, User_Watch_record.class);
            if (record.getUser_id() == userId && record.getUser_id() == showId) {
                ((ObjectNode) node).put("rating", rating);
                ((ObjectNode) node).put("dateOfWatched", dateOfWatched.getTime());
                DataManager.saveData();
                return;
            }
        }
    }

    public static List<User_Watch_record> getRatings() throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode ratingsNode = rootNode.withArray("ratings");

        List<User_Watch_record> ratings = new ArrayList<>();
        for (JsonNode node : ratingsNode) {
            User_Watch_record record = objectMapper.treeToValue(node, User_Watch_record.class);
            ratings.add(record);
        }
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

    public static Show getShowByTitle(String title) throws IOException {
        List<Show> shows = readShows();
        return shows.stream().filter(show -> show.getTitle().equals(title)).findFirst().orElse(null);
    }

    public static List<Show> readShows() throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        JsonNode showsNode = rootNode.path("shows");

        if (showsNode.isMissingNode() || showsNode.isEmpty()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                showsNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Show.class)
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

    public static void deleteShow(int id) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        //ObjectNode rootNode1 = DataManager.getCommentsRootNode();
        ArrayNode shows = (ArrayNode) rootNode.path("shows");
        ArrayNode ratings = (ArrayNode) rootNode.path("ratings");
        //ArrayNode comments = (ArrayNode) rootNode1.path("comments");

        for (int i = 0; i < shows.size(); i++) {
            JsonNode show = shows.get(i);
            if (show.get("id").asInt() == id) {
                shows.remove(i);
                break;
            }
        }

        for (int i = 0; i < shows.size(); i++) {
            JsonNode show = shows.get(i);
            ((ObjectNode) show).put("id", i + 1);
        }

        for (int i = 0; i < ratings.size(); i++) {
            JsonNode rating = ratings.get(i);
            if (rating.get("show_id").asInt() == id) {
                ratings.remove(i);
                i--;
            }
        }

//        for (int i = 0; i < comments.size(); i++) {
//            JsonNode comment = comments.get(i);
//            if (comment.get("show_id").asInt() == id) {
//                comments.remove(i);
//                i--;
//            }
//        }

        rootNode.set("shows", shows);
        rootNode.set("ratings", ratings);
        DataManager.saveData();
        UserJsonHandler.removeFavoriteShowFromAllUsers(id);
    }
    public static void updateShow(Show updatedShow) throws IOException {
        ObjectNode rootNode = DataManager.getShowsRootNode();
        ArrayNode shows = (ArrayNode) rootNode.path("shows");

        for (int i = 0; i < shows.size(); i++) {
            JsonNode showNode = shows.get(i);
            if (showNode.get("id").asInt() == updatedShow.getId()) {
                ObjectNode updatedShowNode = objectMapper.valueToTree(updatedShow);
                shows.set(i, updatedShowNode);
                break;
            }
        }

        rootNode.set("shows", shows);
        DataManager.saveData();
    }



}