package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.ranksystem.Rank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankDAOImpl implements DAO<Rank> {

    @Override
    public boolean add(Rank rank) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_RANK);
            preparedStatement.setString(1, rank.getName());
            preparedStatement.setInt(2, rank.getMinOrders());
            preparedStatement.setInt(3, rank.getMinComments());
            preparedStatement.setFloat(5, rank.getSalePeriod());
            preparedStatement.setFloat(6, rank.getSaleValue());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Rank getRank(ResultSet resultSet) throws SQLException {
        return new Rank(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
                resultSet.getInt(4), resultSet.getFloat(5), resultSet.getFloat(6));
    }

    @Override
    public List<Rank> getAll() {
        List<Rank> ranksList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_RANK);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Rank rank = getRank(resultSet);
                ranksList.add(rank);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ranksList;
    }

    @Override
    public Rank getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(Rank rank) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_RANK);
            statement.setString(1, rank.getName());
            statement.setInt(2, rank.getMinOrders());
            statement.setInt(3, rank.getMinComments());
            statement.setFloat(4, rank.getSalePeriod());
            statement.setFloat(6, rank.getSaleValue());
            statement.setInt(6, rank.getRankId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_RANK);
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