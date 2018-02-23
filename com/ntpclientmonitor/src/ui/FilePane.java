package com.ntpclientmonitor.src.ui;

import com.ntpclientmonitor.src.datamodel.DataModel;
import com.ntpclientmonitor.src.datamodel.HistoryData;
import com.ntpclientmonitor.src.datamodel.Observer;
import com.sun.javafx.scene.control.skin.TreeViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.Date;

public class FilePane extends GridPane {
    private static Image folderCollapseImage = new Image("file:resources/folder.png");
    private static Image folderExpandImage = new Image("file:resources/folder-open.png");
    private static Image fileImage = new Image("file:resources/text-x-generic.png");
    private ArrayList<Observer> observers = new ArrayList<>();
    private TreeView<FileInfo> treeView;

    public FilePane() {
        super();

        setPadding(new Insets(10, 0, 0, 0));

        setHgap(10);
        setVgap(10);

        double[] xPercents = {50, 50};
        for (double xPercent : xPercents) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(xPercent);
            getColumnConstraints().add(col);
        }
        double[] yPercents = {5, 95};
        for (double yPercent : yPercents) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(yPercent);
            getRowConstraints().add(row);
        }

        //setup the file browser root
        String hostName = "computer";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.err.println("exception: " + e.getLocalizedMessage());
        }

        TreeItem<FileInfo> rootNode = new TreeItem<>(new FileInfo(hostName));
        // create the tree view with
        treeView = new TreeView<FileInfo>(rootNode) {
            @Override
            protected Skin createDefaultSkin() {
                return new TTreeViewSkin(this);
            }
        };
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            if (keyCode.equals(KeyCode.ENTER)) {
                //
                try {
                    ArrayList<HistoryData> historyData = new ArrayList<>();

                    ObservableList<TreeItem<FileInfo>> items = treeView.getSelectionModel().getSelectedItems();
                    for (TreeItem<FileInfo> item : items) {

                        BufferedReader reader = new BufferedReader(new FileReader(item.getValue().getFile()));
                        String line;

                        // repeat until all lines read
                        while ((line = reader.readLine()) != null) {
                            String[] tokens = line.trim().split("\\s+");
                            long modifiedJulianDay = Long.parseLong(tokens[0]);
                            double secondOffset = Double.parseDouble(tokens[1]);

                            LocalDate localDate = LocalDate.MIN.with(JulianFields.MODIFIED_JULIAN_DAY, modifiedJulianDay);
                            Date modifiedJulianDayDate = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
                            long time = modifiedJulianDayDate.getTime() + (long) (secondOffset * 1000.0);
                            Date date = new Date(time);

                            double timeOffset = Double.parseDouble(tokens[2]);
                            double frequencyOffsetPpm = Double.parseDouble(tokens[3]);
                            double rmsJitter = Double.parseDouble(tokens[4]);
                            double allanDeviation = Double.parseDouble(tokens[5]);
                            int clockDiscipline = Integer.parseInt(tokens[6]);

                            historyData.add(new HistoryData(date, timeOffset,
                                    frequencyOffsetPpm, rmsJitter, allanDeviation, clockDiscipline));
                        }
                        reader.close();
                    }
                    DataModel.getInstance().getHistoryDataGroup().setHistoryData(historyData);

                } catch (Exception exception) {
                    System.err.println("exception: " + exception.getLocalizedMessage());
                }
            }
        });

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            SimpleFileTreeItem treeNode = new SimpleFileTreeItem(new FileInfo(name.toFile()));
            rootNode.getChildren().add(treeNode);
        }
        // add
        add(new Label("File browser"), 0, 0);

        add(treeView, 0, 1, 2, 1);
    }

    /**
     * REF: https://stackoverflow.com/questions/29402412/how-to-get-javafx-treeview-to-behave-consistently-upon-node-expansion
     * stop scroll jump expanding a node when scrolled to the bottom of the tree
     */
    class TTreeViewSkin<T extends IndexedCell> extends TreeViewSkin<T> {
        TTreeViewSkin(TreeView treeView) {
            super(treeView);
        }

        @Override
        protected VirtualFlow<TreeCell<T>> createVirtualFlow() {
            return new TVirtualFlow<TreeCell<T>>();
        }

    }

    class TVirtualFlow<T extends IndexedCell> extends VirtualFlow<T> {
        @Override
        public double getPosition() {
            double position = super.getPosition();
            if (position == 1.0d) {
                return 0.99999999999;
            }
            return super.getPosition();
        }

        @Override
        public void setPosition(double newPosition) {
            if (newPosition == 1.0d) {
                newPosition = 0.99999999999;
            }
            super.setPosition(newPosition);
        }
    }

    public class FileInfo {
        private File file;

        FileInfo(String pathname) {
            super();
            this.file = new File(pathname);
        }

        FileInfo(File file) {
            super();
            this.file = file;
        }

        public File getFile() {
            return file;
        }

        @Override
        public String toString() {
            Path path = file.toPath();
            if (path.getFileName() == null) {
                return file.toString();
            }
            return path.getFileName().toString();
        }
    }

    public class SimpleFileTreeItem extends TreeItem<FileInfo> {
        private boolean isFirstTimeChildren = true;
        private boolean isFirstTimeLeaf = true;
        private boolean isLeaf;

        SimpleFileTreeItem(FileInfo file) {
            super(file);
        }

        @Override
        public ObservableList<TreeItem<FileInfo>> getChildren() {
            if (isFirstTimeChildren) {
                isFirstTimeChildren = false;
                super.getChildren().setAll(buildChildren(this));
            }
            return super.getChildren();
        }

        @Override
        public boolean isLeaf() {
            if (isFirstTimeLeaf) {
                isFirstTimeLeaf = false;
                isLeaf = getValue().getFile().isFile();
            }
            return isLeaf;
        }

        private ObservableList<TreeItem<FileInfo>> buildChildren(TreeItem<FileInfo> TreeItem) {
            FileInfo f = TreeItem.getValue();
            if (f.getFile() != null && f.getFile().isDirectory()) {
                File[] files = f.getFile().listFiles();
                if (files != null) {
                    ObservableList<TreeItem<FileInfo>> children = FXCollections.observableArrayList();
                    for (File childFile : files) {
                        children.add(new SimpleFileTreeItem(new FileInfo(childFile)));
                    }
                    return children;
                }
            }
            return FXCollections.emptyObservableList();
        }
    }
}