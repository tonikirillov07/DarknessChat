package com.ds.darknesschat.database;

import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ds.darknesschat.database.DatabaseConstants.*;

public class DatabaseService {
    private static @Nullable Connection getConnection(){
        try {
            return DriverManager.getConnection("jdbc:sqlite:database/users.db");
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public String getValue(){
        return null;
    }

    public static void addUser(User user){
        try {
            String insert = "INSERT INTO " + DatabaseConstants.TABLE_NAME + "(" + USER_NAME_ROW + "," + USER_PASSWORD_ROW + "," + USER_DATE_OF_REGISTRATION_ROW + ")" + " VALUES(?,?,?)";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(insert);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, Utils.encodeString(user.getUserPassword()));
            preparedStatement.setString(3, user.getUserDateOfRegistration());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        }catch (Exception e){
            Log.error(e);
        }
    }
}
