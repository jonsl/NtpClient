package com.ntpclient.src.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class OptionPane extends TabPane {

    public OptionPane() {
        super();

        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        Tab serviceTab = new Tab();
        serviceTab.setText("Service");
        getTabs().add(serviceTab);

        Tab syncTab = new Tab();
        syncTab.setText("Sync");
        GridPane syncPane = new GridPane();
        syncPane.setAlignment(Pos.CENTER);
        syncPane.add(new DigitalSystemTimePanel(), 0, 0);
        syncTab.setContent(syncPane);
        getTabs().add(syncTab);
    }
}