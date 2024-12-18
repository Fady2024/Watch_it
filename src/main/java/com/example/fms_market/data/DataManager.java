package com.example.fms_market.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DataManager {
    private static final String RESOURCES_DIR = "src/main/resources/data";
    private static final String SHOWS_FILE_PATH = Path.of(RESOURCES_DIR, "data.json").toString();
    private static final String USERS_FILE_PATH = Path.of(RESOURCES_DIR, "users.json").toString();
    private static final String DIRECTORS_FILE_PATH = Path.of(RESOURCES_DIR, "director.json").toString();
    private static final String CAST_FILE_PATH = Path.of(RESOURCES_DIR, "cast.json").toString();
    private static final String COMMENTS_FILE_PATH = Path.of(RESOURCES_DIR, "comments.json").toString();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectNode showsRootNode;
    private static ObjectNode usersRootNode;
    private static ObjectNode directorsRootNode;
    private static ObjectNode castRootNode;
    private static ObjectNode commentsRootNode;

    public static void loadData() throws IOException {
        showsRootNode = readFile(SHOWS_FILE_PATH);
        usersRootNode = readFile(USERS_FILE_PATH);
        directorsRootNode = readFile(DIRECTORS_FILE_PATH);
        castRootNode = readFile(CAST_FILE_PATH);
        commentsRootNode = readFile(COMMENTS_FILE_PATH);
    }

    public static void saveData() throws IOException {
        writeFile(SHOWS_FILE_PATH, showsRootNode);
        writeFile(USERS_FILE_PATH, usersRootNode);
        writeFile(DIRECTORS_FILE_PATH, directorsRootNode);
        writeFile(CAST_FILE_PATH, castRootNode);
        writeFile(COMMENTS_FILE_PATH, commentsRootNode);
    }

    private static ObjectNode readFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return objectMapper.createObjectNode();
        }
        try {
            return (ObjectNode) objectMapper.readTree(file);
        } catch (JsonParseException | JsonMappingException e) {
            // Handle JSON parsing exceptions
            System.err.println("Error parsing JSON file: " + filePath);
            e.printStackTrace();
            return objectMapper.createObjectNode();
        }
    }

    private static void writeFile(String filePath, ObjectNode rootNode) throws IOException {
        objectMapper.writeValue(new File(filePath), rootNode);
    }

    public static ObjectNode getShowsRootNode() {
        return showsRootNode;
    }

    public static ObjectNode getUsersRootNode() {
        return usersRootNode;
    }

    public static ObjectNode getDirectorsRootNode() {
        return directorsRootNode;
    }

    public static ObjectNode getCastRootNode() {
        return castRootNode;
    }

    public static ObjectNode getCommentsRootNode() {
        return commentsRootNode;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}