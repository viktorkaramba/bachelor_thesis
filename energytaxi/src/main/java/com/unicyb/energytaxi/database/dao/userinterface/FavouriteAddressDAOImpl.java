package com.unicyb.energytaxi.database.dao.userinterface;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.FavouriteAddress;
import com.unicyb.energytaxi.entities.userinterfaceenteties.FavouriteAddressUserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavouriteAddressDAOImpl implements DAO<FavouriteAddress> {

    @Override
    public boolean add(FavouriteAddress favouriteAddress) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_FAVOURITE_ADDRESSES);
            preparedStatement.setInt(1, favouriteAddress.getUserId());
            preparedStatement.setString(2, favouriteAddress.getAddress());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private FavouriteAddress getFavouriteAddress(ResultSet resultSet) throws SQLException {
        return new FavouriteAddress(resultSet.getInt(1), resultSet.getInt(2),
                resultSet.getString(3));
    }

    private FavouriteAddressUserInfo getFavouriteAddressUserInfo(ResultSet resultSet) throws SQLException {
        return new FavouriteAddressUserInfo(resultSet.getInt(1),
                resultSet.getString(2), resultSet.getInt(3));
    }

    public List<FavouriteAddressUserInfo> getAllByUserId(int userId){
        List<FavouriteAddressUserInfo> favouriteAddressUserInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_FAVOURITE_ADDRESSES_INFO_BY_USER_ID);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FavouriteAddressUserInfo favouriteAddressUserInfo = getFavouriteAddressUserInfo(resultSet);
                favouriteAddressUserInfoList.add(favouriteAddressUserInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favouriteAddressUserInfoList;
    }

    @Override
    public List<FavouriteAddress> getAll() {
        List<FavouriteAddress> favouriteAddressList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_FAVOURITE_ADDRESSES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                FavouriteAddress favouriteAddress = getFavouriteAddress(resultSet);
                favouriteAddressList.add(favouriteAddress);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favouriteAddressList;
    }

    @Override
    public FavouriteAddress getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(FavouriteAddress favouriteAddress) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_FAVOURITE_ADDRESSES);
            statement.setInt(1, favouriteAddress.getUserId());
            statement.setString(2, favouriteAddress.getAddress());
            statement.setInt(3, favouriteAddress.getFavouriteAddressId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_FAVOURITE_ADDRESS);
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

    public boolean deleteByUserIdAndAddress(int userId, String address) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_FAVOURITE_ADDRESS_BY_USER_ID_ADDRESS);
            statement.setInt(1, userId);
            statement.setString(2, address);
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
