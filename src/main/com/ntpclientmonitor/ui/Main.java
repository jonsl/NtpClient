package com.ntpclientmonitor.ui;

import com.ntpclientmonitor.ui.FilePane;
import com.ntpclientmonitor.datamodel.DataModel;
import com.ntpclientmonitor.datamodel.SystemUtils;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("NTP Client Monitor");
        // tree / chart pane
        final StackPane sp1 = new StackPane();
        sp1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        FilePane filePane = new FilePane();
        sp1.getChildren().add(filePane);
        final StackPane sp2 = new StackPane();
        HistoryChart historyChart = new HistoryChart();
        sp2.getChildren().add(historyChart);
        SplitPane horizontalSplitPane = new SplitPane();
        horizontalSplitPane.setOrientation(Orientation.HORIZONTAL);
        horizontalSplitPane.getItems().addAll(sp1, sp2);
        horizontalSplitPane.setDividerPositions(0.3f);
        // tree / chart / option
        final StackPane sp3 = new StackPane();
        OptionPane optionPane = new OptionPane();
        sp3.getChildren().add(optionPane);
        SplitPane verticalSplitPane = new SplitPane();
        verticalSplitPane.setOrientation(Orientation.VERTICAL);
        verticalSplitPane.getItems().addAll(horizontalSplitPane, sp3);
        verticalSplitPane.setDividerPositions(0.6f);
        verticalSplitPane.prefWidthProperty().bind(primaryStage.widthProperty());
        verticalSplitPane.prefHeightProperty().bind(primaryStage.heightProperty());
        // menu
//        MenuBar menuBar = new MenuBar();
//        Menu menuFile = new Menu("File");
//        Menu menuEdit = new Menu("Edit");
//        Menu menuView = new Menu("View");
//        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
//        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        BorderPane root = new BorderPane();
//        root.setTop(menuBar);
        root.setCenter(verticalSplitPane);
        Scene scene = new Scene(root, 800, 600);
        boolean added = scene.getStylesheets().add("ntp-client-monitor.css");
//        filePane.requestFocus();
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.show();

        // connect to database and get data
        DataModel.getInstance().connect();
        DataModel.getInstance().getData();
    }
}