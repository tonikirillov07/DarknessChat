package com.ds.darknesschat.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PasswordGenerator {
    public static @NotNull String generate(int length){
        String alphabet = "qwertyuiopasdfghjklzxcvbnm1234567890-=/*QWERTYUIOPASDFGHJKLZXCVBNM?";
        List<Character> characterList = new ArrayList<>();

        for (Character c : alphabet.toCharArray()) {
            characterList.add(c);
        }

        StringBuilder stringBuilder = new StringBuilder();
        Collections.shuffle(characterList);

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(characterList.get(random.nextInt(characterList.size())));
        }

        return stringBuilder.toString();
    }
}
