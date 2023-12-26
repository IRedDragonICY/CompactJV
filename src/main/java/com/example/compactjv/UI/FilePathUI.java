package com.example.compactjv.UI;

import com.example.compactjv.ExecutorServiceManager;
import com.example.compactjv.File;
import com.example.compactjv.Size;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;


public class FilePathUI {

    private final TextField filePathField;
    private final File compact;
    private final Label currentSizeLabel;
    private final Label sizeOnDiskLabel;
    private final Button compressButton;
    private final Button decompressButton;
    private final Label totalFolderOnFileLabel;
    private final ChoiceBox compressionAlgorithmChoiceBox;
    private final ExecutorServiceManager executorServiceManager;

    public FilePathUI(File compact, TextField filePathField, Label currentSizeLabel, Label sizeOnDiskLabel,Label totalFolderOnFileLabel,
                      Button compressButton, Button decompressButton, ChoiceBox compressionAlgorithmChoiceBox,
                      ExecutorServiceManager executorServiceManager) {
        this.filePathField = filePathField;
        this.compact = compact;
        this.currentSizeLabel = currentSizeLabel;
        this.sizeOnDiskLabel = sizeOnDiskLabel;
        this.totalFolderOnFileLabel = totalFolderOnFileLabel;
        this.compressButton = compressButton;
        this.decompressButton = decompressButton;
        this.compressionAlgorithmChoiceBox = compressionAlgorithmChoiceBox;
        this.executorServiceManager = executorServiceManager;
        setupFilePathField();
    }

    private void setupFilePathField() {
        filePathField.textProperty().addListener(this::fieldTextHasChanged);
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
    }

    private void handleMouseClickedOnField(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        java.io.File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            updateFilePath(selectedDirectory);
        }
    }

    private void handleDragOverField(DragEvent event) {
        if (event.getGestureSource() != filePathField && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void handleDragDroppedOnField(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            java.io.File file = db.getFiles().get(0);
            if(file.isDirectory()){
                updateFilePath(file);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void fieldTextHasChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String filePath = compact.getFilePath();
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
        currentSizeLabel.setText("Loading...");
        sizeOnDiskLabel.setText("Loading...");
        if (compact.isValidDirectory(filePath)) {
            prepareFolder(filePath);
            compressionAlgorithmChoiceBox.setDisable(false);
        }
    }

    private void updateFilePath(java.io.File file) {
        compact.setFilePath(file.getAbsolutePath());
        filePathField.setText(file.getName());
    }

    private void prepareFolder(String filePath) {
        executorServiceManager.executeTask(() -> {
            boolean isCompressed = compact.isCompressed(filePath);
            Size size = compact.calculateFolderSize(filePath);
            Platform.runLater(() -> updateUI(isCompressed, size));
        });
    }

    private void updateUI(boolean isCompressed, Size size) {
        compressButton.setDisable(false);
        decompressButton.setDisable(!isCompressed);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
        currentSizeLabel.setText(size.getSizeFormatted());
        sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
        totalFolderOnFileLabel.setText(Long.toString(compact.getTotalFilesInFolder()));

    }
}
