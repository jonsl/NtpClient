package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.CommandExecutor;
import com.ntpclientmonitor.src.datamodel.StatusParser;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import java.util.Timer;
import java.util.TimerTask;

class StatusPane extends GridPane {
    private TableView<PeerRow> tableView = new TableView<>();

    StatusPane() {
        super();

        setPadding(new Insets(10, 10, 10, 10));

        setHgap(10);
        setVgap(10);

        double[] xPercents = {10, 10, 10, 10, 10, 10, 10, 10, 10, 10};
        for (double xPercent : xPercents) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(xPercent);
            getColumnConstraints().add(col);
        }
        double[] yPercents = {20, 20, 20, 20, 20};
        for (double yPercent : yPercents) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(yPercent);
            getRowConstraints().add(row);
        }

        final String columnStyle = "-fx-alignment:center-right; -fx-padding: 2px; -fx-font-size:12px;";

        TableColumn<PeerRow, String> sCol = new TableColumn<>("select");
        sCol.setStyle("-fx-alignment:center");
        sCol.setSortable(false);
        sCol.setPrefWidth(30);
        sCol.setMinWidth(30);
        sCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("select"));
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
        stratumCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("stratum"));
        TableColumn<PeerRow, String> typeCol = new TableColumn<>("type");
        typeCol.setStyle(columnStyle);
        typeCol.setSortable(false);
        typeCol.setPrefWidth(60);
        typeCol.setMinWidth(50);
        typeCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("type"));
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

        tableView.getColumns().addAll(sCol, remoteCol, refidCol, stratumCol, typeCol,
                whenCol, pollCol, reachCol, delayCol, offsetCol, jitterCol);
        tableView.setEditable(false);
        tableView.setSelectionModel(null);
        tableView.setPrefHeight(150);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        add(tableView, 0, 0, 10, 5);

        PseudoClass starSelect = PseudoClass.getPseudoClass("select-star");
        PseudoClass oSelect = PseudoClass.getPseudoClass("select-o");
        PseudoClass plusSelect = PseudoClass.getPseudoClass("select-plus");
        PseudoClass minusSelect = PseudoClass.getPseudoClass("select-minus");
        PseudoClass eSelect = PseudoClass.getPseudoClass("select-e");
        PseudoClass xSelect = PseudoClass.getPseudoClass("select-x");
        PseudoClass hashSelect = PseudoClass.getPseudoClass("select-hash");
        PseudoClass spaceSelect = PseudoClass.getPseudoClass("select-space");

        tableView.setRowFactory(tableView -> {

            TableRow<PeerRow> row = new TableRow<>();
            ChangeListener<String> changeListener = (obs, oldSelect, newSelect) -> {
                row.pseudoClassStateChanged(starSelect, newSelect.equals("*"));
                row.pseudoClassStateChanged(oSelect, newSelect.equals("o"));
                row.pseudoClassStateChanged(plusSelect, newSelect.equals("+"));
                row.pseudoClassStateChanged(minusSelect, newSelect.equals("-"));
                row.pseudoClassStateChanged(eSelect, newSelect.equals("e"));
                row.pseudoClassStateChanged(xSelect, newSelect.equals("x"));
                row.pseudoClassStateChanged(hashSelect, newSelect.equals("#"));
                row.pseudoClassStateChanged(spaceSelect, newSelect.equals(" "));
            };

            row.itemProperty().addListener((obs, previousPeerRow, currentPeerRow) -> {
                if (previousPeerRow != null) {
                    previousPeerRow.selectProperty().removeListener(changeListener);
                }
                if (currentPeerRow != null) {
                    currentPeerRow.selectProperty().addListener(changeListener);
                    row.pseudoClassStateChanged(starSelect, currentPeerRow.selectProperty().getValue().equals("*"));
                    row.pseudoClassStateChanged(oSelect, currentPeerRow.selectProperty().getValue().equals("o"));
                    row.pseudoClassStateChanged(plusSelect, currentPeerRow.selectProperty().getValue().equals("+"));
                    row.pseudoClassStateChanged(minusSelect, currentPeerRow.selectProperty().getValue().equals("-"));
                    row.pseudoClassStateChanged(eSelect, currentPeerRow.selectProperty().getValue().equals("e"));
                    row.pseudoClassStateChanged(xSelect, currentPeerRow.selectProperty().getValue().equals("x"));
                    row.pseudoClassStateChanged(hashSelect, currentPeerRow.selectProperty().getValue().equals("#"));
                    row.pseudoClassStateChanged(spaceSelect, currentPeerRow.selectProperty().getValue().equals(" "));
                } else {
                    row.pseudoClassStateChanged(starSelect, false);
                    row.pseudoClassStateChanged(oSelect, false);
                    row.pseudoClassStateChanged(plusSelect, false);
                    row.pseudoClassStateChanged(minusSelect, false);
                    row.pseudoClassStateChanged(eSelect, false);
                    row.pseudoClassStateChanged(xSelect, false);
                    row.pseudoClassStateChanged(hashSelect, false);
                    row.pseudoClassStateChanged(spaceSelect, false);
                }
            });
            return row;
        });

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new StatusParserTimerTask(), 0, 10000);
    }

    public class PeerRow {
        private final SimpleStringProperty select;
        private final SimpleStringProperty remote;
        private final SimpleStringProperty refid;
        private final SimpleStringProperty stratum;
        private final SimpleStringProperty type;
        private final SimpleStringProperty when;
        private final SimpleStringProperty poll;
        private final SimpleStringProperty reach;
        private final SimpleStringProperty delay;
        private final SimpleStringProperty offset;
        private final SimpleStringProperty jitter;

        PeerRow(StatusParser.Peer peer) {
            this.select = new SimpleStringProperty(peer.getSelect());
            this.remote = new SimpleStringProperty(peer.getRemote());
            this.refid = new SimpleStringProperty(peer.getRefid());
            this.stratum = new SimpleStringProperty(peer.getStratum());
            // Type (u: unicast or manycast client, b: broadcast or multicast client, l: local reference clock,
            // s: symmetric peer, A: manycast server, B: broadcast server, M: multicast server
            switch (peer.getType()) {
                case "u":
                    this.type = new SimpleStringProperty("unicast");
                    break;
                case "b":
                    this.type = new SimpleStringProperty("broadcast");
                    break;
                case "l":
                    this.type = new SimpleStringProperty("local");
                    break;
                case "s":
                    this.type = new SimpleStringProperty("symmetric");
                    break;
                case "A":
                    this.type = new SimpleStringProperty("manycast");
                    break;
                case "B":
                    this.type = new SimpleStringProperty("broadcast");
                    break;
                case "M":
                    this.type = new SimpleStringProperty("multicast");
                    break;
                case "p":
                    this.type = new SimpleStringProperty("pool");
                    break;
                case "-":
                    this.type = new SimpleStringProperty("netaddr");
                    break;
                default:
                    this.type = new SimpleStringProperty(peer.getType());
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
            return select.get();
        }

        public SimpleStringProperty selectProperty() {
            return select;
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

        public String getStratum() {
            return stratum.get();
        }

        public SimpleStringProperty stratumProperty() {
            return stratum;
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
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

    class StatusParserTimerTask extends TimerTask {
        private StatusParser statusParser;

        private synchronized StatusParser getStatusParser() {
            return statusParser;
        }

        private synchronized void setStatusParser(StatusParser statusParser) {
            this.statusParser = statusParser;
        }

        @Override
        public void run() {
            CommandExecutor commandExecutor = new CommandExecutor("ntpq -pn");
            setStatusParser(new StatusParser());
            getStatusParser().addObserver(() -> {
                Platform.runLater(() -> {
                    ObservableList<PeerRow> data = FXCollections.observableArrayList();
                    for (StatusParser.Peer peer : getStatusParser().getPeers()) {
                        data.add(new PeerRow(peer));
                    }
                    tableView.setItems(data);
                });
            });
            commandExecutor.exec(getStatusParser(), null);
        }
    }
}