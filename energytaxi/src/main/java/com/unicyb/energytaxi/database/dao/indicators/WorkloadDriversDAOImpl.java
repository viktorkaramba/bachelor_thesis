package com.unicyb.energytaxi.database.dao.indicators;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.WorkloadDrivers;
import com.unicyb.energytaxi.entities.userinterfaceenteties.Report;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WorkloadDriversDAOImpl implements DAO<WorkloadDrivers> {
    @Override
    public boolean add(WorkloadDrivers workloadDrivers) {
        return false;
    }

    @Override
    public List<WorkloadDrivers> getAll() {
        List<WorkloadDrivers> workloadDriversList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_WORKLOAD_DRIVERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                WorkloadDrivers workloadDrivers = getWorkloadDrivers(resultSet);
                workloadDriversList.add(workloadDrivers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workloadDriversList;
    }

    public List<WorkloadDrivers> getReport(Report report){
        List<WorkloadDrivers> workloadDriversList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_WORKLOAD_DRIVERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                WorkloadDrivers workloadDrivers = getWorkloadDrivers(resultSet);
                workloadDriversList.add(workloadDrivers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workloadDriversList;
    }

    public List<WorkloadDrivers> getReportById(Report report){
        List<WorkloadDrivers> workloadDriversList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_BY_ID_WORKLOAD_DRIVERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                WorkloadDrivers workloadDrivers = getWorkloadDrivers(resultSet);
                workloadDriversList.add(workloadDrivers);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workloadDriversList;
    }

    private WorkloadDrivers getWorkloadDrivers(ResultSet resultSet) throws SQLException {
        return new WorkloadDrivers(resultSet.getInt(1), resultSet.getInt(2), resultSet.getTimestamp(3),
                resultSet.getInt(4), resultSet.getInt(5),
                resultSet.getInt(6), resultSet.getInt(7));
    }


    @Override
    public WorkloadDrivers getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(WorkloadDrivers workloadDrivers) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
