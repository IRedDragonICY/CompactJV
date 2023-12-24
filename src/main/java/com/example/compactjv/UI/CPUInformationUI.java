package com.example.compactjv.UI;

import com.example.compactjv.File;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class CPUInformationUI {
    Label cpuUsageLabel;
    public CPUInformationUI(Label cpuUsageLabel) {
        this.cpuUsageLabel = cpuUsageLabel;
        setupCPUUsageLabel();
    }
    private void setupCPUUsageLabel() {
        Runnable task = () -> {
            while (true) {
                String cpuUsage = File.getCPUUsage();
                Platform.runLater(() -> cpuUsageLabel.setText(cpuUsage + "%"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task).start();
    }
}
