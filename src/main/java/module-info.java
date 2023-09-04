module com.example.deadlockdetection {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.common;
    requires json.lib;

    opens com.example.deadlockdetection to javafx.fxml;
    exports com.example.deadlockdetection;
}