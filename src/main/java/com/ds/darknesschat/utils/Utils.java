package com.ds.darknesschat.utils;

import com.ds.darknesschat.Main;
import com.ds.darknesschat.additionalNodes.AdditionalTextField;
import com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsManager;
import com.ds.darknesschat.utils.eventListeners.IOnAction;
import com.ds.darknesschat.utils.log.Log;
import com.ds.darknesschat.utils.sounds.Sounds;
import com.ds.darknesschat.utils.sounds.SoundsConstants;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.ds.darknesschat.Constants.MAX_PORT_VALUE;
import static com.ds.darknesschat.Constants.MIN_PORT_VALUE;
import static com.ds.darknesschat.utils.appSettings.outsideSettings.OutsideSettingsKeys.APP_LANGUAGE;

public final class Utils {
    @Contract("_ -> new")
    public static @NotNull Image getImage(String resourcePath){
        return new Image(Objects.requireNonNull(Main.class.getResourceAsStream(resourcePath)));
    }

    public static void addActionToNode(@NotNull Node node, IOnAction action, long userId){
        try {
            node.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    if (action != null) {
                        Sounds.playSound(SoundsConstants.CLICK_SOUND, userId);
                        action.onAction();
                    }else
                        Log.info("Action is null and could not be executed in node" + node);
                }
            });
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static boolean isFieldsIsNotEmpty(AdditionalTextField[] textFields){
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

    public static void changeCurrentLanguage(){
        try {
            List<String> allLanguages = List.of(Objects.requireNonNull(new File("languages").list((dir, name) -> name.endsWith(".properties"))));

            int currentLanguageId = allLanguages.indexOf(new File(Objects.requireNonNull(OutsideSettingsManager.getValue(APP_LANGUAGE))).getName());
            if ((currentLanguageId + 1) == allLanguages.size()) {
                currentLanguageId = 0;
            } else
                currentLanguageId++;

            OutsideSettingsManager.changeValue(APP_LANGUAGE, "languages/" + allLanguages.get(currentLanguageId));
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static @Nullable String getLocalIP4Address(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static String encodeString(@NotNull String input){
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    @Contract("_ -> new")
    public static @NotNull String decodeString(@NotNull String input){
        return new String(Base64.getDecoder().decode(input));
    }

    public static void copyStringToClipboard(String string){
        copyTransferable(new StringSelection(string));
        Log.info("String " + string + " copied in clipboard");
    }

    public static void copyImageToClipboard(BufferedImage bufferedImage){
        copyTransferable(new TransferableImage(bufferedImage));
        Log.info("Image " + bufferedImage + " copied in clipboard");
    }

    public static void copyTransferable(Transferable transferable){
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        systemClipboard.setContents(transferable, null);
    }

    public static void openFile(File file){
        try {
            if (Desktop.isDesktopSupported()) {
                if(file.exists())
                    Desktop.getDesktop().open(file);
            }else
                Log.error(new Exception("Desktop is not supported on this device"));
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static @NotNull String getFormattedDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static boolean isStringAreJSON(String string){
        try {
            new JSONObject(string);
            return true;
        }catch (JSONException e){
            return false;
        }
    }

    public static String getStringFromJSON(String json, String key){
        return new JSONObject(json).getString(key);
    }

    public static @NotNull String extractUserNameFromMessage(@NotNull String message){
        int colonIndex = message.indexOf(":");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < colonIndex + 1; i++) {
            stringBuilder.append(message.charAt(i));
        }

        return stringBuilder.toString();
    }

    public static @Nullable String extractMessageTextFromMessage(@NotNull String message){
        try {
            int colonIndex = message.indexOf(":");
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = colonIndex + 1; i < message.length(); i++) {
                stringBuilder.append(message.charAt(i));
            }

            return stringBuilder.toString();
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    @Contract("_ -> new")
    public static int @Nullable [] parseColorRGBFromString(String color){
        try {
            double red;
            double green;
            double blue;

            int commaIndex = color.indexOf(",");

            StringBuilder redStringBuilder = new StringBuilder();
            for (int redCharIndex = 0; redCharIndex < commaIndex; redCharIndex++) {
                redStringBuilder.append(color.charAt(redCharIndex));
            }

            StringBuilder secondPart = new StringBuilder();
            for (int i = commaIndex + 2; i < color.length(); i++) {
                secondPart.append(color.charAt(i));
            }

            String secondColorPart = secondPart.toString();

            StringBuilder greenStringBuilder = new StringBuilder();
            for (int blueCharIndex = 0; blueCharIndex < secondColorPart.indexOf(","); blueCharIndex++) {
                greenStringBuilder.append(secondColorPart.charAt(blueCharIndex));
            }

            StringBuilder blueStringBuilder = new StringBuilder();
            for (int blueCharIndex = secondColorPart.indexOf(",") + 2; blueCharIndex < secondColorPart.length(); blueCharIndex++) {
                blueStringBuilder.append(secondColorPart.charAt(blueCharIndex));
            }

            red = Double.parseDouble(redStringBuilder.toString());
            green = Double.parseDouble(greenStringBuilder.toString());
            blue = Double.parseDouble(blueStringBuilder.toString());

            return new int[]{(int) Math.round(red * 100), (int) Math.round(green * 100), (int) Math.round(blue * 100)};
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static boolean isPortNormal(int port){
        return port <= MAX_PORT_VALUE & port >= MIN_PORT_VALUE;
    }

    public static boolean stringCanBeConvertedToInt(String string){
        try{
            Integer.parseInt(string);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static void clearPaneInHalf(@NotNull Pane pane){
        int stopIndex = Math.floorDiv(pane.getChildren().size(), 2);

        for (int i = 0; i < stopIndex; i++) {
            pane.getChildren().remove(i);
        }
    }

    @Contract(pure = true)
    public static boolean stringMeetsAtLeastOneRegex(String @NotNull [] regexes, String string){
        boolean isMeet = false;

        for (String regex : regexes) {
            if(string.matches(regex)){
                isMeet = true;
                break;
            }
        }

        return isMeet;
    }

    public static @Nullable ChatAddress extractPortAndAddressFromAddress(@NotNull String address){
        try {
            int colonIndex = address.indexOf(":") + 1;
            StringBuilder portBuilder = new StringBuilder();
            StringBuilder addressBuilder = new StringBuilder();

            for (int i = colonIndex; i < address.length(); i++) {
                portBuilder.append(address.charAt(i));
            }

            for (int i = 0; i < colonIndex - 1; i++) {
                addressBuilder.append(address.charAt(i));
            }

            int port = Integer.parseInt(portBuilder.toString());
            String parsedAddress = addressBuilder.toString();

            return new ChatAddress(parsedAddress, port);
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static @NotNull String getFileFormat(@NotNull String fileName){
        int dotIndex = fileName.indexOf(".");
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = dotIndex + 1; i < fileName.length(); i++) {
            stringBuilder.append(fileName.charAt(i));
        }

        return stringBuilder.toString();
    }

    public static @NotNull String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static void onWindowClose(){
        Log.info("Windows closed");
    }

    public static double convertBytesToKiloBytes(int byteValue){
        return Double.parseDouble(new DecimalFormat("#.###").format(byteValue / 1024));
    }

    public static double convertBytesToMegaBytes(int byteValue){
        return Double.parseDouble(new DecimalFormat("#.###").format(byteValue * Math.pow(10, -6)));
    }
}
