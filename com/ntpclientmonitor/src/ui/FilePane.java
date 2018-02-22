package com.ntpclientmonitor.src.ui;

import com.sun.javafx.scene.control.skin.TreeViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilePane extends GridPane {
    private static Image folderCollapseImage = new Image("file:resources/folder.png");

    private static Image folderExpandImage = new Image("file:resources/folder-open.png");

    private static Image fileImage = new Image("file:resources/text-x-generic.png");


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
        //create the tree view
        treeView = new TreeView<FileInfo>(rootNode) {
            @Override
            protected Skin createDefaultSkin() {
                return new TTreeViewSkin(this);
            }
        };
        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

//            if (Files.isDirectory(file.getFile().toPath())) {
//                this.setGraphic(new ImageView(folderCollapseImage));
//            } else {
//                this.setGraphic(new ImageView(fileImage));
//            }

//            addEventHandler(TreeItem.branchCollapsedEvent(), (EventHandler<TreeModificationEvent<FileInfo>>) event -> {
//                TreeItem<FileInfo> source = event.getSource();
//                if (source.getValue().getFile().isDirectory() && !source.isExpanded()) {
//                    source.setGraphic(new ImageView(folderCollapseImage));
//                }
//            });
//
//            addEventHandler(TreeItem.branchExpandedEvent(), (EventHandler<TreeModificationEvent<FileInfo>>) event -> {
//                TreeItem<FileInfo> source = event.getSource();
//                if (source.getValue().getFile().isDirectory() && source.isExpanded()) {
//                    source.setGraphic(new ImageView(folderExpandImage));
//                }
//            });
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