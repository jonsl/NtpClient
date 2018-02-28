package com.ntpclientmonitor.datamodel;

import com.ntpclientmonitor.datamodel.DataGroup;
import com.ntpclientmonitor.datamodel.HistoryData;

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
    public synchronized void getData() {
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