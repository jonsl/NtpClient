package com.ntpclient.src.ui;

import com.ntpclient.src.datamodel.DataModel;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("NTP Client");

        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        final StackPane sp1 = new StackPane();
        HistoryChart historyChart = new HistoryChart();
        sp1.getChildren().add(historyChart);
        final StackPane sp2 = new StackPane();
        Button button = new Button("Button Two");
        button.setStyle("-fx-font-size: 12pt;");
        button.setMaxHeight(Control.USE_PREF_SIZE);
        sp2.getChildren().add(button);
        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.5f);
        StackPane root = new StackPane();
        root.getChildren().add(sp);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();

        // connect to database and get data
        DataModel.getInstance().connect();
        DataModel.getInstance().getData();
    }
}