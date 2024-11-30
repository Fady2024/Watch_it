package com.example.fms_market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowJsonHandler {
    private static final String FILE_PATH = "data.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveShow(Show show) throws IOException {
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

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("shows", objectMapper.valueToTree(shows));
        objectMapper.writeValue(new File(FILE_PATH), rootNode);
    }

    public static List<Show> readShows() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode showsNode = rootNode.path("shows");

        if (showsNode.isMissingNode() || showsNode.isEmpty()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(showsNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Show.class));
    }

}