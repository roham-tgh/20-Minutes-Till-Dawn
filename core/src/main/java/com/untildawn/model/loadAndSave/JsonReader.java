package com.untildawn.model.loadAndSave;

import com.google.gson.*;
import com.untildawn.model.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonReader {
    // Create a single Gson instance for reuse
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final String USER_DATA = "core/src/main/java/com/untildawn/model/loadAndSave/Data.json";

    private JsonReader() {} // Prevent instantiation

    public static User getUser(String username) {
        try {
            // Fixed: Correctly read as JsonArray instead of JsonObject
            JsonArray users = readFromFile(USER_DATA);
            // Fixed: Directly use JsonArray instead of converting to User array
            for (JsonElement element : users) {
                User user = gson.fromJson(element, User.class);
                if (username.equals(user.getUsername())) {
                    return user;
                }
            }
            return null;
        } catch (IOException e) {
            // Fixed: Better error handling
            throw new RuntimeException("Failed to read user data", e);
        }
    }

    public static boolean updateUser(String username, User updatedUser) {
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(updatedUser, "Updated user cannot be null");

        try {
            // Fixed: Correctly read as JsonArray
            JsonArray userArray = readFromFile(USER_DATA);
            boolean userFound = false;

            // Create new array to store updated data
            JsonArray updatedArray = new JsonArray();

            // Iterate through existing users
            for (JsonElement element : userArray) {
                User currentUser = gson.fromJson(element, User.class);
                if (currentUser.getUsername().equals(username)) {
                    // Replace old user data with updated user data
                    updatedArray.add(gson.toJsonTree(updatedUser));
                    userFound = true;
                } else {
                    // Keep existing user data
                    updatedArray.add(element);
                }
            }

            // Write the updated array back to file
            if (userFound) {
                // Fixed: Correctly write JsonArray instead of trying to convert to JsonObject
                writeToFile(updatedArray, USER_DATA);
            }

            return userFound;

        } catch (IOException e) {
            // Fixed: Better error handling
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public static void appendUser(User newUser) {
        Objects.requireNonNull(newUser, "New user cannot be null");

        try {
            // Fixed: Correctly read as JsonArray
            JsonArray userArray = readFromFile(USER_DATA);

            // Convert User object to JsonElement and add to array
            JsonElement userElement = gson.toJsonTree(newUser);
            userArray.add(userElement);

            // Fixed: Correctly write JsonArray instead of trying to convert to JsonObject
            writeToFile(userArray, USER_DATA);

        } catch (IOException e) {
            // Fixed: Better error handling
            throw new RuntimeException("Failed to append user", e);
        }
    }

    public static List<User> readAllUsers() {
        try {
            // Fixed: Correctly read as JsonArray
            JsonArray userArray = readFromFile(USER_DATA);
            List<User> users = new ArrayList<>();

            for (JsonElement element : userArray) {
                User user = gson.fromJson(element, User.class);
                users.add(user);
            }
            return users;

        } catch (IOException e) {
            // Fixed: Better error handling
            throw new RuntimeException("Failed to read all users", e);
        }
    }

    // Fixed: Changed return type to JsonArray
    private static JsonArray readFromFile(String filename) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(
                Paths.get(filename), StandardCharsets.UTF_8)) {
            // Fixed: Read as JsonArray instead of JsonObject
            return new JsonParser().parse(reader).getAsJsonArray();
        }
    }

    // Fixed: Changed parameter type to JsonArray
    private static void writeToFile(JsonArray json, String filename) throws IOException {
        Objects.requireNonNull(json, "JSON array cannot be null");
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(filename), StandardCharsets.UTF_8)) {
            gson.toJson(json, writer);
        }
    }

    public static void deleteUser(String username) {
        Objects.requireNonNull(username, "Username cannot be null");

        try {
            // Read existing users
            JsonArray userArray = readFromFile(USER_DATA);
            boolean userFound = false;

            // Create new array to store updated data
            JsonArray updatedArray = new JsonArray();

            // Iterate through existing users
            for (JsonElement element : userArray) {
                User currentUser = gson.fromJson(element, User.class);
                if (!currentUser.getUsername().equals(username)) {
                    // Keep existing user data
                    updatedArray.add(element);
                } else {
                    userFound = true; // User found and will be deleted
                }
            }

            // Write the updated array back to file
            if (userFound) {
                writeToFile(updatedArray, USER_DATA);
            }

        } catch (IOException e) {
            // Fixed: Better error handling
            throw new RuntimeException("Failed to delete user", e);
        }
    }
}
