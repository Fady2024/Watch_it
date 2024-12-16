package com.example.fms_market.data;

import com.example.fms_market.pages.Subscription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubscriptionManager {
    private static final String FILE_NAME = "src/main/resources/data/subscriptions.json";
    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    private static List<Subscription> subscriptions = new ArrayList<>();

    public static List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public static void addSubscriptions(Subscription new_subscription) {
        subscriptions.add(new_subscription);
    }

    // دالة لكتابة البيانات إلى الملف
    public static void writeSubscriptions() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }

            Map<String, Object> data = Map.of(
                    "subscriptions", subscriptions,
                    "static_data", Map.of(

                            "current_year", Subscription.getCurrent_year()

                    ),
                    "freq_month", Subscription.getFreq_month()
            );

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSubscriptions() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                return;
            }

            Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});


            List<Subscription> subscriptionList = (List<Subscription>) data.get("subscriptions");
            if (subscriptionList != null) {
                subscriptions = subscriptionList;
            }


            List<List<Integer>>freqMonthData = ( List<List<Integer>>) data.get("freq_month");
            if (freqMonthData != null) {
              Subscription.setFreq_month(freqMonthData.stream()
                      .map(list -> list.stream().mapToInt(Integer::intValue).toArray())
                      .toArray(int[][]::new));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



