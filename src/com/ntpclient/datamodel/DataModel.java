package com.ntpclient.datamodel;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private static DataModel ourInstance = new DataModel();
    // database
    private static String dbFileName = "ntp-client.db";
    private final HistoryDataGroup historyDataGroup = new HistoryDataGroup();
    private final OptionDataGroup optionDataGroup = new OptionDataGroup();
    private final ServerDataGroup serverDataGroup = new ServerDataGroup();
    // application data
    private int selectedServerId = -1;
    // data groups
    private List<DataGroup> dataGroups = new ArrayList<>();
    // the one and only sql connection for thread access
    private Connection connection;

    protected DataModel() {

    }

    public static DataModel getInstance() {
        return ourInstance;
    }

    public synchronized int getSelectedServerId() {
        return selectedServerId;
    }

    public synchronized void setSelectedServerId(int selectedServerId) {
        this.selectedServerId = selectedServerId;
    }

    public synchronized Connection getConnection() {
        return connection;
    }

    public synchronized HistoryDataGroup getHistoryDataGroup() {
        return historyDataGroup;
    }

    public synchronized OptionDataGroup getOptionDataGroup() {
        return optionDataGroup;
    }

    public synchronized ServerDataGroup getServerDataGroup() {
        return serverDataGroup;
    }

    /**
     * connect to the database and set up observers
     */
    public void connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:db/" + dbFileName;
        try {
            connection = DriverManager.getConnection(url);
            DatabaseMetaData meta = connection.getMetaData();
            System.out.println("SQLite driver is '" + meta.getDriverName() + "'");
            // add data groups
            dataGroups.add(historyDataGroup);
            dataGroups.add(optionDataGroup);
            DataModel.getInstance().setSelectedServerId(193);
            dataGroups.add(serverDataGroup);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getData() {
        for (DataGroup dataGroup : dataGroups) {
            dataGroup.getData();
        }
    }

    public boolean hasValidHistoryEntries() {
        return (DataModel.getInstance().getHistoryDataGroup().getHistoryEntries().size() > 1);
    }

    public HistoryDataGroup.HistoryEntry getFirstHistoryEntry() {
        return DataModel.getInstance().getHistoryDataGroup().getHistoryEntries().get(0);
    }

    public HistoryDataGroup.HistoryEntry getLastHistoryEntry() {
        return DataModel.getInstance().getHistoryDataGroup().getHistoryEntries().get(
                DataModel.getInstance().getHistoryDataGroup().getHistoryEntries().size() - 1
        );
    }

    public boolean hasValidOptionMap() {
        return (DataModel.getInstance().getOptionDataGroup().getOptionMap().size() > 1);
    }

    public OptionDataGroup.OptionEntry getOptionEntry(String option) {
        if (hasValidOptionMap()) {
            return DataModel.getInstance().getOptionDataGroup().getOptionMap().get(option);
        }
        return null;
    }

    public boolean hasValidServerMap() {
        return (DataModel.getInstance().getServerDataGroup().getServerMap().size() > 1);
    }

    public ServerDataGroup.ServerEntry getSelectedServer() {
        return DataModel.getInstance().getServerDataGroup().getServerMap().get(
                DataModel.getInstance().getSelectedServerId()
        );
    }
}