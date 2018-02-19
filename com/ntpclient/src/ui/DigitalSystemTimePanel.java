package com.ntpclient.src.ui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

public class DigitalSystemTimePanel extends GridPane {
    private final Timer timer;
    private TimeZoneType timeZoneType;
    private TimeDateLabel dateLabel;
    private TimeDateLabel dayLabel;
    private TimeDateLabel timeLabel;

    public DigitalSystemTimePanel() {
        super();

        setStyle("-fx-background-color:black;");

        ColumnConstraints col1 = new ColumnConstraints(110);
        col1.setHgrow(Priority.ALWAYS);
        ColumnConstraints col2 = new ColumnConstraints(110);
        col2.setHgrow(Priority.ALWAYS);

        getColumnConstraints().addAll(col1, col2);

        this.timer = new Timer(true);
        this.timeZoneType = TimeZoneType.LOCAL_TIMEZONE;

        setPadding(new Insets(0, 3, 0, 3));

        this.dateLabel = new TimeDateLabel("date");
        add(this.dateLabel, 0, 0, 1, 1);

        this.timeLabel = new TimeDateLabel("time");
        add(this.timeLabel, 0, 1, 2, 1);

        this.dayLabel = new TimeDateLabel("day");
        add(this.dayLabel, 1, 0, 1, 1);

        setMinWidth(USE_COMPUTED_SIZE);

        this.timer.scheduleAtFixedRate(new UpdateTimeTask(), 100, 100);
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