package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.File;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class MemoryInformationUI extends Controller {


    public MemoryInformationUI() {
        super();
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
