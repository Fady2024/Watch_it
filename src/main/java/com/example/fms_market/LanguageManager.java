package com.example.fms_market;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LanguageManager {
    private static LanguageManager instance = new LanguageManager();

    private StringProperty language = new SimpleStringProperty("English");
    private final List<Runnable> actionListeners = new ArrayList<>();

    private LanguageManager() {}

    public static LanguageManager getInstance() {
        return instance;
    }

    // دالة لتغيير اللغة
    public void toggleLanguage() {
        if ("English".equals(language.get())) {
            language.set("German");
        } else {
            language.set("English");
        }
        notifyListeners();
    }

    // إضافة مستمع للتغيير
    public void addActionListener(Runnable listener) {
        actionListeners.add(listener);
    }

    // إعلام المستمعين عند حدوث تغيير في اللغة
    private void notifyListeners() {
        for (Runnable listener : actionListeners) {
            listener.run();
        }
    }

    // الحصول على خاصية اللغة
    public StringProperty languageProperty() {
        return language;
    }

    public String getLanguage() {
        return language.get();
    }
    public static Label TcreateLanguageitle(String g, String e, String fontSize, String color) {
        // إنشاء خاصية للنص
        StringProperty textProperty = new SimpleStringProperty();

        // إضافة مستمع لتحديث النص عند تغيير اللغة
        LanguageManager.getInstance().addActionListener(() -> {
            if ("German".equals(LanguageManager.getInstance().getLanguage())) {
                textProperty.set(g); // النص باللغة الألمانية
            } else {
                textProperty.set(e); // النص باللغة الإنجليزية
            }
        });

        // ضبط النص الحالي بناءً على اللغة الحالية
        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            textProperty.set(g);
        } else {
            textProperty.set(e);
        }

        // إنشاء كائن Label وربطه بالخاصية
        Label label = new Label();
        label.setStyle("-fx-font-size: " + fontSize + "; -fx-text-fill: " + color + ";");
        label.textProperty().bind(textProperty);

        // إرجاع الكائن
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
        text.setStyle("-fx-font-size: " + fontSize + "; -fx-fill: " + color + ";");
        text.textProperty().bind(text_property);

        return text;

    }
    public static Button createLanguageButton(String g, String e, String fontSize, String color) {
        // إنشاء خاصية للنص
        StringProperty buttonTextProperty = new SimpleStringProperty();

        // إضافة مستمع لتغيير النص عند تغيير اللغة
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
        button.setStyle("-fx-font-size: " + fontSize + "; -fx-text-fill: " + color + ";");
        button.textProperty().bind(buttonTextProperty);

        return button;
    }




    public static TextField languageTextField(String g, String e, String fontSize, String color) {
        // إنشاء StringProperty لتخزين النص التلميحي
        StringProperty promptTextProperty = new SimpleStringProperty();

        // إضافة مستمع لتغيير اللغة
        LanguageManager.getInstance().addActionListener(() -> {
            if ("German".equals(LanguageManager.getInstance().getLanguage())) {
                promptTextProperty.set(g);  // إذا كانت اللغة ألمانية
            } else {
                promptTextProperty.set(e);  // إذا كانت اللغة الإنجليزية أو أخرى
            }
        });

        // تعيين النص التلميحي بناءً على اللغة الحالية
        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            promptTextProperty.set(g);  // تعيين النص التلميحي لألمانية
        } else {
            promptTextProperty.set(e);  // تعيين النص التلميحي للإنجليزية
        }

        // إنشاء TextField وتعيين الخصائص الخاصة به
        TextField textField = new TextField();
        textField.setStyle("-fx-font-size: " + fontSize + "; -fx-text-fill: " + color + ";");

        // ربط النص التلميحي بـ promptTextProperty
        textField.promptTextProperty().bind(promptTextProperty);

        return textField;
    }

}
