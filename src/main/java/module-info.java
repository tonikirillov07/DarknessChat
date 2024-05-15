module com.ds.darknesschat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires java.desktop;
    requires java.sql;
    requires org.json;
    requires me.xdrop.fuzzywuzzy;

    opens com.ds.darknesschat to javafx.fxml;
    exports com.ds.darknesschat;
}