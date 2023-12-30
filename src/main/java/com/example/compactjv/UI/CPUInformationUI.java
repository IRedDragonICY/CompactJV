package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.File;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class CPUInformationUI extends Controller {


    public CPUInformationUI() {
        super();
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
