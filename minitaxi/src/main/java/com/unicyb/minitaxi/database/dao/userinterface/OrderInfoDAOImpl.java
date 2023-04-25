package com.unicyb.minitaxi.database.dao.userinterface;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.userinterfaceenteties.OrderInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderInfoDAOImpl implements DAO<OrderInfo> {
    @Override
    public boolean add(OrderInfo orderInfo) {
        return false;
    }

    @Override
    public List<OrderInfo> getAll() {
        return null;
    }

    public List<OrderInfo> getByDriverId(int ID){
        List<OrderInfo> orderInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_ORDERS_BY_DRIVER_ID);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                OrderInfo orderInfo = getOrderInfo(resultSet);
                orderInfoList.add(orderInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return orderInfoList;
    }

    private OrderInfo getOrderInfo(ResultSet resultSet) throws SQLException {
        return new OrderInfo(resultSet.getTimestamp(1),resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getFloat(5), resultSet.getFloat(6),
                resultSet.getFloat(7), resultSet.getString(8));
    }

    @Override
    public OrderInfo getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(OrderInfo orderInfo) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
