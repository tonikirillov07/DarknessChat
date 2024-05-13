package com.ds.darknesschat.chat;

import com.ds.darknesschat.pages.ImageInfo;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.ImageUtils;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import java.io.File;
import java.util.Objects;

public class ImageMessageUtils {
    public static void createMessageImageView(@NotNull JSONArray jsonArray, String userName, javafx.scene.paint.Color userNameColor, String message, VBox messagesContent, ScrollPane messagesScrollPane, long userId) {
        try {
            byte[] bytes = new byte[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                bytes[i] = (byte) jsonArray.getInt(i);
            }

            VBox vBox = new VBox();
            vBox.setSpacing(10d);

            String messageForLabel = message.isEmpty() ? userName + ": " : MessageUtils.generateUserStringMessage(userName, message);
            Label userNameLabel = MessageUtils.createLabel(messageForLabel, userNameColor);

            ImageInfo imageInfo =  Objects.requireNonNull(ImageUtils.getImageFromBytes(bytes));
            Image image = imageInfo.image();
            ContextMenu contextMenu = createImageContextMenu(imageInfo);

            assert image != null;
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            imageView.maxWidth(699d);
            imageView.setOnContextMenuRequested(contextMenuEvent -> {
                assert contextMenu != null;
                contextMenu.show(imageView, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            });
            vBox.getChildren().addAll(userNameLabel, imageView);
            Animations.addFadeTransitionToNode(imageView, userId);

            messagesContent.getChildren().add(vBox);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private static @Nullable ContextMenu createImageContextMenu(ImageInfo imageInfo){
        try {
            MenuItem copyImageMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.COPY_IMAGE));
            MenuItem openInFolderMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.OPEN_IN_FOLDER));

            copyImageMenuItem.setOnAction(actionEvent -> Utils.copyImageToClipboard(ImageUtils.convertByteArrayToBufferedImage(imageInfo.imageBytes())));
            openInFolderMenuItem.setOnAction(actionEvent -> Utils.openFile(new File(imageInfo.path()).getParentFile()));

            ContextMenu contextMenu = new ContextMenu();
            contextMenu.getItems().addAll(copyImageMenuItem, openInFolderMenuItem);

            return contextMenu;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
