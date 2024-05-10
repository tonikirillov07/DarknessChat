package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class SettingsOptionButton extends SettingsOption {
    public static final double DEFAULT_IMAGE_FIT_WIDTH = 32d;
    public static final double DEFAULT_IMAGE_FIT_HEIGHT = 32d;
    private final long userId;

    public SettingsOptionButton(double width, double height, String text, Image iconSource, double fitWidth, double fitHeight, long userId) {
        super(width, height);
        this.userId = userId;

        createLabel(text, Color.WHITE, Pos.CENTER_LEFT);
        createImage(iconSource, fitWidth, fitHeight);
    }

    public void createImage(Image iconSource, double fitWidth, double fitHeight) {
        try {
            ImageView iconImageView = new ImageView(iconSource);
            iconImageView.setFitHeight(fitWidth);
            iconImageView.setFitWidth(fitHeight);
            iconImageView.setEffect(new DropShadow());
            HBox.setMargin(iconImageView, new Insets(10d));

            HBox imageHbox = new HBox();
            HBox.setHgrow(imageHbox, Priority.ALWAYS);
            imageHbox.setAlignment(Pos.CENTER_RIGHT);
            imageHbox.getChildren().add(iconImageView);

            add(imageHbox);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void setOnAction(IOnAction onAction){
        Utils.addActionToNode(this, onAction, userId);
    }
}
