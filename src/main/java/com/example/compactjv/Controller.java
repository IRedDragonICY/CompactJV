package com.example.compactjv;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.input.Dragboard;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.collections.FXCollections;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.beans.value.ObservableValue;


public class Controller {

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private TextField filePathField;

    @FXML
    private Label currentSizeLabel;

    @FXML
    private Label sizeOnDiskLabel;

    @FXML
    private ChoiceBox<String> compressionAlgorithmChoiceBox;

    @FXML
    private Button compressButton;

    @FXML
    private Button decompressButton;

    public long calculateFolderSize(java.io.File folder) {
        long length = 0;
        java.io.File[] files = folder.listFiles();

        if (files != null) {
            for (java.io.File file : files) {
                length += (file.isFile()) ? file.length() : calculateFolderSize(file);
            }
        }
        return length;
    }

    public void initialize() {
        setupCloseButton();
        setupMinimizeButton();
        setupCompressionAlgorithmChoiceBox();
        setupFilePathField();
        setupCompressButton();
        setupDecompressButton();
    }

    private void setupCloseButton() {
        closeButton.setText("X");
        closeButton.setOnAction(event -> Platform.exit());
    }

    private void setupMinimizeButton() {
        minimizeButton.setText("-");
        minimizeButton.setOnAction(event -> ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true));
    }

    private void setupCompressionAlgorithmChoiceBox() {
        compressionAlgorithmChoiceBox.setItems(FXCollections.observableArrayList("XPRESS4K", "XPRESS8K", "XPRESS16K", "LZX"));
        compressionAlgorithmChoiceBox.setValue("XPRESS4K");
    }

    private void setupFilePathField() {
        filePathField.setEditable(false);
        filePathField.setText("Drag and Drop here or Click here to select a directory");
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
        filePathField.textProperty().addListener(this::handleFieldTextChanged);
    }

    private void handleMouseClickedOnField(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        java.io.File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            filePathField.setText(selectedDirectory.getAbsolutePath());
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
            filePathField.setText(db.getFiles().get(0).getAbsolutePath());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void handleFieldTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null && !newValue.isEmpty()) {
            java.io.File file = new java.io.File(newValue);
            if (file.exists() && file.isDirectory()) {
                Size size = new File().calculateFolderSize(newValue);
                long sizeInBytes = size.getSize();
                double sizeInKB = sizeInBytes / 1024.0;
                double sizeInMB = sizeInKB / 1024.0;
                double sizeInGB = sizeInMB / 1024.0;
                long sizeOnDiskInBytes = size.getSizeOnDisk();
                double sizeOnDiskInKB = sizeOnDiskInBytes / 1024.0;
                double sizeOnDiskInMB = sizeOnDiskInKB / 1024.0;
                double sizeOnDiskInGB = sizeOnDiskInMB / 1024.0;
                sizeOnDiskLabel.setText((sizeOnDiskInKB < 1) ? sizeOnDiskInBytes + " bytes" :
                        (sizeOnDiskInMB < 1) ? String.format("%.2f KB", sizeOnDiskInKB) :
                                (sizeOnDiskInGB < 1) ? String.format("%.2f MB", sizeOnDiskInMB) :
                                        String.format("%.2f GB", sizeOnDiskInGB));
                currentSizeLabel.setText((sizeInKB < 1) ? sizeInBytes + " bytes" :
                        (sizeInMB < 1) ? String.format("%.2f KB", sizeInKB) :
                                (sizeInGB < 1) ? String.format("%.2f MB", sizeInMB) :
                                        String.format("%.2f GB", sizeInGB));
            } else {
                currentSizeLabel.setText(" - ");
            }
        } else {
            currentSizeLabel.setText(" - ");
        }
        File compact = new File();
        if (compact.isCompressed(newValue)) {
            decompressButton.setVisible(true);
        } else {
            decompressButton.setVisible(false);
        }
    }

    private void setupCompressButton() {
        compressButton.setOnAction(event -> {
            String filePath = filePathField.getText();
            String algorithm = compressionAlgorithmChoiceBox.getValue();
            File compact = new File();
            compact.compress(filePath, algorithm);
        });
    }

    private void setupDecompressButton() {
        decompressButton.setVisible(false);
        decompressButton.setOnAction(event -> {
            String filePath = filePathField.getText();
            File compact = new File();
            if (compact.isCompressed(filePath)) {
                compact.decompress(filePath);
            }
        });
    }
}
