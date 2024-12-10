package com.example.fms_market;

import com.graphbuilder.struc.Stack;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.BarChart;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

public class Revenue_page  {

    String cuurent_date=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate parsedDate = LocalDate.parse(cuurent_date, formatter);

    public Revenue_page(Stage stage,User admin){
        Banner.setCurrentUser(admin);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int stageWidth = (int) screenSize.getWidth();
        int stageHeight = (int) (screenSize.getHeight() / 1.1);


        GridPane showContainer =createShowContainer();
        BorderPane layout = new BorderPane();
        layout.setTop(Banner.getBanner(stage, "revenue"));














        String[] months = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };

        Text previousMonth = createMonthLabel(months[parsedDate.getMonthValue() - 2]);
        Text currentMonth = createMonthLabel(months[parsedDate.getMonthValue() - 1]);
        Text nextMonth;
        if(parsedDate.getMonthValue()==12){ nextMonth = createMonthLabel("January");}
        else {nextMonth = createMonthLabel(months[parsedDate.getMonthValue()]);}

        currentMonth.setFont(Font.font(40));
        currentMonth.setFill(Color.BLACK);





        CategoryAxis xAxis = new CategoryAxis();

        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sample Data");

        Rectangle rectangle = new Rectangle();

                int cuurent_index= getMonthIndex(currentMonth.getText(),months);

        StackPane root = new StackPane(rectangle,barChart);

        root.setPrefSize(stageWidth * 0.8, stageHeight * 0.6);

        root.setMinSize(300, 200);
        root.setMaxSize(500, 400);





/////////////////////////////////////////////////////////////////////////////////////////
        Text title = createStyledText("Admin Panel", "50px", "white");
        StackPane.setAlignment(title, Pos.TOP_CENTER);
        title.setTranslateX(stageWidth*0.1);
        title.setTranslateY(stageHeight*0.1);
        /////////////////////////////////////////////////////////////////////////////////////////
        ImageView imageView = new ImageView("file:src/main/resources/image/boxes (2).png");
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
        System.out.println(cuurent_index+" "+Subscription.getFreq_month()[cuurent_index][2]);
        series.getData().add(new XYChart.Data<>("Basic",basic_arr));
        series.getData().add(new XYChart.Data<>("Standard", standard_arr));
        series.getData().add(new XYChart.Data<>("Premium", premium_arr));





        series.getData().sort((d1, d2) -> Double.compare(d1.getYValue().doubleValue(), d2.getYValue().doubleValue()));

        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                String color = getColorForValue(i);

                data.getNode().setStyle("-fx-bar-fill: " + color + ";");
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
            handleScroll(event, hbox, months); // معالجة التمرير
            update_index(basic, Standard, Permium, currentMonth, months, root, stageWidth, stageHeight); // تحديث العرض
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
        createSimpleLineChart(chartContainer, stageWidth, stageHeight,currentMonth,months);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(title,stackPane,root,hbox,chartContainer);
        StackPane stackPane2 = new StackPane();
        stackPane2.setMinWidth(stageWidth * 0.8);
        stackPane2.setMinHeight(stageHeight * 2);
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
    public static Text createStyledText(String content, String fontSize, String color) {
        Text text = new Text(content);
        text.setStyle("-fx-font-size: " + fontSize + "; -fx-fill: " + color + ";");
        return text;
    }
private String getColorForValue(int index) {
    switch (index) {
        case 0: return "red";
        case 1: return "green";
        case 2: return "purple";
        default: return "gray";
    }
}
    private Text createMonthLabel(String month) {
        Text text = new Text(month);
        text.setFont(Font.font(20));
        text.setFill(Color.GRAY);
        return text;
    }

    private int shiftMonthsRight(HBox hbox, String[] months) {
        Text first = (Text) hbox.getChildren().get(0);
        Text middle = (Text) hbox.getChildren().get(1);
        Text last = (Text) hbox.getChildren().get(2);

        int middleIndex = getMonthIndex(middle.getText(), months);
        int prevIndex = (middleIndex - 1 + months.length) % months.length;
        int nextIndex = (middleIndex + 1) % months.length;

        first.setText(months[prevIndex]);
        middle.setText(months[nextIndex]);
        last.setText(months[(nextIndex + 1) % months.length]);

        updateMonthColors(hbox);
        return nextIndex;
    }

    // تحريك الشهور إلى اليسار
    private int shiftMonthsLeft(HBox hbox, String[] months) {
        Text first = (Text) hbox.getChildren().get(0);
        Text middle = (Text) hbox.getChildren().get(1);
        Text last = (Text) hbox.getChildren().get(2);

        int middleIndex = getMonthIndex(middle.getText(), months);
        int prevIndex = (middleIndex - 1 + months.length) % months.length;
        int nextIndex = (middleIndex + 1) % months.length;

        first.setText(months[(prevIndex - 1 + months.length) % months.length]);
        middle.setText(months[prevIndex]);
        last.setText(months[nextIndex]);

        updateMonthColors(hbox);
        return nextIndex;
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
            if (i == 1) { // الشهر في المنتصف
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
        series.getData().add(new XYChart.Data<>("Premium", Subscription.getFreq_month()[current_index][2]));


        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Subscription Type");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");
        yAxis.setLowerBound(0);
        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);

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


        // تخصيص ألوان الأعمدة
        Platform.runLater(() -> {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                String color = getColorForValue(i);
                data.getNode().setStyle("-fx-bar-fill: " + color + ";");
            }
        });
    }



    private void createSimpleLineChart(StackPane chartContainer, double stageWidth, double stageHeight, Text currentMonth, String[] months) {
        int current_index = getMonthIndex(currentMonth.getText(), months);
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Subscription Type");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Subscriptions");

        int arr_revenue[]= new int[12];
        int max_revenue=-1;
        for (int i=0;i<12;i++){

            arr_revenue[i]=Subscription.getFreq_month()[i][0]*Subscription.price_basic+
                    Subscription.getFreq_month()[i][1]*Subscription.price_standard+
                    Subscription.getFreq_month()[i][2]*Subscription.price_permium;
            if( arr_revenue[i]>max_revenue){max_revenue=arr_revenue[i];}

            System.out.println( arr_revenue[i]+" ");
            series.getData().add(new XYChart.Data<>(months[i],arr_revenue[i] ));

        }

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(series);
        lineChart.setTitle("Subscription Frequencies");

        xAxis.setTickLabelFill(Color.BLACK);
        yAxis.setTickLabelFill(Color.BLACK);

        lineChart.setPrefWidth(stageWidth * 0.7);
        lineChart.setPrefHeight(stageHeight * 0.5);
        lineChart.setTranslateY(stageHeight * 0.35);
        lineChart.setTranslateX(stageWidth * 0.14);

        Rectangle rectangle = new Rectangle();

        rectangle.setWidth(stageWidth*0.4);
        rectangle.setHeight(stageHeight*0.6);
        rectangle.setTranslateY(stageHeight*0.3);
        rectangle.setTranslateX(stageWidth*0.15);
        rectangle.setFill(Color.web("#F4F4F4"));
        chartContainer.setPrefSize(stageWidth * 0.8, stageHeight * 0.6);

        chartContainer.setMinSize(300, 200);
        chartContainer.setMaxSize(500, 400);


        chartContainer.getChildren().clear();
        chartContainer.getChildren().addAll(rectangle,lineChart);
    }

}

