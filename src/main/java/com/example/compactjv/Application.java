package com.example.compactjv;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    private static final String TITLE = "CompactJV";
    private static final String ICON_PATH = "/com/example/compactjv/app.png";
    private static final String VIEW_PATH = "view.fxml";

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(VIEW_PATH));
            Scene scene = new Scene(fxmlLoader.load());

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);

            setUpMouseListeners(scene, stage);

            stage.setResizable(false);
            stage.setTitle(TITLE);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(ICON_PATH))));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpMouseListeners(Scene scene, Stage stage) {
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
