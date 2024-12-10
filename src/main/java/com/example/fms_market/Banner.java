// src/main/java/com/example/fms_market/Banner.java

package com.example.fms_market;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Objects;

public class Banner {
    private static User currentUser;
    private static Sidebar.SidebarListener sidebarListener;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static HBox getBanner(Stage stage, String currentPage) {
        HBox banner = new HBox(36);
        banner.setStyle("-fx-background-color: #451952; -fx-padding: 40;");
        banner.setAlignment(Pos.CENTER_LEFT);

        // Title
        Text title = new Text("WATCH IT");
        title.setFont(Font.loadFont(Objects.requireNonNull(Banner.class.getResource("/LexendDecaRegular.ttf")).toString(),40));
        title.setStyle("-fx-fill: white;");

        // Home label
        Text homeLabel = createNavLabel("Home", currentPage.equals("Home"), stage, () -> new HomePage(currentUser, stage));

        // Top Watched label
        Text topWatchedLabel = createNavLabel("Top Watched", currentPage.equals("Top Watched"), stage, () -> {
            new TopWatchedPage(currentUser, stage);
        });

        // Top Rated label
        Text topRatedLabel = createNavLabel("Top Rated", currentPage.equals("Top Rated"), stage, () -> {
            try {
                new Top_Rated(currentUser, stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Account label
        Text accountLabel = createNavLabel("Account", currentPage.equals("Account"), stage, () -> {
            new AccountPage1(currentUser, stage, Sidebar.SidebarState.USER_DETAILS);
        });

        // User icon with emoji
        Label userIcon = new Label("👤");
        userIcon.setStyle("-fx-font-size: 30px;"); // Adjust the size as needed
        userIcon.setOnMouseClicked(e -> {
            if (sidebarListener != null) {
                sidebarListener.onUserDetailsSelected();
            }
            new AccountPage1(currentUser, stage, Sidebar.SidebarState.USER_DETAILS);
        });

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword");
        searchField.setStyle("-fx-background-color: white; -fx-prompt-text-fill: gray;");
        searchField.setFont(Font.font("Arial", 15));

        Text revenue_admin = createNavLabel("Panel", currentPage.equals("Panel"), stage, () -> {
            new Revenue_page(stage,currentUser);
        });
        // Adding components to banner
        if(currentUser.getRole().equals("admin")){banner.getChildren().addAll(title, homeLabel, topWatchedLabel, topRatedLabel, accountLabel, revenue_admin,searchField, userIcon);}
        else{banner.getChildren().addAll(title, homeLabel, accountLabel,searchField);}

        return banner;
    }

    private static Text createNavLabel(String text, boolean isCurrentPage, Stage stage, Runnable action) {
        Text label = new Text(text);
        label.setFont(Font.loadFont(Objects.requireNonNull(Banner.class.getResource("/LexendDecaRegular.ttf")).toString(),20));
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