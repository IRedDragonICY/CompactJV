package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
@Getter @Setter
public class ButtonUI extends Controller {

    public ButtonUI() {
        super();
        init();
    }

    private void setupInformationButton() {
        informationButton.setOnAction(event -> {
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.getScene().getRoot().setEffect(null);
            primaryStage.getScene().getRoot().setDisable(true);
            primaryStage.getScene().getRoot().setOnMousePressed(null);

            Stage dialog = new Stage();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(primaryStage);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setWidth(400);
            dialog.setHeight(420);

            GridPane content = new GridPane();
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: #121212FF; -fx-background-radius: 10;");

            Text header = new Text("CompactJV - Compression Tool");
            header.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            header.setFill(Color.WHITE);

            Text version = new Text("Version 1.0");
            version.setFill(Color.WHITE);

            javafx.scene.image.Image image = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/compactjv/logo-publisher.png")));
            ImageView logoImageView = new ImageView(image);
            logoImageView.setPreserveRatio(true);
            logoImageView.setFitWidth(150);
            logoImageView.setSmooth(true);
            logoImageView.setCache(true);

            Text description = new Text(
                    "CompactJV is a compression tool using Compact.exe from Windows SDK for compressing files, "
                            + "such as games or applications. This tool is developed in Java and intended for use on Windows OS."
            );
            description.setWrappingWidth(350);
            description.setFill(Color.WHITE);

            Text developers = new Text(
                    """
                            Developed by:
                            1. Muhammad Farid Hendianto (Ndik)   {2200018401}
                            2. Reyhanssan Islamey (Justin)       {2200018411}
                            3. Rendie Abdi Saputra (Ryu)         {2200018094}
                            4. Fadhil Raifan Andika              {2200018458}
                                                        
                            """
            );
            developers.setFill(Color.WHITE);

            javafx.scene.control.Button okButton = new javafx.scene.control.Button("OK");
            okButton.setOnAction(e -> {
                dialog.close();
                primaryStage.getScene().getRoot().setEffect(null);
                primaryStage.getScene().getRoot().setDisable(false);
            });
            okButton.setStyle("-fx-background-color: #F56E0FFF; -fx-background-radius: 10; -fx-text-fill: #121212FF;");
            javafx.scene.control.Button githubButton = new javafx.scene.control.Button("GitHub");
            githubButton.setOnAction(e -> {

                String url = "https://github.com/IRedDragonICY/CompactJV";
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            });
            githubButton.setStyle("-fx-background-color: #F56E0FFF; -fx-background-radius: 10; -fx-text-fill: #121212FF;");
            content.add(header, 0, 0, 2, 1);
            content.add(version, 0, 1, 2, 1);
            content.add(logoImageView, 0, 2, 2, 1);
            content.add(description, 0, 3, 2, 1);
            content.add(developers, 0, 4, 2, 1);
            content.add(okButton, 1, 5);
            content.add(githubButton, 0, 5);

            GridPane.setHalignment(header, HPos.CENTER);
            GridPane.setHalignment(version, HPos.CENTER);
            GridPane.setHalignment(logoImageView, HPos.CENTER);
            GridPane.setHalignment(description, HPos.CENTER);
            GridPane.setHalignment(developers, HPos.CENTER);
            GridPane.setHalignment(okButton, HPos.RIGHT);
            GridPane.setHalignment(githubButton, HPos.LEFT);

            Scene scene = new Scene(content);
            scene.setFill(Color.TRANSPARENT);

            dialog.setScene(scene);
            dialog.showAndWait();
        });
    }

    public void init() {

        setupInformationButton();
    }

}

