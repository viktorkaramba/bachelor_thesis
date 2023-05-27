package com.unicyb.energytaxi.database.dao.indicators;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.IncomeCars;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IncomeCarsDAOImpl implements DAO<IncomeCars> {
    @Override
    public boolean add(IncomeCars incomeCars) {
        return false;
    }

    @Override
    public List<IncomeCars> getAll() {
        List<IncomeCars> incomeCarsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_INCOME_CARS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                IncomeCars incomeCars = getIncomeCars(resultSet);
                incomeCarsList.add(incomeCars);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return incomeCarsList;
    }

    public List<IncomeCars> getReport(Report report){
        List<IncomeCars> incomeCarsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_INCOME_CARS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                IncomeCars incomeCars = getIncomeCars(resultSet);
                incomeCarsList.add(incomeCars);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return incomeCarsList;
    }

    public List<IncomeCars> getReportById(Report report){
        List<IncomeCars> incomeCarsList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_BY_ID_INCOME_CARS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                IncomeCars incomeCars = getIncomeCars(resultSet);
                incomeCarsList.add(incomeCars);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return incomeCarsList;
    }

    private IncomeCars getIncomeCars(ResultSet resultSet) throws SQLException {
        return new IncomeCars(resultSet.getInt(1), resultSet.getInt(2), resultSet.getTimestamp(3),
                resultSet.getFloat(4), resultSet.getFloat(5));
    }

    @Override
    public IncomeCars getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(IncomeCars incomeCars) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
