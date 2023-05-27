package com.unicyb.energytaxi.ranksystem;

import com.unicyb.energytaxi.entities.documents.User;
import com.unicyb.energytaxi.entities.ranksystem.EliteRank;
import com.unicyb.energytaxi.entities.ranksystem.Rank;
import com.unicyb.energytaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import com.unicyb.energytaxi.entities.ranksystem.UserRankAchievementInfo;
import com.unicyb.energytaxi.entities.usersinfo.UserStats;
import com.unicyb.energytaxi.services.documents.UserService;
import com.unicyb.energytaxi.services.ranksystem.EliteRankService;
import com.unicyb.energytaxi.services.ranksystem.RankService;
import com.unicyb.energytaxi.services.ranksystem.UserEliteRankAchievementService;
import com.unicyb.energytaxi.services.ranksystem.UserRankAchievementService;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class RankSystem {

    private UserStats userStats;
    private User user;
    private RankService rankService;
    private EliteRankService eliteRankService;
    private UserService userService;
    private UserEliteRankAchievementService userEliteRankAchievementService;
    private UserRankAchievementService userRankAchievementService;

    public RankSystem(UserStats userStats, User user){
        this.userStats = userStats;
        this.user = user;
    }

    public int getNewRank(){
        List<Rank> rankList = rankService.getAll();
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
        userRankAchievementService.add(newUserRankAchievementInfo);
    }

    public void addEliteRank(List<EliteRank> eliteRanksList){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        for(EliteRank eliteRank: eliteRanksList){
            addNewEliteRank(currenDate, eliteRank);
        }
    }

    public void updateBaseRank(float period){
        Timestamp currenDate = new Timestamp(new Date().getTime());
        UserRankAchievementInfo userRankAchievementInfo = userRankAchievementService.getOneByUserId(user.getUserId());
        Calendar cal = Calendar.getInstance();
        cal.setTime(userRankAchievementInfo.getDeadlineDateSale());
        cal.add(Calendar.DATE, (int) period);
        Timestamp saleDeadLine = new Timestamp(cal.getTime().getTime());
        userRankAchievementInfo.setDateUri(currenDate);
        userRankAchievementInfo.setRanksId(user.getRankId());
        userRankAchievementInfo.setNumberOfUsesSale(1);
        userRankAchievementInfo.setDeadlineDateSale(saleDeadLine);
        userRankAchievementService.update(userRankAchievementInfo);
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
        userEliteRankAchievementService.add(userEliteRankAchievementInfo);
    }

    public void updateUserRank(){
        if (checkIfNewRank()){
            Rank rank = rankService.getOne(user.getRankId());
            if(userRankAchievementService.checkIfUserExist(user.getUserId())){
                if(rank.getIsElite() == 1){
                    List<EliteRank> eliteRankList = eliteRankService.getAllByRankId(rank.getRankId());
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
            userService.update(user);
            return true;
        }
        else {
           return false;
        }
    }
}
