package com.example.fms_market;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonHandler {
    private static final String FILE_PATH = "users.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save the new user and assign them a unique ID
    public static void saveUser(User user) throws IOException {
        List<User> users = readUsers();

        // Set the ID for the new user
        int newUserId = users.isEmpty() ? 0 : users.get(users.size() - 1).getId() + 1;

        // Set the ID to the user (id is now set via setter)
        user.setId(newUserId);

        // Add the new user to the list
        users.add(user);

        // Create the root node with the "users" key
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("users", objectMapper.valueToTree(users));

        // Write the updated JSON to the file
        objectMapper.writeValue(new File(FILE_PATH), rootNode);
    }

    // Check if the email already exists
    public static boolean emailExists(String email) throws IOException {
        List<User> users = readUsers();

        // Check if the email exists in the list
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Get a user by email and password
    public static User getUserByEmailAndPassword(String email, String password) throws IOException {
        List<User> users = readUsers();

        // Find the user matching the email and password
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // Read users from the JSON file
    private static List<User> readUsers() throws IOException {
        File file = new File(FILE_PATH);

        // If the file doesn't exist, return an empty list
        if (!file.exists()) {
            return new ArrayList<>();
        }

        // Read the JSON file and map it to a list of User objects
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode usersNode = rootNode.path("users");

        return objectMapper.readValue(usersNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

}
