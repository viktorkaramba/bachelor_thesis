package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.documents.Driver;
import com.unicyb.minitaxi.entities.indicators.STATUS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements DAO<Driver> {

    @Override
    public boolean add(Driver driver) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_DRIVER);
            preparedStatement.setFloat(1, driver.getDriverId());
            preparedStatement.setInt(2, driver.getFullNameId());
            preparedStatement.setString(3, driver.getTelephoneNumber());
            preparedStatement.setFloat(4, driver.getExperience());
            preparedStatement.setFloat(5, driver.getSalary());
            preparedStatement.setTimestamp(6, driver.getDate());
            preparedStatement.setString(7, driver.getResumeStatus().name());
            preparedStatement.setString(8, driver.getDriverUserName());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Driver> getAll() {
        List<Driver> driverList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_DRIVER);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Driver driver = getDriver(resultSet);
                driverList.add(driver);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverList;
    }

    private Driver getDriver(ResultSet resultSet) throws SQLException {
        return new Driver(resultSet.getInt(1),  resultSet.getTimestamp(7), resultSet.getInt(2),
                resultSet.getInt(3), resultSet.getString(4), resultSet.getInt(5),
                resultSet.getFloat(6), STATUS.valueOf(resultSet.getString(8)),
                resultSet.getString(9));
    }

    @Override
    public Driver getOne(int ID) {
        Driver driver = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                driver = getDriver(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return driver;
    }


    @Override
    public boolean update(Driver driver) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_DRIVER);
            statement.setInt(1, driver.getCarId());
            statement.setInt(2, driver.getFullNameId());
            statement.setString(3, driver.getTelephoneNumber());
            statement.setFloat(4, driver.getExperience());
            statement.setFloat(5, driver.getSalary());
            statement.setTimestamp(6, driver.getDate());
            statement.setString(7, driver.getResumeStatus().name());
            statement.setString(8, driver.getDriverUserName());
            statement.setFloat(9, driver.getDriverId());
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateByUsername(String oldUserName, String newUserName) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_DRIVER_BY_USERNAME);
            statement.setString(1, newUserName);
            statement.setString(2, oldUserName);
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_DRIVER);
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

    public Driver getByDriverUserName(String username){
        Driver driver = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_BY_USER_NAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                driver = getDriver(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return driver;
    }
}
