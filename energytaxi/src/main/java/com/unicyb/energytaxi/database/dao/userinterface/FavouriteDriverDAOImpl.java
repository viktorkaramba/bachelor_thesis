package com.unicyb.energytaxi.database.dao.userinterface;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.FavouriteDriver;
import com.unicyb.energytaxi.entities.userinterfaceenteties.FavouriteDriverUserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavouriteDriverDAOImpl implements DAO<FavouriteDriver> {

    @Override
    public boolean add(FavouriteDriver favouriteDriver) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_FAVOURITE_DRIVERS);
            preparedStatement.setInt(1, favouriteDriver.getDriverId());
            preparedStatement.setInt(2, favouriteDriver.getUserId());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    private FavouriteDriverUserInfo getFavouriteDriverUserInfo(ResultSet resultSet) throws SQLException {
        return new FavouriteDriverUserInfo(resultSet.getInt(1), resultSet.getInt(2),
                resultSet.getFloat(3), resultSet.getString(4), resultSet.getString(5),
                resultSet.getString(6), resultSet.getString(7));
    }

    private FavouriteDriver getFavouriteDriver(ResultSet resultSet) throws SQLException {
        return new FavouriteDriver(resultSet.getInt(1), resultSet.getInt(2),
                resultSet.getInt(3));
    }
    public List<FavouriteDriverUserInfo> getAllByUserId(int userId){
        List<FavouriteDriverUserInfo> favouriteDriverUserInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_FAVOURITE_DRIVERS_INFO_BY_USER_ID_AND_DRIVER_ID);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FavouriteDriverUserInfo favouriteDriverUserInfo = getFavouriteDriverUserInfo(resultSet);
                favouriteDriverUserInfoList.add(favouriteDriverUserInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favouriteDriverUserInfoList;
    }

    @Override
    public List<FavouriteDriver> getAll() {
        List<FavouriteDriver> favouriteDriverList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_FAVOURITE_DRIVERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FavouriteDriver favouriteDriver = getFavouriteDriver(resultSet);
                favouriteDriverList.add(favouriteDriver);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favouriteDriverList;
    }

    @Override
    public FavouriteDriver getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(FavouriteDriver favouriteDriver) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_FAVOURITE_DRIVERS);
            statement.setInt(1, favouriteDriver.getUserId());
            statement.setInt(2, favouriteDriver.getDriverId());
            statement.setInt(3, favouriteDriver.getFavouriteDriverId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_FAVOURITE_DRIVERS);
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

    public boolean deleteByUserId(int driverId, int userId){
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_FAVOURITE_DRIVERS_BY_DRIVER_ID_USER_ID);
            statement.setInt(1, driverId);
            statement.setInt(2, userId);
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
