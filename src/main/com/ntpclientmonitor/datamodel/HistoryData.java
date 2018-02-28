package com.ntpclientmonitor.datamodel;

import java.util.Date;

public class HistoryData {
    private final Date date;
    private final double timeOffset;
    private final double frequencyOffsetPpm;
    private final double rmsJitter;
    private final double allanDeviation;
    private final int clockDiscipline;

    public HistoryData(Date date, double timeOffset, double frequencyOffsetPpm,
                       double rmsJitter, double allanDeviation, int clockDiscipline) {
        this.date = date;
        this.timeOffset = timeOffset;
        this.frequencyOffsetPpm = frequencyOffsetPpm;
        this.rmsJitter = rmsJitter;
        this.allanDeviation = allanDeviation;
        this.clockDiscipline = clockDiscipline;
    }

    public double getRmsJitter() {
        return rmsJitter;
    }

    public double getAllanDeviation() {
        return allanDeviation;
    }

    public int getClockDiscipline() {
        return clockDiscipline;
    }

    public Date getDate() {
        return date;
    }

    public double getTimeOffset() {
        return timeOffset;
    }

    public double getFrequencyOffsetPpm() {
        return frequencyOffsetPpm;
    }
}