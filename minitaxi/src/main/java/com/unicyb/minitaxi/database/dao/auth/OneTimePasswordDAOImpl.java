package com.unicyb.minitaxi.database.dao.auth;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.auth.OneTimePassword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OneTimePasswordDAOImpl implements DAO<OneTimePassword> {
    @Override
    public boolean add(OneTimePassword oneTimePassword) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_OTP);
            preparedStatement.setString(1, oneTimePassword.getUserName());
            preparedStatement.setString(2, oneTimePassword.getOtPassword());
            preparedStatement.setTimestamp(3, oneTimePassword.getOtpDate());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OneTimePassword> getAll() {
        List<OneTimePassword> oneTimePasswordList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_OTP);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                OneTimePassword oneTimePassword = getOneTimePassword(resultSet);
                oneTimePasswordList.add(oneTimePassword);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return oneTimePasswordList;
    }

    private OneTimePassword getOneTimePassword(ResultSet resultSet) throws SQLException {
        return new OneTimePassword(resultSet.getInt(1), resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getTimestamp(4));
    }

    @Override
    public OneTimePassword getOne(int ID) {
        return null;
    }

    public OneTimePassword getOneByUserName(String username) {
        OneTimePassword oneTimePassword = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_OTP_BY_USERNAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                oneTimePassword =  getOneTimePassword(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return oneTimePassword;
    }

    @Override
    public boolean update(OneTimePassword oneTimePassword) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }

    public boolean deleteByUserName(String username){
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_OTP_BY_USERNAME);
            statement.setString(1, username);
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }
}
