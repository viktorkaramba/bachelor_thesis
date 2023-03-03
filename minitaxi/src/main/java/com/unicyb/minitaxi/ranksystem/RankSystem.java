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
        for (int i = 0; i < rankList.size(); i++){
            if(userStats.getCountOrders() > rankList.get(i).getMinOrders() &&
                    userStats.getCountComments() > rankList.get(i).getMinComments() && i != rankList.size()-1){
                result = rankList.get(i + 1).getRankId();
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

    public void updateEliteRank(List<EliteRank> eliteRanksList){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        List<UserEliteRankAchievementInfo> userEliteRankAchievementInfoList =
                userEliteRankAchievementInfoDAO.getOneByUserId(user.getUserId());
        for(EliteRank eliteRank: eliteRanksList){
            boolean isExist = false;
            for(UserEliteRankAchievementInfo userEliteRankAchievementInfo: userEliteRankAchievementInfoList){
                if(userEliteRankAchievementInfo.getCarClassId() == eliteRank.getCcId()){
                    isExist = true;
                    userEliteRankAchievementInfo.setEliteRanksId(eliteRank.getEliteRankId());
                    userEliteRankAchievementInfo.setDateUerai(currenDate);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(userEliteRankAchievementInfo.getDeadlineDateFreeOrder());
                    cal.add(Calendar.DATE, (int) eliteRank.getPeriod());
                    Timestamp freeOrderDeadLine = new Timestamp(cal.getTime().getTime());
                    userEliteRankAchievementInfo.setDeadlineDateFreeOrder(freeOrderDeadLine);
                    userEliteRankAchievementInfo.setNumberOfUsesFreeOrder(
                            userEliteRankAchievementInfo.getNumberOfUsesFreeOrder() + eliteRank.getFreeOrders());
                    userEliteRankAchievementInfoDAO.update(userEliteRankAchievementInfo);
                }
            }
            if (!isExist){
                addNewEliteRank(currenDate, eliteRank);
            }
        }
    }

    private void addNewEliteRank(Timestamp currenDate, EliteRank eliteRank) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currenDate);
        cal.add(Calendar.DATE, (int) eliteRank.getPeriod());
        Timestamp freeOrderDeadLine = new Timestamp(cal.getTime().getTime());
        UserEliteRankAchievementInfo userEliteRankAchievementInfo = new UserEliteRankAchievementInfo(-1,
                currenDate, user.getUserId(), eliteRank.getEliteRankId(), eliteRank.getFreeOrders(),
                freeOrderDeadLine, eliteRank.getCcId());
        userEliteRankAchievementInfoDAO.add(userEliteRankAchievementInfo);
    }

    public void updateUserRank(){
        Rank rank = rankDAO.getOne(user.getRankId());
        if (userRankAchievementInfoDAO.checkIfUserExist(user.getUserId())){
            if(checkIfNewRank(userStats.getRankId(), rank.getRankId())){
                if(rank.getIsElite() == 1){
                    List<EliteRank> eliteRankList = eliteRankDAO.getAllByRankId(rank.getRankId());
                    if(userEliteRankAchievementInfoDAO.checkIfUserExist(user.getUserId())){
                        updateBaseRank(rank.getSalePeriod());
                        updateEliteRank(eliteRankList);
                    }
                    else{
                        updateBaseRank(rank.getSalePeriod());
                        addEliteRank(eliteRankList);
                    }
                }
                else {
                    updateBaseRank(rank.getSalePeriod());
                }
            }
        }
        else {
            addNewBaseRank(rank.getSalePeriod());
        }
    }

    public boolean checkIfNewRank(int rank1, int rank2){
        return rank1 != rank2;
    }
}
