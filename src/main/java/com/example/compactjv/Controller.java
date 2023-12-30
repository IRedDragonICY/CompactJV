package com.example.compactjv;

import com.example.compactjv.UI.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Controller {
    @Getter
    private static Controller instance;
    protected final ExecutorServiceManager executorServiceManager = new ExecutorServiceManager();
    @FXML
    protected Button closeButton, minimizeButton, compressButton, decompressButton, informationButton;
    @FXML
    protected TextField filePathField;
    @FXML
    protected Label currentSizeLabel, sizeOnDiskLabel, percentageLabel, cpuUsageLabel, memoryUsageLabel, totalFolderOnFile;
    @FXML
    protected ChoiceBox<String> compressionAlgorithmChoiceBox;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected Label infoText, homeText;
    @FXML
    protected AnchorPane navbar;
    @FXML
    protected Button hamburgerButton;
    @FXML
    protected VBox diskContainer;
    @FXML
    protected TextArea debugTextArea;
    protected File compact = new File();

    public Controller() {
        if (instance == null) {
            instance = this;
        }
        compact = Controller.getInstance().getCompact();
        filePathField = Controller.getInstance().getFilePathField();
        currentSizeLabel = Controller.getInstance().getCurrentSizeLabel();
        sizeOnDiskLabel = Controller.getInstance().getSizeOnDiskLabel();
        totalFolderOnFile = Controller.getInstance().getTotalFolderOnFile();
        compressButton = Controller.getInstance().getCompressButton();
        decompressButton = Controller.getInstance().getDecompressButton();
        percentageLabel = Controller.getInstance().getPercentageLabel();
        progressBar = Controller.getInstance().getProgressBar();
        cpuUsageLabel = Controller.getInstance().getCpuUsageLabel();
        memoryUsageLabel = Controller.getInstance().getMemoryUsageLabel();
        compressionAlgorithmChoiceBox = Controller.getInstance().getCompressionAlgorithmChoiceBox();
        infoText = Controller.getInstance().getInfoText();
        homeText = Controller.getInstance().getHomeText();
        navbar = Controller.getInstance().getNavbar();
        hamburgerButton = Controller.getInstance().getHamburgerButton();
        diskContainer = Controller.getInstance().getDiskContainer();
        debugTextArea = Controller.getInstance().getDebugTextArea();
        closeButton = Controller.getInstance().getCloseButton();
        minimizeButton = Controller.getInstance().getMinimizeButton();
        informationButton = Controller.getInstance().getInformationButton();
    }

    public void initialize() {
        compressionAlgorithmChoiceBox.setDisable(true);
        percentageLabel.setText("");
        progressBar.setVisible(false);

        setupUI();
    }

    private void setupUI() {
        new DebuggingBoxUI();
        new ButtonHandler();
        new NavbarUI();
        new WindowControlsUI();
        new ButtonUI();
        new FilePathUI();
        new DiskInformationUI();
        new CPUInformationUI();
        new MemoryInformationUI();
        new TooltipUI();
    }
}
