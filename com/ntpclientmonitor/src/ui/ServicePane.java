package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.CommandExecutor;
import com.ntpclientmonitor.src.datamodel.Observer;
import com.ntpclientmonitor.src.datamodel.ServiceParser;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

class ServicePane extends GridPane implements Observer {
    private ServiceParser serviceParser;

    private TextField nameDataTextArea;
    private TextField cationDataTextArea;
    private TextArea descriptionDataTextArea;
    private TextArea pathNameDataTextArea;
    private TextField startModeDataTextArea;
    private TextField stateDataTextArea;
    private Button startButton;
    private Button stopButton;

    ServicePane() {
        super();

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
        this.nameDataTextArea = new TextField();
        this.nameDataTextArea.setEditable(false);
        add(this.nameDataTextArea, 1, 0, 1, 1);

        Label captionLabel = new Label("Caption");
        GridPane.setHalignment(captionLabel, HPos.RIGHT);
        add(captionLabel, 0, 1);
        this.cationDataTextArea = new TextField();
        this.cationDataTextArea.setEditable(false);
        add(this.cationDataTextArea, 1, 1, 4, 1);

        Label descritionLabel = new Label("Description");
        GridPane.setHalignment(descritionLabel, HPos.RIGHT);
        add(descritionLabel, 0, 2);
        this.descriptionDataTextArea = new TextArea();
        this.descriptionDataTextArea.setEditable(false);
        this.descriptionDataTextArea.setWrapText(true);
        add(this.descriptionDataTextArea, 1, 2, 4, 2);

        Label pathNameLabel = new Label("Path");
        GridPane.setHalignment(pathNameLabel, HPos.RIGHT);
        add(pathNameLabel, 5, 0);
        this.pathNameDataTextArea = new TextArea();
        this.pathNameDataTextArea.setEditable(false);
        this.pathNameDataTextArea.setWrapText(true);
        add(this.pathNameDataTextArea, 6, 0, 4, 2);

        Label startModeLabel = new Label("Start mode");
        GridPane.setHalignment(startModeLabel, HPos.RIGHT);
        add(startModeLabel, 5, 2);
        this.startModeDataTextArea = new TextField();
        this.startModeDataTextArea.setEditable(false);
        add(this.startModeDataTextArea, 6, 2, 4, 1);

        Label stateLabel = new Label("State");
        GridPane.setHalignment(stateLabel, HPos.RIGHT);
        add(stateLabel, 5, 3);
        this.stateDataTextArea = new TextField();
        this.stateDataTextArea.setEditable(false);
        add(this.stateDataTextArea, 6, 3, 4, 1);

        // process control
        this.startButton = new Button("Start");
        this.startButton.setDisable(true);
        this.startButton.setOnAction(event -> {
        });
        this.startButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(this.startButton, 0, 4, 5, 1);

        this.stopButton = new Button("Stop");
        this.stopButton.setDisable(true);
        this.stopButton.setOnAction(event -> {
        });
        this.stopButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        add(this.stopButton, 5, 4, 5, 1);

        CommandExecutor commandExecutor = new CommandExecutor("wmic service NTP get " +
                "Caption, Description, Name, StartMode, State, PathName /format:list");
        this.serviceParser = new ServiceParser();
        this.serviceParser.addObserver(this);
        commandExecutor.exec(this.serviceParser);
    }

    @Override
    public void onNotify() {
        Platform.runLater(() -> {
            nameDataTextArea.setText(serviceParser.getServiceName());
            cationDataTextArea.setText(serviceParser.getServiceCaption());
            descriptionDataTextArea.setText(serviceParser.getServiceDescription());
            pathNameDataTextArea.setText(serviceParser.getServicePathName());
            startModeDataTextArea.setText(serviceParser.getServiceStartMode());
            stateDataTextArea.setText(serviceParser.getServiceState());
            startButton.setDisable(serviceParser.getServiceState().equals("Running"));
            stopButton.setDisable(serviceParser.getServiceState().equals("Stopped"));
        });
    }
}