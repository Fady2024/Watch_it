// src/main/java/com/example/fms_market/Banner.java

package com.example.fms_market.util;

import com.example.fms_market.admin.Revenue_page;
import com.example.fms_market.model.User;
import com.example.fms_market.pages.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        banner.setStyle("-fx-background-color: #451952; -fx-padding: 40px;");
        banner.setAlignment(Pos.CENTER_LEFT);

        // Title
        Text title = new Text("WATCH IT");
        title.setFont(Font.loadFont(Objects.requireNonNull(Banner.class.getResource("/LexendDecaRegular.ttf")).toString(),40));
        title.setStyle("-fx-fill: white;");

        // Home label
        Text homeLabel = createNavLabel(LanguageManager.getLanguageBasedString("Heim","Home"), currentPage.equals(LanguageManager.getLanguageBasedString("Heim","Home")), () -> new HomePage(currentUser, stage));

        // Top Watched label
        Text topWatchedLabel = createNavLabel(LanguageManager.getLanguageBasedString("Meistgesehen","Top Watched"), currentPage.equals(LanguageManager.getLanguageBasedString("Meistgesehen","Top Watched")), () -> new TopWatchedPage(currentUser, stage));

        // Top Rated label
        Text topRatedLabel = createNavLabel(LanguageManager.getLanguageBasedString("Top-Bewertungen","Top Rated"), currentPage.equals(LanguageManager.getLanguageBasedString("Top-Bewertungen","Top Rated")), () -> {
            try {
                new Top_Rated(currentUser, stage);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Profile icon with emoji
        Image profileIcon = new Image("Acount/iconamoon_profile-circle-fill.png");
        ImageView profileIconView = new ImageView(profileIcon);
        profileIconView.setFitWidth(40);
        profileIconView.setFitHeight(40);

        profileIconView.setOnMouseClicked(_ -> {
            if (sidebarListener != null) {
                sidebarListener.onUserDetailsSelected();
            }
            new AccountPage(currentUser, stage, Sidebar.SidebarState.USER_DETAILS);
        });

        // Search field
        TextField searchField = new TextField();
        searchField.setPrefWidth(300);
        searchField.setPrefHeight(50);
        searchField.setStyle("-fx-background-radius: 50; -fx-border-radius: 50;-fx-border-width: 1;" +
                "-fx-padding: 5px; -fx-prompt-text-fill: gray; -fx-font-size: 14px;");
        searchField.setPromptText(LanguageManager.getLanguageBasedString("\uD83D\uDD0D Suche nach Stichwort","\uD83D\uDD0D Search by keyword"));
        searchField.setFont(Font.font("Arial", 15));


//        ComboBox<String> languageComboBox = new ComboBox<>();
//        languageComboBox.getItems().addAll("English", "German"); // قائمة اللغات
//        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
//            languageComboBox.setValue("German");
//        } else {
//            languageComboBox.setValue("English");
//        }
//        languageComboBox.valueProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                LanguageManager.getInstance().toggleLanguage();
//            }
//        });
     //   LanguageManager.getLanguageBasedString("Bedienfeld","Panel")
        Text revenue_admin = createNavLabel(        LanguageManager.getLanguageBasedString("Bedienfeld","Panel")
                , currentPage.equals(        LanguageManager.getLanguageBasedString("Bedienfeld","Panel")
                ), () -> new Revenue_page(stage,currentUser));
        List<Node> commonComponents = new java.util.ArrayList<>(List.of(title, homeLabel, topWatchedLabel,topRatedLabel, searchField, profileIconView));

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