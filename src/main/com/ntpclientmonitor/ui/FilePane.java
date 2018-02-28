package com.ntpclientmonitor.ui;

import com.ntpclientmonitor.datamodel.CommandExecutor;
import com.ntpclientmonitor.datamodel.DataModel;
import com.ntpclientmonitor.datamodel.HistoryData;
import com.ntpclientmonitor.datamodel.ServiceParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
//import javafx.scene.control.skin.TableViewSkin;
//import javafx.scene.control.skin.TreeViewSkin;
//import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

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
import java.util.*;

public class FilePane extends VBox {
    private static Image folderCollapseImage = new Image("file:resources/folder.png");
    private static Image folderExpandImage = new Image("file:resources/folder-open.png");
    private static Image fileImage = new Image("file:resources/text-x-generic.png");
    private TreeView<FileInfo> treeView;
    private ObservableList<TreeItem<FileInfo>> selectedItems;
    private Timer timer;
    private final int PollDelay = 30000;
    private final int PollPeriod = 30000;

    FilePane() {
        super();
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
            protected Skin<?> createDefaultSkin() {
                return super.createDefaultSkin();
            }
        };

        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // create new list not reference to THE list
                selectedItems = FXCollections.observableArrayList();
                selectedItems.addAll(treeView.getSelectionModel().getSelectedItems());
                updateHistoryData(true);
            }
        });

        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
        for (Path name : rootDirectories) {
            FileTreeItem treeNode = new FileTreeItem(new FileInfo(name.toFile()));
            rootNode.getChildren().add(treeNode);
        }

        // add time panel
        // add file tree
        TitledPane filePane = new TitledPane("Loopstats location", treeView);
        filePane.setCollapsible(false);
        getChildren().add(filePane);

        CommandExecutor commandExecutor = new CommandExecutor("wmic service NTP get PathName /format:list");
        ServiceParser serviceParser = new ServiceParser();
        serviceParser.addObserver(() -> {
            Platform.runLater(() -> {
                try {
                    String pathName = serviceParser.getServicePathName();
                    String strings[] = pathName.trim().split("\\s+");
                    File file = new File(strings[0]);
                    File bin = new File(file.getParent());
                    int row = expandPath(rootNode, bin.getParent() + "\\etc");
                    treeView.getSelectionModel().clearAndSelect(row);
                    treeView.scrollTo(row);
                    System.out.println("selected index: " + String.valueOf(row));
                    // send enter key event to treeview to plot selected path
                    KeyEvent keyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER,
                            false, false, false, false);
                    treeView.fireEvent(keyEvent);
                } catch (Exception exception) {
                    System.err.println("exception: " + exception.getLocalizedMessage());
                }
            });
        });
        commandExecutor.exec(serviceParser, null);

        this.timer = new Timer(true);
        this.timer.scheduleAtFixedRate(new SelectedItemsTimerTask(), 0, PollPeriod);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        treeView.requestFocus();
    }

    private int expandPath(TreeItem<FileInfo> root, String path) {
        File file = new File(path);
        LinkedList<String> pathStack = new LinkedList<>();
        while (file != null) {
            String seg = file.getName();
            if (seg.length() == 0) {
                seg = file.toString();
            }
            pathStack.add(0, seg);
            file = file.getParentFile();
        }
        root.setExpanded(true);
        return expandPath(root, pathStack, 0);
    }

    private int expandPath(TreeItem<FileInfo> root, LinkedList<String> pathStack, int row) {
        if (pathStack.size() > 0) {
            ObservableList<TreeItem<FileInfo>> children = root.getChildren();
            assert children != null;

            String file = pathStack.pop();
            for (int index = 0; index < children.size(); ++index) {
                TreeItem<FileInfo> child = children.get(index);
                if (child.getValue().toString().equals(file)) {
                    System.out.println("found: " + child.getValue().toString());
                    child.setExpanded(true);
                    return expandPath(children.get(index), pathStack, row + index + 1);
                }
            }
        }
        return row;
    }

    private void updateHistoryData(boolean newSelection) {
        //
        try {
            if (selectedItems != null) {
                ArrayList<HistoryData> historyData = new ArrayList<>();
                for (TreeItem<FileInfo> item : selectedItems) {
                    if (item == null) {
                        continue;
                    }
                    if (item.getValue().getFile().isFile()) {
                        List<HistoryData> data = parseSingleFile(item.getValue().getFile());
                        if (data != null) {
                            historyData.addAll(data);
                        }
                    } else if (item.getValue().getFile().isDirectory()) {
                        // only go 1 deep
                        ObservableList<TreeItem<FileInfo>> children = item.getChildren();
                        for (TreeItem<FileInfo> child : children) {
                            if (child.getValue().getFile().isFile()) {
                                List<HistoryData> data = parseSingleFile(child.getValue().getFile());
                                if (data != null) {
                                    historyData.addAll(data);
                                }
                            }
                        }
                    }
                }
                DataModel.getInstance().getHistoryDataGroup().setHistoryData(historyData, newSelection);
            }
        } catch (Exception exception) {
            System.err.println("exception: " + exception.getLocalizedMessage());
        }
    }

    private List<HistoryData> parseSingleFile(File file) {
        try {
            ArrayList<HistoryData> historyData = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));
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
            return historyData;
        } catch (Exception exception) {
            System.err.println("exception: " + exception.getLocalizedMessage());
        }
        return null;
    }

    class SelectedItemsTimerTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {
                timer.cancel();

                updateHistoryData(false);

                timer = new Timer(true);
                timer.scheduleAtFixedRate(new SelectedItemsTimerTask(), PollDelay, PollPeriod);
            });
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

    public class FileTreeItem extends TreeItem<FileInfo> {
        private boolean isFirstTimeChildren = true;
        private boolean isFirstTimeLeaf = true;
        private boolean isLeaf;

        FileTreeItem(FileInfo file) {
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
                isLeaf = (getChildren().size() == 0);
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
                        if (childFile.isDirectory() || childFile.toPath().getFileName().toString().startsWith("loopstats.")) {
                            children.add(new FileTreeItem(new FileInfo(childFile)));
                        }
                    }
                    return children;
                }
            }
            return FXCollections.emptyObservableList();
        }
    }
}