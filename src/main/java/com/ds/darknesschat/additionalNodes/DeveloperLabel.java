package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DeveloperLabel extends Label {
    public DeveloperLabel() {
        try {
            setText(SettingsReader.getStringValue(SettingsKeys.APP_DEVELOPER));
            setTextFill(Color.WHITE);
            setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 16d));
        }catch (Exception e){
            Log.error(e);
        }
    }

    public VBox wrapInVbox(){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().add(this);
        VBox.setVgrow(vBox, Priority.ALWAYS);

        return vBox;
    }
}
