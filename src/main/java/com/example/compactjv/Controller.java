package com.example.compactjv;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.*;


public class Controller {
    @FXML
    private Button closeButton, minimizeButton, compressButton, decompressButton, informationButton;
    @FXML
    private TextField filePathField;
    @FXML
    private Label currentSizeLabel, sizeOnDiskLabel, compressionAlgorithmLabel, percentageLabel, cpuUsageLabel;
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
        setupCPUUsageLabel();
        compact = new File();
        executorServiceManager = new ExecutorServiceManager();

    }

    private void setupButtons() {
        setButtonProperties(closeButton, event -> Platform.exit());
        setButtonProperties(minimizeButton, event -> ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true));
        setupCompressionButton();
        setupDecompressionButton();
        compressionAlgorithmChoiceBox.setDisable(true);
        setupInformationButton();

        percentageLabel.setText("");
        progressBar.setVisible(false);
    }

    private void setupCPUUsageLabel() {
        Runnable task = () -> {
            while (true) {
                String cpuUsage = compact.getCPUUsage();
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
    private void setupCompressionButton() {
        compressButton.setOnAction(event -> handleCompressionOrDecompression(true));
        compressButton.setDisable(true);
    }

    private void setupDecompressionButton() {
        decompressButton.setOnAction(event -> handleCompressionOrDecompression(false));
        decompressButton.setDisable(true);
    }


    private void setupInformationButton() {
        informationButton.setOnAction(event -> {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.getScene().getRoot().setEffect(null);
            primaryStage.getScene().getRoot().setDisable(true);
            primaryStage.getScene().getRoot().setOnMousePressed(null);

            Stage dialog = new Stage();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(primaryStage);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setWidth(400);
            dialog.setHeight(350);

            VBox content = new VBox();
            content.setSpacing(10);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: #121212FF; -fx-background-radius: 10;");

            Text header = new Text("CompactJV - Compression Tool");
            header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            header.setFill(Color.WHITE);

            Text version = new Text("Version 1.0");
            version.setFill(Color.WHITE);

            Text description = new Text(
                    "CompactJV is a compression tool using Compact.exe from Windows SDK for compressing files, "
                            + "such as games or applications. This tool is developed in Java and intended for use on Windows OS."
            );
            description.setWrappingWidth(350);
            description.setFill(Color.WHITE);

            Text developers = new Text(
                    "Developed by:\n"
                            + "1. Muhammad Farid Hendianto (Ndik)   {2200018401}\n"
                            + "2. Reyhanssan Islamey (Justin) {2200018411}\n"
                            + "3. Rendie Abdi Saputra (Ryu)    {2200018094}"
            );
            developers.setFill(Color.WHITE);

            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

            Button okButton = new Button("OK");
            okButton.setOnAction(e -> {
                dialog.close();
                primaryStage.getScene().getRoot().setEffect(null);
                primaryStage.getScene().getRoot().setDisable(false);
            });

            Button githubButton = new Button("GitHub");
            githubButton.setOnAction(e -> {
                // Open GitHub page in a web browser
                String url = "https://github.com/IRedDragonICY/CompactJV";  // Replace with your GitHub repository URL
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });

            buttonBox.getChildren().addAll(okButton, githubButton);

            content.getChildren().addAll(header, version, description, developers, buttonBox);

            Scene scene = new Scene(content);
            scene.setFill(Color.TRANSPARENT);

            dialog.setScene(scene);
            dialog.showAndWait();
        });
    }






    private void setButtonProperties(Button button, EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }


    private void setupFilePathField() {
        filePathField.textProperty().addListener(this::FieldTextHasChanged);
        filePathField.setOnMouseClicked(this::handleMouseClickedOnField);
        filePathField.setOnDragOver(this::handleDragOverField);
        filePathField.setOnDragDropped(this::handleDragDroppedOnField);
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
        Platform.runLater(() -> {
            updateButtonsAndLabels();
            enableButtons();
            progressBar.setVisible(false);
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
            compressionAlgorithmChoiceBox.setDisable(false);
        });
    }

    private void updateButtonVisibilityAndText(boolean isCompressed) {
        compressButton.setDisable(true);
        decompressButton.setDisable(!isCompressed);
        compressButton.setDisable(false);
        compressButton.setText(isCompressed ? "Compress Again" : "Compress");
    }
}
