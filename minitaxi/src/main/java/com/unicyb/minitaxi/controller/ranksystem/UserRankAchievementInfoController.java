package com.unicyb.minitaxi.controller.ranksystem;

import com.unicyb.minitaxi.database.dao.ranksystem.UserRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.entities.ranksystem.UserRankAchievementInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserRankAchievementInfoController {

    private UserRankAchievementInfoDAOImpl userRankAchievementInfoDAO;

    @GetMapping("/api/v1/bonuses/user-rank-achievements-info")
    public ResponseEntity getRanks(){
        try {
            userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
            return ResponseEntity.ok(userRankAchievementInfoDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info");
        }
    }

    @GetMapping("/api/v1/bonuses/user-rank-achievements-info/{userId}/{rankId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") Integer userId, @PathVariable("rankId") Integer rankId){
        try {
            userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
            return ResponseEntity.ok(userRankAchievementInfoDAO.getOneByUserIdAndRankId(userId, rankId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info by user id and rank id");
        }
    }

    @PostMapping("/api/v1/bonuses/user-rank-achievements-info")
    public void save(@RequestBody UserRankAchievementInfo userRankAchievementInfo){
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userRankAchievementInfoDAO.add(userRankAchievementInfo);
    }

    @PutMapping("/api/v1/bonuses/user-rank-achievements-info")
    public void update(@RequestBody UserRankAchievementInfo userRankAchievementInfo){
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userRankAchievementInfoDAO.update(userRankAchievementInfo);
    }

    @DeleteMapping("/api/v1/bonuses/user-rank-achievements-info/{id}")
    public void delete(@PathVariable("id") int id){
        userRankAchievementInfoDAO = new UserRankAchievementInfoDAOImpl();
        userRankAchievementInfoDAO.delete(id);
    }

}
