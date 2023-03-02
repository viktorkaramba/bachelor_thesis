package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.documents.FullName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FullNameDAOImpl implements DAO<FullName> {

    @Override
    public boolean add(FullName fullName) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_FULL_NAME);
            preparedStatement.setString(1, fullName.getFirstName());
            preparedStatement.setString(2, fullName.getSurName());
            preparedStatement.setString(3, fullName.getPatronymic());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FullName> getAll() {
        List<FullName> fullNameList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_FULL_NAME);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FullName fullName = getFullName(resultSet);
                fullNameList.add(fullName);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fullNameList;
    }

    private FullName getFullName(ResultSet resultSet) throws SQLException {
        return new FullName(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4));
    }


    @Override
    public FullName getOne(int ID) {
        FullName fullName = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_FULL_NAME_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                fullName = getFullName(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return fullName;
    }

    @Override
    public boolean update(FullName fullName) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_FULL_NAME);
            statement.setString(1, fullName.getFirstName());
            statement.setString(2, fullName.getSurName());
            statement.setString(3, fullName.getPatronymic());
            statement.setInt(4, fullName.getFullNameId());
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int ID) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_FULL_NAME);
            statement.setInt(1, ID);
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public int getIdByFullName(FullName fullName){
        int result = 0;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ID_BY_FULLNAME);
            statement.setString(1, fullName.getFirstName());
            statement.setString(2, fullName.getSurName());
            statement.setString(3, fullName.getPatronymic());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result = resultSet.getInt(1);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return result;
    }
}