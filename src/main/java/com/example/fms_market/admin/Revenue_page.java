package com.example.fms_market.admin;

import com.example.fms_market.model.Subscription;
import com.example.fms_market.model.User;
import com.example.fms_market.util.Banner;
import com.example.fms_market.util.LanguageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Revenue_page  {

    String cuurent_date=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate parsedDate = LocalDate.parse(cuurent_date, formatter);

    public Revenue_page(Stage stage, User admin){
        Banner.setCurrentUser(admin);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);


        GridPane showContainer =createShowContainer();
        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "revenue", "RevenuePage"));


        String[] monthsGerman = {
                "Januar", "Februar", "März", "April", "Mai", "Juni",
                "Juli", "August", "September", "Oktober", "November", "Dezember"
        };
        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        String[] selectedMonths;
        if ("German".equals(LanguageManager.getInstance().getLanguage())) {
            selectedMonths = monthsGerman;
        } else {
            selectedMonths = months;
        }

        Text previousMonth = createMonthLabel(selectedMonths[parsedDate.getMonthValue() - 2]);
        Text currentMonth = createMonthLabel(selectedMonths[parsedDate.getMonthValue() - 1]);
        Text nextMonth;
        if(parsedDate.getMonthValue()==12){
            nextMonth = createMonthLabel(LanguageManager.getLanguageBasedString("Januar","January"));
        }
        else {nextMonth = createMonthLabel(selectedMonths[parsedDate.getMonthValue()]);}

        currentMonth.setFont(Font.font(40));
        currentMonth.setFill(Color.BLACK);





        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();


            series.setName(LanguageManager.getLanguageBasedString("Beispieldaten","Sample Data")
            );

        Rectangle rectangle = new Rectangle();

                int cuurent_index= getMonthIndex(currentMonth.getText(),selectedMonths);

        StackPane root = new StackPane(rectangle,barChart);

        root.setPrefSize(stageWidth * 0.8, stageHeight * 0.6);

        root.setMinSize(300, 200);
        root.setMaxSize(500, 400);





