package com.example.fms_market;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CastJsonHandler {
    private static final String CAST_FILE_PATH = "cast.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void saveCast(Cast actor) throws IOException {
        ObjectNode rootNode = DataManager.getCastRootNode();
        List<Cast> cast = readCast();

        boolean actorExists = false;
        for (int i = 0; i < cast.size(); i++) {
            if ((cast.get(i).getFirst_name() + cast.get(i).getLast_name()).equals(actor.getFirst_name() + actor.getLast_name())) {
                cast.set(i, actor);
                actorExists = true;
                break;
            }
        }
        if (!actorExists)
            cast.add(actor);

        rootNode.set("cast", objectMapper.valueToTree(cast));
        DataManager.saveData();
    }
    public static List<Cast> readCast() throws IOException {
        ObjectNode rootNode = DataManager.getCastRootNode();
        JsonNode castNode = rootNode.path("cast");

        if (castNode.isMissingNode() || castNode.isEmpty()) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(
                castNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Cast.class)
        );
    }
}