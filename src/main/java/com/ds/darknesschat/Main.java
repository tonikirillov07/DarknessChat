package com.ds.darknesschat;

import com.ds.darknesschat.utils.SettingsKeys;
import com.ds.darknesschat.utils.SettingsReader;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Main extends Application {
    @Override
    public void start(@NotNull Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 906, 957);
            stage.setTitle(SettingsReader.getStringValue(SettingsKeys.APP_NAME));
            stage.getIcons().add(Utils.getImage(SettingsReader.getStringValue(SettingsKeys.APP_ICON)));
            stage.setResizable(SettingsReader.getBooleanValue(SettingsKeys.APP_RESIZABLE));
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}