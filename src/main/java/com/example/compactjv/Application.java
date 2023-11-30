package com.example.compactjv;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;
import java.io.IOException;
import javafx.scene.image.Image;

public class Application extends javafx.application.Application {
    private static final String VIEW_RESOURCE_PATH = "hello-view.fxml";
    private static final String STYLESHEET_PATH = "application.css";
    private static final String ICON_PATH = "com/example/compactjv/app.png";
    private static final String APP_TITLE = "CompactJV";

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = setUpScene();

        setMouseEvents(scene, stage);
        setUpStage(stage, scene);

        stage.show();
    }

    private Scene setUpScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(VIEW_RESOURCE_PATH));
        Scene scene = new Scene(fxmlLoader.load(), 500, 780);

        scene.getStylesheets().add(getClass().getResource(STYLESHEET_PATH).toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        return scene;
    }

    private void setMouseEvents(Scene scene, Stage stage) {
        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    private void setUpStage(Stage stage, Scene scene) {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setTitle(APP_TITLE);
        stage.getIcons().add(new Image(ICON_PATH));
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}
