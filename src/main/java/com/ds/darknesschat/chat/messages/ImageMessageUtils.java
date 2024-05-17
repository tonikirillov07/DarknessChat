package com.ds.darknesschat.chat.messages;

import com.ds.darknesschat.pages.ImageInfo;
import com.ds.darknesschat.utils.*;
import com.ds.darknesschat.utils.dialogs.InfoDialog;
import com.ds.darknesschat.utils.info.FileSize;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
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

            TextFlow textFlow = new TextFlow();

            Label userNameLabel = MessageUtils.createLabel(userName + ": ", userNameColor, true);
            Label userMessageLabel = MessageUtils.createLabel(message, Color.WHITE, false);
            Label messageTimeLabel = MessageUtils.createLabel("(" + Utils.getCurrentTime() + ")", Color.WHITE, false);

            textFlow.getChildren().add(userNameLabel);
            if(!message.isEmpty())
                textFlow.getChildren().add(userMessageLabel);

            ImageInfo imageInfo =  Objects.requireNonNull(ImageUtils.getImageFromBytes(bytes));
            Image image = imageInfo.image();

            ContextMenu contextMenu = createImageContextMenu(imageInfo, messagesContent, vBox, userId, bytes);

            assert contextMenu != null;
            contextMenu.setOnShown(windowEvent -> vBox.setOpacity(0.5d));
            contextMenu.setOnHidden(windowEvent -> vBox.setOpacity(1d));

            assert image != null;
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            imageView.maxWidth(699d);
            imageView.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(imageView, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY()));
            vBox.getChildren().addAll(textFlow, imageView, messageTimeLabel);

            Animations.addFadeTransitionToNode(imageView, userId);

            messagesContent.getChildren().add(vBox);
            messagesScrollPane.setVvalue(1d);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private static @Nullable ContextMenu createImageContextMenu(ImageInfo imageInfo, VBox messagesContent, Node messageNode, long userId, byte[] imageBytes){
        try {
            MenuItem imageInfoMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.SHOW_IMAGE_INFO));
            MenuItem openInFolderMenuItem = new MenuItem(StringGetterWithCurrentLanguage.getString(StringsConstants.OPEN_IN_FOLDER));

            imageInfoMenuItem.setOnAction(actionEvent -> {
                Image currentImage = Objects.requireNonNull(ImageUtils.getImageFromBytes(imageBytes)).image();
                FileSize moreComfortableSize = getMoreComfortableImageSizeFromBytes(imageBytes.length);

                String message = StringGetterWithCurrentLanguage.getString(StringsConstants.IMAGE_RESOLUTION) + " " + (int) currentImage.getWidth() + "x" + (int) currentImage.getHeight() + "\n" +
                        StringGetterWithCurrentLanguage.getString(StringsConstants.IMAGE_SIZE) + " " + moreComfortableSize.size() + " " + moreComfortableSize.format();

                InfoDialog.show(message);
            });

            openInFolderMenuItem.setOnAction(actionEvent -> Utils.openFile(new File(imageInfo.path()).getParentFile()));

            MessageContextMenu messageContextMenu = new MessageContextMenu(new TransferableImage(ImageUtils.convertByteArrayToBufferedImage(imageInfo.imageBytes())), messageNode, messagesContent);
            messageContextMenu.getItems().addAll(openInFolderMenuItem, imageInfoMenuItem);

            return messageContextMenu;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private static @NotNull FileSize getMoreComfortableImageSizeFromBytes(int bytesValue){
        String format = Utils.convertBytesToMegaBytes(bytesValue) >= 1d ? "megaBytes" :
                Utils.convertBytesToKiloBytes(bytesValue) >= 1d ? "kiloBytes" : "bytes";

        double size = Utils.convertBytesToMegaBytes(bytesValue) >= 1d ? Utils.convertBytesToMegaBytes(bytesValue) :
                Utils.convertBytesToKiloBytes(bytesValue) >= 1d ? Utils.convertBytesToKiloBytes(bytesValue) : bytesValue;

        return new FileSize(format, size);
    }
}
