package com.ds.darknesschat;

import com.ds.darknesschat.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Constants.LOGS_PATH);
        System.out.println(Utils.convertBytesToMegaBytes(fileInputStream.readAllBytes().length));
    }

}
