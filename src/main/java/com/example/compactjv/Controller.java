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
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Controller {
    @FXML
    private Button closeButton, minimizeButton, compressButton, decompressButton;
    @FXML
    private TextField filePathField;
    @FXML
    private Label currentSizeLabel, sizeOnDiskLabel;
    @FXML
    private ChoiceBox<String> compressionAlgorithmChoiceBox;
    @FXML
    private Label compressionAlgorithmLabel;

    private File compact;
    private ExecutorServiceManager executorServiceManager;
    private boolean isDialogOpen = false;

    public void initialize() {
        setupButtons();
        setupFilePathField();
        setupCompressionAlgorithmChoiceBox();
        compact = new File();
        executorServiceManager = new ExecutorServiceManager();
    }

    private void setupButtons() {
        setButtonProperties(closeButton, "X", event -> Platform.exit());
        setButtonProperties(minimizeButton, "-", event -> ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true));
        setButtonProperties(compressButton, false, event -> handleCompressionOrDecompression(true));
        setButtonProperties(decompressButton, false, event -> handleCompressionOrDecompression(false));
        setLabelAndChoiceBoxVisibility(false);
    }

    private void setButtonProperties(Button button, String text, EventHandler<ActionEvent> eventHandler) {
        button.setText(text);
        button.setOnAction(eventHandler);
    }

    private void setButtonProperties(Button button, boolean visibility, EventHandler<ActionEvent> eventHandler) {
        button.setVisible(visibility);
        button.setOnAction(eventHandler);
    }

    private void setLabelAndChoiceBoxVisibility(boolean visibility) {
        compressionAlgorithmLabel.setVisible(visibility);
        compressionAlgorithmChoiceBox.setVisible(visibility);
    }

    private void setupFilePathField() {
        filePathField.setEditable(false);
        filePathField.setText("Drag and Drop here");
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
        filePathField.textProperty().addListener(this::handleFieldTextChanged);
    }

    private void setupCompressionAlgorithmChoiceBox() {
        compressionAlgorithmChoiceBox.setItems(FXCollections.observableArrayList("XPRESS4K", "XPRESS8K", "XPRESS16K", "LZX"));
        compressionAlgorithmChoiceBox.setValue("XPRESS4K");
    }

    private void handleMouseClickedOnField(MouseEvent event) {
        if (!isDialogOpen) {
            isDialogOpen = true;
            DirectoryChooser directoryChooser = new DirectoryChooser();
            java.io.File selectedDirectory = directoryChooser.showDialog(null);
            if (selectedDirectory != null) {
                updateFilePath(selectedDirectory);
            }
            isDialogOpen = false;
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

    private void handleFieldTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        updateButtonsAndLabels();
    }

    private void handleCompressionOrDecompression(boolean compression) {
        String filePath = compact.getFilePath();
        sizeOnDiskLabel.setText("Loading...");
        Runnable task = () -> {
            if (compression) {
                String algorithm = compressionAlgorithmChoiceBox.getValue();
                compact.compress(filePath, algorithm);
            } else {
                compact.decompress(filePath);
            }
            Platform.runLater(() -> {
                updateButtonsAndLabels();
                compressButton.setDisable(false);
                decompressButton.setDisable(false);
            });
        };
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
        executorServiceManager.executeTask(task);
    }

    private void updateFilePath(java.io.File file) {
        compact.setFilePath(file.getAbsolutePath());
        filePathField.setText(file.getName());
    }

    private void updateButtonsAndLabels() {
        String filePath = compact.getFilePath();
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
        currentSizeLabel.setText("Loading...");
        sizeOnDiskLabel.setText("Loading...");
        Runnable task = () -> {
            boolean isCompressed = compact.isCompressed(filePath);
            if (compact.isValidDirectory(filePath)) {
                Size size = compact.calculateFolderSize(filePath);
                Platform.runLater(() -> {
                    compressButton.setDisable(false);
                    decompressButton.setDisable(false);
                    decompressButton.setVisible(isCompressed);
                    compressButton.setVisible(!filePath.isEmpty());
                    compressionAlgorithmLabel.setVisible(!filePath.isEmpty());
                    compressionAlgorithmChoiceBox.setVisible(!filePath.isEmpty());
                    compressButton.setText(isCompressed ? "Compress Again" : "Compress");
                    currentSizeLabel.setText(size.getSizeFormatted());
                    sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
                });
            }
        };
        new Thread(task).start();
    }
}
