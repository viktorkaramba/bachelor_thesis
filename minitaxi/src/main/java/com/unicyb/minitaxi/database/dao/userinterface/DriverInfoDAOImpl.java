package com.unicyb.minitaxi.database.dao.userinterface;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.usersinfo.DriverInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DriverInfoDAOImpl implements DAO<DriverInfo> {

    @Override
    public boolean add(DriverInfo driverInfo) {
        return false;
    }

    @Override
    public List<DriverInfo> getAll() {
        return null;
    }

    @Override
    public DriverInfo getOne(int ID) {
        DriverInfo driverInfo = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_DRIVER_INFO_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                driverInfo = getDriverInfo(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return driverInfo;
    }

    private DriverInfo getDriverInfo(ResultSet resultSet) throws SQLException {
        return new DriverInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
    }

    @Override
    public boolean update(DriverInfo driverInfo) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
