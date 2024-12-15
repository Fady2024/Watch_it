package com.example.fms_market;

import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.fms_market.Banner.currentUser;

public class Developer_page {
    private int currentIndex = 0;
    private List<Team_members> teamMembers;
    private User user;
    private Stage stage;

    public Developer_page(User user, Stage stage,Sidebar.SidebarState initialState) throws IOException {
        this.user=user;
        this.stage=stage;
        Sidebar sidebar = new Sidebar(initialState, stage, currentUser);
        teamMembers = getTeamMembers();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);

        Button backButton = new Button("Back");
        backButton.setFont(Font.font(25));
        ImageView backImageView = new ImageView(new Image("images/leftwards_white.png"));
        backImageView.setFitWidth(40);
        backImageView.setFitHeight(40);
        backImageView.setPreserveRatio(true);
        backButton.setGraphic(backImageView);
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        //backButton.setOnAction(e -> navigate());

        sidebar.setSidebarListener(new Sidebar.SidebarListener() {
            @Override
            public void onUserDetailsSelected() {
                new AccountPage(user, stage, Sidebar.SidebarState.USER_DETAILS);

            }

            @Override
            public void onFavouritesSelected() {
                navigateToFavoritesPage();
            }

            @Override
            public void onWatchedSelected() {
                // Placeholder for watched selection action
                System.out.println("Watched menu item selected.");
            }

            @Override
            public void onSubscriptionSelected() {
                // Placeholder for subscription selection action
                System.out.println("Subscription menu item selected.");
            }
            public void onAboutUsSelected(){
                navigateToAboutUs();

            }
        });

        VBox headerBox = new VBox(5);
        headerBox.setPadding(new Insets(10));
        headerBox.setAlignment(Pos.TOP_LEFT);
        headerBox.setPadding(new Insets(10, 40, -20, 20));

        Label projectName = new Label("WATCH IT");
        projectName.setFont(Font.font(28));
        projectName.setTextFill(Color.WHITE);
        headerBox.getChildren().addAll(backButton, projectName);

        String fullDescription = "Watch It is a comprehensive movie platform designed to enhance your cinematic experience. " +
                "Discover new favorites through personalized recommendations tailored to your unique tastes. " +
                "Create a personal profile to track your watched movies, rate your favorites, and build a custom watchlist. " +
                "With a user-friendly interface and intuitive navigation, Watch It makes it easy to find, watch, and enjoy the movies you love.";

        Label descriptionLabel = new Label(fullDescription);
        descriptionLabel.setFont(Font.font(18));
        descriptionLabel.setTextFill(Color.web("#7D7C7C"));
        descriptionLabel.setWrapText(true);

        VBox descriptionContainer = new VBox(10, descriptionLabel);
        descriptionContainer.setAlignment(Pos.TOP_LEFT);
        descriptionContainer.setPadding(new Insets(10, 300, -20, 50));

