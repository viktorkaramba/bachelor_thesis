package com.unicyb.minitaxi.controller.ranksystem;

import com.unicyb.minitaxi.database.dao.ranksystem.UserEliteRankAchievementInfoDAOImpl;
import com.unicyb.minitaxi.entities.ranksystem.UserEliteRankAchievementInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class UserEliteRankAchievementInfoController {

    private UserEliteRankAchievementInfoDAOImpl userEliteRankAchievementInfoDAO;

    @GetMapping("/user-elite-rank-achievements-info")
    public ResponseEntity getRanks(){
        try {
            userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
            return ResponseEntity.ok(userEliteRankAchievementInfoDAO.getAll());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user elite rank achievements info");
        }
    }

    @GetMapping("/user-elite-rank-achievements-info/{userId}/{rankId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") int userId, @PathVariable("rankId") int rankId){
        try {
            userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
            return ResponseEntity.ok(userEliteRankAchievementInfoDAO.getByUserIdAndRankId(userId, rankId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error to get user rank achievements info by user id and rank id");
        }
    }

    @GetMapping("/user-elite-rank-achievements-info-by-driver/{userId}/{rankId}/{driverId}")
    public ResponseEntity getRanksByUserIdAndRankId(@PathVariable("userId") int userId,
                                                    @PathVariable("rankId") int rankId,
                                                    @PathVariable("driverId") int driverId){
        try {
            userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
            return ResponseEntity.ok(userEliteRankAchievementInfoDAO.getByUserIdAndRankIdDriverId(userId, rankId, driverId));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().
                    body("Error to get user rank achievements info by user id and rank id and driver id");
        }
    }
    @PostMapping("/user-elite-rank-achievements-info")
    public void save(@RequestBody UserEliteRankAchievementInfo userEliteRankAchievementInfo){
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO.add(userEliteRankAchievementInfo);
    }

    @PutMapping("/user-elite-rank-achievements-info")
    public void update(@RequestBody UserEliteRankAchievementInfo userEliteRankAchievementInfo){
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO.update(userEliteRankAchievementInfo);
    }

    @DeleteMapping("/user-elite-rank-achievements-info/{id}")
    public void delete(@PathVariable("id") int id){
        userEliteRankAchievementInfoDAO = new UserEliteRankAchievementInfoDAOImpl();
        userEliteRankAchievementInfoDAO.delete(id);
    }
}
