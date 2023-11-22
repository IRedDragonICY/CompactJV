package com.example.compactjv;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.File;
import javafx.scene.input.TransferMode;
import javafx.scene.input.Dragboard;
import javafx.scene.control.Label;


public class CompactController {

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private TextField filePathField;

    @FXML
    private Button selectFileButton;
    @FXML
    private Label currentSizeLabel;

    @FXML
    private Label estimatedSizeField;

    public long calculateFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        if (files != null) { // Null if security restricted
            for (File file : files) {
                if (file.isFile()) {
                    length += file.length();
                } else {
                    length += calculateFolderSize(file); // Recursive call
                }
            }
        }

        return length;
    }

    public void initialize() {
        closeButton.setText("X");
        closeButton.setOnAction(event -> Platform.exit());

        minimizeButton.setText("-");
        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });

        filePathField.setEditable(false); // make the text field non-editable
        filePathField.setText("Drag and Drop here or Click here to select a directory"); // set the initial text

        filePathField.setOnMouseClicked(event -> { // handle mouse click event
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);

            if (selectedDirectory != null) {
                filePathField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        // Add the following code for drag and drop functionality:
        filePathField.setOnDragOver(event -> {
            if (event.getGestureSource() != filePathField && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        filePathField.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                filePathField.setText(db.getFiles().get(0).getAbsolutePath());
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });


        filePathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                File file = new File(newValue);
                if (file.exists() && file.isDirectory()) { // Check that it's a directory
                    long sizeInBytes = calculateFolderSize(file);
                    double sizeInKB = sizeInBytes / 1024.0;
                    double sizeInMB = sizeInKB / 1024.0;
                    double sizeInGB = sizeInMB / 1024.0;

                    // If less than one KB
                    if (sizeInKB < 1) {
                        currentSizeLabel.setText(sizeInBytes + " bytes");
                    }
                    // If less than one MB
                    else if (sizeInMB < 1) {
                        currentSizeLabel.setText(String.format("%.2f KB", sizeInKB));
                    }
                    // If less than one GB
                    else if (sizeInGB < 1) {
                        currentSizeLabel.setText(String.format("%.2f MB", sizeInMB));
                    }
                    // If more than one GB
                    else {
                        currentSizeLabel.setText(String.format("%.2f GB", sizeInGB));
                    }

                } else {
                    currentSizeLabel.setText(" - ");
                }
            } else {
                currentSizeLabel.setText(" - ");
            }
        });




    }
}