/////////////////////////////////////////////////////////////////////////////////////////
        Text title = LanguageManager.createLanguageText("Admin-Bereich","Admin Panel", "50px", "white");
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        title.setTranslateX(stageWidth*0.1);
        title.setTranslateY(stageHeight*0.1);
        /////////////////////////////////////////////////////////////////////////////////////////
        ImageView imageView = new ImageView(LanguageManager.getLanguageBasedString("file:src/main/resources/image/german_boxes-removebg-preview.png","file:src/main/resources/image/boxes (2).png"));

        Text basic = createStyledText(String.valueOf( Subscription.getFreq_month()[cuurent_index][0]), "30px", "white");
        Text Standard = createStyledText(String.valueOf( Subscription.getFreq_month()[cuurent_index][1]), "30px", "white");
        Text Permium = createStyledText(String.valueOf( Subscription.getFreq_month()[cuurent_index][2]), "30px", "white");
        StackPane.setAlignment(imageView, Pos.TOP_CENTER);
        imageView.setTranslateY(stageHeight*0.1);
        imageView.setTranslateX(-stageWidth*0.1);

        StackPane.setAlignment(basic, Pos.TOP_CENTER);
        basic.setTranslateY(stageHeight*0.35);
        basic.setTranslateX(-stageWidth*0.35);
        StackPane.setAlignment(Standard, Pos.TOP_CENTER);
        Standard.setTranslateY(stageHeight*0.35);
        Standard.setTranslateX(-stageWidth*0.15);
        StackPane.setAlignment(Permium, Pos.TOP_CENTER);
        Permium.setTranslateY(stageHeight*0.35);
        Permium.setTranslateX(stageWidth*0.05);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll( imageView,basic, Standard, Permium);
        /////////////////////////////////////////////////////////////////////////////////////////


        barChart.setTitle(Integer.toString(Subscription.getCurrent_year()));

        double basic_arr = Subscription.getFreq_month()[cuurent_index][0];
        double standard_arr = Subscription.getFreq_month()[cuurent_index][1];
        double premium_arr = Subscription.getFreq_month()[cuurent_index][2];
        System.out.println(STR."\{cuurent_index} \{Subscription.getFreq_month()[cuurent_index][2]}");

            series.getData().add(new XYChart.Data<>(LanguageManager.getLanguageBasedString("Prämie","Premium")
                    , premium_arr));

        series.getData().add(new XYChart.Data<>("Basic",basic_arr));
        series.getData().add(new XYChart.Data<>("Standard", standard_arr));




        series.getData().sort(Comparator.comparingDouble(d -> d.getYValue().doubleValue()));

        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                String color = getColorForValue(i);

                data.getNode().setStyle(STR."-fx-bar-fill: \{color};");
            }
        });

        barChart.setPrefWidth(stageWidth * 0.7);
        barChart.setPrefHeight(stageHeight * 0.5);

        barChart.setTranslateY(stageHeight*0.2);
        barChart.setTranslateX(stageWidth*0.14);
        barChart.getData().add(series);

        rectangle.setWidth(stageWidth*0.4);
        rectangle.setHeight(stageHeight*0.6);
        rectangle.setTranslateY(stageHeight*0.15);
        rectangle.setTranslateX(stageWidth*0.15);
        rectangle.setFill(Color.web("#F4F4F4"));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        HBox hbox = new HBox(20);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(previousMonth, currentMonth, nextMonth);
        hbox.setTranslateX(-stageWidth*0.20);
        hbox.setTranslateY(-stageHeight*0.40);
        hbox.setOnScroll(event -> {
            handleScroll(event, hbox, months);
            update_index(basic, Standard, Permium, currentMonth, months, root, stageWidth, stageHeight);
        });
        hbox.setOnMouseClicked(event -> {
            if (event.getX() > hbox.getWidth() / 2) {
                shiftMonthsRight(hbox, months);
                update_index(basic,Standard,Permium,currentMonth,months,root,stageWidth,stageHeight);
            } else {
                shiftMonthsLeft(hbox, months);
                update_index(basic,Standard,Permium,currentMonth,months,root,stageWidth,stageHeight);
            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle("-fx-border-color: transparent;");


        StackPane chartContainer = new StackPane();

            createSimpleLineChart(chartContainer, stageWidth, stageHeight,currentMonth,selectedMonths);

        ImageView image_box_max = new ImageView(LanguageManager.getLanguageBasedString("file:src/main/resources/image/max_box_german.png","file:src/main/resources/image/box_max.png"));
        image_box_max.setScaleX(0.15);
        image_box_max.setScaleY(0.15);

        image_box_max.setTranslateY(stageHeight*0.1);
        image_box_max.setTranslateX(stageWidth*0.05);
        Text max_month=createStyledText(String.valueOf(Subscription.getMax_revenue()),"35px","white");
        max_month.setTranslateY(stageHeight*0.04);
        max_month.setTranslateX(stageWidth*0.04);
        StackPane stack_max_month = new StackPane();
stack_max_month.getChildren().addAll(image_box_max,max_month);
        stack_max_month.setMinSize(300, 200);
        stack_max_month.setMaxSize(500, 400);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(title,stackPane,root,hbox,stack_max_month,chartContainer);
        StackPane stackPane2 = new StackPane();
        stackPane2.setMinWidth(stageWidth * 0.8);
        stackPane2.setMinHeight(stageHeight * 2.4);
        stackPane2.getChildren().addAll( showContainer,vbox);
        scrollPane.setContent(stackPane2);
        layout.setCenter(scrollPane);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.setTitle("revenue");
        stage.show();
    }

    private GridPane createShowContainer() {
        GridPane showContainer = new GridPane();
        showContainer.setPadding(new Insets(20));
        showContainer.setHgap(27);
        showContainer.setVgap(27);
        showContainer.setAlignment(Pos.TOP_LEFT);
        showContainer.setStyle("-fx-background-color: #1c1c1c;");
        return showContainer;
    }
    public  Text createStyledText(String content, String fontSize, String color) {
        Text text = new Text(content);
        text.setStyle(STR."-fx-font-size: \{fontSize}; -fx-fill: \{color};");
        return text;
    }
private String getColorForValue(int index) {
    return switch (index) {
        case 0 -> "red";
        case 1 -> "green";
        case 2 -> "purple";
        default -> "gray";
    };
}
    private Text createMonthLabel(String month) {
        Text text = new Text(month);
        text.setFont(Font.font(20));
        text.setFill(Color.GRAY);
        return text;
    }

    private void shiftMonthsRight(HBox hbox, String[] months) {
        Text first = (Text) hbox.getChildren().get(0);
        Text middle = (Text) hbox.getChildren().get(1);
        Text last = (Text) hbox.getChildren().get(2);

        int middleIndex = getMonthIndex(middle.getText(), months);
        int prevIndex = getMonthIndex(first.getText(), months);
        int nextIndex = getMonthIndex(last.getText(), months);

        first.setText(months[middleIndex]);
        middle.setText(months[nextIndex]);
        last.setText(months[(nextIndex + 1) % months.length]);

        updateMonthColors(hbox);
    }

    private void shiftMonthsLeft(HBox hbox, String[] months) {
        Text first = (Text) hbox.getChildren().get(0);
        Text middle = (Text) hbox.getChildren().get(1);
        Text last = (Text) hbox.getChildren().get(2);

        int middleIndex = getMonthIndex(middle.getText(), months);
        int prevIndex = getMonthIndex(first.getText(), months);
        int nextIndex = getMonthIndex(last.getText(), months);

        first.setText(months[(prevIndex - 1 + months.length) % months.length]);
        middle.setText(months[prevIndex]);
        last.setText(months[middleIndex]);

        updateMonthColors(hbox);
    }
    private void handleScroll(ScrollEvent event, HBox hbox, String[] months) {
        if (event.getDeltaY() > 0) {
            shiftMonthsLeft(hbox, months);
        } else {
            shiftMonthsRight(hbox, months);
        }
    }
    private int getMonthIndex(String month, String[] months) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(month)) {
                return i;
            }
        }
        return -1;
    }

    private void updateMonthColors(HBox hbox) {
        for (int i = 0; i < hbox.getChildren().size(); i++) {
            Text text = (Text) hbox.getChildren().get(i);
            if (i == 1) {
                text.setFont(Font.font(40));
                text.setFill(Color.BLACK);

            } else {
                text.setFont(Font.font(30));
                text.setFill(Color.GRAY);
            }
        }
}
    private void update_index(Text basic, Text Standard, Text Permium, Text currentMonth, String[] months, StackPane chartContainer, double stageWidth, double stageHeight) {
        int current_index = getMonthIndex(currentMonth.getText(), months);


        basic.setText(String.valueOf(Subscription.getFreq_month()[current_index][0]) );
        Standard.setText(String.valueOf(Subscription.getFreq_month()[current_index][1]) );
        Permium.setText(String.valueOf(Subscription.getFreq_month()[current_index][2]));


        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.getData().add(new XYChart.Data<>("Basic", Subscription.getFreq_month()[current_index][0]));
        series.getData().add(new XYChart.Data<>("Standard", Subscription.getFreq_month()[current_index][1]));



        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLowerBound(0);
        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);

        xAxis.setLabel( LanguageManager.getLanguageBasedString("Abonnementtyp","Subscription Type"));
        yAxis.setLabel( LanguageManager.getLanguageBasedString("Frequenz","Frequency"));

        series.getData().add(new XYChart.Data<>( LanguageManager.getLanguageBasedString("Prämie","Premium")
                , Subscription.getFreq_month()[current_index][2]));
        double maxValue = Math.max(Math.max(Subscription.getFreq_month()[current_index][0],
                        Subscription.getFreq_month()[current_index][1]),
                Subscription.getFreq_month()[current_index][2]);
        yAxis.setUpperBound(maxValue + 10);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.getData().add(series);
        barChart.setTitle(Integer.toString(Subscription.getCurrent_year()));


        barChart.setPrefWidth(stageWidth * 0.6);
        barChart.setPrefHeight(stageHeight * 0.4);

        barChart.setPadding(new Insets(30, 30, 40, 30));
        barChart.setTranslateY(stageHeight * 0.2);
        barChart.setTranslateX(stageWidth * 0.14);

        Rectangle rectangle = new Rectangle();

        rectangle.setWidth(stageWidth*0.4);
        rectangle.setHeight(stageHeight*0.6);
        rectangle.setTranslateY(stageHeight*0.15);
        rectangle.setTranslateX(stageWidth*0.15);
        rectangle.setFill(Color.web("#F4F4F4"));
        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(new StackPane(rectangle,barChart));

        chartContainer.setPrefSize(stageWidth * 0.8, stageHeight * 0.6);

        chartContainer.setMinSize(300, 200);
        chartContainer.setMaxSize(500, 400);

        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                String color = getColorForValue(i);
                data.getNode().setStyle(STR."-fx-bar-fill: \{color};");
            }
        });
    }

    private void  createSimpleLineChart(StackPane chartContainer, double stageWidth, double stageHeight, Text currentMonth, String[] months) {
        int current_index = getMonthIndex(currentMonth.getText(), months);
        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();


        XYChart.Series<String, Number> series = new XYChart.Series<>();



        for (int i=0;i<12;i++){

            series.getData().add(new XYChart.Data<>(months[i],Subscription.getArr_revenue()[i]));

        }

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);

            series.setName(LanguageManager.getLanguageBasedString("Abonnements","subscriptions"));
            xAxis.setLabel(LanguageManager.getLanguageBasedString("Abonnementtyp","subscription type"));
            yAxis.setLabel(LanguageManager.getLanguageBasedString("Frequenz","frequency"));
            lineChart.setTitle(LanguageManager.getLanguageBasedString("Abonnementhäufigkeiten","subscription frequencies"));


        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);

        lineChart.setPrefWidth(stageWidth * 0.7);
        lineChart.setPrefHeight(stageHeight * 0.5);
        lineChart.setTranslateY(stageHeight * 0.05);
        lineChart.setTranslateX(stageWidth * 0.14);

        Rectangle rectangle = new Rectangle();

        rectangle.setWidth(stageWidth*0.4);
        rectangle.setHeight(stageHeight*0.6);
        //rectangle.setTranslateY(stageHeight*0.45);
        rectangle.setTranslateX(stageWidth*0.15);
        rectangle.setFill(Color.web("#F4F4F4"));
        chartContainer.setPrefSize(stageWidth * 0.8, stageHeight * 0.6);

        chartContainer.setMinSize(300, 200);
        chartContainer.setMaxSize(500, 400);


        chartContainer.getChildren().clear();
        chartContainer.getChildren().addAll(rectangle,lineChart);
    }

}

