package com.example.fms_market;

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
import javafx.util.Duration;

public class Sidebar extends VBox {

    public enum SidebarState {
        USER_DETAILS("👤", "User Details"),
        FAVOURITES("🌟", "Favourites"),
        WATCHED("🎥", "Watched"),
        SUBSCRIPTION("📅", "Subscription");

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

    private SidebarState selectedState = SidebarState.USER_DETAILS; // Default selected state
    private final VBox menuContainer;
    private final Button toggleButton;
    private SidebarListener listener;
    private boolean isExpanded = true;

    public Sidebar(SidebarState initialState) {
        this.selectedState = initialState;
        this.setStyle("-fx-background-color: #404040;");
        this.setPrefWidth(200);
        this.setAlignment(Pos.TOP_CENTER);

        // Menu container
        menuContainer = new VBox(15);
        menuContainer.setAlignment(Pos.TOP_CENTER);
        menuContainer.setStyle("-fx-padding: 10;");

        // Add menu items
        for (SidebarState state : SidebarState.values()) {
            addMenuItem(state);
        }

        // Toggle button
        toggleButton = new Button("⏪");
        toggleButton.setFont(Font.font(16));
        toggleButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-cursor: hand;");
        toggleButton.setOnAction(event -> toggleSidebar());
        toggleButton.setAlignment(Pos.BOTTOM_CENTER);

        // Add menu container and toggle button to the sidebar
        this.getChildren().addAll(menuContainer, toggleButton);

        // Apply initial selection style
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
        menuItem.setAlignment(Pos.CENTER_LEFT);
        menuItem.setStyle("-fx-padding: 5;");

        // Apply selected style if this item is the selected state
        if (state == selectedState) {
            applySelectedStyle(iconLabel, textLabel);
        }

        menuItem.setOnMouseClicked(event -> {
            // Set the selected state on click
            selectedState = state;
            updateMenuStyles();
            triggerListener(state);

            if (isExpanded) {
                toggleSidebar();
            }
        });

        menuContainer.getChildren().add(menuItem);
    }

    private void toggleSidebar() {
        // Toggle between expanded and minimized states
        isExpanded = !isExpanded;

        // Timeline to smoothly animate the transition between states
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(300), e -> {
                    double targetWidth = isExpanded ? 200 : 80;
                    this.setPrefWidth(targetWidth);
                    toggleButton.setText(isExpanded ? "⏪" : "⏩");

                    // Handle visibility of text labels and alignment for the menu items
                    menuContainer.getChildren().forEach(node -> {
                        if (node instanceof HBox menuItem) {
                            Label textLabel = (Label) menuItem.getChildren().get(1);
                            textLabel.setVisible(isExpanded);
                            textLabel.setManaged(isExpanded);

                            menuItem.setAlignment(isExpanded ? Pos.CENTER_LEFT : Pos.CENTER);
                            menuItem.setStyle(isExpanded ? "-fx-padding: 0;" : "-fx-padding: 5;");
                        }
                    });

                    // Apply curve effect
                    if (isExpanded) {
                        this.setStyle("-fx-background-color: #333;");
                    } else {
                        this.setStyle("-fx-background-color: #333; -fx-border-radius: 10; -fx-background-radius: 10;");
                    }

                    // Set margin based on the expanded state
                    BorderPane.setMargin(this, isExpanded ? Insets.EMPTY : new Insets(15, 0, 15, 0));

                    this.setStyle(isExpanded ? "-fx-background-color: #333;" : "-fx-background-color: #333; -fx-border-radius: 10; -fx-background-radius: 10;");
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

                // Highlight the selected item
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
            }
        }
    }

    public void setSidebarListener(SidebarListener listener) {
        this.listener = listener;
    }

    public interface SidebarListener {
        void onUserDetailsSelected();

        void onFavouritesSelected();

        void onWatchedSelected();

        void onSubscriptionSelected();
    }
}
