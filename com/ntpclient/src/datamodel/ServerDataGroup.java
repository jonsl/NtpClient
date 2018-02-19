package com.ntpclient.src.datamodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

public class ServerDataGroup extends DataGroup {
    //
    private final String serverGroupTable = "server_group";
    private final String serverTable = "server";
    //
    private TreeMap<Integer, GroupEntry> serverGroupMap;
    private TreeMap<Integer, ServerEntry> serverMap;

    public ServerDataGroup() {
        super();
    }

    public synchronized final TreeMap<Integer, GroupEntry> getServerGroupMap() {
        return serverGroupMap;
    }

    public synchronized final TreeMap<Integer, ServerEntry> getServerMap() {
        return serverMap;
    }

    @Override
    public synchronized void getData() {
        new Thread(() -> {
            getServerGroupData();
            getServerData();
            notifyObservers();
        }).start();
    }

    public synchronized void addServer(int groupId, String address, String location, String protocol, String comment) {
        new Thread(() -> {
            String sql = "INSERT INTO " + serverTable +
                    " (group_id, address, location, protocol, comment) VALUES(?,?,?,?,?)";
            try (PreparedStatement pstmt = DataModel.getInstance().getConnection().prepareStatement(sql)) {
                pstmt.setInt(1, groupId);
                pstmt.setString(2, address);
                pstmt.setString(3, location);
                pstmt.setString(4, protocol);
                pstmt.setString(5, comment);
                pstmt.executeUpdate();
                getServerGroupData();
                getServerData();
                notifyObservers();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    private void getServerGroupData() {
        String sql = "SELECT `id`, `name` FROM " + serverGroupTable + ";";
        try (Statement stmt = DataModel.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            serverGroupMap = new TreeMap<>();
            while (rs.next()) {
                GroupEntry groupEntry = new GroupEntry(
                        rs.getInt(1),
                        rs.getString(2)
                );
                serverGroupMap.put(groupEntry.getId(), groupEntry);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getServerData() {
        String sql = "SELECT `id`, `group_id`, `address`, `location`, `protocol`, `comment` FROM " + serverTable + ";";
        try (Statement stmt = DataModel.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            serverMap = new TreeMap<>();
            while (rs.next()) {
                ServerEntry serverEntry = new ServerEntry(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                );
                serverMap.put(serverEntry.getId(), serverEntry);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public class ServerEntry {
        private int id;
        private int groupId;
        private String address;
        private String location;
        private String protocol;
        private String comment;

        public ServerEntry(int id, int groupId, String address, String location, String protocol, String comment) {
            this.id = id;
            this.groupId = groupId;
            this.address = address;
            this.location = location;
            this.protocol = protocol;
            this.comment = comment;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public class GroupEntry {
        private int id;
        private String name;

        public GroupEntry(int id, String name) {

            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}