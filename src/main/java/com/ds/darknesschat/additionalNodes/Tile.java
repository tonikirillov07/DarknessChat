package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.user.User;
import com.ds.darknesschat.user.UserSettings;
import com.ds.darknesschat.utils.Color;
import com.ds.darknesschat.utils.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.jetbrains.annotations.NotNull;

import static com.ds.darknesschat.Constants.TILE_COLOR;

public class Tile extends VBox {
    private final double width, height;
    public static final double DEFAULT_WIDTH = 606d;
    public static final double DEFAULT_HEIGHT = 645d;
    private final float alpha;

    public Tile(double width, double height, float alpha) {
        this.width = width;
        this.height = height;
        this.alpha = alpha;

        init();
    }

    private void init() {
        setWidth(width);
        setHeight(height);
        setMaxWidth(width);
        getStyleClass().add("tile");
        setAlignment(Pos.TOP_CENTER);
        setEffect(new DropShadow());

        setColor(new Color(alpha, TILE_COLOR.getRed(), TILE_COLOR.getGreen(), TILE_COLOR.getBlue()));
    }

    public void setColor(@NotNull Color color){
        setStyle(getStyle() + "-fx-background-color: " + color.generateCssStyle());
    }

    public void addTitle(String title){
        Label label = new Label(title);
        label.getStyleClass().add("header-label");
        label.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 32));
        VBox.setMargin(label, new Insets(15, 0, 0,0));

        addChild(label);
    }

    public void applyAlphaWithUserSettings(User user){
        setColor(new com.ds.darknesschat.utils.Color(UserSettings.getUserAlphaLevel(user), TILE_COLOR.getRed(), TILE_COLOR.getGreen(), TILE_COLOR.getBlue()));
    }

    public void animate(){
        Utils.addTranslateByUpAnimationToNode(this, true);
    }

    public void addChild(Node node){
        getChildren().add(node);
    }
}
