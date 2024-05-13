package com.ds.darknesschat.chat;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys.MAX_MESSAGES_COUNT;

public class MessageUtils {
    public static void createMessageLabel(String text, javafx.scene.paint.Color userNameColor, ScrollPane messagesScrollPane, VBox messagesContent, long userId){
        try {
            Label userLabel = createLabel(Utils.extractUserNameFromMessage(text), userNameColor);
            Label messageLabel = createLabel(Utils.extractMessageTextFromMessage(text), javafx.scene.paint.Color.WHITE);

            TextFlow textFlow = new TextFlow();
            textFlow.getChildren().addAll(userLabel, messageLabel);

            Label label = new Label();
            label.setGraphic(textFlow);

            messagesContent.getChildren().add(label);

            if(messagesContent.getChildren().size() > SettingsReader.getIntegerValue(MAX_MESSAGES_COUNT))
                Utils.clearPaneInHalf(messagesContent);

            Animations.addFadeTransitionToNode(label, userId);
            Animations.addTextTypingAnimationToLabel(Utils.extractMessageTextFromMessage(text), messageLabel, userId);
            messagesScrollPane.setVvalue(1);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static @NotNull Label createLabel(String text, javafx.scene.paint.Color color){
        Label label = new Label(text);
        label.setTextFill(color);
        label.setWrapText(true);
        label.setMaxWidth(756d);
        label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_PATH), 14d));

        return label;
    }

    public static @NotNull String generateUserStringMessage(String userName, String userMessage){
        return userName + ": " + userMessage + " (" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ")";
    }
}
