package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.pages.ConnectToTheChatPage;
import com.ds.darknesschat.pages.Page;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

public class ChatTile extends HBox {
    private final double width, height;
    private final String text;
    private final Pane parent;
    private final Page page;
    public static final double DEFAULT_WIDTH = 300d;
    public static final double DEFAULT_HEIGHT = 54d;

    public ChatTile(double width, double height, String text, Pane parent, Page page) {
        this.width = width;
        this.height = height;
        this.text = text;
        this.parent = parent;
        this.page = page;
        
        init(page);
        animate();
        addAction(page);
    }

    private void addAction(@NotNull Page page) {
        Utils.addActionToNode(this, () -> {
            ConnectToTheChatPage connectToTheChatPage = new ConnectToTheChatPage(page, page.getContentVbox(), StringGetterWithCurrentLanguage.getString(StringsConstants.CONNECTING), true, page.getUser());
            connectToTheChatPage.open();

            connectToTheChatPage.setAddressValue(text);
        }, page.getUser().getId());
    }

    private void init(Page page) {
        try {
            getStyleClass().add("text-field-parent");
            setPrefSize(width, height);
            setAlignment(Pos.CENTER);

            createLabel();
            createClearButton(page);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createClearButton(Page page) {
        try {
            ImageButton deleteButton = new ImageButton(32d, 32d, Utils.getImage("bitmaps/icons/others/delete.png"), this::delete, page.getUser().getId());
            HBox.setMargin(deleteButton, new Insets(10d, 10d, 10d, 50d));

            getChildren().add(deleteButton);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void delete(){
        try {
            parent.getChildren().remove(this);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void createLabel() {
        try {
            Label label = new Label(text);
            label.setTextFill(Color.WHITE);
            label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 16));

            getChildren().add(label);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void animate(){
        Utils.addTranslateByUpAnimationToNode(this, false, page.getUser().getId());
    }
}
