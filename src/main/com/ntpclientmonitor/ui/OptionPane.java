package com.ntpclientmonitor.ui;

import javafx.geometry.Side;
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
        ServicePane servicePane = new ServicePane();
        serviceTab.setContent(servicePane);
        getTabs().add(serviceTab);

        Tab statusTab = new Tab();
        statusTab.setText("Status");
        StatusPane statusPane = new StatusPane();
        statusTab.setContent(statusPane);
        getTabs().add(statusTab);

        setSide(Side.BOTTOM);
    }
}