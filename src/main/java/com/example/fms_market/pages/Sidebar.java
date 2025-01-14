package com.example.fms_market.pages;

import com.example.fms_market.account.WelcomePage;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Sidebar extends VBox {
    private final Stage stage;
    private final User currentUser;

    public enum SidebarState {

        HOME("🏠", LanguageManager.getLanguageBasedString("Heim","Home")),
        USER_DETAILS("👤", LanguageManager.getLanguageBasedString("Benutzerdetails","User Details")),
        FAVOURITES("🌟", LanguageManager.getLanguageBasedString("Favoritinnen","Favourites")),
        WATCHED("🎥", LanguageManager.getLanguageBasedString("Beobachtet","Watched")),
        SUBSCRIPTION("📅", LanguageManager.getLanguageBasedString("Abonnement","Subscription")),
        ABOUT_US("ℹ️", LanguageManager.getLanguageBasedString("Über uns","About Us")),
        LOGOUT("🚪", LanguageManager.getLanguageBasedString("Ausloggen","Logout"));



        private final String icon;
        private final String text;

        SidebarState(String icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }
    }

    private SidebarState selectedState;
    private final VBox menuContainer;
    private final Button toggleButton;
    private SidebarListener listener;
    private boolean isExpanded = false;


    public Sidebar(SidebarState initialState, Stage stage, User currentUser) {
        this.selectedState = initialState;
        this.stage = stage;
        this.currentUser = currentUser;
        this.setStyle("-fx-background-color: #404040; -fx-border-radius: 10; -fx-background-radius: 10;");
        this.setPrefWidth(80);
        this.setAlignment(Pos.TOP_CENTER);

        // Set initial margin for minimized state
        BorderPane.setMargin(this, new Insets(15, 0, 15, 0));

        // Menu container
        menuContainer = new VBox(15);
        menuContainer.setAlignment(Pos.TOP_CENTER);
        menuContainer.setStyle("-fx-padding: 10px;");

        // Add home button
        addHomeButton();

        for (SidebarState state : SidebarState.values()) {
            if (state != SidebarState.LOGOUT && state != SidebarState.ABOUT_US &&state != SidebarState.HOME) {
                addMenuItem(state);
            }
        }

        addMenuItem(SidebarState.ABOUT_US);
        // Add logout button
        addLogoutButton();

        // Toggle button
        toggleButton = new Button("⏩");
        toggleButton.setFont(Font.font(16));
        toggleButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand;");
        toggleButton.setOnAction(_ -> toggleSidebar());
        toggleButton.setAlignment(Pos.BOTTOM_CENTER);

        this.getChildren().addAll(menuContainer, toggleButton);

        updateMenuStyles();
    }

    private void addMenuItem(SidebarState state) {
        Label iconLabel = new Label(state.getIcon());
        iconLabel.setFont(Font.font(18));
        iconLabel.setStyle("-fx-text-fill: white;");

        Label textLabel = new Label(state.getText());
        textLabel.setFont(Font.font(14));
        textLabel.setStyle("-fx-text-fill: white;");
        textLabel.setVisible(isExpanded);
        textLabel.setManaged(isExpanded);

        HBox menuItem = new HBox(10, iconLabel, textLabel);
        menuItem.setAlignment(isExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
        menuItem.setStyle(isExpanded ? "-fx-padding: 0px;" : "-fx-padding: 5px;");

        if (state == selectedState) {
            applySelectedStyle(iconLabel, textLabel);
        }

        menuItem.setOnMouseClicked(_ -> {
            selectedState = state;
            updateMenuStyles();
            triggerListener(state);

            if (isExpanded) {
                toggleSidebar();
            }

            if (state == SidebarState.HOME) {
                navigateToHomePage();
            }

        });

        menuContainer.getChildren().add(menuItem);
    }

    private void toggleSidebar() {
        isExpanded = !isExpanded;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(300), _ -> {
                    double targetWidth = isExpanded ? 200 : 80;
                    this.setPrefWidth(targetWidth);
                    toggleButton.setText(isExpanded ? "⏪" : "⏩");

                    menuContainer.getChildren().forEach(node -> {
                        if (node instanceof HBox menuItem) {
                            Label textLabel = (Label) menuItem.getChildren().get(1);
                            textLabel.setVisible(isExpanded);
                            textLabel.setManaged(isExpanded);

                            menuItem.setAlignment(isExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
                            menuItem.setStyle(isExpanded ? "-fx-padding: 18px;" : "-fx-padding: 5px;");
                        }
                    });

                    VBox logoutVBox = (VBox) menuContainer.getChildren().getLast();
                    HBox logoutButton = (HBox) logoutVBox.getChildren().getFirst();
                    logoutButton.setStyle("-fx-padding: 5px; -fx-background-color: red; -fx-background-radius: 5;");

                    // Apply curve effect
                    this.setStyle("-fx-background-color: #333; -fx-border-radius: 10; -fx-background-radius: 10;");

                    // Set margin based on the expanded state
                    BorderPane.setMargin(this, isExpanded ? Insets.EMPTY : new Insets(15, 0, 15, 0));
                })
        );

        timeline.setCycleCount(1);
        timeline.setRate(1);
        timeline.play();
    }

    private void updateMenuStyles() {
        menuContainer.getChildren().forEach(node -> {
            if (node instanceof HBox menuItem) {
                Label iconLabel = (Label) menuItem.getChildren().get(0);
                Label textLabel = (Label) menuItem.getChildren().get(1);

                resetStyle(iconLabel, textLabel);

                if (textLabel.getText().equals(selectedState.getText())) {
                    applySelectedStyle(iconLabel, textLabel);
                }
            }
        });
    }

    private void applySelectedStyle(Label iconLabel, Label textLabel) {
        iconLabel.setStyle("-fx-text-fill: #8969ba;");
        textLabel.setStyle("-fx-text-fill: #8969ba;");
    }

    private void resetStyle(Label iconLabel, Label textLabel) {
        iconLabel.setStyle("-fx-text-fill: white;");
        textLabel.setStyle("-fx-text-fill: white;");
    }

    private void triggerListener(SidebarState state) {
        if (listener != null) {
            switch (state) {
                case USER_DETAILS -> listener.onUserDetailsSelected();
                case FAVOURITES -> listener.onFavouritesSelected();
                case WATCHED -> listener.onWatchedSelected();
                case SUBSCRIPTION -> listener.onSubscriptionSelected();
                case ABOUT_US -> listener.onAboutUsSelected();
            }
        }
    }

    public void setSidebarListener(SidebarListener listener) {
        this.listener = listener;
    }
    private void addLogoutButton() {
        Label iconLabel = new Label(SidebarState.LOGOUT.getIcon());
        iconLabel.setFont(Font.font(18));
        iconLabel.setStyle("-fx-text-fill: white;");

        Label textLabel = new Label(SidebarState.LOGOUT.getText());
        textLabel.setFont(Font.font(14));
        textLabel.setStyle("-fx-text-fill: white;");
        textLabel.setVisible(isExpanded);
        textLabel.setManaged(isExpanded);

        HBox menuItem = new HBox(5, iconLabel, textLabel);
        menuItem.setAlignment(isExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
        menuItem.setStyle("-fx-padding: 3px;");

        menuItem.setOnMouseClicked(_ -> navigateToWelcomePage());

        VBox logoutVBox = new VBox();
        logoutVBox.setStyle("-fx-background-color: red; -fx-padding: 2px; -fx-border-radius: 10; -fx-background-radius: 10;");
        logoutVBox.getChildren().add(menuItem);

        menuContainer.getChildren().add(logoutVBox);
    }

    private void addHomeButton() {
        Label iconLabel = new Label(SidebarState.HOME.getIcon());
        iconLabel.setFont(Font.font(18));
        iconLabel.setStyle("-fx-text-fill: white;");

        Label textLabel = new Label(SidebarState.HOME.getText());
        textLabel.setFont(Font.font(14));
        textLabel.setStyle("-fx-text-fill: white;");
        textLabel.setVisible(isExpanded);
        textLabel.setManaged(isExpanded);

        HBox menuItem = new HBox(5, iconLabel, textLabel);
        menuItem.setAlignment(isExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
        menuItem.setStyle("-fx-padding: 3px;");

        menuItem.setOnMouseClicked(_ -> navigateToHomePage());

        VBox homeVBox = new VBox();
        homeVBox.setStyle("-fx-background-color: #51209d; -fx-padding: 2px; -fx-border-radius: 10; -fx-background-radius: 10;");
        homeVBox.getChildren().add(menuItem);

        menuContainer.getChildren().add(homeVBox);
    }

    private void navigateToWelcomePage() {
        new WelcomePage(stage);
    }

    private void navigateToHomePage() {
        User currentUser = getCurrentUser();
        new HomePage(currentUser, stage);
    }



    public User getCurrentUser() {
        return currentUser;
    }

    public interface SidebarListener {
        void onUserDetailsSelected();

        void onFavouritesSelected();

        void onWatchedSelected();

        void onSubscriptionSelected();
        void onAboutUsSelected();

    }
}