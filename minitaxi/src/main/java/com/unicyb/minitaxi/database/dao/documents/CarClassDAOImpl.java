package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.documents.Car;
import com.unicyb.minitaxi.entities.documents.CarClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarClassDAOImpl implements DAO<CarClass> {
    @Override
    public boolean add(CarClass carClass) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_CAR_CLASSES);
            preparedStatement.setString(1, carClass.getName());
            preparedStatement.setFloat(2, carClass.getMinExperience());
            preparedStatement.setFloat(3, carClass.getMinNumberOfKilometers());
            preparedStatement.setFloat(4, carClass.getPrice());
            preparedStatement.setFloat(5, carClass.getMinRating());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CarClass> getAll() {
        List<CarClass> carClassesList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_CAR_CLASSES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                CarClass carClass = getCarClass(resultSet);
                carClassesList.add(carClass);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return carClassesList;
    }

    private CarClass getCarClass(ResultSet resultSet) throws SQLException {
        return new CarClass(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3),
                resultSet.getFloat(4), resultSet.getFloat(5),  resultSet.getFloat(6));
    }


    @Override
    public CarClass getOne(int ID) {
        CarClass carClass = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_CAR_CLASS_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                carClass = getCarClass(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return carClass;
    }

    @Override
    public boolean update(CarClass carClass) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_CAR_CLASSES);
            statement.setString(1, carClass.getName());
            statement.setFloat(2, carClass.getMinExperience());
            statement.setFloat(3, carClass.getMinNumberOfKilometers());
            statement.setFloat(4, carClass.getPrice());
            statement.setFloat(5, carClass.getMinRating());
            statement.setInt(6, carClass.getCcId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_CAR_CLASSES);
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
