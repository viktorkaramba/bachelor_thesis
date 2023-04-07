package com.unicyb.minitaxi.database.dao.ranksystem;

import com.unicyb.minitaxi.database.DatabaseConnection;
import com.unicyb.minitaxi.database.SQLQuery;
import com.unicyb.minitaxi.database.dao.DAO;
import com.unicyb.minitaxi.entities.ranksystem.UserEliteRankAchievementInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserEliteRankAchievementInfoDAOImpl implements DAO<UserEliteRankAchievementInfo> {

    @Override
    public boolean add(UserEliteRankAchievementInfo userEliteRankAchievementInfo) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_USER_ELITE_RANK_ACHIEVEMENT_INFO);
            preparedStatement.setTimestamp(1, userEliteRankAchievementInfo.getDateUerai());
            preparedStatement.setInt(2, userEliteRankAchievementInfo.getUsersId());
            preparedStatement.setInt(3, userEliteRankAchievementInfo.getEliteRanksId());
            preparedStatement.setInt(4, userEliteRankAchievementInfo.getNumberOfUsesFreeOrder());
            preparedStatement.setTimestamp(5, userEliteRankAchievementInfo.getDeadlineDateFreeOrder());
            preparedStatement.setInt(6, userEliteRankAchievementInfo.getCarClassId());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private UserEliteRankAchievementInfo getUserEliteRankAchievementInfo(ResultSet resultSet) throws SQLException {
        return new UserEliteRankAchievementInfo(resultSet.getInt(1), resultSet.getTimestamp(2),
                resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5),
                resultSet.getTimestamp(6), resultSet.getInt(7));
    }

    @Override
    public List<UserEliteRankAchievementInfo> getAll() {
        List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UserEliteRankAchievementInfo userEliteRankAchievementInfo = getUserEliteRankAchievementInfo(resultSet);
                userEliteRankAchievementInfoList.add(userEliteRankAchievementInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userEliteRankAchievementInfoList;
    }

    @Override
    public UserEliteRankAchievementInfo getOne(int ID) {
        return null;
    }

    public List<UserEliteRankAchievementInfo> getByUserIdAndRankId(int userId, int rankId) {
        List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID);
            statement.setInt(1, userId);
            statement.setInt(2, rankId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                UserEliteRankAchievementInfo userEliteRankAchievementInfo = getUserEliteRankAchievementInfo(resultSet);
                userEliteRankAchievementInfoList.add(userEliteRankAchievementInfo);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userEliteRankAchievementInfoList;
    }

    public List<UserEliteRankAchievementInfo> getByUserIdAndRankIdDriverId(int userId, int rankId, int driverId) {
        List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList = new ArrayList<>();
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID_AND_DRIVER_ID);
            statement.setInt(1, userId);
            statement.setInt(2, rankId);
            statement.setInt(3, driverId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                UserEliteRankAchievementInfo userEliteRankAchievementInfo = getUserEliteRankAchievementInfo(resultSet);
                userEliteRankAchievementInfoList.add(userEliteRankAchievementInfo);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userEliteRankAchievementInfoList;
    }

    public List<UserEliteRankAchievementInfo> getOneByUserId(int ID) {
        List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_USER_ELITE_RANK_ACHIEVEMENT_INFO_BY_USER_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                UserEliteRankAchievementInfo userEliteRankAchievementInfo = getUserEliteRankAchievementInfo(resultSet);
                userEliteRankAchievementInfoList.add(userEliteRankAchievementInfo);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userEliteRankAchievementInfoList;
    }

    @Override
    public boolean update(UserEliteRankAchievementInfo userEliteRankAchievementInfo) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(SQLQuery.UPDATE_USER_ELITE_RANK_ACHIEVEMENT_INFO);
            preparedStatement.setTimestamp(1, userEliteRankAchievementInfo.getDateUerai());
            preparedStatement.setInt(2, userEliteRankAchievementInfo.getUsersId());
            preparedStatement.setInt(3, userEliteRankAchievementInfo.getEliteRanksId());
            preparedStatement.setInt(4, userEliteRankAchievementInfo.getNumberOfUsesFreeOrder());
            preparedStatement.setTimestamp(5, userEliteRankAchievementInfo.getDeadlineDateFreeOrder());
            preparedStatement.setInt(6, userEliteRankAchievementInfo.getCarClassId());
            preparedStatement.setInt(7, userEliteRankAchievementInfo.getUeraiId());
            preparedStatement.executeUpdate();
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_USER_ELITE_RANK_ACHIEVEMENT_INFO);
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

    public boolean checkIfUserExist(int ID){
        boolean result = false;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.CHECK_IF_EXIST_USER_ELITE_RANK);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getInt(1) >= 1){
                    result = true;
                }
            }
            con.close();
            return result;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }
}
