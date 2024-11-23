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
            int newUserId = users.isEmpty() ? 0 : users.getLast().getId() + 1;
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

    // Get a user by email and password
    public static User getUserByEmailAndPassword(String email, String password) throws IOException {
        List<User> users = readUsers();
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

        if (!file.exists()) {
            return new ArrayList<>();
        }

        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode usersNode = rootNode.path("users");

        return objectMapper.readValue(usersNode.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
    }

}
