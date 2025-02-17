package com.example.fms_market.pages;

import com.example.fms_market.data.ShowJsonHandler;
import com.example.fms_market.data.UserJsonHandler;
import com.example.fms_market.model.Show;
import com.example.fms_market.model.User;
import com.example.fms_market.util.LanguageManager;
import com.example.fms_market.util.ShowCardUtil;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.fms_market.util.Banner.currentUser;
import static com.example.fms_market.util.ShowCardUtil.SHOW_CARD_WIDTH;

public class FavoritesPage {

    private final GridPane showContainer;
    private final BorderPane layout;
    private final User user;
    private final Stage stage;
    private List<Show> favoriteShows;

    public FavoritesPage(User user, Stage stage, Sidebar.SidebarState initialState) throws IOException {
        this.user = user;
        this.stage = stage;

        Sidebar sidebar = new Sidebar(initialState, stage, currentUser);

        showContainer = new GridPane();
        showContainer.setStyle("-fx-background-color: #1c1c1c;");
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(20);
        showContainer.setVgap(20);

        layout = new BorderPane();
        layout.setLeft(sidebar);
        layout.setCenter(showContainer);
        layout.setStyle("-fx-background-color: #1c1c1c;");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle(LanguageManager.getLanguageBasedString("Favoritinnen","Favorites"));
        stage.show();

        sidebar.setSidebarListener(new Sidebar.SidebarListener() {
            @Override
            public void onUserDetailsSelected() {
                new AccountPage(user, stage, Sidebar.SidebarState.USER_DETAILS);
            }

            @Override
            public void onFavouritesSelected() {
            }

            @Override
            public void onWatchedSelected() {
                System.out.println("Watched menu item selected.");
            }

            @Override
            public void onSubscriptionSelected() {
                System.out.println("Subscription menu item selected.");
            }
            public void onAboutUsSelected(){navigateToAboutUs();}
        });


        scene.widthProperty().addListener((_, _, newValue) -> adjustLayout(newValue.doubleValue()));
        loadFavorites();
    }


    private void loadFavorites() throws IOException {
        List<Integer> favoriteShowIds = UserJsonHandler.getFavoriteShows(user.getId());
        List<Show> allShows = ShowJsonHandler.readShows();
        favoriteShows = allShows.stream()
                .filter(show -> favoriteShowIds.contains(show.getId()))
                .collect(Collectors.toList());

        adjustLayout(stage.getScene().getWidth());

        if (favoriteShows.isEmpty()) {
            showEmptyMessage();
        }
    }

    private void adjustLayout(double width) {
        showContainer.getChildren().clear();

        int columns = (int) (width / (SHOW_CARD_WIDTH + 20));
        int column = 0;
        int row = 0;

        for (Show show : favoriteShows) {
            VBox showCard = ShowCardUtil.createShowCard(show, user, stage, this::refreshFavorites);
            showContainer.add(showCard, column, row);

            column++;
            if (column == columns) {
                column = 0;
                row++;
            }
        }
    }

    private void showEmptyMessage() {
        Label emptyLabel = new Label(LanguageManager.getLanguageBasedString("Keine Lieblingssendungen gefunden.","No favorite shows found."));
        emptyLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-alignment: center;");
        emptyLabel.setPadding(new Insets(20));

        BorderPane emptyLayout = new BorderPane(emptyLabel);
        emptyLayout.setStyle("-fx-background-color: #2B2B2B;");
        layout.setCenter(emptyLayout);
    }

    private void refreshFavorites() {
        try {
            loadFavorites();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAboutUs() {
        try {
            new Developer_page(currentUser,stage, Sidebar.SidebarState.ABOUT_US);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}