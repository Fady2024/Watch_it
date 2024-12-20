package com.example.fms_market.model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class Comment {
    private String reviewerName;
    private LocalDateTime date;
    private String commentText;
    private List<Comment> replies;
    private int videoId;

    public Comment() {}

    @JsonCreator
    public Comment(@JsonProperty("reviewerName") String reviewerName,
                   @JsonProperty("date") LocalDateTime date,
                   @JsonProperty("commentText") String commentText,
                   @JsonProperty("replies") List<Comment> replies,
                   @JsonProperty("videoId") int videoId) {
        this.reviewerName = reviewerName;
        this.date = date;
        this.commentText = commentText;
        this.replies = replies;
        this.videoId = videoId;
    }

    public Comment(String reviewerName, LocalDateTime date, String commentText, int videoId) {
        this.reviewerName = reviewerName;
        this.date = date;
        this.commentText = commentText;
        this.videoId = videoId;
    }

    // Getters and setters
    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}