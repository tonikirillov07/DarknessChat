package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageButton extends ImageView {
    private final double width, height;
    private final Image imageSource;
    private IOnAction onAction;

    public ImageButton(double width, double height, Image imageSource) {
        this.width = width;
        this.height = height;
        this.imageSource = imageSource;

        init();
    }

    public ImageButton(double width, double height, Image imageSource, IOnAction onAction) {
        this.width = width;
        this.height = height;
        this.imageSource = imageSource;
        this.onAction = onAction;

        init();
    }

    public void setOnAction(IOnAction onAction){
        Utils.addActionToNode(this, onAction);
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
                Utils.addActionToNode(this, onAction);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
