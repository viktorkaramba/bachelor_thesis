package com.unicyb.energytaxi.database.dao.indicators;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.NumberOfKilometers;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NumberOfKilometersDAOImpl implements DAO<NumberOfKilometers> {
    @Override
    public boolean add(NumberOfKilometers numberOfKilometers) {
        return false;
    }

    @Override
    public List<NumberOfKilometers> getAll() {
        List<NumberOfKilometers> numberOfKilometersList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_NUMBER_OF_KILOMETERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                NumberOfKilometers numberOfKilometers = getNumberOfKilometers(resultSet);
                numberOfKilometersList.add(numberOfKilometers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return numberOfKilometersList;
    }

    public List<NumberOfKilometers> getReport(Report report){
        List<NumberOfKilometers> numberOfKilometersList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_NUMBER_OF_KILOMETERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                NumberOfKilometers numberOfKilometers = getNumberOfKilometers(resultSet);
                numberOfKilometersList.add(numberOfKilometers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return numberOfKilometersList;
    }

    public List<NumberOfKilometers> getReportById(Report report){
        List<NumberOfKilometers> numberOfKilometersList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_BY_ID_NUMBER_OF_KILOMETERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                NumberOfKilometers numberOfKilometers = getNumberOfKilometers(resultSet);
                numberOfKilometersList.add(numberOfKilometers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return numberOfKilometersList;
    }

    public List<NumberOfKilometers> getAllByDriverId(int ID){
        List<NumberOfKilometers> numberOfKilometersList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_NUMBER_OF_KILOMETERS_BY_DRIVER_ID);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                NumberOfKilometers numberOfKilometers = getNumberOfKilometers(resultSet);
                numberOfKilometersList.add(numberOfKilometers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return numberOfKilometersList;
    }

    private NumberOfKilometers getNumberOfKilometers(ResultSet resultSet) throws SQLException {
        return new NumberOfKilometers(resultSet.getInt(1), resultSet.getTimestamp(2), resultSet.getFloat(3),
                resultSet.getInt(4));
    }

    @Override
    public NumberOfKilometers getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(NumberOfKilometers numberOfKilometers) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
