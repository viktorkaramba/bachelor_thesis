package com.unicyb.energytaxi.services.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.UserRankAchievementInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserRankAchievementService {

    private final UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    @Autowired
    public UserRankAchievementService(UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO) {
        this.userRankAchievementInfoDAO = userRankAchievementInfoDAO;
    }

    public boolean add(UserRankAchievementInfo userRankAchievementInfo) {
        return userRankAchievementInfoDAO.add(userRankAchievementInfo);
    }

    public List<UserRankAchievementInfo> getAll() {
        return userRankAchievementInfoDAO.getAll();
    }

    public UserRankAchievementInfo getOneByUserIdAndRankId(int userId, int rankId){
        return userRankAchievementInfoDAO.getOneByUserIdAndRankId(userId, rankId);
    }

    public UserRankAchievementInfo getOneByUserId(int ID) {
        return userRankAchievementInfoDAO.getOneByUserId(ID);
    }

    public boolean update(UserRankAchievementInfo userRankAchievementInfo) {
        return userRankAchievementInfoDAO.update(userRankAchievementInfo);
    }

    public boolean updateNumbersOfUsesSales(int ID) {
        return userRankAchievementInfoDAO.updateNumbersOfUsesSales(ID);
    }

    public boolean updateByDeadline(Timestamp currentDate, Timestamp newDeadlineDate, int rankId) {
        return userRankAchievementInfoDAO.updateByDeadline(currentDate, newDeadlineDate, rankId);
    }

    public boolean delete(int ID) {
        return userRankAchievementInfoDAO.delete(ID);
    }

    public boolean checkIfUserExist(int ID){
        return userRankAchievementInfoDAO.checkIfUserExist(ID);
    }
}
