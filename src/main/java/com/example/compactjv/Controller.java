package com.example.compactjv;

import com.example.compactjv.UI.ButtonUI;
import com.example.compactjv.UI.NavbarUI;
import com.example.compactjv.UI.FilePathUI;
import com.example.compactjv.UI.WindowControlsUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import java.util.concurrent.*;


public class Controller {
    @FXML
    private Button closeButton, minimizeButton, compressButton, decompressButton, informationButton;
    @FXML
    private TextField filePathField;
    @FXML
    private Label currentSizeLabel, sizeOnDiskLabel, percentageLabel, cpuUsageLabel, memoryUsageLabel, totalFolderOnFile;
    @FXML
    private ChoiceBox<String> compressionAlgorithmChoiceBox;
    @FXML
    private ProgressBar progressBar;
    private File compact;
    private ExecutorServiceManager executorServiceManager;


    @FXML
    private Label infoText;
    @FXML
    private Label homeText;
    @FXML
    private AnchorPane navbar;
    @FXML
    private Button hamburgerButton;

    public void initialize() {
        setupButtons();
        setupCompressionAlgorithmChoiceBox();
        setupCPUUsageLabel();
        setupMemoryUsageLabel();
        compact = new File();
        executorServiceManager = new ExecutorServiceManager();
        new NavbarUI(infoText, homeText, navbar, hamburgerButton);
        new WindowControlsUI(minimizeButton, closeButton);
        new ButtonUI(compressButton, decompressButton, informationButton);
        new FilePathUI(compact, filePathField, currentSizeLabel, sizeOnDiskLabel, totalFolderOnFile, compressButton, decompressButton, compressionAlgorithmChoiceBox, executorServiceManager);
    }

    private void setupButtons() {
        setupCompressionButton();
        setupDecompressionButton();
        compressionAlgorithmChoiceBox.setDisable(true);
        percentageLabel.setText("");
        progressBar.setVisible(false);
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

    private void setupMemoryUsageLabel() {
        Runnable task = () -> {
            while (true) {
                String memoryUsage = File.getMemoryUsage();
                Platform.runLater(() -> memoryUsageLabel.setText(memoryUsage + "%"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(task).start();
    }
    private void setupCompressionButton() {
        compressButton.setOnAction(event -> handleCompressionOrDecompression(true));
        compressButton.setDisable(true);
    }

    private void setupDecompressionButton() {
        decompressButton.setOnAction(event -> handleCompressionOrDecompression(false));
        decompressButton.setDisable(true);
    }

    private void setupCompressionAlgorithmChoiceBox() {
        addToolTipsToChoiceBoxItems();
    }
    private void addToolTipsToChoiceBoxItems() {
        Tooltip tooltipXPRESS4K = new Tooltip("Fastest, but weakest");
        Tooltip tooltipXPRESS8K = new Tooltip("Reasonable balance between speed and compression");
        Tooltip tooltipXPRESS16K = new Tooltip("Slower, but stronger");
        Tooltip tooltipLZX = new Tooltip("Slowest, but strongest - note it has a higher overhead, so use it on programs/games only if your CPU is reasonably strong or the program/game is older.");

        compressionAlgorithmChoiceBox.setTooltip(tooltipXPRESS4K);
        compressionAlgorithmChoiceBox.setOnShowing(event -> {
            switch (compressionAlgorithmChoiceBox.getValue()) {
                case "XPRESS4K":
                    compressionAlgorithmChoiceBox.setTooltip(tooltipXPRESS4K);
                    break;
                case "XPRESS8K":
                    compressionAlgorithmChoiceBox.setTooltip(tooltipXPRESS8K);
                    break;
                case "XPRESS16K":
                    compressionAlgorithmChoiceBox.setTooltip(tooltipXPRESS16K);
                    break;
                case "LZX":
                    compressionAlgorithmChoiceBox.setTooltip(tooltipLZX);
                    break;
            }
        });
    }
    private void handleCompressionOrDecompression(boolean isCompression) {
        String filePath = compact.getFilePath();
        updateSizeOnDiskLabel("Loading...");
        disableButtons();
        Runnable task = createCompressionOrDecompressionTask(isCompression, filePath);
        Future<?> future = executorServiceManager.submitTask(task);
        monitorTaskUntilCompletion(future, filePath, isCompression);
    }

    private Runnable createCompressionOrDecompressionTask(boolean isCompression, String filePath) {
        return () -> {
            progressBar.setVisible(true);
            if (isCompression) {
                String algorithm = compressionAlgorithmChoiceBox.getValue();
                compact.compress(filePath, algorithm);
            } else {
                compact.decompress(filePath);
            }
            updateAfterTaskCompletion();
        };
    }

    private void updateAfterTaskCompletion() {
        Size size = compact.calculateFolderSize(compact.getFilePath());
        boolean isCompressed = compact.isCompressed(compact.getFilePath());
        updateUIAfterFolderPreparation(isCompressed, size);
        enableButtons();
    }

    private void updateSizeOnDiskLabel(String text) {
        sizeOnDiskLabel.setText(text);
    }

    private void disableButtons() {
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
    }


    private void monitorTaskUntilCompletion(Future<?> future, String filePath, boolean isCompression) {
        ExecutorServiceManager executorManager = new ExecutorServiceManager();

        executorManager.submitTask(() -> {
            while (!future.isDone()) {
                compact.getTotalFolderCompressed(filePath);
                updatePercentageLabel(isCompression);
            }

            Platform.runLater(() -> {
                percentageLabel.setText("Finished!");

                ScheduledExecutorService scheduler = executorManager.getScheduledExecutor();
                scheduler.schedule(() -> {
                    Platform.runLater(() -> {
                        percentageLabel.setText("");
                        // Jangan lupa untuk shutdown scheduler setelah selesai
                        executorManager.shutdownScheduler();
                    });
                }, 5, TimeUnit.SECONDS);
            });
        });
    }






    private void updatePercentageLabel(boolean isCompression){
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


    private void enableButtons() {
        compressButton.setDisable(false);
        decompressButton.setDisable(false);
    }



    private void updateUIAfterFolderPreparation(boolean isCompressed, Size size) {
        Platform.runLater(() -> {
            updateButtonVisibilityAndText(isCompressed);
            currentSizeLabel.setText(size.getSizeFormatted());
            sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
            compressionAlgorithmChoiceBox.setDisable(false);
        });
    }

    private void updateButtonVisibilityAndText(boolean isCompressed) {
        compressButton.setDisable(true);
        decompressButton.setDisable(!isCompressed);
        compressButton.setDisable(false);
        progressBar.setVisible(false);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
    }
}
