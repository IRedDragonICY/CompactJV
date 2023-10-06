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


public class HelloController {

    @FXML
    private Button closeButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private TextField filePathField;

    @FXML
    private Button selectFileButton;


    public void initialize() {
        closeButton.setText("X");
        closeButton.setOnAction(event -> Platform.exit());

        minimizeButton.setText("-");
        minimizeButton.setOnAction(event -> {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setIconified(true);
        });

        selectFileButton.setOnAction(event -> {
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
    }
}
