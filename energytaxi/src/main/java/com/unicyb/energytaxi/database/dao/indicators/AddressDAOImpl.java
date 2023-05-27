package com.unicyb.energytaxi.database.dao.indicators;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.indicators.Address;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AddressDAOImpl implements DAO<Address> {
    @Override
    public boolean add(Address address) {
        return false;
    }

    @Override
    public List<Address> getAll() {
        List<Address> addressList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_ADDRESSES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Address address = getAddress(resultSet);
                addressList.add(address);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addressList;
    }

    private Address getAddress(ResultSet resultSet) throws SQLException {
        return new Address(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3));
    }

    @Override
    public Address getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(Address address) {
        return false;
    }

    @Override
    public boolean delete(int ID) {
        return false;
    }
}
