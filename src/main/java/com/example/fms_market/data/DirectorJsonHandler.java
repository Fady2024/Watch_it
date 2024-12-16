package com.example.fms_market.data;

import com.example.fms_market.model.Director;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DirectorJsonHandler {
    private static final String DIRECTORS_FILE_PATH = "src/main/resources/data/director.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveDirector(Director director) throws IOException {
        ObjectNode rootNode = DataManager.getDirectorsRootNode();
        List<Director> directors = readDirectors();

        boolean directorExists = false;
        for (int i = 0; i < directors.size(); i++) {
            if ((directors.get(i).getFirstName() + directors.get(i).getLastName()).equals(director.getFirstName() + director.getLastName())) {
                directors.set(i, director);
                directorExists = true;
                break;
            }
        }
        if (!directorExists)
            directors.add(director);

        rootNode.set("directors", objectMapper.valueToTree(directors));
        DataManager.saveData();
    }
    public static List<Director> readDirectors() throws IOException {
        ObjectNode rootNode = DataManager.getDirectorsRootNode();
        JsonNode directorNode = rootNode.path("directors");

        if (directorNode.isMissingNode() || directorNode.isEmpty()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                directorNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Director.class)
        );
    }
}