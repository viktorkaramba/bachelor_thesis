package com.unicyb.energytaxi.services.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserEliteRankAchievementService {
    private final UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    @Autowired
    public UserEliteRankAchievementService(UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO) {
        this.userEliteRankAchievementInfoDAO = userEliteRankAchievementInfoDAO;
    }

    public boolean add(UserEliteRankAchievementInfo userEliteRankAchievementInfo) {
        return userEliteRankAchievementInfoDAO.add(userEliteRankAchievementInfo);
    }

    public List<UserEliteRankAchievementInfo> getAll() {
        return userEliteRankAchievementInfoDAO.getAll();
    }

    public List<UserEliteRankAchievementInfo> getByUserIdAndRankId(int userId, int rankId) {
        return userEliteRankAchievementInfoDAO.getByUserIdAndRankId(userId, rankId);
    }

    public List<UserEliteRankAchievementInfo> getByUserIdAndRankIdDriverId(int userId, int rankId, int driverId) {
        return userEliteRankAchievementInfoDAO.getByUserIdAndRankIdDriverId(userId, rankId, driverId);
    }

    public List<UserEliteRankAchievementInfo> getOneByUserId(int ID) {
        return userEliteRankAchievementInfoDAO.getOneByUserId(ID);
    }

    public boolean update(UserEliteRankAchievementInfo userEliteRankAchievementInfo) {
        return userEliteRankAchievementInfoDAO.update(userEliteRankAchievementInfo);
    }

    public boolean updateByDeadline(int numberOfOrders, Timestamp currentDate, Timestamp newFreeOrderDeadLine, int erId) {
        return userEliteRankAchievementInfoDAO.updateByDeadline(numberOfOrders, currentDate, newFreeOrderDeadLine, erId);
    }

    public boolean delete(int ID) {
        return userEliteRankAchievementInfoDAO.delete(ID);
    }

    public boolean checkIfUserExist(int ID){
        return userEliteRankAchievementInfoDAO.checkIfUserExist(ID);
    }
}
