package com.unicyb.energytaxi.database.dao.indicators;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.DriverCarRecommendations;
import com.unicyb.energytaxi.entities.indicators.STATUS;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DriverCarRecommendationsDAOImpl implements DAO<DriverCarRecommendations> {

    @Override
    public boolean add(DriverCarRecommendations driverCarRecommendations) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_DRIVER_CAR_RECOMMENDATIONS);
            preparedStatement.setTimestamp(1, driverCarRecommendations.getDate());
            preparedStatement.setInt(2, driverCarRecommendations.getDriverId());
            preparedStatement.setInt(3, driverCarRecommendations.getCarId());
            preparedStatement.setString(4, driverCarRecommendations.getStatus().name());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DriverCarRecommendations> getAll() {
        List<DriverCarRecommendations> driverCarRecommendationsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_CAR_REC);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverCarRecommendations driverCarRecommendations = getDriverCarRecommendations(resultSet);
                driverCarRecommendationsList.add(driverCarRecommendations);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverCarRecommendationsList;
    }

    public List<DriverCarRecommendations> getReport(Report report){
        List<DriverCarRecommendations> driverCarRecommendationsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_DRIVER_CAR_REC);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverCarRecommendations driverCarRecommendations = getDriverCarRecommendations(resultSet);
                driverCarRecommendationsList.add(driverCarRecommendations);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverCarRecommendationsList;
    }

    public List<DriverCarRecommendations> getReportById(Report report){
        List<DriverCarRecommendations> driverCarRecommendationsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_BY_ID_DRIVER_CAR_REC);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverCarRecommendations driverCarRecommendations = getDriverCarRecommendations(resultSet);
                driverCarRecommendationsList.add(driverCarRecommendations);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverCarRecommendationsList;
    }
    private DriverCarRecommendations getDriverCarRecommendations(ResultSet resultSet) throws SQLException {
        return new DriverCarRecommendations(resultSet.getInt(1), resultSet.getTimestamp(2),
                resultSet.getInt(3), resultSet.getInt(4), STATUS.valueOf(resultSet.getString(5)));
    }

    @Override
    public DriverCarRecommendations getOne(int ID) {
        return null;
    }

    public DriverCarRecommendations getOneByDriverId(int ID){
        DriverCarRecommendations driverCarRecommendations = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_CAR_REC_BY_DRIVER_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                driverCarRecommendations = getDriverCarRecommendations(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return driverCarRecommendations;
    }

    @Override
    public boolean update(DriverCarRecommendations driverCarRecommendations) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_DRIVER_CAR_REC);
            statement.setTimestamp(1, driverCarRecommendations.getDate());
            statement.setInt(2, driverCarRecommendations.getDriverId());
            statement.setInt(3, driverCarRecommendations.getCarId());
            statement.setString(4, driverCarRecommendations.getStatus().name());
            statement.setInt(5, driverCarRecommendations.getDcrId());
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
        return false;
    }
}
