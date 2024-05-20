package com.ds.darknesschat.chat.messages.emojis;

import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class EmojisParser {
    public static @Nullable List<EmojiInfo> getAllEmojis(){
        try {
            List<EmojiInfo> emojiInfoList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(readJSON(new File("emojis/emojis.json"))));
            JSONArray jsonArray = jsonObject.getJSONArray(EmojisJSONKeys.ALL_EMOJIS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject emojiJSONObject = jsonArray.getJSONObject(i);
                emojiInfoList.add(new EmojiInfo(emojiJSONObject.getString(EmojisJSONKeys.EMOJI_NAME), new File(emojiJSONObject.getString(EmojisJSONKeys.EMOJI_PATH))));
            }

            return emojiInfoList;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    private static @Nullable String readJSON(File file){
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder stringBuilder = new StringBuilder();

            while(scanner.hasNext()){
                stringBuilder.append(scanner.nextLine()).append("\n");
            }

            return stringBuilder.toString();
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }
}
