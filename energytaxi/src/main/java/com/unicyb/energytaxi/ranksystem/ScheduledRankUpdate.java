package com.unicyb.energytaxi.ranksystem;

import com.unicyb.energytaxi.entities.ranksystem.EliteRank;
import com.unicyb.energytaxi.entities.ranksystem.Rank;
import com.unicyb.energytaxi.services.ranksystem.EliteRankService;
import com.unicyb.energytaxi.services.ranksystem.RankService;
import com.unicyb.energytaxi.services.ranksystem.UserEliteRankAchievementService;
import com.unicyb.energytaxi.services.ranksystem.UserRankAchievementService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduledRankUpdate {

    private UserRankAchievementService userRankAchievementService;
    private UserEliteRankAchievementService userEliteRankAchievementService;
    private EliteRankService eliteRankService;
    private RankService rankService;
    @Scheduled(cron = "${application.cron.expression}")
    public void scheduleFixedRateTaskAsync() {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        List<EliteRank> eliteRankList = eliteRankService.getAll();
        List<Rank> ranks = rankService.getAll();
        for(Rank rank: ranks) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, (int) rank.getSalePeriod());
            Timestamp newSaleDeadLine = new Timestamp(cal.getTime().getTime());
            userRankAchievementService.updateByDeadline(currentDate, newSaleDeadLine, rank.getRankId());
        }
        for (EliteRank eliteRank: eliteRankList){
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.add(Calendar.DATE, (int) eliteRank.getPeriod());
            Timestamp newFreeOrderDeadLine = new Timestamp(cal.getTime().getTime());
            userEliteRankAchievementService.updateByDeadline(eliteRank.getFreeOrders(), currentDate, newFreeOrderDeadLine,
                    eliteRank.getEliteRankId());
        }
    }
}
