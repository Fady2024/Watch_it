package com.example.fms_market.data;

import com.example.fms_market.model.Subscription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;


public class SubscriptionManager {
    private static final ObjectMapper objectMapper = DataManager.getObjectMapper();
    private static final File subscriptionsFile = DataManager.getSubscriptionsFile();
    private static List<Subscription> subscriptions = new ArrayList<>();

    static {
        readSubscriptions();
    }

    public static List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public static void addSubscriptions(Subscription newSubscription) {
        if (!subscriptions.contains(newSubscription)) {
            subscriptions.add(newSubscription);
            writeSubscriptions();
        }
    }

    public static void writeSubscriptions() {
        try {
            // Load existing data
            JsonNode existingData = objectMapper.readTree(subscriptionsFile);

            // Create new data
            Map<String, Object> newData = Map.of(
                    "subscriptions", subscriptions,
                    "static_data", Map.of(
                            "current_year", Subscription.getCurrent_year()
                    ),
                    "freq_month", Subscription.getFreq_month()
            );

            // Merge new data with existing data
            ObjectNode mergedData = (ObjectNode) existingData;
            mergedData.setAll(objectMapper.convertValue(newData, ObjectNode.class));

            // Save merged data back to the JSON file
            objectMapper.writeValue(subscriptionsFile, mergedData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSubscriptions() {
        ObjectNode subscriptionsRootNode = DataManager.getSubscriptionsRootNode();

        List<Map<String, Object>> subscriptionList = objectMapper.convertValue(subscriptionsRootNode.get("subscriptions"), new TypeReference<>() {});
        if (subscriptionList != null) {
            subscriptions = objectMapper.convertValue(subscriptionList, new TypeReference<>() {});
        }

        List<List<Integer>> freqMonthData = objectMapper.convertValue(subscriptionsRootNode.get("freq_month"), new TypeReference<>() {});
        if (freqMonthData != null) {
            Subscription.setFreq_month(freqMonthData.stream()
                    .map(list -> list.stream().mapToInt(Integer::intValue).toArray())
                    .toArray(int[][]::new));
        }
    }

    public static Subscription getSubscriptionByUserId(int userId) {
        return subscriptions.stream()
                .filter(subscription -> subscription.getUser_id() == userId)
                .findFirst()
                .orElse(null);
    }

    public static void resetMoviesWatched(int userId) {
        Subscription subscription = getSubscriptionByUserId(userId);
        if (subscription != null) {
            subscription.setMoviesWatched(0);
            writeSubscriptions();
        }
    }
}