module com.ds.darknesschat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires java.desktop;

    opens com.ds.darknesschat to javafx.fxml;
    exports com.ds.darknesschat;
}