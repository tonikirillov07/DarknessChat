package com.ds.darknesschat;

import com.ds.darknesschat.utils.Utils;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Test {
    public static void main(String[] args) throws IOException {
        List<String> allHexColors = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("settings/nicknames_colors.txt"))));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            allHexColors.add(line);
        }

        System.out.println(allHexColors.size());
    }

}
