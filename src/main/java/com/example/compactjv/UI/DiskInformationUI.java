package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.Disk;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class DiskInformationUI extends Controller {

    public DiskInformationUI() {
        super();
        diskContainer = Controller.getInstance().getDiskContainer();
        setupDiskSizeList();
        startUpdateService();
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

            diskName.setStyle("-fx-font-family: 'Segoe UI Black'; -fx-text-fill: white;");
            diskFreeSpace.setStyle("-fx-font-family: 'Segoe UI Semilight'; -fx-text-fill: white;");
            diskTotalSize.setStyle("-fx-font-family: 'Segoe UI Semilight'; -fx-text-fill: white;");

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

    private void startUpdateService() {
        ScheduledService<Void> updateService = new ScheduledService<>() {
            protected Task<Void> createTask() {
                return new Task<>() {
                    protected Void call() {
                        Platform.runLater(() -> {
                            diskContainer.getChildren().clear();
                            setupDiskSizeList();
                        });
                        return null;
                    }
                };
            }
        };

        updateService.setPeriod(Duration.seconds(5));
        updateService.start();
    }
}
