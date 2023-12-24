package com.example.compactjv.UI;

import com.example.compactjv.ExecutorServiceManager;
import com.example.compactjv.File;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.util.concurrent.*;
public class LoadingBarUI {

    private final File compact;
    private final Label percentageLabel;
    private final ProgressBar progressBar;

    public LoadingBarUI(File compact, Label percentageLabel, ProgressBar progressBar, Future<?> future, String filePath, boolean isCompression, ExecutorServiceManager executorServiceManager) {
        this.compact = compact;
        this.percentageLabel = percentageLabel;
        this.progressBar = progressBar;
        monitorTaskUntilCompletion(future, filePath, isCompression, executorServiceManager);
    }

    public void monitorTaskUntilCompletion(Future<?> future, String filePath, boolean isCompression, ExecutorServiceManager executorManager) {
        executorManager.submitTask(() -> {
            while (!future.isDone()) {
                compact.getTotalFolderCompressed(filePath);
                updatePercentageLabel(isCompression);
            }

            Platform.runLater(() -> {
                percentageLabel.setText("Finished!");
                ScheduledExecutorService scheduler = executorManager.getScheduler();
                scheduler.schedule(() -> Platform.runLater(() -> {
                    percentageLabel.setText("");
                    executorManager.shutdownScheduler();
                }), 5, TimeUnit.SECONDS);
            });
        });
    }

    private void updatePercentageLabel(boolean isCompression) {
        long totalCompressed = compact.getTotalCompressed();
        long totalDecompressed = compact.getTotalDecompressed();
        long total = totalCompressed + totalDecompressed;
        double percentage = calculatePercentage(totalCompressed, totalDecompressed, total, isCompression);
        Platform.runLater(() -> {
            percentageLabel.setText(String.format("%.2f", percentage) + "%");
            progressBar.setProgress(percentage / 100);
        });
    }

    private double calculatePercentage(long totalCompressed, long totalDecompressed, long total, boolean isCompression) {
        return (double) (isCompression ? totalCompressed : totalDecompressed) / total * 100;
    }
}

