module com.example.compactjv {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires java.desktop;
    opens com.example.compactjv to javafx.fxml;
    exports com.example.compactjv;
    exports com.example.compactjv.UI;
    opens com.example.compactjv.UI to javafx.fxml;
}