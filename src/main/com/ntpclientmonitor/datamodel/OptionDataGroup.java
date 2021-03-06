package com.ntpclientmonitor.datamodel;

import com.ntpclientmonitor.datamodel.DataGroup;
import com.ntpclientmonitor.datamodel.DataModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;

public class OptionDataGroup extends DataGroup {
    //
    private final String optionTable = "option";
    //
    private TreeMap<String, OptionEntry> optionMap;

    OptionDataGroup() {
        super();
    }

    public synchronized final TreeMap<String, OptionEntry> getOptionMap() {
        return optionMap;
    }

    @Override
    public synchronized void getData() {
        new Thread(() -> {
            getOptionData();
            notifyObservers();
        }).start();
    }

    private void createTable() {
        String sql = "CREATE TABLE \"option\" ( " +
                "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `name` TEXT NOT NULL, `value` TEXT NOT NULL " +
                ")";
        try (Statement stmt = DataModel.getInstance().getConnection().createStatement();) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getOptionData() {
        String sql = "SELECT `id`, `name`, `value` FROM " + optionTable + ";";
        try (Statement stmt = DataModel.getInstance().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            optionMap = new TreeMap<>();
            while (rs.next()) {
                OptionEntry optionEntry = new OptionEntry(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3)
                );
                optionMap.put(optionEntry.getName(), optionEntry);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public class OptionEntry {
        private int id;
        private String name;
        private String value;

        public OptionEntry(int id, String name, String value) {
            this.id = id;
            this.name = name;
            this.value = value;
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

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}