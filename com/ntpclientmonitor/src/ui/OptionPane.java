package com.ntpclientmonitor.src.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

class OptionPane extends TabPane {
    OptionPane() {
        super();

        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Tab serviceTab = new Tab();
        serviceTab.setText("Service");
        getTabs().add(serviceTab);

        Tab peersTab = new Tab();
        peersTab.setText("Peers");
        PeersListPane peersListPane = new PeersListPane();
        peersTab.setContent(peersListPane);
        getTabs().add(peersTab);

        Tab syncTab = new Tab();
        syncTab.setText("Sync");
        GridPane syncPane = new GridPane();
        DigitalSystemTimePanel digitalSystemTimePanel = new DigitalSystemTimePanel();
        TitledPane systemTimeTitledPane = new TitledPane("System time", digitalSystemTimePanel);
        systemTimeTitledPane.setCollapsible(false);
        syncPane.add(systemTimeTitledPane, 0, 0);
        syncTab.setContent(syncPane);
        getTabs().add(syncTab);
    }
}