package com.example.fms_market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserJsonHandler {
    private static final String FILE_PATH = "users.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveUser(User user) throws IOException {
        List<User> users = readUsers();
        boolean userExists = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                userExists = true;
                break;
            }
        }

        if (!userExists) {
            int newUserId = users.isEmpty() ? 0 : users.get(users.size() - 1).getId() + 1;
            user.setId(newUserId);
            users.add(user);
        }

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("users", objectMapper.valueToTree(users));
        objectMapper.writeValue(new File(FILE_PATH), rootNode);
    }

    public static boolean emailExists(String email) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public static User getUserByEmailAndPassword(String email, String password) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static void addFavoriteShow(int userId, int showId) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                List<Integer> favoriteShowIds = user.getFavoriteShowIds();
                if (!favoriteShowIds.contains(showId)) {
                    favoriteShowIds.add(showId);
                }
                saveUser(user);
                break;
            }
        }
    }

    public static List<Integer> getFavoriteShows(int userId) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                return user.getFavoriteShowIds();
            }
        }
        return new ArrayList<>();
    }

    public static void removeFavoriteShow(int userId, int showId) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getId() == userId) {
                List<Integer> favoriteShowIds = user.getFavoriteShowIds();
                if (favoriteShowIds.contains(showId)) {
                    favoriteShowIds.remove(Integer.valueOf(showId)); // Remove by value
                    saveUser(user); // Save the updated user
                    break;
                }
            }
        }
    }

    private static List<User> readUsers() throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode usersNode = rootNode.path("users");

        if (usersNode.isMissingNode() || usersNode.isEmpty()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(usersNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }
}