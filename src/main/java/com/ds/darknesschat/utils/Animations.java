package com.ds.darknesschat.utils;

import com.ds.darknesschat.user.UserSettings;
import com.ds.darknesschat.utils.log.Log;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

public final class Animations {
    public static void addTranslateByUpAnimationToNode(Node node, boolean byUp, long userId){
        try {
            if(UserSettings.getUserAnimationsUsing(userId)) {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), node);
                translateTransition.setFromY(-200 * (byUp ? 1 : -1));
                translateTransition.setToY(0);
                translateTransition.setAutoReverse(true);
                translateTransition.setDelay(Duration.millis(30));
                translateTransition.play();
            }
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void addTranslateByLeftAnimationToNode(Node node, long userId){
        try {
            if(UserSettings.getUserAnimationsUsing(userId)) {
                TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), node);
                translateTransition.setFromX(-200);
                translateTransition.setToX(0);
                translateTransition.setAutoReverse(true);
                translateTransition.setDelay(Duration.millis(30));
                translateTransition.play();
            }
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void addFadeTransitionToNode(Node node, long userId){
        try{
            if(UserSettings.getUserAnimationsUsing(userId)) {
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(100), node);
                fadeTransition.setFromValue(0.1f);
                fadeTransition.setToValue(1f);
                fadeTransition.setAutoReverse(true);
                fadeTransition.play();
            }
        }catch (Exception e) {
            Log.error(e);
        }
    }

    public static void addRotateTranslationToNode(Node node, long userId){
        try {
            if(UserSettings.getUserAnimationsUsing(userId)) {
                RotateTransition rotateTransition = new RotateTransition(Duration.millis(100), node);
                rotateTransition.setFromAngle(0d);
                rotateTransition.setToAngle(360d);
                rotateTransition.setAutoReverse(true);
                rotateTransition.play();
            }
        }catch (Exception e) {
            Log.error(e);
        }
    }

    public static void addTextTypingAnimationToLabel(String text, Label label, long userId){
        try{
            if(UserSettings.getUserAnimationsUsing(userId)) {
                Animation animation = new Transition() {
                    {
                        setCycleDuration(Duration.millis(200d));
                    }

                    @Override
                    protected void interpolate(double v) {
                        label.setText(text.substring(0, (int) Math.round(text.length() * v)));
                    }
                };
                animation.setCycleCount(1);
                animation.play();
            }
        }catch (Exception e) {
            Log.error(e);
        }
    }
}
