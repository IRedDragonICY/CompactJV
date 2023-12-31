package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.File;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class MemoryInformationUI extends Controller implements InformationUI {

    public MemoryInformationUI() {
        super();
        setupUsageLabel();
    }

    @Override
    public void setupUsageLabel() {
        Runnable task = () -> {
            String memoryUsage = File.getMemoryUsage();
            Platform.runLater(() -> memoryUsageLabel.setText(memoryUsage + "%"));
        };

        executorServiceManager.executePeriodicTask(task, 0, 1, TimeUnit.SECONDS);
    }
}
