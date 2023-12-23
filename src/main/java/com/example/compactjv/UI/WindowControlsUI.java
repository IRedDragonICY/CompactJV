package com.example.compactjv.UI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WindowControlsUI {

    public WindowControlsUI(Button minimizeButton, Button closeButton) {

        setButtonProperties(closeButton, event -> System.exit(0));
        setButtonProperties(minimizeButton, event -> ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true));
    }
    private void setButtonProperties(javafx.scene.control.Button button, EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);
    }
}
