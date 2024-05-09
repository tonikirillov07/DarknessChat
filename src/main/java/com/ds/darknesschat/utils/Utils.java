package com.ds.darknesschat.utils;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;
import com.ds.darknesschat.utils.interfaces.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys.APP_LANGUAGE;

public final class Utils {
    @Contract("_ -> new")
    public static @NotNull Image getImage(String resourcePath){
        return new Image(Objects.requireNonNull(Main.class.getResourceAsStream(resourcePath)));
    }

    public static void addActionToNode(@NotNull Node node, IOnAction action){
        try {
            node.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (action != null) {
                        Sounds.playSound(SoundsConstants.CLICK_SOUND);
                        action.onAction();
                    }else
                        Log.info("Action is null and could not be executed in node" + node);
                }
            });
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void addTranslateByUpAnimationToNode(Node node, boolean byUp){
        try {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), node);
            translateTransition.setFromY(-200 * (byUp ? 1 : -1));
            translateTransition.setToY(0);
            translateTransition.setAutoReverse(true);
            translateTransition.setDelay(Duration.millis(30));
            translateTransition.play();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void addTranslateByLeftAnimationToNode(Node node){
        try {
            TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), node);
            translateTransition.setFromX(-200);
            translateTransition.setToX(0);
            translateTransition.setAutoReverse(true);
            translateTransition.setDelay(Duration.millis(30));
            translateTransition.play();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static boolean isFieldIsNotEmpty(AdditionalTextField[] textFields){
        try {
            List<AdditionalTextField> additionalTextFields = new ArrayList<>(Arrays.stream(textFields).toList());
            additionalTextFields.removeIf(Objects::isNull);

            return additionalTextFields.stream().noneMatch(predicate -> predicate.getText().trim().isEmpty());
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }

    public static @Nullable List<AdditionalTextField> getEmptyFieldsFromArray(AdditionalTextField[] textFields){
        try {
            List<AdditionalTextField> textInputControlList = new ArrayList<>();

            Arrays.stream(textFields).toList().forEach(textInputControl -> {
                if (textInputControl == null)
                    return;

                if (textInputControl.getText().trim().isEmpty())
                    textInputControlList.add(textInputControl);
            });

            return textInputControlList;
        }catch (Exception e) {
            Log.error(e);
        }

        return null;
    }

    public static void addRotateTranslationToNode(Node node){
        try {
            RotateTransition rotateTransition = new RotateTransition(Duration.millis(100), node);
            rotateTransition.setFromAngle(0d);
            rotateTransition.setToAngle(360d);
            rotateTransition.setAutoReverse(true);
            rotateTransition.play();
        }catch (Exception e) {
            Log.error(e);
        }
    }

    public static void changeCurrentLanguage(){
        List<String> allLanguages = List.of(Objects.requireNonNull(new File("languages").list((dir, name) -> name.endsWith(".properties"))));

        int currentLanguageId = allLanguages.indexOf(new File(Objects.requireNonNull(OutsideSettingsManager.getValue(APP_LANGUAGE))).getName());
        if((currentLanguageId + 1) == allLanguages.size()){
            currentLanguageId = 0;
        }else
            currentLanguageId++;

        OutsideSettingsManager.changeValue(APP_LANGUAGE, "languages/" + allLanguages.get(currentLanguageId));
    }
}
