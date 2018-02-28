package com.ntpclientmonitor.ui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

enum TimeZoneType {LOCAL, UTC}

enum TimeDateLabelType {DATE, TIME, DAY};

class DigitalSystemTimePanel extends GridPane {
    private TimeZoneType timeZoneType;
    private TimeDateLabel dateLabel;
    private TimeDateLabel dayLabel;
    private TimeDateLabel timeLabel;

    DigitalSystemTimePanel() {
        super();

        getColumnConstraints().add(new ColumnConstraints(220));

        GridPane gridPane = new GridPane();

        gridPane.setStyle("-fx-background-color:black;");

        ColumnConstraints col1 = new ColumnConstraints(110);
//        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints(110);
//        col2.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(col1, col2);

        final Timer timer = new Timer(true);
        this.timeZoneType = TimeZoneType.LOCAL;

        setPadding(new Insets(0, 3, 0, 3));

        this.dateLabel = new TimeDateLabel(TimeDateLabelType.DATE);
        gridPane.add(this.dateLabel, 0, 0, 1, 1);

        this.timeLabel = new TimeDateLabel(TimeDateLabelType.TIME);
        gridPane.add(this.timeLabel, 0, 1, 2, 1);

        this.dayLabel = new TimeDateLabel(TimeDateLabelType.DAY);
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
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (group.getSelectedToggle().getUserData() != null) {
                if (group.getSelectedToggle().getUserData().toString().equals("L")) {
                    timeZoneType = TimeZoneType.LOCAL;
                } else if (group.getSelectedToggle().getUserData().toString().equals("U")) {
                    timeZoneType = TimeZoneType.UTC;
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
        TimeDateLabelType type;
        DateTimeFormatter formatter;

        TimeDateLabel(TimeDateLabelType type) {
            this.type = type;
        }

        public void updateDisplay() {
            setTextFill(Color.GREEN);
            setStyle("-fx-background-color:black;");
            switch (type) {
                case DATE:
                    formatter = new DateTimeFormatterBuilder().appendPattern("dd MMMM yyyy").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.ITALIC, 12));
                    GridPane.setHalignment(this, HPos.LEFT);
                    break;
                case TIME:
                    formatter = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss.S").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.REGULAR, 40));
                    GridPane.setHalignment(this, HPos.CENTER);
                    break;
                case DAY:
                    formatter = new DateTimeFormatterBuilder().appendPattern("z (Z)").toFormatter();
                    setFont(Font.font("sans-serif", FontPosture.ITALIC, 12));
                    GridPane.setHalignment(this, HPos.RIGHT);
                    break;
                default:
                    formatter = new DateTimeFormatterBuilder().toFormatter();
                    break;
            }
            switch (timeZoneType) {
                case LOCAL: {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now();
                    setText(formatter.format(zonedDateTime));
                    break;
                }
                case UTC: {
                    ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
                    setText(formatter.format(zonedDateTime));
                    break;
                }
            }
        }
    }
}