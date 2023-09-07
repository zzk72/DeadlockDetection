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
    requires atlantafx.base;
    requires lombok;

    opens com.example.deadlockdetection to javafx.fxml;
    exports com.example.deadlockdetection;
    exports com.example.deadlockdetection.ResourceNode;
    opens com.example.deadlockdetection.ResourceNode to javafx.fxml;
    exports com.example.deadlockdetection.Config;
    opens com.example.deadlockdetection.Config to javafx.fxml;
    exports com.example.deadlockdetection.ProcessNode;
    opens com.example.deadlockdetection.ProcessNode to javafx.fxml;
}