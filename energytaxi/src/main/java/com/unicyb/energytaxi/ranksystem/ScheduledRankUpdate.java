package com.unicyb.energytaxi.ranksystem;

import com.unicyb.energytaxi.database.dao.ranksystem.EliteRankDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.RankDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.energytaxi.entities.ranksystem.EliteRank;
import com.unicyb.energytaxi.entities.ranksystem.Rank;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledRankUpdate {

    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;
    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;
    private EliteRankDAOImpl eliteRankDAO;
    private RankDAOImpl rankDAO;
    @Scheduled(cron = "${application.cron.expression}")
    public void scheduleFixedRateTaskAsync() {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
        eliteRankDAO = new EliteRankDAOImpl();
        rankDAO = new RankDAOImpl();
        List<EliteRank> eliteRankList = eliteRankDAO.getAll();
        List<Rank> ranks = rankDAO.getAll();
        for(Rank rank: ranks) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, (int) rank.getSalePeriod());
            Timestamp newSaleDeadLine = new Timestamp(cal.getTime().getTime());
            userRankAchievementInfoDAO.updateByDeadline(currentDate, newSaleDeadLine, rank.getRankId());
        }
        for (EliteRank eliteRank: eliteRankList){
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, (int) eliteRank.getPeriod());
            Timestamp newFreeOrderDeadLine = new Timestamp(cal.getTime().getTime());
            userEliteRankAchievementInfoDAO.updateByDeadline(eliteRank.getFreeOrders(), currentDate, newFreeOrderDeadLine,
                    eliteRank.getEliteRankId());
        }
    }
}
