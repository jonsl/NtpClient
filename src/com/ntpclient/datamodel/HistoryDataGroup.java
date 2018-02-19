package com.ntpclient.datamodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class HistoryDataGroup extends DataGroup {
    //
    private final String historyTable = "history";
    //
    private ArrayList<HistoryEntry> historyEntries;

    public HistoryDataGroup() {
        super();
    }

    public synchronized final ArrayList<HistoryEntry> getHistoryEntries() {
        return historyEntries;
    }

    @Override
    public synchronized void getData() {
        new Thread(() -> {
            getHistoryData();
            notifyObservers();
        }).start();
    }

    public synchronized void addHistoryData(int serverId, double currentTime, double timeOffset, double roundTripDelay) {
        new Thread(() -> {
            String sql = "INSERT INTO " + historyTable +
                    " (server_id, current_time, time_offset, round_trip) VALUES(?,?,?,?)";
            try (PreparedStatement pstmt = DataModel.getInstance().getConnection().prepareStatement(sql)) {
                pstmt.setInt(1, serverId);
                pstmt.setDouble(2, currentTime);
                pstmt.setDouble(3, timeOffset);
                pstmt.setDouble(4, roundTripDelay);
                pstmt.executeUpdate();
                getHistoryData();
                notifyObservers();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void getHistoryData() {
        String sql = "SELECT `id`, `server_id`, `current_time`, `time_offset`, `round_trip` FROM "
                + historyTable + " ORDER BY `current_time` ASC";
        try (Statement stmt = DataModel.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // get result set
            historyEntries = new ArrayList<HistoryEntry>();
            while (rs.next()) {
                HistoryEntry historyEntry = new HistoryEntry(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getDouble(3),
                        rs.getDouble(4),
                        rs.getDouble(5)
                );
                historyEntries.add(historyEntry);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * define data for a single row of the history table
     */
    public class HistoryEntry {
        private int id;
        private int serverId;
        private double currentTime;
        private double timeOffset;
        private double roundTripDelay;

        public HistoryEntry(int id, int serverId, double currentTime, double timeOffset, double roundTripDelay) {
            this.id = id;
            this.serverId = serverId;
            this.currentTime = currentTime;
            this.timeOffset = timeOffset;
            this.roundTripDelay = roundTripDelay;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public double getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(double currentTime) {
            this.currentTime = currentTime;
        }

        public double getTimeOffset() {
            return timeOffset;
        }

        public void setTimeOffset(double timeOffset) {
            this.timeOffset = timeOffset;
        }

        public double getRoundTripDelay() {
            return roundTripDelay;
        }

        public void setRoundTripDelay(double roundTripDelay) {
            this.roundTripDelay = roundTripDelay;
        }
    }
}