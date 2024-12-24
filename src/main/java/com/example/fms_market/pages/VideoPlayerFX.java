package com.example.fms_market.pages;

import com.example.fms_market.data.CommentJsonHandler;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.Comment;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayerFX {
    private final String videoUrl;
    private final int videoId;
    private final Stage stage;
    private int stageWidth;
    private int stageHeight;
    private final User user;
    private VBox commentsContainer;
    private Stage commentDialog;
    private Stage replyDialog;

    public VideoPlayerFX(String videoUrl, int videoId, Stage stage, User user) {
        this.videoUrl = videoUrl;
        this.videoId = videoId;
        this.stage = stage;
        this.user = user;
        initialize();
    }

    private void initialize() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webEngine.setCreatePopupHandler(_ -> null);

        String adBlockScript =
                "document.querySelectorAll('iframe, img, .ads, .ad, .banner, .popup').forEach(function(el) { el.style.display = 'none'; });";

        webEngine.getLoadWorker().stateProperty().addListener((_, _, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                webEngine.executeScript(adBlockScript);
            }
        });

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(videoUrl)) {
                stage.close();
            }
        });

        webEngine.load(videoUrl);
        setScreenDimensions();

        StackPane videoContainer = new StackPane();
        videoContainer.getChildren().add(webView);

        webView.setScaleX(0.55);
        webView.setScaleY(0.7);

        BorderPane root = new BorderPane();
        root.setTop(Banner.getBanner(stage, "Video Player", "VideoPlayerPage"));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #252525;");

        VBox mainContent = new VBox(16);
        mainContent.setStyle("-fx-background-color: #252525;");
        mainContent.getChildren().addAll(videoContainer, createCommentsSection());

        scrollPane.setContent(mainContent);

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("Video Player");

        stage.setOnCloseRequest(e -> {
            if (commentDialog != null) {
                commentDialog.close();
            }
            if (replyDialog != null) {
                replyDialog.close();
            }
        });

        stage.show();
    }

    private VBox createCommentsSection() {
        VBox commentsSection = new VBox(16);
        commentsSection.setStyle("-fx-padding: 16 40 16 40; -fx-background-color: #252525;");

        Text title = new Text("Comments");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: white;");

        commentsContainer = new VBox(12);
        commentsContainer.setStyle("-fx-background-color: #333333; -fx-padding: 16; -fx-border-radius: 8;");
        loadComments();

        Button addCommentButton = new Button(LanguageManager.getLanguageBasedString("Kommentar hinzufügen","Add Comment"));
        addCommentButton.setTranslateX(20);
        addCommentButton.setStyle("-fx-background-color: #8e5bdc; -fx-text-fill: black;-fx-border-radius: 10; -fx-background-radius: 10;");
        addCommentButton.setOnAction(e -> showAddCommentDialog());
        commentsSection.getChildren().addAll(title, commentsContainer, addCommentButton);
        return commentsSection;
    }

    private void loadComments() {
        commentsContainer.getChildren().clear();
        try {
            List<Comment> comments = CommentJsonHandler.readComments(videoId);
            if (comments.isEmpty()) {
                Text noCommentsText = new Text(LanguageManager.getLanguageBasedString("Noch keine Kommentare. Sei der Erste, der einen Kommentar schreibt!","No comments yet. Be the first to comment!"));
                noCommentsText.setStyle("-fx-font-size: 14px; -fx-fill: white;");
                commentsContainer.getChildren().add(noCommentsText);
            } else {
                for (Comment comment : comments) {
                    commentsContainer.setStyle("-fx-background-color: #333333; -fx-padding: 16; -fx-border-radius: 10; -fx-background-radius: 10;");
                    commentsContainer.getChildren().add(createCommentBox(comment));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageView createCircularImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        Circle clip = new Circle(20, 20, 20);
        imageView.setClip(clip);
        return imageView;
    }

    private VBox createCommentBox(Comment comment) throws IOException {
        VBox commentBox = new VBox(8);
        commentBox.setStyle("-fx-background-color: #444444; -fx-padding: 8; -fx-border-radius: 10; -fx-background-radius: 10;");

        String photoPath = UserJsonHandler.getUserPhotoPathByUsername(comment.getReviewerName());
        File photoFile = new File(photoPath);
        Image userImage;
        if (photoFile.exists()) {
            userImage = new Image(photoFile.toURI().toString());
        } else {
            userImage = new Image("src/main/resources/Acount/user.png");
        }

        ImageView userPhoto = createCircularImageView(userImage);
        Text nameText = new Text(comment.getReviewerName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: white;");
        Text dateText = new Text(formatDate(comment.getDate()));
        dateText.setStyle("-fx-font-size: 12px; -fx-fill: #AAAAAA;");
        Text commentText = new Text(comment.getCommentText());
        commentText.setStyle("-fx-font-size: 14px; -fx-fill: white;");

        Button replyButton = new Button(LanguageManager.getLanguageBasedString("Antwort","Reply"));
        replyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        replyButton.setOnAction(e -> showAddReplyDialog(comment));

        HBox header = new HBox(8, userPhoto, nameText, dateText);
        commentBox.getChildren().addAll(header, commentText, replyButton);

        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            VBox repliesBox = new VBox(4);
            repliesBox.setStyle("-fx-padding: 8; -fx-background-color: #666666; -fx-border-radius: 10;-fx-background-radius: 10;");

            for (Comment reply : comment.getReplies()) {
                repliesBox.getChildren().add(createReplyBox(reply));
            }

            Button toggleRepliesButton = new Button(
                    STR."Show Replies (\{comment.getReplies().size()})");
            toggleRepliesButton.setOnAction(e -> {
                if (repliesBox.isVisible()) {
                    repliesBox.setVisible(false);
                    toggleRepliesButton.setText(STR."Show Replies (\{comment.getReplies().size()})");
                } else {
                    repliesBox.setVisible(true);
                    toggleRepliesButton.setText(STR."Hide Replies (\{comment.getReplies().size()})");
                }
            });

            commentBox.getChildren().addAll(toggleRepliesButton, repliesBox);
            repliesBox.setVisible(false);
            repliesBox.managedProperty().bind(repliesBox.visibleProperty());
        }

        return commentBox;
    }

    private VBox createReplyBox(Comment reply) throws IOException {
        VBox replyBox = new VBox(8);
        replyBox.setStyle("-fx-background-color: #555555; -fx-padding: 8; -fx-border-radius: 10; -fx-background-radius: 10;");

        ImageView userPhoto;
        try {
            String photoPath = UserJsonHandler.getUserPhotoPathByUsername(reply.getReviewerName());
            File photoFile = new File(photoPath);
            Image userImage;
            if (photoFile.exists()) {
                userImage = new Image(photoFile.toURI().toString());
            } else {
                userImage = new Image("src/main/resources/Acount/user.png");
            }
            userPhoto = createCircularImageView(userImage);
        } catch (IllegalArgumentException e) {
            System.err.println(STR."Invalid URL or resource not found for user photo: \{reply.getReviewerName()}");
            userPhoto = createCircularImageView(new Image("src/main/resources/Acount/user.png"));
        }

        Text nameText = new Text(reply.getReviewerName());
        nameText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: white;");
        Text dateText = new Text(formatDate(reply.getDate()));
        dateText.setStyle("-fx-font-size: 12px; -fx-fill: #AAAAAA;");
        Text replyText = new Text(reply.getCommentText());
        replyText.setStyle("-fx-font-size: 14px; -fx-fill: white;");

        Button replyButton = new Button("Reply");
        replyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        replyButton.setOnAction(e -> showAddReplyDialog(reply));

        HBox header = new HBox(8, userPhoto, nameText, dateText);
        replyBox.getChildren().addAll(header, replyText, replyButton);

        if (reply.getReplies() != null && !reply.getReplies().isEmpty()) {
            VBox repliesBox = new VBox(4);
            repliesBox.setStyle("-fx-padding: 8; -fx-background-color: #666666; -fx-border-radius: 10;-fx-background-radius: 10;");

            for (Comment nestedReply : reply.getReplies()) {
                repliesBox.getChildren().add(createReplyBox(nestedReply));
            }

            Button toggleRepliesButton = new Button(STR."Show Replies (\{reply.getReplies().size()})");
            toggleRepliesButton.setOnAction(e -> {
                if (repliesBox.isVisible()) {
                    repliesBox.setVisible(false);
                    toggleRepliesButton.setText(STR."Show Replies (\{reply.getReplies().size()})");
                } else {
                    repliesBox.setVisible(true);
                    toggleRepliesButton.setText(STR."Hide Replies (\{reply.getReplies().size()})");
                }
            });

            replyBox.getChildren().addAll(toggleRepliesButton, repliesBox);
            repliesBox.setVisible(false);
            repliesBox.managedProperty().bind(repliesBox.visibleProperty());
        }

        return replyBox;
    }

    private void showAddCommentDialog() {
        commentDialog = new Stage(StageStyle.TRANSPARENT);
        VBox dialogLayout = new VBox(12);
        dialogLayout.setStyle("-fx-padding: 16; -fx-background-color: rgba(68, 68, 68, 0.9); -fx-border-radius: 10; -fx-background-radius: 10;");

        TextArea commentField = new TextArea();
        commentField.setPromptText(LanguageManager.getLanguageBasedString("Schreiben Sie Ihren Kommentar","Write your comment"));

        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10 0 0 0;");

        Button submitButton = new Button(LanguageManager.getLanguageBasedString("Einreichen","Submit"));
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            try {
                Comment newComment = new Comment(user.getUsername(), LocalDateTime.now(), commentField.getText(), videoId);
                CommentJsonHandler.saveComment(newComment);
                loadComments();
                commentDialog.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Button cancelButton = new Button(LanguageManager.getLanguageBasedString("Stornieren","Cancel"));
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> commentDialog.close());

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        Label addCommentLabel = new Label(LanguageManager.getLanguageBasedString("Hinzufügen Dein Kommentar","Add Your Comment"));
        addCommentLabel.setStyle("-fx-text-fill: white;");

        dialogLayout.getChildren().addAll(addCommentLabel, commentField, buttonBox);
        Scene scene = new Scene(dialogLayout, 300, 200);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        commentDialog.setScene(scene);
        commentDialog.show();
    }

    private void showAddReplyDialog(Comment parentComment) {
        replyDialog = new Stage(StageStyle.TRANSPARENT);
        VBox dialogLayout = new VBox(12);
        dialogLayout.setStyle("-fx-padding: 16; -fx-background-color: rgba(68, 68, 68, 0.9); -fx-border-radius: 10; -fx-background-radius: 10;");

        TextArea replyField = new TextArea();
        replyField.setPromptText(LanguageManager.getLanguageBasedString("Schreiben Sie Ihre Antwort","Write your reply"));

        HBox buttonBox = new HBox(10);
        buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10 0 0 0;");

        Button submitButton = getSubmitButton(parentComment, replyField);

        Button cancelButton = new Button(LanguageManager.getLanguageBasedString("Stornieren","Cancel"));
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelButton.setOnAction(e -> replyDialog.close());

        buttonBox.getChildren().addAll(submitButton, cancelButton);

        Label addReplyLabel = new Label(LanguageManager.getLanguageBasedString("Fügen Sie Ihre Antwort hinzu","Add Your Reply"));
        addReplyLabel.setStyle("-fx-text-fill: white;");

        dialogLayout.getChildren().addAll(addReplyLabel, replyField, buttonBox);
        Scene scene = new Scene(dialogLayout, 300, 200);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        replyDialog.setScene(scene);
        replyDialog.show();
    }

    @NotNull
    private Button getSubmitButton(Comment parentComment, TextArea replyField) {
        Button submitButton = new Button(LanguageManager.getLanguageBasedString("Einreichen","Submit"));
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            try {
                Comment reply = new Comment(user.getUsername(), LocalDateTime.now(), replyField.getText(), videoId);
                if (parentComment.getReplies() == null) {
                    parentComment.setReplies(new ArrayList<>());
                }
                parentComment.getReplies().add(reply);

                List<Comment> comments = CommentJsonHandler.readComments(videoId);
                updateCommentReplies(comments, parentComment);
                CommentJsonHandler.saveComments(comments);
                loadComments();
                replyDialog.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        return submitButton;
    }

    private void updateCommentReplies(List<Comment> comments, Comment updatedComment) {
        for (Comment comment : comments) {
            if (comment.getVideoId() == updatedComment.getVideoId() && comment.getReviewerName().equals(updatedComment.getReviewerName()) && comment.getDate().equals(updatedComment.getDate())) {
                comment.setReplies(updatedComment.getReplies());
                return;
            }
            if (comment.getReplies() != null) {
                updateCommentReplies(comment.getReplies(), updatedComment);
            }
        }
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm a");
        return date.format(formatter);
    }

    private void setScreenDimensions() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stageWidth = (int) screenSize.getWidth();
        stageHeight = (int) (screenSize.getHeight() / 1.1);
    }
}