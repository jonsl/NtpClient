package com.ntpclient.src.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Timer;
import java.util.TimerTask;

enum TimeZoneType {LOCAL_TIMEZONE, UTC_TIMEZONE}

class DigitalSystemTimePanel extends GridPane {
    private TimeZoneType timeZoneType;
    private TimeDateLabel dateLabel;
    private TimeDateLabel dayLabel;
    private TimeDateLabel timeLabel;

    DigitalSystemTimePanel() {
        super();

        GridPane gridPane = new GridPane();

        gridPane.setStyle("-fx-background-color:black;");

        ColumnConstraints col1 = new ColumnConstraints(110);
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints(110);
        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        final Timer timer = new Timer(true);
        this.timeZoneType = TimeZoneType.LOCAL_TIMEZONE;

        setPadding(new Insets(0, 3, 0, 3));

        this.dateLabel = new TimeDateLabel("date");
        gridPane.add(this.dateLabel, 0, 0, 1, 1);

        this.timeLabel = new TimeDateLabel("time");
        gridPane.add(this.timeLabel, 0, 1, 2, 1);

        this.dayLabel = new TimeDateLabel("day");
        gridPane.add(this.dayLabel, 1, 0, 1, 1);

        add(gridPane, 0, 0);

        BorderPane borderPane = new BorderPane();

        RadioButton rb1 = new RadioButton("Local time");
        rb1.setUserData("L");
        borderPane.setLeft(rb1);
        GridPane.setHalignment(rb1, HPos.LEFT);
        RadioButton rb2 = new RadioButton("UTC Time");
        rb2.setUserData("U");
        borderPane.setRight(rb2);

        add(borderPane, 0, 1);

        final ToggleGroup group = new ToggleGroup();
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (group.getSelectedToggle().getUserData() != null) {
                    if (group.getSelectedToggle().getUserData().toString().equals("L")) {
                        timeZoneType = TimeZoneType.LOCAL_TIMEZONE;
                    } else if (group.getSelectedToggle().getUserData().toString().equals("U")) {
                        timeZoneType = TimeZoneType.UTC_TIMEZONE;
                    }
                }
            }
        });

        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);

        rb1.setSelected(true);

        timer.scheduleAtFixedRate(new UpdateTimeTask(), 100, 100);
    }

    class UpdateTimeTask extends TimerTask {
        @Override
        public void run() {
            // thread started by the business logic.
            Platform.runLater(() -> {
                dateLabel.updateDisplay();
                timeLabel.updateDisplay();
                dayLabel.updateDisplay();
            });
        }
    }

    class TimeDateLabel extends Label {
        String type;
        DateTimeFormatter formatter;

        public TimeDateLabel(String type) {
            this.type = type;
        }

        public void updateDisplay() {
            setTextFill(Color.web("#00ff00"));
            setStyle("-fx-background-color:black;");
            switch (type) {
                case "date":
                    formatter = new DateTimeFormatterBuilder().appendPattern("dd MMMM yyyy").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.ITALIC, 12));
                    GridPane.setHalignment(this, HPos.LEFT);
                    break;
                case "time":
                    formatter = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss.S").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.REGULAR, 40));
                    GridPane.setHalignment(this, HPos.CENTER);
                    break;
                case "day":
                    formatter = new DateTimeFormatterBuilder().appendPattern("z (Z)").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.ITALIC, 12));
                    GridPane.setHalignment(this, HPos.RIGHT);
                    break;
                default:
                    formatter = new DateTimeFormatterBuilder().toFormatter();
                    break;
            }
            switch (timeZoneType) {
                case LOCAL_TIMEZONE: {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now();
                    setText(formatter.format(zonedDateTime));
                    break;
                }
                case UTC_TIMEZONE: {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
                    setText(formatter.format(zonedDateTime));
                    break;
                }
            }
        }
    }
}