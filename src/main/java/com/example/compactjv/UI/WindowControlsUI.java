package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowControlsUI extends Controller {

    public WindowControlsUI() {
        super();
        setButtonProperties(closeButton, event -> System.exit(0));
        setButtonProperties(minimizeButton, event -> ((Stage) ((Node) event.getSource()).getScene().getWindow()).setIconified(true));
    }

    private void setButtonProperties(Button button, EventHandler<ActionEvent> eventHandler) {
        button.setOnAction(eventHandler);

        ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
        st.setByX(0.15);
        st.setByY(0.15);
        st.setCycleCount(2);
        st.setAutoReverse(true);

        button.setOnMouseEntered(event -> st.playFromStart());

        button.setOnMouseExited(event -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);
        });
    }
}
