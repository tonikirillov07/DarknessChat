package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.chat.messages.emojis.EmojisParser;
import com.ds.darknesschat.utils.Animations;
import com.ds.darknesschat.utils.eventListeners.IOnEmojiSelect;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import static com.ds.darknesschat.Constants.FONT_BOLD_ITALIC_PATH;

public class EmojisView extends ScrollPane {
    public static final String EMOJIS_VIEW_ID = "emojisView";
    private final long userId;
    private final IOnEmojiSelect onEmojiSelect;

    public EmojisView(long userId, IOnEmojiSelect onEmojiSelect) {
        this.userId = userId;
        this.onEmojiSelect = onEmojiSelect;

        init();
    }

    private void init() {
        setId(EMOJIS_VIEW_ID);
        setMaxHeight(120d);

        VBox scrollPaneContent = initVboxContent();
        initTitle(scrollPaneContent);
        loadEmojis(scrollPaneContent);
    }

    private void loadEmojis(@NotNull VBox scrollPaneContent) {
        EmojisHBox emojisHBox = new EmojisHBox(EmojisParser.getAllEmojis(), onEmojiSelect, userId);
        scrollPaneContent.getChildren().add(emojisHBox);
    }

    private void initTitle(@NotNull VBox scrollPaneContent) {
        Label title = new Label("Emojis");
        title.setId("emojisTitle");
        title.setFont(Font.loadFont(Main.class.getResourceAsStream(FONT_BOLD_ITALIC_PATH), 18d));
        title.setTextFill(javafx.scene.paint.Color.WHITE);
        title.setUnderline(true);
        scrollPaneContent.getChildren().add(title);

        Animations.addFadeTransitionToNode(title, userId);
    }

    private @NotNull VBox initVboxContent() {
        VBox emojisScrollPaneContent = new VBox();
        emojisScrollPaneContent.setSpacing(19d);
        emojisScrollPaneContent.setPadding(new Insets(20d));

        setContent(emojisScrollPaneContent);

        return emojisScrollPaneContent;
    }
}
