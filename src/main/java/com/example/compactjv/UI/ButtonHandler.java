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
        setButtonState(true);
    }

    private void setupDecompressionButton() {
        decompressButton.setOnAction(event -> handleCompressionOrDecompression(false));
        setButtonState(true);
    }

    private void handleCompressionOrDecompression(boolean isCompression) {
        String filePath = compact.getFilePath();
        updateSizeOnDiskLabel("Loading...");
        setButtonState(true);
        Runnable task = createCompressionOrDecompressionTask(isCompression, filePath);
        Future<?> future = executorServiceManager.submitTask(task);
        new LoadingBarUI(future, filePath, isCompression);
    }

    private Runnable createCompressionOrDecompressionTask(boolean isCompression, String filePath) {
        return () -> {
            progressBar.setVisible(true);
            setUIState(true);
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
        setButtonState(false);
    }

    private void updateSizeOnDiskLabel(String text) {
        sizeOnDiskLabel.setText(text);
    }

    private void setButtonState(boolean state) {
        compressButton.setDisable(state);
        decompressButton.setDisable(state);
    }

    private void setUIState(boolean state) {
        filePathField.setDisable(state);
        compressionAlgorithmChoiceBox.setDisable(state);
    }

    private void updateUIAfterFolderPreparation(boolean isCompressed, Size size) {
        Platform.runLater(() -> {
            updateButtonVisibilityAndText(isCompressed);
            currentSizeLabel.setText(size.getSizeFormatted());
            setUIState(false);
            sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
        });
    }

    private void updateButtonVisibilityAndText(boolean isCompressed) {
        decompressButton.setDisable(!isCompressed);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
    }
}
