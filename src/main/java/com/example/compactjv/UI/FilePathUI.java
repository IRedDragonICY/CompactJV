package com.example.compactjv.UI;

import com.example.compactjv.File;
import com.example.compactjv.Size;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.Button;




public class FilePathUI {

    private final TextField filePathField;
    private final File compact;
    private final Label currentSizeLabel;
    private final Label sizeOnDiskLabel;
    private final Button compressButton;
    private final Button decompressButton;
    private final ChoiceBox compressionAlgorithmChoiceBox;
    public FilePathUI(File compact, TextField filePathField , Label currentSizeLabel, Label sizeOnDiskLabel, Button compressButton, Button decompressButton, ChoiceBox compressionAlgorithmChoiceBox) {
        this.filePathField = filePathField;
        this.compact = compact;
        this.currentSizeLabel = currentSizeLabel;
        this.sizeOnDiskLabel = sizeOnDiskLabel;
        this.compressButton = compressButton;
        this.decompressButton = decompressButton;
        this.compressionAlgorithmChoiceBox = compressionAlgorithmChoiceBox;
        setupFilePathField();
    }

    private void setupFilePathField() {
        filePathField.textProperty().addListener(this::FieldTextHasChanged);
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

    private void FieldTextHasChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String filePath = compact.getFilePath();
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
        currentSizeLabel.setText("Loading...");
        sizeOnDiskLabel.setText("Loading...");
        if (compact.isValidDirectory(filePath)) {
            prepareFolder(filePath);
        }
    }
    private void updateFilePath(java.io.File file) {
        compact.setFilePath(file.getAbsolutePath());
        filePathField.setText(file.getName());
    }

    private void prepareFolder(String filePath) {
        Runnable task = () -> {
            boolean isCompressed = compact.isCompressed(filePath);
            Size size = compact.calculateFolderSize(filePath);
            Platform.runLater(() -> {
                compressButton.setDisable(isCompressed);
                decompressButton.setDisable(!isCompressed);
                compressButton.setText(isCompressed ? "Compress Again" : "Compress");
                currentSizeLabel.setText(size.getSizeFormatted());
                sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
                compressionAlgorithmChoiceBox.setDisable(false);
            });
        };
        new Thread(task).start();
    }

}
