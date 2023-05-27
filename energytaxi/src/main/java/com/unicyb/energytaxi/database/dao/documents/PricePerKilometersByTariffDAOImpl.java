package com.unicyb.energytaxi.database.dao.documents;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.documents.PricePerKilometersByTariff;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PricePerKilometersByTariffDAOImpl implements DAO<PricePerKilometersByTariff> {

    @Override
    public boolean add(PricePerKilometersByTariff pricePerKilometersByTariff) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_PRICE_PER_KILOMETER_BY_TARIFF);
            preparedStatement.setInt(1, pricePerKilometersByTariff.getPpkbtId());
            preparedStatement.setInt(2, pricePerKilometersByTariff.getCarClassId());
            preparedStatement.setFloat(3, pricePerKilometersByTariff.getMorning());
            preparedStatement.setFloat(4, pricePerKilometersByTariff.getDay());
            preparedStatement.setFloat(5, pricePerKilometersByTariff.getEvening());
            preparedStatement.setFloat(6, pricePerKilometersByTariff.getNight());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PricePerKilometersByTariff> getAll() {
        List<PricePerKilometersByTariff> pricePerKilometersByTariffList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_PRICE_PER_KILOMETER_BY_TARIFF);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                PricePerKilometersByTariff pricePerKilometersByTariff = getPricePerKilometersByTariff(resultSet);
                pricePerKilometersByTariffList.add(pricePerKilometersByTariff);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pricePerKilometersByTariffList;
    }

    private PricePerKilometersByTariff getPricePerKilometersByTariff(ResultSet resultSet) throws SQLException {
        return new PricePerKilometersByTariff(resultSet.getInt(1), resultSet.getInt(2),
                resultSet.getFloat(3), resultSet.getFloat(4),
                resultSet.getFloat(5), resultSet.getFloat(6));
    }

    @Override
    public PricePerKilometersByTariff getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(PricePerKilometersByTariff pricePerKilometersByTariff) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_PRICE_PER_KILOMETER_BY_TARIFF);
            statement.setInt(1, pricePerKilometersByTariff.getCarClassId());
            statement.setFloat(2, pricePerKilometersByTariff.getMorning());
            statement.setFloat(3, pricePerKilometersByTariff.getDay());
            statement.setFloat(4, pricePerKilometersByTariff.getEvening());
            statement.setFloat(5, pricePerKilometersByTariff.getNight());
            statement.setInt(6, pricePerKilometersByTariff.getPpkbtId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_PRICE_PER_KILOMETER_BY_TARIFF);
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
