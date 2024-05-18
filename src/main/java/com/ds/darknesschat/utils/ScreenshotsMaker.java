package com.ds.darknesschat.utils;

import com.ds.darknesschat.utils.languages.StringGetterWithCurrentLanguage;
import com.ds.darknesschat.utils.languages.StringsConstants;
import com.ds.darknesschat.utils.log.Log;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ds.darknesschat.Constants.SCREENSHOTS_FOLDER;

public class ScreenshotsMaker {
    public static void createScreenshot(@NotNull Stage stage){
        try {
            Log.info("Preparing to create screenshot...");

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            Bounds bounds = stage.getScene().getRoot().getLayoutBounds();

            snapshotParameters.setViewport(new Rectangle2D(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
            WritableImage writableImage = stage.getScene().getRoot().snapshot(snapshotParameters, null);

            File screenshotsFolder = new File(SCREENSHOTS_FOLDER);
            if(!screenshotsFolder.exists())
                Log.info("Created folder " + screenshotsFolder + " with result " + screenshotsFolder.mkdir());

            File file = new File(generateFileName());
            try {
                ImageIO.write(Objects.requireNonNull(convert(writableImage)), "png", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Log.info("Screenshot saved to " + file);
            NotificationsSender.send(StringGetterWithCurrentLanguage.getString(StringsConstants.SCREENSHOTS_MAKER_NOTIFICATION_CAPTION), StringGetterWithCurrentLanguage.getString(StringsConstants.SCREENSHOTS_MAKER_NOTIFICATION_SCREENSHOT_SAVED_TO_TEXT) + " " + file + "." + StringGetterWithCurrentLanguage.getString(StringsConstants.SCREENSHOTS_MAKER_NOTIFICATION_CLICK_HERE_TO_OPEN_TEXT), TrayIcon.MessageType.INFO, () -> Utils.openFile(file));
        }catch (Exception e){
            Log.error(e);
        }
    }

    private static @NotNull String generateFileName() {
        LocalDateTime localDateTime = LocalDateTime.now();

        return SCREENSHOTS_FOLDER + "/screenshot_" + localDateTime.getYear() + "_" + localDateTime.getDayOfMonth() + "_" +
                localDateTime.getMonth() + "_" + localDateTime.getHour() + "_" + localDateTime.getMinute() + "_" + localDateTime.getSecond() + ".png";
    }

    private static @Nullable BufferedImage convert(@NotNull Image fxImage) {
        try {
            int width = (int) Math.ceil(fxImage.getWidth());
            int height = (int) Math.ceil(fxImage.getHeight());

            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);

            int[] buffer = new int[width];

            PixelReader reader = fxImage.getPixelReader();
            WritablePixelFormat<IntBuffer> format =
                    PixelFormat.getIntArgbInstance();
            for (int y = 0; y < height; y++) {
                reader.getPixels(0, y, width, 1, format, buffer, 0, width);
                image.getRaster().setDataElements(0, y, width, 1, buffer);
            }

            return image;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
