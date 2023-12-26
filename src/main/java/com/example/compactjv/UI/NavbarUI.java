package com.example.compactjv.UI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NavbarUI {
    private final Label infoText;
    private final Label homeText;
    private final AnchorPane navbar;
    private final Button hamburgerButton;
    private boolean isNavbarVisible;

    public NavbarUI(Label infoText, Label homeText, AnchorPane navbar, Button hamburgerButton) {
        this.infoText = infoText;
        this.homeText = homeText;
        this.navbar = navbar;
        this.hamburgerButton = hamburgerButton;

        // hide the navbar initially
        hideNavbar();

        // toggle navbar visibility when hamburger button is clicked
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
        Timeline timeline = createTimeline(200);
        infoText.visibleProperty().setValue(true);
        homeText.visibleProperty().setValue(true);
        timeline.setOnFinished(e -> navbar.getChildren().addAll(infoText, homeText));
        timeline.play();
        isNavbarVisible = true;
    }

    private void hideNavbar() {
        navbar.getChildren().removeAll(infoText, homeText);
        Timeline timeline = createTimeline(65);
        timeline.play();
        isNavbarVisible = false;
    }

    /**
     * Create a timeline for animating the navbar
     * @param width target width
     * @return timeline object
     */
    private Timeline createTimeline(int width) {
        KeyValue kv = new KeyValue(navbar.prefWidthProperty(), width);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);

        return timeline;
    }
}
