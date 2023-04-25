package com.unicyb.minitaxi.ranksystem;

import com.unicyb.minitaxi.database.dao.documents.UserDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.EliteRankDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.RankDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.entities.documents.User;
import com.unicyb.minitaxi.entities.ranksystem.EliteRank;
import com.unicyb.minitaxi.entities.ranksystem.Rank;
import com.unicyb.minitaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import com.unicyb.minitaxi.entities.ranksystem.UserRankAchievementInfo;
import com.unicyb.minitaxi.entities.usersinfo.UserStats;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class RankSystem {
    private RankDAOImpl rankDAO;
    private EliteRankDAOImpl eliteRankDAO;
    private UserDAOImpl userDAO;
    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    private UserStats userStats;
    private User user;

    public RankSystem(UserStats userStats, User user){
        this.userStats = userStats;
        this.user = user;
        userDAO = new UserDAOImpl();
        rankDAO = new RankDAOImpl();
        eliteRankDAO = new EliteRankDAOImpl();
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
    }

    public int getNewRank(){
        rankDAO = new RankDAOImpl();
        List<Rank> rankList = rankDAO.getAll();
        int result = userStats.getRankId();
        System.out.println("ranks: " + rankList);
        for (int i = 1; i < rankList.size(); i++){
            if(userStats.getCountOrders() >= rankList.get(i).getMinOrders() &&
                    userStats.getCountComments() >= rankList.get(i).getMinComments()){
                result = rankList.get(i).getRankId();
            }
        }
        return result;
    }

    public void addNewBaseRank(float period){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currenDate);
        cal.add(Calendar.DATE, (int) period);
        Timestamp saleDeadLine = new Timestamp(cal.getTime().getTime());
        UserRankAchievementInfo newUserRankAchievementInfo = new UserRankAchievementInfo(-1, currenDate, user.getUserId(),
                user.getRankId(), 1, saleDeadLine);
        userRankAchievementInfoDAO.add(newUserRankAchievementInfo);
    }

    public void addEliteRank(List<EliteRank> eliteRanksList){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        for(EliteRank eliteRank: eliteRanksList){
            addNewEliteRank(currenDate, eliteRank);
        }
    }

    public void updateBaseRank(float period){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        UserRankAchievementInfo userRankAchievementInfo = userRankAchievementInfoDAO.getOneByUserId(user.getUserId());
        Calendar cal = Calendar.getInstance();
        cal.setTime(userRankAchievementInfo.getDeadlineDateSale());
        cal.add(Calendar.DATE, (int) period);
        Timestamp saleDeadLine = new Timestamp(cal.getTime().getTime());
        userRankAchievementInfo.setDateUri(currenDate);
        userRankAchievementInfo.setRanksId(user.getRankId());
        userRankAchievementInfo.setNumberOfUsesSale(1);
        userRankAchievementInfo.setDeadlineDateSale(saleDeadLine);
        userRankAchievementInfoDAO.update(userRankAchievementInfo);
    }

    private void addNewEliteRank(Timestamp currenDate, EliteRank eliteRank) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currenDate);
        cal.add(Calendar.DATE, (int) eliteRank.getPeriod());
        Timestamp freeOrderDeadLine = new Timestamp(cal.getTime().getTime());
        UserEliteRankAchievementInfo userEliteRankAchievementInfo = new UserEliteRankAchievementInfo(-1,
                currenDate, user.getUserId(), eliteRank.getEliteRankId(), eliteRank.getFreeOrders(),
                freeOrderDeadLine, eliteRank.getCcId());
        System.out.println("userEliteRankAchievementInfo: " + userEliteRankAchievementInfo);
        userEliteRankAchievementInfoDAO.add(userEliteRankAchievementInfo);
    }

    public void updateUserRank(){
        if (checkIfNewRank()){
            Rank rank = rankDAO.getOne(user.getRankId());
            if(userRankAchievementInfoDAO.checkIfUserExist(user.getUserId())){
                if(rank.getIsElite() == 1){
                    List<EliteRank> eliteRankList = eliteRankDAO.getAllByRankId(rank.getRankId());
                    System.out.println("elite ranks: " + eliteRankList);
                    addEliteRank(eliteRankList);
                    updateBaseRank(rank.getSalePeriod());
                }
                else {
                    updateBaseRank(rank.getSalePeriod());
                }
            }
            else {
                addNewBaseRank(rank.getSalePeriod());
            }
        }
    }

    public boolean checkIfNewRank(){
        int newRank = getNewRank();
        System.out.println(newRank);
        if (newRank != user.getRankId()){
            user.setRankId(newRank);
            userDAO.update(user);
            return true;
        }
        else {
           return false;
        }
    }
}
