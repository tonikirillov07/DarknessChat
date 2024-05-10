package com.ds.darknesschat;

import com.ds.darknesschat.utils.FilesChecker;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(@NotNull Stage stage) {
        try {
            Log.info("Opening window...");

            if(!FilesChecker.checkFiles())
                System.exit(-1);

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 906, 957);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles/style.css")).toExternalForm());
            stage.setTitle(SettingsReader.getStringValue(SettingsKeys.APP_NAME));
            stage.getIcons().add(Utils.getImage(SettingsReader.getStringValue(SettingsKeys.APP_ICON)));
            stage.setResizable(SettingsReader.getBooleanValue(SettingsKeys.APP_RESIZABLE));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

            Log.info("Window was opened!");
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}