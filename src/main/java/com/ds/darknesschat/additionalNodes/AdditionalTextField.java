package com.ds.darknesschat.additionalNodes;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.Main;
import com.ds.darknesschat.utils.InputTypes;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.interfaces.IOnTextTyping;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdditionalTextField extends HBox {
    private final double width, height;
    private final String placeholder;
    private String defaultValue;
    private TextInputControl textField;
    private final Image fieldIcon;
    private final boolean isPasswordField;
    public static final double DEFAULT_WIDTH = 340d;
    public static final double DEFAULT_HEIGHT = 49d;
    private IOnTextTyping onTextTyping;

    public AdditionalTextField(double width, double height, String placeholder, Image fieldIcon, boolean isPasswordField) {
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
        this.fieldIcon = fieldIcon;
        this.isPasswordField = isPasswordField;

        init();
    }

    public AdditionalTextField(double width, double height, String placeholder, String defaultValue, Image fieldIcon, boolean isPasswordField) {
        this.width = width;
        this.height = height;
        this.placeholder = placeholder;
        this.defaultValue = defaultValue;
        this.fieldIcon = fieldIcon;
        this.isPasswordField = isPasswordField;

        init();
    }

    public void setError(long userId){
        try {
            addShadowEffect(Color.RED);

            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), this);
            translateTransition.setFromX(0);
            translateTransition.setByX(25);
            translateTransition.setCycleCount(2);
            translateTransition.setAutoReverse(true);
            translateTransition.setOnFinished(actionEvent -> addShadowEffect(Color.BLACK));
            translateTransition.play();

            if(userId == -1)
                Sounds.playWithIgnoreUserSettings(SoundsConstants.ERROR_SOUND);
            else
                Sounds.playSound(SoundsConstants.ERROR_SOUND, userId);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public void addOnTextTyping(IOnTextTyping onTextTyping){
        this.onTextTyping = onTextTyping;
    }

    public void addOnEnterKeyPressed(IOnAction onAction){
        setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                onAction.onAction();
        });
    }

    public void setInputType(InputTypes inputType){
        if(inputType == InputTypes.NUMERIC) {
            getTextField().setOnKeyTyped(keyEvent -> {
                if(!getCharactersList().stream().allMatch(Character::isDigit)){
                    removeAllButDigits();
                }

                if(onTextTyping != null)
                    onTextTyping.onTextTyping(getText());
            });
        }
    }

    private void removeAllButDigits() {
        List<Character> characterList = getCharactersList();
        characterList.removeIf(predicate -> !Character.isDigit(predicate));

        StringBuilder stringBuilder = new StringBuilder();
        characterList.forEach(stringBuilder::append);

        getTextField().setText(stringBuilder.toString());
    }

    private @NotNull List<Character> getCharactersList(){
        List<Character> characterList = new ArrayList<>();
        for (char character : getText().toCharArray()) {
            characterList.add(character);
        }

        return characterList;
    }

    private void addShadowEffect(Color color){
        try {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(color);
            setEffect(dropShadow);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void init() {
        try {
            setPrefSize(width, height);
            setAlignment(Pos.CENTER);
            getStyleClass().add("text-field-parent");
            addShadowEffect(Color.BLACK);

            addTextField();
            addFieldIcon();
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void addFieldIcon() {
        try{
            ImageView imageView = new ImageView();
            imageView.setImage(fieldIcon);
            imageView.setFitWidth(32);
            imageView.setFitHeight(32);
            HBox.setMargin(imageView, new Insets(10, 10, 10, 10));

            getChildren().add(imageView);
        }catch (Exception e){
            Log.error(e);
        }
    }

    private void addTextField() {
        try{
            textField = isPasswordField ? new PasswordField() : new TextField();
            textField.setPromptText(placeholder);
            textField.setPrefHeight(height / 1.16d);
            textField.setFocusTraversable(false);
            textField.setFont(Font.loadFont(Main.class.getResourceAsStream(Constants.FONT_BOLD_ITALIC_PATH), 16));
            HBox.setHgrow(textField, Priority.ALWAYS);

            if(defaultValue != null)
                if (!defaultValue.isEmpty())
                    textField.setText(defaultValue);

            getChildren().add(textField);
        }catch (Exception e){
            Log.error(e);
        }
    }

    public String getText(){
        return textField.getText().trim();
    }

    public TextInputControl getTextField() {
        return textField;
    }
}
