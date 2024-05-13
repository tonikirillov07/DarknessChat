package com.ds.darknesschat;

import com.ds.darknesschat.utils.Utils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        Utils.copyImageToClipboard(ImageIO.read(new File(Constants.ATTACHMENTS_FOLDER + "/image1.png")));
    }

}
