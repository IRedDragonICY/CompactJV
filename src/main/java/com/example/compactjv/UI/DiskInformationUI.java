package com.example.compactjv.UI;

import com.example.compactjv.Disk;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DiskInformationUI {
    private final VBox diskContainer;
    public DiskInformationUI(VBox diskContainer) {
        this.diskContainer = diskContainer;
        setupDiskSizeList();
    }
    private void setupDiskSizeList() {
        Disk[] disks = Disk.getDisks();
        diskContainer.setPadding(new Insets(65, 0, 0, 10));
        for (Disk disk : disks) {
            VBox diskBox = new VBox();
            Label diskName = new Label(Character.toString(disk.getLabel()));
            Label diskFreeSpace = new Label("Free Space: " + disk.getFreeSpace().getSizeFormatted());
            Label diskTotalSize = new Label("Total Size: " + disk.getTotalSpace().getSizeFormatted());
            double freeSpaceRatio = (double)disk.getFreeSpace().getSize() / disk.getTotalSpace().getSize();
            ProgressBar diskSpaceBar = new ProgressBar();
            diskSpaceBar.setPrefWidth(160);
            diskSpaceBar.setProgress(1-freeSpaceRatio);

            diskName.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: white;");
            diskFreeSpace.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: white;");
            diskTotalSize.setStyle("-fx-font-family: 'Segoe UI'; -fx-text-fill: white;");

            if(freeSpaceRatio < 0.2) {
                diskSpaceBar.setStyle("-fx-accent: red;");
            } else {
                diskSpaceBar.setStyle("-fx-accent: green;");
            }

            HBox labelAndBar = new HBox();
            labelAndBar.setSpacing(10);
            labelAndBar.getChildren().addAll(diskName, diskSpaceBar);
            diskBox.getChildren().addAll(labelAndBar, diskFreeSpace, diskTotalSize);
            diskContainer.getChildren().add(diskBox);
        }
    }
}
