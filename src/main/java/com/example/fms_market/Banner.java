// src/main/java/com/example/fms_market/Banner.java

package com.example.fms_market;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Banner {
    public static User currentUser;
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
        Text homeLabel = createNavLabel("Home", currentPage.equals("Home"), () -> new HomePage(currentUser, stage));

        // Top Watched label
        Text topWatchedLabel = createNavLabel("Top Watched", currentPage.equals("Top Watched"), () -> new TopWatchedPage(currentUser, stage));

        // Top Rated label
        Text topRatedLabel = createNavLabel("Top Rated", currentPage.equals("Top Rated"), () -> {
            try {
                new Top_Rated(currentUser, stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // User icon with emoji
        Label userIcon = new Label("👤");
        userIcon.setStyle("-fx-font-size: 30px;"); // Adjust the size as needed
        userIcon.setOnMouseClicked(_ -> {
            if (sidebarListener != null) {
                sidebarListener.onUserDetailsSelected();
            }
            new AccountPage(currentUser, stage, Sidebar.SidebarState.USER_DETAILS);
        });

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword");
        searchField.setStyle("-fx-background-color: white; -fx-prompt-text-fill: gray;");
        searchField.setFont(Font.font("Arial", 15));
        Button changeLanguageButton = new Button("Change Language");
        changeLanguageButton.setOnAction(e -> LanguageManager.getInstance().toggleLanguage());


        Text revenue_admin = createNavLabel("Panel", currentPage.equals("Panel"), () -> new Revenue_page(stage,currentUser));
        List<Node> commonComponents = new java.util.ArrayList<>(List.of(title, homeLabel, topWatchedLabel, changeLanguageButton,topRatedLabel, searchField, userIcon));

        if (currentUser.getRole().equals("admin")) {
            commonComponents.add(revenue_admin); // Admin-specific component
        }
        banner.getChildren().addAll(commonComponents);

        return banner;
    }

    public static void setSidebarListener(Sidebar.SidebarListener listener) {
        sidebarListener = listener;
    }

    private static Text createNavLabel(String text, boolean isCurrentPage, Runnable action) {
        Text label = new Text(text);
        label.setFont(Font.loadFont(Objects.requireNonNull(Banner.class.getResource("/LexendDecaRegular.ttf")).toString(),20));
        label.setStyle(STR."-fx-fill: \{isCurrentPage ? "white" : "#888888"}; -fx-cursor: hand;");
        label.addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> {
            if (!isCurrentPage) label.setStyle("-fx-fill: lightgrey; -fx-cursor: hand; -fx-font-weight: bold;");
        });
        label.addEventHandler(MouseEvent.MOUSE_EXITED, _ -> {
            if (!isCurrentPage) label.setStyle("-fx-fill: #888888;");
        });

        label.setOnMouseClicked(_ -> action.run());


        return label;
    }

}