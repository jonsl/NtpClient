package com.ntpclientmonitor.datamodel;

public interface Observer {
    /**
     * events received from com.ntp_client.datamodel.Observable
     */
    void onNotify();
}