package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.File;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class CPUInformationUI extends Controller implements InformationUI {

    public CPUInformationUI() {
        super();
        setupUsageLabel();
    }

    @Override
    public void setupUsageLabel() {
        Runnable task = () -> {
            String cpuUsage = File.getCPUUsage();
            Platform.runLater(() -> cpuUsageLabel.setText(cpuUsage + "%"));
        };

        executorServiceManager.executePeriodicTask(task, 0, 1, TimeUnit.SECONDS);
    }
}
