package com.example.compactjv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 780);
        stage.initStyle(StageStyle.UNDECORATED); // Menambahkan baris ini
        stage.setResizable(false);
        stage.setTitle("CompactJV");
        stage.setScene(scene);
        stage.show();
//        testing
    }

    public static void main(String[] args) {
        launch();
    }
}
