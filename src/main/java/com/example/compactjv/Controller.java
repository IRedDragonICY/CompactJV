package com.example.compactjv;

import com.example.compactjv.UI.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Controller {
    @FXML
    private Button closeButton, minimizeButton, compressButton, decompressButton, informationButton;
    @FXML
    private TextField filePathField;
    @FXML
    private Label currentSizeLabel, sizeOnDiskLabel, percentageLabel, cpuUsageLabel, memoryUsageLabel, totalFolderOnFile;
    @FXML
    private ChoiceBox<String> compressionAlgorithmChoiceBox;
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label infoText;
    @FXML
    private Label homeText;
    @FXML
    private AnchorPane navbar;
    @FXML
    private Button hamburgerButton;
    @FXML
    private VBox diskContainer;
    @FXML
    private TextArea debugTextArea;
    public void initialize() {
        File compact = new File();
        ExecutorServiceManager executorServiceManager = new ExecutorServiceManager();
        compressionAlgorithmChoiceBox.setDisable(true);
        percentageLabel.setText("");
        progressBar.setVisible(false);

        new DebuggingBoxUI(debugTextArea, executorServiceManager);
        new ButtonHandler(compact, currentSizeLabel, sizeOnDiskLabel, compressButton, decompressButton, compressionAlgorithmChoiceBox, executorServiceManager, percentageLabel, progressBar);
        new NavbarUI(infoText, homeText, navbar, hamburgerButton);
        new WindowControlsUI(minimizeButton, closeButton);
        new ButtonUI(compressButton, decompressButton, informationButton);
        new FilePathUI(compact, filePathField, currentSizeLabel, sizeOnDiskLabel, totalFolderOnFile, compressButton, decompressButton, compressionAlgorithmChoiceBox, executorServiceManager);
        new DiskInformationUI(diskContainer);
        new CPUInformationUI(cpuUsageLabel);
        new MemoryInformationUI(memoryUsageLabel, executorServiceManager);
    }

}


