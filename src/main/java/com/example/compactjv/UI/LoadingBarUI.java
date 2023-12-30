package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.ExecutorServiceManager;
import javafx.application.Platform;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadingBarUI extends Controller {

    public LoadingBarUI(Future<?> future, String filePath, boolean isCompression) {
        super();
        monitorTaskUntilCompletion(future, filePath, isCompression, executorServiceManager);
    }

    public void monitorTaskUntilCompletion(Future<?> future, String filePath, boolean isCompression, ExecutorServiceManager executorManager) {
        executorManager.submitTask(() -> {
            while (!future.isDone()) {
                compact.getTotalFolderCompressed(filePath);
                updatePercentageLabel(isCompression);
            }

            if (future.isDone()) {
                Platform.runLater(() -> {
                    percentageLabel.setText("Done!");
                    progressBar.setVisible(false);
                    ScheduledExecutorService scheduler = executorManager.getScheduler();
                    scheduler.schedule(() -> Platform.runLater(() -> {
                        percentageLabel.setText("");
                    }), 5, TimeUnit.SECONDS);
                });
            }
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
            progressBar.setVisible(true);
        });
    }

    private double calculatePercentage(long totalCompressed, long totalDecompressed, long total, boolean isCompression) {
        return (double) (isCompression ? totalCompressed : totalDecompressed) / total * 100;
    }
}
