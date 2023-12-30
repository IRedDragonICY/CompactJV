package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.Size;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class FilePathUI extends Controller {


    public FilePathUI() {
        super();
        setupFilePathField();
    }

    private void setupFilePathField() {
        filePathField.textProperty().addListener((observable, oldValue, newValue) -> fieldTextHasChanged());
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
    }

    private void handleMouseClickedOnField(MouseEvent event) {
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        java.io.File selectedDirectory = new DirectoryChooser().showDialog(primaryStage);
        if (selectedDirectory != null) updateFilePath(selectedDirectory);
    }

    private void handleDragOverField(DragEvent event) {
        if (event.getGestureSource() != filePathField && event.getDragboard().hasFiles())
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        event.consume();
    }

    private void handleDragDroppedOnField(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            java.io.File file = event.getDragboard().getFiles().get(0);
            if(file.isDirectory()){
                updateFilePath(file);
                event.setDropCompleted(true);
            }
        }
        event.consume();
    }

    private void fieldTextHasChanged() {
        String filePath = compact.getFilePath();
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
        currentSizeLabel.setText("Loading...");
        sizeOnDiskLabel.setText("Loading...");
        if (compact.isValidDirectory(filePath)) prepareFolder(filePath);
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
        compressionAlgorithmChoiceBox.setDisable(false);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
        currentSizeLabel.setText(size.getSizeFormatted());
        sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
        totalFolderOnFile.setText(Long.toString(compact.getTotalFilesInFolder()));
    }
}

