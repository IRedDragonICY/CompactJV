package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NavbarUI extends Controller {
    private boolean isNavbarVisible;

    public NavbarUI() {
        super();
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
