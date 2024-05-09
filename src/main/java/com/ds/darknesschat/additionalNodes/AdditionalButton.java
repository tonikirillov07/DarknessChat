package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;

public class AdditionalButton extends Button {
    private final double width;
    private final double height;
    private final com.ds.darknesschat.utils.Color textColor;
    private final com.ds.darknesschat.utils.Color backgroundColor;

    public AdditionalButton(String s, double width, double height, com.ds.darknesschat.utils.Color color, com.ds.darknesschat.utils.Color textColor) {
        super(s);
        this.width = width;
        this.height = height;
        this.backgroundColor = color;
        this.textColor = textColor;

        init();
    }

    private void init() {
        try{
            setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 20));
            setPrefSize(width, height);
            getStyleClass().add("button-next");
            setEffect(new DropShadow());

            setStyle("-fx-background-color: rgb(" + backgroundColor.red() + ", " + backgroundColor.green() + ", " + backgroundColor.blue() + ");" +
                    "-fx-text-fill: rgb(" + textColor.red() + ", " + textColor.green() + ", " + textColor.blue() + ");");
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void addAction(IOnAction onAction){
        setOnAction(actionEvent -> {
            Sounds.playSound(SoundsConstants.CLICK_SOUND);
            onAction.onAction();
        });
    }
}
