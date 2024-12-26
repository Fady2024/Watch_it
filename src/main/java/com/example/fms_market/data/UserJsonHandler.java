package com.example.fms_market.data;

import com.example.fms_market.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserJsonHandler {
    private static final String USER_JSON_PATH = "src/main/resources/data/users.json";

    public static void saveUser(User user) throws IOException {
        ObjectNode rootNode = DataManager.getUsersRootNode();
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
            int newUserId = users.isEmpty() ? 1 : users.getLast().getId() + 1;
            user.setId(newUserId);
            users.add(user);
        }

        rootNode.set("users", DataManager.getObjectMapper().valueToTree(users));
        DataManager.saveData();
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

    public static String getUserPhotoPathByUsername(String username) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getUser_photo_path() != null ? user.getUser_photo_path() : "src/main/resources/Acount/user.png";
            }
        }
        return "src/main/resources/Acount/user.png";
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
                    saveUser(user);
                    break;
                }
            }
        }
    }

    public static void removeFavoriteShowFromAllUsers(int showId) throws IOException {
        List<User> users = readUsers();
        for (User user : users) {
            List<Integer> favoriteShowIds = user.getFavoriteShowIds();
            if (favoriteShowIds.contains(showId)) {
                favoriteShowIds.remove(Integer.valueOf(showId)); // Remove by value
                saveUser(user);
            }
        }
    }

    private static List<User> readUsers() throws IOException {
        ObjectNode rootNode = DataManager.getUsersRootNode();
        JsonNode usersNode = rootNode.path("users");

        if (usersNode.isMissingNode() || usersNode.isEmpty()) {
            return new ArrayList<>();
        }

        return DataManager.getObjectMapper().readValue(usersNode.toString(),
                DataManager.getObjectMapper().getTypeFactory().constructCollectionType(List.class, User.class));
    }
}