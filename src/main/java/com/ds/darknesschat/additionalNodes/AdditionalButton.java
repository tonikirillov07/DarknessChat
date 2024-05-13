package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

public class AdditionalButton extends Button {
    private final double width;
    private final double height;
    private final com.ds.darknesschat.utils.Color textColor;
    private final com.ds.darknesschat.utils.Color backgroundColor;
    private long userId;

    public AdditionalButton(String s, double width, double height, com.ds.darknesschat.utils.Color color, com.ds.darknesschat.utils.Color textColor) {
        super(s);
        this.width = width;
        this.height = height;
        this.backgroundColor = color;
        this.textColor = textColor;

        init();
    }

    public AdditionalButton(String s, double width, double height, com.ds.darknesschat.utils.Color backgroundColor, com.ds.darknesschat.utils.Color textColor, long userId) {
        super(s);
        this.userId = userId;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.height = height;
        this.width = width;

        init();
    }

    private void init() {
        try{
            setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 20));
            setPrefSize(width, height);
            setMinSize(width, height);
            getStyleClass().add("button-next");
            setEffect(new DropShadow());

            setBackgroundColor(backgroundColor);
            setTextColor(textColor);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void setBackgroundColor(@NotNull Color color){
        setStyle(getStyle() + "-fx-background-color: " + color.generateCssStyle());
    }

    public void setTextColor(@NotNull Color color){
        setStyle(getStyle() + "-fx-text-fill: " + color.generateCssStyle());
    }

    public void addAction(IOnAction onAction){
        setOnAction(actionEvent -> {
            Sounds.playSound(SoundsConstants.CLICK_SOUND, userId);
            onAction.onAction();
        });
    }
}
