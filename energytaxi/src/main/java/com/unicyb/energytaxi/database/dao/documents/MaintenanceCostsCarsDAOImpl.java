package com.unicyb.energytaxi.database.dao.documents;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.documents.MaintenanceCostsCars;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceCostsCarsDAOImpl implements DAO<MaintenanceCostsCars> {
    @Override
    public boolean add(MaintenanceCostsCars maintenanceCostsCars) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_MAINTENANCE_COSTS_CARS);
            preparedStatement.setTimestamp(1, maintenanceCostsCars.getDate());
            preparedStatement.setString(2, maintenanceCostsCars.getType());
            preparedStatement.setFloat(3, maintenanceCostsCars.getCosts());
            preparedStatement.setInt(4, maintenanceCostsCars.getCarId());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MaintenanceCostsCars> getAll() {
        List<MaintenanceCostsCars> maintenanceCostsCarsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_MAINTENANCE_COSTS_CARS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                MaintenanceCostsCars maintenanceCostsCars = getMaintenanceCostsCars(resultSet);
                maintenanceCostsCarsList.add(maintenanceCostsCars);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return maintenanceCostsCarsList;
    }

    private MaintenanceCostsCars getMaintenanceCostsCars(ResultSet resultSet) throws SQLException {
        return new MaintenanceCostsCars(resultSet.getInt(1), resultSet.getTimestamp(2),
                resultSet.getString(3), resultSet.getFloat(4), resultSet.getInt(5));
    }

    @Override
    public MaintenanceCostsCars getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(MaintenanceCostsCars maintenanceCostsCars) {

        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_MAINTENANCE_COSTS_CARS);
            statement.setTimestamp(1, maintenanceCostsCars.getDate());
            statement.setString(2, maintenanceCostsCars.getType());
            statement.setFloat(3, maintenanceCostsCars.getCosts());
            statement.setInt(4, maintenanceCostsCars.getCarId());
            statement.setInt(5, maintenanceCostsCars.getMccId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_MAINTENANCE_COSTS_CARS);
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
