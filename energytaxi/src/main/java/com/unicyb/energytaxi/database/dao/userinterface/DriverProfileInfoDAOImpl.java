package com.unicyb.energytaxi.database.dao.userinterface;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.userinterfaceenteties.DriverProfileInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DriverProfileInfoDAOImpl implements DAO<DriverProfileInfo> {
    @Override
    public boolean add(DriverProfileInfo driverProfileInfo) {
        return false;
    }

    @Override
    public List<DriverProfileInfo> getAll() {
        return null;
    }

    public DriverProfileInfo getByDriverId(int ID){
        DriverProfileInfo orderProfileInfo = null;
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_DRIVER_PROFILE_INFO);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                orderProfileInfo = getDriverProfileInfo(resultSet);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderProfileInfo;
    }

    private DriverProfileInfo getDriverProfileInfo(ResultSet resultSet) throws SQLException {
        return new DriverProfileInfo(resultSet.getInt(1), resultSet.getString(2),
                resultSet.getString(3), resultSet.getString(4), resultSet.getString(5),
                resultSet.getString(6), resultSet.getString(7), resultSet.getFloat(8),
                resultSet.getFloat(9));
    }

    @Override
    public DriverProfileInfo getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(DriverProfileInfo driverProfileInfo) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
