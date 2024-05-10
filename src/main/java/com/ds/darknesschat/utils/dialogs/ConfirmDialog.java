package com.ds.darknesschat.utils.dialogs;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ConfirmDialog {
    public static boolean show(String message){
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.CONFIRM));
            alert.setHeaderText(message);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(Utils.getImage("bitmaps/icons/others/confirm.png"));
            alert.showAndWait();

            return alert.getResult() == ButtonType.OK;
        }catch (Exception e){
           e.printStackTrace();
        }

        return false;
    }
}
