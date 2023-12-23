package com.example.compactjv.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.scene.control.Button;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NavbarUI  {

    private Label infoText;
    private Label homeText;
    private AnchorPane navbar;
    private Button hamburgerButton;
    private boolean isNavbarVisible = false;

    public NavbarUI(Label infoText, Label homeText, AnchorPane navbar, Button hamburgerButton) {
        this.infoText = infoText;
        this.homeText = homeText;
        this.navbar = navbar;
        this.hamburgerButton = hamburgerButton;

        hideNavbar();
        hamburgerButton.setOnMouseClicked(event -> toggleNavbar());
    }

    private void toggleNavbar() {
        if (isNavbarVisible) {
            hideNavbar();
        } else {
            showNavbar();
        }
    }

    private void showNavbar() {
        Timeline timeline = getTimeline(200);
        timeline.setOnFinished(e -> navbar.getChildren().addAll(infoText, homeText));
        timeline.play();
        isNavbarVisible = true;
    }

    private void hideNavbar() {
        navbar.getChildren().removeAll(infoText, homeText);
        Timeline timeline = getTimeline(65);
        timeline.play();
        isNavbarVisible = false;
    }

    private Timeline getTimeline(int width) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(navbar.prefWidthProperty(), width);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        timeline.getKeyFrames().add(kf);
        return timeline;
    }
}
