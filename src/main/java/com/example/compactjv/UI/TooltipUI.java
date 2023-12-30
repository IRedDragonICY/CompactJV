package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class TooltipUI extends Controller {
    public TooltipUI() {
        super();
        setupTooltips();
    }

    private void setupTooltips() {
        Tooltip closeButtonTooltip = createTooltip("Close the application");
        Tooltip.install(closeButton, closeButtonTooltip);

        Tooltip minimizeButtonTooltip = createTooltip("Minimize the application");
        Tooltip.install(minimizeButton, minimizeButtonTooltip);

        Tooltip compressButtonTooltip = createTooltip("Compress the file");
        Tooltip.install(compressButton, compressButtonTooltip);

        Tooltip decompressButtonTooltip = createTooltip("Decompress the file");
        Tooltip.install(decompressButton, decompressButtonTooltip);

        Tooltip informationButtonTooltip = createTooltip("Get information about the file");
        Tooltip.install(informationButton, informationButtonTooltip);

        Tooltip filePathFieldTooltip = createTooltip("Enter the path of the file");
        Tooltip.install(filePathField, filePathFieldTooltip);

        Tooltip currentSizeLabelTooltip = createTooltip("Displays the current size of the file");
        Tooltip.install(currentSizeLabel, currentSizeLabelTooltip);

        Tooltip sizeOnDiskLabelTooltip = createTooltip("Displays the size of the file on disk");
        Tooltip.install(sizeOnDiskLabel, sizeOnDiskLabelTooltip);

        Tooltip percentageLabelTooltip = createTooltip("Displays the percentage of compression/decompression");
        Tooltip.install(percentageLabel, percentageLabelTooltip);

        Tooltip cpuUsageLabelTooltip = createTooltip("Displays the current CPU usage");
        Tooltip.install(cpuUsageLabel, cpuUsageLabelTooltip);

        Tooltip memoryUsageLabelTooltip = createTooltip("Displays the current memory usage");
        Tooltip.install(memoryUsageLabel, memoryUsageLabelTooltip);

        Tooltip totalFolderOnFileTooltip = createTooltip("Displays the total number of folders in the file");
        Tooltip.install(totalFolderOnFile, totalFolderOnFileTooltip);

        Tooltip compressionAlgorithmChoiceBoxTooltip = createTooltip("Select the compression algorithm");
        Tooltip.install(compressionAlgorithmChoiceBox, compressionAlgorithmChoiceBoxTooltip);

        Tooltip progressBarTooltip = createTooltip("Displays the progress of compression/decompression");
        Tooltip.install(progressBar, progressBarTooltip);

        Tooltip infoTextTooltip = createTooltip("Displays information about the application");
        Tooltip.install(infoText, infoTextTooltip);

        Tooltip homeTextTooltip = createTooltip("Navigate to the home screen");
        Tooltip.install(homeText, homeTextTooltip);

        Tooltip hamburgerButtonTooltip = createTooltip("Open the navigation menu");
        Tooltip.install(hamburgerButton, hamburgerButtonTooltip);

        Tooltip diskContainerTooltip = createTooltip("Displays information about the disk");
        Tooltip.install(diskContainer, diskContainerTooltip);

        Tooltip debugTextAreaTooltip = createTooltip("Displays debug information");
        Tooltip.install(debugTextArea, debugTextAreaTooltip);
    }

    private Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.seconds(0.5));
        tooltip.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14; -fx-background-color: #FFFFFF; -fx-text-fill: #000000;");

        Node scene = tooltip.getScene().getRoot();
        scene.setOnMouseMoved(event -> {
            tooltip.setX(event.getScreenX());
            tooltip.setY(event.getScreenY());
        });

        return tooltip;
    }

}
