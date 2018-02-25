package com.ntpclientmonitor.src.datamodel;

import java.util.ArrayList;

public class HistoryDataGroup extends DataGroup {
    //
    private ArrayList<HistoryData> historyData;
    private boolean newSelection;

    HistoryDataGroup() {
        super();
    }

    @Override
    protected void getData() {
    }

    public final ArrayList<HistoryData> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(ArrayList<HistoryData> historyData, boolean newSelection) {
        this.historyData = historyData;
        this.newSelection = newSelection;
        super.notifyObservers();
    }

    public boolean isNewSelection() {
        return newSelection;
    }
}