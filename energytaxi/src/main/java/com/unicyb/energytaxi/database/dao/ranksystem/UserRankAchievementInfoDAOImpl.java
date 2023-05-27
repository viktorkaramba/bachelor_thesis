package com.unicyb.energytaxi.database.dao.ranksystem;

import com.unicyb.energytaxi.database.DatabaseConnection;
import com.unicyb.energytaxi.database.SQLQuery;
import com.unicyb.energytaxi.database.dao.DAO;
import com.unicyb.energytaxi.entities.ranksystem.UserRankAchievementInfo;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRankAchievementInfoDAOImpl implements DAO<UserRankAchievementInfo> {

    @Override
    public boolean add(UserRankAchievementInfo userRankAchievementInfo) {
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.INSERT_USER_RANK_ACHIEVEMENT_INFO);
            preparedStatement.setTimestamp(1, userRankAchievementInfo.getDateUri());
            preparedStatement.setInt(2, userRankAchievementInfo.getUsersId());
            preparedStatement.setInt(3, userRankAchievementInfo.getRanksId());
            preparedStatement.setInt(4, userRankAchievementInfo.getNumberOfUsesSale());
            preparedStatement.setTimestamp(5, userRankAchievementInfo.getDeadlineDateSale());
            preparedStatement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private UserRankAchievementInfo getUserRankAchievementInfo(ResultSet resultSet) throws SQLException {
        return new UserRankAchievementInfo(resultSet.getInt(1), resultSet.getTimestamp(2),
                resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5),
                resultSet.getTimestamp(6));
    }

    @Override
    public List<UserRankAchievementInfo> getAll() {
        List<UserRankAchievementInfo> userRankAchievementInfoList = new ArrayList<>();
        try {
            Connection connection = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery.SELECT_USER_RANK_ACHIEVEMENT_INFO);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                UserRankAchievementInfo userRankAchievementInfo = getUserRankAchievementInfo(resultSet);
                userRankAchievementInfoList.add(userRankAchievementInfo);
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userRankAchievementInfoList;
    }

    @Override
    public UserRankAchievementInfo getOne(int ID) {
        return null;
    }

    public UserRankAchievementInfo getOneByUserIdAndRankId(int userId, int rankId){
        UserRankAchievementInfo userRankAchievementInfo = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID_AND_RANK_ID);
            statement.setInt(1, userId);
            statement.setInt(2, rankId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                userRankAchievementInfo = getUserRankAchievementInfo(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userRankAchievementInfo;
    }

    public UserRankAchievementInfo getOneByUserId(int ID) {
        UserRankAchievementInfo userRankAchievementInfo = null;
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement statement = con.prepareStatement(SQLQuery.SELECT_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                userRankAchievementInfo = getUserRankAchievementInfo(resultSet);
            }
            con.close();
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userRankAchievementInfo;
    }

    @Override
    public boolean update(UserRankAchievementInfo userRankAchievementInfo) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(SQLQuery.UPDATE_USER_RANK_ACHIEVEMENT_INFO);
            preparedStatement.setTimestamp(1, userRankAchievementInfo.getDateUri());
            preparedStatement.setInt(2, userRankAchievementInfo.getUsersId());
            preparedStatement.setInt(3, userRankAchievementInfo.getRanksId());
            preparedStatement.setInt(4, userRankAchievementInfo.getNumberOfUsesSale());
            preparedStatement.setTimestamp(5, userRankAchievementInfo.getDeadlineDateSale());
            preparedStatement.setInt(6, userRankAchievementInfo.getUriId());
            preparedStatement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateNumbersOfUsesSales(int ID) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(SQLQuery.UPDATE_USER_RANK_ACHIEVEMENT_INFO_BY_USER_ID);
            preparedStatement.setInt(1, ID);
            preparedStatement.executeUpdate();
            con.close();
            return true;
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateByDeadline(Timestamp currentDate, Timestamp newDeadlineDate, int rankId) {
        try {
            Connection con = DatabaseConnection.initializeDatabase();
            PreparedStatement preparedStatement = con.prepareStatement(SQLQuery.UPDATE_USER_RANK_ACHIEVEMENT_INFO_BY_DEADLINE);
            preparedStatement.setTimestamp(1, newDeadlineDate);
            preparedStatement.setTimestamp(2, currentDate);
            preparedStatement.setInt(3, rankId);
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.DELETE_USER_RANK_ACHIEVEMENT_INFO);
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
            PreparedStatement statement = con.prepareStatement(SQLQuery.CHECK_IF_EXIST_USER_RANK);
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                if (resultSet.getInt(1) == 1){
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
