package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.Size;
import javafx.application.Platform;

import java.util.concurrent.Future;

public class ButtonHandler extends Controller {


    public ButtonHandler() {
        super();
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
        new LoadingBarUI(future, filePath, isCompression);
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
        decompressButton.setDisable(!isCompressed);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
    }
}
