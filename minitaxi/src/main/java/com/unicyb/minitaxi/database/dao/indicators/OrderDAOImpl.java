package com.unicyb.minitaxi.database.dao.indicators;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.indicators.Order;
import com.unicyb.minitaxi.entities.userinterfaceenteties.Report;
import com.unicyb.minitaxi.entities.userinterfaceenteties.UserOrderInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements DAO<Order> {
    @Override
    public boolean add(Order order) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_ORDER);
            preparedStatement.setInt(1, order.getDriverId());
            preparedStatement.setString(2, order.getAddressCustomer());
            preparedStatement.setString(3, order.getAddressDelivery());
            preparedStatement.setString(4, order.getTelephoneCustomer());
            preparedStatement.setFloat(5, order.getPrice());
            preparedStatement.setTimestamp(6, order.getDate());
            preparedStatement.setFloat(7, order.getRating());
            preparedStatement.setFloat(8, order.getNumberOfKilometers());
            preparedStatement.setString(9, order.getCustomerName());
            preparedStatement.setInt(10, order.getUserId());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_ORDERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = getOrder(resultSet);
                orderList.add(order);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Order> getReport(Report report){
        List<Order> orderList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_ORDERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = getOrder(resultSet);
                orderList.add(order);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public List<Order> getReportById(Report report){
        List<Order> orderList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.REPORT_BY_ID_ORDERS);
            preparedStatement.setTimestamp(1, report.getStartDate());
            preparedStatement.setTimestamp(2, report.getEndDate());
            preparedStatement.setInt(3, report.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = getOrder(resultSet);
                orderList.add(order);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    private Order getOrder(ResultSet resultSet) throws SQLException {
        return new Order(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getString(5),
                resultSet.getFloat(6), resultSet.getTimestamp(7),
                resultSet.getFloat(8), resultSet.getFloat(9),
                resultSet.getString(10), resultSet.getInt(11));
    }

    private UserOrderInfo getUserOrderInfo(ResultSet resultSet) throws SQLException {
        return new UserOrderInfo(resultSet.getString(1) + " " + resultSet.getString(2),
                resultSet.getString(3) + " "+ resultSet.getString(4),
                resultSet.getString(5), resultSet.getString(6), resultSet.getFloat(7),
                resultSet.getFloat(8), resultSet.getTimestamp(9));
    }

    public List<UserOrderInfo> getUserOrderInfo(int ID){
        List<UserOrderInfo> userOrderInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_USER_ORDERS_INFO);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UserOrderInfo userOrderInfo = getUserOrderInfo(resultSet);
                userOrderInfoList.add(userOrderInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userOrderInfoList;
    }

    @Override
    public Order getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
