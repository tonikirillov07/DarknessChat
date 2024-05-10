package com.ds.darknesschat.utils.dialogs;

import com.ds.darknesschat.utils.Utils;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public final class ErrorDialog {
    public static void show(@NotNull Exception e){
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An exception has occurred");
            alert.setHeaderText(e.toString());
            alert.setContentText("Cause: " + e.getCause());

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(Utils.getImage("bitmaps/icons/others/error.png"));

            alert.show();
        }catch (Exception exception){
            e.printStackTrace();
            exception.printStackTrace();
        }
    }
}
