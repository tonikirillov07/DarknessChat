package com.ds.darknesschat.database;

import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public static boolean isUserExists(User user){
        try {
            String select = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_NAME_ROW + "='" + user.getUserName() + "' AND " +
                    USER_PASSWORD_ROW + "='" + user.getUserPassword()+"'";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean isExists = resultSet.next();

            preparedStatement.close();
            resultSet.close();

            Log.info("User " + user + " is exists: " + isExists);

            return isExists;
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }

    public static @Nullable String getValue(String row, int id){
        try {
            String select = "SELECT " + row + " FROM " + TABLE_NAME + " WHERE " + USER_ID_ROW + "=" + id;

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(select);
            ResultSet resultSet = preparedStatement.executeQuery();
            String returnValue = resultSet.getString(1);

            Log.info("Defined value in row " + row + " is " + returnValue);

            preparedStatement.close();
            resultSet.close();

            return returnValue;
        }catch (Exception e){
            Log.error(e);
        }

        return null;
    }

    public static void addUser(User user){
        try {
            String insert = "INSERT INTO " + TABLE_NAME + "(" + USER_NAME_ROW + "," + USER_PASSWORD_ROW + "," + USER_DATE_OF_REGISTRATION_ROW + ")" + " VALUES(?,?,?)";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(insert);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, Utils.encodeString(user.getUserPassword()));
            preparedStatement.setString(3, user.getUserDateOfRegistration());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            Log.info("Added user " + user);
        }catch (Exception e){
            Log.error(e);
        }
    }
}
