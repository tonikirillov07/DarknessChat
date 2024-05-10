package com.ds.darknesschat.utils.dialogs;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class InfoDialog {
    public static void show(String message){
        try {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(StringGetterWithCurrentLanguage.getString(StringsConstants.INFO));
            alert.setHeaderText(message);

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(Utils.getImage("bitmaps/icons/others/info_1.png"));

            alert.showAndWait();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
