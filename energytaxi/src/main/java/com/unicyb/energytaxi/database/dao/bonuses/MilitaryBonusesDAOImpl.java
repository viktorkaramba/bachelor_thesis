package com.unicyb.energytaxi.database.dao.bonuses;


import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.bonuses.MILITARY_BONUS_STATUS;
import com.unicyb.energytaxi.entities.bonuses.MilitaryBonuses;
import com.unicyb.energytaxi.entities.indicators.STATUS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MilitaryBonusesDAOImpl  implements DAO<MilitaryBonuses> {

    @Override
    public boolean add(MilitaryBonuses militaryBonuses) {
        try {
            InputStream photo = new ByteArrayInputStream(militaryBonuses.getDocumentPhotoByteArray());
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_MILITARY_BONUSES);
            preparedStatement.setInt(1, militaryBonuses.getUserId());
            preparedStatement.setBlob(2, photo);
            preparedStatement.setString(3, militaryBonuses.getMilitaryBonusStatus().name());
            preparedStatement.setFloat(4, militaryBonuses.getSaleValue());
            preparedStatement.setTimestamp(5, militaryBonuses.getDate());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<MilitaryBonuses> getAll() {
        List<MilitaryBonuses> militaryBonusesList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_MILITARY_BONUSES);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                MilitaryBonuses MilitaryBonuses = getMilitaryBonuses(resultSet);
                militaryBonusesList.add(MilitaryBonuses);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return militaryBonusesList;
    }

    public List<MilitaryBonuses> getAllByStatus(STATUS status) {
        List<MilitaryBonuses> militaryBonusesList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_MILITARY_BONUSES_BY_STATUS);
            preparedStatement.setString(1, status.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                MilitaryBonuses MilitaryBonuses = getMilitaryBonuses(resultSet);
                militaryBonusesList.add(MilitaryBonuses);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return militaryBonusesList;
    }

    private MilitaryBonuses getMilitaryBonuses(ResultSet resultSet) throws SQLException {
        Blob blob  = resultSet.getBlob(3);
        byte[] photo = blob.getBytes(1l, (int)blob.length());
        return new MilitaryBonuses(resultSet.getInt(1), resultSet.getInt(2), photo,
                MILITARY_BONUS_STATUS.valueOf(resultSet.getString(4)), resultSet.getFloat(5),
                resultSet.getTimestamp(6));
    }

    private MilitaryBonuses getMilitaryWithoutPhoto(ResultSet resultSet) throws SQLException {
        return new MilitaryBonuses(resultSet.getInt(1), resultSet.getInt(2), null,
                MILITARY_BONUS_STATUS.valueOf(resultSet.getString(3)), resultSet.getFloat(4),
                resultSet.getTimestamp(5));
    }


    @Override
    public MilitaryBonuses getOne(int ID) {
        MilitaryBonuses militaryBonuses = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_MILITARY_BONUSES_BY_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                militaryBonuses = getMilitaryBonuses(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return militaryBonuses;
    }

    public MilitaryBonuses getOneByUserIdWithoutPhoto(int ID) {
        MilitaryBonuses militaryBonuses = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_ALL_MILITARY_BONUSES_BY_USER_ID_WITHOUT_PHOTO);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                militaryBonuses = getMilitaryWithoutPhoto(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return militaryBonuses;
    }

    @Override
    public boolean update(MilitaryBonuses militaryBonuses) {
        try {
            InputStream photo = new ByteArrayInputStream(militaryBonuses.getDocumentPhotoByteArray());
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_MILITARY_BONUSES);
            statement.setInt(1, militaryBonuses.getUserId());
            statement.setBlob(2,photo);
            statement.setString(3, militaryBonuses.getMilitaryBonusStatus().name());
            statement.setFloat(4, militaryBonuses.getSaleValue());
            statement.setTimestamp(5, militaryBonuses.getDate());
            statement.setInt(6, militaryBonuses.getMilitaryBonusesId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_MILITARY_BONUSES);
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
