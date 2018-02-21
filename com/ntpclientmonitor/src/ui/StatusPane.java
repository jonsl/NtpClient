package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.SystemUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class StatusPane extends BorderPane {
    private TableView<PeerRow> table = new TableView<>();

    StatusPane() {
        super();

        final String columnStyle = "-fx-alignment:center-right; -fx-padding: 2px; -fx-font-size:12px;";

        TableColumn<PeerRow, String> sCol = new TableColumn<>("select");
        sCol.setStyle("-fx-alignment:center");
        sCol.setSortable(false);
        sCol.setPrefWidth(30);
        sCol.setMinWidth(30);
        sCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("s"));
        TableColumn<PeerRow, String> remoteCol = new TableColumn<>("remote");
        remoteCol.setStyle(columnStyle);
        remoteCol.setSortable(false);
        remoteCol.setPrefWidth(100);
        remoteCol.setMinWidth(100);
        remoteCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("remote"));
        TableColumn<PeerRow, String> refidCol = new TableColumn<>("refid");
        refidCol.setStyle(columnStyle);
        refidCol.setSortable(false);
        refidCol.setPrefWidth(100);
        refidCol.setMinWidth(100);
        refidCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("refid"));
        TableColumn<PeerRow, String> stratumCol = new TableColumn<>("stratum");
        stratumCol.setStyle(columnStyle);
        stratumCol.setSortable(false);
        stratumCol.setPrefWidth(40);
        stratumCol.setMinWidth(40);
        stratumCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("st"));
        TableColumn<PeerRow, String> typeCol = new TableColumn<>("type");
        typeCol.setStyle(columnStyle);
        typeCol.setSortable(false);
        typeCol.setPrefWidth(60);
        typeCol.setMinWidth(50);
        typeCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("t"));
        TableColumn<PeerRow, String> whenCol = new TableColumn<>("when");
        whenCol.setStyle(columnStyle);
        whenCol.setSortable(false);
        whenCol.setPrefWidth(60);
        whenCol.setMinWidth(50);
        whenCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("when"));
        TableColumn<PeerRow, String> pollCol = new TableColumn<>("poll");
        pollCol.setStyle(columnStyle);
        pollCol.setSortable(false);
        pollCol.setPrefWidth(60);
        pollCol.setMinWidth(50);
        pollCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("poll"));
        TableColumn<PeerRow, String> reachCol = new TableColumn<>("reach");
        reachCol.setStyle(columnStyle);
        reachCol.setSortable(false);
        reachCol.setPrefWidth(60);
        reachCol.setMinWidth(50);
        reachCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("reach"));
        TableColumn<PeerRow, String> delayCol = new TableColumn<>("delay");
        delayCol.setStyle(columnStyle);
        delayCol.setSortable(false);
        delayCol.setPrefWidth(60);
        delayCol.setMinWidth(50);
        delayCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("delay"));
        TableColumn<PeerRow, String> offsetCol = new TableColumn<>("offset");
        offsetCol.setStyle(columnStyle);
        offsetCol.setSortable(false);
        offsetCol.setPrefWidth(60);
        offsetCol.setMinWidth(50);
        offsetCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("offset"));
        TableColumn<PeerRow, String> jitterCol = new TableColumn<>("jitter");
        jitterCol.setStyle(columnStyle);
        jitterCol.setSortable(false);
        jitterCol.setPrefWidth(60);
        jitterCol.setMinWidth(50);
        jitterCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("jitter"));

        table.getColumns().addAll(sCol, remoteCol, refidCol, stratumCol, typeCol,
                whenCol, pollCol, reachCol, delayCol, offsetCol, jitterCol);

        final String tableStyle = ".table-cell { -fx-font-weight:normal; -fx-font-size:12px; }";

        table.setEditable(false);
        table.setSelectionModel(null);
        table.setPrefHeight(150);
        table.setStyle(tableStyle);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setTop(table);

        setBottom(new GridPane());

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new UpdatePollTask(), 0, 10000);
    }

    public static class PeerRow {
        private final SimpleStringProperty s;
        private final SimpleStringProperty remote;
        private final SimpleStringProperty refid;
        private final SimpleStringProperty st;
        private final SimpleStringProperty t;
        private final SimpleStringProperty when;
        private final SimpleStringProperty poll;
        private final SimpleStringProperty reach;
        private final SimpleStringProperty delay;
        private final SimpleStringProperty offset;
        private final SimpleStringProperty jitter;

        PeerRow(SystemUtils.Peer peer) {
            this.s = new SimpleStringProperty(peer.getS());
            this.remote = new SimpleStringProperty(peer.getRemote());
            this.refid = new SimpleStringProperty(peer.getRefid());
            this.st = new SimpleStringProperty(peer.getSt());
            // Type (u: unicast or manycast client, b: broadcast or multicast client, l: local reference clock,
            // s: symmetric peer, A: manycast server, B: broadcast server, M: multicast server
            switch (peer.getT()) {
                case "u":
                    this.t = new SimpleStringProperty("unicast");
                    break;
                case "b":
                    this.t = new SimpleStringProperty("broadcast");
                    break;
                case "l":
                    this.t = new SimpleStringProperty("local");
                    break;
                case "s":
                    this.t = new SimpleStringProperty("symmetric");
                    break;
                case "A":
                    this.t = new SimpleStringProperty("manycast");
                    break;
                case "B":
                    this.t = new SimpleStringProperty("broadcast");
                    break;
                case "M":
                    this.t = new SimpleStringProperty("multicast");
                    break;
                case "p":
                    this.t = new SimpleStringProperty("pool");
                    break;
                case "-":
                    this.t = new SimpleStringProperty("netaddr");
                    break;
                default:
                    this.t = new SimpleStringProperty(peer.getT());
                    break;
            }
            this.when = new SimpleStringProperty(peer.getWhen());
            this.poll = new SimpleStringProperty(peer.getPoll());
            this.reach = new SimpleStringProperty(peer.getReach());
            this.delay = new SimpleStringProperty(peer.getDelay());
            this.offset = new SimpleStringProperty(peer.getOffset());
            this.jitter = new SimpleStringProperty(peer.getJitter());
        }

        public String getSelection() {
            return s.get();
        }

        public SimpleStringProperty sProperty() {
            return s;
        }

        public String getRemote() {
            return remote.get();
        }

        public SimpleStringProperty remoteProperty() {
            return remote;
        }

        public String getRefid() {
            return refid.get();
        }

        public SimpleStringProperty refidProperty() {
            return refid;
        }

        public String getSt() {
            return st.get();
        }

        public SimpleStringProperty stProperty() {
            return st;
        }

        public String getT() {
            return t.get();
        }

        public SimpleStringProperty tProperty() {
            return t;
        }

        public String getWhen() {
            return when.get();
        }

        public SimpleStringProperty whenProperty() {
            return when;
        }

        public String getPoll() {
            return poll.get();
        }

        public SimpleStringProperty pollProperty() {
            return poll;
        }

        public String getReach() {
            return reach.get();
        }

        public SimpleStringProperty reachProperty() {
            return reach;
        }

        public String getDelay() {
            return delay.get();
        }

        public SimpleStringProperty delayProperty() {
            return delay;
        }

        public String getOffset() {
            return offset.get();
        }

        public SimpleStringProperty offsetProperty() {
            return offset;
        }

        public String getJitter() {
            return jitter.get();
        }

        public SimpleStringProperty jitterProperty() {
            return jitter;
        }
    }

    class UpdatePollTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {
                ObservableList<PeerRow> data = FXCollections.observableArrayList();
                List<SystemUtils.Peer> peers = SystemUtils.getInstance().getPeerList();
                for (SystemUtils.Peer peer : peers) {
                    data.add(new PeerRow(peer));
                }
                table.setItems(data);
            });
        }
    }
}