package com.unicyb.minitaxi.database.dao.indicators;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.documents.Car;
import com.unicyb.minitaxi.entities.indicators.DriverRating;
import com.unicyb.minitaxi.entities.indicators.NumberOfKilometers;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverRatingDAOImpl implements DAO<DriverRating> {
    @Override
    public boolean add(DriverRating driverRating) {
        return false;
    }

    @Override
    public List<DriverRating> getAll() {
        List<DriverRating> driverRatingsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_RATING);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverRating driverRating = getDriverRating(resultSet);
                driverRatingsList.add(driverRating);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverRatingsList;
    }

    public List<DriverRating> getReport(Report report){
        List<DriverRating> driverRatingsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_DRIVER_RATING);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverRating driverRating = getDriverRating(resultSet);
                driverRatingsList.add(driverRating);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverRatingsList;
    }

    public List<DriverRating> getReportById(Report report){
        List<DriverRating> driverRatingsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_DRIVER_RATING_BY_DRIVER_ID);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverRating driverRating = getDriverRating(resultSet);
                driverRatingsList.add(driverRating);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverRatingsList;
    }


    public List<DriverRating> getAllByDriverId(int ID){
        List<DriverRating> driverRatingList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_DRIVER_RATING_BY_DRIVER_ID);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                DriverRating driverRating = getDriverRating(resultSet);
                driverRatingList.add(driverRating);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return driverRatingList;
    }

    private DriverRating getDriverRating(ResultSet resultSet) throws SQLException {
        return new DriverRating(resultSet.getInt(1), resultSet.getInt(2), resultSet.getTimestamp(3),
                resultSet.getFloat(4));
    }

    @Override
    public DriverRating getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(DriverRating driverRating) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
