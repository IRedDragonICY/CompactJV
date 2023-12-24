package com.example.compactjv.UI;

import com.example.compactjv.ExecutorServiceManager;
import com.example.compactjv.File;
import com.example.compactjv.Size;
import javafx.application.Platform;

import java.util.concurrent.Future;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
public class ButtonHandler {

    private final File compact;
    private final Label currentSizeLabel;
    private final Label sizeOnDiskLabel;
    private final Button compressButton;
    private final Button decompressButton;
    private final ChoiceBox compressionAlgorithmChoiceBox;
    private final ExecutorServiceManager executorServiceManager;
    private final Label percentageLabel;
    private final ProgressBar progressBar;

    public ButtonHandler(File compact, Label currentSizeLabel, Label sizeOnDiskLabel, Button compressButton, Button decompressButton, ChoiceBox compressionAlgorithmChoiceBox, ExecutorServiceManager executorServiceManager, Label percentageLabel, ProgressBar progressBar) {
        this.compact = compact;
        this.currentSizeLabel = currentSizeLabel;
        this.sizeOnDiskLabel = sizeOnDiskLabel;
        this.compressButton = compressButton;
        this.decompressButton = decompressButton;
        this.compressionAlgorithmChoiceBox = compressionAlgorithmChoiceBox;
        this.executorServiceManager = executorServiceManager;
        this.percentageLabel = percentageLabel;
        this.progressBar = progressBar;
        setupCompressionButton();
        setupDecompressionButton();
    }
    private void setupCompressionButton() {
        compressButton.setOnAction(event -> handleCompressionOrDecompression(true));
        compressButton.setDisable(true);
    }

    private void setupDecompressionButton() {
        decompressButton.setOnAction(event -> handleCompressionOrDecompression(false));
        decompressButton.setDisable(true);
    }

    private void handleCompressionOrDecompression(boolean isCompression) {
        String filePath = compact.getFilePath();
        updateSizeOnDiskLabel("Loading...");
        disableButtons();
        Runnable task = createCompressionOrDecompressionTask(isCompression, filePath);
        Future<?> future = executorServiceManager.submitTask(task);
        new LoadingBarUI(compact, percentageLabel, progressBar, future, filePath, isCompression, executorServiceManager);
    }

    private Runnable createCompressionOrDecompressionTask(boolean isCompression, String filePath) {
        return () -> {
            progressBar.setVisible(true);
            if (isCompression) {
                String algorithm = compressionAlgorithmChoiceBox.getValue().toString();
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
