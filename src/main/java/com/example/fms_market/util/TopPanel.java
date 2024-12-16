package com.example.fms_market.util;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TopPanel extends Application {

    private static boolean isDay = true;
    private final int width = 200;
    private final int height = 100;
    private float position;
    private final List<Runnable> actionListeners = new ArrayList<>();
    private final Image sunImage;
    private final Image moonImage;
    private final Canvas canvas;

    public TopPanel() {
        sunImage = new Image(Objects.requireNonNull(getClass().getResource("/sun.png")).toString());
        moonImage = new Image(Objects.requireNonNull(getClass().getResource("/moon.png")).toString());

        canvas = new Canvas(width, height);
        canvas.setOnMouseClicked(this::handleMouseClicked);

        position = isDay ? 0f : 1f;

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isDay) {
                    if (position > 0) {
                        position -= 0.02F;
                    }
                } else {
                    if (position < 1) {
                        position += 0.02F;
                    }
                }
                draw();
            }
        }.start();
    }

    private void handleMouseClicked(MouseEvent e) {
        if (isWithinSwitchBounds(e.getX(), e.getY())) {
            toggleSwitch();
        }
    }

    private boolean isWithinSwitchBounds(double x, double y) {
        int trackWidth = 80;
        int trackHeight = 40;
        int switchX = (width - trackWidth) / 2;
        int switchY = ((height - trackHeight) / 2);

        return x >= switchX && x <= switchX + trackWidth && y >= switchY && y <= switchY + trackHeight;
    }

    public void toggleSwitch() {
        isDay = !isDay;
        notifyListeners();
    }

    private void notifyListeners() {
        for (Runnable listener : actionListeners) {
            listener.run();
        }
    }

    public void addActionListener(Runnable listener) {
        actionListeners.add(listener);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        int trackHeight = 30;
        int trackWidth = 80;
        Color trackColor = lerpColor(Color.rgb(57, 57, 57), position);
        gc.setFill(trackColor);

        gc.fillRoundRect((double) (width - trackWidth) / 2, (double) (height - trackHeight) / 2, trackWidth, trackHeight, trackHeight, trackHeight);

        if (isDay && position < 0.5) {
            gc.setFill(Color.YELLOW);
            drawSunbeams(gc);
        } else if (!isDay && position > 0.5) {
            gc.setFill(Color.WHITE);
            drawStarsInsideCurves(gc);
        }

        int thumbRadius = 15;
        Image thumbImage = position < 0.5 ? sunImage : moonImage;
        gc.drawImage(thumbImage, (double) (width - trackWidth) / 2 + (trackWidth - 2 * trackHeight) * 2.5f * position, (double) height / 2 - thumbRadius, 2 * thumbRadius, 2 * thumbRadius);
    }

    private void drawSunbeams(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        int sunX = (int) ((float) (width - 80) / 2 + 80 * (1 - position) - 15);
        int sunY = 50;

        int shortLineLength = 12;
        int longLineLength = 15;
        int thickness = 3;

        gc.fillRoundRect(sunX - shortLineLength * 3.5f, sunY - 10 - (double) thickness / 2, shortLineLength * 2, thickness, thickness, thickness);
        gc.fillRoundRect(sunX - longLineLength * 2.5f, sunY - (double) thickness / 2, longLineLength * 2, thickness, thickness, thickness);
        gc.fillRoundRect(sunX - shortLineLength * 3.5f, sunY + 8, shortLineLength * 2, thickness, thickness, thickness);
    }

    private void drawStarsInsideCurves(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillOval((double) (width - 80) / 2 + 25, (double) (height - 30) / 2 + 20, 4, 4);
        gc.fillOval((double) (width - 80) / 2 + 25, (double) (height - 30) / 2 + 5, 4, 4);
        gc.fillOval((double) (width - 80) / 2 + 15, (double) (height - 30) / 2 + 13, 4, 4);
        gc.fillOval((double) (width - 80) / 2 + 35, (double) (height - 30) / 2 + 13, 4, 4);
    }

    private Color lerpColor(Color nightColor, float t) {
        int r = clamp((int) (Color.SKYBLUE.getRed() * 255 * (1 - t) + nightColor.getRed() * 255 * t));
        int g = clamp((int) (Color.SKYBLUE.getGreen() * 255 * (1 - t) + nightColor.getGreen() * 255 * t));
        int b = clamp((int) (Color.SKYBLUE.getBlue() * 255 * (1 - t) + nightColor.getBlue() * 255 * t));
        return Color.rgb(r, g, b);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isDayValue() {
        return isDay;
    }
}