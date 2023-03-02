package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.documents.DriverLocation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverLocationDAOImpl implements DAO<DriverLocation> {


    @Override
    public boolean add(DriverLocation driverLocation) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_DRIVER_LOCATION);
            preparedStatement.setInt(1, driverLocation.getDlId());
            preparedStatement.setInt(2, driverLocation.getDriverId());
            preparedStatement.setString(3, driverLocation.getLatitude());
            preparedStatement.setString(4, driverLocation.getLongitude());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DriverLocation> getAll() {
        List<DriverLocation> driverLocationList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_LOCATION);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverLocation driverLocation = getDriverLocation(resultSet);
                driverLocationList.add(driverLocation);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverLocationList;
    }

    private DriverLocation getDriverLocation(ResultSet resultSet) throws SQLException {
        return new DriverLocation(resultSet.getInt(1),  resultSet.getInt(2),
                resultSet.getString(3), resultSet.getString(4));
    }

    @Override
    public DriverLocation getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(DriverLocation driverLocation) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_DRIVER_LOCATION);
            statement.setInt(1, driverLocation.getDriverId());
            statement.setString(2, driverLocation.getLatitude());
            statement.setString(3, driverLocation.getLongitude());
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int ID) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_DRIVER_LOCATION);
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
}
