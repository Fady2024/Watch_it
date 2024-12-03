// src/main/java/com/example/fms_market/Banner.java

package com.example.fms_market;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.IOException;

public class Banner {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static HBox getBanner(Stage stage, String currentPage) {
        HBox banner = new HBox(36);
        banner.setStyle("-fx-background-color: #451952; -fx-padding: 40;");
        banner.setAlignment(Pos.CENTER_LEFT);

        // Title
        Text title = new Text("WATCH IT");
        title.setFont(Font.font("Tahoma", 40));
        title.setStyle("-fx-fill: white;");

        // Home label
        Text homeLabel = createNavLabel("Home", currentPage.equals("Home"), stage, () -> new HomePage(currentUser, stage));

        // Favorites label
        Text favoritesLabel = createNavLabel("Favorites", currentPage.equals("Favorites"), stage, () -> {
            try {
                new FavoritesPage(currentUser,stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Account label
        Text accountLabel = createNavLabel("Account", currentPage.equals("Account"), stage, () -> {
            try {
                new AccountPage(currentUser, stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword");
        searchField.setStyle("-fx-background-color: white; -fx-prompt-text-fill: gray;");
        searchField.setFont(Font.font("Arial", 15));

        // Adding components to banner
        banner.getChildren().addAll(title, homeLabel, favoritesLabel, accountLabel, searchField);

        return banner;
    }

    private static Text createNavLabel(String text, boolean isCurrentPage, Stage stage, Runnable action) {
        Text label = new Text(text);
        label.setFont(Font.font("Georgia", 20));
        label.setStyle("-fx-fill: " + (isCurrentPage ? "white" : "#888888") + "; -fx-cursor: hand;");
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            if (!isCurrentPage) label.setStyle("-fx-fill: lightgrey; -fx-cursor: hand; -fx-font-weight: bold;");
        });
        label.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            if (!isCurrentPage) label.setStyle("-fx-fill: #888888;");
        });

        label.setOnMouseClicked(e -> action.run());

        return label;
    }
}