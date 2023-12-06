package com.example.compactjv;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;

import java.util.concurrent.*;

public class Controller {
    @FXML
    private Button closeButton, minimizeButton, compressButton, decompressButton;
    @FXML
    private TextField filePathField;
    @FXML
    private Label currentSizeLabel, sizeOnDiskLabel, compressionAlgorithmLabel, percentageLabel;
    @FXML
    private ChoiceBox<String> compressionAlgorithmChoiceBox;
    @FXML
    private ProgressBar progressBar;
    private File compact;
    private ExecutorServiceManager executorServiceManager;


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
        setupCompressionButton();
        setupDecompressionButton();
        setLabelAndChoiceBoxVisibility(false);
    }

    private void setupCompressionButton() {
        compressButton.setOnAction(event -> handleCompressionOrDecompression(true));
        compressButton.setVisible(false);
    }

    private void setupDecompressionButton() {
        decompressButton.setOnAction(event -> handleCompressionOrDecompression(false));
        decompressButton.setVisible(false);
    }

    private void setButtonProperties(Button button, String text, EventHandler<ActionEvent> eventHandler) {
        button.setText(text);
        button.setOnAction(eventHandler);
    }

    private void setLabelAndChoiceBoxVisibility(boolean visibility) {
        compressionAlgorithmLabel.setVisible(visibility);
        compressionAlgorithmChoiceBox.setVisible(visibility);
    }

    private void setupFilePathField() {
        filePathField.setEditable(false);
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
        filePathField.textProperty().addListener(this::FieldTextHasChanged);
    }

    private void setupCompressionAlgorithmChoiceBox() {
        compressionAlgorithmChoiceBox.setItems(FXCollections.observableArrayList("XPRESS4K", "XPRESS8K", "XPRESS16K", "LZX"));
        compressionAlgorithmChoiceBox.setValue("XPRESS4K");
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
        updateButtonsAndLabels();
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
        Platform.runLater(() -> {
            updateButtonsAndLabels();
            enableButtons();
        });
    }

    private void updateSizeOnDiskLabel(String text) {
        sizeOnDiskLabel.setText(text);
    }

    private void disableButtons() {
        compressButton.setDisable(true);
        decompressButton.setDisable(true);
    }

    private void monitorTaskUntilCompletion(Future<?> future, String filePath, boolean isCompression) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.submit(() -> {
                while (!future.isDone()) {
                    compact.getTotalFolderCompressed(filePath);
                    updatePercentageLabel(isCompression);
                }

                Platform.runLater(() -> {
                    percentageLabel.setText("100%");
                    progressBar.setProgress(1);
                });
            });
        } finally {
            executorService.shutdown();
        }
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
        if (isCompression) {
            return (double) totalCompressed / total * 100;
        } else {
            return (double) totalDecompressed / total * 100;
        }
    }

    private void enableButtons() {
        compressButton.setDisable(false);
        decompressButton.setDisable(false);
    }

    private void updateFilePath(java.io.File file) {
        compact.setFilePath(file.getAbsolutePath());
        filePathField.setText(file.getName());
    }

    private void updateButtonsAndLabels() {
        String filePath = compact.getFilePath();
        disableButtons();
        currentSizeLabel.setText("Loading...");
        sizeOnDiskLabel.setText("Loading...");
        setLabelAndChoiceBoxVisibility(false);
        if (compact.isValidDirectory(filePath)) {
            prepareFolder(filePath);
        }
    }

    private void prepareFolder(String filePath) {
        Runnable task = () -> {
            boolean isCompressed = compact.isCompressed(filePath);
            Size size = compact.calculateFolderSize(filePath);
            updateUIAfterFolderPreparation(isCompressed, size);
        };
        new Thread(task).start();
    }

    private void updateUIAfterFolderPreparation(boolean isCompressed, Size size) {
        Platform.runLater(() -> {
            updateButtonVisibilityAndText(isCompressed);
            currentSizeLabel.setText(size.getSizeFormatted());
            sizeOnDiskLabel.setText(isCompressed ? size.getSizeOnDiskFormatted() : "??");
            setLabelAndChoiceBoxVisibility(true);
        });
    }

    private void updateButtonVisibilityAndText(boolean isCompressed) {
        compressButton.setDisable(false);
        decompressButton.setDisable(false);
        decompressButton.setVisible(isCompressed);
        compressButton.setVisible(true);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
    }
}
