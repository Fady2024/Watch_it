package com.example.fms_market.util;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
    private static final LanguageManager instance = new LanguageManager();

    private final StringProperty language = new SimpleStringProperty("English");
    private final List<Runnable> actionListeners = new ArrayList<>();


    public static LanguageManager getInstance() {
        return instance;
    }

    public void toggleLanguage() {
        if ("English".equals(language.get())) {
            language.set("German");
        } else {
            language.set("English");
        }
        notifyListeners();
    }

    public void addActionListener(Runnable listener) {
        actionListeners.add(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : actionListeners) {
            listener.run();
        }
    }

    public StringProperty languageProperty() {
        return language;
    }

    public String getLanguage() {
        return language.get();
    }
    public static Label TcreateLanguageitle(String g, String e, String fontSize, String color) {
        StringProperty textProperty = new SimpleStringProperty();

        LanguageManager.getInstance().addActionListener(() -> {
            if ("German".equals(LanguageManager.getInstance().getLanguage())) {
                textProperty.set(g);
            } else {
                textProperty.set(e);
            }
        });

        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            textProperty.set(g);
        } else {
            textProperty.set(e);
        }

        Label label = new Label();
        label.setStyle(STR."-fx-font-size: \{fontSize}; -fx-text-fill: \{color};");
        label.textProperty().bind(textProperty);
        return label;
    }



    public static Text createLanguageText(String g, String e,String fontSize, String color){

        StringProperty text_property = new SimpleStringProperty();

        LanguageManager.getInstance().addActionListener(() -> { if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            text_property.set(g);

        } else {
            text_property.set(e);


        }
        });
        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            text_property.set(g);

        } else {
            text_property.set(e);


        }

        Text text = new Text();
        text.setStyle(STR."-fx-font-size: \{fontSize}; -fx-fill: \{color};");
        text.textProperty().bind(text_property);

        return text;

    }
    public static String getLanguageBasedString(String g, String e) {
        return "German".equals(LanguageManager.getInstance().getLanguage()) ? g : e;
    }
    public static Button createLanguageButton(String g, String e, String fontSize, String color) {
        StringProperty buttonTextProperty = new SimpleStringProperty();

        LanguageManager.getInstance().addActionListener(() -> {
            if ("German".equals(LanguageManager.getInstance().getLanguage())) {
                buttonTextProperty.set(g);
            } else {
                buttonTextProperty.set(e);
            }
        });

        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            buttonTextProperty.set(g);
        } else {
            buttonTextProperty.set(e);
        }

        Button button = new Button();
        button.setStyle(STR."-fx-font-size: \{fontSize}; -fx-text-fill: \{color};");
        button.textProperty().bind(buttonTextProperty);

        return button;
    }




    public static TextField languageTextField(String g, String e, String fontSize, String color) {
        StringProperty promptTextProperty = new SimpleStringProperty();

        LanguageManager.getInstance().addActionListener(() -> {
            if ("German".equals(LanguageManager.getInstance().getLanguage())) {
                promptTextProperty.set(g);
            } else {
                promptTextProperty.set(e);
            }
        });

        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            promptTextProperty.set(g);
        } else {
            promptTextProperty.set(e);
        }

        TextField textField = new TextField();
        textField.setStyle(STR."-fx-font-size: \{fontSize}; -fx-text-fill: \{color};");

        textField.promptTextProperty().bind(promptTextProperty);

        return textField;
    }

}
