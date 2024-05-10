package com.ds.darknesschat.database;

import com.ds.darknesschat.user.User;
import com.ds.darknesschat.utils.Utils;
import com.ds.darknesschat.utils.log.Log;
import org.jetbrains.annotations.NotNull;
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
                    USER_PASSWORD_ROW + "='" + Utils.encodeString(user.getUserPassword())+"'";

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

    public static @Nullable String getValue(String row, long userId){
        return getValueWithWhereValue(row, USER_ID_ROW, String.valueOf(userId));
    }

    public static boolean getBoolean(@NotNull String value){
        return value.equals("1") | value.equals("true") | value.equals("yes");
    }

    public static @Nullable String getValueWithWhereValue(String row, String whereRow, String whereValue){
        try {
            String select = "SELECT " + row + " FROM " + TABLE_NAME + " WHERE " + whereRow + "='" + whereValue + "'";

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

    public static void changeValue(String row, String newValue, long userId){
        try{
            String change = "UPDATE " + TABLE_NAME + " SET " + row + "=" + "'" + newValue + "'" + " WHERE " + USER_ID_ROW + "=" + "'" + userId + "'";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(change);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void setNull(String row, long userId){
        try {
            String changeCommand = "UPDATE " + TABLE_NAME + " SET " + row + " = null WHERE " + USER_ID_ROW + "= '" + userId + "'";
            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(changeCommand);
            int result = preparedStatement.executeUpdate();

            Log.info("Setting row " + row + " to null ended with result " + result);

            preparedStatement.close();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static void deleteUser(long userId){
        try{
            String delete = "DELETE FROM " + TABLE_NAME + " WHERE " + USER_ID_ROW + "='" + userId + "'";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(delete);
            int result = preparedStatement.executeUpdate();

            Log.info("User with id " + userId + " deleted with result " + result);

            preparedStatement.close();
        }catch (Exception e){
            Log.error(e);
        }
    }

    public static boolean addUser(User user){
        try {
            String insert = "INSERT INTO " + TABLE_NAME + "(" + USER_NAME_ROW + "," + USER_PASSWORD_ROW + "," + USER_DATE_OF_REGISTRATION_ROW + ")" + " VALUES(?,?,?)";

            PreparedStatement preparedStatement = Objects.requireNonNull(getConnection()).prepareStatement(insert);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, Utils.encodeString(user.getUserPassword()));
            preparedStatement.setString(3, user.getUserDateOfRegistration());
            preparedStatement.executeUpdate();

            preparedStatement.close();

            Log.info("Added user " + user);

            return true;
        }catch (Exception e){
            Log.error(e);
        }

        return false;
    }
}
