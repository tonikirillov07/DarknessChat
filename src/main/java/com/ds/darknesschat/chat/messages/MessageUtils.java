package com.ds.darknesschat.chat.messages;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.appSettings.settingsReader.SettingsReader;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.utils.appSettings.settingsReader.SettingsKeys.MAX_MESSAGES_COUNT;

public class MessageUtils {
    public static void createMessageLabel(String text, javafx.scene.paint.Color userNameColor, ScrollPane messagesScrollPane, VBox messagesContent, long userId){
        try {
            Label userLabel = createLabel(Utils.extractUserNameFromMessage(text), userNameColor, true);

            Label messageLabel = createLabel(Utils.extractMessageTextFromMessage(text), javafx.scene.paint.Color.WHITE, false);
            messageLabel.setMaxWidth(messagesScrollPane.getMaxWidth() - 10d);
            messageLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));

            Label timeLabel = createLabel(" (" + Utils.getCurrentTime() + ")", Color.DARKGRAY, false);
            timeLabel.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 14d));

            TextFlow textFlow = new TextFlow();
            VBox.setMargin(textFlow, new Insets(10d, 0, 0, 0));
            textFlow.getChildren().addAll(userLabel, messageLabel, timeLabel);

            Label label = getLabel(text, messagesContent, textFlow);
            messagesContent.getChildren().add(label);

            if(messagesContent.getChildren().size() > SettingsReader.getIntegerValue(MAX_MESSAGES_COUNT))
                Utils.clearPaneInHalf(messagesContent);

            Animations.addFadeTransitionToNode(label, userId);
            Animations.addTextTypingAnimationToLabel(Utils.extractMessageTextFromMessage(text), messageLabel, userId);

            messagesScrollPane.setVvalue(1d);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private static @NotNull Label getLabel(String text, VBox messagesContent, TextFlow textFlow) {
        Label label = new Label();
        label.setWrapText(true);
        label.setGraphic(textFlow);

        MessageContextMenu messageContextMenu = new MessageContextMenu(new StringSelection(text + " (" + Utils.getCurrentTime() + ")"), label, messagesContent);
        messageContextMenu.setOnShown(windowEvent -> label.setOpacity(0.5d));
        messageContextMenu.setOnHidden(windowEvent -> label.setOpacity(1d));

        label.setOnContextMenuRequested(contextMenuEvent -> messageContextMenu.show(label, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));

        return label;
    }

    public static @NotNull Label createLabel(String text, javafx.scene.paint.Color color, boolean isUserName){
        Label label = new Label(text);
        label.setTextFill(color);
        label.setWrapText(true);
        label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_PATH), isUserName ? 16d : 14d));

        return label;
    }

    public static @Nullable List<String> getAvailableHexColorsForNicknames(){
        try {
            List<String> allHexColors = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("settings/nicknames_colors.txt"))));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                allHexColors.add(line);
            }

            bufferedReader.close();

            return allHexColors;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