        Label title = new Label("Project Team");
        title.setFont(Font.font(28));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(20, 0, 10, 0));

        VBox descriptionBox = new VBox(10, descriptionContainer, title);
        descriptionBox.setAlignment(Pos.TOP_LEFT);
        descriptionBox.setPadding(new Insets(10, 20, 20, 20));

        HBox teamCarousel = new HBox(90);
        teamCarousel.setAlignment(Pos.CENTER);

        Button leftButton = new Button();
        ImageView leftArrowImage = new ImageView(new Image("images/left_arrow.png"));
        leftArrowImage.setFitWidth(40);
        leftArrowImage.setFitHeight(40);
        leftButton.setGraphic(leftArrowImage);

        Button rightButton = new Button();
        ImageView rightArrowImage = new ImageView(new Image("images/right_arrow.png"));
        rightArrowImage.setFitWidth(40);
        rightArrowImage.setFitHeight(40);
        rightButton.setGraphic(rightArrowImage);

        styleButton(leftButton);
        styleButton(rightButton);

        leftButton.setOnAction(e -> navigate(teamCarousel, -1));
        rightButton.setOnAction(e -> navigate(teamCarousel, 1));

        HBox teamDisplayWithButtons = new HBox(40, leftButton, teamCarousel, rightButton);
        teamDisplayWithButtons.setAlignment(Pos.CENTER);
        teamDisplayWithButtons.setPadding(new Insets(0, 20, 0, 20));
        TeamDisplay(teamCarousel);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(headerBox, descriptionBox, teamDisplayWithButtons);
        layout.setBackground(new Background(new BackgroundFill(Color.web("#1c1c1c"), CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setTitle("Developer Page");
        stage.setScene(scene);
        stage.show();

    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #1c1c1c; " +
                "-fx-background-size: 40px 40px; " +
                "-fx-text-fill: white;");
        button.setPrefSize(40, 40);
    }

    private void TeamDisplay(HBox teamDisplay) {
        teamDisplay.getChildren().clear();

        Team_members leftMember = teamMembers.get((currentIndex - 1 + teamMembers.size()) % teamMembers.size());
        VBox leftBox = MemberBox(leftMember, 150, 150, true, false);

        Team_members centerMember = teamMembers.get(currentIndex);
        VBox centerBox = MemberBox(centerMember, 250, 250, true, true);

        Team_members rightMember = teamMembers.get((currentIndex + 1) % teamMembers.size());
        VBox rightBox = MemberBox(rightMember, 150, 150, true, false);

        teamDisplay.getChildren().addAll(leftBox, centerBox, rightBox);
    }


    private VBox MemberBox(Team_members member, double width, double height, boolean showTasks, boolean isCenterMember) {
        VBox memberBox = new VBox(10);
        memberBox.setAlignment(Pos.CENTER);

        ImageView memberImage = new ImageView(member.getPersonImage());
        memberImage.setFitWidth(width);
        memberImage.setFitHeight(height);
        memberImage.setPreserveRatio(false);

        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        memberImage.setClip(clip);

        StackPane imageContainer = new StackPane();
        imageContainer.getChildren().add(memberImage);
        imageContainer.setStyle("-fx-border-color: 1c1c1c; -fx-border-radius: 15; -fx-background-radius: 15;");
        imageContainer.setPrefSize(width, height);

        Label memberName = new Label(member.getName());
        memberName.setTextFill(Color.WHITE);
        memberName.setFont(Font.font(18));
        memberName.setAlignment(Pos.CENTER);

        VBox imageAndNameBox = new VBox(10, imageContainer, memberName);
        imageAndNameBox.setAlignment(Pos.CENTER);
        imageAndNameBox.setPrefHeight(height + 30);

        ScrollPane tasksScrollPane = new ScrollPane();
        tasksScrollPane.setFitToWidth(true);
        tasksScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tasksScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        if (showTasks && isCenterMember) {
            Label tasks = new Label(String.join("\n", member.getTasks()));
            tasks.setWrapText(true);
            tasks.setMaxWidth(200);
            tasks.setMinWidth(200);
            tasks.setFont(Font.font(13));
            tasks.setTextFill(Color.web("#7D7C7C"));
            tasks.setAlignment(Pos.CENTER);

            tasksScrollPane.setContent(tasks);
            tasksScrollPane.setPrefHeight(100);
            tasksScrollPane.setStyle("-fx-background-color: #1c1c1c; -fx-background: #1c1c1c;");
            memberBox.getChildren().addAll(imageAndNameBox, tasksScrollPane);
        } else {
            memberBox.getChildren().add(imageAndNameBox);
        }
        return memberBox;
    }



    private void navigate(HBox teamDisplay, int direction) {
        currentIndex = (currentIndex + direction + teamMembers.size()) % teamMembers.size();
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), teamDisplay);
        transition.setByX(direction > 0 ? -0 : 0);
        transition.setOnFinished(e -> TeamDisplay(teamDisplay));
        transition.play();
    }

    private List<Team_members> getTeamMembers() {
        List<Team_members> team = new ArrayList<>();
        team.add(new Team_members("Salma Fawzy", List.of("Class: User Watch record", "Display top-rated shows", "Developer page", "UI design", "GUI"),
                new Image(Objects.requireNonNull(getClass().getResource("image/salma (2).jpg")).toString())));

        team.add(new Team_members("Mahmoud Ahmed", List.of("Class: Movie, GUI", "JSON files, UI design", "Display recent shows", "Display top watched shows", "Add & remove shows"),
                new Image(Objects.requireNonNull(getClass().getResource("image/Mahoud..jpg")).toString())));

        team.add(new Team_members("Sandra Hany", List.of("Class: Series, GUI", "Search & filter (shows/director/actor)", "Categorized movie search results", "Edit shows (show, cast, director)", "Restriction on subscriptions"),
                new Image(Objects.requireNonNull(getClass().getResource("image/Sandra.jpg")).toString())));

        team.add(new Team_members("Sara Emad", List.of("Class: Subscription, GUI", "Subscribed plans", "Admin find highest revenue month"),
                new Image(Objects.requireNonNull(getClass().getResource("image/Sara.jpg")).toString())));

        team.add(new Team_members("Fady Gerges", List.of("Class: User, GUI", "Banner", "Add to favorite", "JSON files, Add Rate", "Display watched shows", "Add shows to watch & Favorite list"),
                new Image(Objects.requireNonNull(getClass().getResource("image/Fady.jpg")).toString())));

        team.add(new Team_members("Marwan Waleed", List.of("Class: Cast, Director", "GUI", "Login in", "Sign up", "Best Actor"),
                new Image(Objects.requireNonNull(getClass().getResource("image/Marwan.jpg")).toString())));

        return team;

    }

    private void navigateToFavoritesPage() {
        try {
            new FavoritesPage(user, stage, Sidebar.SidebarState.FAVOURITES);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void navigateToAboutUs() {
        try {
            new Developer_page(user,stage, Sidebar.SidebarState.ABOUT_US);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onUserDetailsSelected() {
        new AccountPage(user, stage, Sidebar.SidebarState.USER_DETAILS);
    }

}