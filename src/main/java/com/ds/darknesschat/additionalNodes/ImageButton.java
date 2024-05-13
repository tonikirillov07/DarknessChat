package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends ImageView {
    private final double width, height;
    private final Image imageSource;
    private IOnAction onAction;
    private final long userId;

    public ImageButton(double width, double height, Image imageSource, long userId) {
        this.width = width;
        this.height = height;
        this.imageSource = imageSource;
        this.userId = userId;

        init();
    }

    public ImageButton(double width, double height, Image imageSource, IOnAction onAction, long userId) {
        this.width = width;
        this.height = height;
        this.imageSource = imageSource;
        this.onAction = onAction;
        this.userId = userId;

        init();
    }

    public void setOnAction(IOnAction onAction){
        Utils.addActionToNode(this, onAction, userId);
    }

    private void init() {
        try{
            setImage(imageSource);
            setFitWidth(width);
            setFitHeight(height);
            setCursor(Cursor.HAND);
            setEffect(new DropShadow());
            getStyleClass().add("image-button");

            if(onAction != null)
                Utils.addActionToNode(this, onAction, userId);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
