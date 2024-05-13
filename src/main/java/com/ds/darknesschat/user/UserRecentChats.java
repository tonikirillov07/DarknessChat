package com.ds.darknesschat.user;

import com.ds.darknesschat.database.DatabaseConstants;
import com.ds.darknesschat.database.DatabaseService;
import com.ds.darknesschat.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ds.darknesschat.utils.Utils.isStringAreJSON;

public final class UserRecentChats {
    private static final String USER_RECENT_CHATS_ARRAY = "recent_chats_array";

    public static void deleteOneRecentChat(String address, long userId){
        JSONArray jsonArray = new JSONArray(getRecentChatsArrayList(userId));

        for (int i = 0; i < jsonArray.length(); i++) {
            if(jsonArray.getString(i).equals(address)){
                jsonArray.remove(i);
                break;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER_RECENT_CHATS_ARRAY, jsonArray);

        DatabaseService.changeValue(DatabaseConstants.USER_RECENT_CHATS_ROW, jsonObject.toString(), userId);
    }

    public static void clearRecentChats(long userId){
        DatabaseService.setNull(DatabaseConstants.USER_RECENT_CHATS_ROW, userId);
    }

    public static @NotNull List<String> getRecentChats(long userId){
        List<String> recentChatsList = new ArrayList<>();
        String currentRecord = DatabaseService.getValue(DatabaseConstants.USER_RECENT_CHATS_ROW, userId);

        if(currentRecord != null){
            if(Utils.isStringAreJSON(currentRecord)){
                Objects.requireNonNull(getRecentChatsArrayList(userId)).forEach(address -> recentChatsList.add(address.toString()));
            }
        }

        return recentChatsList;
    }

    private static @Nullable JSONArray getRecentChatsArrayList(long userId){
        if(isCurrentRecordIsJSON(userId)){
            JSONObject jsonObject = new JSONObject(getCurrentRecord(userId));

            return jsonObject.getJSONArray(USER_RECENT_CHATS_ARRAY);
        }

        return null;
    }

    public static void addUserRecentChat(long userId, String recentChatAddress){
        if (isCurrentRecordIsJSON(userId))
            addRecentChatRecordToExistsRecord(userId, recentChatAddress, getCurrentRecord(userId));
        else
            createRecentChatFirstRecord(userId, recentChatAddress);

    }

    private static void addRecentChatRecordToExistsRecord(long userId, String recentChatAddress, String currentRecord) {
        JSONObject jsonObject = new JSONObject(currentRecord);

        JSONArray jsonArray = jsonObject.getJSONArray(USER_RECENT_CHATS_ARRAY);
        jsonArray.put(recentChatAddress);

        DatabaseService.changeValue(DatabaseConstants.USER_RECENT_CHATS_ROW, jsonObject.toString(), userId);
    }

    private static void createRecentChatFirstRecord(long userId, String recentChatAddress) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(recentChatAddress);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER_RECENT_CHATS_ARRAY, jsonArray);

        DatabaseService.changeValue(DatabaseConstants.USER_RECENT_CHATS_ROW, jsonObject.toString(), userId);
    }

    private static String getCurrentRecord(long userId){
        return DatabaseService.getValue(DatabaseConstants.USER_RECENT_CHATS_ROW, userId);
    }

    private static boolean isCurrentRecordIsJSON(long userId){
        String currentRecord = getCurrentRecord(userId);
        boolean isJSON = false;

        if(currentRecord != null) {
            isJSON =  isStringAreJSON(currentRecord);
        }

        return isJSON;
    }
}
