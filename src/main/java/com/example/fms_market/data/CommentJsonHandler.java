package com.example.fms_market.data;

import com.example.fms_market.model.Comment;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentJsonHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }
    public static List<Comment> readComments(int videoId) throws IOException {
        ObjectNode commentsRootNode = DataManager.getCommentsRootNode();
        ArrayNode commentsArrayNode = (ArrayNode) commentsRootNode.get("comments");
        if (commentsArrayNode == null) {
            return new ArrayList<>();
        }
        List<Comment> allComments = objectMapper.readValue(commentsArrayNode.toString(), new TypeReference<>() {
        });
        List<Comment> videoComments = new ArrayList<>();
        for (Comment comment : allComments) {
            if (comment.getVideoId() == videoId) {
                videoComments.add(comment);
            }
        }
        return videoComments;
    }

    public static void saveComment(Comment newComment) throws IOException {
        List<Comment> allComments = readAllComments();
        allComments.add(newComment);
        saveComments(allComments);
    }

    private static List<Comment> readAllComments() throws IOException {
        ObjectNode commentsRootNode = DataManager.getCommentsRootNode();
        ArrayNode commentsArrayNode = (ArrayNode) commentsRootNode.get("comments");
        if (commentsArrayNode == null) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(commentsArrayNode.toString(), new TypeReference<>() {
        });
    }

    public static void saveComments(List<Comment> comments) throws IOException {
        ObjectNode commentsRootNode = DataManager.getCommentsRootNode();
        ArrayNode commentsArrayNode = objectMapper.valueToTree(comments);
        commentsRootNode.set("comments", commentsArrayNode);
        DataManager.saveData();
    }
}