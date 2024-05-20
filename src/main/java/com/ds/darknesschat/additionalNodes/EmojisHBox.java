package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.chat.messages.emojis.EmojiInfo;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.eventListeners.IOnEmojiSelect;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.util.List;

public class EmojisHBox extends HBox {
    private final List<EmojiInfo> emojisInfos;
    private final IOnEmojiSelect onEmojiSelect;
    private final long userId;

    public EmojisHBox(List<EmojiInfo> emojisInfos, IOnEmojiSelect onEmojiSelect, long userId) {
        this.emojisInfos = emojisInfos;
        this.onEmojiSelect = onEmojiSelect;
        this.userId = userId;

        init();
    }

    private void init() {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(15d);

        emojisInfos.forEach(emojiInfo -> getChildren().add(createEmoji(emojiInfo)));
    }

    private @Nullable ImageView createEmoji(EmojiInfo emojiInfo){
        try {
            FileInputStream fileInputStream = new FileInputStream(emojiInfo.imageFile());

            ImageView emojiButtonImageView = new ImageView(new Image(fileInputStream));
            emojiButtonImageView.setFitHeight(32d);
            emojiButtonImageView.setFitWidth(32d);
            emojiButtonImageView.setCursor(Cursor.HAND);
            emojiButtonImageView.getStyleClass().add("image-button");
            Utils.addActionToNode(emojiButtonImageView, () -> onEmojiSelect.onEmojiSelect(emojiInfo), userId);

            Tooltip.install(emojiButtonImageView, Utils.createTooltip(emojiInfo.name()));

            fileInputStream.close();

            return emojiButtonImageView;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
