package com.example.fms_market.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class DataManager {
    private static final String SHOWS_FILE_PATH = "data.json";
    private static final String USERS_FILE_PATH = "users.json";
    private static final String DIRECTORS_FILE_PATH = "director.json";
    private static final String CAST_FILE_PATH = "cast.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectNode showsRootNode;
    private static ObjectNode usersRootNode;
    private static ObjectNode directorsRootNode;
    private static ObjectNode castRootNode;

    public static void loadData() throws IOException {
        showsRootNode = readFile(SHOWS_FILE_PATH);
        usersRootNode = readFile(USERS_FILE_PATH);
        directorsRootNode = readFile(DIRECTORS_FILE_PATH);
        castRootNode = readFile(CAST_FILE_PATH);
    }

    public static void saveData() throws IOException {
        writeFile(SHOWS_FILE_PATH, showsRootNode);
        writeFile(USERS_FILE_PATH, usersRootNode);
        writeFile(DIRECTORS_FILE_PATH, directorsRootNode);
        writeFile(CAST_FILE_PATH, castRootNode);
    }

    private static ObjectNode readFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return objectMapper.createObjectNode();
        }
        return (ObjectNode) objectMapper.readTree(file);
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

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}