package com.example.compactjv;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.application.Platform;

public class HelloController {

    @FXML
    private Button closeButton;

    @FXML
    public void initialize() {
        closeButton.setText("X");
        closeButton.setOnAction(event -> Platform.exit());
    }
}
