package com.example.compactjv.UI;

import com.example.compactjv.File;
import com.example.compactjv.ExecutorServiceManager;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.concurrent.TimeUnit;

public class MemoryInformationUI {
    private final Label memoryUsageLabel;
    private final ExecutorServiceManager executorServiceManager;

    public MemoryInformationUI(Label memoryUsageLabel, ExecutorServiceManager executorServiceManager) {
        this.memoryUsageLabel = memoryUsageLabel;
        this.executorServiceManager = executorServiceManager;
        setupMemoryUsageLabel();
    }

    private void setupMemoryUsageLabel() {
        Runnable task = () -> {
            String memoryUsage = File.getMemoryUsage();
            Platform.runLater(() -> memoryUsageLabel.setText(memoryUsage + "%"));
        };

        executorServiceManager.executePeriodicTask(task, 0, 1, TimeUnit.SECONDS);
    }
}
