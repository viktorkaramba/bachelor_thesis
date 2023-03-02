package com.unicyb.minitaxi.database.dao.userinterface;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.userinterfaceenteties.UserPickCar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPickCarDAOImpl implements DAO<UserPickCar> {
    @Override
    public boolean add(UserPickCar userPickCar) {
        return false;
    }

    @Override
    public List<UserPickCar> getAll() {
        List<UserPickCar> userPickCarList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_CARS_DRIVERS_PRICE_FOR_USER_MENU);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UserPickCar userPickCar = getUserPickCar(resultSet);
                userPickCarList.add(userPickCar);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userPickCarList;
    }

    private UserPickCar getUserPickCar(ResultSet resultSet) throws SQLException {
        return new UserPickCar(resultSet.getInt(1),resultSet.getString(2), resultSet.getString(3),
                resultSet.getString(4), resultSet.getString(5), resultSet.getFloat(6));
    }

    @Override
    public UserPickCar getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(UserPickCar userPickCar) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
