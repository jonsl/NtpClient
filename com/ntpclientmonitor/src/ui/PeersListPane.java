package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.SystemUtils;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

class PeersListPane extends BorderPane {
    private TableView<PeerRow> table = new TableView<>();

    PeersListPane() {
        super();

        TableColumn<PeerRow, String> sCol = new TableColumn<>("s");
        sCol.setSortable(false);
        sCol.setPrefWidth(20);
        sCol.setMinWidth(20);
        sCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("selection"));
        TableColumn<PeerRow, String> remoteCol = new TableColumn<>("remote");
        remoteCol.setSortable(false);
        remoteCol.setPrefWidth(100);
        remoteCol.setMinWidth(100);
        remoteCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("remote"));
        TableColumn<PeerRow, String> refidCol = new TableColumn<>("refid");
        refidCol.setSortable(false);
        refidCol.setPrefWidth(100);
        refidCol.setMinWidth(100);
        refidCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("refid"));
        TableColumn<PeerRow, Integer> stratumCol = new TableColumn<>("stratum");
        stratumCol.setSortable(false);
        stratumCol.setPrefWidth(60);
        stratumCol.setMinWidth(20);
        stratumCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Integer>("st"));
        TableColumn<PeerRow, String> typeCol = new TableColumn<>("type");
        typeCol.setSortable(false);
        typeCol.setPrefWidth(60);
        typeCol.setMinWidth(50);
        typeCol.setCellValueFactory(new PropertyValueFactory<PeerRow, String>("t"));
        TableColumn<PeerRow, Integer> whenCol = new TableColumn<>("when");
        whenCol.setSortable(false);
        whenCol.setPrefWidth(60);
        whenCol.setMinWidth(50);
        whenCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Integer>("when"));
        TableColumn<PeerRow, Integer> pollCol = new TableColumn<>("poll");
        pollCol.setSortable(false);
        pollCol.setPrefWidth(60);
        pollCol.setMinWidth(50);
        pollCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Integer>("poll"));
        TableColumn<PeerRow, Integer> reachCol = new TableColumn<>("reach");
        reachCol.setSortable(false);
        reachCol.setPrefWidth(60);
        reachCol.setMinWidth(50);
        reachCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Integer>("reach"));
        TableColumn<PeerRow, Double> delayCol = new TableColumn<>("delay");
        delayCol.setSortable(false);
        delayCol.setPrefWidth(60);
        delayCol.setMinWidth(50);
        delayCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Double>("delay"));
        TableColumn<PeerRow, Double> offsetCol = new TableColumn<>("offset");
        offsetCol.setSortable(false);
        offsetCol.setPrefWidth(60);
        offsetCol.setMinWidth(50);
        offsetCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Double>("offset"));
        TableColumn<PeerRow, Double> jitterCol = new TableColumn<>("jitter");
        jitterCol.setSortable(false);
        jitterCol.setPrefWidth(60);
        jitterCol.setMinWidth(50);
        jitterCol.setCellValueFactory(new PropertyValueFactory<PeerRow, Double>("jitter"));

        table.getColumns().addAll(sCol, remoteCol, refidCol, stratumCol, typeCol,
                whenCol, pollCol, reachCol, delayCol, offsetCol, jitterCol);

        table.setEditable(false);
        table.setSelectionModel(null);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setTop(table);

        setBottom(new GridPane());

        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new UpdatePollTask(), 0, 10000);
    }

    public static class PeerRow {
        private final SimpleStringProperty selection;
        private final SimpleStringProperty remote;
        private final SimpleStringProperty refid;
        private final SimpleIntegerProperty st;
        private final SimpleStringProperty t;
        private final SimpleIntegerProperty when;
        private final SimpleIntegerProperty poll;
        private final SimpleIntegerProperty reach;
        private final SimpleDoubleProperty delay;
        private final SimpleDoubleProperty offset;
        private final SimpleDoubleProperty jitter;

        PeerRow(SystemUtils.Peer peer) {
            this.selection = new SimpleStringProperty(Character.toString(peer.getSelection()));
            this.remote = new SimpleStringProperty(peer.getRemote());
            this.refid = new SimpleStringProperty(peer.getRefid());
            this.st = new SimpleIntegerProperty(peer.getSt());
            switch (peer.getT()) {
                case 'l':
                    this.t = new SimpleStringProperty("local");
                    break;
                case 'u':
                    this.t = new SimpleStringProperty("unicast");
                    break;
                case 'm':
                    this.t = new SimpleStringProperty("multicast");
                    break;
                case 'b':
                    this.t = new SimpleStringProperty("broadcast");
                    break;
                default:
                    this.t = new SimpleStringProperty(Character.toString(peer.getT()));
                    break;
            }
            this.when = new SimpleIntegerProperty(peer.getWhen());
            this.poll = new SimpleIntegerProperty(peer.getPoll());
            this.reach = new SimpleIntegerProperty(peer.getReach());
            this.delay = new SimpleDoubleProperty(peer.getDelay());
            this.offset = new SimpleDoubleProperty(peer.getOffset());
            this.jitter = new SimpleDoubleProperty(peer.getJitter());
        }

        public String getSelection() {
            return selection.get();
        }

        public SimpleStringProperty selectionProperty() {
            return selection;
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

        public int getSt() {
            return st.get();
        }

        public SimpleIntegerProperty stProperty() {
            return st;
        }

        public String getT() {
            return t.get();
        }

        public SimpleStringProperty tProperty() {
            return t;
        }

        public int getWhen() {
            return when.get();
        }

        public SimpleIntegerProperty whenProperty() {
            return when;
        }

        public int getPoll() {
            return poll.get();
        }

        public SimpleIntegerProperty pollProperty() {
            return poll;
        }

        public int getReach() {
            return reach.get();
        }

        public SimpleIntegerProperty reachProperty() {
            return reach;
        }

        public double getDelay() {
            return delay.get();
        }

        public SimpleDoubleProperty delayProperty() {
            return delay;
        }

        public double getOffset() {
            return offset.get();
        }

        public SimpleDoubleProperty offsetProperty() {
            return offset;
        }

        public double getJitter() {
            return jitter.get();
        }

        public SimpleDoubleProperty jitterProperty() {
            return jitter;
        }
    }

    class UpdatePollTask extends TimerTask {
        @Override
        public void run() {
            SystemUtils systemUtils = new SystemUtils();
            List<SystemUtils.Peer> peers = systemUtils.getPeerList();
            Platform.runLater(() -> {
                ObservableList<PeerRow> data = FXCollections.observableArrayList();
                for (SystemUtils.Peer peer : peers) {
                    data.add(new PeerRow(peer));
                }
                table.setItems(data);
            });
        }
    }
}