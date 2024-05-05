module com.ds.darknesschat {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires java.logging;


    opens com.ds.darknesschat to javafx.fxml;
    exports com.ds.darknesschat;
}