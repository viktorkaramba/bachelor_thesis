package com.unicyb.energytaxi.database.dao.documents;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.documents.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarsDAOImpl implements DAO<Car> {

    @Override
    public boolean add(Car car) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_CAR);
            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setString(2, car.getProducer());
            preparedStatement.setInt(3, car.getNumberOfSeats());
            preparedStatement.setInt(4, car.getCcID());
            preparedStatement.setString(5, car.getInUse());
            preparedStatement.setString(6, car.getInOrder());
            preparedStatement.setTimestamp(7, car.getDate());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Car> getAll() {
        List<Car> carList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_CAR);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Car car = getCar(resultSet);
                carList.add(car);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return carList;
    }

    public List<Car> getAllByCarClass(int ID) {
        List<Car> carList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_CAR_BY_CAR_CLASS);
            preparedStatement.setInt(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Car car = getCar(resultSet);
                carList.add(car);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return carList;
    }

    private Car getCar(ResultSet resultSet) throws SQLException {
        return new Car(resultSet.getInt(1), resultSet.getTimestamp(8), resultSet.getString(2),
                resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5),
                resultSet.getString(6), resultSet.getString(7));
    }

    @Override
    public Car getOne(int ID) {
        Car car = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_CAR_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                car = getCar(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public boolean update(Car car) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_CAR);
            statement.setString(1, car.getBrand());
            statement.setString(2, car.getProducer());
            statement.setInt(3, car.getNumberOfSeats());
            statement.setInt(4, car.getCcID());
            statement.setString(5, car.getInUse());
            statement.setString(6, car.getInOrder());
            statement.setInt(7, car.getCarId());
            statement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCarUse(int carID, String status) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_CAR_USE);
            statement.setString(1, status);
            statement.setInt(2, carID);
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_CAR);
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
