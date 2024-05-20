package com.ds.darknesschat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println(new File("emojis/emojis.json").exists());
    }

}
