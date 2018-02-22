package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.SystemUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class ServicePane extends GridPane {
    public ServicePane() {
        super();

        SystemUtils.ServiceInfo serviceInfo = SystemUtils.getInstance().getServiceInfo("NTP");

        setPadding(new Insets(10, 10, 10, 10));

        setHgap(10);
        setVgap(10);

        double[] xPercents = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        for (double xPercent : xPercents) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(xPercent);
            getColumnConstraints().add(col);
        }
        double[] yPercents = {20, 20, 20, 20, 20};
        for (double yPercent : yPercents) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(yPercent);
            getRowConstraints().add(row);
        }

        Label nameLabel = new Label("Name");
        GridPane.setHalignment(nameLabel, HPos.RIGHT);
        add(nameLabel, 0, 0);
        TextField nameDataTextArea = new TextField(serviceInfo.getName());
        nameDataTextArea.setEditable(false);
        add(nameDataTextArea, 1, 0, 1, 1);

        Label captionLabel = new Label("Caption");
        GridPane.setHalignment(captionLabel, HPos.RIGHT);
        add(captionLabel, 0, 1);
        TextField cationDataTextArea = new TextField(serviceInfo.getCaption());
        cationDataTextArea.setEditable(false);
        add(cationDataTextArea, 1, 1, 4, 1);

        Label descritionLabel = new Label("Description");
        GridPane.setHalignment(descritionLabel, HPos.RIGHT);
        add(descritionLabel, 0, 2);
        TextArea descriptionDataTextArea = new TextArea(serviceInfo.getDescription());
        descriptionDataTextArea.setEditable(false);
        descriptionDataTextArea.setWrapText(true);
        add(descriptionDataTextArea, 1, 2, 4, 2);

        Label pathNameLabel = new Label("Path");
        GridPane.setHalignment(pathNameLabel, HPos.RIGHT);
        add(pathNameLabel, 5, 0);
        TextArea pathNameDataTextArea = new TextArea(serviceInfo.getPathName());
        pathNameDataTextArea.setEditable(false);
        pathNameDataTextArea.setWrapText(true);
        add(pathNameDataTextArea, 6, 0, 4, 2);

        Label startModeLabel = new Label("Start mode");
        GridPane.setHalignment(startModeLabel, HPos.RIGHT);
        add(startModeLabel, 5, 2);
        TextField startModeDataTextArea = new TextField(serviceInfo.getStartMode());
        startModeDataTextArea.setEditable(false);
        add(startModeDataTextArea, 6, 2, 4, 1);

        Label stateLabel = new Label("State");
        GridPane.setHalignment(stateLabel, HPos.RIGHT);
        add(stateLabel, 5, 3);
        TextField stateDataTextArea = new TextField(serviceInfo.getState());
        stateDataTextArea.setEditable(false);
        add(stateDataTextArea, 6, 3, 4, 1);

        Button startButton = new Button("Start");
        startButton.setDisable(serviceInfo.getState().equals("Running"));
        startButton.setOnAction(event -> {
        });
        startButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(startButton, 0, 4, 5, 1);

        Button stopButton = new Button("Stop");
        stopButton.setDisable(serviceInfo.getState().equals("Stopped"));
        stopButton.setOnAction(event -> {
        });
        stopButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(stopButton, 5, 4, 5, 1);
    }
}