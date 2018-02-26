package com.ntpclientmonitor.src.datamodel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HistoryDataGroup extends DataGroup {
    //
    private List<HistoryData> historyData;
    private boolean newSelection;

    HistoryDataGroup() {
        super();
    }

    @Override
    protected void getData() {
    }

    public final List<HistoryData> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(List<HistoryData> historyData, boolean newSelection) {
        this.historyData = historyData;
        this.newSelection = newSelection;
        super.notifyObservers();
    }

    public boolean isNewSelection() {
        return newSelection;
    }
}