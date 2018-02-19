package com.ntpclient.ui;

import com.ntpclient.datamodel.DataModel;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("NTP Client");

        SplitPane sp = new SplitPane();
        sp.setOrientation(Orientation.VERTICAL);
        final StackPane sp1 = new StackPane();
        sp1.getChildren().add(new HistoryChart());
        final StackPane sp2 = new StackPane();
        sp2.getChildren().add(new Button("Button Two"));
        sp.getItems().addAll(sp1, sp2);
        sp.setDividerPositions(0.5f);
        StackPane root = new StackPane();
        root.getChildren().add(sp);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        // connect to database and get data
        DataModel.getInstance().connect();
        DataModel.getInstance().getData();
    }
}