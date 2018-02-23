package com.ntpclientmonitor.src.datamodel;

import java.util.ArrayList;

public class HistoryDataGroup extends DataGroup {
    //
    private final String historyTable = "history";
    //
    private ArrayList<HistoryData> historyData;

    HistoryDataGroup() {
        super();
    }

    @Override
    protected void getData() {
    }

    public final ArrayList<HistoryData> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(ArrayList<HistoryData> historyData) {
        this.historyData = historyData;
        super.notifyObservers();
    }
}