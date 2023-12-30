package com.example.compactjv.UI;

import com.example.compactjv.ExecutorServiceManager;
import com.example.compactjv.File;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.concurrent.TimeUnit;

public class CPUInformationUI {
    Label cpuUsageLabel;
    ExecutorServiceManager executorServiceManager;

    public CPUInformationUI(Label cpuUsageLabel, ExecutorServiceManager executorServiceManager) {
        this.cpuUsageLabel = cpuUsageLabel;
        this.executorServiceManager = executorServiceManager;
        setupCPUUsageLabel();
    }

    private void setupCPUUsageLabel() {
        Runnable task = () -> {
            String cpuUsage = File.getCPUUsage();
            Platform.runLater(() -> cpuUsageLabel.setText(cpuUsage + "%"));
        };

        executorServiceManager.executePeriodicTask(task, 0, 1, TimeUnit.SECONDS);
    }
}
