package com.ntpclient.src.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

class OptionPane extends TabPane {
    OptionPane() {
        super();

        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Tab serviceTab = new Tab();
        serviceTab.setText("Service");
        getTabs().add(serviceTab);

        Tab syncTab = new Tab();
        syncTab.setText("Sync");
        GridPane syncPane = new GridPane();
        syncPane.setAlignment(Pos.CENTER);

        DigitalSystemTimePanel digitalSystemTimePanel = new DigitalSystemTimePanel();
        TitledPane titledPane = new TitledPane("System time", digitalSystemTimePanel);
        titledPane.setCollapsible(false);
        syncPane.add(titledPane, 0, 0);
        syncTab.setContent(syncPane);
        getTabs().add(syncTab);
    }
}