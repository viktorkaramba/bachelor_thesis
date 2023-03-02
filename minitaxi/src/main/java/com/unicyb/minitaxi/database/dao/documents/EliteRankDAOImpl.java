package com.unicyb.minitaxi.database.dao.documents;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.ranksystem.EliteRank;
import com.unicyb.minitaxi.ranksystem.EliteRankUserInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EliteRankDAOImpl implements DAO<EliteRank> {

    @Override
    public boolean add(EliteRank eliteRank) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_ELITE_RANK);
            preparedStatement.setInt(1, eliteRank.getRankId());
            preparedStatement.setInt(2, eliteRank.getCcId());
            preparedStatement.setFloat(3, eliteRank.getPeriod());
            preparedStatement.setInt(5, eliteRank.getFreeOrders());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private EliteRank getEliteRank(ResultSet resultSet) throws SQLException {
        return new EliteRank(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3),
                resultSet.getFloat(4), resultSet.getInt(5));
    }

    @Override
    public List<EliteRank> getAll() {
        List<EliteRank> eliteRanksList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ALL_ELITE_RANK);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                EliteRank eliteRank = getEliteRank(resultSet);
                eliteRanksList.add(eliteRank);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return eliteRanksList;
    }

    private EliteRankUserInfo getEliteRankUserInfo(ResultSet resultSet) throws SQLException {
        return new EliteRankUserInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
                resultSet.getInt(4), resultSet.getFloat(5), resultSet.getFloat(6),
                resultSet.getString(7), resultSet.getFloat(8), resultSet.getInt(9));
    }

    public List<EliteRankUserInfo> getAllUserInfo() {
        List<EliteRankUserInfo> eliteRankUserInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_ELITE_RANK_USER_INFO);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                EliteRankUserInfo eliteRankUserInfo = getEliteRankUserInfo(resultSet);
                eliteRankUserInfoList.add(eliteRankUserInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return eliteRankUserInfoList;
    }

    @Override
    public EliteRank getOne(int ID) {
        return null;
    }

    @Override
    public boolean update(EliteRank eliteRank) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.UPDATE_ELITE_RANK);
            statement.setInt(1, eliteRank.getRankId());
            statement.setInt(2, eliteRank.getCcId());
            statement.setFloat(3, eliteRank.getPeriod());
            statement.setFloat(4, eliteRank.getFreeOrders());
            statement.setFloat(5, eliteRank.getEliteRankId());
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_ELITE_RANK);
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
