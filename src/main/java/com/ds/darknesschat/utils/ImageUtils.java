package com.ds.darknesschat.utils;

import com.ds.darknesschat.Constants;
import com.ds.darknesschat.utils.log.Log;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import static com.ds.darknesschat.Constants.ATTACHMENT_PNG;

public final class ImageUtils {
    public static byte @Nullable [] convertImageFileToBytes(File file){
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] convertedToBytesImage = fileInputStream.readAllBytes();

            fileInputStream.close();

            return convertedToBytesImage;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static @Nullable ImageInfo getImageFromBytes(byte[] image){
        try {
            File folder = new File(Constants.ATTACHMENTS_FOLDER);
            if (!folder.exists())
                folder.mkdir();

            String path = Constants.ATTACHMENTS_FOLDER + "/" + ATTACHMENT_PNG;

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(image);
            fileOutputStream.close();

            return new ImageInfo(path, new Image(new FileInputStream(path)), image);
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static @Nullable File compressImage(File image, String format){
        try{
            BufferedImage bufferedImage = ImageIO.read(image);

            File compressedImageFile = new File(Constants.ATTACHMENTS_FOLDER + "/compressed." + format);
            OutputStream outputStream = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> list = ImageIO.getImageWritersByFormatName(format);
            ImageWriter obj = list.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            obj.setOutput(ios);

            ImageWriteParam param = obj.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0f);
            obj.write(null, new IIOImage(bufferedImage, null, null), param);

            outputStream.close();
            ios.close();
            obj.dispose();

            return compressedImageFile;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static @Nullable BufferedImage convertByteArrayToBufferedImage(byte[] bytes) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(byteArrayInputStream);
            byteArrayInputStream.close();

            return image;
        } catch (Exception e) {
            Log.error(e);
            return null;
        }
    }
}
